package com.photo.choosephotos;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.ChoosePicsActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.PermissionUtils;
import com.photo.choosephotos.adapter.AddImageGridAdapter;
import com.photo.choosephotos.controller.SelectPicPopupWindow;
import com.photo.choosephotos.photo.Item;
import com.photo.choosephotos.photo.PhotoAlbumActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.photo.choosephotos.util.PictureManageUtil;

public class ChooseMorePicsActivity2 extends Activity implements
		OnClickListener {

	/* 用来标识请求照相功能的activity */
	private final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	// GridView预览删除页面过来
	private final int PIC_VIEW_CODE = 2016;
	/* 拍照的照片存储位置 */
	private final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory()
					+ "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	private AddImageGridAdapter imgAdapter;
	private Bitmap addNewPic;
	private GridView gridView;// 显示所有上传图片
	private SelectPicPopupWindow menuWindow;
	private int LIMIT_SAVE = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_more_pics);
		ImageView btn_back = (ImageView) findViewById(R.id.back);
		TextView tv_public = (TextView) findViewById(R.id.fabu);
		btn_back.setOnClickListener(this);
		tv_public.setOnClickListener(this);
		if (!(PHOTO_DIR.exists() && PHOTO_DIR.isDirectory())) {
			PHOTO_DIR.mkdirs();
		}
		// 添加图片
		gridView = (GridView) findViewById(R.id.allPic);
		// 加号图片
		addNewPic = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.tianjia);
		// addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
		if (Data.microBmList.size() == 0) {
			Data.microBmList.add(addNewPic);
		}

		imgAdapter = new AddImageGridAdapter(this, Data.microBmList,
				Data.photoList);
		gridView.setAdapter(imgAdapter);
		// 事件监听，点击GridView里的图片时，在ImageView里显示出来
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == (Data.photoList.size())) {
					menuWindow = new SelectPicPopupWindow(
							ChooseMorePicsActivity2.this, itemsOnClick);
					menuWindow.showAtLocation(ChooseMorePicsActivity2.this
							.findViewById(R.id.uploadPictureLayout),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				} else {
					Intent intent = new Intent(ChooseMorePicsActivity2.this,
							ViewPagerDeleteActivity.class);
					intent.putParcelableArrayListExtra("files", Data.photoList);
					intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);
					startActivityForResult(intent, PIC_VIEW_CODE);
				}
			}
		});
	}

	// 为弹出窗口实现监听类
	private View.OnClickListener itemsOnClick = new View.OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo: {
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					boolean requestCamerPermissions = new PermissionUtils(
							ChooseMorePicsActivity2.this)
							.requestCamerPermissions(200);
					if (requestCamerPermissions) {
						// 判断是否有SD卡
						doTakePhoto();// 用户点击了从照相机获取
					}

				} else {
					Toast.makeText(ChooseMorePicsActivity2.this, "没有SD卡",
							Toast.LENGTH_LONG).show();
				}
				break;
			}
			case R.id.btn_pick_photo: {
				boolean requestReadSDCardPermissions = new PermissionUtils(
						ChooseMorePicsActivity2.this)
						.requestReadSDCardPermissions(200);
				if (requestReadSDCardPermissions) {
					// 打开选择图片界面
					doPickPhotoFromGallery();
				}

				break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			// 创建照片的存储目录
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "找不到相机", Toast.LENGTH_LONG).show();
		}
	}

	public String getPhotoFileName() {
		UUID uuid = UUID.randomUUID();
		return uuid + ".jpg";
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final ProgressDialog dialog;
			dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置为圆形
			dialog.setMessage("数据加载中...");
			dialog.setIndeterminate(false);//
			// dialog.setCancelable(true); //按回退键取消
			dialog.show();
			Window window = dialog.getWindow();
			View view = window.getDecorView();
			// Tools.setViewFontSize(view,21);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// 初始化提示框
					dialog.dismiss();
				}

			}, 1000);
			// final Intent intent = new
			// Intent(MainActivity.this,GetAllImgFolderActivity.class);
			final Intent intent = new Intent(ChooseMorePicsActivity2.this,
					PhotoAlbumActivity.class);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "图库中找不到照片", Toast.LENGTH_LONG).show();
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				imgAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 处理其他页面返回数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {

			// 调用Gallery返回的
			ArrayList<Item> tempFiles = new ArrayList<Item>();
			if (data == null)
				return;
			tempFiles = data.getParcelableArrayListExtra("fileNames");
			if ((tempFiles.size() + Data.photoList.size()) > LIMIT_SAVE) {
				Toast.makeText(ChooseMorePicsActivity2.this,
						"选择上传图片数量不能超过" + LIMIT_SAVE + "张", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (tempFiles == null) {
				return;
			}
			// Data.microBmList.remove(addNewPic);
			Data.microBmList.remove(Data.microBmList.size() - 1);
			Bitmap bitmap;
			for (int i = 0; i < tempFiles.size(); i++) {
				bitmap = MediaStore.Images.Thumbnails.getThumbnail(this
						.getContentResolver(), tempFiles.get(i).getPhotoID(),
						Thumbnails.MICRO_KIND, null);
				int rotate = PictureManageUtil
						.getCameraPhotoOrientation(tempFiles.get(i)
								.getPhotoPath());
				bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
				Data.microBmList.add(bitmap);
				Data.photoList.add(tempFiles.get(i));
			}
			Data.microBmList.add(addNewPic);
			imgAdapter.notifyDataSetChanged();
			break;
		}
		case CAMERA_WITH_DATA: {

			if ((1 + Data.photoList.size()) > LIMIT_SAVE) {
				Toast.makeText(ChooseMorePicsActivity2.this,
						"选择上传图片数量不能超过" + LIMIT_SAVE + "张", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Long delayMillis = 0L;
			if (mCurrentPhotoFile == null) {
				delayMillis = 500L;
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
					// 去掉GridView里的加号
					// Data.microBmList.remove(addNewPic);
					Data.microBmList.remove(Data.microBmList.size() - 1);
					Item item = new Item();
					item.setPhotoPath(mCurrentPhotoFile.getAbsolutePath());
					Data.photoList.add(item);
					// 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
					Bitmap bitmap = PictureManageUtil
							.getCompressBm(mCurrentPhotoFile.getAbsolutePath());
					// 获取旋转参数
					int rotate = PictureManageUtil
							.getCameraPhotoOrientation(mCurrentPhotoFile
									.getAbsolutePath());
					// 把压缩的图片进行旋转
					bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
					Data.microBmList.add(bitmap);
					Data.microBmList.add(addNewPic);
					Message msg = handler.obtainMessage(1);
					msg.sendToTarget();
				}
			}, delayMillis);
			break;
		}
		case PIC_VIEW_CODE: {
			ArrayList<Integer> deleteIndex = data
					.getIntegerArrayListExtra("deleteIndexs");

			if (deleteIndex.size() > 0) {
				for (int i = deleteIndex.size() - 1; i >= 0; i--) {
					Data.microBmList.remove((int) deleteIndex.get(i));
					Data.photoList.remove((int) deleteIndex.get(i));
				}
			}
			imgAdapter.notifyDataSetChanged();
			break;
		}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fabu:
			if (Data.photoList.size() > 0) {
				Intent intent = new Intent();
				setResult(5, intent);
				onBackPressed();
			} else {
				Toast.makeText(ChooseMorePicsActivity2.this, "您未选择上传的图片",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.back:
			onBackPressed();
			break;

		default:
			break;
		}
	}

}
