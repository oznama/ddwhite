package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.ProductDto;
import mx.com.ddwhite.ws.service.ProductService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/product")
public class ProductController implements GenericController<ProductDto> {
	
	@Autowired
	private ProductService service;

	@Override
	public Page<ProductDto> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	public ProductDto findById(Long id) {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(ProductDto entity) {
		try {
			return ResponseEntity.ok(service.create(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(ProductDto productDto) {
		try {
			service.update(productDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/saveBulk")
	public ResponseEntity<?> createBuilk(@RequestBody List<ProductDto> productsDto) {
		try {
			service.createBuilk(productsDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}
	
	@GetMapping("/findBySku")
	public List<ProductDto> findBySku(@RequestParam(value = "sku", required = true) String sku) {
		return service.findBySku(sku);
	}
	
	@GetMapping("/findByName")
	public List<ProductDto> findByName(@RequestParam(value = "name", required = true) String name) {
		return service.findByName(name);
	}
	
	@GetMapping("/findBySkuAndName")
	public List<ProductDto> findBySkuAndName(@RequestParam(value = "sku", required = true) String sku, @RequestParam(value = "name", required = true) String name) {
		return service.findBySkuAndName(sku, name);
	}
	
}
