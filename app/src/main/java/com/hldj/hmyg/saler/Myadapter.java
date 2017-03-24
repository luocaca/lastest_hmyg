package com.hldj.hmyg.saler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hldj.hmyg.buy.bean.StorageSave;
import com.hldj.hmyg.buyer.ArithUtil;
import com.hy.utils.ValueGetInfo;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;

class Myadapter extends BaseAdapter {

	private Handler mHandler;
	private int resourceId; // 适配器视图资源ID
	private Context context; // 上下午对象
	private ArrayList<CollectCar> list; // 数据集合List
	private LayoutInflater inflater; // 布局填充器
	public static HashMap<Integer, Boolean> isSelected;
	private Gson gson;
	private FinalBitmap fb;
	private StorageSave fromJson;
	private LinearLayout ll_reason;
	private TextView reason;

	@SuppressLint("UseSparseArrays")
	public Myadapter(Context context, ArrayList<CollectCar> list,
			Handler mHandler) {
		this.list = list;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		gson = new Gson();
		this.context = context;
		this.mHandler = mHandler;
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		Myadapter.isSelected = isSelected;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(
					R.layout.manager_group_list_item_parent, parent, false);
		}
		final CollectCar itemData = (CollectCar) list.get(position);
		ImageView iv_car_pic_1 = (ImageView) convertView
				.findViewById(R.id.iv_img);
		ImageView iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
		iv_like.setVisibility(View.GONE);
		TextView tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) convertView.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) convertView.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) convertView.findViewById(R.id.tv_09);
		reason = (TextView) convertView.findViewById(R.id.tv_reason);
		ll_reason = (LinearLayout) convertView.findViewById(R.id.ll_reason);
		CheckBox shop_check = (CheckBox) convertView.findViewById(R.id.remmber);

		TextView tv_floorPrice = (TextView) convertView
				.findViewById(R.id.tv_floorPrice);
		RelativeLayout rl_floorPrice = (RelativeLayout) convertView
				.findViewById(R.id.rl_floorPrice);
		TextView tv_status_01 = (TextView) convertView
				.findViewById(R.id.tv_status_01);
		TextView tv_status_02 = (TextView) convertView
				.findViewById(R.id.tv_status_02);
		TextView tv_status_03 = (TextView) convertView
				.findViewById(R.id.tv_status_03);
		TextView tv_status_04 = (TextView) convertView
				.findViewById(R.id.tv_status_04);
		TextView tv_status_05 = (TextView) convertView
				.findViewById(R.id.tv_status_05);
		ImageView sc_ziying = (ImageView) convertView
				.findViewById(R.id.sc_ziying);
		ImageView sc_fuwufugai = (ImageView) convertView
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) convertView
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) convertView
				.findViewById(R.id.sc_huodaofukuan);

		shop_check.setTag(position);
		shop_check.setChecked(getIsSelected().get(position));
		shop_check.setOnCheckedChangeListener(new CheckBoxChangedListener());
		fromJson = gson.fromJson(itemData.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class);
		if ("".equals(((CollectCar) list.get(position)).getReason()) && reason!=null) {
				reason.setVisibility(View.GONE);
			ll_reason.setVisibility(View.GONE);
		} else {
			if(reason!=null){
				reason.setVisibility(View.VISIBLE);
			}
			ll_reason.setVisibility(View.VISIBLE);
			reason.setText(((CollectCar) list.get(position)).getReason());
		}
		if (fromJson.getPlantType().contains("planted")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (fromJson.getPlantType().contains("transplant")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (fromJson.getPlantType().contains("heelin")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (fromJson.getPlantType().contains("container")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			tv_01.setVisibility(View.GONE);
		}

		if (fromJson.getFloorPrice() != null
				&& !"".equals(fromJson.getFloorPrice())) {
			rl_floorPrice.setVisibility(View.VISIBLE);
			tv_floorPrice.setText("底价：" + fromJson.getFloorPrice());
		} else {
			tv_floorPrice.setText("");
		}
		tv_05.setText("苗源地址：" + fromJson.getAddress());
		tv_06.setText("保存日期：" + itemData.getTime().toString());
		tv_02.setText(fromJson.getName());
		tv_04.setText(ValueGetInfo.getValueString(fromJson.getDbh(),
				fromJson.getHeight(), fromJson.getCrown(),
				fromJson.getDiameter(), fromJson.getOffbarHeight()));
		tv_07.setText(fromJson.getPrice());
		if (!"".equals(fromJson.getCount())) {
			tv_09.setText("数量:" + fromJson.getCount());
		}
		if (StringUtil.isHttpUrlPicPath(itemData.getImg())) {
			fb.display(iv_car_pic_1, itemData.getImg());
		} else {
			iv_car_pic_1.setImageResource(R.drawable.tupian_shibai);
			File file = new File(itemData.getImg());
			if (file.exists()) {
				try {
					Bitmap bm = ImageTools.CompressAndSaveImg(file);
					if (bm != null) {
						iv_car_pic_1.setImageBitmap(bm);

					} else {
						iv_car_pic_1
								.setImageResource(R.drawable.un_down_load_pic_icon);
					}
				} catch (IOException e) {
					e.printStackTrace();
					iv_car_pic_1
							.setImageResource(R.drawable.un_down_load_pic_icon);
				}
			} else {
				iv_car_pic_1.setImageResource(R.drawable.un_down_load_pic_icon);
			}
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toSaveSeedlingActivity = new Intent(context,
						SaveSeedlingActivity.class);
				toSaveSeedlingActivity.putExtra("storage_save_id",
						list.get(position).getStorage_save_id());
				Bundle bundleObject = new Bundle();
				final PicSerializableMaplist myMap = new PicSerializableMaplist();
				myMap.setMaplist(gson.fromJson(list.get(position).getTitle(),
						com.hldj.hmyg.buy.bean.StorageSave.class).getUrlPaths());
				bundleObject.putSerializable("urlPaths", myMap);
				toSaveSeedlingActivity.putExtras(bundleObject);
				toSaveSeedlingActivity.putExtra(
						"id",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getId());
				toSaveSeedlingActivity.putExtra(
						"name",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getName());
				toSaveSeedlingActivity.putExtra(
						"price",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getPrice());
				toSaveSeedlingActivity.putExtra(
						"floorPrice",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getFloorPrice());
				toSaveSeedlingActivity.putExtra(
						"count",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getCount());
				toSaveSeedlingActivity.putExtra(
						"lastDay",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getValidity());
				toSaveSeedlingActivity.putExtra(
						"firstSeedlingTypeId",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getFirstSeedlingTypeId());
				toSaveSeedlingActivity.putExtra(
						"validity",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getValidity());
				toSaveSeedlingActivity.putExtra(
						"addressId",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getAddress());
				toSaveSeedlingActivity.putExtra(
						"address",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getAddress());
				toSaveSeedlingActivity.putExtra(
						"contactName",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getContactName());
				toSaveSeedlingActivity.putExtra(
						"contactPhone",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getContactPhone());
				toSaveSeedlingActivity.putExtra(
						"isDefault",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.isDefault());
				toSaveSeedlingActivity.putExtra(
						"firstSeedlingTypeName",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getFirstSeedlingTypeId());
				toSaveSeedlingActivity.putExtra(
						"seedlingParams",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getSeedlingParams());
				toSaveSeedlingActivity.putExtra(
						"diameter",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getDiameter());
				toSaveSeedlingActivity.putExtra(
						"diameterType",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getDiameterType());
				toSaveSeedlingActivity.putExtra(
						"dbh",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getDbh());
				toSaveSeedlingActivity.putExtra(
						"dbhType",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getDbhType());
				toSaveSeedlingActivity.putExtra(
						"height",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getHeight());
				toSaveSeedlingActivity.putExtra(
						"crown",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getCrown());
				toSaveSeedlingActivity.putExtra(
						"offbarHeight",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getOffbarHeight());
				toSaveSeedlingActivity.putExtra(
						"length",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getLength());
				toSaveSeedlingActivity.putExtra(
						"plantType",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getPlantType());
				toSaveSeedlingActivity.putExtra(
						"unitType",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getUnitType());
				toSaveSeedlingActivity.putExtra(
						"paramsData",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getParamsData());
				toSaveSeedlingActivity.putExtra(
						"remarks",
						gson.fromJson(list.get(position).getTitle(),
								com.hldj.hmyg.buy.bean.StorageSave.class)
								.getRemarks());
				((Activity) context).startActivityForResult(
						toSaveSeedlingActivity, 1);
			}
		});
		return convertView;
	}

	// CheckBox选择改变监听器
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
			CollectCar collectCar = list.get(position);
			collectCar.setChoosed(flag);
			mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
			// 如果所有的物品全部被选中，则全选按钮也默认被选中
			mHandler.sendMessage(mHandler.obtainMessage(11, isAllSelected()));
		}
	}

	/**
	 * 计算选中商品的金额
	 * 
	 * @return 返回需要付费的总金额
	 */
	private double getTotalPrice() {
		CollectCar collectCar = null;
		double totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			collectCar = list.get(i);
			if (getIsSelected().get(i)) {
				// 选择，选中获取价格总量
				// totalPrice +=ArithUtil.mul(bean.getNum(), bean.getAmount());
				if (!"".equals(collectCar.getMoney())) {
					totalPrice = ArithUtil.add(totalPrice,
							Double.parseDouble(collectCar.getMoney()));
				} else {
					totalPrice = ArithUtil.add(totalPrice, 0);
				}
			}
		}
		return totalPrice;
	}

	/**
	 * 判断是否购物车中所有的商品全部被选中
	 * 
	 * @return true所有条目全部被选中 false还有条目没有被选中
	 */
	private boolean isAllSelected() {
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
			if (!getIsSelected().get(i)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	public void notify(ArrayList<CollectCar> list) {
		this.list = list;
		notifyDataSetChanged();

	}
}