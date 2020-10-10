package mx.com.ddwhite.ws.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.ddwhite.ws.constants.GeneralConstants;
import mx.com.ddwhite.ws.dto.ProductDto;
import mx.com.ddwhite.ws.dto.SaleDetailDto;
import mx.com.ddwhite.ws.dto.SaleDto;
import mx.com.ddwhite.ws.dto.SalePaymentDto;
import mx.com.ddwhite.ws.reports.Cashout;
import mx.com.ddwhite.ws.reports.CashoutDetail;
import mx.com.ddwhite.ws.reports.CashoutPayment;

@Service
public class CashoutService {

	@Autowired
	private SaleService saleService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CatalogService catalogService;

	public Cashout getCashout(String startDate, String endDate) {
		Cashout cashout = new Cashout();
		bindCashout(startDate, endDate, cashout);
		return cashout;
	}

	private void bindCashout(String startDate, String endDate, Cashout cashout) {
		List<SaleDto> salesDto = saleService.findByRange(startDate, endDate);
		salesDto.forEach(saleDto -> {
			cashout.setTotal(cashout.getTotal().add(saleDto.getTotal()));
			cashout.setTotalChange(cashout.getTotalChange().add(saleDto.getChange()));
			bindCashoutTotals(cashout, saleDto);
		});
	}

	private void bindCashoutTotals(Cashout cashout, SaleDto saleDto) {
		saleDto.getDetail().forEach(detail -> bindCashoutDetail(cashout, detail));
		saleDto.getPayments().forEach(payment -> bindCashoutPayment(cashout, payment));
	}

	private void bindCashoutPayment(Cashout cashout, SalePaymentDto paymentDto) {
		String payment = catalogService.findById(paymentDto.getPayment()).getName();
		CashoutPayment cp = new CashoutPayment(payment, paymentDto.getAmount());
		if (cashout.getPayment().contains(cp)) {
			int index = cashout.getPayment().indexOf(cp);
			CashoutPayment cpFinded = cashout.getPayment().get(index);
			cp.setAmount(cp.getAmount().add(cpFinded.getAmount()));
			cashout.getPayment().remove(cpFinded);
		}
		cashout.getPayment().add(cp);
	}

	private void bindCashoutDetail(Cashout cashout, SaleDetailDto detailDto) {
		ProductDto product = productService.findById(detailDto.getProductId());
		BigDecimal dtotal = detailDto.getPrice().multiply(BigDecimal.valueOf(detailDto.getQuantity()))
				.setScale(GeneralConstants.BIG_DECIMAL_ROUND, BigDecimal.ROUND_HALF_EVEN);
		CashoutDetail cd = new CashoutDetail(product.getGroupDesc(), dtotal);
		if (cashout.getDetail().contains(cd)) {
			int index = cashout.getDetail().indexOf(cd);
			CashoutDetail cdFinded = cashout.getDetail().get(index);
			cd.setTotal(cd.getTotal().add(cdFinded.getTotal()));
			cashout.getDetail().remove(cdFinded);
		}
		cashout.getDetail().add(cd);
	}

}
