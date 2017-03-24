package com.hldj.hmyg.saler;

import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.MyDialog;
import com.dyr.custom.MyDialog.Dialogcallback;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.ArithUtil;
import com.hldj.hmyg.buyer.BuyOrderBean;
import com.hldj.hmyg.buyer.ShopAdapter.onNeedRefreshListener;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

@SuppressLint("HandlerLeak")
public class SalerOrderAdapter extends BaseAdapter {

	private Handler mHandler;
	private int resourceId; // 适配器视图资源ID
	private Context context; // 上下午对象
	private List<BuyOrderBean> list; // 数据集合List
	private LayoutInflater inflater; // 布局填充器
	private static HashMap<Integer, Boolean> isSelected;
	private String sourceId = "";
	private int count = 0;

	public interface NeedRefreshListener {
		void OnNeedRefreshListener(boolean refresh);
	}

	NeedRefreshListener oNeedRefreshListener;

	public void setoNeedRefreshListener(NeedRefreshListener oNeedRefreshListener) {
		this.oNeedRefreshListener = oNeedRefreshListener;
	}

	@SuppressLint("UseSparseArrays")
	public SalerOrderAdapter(Context context, List<BuyOrderBean> list,
			Handler mHandler, int resourceId) {
		this.list = list;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		this.mHandler = mHandler;
		this.resourceId = resourceId;
		inflater = LayoutInflater.from(context);
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
		SalerOrderAdapter.isSelected = isSelected;
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
		bean = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			holder = new ViewHolder();
			holder.shop_photo = (ImageView) convertView
					.findViewById(R.id.iv_img);
			holder.tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
			holder.tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
			holder.tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
			holder.tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
			holder.tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
			holder.tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
			holder.tv_07 = (TextView) convertView.findViewById(R.id.tv_07);
			holder.tv_08 = (TextView) convertView.findViewById(R.id.tv_08);
			holder.tv_09 = (TextView) convertView.findViewById(R.id.tv_09);
			holder.tv_10 = (TextView) convertView.findViewById(R.id.tv_10);
			holder.tv_caozuo01 = (TextView) convertView
					.findViewById(R.id.tv_caozuo01);
			holder.tv_caozuo02 = (TextView) convertView
					.findViewById(R.id.tv_caozuo02);
			holder.tv_caozuo03 = (TextView) convertView
					.findViewById(R.id.tv_caozuo03);
			holder.ll_caozuo = (LinearLayout) convertView
					.findViewById(R.id.ll_caozuo);
			holder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
			holder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			holder.shop_check = (CheckBox) convertView
					.findViewById(R.id.remmber);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		fb.display(holder.shop_photo, bean.getImg());
		if (bean.getPlantType().contains("planted")) {
			holder.tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (bean.getPlantType().contains("transplant")) {
			holder.tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (bean.getPlantType().contains("heelin")) {
			holder.tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (bean.getPlantType().contains("container")) {
			holder.tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		}

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
		if (list.get(position).getTagList().contains(Data.ZIYING)) {
			sc_ziying.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getTagList().contains(Data.FUWU)) {
			sc_fuwufugai.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getTagList().contains(Data.HEZUOSHANGJIA)) {
			sc_hezuoshangjia.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getTagList().contains(Data.ZIJINDANBAO)) {
			sc_huodaofukuan.setVisibility(View.VISIBLE);
		}

		if (list.get(position).isSelfSupport()) {
			tv_status_01.setVisibility(View.VISIBLE);
		}
		if (list.get(position).isFreeValidatePrice()) {
			tv_status_02.setVisibility(View.VISIBLE);
		}
		if (list.get(position).isCashOnDelivery()) {
			tv_status_03.setVisibility(View.VISIBLE);
		}
		if (list.get(position).isFreeDeliveryPrice()) {
			tv_status_04.setVisibility(View.VISIBLE);
		}
		if (list.get(position).isFreeValidate()) {
			tv_status_05.setVisibility(View.VISIBLE);
		}

		holder.tv_02.setText(bean.getName());
		holder.tv_04.setText(bean.getSpecText());
		holder.tv_05.setText("发货费：" + bean.getDeliveryPrice() + "元");
		if (bean.getTradeType().contains("proxy")) {
			holder.tv_06.setText("委");
		} else {
			holder.tv_06.setText("担");
		}
		holder.tv_06.setVisibility(View.VISIBLE);
		holder.tv_07.setText(bean.getPrice() + "");
		holder.tv_08.setText("元／" + bean.getUnitTypeName());
		holder.tv_09.setText(bean.getCount() + bean.getUnitTypeName());
		holder.tv_10.setText("合计：" + bean.getTotalPrice());
		if (list.get(position).getStatus().contains("unpay")) {
			holder.tv_status.setText("待付款");
			holder.tv_status.setTextColor(Data.STATUS_ORANGE);
			holder.tv_caozuo03.setText("改价");
			holder.tv_caozuo03.setVisibility(View.VISIBLE);
			holder.ll_caozuo.setVisibility(View.VISIBLE);
		} else if (list.get(position).getStatus().contains("unsend")) {
			holder.tv_status.setText("待发货");
			holder.tv_status.setTextColor(Data.STATUS_BLUE);
			holder.tv_caozuo03.setVisibility(View.GONE);
			holder.ll_caozuo.setVisibility(View.GONE);
		} else if (list.get(position).getStatus().contains("unreceipt")) {
			holder.tv_status.setText("待收货");
			holder.tv_status.setTextColor(Data.STATUS_GREEN);
			holder.tv_caozuo03.setVisibility(View.GONE);
			holder.ll_caozuo.setVisibility(View.GONE);
		} else if (list.get(position).getStatus().contains("finished")) {
			holder.tv_status.setText("已完成");
			holder.tv_status.setTextColor(Data.STATUS_GRAY);
			holder.tv_caozuo03.setVisibility(View.GONE);
			holder.ll_caozuo.setVisibility(View.GONE);
		}
		holder.tv_data.setText(bean.getCreateDate());
		// holder.shop_number.setTag(position);
		// holder.shop_number.setText(String.valueOf(bean.getShopNumber()));
		// holder.shop_number.setOnClickListener(new ShopNumberClickListener());
		holder.shop_check.setTag(position);
		holder.shop_check.setChecked(getIsSelected().get(position));
		holder.shop_check
				.setOnCheckedChangeListener(new CheckBoxChangedListener());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent toOrderDetailActivity = new Intent(context,
						OrderDetailActivity.class);
				toOrderDetailActivity
						.putExtra("id", list.get(position).getId());
				context.startActivity(toOrderDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		holder.tv_caozuo03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 待取反
				if (bean.getStatus().contains("unpay")) {
					sourceId = list.get(position).getId();
					count = list.get(position).getCount();
					MyDialog myDialog = new MyDialog(context);
					myDialog.setContent("请填写需要修改的价格");
					myDialog.setDialogCallback(dialogcallback);
					myDialog.show();
				} else {
				}

			}

			public void doPay(String orderIds) {
				FinalHttp finalHttp = new FinalHttp();
				GetServerUrl.addHeaders(finalHttp,true);
				AjaxParams params = new AjaxParams();
				params.put("orderIds", orderIds);
				finalHttp.post(GetServerUrl.getUrl()
						+ "admin/buyer/order/doPay", params,
						new AjaxCallBack<Object>() {

							@Override
							public void onSuccess(Object t) {
								// TODO Auto-generated method stub

								try {
									JSONObject jsonObject = new JSONObject(t
											.toString());
									String code = JsonGetInfo.getJsonString(
											jsonObject, "code");
									String msg = JsonGetInfo.getJsonString(
											jsonObject, "msg");
									if (!"".equals(msg)) {
										Toast.makeText(context, msg,
												Toast.LENGTH_SHORT).show();
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
								Toast.makeText(context, R.string.error_net,
										Toast.LENGTH_SHORT).show();
								super.onFailure(t, errorNo, strMsg);
							}

						});
			}
		});

		return convertView;
	}

	private final class ViewHolder {
		public ImageView shop_photo; // 商品图片
		public TextView tv_01;
		public TextView tv_02;
		public TextView tv_03;
		public TextView tv_04;
		public TextView tv_05;
		public TextView tv_06;
		public TextView tv_07;
		public TextView tv_08;
		public TextView tv_09;
		public TextView tv_10;
		public TextView tv_caozuo01;
		public TextView tv_caozuo02;
		public TextView tv_caozuo03;
		public LinearLayout ll_caozuo;
		public TextView tv_data;
		public TextView tv_status;
		public CheckBox shop_check; // 商品选择按钮
	}

	// 数量TextView点击监听器
	private final class ShopNumberClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 获取商品的数量
			String str = ((TextView) v).getText().toString();
			int shopNum = Integer.valueOf(str);
		}
	}

	private int number = 0; // 记录对话框中的数量
	private EditText editText; // 对话框中数量编辑器
	/**
	 * 弹出对话框更改商品的数量
	 * 
	 * @param shopNum
	 *            商品原来的数量
	 * @param textNum
	 *            Item中显示商品数量的控件
	 */

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) { // 更改商品数量
				((TextView) msg.obj).setText(String.valueOf(number));
				// 更改商品数量后，通知Activity更新需要付费的总金额
				mHandler.sendMessage(mHandler
						.obtainMessage(10, getTotalPrice()));
			} else if (msg.what == 2) {// 更改对话框中的数量
				editText.setText(String.valueOf(number));
			}
		}
	};
	private FinalBitmap fb;
	private BuyOrderBean bean;

	// CheckBox选择改变监听器
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
			BuyOrderBean bean = list.get(position);
			bean.setChoosed(flag);
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
		BuyOrderBean bean = null;
		double totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			bean = list.get(i);
			if (bean.isChoosed()) {
				// totalPrice += bean.getNum() * bean.getAmount();
				totalPrice = ArithUtil.add(totalPrice, bean.getAmount());
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

	/**
	 * 设置mydialog需要处理的事情
	 */
	Dialogcallback dialogcallback = new Dialogcallback() {
		@Override
		public void dialogdo(String string) {
			saveReturnApply(sourceId, string);

		}
	};

	public void notify(List<BuyOrderBean> list) {
		this.list = list;
		// for (int i = 0; i < list.size(); i++) {
		// getIsSelected().put(i, false);
		// }
		notifyDataSetChanged();

	}

	public void saveReturnApply(final String orderIds, String content) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", orderIds);
		params.put("newPrice", content);
		// params.put("count", count + "");
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/seller/order/updatePrice",
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
							if (!"".equals(msg)) {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {

								oNeedRefreshListener
										.OnNeedRefreshListener(true);
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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}
}
