package com.hldj.hmyg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.application.StateBarUtil;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PlatformForShare;
import com.hldj.hmyg.buyer.PurchaseDemandListActivity;
import com.hy.utils.FlowTagActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.Loading;
import com.hy.utils.TagViewActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.StatusBarUtil;
import com.soundcloud.android.crop.Crop;
import com.xingguo.huang.mabiwang.util.CacheUtils;
import com.xingguo.huang.mabiwang.util.PictureManageUtil;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zym.selecthead.config.Configs;
import com.zym.selecthead.tools.FileTools;
import com.zym.selecthead.tools.SelectHeadTools;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的 界面
 */
public class EActivity extends LoginActivity implements PlatformActionListener {

	private Dialog dialog;
	private String platform = "1,2,3,4,5,6,7,8";
	private ArrayList<PlatformForShare> shares = new ArrayList<PlatformForShare>();
	private Context mContext = this;
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;
	private ImageView iv_msg;
	private FinalBitmap fb;
	private CircleImageView iv_icon_persion_pic;
	private TextView tv_user_name;
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	String dateTime;
	String targeturl = null;
	private static final int SCALE = 5;// 照片缩小比例
	private Bitmap bitmap;
	private Loading loading;

	private Uri photoUri = null;
	private Bitmap cropBitmap = null;
	private String img = GetServerUrl.ICON_PAHT;

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	private static final String TAG = "EActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);//不能侧滑
		Log.e(TAG, "onCreate: " );


		setContentView(R.layout.activity_e);
		fb = FinalBitmap.create(this);

		if (platform.contains("1")) {
			PlatformForShare platformForShare = new PlatformForShare("朋友圈",
					"WechatMoments", "1", R.drawable.sns_icon_23);
			shares.add(platformForShare);
		}
		if (platform.contains("2")) {
			PlatformForShare platformForShare = new PlatformForShare("微信好友",
					"Wechat", "2", R.drawable.sns_icon_22);
			shares.add(platformForShare);
		}
		if (platform.contains("3")) {
			PlatformForShare platformForShare = new PlatformForShare("新浪微博",
					"SinaWeibo", "3", R.drawable.sns_icon_1);
			shares.add(platformForShare);
		}
		if (platform.contains("4")) {
			PlatformForShare platformForShare = new PlatformForShare("QQ好友",
					"QZone", "4", R.drawable.sns_icon_24);
			shares.add(platformForShare);
		}

		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		iv_icon_persion_pic = (CircleImageView) findViewById(R.id.iv_icon_persion_pic);
		ImageView iv_setting = (ImageView) findViewById(R.id.iv_setting);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);
		LinearLayout ll_buyer = (LinearLayout) findViewById(R.id.ll_buyer);
		LinearLayout ll_saler = (LinearLayout) findViewById(R.id.ll_saler);
		ll_jingji = (LinearLayout) findViewById(R.id.ll_jingji);
		LinearLayout ll_personal_profile = (LinearLayout) findViewById(R.id.ll_personal_profile);
		LinearLayout ll_icon_message = (LinearLayout) findViewById(R.id.ll_icon_message);
		LinearLayout ll_friends = (LinearLayout) findViewById(R.id.ll_friends);
		LinearLayout ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		LinearLayout ll_feek_back = (LinearLayout) findViewById(R.id.ll_feek_back);

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		iv_setting.setOnClickListener(multipleClickProcess);
		iv_msg.setOnClickListener(multipleClickProcess);
		ll_buyer.setOnClickListener(multipleClickProcess);
		ll_saler.setOnClickListener(multipleClickProcess);
		ll_jingji.setOnClickListener(multipleClickProcess);
		ll_personal_profile.setOnClickListener(multipleClickProcess);
		ll_icon_message.setOnClickListener(multipleClickProcess);
		ll_friends.setOnClickListener(multipleClickProcess);
		ll_phone.setOnClickListener(multipleClickProcess);
		ll_feek_back.setOnClickListener(multipleClickProcess);
		iv_icon_persion_pic.setOnClickListener(multipleClickProcess);
		iv_msg.setBackgroundResource(R.drawable.icon_msg);
		if (MyApplication.Userinfo.getBoolean("isLogin", false)
				&& iv_icon_persion_pic != null) {
			if (!"".equals(MyApplication.Userinfo.getString("headImage", ""))) {
				ImageLoader.getInstance().displayImage(
						MyApplication.Userinfo.getString("headImage", ""),
						iv_icon_persion_pic);
			}
			getUserInfo(MyApplication.Userinfo.getString("id", ""),
					"SetProfileActivity");
			tv_user_name.setText(MyApplication.Userinfo.getString(
					"showUserName", ""));
			if (MyApplication.Userinfo.getBoolean("isAgent", false)) {
				ll_jingji.setVisibility(View.VISIBLE);
			} else {
				ll_jingji.setVisibility(View.GONE);
			}
		} else {
			iv_icon_persion_pic.setImageResource(R.drawable.icon_persion_pic);
			tv_user_name.setText("");
		}

	}

	@Override
	protected void onResume() {
		Log.e(TAG, "onResume: " );
//		StateBarUtil.setColorPrimaryDark(R.color.main_color,this.getWindow());
		// TODO Auto-generated method stub
		if (iv_msg != null) {
			unReadCount();
		}
		if (MyApplication.Userinfo.getBoolean("isLogin", false)
				&& iv_icon_persion_pic != null) {
			getUserInfo(MyApplication.Userinfo.getString("id", ""),
					"SetProfileActivity");
			if (!"".equals(MyApplication.Userinfo.getString("headImage", ""))) {
				ImageLoader.getInstance().displayImage(
						MyApplication.Userinfo.getString("headImage", ""),
						iv_icon_persion_pic);
			}
			tv_user_name.setText(MyApplication.Userinfo.getString(
					"showUserName", ""));
			if (MyApplication.Userinfo.getBoolean("isAgent", false)) {
				ll_jingji.setVisibility(View.VISIBLE);
			} else {
				ll_jingji.setVisibility(View.GONE);
			}
		} else {
			iv_icon_persion_pic.setImageResource(R.drawable.icon_persion_pic);
			tv_user_name.setText("");
		}
		super.onResume();
	}

	private void unReadCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/notice/unReadCount",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if ("1".equals(code)) {

								int unReadCount = JsonGetInfo.getJsonInt(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "unReadCount");
								if (unReadCount > 0) {
									iv_msg.setBackgroundResource(R.anim.anim_message);
									AnimationDrawable anim = (AnimationDrawable) iv_msg
											.getBackground();
									anim.start();
								} else {
									iv_msg.setBackgroundResource(R.drawable.icon_msg);
								}
							} else {

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
					}

				});
	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
					Intent toLoginActivity = new Intent(EActivity.this,
							LoginActivity.class);
					startActivityForResult(toLoginActivity, 4);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					return;
				}
				switch (view.getId()) {
				case R.id.iv_setting:

					// Intent demo = new Intent(EActivity.this,
					// TagViewActivity.class);
					// startActivityForResult(demo, 6);
					// getParent().overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					// Intent demo = new Intent(EActivity.this,
					// BarCodeTestActivity.class);
					// startActivityForResult(demo, 6);
					// getParent().overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					// Intent demo = new Intent(EActivity.this,
					// FlowTagActivity.class);
					// startActivityForResult(demo, 6);
					// getParent().overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					// Intent demo = new Intent(EActivity.this,
					// TagActivity.class);
					// startActivityForResult(demo, 6);
					// getParent().overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);




					Intent toSettingActivity = new Intent(EActivity.this,
							SettingActivity.class);
					startActivityForResult(toSettingActivity, 6);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);

					break;
				case R.id.iv_msg:
					Intent toMessageListActivity = new Intent(EActivity.this,
							MessageListActivity.class);
					startActivity(toMessageListActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;
				case R.id.iv_icon_persion_pic:
					if (!FileTools.hasSdcard()) {
						Toast.makeText(EActivity.this, "没有找到SD卡，请检查SD卡是否存在",
								Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						photoUri = FileTools.getUriByFileDirAndFileName(
								Configs.SystemPicture.SAVE_DIRECTORY,
								Configs.SystemPicture.SAVE_PIC_NAME);
					} catch (IOException e) {
						Toast.makeText(EActivity.this, "创建文件失败。",
								Toast.LENGTH_SHORT).show();
						return;
					}
					setPics();
					break;
				case R.id.ll_buyer:
					Intent toBuyerActivity = new Intent(EActivity.this,
							BuyerActivity.class);
					startActivity(toBuyerActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;
				case R.id.ll_saler:
					Intent toSalerActivity = new Intent(EActivity.this,
							SalerActivity.class);
					startActivity(toSalerActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;
				case R.id.ll_jingji:
					Intent toJingjiActivity = new Intent(EActivity.this,
							BrokerActivity.class);
					startActivity(toJingjiActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;
				case R.id.ll_personal_profile:
					Intent toSetProfileActivity = new Intent(EActivity.this,
							SetProfileActivity.class);
					startActivity(toSetProfileActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					// Intent toSetPasswardActivity = new Intent(EActivity.this,
					// SetPasswardActivity.class);
					// startActivity(toSetPasswardActivity);
					// getParent().overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);
					break;
				case R.id.ll_icon_message:
					Intent toSafeAcountActivity = new Intent(EActivity.this,
							SafeAcountActivity.class);
					startActivity(toSafeAcountActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;
				case R.id.ll_friends:
					// Share();
					// showShare();
					showDialog();
					break;
				case R.id.ll_phone:
					Call_Phone();
					break;
				case R.id.ll_feek_back:
					Intent toFeedBackActivity = new Intent(EActivity.this,
							FeedBackActivity.class);
					startActivity(toFeedBackActivity);
					getParent().overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_left);
					break;

				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void Share() {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.setType("text/*");
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
			sendIntent.putExtra(Intent.EXTRA_TEXT, "花木易购" + GetServerUrl.WEB);
			startActivity(sendIntent);
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

	private void Customer_Care_Phone() {
		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				mContext);
		dialog.isTitleShow(false)
		//
				.btnNum(3).content("请选择花木易购客服联系方式")//
				.btnText("电话客服", "取消", "QQ客服")//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
					@Override
					public void onBtnClick() {

						dialog.dismiss();
					}
				}, new OnBtnClickL() {// right btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				}, new OnBtnClickL() {// middle btn click listener
					@Override
					public void onBtnClick() {

						dialog.dismiss();
					}
				});
	}

	public void Call_Phone() {
		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				mContext);
		dialog.title("客服热线4006-579-888").content("客服热线 周一至周日9:00-17:30")//
				.btnText("确认拨号", "取消")//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:"
										+ GetServerUrl.Customer_Care_Phone));
						// Intent intent = new Intent(Intent.ACTION_DIAL, Uri
						// .parse("tel:"
						// + GetServerUrl.Customer_Care_Phone));
						startActivity(intent);
					}
				}, new OnBtnClickL() {// right btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				});
	}

	public void setPics() {
		new ActionSheetDialog(EActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("拍照", SheetItemColor.Red,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								boolean requestCamerPermissions = new PermissionUtils(
										EActivity.this)
										.requestCamerPermissions(200);
								if (requestCamerPermissions) {
									SelectHeadTools.startCamearPicCut(
											EActivity.this, photoUri);
								}

							}
						})
				.addSheetItem("从相册选择", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								boolean requestReadSDCardPermissions = new PermissionUtils(
										EActivity.this)
										.requestReadSDCardPermissions(200);
								if (requestReadSDCardPermissions) {
									Crop.pickImage(EActivity.this);
								}

							}
						})
				.addSheetItem("查看大图", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								if (!"".equals(MyApplication.Userinfo
										.getString("headImage", ""))) {
									ArrayList<Pic> ossUrls = new ArrayList<Pic>();
									ossUrls.add(new Pic("", false,
											MyApplication.Userinfo.getString(
													"headImage", ""), 0));
									GalleryImageActivity
											.startGalleryImageActivity(
													EActivity.this, 0, ossUrls);
								} else {
									Toast.makeText(EActivity.this,
											"您还未设置头像，赶紧拍张靓照吧",
											Toast.LENGTH_SHORT).show();
								}

							}
						}).show();
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
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

	private String imagType_tag = "";
	private LinearLayout ll_jingji;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 6:
			if (resultCode == 6) {
				MainActivity.toA();
			}
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
			Log.i("onActivityResult", "PHOTO_REQUEST_TAKEPHOTO");
			beginCrop(photoUri);
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_GALLERY:// 相册获取
			if (data != null)
				photoUri = data.getData();
			SelectHeadTools.startPhotoZoom(this, photoUri, 600);
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_CUT: // 接收处理返回的图片结果，这个过程比较重要
			Log.i("onActivityResult", "PHOTO_REQUEST_CUT");
			if (photoUri == null)
				break;
			/*
			 * Bitmap bit = data.getExtras().getParcelable("data");
			 * //不要再用data的方式了，会出现activity result 的时候data == null的空的情况
			 * iv_show.setImageBitmap(bit);
			 */
			try {
				cropBitmap = getBitmapFromUri(photoUri, this); // 通过获取uri的方式，直接解决了报空和图片像素高的oom问题
				if (cropBitmap != null) {
					iv_icon_persion_pic.setImageBitmap(cropBitmap);
					Log.i("onActivityResult", "setImageBitmap");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// 下面可以用来上传pc服务端
			File file = FileTools.getFileByUri(this, photoUri);
			Log.d("File", file.toString());
			break;
		}
		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			beginCrop(data.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}
	}

	public Bitmap getBitmapFromUri(Uri uri, Context mContext) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					mContext.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
		Crop.of(source, destination).asSquare().start(this);
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			// iv_icon_persion_pic.setImageDrawable(null);
			// iv_icon_persion_pic.setImageURI(Crop.getOutput(result));
			File file = FileTools.getFileByUri(this, Crop.getOutput(result));
			iv_icon_persion_pic.setImageBitmap(getBitmapFromUri(
					Crop.getOutput(result), EActivity.this));
			ImageLoader.getInstance().displayImage(file.getAbsolutePath(),
					iv_icon_persion_pic);
			updateImage(file.getAbsolutePath(), imagType_tag, "");
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils
				.getCacheDirectory(EActivity.this, true, "pic")
				+ dateTime
				+ "_11";
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

	public void updateImage(String url, String imagType, String sourceId) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		int rotate = PictureManageUtil.getCameraPhotoOrientation(url);
		// 把压缩的图片进行旋转
		params.put(
				"headImage",
				new ByteArrayInputStream(Bitmap2Bytes(PictureManageUtil
						.rotateBitmap(PictureManageUtil.getCompressBm(url),
								rotate))), System.currentTimeMillis() + ".png");

		finalHttp.post(GetServerUrl.getUrl() + "admin/file/uploadHeadImage",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onStart() {

						if (loading != null && !EActivity.this.isFinishing()) {
							loading.showToastAlong();
						} else if (loading == null
								&& !EActivity.this.isFinishing()) {
							loading = new Loading(EActivity.this, "头像修改中.....");
							loading.showToastAlong();
						}
						super.onStart();
					}

					@Override
					public void onSuccess(Object t) {
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String msg = jsonObject.getString("msg");
							int code = jsonObject.getInt("code");
							if (code == 1) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								String headImage = JsonGetInfo.getJsonString(
										data, "headImage");
								e.putString("headImage", headImage);
								e.commit();
								if (!"".equals(MyApplication.Userinfo
										.getString("headImage", ""))) {
									ImageLoader.getInstance().displayImage(
											MyApplication.Userinfo.getString(
													"headImage", ""),
											iv_icon_persion_pic);
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (loading != null && !EActivity.this.isFinishing()) {
							loading.cancel();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						if (loading != null && !EActivity.this.isFinishing()) {
							loading.cancel();
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	private void showShare() {

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		// oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("欢迎使用花木易购代购型苗木交易平台！");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(Data.share);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(Data.share);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(Data.share);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(Data.share);

		// 启动分享GUI
		oks.show(this);
	}

	class SharePlatformAdapter extends BaseAdapter {

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shares.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View inflate = getLayoutInflater().inflate(
					R.layout.list_item_share_platform, null);
			ImageView iv_icon = (ImageView) inflate.findViewById(R.id.iv_icon);
			TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
			iv_icon.setImageResource(shares.get(position).getPic());
			tv_name.setText(shares.get(position).getName());
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!EActivity.this.isFinishing() && dialog != null
							&& dialog.isShowing()) {
						dialog.cancel();
					}

					if ("WechatMoments".equals(shares.get(position).getEname())) {
						ShareToWechatMoments();
					} else if ("Wechat".equals(shares.get(position).getEname())) {
						ShareToWechat();
					} else if ("SinaWeibo".equals(shares.get(position)
							.getEname())) {
						ShareToSinaWeibo();
					} else if ("QZone".equals(shares.get(position).getEname())) {
						ShareToQzone();
					}

				}
			});
			return inflate;
		}
	}

	private void ShareToQzone() {
		ShareParams sp5 = new ShareParams();
		sp5.setTitle("欢迎使用花木易购代购型苗木交易平台！");
		sp5.setTitleUrl(Data.share); // 标题的超链接
		sp5.setText("苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。");
		sp5.setImageUrl(img);
		sp5.setSite(getString(R.string.app_name));
		sp5.setSiteUrl(Data.share);
		Platform qzone = ShareSDK.getPlatform(QQ.NAME);
		qzone.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp5);
	}

	private void ShareToSinaWeibo() {
		ShareParams sp3 = new ShareParams();
		sp3.setText("苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。");
		// sp3.setImagePath("/mnt/sdcard/share/" + system_time + ".jpg");
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp3);
	}

	private void ShareToWechat() {
		ShareParams sp1 = new ShareParams();
		sp1.setShareType(Platform.SHARE_WEBPAGE);
		sp1.setTitle("欢迎使用花木易购代购型苗木交易平台！");
		sp1.setText("苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。");
		sp1.setImageUrl(img);
		sp1.setUrl(Data.share);
		sp1.setSiteUrl(Data.share);
		Platform Wechat = ShareSDK.getPlatform("Wechat");
		Wechat.setPlatformActionListener(this);
		Wechat.share(sp1);
	}

	private void ShareToWechatMoments() {
		ShareParams sp2 = new ShareParams();
		sp2.setShareType(Platform.SHARE_WEBPAGE);
		sp2.setTitle("欢迎使用花木易购代购型苗木交易平台！");
		sp2.setText("苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。");
		sp2.setImageUrl(img);
		sp2.setUrl(Data.share);
		sp2.setTitleUrl(Data.share);
		sp2.setSiteUrl(Data.share);
		Platform Wechat_men = ShareSDK.getPlatform("WechatMoments");
		Wechat_men.setPlatformActionListener(this);
		Wechat_men.share(sp2);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(EActivity.this, "分享出错", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(EActivity.this, "分享已取消", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		String expName = arg0.getName();
		if ("SinaWeibo".equals(expName)) {
			platform = "3";
		} else if ("QZone".equals(expName)) {
			platform = "4";
		} else if ("Wechat".equals(expName)) {
			platform = "2";
		} else if ("WechatMoments".equals(expName)) {
			platform = "1";
		}

	}

	private void showDialog() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_share, null);
		GridView gridView = (GridView) dia_choose_share
				.findViewById(R.id.gridView);
		Button btn_cancle = (Button) dia_choose_share
				.findViewById(R.id.btn_cancle);
		gridView.setAdapter(new SharePlatformAdapter());
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(dia_choose_share, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dia_choose_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!EActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!EActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!EActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!EActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

}
