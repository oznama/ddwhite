package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger LOGGER = LoggerFactory.getLogger(CatalogService.class);

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
				LOGGER.error("Error finding all", e);
			}
		});
		return catalogsReadDto;
	}

	public CatalogReadDto findById(Long id) {
		LOGGER.debug("Finding by id {}", id);
		try {
			return buildCatalog(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id)));
		} catch (Exception e) {
			LOGGER.error("Error finding by id", e);
		}
		return null;
	}

	public CatalogReadDto findByName(String name) {
		LOGGER.debug("Finding by name {}", name);
		try {
			return buildCatalog(repository.findByName(name.toUpperCase()));
		} catch (Exception e) {
			LOGGER.error("Error finding by name", e);
		}
		return null;
	}

	public CatalogReadDto findByNameAndCatalogParent(Long catalogParentId, String name) {
		Catalog catalog = repository.findByNameAndCatalogParent(catalogParentId, name);
		return mapCatalog(catalog);
	}

	public Catalog create(Catalog catalog) {
		return repository.saveAndFlush(catalog);
	}

	public void updateDescription(Long id, String description) {
		repository.updateDescription(description, id);
	}
	
	public void updateItems(Long catalogId, List<Catalog> items) {
		items.forEach(item -> {
			if( item.getId() != null ) {
				Catalog cToUp = repository.getOne(item.getId());
				cToUp.setName(item.getName());
				cToUp.setDescription(item.getDescription());
				repository.save(cToUp);
			} else {
				item.setCatalogParentId(catalogId);
				repository.save(item);
			}
		});
		repository.flush();
	}

	private CatalogReadDto mapCatalog(Catalog catalog) {
		if( catalog == null ) return null;
		CatalogReadDto catalogReadDto = new CatalogReadDto();
		BeanUtils.copyProperties(catalog, catalogReadDto);
		return catalogReadDto;
	}

	private CatalogReadDto buildCatalog(Catalog catalog) throws Exception {
		List<Catalog> items = repository.findByParent(catalog.getId());
		final CatalogReadDto catalogReadDto = mapCatalog(catalog);
		items.forEach( item -> {
			CatalogItemReadDto catalogReadItemDto = new CatalogItemReadDto();
			BeanUtils.copyProperties(item, catalogReadItemDto);
			catalogReadDto.getItems().add(catalogReadItemDto);	
		});
		return catalogReadDto;
	}

}
