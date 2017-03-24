package com.zzy.common.widget.galleryView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Pic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;
import com.zzy.common.widget.gestureimageview.GestureImageView;

public class ImageGalleryPageAdapter extends BaseAdapter {

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private ViewHolder holder;
	private LayoutInflater inflater;

	private DisplayImageOptions options;

	public ImageGalleryPageAdapter(Context context, ArrayList<Pic> urlPaths) {
		this.urlPaths = urlPaths;
		inflater = LayoutInflater.from(context);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_image_big_show)
				.showImageOnFail(R.drawable.no_image_big_show_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888).cacheOnDisc(true)
				.cacheInMemory(true).build();
	}

	public void refreshView() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return urlPaths.size();
	}

	@Override
	public Object getItem(int position) {
		return urlPaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void delItem(int pos) {
		refreshView();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.image_gallery_item, null);
			holder.mGestureView = (GestureImageView) convertView
					.findViewById(R.id.image_gallery_item_image);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.image_gallery_item_pb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (!StringUtil.isHttpUrlPicPath(urlPaths.get(position).getUrl())) {
			// ImageLoader.getInstance().displayImage(urlPaths.get(position),
			// holder.photoIv);
			holder.mGestureView.setImageResource(R.drawable.tupian_shibai);
			// holder.iv_img1.setVisibility(View.INVISIBLE);
			File file = new File(urlPaths.get(position).getUrl());
			if (file.exists()) {
				try {
					Bitmap bm = ImageTools.CompressAndSaveImg(file);
					if (bm != null) {
						holder.mGestureView.setImageBitmap(bm);

					} else {
						holder.mGestureView
								.setImageResource(R.drawable.un_down_load_pic_icon);
					}
				} catch (IOException e) {
					e.printStackTrace();
					holder.mGestureView
							.setImageResource(R.drawable.un_down_load_pic_icon);
				}
			} else {
				holder.mGestureView
						.setImageResource(R.drawable.un_down_load_pic_icon);
			}

		} else {
			ImageLoader.getInstance().displayImage(urlPaths.get(position).getUrl(),
					holder.mGestureView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							holder.progressBar.setVisibility(View.GONE);
							notifyDataSetChanged();
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							holder.progressBar.setVisibility(View.GONE);
							holder.mGestureView
									.setImageResource(R.drawable.no_image_big_show_2);
							notifyDataSetChanged();
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							holder.progressBar.setVisibility(View.VISIBLE);
							notifyDataSetChanged();
						}
					});
		}
		/*
		 * ImageLoader.getInstance().displayImage(urlPaths.get(position),
		 * holder.mGestureView);
		 */
		
		return convertView;
	}

	private class ViewHolder {
		public GestureImageView mGestureView;
		public ProgressBar progressBar;

	}

	public GestureImageView getImageView() {
		return holder.mGestureView;
	}
}
