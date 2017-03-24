package com.hldj.hmyg.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.SeedlingParm;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class SeedlingParmAdapter extends BaseAdapter {
	private static final String TAG = "SeedlingParmAdapter";

	private ArrayList<SeedlingParm> data = null;
	private Context context = null;
	private FinalBitmap fb;

	private LayoutParams l_params;

	public SeedlingParmAdapter(Context context, ArrayList<SeedlingParm> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		l_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		WindowManager wm = ((Activity) context).getWindowManager();
		l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 9 / 20);
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
				R.layout.list_item_seedling_parm, null);
		TextView name = (TextView) inflate.findViewById(R.id.name);
		TextView values = (TextView) inflate.findViewById(R.id.values);
		name.setText(data.get(position).getName());
		values.setText(data.get(position).getValue());
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return inflate;
	}

	public void notify(ArrayList<SeedlingParm> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
