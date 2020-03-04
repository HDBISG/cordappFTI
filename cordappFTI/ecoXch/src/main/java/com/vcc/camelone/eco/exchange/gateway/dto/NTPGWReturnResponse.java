package com.vcc.camelone.eco.exchange.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NTPGWReturnResponse  implements Serializable {
	
	// Static Attributes
	////////////////////
	private static final long serialVersionUID = -25492119856739628L;
	
	// Attributes
	//////////////	
	private String gwType;
	private String svcType;
	private String docType;
	private String accnId;
	private String docNo;
	private String docData;
	private String docName;
	private String docPath;
	private char docStatus;
	private NTPGWRespStatus status;
	
	// Constructor
	//////////////
	public NTPGWReturnResponse() {
		super();
	}
	
	// Override Methods
	///////////////////
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWReturnResponse [gwType=" + gwType + ", svcType=" + svcType + ", docType=" + docType + ", accnId="
				+ accnId + ", docNo=" + docNo + ", docData=" + docData + ", docName=" + docName + ", docPath=" + docPath
				+ ", docStatus=" + docStatus + ", status=" + status + "]";
	}


	
	// Properties
	/////////////
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

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getDocData() {
		return docData;
	}

	public void setDocData(String docData) {
		this.docData = docData;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public char getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(char docStatus) {
		this.docStatus = docStatus;
	}

	public NTPGWRespStatus getStatus() {
		return status;
	}

	public void setStatus(NTPGWRespStatus status) {
		this.status = status;
	}	
	
}
