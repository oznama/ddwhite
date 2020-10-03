package mx.com.ddwhite.ws.service;

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
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Sale;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.model.SalePayment;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.repository.SalePaymentRepository;
import mx.com.ddwhite.ws.repository.SaleRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class SaleService {
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	
	@Autowired
	private SalePaymentRepository salePaymentRepository;
	
	@Autowired
	private TicketPrintService ticketPrintService;
	
	public SaleDto save(SaleDto saleDto) throws Throwable {
		Sale sale = new Sale();
		BeanUtils.copyProperties(saleDto, sale);
		try {
			sale = saleRepository.saveAndFlush(sale);
			persistDetail(saleDto.getDetail(), sale.getId());
			persistPayment(saleDto.getPayments(), sale.getId());
			saleDto.setId(sale.getId());
			saleDto.setDateCreated(GenericUtils.dateToString(sale.getDateCreated(), GeneralConstants.FORMAT_DATE_TIME_SHORT));
			// Manda a imprimir el ticket
			ticketPrintService.ticket(saleDto);
			return saleDto;
		} catch (DataAccessException e) {
			throw e.getRootCause();
		}
	}
	
	public void updateInvoice(Long id, String invoice) {
		saleRepository.updateInvoice(invoice, id);
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

	public SaleDto findById(Long id) throws ResourceNotFoundException {
		Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Sale.class.getSimpleName(), "id", id));
		SaleDto saleDto = new SaleDto();
		bindSale(sale, saleDto);
		return saleDto;
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
	
	private List<SaleDto> buildSalesDto(final List<Sale> sales) {
		final List<SaleDto> salesDto = new ArrayList<>();
		sales.forEach( sale -> {
			final SaleDto saleDto = new SaleDto();
			bindSale(sale, saleDto);
			salesDto.add(saleDto);
		} );
		return salesDto;
	}
	
	private void bindSale(Sale sale, final SaleDto saleDto) {
		BeanUtils.copyProperties(sale, saleDto);
		bindDetail(saleDto);
		bindPayment(saleDto);
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
