package mx.com.ddwhite.ws.controller;

import java.math.BigDecimal;
import java.util.Date;

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
	
	private final Logger LOGGER = LoggerFactory.getLogger(SaleController.class);

	@Autowired
	private SaleService service;

	@Override
	public Page<SaleDto> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@Override
	public SaleDto findById(Long id) throws ResourceNotFoundException {
		return service.findById(id);
	}

	@Override
	public ResponseEntity<?> create(SaleDto entity) {
		try {
			return ResponseEntity.ok(service.save(entity).getId());
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(SaleDto entity) {
		try {
			service.updateInvoice(entity.getId(), entity.getInvoice(), entity.getClientId());
			return ResponseEntity.ok().build();
		}  catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		try {
			// TODO Checar si sera necesario registrar las cancelaciones en otra tabla
			service.delete(id);	// Esto sera el cancelar compra
			return ResponseEntity.ok().build();
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La venta con folio " + id + " no existe");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

//	@GetMapping("/find/bydates")
//	public Page<SaleDto> findByRange(@RequestParam("start") Date start, @RequestParam("end") Date end,Pageable pageable) {
//		return service.findByRange(start, end, pageable);
//	}
	
	@GetMapping("/find/byIdAndDates")
	public Page<SaleDto> findSaleIdAndDates(
			@RequestParam(value = "id", required = false) Long id, 
			@RequestParam(value = "start", required = false) Date start, 
			@RequestParam(value = "end", required = false) Date end,
			Pageable pageable) {
		if( id != null && start != null && end != null ) return service.findByIdAndRange(id, start, end, pageable);
		else if (id != null) return service.findById(id, pageable);
		else if (start != null && end != null) return service.findByRange(start, end, pageable);
		return null;
	}
	
	@GetMapping("/getChasInRegister")
	public BigDecimal getChasInRegister(@RequestParam(value = "userId", required = true) Long userId) {
		return service.getChasInRegister(userId);
	}
	
	@GetMapping("/getExcedent")
	public BigDecimal getExcedent(@RequestParam(value = "userId", required = true) Long userId) {
		return service.getExcedent(userId);
	}
	
	@GetMapping("/getLastSaleId")
	public Long getLastSaleId() {
		return service.getLastSaleId();
	}

}
