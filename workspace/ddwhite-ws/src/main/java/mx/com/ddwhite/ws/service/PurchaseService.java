package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.PurchaseDto;
import mx.com.ddwhite.ws.dto.PurchaseListDto;
import mx.com.ddwhite.ws.dto.PurchaseReasignDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.repository.ProviderRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class PurchaseService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(PurchaseService.class);

	private final String MODULE = Purchase.class.getSimpleName();

	@Autowired
	private PurchaseRepository repository;

	@Autowired
	private SaleDetailRepository saleDetailRepository;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProviderRepository providerRepository;

	@Autowired
	private PurchaseReasingService purchaseReasignService;

	public Page<PurchaseDto> findAll(Pageable pageable) {
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		Page<Purchase> purchases = repository.findAll(pageable);
		purchases.forEach(purchase -> purchasesDto.add(setPurchaseDto(purchase)));
		return new PageImpl<>(purchasesDto, pageable, repository.count());
	}

	public PurchaseDto findById(Long id) {
		return setPurchaseDto(
				repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id)));
	}

	public PurchaseDto create(PurchaseDto purchaseDto) throws DataAccessException {
		Purchase purchase = new Purchase();
		BeanUtils.copyProperties(purchaseDto, purchase);
		repository.saveAndFlush(purchase);
		purchaseDto.setId(purchase.getId());
		return purchaseDto;
	}

	public void update(PurchaseDto purchaseDto) {
		repository.update(purchaseDto.getId(), purchaseDto.getQuantity(), purchaseDto.getCost(), purchaseDto.getUnity(),
				purchaseDto.getNumPiece(), purchaseDto.getUserId());
	}

	public void delete(Long id) {
		Purchase purchase = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(purchase);
		repository.flush();
	}

	public void createBuilk(List<PurchaseDto> purchasesDto) throws DataAccessException {
		final List<Purchase> purchases = new ArrayList<>();
		purchasesDto.forEach(purchaseDto -> {
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
		purchases.forEach(purchase -> {
			PurchaseDto purchaseDto = setPurchaseDto(purchase);
			Double saled = sumSaleQuantity(saleDetailRepository
					.findByProductAndUnityWithoutPieces(purchaseDto.getProduct().getId(), purchaseDto.getUnity()));
			Double reasigned = sumQuantityReasigned(purchaseReasignService.findByDestiny(purchase.getId()));
			purchaseDto.setQuantity(purchaseDto.getQuantity() + reasigned - saled);
			purchasesDto.add(purchaseDto);
		});
		return purchasesDto;
	}

	public List<PurchaseDto> findByReasignProduct() {
		final List<PurchaseDto> purchasesDto = new ArrayList<>();
		List<Purchase> purchasesToReasing = repository.findByReasignProduct();
		purchasesToReasing.forEach(purchase -> {
//			List<SaleDetail> productsOfPurchaseSaled = saleDetailRepository.findByProductAndUnity(purchase.getProduct().getId(), purchase.getUnity());
//			if( productsOfPurchaseSaled.isEmpty() ) {
			PurchaseDto purchaseDto = setPurchaseDto(purchase);
			PurchaseDto purchaseAlreadyInList = findInList(purchaseDto, purchasesDto);
			if (purchaseAlreadyInList != null) {
				purchaseDto.setQuantity(purchaseDto.getQuantity() + purchaseAlreadyInList.getQuantity() + getQuantitySales(purchaseDto, purchase.getId()));
				purchasesDto.remove(purchaseAlreadyInList);
			}
			purchaseDto.setQuantity( purchaseDto.getQuantity() - getQuantityReasigned(purchaseDto, purchase.getId()));
			purchasesDto.add(purchaseDto);
//			}
		});
		List<PurchaseDto> purchasesDtoDepured = purchasesDto.stream().filter(p -> p.getQuantity() > 0)
				.collect(Collectors.toList());
		return purchasesDtoDepured;
	}

	public void saveReasign(PurchaseReasignDto purchaseReasignDto) {
		purchaseReasignService.create(purchaseReasignDto);
	}

	public BigDecimal getCostByProductAndDate(Long productId, Long unity, String date) {
		try {
			return repository.findCosts(productId, unity, date).get(0);
		} catch (Exception e) {
			LOGGER.error("Error getting purchase by product and date, params [productId: {}, unity: {}, date: {}], error: {}", productId, unity, date, e.getMessage());
			return BigDecimal.ZERO;
		}
	}

	public List<PurchaseListDto> getList(Date startDate, Date endDate) {
		final List<PurchaseListDto> purchasesListDto = new ArrayList<>();
		String strStartDate = GenericUtils.dateToString(startDate, GeneralConstants.FORMAT_DATE_TIME);
		endDate = GenericUtils.plusDay(endDate, 1);
		String strEndDate = GenericUtils.dateToString(endDate, GeneralConstants.FORMAT_DATE_TIME);
		List<Purchase> purchases = repository.findByDates(strStartDate, strEndDate);
		purchases.forEach(purchase -> purchasesListDto.add(getPurchaseListDto(purchase)));
		return purchasesListDto;
	}

	public PurchaseListDto getOne(Long purchaseId) {
		Purchase purchase = repository.getOne(purchaseId);
		return getPurchaseListDto(purchase);
	}

	private PurchaseListDto getPurchaseListDto(Purchase purchase) {
		PurchaseListDto purchaseListDto = new PurchaseListDto();
		BeanUtils.copyProperties(purchase, purchaseListDto);
		purchaseListDto.setDateCreated(purchase.getDateCreated());
		purchaseListDto.setProduct(purchase.getProduct().getNameLarge());
		purchaseListDto.setProvider(providerRepository.getOne(purchase.getProviderId()).getBussinesName());
		purchaseListDto.setUnity(purchase.getUnity());
		purchaseListDto.setUnityDesc(catalogService.findById(purchase.getUnity()).getName());
		purchaseListDto.setUser(userService.findById(purchase.getUserId()).getFullName());
		List<SaleDetail> sales = purchase.getNumPiece() != null
				? saleDetailRepository.findByProductAndUnityWithPieces(purchase.getProduct().getId(),purchase.getUnity(), purchase.getNumPiece())
				: saleDetailRepository.findByProductAndUnityWithoutPieces(purchase.getProduct().getId(),purchase.getUnity());
		purchaseListDto.setUsedInSale(!sales.isEmpty());
		return purchaseListDto;
	}

	private PurchaseDto setPurchaseDto(Purchase purchase) {
		PurchaseDto purchaseDto = new PurchaseDto();
		BeanUtils.copyProperties(purchase, purchaseDto);
		BeanUtils.copyProperties(purchase.getProduct(), purchaseDto.getProduct());
		purchaseDto.setUnityDesc(catalogService.findById(purchaseDto.getUnity()).getName());
		return purchaseDto;
	}

	private PurchaseDto findInList(PurchaseDto purchaseDto, List<PurchaseDto> purchasesDto) {
		return purchasesDto.stream()
				.filter(p -> p.getProduct().getId().equals(purchaseDto.getProduct().getId())
						&& p.getUnity().equals(purchaseDto.getUnity())
						&& p.getNumPiece().equals(purchaseDto.getNumPiece()))
				.findAny().orElse(null);
	}
	
	private Double getQuantitySales(PurchaseDto purchaseDto, Long purchaseId) {
		Double saled = sumSaleQuantity(purchaseDto.getNumPiece() != null
				? saleDetailRepository.findByProductAndUnityWithPieces(purchaseDto.getProduct().getId(),
						purchaseDto.getUnity(), purchaseDto.getNumPiece())
				: saleDetailRepository.findByProductAndUnityWithoutPieces(purchaseDto.getProduct().getId(),
						purchaseDto.getUnity()));
		return saled;
	}
	
	private Double getQuantityReasigned(PurchaseDto purchaseDto, Long purchaseId) {
		return sumPurchasesReasign(purchaseReasignService.findByOrigin(purchaseId)) + getQuantitySales(purchaseDto, purchaseId);
	}

	private Double sumSaleQuantity(List<SaleDetail> salesDetail) {
		return salesDetail.stream().mapToDouble(sd -> sd.getQuantity().doubleValue()).sum();
	}

	private Double sumPurchasesReasign(List<PurchaseReasignDto> purchasesReasigned) {
		return purchasesReasigned.stream().mapToDouble(sd -> sd.getQuantity().doubleValue()).sum();
	}
	
	private Double sumQuantityReasigned(List<PurchaseReasignDto> purchasesReasigned) {
		return purchasesReasigned.stream().mapToDouble( p -> (p.getQuantity().doubleValue() * findById(p.getPurchasesOrigin()).getNumPiece())).sum();
	}

}
