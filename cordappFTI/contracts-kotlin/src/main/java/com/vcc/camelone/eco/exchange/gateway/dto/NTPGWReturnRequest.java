package com.vcc.camelone.eco.exchange.gateway.dto;


import java.io.Serializable;

public class NTPGWReturnRequest  implements Serializable {

	// Static Attributes
	////////////////////
	private static final long serialVersionUID = -1701339198253457546L;

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
	private String retReqType;
	private char docStatus;
	
	// Constructor
	//////////////
	public NTPGWReturnRequest() {
		super();
	}
	
	// Override Methods
	///////////////////	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWReturnRequest [gwType=" + gwType + ", svcType=" + svcType + ", docType=" + docType + ", accnId="
				+ accnId + ", docNo=" + docNo + ", docData=" + docData + ", docName=" + docName + ", docPath=" + docPath
				+ ", retReqType=" + retReqType + ", docStatus=" + docStatus + "]";
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

	/**
	 * @return the docData
	 */
	public String getDocData() {
		return docData;
	}

	/**
	 * @param docData the docData to set
	 */
	public void setDocData(String docData) {
		this.docData = docData;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * @return the docPath
	 */
	public String getDocPath() {
		return docPath;
	}

	/**
	 * @param docPath the docPath to set
	 */
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getRetReqType() {
		return retReqType;
	}

	public void setRetReqType(String retReqType) {
		this.retReqType = retReqType;
	}

	/**
	 * @return the docStatus
	 */
	public char getDocStatus() {
		return docStatus;
	}

	/**
	 * @param docStatus the docStatus to set
	 */
	public void setDocStatus(char docStatus) {
		this.docStatus = docStatus;
	}

}
