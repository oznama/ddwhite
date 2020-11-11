package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
	
	@Query("SELECT sd FROM SaleDetail sd WHERE sd.productId = :productId")
	List<SaleDetail> findByProduct(@Param("productId") Long productId);
	
	@Query("SELECT sd FROM SaleDetail sd WHERE sd.productId = :productId AND sd.unity = :unity AND sd.numPiece = :numPiece")
	List<SaleDetail> findByProductAndUnityWithPieces(@Param("productId") Long productId, @Param("unity") Long unity, @Param("numPiece") Integer numPiece );
	
	@Query("SELECT sd FROM SaleDetail sd WHERE sd.productId = :productId AND sd.unity = :unity AND sd.numPiece IS NULL")
	List<SaleDetail> findByProductAndUnityWithoutPieces(@Param("productId") Long productId, @Param("unity") Long unity);
	
	@Modifying
	@Transactional
	@Query("DELETE SaleDetail sd where sd.saleId=:saleId")
	void deleteBySale(@Param("saleId") Long saleId);
}
