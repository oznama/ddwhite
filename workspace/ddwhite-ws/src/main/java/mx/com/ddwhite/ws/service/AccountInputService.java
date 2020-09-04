package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.ProductDto;
import mx.com.ddwhite.ws.dto.SaleDetailDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.reports.AccountInput;
import mx.com.ddwhite.ws.repository.ClientRepository;

@Service
public class AccountInputService {
	
	@Autowired
	private SaleService saleService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ClientRepository clientRep;
	@Autowired
	private UserService userService;
	
	public List<AccountInput> getInputs(String startDate, String endDate){
		final List<AccountInput> lstAccIn = new ArrayList<>();
		// Loading sales
		List<SaleDto> salesDto = saleService.findByRange(startDate, endDate);
		salesDto.forEach(saleDto -> {
			lstAccIn.addAll(getAccountInput(saleDto));
		});
		return lstAccIn;
	}

	private List<AccountInput> getAccountInput(SaleDto saleDto) {
		final List<AccountInput> lstAccIn = new ArrayList<>();
		final Client client;
		if( saleDto.getClientId() != null ) client = clientRep.findById(saleDto.getClientId()).get();
		else client = null;
		final UserDto userDto = userService.findById(saleDto.getUserId());
		saleDto.getDetail().forEach(detailDto -> {
			lstAccIn.add( getAccountInput(saleDto, detailDto, client, userDto) );
		});
		return lstAccIn;
	}

	private AccountInput getAccountInput(SaleDto saleDto, SaleDetailDto detailDto, Client client, UserDto userDto) {
		ProductDto productDto = productService.findById(detailDto.getProductId());
		AccountInput ai = new AccountInput();
		ai.setSaleId(saleDto.getId());
		ai.setUser(userDto.getFullName().toUpperCase());
		if( client != null ) {
			StringBuffer buffer = new StringBuffer();
			if( client.getRfc() != null )
				buffer.append(client.getRfc().toUpperCase()).append(" - ");
			buffer.append(client.getName().toUpperCase()).append(" ");
			buffer.append(client.getMidleName().toUpperCase()).append(" ");
			buffer.append(client.getLastName().toUpperCase());
			ai.setClient(buffer.toString());	
		}
		ai.setProduct(productDto.getNameLarge());
		ai.setQuantity(detailDto.getQuantity());
		ai.setPrice(detailDto.getPrice());
		ai.setTotal( ai.getPrice().multiply(BigDecimal.valueOf(ai.getQuantity())).setScale(GeneralConstants.BIG_DECIMAL_ROUND,BigDecimal.ROUND_HALF_EVEN) );
		ai.setSubTotal( ai.getTotal().divide(GeneralConstants.TAX, GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN) );
		ai.setIva(ai.getTotal().subtract(ai.getSubTotal()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN));
		ai.setDate(saleDto.getDateCreated());
		return ai;
	}
	

}
