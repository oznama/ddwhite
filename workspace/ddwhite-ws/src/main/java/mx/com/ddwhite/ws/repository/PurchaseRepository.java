package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Purchase;

@Repository
@Transactional
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	
	@Query("SELECT p FROM Purchase p WHERE p.product.id = :productId")
	List<Purchase> findByProduct(@Param("productId") Long productId);
	
	@Query("SELECT p FROM Purchase p WHERE p.dateCreated BETWEEN :startDate AND :endDate")
	List<Purchase> findByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
