package mx.com.ddwhite.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.CompanyDataDto;
import mx.com.ddwhite.ws.service.CompanyDataService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/company")
public class CompanyDataController implements GenericController<CompanyDataDto> {
	
	@Autowired
	private CompanyDataService service;

	@Override
	public Page<CompanyDataDto> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public CompanyDataDto findById(Long id) {
		return null;
	}

	@Override
	public ResponseEntity<?> create(CompanyDataDto entity) {
		return null;
	}

	@Override
	public ResponseEntity<?> update(CompanyDataDto entity) {
		try {
			service.update(entity);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return null;
	}

}
