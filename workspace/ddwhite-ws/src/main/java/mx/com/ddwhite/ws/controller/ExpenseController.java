package mx.com.ddwhite.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Expense;
import mx.com.ddwhite.ws.repository.ExpenseRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/expense")
public class ExpenseController implements GenericController<Expense> {
	
	private final String MODULE = Expense.class.getSimpleName();

	@Autowired
	private ExpenseRepository repository;

	@Override
	public Page<Expense> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Expense findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
	}

	@Override
	public ResponseEntity<?> create(Expense entity) {
		try {
			return ResponseEntity.ok(repository.saveAndFlush(entity));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(Expense entity) {
		return null;
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}

}
