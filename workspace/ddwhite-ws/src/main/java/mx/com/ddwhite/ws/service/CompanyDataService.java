package mx.com.ddwhite.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.CatalogItemReadDto;
import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.dto.CompanyDataDto;
import mx.com.ddwhite.ws.model.Catalog;

@Service
public class CompanyDataService {
	
	@Autowired
	private CatalogService catalogService;
	
	public void update(final CompanyDataDto companyDataDto) throws Exception {
		CatalogReadDto catalogCompany = catalogService.findByName(GeneralConstants.CATALOG_NAME_COMPANY);
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_NAME, companyDataDto.getName(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_ADDRESS, companyDataDto.getAddress(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_PHONE, companyDataDto.getPhone(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_MESSAGE, companyDataDto.getMessageTicket(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_WEBSITE, companyDataDto.getWebsite(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_MAIL, companyDataDto.getEmail(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_BUSSINES_NAME, companyDataDto.getBussinesName(), catalogCompany.getId() );
		update( catalogCompany.getItems(), GeneralConstants.CATALOG_NAME_COMPANY_RFC, companyDataDto.getRfc(), catalogCompany.getId() );
	}
	
	private void update( List<CatalogItemReadDto> items, final String property, final String description, final Long catalogParentId ) {
		createCatalogItem(catalogParentId, property, description);
		items.forEach( item -> {
			if( item.getName().equals(property) && item.getId() != null ) {
				updateCatalogItem(item.getId(), description, property);
				return;
			}
		});
	}
	
	private void createCatalogItem(Long catalogParentId, String property, String description) {
		if( !StringUtils.isEmpty(description) ) {
			if( catalogService.findByNameAndCatalogParent(catalogParentId, property) != null ) return;
			Catalog catalogItem = new Catalog();
			catalogItem.setCatalogParentId(catalogParentId);
			catalogItem.setName(property);
			catalogItem.setDescription(description);
			try {
				catalogService.create(catalogItem);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateCatalogItem(Long id, String description, String property) {
		description = StringUtils.isEmpty(description) ? null : description;
		try {
			catalogService.updateDescription(id, description);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
