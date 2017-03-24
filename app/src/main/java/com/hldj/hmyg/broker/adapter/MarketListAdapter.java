package com.hldj.hmyg.broker.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.GalleryImageActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.broker.bean.MarketPrice;
import com.hy.utils.ValueGetInfo;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class MarketListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";

	private ArrayList<MarketPrice> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;

	public MarketListAdapter(Context context, ArrayList<MarketPrice> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
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
				R.layout.list_item_market_price, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		TextView tv_pu_time = (TextView) inflate.findViewById(R.id.tv_pu_time);
		TextView tv_floorPrice = (TextView) inflate
				.findViewById(R.id.tv_floorPrice);
		RelativeLayout rl_floorPrice = (RelativeLayout) inflate
				.findViewById(R.id.rl_floorPrice);
		TextView tv_status_01 = (TextView) inflate
				.findViewById(R.id.tv_status_01);
		TextView tv_status_02 = (TextView) inflate
				.findViewById(R.id.tv_status_02);
		TextView tv_status_03 = (TextView) inflate
				.findViewById(R.id.tv_status_03);
		TextView tv_status_04 = (TextView) inflate
				.findViewById(R.id.tv_status_04);
		TextView tv_status_05 = (TextView) inflate
				.findViewById(R.id.tv_status_05);

		ImageView sc_ziying = (ImageView) inflate.findViewById(R.id.sc_ziying);
		ImageView sc_fuwufugai = (ImageView) inflate
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) inflate
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) inflate
				.findViewById(R.id.sc_huodaofukuan);

		if (data.get(position).getPlantType().toString().contains("planted")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (data.get(position).getPlantType().toString()
				.contains("transplant")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (data.get(position).getPlantType().toString()
				.contains("heelin")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (data.get(position).getPlantType().toString()
				.contains("container")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			tv_01.setVisibility(View.GONE);
		}

		
		
		if("".equals(data.get(position).getQualityGradeName().toString()) && "".equals(data.get(position).getQualityTypeName().toString())){
			tv_06.setText("品质：-");
		}else {
			if ("".equals(data.get(position).getQualityGradeName().toString())) {
				tv_06.setText("品质："
						+ data.get(position).getQualityTypeName().toString());
			} else {
				tv_06.setText("品质："
						+ data.get(position).getQualityTypeName().toString() + "("
						+ data.get(position).getQualityGradeName().toString() + ")");
			}
		}
		

		if (data.get(position).getPriceDate().toString().length() >= 11) {
			tv_pu_time.setText("发布时间："
					+ data.get(position).getPriceDate().toString()
							.substring(0, 11));
		}
		tv_05.setText("地区：" + data.get(position).getCityName().toString());
		if ("".equals(data.get(position).getFirstTypeName().toString())) {
			tv_02.setText(data.get(position).getName().toString());
		} else {
			tv_02.setText("["
					+ data.get(position).getFirstTypeName().toString() + "]"
					+ data.get(position).getName().toString());
		}
		tv_04.setText(data.get(position).getSpecText().toString());

		tv_07.setText(ValueGetInfo.doubleTrans1(data.get(position).getPrice()));
		fb.display(iv_img, data.get(position).getImageUrl().toString());
		iv_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (data.get(position).getSeedlingTypeJson().size() > 0) {
					GalleryImageActivity.startGalleryImageActivity(context, 0,
							data.get(position).getSeedlingTypeJson());
				}
			}
		});

		return inflate;
	}

	public void notify(ArrayList<MarketPrice> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public interface OnGoodsCheckedChangeListener {
		void onGoodsCheckedChange(String id, boolean isRefresh);
	}

	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;

	public void setOnGoodsCheckedChangeListener(
			OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
		this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
	}

}
