package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.repository.ProductRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/product")
public class ProductController implements GenericController<Product> {

	private final String MODULE = Product.class.getSimpleName();

	@Autowired
	private ProductRepository repository;

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Product findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public ResponseEntity<?> create(Product entity) {
		try {
			return ResponseEntity.ok(repository.saveAndFlush(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(Product entity) {
		try {
			return ResponseEntity.ok(repository.findById(entity.getId()).map(t -> {
				BeanUtils.copyProperties(entity, t, "id");
				return repository.saveAndFlush(t);
			}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", entity.getId())));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(product);
		repository.flush();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/saveBulk")
	public ResponseEntity<?> createBuilk(@RequestBody List<Product> entities) {
		try {
			repository.saveAll(entities);
			repository.flush();
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}	

}
