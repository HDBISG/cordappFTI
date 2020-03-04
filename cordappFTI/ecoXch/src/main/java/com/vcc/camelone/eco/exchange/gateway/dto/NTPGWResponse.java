package com.vcc.camelone.eco.exchange.gateway.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NTPGWResponse  implements Serializable {
	
	// Static Attributes
	////////////////////
	private static final long serialVersionUID = -929936951459902614L;
	
	// Attributes
	//////////////
	private String viewType;
	private String gwType;
	private String svcType;
	private String docType;
	private String accnId;
	private String permitNo;
	private List<PermitLst> permitLst;
	private String docData;
	private String docName;
	private NTPGWRespStatus status;
	
	// Constructor
	//////////////
	public NTPGWResponse() {
		super();
	}
	
	// Override Methods
	///////////////////
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWResponse [viewType=" + viewType + ", gwType=" + gwType + ", svcType=" + svcType + ", docType="
				+ docType + ", accnId=" + accnId + ", permitNo=" + permitNo + ", permitLst=" + permitLst + ", docData="
				+ docData + ", docName=" + docName + ", status=" + status + "]";
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

	public List<PermitLst> getPermitLst() {
		return permitLst;
	}

	public void setPermitLst(List<PermitLst> permitLst) {
		this.permitLst = permitLst;
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

	public NTPGWRespStatus getStatus() {
		return status;
	}

	public void setStatus(NTPGWRespStatus status) {
		this.status = status;
	}	

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PermitLst {
			
		// Attributes
		////////////////
		private String docrDocref;
		private String docrPermitNo;
		private String docrPermitValidityStartDt;
		private String docrPermitValidityEndDt;
		private String docrConsigneeName;
		private String docrDischargePort;

		// Properties
		//////////////////
		public String getDocrDocref() {
			return docrDocref;
		}

		public void setDocrDocref(String docrDocref) {
			this.docrDocref = docrDocref;
		}

		public String getDocrPermitNo() {
			return docrPermitNo;
		}

		public void setDocrPermitNo(String docrPermitNo) {
			this.docrPermitNo = docrPermitNo;
		}

		public String getDocrPermitValidityStartDt() {
			return docrPermitValidityStartDt;
		}

		public void setDocrPermitValidityStartDt(String docrPermitValidityStartDt) {
			this.docrPermitValidityStartDt = docrPermitValidityStartDt;
		}

		public String getDocrPermitValidityEndDt() {
			return docrPermitValidityEndDt;
		}

		public void setDocrPermitValidityEndDt(String docrPermitValidityEndDt) {
			this.docrPermitValidityEndDt = docrPermitValidityEndDt;
		}

		public String getDocrConsigneeName() {
			return docrConsigneeName;
		}

		public void setDocrConsigneeName(String docrConsigneeName) {
			this.docrConsigneeName = docrConsigneeName;
		}

		public String getDocrDischargePort() {
			return docrDischargePort;
		}

		public void setDocrDischargePort(String docrDischargePort) {
			this.docrDischargePort = docrDischargePort;
		}
	}
}
