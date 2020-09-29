package mx.com.ddwhite.ws.service;


import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.CatalogItemReadDto;
import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.repository.ClientRepository;
import mx.com.ddwhite.ws.service.utils.AlignedEmun;
import mx.com.ddwhite.ws.service.utils.CustomPrintUtils;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class CustomPrintService {
	
	@Autowired
	private CustomPrintUtils printUtil;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	public void test() {
		System.out.println(printUtil.getPrinters());
		//print some stuff
		printUtil.printString(CustomPrintUtils.PRINTER, "\n12345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
	}
	
	public void ticket(SaleDto saleDto) {
		try {
			buildTicket(saleDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buildTicket(SaleDto saleDto) {
		
		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		CatalogReadDto companyData = catalogService.findByName(GeneralConstants.CATALOG_NAME_COMPANY);
		
		CatalogItemReadDto companyDataName = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_NAME)).findAny().orElse(null);
		CatalogItemReadDto companyDataAddress = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_ADDRESS)).findAny().orElse(null);
		CatalogItemReadDto companyDataPhone = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_PHONE)).findAny().orElse(null);
		CatalogItemReadDto companyDataWebsite = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_WEBSITE)).findAny().orElse(null);
		CatalogItemReadDto companyDataEmail = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_MAIL)).findAny().orElse(null);
		CatalogItemReadDto companyDataBusinessName = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_BUSSINES_NAME)).findAny().orElse(null);
		CatalogItemReadDto companyDataRFC = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_RFC)).findAny().orElse(null);
		CatalogItemReadDto messageTicket = companyData.getItems().stream().filter( data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_MESSAGE)).findAny().orElse(null);
		
		// Header
		
		// TODO: Check img
		
		if(companyDataName != null) content.append(lineFormatted(companyDataName.getDescription(), AlignedEmun.CENTERED));
		if(companyDataAddress != null) content.append(lineFormatted(companyDataAddress.getDescription(), AlignedEmun.CENTERED));
		if( saleDto.getClientId() != null ) {
			Client client = clientRepository.findById(saleDto.getClientId()).orElse(null);
			content.append(lineFormatted("CLIENTE: " + client.getName() + " " + client.getMidleName() + " " + client.getLastName(), AlignedEmun.CENTERED));
			content.append(lineFormatted("RFC: " + client.getRfc(), AlignedEmun.CENTERED));
		}
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		
		// Articles
		// Columns
		content.append(lineFormatted("  CANT   ARTICULO  P.U. TOTAL ", AlignedEmun.LEFT));
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		// Rows
		saleDto.getDetail().forEach( detail -> {
			StringBuffer line = new StringBuffer(buildLine(detail.getQuantity().toString(), AlignedEmun.LEFT, 4));
			line.append(buildLine(catalogService.findById(detail.getUnity()).getName().substring(0, 3),AlignedEmun.LEFT, 4));
			line.append(buildLine(productService.findById(detail.getProductId()).getNameShort(), AlignedEmun.CENTERED, 10));
			line.append(buildLine("$" + detail.getPrice(), AlignedEmun.CENTERED, 6));
			line.append(buildLine("$" + detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())), AlignedEmun.CENTERED, 6));
			content.append(padding() + line.toString() + GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		
		// Total
		if( saleDto.getClientId() != null ) {
			content.append(lineFormatted("SUBTOTAL: $ " + saleDto.getSubTotal(), AlignedEmun.RIGHT));
			content.append(lineFormatted("IVA: $ " + saleDto.getTax(), AlignedEmun.RIGHT));
		}
		content.append(lineFormatted("TOTAL M.N. $ " + saleDto.getTotal(), AlignedEmun.RIGHT));
		// Payments
		saleDto.getPayments().forEach( payment -> {
			CatalogReadDto catPayment = catalogService.findById(payment.getPayment());
			String strPayment = buildLine(catPayment.getName() + ":", AlignedEmun.LEFT, 20);
			String ammount = buildLine("$ " + payment.getAmount(), AlignedEmun.RIGHT, 10);
			content.append(padding() + strPayment + ammount + GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted("CAMBIO $ " + saleDto.getChange(), AlignedEmun.RIGHT));

		//Footer
		UserDto userCreateSale = userService.findById(saleDto.getUserId());
		content.append(lineFormatted("Le atendio", AlignedEmun.CENTERED));
		content.append(lineFormatted(userCreateSale.getFullName(), AlignedEmun.CENTERED));
		content.append(lineFormatted(GenericUtils.currentDateToString(CustomPrintUtils.FORMAT_DATE), AlignedEmun.CENTERED));
		content.append(lineFormatted("*** No Venta: " + saleDto.getId() + " ***", AlignedEmun.CENTERED));
		if(messageTicket != null && messageTicket.getDescription() != null) content.append(lineFormatted(messageTicket.getDescription(), AlignedEmun.CENTERED));
		if(companyDataPhone != null && companyDataPhone.getDescription() != null) content.append(lineFormatted("Comentarios o sugerencias " + companyDataPhone.getDescription(), AlignedEmun.CENTERED));
		if(companyDataEmail != null && companyDataEmail.getDescription() != null) content.append(lineFormatted("o en " + companyDataEmail.getDescription(), AlignedEmun.CENTERED));
		if(companyDataWebsite != null && companyDataWebsite.getDescription() != null) content.append(lineFormatted("visite " + companyDataWebsite.getDescription(), AlignedEmun.CENTERED));
		if(companyDataRFC != null && companyDataRFC.getDescription() != null) content.append(lineFormatted(companyDataBusinessName.getDescription() + " " + companyDataRFC.getDescription(), AlignedEmun.CENTERED));
		
		content.append(GeneralConstants.LINE_BREAK);
		
		System.out.println(content.toString());
		
//		printUtil.printImage(CustomPrintUtils.PRINTER, CustomPrintUtils.LOGO_DDWHITE);
		printUtil.printString(CustomPrintUtils.PRINTER, content.toString());
		printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
	}
	
	private String lineFormatted(String string, AlignedEmun aligned) {
		boolean multiLine = string.length() > CustomPrintUtils.CHARACTERS_BY_ROW;
		string = !multiLine ? 
				buildLine(string, aligned, CustomPrintUtils.CHARACTERS_BY_ROW) : 
				buildLine(string.split("\\s+"), aligned, CustomPrintUtils.CHARACTERS_BY_ROW);
		return padding() + string + GeneralConstants.LINE_BREAK;
	}
	
	private String buildLine(String string, AlignedEmun aligned, int size) {
		if( string.length() >= size ) return string.substring(0, size);
		int diff = size - string.length();
		switch(aligned) {
		case RIGHT:
			string = tabulation(diff) + string;
			break;
		case CENTERED:
			BigDecimal middle = BigDecimal.valueOf(diff/2);
			int roundUp = middle.setScale(0, RoundingMode.UP).intValue();
			int roundDown = middle.setScale(0, RoundingMode.DOWN).intValue();
			string = tabulation(roundDown) + string + tabulation(roundUp);
			break;
		default:
			string += tabulation(diff);
			break;
		}
		return string;
	}
	
	private String buildLine(String[] strings, AlignedEmun aligned, int size) {
		StringBuffer strAux = new StringBuffer();
		StringBuffer strBuilded = new StringBuffer();
		for(String string: strings) {
			if( (strAux + string).length() <= CustomPrintUtils.CHARACTERS_BY_ROW ) {
				strAux.append(string + " ");
			} else {
				strBuilded.append(buildLine(strAux.toString(), aligned, size));
				strBuilded.append(GeneralConstants.LINE_BREAK);
				strAux = new StringBuffer(string + " ");
			}
		}
		strBuilded.append(buildLine(strAux.toString(), aligned, size));
		strBuilded.append(GeneralConstants.LINE_BREAK);
		return strBuilded.toString();
	}
	
	private String tabulation(int size) {
		String tab = "";
		for(int i=0;i<size;i++)
			tab += " ";
		return tab;
	}
	
	private String separator() {
		String sep = "";
		for(int i = 0; i < CustomPrintUtils.CHARACTERS_BY_ROW; i++)
			sep += "-";
		return sep;
	}
	
	private String padding() {
		String sep = "";
		for(int i = 0; i < CustomPrintUtils.PADDING; i++)
			sep += " ";
		return sep;
	}

}
