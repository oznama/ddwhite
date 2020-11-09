package mx.com.ddwhite.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.service.UserService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/user")
public class UserController implements GenericController<UserDto> {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService service;

	@Override
	public Page<UserDto> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	public UserDto findById(Long id) {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(UserDto userDto) {
		try {
			return ResponseEntity.ok(service.create(userDto));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(UserDto userDto) {
		try {
			service.update(userDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		try {
			service.delete(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		LOGGER.info("Logging user {}", userDto.getUsername());
		try {
			return ResponseEntity.ok(service.login(userDto));
		} catch (Exception e) {
			LOGGER.error("Impossible login, cause: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

}
