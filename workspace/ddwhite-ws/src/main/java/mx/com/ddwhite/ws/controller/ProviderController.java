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
import mx.com.ddwhite.ws.model.Provider;
import mx.com.ddwhite.ws.repository.ProviderRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/provider")
public class ProviderController implements GenericController<Provider> {
	
	private final String MODULE = Provider.class.getSimpleName();

	@Autowired
	private ProviderRepository repository;
	
	@Override
	public Page<Provider> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Provider findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public Provider create(Provider entity) {
		return repository.save(entity);
	}

	@Override
	public Provider update(Provider entity) {
		return repository.findById(entity.getId()).map(t -> {
			BeanUtils.copyProperties(entity, t, "id");
			return repository.save(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", entity.getId()));
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Provider e = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(e);
		return ResponseEntity.ok().build();
	}

}
