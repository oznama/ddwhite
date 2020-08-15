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
	public Client create(Client entity) {
		return repository.save(entity);
	}

	@Override
	public Client update(Client entity) {
		return repository.findById(entity.getId()).map(t -> {
			BeanUtils.copyProperties(entity, t, "id");
			return repository.save(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", entity.getId()));
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Client e = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(e);
		return ResponseEntity.ok().build();
	}

}
