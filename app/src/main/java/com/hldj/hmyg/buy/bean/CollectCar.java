package com.hldj.hmyg.buy.bean;

public class CollectCar {
	String img="";
	String title="";//title里面放所有item的json
	String time="";
	String money="";
	int id=0;
	String storage_save_id="";
	private boolean isShowChoose; // 是否显示
	private boolean isChoosed; // 商品是否在购物车中被选中
	String reason="";
	@Override
	public String toString() {
		return "CollectCar [id=" + id + ", storage_save_id=" + storage_save_id
				+ ", reason=" + reason + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((storage_save_id == null) ? 0 : storage_save_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectCar other = (CollectCar) obj;
		if (storage_save_id == null) {
			if (other.storage_save_id != null)
				return false;
		} else if (!storage_save_id.equals(other.storage_save_id))
			return false;
		return true;
	}
	public String getStorage_save_id() {
		return storage_save_id;
	}
	public void setStorage_save_id(String storage_save_id) {
		this.storage_save_id = storage_save_id;
	}
	public boolean isShowChoose() {
		return isShowChoose;
	}
	public void setShowChoose(boolean isShowChoose) {
		this.isShowChoose = isShowChoose;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	public CollectCar(String img, String title, String time, String money,
			int id, String storage_save_id, boolean isShowChoose,
			boolean isChoosed, String reason) {
		super();
		this.img = img;
		this.title = title;
		this.time = time;
		this.money = money;
		this.id = id;
		this.storage_save_id = storage_save_id;
		this.isShowChoose = isShowChoose;
		this.isChoosed = isChoosed;
		this.reason = reason;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setstorage_save_id(String storage_save_id) {
		this.storage_save_id = storage_save_id;
	}
}
