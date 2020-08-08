package mx.com.ddwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.ddwhite.ws.model.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
	
}
