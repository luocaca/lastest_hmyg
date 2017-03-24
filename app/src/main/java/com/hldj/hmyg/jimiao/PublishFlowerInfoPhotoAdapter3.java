package com.hldj.hmyg.jimiao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Pic;
import com.white.utils.AndroidUtil;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;

public class PublishFlowerInfoPhotoAdapter3 extends BaseAdapter {

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();

	private LayoutInflater inflater;

	private Context context;

	private ViewHolder holder;

	private int dip30px;
	public static final int TO_CHOOSE_NEW_PIC = 20;

	public static final int MAX_IMAGE_COUNT = 10;

	private int width;
	private boolean isUpdataNoti;

	private boolean isShowSucceesOrOrrer;

	@SuppressWarnings("deprecation")
	public PublishFlowerInfoPhotoAdapter3(Context context,
			ArrayList<Pic> urlPaths) {
		super();
		this.urlPaths = urlPaths;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		dip30px = AndroidUtil.dip2px(context, 30);
		WindowManager wm = ((Activity) context).getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		if (urlPaths.size() < MAX_IMAGE_COUNT) {
			return urlPaths.size();
		}
		return MAX_IMAGE_COUNT;
	}

	@Override
	public Object getItem(int position) {
		if (position < urlPaths.size())
			return urlPaths.get(position);
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getUrlPathsCount() {
		return urlPaths.size();
	}

	public void addItem(Pic item) {
		urlPaths.add(item);
		notifyDataSetChanged();
	}

	public void addItems(ArrayList<Pic> items) {
		urlPaths.addAll(items);
		notify(urlPaths);
	}

	public void removeItem(int pos) {
		urlPaths.remove(pos);
		notify(urlPaths);

	}

	public ArrayList<Pic> getDataList() {
		return urlPaths;
	}

	public void notify(ArrayList<Pic> data) {
		this.urlPaths = data;
		notifyDataSetChanged();
	}

	public void notify(ArrayList<Pic> data, boolean isShowSucceesOrOrrer) {
		this.urlPaths = data;
		this.isShowSucceesOrOrrer = isShowSucceesOrOrrer;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.publish_flower_info_photo_list_item_view, null);
			holder = new ViewHolder();
			holder.photoIv = (ImageView) convertView
					.findViewById(R.id.publish_flower_info_photo_item_iv);
			holder.iv_img2 = (ImageView) convertView.findViewById(R.id.iv_img2);
			holder.iv_img1 = (ImageView) convertView.findViewById(R.id.iv_img1);
			LayoutParams para = holder.photoIv.getLayoutParams();
			para.width = (width - dip30px) / 4;
			para.height = para.width * 4 / 3;
			holder.photoIv.setLayoutParams(para);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == urlPaths.size()) {
		} else {
			holder.iv_img2.setVisibility(View.GONE);
			holder.iv_img1.setVisibility(View.VISIBLE);
			holder.iv_img2.setImageResource(R.drawable.shanchu);
			if (StringUtil.isHttpUrlPicPath(urlPaths.get(position).getUrl())) {
				// ImageLoader.getInstance().displayImage(urlPaths.get(position),
				// holder.photoIv);
				FinalBitmap.create(context).display(holder.photoIv,
						urlPaths.get(position).getUrl());
				holder.iv_img1.setVisibility(View.VISIBLE);
				holder.iv_img1.setImageResource(R.drawable.tupian_chenggou);
			} else {
				holder.iv_img1.setImageResource(R.drawable.tupian_shibai);
				// holder.iv_img1.setVisibility(View.INVISIBLE);
				File file = new File(urlPaths.get(position).getUrl());
				if (file.exists()) {
					try {
						Bitmap bm = ImageTools.CompressAndSaveImg(file);
						if (bm != null) {
							holder.photoIv.setImageBitmap(bm);

						} else {
							holder.photoIv
									.setImageResource(R.drawable.un_down_load_pic_icon);
						}
					} catch (IOException e) {
						e.printStackTrace();
						holder.photoIv
								.setImageResource(R.drawable.un_down_load_pic_icon);
					}
				} else {
					holder.photoIv
							.setImageResource(R.drawable.un_down_load_pic_icon);
				}

			}
//			holder.iv_img2.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					removeItem(position);
//
//				}
//			});

		}
		return convertView;
	}

	class ViewHolder {
		private ImageView photoIv;
		private ImageView iv_img2;
		private ImageView iv_img1;
	}

}
