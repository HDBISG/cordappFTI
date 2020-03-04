package com.vcc.camelone.eco.exchange.gateway.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NTPGWSubscriptionResponse  implements Serializable {

	// Static Attributes
	////////////////////
	private static final long serialVersionUID = -6924221360668495085L;
	
	// Attributes
	//////////////	
	private String gwType;
	private String svcType;
	private String accnRefid;
	private String accnOwnerId;
	private String accnObjectName;
	private String accnObjectId;
	private String accnStatus;
	private List<GwNtpAccn> accnList;
	private NTPGWRespStatus status;
	
	// Constructor
	//////////////
	public NTPGWSubscriptionResponse() {
		super();
	}
	
	// Override Methods
	///////////////////
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NTPGWSubscriptionResponse [gwType=" + gwType + ", svcType=" + svcType + ", accnRefid=" + accnRefid
				+ ", accnOwnerId=" + accnOwnerId + ", accnObjectName=" + accnObjectName + ", accnObjectId="
				+ accnObjectId + ", accnStatus=" + accnStatus + ", accnList=" + accnList + ", status=" + status + "]";
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

	public String getAccnRefid() {
		return accnRefid;
	}

	public void setAccnRefid(String accnRefid) {
		this.accnRefid = accnRefid;
	}

	public String getAccnOwnerId() {
		return accnOwnerId;
	}

	public void setAccnOwnerId(String accnOwnerId) {
		this.accnOwnerId = accnOwnerId;
	}

	public String getAccnObjectName() {
		return accnObjectName;
	}

	public void setAccnObjectName(String accnObjectName) {
		this.accnObjectName = accnObjectName;
	}

	public String getAccnObjectId() {
		return accnObjectId;
	}

	public void setAccnObjectId(String accnObjectId) {
		this.accnObjectId = accnObjectId;
	}

	public String getAccnStatus() {
		return accnStatus;
	}

	public void setAccnStatus(String accnStatus) {
		this.accnStatus = accnStatus;
	}

	public List<GwNtpAccn> getAccnList() {
		return accnList;
	}

	public void setAccnList(List<GwNtpAccn> accnList) {
		this.accnList = accnList;
	}

	public NTPGWRespStatus getStatus() {
		return status;
	}

	public void setStatus(NTPGWRespStatus status) {
		this.status = status;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GwNtpAccn {
		
		// Attributes
		////////////////
		private String accnGwtypid;
		private String accnSvcid;
		private String accnRefid;
		private String accnOwnerId;
		private String accnObjectName;
		private String accnObjectId;
		private String accnStatus;
		
		public String getAccnGwtypid() {
			return accnGwtypid;
		}
		public void setAccnGwtypid(String accnGwtypid) {
			this.accnGwtypid = accnGwtypid;
		}
		
		public String getAccnSvcid() {
			return accnSvcid;
		}
		
		public void setAccnSvcid(String accnSvcid) {
			this.accnSvcid = accnSvcid;
		}
		
		public String getAccnRefid() {
			return accnRefid;
		}
		public void setAccnRefid(String accnRefid) {
			this.accnRefid = accnRefid;
		}
		
		public String getAccnOwnerId() {
			return accnOwnerId;
		}
		
		public void setAccnOwnerId(String accnOwnerId) {
			this.accnOwnerId = accnOwnerId;
		}
		
		public String getAccnObjectName() {
			return accnObjectName;
		}
		
		public void setAccnObjectName(String accnObjectName) {
			this.accnObjectName = accnObjectName;
		}
		
		public String getAccnObjectId() {
			return accnObjectId;
		}
		
		public void setAccnObjectId(String accnObjectId) {
			this.accnObjectId = accnObjectId;
		}
		
		public String getAccnStatus() {
			return accnStatus;
		}
		
		public void setAccnStatus(String accnStatus) {
			this.accnStatus = accnStatus;
		}		
	}
	
}
