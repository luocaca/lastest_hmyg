package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.hldj.hmyg.R;
import com.hldj.hmyg.ThematicActivity;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class ThematicAdapter extends BaseAdapter {
	private static final String TAG = "ThematicAdapter";

	private ArrayList<HashMap<String, Object>> data = null;
	private Context context = null;
	private FinalBitmap fb;

	private LayoutParams l_params;

	public ThematicAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
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
				R.layout.list_item_home, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_img.setLayoutParams(l_params);
		fb.display(iv_img, data.get(position).get("url")
				.toString());

		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toThematicActivity = new Intent(context,
						ThematicActivity.class);
				toThematicActivity.putExtra("id", data.get(position).get("id")
						.toString());
				toThematicActivity.putExtra("title",
						data.get(position).get("title").toString());
				toThematicActivity.putExtra("type",
						data.get(position).get("type").toString());
				toThematicActivity.putExtra("ossLargeImagePath", data.get(position)
						.get("url").toString());
				context.startActivity(toThematicActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
