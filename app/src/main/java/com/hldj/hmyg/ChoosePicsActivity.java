package com.hldj.hmyg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.framework.DragGridBaseAdapter;
import com.example.framework.DragGridView;
import com.hldj.hmyg.application.Data;
import com.white.utils.ImageTools;
import com.xingguo.huang.mabiwang.util.CacheUtils;
import com.xingguo.huang.mabiwang.util.PictureManageUtil;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

/**
 * 
 */
public class ChoosePicsActivity extends NeedSwipeBackActivity implements OnClickListener, OnItemClickListener {
	private ChoosepicAdapter myadapter;
	private DragGridView gridView;
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	String dateTime;
	String targeturl = null;
	private static final int SCALE = 5;// 照片缩小比例   本地图库
	private Bitmap bitmap;
	MaterialDialog mMaterialDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pic);
		mMaterialDialog = new MaterialDialog(this);
		Toast.makeText(ChoosePicsActivity.this, "长按移动可排序，点击可删除，第一张为封面",
				Toast.LENGTH_LONG).show();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_public = (TextView) findViewById(R.id.tv_public);
		TextView sure = (TextView) findViewById(R.id.sure);
		gridView = (DragGridView) findViewById(R.id.gridView);
		btn_back.setOnClickListener(this);
		tv_public.setOnClickListener(this);
		sure.setOnClickListener(this);

		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (myadapter != null) {
			myadapter.notifyDataSetChanged();
		} else {
			myadapter = new ChoosepicAdapter(ChoosePicsActivity.this, Data.pics);
			gridView.setAdapter(myadapter);
			gridView.setOnItemClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_public:
			new ActionSheetDialog(ChoosePicsActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("拍照", SheetItemColor.Red,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {

									Date date = new Date(System
											.currentTimeMillis());
									dateTime = date.getTime() + "";
									File f = new File(CacheUtils
											.getCacheDirectory(
													ChoosePicsActivity.this,
													true, "pic")
											+ dateTime);
									if (f.exists()) {
										f.delete();
									}
									try {
										f.createNewFile();
									} catch (IOException e) {
										e.printStackTrace();
									}
									Uri uri = Uri.fromFile(f);

									Intent camera = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									camera.putExtra(MediaStore.EXTRA_OUTPUT,
											uri);
									startActivityForResult(camera,
											REQUEST_CODE_CAMERA);

								}
							})
							
					.addSheetItem("从相册选择", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									Date date = new Date(System
											.currentTimeMillis());
									dateTime = date.getTime() + "";
									File f = new File(CacheUtils
											.getCacheDirectory(
													ChoosePicsActivity.this,
													true, "pic")
											+ dateTime);
									if (f.exists()) {
										f.delete();
									}
									try {
										f.createNewFile();
									} catch (IOException e) {
										e.printStackTrace();
									}
									Uri uri = Uri.fromFile(f);

									Intent openAlbumIntent = new Intent(
											Intent.ACTION_GET_CONTENT);
									openAlbumIntent
											.setDataAndType(
													MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
													"image/*");
									startActivityForResult(openAlbumIntent,
											REQUEST_CODE_ALBUM);

								}
							}).show();
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.sure:
			if (Data.pics.size() > 0) {
				Intent intent = new Intent();
				setResult(5, intent);
				onBackPressed();
			} else {
				Toast.makeText(ChoosePicsActivity.this, "您未选择上传的图片",
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.onBackPressed();
	}
	
	
	

	public class ChoosepicAdapter extends BaseAdapter implements
			DragGridBaseAdapter {
		private static final String TAG = "ChoosepicAdapter";

		private ArrayList<String> data = null;

		private Context context = null;
		private FinalBitmap fb;
		private int mHidePosition = -1;

		public ChoosepicAdapter(Context context, ArrayList<String> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
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

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.grid_item_pic, null);
			ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
			if (data.get(position).startsWith("http")) {
				fb.display(iv_img, data.get(position));
			} else {
				iv_img.setImageDrawable(new BitmapDrawable(PictureManageUtil
						.getCompressBm(data.get(position))));
			}
			//
			if (position == mHidePosition) {
				inflate.setVisibility(View.INVISIBLE);
			}
//			inflate.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//				public void onClick(View v) {
//					if (mMaterialDialog != null) {
//						mMaterialDialog
//								.setMessage("确定删除该图片？")
//								// mMaterialDialog.setBackgroundResource(R.drawable.background);
//								.setPositiveButton(getString(R.string.ok),
//										new View.OnClickListener() {
//											@Override
//											public void onClick(View v) {
//												mMaterialDialog.dismiss();
//												data.remove(position);
//												myadapter.notify(data);
//											}
//										})
//								.setNegativeButton(getString(R.string.cancle),
//										new View.OnClickListener() {
//											public void onClick(View v) {
//												mMaterialDialog.dismiss();
//											}
//										}).setCanceledOnTouchOutside(true)
//								// You can change the message anytime.
//								// mMaterialDialog.setTitle("提示");
//								.setOnDismissListener(
//										new DialogInterface.OnDismissListener() {
//											@Override
//											public void onDismiss(
//													DialogInterface dialog) {
//											}
//										}).show();
//					} else {
//					}
//
////					new AlertDialog(ChoosePicsActivity.this).builder()
////							.setTitle("提示").setMsg("确定删除该图片？")
////							.setPositiveButton("确认", new OnClickListener() {
////								@Override
////								public void onClick(View v) {
////									data.remove(position);
////									myadapter.notifyDataSetChanged();
////
////								}
////							}).setNegativeButton("取消", new OnClickListener() {
////								@Override
////								public void onClick(View v) {
////
////								}
////							}).show();
//
//				}
//			});

			return inflate;
		}

		public void notify(ArrayList<String> data) {
			this.data = data;
			notifyDataSetChanged();
		}

		@Override
		public void reorderItems(int oldPosition, int newPosition) {
			String temp = data.get(oldPosition);
			if (oldPosition < newPosition) {
				for (int i = oldPosition; i < newPosition; i++) {
					Collections.swap(data, i, i + 1);
				}
			} else if (oldPosition > newPosition) {
				for (int i = oldPosition; i > newPosition; i--) {
					Collections.swap(data, i, i - 1);
				}
			}

			data.set(newPosition, temp);
		}

		@Override
		public void setHideItem(int hidePosition) {
			this.mHidePosition = hidePosition;
			notifyDataSetChanged();
		}
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 1600f;//
		float ww = 960f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CODE_ALBUM:
			ContentResolver resolver = getContentResolver();
			// 照片的原始资源地址
			Uri originalUri = data.getData();
			try {
				// 使用ContentProvider通过URI获取原始图片
				Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
						originalUri);
				if (photo != null) {
					// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
					Bitmap smallBitmap = ImageTools
							.zoomBitmap(photo, photo.getWidth() / SCALE,
									photo.getHeight() / SCALE);
					// 释放原始图片占用的内存，防止out of memory异常发生
					photo.recycle();
					targeturl = saveToSdCard(smallBitmap);
					Data.pics.add(targeturl);
					myadapter.notifyDataSetChanged();
					// head.setImageDrawable(new BitmapDrawable(smallBitmap));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case REQUEST_CODE_CAMERA:
			String files = CacheUtils.getCacheDirectory(
					ChoosePicsActivity.this, true, "pic") + dateTime;
			File file = new File(files);
			if (file.exists()) {
				bitmap = compressImageFromFile(files);
				targeturl = saveToSdCard(bitmap);
				Data.pics.add(targeturl);
				myadapter.notifyDataSetChanged();
				// img_head.setBackgroundDrawable(new BitmapDrawable(bitmap));
				// head.setImageDrawable(new BitmapDrawable(bitmap));
			} else {

			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils.getCacheDirectory(ChoosePicsActivity.this,
				true, "pic") + dateTime + "_11";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	public int i=0;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		i = position;
		if (mMaterialDialog != null) {
			mMaterialDialog
					.setMessage("确定删除该图片？")
					// mMaterialDialog.setBackgroundResource(R.drawable.background);
					.setPositiveButton(getString(R.string.ok),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									mMaterialDialog.dismiss();
									Data.pics.remove(i);
									myadapter.notify(Data.pics);
								}
							})
					.setNegativeButton(getString(R.string.cancle),
							new View.OnClickListener() {
								public void onClick(View v) {
									mMaterialDialog.dismiss();
								}
							}).setCanceledOnTouchOutside(true)
					.setOnDismissListener(
							new DialogInterface.OnDismissListener() {
								@Override
								public void onDismiss(
										DialogInterface dialog) {
								}
							}).show();
		} else {
		}


	
	}


}
