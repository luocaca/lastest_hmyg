package com.mrwujay.cascade.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;

public class JsonParserHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();

	public JsonParserHandler(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			provinceModel = new ProvinceModel();
			JSONObject jsonObject = array.getJSONObject(i);
			List<CityModel> cityList = new ArrayList<CityModel>();
			JSONArray jsonArray = jsonObject.getJSONArray("childs");
			for (int j = 0; j < jsonArray.length(); j++) {
				cityModel = new CityModel();
				JSONObject jsonObject2 = jsonArray.getJSONObject(j);
				List<DistrictModel> districtList = new ArrayList<DistrictModel>();
				JSONArray jsonArray2 = JsonGetInfo.getJsonArray(jsonObject2,
						"childs");
				if (jsonArray2.length() > 0) {
					for (int k = 0; k < jsonArray2.length(); k++) {
						districtModel = new DistrictModel();
						JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
						districtModel.setName(jsonObject3.getString("name"));
						districtModel.setCityCode(jsonObject3
								.getString("cityCode"));
						districtModel.setZipcode(jsonObject3
								.getString("cityCode"));
						districtList.add(districtModel);
					}
				} else {
					districtModel = new DistrictModel();
					districtModel.setName("");
					districtModel.setZipcode("");
					districtModel.setCityCode("");
					districtList.add(districtModel);
				}

				cityModel.setName(jsonObject2.getString("name"));
				cityModel.setCityCode(jsonObject2.getString("cityCode"));
				cityModel.setDistrictList(districtList);
				cityList.add(cityModel);
			}
			provinceModel.setCityCode(jsonObject.getString("cityCode"));
			provinceModel.setName(jsonObject.getString("name"));
			provinceModel.setCityList(cityList);
			provinceList.add(provinceModel);

		}

	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

}
