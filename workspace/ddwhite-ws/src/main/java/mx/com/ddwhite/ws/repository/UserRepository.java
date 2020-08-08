package mx.com.ddwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.ddwhite.ws.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
