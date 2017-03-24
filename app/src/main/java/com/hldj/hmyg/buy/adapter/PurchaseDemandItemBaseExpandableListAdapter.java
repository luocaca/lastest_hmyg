package com.hldj.hmyg.buy.adapter;

import java.io.InputStream;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hldj.hmyg.GalleryImageActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.bean.PurchaseDemandChildItem;
import com.hldj.hmyg.buy.bean.PurchaseDemandGroupItem;
import com.hy.utils.CommonUtil;
import com.hy.utils.GetServerUrl;
import com.hy.utils.GifHelper.GifFrame;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.StringFormatUtil;
import com.hy.utils.ValueGetInfo;

/**
 * ExpandListView的适配器，继承自BaseExpandableListAdapter
 * 
 */
@SuppressLint("ResourceAsColor")
public class PurchaseDemandItemBaseExpandableListAdapter extends
		BaseExpandableListAdapter implements OnClickListener {

	private Context mContext;
	private List<PurchaseDemandGroupItem> groupTitle;
	// 子项是一个map，key是group的id，每一个group对应一个PurchaseDemandChildItem的list
	private List<List<PurchaseDemandChildItem>> childMap;
	private Button groupButton;// group上的按钮
	private boolean isChoose;
	private String status;

	public PurchaseDemandItemBaseExpandableListAdapter(Context context,
			List<PurchaseDemandGroupItem> groupTitle,
			List<List<PurchaseDemandChildItem>> childMap, boolean isChoose,
			String status) {
		this.mContext = context;
		this.groupTitle = groupTitle;
		this.childMap = childMap;
		this.isChoose = isChoose;
		this.status = status;
	}

	/*
	 * Gets the data associated with the given child within the given group
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// 我们这里返回一下每个item的名称，以便单击item时显示
		return childMap.get(groupPosition).get(childPosition).getId();
	}

	/*
	 * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*
	 * Gets a View that displays the data for the given child within the given
	 * group
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.purchase_demand_childitem, null);
			childHolder = new ChildHolder();
			childHolder.childImg = (ImageView) convertView
					.findViewById(R.id.img_child);
			childHolder.iv_img2 = (ImageView) convertView
					.findViewById(R.id.iv_img2);
			childHolder.childText = (TextView) convertView
					.findViewById(R.id.tv_child_text);
			childHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			childHolder.tv_00 = (TextView) convertView.findViewById(R.id.tv_00);
			childHolder.tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
			childHolder.tv_floorPrice = (TextView) convertView
					.findViewById(R.id.tv_floorPrice);
			childHolder.tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
			childHolder.tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
			childHolder.tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
			childHolder.tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
			childHolder.tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
			childHolder.tv_07 = (TextView) convertView.findViewById(R.id.tv_07);
			childHolder.tv_08 = (TextView) convertView.findViewById(R.id.tv_08);
			childHolder.tv_09 = (TextView) convertView.findViewById(R.id.tv_09);
			childHolder.line = (View) convertView.findViewById(R.id.line);
			childHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			childHolder.iv_baojia_chakantupian = (ImageView) convertView
					.findViewById(R.id.iv_baojia_chakantupian);
			childHolder.new_gif = (ImageView) convertView
					.findViewById(R.id.new_gif);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		if (childMap.get(groupPosition).get(childPosition).getFloorPrice() > 0) {
			StringFormatUtil fillColor = new StringFormatUtil(mContext, "底价："
					+ ValueGetInfo.doubleTrans1(childMap.get(groupPosition)
							.get(childPosition).getFloorPrice()) + "元", "底价：",
					R.color.black).fillColor();
			childHolder.tv_floorPrice.setText(fillColor.getResult());
		}
		childHolder.tv_00.setText("["
				+ childMap.get(groupPosition).get(childPosition)
						.getPlantTypeName() + "]");
		childHolder.tv_02.setText(ValueGetInfo.formatNoDataString(childMap
				.get(groupPosition).get(childPosition).getSpecText()));
		childHolder.tv_01.setText(ValueGetInfo.doubleTrans1(childMap
				.get(groupPosition).get(childPosition).getPrice())
				+ "元/"
				+ childMap.get(groupPosition).get(childPosition)
						.getUnitTypeName());
		childHolder.tv_05.setText("苗源地："
				+ childMap.get(groupPosition).get(childPosition).getCityName());
		childHolder.tv_07.setText("备注："
				+ childMap.get(groupPosition).get(childPosition).getRemarks());
		StringFormatUtil fillColor = new StringFormatUtil(mContext, "有"
				+ childMap.get(groupPosition).get(childPosition)
						.getImagesJson().size() + "张图片", childMap
				.get(groupPosition).get(childPosition).getImagesJson().size()
				+ "", R.color.orange).fillColor();
		childHolder.tv_08.setText(fillColor.getResult());
		if (isChoose && "confirm".equals(status)) {
			if (childMap.get(groupPosition).get(childPosition).isUse()) {
				childHolder.tv_status.setText("	 取消	 ");
				childHolder.tv_status.setTextColor(mContext.getResources()
						.getColor(R.color.orange));
				childHolder.tv_status
						.setBackgroundResource(R.drawable.orange_btn_selector);
			} else {
				childHolder.tv_status.setText("选中价格");
				childHolder.tv_status.setTextColor(mContext.getResources()
						.getColor(R.color.main_color));
				childHolder.tv_status
						.setBackgroundResource(R.drawable.green_btn_selector);
			}
		} else {
			childHolder.tv_status.setVisibility(View.GONE);
		}

		if (childMap.get(groupPosition).get(childPosition).isUse()) {
			childHolder.iv_img2.setVisibility(View.VISIBLE);
		} else {
			childHolder.iv_img2.setVisibility(View.GONE);
		}

		if (childMap.get(groupPosition).get(childPosition).getImagesJson()
				.size() > 0) {
			childHolder.iv_baojia_chakantupian.setVisibility(View.VISIBLE);
			childHolder.tv_09.setVisibility(View.GONE);
		} else {
			childHolder.iv_baojia_chakantupian.setVisibility(View.GONE);
			childHolder.tv_09.setVisibility(View.VISIBLE);
		}
		if (childMap.get(groupPosition).get(childPosition).isNew()
				&& !childMap.get(groupPosition).get(childPosition)
						.isClerkConfirm()) {
			childHolder.new_gif.setVisibility(View.VISIBLE);
			// final InputStream fis = mContext.getResources().openRawResource(
			// R.drawable.new_gif);
			// GifFrame[] frames = CommonUtil.getGif(fis);
			// PlayGifTask mGifTask = new PlayGifTask(childHolder.new_gif,
			// frames);
			// mGifTask.startTask();
			// Thread th = new Thread(mGifTask);
			// th.start();
		} else {
			childHolder.new_gif.setVisibility(View.GONE);
		}

		if (childPosition == (childMap.get(groupPosition).size() - 1)) {
			childHolder.line.setVisibility(View.VISIBLE);
		} else {
			childHolder.line.setVisibility(View.GONE);
		}
		childHolder.iv_baojia_chakantupian
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (childMap.get(groupPosition).get(childPosition)
								.getImagesJson().size() > 0) {
							GalleryImageActivity.startGalleryImageActivity(
									v.getContext(),
									0,
									childMap.get(groupPosition)
											.get(childPosition).getImagesJson());
						}

					}
				});

		childHolder.tv_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isChoose) {
					usedQuote(childMap.get(groupPosition).get(childPosition)
							.getId(), groupPosition);
				}

			}
		});
		return convertView;
	}

	/*
	 * 取得指定分组的子元素数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childMap.get(groupPosition).size();
	}

	/**
	 * 取得与给定分组关联的数据
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groupTitle.get(groupPosition);
	}

	/**
	 * 取得分组数
	 */
	@Override
	public int getGroupCount() {
		return groupTitle.size();
	}

	/**
	 * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * Gets a View that displays the given groupreturn: the View corresponding
	 * to the group at the specified position
	 */
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.purchase_demand_groupitem, null);
			groupHolder = new GroupHolder();
			groupHolder.groupImg = (ImageView) convertView
					.findViewById(R.id.img_indicator);
			groupHolder.groupText = (TextView) convertView
					.findViewById(R.id.tv_group_text);
			groupHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			groupHolder.tv_00 = (TextView) convertView.findViewById(R.id.tv_00);
			groupHolder.tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
			groupHolder.tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
			groupHolder.tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
			groupHolder.tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
			groupHolder.tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
			groupHolder.tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
			groupHolder.tv_07 = (TextView) convertView.findViewById(R.id.tv_07);
			groupHolder.tv_08 = (TextView) convertView.findViewById(R.id.tv_08);
			groupHolder.tv_09 = (TextView) convertView.findViewById(R.id.tv_09);
			groupHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			groupHolder.iv_img2 = (ImageView) convertView
					.findViewById(R.id.iv_img2);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		if (isExpanded) {
			groupHolder.groupImg.setBackgroundResource(R.drawable.downarrow);
			groupHolder.tv_09
					.setBackgroundResource(R.drawable.caigoubaojiao_shouqibaojia);
		} else {
			groupHolder.groupImg.setBackgroundResource(R.drawable.rightarrow);
			groupHolder.tv_09
					.setBackgroundResource(R.drawable.caigoubaojiao_chakanbaojia);
		}
		groupHolder.tv_00.setText((groupPosition + 1) + "、"
				+ groupTitle.get(groupPosition).getName());
		groupHolder.tv_01.setText(groupTitle.get(groupPosition).getCount()
				+ groupTitle.get(groupPosition).getUnit());
		groupHolder.tv_02.setText(ValueGetInfo.formatNoDataString(groupTitle
				.get(groupPosition).getSpecText()));
		groupHolder.tv_04.setText("备注："
				+ groupTitle.get(groupPosition).getRemarks());
		if (groupTitle.get(groupPosition).getQuoteCount() > 0) {
			StringFormatUtil fillColor = new StringFormatUtil(mContext, "有"
					+ groupTitle.get(groupPosition).getQuoteCount() + "条报价",
					groupTitle.get(groupPosition).getQuoteCount() + "",
					R.color.orange).fillColor();
			groupHolder.tv_06.setText(fillColor.getResult());
		} else {
			groupHolder.tv_06.setVisibility(View.GONE);
		}
		if (groupTitle.get(groupPosition).isUsedQuote()) {
			groupHolder.iv_img2.setVisibility(View.VISIBLE);
			groupHolder.tv_07.setVisibility(View.VISIBLE);
		} else {
			groupHolder.iv_img2.setVisibility(View.INVISIBLE);
			groupHolder.tv_07.setVisibility(View.GONE);
		}

		StringFormatUtil fillColor = new StringFormatUtil(mContext, "已确认报价："
				+ groupTitle.get(groupPosition).getQuotePrice()
				+ "/"
				+ groupTitle.get(groupPosition).getQuoteUnit()
				+ ",共"
				+ ValueGetInfo.formatFloatNumber(groupTitle.get(groupPosition)
						.getQuoteTotalPrice()) + "元。",
				ValueGetInfo.formatFloatNumber(groupTitle.get(groupPosition)
						.getQuoteTotalPrice()) + "", R.color.red).fillColor();
		groupHolder.tv_07.setText(fillColor.getResult());
		groupButton = (Button) convertView
				.findViewById(R.id.btn_group_function);
		groupButton.setOnClickListener(this);
		// groupHolder.tv_09.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// cleanNew(groupTitle.get(groupPosition).getId(), groupPosition);
		// }
		// });
		return convertView;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupExpanded(groupPosition);
		// 展开时被调用
		cleanNew(groupTitle.get(groupPosition).getId(), groupPosition);
	}

	@Override
	public boolean hasStableIds() {
		// Indicates whether the child and group IDs are stable across changes
		// to the underlying data
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// Whether the child at the specified position is selectable
		return true;
	}

	/**
	 * show the text on the child and group item
	 */
	private class GroupHolder {
		ImageView iv_img2;
		TextView tv_count;
		ImageView groupImg;
		TextView groupText;
		TextView tv_00;
		TextView tv_01;
		TextView tv_02;
		TextView tv_03;
		TextView tv_04;
		TextView tv_05;
		TextView tv_06;
		TextView tv_07;
		TextView tv_08;
		TextView tv_09;
		TextView tv_status;
	}

	private class ChildHolder {
		public TextView tv_floorPrice;
		ImageView iv_img2;
		View line;
		ImageView new_gif;
		TextView tv_count;
		ImageView childImg;
		TextView childText;
		TextView tv_00;
		TextView tv_01;
		TextView tv_02;
		TextView tv_03;
		TextView tv_04;
		TextView tv_05;
		TextView tv_06;
		TextView tv_07;
		TextView tv_08;
		TextView tv_09;
		TextView tv_status;
		ImageView iv_baojia_chakantupian;
	}

	public interface OnGoodsCheckedChangeListener {
		void onGoodsCheckedChange(int position, String t, boolean isRefresh);
	}

	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;

	public void setOnGoodsCheckedChangeListener(
			OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
		this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
	}

	private void cleanNew(String id, final int pos) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		Log.e("admin/buyer/purchaseDemandItem/updIsNew", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/purchaseDemandItem/updIsNew", params,
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
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void usedQuote(String id, final int pos) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		Log.e("admin/buyer/purchaseDemandItem/usedQuote", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/purchaseDemandItem/usedQuote", params,
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
								onGoodsCheckedChangeListener
										.onGoodsCheckedChange(pos,
												t.toString(), true);
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
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_group_function:
			Log.d("MyBaseExpandableListAdapter", "你点击了group button");
		default:
			break;
		}

	}
}
