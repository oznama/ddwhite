package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.constants.Utils;
import mx.com.ddwhite.ws.dto.PurchaseDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.repository.ProductRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;

@Service
public class PurchaseService extends GenericService<PurchaseDto> {
	
	private final String MODULE = Purchase.class.getSimpleName();

	@Autowired
	private PurchaseRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Page<PurchaseDto> findAll(Pageable pageable) {
		Page<PurchaseDto> result = null;
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		List<Purchase> purchases = repository.findAll();
		purchases.forEach( purchase -> {
			PurchaseDto purchaseDto = new PurchaseDto();
			BeanUtils.copyProperties(purchase, purchaseDto);
			purchasesDto.add(purchaseDto);
		});
		result = new PageImpl<>(purchasesDto, pageable, purchasesDto.size());
		return result;
	}

	public PurchaseDto findById(Long id) {
		Purchase purchase = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		PurchaseDto purchaseDto = new PurchaseDto();
		BeanUtils.copyProperties(purchase, purchaseDto);
		return purchaseDto;
	}

	public PurchaseDto create(PurchaseDto purchaseDto) throws DataAccessException {
		Purchase purchase = new Purchase();
		BeanUtils.copyProperties(purchaseDto, purchase);
		repository.saveAndFlush(purchase);
		purchaseDto.setId(purchase.getId());
		return purchaseDto;
	}

	public void update(PurchaseDto purchaseDto) {
		Purchase purchase = new Purchase();
		BeanUtils.copyProperties(purchaseDto, purchase);
		repository.findById(purchaseDto.getId()).map(t -> {
			BeanUtils.copyProperties(purchaseDto, t);
			t.setDateCreated(Utils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
			return repository.saveAndFlush(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", purchaseDto.getId()));
	}

	public void delete(Long id) {
		Purchase purchase = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(purchase);
		repository.flush();
	}
	
	public void createBuilk(List<PurchaseDto> purchasesDto) throws DataAccessException {
		final List<Purchase> purchases = new ArrayList<>();
		purchasesDto.forEach( purchaseDto -> {
			Purchase purchase = new Purchase();
			BeanUtils.copyProperties(purchaseDto, purchase);
			purchases.add(purchase);
		});
		repository.saveAll(purchases);
		repository.flush();
		updateCostProduct(purchasesDto);
	}
	
	private void updateCostProduct(List<PurchaseDto> purchasesDto) {
		for(PurchaseDto e : purchasesDto) {
			if(e.getProduct() != null && e.getProduct().getCost() != null ) { // Si existe producto y trae costo, es por que se debe actualizar
				Product pFinded = productRepository.findById(e.getProduct().getId()).orElse(null); // Por si el producto no existiera, se asigna nulo
				if(pFinded != null) { // Cuando el producto si existe
					pFinded.setCost(e.getProduct().getCost()); // Cambiando el precio
					pFinded.setDateCreated(Utils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
					pFinded.setUserId(e.getUserId());
					productRepository.saveAndFlush(pFinded);
				}
			}
		}
	}
	
	

}
