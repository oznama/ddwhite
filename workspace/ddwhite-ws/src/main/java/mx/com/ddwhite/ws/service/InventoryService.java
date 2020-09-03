package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
				inventory.add(getPurchase(product, purchase));
			}
		});
		return new PageImpl<>(inventory, pageable, inventory.size());
	}

	public InventoryDto getInventory(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", productId));
		List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
		if (!purchase.isEmpty()) {
			return getPurchase(product, purchase);
		}
		return null;
	}
	
	public Page<ProductInventory> findForSale(Pageable pageable) {
		List<ProductInventory> list = findInventory();
		list.forEach(product -> {
			int quantity = product.getInventory().getQuantity();
			product.getInventory()
					.setQuantity(quantity - sumSaleQuantity(saleDetailRepository.findByProduct(product.getId())));
		});
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
			ProductInventory pi = new ProductInventory();
			BeanUtils.copyProperties(product, pi);
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if (!purchase.isEmpty()) {
				pi.setInventory(getPurchase(product, purchase));
			}
			list.add(pi);
		});
		return list;
	}
}
