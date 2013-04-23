package com.example.budgettrip;

public class BudgetItems {

	private long itemId;
	private long planId;
	private double price;
	private String name;
	private String desc;
	
	public BudgetItems(long planid, double cost, String name, String desc) {
		this.price = cost;
		this.name = name;
		this.planId = planid;
		this.desc = desc;
	}
	
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}
	
}
