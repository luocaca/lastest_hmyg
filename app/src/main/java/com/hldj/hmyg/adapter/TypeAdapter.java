package com.hldj.hmyg.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hldj.hmyg.BActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Type;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class TypeAdapter extends BaseAdapter {
	private static final String TAG = "HomeAdapter";

	private ArrayList<Type> data = null;
	private Context context = null;
	private FinalBitmap fb;
	private LayoutParams l_params;

	public TypeAdapter(Context context, ArrayList<Type> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		l_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		WindowManager wm = ((Activity) context).getWindowManager();
		l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 1 / 4);
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
				R.layout.grid_item_type, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		// iv_img.setLayoutParams(l_params);
		if ("".equals(data.get(position).getUrlString())) {
			iv_img.setImageResource(data.get(position).getImg());
		} else {
			fb.display(iv_img, data.get(position).getUrlString());
		}
		tv_title.setText(data.get(position).getName());

		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toBActivity = new Intent(context, BActivity.class);
				toBActivity.putExtra("from", "context");
				toBActivity.putExtra("firstSeedlingTypeId", data.get(position)
						.getId());
				toBActivity.putExtra("firstSeedlingTypeName", data
						.get(position).getName());
				context.startActivity(toBActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		return inflate;
	}

	public void notify(ArrayList<Type> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
