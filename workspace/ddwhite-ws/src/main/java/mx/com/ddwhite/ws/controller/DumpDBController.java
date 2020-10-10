package mx.com.ddwhite.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.com.ddwhite.ws.service.RuntimeService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/database")
public class DumpDBController {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@PostMapping("/restore")
	public ResponseEntity<?> restore(@RequestBody MultipartFile file) {
		try {
			if(file != null) {
				runtimeService.mysql(file.getInputStream());
				return ResponseEntity.ok().build();	
			} else
				return ResponseEntity.badRequest().body("File null");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/backup")
	public String backUp() {
		return runtimeService.mysqlDump();
	}

}
