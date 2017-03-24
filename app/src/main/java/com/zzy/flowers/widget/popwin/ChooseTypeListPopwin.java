package com.zzy.flowers.widget.popwin;

import org.json.JSONArray;
import org.json.JSONException;

import android.R.anim;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hy.utils.JsonGetInfo;
import com.white.utils.AndroidUtil;

@SuppressLint("CutPasteId")
public abstract class ChooseTypeListPopwin extends PopupWindow {

	// private BaseAdapter adapter;
	protected static int DISTANCE_3_DIP = 0;
	/** 显示的列表文本 */

	protected LayoutInflater inflater;

	protected Context context;
	protected String str;
	protected JSONArray typeList;
	protected View mainView;
	private TextView iv_reset;
	private TextView sure;

	protected abstract void handleClickListener(String str,String name);

	public ChooseTypeListPopwin(Context context, JSONArray typeList, String str) {
		super(context);
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		this.context = context;
		this.str = str;
		this.typeList = typeList;
		init();
	}

	protected void init() {
		if (DISTANCE_3_DIP == 0) {
			DISTANCE_3_DIP = AndroidUtil.dip2px(context, 3);
		}
		inflater = LayoutInflater.from(context);
		mainView = inflater.inflate(R.layout.bottom_choose_type_list, null);
		ListView listView = (ListView) mainView.findViewById(R.id.listView);
		listView.setAdapter(new Myadapter());
		iv_reset = (TextView) mainView.findViewById(R.id.iv_reset);
		sure = (TextView) mainView.findViewById(R.id.sure);
		sure.setOnClickListener(new myOnclick());
		iv_reset.setOnClickListener(new myOnclick());
		setContentView(mainView);
		initPopwin();
	}

	class myOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_reset:
				str = "";
				handleClickListener(str,"分类");
				dismiss();
				break;
			case R.id.sure:
				dismiss();
				break;

			default:
				break;
			}
		}

	}

	protected void initPopwin() {
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ChooseTypeListPopwin.this.dismiss();
				return true;
			}
		});
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
	}

	public class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return typeList.length();
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
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_store_type_list, null);
			TextView tv_item = (TextView) inflate.findViewById(R.id.tv_item);
			try {
				tv_item.setText(JsonGetInfo.getJsonString(
						typeList.getJSONObject(position), "name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						str = JsonGetInfo.getJsonString(
								typeList.getJSONObject(position), "id");
						handleClickListener(str,JsonGetInfo.getJsonString(
								typeList.getJSONObject(position), "name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dismiss();
				}
			});
			return inflate;
		}

	}

}
