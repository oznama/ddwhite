package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.SaleDetailDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.dto.SalePaymentDto;
import mx.com.ddwhite.ws.dto.SessionDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.model.Sale;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.model.SalePayment;
import mx.com.ddwhite.ws.repository.ClientRepository;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.repository.SalePaymentRepository;
import mx.com.ddwhite.ws.repository.SaleRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class SaleService {
	
	private final String MODULE = Sale.class.getSimpleName();
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	
	@Autowired
	private SalePaymentRepository salePaymentRepository;
	
	@Autowired
	private TicketPrintService ticketPrintService;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private WithdrawalService withdrawalService;
	
	public SaleDto save(SaleDto saleDto) throws Throwable {
		Sale sale = new Sale();
		BeanUtils.copyProperties(saleDto, sale);
		try {
			sale = saleRepository.saveAndFlush(sale);
			persistDetail(saleDto.getDetail(), sale.getId());
			persistPayment(saleDto.getPayments(), sale.getId());
			saleDto.setId(sale.getId());
			saleDto.setDateCreated(GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME_SHORT));
			// Manda a imprimir el ticket
			ticketPrintService.ticket(saleDto);
			return saleDto;
		} catch (DataAccessException e) {
			throw e.getRootCause();
		}
	}
	
	public void updateInvoice(Long id, String invoice, Long clientId) {
		saleRepository.updateInvoice(invoice, clientId, id);
		saleRepository.flush();
	}

	private void persistDetail(List<SaleDetailDto> detailsDto, Long saleId) throws Throwable {
		final List<SaleDetail> detail = new ArrayList<>();
		detailsDto.forEach( d -> {
			SaleDetail saleDetail = new SaleDetail();
			BeanUtils.copyProperties(d, saleDetail);
			saleDetail.setSaleId(saleId);
			detail.add(saleDetail);
		});
		try {
			saleDetailRepository.saveAll(detail);
			saleDetailRepository.flush();
		} catch (DataAccessException e) {
			throw e.getRootCause();
		}
	}
	
	private void persistPayment(List<SalePaymentDto> paymentsDto, Long saleId) throws Throwable {
		final List<SalePayment> payment = new ArrayList<>();
		paymentsDto.forEach( d -> {
			SalePayment salePayment = new SalePayment();
			BeanUtils.copyProperties(d, salePayment);
			salePayment.setSaleId(saleId);
			payment.add(salePayment);
		});
		try {
			salePaymentRepository.saveAll(payment);
			salePaymentRepository.flush();
		} catch (DataAccessException e) {
			throw e.getRootCause();
		}
	}

	public SaleDto findById(Long id) {
		return bindSale(saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id)));
	}
	
	public List<SaleDto> findByRange(String startDate, String endDate) {
		List<Sale> sales = saleRepository.findByRange(startDate, endDate);
		return buildSalesDto(sales);
	}

	public Page<SaleDto> findByRange(Date startDate, Date endDate, Pageable pageable) {
		String strStartDate = GenericUtils.dateToString(startDate, GeneralConstants.FORMAT_DATE_TIME);
		endDate = GenericUtils.plusDay(endDate, 1);
		String strEndDate = GenericUtils.dateToString(endDate, GeneralConstants.FORMAT_DATE_TIME);
		List<Sale> sales = saleRepository.findByRange(strStartDate, strEndDate, pageable);
		return new PageImpl<>(buildSalesDto(sales), pageable, saleRepository.findByRange(strStartDate, strEndDate).size());
	}
	
	public List<SaleDto> findByUserAndRange(Long userId, String startDate, String endDate) {
		List<Sale> sales = saleRepository.findByUserAndRange(userId, startDate, endDate);
		return buildSalesDto(sales);
	}
	
	public BigDecimal getExcedent(Long userId) {
		BigDecimal maxAmount = BigDecimal.valueOf(
				Double.valueOf(catalogService.findByName(GeneralConstants.CATALOG_MAX_AMOUNT_CASH_WITHDRAWAL).getDescription()))
				.setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal currentAmount = getChasInRegister(userId);
		return currentAmount.subtract(maxAmount).doubleValue() > 0 ? currentAmount : BigDecimal.ZERO;
	}
	
	public BigDecimal getChasInRegister(Long userId) {
		Long paymentCashId = catalogService.findByName(GeneralConstants.CATALOG_PAYMENT_METHOD_CASH).getId();
		BigDecimal currentAmount = new BigDecimal(0);
		SessionDto currentSession = sessionService.findCurrentSession(userId);
		String lastWithdrawal = withdrawalService.getLastDateWithdrawalBySession(currentSession.getId(), currentSession.getInDate());
		List<SaleDto> currentSales = findByRange(lastWithdrawal, GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
		for(SaleDto sale : currentSales) {
			List<SalePayment> salePayments = salePaymentRepository.findBySaleAndPayment(sale.getId(), paymentCashId);
			BigDecimal totalOfSale = salePayments.stream().map(s -> s.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
			currentAmount = currentAmount.add(totalOfSale.subtract(sale.getChange()));
		};
		return currentAmount;
	}
	
	public Long getLastSaleId() {
		return saleRepository.findTopByOrderByIdDesc().getId();
	}

	private List<SaleDto> buildSalesDto(final List<Sale> sales) {
		final List<SaleDto> salesDto = new ArrayList<>();
		sales.forEach( sale -> salesDto.add(bindSale(sale)));
		return salesDto;
	}
	
	private SaleDto bindSale(Sale sale) {
		SaleDto saleDto = new SaleDto();
		BeanUtils.copyProperties(sale, saleDto);
		bindDetail(saleDto);
		bindPayment(saleDto);
		if(sale.getClientId() != null) {
			Client client = clientRepository.getOne(sale.getClientId());
			if( client != null) {
				saleDto.setClientRfc(client.getRfc());
				saleDto.setClientName(client.getName() + " " + client.getMidleName() + " " + client.getLastName());	
			}
		}
		return saleDto;
	}

	private void bindPayment(final SaleDto saleDto) {
		List<SalePayment> salePayments = salePaymentRepository.findBySale(saleDto.getId());
		salePayments.forEach( salePayment -> {
			SalePaymentDto salePaymentDto = new SalePaymentDto();
			BeanUtils.copyProperties(salePayment, salePaymentDto);
			saleDto.getPayments().add(salePaymentDto);
		});
	}

	private void bindDetail(final SaleDto saleDto) {
		List<SaleDetail> saleDetails = saleDetailRepository.findBySale(saleDto.getId());
		saleDetails.forEach( saleDetail -> {
			SaleDetailDto saleDetailDto = new SaleDetailDto();
			BeanUtils.copyProperties(saleDetail, saleDetailDto);
			saleDto.getDetail().add(saleDetailDto);
		});
	}

}
