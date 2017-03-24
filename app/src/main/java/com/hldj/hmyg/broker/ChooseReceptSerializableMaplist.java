package com.hldj.hmyg.broker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseReceptSerializableMaplist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ReceiptListJson> maplist;
	// 定义子列表项List数据集合

	public ArrayList<ReceiptListJson> getMaplist() {
		return maplist;
	}

	public void setMaplist(ArrayList<ReceiptListJson> maplist) {
		this.maplist = maplist;
	}

}