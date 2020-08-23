package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.dto.InventoryDto;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.repository.ProductRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;

@Service
public class InventoryService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	public List<ProductInventory> findInventory() {
		List<ProductInventory> list = new ArrayList<>();
		List<Product> products = productRepository.findAll();
		products.forEach( product -> {
			ProductInventory pi = new ProductInventory();
			BeanUtils.copyProperties(product, pi);
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if(!purchase.isEmpty()) {
				pi.setInventory(getPurchase(product, purchase));
			}
			list.add(pi);
		});
		return list;
	}
	
	public List<InventoryDto> getInventory() {
		final List<InventoryDto> inventory = new ArrayList<>();
		List<Product> products = productRepository.findAll();
		products.forEach( product -> {
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if(!purchase.isEmpty()) {
				inventory.add(getPurchase(product, purchase));
			}
		});
		return inventory;
	}

	public InventoryDto getInventory(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", productId));
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if(!purchase.isEmpty()) {
				return getPurchase(product, purchase);
			}

		return null;
	}

	private InventoryDto getPurchase(Product product, List<Purchase> purchase) {
		InventoryDto inv = new InventoryDto();
		inv.setProductId(product.getUserId());
		inv.setQuantity(purchase.size());
		purchase.sort(Comparator.comparing(Purchase::getDateCreated).reversed());
		inv.setCost(purchase.get(0).getUnitPrice());
		inv.setPrice(purchase.get(0).getUnitPrice().multiply(product.getPercentage()));	
		return inv;
	}
}
