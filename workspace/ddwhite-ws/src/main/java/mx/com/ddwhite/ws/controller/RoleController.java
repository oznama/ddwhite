package mx.com.ddwhite.ws.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.model.Role;
import mx.com.ddwhite.ws.repository.RoleRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/role")
public class RoleController implements GenericController<Role> {
	
	@Autowired
	private RoleRepository repository;

	@Override
	public Page<Role> findAll(Pageable pageable) {
		List<Role> roles = repository.findAll();
		roles = roles.stream().filter(role -> !role.getName().equals(GeneralConstants.ROOT_ROLE))
				.collect(Collectors.toList());
		return new PageImpl<>(roles, pageable, roles.size());
	}

	@Override
	public Role findById(Long id) {
		return null;
	}

	@Override
	public ResponseEntity<?> create(Role entity) {
		return null;
	}

	@Override
	public ResponseEntity<?> update(Role entity) {
		return null;
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}

}
