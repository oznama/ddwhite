package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.PurchaseDto;
import mx.com.ddwhite.ws.dto.PurchaseReasignDto;
import mx.com.ddwhite.ws.service.PurchaseService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/purchase")
public class PurchaseController implements GenericController<PurchaseDto> {

	@Autowired
	private PurchaseService service;

	@Override
	public Page<PurchaseDto> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	public PurchaseDto findById(Long id) {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(PurchaseDto purchaseDto) {
		try {
			return ResponseEntity.ok(service.create(purchaseDto));
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(PurchaseDto purchaseDto) {
		try {
			service.update(purchaseDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/saveBulk")
	public ResponseEntity<?> createBuilk(@RequestBody List<PurchaseDto> purchasesDto) {
		System.out.println(purchasesDto.toString());
		try {
			service.createBuilk(purchasesDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}
	
	@GetMapping("/findByProduct/{id}/{productId}")
	public List<PurchaseDto> findByProduct(@PathVariable("id") Long id, @PathVariable("productId") Long productId) {
		return service.findByProductExceptCurrent(id, productId);
	}
	
	@GetMapping("/findReasign")
	public List<PurchaseDto> findForReasign() {
		return service.findByReasignProduct();
	}
	
	@PutMapping("/reasign")
	public ResponseEntity<?> reasign(@RequestBody PurchaseReasignDto purchaseReasingDto) {
		try {
			service.updateReasign(purchaseReasingDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		}
	}

}
