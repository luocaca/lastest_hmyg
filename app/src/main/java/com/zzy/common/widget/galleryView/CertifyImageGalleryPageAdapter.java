package com.zzy.common.widget.galleryView;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hldj.hmyg.R;
import com.hy.utils.CommonBitmapUtil;
import com.white.utils.FileUtil;
import com.white.utils.GlobalData;
import com.white.utils.ZzyUtil;
import com.zzy.common.widget.gestureimageview.GestureImageView;

public class CertifyImageGalleryPageAdapter extends BaseAdapter {

	private ArrayList<CertifyPhotoItem> certifyPhotoItems = new ArrayList<CertifyPhotoItem>();
	private ViewHolder holder;
	private LayoutInflater inflater;
	private int pos;
	private Context context;
	private int type;

	public CertifyImageGalleryPageAdapter(Context context,
			ArrayList<CertifyPhotoItem> certifyPhotoItems, int type) {
		this.context = context;
		this.certifyPhotoItems = certifyPhotoItems;
		this.type = type;
		inflater = LayoutInflater.from(context);
	}

	public void refreshView() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return certifyPhotoItems.size();
	}

	@Override
	public Object getItem(int position) {
		return certifyPhotoItems.get(position);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		pos = position;
		// holder.mGestureView.setImageBitmap(certifyPhotoItems.get(position));
		setImageViews();
		return convertView;
	}

	private class ViewHolder {
		public GestureImageView mGestureView;
	}

	private void setImageViews() {
		String imagePath = FileUtil.getFlowerPicPath(certifyPhotoItems.get(pos)
				.getBigPicPath());
		File imageFile = new File(imagePath);
		if (imageFile.exists()) {
			Bitmap imageBm = CommonBitmapUtil.converBitmap(imageFile,
					CommonBitmapUtil.COMPRESS_IMAGE_WIDTH_PX,
					CommonBitmapUtil.COMPRESS_IMAGE_HEIGHT_PX);
			if (imageBm != null) {
				holder.mGestureView.setImageBitmap(imageBm);
			} else {
				holder.mGestureView.setImageBitmap(BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.un_down_load_pic_icon));
			}
		} else {
			imagePath = FileUtil.getFlowerPicPath(certifyPhotoItems.get(pos)
					.getSmallPicPath());
			imageFile = new File(imagePath);
			if (imageFile.exists()) {
				Bitmap imageBm = CommonBitmapUtil.converBitmap(imageFile,
						CommonBitmapUtil.COMPRESS_IMAGE_WIDTH_PX,
						CommonBitmapUtil.COMPRESS_IMAGE_HEIGHT_PX);
				if (imageBm != null) {
					holder.mGestureView.setImageBitmap(imageBm);
				} else {
					holder.mGestureView.setImageBitmap(BitmapFactory
							.decodeResource(context.getResources(),
									R.drawable.un_down_load_pic_icon));
				}
			} else {
				holder.mGestureView.setImageBitmap(BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.un_down_load_pic_icon));
			}
		}

	}

	public void setPhotoList(ArrayList<CertifyPhotoItem> certifyPhotoItems) {
		this.certifyPhotoItems = certifyPhotoItems;
		notifyDataSetChanged();
	}
}
