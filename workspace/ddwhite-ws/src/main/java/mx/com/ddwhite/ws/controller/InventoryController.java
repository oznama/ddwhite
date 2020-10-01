package mx.com.ddwhite.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.InventoryDto;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.service.InventoryService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService service;
	
	@GetMapping("/find")
	public Page<InventoryDto> getAll(Pageable pageable) {
		return service.getInventory(pageable);
	}
	
	@GetMapping("/find/{productId}")
	public InventoryDto getByProduct(@PathVariable(value = "productId")Long productId) {
		return service.getInventory(productId);
	}
	
	@GetMapping("/find/product")
	public Page<ProductInventory> findInventory(
			@RequestParam(value = "sku", required = false) String sku, 
			@RequestParam(value = "name", required = false) String name, 
			Pageable pageable) {
		return service.findInventory(pageable, sku, name);
	}
	
	@GetMapping("/find/sale")
	public Page<ProductInventory> findForSale(
			@RequestParam(value = "sku", required = false) String sku, 
			@RequestParam(value = "name", required = false) String name, 
			Pageable pageable) {
		return service.findForSale(pageable, sku, name);
	}

}
