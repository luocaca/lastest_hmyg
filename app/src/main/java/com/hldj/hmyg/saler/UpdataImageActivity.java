package com.hldj.hmyg.saler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.PublishFlowerInfoPhotoAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.white.utils.FileUtil;
import com.white.utils.GifImgHelperUtil;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;
import com.white.utils.SystemSetting;
import com.white.utils.ZzyUtil;
import com.zzy.common.widget.MeasureGridView;
import com.zzy.flowers.activity.photoalbum.EditGalleryImageActivity;
import com.zzy.flowers.activity.photoalbum.PhotoActivity;
import com.zzy.flowers.activity.photoalbum.PhotoAlbumActivity;

public class UpdataImageActivity extends Activity {

	/**
	 */
	public static final String INTENT_START_TYPE_KEY = "intent_start_type";
	public static final String INTENT_DIR_ID_KEY = "intent_dir_id";
	public static final String INTENT_PHOTO_TYPE_KEY = "intent_photo_type";
	public static final String INTENT_HAD_CHOOSE_PHOTO_KEY = "intent_had_choose_photo";
	public static final int INTENT_NOT_NEED_FOR_RESULT = -1;
	private ImageView btn_back;
	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;
	/** 显示图片压缩至160像素以内 */
	public static final int GRID_COMPRESS_IMAGE_HEIGHT_PX = 160;
	public static final int GRID_COMPRESS_IMAGE_WIDTH_PX = 160;

	public static final int TO_TAKE_PIC = 1;
	public static final int TO_CHOOSE_PIC = 2;
	/** 图片太大 */
	public static final int PIC_IS_TOO_BIG = 3;
	/** 加载图片失败 */
	public static final int LOAD_PIC_FAILURE = 4;
	/** 添加图片 */
	public static final int ADD_NEW_PIC = 5;
	/** 照片列表 */
	private MeasureGridView photoGv;
	/** 照片适配器 */
	private PublishFlowerInfoPhotoAdapter adapter;
	/** 新增图片的宽 */
	public int newWidth;
	/** 新增图片的高 */
	public int newHeight;

	private String flowerInfoPhotoPath = "";

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private RefreshHandler handler;
	private FlowerInfoPhotoChoosePopwin popwin;
	private View mainView;
	public static UpdataImageActivity instance;
	private TextView fabu;
	private KProgressHUD hud_numHud;
	FinalHttp finalHttp = new FinalHttp();
	public int a = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updata_img);
		hud_numHud = KProgressHUD.create(UpdataImageActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		instance = this;
		SystemSetting.getInstance(UpdataImageActivity.this).choosePhotoDirId = "";
		Bundle bundle = getIntent().getExtras();
		urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
				.getMaplist();
		// 初始化
		mainView = (View) findViewById(R.id.ll_mainView);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		photoGv = (MeasureGridView) findViewById(R.id.publish_flower_info_gv);
		fabu = (TextView) findViewById(R.id.fabu);
		adapter = new PublishFlowerInfoPhotoAdapter(UpdataImageActivity.this,
				urlPaths);
		photoGv.setAdapter(adapter);
		PhotoGvOnItemClickListener itemClickListener = new PhotoGvOnItemClickListener();
		photoGv.setOnItemClickListener(itemClickListener);
		handler = new RefreshHandler(this.getMainLooper());
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		fabu.setOnClickListener(multipleClickProcess);
	}

	@Override
	public void finish() {
		super.finish();
		instance = null;
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				switch (view.getId()) {
				case R.id.btn_back:
					finish();
					break;
				case R.id.fabu:
					if (urlPaths.size() == 0) {
						Toast.makeText(UpdataImageActivity.this, "请选择图片上传",
								Toast.LENGTH_SHORT).show();
						return;
					}
					a = 0;
					fabu.setClickable(false);
					hud_numHud.show();
					for (int i = 0; i < urlPaths.size(); i++) {

						if (!StringUtil.isHttpUrlPicPath(urlPaths.get(i)
								.getUrl())) {
							GetServerUrl.addHeaders(finalHttp,true);
							finalHttp.addHeader("Content-Type",
									"application/octet-stream");
							AjaxParams params1 = new AjaxParams();
							params1.put("sourceId", "");
							File file1 = new File(urlPaths.get(i).getUrl());
							try {
								params1.put("file", file1);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							params1.put("imagType", "seedling");
							finalHttp.post(GetServerUrl.getUrl()
									+ "admin/file/image", params1,
									new AjaxCallBack<Object>() {

										@Override
										public void onStart() {
											super.onStart();
										}

										@Override
										public void onSuccess(Object t) {
											try {
												JSONObject jsonObject = new JSONObject(
														t.toString());
												int code = jsonObject
														.getInt("code");
												if (code == 1) {
													JSONObject image = JsonGetInfo.getJSONObject(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject,
																			"data"),
															"image");
													urlPaths.set(
															a,
															new Pic(
																	JsonGetInfo
																			.getJsonString(
																					image,
																					"id"),
																	false,
																	JsonGetInfo
																			.getJsonString(
																					image,
																					"url"),
																	0));
													a++;
													hudProgress();
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

											super.onSuccess(t);
										}

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
											fabu.setClickable(true);
											a++;
											hudProgress();

										}
									});
						} else {
							a++;
							hudProgress();
						}

					}

					break;
				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void hudProgress() {
			if (hud_numHud != null && !UpdataImageActivity.this.isFinishing()) {
				hud_numHud.setProgress(a * 100 / urlPaths.size());
				hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
						+ "张)");
			}
			if (a == urlPaths.size()) {
				if (urlPaths.size() > 0) {
					if (hud_numHud != null
							&& !UpdataImageActivity.this.isFinishing()) {
						hud_numHud.dismiss();
					}
					Intent intent = new Intent();
					Bundle bundleObject = new Bundle();
					final PicSerializableMaplist myMap = new PicSerializableMaplist();
					myMap.setMaplist(urlPaths);
					bundleObject.putSerializable("urlPaths", myMap);
					intent.putExtras(bundleObject);
					setResult(5, intent);
					finish();
				}
			}

		}

		/**
		 * 计时线程（防止在一定时间段内重复点击按钮）
		 */
		private class TimeThread extends Thread {
			public void run() {
				try {
					Thread.sleep(Data.loading_time);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void startPhotoActivity(Activity context, int startType,
			String dirId, int photoType, int hadChoosePicCount, int requestCode) {
		Intent intent = new Intent(context, UpdataImageActivity.class);
		intent.putExtra(INTENT_START_TYPE_KEY, startType);
		intent.putExtra(INTENT_DIR_ID_KEY, dirId);
		intent.putExtra(INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(INTENT_HAD_CHOOSE_PHOTO_KEY, hadChoosePicCount);
		if (requestCode != INTENT_NOT_NEED_FOR_RESULT) {
			context.startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

	public static void startPhotoAlbumActivity(Context context, int photoType,
			int hadChoosePicCount) {
		Intent intent = new Intent(context, PhotoAlbumActivity.class);
		intent.putExtra(UpdataImageActivity.INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(UpdataImageActivity.INTENT_HAD_CHOOSE_PHOTO_KEY,
				hadChoosePicCount);
		context.startActivity(intent);
	}

	private class PhotoGvOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == adapter.getUrlPathsCount()) {
				boolean requestCamerPermissions = new PermissionUtils(
						UpdataImageActivity.this).requestCamerPermissions(200);
				if (!requestCamerPermissions) {
					Toast.makeText(UpdataImageActivity.this, "您未同意拍照权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				boolean requestReadSDCardPermissions = new PermissionUtils(
						UpdataImageActivity.this)
						.requestReadSDCardPermissions(200);
				if (!requestReadSDCardPermissions) {
					Toast.makeText(UpdataImageActivity.this, "您未同意应用读取SD卡权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				popwin = new FlowerInfoPhotoChoosePopwin(
						UpdataImageActivity.this, UpdataImageActivity.this);
				popwin.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				EditGalleryImageActivity.startEditGalleryImageActivity(
						UpdataImageActivity.this,
						EditGalleryImageActivity.TO_EDIT_PUBLISH_IMAGE,
						position, adapter.getDataList());
			}

		}
	}

	/**
	 * 拍照
	 */
	public void toTakePic() {
		String photostatus = Environment.getExternalStorageState();
		if (photostatus.equals(Environment.MEDIA_MOUNTED)) {
			if (!ZzyUtil.ToastForSdcardSpaceEnough(UpdataImageActivity.this,
					true)) {
				// SD卡空间不足
				Toast.makeText(UpdataImageActivity.this,
						R.string.sdcard_is_full, Toast.LENGTH_SHORT).show();
				return;
			}
			doTakePhoto();
		} else {
			Toast.makeText(UpdataImageActivity.this,
					R.string.sdcard_is_unmount, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 进入拍照
	 */
	private void doTakePhoto() {
		long str = System.currentTimeMillis();
		String filename = "flower_info_" + str + ".png";
		File photoFile = new File(FileUtil.getFlowerPicPath(""), filename);
		flowerInfoPhotoPath = FileUtil.getFlowerPicPath(filename);
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			startActivityForResult(intent, TO_TAKE_PIC);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(UpdataImageActivity.this,
					R.string.cannot_select_pic, Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 选择相片
	 */
	public void toChoosePic() {
		String picstatus = Environment.getExternalStorageState();
		if (picstatus.equals(Environment.MEDIA_MOUNTED)) {
			if (SystemSetting.getInstance(UpdataImageActivity.this).choosePhotoDirId
					.length() > 0
					&& SystemSetting.isAlbumHasPhoto(
							UpdataImageActivity.this.getContentResolver(),
							UpdataImageActivity.this)) {
				// UpdataImageActivity
				// .startPhotoActivity(
				// UpdataImageActivity.this,
				// PhotoActivity.START_TYPE_JUMP_IN_NOT_FROM_ALBUM,
				// SystemSetting
				// .getInstance(UpdataImageActivity.this).choosePhotoDirId,
				// PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
				// adapter.getUrlPathsCount(),
				// PhotoActivity.INTENT_NOT_NEED_FOR_RESULT);
				PhotoAlbumActivity.startPhotoAlbumActivity(
						UpdataImageActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			} else {
				PhotoAlbumActivity.startPhotoAlbumActivity(
						UpdataImageActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			}
		} else {
			Toast.makeText(UpdataImageActivity.this,
					R.string.sdcard_is_unmount, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TO_TAKE_PIC && resultCode == RESULT_OK) {
			try {
				addImageItem(flowerInfoPhotoPath);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 其次把文件插入到系统图库
			// try {
			// MediaStore.Images.Media.insertImage(getContentResolver(),
			// flowerInfoPhotoPath, flowerInfoPhotoPath, null);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// }
			// 最后通知图库更新
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + flowerInfoPhotoPath)));
		}

	}

	/**
	 * 添加图片
	 * 
	 * @param sourchImagePath
	 */
	private void addImageItem(String sourchImagePath) throws IOException {
		// TODO 还需要做动态图片的预览处理和大小限制
		String image_path = "";
		long imageSize = 0;
		File file = new File(sourchImagePath);
		// 获取原图的宽高
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bm = ImageTools.decodeFile(sourchImagePath, opts,
				COMPRESS_IMAGE_WIDTH_PX, COMPRESS_IMAGE_HEIGHT_PX);
		if (bm != null) {
			newWidth = bm.getWidth();
			newHeight = bm.getHeight();
		}
		ExifInterface exifInterface = new ExifInterface(sourchImagePath);
		int orc = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 然后旋转
		int degree = 0;
		if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		long sourceImgSize = file.length();
		imageSize = sourceImgSize;
		boolean isGif = GifImgHelperUtil.isGif(sourchImagePath);

		/** 如果不是GIF图片 */
		if (!isGif) {
			// SD卡空间足够才压缩
			if (ZzyUtil.ToastForSdcardSpaceEnough(UpdataImageActivity.this,
					false)) {
				image_path = CompressAndSaveImg(file, degree, sourchImagePath);
				file = new File(image_path);
				imageSize = file.length();
				if (imageSize > sourceImgSize) {
					image_path = sourchImagePath;
					file = new File(sourchImagePath);
					imageSize = sourceImgSize;
				}
			}
		} else {
			image_path = sourchImagePath;
		}
		// 图片不可超过5M，如果压缩成功，则用压缩后图片
		if (imageSize > ImageTools.MAX_IMAGE_SIZE) {
			handler.sendEmptyMessage(PIC_IS_TOO_BIG);
			return;
		}
		Bitmap showbm = null;
		if (degree == 0) {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		} else {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
			showbm = ImageTools
					.rotate(file.getAbsolutePath(), showbm, degree,
							GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		}
		if (showbm != null) {
			flowerInfoPhotoPath = image_path;
			Pic pic = new Pic("", false, flowerInfoPhotoPath, 0);
			adapter.addItem(pic);
			handler.sendEmptyMessage(ADD_NEW_PIC);
		} else {
			handler.sendEmptyMessage(LOAD_PIC_FAILURE);
			return;
		}
	}

	private class RefreshHandler extends Handler {

		public RefreshHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_PIC_FAILURE:
				Toast.makeText(UpdataImageActivity.this,
						R.string.image_load_failed, Toast.LENGTH_SHORT).show();
				break;
			case ADD_NEW_PIC:
				// adapter.notifyDataSetChanged();
				adapter.notify(urlPaths);

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 添加图片
	 */
	public void addPicUrls(ArrayList<Pic> items) {
		// GlobalData.reqSeedlingData.addPictureUrlItems(items);
		adapter.addItems(items);
	}

	/**
	 * 删除图片
	 */
	public void removePicUrls(int pos) {
		// GlobalData.reqSeedlingData.removePic(pos);
		// ZzyUtil.printMessage(adapter.getDataList().get(pos));
		adapter.removeItem(pos);
	}

	/**
	 * 如果是静态图片，则进行压缩处理 压缩并存储临时文件至Image目录
	 * 
	 * @param rotate
	 */
	private String CompressAndSaveImg(File file, int degree,
			String sourceImgPath) throws IOException {
		/** 用于压缩的原图Image */
		Bitmap bitmapSourceImg;
		if (degree == 0) {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX, COMPRESS_IMAGE_WIDTH_PX);
		} else {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX / 2, COMPRESS_IMAGE_WIDTH_PX / 2);
			bitmapSourceImg = ImageTools.rotate(file.getAbsolutePath(),
					bitmapSourceImg, degree, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
		}
		String img_path = "";
		img_path = FileUtil.getFlowerPicPath("") + "/" + "flower_image_"
				+ System.currentTimeMillis() + ".png";
		File filetemp = new File(img_path);
		// 存储临时文件
		if (bitmapSourceImg != null) {
			FileOutputStream out = new FileOutputStream(filetemp);
			bitmapSourceImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} else {
			return sourceImgPath;
		}
		newWidth = bitmapSourceImg.getWidth();
		newHeight = bitmapSourceImg.getHeight();
		bitmapSourceImg = null;
		return img_path;
	}

}
