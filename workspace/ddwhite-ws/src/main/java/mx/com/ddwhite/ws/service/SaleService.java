package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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

@Service
public class SaleService extends GenericService<Sale> {
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	
	@Autowired
	private SalePaymentRepository salePaymentRepository;
	
	public SaleDto save(SaleDto saleDto) throws Throwable {
		Sale sale = new Sale();
		BeanUtils.copyProperties(saleDto, sale);
		try {
			sale = saleRepository.saveAndFlush(sale);
			persistDetail(saleDto.getDetail(), sale.getId());
			persistPayment(saleDto.getPayments(), sale.getId());
			saleDto.setId(sale.getId());
			return saleDto;
		} catch (DataAccessException e) {
			throw e.getRootCause();
		}
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

	public List<SaleDto> findByRange(String start, String end) {
		final List<Sale> sales = saleRepository.findByRange(start, end);
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
		List<SaleDetail> saleDetails = saleDetailRepository.findBySale(saleDto.getId());
		saleDetails.forEach( saleDetail -> {
			SaleDetailDto saleDetailDto = new SaleDetailDto();
			BeanUtils.copyProperties(saleDetail, saleDetailDto);
			saleDto.getDetail().add(saleDetailDto);
		});
	}

}
