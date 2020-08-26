package mx.com.ddwhite.ws.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.SalePayment;

@Repository
@Transactional
public interface SalePaymentRepository extends JpaRepository<SalePayment, Long> {

}
