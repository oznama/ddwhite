package mx.com.ddwhite.ws.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.service.utils.CustomPrintUtils;

@Service
public class CustomPrintService {
	
	@Autowired
	private CustomPrintUtils printUtil;
	
	public void test() {
		System.out.println(printUtil.getPrinters());
		//print some stuff
		printUtil.printString(GeneralConstants.THERMICAL_PRINTER, "\n12345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		printUtil.printBytes(GeneralConstants.THERMICAL_PRINTER, CustomPrintUtils.CUT_P);
	}
	
	public void ticket(SaleDto saleDto) {
		
		// TODO: Crear string para ticket con SaleDto
		
	}

}
