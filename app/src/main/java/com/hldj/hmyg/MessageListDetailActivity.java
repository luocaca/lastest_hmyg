package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.adapter.MessageListDetailAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Message;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class MessageListDetailActivity extends NeedSwipeBackActivity implements
		IXListViewListener, OnItemClickListener {
	private XListView xListView;
	private ArrayList<Message> datas = new ArrayList<Message>();

	boolean getdata; // 避免刷新多出数据
	private MessageListDetailAdapter listAdapter;
	private int pageSize = 20;
	private int pageIndex = 0;
	private MultipleClickProcess multipleClickProcess;
	private String text = "";
	private String count = "";
	private String value = "";
	private Button editButton, selectAll, invertSelection;
	private LinearLayout title_ll;
	private boolean listViewState = true;
	private CheckBox id_cb_select_all;
	private TextView id_tv_delete_all;
	private TextView id_tv_save_star_all;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail_list);
		text = getIntent().getStringExtra("text");
		count = getIntent().getStringExtra("count");
		value = getIntent().getStringExtra("value");
		editButton = (Button) findViewById(R.id.edit_btn);
		selectAll = (Button) findViewById(R.id.select_all_btn);
		invertSelection = (Button) findViewById(R.id.disselected_all_btn);
		id_cb_select_all = (CheckBox) findViewById(R.id.id_cb_select_all);
		id_tv_delete_all = (TextView) findViewById(R.id.id_tv_delete_all);
		id_tv_save_star_all = (TextView) findViewById(R.id.id_tv_save_star_all);

		title_ll = (LinearLayout) findViewById(R.id.title_ll);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(text);

		multipleClickProcess = new MultipleClickProcess();
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		btn_back.setOnClickListener(multipleClickProcess);
		editButton.setOnClickListener(multipleClickProcess);
		selectAll.setOnClickListener(multipleClickProcess);
		invertSelection.setOnClickListener(multipleClickProcess);
		id_tv_delete_all.setOnClickListener(multipleClickProcess);
		id_tv_save_star_all.setOnClickListener(multipleClickProcess);
		id_cb_select_all
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							selectAllData();
						} else {
							disSelectedAllData();
						}
					}
				});

		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("type", value);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("updateReaded", true+"");
		
		finalHttp.post(GetServerUrl.getUrl() + "admin/notice/list", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								// Toast.makeText(MessageListDetailActivity.this,
								// msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										Message products_hash = new Message(
												JsonGetInfo.getJsonString(
														jsonObject4, "id"),
												JsonGetInfo.getJsonString(
														jsonObject4, "message"),
												JsonGetInfo.getJsonString(
														jsonObject4,
														"contentTypeName"),
												JsonGetInfo.getJsonString(
														jsonObject4,
														"createDate"), false,
												JsonGetInfo.getJsonBoolean(
														jsonObject4, "isRead"),
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"sourceId"),
												JsonGetInfo.getJsonString(
														jsonObject4,
														"contentType"),
												JsonGetInfo.getJsonString(
														jsonObject4,
														"modelType"),
												JsonGetInfo.getJsonString(
														jsonObject4,
														"targetUserType")

										);
										datas.add(products_hash);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new MessageListDetailAdapter(
												MessageListDetailActivity.this,
												datas);
										xListView.setAdapter(listAdapter);
										xListView
												.setOnItemClickListener(MessageListDetailActivity.this);
									}
									pageIndex++;

								}

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(MessageListDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new MessageListDetailAdapter(
					MessageListDetailActivity.this, datas);

			xListView.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		xListView.setPullRefreshEnable(false);
		initData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				xListView.stopRefresh();
				xListView.stopLoadMore();
				xListView.setRefreshTime(new Date().toLocaleString());
				xListView.setPullLoadEnable(true);
				xListView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;
		private EditP2 popwin;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				switch (view.getId()) {
				case R.id.btn_back:
					onBackPressed();
					break;
				case R.id.edit_btn:
					if (listAdapter != null && datas.size() > 0) {
						boolean isShow = listAdapter.isShow();
						listAdapter.setShow(!isShow);
						listAdapter.notifyDataSetChanged();
						if (listViewState) {
							changeState2Edit();
						} else {
							changeState2Normal();
							// deleteSelectedData();
							// changeState2Normal();
						}
					}

					break;

				case R.id.select_all_btn:
					selectAllData();
					break;

				case R.id.disselected_all_btn:
					disSelectedAllData();
					break;
				case R.id.id_tv_delete_all:
					Delete();

					break;
				case R.id.id_tv_save_star_all:

					StringBuffer sbf = new StringBuffer();
					for (int i = 0; i < datas.size(); i++) {
						if (datas.get(i).isChecked()) {
							sbf.append(datas.get(i).getId() + ",");
						}
					}
					sbf.deleteCharAt(sbf.length() - 1);

					FinalHttp finalHttp = new FinalHttp();
					GetServerUrl.addHeaders(finalHttp,true);
					AjaxParams params = new AjaxParams();
					params.put("ids", sbf.toString());
					finalHttp.post(GetServerUrl.getUrl()
							+ "admin/notice/setReaded", params,
							new AjaxCallBack<Object>() {

								@Override
								public void onSuccess(Object t) {
									// TODO Auto-generated method stub
									try {
										JSONObject jsonObject = new JSONObject(
												t.toString());
										String code = JsonGetInfo
												.getJsonString(jsonObject,
														"code");
										String msg = JsonGetInfo.getJsonString(
												jsonObject, "msg");
										if (!"".equals(msg)) {
											Toast.makeText(
													MessageListDetailActivity.this,
													msg, Toast.LENGTH_SHORT)
													.show();
										}
										if ("1".equals(code)) {
											// 标记已读
											for (Message message : datas) {
												if (message.isChecked()) {
													message.setRead(true);
													message.setChecked(false);
												}
											}
											listAdapter.notifyDataSetChanged();

										} else if ("6007".equals(code)) {

										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									super.onSuccess(t);
								}

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									Toast.makeText(
											MessageListDetailActivity.this,
											R.string.error_net,
											Toast.LENGTH_SHORT).show();
									super.onFailure(t, errorNo, strMsg);
								}

							});

					break;

				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void Delete() {
			StringBuffer sbf = new StringBuffer();
			for (int i = 0; i < datas.size(); i++) {
				if (datas.get(i).isChecked()) {
					sbf.append(datas.get(i).getId() + ",");
				}
			}
			sbf.deleteCharAt(sbf.length() - 1);

			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("ids", sbf.toString());
			finalHttp.post(GetServerUrl.getUrl() + "admin/notice/delete",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {
									Toast.makeText(
											MessageListDetailActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {
									// 删除
									listAdapter.setShow(!listAdapter.isShow());
									listAdapter.notifyDataSetChanged();
									deleteSelectedData();
									changeState2Normal();

								} else if ("6007".equals(code)) {

								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							super.onSuccess(t);
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							Toast.makeText(MessageListDetailActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});
		}

		/**
		 * 计时线程（防止在一定时间段内重复点击按钮）
		 */
		private class TimeThread extends Thread {
			public void run() {
				try {
					Thread.sleep(Data.loading_time);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 反选
	 */
	private void disSelectedAllData() {
		for (Message message : datas) {
			message.setChecked(!message.isChecked());
		}
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 全选
	 */
	private void selectAllData() {
		for (Message message : datas) {
			message.setChecked(true);
		}
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 删除选中
	 */
	private void deleteSelectedData() {
		Iterator<Message> it = datas.iterator();
		while (it.hasNext()) {
			Message people = it.next();
			if (people.isChecked()) {
				it.remove();
			}
		}
	}

	/**
	 */
	private void changeState2Normal() {
		editButton.setText("编辑");
		title_ll.setVisibility(View.GONE);
		listViewState = true;
	}

	private void changeState2Edit() {
		editButton.setText("完成");
		title_ll.setVisibility(View.VISIBLE);
		listViewState = false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Message message = datas.get(position - 1);
		message.setChecked(!message.isChecked());
		listAdapter.notifyDataSetChanged();
	}

}
