package com.hldj.hmyg.bean;


public class saveForCart {

	String seedlingId;
	int  count;
	String  tradeType;
	public saveForCart(String seedlingId, int count, String tradeType) {
		super();
		this.seedlingId = seedlingId;
		this.count = count;
		this.tradeType = tradeType;
	}
}
