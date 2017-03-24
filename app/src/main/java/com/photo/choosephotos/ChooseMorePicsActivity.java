package com.photo.choosephotos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.framework.DragGridView;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hy.utils.CommonBitmapUtil;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.photo.choosephotos.adapter.AddImageGridAdapterD;
import com.photo.choosephotos.controller.SelectPicPopupWindow;
import com.photo.choosephotos.photo.Item;
import com.photo.choosephotos.photo.PhotoAlbumActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.photo.choosephotos.util.PictureManageUtil;
import com.white.utils.GifImgHelperUtil;
import com.white.utils.ZzyUtil;
import com.xingguo.huang.mabiwang.util.CacheUtils;

public class ChooseMorePicsActivity extends Activity implements OnClickListener {

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
	private AddImageGridAdapterD imgAdapter;
	private Bitmap addNewPic;
	private DragGridView gridView;// 显示所有上传图片
	private SelectPicPopupWindow menuWindow;
	private int LIMIT_SAVE = 8;

	public int a = 0;

	/** 新增图片的宽 */
	public int newWidth;
	/** 新增图片的高 */
	public int newHeight;

	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;
	/** 显示图片压缩至160像素以内 */
	public static final int GRID_COMPRESS_IMAGE_HEIGHT_PX = 160;
	public static final int GRID_COMPRESS_IMAGE_WIDTH_PX = 160;

	private String flowerInfoPhotoPath = "";

	public static final int TO_TAKE_PIC = 1;
	public static final int TO_CHOOSE_PIC = 2;

	/** 图片太大 */
	public static final int PIC_IS_TOO_BIG = 3;
	/** 加载图片失败 */
	public static final int LOAD_PIC_FAILURE = 4;
	/** 添加图片 */
	public static final int ADD_NEW_PIC = 5;

	private ArrayList<String> temImageLists = new ArrayList<String>();
	String dateTime;
	FinalHttp finalHttp = new FinalHttp();
	boolean isCover = false;
	public int i = 0;
	MaterialDialog mMaterialDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_more_pics);
		Toast.makeText(ChooseMorePicsActivity.this, "长按移动可排序，点击可删除，第一张为封面",
				Toast.LENGTH_LONG).show();
		mMaterialDialog = new MaterialDialog(this);
		hud = KProgressHUD.create(ChooseMorePicsActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		ImageView btn_back = (ImageView) findViewById(R.id.back);
		TextView tv_public = (TextView) findViewById(R.id.tv_public);
		tv_public.setVisibility(View.INVISIBLE);
		fabu = (TextView) findViewById(R.id.fabu);
		btn_back.setOnClickListener(this);
		tv_public.setOnClickListener(this);
		fabu.setOnClickListener(this);
		if (!(PHOTO_DIR.exists() && PHOTO_DIR.isDirectory())) {
			PHOTO_DIR.mkdirs();
		}
		// 添加图片
		gridView = (DragGridView) findViewById(R.id.allPic);
		// 加号图片
		addNewPic = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.tianjia);
		// addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
		if (Data.microBmList.size() == 0) {
			Data.microBmList.add(addNewPic);
		}

		imgAdapter = new AddImageGridAdapterD(this, Data.microBmList,
				Data.photoList);
		gridView.setAdapter(imgAdapter);
		// 事件监听，点击GridView里的图片时，在ImageView里显示出来
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == (Data.photoList.size())) {
					menuWindow = new SelectPicPopupWindow(
							ChooseMorePicsActivity.this, itemsOnClick);
					menuWindow.showAtLocation(ChooseMorePicsActivity.this
							.findViewById(R.id.uploadPictureLayout),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				} else {
					Intent intent = new Intent(ChooseMorePicsActivity.this,
							ViewPagerDeleteActivity.class);
					intent.putParcelableArrayListExtra("files", Data.photoList);
					intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);
					startActivityForResult(intent, PIC_VIEW_CODE);
				}
			}
		});

		// gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// i = position;
		// if (mMaterialDialog != null) {
		// mMaterialDialog
		// .setMessage("确定删除该图片？")
		// // mMaterialDialog.setBackgroundResource(R.drawable.background);
		// .setPositiveButton(getString(R.string.ok),
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mMaterialDialog.dismiss();
		// Data.photoList.remove(i);
		// Data.microBmList.remove(i);
		// imgAdapter.notifyDataSetChanged();
		// }
		// })
		// .setNegativeButton(getString(R.string.cancle),
		// new View.OnClickListener() {
		// public void onClick(View v) {
		// mMaterialDialog.dismiss();
		// }
		// }).setCanceledOnTouchOutside(true)
		// // You can change the message anytime.
		// // mMaterialDialog.setTitle("提示");
		// .setOnDismissListener(
		// new DialogInterface.OnDismissListener() {
		// @Override
		// public void onDismiss(
		// DialogInterface dialog) {
		// }
		// }).show();
		// } else {
		// }
		// return false;
		// }
		// });
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
							ChooseMorePicsActivity.this)
							.requestCamerPermissions(200);
					if (requestCamerPermissions) {
						// 判断是否有SD卡
						doTakePhoto();// 用户点击了从照相机获取
					}

				} else {
					Toast.makeText(ChooseMorePicsActivity.this, "没有SD卡",
							Toast.LENGTH_LONG).show();
				}
				break;
			}
			case R.id.btn_pick_photo: {
				boolean requestReadSDCardPermissions = new PermissionUtils(
						ChooseMorePicsActivity.this)
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

		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			// try {
			// // 创建照片的存储目录
			// mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());//
			// 给新照的照片文件命名
			// final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			// // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(intent, CAMERA_WITH_DATA);
			// } catch (ActivityNotFoundException e) {
			// Toast.makeText(this, "找不到相机", Toast.LENGTH_LONG).show();
			// }
			// 判断是否有SD卡
			Date date = new Date(System.currentTimeMillis());
			dateTime = date.getTime() + "";
			File f = new File(CacheUtils.getCacheDirectory(
					ChooseMorePicsActivity.this, true, "pic") + dateTime);
			if (f.exists()) {
				f.delete();
			}
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Uri uri = Uri.fromFile(f);
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(camera, CAMERA_WITH_DATA);
		} else {
			Toast.makeText(getApplicationContext(), "内存卡不存在",
					Toast.LENGTH_SHORT).show();
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
			final Intent intent = new Intent(ChooseMorePicsActivity.this,
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
	private KProgressHUD hud;
	private TextView fabu;

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
				Toast.makeText(ChooseMorePicsActivity.this,
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
				Toast.makeText(ChooseMorePicsActivity.this,
						"选择上传图片数量不能超过" + LIMIT_SAVE + "张", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Long delayMillis = 0L;
			if (mCurrentPhotoFile == null) {
				delayMillis = 500L;
			}
			handler.postDelayed(new Runnable() {
				private String picPath;

				@Override
				public void run() {
					picPath = CacheUtils.getCacheDirectory(
							ChooseMorePicsActivity.this, true, "pic")
							+ dateTime;
					File file = new File(picPath);
					if (file.exists()) {
						Data.microBmList.remove(Data.microBmList.size() - 1);
						Item item = new Item();
						item.setPhotoPath(picPath);
						Data.photoList.add(item);
						// 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
						Bitmap bitmap = PictureManageUtil
								.getCompressBm(picPath);
						// 获取旋转参数
						int rotate = PictureManageUtil
								.getCameraPhotoOrientation(picPath);
						// 把压缩的图片进行旋转
						bitmap = PictureManageUtil.rotateBitmap(
								PictureManageUtil
										.compressImageFromFile(picPath), rotate);
						Data.microBmList.add(bitmap);
						Data.microBmList.add(addNewPic);
						Message msg = handler.obtainMessage(1);
						msg.sendToTarget();
					} else {

					}

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

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_public:
			if (Data.photoList.size() > 0) {
				Intent intent = new Intent();
				setResult(5, intent);
				finish();
			} else {
				Toast.makeText(ChooseMorePicsActivity.this, "您未选择上传的图片",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.back:
			finish();
			break;
		case R.id.fabu:
			fabu.setClickable(false);
			temImageLists.clear();
			for (int i = 0; i < Data.photoList.size(); i++) {
				try {
					// addImageItem(Data.photoList.get(i).getPhotoPath());
					addImageItem(imgAdapter.getPhotoList().get(i)
							.getPhotoPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (temImageLists.size() == 0) {
				Toast.makeText(ChooseMorePicsActivity.this, "请选择图片上传",
						Toast.LENGTH_SHORT).show();
				return;
			}
			a = 0;
			Data.pics1.clear();
			hud.show();
			for (int i = 0; i < temImageLists.size(); i++) {
				// KJHttp kjh = new KJHttp();
				// HttpParams params = new HttpParams();
				// params.putHeaders("token",
				// GetServerUrl.getKeyStr(System.currentTimeMillis()));
				// params.putHeaders("authc",
				// MyApplication.Userinfo.getString("id", ""));
				// params.setContentType("application/octet-stream");
				// // 可多次put，支持多文件上传
				// params.put("sourceId", "");
				// params.put("imagType", "seedling");
				// File file = new File(Data.photoList.get(i).getPhotoPath());
				// if (file.exists())
				// // params.put("file", FileUtils.getSaveFile(Data.photoList
				// // .get(i).getPhotoPath(), "logo.jpg"));
				// params.put("file", file);
				// kjh.post(GetServerUrl.getTestUrl() + "admin/file/image",
				// params, new HttpCallBack() {
				// @Override
				// public void onSuccess(String t) {
				// try {
				// Log.e("onSuccess", t.toString());
				// JSONObject jsonObject = new JSONObject(t
				// .toString());
				// String msg = jsonObject.getString("msg");
				// int code = jsonObject.getInt("code");
				// if (code == 1) {
				// JSONObject image = JsonGetInfo
				// .getJSONObject(JsonGetInfo
				// .getJSONObject(
				// jsonObject,
				// "data"),
				// "image");
				// Data.pics1.add(new Pic(JsonGetInfo
				// .getJsonString(image, "id"),
				// true, JsonGetInfo
				// .getJsonString(image,
				// "url"),
				// Data.photoList.size()));
				// a++;
				// hud.setLabel(
				// a + "/" + Data.photoList.size()
				// + "张").show();
				// if (a == Data.photoList.size()) {
				// if (Data.pics1.size() > 0) {
				// Intent intent = new Intent();
				// setResult(5, intent);
				// finish();
				// }
				// }
				// }
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// super.onSuccess(t);
				// }
				//
				// @Override
				// public void onFailure(int errorNo, String strMsg) {
				// // TODO Auto-generated method stub
				// Log.e("onFailure", strMsg.toString());
				// super.onFailure(errorNo, strMsg);
				// }
				//
				// });

				// RequestParams params1 = new RequestParams();
				// AsyncHttpClient client = new AsyncHttpClient();
				// client.addHeader("token",
				// GetServerUrl.getKeyStr(System.currentTimeMillis()));
				// client.addHeader("authc",
				// MyApplication.Userinfo.getString("id", ""));
				// // client.addHeader("Content-Type",
				// // "application/octet-stream");
				// client.setMaxRetriesAndTimeout(1, 15000);
				// client.setTimeout(15000); // 设置链接超时，如果不设置，默认为10s
				// client.getHttpClient()
				// .getParams()
				// .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
				// true);
				// params1.put("sourceId", "");
				// params1.put("imagType", "seedling");
				// File file1 = new File(Data.photoList.get(i).getPhotoPath());
				// if (file1.exists())
				// // params.put("file", getFileToByte(file));
				// try {
				// params1.put("file", file1, "application/octet-stream");
				// } catch (FileNotFoundException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				// client.post(GetServerUrl.getUrl() + "admin/file/image",
				// params1, new TextHttpResponseHandler() {
				//
				// @Override
				// public void onFailure(int arg0, Header[] arg1,
				// String arg2, Throwable arg3) {
				// // TODO Auto-generated method stub
				// Log.e("onFailure", "onFailure" + arg3);
				// a++;
				// hud.setLabel(
				// a + "/" + Data.photoList.size() + "张")
				// .show();
				// if (a == Data.photoList.size()) {
				// if (Data.pics1.size() > 0) {
				// Intent intent = new Intent();
				// setResult(5, intent);
				// finish();
				// }
				// }
				// if (hud != null) {
				// hud.dismiss();
				// }
				// }
				//
				// @Override
				// public void onSuccess(int arg0, Header[] arg1,
				// String arg2) {
				// // TODO Auto-generated method stub
				// try {
				// Log.e("onSuccess", arg2.toString());
				// JSONObject jsonObject = new JSONObject(arg2
				// .toString());
				// String msg = jsonObject.getString("msg");
				// int code = jsonObject.getInt("code");
				// if (code == 1) {
				// JSONObject image = JsonGetInfo
				// .getJSONObject(JsonGetInfo
				// .getJSONObject(
				// jsonObject,
				// "data"),
				// "image");
				// Data.pics1.add(new Pic(JsonGetInfo
				// .getJsonString(image, "id"),
				// true, JsonGetInfo
				// .getJsonString(image,
				// "url"),
				// Data.photoList.size()));
				// a++;
				// hud.setLabel(
				// a + "/" + Data.photoList.size()
				// + "张").show();
				// if (a == Data.photoList.size()) {
				// if (Data.pics1.size() > 0) {
				// Intent intent = new Intent();
				// setResult(5, intent);
				// finish();
				// }
				// }
				// }
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// }
				//
				// });

				GetServerUrl.addHeaders(finalHttp,true);
				finalHttp.addHeader("Content-Type", "application/octet-stream");
				AjaxParams params1 = new AjaxParams();
				params1.put("sourceId", "");
				File file1 = new File(temImageLists.get(i));
				try {
					params1.put("file", file1);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				params1.put("imagType", "seedling");
				finalHttp.post(GetServerUrl.getUrl() + "admin/file/image",
						params1, new AjaxCallBack<Object>() {

							@Override
							public void onStart() {
								super.onStart();
							}

							@Override
							public void onSuccess(Object t) {
								Log.e("onSuccess", t.toString());
								try {
									JSONObject jsonObject = new JSONObject(t
											.toString());
									String msg = jsonObject.getString("msg");
									int code = jsonObject.getInt("code");
									if (code == 1) {
										JSONObject image = JsonGetInfo
												.getJSONObject(JsonGetInfo
														.getJSONObject(
																jsonObject,
																"data"),
														"image");
										if (a == 0) {
											isCover = true;
										}
										Data.pics1.add(new Pic(JsonGetInfo
												.getJsonString(image, "id"),
												isCover, JsonGetInfo
														.getJsonString(image,
																"url"), a));
										a++;
										isCover = false;
										if (!ChooseMorePicsActivity.this
												.isFinishing()) {
											hud.setProgress(a * 100
													/ Data.photoList.size());
											hud.setProgressText("上传中(" + a
													+ "/"
													+ Data.photoList.size()
													+ "张)");
										}
										if (a == Data.photoList.size()) {
											if (Data.pics1.size() > 0) {
												Intent intent = new Intent();
												setResult(5, intent);
												finish();
											}
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								super.onSuccess(t);
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// TODO Auto-generated method stub
								super.onFailure(t, errorNo, strMsg);
								fabu.setClickable(true);
								a++;
								if (hud != null
										&& !ChooseMorePicsActivity.this
												.isFinishing()) {
									hud.setLabel(
											a + "/" + Data.photoList.size()
													+ "张").show();
								}
								if (a == Data.photoList.size()) {
									if (Data.pics1.size() > 0) {
										Intent intent = new Intent();
										setResult(5, intent);
										finish();
									}
								}
								if (hud != null
										&& !ChooseMorePicsActivity.this
												.isFinishing()) {
									hud.dismiss();
								}
							}
						});

			}

			break;

		default:
			break;
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
		Bitmap bm = CommonBitmapUtil.decodeFile(sourchImagePath, opts,
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
			if (ZzyUtil.ToastForSdcardSpaceEnough(ChooseMorePicsActivity.this,
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
		if (imageSize > CommonBitmapUtil.MAX_IMAGE_SIZE) {
			handler.sendEmptyMessage(PIC_IS_TOO_BIG);
			return;
		}
		Bitmap showbm = null;
		if (degree == 0) {
			showbm = CommonBitmapUtil
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		} else {
			showbm = CommonBitmapUtil
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
			showbm = CommonBitmapUtil.rotate(file.getAbsolutePath(), showbm,
					degree, GRID_COMPRESS_IMAGE_HEIGHT_PX,
					GRID_COMPRESS_IMAGE_WIDTH_PX);
		}
		if (showbm != null) {
			flowerInfoPhotoPath = image_path;
			temImageLists.add(flowerInfoPhotoPath);
			// adapter.addItem(flowerInfoPhotoPath);
			// handler.sendEmptyMessage(ADD_NEW_PIC);
		} else {
			handler.sendEmptyMessage(LOAD_PIC_FAILURE);
			return;
		}
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
			bitmapSourceImg = CommonBitmapUtil.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX, COMPRESS_IMAGE_WIDTH_PX);
		} else {
			bitmapSourceImg = CommonBitmapUtil.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX / 2, COMPRESS_IMAGE_WIDTH_PX / 2);
			bitmapSourceImg = CommonBitmapUtil.rotate(file.getAbsolutePath(),
					bitmapSourceImg, degree, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
		}
		String img_path = "";
		img_path = com.white.utils.FileUtil.getFlowerPicPath("") + "/"
				+ "flower_image_" + System.currentTimeMillis() + ".png";
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

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] getFileToByte(File file) {
		byte[] by = new byte[(int) file.length()];
		try {
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			byte[] bb = new byte[2048];
			int ch;
			ch = is.read(bb);
			while (ch != -1) {
				bytestream.write(bb, 0, ch);
				ch = is.read(bb);
			}
			by = bytestream.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return by;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		fabu.setClickable(true);
		if (finalHttp != null) {
		}
		super.onBackPressed();
	}
}
