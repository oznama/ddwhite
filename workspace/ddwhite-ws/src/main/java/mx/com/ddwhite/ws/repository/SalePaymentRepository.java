package mx.com.ddwhite.ws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.SalePayment;

@Repository
@Transactional
public interface SalePaymentRepository extends JpaRepository<SalePayment, Long> {
	
	@Query("SELECT sp FROM SalePayment sp WHERE sp.saleId = :saleId")
	List<SalePayment> findBySale(@Param("saleId") Long saleId);
	
	@Query("SELECT sp FROM SalePayment sp WHERE sp.payment = :payment")
	List<SalePayment> findByPayment(@Param("payment") Long payment);
	
	@Query("SELECT sp FROM SalePayment sp WHERE sp.saleId = :saleId AND sp.payment = :payment")
	List<SalePayment> findBySaleAndPayment(@Param("saleId") Long saleId, @Param("payment") Long payment);
	
	@Modifying
	@Transactional
	@Query("DELETE SalePayment sp where sp.saleId=:saleId")
	void deleteBySale(@Param("saleId") Long saleId);

}
