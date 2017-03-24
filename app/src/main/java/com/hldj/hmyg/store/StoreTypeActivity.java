package com.hldj.hmyg.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hy.utils.JsonGetInfo;

public class StoreTypeActivity extends Activity {
	private ExpandableListView expandList;
	private List<GroupItem> groupData;// group的数据源
	private Map<Integer, List<ChildItem>> childData;// child的数据源
	private StoreBaseExpandableListAdapter myAdapter;

	final int CONTEXT_MENU_GROUP_DELETE = 0;// 添加上下文菜单时每一个菜单项的item ID
	final int CONTEXT_MENU_GROUP_RENAME = 1;
	final int CONTEXT_MENU_CHILD_EDIT = 2;
	final int CONTEXT_MENU_CHILD_DELETE = 3;
	private String typeList = "";
	private TypeEx typeEx;
	private Bundle bundle;
	private ImageView btn_back;
	private TextView id_tv_edit_all;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_type);
		bundle = getIntent().getExtras();
		if (bundle.get("TypeEx") != null) {
			typeEx = (TypeEx) bundle.get("TypeEx");
		}

		initDatas();
		initView();
		initEvents();
	}

	/**
	 * group和child子项的数据源
	 */
	private void initDatas() {

		groupData = new ArrayList<GroupItem>();
		if (bundle.getString("typeList") != null) {
			typeList = bundle.getString("typeList");
			try {
				JSONArray jsonArray = new JSONArray(typeList);
				childData = new HashMap<Integer, List<ChildItem>>();
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jsonObject = jsonArray.getJSONObject(j);
					List<ChildItem> childItems = new ArrayList<ChildItem>();
					JSONArray childsJson = JsonGetInfo.getJsonArray(jsonObject,
							"childsJson");
					if (childsJson.length() > 0) {
						for (int i = 0; i < childsJson.length(); i++) {
							JSONObject childs = childsJson.getJSONObject(i);
							ChildItem childItem = new ChildItem(
									JsonGetInfo.getJsonString(childs, "name"),
									R.drawable.ic_launcher,
									JsonGetInfo.getJsonString(childs, "id"),
									JsonGetInfo.getJsonString(childs, "name"),
									JsonGetInfo.getJsonString(childs,
											"aliasName"),
									JsonGetInfo.getJsonString(childs,
											"parentId"),
									JsonGetInfo.getJsonInt(childs, "count"),
									JsonGetInfo.getJsonString(childs,
											"firstPinyin"),
									JsonGetInfo.getJsonString(childs,
											"fullPinyin"),
									JsonGetInfo.getJsonString(childs,
											"mainSpec"));
							childItems.add(childItem);
						}
						groupData
								.add(new GroupItem(JsonGetInfo.getJsonString(
										jsonObject, "name"), JsonGetInfo
										.getJsonInt(jsonObject, "count"),
										JsonGetInfo.getJsonString(jsonObject,
												"id"), JsonGetInfo
												.getJsonString(jsonObject,
														"name"), JsonGetInfo
												.getJsonString(jsonObject,
														"aliasName"),
										JsonGetInfo.getJsonString(jsonObject,
												"parentId"),
										JsonGetInfo.getJsonInt(jsonObject,
												"count"), JsonGetInfo
												.getJsonString(jsonObject,
														"firstPinyin"),
										JsonGetInfo.getJsonString(jsonObject,
												"fullPinyin"), JsonGetInfo
												.getJsonString(jsonObject,
														"mainSpec")));
						childData.put(j, childItems);
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myAdapter = new StoreBaseExpandableListAdapter(this, groupData,
				childData, typeEx);

	}

	private void initView() {
		btn_back = (ImageView) findViewById(R.id.btn_back);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		expandList = (ExpandableListView) findViewById(R.id.expandlist);
		// 在drawable文件夹下新建了indicator.xml，下面这个语句也可以实现group伸展收缩时的indicator变化
		// expandList.setGroupIndicator(this.getResources().getDrawable(R.drawable.indicator));
		expandList.setGroupIndicator(null);// 这里不显示系统默认的group indicator
		expandList.setAdapter(myAdapter);
		if (typeEx.getGroupPosition() != -1) {
			expandList.expandGroup(typeEx.getGroupPosition());
		}
		registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单
	}

	private void initEvents() {
		// child子项的单击事件
		expandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// Toast.makeText(
				// StoreTypeActivity.this,
				// "你单击了："
				// + myAdapter.getChild(groupPosition,
				// childPosition), Toast.LENGTH_SHORT)
				// .show();
				Intent intent = new Intent();
				typeEx = new TypeEx(groupPosition, childPosition, childData
						.get(groupPosition).get(childPosition).getName(),
						childData.get(groupPosition).get(childPosition).getId());
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("TypeEx", typeEx);
				intent.putExtras(mBundle);
				setResult(1, intent);
				finish();
				return true;
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		id_tv_edit_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				typeEx = new TypeEx(-1, -1, "苗木分类", "");
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("TypeEx", typeEx);
				intent.putExtras(mBundle);
				setResult(1, intent);
				finish();
			}
		});

	}

	/*
	 * 添加上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		// ExpandableListView.ExpandableListContextMenuInfo info =
		// (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		// int type = ExpandableListView
		// .getPackedPositionType(info.packedPosition);
		// if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
		// menu.setHeaderTitle("Options");
		// menu.add(0, CONTEXT_MENU_GROUP_DELETE, 0, "删除");
		// menu.add(0, CONTEXT_MENU_GROUP_RENAME, 0, "重命名");
		// }
		// if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
		// menu.setHeaderTitle("Options");
		// menu.add(1, CONTEXT_MENU_CHILD_EDIT, 0, "编辑");
		// menu.add(1, CONTEXT_MENU_CHILD_DELETE, 0, "删除");
		// }

	}

	/*
	 * 每个菜单项的具体点击事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_MENU_GROUP_DELETE:
			Toast.makeText(this, "这是group的删除", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_GROUP_RENAME:
			Toast.makeText(this, "这是group的重命名", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_EDIT:
			Toast.makeText(this, "这是child的编辑", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_DELETE:
			Toast.makeText(this, "这是child的删除", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}
}
