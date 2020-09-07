package mx.com.ddwhite.ws.service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import mx.com.ddwhite.ws.dto.InventoryDto;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.model.SaleDetail;

public class InventoryUtils {
	
	private final String KEY_EXTRACTOR_COMPARATOR_SKU = "sku";
	
	protected Page<ProductInventory> pagging( List<ProductInventory> list, final Pageable pageable ){
		int originalSize = list.size();
		int start = Math.min(originalSize, Math.abs(pageable.getPageNumber() * pageable.getPageSize()));
		list.subList(0, start).clear();
		int size = list.size();
		int end = Math.min(pageable.getPageSize(), size);
		list.subList(end, size).clear();
		sorting(list, pageable.getSort());
		return new PageImpl<>(list, pageable, originalSize);
	}

	protected void sorting(List<ProductInventory> list, final Sort sort) {
		if(sort.isSorted()) {
			sort.forEach(order -> {
				if( order.getProperty().equals(KEY_EXTRACTOR_COMPARATOR_SKU)) {
					list.sort(Comparator.comparing(ProductInventory::getSku));
				}
			});
		}
	}

	protected InventoryDto getPurchase(Product product, List<Purchase> purchase) {
		InventoryDto inv = new InventoryDto();
		inv.setProductId(product.getUserId());
		inv.setQuantity(sumPurchaseQuantity(purchase));
		inv.setAverageCost(averageCost(purchase));
//		inv.setPrice(product.getCost().multiply(product.getPercentage()).setScale(GeneralConstants.BIG_DECIMAL_ROUND,
//				BigDecimal.ROUND_HALF_EVEN));
		inv.setPrice(product.getCost().multiply(product.getPercentage()).setScale(0,RoundingMode.UP).intValue());
		return inv;
	}

	protected int sumPurchaseQuantity(List<Purchase> purchases) {
		int sum = 0;
		for (Purchase purchase : purchases)
			sum += purchase.getQuantity();
		return sum;
	}

	protected int sumSaleQuantity(List<SaleDetail> salesDetail) {
		int sum = 0;
		for (SaleDetail saleDetail : salesDetail)
			sum += saleDetail.getQuantity();
		return sum;
	}

	protected BigDecimal averageCost(List<Purchase> purchases) {
		BigDecimal average = BigDecimal.valueOf(0);
		for (Purchase purchase : purchases)
			average = average.add(purchase.getCost());
		average = average.divide(BigDecimal.valueOf(purchases.size()), BigDecimal.ROUND_HALF_EVEN);
		return average;
	}

}
