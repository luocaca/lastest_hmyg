package com.hldj.hmyg.store;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hldj.hmyg.R;

/**
 * ExpandListView的适配器，继承自BaseExpandableListAdapter
 * 
 */
public class StoreBaseExpandableListAdapter extends BaseExpandableListAdapter
		implements OnClickListener {

	private Context mContext;
	private List<GroupItem> groupTitle;
	// 子项是一个map，key是group的id，每一个group对应一个ChildItem的list
	private Map<Integer, List<ChildItem>> childMap;
	private Button groupButton;// group上的按钮
	private TypeEx typeEx;

	public StoreBaseExpandableListAdapter(Context context,
			List<GroupItem> groupTitle, Map<Integer, List<ChildItem>> childMap,
			TypeEx typeEx) {
		this.mContext = context;
		this.groupTitle = groupTitle;
		this.childMap = childMap;
		this.typeEx = typeEx;
	}

	/*
	 * Gets the data associated with the given child within the given group
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// 我们这里返回一下每个item的名称，以便单击item时显示
		return childMap.get(groupPosition).get(childPosition).getTitle();
	}

	/*
	 * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*
	 * Gets a View that displays the data for the given child within the given
	 * group
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.store_childitem, null);
			childHolder = new ChildHolder();
			childHolder.childImg = (ImageView) convertView
					.findViewById(R.id.img_child);
			childHolder.childText = (TextView) convertView
					.findViewById(R.id.tv_child_text);
			childHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		if (typeEx.getGroupPosition() != -1 && typeEx.getChildPosition() != -1
				&& typeEx.getGroupPosition() == groupPosition
				&& typeEx.getChildPosition() == childPosition) {
			childHolder.childText.setTextColor(mContext.getResources()
					.getColor(R.color.red));
		} else {
			childHolder.childText.setTextColor(mContext.getResources()
					.getColor(R.color.gray));
		}
		childHolder.childImg.setBackgroundResource(childMap.get(groupPosition)
				.get(childPosition).getMarkerImgId());
		childHolder.childText.setText(childMap.get(groupPosition)
				.get(childPosition).getTitle()
				+ "["
				+ childMap.get(groupPosition).get(childPosition).getLevel()
				+ "]");
		return convertView;
	}

	/*
	 * 取得指定分组的子元素数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childMap.get(groupPosition).size();
	}

	/**
	 * 取得与给定分组关联的数据
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groupTitle.get(groupPosition);
	}

	/**
	 * 取得分组数
	 */
	@Override
	public int getGroupCount() {
		return groupTitle.size();
	}

	/**
	 * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * Gets a View that displays the given groupreturn: the View corresponding
	 * to the group at the specified position
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.store_groupitem, null);
			groupHolder = new GroupHolder();
			groupHolder.groupImg = (ImageView) convertView
					.findViewById(R.id.img_indicator);
			groupHolder.groupText = (TextView) convertView
					.findViewById(R.id.tv_group_text);
			groupHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		if (isExpanded) {
			groupHolder.groupImg.setBackgroundResource(R.drawable.downarrow);
		} else {
			groupHolder.groupImg.setBackgroundResource(R.drawable.rightarrow);
		}
		groupHolder.groupText.setText(groupTitle.get(groupPosition).getName()
				+ "[" + groupTitle.get(groupPosition).getImageId() + "]");

		groupButton = (Button) convertView
				.findViewById(R.id.btn_group_function);
		groupButton.setOnClickListener(this);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// Indicates whether the child and group IDs are stable across changes
		// to the underlying data
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// Whether the child at the specified position is selectable
		return true;
	}

	/**
	 * show the text on the child and group item
	 */
	private class GroupHolder {
		TextView tv_count;
		ImageView groupImg;
		TextView groupText;
	}

	private class ChildHolder {
		TextView tv_count;
		ImageView childImg;
		TextView childText;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_group_function:
			Log.d("MyBaseExpandableListAdapter", "你点击了group button");
		default:
			break;
		}

	}
}
