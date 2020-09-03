package mx.com.ddwhite.ws.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.com.ddwhite.ws.model.RolePrivileges;

@Repository
@Transactional
public interface RolePrivilegesRepository extends JpaRepository<RolePrivileges, Long> {
	
	@Query("SELECT rp FROM RolePrivileges rp WHERE rp.roleId = :roleId")
	List<RolePrivileges> findByRole(@Param("roleId") Long roleId);
	
	@Query("SELECT rp FROM RolePrivileges rp WHERE rp.privilegeId = :privilegeId")
	List<RolePrivileges> findByPrivilege(@Param("privilegeId") Long privilegeId);

}
