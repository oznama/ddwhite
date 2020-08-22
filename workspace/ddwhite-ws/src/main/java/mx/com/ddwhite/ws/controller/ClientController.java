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

import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.repository.ClientRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/client")
public class ClientController implements GenericController<Client> {

	private final String MODULE = Client.class.getSimpleName();

	@Autowired
	private ClientRepository repository;

	@Override
	public Page<Client> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Client findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public ResponseEntity<?> create(Client entity) {
		try {
			return ResponseEntity.ok(repository.saveAndFlush(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(Client entity) {
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
		Client e = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(e);
		repository.flush();
		return ResponseEntity.ok().build();
	}

}
