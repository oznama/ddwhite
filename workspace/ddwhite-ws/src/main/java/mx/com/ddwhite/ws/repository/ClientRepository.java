package mx.com.ddwhite.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Client;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	@Query("SELECT c FROM Client c WHERE UPPER(c.rfc) LIKE :rfc")
	List<Client> findByRfc(@Param("rfc") String rfc);
	
	@Query("SELECT c FROM Client c WHERE UPPER(CONCAT(c.name, ' ', c.midleName, ' ', c.lastName)) LIKE :nameLarge")
	List<Client> findByFullName(@Param("nameLarge") String nameLarge);

}
