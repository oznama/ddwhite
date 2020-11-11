package mx.com.ddwhite.ws.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.PurchaseDto;
import mx.com.ddwhite.ws.dto.PurchaseListDto;
import mx.com.ddwhite.ws.dto.PurchaseReasignDto;
import mx.com.ddwhite.ws.service.PurchaseService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/purchase")
public class PurchaseController implements GenericController<PurchaseDto> {
	
	private final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);

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
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(PurchaseDto purchaseDto) {
		try {
			service.update(purchaseDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		try {
			service.delete(id);
			return ResponseEntity.ok().build();
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La venta con folio " + id + " no existe");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/saveBulk")
	public ResponseEntity<?> createBuilk(@RequestBody List<PurchaseDto> purchasesDto) {
		try {
			service.createBuilk(purchasesDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
			service.saveReasign(purchaseReasingDto);
			return ResponseEntity.ok().build();
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/getList")
	public List<PurchaseListDto> getList(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getList(startDate, endDate);
	}
	
	@GetMapping("/getOne/{id}")
	public PurchaseListDto getOne(@PathVariable("id") Long id) {
		return service.getOne(id);
	}

}
