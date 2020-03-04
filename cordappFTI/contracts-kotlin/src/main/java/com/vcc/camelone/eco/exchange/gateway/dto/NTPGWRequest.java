package com.vcc.camelone.eco.exchange.gateway.dto;

import java.io.Serializable;

public class NTPGWRequest implements Serializable {
	
	// Static Attributes
	////////////////////

	private static final long serialVersionUID = 3527185838874929432L;
	
	// Attributes
	//////////////
	private String viewType;
	private String gwType;
	private String svcType;
	private String docType;
	private String accnId;
	private String permitNo;
	
	// Constructor
	//////////////
	public NTPGWRequest() {
		super();
	}
	
	// Override Methods
	///////////////////
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWRequest [viewType=" + viewType + ", gwType=" + gwType + ", svcType=" + svcType + ", docType="
				+ docType + ", accnId=" + accnId + "]";
	}
		
	// Properties
	/////////////
	
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getSvcType() {
		return svcType;
	}

	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getAccnId() {
		return accnId;
	}

	public void setAccnId(String accnId) {
		this.accnId = accnId;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}



}
