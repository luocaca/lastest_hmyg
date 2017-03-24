package com.hldj.hmyg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.saler.CoreActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.white.utils.FileUtil;
import com.white.utils.StringUtil;
import com.zzy.common.widget.galleryView.ImageGalleryPageAdapter;
import com.zzy.common.widget.galleryView.ImageGalleryView;
import com.zzy.common.widget.galleryView.SingleTapListener;

/**
 */
public class GalleryImageActivity extends CoreActivity implements
		OnTouchListener, SingleTapListener, PlatformActionListener {

	private static final String INTENT_CURRENT_PAGE = "intent_current_page";
	private static final String INTENT_URL_LIST = "intent_url_list";

	private static final int SAVE_IMAGE_SUCCESS = 1;

	private static final int SAVE_IMAGE_FAILURE = 2;

	public static final int PAGE_VIEW_HIDDEN = 22;

	private ImageGalleryView imageGallery;

	private float beforeLenght = 0.0f;
	private float afterLenght = 0.0f;
	private boolean isScale = false;
	private float currentScale = 1.0f;

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();

	private int currentPage = 0;

	private ImageGalleryPageAdapter adapter;

	private TextView pageTv;
	private Handler handler;
	private Timer timer;
	// private ZzyDialog dialog;
	private String mSavePath;
	private boolean mCancelUpdate = false;
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;

	public static void startGalleryImageActivity(Context context,
			int currentPage, ArrayList<Pic> urlPaths) {
		Intent intent = new Intent(context, GalleryImageActivity.class);
		intent.putExtra(INTENT_CURRENT_PAGE, currentPage);
		Bundle bundleObject = new Bundle();
		final PicSerializableMaplist myMap = new PicSerializableMaplist();
		myMap.setMaplist(urlPaths);
		bundleObject.putSerializable("INTENT_URL_LIST", myMap);
		intent.putExtras(bundleObject);
		context.startActivity(intent);
	}

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	@Override
	protected void doOnCreate(Bundle savedInstanceState) {
		setContentView(R.layout.gallery_image_layout);
		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		currentPage = getIntent().getIntExtra(INTENT_CURRENT_PAGE, 0);
		Bundle bundle = getIntent().getExtras();
		urlPaths =( (PicSerializableMaplist) bundle
				.get("INTENT_URL_LIST")).getMaplist();

		initView();
		initListener();
		initContent();
	}

	private void initView() {
		imageGallery = (ImageGalleryView) findViewById(R.id.gallery_image);
		pageTv = (TextView) findViewById(R.id.pageTv);
	}

	private void initListener() {
		handler = new RefreshHandler(getMainLooper());
	}

	private void setPointView() {
		pageTv.setVisibility(View.VISIBLE);
		String text = String.format(
				getResources().getString(R.string.cur_page), currentPage + 1,
				urlPaths.size());
		pageTv.setText(text);
		timeToPointLayout();
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > urlPaths.size() - 1
				|| currentPage == position) {
			return;
		}
		currentPage = position;
		String text = String.format(
				getResources().getString(R.string.cur_page), currentPage + 1,
				urlPaths.size());
		pageTv.setText(text);
		pageTv.setVisibility(View.VISIBLE);
		timer = new Timer();
		timeToPointLayout();
	}

	private void timeToPointLayout() {
		timerRelease();
		if (timer == null)
			timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (handler != null)
					handler.sendEmptyMessage(PAGE_VIEW_HIDDEN);
			}
		}, 1000);
	}

	private void timerRelease() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	private void initContent() {
		imageGallery.setVerticalFadingEdgeEnabled(false);
		imageGallery.setHorizontalFadingEdgeEnabled(false);
		adapter = new ImageGalleryPageAdapter(GalleryImageActivity.this,
				urlPaths);
		imageGallery.setAdapter(adapter);
		imageGallery.setSelection(currentPage);
		imageGallery.setSingleTapListener(this);
		imageGallery
				.setOnItemLongClickListener(new ImageOnItemLongClickListener());

		imageGallery.setOnItemSelectedListener(new GalleryChangeListener());
		setPointView();
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class ImageOnItemLongClickListener implements
			OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {

			// dialog = new ZzyDialog(GalleryImageActivity.this);
			// dialog.setContentText(R.string.gallery_image_save);
			// dialog.setConfirmBtnListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// saveImage(position);
			// dialog.dismiss();
			// }
			// });
			// dialog.setCancelBtnListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// }
			// });
			// dialog.show();
			
			boolean requesCallPhonePermissions = new PermissionUtils(
					GalleryImageActivity.this)
					.requestReadSDCardPermissions(200);
			if (requesCallPhonePermissions) {
				final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
						GalleryImageActivity.this);
				dialog.content("您可以将图片保存到本地相册或者分享给微信好友").btnNum(3).btnText("保存到相册", "分享给微信好友", "取消")//
						.showAnim(mBasIn)//
						.dismissAnim(mBasOut)//
						.show();

				dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
							@Override
							public void onBtnClick() {
								saveImage(position);
								dialog.dismiss();
							}
						}, new OnBtnClickL() {// left btn click listener
							@Override
							public void onBtnClick() {
								ShareToWechat(position);
								dialog.dismiss();
								
								
							}
						}, new OnBtnClickL() {// right btn click listener
							@Override
							public void onBtnClick() {
								dialog.dismiss();
							}
						});

			}

		
			return true;
		}

	}

	private void saveImage(int pos) {
		SaveImageThread downloadApk = new SaveImageThread(pos);
		downloadApk.start();
	}
	
	
	
	private void ShareToWechat(int position) {
		ShareParams sp1 = new ShareParams();
		sp1.setShareType(Platform.SHARE_IMAGE);
		if(StringUtil.isHttpUrlPicPath(urlPaths.get(position).getUrl())){
			sp1.setImageUrl(urlPaths.get(position).getUrl());
		}else {
			sp1.setImagePath(urlPaths.get(position).getUrl());
		}
		
		Platform Wechat = ShareSDK.getPlatform("Wechat");
		Wechat.setPlatformActionListener(this);
		Wechat.share(sp1);
	}

	/**
	 * 
	 */
	private class SaveImageThread extends Thread {

		private int pos;

		public SaveImageThread(int pos) {
			super();
			this.pos = pos;
		}

		@Override
		public void run() {
			try {
				File tempfile = ImageLoader.getInstance().getDiskCache()
						.get(urlPaths.get(pos).getUrl());
				if (tempfile.length() > 0) {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						String sdPath = FileUtil
								.getPath_Space(GalleryImageActivity.this)
								+ File.separator + "saveImage";
						long time = System.currentTimeMillis();
						mSavePath = sdPath + File.separator + time + ".jpg";
						File file = new File(sdPath);
						if (!file.exists()) {
							file.mkdirs();
						}
						File apkFile = new File(mSavePath);
						InputStream is = new FileInputStream(tempfile);
						FileOutputStream fos = new FileOutputStream(apkFile);
						byte buf[] = new byte[1024];
						int acount = 0;
						int numReaded = 0;
						do {
							numReaded = is.read(buf);
							acount = acount + numReaded;
							if (numReaded <= 0) {
								String text = getResources().getString(
										R.string.feed_has_save_to_album)
										+ mSavePath;
								Message msg = new Message();
								msg.what = SAVE_IMAGE_SUCCESS;
								msg.obj = text;
								handler.sendMessage(msg);
								break;
							}
							fos.write(buf, 0, numReaded);
						} while (!mCancelUpdate);
						fos.close();
						is.close();

					}
				}

			} catch (Exception e) {
				handler.sendEmptyMessage(SAVE_IMAGE_FAILURE);
			}
		}

	}

	private class GalleryChangeListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			setcurrentPoint(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			beforeLenght = spacing(event);
			if (beforeLenght > 5f) {
				isScale = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isScale) {
				afterLenght = spacing(event);
				if (afterLenght < 5f)
					break;
				float gapLenght = afterLenght - beforeLenght;
				if (gapLenght == 0) {
					break;
				} else if (Math.abs(gapLenght) > 5f) {
					float scaleRate = gapLenght / 854;
					Animation myAnimation_Scale = new ScaleAnimation(
							currentScale, currentScale + scaleRate,
							currentScale, currentScale + scaleRate,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					myAnimation_Scale.setDuration(100);
					myAnimation_Scale.setFillAfter(true);
					myAnimation_Scale.setFillEnabled(true);
					currentScale = currentScale + scaleRate;
					imageGallery.getSelectedView().setLayoutParams(
							new Gallery.LayoutParams(
									(int) (480 * (currentScale)),
									(int) (854 * (currentScale))));
					beforeLenght = afterLenght;
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			isScale = false;
			break;
		}
		return false;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private class RefreshHandler extends Handler {

		public RefreshHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PAGE_VIEW_HIDDEN:
				timerRelease();
				pageTv.setVisibility(View.VISIBLE);
//				pageTv.setVisibility(View.GONE);
				break;
			case SAVE_IMAGE_SUCCESS:
				String text = msg.obj.toString();
				ToastUtil.showShortToast(GalleryImageActivity.this, text);
				break;
			case SAVE_IMAGE_FAILURE:
				ToastUtil.showShortToast(GalleryImageActivity.this,
						R.string.save_image_failure);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler = null;
		timerRelease();
		adapter = null;
	}

	@Override
	public void doSngleTap() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GalleryImageActivity.this.finish();
			}
		});
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}
}
