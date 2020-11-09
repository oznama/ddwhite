package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.CatalogItemReadDto;
import mx.com.ddwhite.ws.dto.CatalogReadDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.dto.SessionDto;
import mx.com.ddwhite.ws.dto.UserDto;
import mx.com.ddwhite.ws.dto.WithdrawalDto;
import mx.com.ddwhite.ws.exception.ResourceNotFoundException;
import mx.com.ddwhite.ws.model.Client;
import mx.com.ddwhite.ws.model.Withdrawal;
import mx.com.ddwhite.ws.reports.AccountInputTotal;
import mx.com.ddwhite.ws.reports.AccountOutput;
import mx.com.ddwhite.ws.reports.Cashout;
import mx.com.ddwhite.ws.reports.Payment;
import mx.com.ddwhite.ws.repository.ClientRepository;
import mx.com.ddwhite.ws.service.utils.AlignedEmun;
import mx.com.ddwhite.ws.service.utils.CustomPrintUtils;
import mx.com.ddwhite.ws.service.utils.GenericUtils;

@Service
public class TicketPrintService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(TicketPrintService.class);

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
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private WithdrawalService withdrawalService;
	
	private final int COLUMN_20_SIZE = 20;
	private final int COLUMN_12_SIZE = 12;

	public void test() {
		LOGGER.debug(printUtil.getPrinters().toString());
		// print some stuff
		printUtil.printString(CustomPrintUtils.PRINTER,
				"\n12345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
	}

	public void ticket(SaleDto saleDto) {
		try {
			String content = buildTicket(saleDto);
			LOGGER.info(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reprint(Long saleId) throws ResourceNotFoundException  {
		ticket(saleService.findById(saleId));
	}
	
	public void cashout(Cashout cashout, Long userId, String start, String end, Long sessionId) {
		try {
			String content = buildCashout(cashout, userId, start, end, sessionId);
			LOGGER.info(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void withdrawall(Long userId, List<WithdrawalDto> denominations) {
		try {
			String content = buildWithdrawall(denominations, userId);
			LOGGER.info(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void general(Long userId, String start, String end, AccountInputTotal accountInputTotal, List<Payment> payments, List<AccountOutput> purchases, List<AccountOutput> expenses) {
		try {
			String content = buildGeneral(userId, start, end, accountInputTotal, payments, purchases, expenses);
			LOGGER.info(content);
			printUtil.printString(CustomPrintUtils.PRINTER, content);
			printUtil.printBytes(CustomPrintUtils.PRINTER, CustomPrintUtils.CUT_P);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String buildGeneral(Long userId, String start, String end, AccountInputTotal accountInputTotal,
			List<Payment> payments, List<AccountOutput> purchases, List<AccountOutput> expenses) {
		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("REPORTE GENERAL", AlignedEmun.CENTERED)).append(GeneralConstants.LINE_BREAK);
		
		UserDto userDto = userService.findById(userId);
		content.append(lineFormatted("Generado por: " + userDto.getFullName().toUpperCase(), AlignedEmun.CENTERED));
		content.append(lineFormatted("De " + start + " A " + end, AlignedEmun.CENTERED));
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		
		content.append(lineFormatted("EGRESOS", AlignedEmun.CENTERED)).append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("COMPRAS", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + purchases.stream()
			.map(p -> p.getCost().multiply(BigDecimal.valueOf(p.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN)
			, AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("GASTOS EXTRAS", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + expenses.stream()
			.map(p -> p.getCost().multiply(BigDecimal.valueOf(p.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN)
			, AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		
		content.append(lineFormatted("INGRESOS", AlignedEmun.CENTERED)).append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("TOTAL", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + accountInputTotal.gettTotal(), AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("SUBTOTAL", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + accountInputTotal.getSbTotal(), AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("IVA", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + accountInputTotal.getIvaTotal(), AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(buildLine("GANANCIA", AlignedEmun.LEFT, COLUMN_20_SIZE));
		content.append(buildLine("$" + accountInputTotal.getGanancia(), AlignedEmun.RIGHT, COLUMN_12_SIZE));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		
		int columnProdSize = 20;
		int columnAmouSize = 6;
		// Payments
		content.append(lineFormatted("FORMAS DE PAGO", AlignedEmun.CENTERED)).append(GeneralConstants.LINE_BREAK);
		String hdDesc = buildLine("FORMA", AlignedEmun.LEFT, columnProdSize);
		String hdAmmount = buildLine("MONTO", AlignedEmun.RIGHT, columnAmouSize);
		String hdComission = buildLine("COM", AlignedEmun.RIGHT, columnAmouSize);
		content.append(hdDesc).append(hdAmmount).append(hdComission).append(GeneralConstants.LINE_BREAK);
		payments.forEach(payment -> {
			String strPayment = buildLine(payment.getPayment().toUpperCase(), AlignedEmun.LEFT, columnProdSize);
			String ammount = buildLine("$" + payment.getAmount(), AlignedEmun.RIGHT, columnAmouSize);
			String comission = buildLine(payment.getComision() != null ? "$" + payment.getComision() : "", AlignedEmun.RIGHT, columnAmouSize);
			content.append(strPayment).append(ammount).append(comission).append(GeneralConstants.LINE_BREAK);
		});
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		return content.toString();
	}

	private String buildCashout(Cashout cashout, Long userId, String start, String end, Long sessionId) {
		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("CORTE DE CAJA", AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK);
		
		UserDto userDto = userService.findById(userId);
		content.append(lineFormatted("De: " + userDto.getFullName().toUpperCase(), AlignedEmun.CENTERED));
		content.append(lineFormatted("En el turno", AlignedEmun.CENTERED));
		content.append(lineFormatted("De: " + start, AlignedEmun.CENTERED));
		content.append(lineFormatted("A: " + end, AlignedEmun.CENTERED));
		content.append(lineFormatted("Abierta con: $" + cashout.getInitialAmount().setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN), AlignedEmun.CENTERED));
		content.append(lineFormatted("Cerrada con: $" + cashout.getCurrentAmount().setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN), AlignedEmun.CENTERED));
		BigDecimal diff = cashout.getCurrentAmount().subtract(cashout.getInitialAmount()).setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN);
		content.append(lineFormatted( diff.compareTo(BigDecimal.ZERO) == 0 ? "Efectivo balanceado" : (diff.compareTo(BigDecimal.ZERO) == 1 ? "Ganancia" : "Diferencia") +" de: $" + diff, AlignedEmun.CENTERED));	
		content.append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Groups
		String hdGroup = buildLine("GRUPO", AlignedEmun.LEFT, COLUMN_20_SIZE);
		String hdTotal = buildLine("TOTAL", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		cashout.getDetail().forEach(detail -> {
			StringBuffer line = new StringBuffer(buildLine(detail.getGroup().toUpperCase(), AlignedEmun.LEFT, COLUMN_20_SIZE));
			line.append(buildLine("$" + detail.getTotal(),AlignedEmun.RIGHT, COLUMN_12_SIZE));
			content.append(line.toString()).append(GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Payments
		hdGroup = buildLine("FORMA DE PAGO", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("MONTO", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		cashout.getPayment().forEach(payment -> {
			String strPayment = buildLine(payment.getPayment().toUpperCase(), AlignedEmun.LEFT, COLUMN_20_SIZE);
			String ammount = "$" + (payment.getPayment().toUpperCase().equals(GeneralConstants.CATALOG_PAYMENT_METHOD_CASH) ? payment.getAmount().subtract(cashout.getTotalChange()) : payment.getAmount());
			ammount = buildLine(ammount, AlignedEmun.RIGHT, COLUMN_12_SIZE);
			content.append(strPayment).append(ammount).append(GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Retiros
		List<Withdrawal> withdrawals = withdrawalService.findBySessionAndRange(sessionId, start, end);
		hdGroup = buildLine("RETIROS", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("MONTO", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		withdrawals.forEach(w -> {
			String strRange = buildLine(w.getDateCreated(), AlignedEmun.LEFT, COLUMN_20_SIZE);
			String strCreated = buildLine("$" + w.getAmmount(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
			content.append(strRange).append(strCreated).append(GeneralConstants.LINE_BREAK);
		});
		
		
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Total
		hdGroup = buildLine("TOTAL M.N.", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("$" + cashout.getTotal().setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN).toString(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
//		hdGroup = buildLine("CAMBIO", AlignedEmun.LEFT, COLUMN_20_SIZE);
//		hdTotal = buildLine("$" + cashout.getTotalChange().toString(), AlignedEmun.RIGHT, COLUMN_12_SIZE);
//		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		content.append(lineFormatted("Fecha imp: " + GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME_SHORT), AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("AUTORIZADO POR", AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		return content.toString();
	}
	
	private String buildWithdrawall(List<WithdrawalDto> denominations, Long userId) {
		StringBuffer content = new StringBuffer(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("RETIRO DE EFECTIVO", AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK);
		
		UserDto userDto = userService.findById(userId);
		SessionDto currentSession = sessionService.findCurrentSession(userId);
		String lastWithdrawal = withdrawalService.getLastDateWithdrawalBySession(currentSession.getId(), currentSession.getInDate());
		content.append(lineFormatted("De: " + userDto.getFullName().toUpperCase(), AlignedEmun.CENTERED));
		content.append(lineFormatted("En el turno", AlignedEmun.CENTERED));
		content.append(lineFormatted("De: " + lastWithdrawal, AlignedEmun.CENTERED));
		content.append(lineFormatted("A: " + GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME), AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Groups
		String hdGroup = buildLine("DENOMINACION", AlignedEmun.LEFT, COLUMN_20_SIZE);
		String hdTotal = buildLine("CANTIDAD", AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		denominations.forEach(denomination -> {
			if( denomination.getQuantity() > 0 ) {
				StringBuffer line = new StringBuffer(buildLine(denomination.getDenomination(), AlignedEmun.LEFT, COLUMN_20_SIZE));
				line.append(buildLine(String.valueOf(denomination.getQuantity()),AlignedEmun.RIGHT, COLUMN_12_SIZE));
				content.append(line.toString()).append(GeneralConstants.LINE_BREAK);
			}
		});
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Total
		BigDecimal total = denominations.stream().map(d -> d.getDenominationValue().multiply(BigDecimal.valueOf(d.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
		hdGroup = buildLine("TOTAL M.N.", AlignedEmun.LEFT, COLUMN_20_SIZE);
		hdTotal = buildLine("$" + total, AlignedEmun.RIGHT, COLUMN_12_SIZE);
		content.append(hdGroup).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		content.append(lineFormatted("Fecha imp: " + GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME_SHORT), AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted("AUTORIZADO POR", AlignedEmun.CENTERED));
		content.append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK).append(GeneralConstants.LINE_BREAK);
		
		withdrawalService.save(currentSession.getId(), denominations);
		
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
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));

		// Articles
		int columnCantSize = 4;
		int columnProdSize = 12;
		int columnAmouSize = 6;
		// Columns
		String hdCant = buildLine("CANT", AlignedEmun.CENTERED, columnCantSize * 2);
		String hdDesc = buildLine("ARTICULO", AlignedEmun.CENTERED, columnProdSize);
		String hdPU = buildLine("P.U.", AlignedEmun.RIGHT, columnAmouSize);
		String hdTotal = buildLine("TOTAL", AlignedEmun.RIGHT, columnAmouSize);
		content.append(hdCant).append(hdDesc).append(hdPU).append(hdTotal).append(GeneralConstants.LINE_BREAK);
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));
		// Rows
		saleDto.getDetail().forEach(detail -> {
			StringBuffer line = new StringBuffer(buildLine(detail.getQuantity().toString(), AlignedEmun.LEFT, columnCantSize+1));
			line.append(buildLine(catalogService.findById(detail.getUnity()).getName().toUpperCase(), AlignedEmun.LEFT, columnCantSize-1));
			line.append(buildLine(productService.findById(detail.getProductId()).getNameShort().toUpperCase(),AlignedEmun.CENTERED, columnProdSize));
			line.append(buildLine("$" + detail.getPrice().setScale(0,RoundingMode.UP), AlignedEmun.RIGHT, columnAmouSize));
			line.append(buildLine("$" + detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())).setScale(0,RoundingMode.UP),AlignedEmun.RIGHT, columnAmouSize));
			content.append(padding()).append(line.toString()).append(GeneralConstants.LINE_BREAK);
		});
		content.append(lineFormatted(separator(null), AlignedEmun.CENTERED));

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
			String ammount = buildLine("$ " + payment.getAmount().add(payment.getComision() != null ? payment.getComision() : BigDecimal.ZERO), AlignedEmun.RIGHT, COLUMN_12_SIZE);
			content.append(strPayment).append(ammount).append(GeneralConstants.LINE_BREAK);
		});
		if( saleDto.getChange() != null && saleDto.getChange().compareTo(BigDecimal.ZERO) > 0)
			content.append(lineFormatted("CAMBIO $ " + saleDto.getChange(), AlignedEmun.RIGHT));
		if (saleDto.getDiscount() != null) {
			BigDecimal totalReal = saleDto.getDetail().stream()
					.map(s -> s.getPrice().multiply(BigDecimal.valueOf(s.getQuantity())))
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN);
			content.append(lineFormatted("TOTAL SIN DESCUENTO $ " +  totalReal, AlignedEmun.RIGHT));
			content.append(lineFormatted("DESCUENTO " + saleDto.getDiscount() + "%", AlignedEmun.RIGHT));
			content.append(lineFormatted("USTED AHORRO $ " + totalReal.subtract(saleDto.getTotal()), AlignedEmun.RIGHT));
		}
		content.append(GeneralConstants.LINE_BREAK);
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

	private String separator(Character caracter) {
		if(caracter == null)
			caracter = '-';
		String sep = "";
		for (int i = 0; i < CustomPrintUtils.CHARACTERS_BY_ROW; i++)
			sep += caracter;
		return sep;
	}

	private String padding() {
		String sep = "";
		for (int i = 0; i < CustomPrintUtils.PADDING; i++)
			sep += " ";
		return sep;
	}

}
