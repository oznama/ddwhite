package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.constants.Utils;
import mx.com.ddwhite.ws.dto.ProductDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Product;
import mx.com.ddwhite.ws.repository.ProductRepository;

@Service
public class ProductService {
	
	private final String MODULE = Product.class.getSimpleName();

	@Autowired
	private ProductRepository repository;
	
	public Page<ProductDto> findAll(Pageable pageable) {
		final List<ProductDto> productsDto = new ArrayList<>();
		Page<Product> products = repository.findAll(pageable);
		products.forEach( product -> {
			ProductDto productDto = new ProductDto();
			BeanUtils.copyProperties(product, productDto);
			productDto.setPrice(product.getCost().multiply(product.getPercentage()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN));
			productsDto.add(productDto);
		});
		return new PageImpl<>(productsDto, pageable, repository.count());
	}
	
	public ProductDto findById(Long id) {
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		ProductDto productDto = new ProductDto();
		BeanUtils.copyProperties(product, productDto);
		productDto.setPrice(product.getCost().multiply(product.getPercentage()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN));
		return productDto;
	}
	
	public ProductDto create(ProductDto productDto) {
		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);
		repository.saveAndFlush(product);
		productDto.setId(product.getId());
		return productDto;
	}
	
	public void update(ProductDto productDto) {
		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);
		repository.findById(productDto.getId()).map(t -> {
			BeanUtils.copyProperties(productDto, t);
			t.setDateCreated(Utils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
			return repository.saveAndFlush(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", productDto.getId()));
	}
	
	public void delete(Long id) {
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		repository.delete(product);
		repository.flush();
	}
	
	public void createBuilk(List<ProductDto> productsDto) throws DataAccessException {
		final List<Product> products = new ArrayList<>();
		productsDto.forEach( purchaseDto -> {
			Product purchase = new Product();
			BeanUtils.copyProperties(purchaseDto, purchase);
			products.add(purchase);
		});
		repository.saveAll(products);
		repository.flush();
	}

}
