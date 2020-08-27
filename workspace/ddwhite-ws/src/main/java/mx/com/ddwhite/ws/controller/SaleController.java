package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return null;
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}

	@GetMapping("/find/bydates")
	public List<SaleDto> findByRange(@RequestParam("start") String start, @RequestParam("end") String end) {
		return service.findByRange(start, end);
	}

}
