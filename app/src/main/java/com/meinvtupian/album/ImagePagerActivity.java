package com.meinvtupian.album;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;

@SuppressLint("NewApi")
public class ImagePagerActivity extends FragmentActivity implements
		OnClickListener {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private String[] urls;
	private String name;
	private String picname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		name = getIntent().getStringExtra("name");
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);

		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);

		findViewById(R.id.save).setOnClickListener(new OnClickListener() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onClick(View v) {
				File appDir = new File(Environment
						.getExternalStorageDirectory(), "hmyg");
				if (!appDir.exists()) {
					appDir.mkdir();
				}
				FinalHttp finalHttp = new FinalHttp();
				picname = System.currentTimeMillis() + "";
				finalHttp.download(urls[mPager.getCurrentItem()],
						Environment.getExternalStorageDirectory() + "/hmyg/"
								+ picname + ".jpg", true, new AjaxCallBack() {

							public void onLoading(long count, long current) {
							};

							public void onSuccess(Object t) {
								Toast.makeText(ImagePagerActivity.this,
										"下载成功，请到相册中查看", Toast.LENGTH_SHORT)
										.show();
								sendBroadcast(new Intent(
										Intent.ACTION_MEDIA_MOUNTED,
										Uri.parse("file://"
												+ Environment
														.getExternalStorageDirectory())));

							};

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// TODO Auto-generated method stub
								Toast.makeText(ImagePagerActivity.this,
										"图片下载失败,请检查网络", Toast.LENGTH_SHORT)
										.show();
								super.onFailure(t, errorNo, strMsg);
							}
						});

			}
		});

		findViewById(R.id.img_set_wall).setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			return ImageDetailFragment.newInstance(url);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_set_wall:

			new com.zf.iosdialog.widget.AlertDialog(this).builder()
					.setTitle("设置桌面壁纸").setMsg("您确定要设置当前图片为桌面壁纸？")
					.setPositiveButton("确认设置", new OnClickListener() {
						@Override
						public void onClick(View v) {

							FinalHttp finalHttp = new FinalHttp();
							picname = System.currentTimeMillis() + "";
							finalHttp.download(urls[mPager.getCurrentItem()],
									"/mnt/sdcard/hmyg/" + picname + ".jpg",
									true, new AjaxCallBack() {

										public void onLoading(long count,
												long current) {
										};

										public void onSuccess(Object t) {

											WallpaperManager wallpaperManager = WallpaperManager
													.getInstance(getApplicationContext());

											try {
												wallpaperManager
														.setBitmap(BitmapFactory
																.decodeFile("/mnt/sdcard/hmyg/"
																		+ picname
																		+ ".jpg"));
												Toast.makeText(
														ImagePagerActivity.this,
														"桌面壁纸设置成功",
														Toast.LENGTH_SHORT)
														.show();

											} catch (IOException e) {
												e.printStackTrace();
											}

										};

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											Toast.makeText(
													ImagePagerActivity.this,
													"图片下载失败,请检查网络",
													Toast.LENGTH_SHORT).show();
											super.onFailure(t, errorNo, strMsg);
										}
									});
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

			break;

		default:
			break;
		}

	}
}