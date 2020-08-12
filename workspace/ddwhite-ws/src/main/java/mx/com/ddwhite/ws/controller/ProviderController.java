package mx.com.ddwhite.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Provider;
import mx.com.ddwhite.ws.repository.ProviderRepository;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	private ProviderRepository providerRepository;
	
	@GetMapping("/find")
	public Page<Provider> findAll(Pageable pageable) {
		return providerRepository.findAll(pageable);
	}

	@GetMapping("/find/{id}")
	public Provider findById(@PathVariable(value = "id") Long id) {
		return providerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Provider", "id", id));
	}

	@PostMapping("/save")
	public Provider create(@RequestBody Provider provider) {
		return providerRepository.save(provider);
	}

	@PutMapping("/update")
	public Provider update(@RequestBody Provider provider) {
		return providerRepository.findById(provider.getId()).map(p -> {
			BeanUtils.copyProperties(provider, p, "id");
			return providerRepository.save(p);
		}).orElseThrow(() -> new ResourceNotFoundException("Provider", "id", provider.getId()));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Provider user = providerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Provider", "id", id));
		providerRepository.delete(user);
		return ResponseEntity.ok().build();
	}

}
