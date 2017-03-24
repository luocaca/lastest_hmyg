package com.louisgeek.louisshopcart.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.bigkoo.snappingstepper.listener.SnappingStepperValueChangeListener;
import com.dyr.custom.GetValidatePriceCustomDialog;
import com.dyr.custom.GetValidatePriceCustomDialog2;
import com.google.gson.Gson;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.preSaveForCart;
import com.hldj.hmyg.bean.saveForCart;
import com.hldj.hmyg.bean.validateApply;
import com.hldj.hmyg.buyer.ArithUtil;
import com.hldj.hmyg.buyer.CheckOutValidateActivity;
import com.hldj.hmyg.buyer.SaveForCartActivity;
import com.hldj.hmyg.buyer.SerializableMaplist;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.louisgeek.louisshopcart.bean.GoodsBean;
import com.louisgeek.louisshopcart.bean.StoreBean;

/**
 * Created by louisgeek on 2016/4/27.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

	private static final String TAG = "MyBaseEtAdapter";
	List<Map<String, Object>> parentMapList;
	List<List<Map<String, Object>>> childMapList_list;
	Context context;
	private ListView listView;
	int totalCount = 0;
	double totalPrice = 0.00;
	private double to_totalPrice;
	private int to_totalCount;
	public static final String EDITING = "编辑";
	public static final String FINISH_EDITING = "完成";
	OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener;
	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;
	OnCheckHasGoodsListener onCheckHasGoodsListener;
	List<Map<String, Object>> parentMapList_tocheck = new ArrayList<Map<String, Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list_tocheck = new ArrayList<List<Map<String, Object>>>();
	MaterialDialog mMaterialDialog;
	int click_positon;

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
	onNeedChangeNum onChangeNum;
	private View frontView;
	private SnappingStepper stepperCustom;

	public void setOnChangeNum(onNeedChangeNum onChangeNum) {
		this.onChangeNum = onChangeNum;
	}

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
			List<List<Map<String, Object>>> childMapList_list, ListView listView) {
		mMaterialDialog = new MaterialDialog(context);
		this.parentMapList = parentMapList;
		this.childMapList_list = childMapList_list;
		this.context = context;
		this.listView = listView;
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
					R.layout.d4_parent_layout_cart, null);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.tv_title_parent = (TextView) convertView
					.findViewById(R.id.tv_title_parent);
			groupViewHolder.tv_title_cityname = (TextView) convertView
					.findViewById(R.id.tv_title_cityname);
			groupViewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
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
		groupViewHolder.tv_title_cityname.setText(parentName);
		groupViewHolder.tv_title_parent.setTextColor(storeBean.getText_color());
		groupViewHolder.tv_title_cityname.setTextColor(storeBean
				.getText_color());
		groupViewHolder.id_tv_apply.setText(storeBean.getType());
		// 验苗和下单
		if ("下单".equals(storeBean.getType())) {
			groupViewHolder.id_tv_apply.setVisibility(View.VISIBLE);
			groupViewHolder.tv_price.setVisibility(View.GONE);
			groupViewHolder.tv_title_cityname.setVisibility(View.GONE);
			groupViewHolder.tv_title_parent.setVisibility(View.VISIBLE);
		} else if ("验苗".equals(storeBean.getType())) {
			groupViewHolder.tv_price.setVisibility(View.VISIBLE);
			groupViewHolder.tv_title_cityname.setVisibility(View.VISIBLE);
			groupViewHolder.tv_title_parent.setVisibility(View.GONE);
			groupViewHolder.tv_price.setText("基础费："
					+ parentMapList.get(groupPosition).get("price").toString()
					+ "，递增费："
					+ parentMapList.get(groupPosition).get("increasePrice")
							.toString());
		}
		// groupViewHolder.id_tv_apply.setTextColor(storeBean.getText_color());
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
						click_positon = groupPosition;
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
							getValidatePrice(
									num,
									gson.toJson(itemDatas),
									cartIds.toString(),
									parentMapList.get(groupPosition)
											.get("cityCode").toString());

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
		return convertView;
	}

	// 设置子item的组件
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.d4_child_layout_cart, null);
			childViewHolder = new ChildViewHolder();
			childViewHolder.tv_01 = (TextView) convertView
					.findViewById(R.id.tv_01);
			childViewHolder.tv_04 = (TextView) convertView
					.findViewById(R.id.tv_04);
			childViewHolder.tv_05 = (TextView) convertView
					.findViewById(R.id.tv_05);
			childViewHolder.sc_ziying = (ImageView) convertView
					.findViewById(R.id.sc_ziying);
			childViewHolder.sc_fuwufugai = (ImageView) convertView
					.findViewById(R.id.sc_fuwufugai);
			childViewHolder.sc_hezuoshangjia = (ImageView) convertView
					.findViewById(R.id.sc_hezuoshangjia);
			childViewHolder.sc_huodaofukuan = (ImageView) convertView
					.findViewById(R.id.sc_huodaofukuan);
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
			childViewHolder.iv_img2 = (ImageView) convertView
					.findViewById(R.id.iv_img2);
			childViewHolder.iv_img = (ImageView) convertView
					.findViewById(R.id.iv_img);
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
			stepperCustom = (SnappingStepper) convertView
					.findViewById(R.id.stepperCustom);

			frontView = convertView.findViewById(R.id.ll_content);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		final GoodsBean goodsBean = (GoodsBean) childMapList_list
				.get(groupPosition).get(childPosition).get("childName");
		// 侧滑删除
		// new FrontViewToMove(frontView, listView,
		// AndroidUtil.dip2px(context, 50));

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
			}
		});
		stepperCustom.setMinValue(1);
		// 库存
		stepperCustom.setMaxValue(10000);
		stepperCustom.setValue(goodsBean.getCount());
		stepperCustom
				.setOnValueChangeListener(new SnappingStepperValueChangeListener() {

					@Override
					public void onValueChange(View view, int value) {
						// TODO Auto-generated method stub
						dealCus(goodsBean, value);
					}
				});

		if (!"published".equals(childMapList_list.get(groupPosition)
				.get(childPosition).get("status").toString())) {
			childViewHolder.iv_img
					.setImageResource(R.drawable.gouwuche_yiguoqi);
		}
		fb.display(
				childViewHolder.id_iv_logo,
				childMapList_list.get(groupPosition).get(childPosition)
						.get("imageUrl").toString());

		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("tagList").toString().contains(Data.ZIYING)) {
			childViewHolder.sc_ziying.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("tagList").toString().contains(Data.FUWU)) {
			childViewHolder.sc_fuwufugai.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("tagList").toString().contains(Data.HEZUOSHANGJIA)) {
			childViewHolder.sc_hezuoshangjia.setVisibility(View.VISIBLE);
		}
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("tagList").toString().contains(Data.ZIJINDANBAO)) {
			childViewHolder.sc_huodaofukuan.setVisibility(View.VISIBLE);
		}

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
		if (childMapList_list.get(groupPosition).get(childPosition)
				.get("closeDate").toString().length() > 10) {
			childViewHolder.tv_05.setText("下架日期："
					+ childMapList_list.get(groupPosition).get(childPosition)
							.get("closeDate").toString().substring(0, 10));
		} else {
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
							getValidatePrice(
									num,
									gson.toJson(itemDatas),
									goodsBean.getId(),
									parentMapList.get(groupPosition)
											.get("cityCode").toString());

						}

					}

				});
		childViewHolder.id_tv_goods_delete.setVisibility(View.GONE);
		childViewHolder.id_tv_goods_delete
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						if (mMaterialDialog != null) {
							mMaterialDialog
									.setTitle("提示")
									.setMessage("是否确认删除？")
									.setPositiveButton(
											context.getString(R.string.ok),
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													mMaterialDialog.dismiss();
													FinalHttp finalHttp = new FinalHttp();
													finalHttp
															.addHeader(
																	"token",
																	GetServerUrl
																			.getKeyStr(System
																					.currentTimeMillis()));
													finalHttp
															.addHeader(
																	"authc",
																	MyApplication.Userinfo
																			.getString(
																					"id",
																					""));
													AjaxParams params = new AjaxParams();
													params.put("ids",
															goodsBean.getId());
													finalHttp.post(
															GetServerUrl
																	.getUrl()
																	+ "admin/cart/delete",
															params,
															new AjaxCallBack<Object>() {

																@Override
																public void onSuccess(
																		Object t) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	try {
																		JSONObject jsonObject = new JSONObject(
																				t.toString());
																		String code = JsonGetInfo
																				.getJsonString(
																						jsonObject,
																						"code");
																		String msg = JsonGetInfo
																				.getJsonString(
																						jsonObject,
																						"msg");
																		if (!"".equals(msg)) {
																		}
																		if ("1".equals(code)) {
																			removeOneGood(
																					groupPosition,
																					childPosition);
																		} else if ("6007"
																				.equals(code)) {

																		}

																	} catch (JSONException e) {
																		// TODO
																		// Auto-generated
																		// catch
																		// block
																		e.printStackTrace();
																	}
																	super.onSuccess(t);
																}

																@Override
																public void onFailure(
																		Throwable t,
																		int errorNo,
																		String strMsg) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	Toast.makeText(
																			context,
																			R.string.error_net,
																			Toast.LENGTH_SHORT)
																			.show();
																	super.onFailure(
																			t,
																			errorNo,
																			strMsg);
																}

															});
												}

											})
									.setNegativeButton(
											context.getString(R.string.cancle),
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													mMaterialDialog.dismiss();
												}
											}).setCanceledOnTouchOutside(true)
									.show();
						}

					}
				});

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
			StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
					"parentName");
			int price = (Integer) parentMapList.get(i).get("price");
			int increasePrice = (Integer) parentMapList.get(i).get(
					"increasePrice");
			int base_check_item_count = 0;
			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = 0; j < childMapList.size(); j++) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				int count = goodsBean.getCount();
				double discountPrice = goodsBean.getDiscountPrice();
				if (goodsBean.isChecked()) {
					base_check_item_count++;
					totalCount++;// 单品多数量只记1
					// totalPrice += discountPrice * count;
					// 直接下单和验苗费分开计算
					if ("下单".equals(storeBean.getType())) {
						totalPrice = ArithUtil.add(totalPrice,
								ArithUtil.mul(discountPrice, count));
					} else if ("验苗".equals(storeBean.getType())) {
						if (j == childMapList.size() - 1) {
							// item中的内部循环完成
							totalPrice = ArithUtil.add(totalPrice, ArithUtil
									.mul(increasePrice,
											base_check_item_count - 1)); // 递增费
							totalPrice = ArithUtil.add(totalPrice, price);// 基础费

						}
					}

					Log.e("totalPrice", totalPrice + "");
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

	public void dealCus(GoodsBean goodsBean, int cut_num) {
		goodsBean.setCount(cut_num);
		notifyDataSetChanged();
		dealPrice();
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
		onChangeNum.OnSaveResultChange(true);
		notifyDataSetChanged();
		dealPrice();
	}

	public void removeGoods() {
		int removeCounts = 0;
		StringBuffer sbf = new StringBuffer();
		for (int i = parentMapList.size() - 1; i >= 0; i--) {
			List<Map<String, Object>> childMapList = childMapList_list.get(i);
			for (int j = childMapList.size() - 1; j >= 0; j--) {
				GoodsBean goodsBean = (GoodsBean) childMapList.get(j).get(
						"childName");
				if (goodsBean.isChecked()) {
					sbf.append(goodsBean.getId() + ",");
					removeCounts++;
				}
			}
		}
		if (removeCounts == 0) {

			return;
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
		/**
		 * 1.不要边遍历边删除，容易出现数组越界的情况<br>
		 * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
		 */
		if (parentMapList != null && parentMapList.size() > 0) {
			onCheckHasGoodsListener.onCheckHasGoods(true);//
		} else {
			onCheckHasGoodsListener.onCheckHasGoods(false);//
		}
		notifyDataSetChanged();//
		dealPrice();// 重新计算
		onChangeNum.OnSaveResultChange(true);
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
		TextView tv_title_cityname;
		TextView tv_price;
		TextView id_tv_apply;
		TextView id_tv_edit;
		CheckBox id_cb_select_parent;
	}

	class ChildViewHolder {
		ImageView id_iv_logo;
		ImageView iv_img2;
		ImageView iv_img;
		TextView tv_01;
		TextView tv_04;
		TextView tv_05;
		TextView tv_status_01;
		TextView tv_status_02;
		TextView tv_status_03;
		TextView tv_status_04;
		TextView tv_status_05;
		ImageView sc_ziying;
		ImageView sc_fuwufugai;
		ImageView sc_hezuoshangjia;
		ImageView sc_huodaofukuan;

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
			String cartIds, String cityCode) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("cityCode", cityCode);
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
							}
							if ("1".equals(code)) {

								parentMapList_tocheck.clear();
								childMapList_list_tocheck.clear();
								JSONObject jsonObject3 = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "apply");
								HashMap<String, Object> hMap = new HashMap<String, Object>();
								hMap.put("isCheck", false);
								hMap.put("cityName", JsonGetInfo.getJsonString(
										jsonObject3, "cityName"));
								hMap.put("cityCode", JsonGetInfo.getJsonString(
										jsonObject3, "cityCode"));
								hMap.put("totalPrice", JsonGetInfo
										.getJsonDouble(jsonObject3,
												"totalPrice"));
								JSONArray jsonArray_cartList = JsonGetInfo
										.getJsonArray(jsonObject3,
												"itemListJson");
								// 提供父列表的数据
								Map<String, Object> parentMap = new HashMap<String, Object>();

								parentMap
										.put("parentName",
												new com.hldj.hmyg.buy.bean.StoreBean(
														"" + 1,
														JsonGetInfo
																.getJsonString(
																		jsonObject3,
																		"cityName"),
														false, false,
														Color.BLACK, "验苗"));
								parentMap.put("id", JsonGetInfo.getJsonString(
										jsonObject3, "id"));
								parentMap.put("cityName", JsonGetInfo
										.getJsonString(jsonObject3, "cityName"));
								parentMap.put("cityCode", JsonGetInfo
										.getJsonString(jsonObject3, "cityCode"));
								parentMap.put("createDate", JsonGetInfo
										.getJsonString(jsonObject3,
												"createDate"));
								parentMap.put("num", JsonGetInfo.getJsonString(
										jsonObject3, "num"));
								parentMap.put("validatePrice", JsonGetInfo
										.getJsonDouble(jsonObject3,
												"validatePrice"));
								parentMap.put("itemCountJson", JsonGetInfo
										.getJsonInt(jsonObject3,
												"itemCountJson"));
								parentMap.put("verifyedCountJson", JsonGetInfo
										.getJsonInt(jsonObject3,
												"verifyedCountJson"));
								parentMap.put("acceptStatus", "unpay");
								parentMapList_tocheck.add(parentMap);
								// 提供当前父列的子列数据
								List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
								if (jsonArray_cartList.length() > 0) {
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										// JSONObject jsonObject4 =
										// JsonGetInfo.getJSONObject(
										// jsonArray_cartList
										// .getJSONObject(j),
										// "seedling");
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										Map<String, Object> products_hash = new HashMap<String, Object>();
										com.hldj.hmyg.buy.bean.GoodsBean goodsBean = new com.hldj.hmyg.buy.bean.GoodsBean(
												JsonGetInfo.getJsonString(
														jsonObject4, "id"),
												JsonGetInfo.getJsonString(
														jsonObject4, "name"),
												"url",
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"specText"),
												150,
												JsonGetInfo.getJsonDouble(
														jsonObject4,
														"seedlingPrice"),
												JsonGetInfo.getJsonInt(
														jsonObject4,
														"applyCount"),
												com.hldj.hmyg.buy.bean.GoodsBean.STATUS_VALID,
												false, false);
										products_hash.put("clickTpye", "2");
										products_hash.put("show_type",
												"seedling_list");
										products_hash.put("id", JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										products_hash.put("name", JsonGetInfo
												.getJsonString(jsonObject4,
														"name"));
										products_hash.put("imageUrl",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"largeImageUrl"));
										products_hash.put("cityName",
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"cityName"));
										products_hash.put("price", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"seedlingPrice"));
										products_hash.put("count", JsonGetInfo
												.getJsonInt(jsonObject4,
														"count"));
										products_hash.put("unitTypeName",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"unitTypeName"));
										products_hash.put(
												"diameter",
												JsonGetInfo.getJsonDouble(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"seedlingJson"),
														"diameter"));
										products_hash.put(
												"height",
												JsonGetInfo.getJsonDouble(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"seedlingJson"),
														"height"));
										products_hash.put(
												"crown",
												JsonGetInfo.getJsonDouble(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"seedlingJson"),
														"crown"));
										products_hash.put("cityName",
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"cityName"));
										products_hash.put(
												"fullName",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ciCity"),
														"fullName"));
										products_hash.put(
												"ciCity_name",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ciCity"),
														"name"));
										products_hash.put(
												"realName",
												JsonGetInfo.getJsonString(
														JsonGetInfo.getJSONObject(
																JsonGetInfo
																		.getJSONObject(
																				jsonObject4,
																				"seedlingJson"),
																"ownerJson"),
														"realName"));
										products_hash.put(
												"companyName",
												JsonGetInfo.getJsonString(
														JsonGetInfo.getJSONObject(
																JsonGetInfo
																		.getJSONObject(
																				jsonObject4,
																				"seedlingJson"),
																"ownerJson"),
														"companyName"));
										products_hash.put(
												"publicName",
												JsonGetInfo.getJsonString(
														JsonGetInfo.getJSONObject(
																JsonGetInfo
																		.getJSONObject(
																				jsonObject4,
																				"seedlingJson"),
																"ownerJson"),
														"publicName"));
										products_hash.put("status", JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										products_hash.put("statusName",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"statusName"));
										products_hash.put(
												"closeDate",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"seedlingJson"),
														"closeDate"));
										products_hash.put("childName",
												goodsBean);
										products_hash.put("plantType",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"plantType"));
										products_hash.put("isSelfSupport",
												JsonGetInfo.getJsonBoolean(
														jsonObject4,
														"isSelfSupportJson")); // 自营
										products_hash.put("freeValidatePrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject4,
														"freeValidatePrice")); // 返验苗费
										products_hash.put("cashOnDelivery",
												JsonGetInfo.getJsonBoolean(
														jsonObject4,
														"cashOnDelivery")); // 担
																			// 资金担保
										products_hash.put("freeDeliveryPrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject4,
														"freeDeliveryPrice"));// 免发货费
										products_hash.put("freeValidate",
												JsonGetInfo.getJsonBoolean(
														jsonObject4,
														"freeValidate")); // 免验苗
										products_hash.put(
												"tagList",
												JsonGetInfo.getJsonArray(
														jsonObject4, "tagList")
														.toString());//
										childMapList.add(products_hash);
									}

									childMapList_list_tocheck.add(childMapList);
								}

								com.hldj.hmyg.buy.bean.StoreBean storeBean = (com.hldj.hmyg.buy.bean.StoreBean) parentMapList_tocheck
										.get(0).get("parentName");
								if (parentMapList_tocheck.size() > 0
										&& childMapList_list_tocheck.size() > 0
										&& (parentMapList_tocheck.size() == childMapList_list_tocheck
												.size())) {
									Intent intent = new Intent(context,
											CheckOutValidateActivity.class);
									Bundle bundleObject = new Bundle();
									final SerializableMaplist myMap = new SerializableMaplist();
									myMap.setMap(parentMapList_tocheck);
									myMap.setChildMapList(childMapList_list_tocheck);
									bundleObject.putSerializable("map", myMap);
									bundleObject.putInt("to_totalCount",
											to_totalCount);
									bundleObject.putDouble("to_totalPrice",
											to_totalPrice);
									bundleObject.putString("acceptStatus",
											"unpay");
									intent.putExtras(bundleObject);
									((Activity) context)
											.startActivityForResult(intent, 1);
								}
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

	public void preSave(final int num, final String str_itemData,
			final String str_preSaveForCart, final String str_SaveForCart,
			final String cartIds) {
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
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								// 传递值到下单页面
								Intent intent = new Intent(context,
										SaveForCartActivity.class);
								intent.putExtra("str_SaveForCart",
										str_SaveForCart);
								intent.putExtra("str_itemData", str_itemData);
								intent.putExtra("cartIds", cartIds);
								intent.putExtra("t", t.toString());
								intent.putExtra("tag", "cart");
								context.startActivity(intent);
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
			final String cartIds, final String cityCode) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("cityCode", cityCode);
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
								final double totalPrice = JsonGetInfo
										.getJsonDouble(accountInfo, "price");
								final int count = num;
								to_totalPrice = totalPrice;
								to_totalCount = count;

								// 未付款
								GetValidatePriceCustomDialog2.Builder builder = new GetValidatePriceCustomDialog2.Builder(
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
												validateApplySave(num,
														str_itemData, cartIds,
														cityCode);
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
										context);
								builder.setTitle("申请验苗");
								builder.setPrice("");
								builder.setCount("");
								builder.setAccountName("");
								builder.setAccountBank("");
								builder.setAccountNum("");
								builder.setPositiveButton("立即升级",
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

	public interface onNeedChangeNum {
		void OnSaveResultChange(boolean need);
	}

}
