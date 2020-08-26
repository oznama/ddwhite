package mx.com.ddwhite.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.constants.Utils;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.model.Provider;
import mx.com.ddwhite.ws.repository.ProviderRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/provider")
public class ProviderController implements GenericController<Provider> {

	private final String MODULE = Product.class.getSimpleName();

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
	public ResponseEntity<?> create(Provider entity) {
		try {
			return ResponseEntity.ok(repository.saveAndFlush(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(Provider entity) {
		try {
			return ResponseEntity.ok(repository.findById(entity.getId()).map(t -> {
				BeanUtils.copyProperties(entity, t, "id");
				t.setDateCreated(Utils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
				return repository.saveAndFlush(t);
			}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", entity.getId())));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Provider provider = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(provider);
		repository.flush();
		return ResponseEntity.ok().build();
	}
}
