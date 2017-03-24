package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.GetValidatePriceCustomDialog;
import com.dyr.custom.PayCustomDialog;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.unionpay.UPPayAssistEx;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

public class CheckOutValidateActivity extends NeedSwipeBackActivity {

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private final String mMode = "00";
	// 定义父列表项List数据集合
	List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();

	BuyerValidateExpandableListAdapter BuyerValidateExpandableListAdapter;
	CheckBox id_cb_select_all;
	LinearLayout id_ll_normal_all_state;
	LinearLayout id_ll_editing_all_state;
	ExpandableListView expandableListView;
	RelativeLayout id_rl_cart_is_empty;
	RelativeLayout id_rl_foot;
	TextView id_tv_edit_all;
	private String acceptStatus = "";
	private int to_totalCount;
	private double to_totalPrice;
	private String id = "";
	private TextView id_tv_totalPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_validate_check);
		onRefresh();
		// initData();
		// initCartData();

		/*    *//**
		 * 第一个参数 应用程序接口 this 第二个父列List<?extends Map<String,Object>>集合
		 * 为父列提供数据 第三个参数 父列显示的组件资源文件 第四个参数 键值列表 父列Map字典的key 第五个要显示的父列组件id 第六个
		 * 子列的显示资源文件 第七个参数 键值列表的子列Map字典的key 第八个要显示子列的组件id
		 * 
		 * 第五个参数groupTo - The group views that should display column in the
		 * "groupFrom" parameter. These should all be TextViews. The first N
		 * views in this list are given the values of the first N columns in the
		 * groupFrom parameter.
		 */
		/*
		 * 
		 * SimpleExpandableListAdapter simpleExpandableListAdapter = new
		 * SimpleExpandableListAdapter( this, parentMapList,
		 * R.layout.parent_layout, new String[] { "parentName"}, new int[] {
		 * R.id.tv_title_parent}, childMapList_list, R.layout.child_layout, new
		 * String[] { "childName"}, new int[] { R.id.tv_items_child});
		 * expandableListView.setAdapter(simpleExpandableListAdapter);
		 */

	}

	public void initView() {
		expandableListView = (ExpandableListView) findViewById(R.id.id_elv_listview);
		BuyerValidateExpandableListAdapter = new BuyerValidateExpandableListAdapter(
				this, parentMapList, childMapList_list);
		expandableListView.setAdapter(BuyerValidateExpandableListAdapter);
		expandableListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(CheckOutValidateActivity.this,
								"click：" + position, Toast.LENGTH_SHORT).show();
					}
				});

		for (int i = 0; i < parentMapList.size(); i++) {
			expandableListView.expandGroup(i);
		}

		ImageView id_iv_back = (ImageView) findViewById(R.id.id_iv_back);
		id_iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(CheckOutValidateActivity.this, "click :back",
						Toast.LENGTH_SHORT).show();
			}
		});

		id_ll_normal_all_state = (LinearLayout) findViewById(R.id.id_ll_normal_all_state);
		id_ll_editing_all_state = (LinearLayout) findViewById(R.id.id_ll_editing_all_state);
		id_rl_cart_is_empty = (RelativeLayout) findViewById(R.id.id_rl_cart_is_empty);

		TextView id_tv_save_star_all = (TextView) findViewById(R.id.id_tv_save_star_all);
		id_tv_save_star_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(CheckOutValidateActivity.this, "收藏多选商品",
						Toast.LENGTH_SHORT).show();
			}
		});
		TextView id_tv_delete_all = (TextView) findViewById(R.id.id_tv_delete_all);
		id_tv_delete_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BuyerValidateExpandableListAdapter.removeGoods();
				// Toast.makeText(MainActivity.this, "删除多选商品",
				// Toast.LENGTH_SHORT).show();

			}
		});

		id_tv_edit_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof TextView) {
					TextView tv = (TextView) v;
					if (BuyerValidateExpandableListAdapter.EDITING.equals(tv
							.getText())) {
						BuyerValidateExpandableListAdapter
								.setupEditingAll(true);
						tv.setText(BuyerValidateExpandableListAdapter.FINISH_EDITING);
						changeFootShowDeleteView(true);// 这边类似的功能 后期待使用观察者模式
					} else {
						BuyerValidateExpandableListAdapter
								.setupEditingAll(false);
						tv.setText(BuyerValidateExpandableListAdapter.EDITING);
						changeFootShowDeleteView(false);// 这边类似的功能 后期待使用观察者模式
					}

				}
			}
		});

		id_cb_select_all = (CheckBox) findViewById(R.id.id_cb_select_all);
		/*
		 * 要么遍历判断再选择 id_cb_select_all.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // Toast.makeText(MainActivity.this,
		 * "all isChecked：" + isChecked, Toast.LENGTH_SHORT).show();
		 * BuyerValidateExpandableListAdapter.setupAllChecked(isChecked); } });
		 */
		id_cb_select_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof CheckBox) {
					CheckBox checkBox = (CheckBox) v;
					BuyerValidateExpandableListAdapter.setupAllChecked(checkBox
							.isChecked());
				}
			}
		});

		id_tv_totalPrice = (TextView) findViewById(R.id.id_tv_totalPrice);

		final TextView id_tv_totalCount_jiesuan = (TextView) findViewById(R.id.id_tv_totalCount_jiesuan);
		final TextView id_tv_pay = (TextView) findViewById(R.id.id_tv_pay);

		id_tv_totalPrice.setText(to_totalPrice + "");
		// id_tv_totalCount_jiesuan.setText(to_totalCount + "");
		id_tv_totalCount_jiesuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(BuyerValidateApplyActivity.this, "click：结算",
				// Toast.LENGTH_SHORT).show();
				// 线上付款
				new ActionSheetDialog(CheckOutValidateActivity.this)
						.builder()
						.setTitle("在线支付只支持个人账户付款，对公账户请到网页版操作，是否继续？")
						.setCancelable(false)
						.setCanceledOnTouchOutside(false)
						.addSheetItem("是", SheetItemColor.Blue,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										StringBuffer ids = new StringBuffer();
										for (int i = 0; i < parentMapList
												.size(); i++) {
											ids.append(parentMapList.get(i)
													.get("id").toString()
													+ ",");
										}
										if (ids.length() > 0) {
											ids.deleteCharAt(ids.length() - 1);
											if (!"".equals(acceptStatus)
													&& "unpay"
															.equals(acceptStatus)) {
												appConsumeToPay(ids.toString(),
														"VALIDATE", "");
											} else if (!"".equals(acceptStatus)
													&& "verifyed"
															.equals(acceptStatus)) {
												appConsumeToPay(ids.toString(),
														"ORDER", "");
											}

										}
									}
								}).show();

			}
		});
		id_tv_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Toast.makeText(BuyerValidateApplyActivity.this, "click：结算",
				// Toast.LENGTH_SHORT).show();
				// 线下付款
				getValidatePrice(to_totalCount, "", "");
			}
		});
		BuyerValidateExpandableListAdapter
				.setOnGoodsCheckedChangeListener(new BuyerValidateExpandableListAdapter.OnGoodsCheckedChangeListener() {
					@Override
					public void onGoodsCheckedChange(int totalCount,
							double totalPrice) {
						// id_tv_totalPrice.setText(String.format(
						// getString(R.string.total), totalPrice));
						// id_tv_totalCount_jiesuan.setText(String.format(
						// getString(R.string.jiesuan), totalCount));
					}
				});

		BuyerValidateExpandableListAdapter
				.setOnAllCheckedBoxNeedChangeListener(new BuyerValidateExpandableListAdapter.OnAllCheckedBoxNeedChangeListener() {
					@Override
					public void onCheckedBoxNeedChange(
							boolean allParentIsChecked) {
						id_cb_select_all.setChecked(allParentIsChecked);
					}
				});

		BuyerValidateExpandableListAdapter
				.setOnEditingTvChangeListener(new BuyerValidateExpandableListAdapter.OnEditingTvChangeListener() {
					@Override
					public void onEditingTvChange(boolean allIsEditing) {

						changeFootShowDeleteView(allIsEditing);// 这边类似的功能
																// 后期待使用观察者模式

					}
				});

		BuyerValidateExpandableListAdapter
				.setOnCheckHasGoodsListener(new BuyerValidateExpandableListAdapter.OnCheckHasGoodsListener() {
					@Override
					public void onCheckHasGoods(boolean isHasGoods) {
						setupViewsShow(isHasGoods);
					}
				});

		/** ====include进来方式可能会导致view覆盖listview的最后一个item 解决 */
		// 在onCreate方法中一般没办法直接调用view.getHeight方法来获取到控件的高度
		id_rl_foot = (RelativeLayout) findViewById(R.id.id_rl_foot);
		// int w = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// id_rl_foot.measure(w, h);
		// int r_width = id_rl_foot.getMeasuredWidth();
		// int r_height = id_rl_foot.getMeasuredHeight();
		// Log.i("MeasureSpec", "MeasureSpec r_width = " + r_width);
		// Log.i("MeasureSpec", "MeasureSpec r_height = " + r_height);
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		// int top = dp2px(this, 56);
		// lp.setMargins(0, top, 0, r_height);// 48
		//
		// expandableListView.setLayoutParams(lp);
		/** ========== */

		if (parentMapList != null && parentMapList.size() > 0) {
			setupViewsShow(true);
		} else {
			setupViewsShow(false);
		}
	}

	public void appConsumeToPay(String ids, String stype, String bizType) {

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", ids);
		params.put("stype", stype);
		params.put("bizType", bizType);
		finalHttp.post(GetServerUrl.getUrl() + "admin/appConsume/toPay",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							JSONObject data = JsonGetInfo.getJSONObject(
									jsonObject, "data");
							if (!"".equals(msg)) {
								Toast.makeText(CheckOutValidateActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								id = JsonGetInfo.getJsonString(data, "id");
								String tn = JsonGetInfo.getJsonString(data,
										"tn");
								UPPayAssistEx.startPay(
										CheckOutValidateActivity.this, null,
										null, tn, mMode);
							} else if ("6007".equals(code)) {

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
						Toast.makeText(CheckOutValidateActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void setupViewsShow(boolean isHasGoods) {
		if (isHasGoods) {
			expandableListView.setVisibility(View.VISIBLE);
			id_rl_cart_is_empty.setVisibility(View.GONE);
			id_rl_foot.setVisibility(View.VISIBLE);
			id_tv_edit_all.setVisibility(View.GONE);
			setAcceptStatus();
		} else {
			expandableListView.setVisibility(View.GONE);
			id_rl_cart_is_empty.setVisibility(View.VISIBLE);
			id_rl_foot.setVisibility(View.GONE);
			id_tv_edit_all.setVisibility(View.GONE);
			setAcceptStatus();
		}
	}

	public void getValidatePrice(final int num, final String str_itemData,
			final String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "common/getAccount", params,
				new AjaxCallBack<Object>() {

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
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								// 验苗

								JSONObject jsonObject3 = JsonGetInfo
										.getJSONObject(jsonObject, "data");
								JSONObject accountInfo = JsonGetInfo
										.getJSONObject(jsonObject3,
												"accountInfo");
								String accountName = JsonGetInfo.getJsonString(
										accountInfo, "accountName");
								String accountBank = JsonGetInfo.getJsonString(
										accountInfo, "accountBank");
								String accountNum = JsonGetInfo.getJsonString(
										accountInfo, "accountNum");
								double totalPrice = JsonGetInfo.getJsonDouble(
										accountInfo, "price");
								int count = num;
								// 未付款
								PayCustomDialog.Builder builder = new PayCustomDialog.Builder(
										CheckOutValidateActivity.this);
								builder.setTitle("线下付款");
								builder.setPrice(totalPrice + "");
								builder.setCount(count + "");
								builder.setAccountName(accountName);
								builder.setAccountBank(accountBank);
								builder.setAccountNum(accountNum);
								builder.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												setResult(1);
												finish();
												// 设置你的操作事项
											}
										});

								builder.setNegativeButton(
										"取消",
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});

								builder.create().show();

							} else if ("1".equals(code)
									&& jsonObject.isNull("data")) {

								GetValidatePriceCustomDialog.Builder builder = new GetValidatePriceCustomDialog.Builder(
										CheckOutValidateActivity.this);
								builder.setTitle("申请验苗");
								builder.setPrice("");
								builder.setCount("");
								builder.setAccountName("");
								builder.setAccountBank("");
								builder.setAccountNum("");
								builder.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												// 设置你的操作事项
											}
										});

								builder.setNegativeButton(
										"取消",
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});

								builder.create().show();

							} else if ("6007".equals(code)) {

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
						Toast.makeText(CheckOutValidateActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	public void changeFootShowDeleteView(boolean showDeleteView) {

		if (showDeleteView) {
			id_tv_edit_all
					.setText(BuyerValidateExpandableListAdapter.FINISH_EDITING);

			id_ll_normal_all_state.setVisibility(View.INVISIBLE);
			id_ll_editing_all_state.setVisibility(View.VISIBLE);
		} else {
			id_tv_edit_all.setText(BuyerValidateExpandableListAdapter.EDITING);

			id_ll_normal_all_state.setVisibility(View.VISIBLE);
			// id_ll_normal_all_state.setVisibility(View.VISIBLE);
			id_ll_editing_all_state.setVisibility(View.INVISIBLE);
		}
	}

	public int dp2px(Context context, float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void onRefresh() {
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		Bundle bundle = getIntent().getExtras();
		SerializableMaplist serializableMap = (SerializableMaplist) bundle
				.get("map");
		parentMapList = serializableMap.getMap();
		childMapList_list = serializableMap.getChildMapList();
		to_totalCount = bundle.getInt("to_totalCount");
		to_totalPrice = bundle.getDouble("to_totalPrice");
		acceptStatus = bundle.getString("acceptStatus");
		id_rl_foot = (RelativeLayout) findViewById(R.id.id_rl_foot);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		setAcceptStatus();
		// parentMapList.clear();
		// childMapList_list.clear();
		if (BuyerValidateExpandableListAdapter != null) {
			BuyerValidateExpandableListAdapter.notifyDataSetChanged();
			id_cb_select_all.setChecked(false);
			id_tv_edit_all.setText(BuyerValidateExpandableListAdapter.EDITING);
			id_ll_editing_all_state.setVisibility(View.INVISIBLE);
		}
		initView();
		if (BuyerValidateExpandableListAdapter != null
				&& parentMapList.size() == 0) {
			setupViewsShow(false);
		}
	}

	public void setAcceptStatus() {
		if ("unpay".equals(acceptStatus) || "ordered".equals(acceptStatus)) {
			id_tv_edit_all.setVisibility(View.INVISIBLE);
			id_rl_foot.setVisibility(View.VISIBLE);
		} else {
			id_tv_edit_all.setVisibility(View.INVISIBLE);
			id_rl_foot.setVisibility(View.VISIBLE);
		}
	}

	private void initData() {

		for (int i = 0; i < 15; i++) {
			// 提供父列表的数据
			Map<String, Object> parentMap = new HashMap<String, Object>();
			parentMap.put("parentName", "parentName" + i);
			if (i % 2 == 0) {
				parentMap.put("parentIcon", R.drawable.ic_launcher);
			} else {
				parentMap.put("parentIcon", R.drawable.ic_launcher);
			}
			parentMapList.add(parentMap);
			// 提供当前父列的子列数据
			List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < 10; j++) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put("childName", "parentName" + i + "下面的childName" + j);
				childMapList.add(childMap);
			}
			childMapList_list.add(childMapList);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			// 支付成功后，extra中如果存在result_data，取出校验
			// result_data结构见c）result_data参数说明
			if (data.hasExtra("result_data")) {
				String result = data.getExtras().getString("result_data");
				// { "sign" :
				// "X/6I//DjIjRvGa8WnKyPxFfrg1Yn0btJZT/5BwQFMqmGGAAKl3twtSc23DRvcQaGiWbPI0g51wFhYlTQWo67UbYygM7rjyELxIbGxzLdplmjpk5AoNSX5G8mualcHNVYwWinxyKuUyle03wt3p0rMwLOEpI3Eu1dZZZdkEKgs7aci/vsYuTlDo7ax+Xcglap+jMCphprhMRhOpo0Mf5rORaNsi/y8dI5XjNpPUgsuXE0NmDe+fENNZYgn90AT8bCyiGk3MiMxUVDplmxt4JkzQAnNlgu+jWnPNYXz5VHICugh1blZrDkRs67epdgWvv7gu9jxA+No7mHtwDFWewgTw==",
				// "data" :
				// "pay_result=success&tn=201606270929246665928&cert_id=68759585097"}
				try {
					JSONObject resultJson = new JSONObject(result);
					String sign = resultJson.getString("sign");
					String dataOrg = resultJson.getString("data");
					// 验签证书同后台验签证书
					// 此处的verify，商户需送去商户后台做验签
					boolean ret = verify(dataOrg, sign, mMode);
					if (ret) {
						// 验证通过后，显示支付结果
						msg = "支付成功！";
						setResult(1);
						finish();
					} else {
						// 验证不通过后的处理
						// 建议通过商户后台查询支付结果
						msg = "支付失败！";
					}
				} catch (JSONException e) {
				}
			} else {
				// 未收到签名信息
				// 建议通过商户后台查询支付结果
				msg = "支付成功！";
				setResult(1);
				finish();
			}
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("支付结果通知");
		// builder.setMessage(msg);
		// builder.setInverseBackgroundForced(true);
		// // builder.setCustomTitle();
		// builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
	}

	private boolean verify(String msg, String sign64, String mode) {
		// 此处的verify，商户需送去商户后台做验签

		return true;

	}
}
