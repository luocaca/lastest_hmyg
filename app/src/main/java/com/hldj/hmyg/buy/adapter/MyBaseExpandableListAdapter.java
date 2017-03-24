package com.hldj.hmyg.buy.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.GetValidatePriceCustomDialog;
import com.google.gson.Gson;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.preSaveForCart;
import com.hldj.hmyg.bean.saveForCart;
import com.hldj.hmyg.bean.validateApply;
import com.hldj.hmyg.buy.bean.GoodsBean;
import com.hldj.hmyg.buy.bean.StoreBean;
import com.hldj.hmyg.buyer.ArithUtil;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

/**
 * Created by louisgeek on 2016/4/27.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

	private static final String TAG = "MyBaseEtAdapter";
	List<Map<String, Object>> parentMapList;
	List<List<Map<String, Object>>> childMapList_list;
	Context context;
	int totalCount = 0;
	double totalPrice = 0.00;
	public static final String EDITING = "编辑";
	public static final String FINISH_EDITING = "完成";
	OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener;
	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;
	OnCheckHasGoodsListener onCheckHasGoodsListener;

	public void setOnCheckHasGoodsListener(
			OnCheckHasGoodsListener onCheckHasGoodsListener) {
		this.onCheckHasGoodsListener = onCheckHasGoodsListener;
	}

	public void setOnEditingTvChangeListener(
			OnEditingTvChangeListener onEditingTvChangeListener) {
		this.onEditingTvChangeListener = onEditingTvChangeListener;
	}

	OnEditingTvChangeListener onEditingTvChangeListener;
	private FinalBitmap fb;
	private Gson gson;

	public void setOnGoodsCheckedChangeListener(
			OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
		this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
	}

	public void setOnAllCheckedBoxNeedChangeListener(
			OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener) {
		this.onAllCheckedBoxNeedChangeListener = onAllCheckedBoxNeedChangeListener;
	}

	public MyBaseExpandableListAdapter(Context context,
			List<Map<String, Object>> parentMapList,
			List<List<Map<String, Object>>> childMapList_list) {
		this.parentMapList = parentMapList;
		this.childMapList_list = childMapList_list;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		gson = new Gson();
	}

	// 获取当前父item的数据数量
	@Override
	public int getGroupCount() {
		return parentMapList.size();
	}

	// 获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return childMapList_list.get(groupPosition).size();
	}

	// 获取当前父item的数据
	@Override
	public Object getGroup(int groupPosition) {
		return parentMapList.get(groupPosition);
	}

	// 得到子item需要关联的数据
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childMapList_list.get(groupPosition).get(childPosition);
	}

	// 得到父item的ID
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// return false;
		return true;
	}

	// 设置父item组件
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.d4_parent_layout, null);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.tv_title_parent = (TextView) convertView
					.findViewById(R.id.tv_title_parent);
			groupViewHolder.id_tv_edit = (TextView) convertView
					.findViewById(R.id.id_tv_edit);
			groupViewHolder.id_tv_apply = (TextView) convertView
					.findViewById(R.id.id_tv_apply);
			groupViewHolder.id_cb_select_parent = (CheckBox) convertView
					.findViewById(R.id.id_cb_select_parent);

			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}

		final StoreBean storeBean = (StoreBean) parentMapList
				.get(groupPosition).get("parentName");
		final String parentName = storeBean.getName();

		groupViewHolder.tv_title_parent.setText(parentName);
		groupViewHolder.tv_title_parent.setTextColor(storeBean.getText_color());
		groupViewHolder.id_tv_apply.setText(storeBean.getType());
		groupViewHolder.id_tv_apply.setTextColor(storeBean.getText_color());
		if (storeBean.isEditing()) {
			groupViewHolder.id_tv_edit.setText(FINISH_EDITING);
		} else {
			groupViewHolder.id_tv_edit.setText(EDITING);
		}
		groupViewHolder.id_tv_edit
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String text = "";
						TextView textView = null;
						if (v instanceof TextView) {
							textView = (TextView) v;
						}
						// Toast.makeText(context, "编辑：" + groupPosition,
						// Toast.LENGTH_SHORT).show();
						textView.setText(text);
						setupEditing(groupPosition);

						onEditingTvChangeListener
								.onEditingTvChange(dealAllEditingIsEditing());
					}
				});
		groupViewHolder.id_tv_apply
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						StringBuffer cartIds = new StringBuffer();
						int num = 0;
						ArrayList<validateApply> itemDatas = new ArrayList<validateApply>();
						ArrayList<preSaveForCart> preSaveForCarts = new ArrayList<preSaveForCart>();
						ArrayList<saveForCart> saveForCarts = new ArrayList<saveForCart>();
						List<Map<String, Object>> childMapList = childMapList_list
								.get(groupPosition);

						for (int j = childMapList.size() - 1; j >= 0; j--) {
							GoodsBean goodsBean = (GoodsBean) childMapList.get(
									j).get("childName");
							if (goodsBean.isChecked()) {
								cartIds.append(goodsBean.getId() + ",");
								num++;
								itemDatas.add(new validateApply(childMapList
										.get(j).get("id").toString(), goodsBean
										.getCount()));
								preSaveForCarts.add(new preSaveForCart(
										childMapList.get(j).get("id")
												.toString(), goodsBean
												.getCount()));
								saveForCarts.add(new saveForCart(childMapList
										.get(j).get("id").toString(), goodsBean
										.getCount(), "proxy"));

							}
						}
						if (cartIds.length() <= 0) {
							Toast.makeText(context, "请勾选需要操作的苗源", 1).show();
							return;
						}
						cartIds.deleteCharAt(cartIds.length() - 1);

						// 验苗和下单
						if ("下单".equals(storeBean.getType())) {
							preSave(num, gson.toJson(itemDatas),
									gson.toJson(preSaveForCarts),
									gson.toJson(saveForCarts),
									cartIds.toString());
						} else if ("验苗".equals(storeBean.getType())) {
							getValidatePrice(num, gson.toJson(itemDatas),
									cartIds.toString());

						}
					}
				});

		// 覆盖原有收起展开事件
		// convertView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(context, "店铺：" + parentName,
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		groupViewHolder.id_cb_select_parent.setChecked(storeBean.isChecked());
		final boolean nowBeanChecked = storeBean.isChecked();
		groupViewHolder.id_cb_select_parent
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setupOneParentAllChildChecked(!nowBeanChecked,
								groupPosition);
						// 控制总checkedbox 接口
						onAllCheckedBoxNeedChangeListener
								.onCheckedBoxNeedChange(dealAllParentIsChecked());
					}
				});
		// 遍历改变也会触发这个方法
		/*
		 * groupViewHolder.id_cb_select_parent.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) {
		 * 
		 * boolean allParentIsChecked=dealAllParentIsChecked(); Log.d(TAG,
		 * "=====onCheckedChanged:  =============="); Log.d(TAG,
		 * "=====onCheckedChanged: allParentIsChecked:"+allParentIsChecked);
		 * Log.d(TAG, "=====onCheckedChanged: groupPosition:"+groupPosition);
		 * Log.d(TAG, "=====onCheckedChanged: isChecked:" + isChecked);
		 * //Toast.makeText(context,
		 * "allParentIsChecked！！！ isChecked："+groupPosition+"=" + isChecked,
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * //控制总checkedbox 接口
		 * onAllCheckedBoxChangeListener.OnCheckedBoxChange(allParentIsChecked);
		 * 
		 * });
		 */

		/*
		 * ImageView
		 * iv_img_parent=(ImageView)convertView.findViewById(R.id.iv_img_parent
		 * ); int parentIcon =
		 * Integer.parseInt(parentMapList.get(groupPosition).
		 * get("parentIcon").toString());
		 * iv_img_parent.setImageResource(parentIcon);
		 */

		/*
		 * ImageView
		 * iv_img_parent_right=(ImageView)convertView.findViewById(R.id
		 * .iv_img_parent_right); //判断isExpanded就可以控制是按下还是关闭，同时更换图片
		 * if(isExpanded){ iv_img_parent_right.setImageResource(R.mipmap.
		 * channel_expandablelistview_top_icon); }else{
		 * iv_img_parent_right.setImageResource
		 * (R.mipmap.channel_expandablelistview_bottom_icon); }
		 */
		return convertView;
	}

	// 设置子item的组件
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.d4_child_layout2, null);

			childViewHolder = new ChildViewHolder();
			childViewHolder.tv_01 = (TextView) convertView
					.findViewById(R.id.tv_01);
			childViewHolder.tv_04 = (TextView) convertView
					.findViewById(R.id.tv_04);
			childViewHolder.tv_05 = (TextView) convertView
					.findViewById(R.id.tv_05);
			childViewHolder.tv_status_01 = (TextView) convertView
					.findViewById(R.id.tv_status_01);
			childViewHolder.tv_status_02 = (TextView) convertView
					.findViewById(R.id.tv_status_02);
			childViewHolder.tv_status_03 = (TextView) convertView
					.findViewById(R.id.tv_status_03);
			childViewHolder.tv_status_04 = (TextView) convertView
					.findViewById(R.id.tv_status_04);
			childViewHolder.tv_status_05 = (TextView) convertView
					.findViewById(R.id.tv_status_05);
			childViewHolder.id_iv_logo = (ImageView) convertView
					.findViewById(R.id.id_iv_logo);
			childViewHolder.tv_items_child = (TextView) convertView
					.findViewById(R.id.tv_items_child);
			childViewHolder.id_cb_select_child = (CheckBox) convertView
					.findViewById(R.id.id_cb_select_child);
			childViewHolder.id_ll_normal = (LinearLayout) convertView
					.findViewById(R.id.id_ll_normal);
			childViewHolder.id_ll_edtoring = (LinearLayout) convertView
					.findViewById(R.id.id_ll_edtoring);
			// 常规下：
			childViewHolder.tv_items_child_desc = (TextView) convertView
					.findViewById(R.id.tv_items_child_desc);
			childViewHolder.id_tv_price = (TextView) convertView
					.findViewById(R.id.id_tv_price);
			childViewHolder.id_tv_discount_price = (TextView) convertView
					.findViewById(R.id.id_tv_discount_price);
			childViewHolder.id_tv_count = (TextView) convertView
					.findViewById(R.id.id_tv_count);
			// 编辑下：
			childViewHolder.id_iv_reduce = (ImageView) convertView
					.findViewById(R.id.id_iv_reduce);
			childViewHolder.id_iv_add = (ImageView) convertView
					.findViewById(R.id.id_iv_add);
			childViewHolder.id_tv_count_now = (TextView) convertView
					.findViewById(R.id.id_tv_count_now);
			childViewHolder.id_tv_price_now = (TextView) convertView
					.findViewById(R.id.id_tv_price_now);
			childViewHolder.id_tv_des_now = (TextView) convertView
					.findViewById(R.id.id_tv_des_now);
			childViewHolder.id_tv_goods_star = (TextView) convertView
					.findViewById(R.id.id_tv_goods_star);
			childViewHolder.id_tv_goods_delete = (TextView) convertView
					.findViewById(R.id.id_tv_goods_delete);

			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		final GoodsBean goodsBean = (GoodsBean) childMapList_list
				.get(groupPosition).get(childPosition).get("childName");

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent toFlowerDetailActivity = new Intent(context,
						FlowerDetailActivity.class);
				toFlowerDetailActivity.putExtra("id",
						childMapList_list.get(groupPosition).get(childPosition)
								.get("id").toString());
				toFlowerDetailActivity.putExtra("show_type", childMapList_list
						.get(groupPosition).get(childPosition).get("show_type")
						.toString());
				context.startActivity(toFlowerDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

				// Toast.makeText(context, "商品：" + goodsBean.getName(),
				// Toast.LENGTH_SHORT).show();
			}
		});
		fb.display(
				childViewHolder.id_iv_logo,
				childMapList_list.get(groupPosition).get(childPosition)
						.get("imageUrl").toString());

		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("isSelfSupport").toString().contains("true")) {
			childViewHolder.tv_status_01.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("freeValidatePrice").toString().contains("true")) {
			childViewHolder.tv_status_02.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("cashOnDelivery").toString().contains("true")) {
			childViewHolder.tv_status_03.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("freeDeliveryPrice").toString().contains("true")) {
			childViewHolder.tv_status_04.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("freeValidate").toString().contains("true")) {
			childViewHolder.tv_status_05.setVisibility(View.VISIBLE);
		}

		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("plantType").toString().contains("planted")) {
			childViewHolder.tv_01
					.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (childMapList_list.get(groupPosition).get(childPosition)
				.get("plantType").toString().contains("transplant")) {
			childViewHolder.tv_01
					.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (childMapList_list.get(groupPosition).get(childPosition)
				.get("plantType").toString().contains("heelin")) {
			childViewHolder.tv_01
					.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (childMapList_list.get(groupPosition).get(childPosition)
				.get("plantType").toString().contains("container")) {
			childViewHolder.tv_01
					.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			childViewHolder.tv_01.setVisibility(View.GONE);
		}

		if (!"".equals(childMapList_list.get(groupPosition).get(childPosition)
				.get("companyName").toString())) {
			childViewHolder.tv_04.setText("发布人："
					+ childMapList_list.get(groupPosition).get(childPosition)
							.get("companyName").toString());
		} else if ("".equals(childMapList_list.get(groupPosition)
				.get(childPosition).get("companyName").toString())
				&& !"".equals(childMapList_list.get(groupPosition)
						.get(childPosition).get("publicName").toString())) {
			childViewHolder.tv_04.setText("发布人："
					+ childMapList_list.get(groupPosition).get(childPosition)
							.get("publicName").toString());
		} else if ("".equals(childMapList_list.get(groupPosition)
				.get(childPosition).get("companyName").toString())
				&& "".equals(childMapList_list.get(groupPosition)
						.get(childPosition).get("publicName").toString())) {
			childViewHolder.tv_04.setText("发布人："
					+ childMapList_list.get(groupPosition).get(childPosition)
							.get("realName").toString());
		}
		if( childMapList_list.get(groupPosition).get(childPosition)
				.get("closeDate").toString().length()>10){
			childViewHolder.tv_05.setText("下架日期："
					+ childMapList_list.get(groupPosition).get(childPosition)
					.get("closeDate").toString().substring(0, 10));
		}else {
			childViewHolder.tv_05.setText("下架日期："
					+ childMapList_list.get(groupPosition).get(childPosition)
					.get("closeDate").toString());
		}
		childViewHolder.tv_items_child.setText(goodsBean.getName());

		childViewHolder.id_tv_price.setText("");
		// childViewHolder.id_tv_price.setText(String.format(
		// context.getString(R.string.price), goodsBean.getPrice()));
		// childViewHolder.id_tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//数字划线效果
		// 原价是多少
		// childViewHolder.id_tv_price.getPaint().setFlags(
		// Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并抗锯齿
		childViewHolder.id_tv_discount_price
				.setText(String.format(context.getString(R.string.price),
						goodsBean.getDiscountPrice()));

		childViewHolder.tv_items_child_desc.setText(String.valueOf(goodsBean
				.getDesc()));

		childViewHolder.id_tv_count.setText(String.format(
				context.getString(R.string.good_count), goodsBean.getCount()));
		childViewHolder.id_tv_count_now.setText(String.valueOf(goodsBean
				.getCount()));

		double priceNow = ArithUtil.mul(goodsBean.getDiscountPrice(),
				goodsBean.getCount());// 小结
		childViewHolder.id_tv_price_now.setText(String.format(
				context.getString(R.string.price), priceNow));
		childViewHolder.id_tv_des_now.setText(goodsBean.getDesc());

		childViewHolder.id_cb_select_child.setChecked(goodsBean.isChecked());
		childViewHolder.id_cb_select_child
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final boolean nowBeanChecked = goodsBean.isChecked();
						// 更新数据
						goodsBean.setIsChecked(!nowBeanChecked);

						boolean isOneParentAllChildIsChecked = dealOneParentAllChildIsChecked(groupPosition);
						Log.d(TAG, "getChildView:onClick:  ==============");
						Log.d(TAG,
								"getChildView:onClick:isOneParentAllChildIsChecked:"
										+ isOneParentAllChildIsChecked);

						StoreBean storeBean = (StoreBean) parentMapList.get(
								groupPosition).get("parentName");
						storeBean.setIsChecked(isOneParentAllChildIsChecked);

						notifyDataSetChanged();
						// 控制总checkedbox 接口
						onAllCheckedBoxNeedChangeListener
								.onCheckedBoxNeedChange(dealAllParentIsChecked());
						dealPrice();
					}
				});

		if (goodsBean.isEditing()) {
			childViewHolder.id_ll_normal.setVisibility(View.GONE);
			childViewHolder.id_ll_edtoring.setVisibility(View.VISIBLE);
		} else {
			childViewHolder.id_ll_normal.setVisibility(View.VISIBLE);
			childViewHolder.id_ll_edtoring.setVisibility(View.GONE);
		}

		if ("1".equals(childMapList_list.get(groupPosition).get(childPosition)
				.get("clickTpye").toString())) {
			childViewHolder.id_tv_goods_star.setText("下单");
		} else {
			childViewHolder.id_tv_goods_star.setText("验苗");
		}
		childViewHolder.id_iv_add
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TextView textView= (TextView)
						// v.getRootView().findViewById(R.id.id_tv_num);
						dealAdd(goodsBean);
					}
				});
		childViewHolder.id_iv_reduce
				.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TextView textView= (TextView)
						// v.getRootView().findViewById(R.id.id_tv_num);
						dealReduce(goodsBean);
					}
				});
		childViewHolder.id_tv_goods_star
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						int num = 1;
						ArrayList<validateApply> itemDatas = new ArrayList<validateApply>();
						ArrayList<preSaveForCart> preSaveForCarts = new ArrayList<preSaveForCart>();
						ArrayList<saveForCart> saveForCarts = new ArrayList<saveForCart>();
						List<Map<String, Object>> childMapList = childMapList_list
								.get(groupPosition);
						for (int j = childMapList.size() - 1; j >= 0; j--) {
							if (goodsBean.isChecked()) {
								itemDatas.add(new validateApply(childMapList
										.get(j).get("id").toString(), goodsBean
										.getCount()));
								preSaveForCarts.add(new preSaveForCart(
										childMapList.get(j).get("id")
												.toString(), goodsBean
												.getCount()));
								saveForCarts.add(new saveForCart(childMapList
										.get(j).get("id").toString(), goodsBean
										.getCount(), "proxy"));
							}
						}

						if (itemDatas.size() <= 0) {
							Toast.makeText(context, "请勾选需要操作的苗源", 1).show();
							return;
						}

						if ("1".equals(childMapList_list.get(groupPosition)
								.get(childPosition).get("clickTpye").toString())) {
							// 下单
							preSave(num, gson.toJson(itemDatas),
									gson.toJson(preSaveForCarts),
									gson.toJson(saveForCarts),
									goodsBean.getId());
						} else {
							// 验苗
							// Toast.makeText(context, "验苗商品：" +
							// goodsBean.getName(),
							// Toast.LENGTH_SHORT).show();
							getValidatePrice(num, gson.toJson(itemDatas),
									goodsBean.getId());

						}

					}

				});
		childViewHolder.id_tv_goods_delete
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Toast.makeText(context, "删除商品：" +
						// goodsBean.getName(), Toast.LENGTH_SHORT).show();
						FinalHttp finalHttp = new FinalHttp();
						GetServerUrl.addHeaders(finalHttp,true);
						AjaxParams params = new AjaxParams();
						params.put("ids", goodsBean.getId());
						finalHttp.post(GetServerUrl.getUrl()
								+ "admin/cart/delete", params,
								new AjaxCallBack<Object>() {

									@Override
									public void onSuccess(Object t) {
										// TODO Auto-generated method stub
										try {
											JSONObject jsonObject = new JSONObject(
													t.toString());
											String code = JsonGetInfo
													.getJsonString(jsonObject,
															"code");
											String msg = JsonGetInfo
													.getJsonString(jsonObject,
															"msg");
											if (!"".equals(msg)) {
												Toast.makeText(context, msg,
														Toast.LENGTH_SHORT)
														.show();
											}
											if ("1".equals(code)) {
												removeOneGood(groupPosition,
														childPosition);
											} else if ("6007".equals(code)) {

											}

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										super.onSuccess(t);
									}

									@Override
									public void onFailure(Throwable t,
											int errorNo, String strMsg) {
										// TODO Auto-generated method stub
										Toast.makeText(context,
												R.string.error_net,
												Toast.LENGTH_SHORT).show();
										super.onFailure(t, errorNo, strMsg);
									}

								});

					}
				});
		/*
		 * id_cb_select_child.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { GoodsBean goodsBean = (GoodsBean)
		 * childMapList_list
		 * .get(groupPosition).get(childPosition).get("childName"); //更新数据
		 * goodsBean.setIsChecked(isChecked); boolean
		 * isAllChecked=dealOneParentAllChildIsChecked(groupPosition,isChecked);
		 * if (isAllChecked){ StoreBean storeBean= (StoreBean)
		 * parentMapList.get(groupPosition).get("parentName");
		 * storeBean.setIsChecked(true); }
		 * 
		 * Toast.makeText(context, "CHALID :isAllChecked" + isAllChecked,
		 * Toast.LENGTH_SHORT).show(); notifyDataSetChanged(); } });
		 */

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// return false;
		return true;
	}

	// 供全选按钮调用
	public void setupAllChecked(boolean isChecked) {
		Log.d(TAG, "setupAllChecked: ============");
		Log.d(TAG, "setupAllChecked: isChecked：" + isChecked);

		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			storeBean.setIsChecked(isChecked);

			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				goodsBean.setIsChecked(isChecked);
			}
		}
		notifyDataSetChanged();
		dealPrice();
	}

	private void setupOneParentAllChildChecked(boolean isChecked,
			int groupPosition) {
		Log.d(TAG, "setupOneParentAllChildChecked: ============");
		Log.d(TAG, "setupOneParentAllChildChecked: groupPosition:"
				+ groupPosition);
		Log.d(TAG, "setupOneParentAllChildChecked: isChecked：" + isChecked);
		StoreBean storeBean = (StoreBean) parentMapList.get(groupPosition).get(
				"parentName");
		storeBean.setIsChecked(isChecked);

		List<Map<String, Object>> childMapList = childMapList_list
				.get(groupPosition);
		for (int j = 0; j < childMapList.size(); j++) {
			GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
					"childName");
			goodsBean.setIsChecked(isChecked);
		}
		notifyDataSetChanged();
		dealPrice();
	}

	public boolean dealOneParentAllChildIsChecked(int groupPosition) {
		Log.d(TAG, "dealOneParentAllChildIsChecked: ============");
		Log.d(TAG, "dealOneParentAllChildIsChecked: groupPosition："
				+ groupPosition);
		// StoreBean storeBean= (StoreBean)
		// parentMapList.get(groupPosition).get("parentName");
		List<Map<String, Object>> childMapList = childMapList_list
				.get(groupPosition);
		for (int j = 0; j < childMapList.size(); j++) {
			GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
					"childName");
			if (!goodsBean.isChecked()) {
				return false;// 如果有一个没选择 就false
			}
		}
		return true;
	}

	public boolean dealAllParentIsChecked() {
		Log.d(TAG, "dealAllParentIsChecked: ============");
		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			if (!storeBean.isChecked()) {
				return false;// 如果有一个没选择 就false
			}
		}
		return true;
	}

	public void dealPrice() {
		// showList();
		totalCount = 0;
		totalPrice = 0.00;
		for (int i = 0; i < parentMapList.size(); i++) {
			// StoreBean storeBean= (StoreBean)
			// parentMapList.get(i).get("parentName");

			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				int count = goodsBean.getCount();
				double discountPrice = goodsBean.getDiscountPrice();
				if (goodsBean.isChecked()) {
					totalCount++;// 单品多数量只记1
					// totalPrice += discountPrice * count;
					totalPrice = ArithUtil.add(totalPrice,
							ArithUtil.mul(discountPrice, count));
				}

			}
		}
		// 计算回调
		onGoodsCheckedChangeListener.onGoodsCheckedChange(totalCount,
				totalPrice);
	}

	// 供总编辑按钮调用
	public void setupEditingAll(boolean isEditingAll) {
		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			storeBean.setIsEditing(isEditingAll);

			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				goodsBean.setIsEditing(isEditingAll);
			}
		}
		notifyDataSetChanged();
	}

	public boolean dealAllEditingIsEditing() {
		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			if (storeBean.isEditing()) {// !!!
				return true;// 如果有一个是编辑状态 就true
			}
		}
		return false;
	}

	public void setupEditing(int groupPosition) {
		StoreBean storeBean = (StoreBean) parentMapList.get(groupPosition).get(
				"parentName");
		boolean isEditing = !storeBean.isEditing();
		storeBean.setIsEditing(isEditing);
		List<Map<String, Object>> childMapList = childMapList_list
				.get(groupPosition);
		for (int j = 0; j < childMapList.size(); j++) {
			GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
					"childName");
			goodsBean.setIsEditing(isEditing);
		}
		notifyDataSetChanged();
	}

	public void dealAdd(GoodsBean goodsBean) {
		int count = goodsBean.getCount();
		count++;
		goodsBean.setCount(count);
		// textView.setText(String.valueOf(count));
		notifyDataSetChanged();
		dealPrice();
	}

	public void dealReduce(GoodsBean goodsBean) {
		int count = goodsBean.getCount();
		if (count == 1) {
			return;
		}
		count--;
		goodsBean.setCount(count);
		// textView.setText(String.valueOf(count));
		notifyDataSetChanged();
		dealPrice();
	}

	public void removeOneGood(int groupPosition, int childPosition) {
		// StoreBean storeBean = (StoreBean)
		// parentMapList.get(groupPosition).get("parentName");
		List<Map<String, Object>> childMapList = childMapList_list
				.get(groupPosition);
		// GoodsBean goodsBean = (GoodsBean)
		// childMapList.get(childPosition).get("childName");
		childMapList.remove(childPosition);

		// 通过子项
		if (childMapList != null && childMapList.size() > 0) {

		} else {
			parentMapList.remove(groupPosition);
			childMapList_list.remove(groupPosition);// ！！！！因为parentMapList和childMapList_list是pos关联的
													// 得保持一致
		}
		if (parentMapList != null && parentMapList.size() > 0) {
			onCheckHasGoodsListener.onCheckHasGoods(true);//
		} else {
			onCheckHasGoodsListener.onCheckHasGoods(false);//
		}
		notifyDataSetChanged();
		dealPrice();
	}

	public void removeGoods() {
		/*
		 * for (int i = 0; i <parentMapList.size(); i++) { StoreBean storeBean=
		 * (StoreBean) parentMapList.get(i).get("parentName");
		 * 
		 * List<Map<String, Object>> childMapList = childMapList_list.get(i);
		 * for (int j = 0; j < childMapList.size(); j++) { GoodsBean goodsBean =
		 * (GoodsBean) childMapList.get(j).get("childName"); Log.d(TAG,
		 * "removeGoods:============goodsBean:" + goodsBean.isChecked()); if
		 * (goodsBean.isChecked()) { childMapList.remove(j); j--;//!!!!!!!!!!
		 * List remove方法比较特殊 每移除一个元素以后再把pos移回来 } } }
		 */
		StringBuffer sbf = new StringBuffer();
		for (int i = parentMapList.size() - 1; i >= 0; i--) {
			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = childMapList.size() - 1; j >= 0; j--) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				if (goodsBean.isChecked()) {
					sbf.append(goodsBean.getId() + ",");
				}

			}
		}

		sbf.deleteCharAt(sbf.length() - 1);
		// 删除
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", sbf.toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/cart/delete", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {
								Sel();
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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

		// Sel();
	}

	public void Sel() {
		for (int i = parentMapList.size() - 1; i >= 0; i--) {// 倒过来遍历 remove
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			if (storeBean.isChecked()) {
				parentMapList.remove(i);
				childMapList_list.remove(i);
			} else {
				List<Map<String, Object>> childMapList = childMapList_list
						.get(i);
				for (int j = childMapList.size() - 1; j >= 0; j--) {// 倒过来遍历
																	// remove
					GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
							"childName");
					if (goodsBean.isChecked()) {
						childMapList.remove(j);
					}
				}
			}

		}
		// showList("begin###############");
		/**
		 * 1.不要边遍历边删除，容易出现数组越界的情况<br>
		 * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
		 */
		// 还是有问题
		/*
		 * List<Map<String, Object>> needRemoreParentMapList = new
		 * ArrayList<>();// 待删除的组元素列表 List<List<Map<String, Object>>>
		 * needRemoreChildMapList_List = new ArrayList<>();// 待删除的 最大的
		 * 
		 * for (int i = 0; i < parentMapList.size(); i++) { StoreBean storeBean
		 * = (StoreBean) parentMapList.get(i).get("parentName");
		 * 
		 * if(storeBean.isChecked()){
		 * needRemoreParentMapList.add(parentMapList.get(i));
		 * needRemoreChildMapList_List.add(childMapList_list.get(i));//！！！！
		 * 因为parentMapList和childMapList_list是pos关联的 得保持一致 } // List<Map<String,
		 * Object>> childMapList = childMapList_list.get(i);//最大的
		 * 
		 * List<Map<String, Object>> needRemoreChildMapList = new
		 * ArrayList<>();// 待删除的子元素列表
		 * 
		 * for (int j = 0; j < childMapList.size(); j++) { GoodsBean goodsBean =
		 * (GoodsBean) childMapList.get(j).get("childName"); if
		 * (goodsBean.isChecked()) {
		 * needRemoreChildMapList.add(childMapList.get(j)); } }
		 * 
		 * childMapList.removeAll(needRemoreChildMapList);//正式删除子元素
		 * 不是childMapList_list ！！！
		 * 
		 * } parentMapList.removeAll(needRemoreParentMapList);//正式删除父元素
		 * Log.d(TAG,
		 * "removeGoods: needRemoreChildMapList_List"+needRemoreChildMapList_List
		 * ); childMapList_list.remove(needRemoreChildMapList_List);//！！！！
		 * 因为parentMapList和childMapList_list是pos关联的 得保持一致
		 */
		// !!!!!!!!!!!!!!!删除完 状态需要重置 待思考 why？
		// resetViewAfterDelete();
		if (parentMapList != null && parentMapList.size() > 0) {
			onCheckHasGoodsListener.onCheckHasGoods(true);//
		} else {
			onCheckHasGoodsListener.onCheckHasGoods(false);//
		}
		// showList("after@@@@@@@@@@@@@@@@@@@@@@@");
		notifyDataSetChanged();//
		dealPrice();// 重新计算
	}

	private void resetViewAfterDelete() {
		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			storeBean.setIsChecked(false);
			storeBean.setIsEditing(false);
			List<Map<String, Object>> childMapList = childMapList_list.get(i);

			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				goodsBean.setIsChecked(false);
				goodsBean.setIsEditing(false);
			}
		}
	}

	void showList(String tempStr) {
		Log.d(TAG, "showList:" + tempStr);
		for (int i = 0; i < parentMapList.size(); i++) {
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			Log.d(TAG, "showList:  parentName:【" + storeBean.getName()
					+ "】isChecked:" + storeBean.isChecked());
			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				Log.d(TAG, "showList:  childName:" + goodsBean.getName()
						+ "isChecked:" + goodsBean.isChecked());
			}
		}
	}

	public interface OnAllCheckedBoxNeedChangeListener {
		void onCheckedBoxNeedChange(boolean allParentIsChecked);
	}

	public interface OnEditingTvChangeListener {
		void onEditingTvChange(boolean allIsEditing);
	}

	public interface OnGoodsCheckedChangeListener {
		void onGoodsCheckedChange(int totalCount, double totalPrice);
	}

	public interface OnCheckHasGoodsListener {
		void onCheckHasGoods(boolean isHasGoods);
	}

	class GroupViewHolder {
		TextView tv_title_parent;
		TextView id_tv_apply;
		TextView id_tv_edit;
		CheckBox id_cb_select_parent;
	}

	class ChildViewHolder {
		ImageView id_iv_logo;
		TextView tv_01;
		TextView tv_04;
		TextView tv_05;
		TextView tv_status_01;
		TextView tv_status_02;
		TextView tv_status_03;
		TextView tv_status_04;
		TextView tv_status_05;
		TextView tv_items_child;
		CheckBox id_cb_select_child;
		LinearLayout id_ll_normal;
		LinearLayout id_ll_edtoring;

		TextView tv_items_child_desc;
		TextView id_tv_price;
		TextView id_tv_discount_price;
		TextView id_tv_count;

		ImageView id_iv_reduce;
		ImageView id_iv_add;
		TextView id_tv_des_now;
		TextView id_tv_count_now;
		TextView id_tv_price_now;
		TextView id_tv_goods_star;
		TextView id_tv_goods_delete;

	}

	// 验苗
	public void validateApplySave(final int num, String str_itemData,
			String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("cityCode", "350205001");
		params.put("itemData", str_itemData);
		params.put("cartIds", cartIds);
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/buyer/validateApply/save",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						// 将购物车传到下一级
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {
								Sel();
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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	public void preSave(final int num, String str_itemData,
			String str_preSaveForCart, String str_SaveForCart, String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ordersData", str_preSaveForCart);
		params.put("cartIds", cartIds);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/order/preSaveForCart", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								// 传递值到下单页面
								//

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	public void getValidatePrice(final int num, final String str_itemData,
			final String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("cityCode", "350205001");
		params.put("count", num + "");
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/validateApply/getValidatePrice", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
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
								GetValidatePriceCustomDialog.Builder builder = new GetValidatePriceCustomDialog.Builder(
										context);
								builder.setTitle("申请验苗");
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
												// 设置你的操作事项
												validateApplySave(num,
														str_itemData, cartIds);
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
										context);
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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

}
