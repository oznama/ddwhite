package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE p.sku LIKE :sku")
	List<Product> findBySku(@Param("sku") String sku);
	
	@Query("SELECT p FROM Product p WHERE p.nameLarge LIKE :nameLarge")
	List<Product> findByName(@Param("nameLarge") String nameLarge);
	
	@Query("SELECT p FROM Product p WHERE p.sku LIKE :sku AND p.nameLarge LIKE :nameLarge")
	List<Product> findBySkuAndName(@Param("sku") String sku, @Param("nameLarge") String nameLarge);

}
