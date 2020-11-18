package mx.com.ddwhite.ws.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.SessionDto;
import mx.com.ddwhite.ws.service.SessionService;
import mx.com.ddwhite.ws.service.WithdrawalService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/session")
public class SessionController implements GenericController<SessionDto> {
	
	@Autowired
	private SessionService service;
	
	@Autowired
	private WithdrawalService withdawallService;

	@Override
	public Page<SessionDto> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public SessionDto findById(Long id) {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(SessionDto sessionDto) {
		try {
			return ResponseEntity.ok(service.create(sessionDto));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(SessionDto sessionDto) {
		try {
			service.update(sessionDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}
	
	@PutMapping("/close")
	public ResponseEntity<?> updateCloseSession(
			@RequestParam(value = "id", required = true) Long id, 
			@RequestParam(value = "finalAmount", required = true) BigDecimal finalAmount) {
		try {
			service.updateCloseSession(id, finalAmount);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}
	
	@GetMapping("/findByRange")
	public List<SessionDto> findByRange(
			@RequestParam(value = "startDate", required = false) Date startDate, 
			@RequestParam(value = "endDate", required = false) Date endDate) {
		if( startDate == null || endDate == null ) {
			startDate = Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		return service.findByRange(startDate, endDate);
	}
	
	@GetMapping("/findCurrentSession")
	public SessionDto findCurrentSession(@RequestParam(value = "userId", required = true) Long userId) {
		return service.findCurrentSession(userId);
	}
	
	@GetMapping("/withdrawals")
	public ResponseEntity<?> updateCloseSession(@RequestParam(value = "id", required = true) Long id) {
		try {
			return ResponseEntity.ok().body(withdawallService.findBySession(id));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

}
