package mx.com.ddwhite.ws.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.constants.Utils;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.User;
import mx.com.ddwhite.ws.repository.UserRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/user")
public class UserController implements GenericController<User> {

	private final String MODULE = User.class.getSimpleName();

	@Autowired
	private UserRepository repository;

	@Override
	public Page<User> findAll(Pageable pageable) {
		List<User> users = repository.findAll(pageable).getContent();
		users = users.stream().filter( user -> !user.getUsername().equals(GeneralConstants.USERNAME_ADMIN) ).collect(Collectors.toList());
		return new PageImpl<>(users, pageable, repository.count());
	}

	@Override
	public User findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public ResponseEntity<?> create(User entity) {
		try {
			return ResponseEntity.ok(repository.saveAndFlush(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(User entity) {
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
		User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(user);
		repository.flush();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		User userFinded = repository.findByUsername(user.getUsername());
		if (userFinded != null) {
			if( user.getPassword().equals(userFinded.getPassword()) ) return ResponseEntity.ok(userFinded);
			else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La contraseña es incorrecta");
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario no se encuentra registrado");
	}

}
