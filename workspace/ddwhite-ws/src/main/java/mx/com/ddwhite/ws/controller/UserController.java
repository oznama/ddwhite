package mx.com.ddwhite.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.User;
import mx.com.ddwhite.ws.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/find")
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@GetMapping("/find/{id}")
	public User findById(@PathVariable(value = "id") Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}

	@PostMapping("/save")
	public User create(@RequestBody User user) {
		return userRepository.save(user);
	}

	@PutMapping("/update")
	public User update(@RequestBody User user) {
		return userRepository.findById(user.getId()).map(u -> {
			BeanUtils.copyProperties(user, u, "id");
			return userRepository.save(u);
		}).orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		userRepository.delete(user);
		return ResponseEntity.ok().build();
	}

}
