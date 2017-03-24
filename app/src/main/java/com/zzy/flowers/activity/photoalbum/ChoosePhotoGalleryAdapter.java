package com.zzy.flowers.activity.photoalbum;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hldj.hmyg.R;
import com.util.androidgiflib.GifImageView;
import com.zzy.common.widget.gestureimageview.GestureImageView;
import com.zzy.common.widget.gestureimageview.RomateOverCallback;

public class ChoosePhotoGalleryAdapter extends BaseAdapter {

	private List<PhotoItem> list = new ArrayList<PhotoItem>();
	private ViewHolder holder;
	private LayoutInflater inflater;
	private Context context;

	public ChoosePhotoGalleryAdapter(Context context, List<PhotoItem> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public void refreshView() {
		notifyDataSetChanged();
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

	public void delItem(int pos) {
		refreshView();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.image_gallery_item, null);
			holder.mGestureView = (GestureImageView) convertView
					.findViewById(R.id.image_gallery_item_image);
			holder.gifImageView = (GifImageView) convertView
					.findViewById(R.id.image_gallery_item_gif_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!item.isGifPic()) {
			holder.gifImageView.setVisibility(View.GONE);
			holder.mGestureView.setVisibility(View.VISIBLE);
			Bitmap bm = item.getSourceBitmap(context);
			holder.mGestureView.setGalleryImageBitmap(bm, false, 0,
					RomateOverCallback.TYPE_CHAT_PIC, 0);
		} else {
			holder.gifImageView.setVisibility(View.VISIBLE);
			holder.mGestureView.setVisibility(View.GONE);
			if (!holder.gifImageView.setImagePath(item.getPath())) { // 动态图片加载失败
				holder.gifImageView.setVisibility(View.GONE);
				holder.mGestureView.setVisibility(View.VISIBLE);
				Bitmap bm = item.getSourceBitmap(context);
				holder.mGestureView.setGalleryImageBitmap(bm, false, 0,
						RomateOverCallback.TYPE_CHAT_PIC, 0);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		public GestureImageView mGestureView;
		public GifImageView gifImageView;
	}
}
