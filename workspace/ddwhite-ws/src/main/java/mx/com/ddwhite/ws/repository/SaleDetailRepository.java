package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.SaleDetail;

@Repository
@Transactional
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

	@Query("SELECT sd FROM SaleDetail sd WHERE sd.saleId = :saleId")
	List<SaleDetail> findBySale(@Param("saleId") Long saleId);
}
