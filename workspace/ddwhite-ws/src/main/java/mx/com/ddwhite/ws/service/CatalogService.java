package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.dto.CatalogItemReadDto;
import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Catalog;
import mx.com.ddwhite.ws.repository.CatalogRepository;

@Service
public class CatalogService {

	private final String MODULE = Catalog.class.getSimpleName();

	@Autowired
	private CatalogRepository repository;

	public List<CatalogReadDto> findAll() {
		final List<CatalogReadDto> catalogsReadDto = new ArrayList<>();
		final List<Catalog> catalogs = repository.findOnlyParents();
		catalogs.forEach(catalog -> {
			try {
				catalogsReadDto.add(buildCatalog(catalog));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return catalogsReadDto;
	}

	public CatalogReadDto findById(Long id) {
		Catalog catalog = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		try {
			return buildCatalog(catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CatalogReadDto findByName(String name) {
		Catalog catalog = repository.findByName(name.toUpperCase());
		try {
			return buildCatalog(catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CatalogReadDto buildCatalog(Catalog catalog) throws Exception {
		List<Catalog> items = repository.findByParent(catalog.getId());
		CatalogReadDto catalogReadDto = new CatalogReadDto();
		BeanUtils.copyProperties(catalog, catalogReadDto);
		for (Catalog item : items) {
			CatalogItemReadDto catalogReadItemDto = new CatalogItemReadDto();
			BeanUtils.copyProperties(item, catalogReadItemDto);
			catalogReadDto.getItems().add(catalogReadItemDto);
		}
		return catalogReadDto;
	}

}
