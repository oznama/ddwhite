package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public List<InventoryDto> getAll() {
		return service.getInventory();
	}
	
	@GetMapping("/find/{productId}")
	public InventoryDto getByProduct(@PathVariable(value = "productId")Long productId) {
		return service.getInventory(productId);
	}
	
	@GetMapping("/find/product")
	public List<ProductInventory> findInventory() {
		return service.findInventory();
	}

	

}
