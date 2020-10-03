package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.repository.ClientRepository;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

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
				t.setDateCreated(GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
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
	
	@GetMapping("/findByRfc")
	public List<Client> findByRfc(@RequestParam(value = "rfc") String rfc) {
		return repository.findByRfc("%" + rfc.toUpperCase() + "%");
	}
	
	@GetMapping("/findByName")
	public List<Client> findByName(@RequestParam(value = "fullName") String fullName) {
		return repository.findByFullName("%" + fullName.toUpperCase() + "%");
	}

}
