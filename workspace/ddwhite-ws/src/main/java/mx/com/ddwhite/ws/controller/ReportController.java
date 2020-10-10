package mx.com.ddwhite.ws.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ddwhite.ws.dto.WithdrawalDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.reports.Cashout;
import mx.com.ddwhite.ws.service.ReportService;
import mx.com.ddwhite.ws.service.TicketPrintService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private ReportService service;
	
	@Autowired
	private TicketPrintService ticketPrintService;
	
	@GetMapping("/general/csv")
	public String getGeneralCSV(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getReportGeneralInCSV(startDate, endDate);
	}
	
	@GetMapping("/purchases/csv")
	public String getPurchasesCSV(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getReportPurchasesInCSV(startDate, endDate);
	}
	
	@GetMapping("/sales/csv")
	public String getSalesCSV(@RequestParam(value = "startDate", required = true) Date startDate, 
			@RequestParam(value = "endDate", required = true) Date endDate) {
		return service.getReportSalesInCSV(startDate, endDate);
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
	
	@GetMapping("/print/cashout")
	public ResponseEntity<?> printCashout(
			@RequestParam(value = "userId", required = true) Long userId,
			@RequestParam(value = "startDate", required = false) Date startDate, 
			@RequestParam(value = "endDate", required = false) Date endDate,
			@RequestParam(value = "cashInBox", required = false) BigDecimal cashInBox) {
		try {
			service.printCashout(userId, startDate, endDate, cashInBox);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/reprint/ticket")
	public ResponseEntity<?> reprintTicket(@RequestParam(value = "saleId", required = true) Long saleId)  {
		try {
			ticketPrintService.reprint(saleId);
			return ResponseEntity.ok().build();
		} catch(ResourceNotFoundException rnf) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rnf.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/payments")
	public String payments(@RequestParam(value = "paymentId", required = false) Long paymentId,
			@RequestParam(value = "startDate", required = false) Date startDate, 
			@RequestParam(value = "endDate", required = false) Date endDate) {
		return service.payments(paymentId, startDate, endDate);
	}
	
	@PostMapping("/print/withdrawall")
	public ResponseEntity<?> printWithdrawall(
			@RequestParam(value = "userId", required = true) Long userId,
			@RequestBody List<WithdrawalDto> denominations) {
		try {
			ticketPrintService.printWithdrawall(userId, denominations);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
