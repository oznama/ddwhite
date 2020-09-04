package mx.com.ddwhite.ws.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.service.ReportService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private ReportService service;
	
	@GetMapping("/general/csv")
	public String getGeneralCSV(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getReportGeneralInCSV(startDate, endDate);
	}
	
	@GetMapping("/warehouse/csv")
	public String getWarehouseCSV(Sort sort) {
		return service.getWarehouseInCSV(sort);
	}

//	@GetMapping("/account/out")
//	public List<AccountOutput> getOutputs(
//			@RequestParam(value = "startDate", required = true) String startDate, 
//			@RequestParam(value = "endDate", required = true) String endDate) {
//		return service.getOutputs(startDate, endDate);
//	}
//
//	@GetMapping("/account/in")
//	public List<AccountInput> getInputs(
//			@RequestParam(value = "startDate", required = true) String startDate, 
//			@RequestParam(value = "endDate", required = true) String endDate) {
//		return service.getInputs(startDate, endDate);
//	}
//	
//	@GetMapping("/general")
//	public ReportGeneral getGeneral(@RequestParam(value = "startDate", required = true) String startDate, 
//			@RequestParam(value = "endDate", required = true) String endDate) {
//		return service.getReportGeneral(startDate, endDate);
//	}
	
//	@GetMapping("/warehouse")
//	public List<Warehouse> getWarehouse(Sort sort) {
//		return service.getWarehouse(sort);
//	}

}
