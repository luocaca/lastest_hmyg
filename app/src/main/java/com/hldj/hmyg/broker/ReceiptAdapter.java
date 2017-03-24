package com.hldj.hmyg.broker;

import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dyr.custom.MyDialog.Dialogcallback;
import com.hldj.hmyg.R;

@SuppressLint("HandlerLeak")
public class ReceiptAdapter extends BaseAdapter {

	private Handler mHandler;
	private int resourceId; // 适配器视图资源ID
	private Context context; // 上下午对象
	private List<ReceiptListJson> list; // 数据集合List
	private LayoutInflater inflater; // 布局填充器
	private static HashMap<Integer, Boolean> isSelected;
	private String sourceId = "";
	private static final String[] SPINNER_TIME = { "手机", "住宅", "其他" };

	@SuppressLint("UseSparseArrays")
	public ReceiptAdapter(Context context, List<ReceiptListJson> list,
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
		ReceiptAdapter.isSelected = isSelected;
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
			holder.tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
			holder.tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
			holder.tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
			holder.tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
			holder.tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
			holder.tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
			holder.shop_check = (CheckBox) convertView
					.findViewById(R.id.remmber);
			holder.tv_position = (TextView) convertView
					.findViewById(R.id.tv_position);
			holder.et_number = (EditText) convertView
					.findViewById(R.id.et_number);
			holder.et_number.setTag(position);
			holder.et_number.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			holder.et_number.addTextChangedListener(new MyTextWatcher(holder) {
				@Override
				public void afterTextChanged(Editable s, ViewHolder holder) {

					String text = s.toString();
					int len = s.toString().length();
					if (len == 1 && text.equals("0")) {
						s.clear();
						return;
					}
					if ("".equals(s.toString())) {
						return;
					}

					int position = (Integer) holder.et_number.getTag();
					ReceiptListJson n = list.get(position);
					n.setNeed_loadedCount(Integer.parseInt(s.toString()));
					list.set(position, n);
				}
			});

			convertView.setTag(holder);
		} else {
			ReceiptListJson n = list.get(position);
			holder = (ViewHolder) convertView.getTag();
			holder.et_number.setTag(position);
			holder.tv_position.setText("装车数量：");
			holder.et_number.setText(n.getNeed_loadedCount() + "");
			holder.et_number.setHint("可装："
					+ (list.get(position).getCount() - list.get(position)
							.getLoadedCountJson()));
		}

		holder.tv_01.setText("品种名称：" + bean.getOrderName());
		 holder.tv_02.setText("品种规格：" + bean.getCreateBy());
		// holder.tv_03.setText("装车数量：" + bean.getLoadedCountJson() + "/"
		// + bean.getCount());
		// holder.tv_04.setText("姓名：" + bean.getContactName());
		// holder.tv_05.setText("收货电话：" + bean.getContactPhone());
		holder.tv_06.setText("收货地址：" + bean.getFullAddress());
		holder.shop_check.setTag(position);
		holder.shop_check.setChecked(getIsSelected().get(position));
		holder.shop_check
				.setOnCheckedChangeListener(new CheckBoxChangedListener());
		if (bean.isShowCheckBox()) {
			holder.shop_check.setVisibility(View.VISIBLE);
		} else {
			holder.shop_check.setVisibility(View.GONE);
		}

		return convertView;
	}

	public interface onNeedRefreshListener {
		void OnNeedRefreshListener(String id, int pos);
	}

	onNeedRefreshListener oNeedRefreshListener;

	public void setoNeedRefreshListener(
			onNeedRefreshListener oNeedRefreshListener) {
		this.oNeedRefreshListener = oNeedRefreshListener;
	}

	private int getPositionForAdapter(int po) {

		ReceiptListJson t = list.get(po);
		int p = 0;
		for (int i = 0; i < SPINNER_TIME.length; i++) {
			if (t.getOrderName().equals(SPINNER_TIME[i])) {
				p = i;
			}
		}
		return p;
	}

	// 动态添加List里面数据
	public void addItem(ReceiptListJson n) {
		list.add(n);
	}

	private abstract class MySpinnerListener implements OnItemSelectedListener {
		private ViewHolder holder;

		public MySpinnerListener(ViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			onItemSelected(arg0, arg1, arg2, arg3, holder);
		}

		public abstract void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3, ViewHolder holder);
	}

	private abstract class MyTextWatcher implements TextWatcher {
		private ViewHolder mHolder;

		public MyTextWatcher(ViewHolder holder) {
			this.mHolder = holder;
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			afterTextChanged(s, mHolder);
		}

		public abstract void afterTextChanged(Editable s, ViewHolder holder);
	}

	private abstract class MyOnClickListener implements OnClickListener {

		private ViewHolder mHolder;

		public MyOnClickListener(ViewHolder holder) {
			this.mHolder = holder;
		}

		@Override
		public void onClick(View v) {
			onClick(v, mHolder);
		}

		public abstract void onClick(View v, ViewHolder holder);

	}

	private final class ViewHolder {
		public TextView tv_01;
		public TextView tv_02;
		public TextView tv_03;
		public TextView tv_04;
		public TextView tv_05;
		public TextView tv_06;
		public CheckBox shop_check; // 商品选择按钮
		public TextView tv_position;
		public EditText et_number;
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
	private ReceiptListJson bean;

	// CheckBox选择改变监听器
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
			ReceiptListJson bean = list.get(position);
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
		ReceiptListJson bean = null;
		float totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			bean = list.get(i);
			if (getIsSelected().get(i)) {
				// 选择，选中获取价格总量
				totalPrice += bean.getCount();
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
			
		}
	};

}
