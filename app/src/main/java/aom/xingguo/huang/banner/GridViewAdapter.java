package aom.xingguo.huang.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.StoreActivity;
import com.hldj.hmyg.bean.HomeStore;
import com.white.utils.AndroidUtil;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HomeStore> arrayList;
	private int dip20px;
	private int width;

	public GridViewAdapter(Context context, ArrayList<HomeStore> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
		dip20px = AndroidUtil.dip2px(context, 2);
		WindowManager wm = ((Activity) context).getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

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
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.gird_item_main_image, null);
		ImageView imageView1 = (ImageView) inflate
				.findViewById(R.id.imageView1);
		if(!"".equals(arrayList.get(position).getImg())){
			FinalBitmap.create(context).display(imageView1,
					arrayList.get(position).getImg());
		}
		LayoutParams para = imageView1.getLayoutParams();
		para.width = (width - dip20px) / MyFragment.hang_Num;
		para.height = para.width / 2;
		imageView1.setLayoutParams(para);

		// Ion.with(context).load(arrayList.get(position).getImg())
		// .progressHandler(new ProgressCallback() {
		//
		// @Override
		// public void onProgress(long downloaded, long total) {
		// // TODO Auto-generated method stub
		// if (total == downloaded) {
		// }
		//
		// }
		// }).withBitmap().placeholder(R.drawable.shouyedaijitu)
		// .error(R.drawable.shouyedaijitu)
		// // .animateGif(AnimateGifMode.ANIMATE_ONCE)
		// .intoImageView(imageView1)
		// .setCallback(new FutureCallback<ImageView>() {
		//
		// @Override
		// public void onCompleted(Exception arg0, ImageView arg1) {
		// // TODO Auto-generated method stub
		// }
		// });

		/*跳转热门商家*/
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!"".equals(arrayList.get(position)
						.getId())){
					Intent toStoreActivity = new Intent(context,
							StoreActivity.class);
					toStoreActivity.putExtra("code", arrayList.get(position)
							.getId());
					context.startActivity(toStoreActivity);
				}
				
			}
		});
		return inflate;
	}

}
