package com.hldj.hmyg.saler;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hy.utils.ToastUtil;
import com.white.utils.ExpandViewTouchUtil;
import com.white.utils.FileUtil;
import com.white.utils.GlobalData;
import com.white.utils.ImageTools;
import com.zzy.common.widget.galleryView.ImageGalleryView;
import com.zzy.common.widget.galleryView.SingleTapListener;
import com.zzy.flowers.activity.photoalbum.ChoosePhotoGalleryAdapter;
import com.zzy.flowers.activity.photoalbum.PhotoActivity;
import com.zzy.flowers.activity.photoalbum.PhotoItem;

public class ChoosePhotoGalleryActivity extends CoreActivity implements
		OnTouchListener, SingleTapListener {

	public static final String INTENT_CURRENT_PAGE_KEY = "intent_current_page";

	public static final int COMPRESS_IMAGE_OVER = 10;

	private RelativeLayout titleLayout;
	private Button backBtn;
	private CheckBox isCheckBtn;
	private ImageGalleryView imageGallery;
	private RelativeLayout bottomLayout;
	private CheckBox isSourceCheckBtn;
	private TextView sourceSizeTv;
	private TextView chooseNumTv;
	private Button sendBtn;

	private List<PhotoItem> list = new ArrayList<PhotoItem>();

	private float beforeLenght = 0.0f;
	private float afterLenght = 0.0f;
	private boolean isScale = false;
	private float currentScale = 1.0f;

	private ChoosePhotoGalleryAdapter adapter;

	private Handler handler;

	private int currentPage;

	public static void startChoosePhotoGalleryActivity(PhotoActivity context,
			int currentPage, boolean isSendSourcePic, int requestCode) {
		Intent intent = new Intent(context, ChoosePhotoGalleryActivity.class);
		intent.putExtra(INTENT_CURRENT_PAGE_KEY, currentPage);
		context.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void doOnCreate(Bundle savedInstanceState) {
		setContentView(R.layout.chat_photo_gallery_image_layout);

		list = GlobalData.galleryImageData;
		currentPage = getIntent().getIntExtra(INTENT_CURRENT_PAGE_KEY, 0);
		initView();
		initListener();
		initContent();
	}

	private void initView() {
		titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
		backBtn = (Button) findViewById(R.id.common_titlebar_leftBtn);
		isCheckBtn = (CheckBox) findViewById(R.id.photo_choose_check_iv);
		imageGallery = (ImageGalleryView) findViewById(R.id.gallery_image);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
		isSourceCheckBtn = (CheckBox) findViewById(R.id.photo_choose_source_check_iv);
		sourceSizeTv = (TextView) findViewById(R.id.choose_photo_source_size_tv);
		chooseNumTv = (TextView) findViewById(R.id.choose_photo_num_tv);
		sendBtn = (Button) findViewById(R.id.choose_photo_send_btn);

		backBtn.setText("      ");
	}

	private void initListener() {
		handler = new PhotoHandler(getMainLooper());
		BtnOnClickListener listener = new BtnOnClickListener();
		backBtn.setOnClickListener(listener);
		sendBtn.setOnClickListener(listener);
		isCheckBtn.setOnClickListener(listener);
		sourceSizeTv.setOnClickListener(listener);
		isSourceCheckBtn.setOnClickListener(listener);
	}

	private void initContent() {
		imageGallery.setVerticalFadingEdgeEnabled(false);
		imageGallery.setHorizontalFadingEdgeEnabled(false);
		adapter = new ChoosePhotoGalleryAdapter(ChoosePhotoGalleryActivity.this,
				list);
		imageGallery.setAdapter(adapter);
		imageGallery.setSelection(currentPage);
		imageGallery.setOnItemSelectedListener(new GalleryChangeListener());
		imageGallery.setSingleTapListener(this);
		setcurrentView(currentPage);
		
		ExpandViewTouchUtil.expandViewTouchDelegate(backBtn, 30, 30, 20, 30);
	}

	private class GalleryChangeListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg3 < 0 || arg3 > list.size() - 1 || currentPage == arg3) {
				return;
			}
			setcurrentView(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void setcurrentView(int position) {
		currentPage = position;
		PhotoItem item = list.get(currentPage);
		isCheckBtn.setChecked(PhotoActivity.instance.isCheckByPhotoId(item
				.getPhotoId()));
		isSourceCheckBtn.setChecked(PhotoActivity.instance.isSendSourcePic());
		sourceSizeTv.setText(getString(R.string.source_image_str) + " ("
				+ FileUtil.getShowFileSizeString(item.picSize) + ")");
		if (isSourceCheckBtn.isChecked()) {
			sourceSizeTv.setTextColor(getResources().getColor(R.color.white));
		} else {
			sourceSizeTv.setTextColor(getResources().getColor(
					R.color.common_bottom_gray_btn_hover_color));
		}
		// TODO
		if (PhotoActivity.instance.getCheckCount() < 1) {
			chooseNumTv.setVisibility(View.GONE);
		} else {
			chooseNumTv.setVisibility(View.VISIBLE);
			chooseNumTv.setText(PhotoActivity.instance.getCheckCount() + "");
		}
		PhotoActivity.instance.refreshAll();
	}

	private class BtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.common_titlebar_leftBtn:
				ChoosePhotoGalleryActivity.this.finish();
				break;
			case R.id.choose_photo_send_btn:
				showDialog(false);
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (PhotoActivity.instance.getCheckCount() < 1) {
							PhotoItem sendItem = list.get(currentPage);
							sendItem.setSelect(true);
							PhotoActivity.instance.addCheckItem(sendItem);
							PhotoActivity.instance.compressPhotos();
							handler.sendEmptyMessage(COMPRESS_IMAGE_OVER);
						} else {
							PhotoActivity.instance.compressPhotos();
							handler.sendEmptyMessage(COMPRESS_IMAGE_OVER);
						}
					}
				}).start();
				break;
			case R.id.photo_choose_check_iv:
				PhotoItem item = list.get(currentPage);
				if (!PhotoActivity.instance.isSendSourcePic()
						|| item.picSize < ImageTools.MAX_IMAGE_SIZE) {
					if (PhotoActivity.instance.isNotOutOfLimitCount()
							&& ((CheckBox) v).isChecked()) {
						item.setSelect(((CheckBox) v).isChecked());
						PhotoActivity.instance.addCheckItem(item);
					} else if (!((CheckBox) v).isChecked()) {
						item.setSelect(((CheckBox) v).isChecked());
						PhotoActivity.instance.delCheckItem(item);
					} else {
						ToastUtil.showShortToast(PhotoActivity.instance,
								R.string.choose_pic_is_full);
					}
				} else {
					ToastUtil.showShortToast(ChoosePhotoGalleryActivity.this,
							R.string.image_size_is_too_big_to_send);
				}
				setcurrentView(currentPage);
				break;
			case R.id.choose_photo_source_size_tv:
				PhotoItem feedAttach = list.get(currentPage);
				if (!PhotoActivity.instance.isSendSourcePic()
						&& PhotoActivity.instance.checkIsPicsSizeMax()) {
					ToastUtil.showShortToast(ChoosePhotoGalleryActivity.this,
							R.string.image_size_is_too_big_to_send);
				} else {
					if (feedAttach.isSelect()
							|| PhotoActivity.instance.isSendSourcePic()) {
						PhotoActivity.instance
								.setSendSourcePic(!PhotoActivity.instance
										.isSendSourcePic());
					} else {
						if (PhotoActivity.instance.isNotOutOfLimitCount()) {
							feedAttach.setSelect(true);
							PhotoActivity.instance.addCheckItem(feedAttach);
						}
						PhotoActivity.instance
								.setSendSourcePic(!PhotoActivity.instance
										.isSendSourcePic());
					}
				}
				setcurrentView(currentPage);
				break;
			case R.id.photo_choose_source_check_iv:
				PhotoItem temp = list.get(currentPage);
				if (((CheckBox) v).isChecked()
						&& PhotoActivity.instance.checkIsPicsSizeMax()) {
					ToastUtil.showShortToast(ChoosePhotoGalleryActivity.this,
							R.string.image_size_is_too_big_to_send);
				} else {
					if (temp.isSelect()
							|| PhotoActivity.instance.isSendSourcePic()) {
						PhotoActivity.instance
								.setSendSourcePic(!PhotoActivity.instance
										.isSendSourcePic());
					} else {
						if (PhotoActivity.instance.isNotOutOfLimitCount()) {
							temp.setSelect(true);
							PhotoActivity.instance.addCheckItem(temp);
						}
						PhotoActivity.instance
								.setSendSourcePic(!PhotoActivity.instance
										.isSendSourcePic());
					}
				}
				setcurrentView(currentPage);
				break;
			default:
				break;
			}
		}
	}

	private class PhotoHandler extends Handler {

		public PhotoHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case COMPRESS_IMAGE_OVER:
				PhotoActivity.instance.sendPhotos();
				hideDialog();
				Intent completeIntent = new Intent();
				setResult(RESULT_OK, completeIntent);
				ChoosePhotoGalleryActivity.this.finish();
				break;
			default:
				break;
			}
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler = null;
		GlobalData.galleryImageData = new ArrayList<PhotoItem>();
		list = new ArrayList<PhotoItem>();
		adapter = null;
	}

	@Override
	public void doSngleTap() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (titleLayout.getVisibility() == View.VISIBLE) {
					titleLayout.setVisibility(View.GONE);
					bottomLayout.setVisibility(View.GONE);
				} else {
					titleLayout.setVisibility(View.VISIBLE);
					bottomLayout.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}
