package mx.com.ddwhite.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	public Product create(Product entity) {
		return repository.save(entity);
	}

	@Override
	public Product update(Product entity) {
		return repository.findById(entity.getId()).map(t -> {
			BeanUtils.copyProperties(entity, t, "id");
			return repository.save(t);
		}).orElseThrow(() -> new ResourceNotFoundException("User", "id", entity.getId()));
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(product);
		return ResponseEntity.ok().build();
	}

}
