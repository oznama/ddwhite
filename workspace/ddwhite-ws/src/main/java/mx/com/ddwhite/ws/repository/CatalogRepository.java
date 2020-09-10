package mx.com.ddwhite.ws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.Catalog;

@Repository
@Transactional
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
	
	@Query("SELECT c FROM Catalog c WHERE c.name = :name")
	Catalog findByName(@Param("name") String name);
	
	@Query("SELECT c FROM Catalog c WHERE c.catalogParentId = :catalogParentId AND c.name = :name")
	Catalog findByNameAndCatalogParent(@Param("catalogParentId") Long catalogParentId, @Param("name") String name);
	
	@Query("SELECT c FROM Catalog c WHERE c.catalogParentId = :catalogParentId")
	List<Catalog> findByParent(@Param("catalogParentId") Long catalogParentId);
	
	@Query("SELECT c FROM Catalog c WHERE c.catalogParentId = null")
	List<Catalog> findOnlyParents();
	
	@Modifying
	@Transactional
	@Query("UPDATE Catalog c SET c.description=:description WHERE c.id = :id")
	void updateDescription(@Param("description") String description, @Param("id") Long id);

}
