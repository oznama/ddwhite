package mx.com.ddwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Purchase;

@Repository
@Transactional
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
