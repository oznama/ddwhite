package mx.com.ddwhite.ws.reports;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.Utils;
import mx.com.ddwhite.ws.dto.ProductInventory;
import mx.com.ddwhite.ws.service.InventoryService;

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
		general.setTotalIn(getTotalIn(in));
		general.setTotalOut(getTotalOut(out));
		return general;
	}
	
	public List<AccountOutput> getOutputs(String startDate, String endDate){
		return outService.getOutputs(startDate, endDate);
	}
	
	public List<AccountInput> getInputs(String startDate, String endDate){
		return inService.getInputs(startDate, endDate);
	}
	
	private AccountTotal getTotalOut(List<AccountOutput> outs) {
		final AccountTotal at = new AccountTotal();
		outs.forEach( o -> {
			at.setQuantityTotal( at.getQuantityTotal() + o.getQuantity() );
			at.setAmountTotal( at.getAmountTotal().add(o.getCost()) );
			at.settTotal(at.gettTotal().add(o.getTotal()));
			at.setSbTotal(at.getSbTotal().add(o.getSubTotal()));
			at.setIvaTotal(at.getIvaTotal().add(o.getIva()));
		});
		return at;
	}
	
	private AccountTotal getTotalIn(List<AccountInput> ins) {
		final AccountTotal at = new AccountTotal();
		ins.forEach( i -> {
			at.setQuantityTotal( at.getQuantityTotal() + i.getQuantity() );
			at.setAmountTotal( at.getAmountTotal().add(i.getPrice()) );
			at.settTotal(at.gettTotal().add(i.getTotal()));
			at.setSbTotal(at.getSbTotal().add(i.getSubTotal()));
			at.setIvaTotal(at.getIvaTotal().add(i.getIva()));
		});
		return at;
	}
	
	public List<Warehouse> getWarehouse(Sort sort) {
		List<ProductInventory> productsInventory = invetory.findWarehouse(sort);
		final List<Warehouse> warehouses = new ArrayList<>();
		productsInventory.forEach(pi -> {
			Warehouse wh = new Warehouse();
			wh.setId(pi.getId());
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
		return Utils.ExportListToCSV(true, getWarehouse(sort), Warehouse.class);
	}

}
