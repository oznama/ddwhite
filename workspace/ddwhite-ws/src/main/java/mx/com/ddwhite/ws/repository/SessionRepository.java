package mx.com.ddwhite.ws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.Session;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, Long> {
	
	@Query("SELECT s FROM Session s WHERE s.inDate >= :startDate AND s.outDate <= :endDate")
	List<Session> findByRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("SELECT s FROM Session s WHERE s.userId = :userId AND :date BETWEEN s.inDate AND s.outDate")
	Session findSessionByUserInDate(@Param("userId") Long userId, @Param("date") String date);
	
	@Query("SELECT s FROM Session s WHERE s.userId = :userId AND s.inDate < :date AND s.outDate IS NULL")
	Session findCurrentSession(@Param("userId") Long userId, @Param("date") String date);
	
	@Query("SELECT s FROM Session s WHERE s.userId = :userId AND s.inDate >= :startDate AND s.outDate <= :endDate ORDER BY id DESC")
	List<Session> findByUserIdAndRange(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Modifying
	@Transactional
	@Query("UPDATE Session s SET s.outDate = :outDate  WHERE s.id = :id")
	void updateCloseSession(@Param("id") Long id, @Param("outDate") String outDate);
	
}
