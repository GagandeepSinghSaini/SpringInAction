package com.frys.bean;

public class ProductTypeBean {

	private String prodId;
	private String prodType;
	private String TransportType;
	public String getTransportType() {
		return TransportType;
	}
	public void setTransportType(String transportType) {
		TransportType = transportType;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	
	
}
