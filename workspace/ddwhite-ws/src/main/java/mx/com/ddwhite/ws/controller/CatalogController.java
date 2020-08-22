package mx.com.ddwhite.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.service.CatalogService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	private CatalogService service;
	
	@GetMapping("/find")
	public List<CatalogReadDto> findAll() {
		return service.findAll();
	}

	@GetMapping("/find/{id}")
	public CatalogReadDto findById(@PathVariable(value = "id") Long id) {
		return service.findById(id);
	}
	
	@GetMapping("/findByName/{name}")
	public CatalogReadDto findByName(@PathVariable(value = "name") String name) {
		return service.findByName(name);
	}
	
	

}
