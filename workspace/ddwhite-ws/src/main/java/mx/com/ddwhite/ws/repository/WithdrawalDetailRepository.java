package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.WithdrawalDetail;

@Repository
@Transactional
public interface WithdrawalDetailRepository extends JpaRepository<WithdrawalDetail, Long> {
	
	@Query("SELECT wd FROM WithdrawalDetail wd WHERE wd.withdrawalId = :withdrawalId")
	List<WithdrawalDetail> findByWithdrawal(@Param("withdrawalId") Long withdrawalId);

}
