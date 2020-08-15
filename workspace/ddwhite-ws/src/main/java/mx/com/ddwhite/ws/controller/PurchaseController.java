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
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.repository.PurchaseRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/purchase")
public class PurchaseController implements GenericController<Purchase> {
	
	private final String MODULE = Purchase.class.getSimpleName();
	
	@Autowired
	private PurchaseRepository repository;

	@Override
	public Page<Purchase> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Purchase findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public Purchase create(Purchase entity) {
		return repository.save(entity);
	}

	@Override
	public Purchase update(Purchase entity) {
		return repository.findById(entity.getId()).map(t -> {
			BeanUtils.copyProperties(entity, t, "id");
			return repository.save(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", entity.getId()));
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Purchase purchase = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(purchase);
		return ResponseEntity.ok().build();
	}

}
