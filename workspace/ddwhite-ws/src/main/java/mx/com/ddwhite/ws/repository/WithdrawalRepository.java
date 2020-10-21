package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Withdrawal;

@Repository
@Transactional
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
	
	@Query("SELECT w FROM Withdrawal w WHERE w.sessionId = :sessionId ORDER BY w.dateCreated DESC")
	List<Withdrawal> findBySession(@Param("sessionId") Long sessionId);
	
	@Query("SELECT w FROM Withdrawal w WHERE w.dateCreated BETWEEN :startDate AND :endDate")
	List<Withdrawal> findByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
