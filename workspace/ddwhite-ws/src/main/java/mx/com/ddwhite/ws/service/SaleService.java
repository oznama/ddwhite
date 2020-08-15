package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.dto.SaleDetailDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Sale;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.repository.SaleRepository;

@Service
public class SaleService extends GenericService<Sale> {
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	
	public SaleDto save(SaleDto saleDto) {
		Sale sale = new Sale();
		BeanUtils.copyProperties(saleDto, sale);
		sale = saleRepository.saveAndFlush(sale);
		persistDetail(saleDto.getDetail(), sale.getId());
		return saleDto;
	}

	private void persistDetail(List<SaleDetailDto> detailsDto, Long saleId) {
		final List<SaleDetail> detail = new ArrayList<>();
		detailsDto.forEach( d -> {
			SaleDetail saleDetail = new SaleDetail();
			BeanUtils.copyProperties(d, saleDetail);
			detail.add(saleDetail);
		});
		saleDetailRepository.saveAll(detail);
		saleDetailRepository.flush();
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
