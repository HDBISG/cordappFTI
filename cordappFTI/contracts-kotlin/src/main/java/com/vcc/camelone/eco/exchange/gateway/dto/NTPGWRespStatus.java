package com.vcc.camelone.eco.exchange.gateway.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NTPGWRespStatus  implements Serializable {

	// Static Attributes
	////////////////////
	private static final long serialVersionUID = 7804167615488778108L;

	// Attributes
	//////////////
	private String status;
	private String message;
	private String exception;
	private String[] results;
	private String[] errors;

	// Constructor
	//////////////
	public NTPGWRespStatus() {
		super();
	}

	// Override Methods
	///////////////////

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWRespStatus [status=" + status + ", message=" + message + ", exception=" + exception + ", results="
				+ Arrays.toString(results) + ", errors=" + errors + "]";
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String[] getResults() {
		return results;
	}

	public void setResults(String[] results) {
		this.results = results;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

}
