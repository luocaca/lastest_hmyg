package com.hldj.hmyg.saler.purchaseconfirmlist;

import java.io.Serializable;

public class SellectForPurchase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String str_startDate = "";
	String str_endDate = "";
	long l_startDate = 0;
	long l_endDate = 0;
	public String getStr_startDate() {
		return str_startDate;
	}
	public void setStr_startDate(String str_startDate) {
		this.str_startDate = str_startDate;
	}
	public SellectForPurchase(String str_startDate, String str_endDate,
			long l_startDate, long l_endDate) {
		super();
		this.str_startDate = str_startDate;
		this.str_endDate = str_endDate;
		this.l_startDate = l_startDate;
		this.l_endDate = l_endDate;
	}
	public String getStr_endDate() {
		return str_endDate;
	}
	public void setStr_endDate(String str_endDate) {
		this.str_endDate = str_endDate;
	}
	public long getL_startDate() {
		return l_startDate;
	}
	public void setL_startDate(long l_startDate) {
		this.l_startDate = l_startDate;
	}
	public long getL_endDate() {
		return l_endDate;
	}
	public void setL_endDate(long l_endDate) {
		this.l_endDate = l_endDate;
	}
}
