package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.dto.InventoryDto;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.repository.ProductRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;
import mx.com.ddwhite.ws.service.utils.InventoryUtils;

@Service
public class InventoryService extends InventoryUtils {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private SaleDetailRepository saleDetailRepository;
	@Autowired
	private CatalogService catalogService;

	public Page<ProductInventory> findInventory(Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable);
		return new PageImpl<>(setPurchasesToProducts(products.getContent()), pageable, productRepository.count());
	}

	public Page<InventoryDto> getInventory(Pageable pageable) {
		final List<InventoryDto> inventory = new ArrayList<>();
		List<Product> products = productRepository.findAll();
		products.forEach(product -> {
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if (!purchase.isEmpty()) {
				inventory.add(getPurchase(product, purchase, null));
			}
		});
		return new PageImpl<>(inventory, pageable, inventory.size());
	}

	public InventoryDto getInventory(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", productId));
		List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
		if (!purchase.isEmpty()) {
			return getPurchase(product, purchase, null);
		}
		return null;
	}
	
	public List<ProductInventory> findWarehouse(Sort sort){
		List<ProductInventory> list = findInventory();
		list.forEach(product -> {
			int quantity = product.getInventory().getQuantity();
			int saled = sumSaleQuantity(
					product.getInventory().getNumPiece() != null 
					? saleDetailRepository.findByProductAndUnityWithPieces(product.getId(), product.getInventory().getUnity(), product.getInventory().getNumPiece())
					: saleDetailRepository.findByProductAndUnityWithoutPieces(product.getId(), product.getInventory().getUnity())
					);
			product.getInventory().setQuantity(quantity - saled);
		});
		sorting(list, sort);
		return list;
	}
	
	public Page<ProductInventory> findForSale(Pageable pageable) {
		List<ProductInventory> list = findWarehouse(pageable.getSort());
		List<ProductInventory> listForSale = list.stream().filter(p -> p.getInventory().getQuantity() > 0)
				.collect(Collectors.toList());
		return pagging(listForSale, pageable);
	}
	
	private List<ProductInventory> findInventory() {
		List<Product> products = productRepository.findAll();
		return setPurchasesToProducts(products);
	}
	
	private List<ProductInventory> setPurchasesToProducts(List<Product> products) {
		List<ProductInventory> list = new ArrayList<>();
		products.forEach(product -> {
			List<Long> unityTypes = purchaseRepository.findTypesProduct(product.getId());
			if( !unityTypes.isEmpty() ) {
				unityTypes.forEach( unity -> {
					Map<Integer, List<Purchase>> groupByPieces = groupPurchasesByPieces(product.getId(), unity);
					if( groupByPieces != null && !groupByPieces.isEmpty() ) {
						groupByPieces.forEach( (numPiece, listPurchase) -> list.add(getProductInventory(product, listPurchase, unity, numPiece)));
					} else {
						list.add(getProductInventory(product, purchaseRepository.findByProductAndUnity(product.getId(), unity), unity, null));
					}
				});
			} else {
				list.add(getProductInventory(product, purchaseRepository.findByProduct(product.getId()), null));
			}
		});
		return list;
	}
	
	private ProductInventory getProductInventory(Product product, List<Purchase> purchases, Integer numPieces) {
		ProductInventory pi = new ProductInventory();
		BeanUtils.copyProperties(product, pi);
		pi.setInventory(getPurchase(product, purchases, numPieces));
		return pi;
	}
	
	private ProductInventory getProductInventory(Product product, List<Purchase> purchases, Long unity, Integer numPieces) {
		ProductInventory pi = getProductInventory(product, purchases, numPieces);
		pi.setSku(pi.getSku().concat(String.valueOf(unity)));
		pi.getInventory().setUnity(unity);
		pi.getInventory().setUnityDesc( catalogService.findById(unity).getName() );
		return pi;
	}
	
	
	private Map<Integer, List<Purchase>> groupPurchasesByPieces(Long productId, Long unity){
		List<Purchase> findeds = purchaseRepository.findByProductAndUnityAndNumPieceNotNull(productId, unity);
		try {
			return findeds.stream().collect(Collectors.groupingBy(Purchase::getNumPiece));	
		} catch (NullPointerException e) {
			return null;
		}
	}
	
}
