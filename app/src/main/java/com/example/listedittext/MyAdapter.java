package com.example.listedittext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hldj.hmyg.R;

public class MyAdapter extends BaseAdapter {
	private List<paramsData> list = new ArrayList<paramsData>();

	private Context context;
	private OnListRemovedListener mListener;

	// 下拉列表的适配器
	private ArrayAdapter<String> arrayAdapter;
	// 下拉列表的选项
	private static final String[] SPINNER_TIME = { "手机", "住宅", "其他" };

	public void setOnListRemovedListener(OnListRemovedListener listener) {
		this.mListener = listener;
	}

	public MyAdapter(List<paramsData> list, Context context) {
		this.context = context;
		this.list = list;
		arrayAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, SPINNER_TIME);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listrow_item, null);
			holder = new ViewHolder();
			holder.tv_position = (TextView) convertView
					.findViewById(R.id.tv_position);
			holder.et_number = (EditText) convertView
					.findViewById(R.id.et_number);
			holder.et_number.setTag(position);
//			holder.et_number.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			holder.et_number.addTextChangedListener(new MyTextWatcher(holder) {
				@Override
				public void afterTextChanged(Editable s, ViewHolder holder) {

					String text = s.toString();
					int len = s.toString().length();
					if (len == 1 && text.equals("0")) {
						s.clear();
						return;
					}
					
					int position = (Integer) holder.et_number.getTag();
					paramsData n = list.get(position);
					n.setValue(s.toString());
					list.set(position, n);
				}
			});
			holder.btn_delete = (Button) convertView
					.findViewById(R.id.btn_delete);
			holder.btn_delete.setOnClickListener(new MyOnClickListener(holder) {
				@Override
				public void onClick(View v, ViewHolder holder) {
					if (mListener != null) {
						int position = (Integer) holder.et_number.getTag();
						list.remove(position);
						mListener.onRemoved(); // 通知主线程更新Adapter
					}
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.et_number.setTag(position);
		}
		paramsData n = list.get(position);
		holder.tv_position.setText(list.get(position).getName());
		holder.et_number.setText(n.getValue());

		int p = getPositionForAdapter(position);
		return convertView;
	}

	private int getPositionForAdapter(int po) {

		paramsData t = list.get(po);
		int p = 0;
		for (int i = 0; i < SPINNER_TIME.length; i++) {
			if (t.getName().equals(SPINNER_TIME[i])) {
				p = i;
			}
		}
		return p;
	}

	// 动态添加List里面数据
	public void addItem(paramsData n) {
		list.add(n);
	}

	private abstract class MySpinnerListener implements OnItemSelectedListener {
		private ViewHolder holder;

		public MySpinnerListener(ViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			onItemSelected(arg0, arg1, arg2, arg3, holder);
		}

		public abstract void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3, ViewHolder holder);
	}

	private abstract class MyTextWatcher implements TextWatcher {
		private ViewHolder mHolder;

		public MyTextWatcher(ViewHolder holder) {
			this.mHolder = holder;
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			afterTextChanged(s, mHolder);
		}

		public abstract void afterTextChanged(Editable s, ViewHolder holder);
	}

	private abstract class MyOnClickListener implements OnClickListener {

		private ViewHolder mHolder;

		public MyOnClickListener(ViewHolder holder) {
			this.mHolder = holder;
		}

		@Override
		public void onClick(View v) {
			onClick(v, mHolder);
		}

		public abstract void onClick(View v, ViewHolder holder);

	}

	private class ViewHolder {
		TextView tv_position;
		EditText et_number;
		Button btn_delete;
	}

	// 删除操作回调
	public interface OnListRemovedListener {
		public void onRemoved();
	}

}