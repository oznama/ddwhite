package mx.com.ddwhite.ws.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.service.SaleService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/sale")
public class SaleController implements GenericController<SaleDto> {

	@Autowired
	private SaleService service;

	@Override
	public Page<SaleDto> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public SaleDto findById(Long id) throws ResourceNotFoundException {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(SaleDto entity) {
		try {
			return ResponseEntity.ok(service.save(entity).getId());
		} catch (Throwable e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(SaleDto entity) {
		try {
			service.updateInvoice(entity.getId(), entity.getInvoice(), entity.getClientId());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}

	@GetMapping("/find/bydates")
	public Page<SaleDto> findByRange(@RequestParam("start") Date start, @RequestParam("end") Date end,Pageable pageable) {
		return service.findByRange(start, end, pageable);
	}
	
	@GetMapping("/getChasInRegister")
	public BigDecimal getChasInRegister(@RequestParam(value = "userId", required = true) Long userId) {
		return service.getChasInRegister(userId);
	}
	
	@GetMapping("/getExcedent")
	public BigDecimal getExcedent(@RequestParam(value = "userId", required = true) Long userId) {
		return service.getExcedent(userId);
	}

}
