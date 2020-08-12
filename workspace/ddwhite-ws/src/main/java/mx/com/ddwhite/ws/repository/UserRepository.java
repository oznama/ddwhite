package mx.com.ddwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.ddwhite.ws.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
	User findByUserAndPassword(@Param("username") String username, @Param("password") String password);

}
