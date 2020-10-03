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
import mx.com.ddwhite.ws.reports.Cashout;
import mx.com.ddwhite.ws.repository.ClientRepository;
import mx.com.ddwhite.ws.service.utils.AlignedEmun;
import mx.com.ddwhite.ws.service.utils.CustomPrintUtils;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class TicketPrintService {

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

	@Autowired
	private SaleService saleService;
	
	private final int COLUMN_20_SIZE = 20;
	private final int COLUMN_12_SIZE = 12;

	public void test() {
		System.out.println(printUtil.getPrinters());
		// print some stuff
		printUtil.printString(CustomPrintUtils.PRINTER,
				"\n12345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
	}

	public void ticket(SaleDto saleDto) {
		try {
			String content = buildTicket(saleDto);
			System.out.println(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reprint(Long saleId) {
		ticket(saleService.findById(saleId));
	}
	
	public void cashout(Cashout cashout, Long userId, String start, String end) {
		try {
			String content = buildCashout(cashout, userId, start, end);
			System.out.println(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String buildCashout(Cashout cashout, Long userId, String start, String end) {
		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("CORTE DE CAJA", AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK);
		
		UserDto userDto = userService.findById(userId);
		content.append(lineFormatted("Realizado por: " + userDto.getFullName().toUpperCase(), AlignedEmun.CENTERED));
		content.append(lineFormatted("En el turno", AlignedEmun.CENTERED));
		content.append(lineFormatted("De: " + start, AlignedEmun.CENTERED));
		content.append(lineFormatted("A: " + end, AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		// Groups
		String hdGroup = buildLine("GRUPO", AlignedEmun.LEFT, COLUMN_20_SIZE);
		String hdTotal = buildLine("TOTAL", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		cashout.getDetail().forEach(detail -> {
			StringBuffer line = new StringBuffer(buildLine(detail.getGroup(), AlignedEmun.LEFT, COLUMN_20_SIZE));
			line.append(buildLine("$" + detail.getTotal(),AlignedEmun.RIGHT, COLUMN_12_SIZE));
			content.append(padding()).append(line.toString()).append(GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		// Payments
		hdGroup = buildLine("FORMA DE PAGO", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("MONTO", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		cashout.getPayment().forEach(payment -> {
			String strPayment = buildLine(payment.getPayment().toUpperCase(), AlignedEmun.LEFT, COLUMN_20_SIZE);
			String ammount = buildLine("$" + payment.getAmount(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
			content.append(padding() + strPayment + ammount + GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		// Total
		hdGroup = buildLine("TOTAL M.N.", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("$" + cashout.getTotal().toString(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		hdGroup = buildLine("CAMBIO", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("$" + cashout.getTotalChange().toString(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		content.append(lineFormatted("Fecha imp: " + GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME_SHORT), AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("AUTORIZADO POR", AlignedEmun.CENTERED));
		return content.toString();
	}

	private String buildTicket(SaleDto saleDto) {

		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		CatalogReadDto companyData = catalogService.findByName(GeneralConstants.CATALOG_NAME_COMPANY);

		CatalogItemReadDto companyDataName = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_NAME)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataAddress = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_ADDRESS)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataPhone = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_PHONE)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataWebsite = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_WEBSITE)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataEmail = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_MAIL)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataBusinessName = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_BUSSINES_NAME)).findAny()
				.orElse(null);
		CatalogItemReadDto companyDataRFC = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_RFC)).findAny()
				.orElse(null);
		CatalogItemReadDto messageTicket = companyData.getItems().stream()
				.filter(data -> data.getName().equals(GeneralConstants.CATALOG_NAME_COMPANY_MESSAGE)).findAny()
				.orElse(null);

		// Header

		// TODO: Check img

		if (companyDataName != null)
			content.append(lineFormatted(companyDataName.getDescription().toUpperCase(), AlignedEmun.CENTERED));
		if (companyDataAddress != null)
			content.append(lineFormatted(companyDataAddress.getDescription().toUpperCase(), AlignedEmun.CENTERED));
		if (saleDto.getClientId() != null) {
			Client client = clientRepository.findById(saleDto.getClientId()).orElse(null);
			content.append(lineFormatted("CLIENTE: " + client.getName().toUpperCase() + " "
					+ client.getMidleName().toUpperCase() + " " + client.getLastName().toUpperCase(),
					AlignedEmun.CENTERED));
			content.append(lineFormatted("RFC: " + client.getRfc().toUpperCase(), AlignedEmun.CENTERED));
		}
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));

		// Articles
		int columnCantSize = 4;
		int columnProdSize = 12;
		int columnAmouSize = 6;
		// Columns
		String hdCant = buildLine("CANT", AlignedEmun.CENTERED, columnCantSize * 2);
		String hdDesc = buildLine("ARTICULO", AlignedEmun.CENTERED, columnProdSize);
		String hdPU = buildLine("P.U.", AlignedEmun.CENTERED, columnAmouSize);
		String hdTotal = buildLine("TOTAL", AlignedEmun.CENTERED, columnAmouSize);
		content.append(hdCant).append(hdDesc).append(hdPU).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));
		// Rows
		saleDto.getDetail().forEach(detail -> {
			StringBuffer line = new StringBuffer(buildLine(detail.getQuantity().toString(), AlignedEmun.LEFT, columnCantSize));
			line.append(buildLine(catalogService.findById(detail.getUnity()).getName().toUpperCase(), AlignedEmun.LEFT, columnCantSize));
			line.append(buildLine(productService.findById(detail.getProductId()).getNameShort().toUpperCase(),AlignedEmun.CENTERED, columnProdSize));
			line.append(buildLine("$" + detail.getPrice(), AlignedEmun.CENTERED, columnAmouSize));
			line.append(buildLine("$" + detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())),AlignedEmun.CENTERED, columnAmouSize));
			content.append(padding()).append(line.toString()).append(GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(), AlignedEmun.CENTERED));

		// Total
//		if (saleDto.getClientId() != null) {
//			content.append(lineFormatted("SUBTOTAL: $ " + saleDto.getSubTotal(), AlignedEmun.RIGHT));
//			content.append(lineFormatted("IVA: $ " + saleDto.getTax(), AlignedEmun.RIGHT));
//		}
		content.append(lineFormatted("TOTAL M.N. $ " + saleDto.getTotal(), AlignedEmun.RIGHT));
		// Payments
		saleDto.getPayments().forEach(payment -> {
			CatalogReadDto catPayment = catalogService.findById(payment.getPayment());
			String strPayment = buildLine(catPayment.getName().toUpperCase() + ":", AlignedEmun.LEFT, COLUMN_20_SIZE);
			String ammount = buildLine("$ " + payment.getAmount(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
			content.append(padding() + strPayment + ammount + GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted("CAMBIO $ " + saleDto.getChange(), AlignedEmun.RIGHT));

		// Footer
		UserDto userCreateSale = userService.findById(saleDto.getUserId());
		content.append(lineFormatted("Le atendio", AlignedEmun.CENTERED));
		content.append(lineFormatted(userCreateSale.getFullName().toUpperCase(), AlignedEmun.CENTERED));
		content.append(lineFormatted(saleDto.getDateCreated(), AlignedEmun.CENTERED));
		content.append(lineFormatted("*** No Venta: " + saleDto.getId() + " ***", AlignedEmun.CENTERED));
		if (messageTicket != null && messageTicket.getDescription() != null)
			content.append(lineFormatted(messageTicket.getDescription(), AlignedEmun.CENTERED));
		if (companyDataPhone != null && companyDataPhone.getDescription() != null)
			content.append(lineFormatted("Comentarios o sugerencias " + companyDataPhone.getDescription(),AlignedEmun.CENTERED));
		if (companyDataEmail != null && companyDataEmail.getDescription() != null)
			content.append(lineFormatted("o en " + companyDataEmail.getDescription(), AlignedEmun.CENTERED));
		if (companyDataWebsite != null && companyDataWebsite.getDescription() != null)
			content.append(lineFormatted("visite " + companyDataWebsite.getDescription(), AlignedEmun.CENTERED));
		if (companyDataRFC != null && companyDataRFC.getDescription() != null) {
			content.append(lineFormatted(companyDataBusinessName.getDescription().toUpperCase() + " "+ companyDataRFC.getDescription().toUpperCase(), AlignedEmun.CENTERED));
		}

		content.append(GeneralConstants.LINE_BREAK);
		return content.toString();
	}

	private String lineFormatted(String string, AlignedEmun aligned) {
		boolean multiLine = string.length() > CustomPrintUtils.CHARACTERS_BY_ROW;
		string = !multiLine ? buildLine(string, aligned, CustomPrintUtils.CHARACTERS_BY_ROW)
				: buildLine(string.split("\\s+"), aligned, CustomPrintUtils.CHARACTERS_BY_ROW);
		return padding() + string + GeneralConstants.LINE_BREAK;
	}

	private String buildLine(String string, AlignedEmun aligned, int size) {
		if (string.length() >= size)
			return string.substring(0, size);
		int diff = size - string.length();
		switch (aligned) {
		case RIGHT:
			string = tabulation(diff) + string;
			break;
		case CENTERED:
			BigDecimal middle = BigDecimal.valueOf(diff / 2);
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
		for (String string : strings) {
			if ((strAux + string).length() <= CustomPrintUtils.CHARACTERS_BY_ROW) {
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
		for (int i = 0; i < size; i++)
			tab += " ";
		return tab;
	}

	private String separator() {
		String sep = "";
		for (int i = 0; i < CustomPrintUtils.CHARACTERS_BY_ROW; i++)
			sep += "-";
		return sep;
	}

	private String padding() {
		String sep = "";
		for (int i = 0; i < CustomPrintUtils.PADDING; i++)
			sep += " ";
		return sep;
	}

}
