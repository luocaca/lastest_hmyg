package aom.xingguo.huang.banner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.HomeStore;

import java.util.ArrayList;

/**
 * 热门商家 片段
 */
public class MyFragment extends Fragment implements OnPageChangeListener {
	ArrayList<HomeStore> urls = new ArrayList<HomeStore>();
	private ArrayList<View> views;
	private CustomViewPager viewpager;
	private ViewPagerAdapter vpAdapter;
	LayoutInflater mInflater;
	// 设置默认当前页
	private int currentIndex = 0;
	private ImageView[] viewpager_points;
	private GridView gv;
	private GridViewAdapter gridViewAdapter;
	private View inflate;
	public static int Num = 6;
	public static int hang_Num = 3;
	public static int lie_Num = 2;
	private int width;

	// private int Num =8;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_main, null);
		// 初始打气筒
		mInflater = LayoutInflater.from(getActivity());
		WindowManager wm = ((Activity) getActivity()).getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		initData();

		views = new ArrayList<View>();
		viewpager = (CustomViewPager) inflate.findViewById(R.id.viewpager);
		LayoutParams para = viewpager.getLayoutParams();
		para.height = width / hang_Num * lie_Num / 2;
		// viewpager.setLayoutParams(para);
		vpAdapter = new ViewPagerAdapter(views);
		// 清空一下
		if (views.size() > 0) {
			views.clear();
		}

		for (int i = 0; i < (urls.size() - 1) / Num + 1; i++) {
			views.add(mInflater.inflate(R.layout.viewpager_gridview, null));
		}
		// 设置适配器
		viewpager.setAdapter(vpAdapter);
		viewpager.setOffscreenPageLimit(views.size());
		// 设置当前view--gridView
		viewpager.setCurrentItem(currentIndex);
		// 设置监听器
		viewpager.setOnPageChangeListener(this);
		// 初始化底部point
		initViewPagerPoint();
		// 设置grids
		initGrids(currentIndex);
		mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
		return inflate;
	}

	private void initGrids(int currentIndex2) {
		// TODO Auto-generated method stub
		gv = (GridView) views.get(currentIndex2).findViewById(R.id.gridview);

		ArrayList<HomeStore> arrayList2 = new ArrayList<HomeStore>();
		if (urls.size() > Num) {
			if (currentIndex2 == 0) {
				for (int i = 0; i < Num; i++) {
					arrayList2.add(urls.get(i)); // 第一页为8张
				}
			} else if (currentIndex2 == (urls.size() - 1) / Num
					&& currentIndex2 != 0) { // 多页数，最后一页 for循环是最后
				for (int i = currentIndex2 * Num; i < urls.size(); i++) {
					arrayList2.add(urls.get(i));
				}
			} else { // for循环都是8次
				for (int i = currentIndex2 * Num; i < (currentIndex2 + 1) * Num; i++) {
					arrayList2.add(urls.get(i));
				}
			}
		} else { // 单页数
			arrayList2 = urls;
		}

		gridViewAdapter = new GridViewAdapter(getActivity(), arrayList2);
		gv.setAdapter(gridViewAdapter);
	}

	/**
	 * 初始化小点
	 */
	private void initViewPagerPoint() {
		LinearLayout linerLayout = (LinearLayout) inflate
				.findViewById(R.id.viewpager_point);
		viewpager_points = new ImageView[(urls.size() - 1) / Num + 1];
		// 清空linerLayout中的小点
		if (linerLayout.getChildCount() > 0) {
			linerLayout.removeAllViewsInLayout();
		}
		// 循环添加小点
		for (int i = 0; i < viewpager_points.length; i++) {
			// inflate出ImageView对象 并设置参数
			ImageView point = (ImageView) mInflater.inflate(
					R.layout.viewpager_point_item, null);
			// 默认为白色
			point.setEnabled(true);
			// 设置监听
			// point.setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			point.setTag(i);
			viewpager_points[i] = point;
			linerLayout.addView(point);
		}
		// 设置当前小点 为黑色 表示选中
		viewpager_points[currentIndex].setEnabled(false);

		if (viewpager_points.length == 1) {
			linerLayout.setVisibility(View.GONE);
		}
	}

	private void initData() {
		// urls.add("http://ww1.sinaimg.cn/mw1024/648ac377gw1et56o4zggwj20kg17t78c.jpg");
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		setCurDot(position);
	}

	private void setCurDot(int position) {
		if (position < 0 || position > viewpager_points.length - 1
				|| currentIndex == position) {
			return;
		}
		// 解决删除一页grid后角标越界bug
		if (currentIndex >= viewpager_points.length) {
			currentIndex = viewpager_points.length - 1;
		}
		viewpager_points[position].setEnabled(false);
		viewpager_points[currentIndex].setEnabled(true);
		currentIndex = position;

		// 设置grids
		// initGrids(views.get(currentIndex));
		initGrids(currentIndex);
	}

	public void setUrls(ArrayList<HomeStore> urls) {
		// TODO Auto-generated method stub
		this.urls = urls;
	}

	private final int AUTO_MSG = 1;
	private final int HANDLE_MSG = AUTO_MSG + 1;
	private static final int PHOTO_CHANGE_TIME = 8000;// 定时变量
	private int index = 0;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO_MSG:
				viewpager.setCurrentItem((viewpager.getCurrentItem() + 1)
						% views.size());
				// viewpager.setCurrentItem(index++ % views.size());//
				// 收到消息后设置当前要显示的图片
				mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
				break;
			case HANDLE_MSG:
				mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
				break;
			default:
				break;
			}
		};
	};

}
