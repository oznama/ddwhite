package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Sale;

@Repository
@Transactional
public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	@Query("SELECT s FROM Sale s WHERE s.dateCreated BETWEEN :start AND :end")
	List<Sale> findByRange(@Param("start") String start, @Param("end") String end);
	
	@Query("SELECT s FROM Sale s WHERE s.dateCreated BETWEEN :start AND :end")
	List<Sale> findByRange(@Param("start") String start, @Param("end") String end, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("UPDATE Sale s SET s.invoice=:invoice WHERE s.id = :id")
	void updateInvoice(@Param("invoice") String invoice, @Param("id") Long id);
}
