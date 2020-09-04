package mx.com.ddwhite.ws.reports;

import java.io.Serializable;
import java.util.List;

public class ReportGeneral implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7417728874639348435L;

	private AccountTotal totalOut;
	private List<AccountOutput> outputs;

	private AccountTotal totalIn;
	private List<AccountInput> inputs;

	public AccountTotal getTotalOut() {
		return totalOut;
	}

	public void setTotalOut(AccountTotal totalOut) {
		this.totalOut = totalOut;
	}

	public List<AccountOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<AccountOutput> outputs) {
		this.outputs = outputs;
	}

	public AccountTotal getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(AccountTotal totalIn) {
		this.totalIn = totalIn;
	}

	public List<AccountInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<AccountInput> inputs) {
		this.inputs = inputs;
	}

}
