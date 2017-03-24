package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;

@SuppressLint("ResourceAsColor")
public class LoadCarListAdapter extends BaseAdapter {
	private static final String TAG = "LoadCarListAdapter";

	private ArrayList<loadCarList> data = null;

	private Context context = null;
	private FinalBitmap fb;
	private HashMap<Integer, Boolean> isSelected;

	private OnCheckBoxCheckedLsitener onCheckBoxCheckedLsitener;

	private CheckBox remmber;

	private LoadCarListDetailAdapter adapternei;

	private TextView remmber_tv;

	public LoadCarListAdapter(Context context, ArrayList<loadCarList> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}

	public void initData() {

		for (int i = 0; i < data.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.list_item_loadcar, null);
		final int mPosition = position;
		remmber = (CheckBox) inflate.findViewById(R.id.remmber);
		remmber_tv = (TextView) inflate.findViewById(R.id.remmber_tv);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
		ListView lv_waitpay_nei = (ListView) inflate.findViewById(R.id.lv_waitpay_nei);

		
		if(data.get(position).isShowCheck()){
			remmber.setVisibility(View.VISIBLE);
		}else {
			remmber.setVisibility(View.GONE);
		}
		remmber_tv.setText("收货日期：" + data.get(position).getCreateDate());
		tv_01.setText("车牌号：" + data.get(position).getCarNum());
		tv_02.setText("司机姓名：" + data.get(position).getDriverName());
		tv_03.setText("司机电话：" + data.get(position).getDriverPhone());
		tv_04.setText("备注：" + data.get(position).getRemarks());
		if ("loaded".equals(data.get(position).getStatus())) {
			tv_ac.setTextColor(Data.STATUS_ORANGE);
		} else if ("unreceipt".equals(data.get(position).getStatus())) {
			tv_ac.setTextColor(Data.STATUS_GREEN);
		} else if ("receipted".equals(data.get(position).getStatus())) {
			tv_ac.setTextColor(Data.STATUS_GRAY);
		}
		tv_ac.setText(data.get(position).getStatusName());
		if(data.get(position).getItemListJsons().size()>0){
			adapternei = new LoadCarListDetailAdapter(context, data.get(position).getItemListJsons());
			lv_waitpay_nei.setAdapter(adapternei);
		}else {
			lv_waitpay_nei.setVisibility(View.GONE);
		}
		remmber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// for (int i = 0; i < titles.size(); i++) {
				//
				//
				// }
				LoadCarListAdapter.this.onCheckBoxCheckedLsitener
						.getChecked(mPosition);
				for (Integer p : isSelected.keySet()) {
					isSelected.put(p, false);
				}
				LoadCarListAdapter.this.notifyDataSetChanged();
				isSelected.put(mPosition, true);
				LoadCarListAdapter.this.notifyDataSetChanged();
			}
		});
		remmber.setChecked(isSelected.get(mPosition));
		inflate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		return inflate;
	}

	public void notify(ArrayList<loadCarList> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public interface OnCheckBoxCheckedLsitener {
		void getChecked(int position);
	}

	public void setOnCheckBoxCheckedLsitener(
			OnCheckBoxCheckedLsitener onCheckBoxCheckedLsitener) {
		this.onCheckBoxCheckedLsitener = onCheckBoxCheckedLsitener;
	}

}
