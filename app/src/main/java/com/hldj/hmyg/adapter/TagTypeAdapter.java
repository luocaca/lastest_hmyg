package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhl.library.OnInitSelectedPosition;
import com.hldj.hmyg.R;
import com.hldj.hmyg.bean.Type;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagTypeAdapter<T> extends BaseAdapter implements
		OnInitSelectedPosition {

	private final Context mContext;
	private final List<T> mDataList;

	public TagTypeAdapter(Context context) {
		this.mContext = context;
		mDataList = new ArrayList<T>();
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item,
				null);

		TextView textView = (TextView) view.findViewById(R.id.tv_tag);
		T t = mDataList.get(position);

		if (t instanceof Type) {
			textView.setText(((Type) t).getName());
		}
		return view;
	}

	public void onlyAddAll(List<T> datas) {
		mDataList.addAll(datas);
		notifyDataSetChanged();
	}

	public void clearAndAddAll(List<T> datas) {
		mDataList.clear();
		onlyAddAll(datas);
	}

	@Override
	public boolean isSelectedPosition(int position) {
		if (position % 2 == 0) {
			return true;
		}
		return false;
	}
}
