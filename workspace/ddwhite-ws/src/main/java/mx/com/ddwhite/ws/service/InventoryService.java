package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.InventoryDto;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.model.SaleDetail;
import mx.com.ddwhite.ws.repository.ProductRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;
import mx.com.ddwhite.ws.repository.SaleDetailRepository;

@Service
public class InventoryService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Autowired
	private SaleDetailRepository saleDetailRepository;

	public Page<ProductInventory> findInventory(Pageable pageable) {
		List<ProductInventory> list = new ArrayList<>();
		Page<Product> products = productRepository.findAll(pageable);
		products.forEach(product -> {
			ProductInventory pi = new ProductInventory();
			BeanUtils.copyProperties(product, pi);
			List<Purchase> purchase = purchaseRepository.findByProduct(product.getId());
			if (!purchase.isEmpty()) {
				pi.setInventory(getPurchase(product, purchase));
			}
			list.add(pi);
		});
		return new PageImpl<>(list, pageable, productRepository.count());
	}

	public Page<ProductInventory> findForSale(Pageable pageable) {
		Page<ProductInventory> list = findInventory(pageable);
		list.forEach(product -> {
			int quantity = product.getInventory().getQuantity();
			product.getInventory()
					.setQuantity(quantity - sumSaleQuantity(saleDetailRepository.findByProduct(product.getId())));
		});
		List<ProductInventory> listForSale = list.stream().filter(p -> p.getInventory().getQuantity() > 0)
				.collect(Collectors.toList());
		return new PageImpl<>(listForSale, pageable, listForSale.size());
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

	private InventoryDto getPurchase(Product product, List<Purchase> purchase) {
		InventoryDto inv = new InventoryDto();
		inv.setProductId(product.getUserId());
		inv.setQuantity(sumPurchaseQuantity(purchase));
		inv.setAverageCost(averageCost(purchase));
		inv.setPrice(product.getCost().multiply(product.getPercentage()).setScale(GeneralConstants.BIG_DECIMAL_ROUND,
				BigDecimal.ROUND_HALF_EVEN));
		return inv;
	}

	private int sumPurchaseQuantity(List<Purchase> purchases) {
		int sum = 0;
		for (Purchase purchase : purchases)
			sum += purchase.getQuantity();
		return sum;
	}

	private int sumSaleQuantity(List<SaleDetail> salesDetail) {
		int sum = 0;
		for (SaleDetail saleDetail : salesDetail)
			sum += saleDetail.getQuantity();
		return sum;
	}

	private BigDecimal averageCost(List<Purchase> purchases) {
		BigDecimal average = BigDecimal.valueOf(0);
		for (Purchase purchase : purchases)
			average = average.add(purchase.getCost());
		average = average.divide(BigDecimal.valueOf(purchases.size()), BigDecimal.ROUND_HALF_EVEN);
		return average;
	}
}
