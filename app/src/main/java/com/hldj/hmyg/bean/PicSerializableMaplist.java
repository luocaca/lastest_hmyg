package com.hldj.hmyg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PicSerializableMaplist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Pic> maplist;
	// 定义子列表项List数据集合

	public ArrayList<Pic> getMaplist() {
		return maplist;
	}

	public void setMaplist(ArrayList<Pic> maplist) {
		this.maplist = maplist;
	}

}