package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.model.Expense;
import mx.com.ddwhite.ws.model.Provider;
import mx.com.ddwhite.ws.model.Purchase;
import mx.com.ddwhite.ws.reports.AccountOutput;
import mx.com.ddwhite.ws.repository.ExpenseRepository;
import mx.com.ddwhite.ws.repository.ProviderRepository;
import mx.com.ddwhite.ws.repository.PurchaseRepository;

@Service
public class AccountOutputService {
	
	@Autowired
	private PurchaseRepository purchaseRep;
	@Autowired
	private ProviderRepository providerRep;
	@Autowired
	private UserService userService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private ExpenseRepository expenseRep;
	
	public List<AccountOutput> getOutputs(String startDate, String endDate) {
		final List<AccountOutput> lstAccOut = new ArrayList<>();
		// Loading purchases
		List<Purchase> purchases = purchaseRep.findByDates(startDate, endDate);
		purchases.forEach( p -> {
			lstAccOut.add(getAccountOutput(p));
		});
		// Loading expenses
		List<Expense> expenses = expenseRep.findByDate(startDate, endDate);
		expenses.forEach(e -> {
			lstAccOut.add(getAccountOutput(e));
		});
		return lstAccOut;
	}
	
	private AccountOutput getAccountOutput(Purchase p) {
		Provider provider = providerRep.findById(p.getProviderId()).get();
		UserDto userDto = userService.findById(p.getUserId());
		CatalogReadDto catGroup = catalogService.findById( p.getProduct().getGroup() );
		AccountOutput ao = new AccountOutput();
		ao.setUser(userDto.getFullName().toUpperCase());
		ao.setProduct(p.getProduct().getNameLarge().toUpperCase());
		ao.setProvider(provider.getBussinesName().toUpperCase());
		ao.setGroup(catGroup.getName().toUpperCase());
		ao.setQuantity(p.getQuantity());
		ao.setCost(p.getCost());
		ao.setTotal( p.getCost().multiply(BigDecimal.valueOf(p.getQuantity())).setScale(GeneralConstants.BIG_DECIMAL_ROUND,BigDecimal.ROUND_HALF_EVEN) );
		ao.setSubTotal( ao.getTotal().divide(GeneralConstants.TAX, GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN) );
		ao.setIva(ao.getTotal().subtract(ao.getSubTotal()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN));
		ao.setDate(p.getDateCreated());
		return ao;
	}
	
	private AccountOutput getAccountOutput(Expense e) {
		UserDto userDto = userService.findById(e.getUserId());
		AccountOutput ao = new AccountOutput();
		ao.setUser(userDto.getFullName().toUpperCase());
		ao.setProduct(e.getDescription().toUpperCase());
		ao.setGroup(GeneralConstants.GROUP_EXPENSE);
		ao.setQuantity(1);
		ao.setCost(e.getAmount());
		ao.setTotal( e.getAmount() );
		ao.setSubTotal( e.getAmount().divide(GeneralConstants.TAX, GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN) );
		ao.setIva(ao.getTotal().subtract(ao.getSubTotal()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN));
		ao.setDate(e.getDateCreated());
		return ao;
	}

}
