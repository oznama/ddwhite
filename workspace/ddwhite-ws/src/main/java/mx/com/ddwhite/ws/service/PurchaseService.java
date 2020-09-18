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
import mx.com.ddwhite.ws.dto.PurchaseDto;
import mx.com.ddwhite.ws.dto.PurchaseReasignDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.repository.PurchaseRepository;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class PurchaseService {
	
	private final String MODULE = Purchase.class.getSimpleName();

	@Autowired
	private PurchaseRepository repository;
	
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	
	@Autowired
	private CatalogService catalogService;
	
	public Page<PurchaseDto> findAll(Pageable pageable) {
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		Page<Purchase> purchases = repository.findAll(pageable);
		purchases.forEach( purchase -> purchasesDto.add(setPurchaseDto(purchase)));
		return new PageImpl<>(purchasesDto, pageable, repository.count());
	}

	public PurchaseDto findById(Long id) {
		return setPurchaseDto(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id)));
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
			t.setDateCreated(GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
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
			purchase.getProduct().setId(purchaseDto.getProductId());
			purchases.add(purchase);
		});
		repository.saveAll(purchases);
		repository.flush();
	}
	
	public List<PurchaseDto> findByProductExceptCurrent(Long id, Long productId) {
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		List<Purchase> purchases = repository.findByProductExceptCurrent(id, productId);
		purchases.forEach( purchase -> purchasesDto.add(setPurchaseDto(purchase)));
		return purchasesDto;
	}
	
	public List<PurchaseDto> findByReasignProduct() {
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		List<Purchase> purchasesToReasing = repository.findByReasignProduct();
		purchasesToReasing.forEach( purchase -> {
//			List<SaleDetail> productsOfPurchaseSaled = saleDetailRepository.findByProductAndUnity(purchase.getProduct().getId(), purchase.getUnity());
//			if( productsOfPurchaseSaled.isEmpty() ) {
			PurchaseDto purchaseDto = setPurchaseDto(purchase); 
			PurchaseDto purchaseAlreadyInList = findInList(purchaseDto, purchasesDto);
			if( purchaseAlreadyInList != null )  {
				purchaseDto.setQuantity( purchaseDto.getQuantity() + purchaseAlreadyInList.getQuantity() );
				purchasesDto.remove(purchaseAlreadyInList);
			} else {
				int saled = sumSaleQuantity(
						purchaseDto.getNumPiece() != null 
						? saleDetailRepository.findByProductAndUnityWithPieces(purchaseDto.getProduct().getId(), purchaseDto.getUnity(), purchaseDto.getNumPiece())
						: saleDetailRepository.findByProductAndUnityWithoutPieces(purchaseDto.getProduct().getId(), purchaseDto.getUnity())
						);
				System.out.printf("Numero de ventas realizadas %d para producto %d, con unidad %d y num piezas: %d\n", saled, purchaseDto.getProduct().getId(), purchaseDto.getUnity(), purchaseDto.getNumPiece());
				purchaseDto.setQuantity(purchaseDto.getQuantity() - saled);
			}
			purchasesDto.add(purchaseDto);
//			}
		});
		return purchasesDto;
	}
	
	public void updateReasign(PurchaseReasignDto purchaseReasignDto) {
		PurchaseDto purchaseDto = findById(purchaseReasignDto.getPurchasesOrigin());
		if(purchaseDto.getQuantity() == purchaseReasignDto.getQuantity())
			repository.deleteById(purchaseReasignDto.getPurchasesOrigin());
		else
			repository.upgradeReasign(purchaseReasignDto.getQuantity(), purchaseReasignDto.getPurchasesOrigin());
		repository.updateReasign((purchaseReasignDto.getQuantity() * purchaseDto.getNumPiece()), purchaseReasignDto.getPurchaseDestity());
	}
	
	private PurchaseDto setPurchaseDto(Purchase purchase) {
		PurchaseDto purchaseDto = new PurchaseDto();
		BeanUtils.copyProperties(purchase, purchaseDto);
		BeanUtils.copyProperties(purchase.getProduct(), purchaseDto.getProduct());
		purchaseDto.setUnityDesc(catalogService.findById(purchaseDto.getUnity()).getName());
		return purchaseDto;
	}
	
	private PurchaseDto findInList(PurchaseDto purchaseDto, List<PurchaseDto> purchasesDto) {
		return purchasesDto.stream().filter( p -> p.getProduct().getId().equals(purchaseDto.getProduct().getId())
				&& p.getUnity().equals(purchaseDto.getUnity()) 
				&& p.getNumPiece().equals(purchaseDto.getNumPiece()))
			.findAny().orElse(null);
	}
	
	protected int sumSaleQuantity(List<SaleDetail> salesDetail) {
		return salesDetail.stream().map( sd -> sd.getQuantity()).reduce(0, Integer::sum);
	}

}
