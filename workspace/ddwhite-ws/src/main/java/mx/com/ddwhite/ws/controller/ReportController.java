package mx.com.ddwhite.ws.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.reports.Cashout;
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
	
	@GetMapping("/cashout")
	public Cashout getCashout(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getCashout(startDate, endDate);
	}

}
