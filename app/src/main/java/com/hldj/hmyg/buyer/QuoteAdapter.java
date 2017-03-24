package com.hldj.hmyg.buyer;

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
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.MyDialog.Dialogcallback;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.StoreActivity;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;
import com.zf.iosdialog.widget.AlertDialog;

@SuppressLint("HandlerLeak")
public class QuoteAdapter extends BaseAdapter {

	private Handler mHandler;
	private int resourceId; // 适配器视图资源ID
	private Context context; // 上下午对象
	private List<Quote> list; // 数据集合List
	private LayoutInflater inflater; // 布局填充器
	private static HashMap<Integer, Boolean> isSelected;
	private String sourceId = "";
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	@SuppressLint("UseSparseArrays")
	public QuoteAdapter(Context context, List<Quote> list, Handler mHandler,
			int resourceId) {
		this.list = list;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
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
		QuoteAdapter.isSelected = isSelected;
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
			holder.iv_img2 = (ImageView) convertView.findViewById(R.id.iv_img2);
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
			holder.shop_check = (CheckBox) convertView
					.findViewById(R.id.remmber);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.shop_check.setTag(position);
		holder.shop_check.setChecked(getIsSelected().get(position));
		holder.shop_check
				.setOnCheckedChangeListener(new CheckBoxChangedListener());

		if (!"".equals(bean.getCompanyName())) {
			holder.tv_01.setText("供应商家：" + bean.getCompanyName());
		} else if ("".equals(bean.getCompanyName())
				&& !"".equals(bean.getPublicName())) {
			holder.tv_01.setText("供应商家：" + bean.getPublicName());
		} else if ("".equals(bean.getCompanyName())
				&& "".equals(bean.getPublicName())) {
			holder.tv_01.setText("供应商家：" + bean.getRealName());
		}
		holder.tv_03.setText("联系人：" + bean.getShowName());
		if (!"".equals(bean.getPublicPhone())) {
			holder.tv_04.setText("联系电话：" + bean.getPublicPhone());
		} else if ("".equals(bean.getPublicPhone())
				&& !"".equals(bean.getPhone())) {
			holder.tv_04.setText("联系电话：" + bean.getPhone());
		}
		holder.tv_05.setText("价格：" + ValueGetInfo.doubleTrans1(bean.getPrice()));
		holder.tv_06.setText("数量：" + bean.getCount());
		holder.tv_07.setText("苗源地址：" + bean.getCityName());
		if (bean.isInvoice()) {
			holder.tv_08.setText("提供发票");
		} else {
			holder.tv_08.setText("不提供发票");
		}
		holder.tv_09.setText("报价说明：" + bean.getRemarks());
		if (bean.isUsed()) {
			holder.shop_photo
					.setImageResource(R.drawable.baojiaxiangqing_yicaiyong);
		} else if (bean.isAlternative()) {
			holder.iv_img2.setImageResource(R.drawable.baojiaxiangqing_beixuan);
		}
		holder.tv_02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean requesCallPhonePermissions = new PermissionUtils(
						(Activity) context).requesCallPhonePermissions(200);
				if (requesCallPhonePermissions) {
					final String phoneString = bean.getPhone();
					CallPhone(phoneString);
				}

			}
		});
		return convertView;
	}

	private final class ViewHolder {
		public ImageView shop_photo; // 商品图片
		public ImageView iv_img2; // 商品图片
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
	private Quote bean;

	// CheckBox选择改变监听器
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
			Quote bean = list.get(position);
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
	private float getTotalPrice() {
		Quote bean = null;
		float totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			bean = list.get(i);
			if (getIsSelected().get(i)) {
				// 选择，选中获取价格总量
				totalPrice += bean.getPrice() * 1;
				// totalPrice += bean.getNum() * bean.getTotalPrice();
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

	public void saveReturnApply(String orderIds, String content) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("sourceId", orderIds);
		params.put("content", content);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/loadCar/returnApply", params,
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

	private void CallPhone(final String phoneString) {
		// TODO Auto-generated method stub
		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				context);
		dialog.title("是否拨号：").content(phoneString).btnText("确认拨号", "取消")//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click
					// listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phoneString));
//						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
//								.parse("tel:" + phoneString));
						context.startActivity(intent);
					}
				}, new OnBtnClickL() {// right btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				});

	}
}
