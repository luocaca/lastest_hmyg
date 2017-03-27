package com.zzy.flowers.activity.photoalbum;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.broker.AddCarActivity;
import com.hldj.hmyg.broker.SaveMarketPriceActivity;
import com.hldj.hmyg.buyer.SerializableMaplist;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.hldj.hmyg.saler.CoreActivity;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
import com.hldj.hmyg.saler.UpdataImageActivity;
import com.white.utils.ExpandViewTouchUtil;
import com.zzy.common.widget.galleryView.ImageGalleryView;
import com.zzy.common.widget.galleryView.SingleTapListener;
import com.zzy.flowers.activity.imageedit.EditImageGalleryPageAdapter;

public class EditGalleryImageActivity extends CoreActivity implements
		OnTouchListener, SingleTapListener {

	private static final String INTENT_MODE = "intent_mode";
	private static final String INTENT_CURRENT_PAGE = "intent_current_page";
	private static final String INTENT_URL_LIST = "intent_url_list";
	public static final int TO_EDIT_PRICE_IMAGE = 0;
	public static final int TO_EDIT_PUBLISH_IMAGE = 1;
	public static final int TO_EDIT_SELLER_SEED_IMAGE = 2;
	public static final int PAGE_VIEW_HIDDEN = 22;

	private TextView titleTv;
	private Button backBtn;
	private Button delBtn;

	private ImageGalleryView imageGallery;

	private float beforeLenght = 0.0f;
	private float afterLenght = 0.0f;
	private boolean isScale = false;
	private float currentScale = 1.0f;

	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();

	private int currentPage = 0;

	private EditImageGalleryPageAdapter adapter;

	private TextView pageTv;
	private Handler handler;
	private Timer timer;

	private int mode;

	public static void startEditGalleryImageActivity(Context context, int mode,
			int currentPage, ArrayList<Pic> urlPaths) {

		Intent intent = new Intent(context, EditGalleryImageActivity.class);
		Bundle bundleObject = new Bundle();
		final PicSerializableMaplist myMap = new PicSerializableMaplist();
		myMap.setMaplist(urlPaths);
		bundleObject.putSerializable("INTENT_URL_LIST", myMap);
		intent.putExtras(bundleObject);
		intent.putExtra(INTENT_MODE, mode);
		intent.putExtra(INTENT_CURRENT_PAGE, currentPage);
		// intent.putStringArrayListExtra(INTENT_URL_LIST, urlPaths);
		context.startActivity(intent);
	}

	@Override
	protected void doOnCreate(Bundle savedInstanceState) {
		setContentView(R.layout.edit_gallery_image_layout);

		mode = getIntent().getIntExtra(INTENT_MODE, TO_EDIT_PRICE_IMAGE);
		currentPage = getIntent().getIntExtra(INTENT_CURRENT_PAGE, 0);
		Bundle bundle = getIntent().getExtras();
		urlPaths = ((PicSerializableMaplist) bundle.get("INTENT_URL_LIST"))
				.getMaplist();
		// urlPaths = getIntent().getStringArrayListExtra(INTENT_URL_LIST);
		initView();
		initListener();
		initContent();
	}

	private void initView() {
		titleTv = (TextView) findViewById(R.id.common_titlebar_titleName);
		backBtn = (Button) findViewById(R.id.common_titlebar_leftBtn);
		delBtn = (Button) findViewById(R.id.common_titlebar_rightBtn);
		backBtn.setText("返回");
		delBtn.setText(R.string.delete);

		imageGallery = (ImageGalleryView) findViewById(R.id.gallery_image);
		pageTv = (TextView) findViewById(R.id.pageTv);

		titleTv.setText(R.string.review_image);
	}

	private void initListener() {
		handler = new RefreshHandler(getMainLooper());

		BtnOnClickListener listener = new BtnOnClickListener();
		backBtn.setOnClickListener(listener);
		delBtn.setOnClickListener(listener);
	}

	private class BtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.common_titlebar_leftBtn:
				EditGalleryImageActivity.this.finish();
				break;
			case R.id.common_titlebar_rightBtn:
				if (mode == TO_EDIT_PRICE_IMAGE) {
				} else if (mode == TO_EDIT_PUBLISH_IMAGE) {
					if (UpdataImageActivity.instance != null) {
						UpdataImageActivity.instance.removePicUrls(currentPage);
					} else if (SaveSeedlingActivity.instance != null) {
						SaveSeedlingActivity.instance
								.removePicUrls(currentPage);
					} else if (SaveMiaoActivity.instance != null) {
						SaveMiaoActivity.instance.removePicUrls(currentPage);
					}else if (SaveMarketPriceActivity.instance != null) {
						SaveMarketPriceActivity.instance.removePicUrls(currentPage);
					}else if (AddCarActivity.instance != null) {
						AddCarActivity.instance.removePicUrls(currentPage);
					}

				} else {
				}
				urlPaths.remove(currentPage);
				if (urlPaths.size() == 0) {
					EditGalleryImageActivity.this.finish();
					return;
				}
				setPointView();
				adapter.refreshView();
				break;
			default:
				break;
			}
		}
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
		adapter = new EditImageGalleryPageAdapter(
				EditGalleryImageActivity.this, urlPaths);
		imageGallery.setAdapter(adapter);
		imageGallery.setSelection(currentPage);
		imageGallery.setSingleTapListener(this);

		imageGallery.setOnItemSelectedListener(new GalleryChangeListener());
		setPointView();

		ExpandViewTouchUtil.expandViewTouchDelegate(backBtn, 30, 30, 20, 30);
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
		return (float) Math.sqrt(x * x + y * y);
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
				pageTv.setVisibility(View.GONE);
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
				EditGalleryImageActivity.this.finish();
			}
		});
	}

}
