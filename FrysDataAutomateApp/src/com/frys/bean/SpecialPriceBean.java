package com.frys.bean;

public class SpecialPriceBean {

	private String prodId;
	private String startDate;
	private String endDate;
	private Double amount;
	private String typeOfSpecPrice;
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getTypeOfSpecPrice() {
		return typeOfSpecPrice;
	}
	public void setTypeOfSpecPrice(String typeOfSpecPrice) {
		this.typeOfSpecPrice = typeOfSpecPrice;
	}
	
	
}
