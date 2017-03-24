package com.mrwujay.cascade.model;

import java.util.List;

public class CityModel {
	private String name;
	private String cityCode;
	private List<DistrictModel> districtList;
	
	public CityModel(String name, String cityCode,
			List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.cityCode = cityCode;
		this.districtList = districtList;
	}
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}


	public CityModel() {
		super();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
