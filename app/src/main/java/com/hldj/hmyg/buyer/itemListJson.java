package com.hldj.hmyg.buyer;

public class itemListJson {
	String num;
	String receiptDate;
	String name;
	int count;
	@Override
	public String toString() {
		return "itemListJson [num=" + num + ", receiptDate=" + receiptDate
				+ ", name=" + name + ", count=" + count + "]";
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
