package com.hldj.hmyg.buy.bean;


public class OrderPreSave {

	String validateItemId;
	String tradeType;
	int  count;
	public OrderPreSave(String validateItemId,String tradeType,int count) {
		super();
		this.validateItemId = validateItemId;
		this.tradeType = tradeType;
		this.count = count;
	}
}
