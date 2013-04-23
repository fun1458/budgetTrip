package com.example.budgettrip;

import java.util.ArrayList;

public class Plans {
	private double limit;
	private String date;
	private long planId;
	private String name;
	
	public Plans(double limit, String name) {
		this.setLimit(limit);
		this.setName(name);
	}
	
	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
