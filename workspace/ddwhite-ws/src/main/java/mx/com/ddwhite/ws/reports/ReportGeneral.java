package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.util.List;

public class ReportGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7417728874639348435L;

	private Totals totals;

	private AccountOutputTotal totalOut;
	private List<AccountOutput> outputs;

	private AccountInputTotal totalIn;
	private List<AccountInput> inputs;

	public ReportGeneral() {
		totals = new Totals();
	}

	public AccountOutputTotal getTotalOut() {
		return totalOut;
	}

	public void setTotalOut(AccountOutputTotal totalOut) {
		this.totalOut = totalOut;
	}

	public List<AccountOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<AccountOutput> outputs) {
		this.outputs = outputs;
	}

	public AccountInputTotal getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(AccountInputTotal totalIn) {
		this.totalIn = totalIn;
	}

	public List<AccountInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<AccountInput> inputs) {
		this.inputs = inputs;
	}

	public Totals getTotals() {
		return totals;
	}

	public void setTotals(Totals totals) {
		this.totals = totals;
	}

}
