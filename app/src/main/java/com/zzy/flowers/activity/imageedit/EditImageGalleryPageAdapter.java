package com.zzy.flowers.activity.imageedit;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.saler.GlobalConstant;
import com.hy.utils.CommonBitmapUtil;
import com.hy.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.photo.choosephotos.util.GifImgHelperUtil;
import com.util.androidgiflib.GifImageView;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;
import com.zzy.common.widget.gestureimageview.GestureImageView;
import com.zzy.common.widget.gestureimageview.RomateOverCallback;

public class EditImageGalleryPageAdapter extends BaseAdapter {

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private ViewHolder holder;
	private LayoutInflater inflater;
	private Context context;
	private DisplayImageOptions options;
	public EditImageGalleryPageAdapter(Context context,
			ArrayList<Pic> urlPaths) {
		this.urlPaths = urlPaths;
		this.context = context;
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
			holder.gifImageView = (GifImageView) convertView
					.findViewById(R.id.image_gallery_item_gif_image);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.image_gallery_item_pb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String picPath = urlPaths.get(position).getUrl();
		if (StringUtil.isHttpUrlPicPath(picPath)) {
			holder.gifImageView.setVisibility(View.GONE);
			holder.mGestureView.setVisibility(View.VISIBLE);
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
		} else {
			if (!isGifPic(picPath)) {
				holder.gifImageView.setVisibility(View.GONE);
				holder.mGestureView.setVisibility(View.VISIBLE);
				Bitmap bm = getSourceBitmap(picPath);
				holder.mGestureView.setGalleryImageBitmap(bm, false, 0,
						RomateOverCallback.TYPE_CHAT_PIC, 0);
			} else {
				holder.gifImageView.setVisibility(View.VISIBLE);
				holder.mGestureView.setVisibility(View.GONE);
				if (!holder.gifImageView.setImagePath(picPath)) {
					holder.gifImageView.setVisibility(View.GONE);
					holder.mGestureView.setVisibility(View.VISIBLE);
					Bitmap bm = getSourceBitmap(picPath);
					holder.mGestureView.setGalleryImageBitmap(bm, false, 0,
							RomateOverCallback.TYPE_CHAT_PIC, 0);
				}
			}
		}
		return convertView;
	}

	public Bitmap getSourceBitmap(String picPath) {
		Bitmap bm = null;
		if (picPath != null && picPath.length() > 0) {
			File file = new File(picPath);
			if (file.exists()) {
				bm = getCompressBitmap(file);
				if (bm == null) { 
					Bitmap loadFailPic = CommonBitmapUtil
							.getFromCache(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE);
					if (loadFailPic == null) {
						loadFailPic = BitmapFactory.decodeResource(
								context.getResources(),
								R.drawable.un_down_load_pic_icon);
						CommonBitmapUtil.addBitmap(
								GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE,
								loadFailPic);
					}
					bm = loadFailPic;
					ToastUtil.showShortToast(context,
							R.string.image_load_failed);
				}
			}
		}
		if (bm == null) { 
			Bitmap unDownPic = CommonBitmapUtil
					.getFromCache(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE);
			if (unDownPic == null) {
				unDownPic = BitmapFactory.decodeResource(
						context.getResources(),
						R.drawable.un_down_load_pic_icon);
				CommonBitmapUtil.addBitmap(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE,
						unDownPic);
			}
			bm = unDownPic;
			return bm;
		}
		return bm;
	}

	public boolean isGifPic(String picPath) {
		boolean isGif = false;
		if (picPath != null && picPath.length() > 0) {
			File file = new File(picPath);
			if (file.exists()) {
				try {
					isGif = GifImgHelperUtil.isGif(picPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return isGif;
	}

	private Bitmap getCompressBitmap(File file) {
		Bitmap result = null;
		try {
			result = ImageTools.CompressAndSaveImg(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private class ViewHolder {
		public GestureImageView mGestureView;
		public GifImageView gifImageView;
		public ProgressBar progressBar;
	}

}
