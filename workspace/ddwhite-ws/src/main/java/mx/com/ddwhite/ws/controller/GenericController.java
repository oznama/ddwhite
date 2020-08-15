package mx.com.ddwhite.ws.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GenericController<E> {
	
	@GetMapping("/find")
	public Page<E> findAll(Pageable pageable);
	
	@GetMapping("/find/{id}")
	public E findById(@PathVariable(value = "id") Long id);

	@PostMapping("/save")
	public E create(@RequestBody E entity);

	@PutMapping("/update")
	public E update(@RequestBody E entity);

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id);
	

}
