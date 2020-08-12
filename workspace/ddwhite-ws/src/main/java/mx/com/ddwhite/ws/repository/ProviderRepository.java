package mx.com.ddwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.Provider;

@Repository
@Transactional
public interface ProviderRepository extends JpaRepository<Provider, Long> {
	
}
