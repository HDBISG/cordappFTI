package com.vcc.camelone.eco.exchange.gateway.dto;


import java.io.Serializable;

public class NTPGWSubscriptionRequest  implements Serializable {

	// Static Attributes
	////////////////////
	private static final long serialVersionUID = 6940626182012405860L;
	
	// Attributes
	//////////////
	private String gwType;
	private String svcType;
	private String accnId;

	
	// Constructor
	//////////////
	public NTPGWSubscriptionRequest() {
		super();
	}
	
	// Override Methods
	///////////////////

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWSubscriptionRequest [gwType=" + gwType + ", svcType=" + svcType + ", accnId=" + accnId + "]";
	}

	// Properties
	/////////////

	/**
	 * @return the gwType
	 */
	public String getGwType() {
		return gwType;
	}

	/**
	 * @param gwType the gwType to set
	 */
	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	/**
	 * @return the svcType
	 */
	public String getSvcType() {
		return svcType;
	}

	/**
	 * @param svcType the svcType to set
	 */
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	/**
	 * @return the accnId
	 */
	public String getAccnId() {
		return accnId;
	}

	/**
	 * @param accnId the accnId to set
	 */
	public void setAccnId(String accnId) {
		this.accnId = accnId;
	}

}
