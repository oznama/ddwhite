package mx.com.ddwhite.ws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.Expense;

@Repository
@Transactional
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	@Query("SELECT e FROM Expense e WHERE e.dateCreated BETWEEN :startDate AND :endDate")
	Page<Expense> findByDatePageable(@Param("startDate") String startDate, @Param("endDate") String endDate, Pageable pageable);
	
	@Query("SELECT e FROM Expense e WHERE e.dateCreated BETWEEN :startDate AND :endDate")
	List<Expense> findByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
