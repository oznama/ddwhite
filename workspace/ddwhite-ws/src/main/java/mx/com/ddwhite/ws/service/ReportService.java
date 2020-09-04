package mx.com.ddwhite.ws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.reports.AccountInput;
import mx.com.ddwhite.ws.reports.AccountOutput;
import mx.com.ddwhite.ws.reports.AccountTotal;
import mx.com.ddwhite.ws.reports.ReportGeneral;
import mx.com.ddwhite.ws.reports.Totals;
import mx.com.ddwhite.ws.reports.Warehouse;
import mx.com.ddwhite.ws.service.utils.GenericUtils;
import mx.com.ddwhite.ws.service.utils.ReportUtils;

@Service
public class ReportService {
	
	@Autowired
	private AccountOutputService outService;
	
	@Autowired
	private AccountInputService inService;
	
	@Autowired
	private InventoryService invetory;
	
	public ReportGeneral getReportGeneral(String startDate, String endDate){
		ReportGeneral general = new ReportGeneral();
		List<AccountInput> in = getInputs(startDate, endDate);
		List<AccountOutput> out = getOutputs(startDate, endDate);
		general.setInputs(in);
		general.setOutputs(out);
		general.setTotalIn(ReportUtils.getTotalIn(in));
		general.setTotalOut(ReportUtils.getTotalOut(out));
		// Total diffs
		general.getTotals().setTotal( general.getTotalIn().gettTotal().subtract( general.getTotalOut().gettTotal() ) );
		general.getTotals().setSubTotal( general.getTotalIn().getSbTotal().subtract(general.getTotalOut().getSbTotal()) );
		general.getTotals().setIva( general.getTotalIn().getIvaTotal().subtract(general.getTotalOut().getIvaTotal()) );
		return general;
	}
	
	public String getReportGeneralInCSV(Date startDate, Date endDate) {
		String strStartDate = GenericUtils.dateToString(startDate, GeneralConstants.FORMAT_DATE_TIME);
		endDate = GenericUtils.plusDay(endDate, 1);
		String strEndDate = GenericUtils.dateToString(endDate, GeneralConstants.FORMAT_DATE_TIME);
		ReportGeneral general = getReportGeneral(strStartDate, strEndDate);
		StringBuilder builder = new StringBuilder();
		builder.append("COMPRAS-GASTOS").append(GeneralConstants.CSV_EOF);
		builder.append( ReportUtils.ExportListToCSV(true, general.getOutputs(), AccountOutput.class) );
		builder.append(GeneralConstants.CSV_EOF);
		builder.append("VENTAS").append(GeneralConstants.CSV_EOF);
		builder.append( ReportUtils.ExportListToCSV(true, general.getInputs(), AccountInput.class) );
		builder.append(GeneralConstants.CSV_EOF);
		builder.append("TOTAL COMPRAS-GASTOS").append(GeneralConstants.CSV_EOF);
		builder.append( ReportUtils.ExportObjectToCSV(true, general.getTotalOut(), AccountTotal.class) );
		builder.append(GeneralConstants.CSV_EOF);
		builder.append("TOTAL VENTAS").append(GeneralConstants.CSV_EOF);
		builder.append( ReportUtils.ExportObjectToCSV(true, general.getTotalIn(), AccountTotal.class) );
		builder.append(GeneralConstants.CSV_EOF);
		builder.append("DIFERENCIA").append(GeneralConstants.CSV_EOF);
		builder.append( ReportUtils.ExportObjectToCSV(true, general.getTotals(), Totals.class) );
		return builder.toString();
	}
	
	public List<AccountOutput> getOutputs(String startDate, String endDate){
		return outService.getOutputs(startDate, endDate);
	}
	
	public List<AccountInput> getInputs(String startDate, String endDate){
		return inService.getInputs(startDate, endDate);
	}
	
	public List<Warehouse> getWarehouse(Sort sort) {
		List<ProductInventory> productsInventory = invetory.findWarehouse(sort);
		final List<Warehouse> warehouses = new ArrayList<>();
		productsInventory.forEach(pi -> {
			Warehouse wh = new Warehouse();
			wh.setNameLarge(pi.getNameLarge());
			wh.setPercentage(pi.getPercentage());
			wh.setAverageCost(pi.getInventory().getAverageCost());
			wh.setPrice(pi.getInventory().getPrice());
			wh.setQuantity(pi.getInventory().getQuantity());
			wh.setSku(pi.getSku());
			warehouses.add(wh);
		});
		return warehouses;
	}
	
	public String getWarehouseInCSV(Sort sort) {
		return ReportUtils.ExportListToCSV(true, getWarehouse(sort), Warehouse.class);
	}

}
