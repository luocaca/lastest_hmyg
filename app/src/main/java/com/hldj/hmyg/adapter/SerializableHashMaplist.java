package com.hldj.hmyg.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SerializableHashMaplist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<HashMap<String, Object>> maplist;
	private HashMap<String, Object> hashMap;

	// 定义子列表项List数据集合

	public ArrayList<HashMap<String, Object>> getMaplist() {
		return maplist;
	}

	public void setMaplist(ArrayList<HashMap<String, Object>> maplist) {
		this.maplist = maplist;
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}

	public ArrayList<HashMap<String, Object>> getMap() {
		return maplist;
	}

	public void setMap(ArrayList<HashMap<String, Object>> maplist) {
		this.maplist = maplist;
	}

}