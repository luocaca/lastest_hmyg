package com.hldj.hmyg.broker;

public class itemsData {
	String id; // 装车项ID，修改时必须
	String deliveryId; // 发货单ID
	String orderId; // 订单ID
	String receiptId; // 发货单中收货要求ID
	String loadCount; // 装车数量
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getLoadCount() {
		return loadCount;
	}
	public void setLoadCount(String loadCount) {
		this.loadCount = loadCount;
	}

}
