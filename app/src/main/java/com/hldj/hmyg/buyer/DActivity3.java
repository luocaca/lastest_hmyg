package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Product;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.jock.adapter.ShopcartExpandableListViewAdapter;
import com.jock.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.jock.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.jock.entity.GroupInfo;
import com.jock.entity.ProductInfo;

public class DActivity3 extends Activity implements CheckInterface,
		ModifyCountInterface, OnClickListener {
	private ExpandableListView exListView;
	private CheckBox cb_check_all;
	private TextView tv_total_price;
	private TextView tv_delete;
	private TextView tv_go_to_pay;
	private Context context;

	private double totalPrice = 0.00;// 购买的商品总价
	private int totalCount = 0;// 购买的商品总数量

	private ShopcartExpandableListViewAdapter selva;
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();// 组元素数据列表
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();// 子元素数据列表
	boolean getdata = true; // 避免刷新多出数据

	private int pageSize = 20;
	private int pageIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_d3);

		initView();

	}

	private void initView() {
		context = this;
		exListView = (ExpandableListView) findViewById(R.id.exListView);
		cb_check_all = (CheckBox) findViewById(R.id.all_chekbox);
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		tv_delete = (TextView) findViewById(R.id.tv_delete);
		tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
		// virtualData();
		webData();
	}

	private void initEvents() {
		selva = new ShopcartExpandableListViewAdapter(groups, children, this);
		selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
		selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
		exListView.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++) {
			exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}

		cb_check_all.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		tv_go_to_pay.setOnClickListener(this);
	}

	/**
	 * 模拟数据<br>
	 * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
	 * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
	 */
	private void virtualData() {

		for (int i = 0; i < 6; i++) {

			groups.add(new GroupInfo(i + "", "第八号当铺" + (i + 1) + "号店"));

			List<ProductInfo> products = new ArrayList<ProductInfo>();
			for (int j = 0; j <= i; j++) {
			}
			children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
		}
		initEvents();

	}

	public void webData() {
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("loadItems", true + "");
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/buyer/validateApply/list",
				params, new AjaxCallBack<Object>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub

						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(DActivity3.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								JSONArray jsonArray = JsonGetInfo.getJsonArray(
										jsonObject2, "data");
								if (jsonArray.length() > 0) {
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										String cityName1 = JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName");
										String cityCode = JsonGetInfo
												.getJsonString(jsonObject3,
														"cityCode");
										Double totalPrice = JsonGetInfo
												.getJsonDouble(jsonObject3,
														"totalPrice");
										groups.add(new GroupInfo(cityName1,
												cityName1 + cityCode));
										List<ProductInfo> products = new ArrayList<ProductInfo>();
										JSONArray jsonArray_cartList = JsonGetInfo
												.getJsonArray(jsonObject3,
														"itemListJson");
										if (jsonArray_cartList.length() > 0) {

											List<Product> plist = new ArrayList<Product>();
											for (int j = 0; j < jsonArray_cartList
													.length(); j++) {
												JSONObject jsonObject4 = JsonGetInfo.getJSONObject(
														jsonArray_cartList
																.getJSONObject(j),
														"seedling");
												String show_type = "seedling_list";
												String id = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"id");
												String name = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"name");
												String imageUrl = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"largeImageUrl");
												String cityName = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"cityName");
												Double price = JsonGetInfo
														.getJsonDouble(
																jsonObject4,
																"seedlingPrice");
												int count = JsonGetInfo
														.getJsonInt(
																jsonObject4,
																"count");
												String unitTypeName = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"unitTypeName");
												Double dbh = JsonGetInfo
														.getJsonDouble(
																jsonObject4,
																"dbh");
												Double height = JsonGetInfo
														.getJsonDouble(
																jsonObject4,
																"height");
												Double crown = JsonGetInfo
														.getJsonDouble(
																jsonObject4,
																"crown");
												String fullName = JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ciCity"),
														"fullName");
												String ciCity_name = JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ciCity"),
														"name");
												String realName = JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"realName");
												String companyName = JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"companyName");
												String publicName = JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"publicName");
												String status = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"status");
												String statusName = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"statusName");
												String createDate = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"createDate");
												String closeDate = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"closeDate");
												String plantType = JsonGetInfo
														.getJsonString(
																jsonObject4,
																"plantType");

												boolean isSelfSupport = JsonGetInfo
														.getJsonBoolean(
																jsonObject4,
																"isSelfSupport");
												boolean freeValidatePrice = JsonGetInfo
														.getJsonBoolean(
																jsonObject4,
																"freeValidatePrice");
												boolean cashOnDelivery = JsonGetInfo
														.getJsonBoolean(
																jsonObject4,
																"cashOnDelivery");
												boolean freeDeliveryPrice = JsonGetInfo
														.getJsonBoolean(
																jsonObject4,
																"freeDeliveryPrice");
												boolean freeValidate = JsonGetInfo
														.getJsonBoolean(
																jsonObject4,
																"freeValidate");
												ProductInfo productInfo = new ProductInfo(
														imageUrl, name, price,
														count, status,
														createDate, statusName,
														id, imageUrl,
														plantType, dbh, height,
														crown, price,
														totalPrice, price,
														count, unitTypeName,
														"tradeType", false,
														false, isSelfSupport,
														freeValidatePrice,
														cashOnDelivery,
														freeDeliveryPrice,
														freeValidate);
												products.add(productInfo);
											}

											children.put(groups.get(i).getId(),
													products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
											initEvents();
										}

										pageIndex++;
									}

								}

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(DActivity3.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	public void onClick(View v) {
		AlertDialog alert;
		switch (v.getId()) {
		case R.id.all_chekbox:
			doCheckAll();
			break;
		case R.id.tv_go_to_pay:
			if (totalCount == 0) {
				Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.show();
			break;
		case R.id.tv_delete:
			if (totalCount == 0) {
				Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
				return;
			}
			alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("您确定要将这些商品从购物车中移除吗？");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doDelete();
						}
					});
			alert.show();
			break;
		}
	}

	/**
	 * 删除操作<br>
	 * 1.不要边遍历边删除，容易出现数组越界的情况<br>
	 * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
	 */
	protected void doDelete() {
		List<GroupInfo> toBeDeleteGroups = new ArrayList<GroupInfo>();// 待删除的组元素列表
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			if (group.isChoosed()) {
				toBeDeleteGroups.add(group);
			}
			List<ProductInfo> toBeDeleteProducts = new ArrayList<ProductInfo>();// 待删除的子元素列表
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				if (childs.get(j).isChoosed()) {
					toBeDeleteProducts.add(childs.get(j));
				}
			}
			childs.removeAll(toBeDeleteProducts);

		}

		groups.removeAll(toBeDeleteGroups);

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doIncrease(int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {

		ProductInfo product = (ProductInfo) selva.getChild(groupPosition,
				childPosition);
		int currentCount = product.getCount();
		currentCount++;
		product.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void doDecrease(int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {

		ProductInfo product = (ProductInfo) selva.getChild(groupPosition,
				childPosition);
		int currentCount = product.getCount();
		if (currentCount == 1)
			return;
		currentCount--;

		product.setCount(currentCount);
		((TextView) showCountView).setText(currentCount + "");

		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++) {
			childs.get(i).setChoosed(isChecked);
		}
		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkChild(int groupPosition, int childPosiTion,
			boolean isChecked) {
		boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++) {
			if (childs.get(i).isChoosed() != isChecked) {
				allChildSameState = false;
				break;
			}
		}
		if (allChildSameState) {
			group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
		} else {
			group.setChoosed(false);// 否则，组元素一律设置为未选中状态
		}

		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	private boolean isAllCheck() {

		for (GroupInfo group : groups) {
			if (!group.isChoosed())
				return false;

		}

		return true;
	}

	/** 全选与反选 */
	private void doCheckAll() {
		for (int i = 0; i < groups.size(); i++) {
			groups.get(i).setChoosed(cb_check_all.isChecked());
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				childs.get(j).setChoosed(cb_check_all.isChecked());
			}
		}
		selva.notifyDataSetChanged();
		// 计算
		calculate();
	}

	/**
	 * 统计操作<br>
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
	 * 3.给底部的textView进行数据填充
	 */
	private void calculate() {
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				ProductInfo product = childs.get(j);
				if (product.isChoosed()) {
					totalCount++;
					totalPrice += product.getPrice() * product.getCount();
				}
			}
		}
		tv_total_price.setText("￥" + totalPrice);
		tv_go_to_pay.setText("去支付(" + totalCount + ")");
	}
}
