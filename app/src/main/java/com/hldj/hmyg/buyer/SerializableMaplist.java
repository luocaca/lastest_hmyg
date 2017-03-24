package com.hldj.hmyg.buyer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hldj.hmyg.buy.bean.GoodsBean;
import com.hldj.hmyg.buy.bean.StoreBean;

public class SerializableMaplist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Map<String, Object>> maplist =new ArrayList<Map<String,Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList =new ArrayList<List<Map<String,Object>>>();

	public List<List<Map<String, Object>>> getChildMapList() {
		return childMapList;
	}

	public void setChildMapList(List<List<Map<String, Object>>> childMapList) {
		this.childMapList = childMapList;
	}

	public List<Map<String, Object>> getMap() {
		return maplist;
	}

	public void setMap(List<Map<String, Object>> maplist) {
		this.maplist = maplist;
	}

}