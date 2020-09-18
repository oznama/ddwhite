package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.PurchaseReasign;

@Repository
@Transactional
public interface PurchaseReasignRepository extends JpaRepository<PurchaseReasign, Long> {
	
	@Query("SELECT pr FROM PurchaseReasign pr WHERE pr.purchasesOrigin = :purchasesOrigin")
	List<PurchaseReasign> findByOrigin(@Param("purchasesOrigin") Long purchasesOrigin);
	
	@Query("SELECT pr FROM PurchaseReasign pr WHERE pr.purchaseDestity = :purchaseDestity")
	List<PurchaseReasign> findByDestiny(@Param("purchaseDestity") Long purchaseDestity);

}
