package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import me.drakeet.materialdialog.MaterialDialog;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.ManagerPurchaseListActivity;
import com.hldj.hmyg.buyer.ManagerPurchaseListDetailActivity;
import com.hldj.hmyg.buyer.SavePurchaseActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP4;

@SuppressLint("ResourceAsColor")
public class ManagerPurchaseListAdapter extends BaseAdapter {
	private static final String TAG = "ManagerPurchaseListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;
	private View mainView;
	MaterialDialog mMaterialDialog;

	private LinearLayout ll_caozuo;

	public ManagerPurchaseListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data, View mainView) {
		this.data = data;
		this.context = context;
		this.mainView = mainView;
		mMaterialDialog = new MaterialDialog(context);
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.list_item_manager_purchase, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		ll_caozuo = (LinearLayout) inflate.findViewById(R.id.ll_caozuo);
		TextView tv_caozuo01 = (TextView) inflate
				.findViewById(R.id.tv_caozuo01);
		TextView tv_caozuo02 = (TextView) inflate
				.findViewById(R.id.tv_caozuo02);
		TextView tv_caozuo03 = (TextView) inflate
				.findViewById(R.id.tv_caozuo03);
		tv_ac.setText(data.get(position).get("statusName").toString());
		tv_01.setText("项目名称："
				+ data.get(position).get("projectName").toString());
		tv_02.setText("采购单号：" + data.get(position).get("num").toString());
		tv_04.setText("采购地：" + data.get(position).get("cityName").toString());
		tv_03.setText("发布日期："
				+ data.get(position).get("publishDate").toString());
		tv_07.setText("截止日期" + data.get(position).get("closeDate").toString());
		tv_05.setText("收货日期："
				+ data.get(position).get("receiptDate").toString());
		if (data.get(position).get("needInvoice").toString().contains("true")) {
			tv_06.setText("发票要求:提供发票");
		} else {
			tv_06.setText("发票要求:不提供发票");
		}
		tv_08.setText("采购品种："
				+ data.get(position).get("itemCountJson").toString());
		tv_09.setText("报价总数："
				+ data.get(position).get("quoteCountJson").toString());

		if ("unsubmit".equals(data.get(position).get("status").toString())
				&& "unaudit"
						.equals(data.get(position).get("status").toString())) {
			tv_caozuo01.setText("剩余天数："
					+ data.get(position).get("lastDays").toString());
		}
		if ("unsubmit".equals(data.get(position).get("status").toString())
				|| "backed".equals(data.get(position).get("status").toString())) {
			tv_caozuo02.setText("编辑");
			tv_caozuo03.setText("删除");
			if ("unsubmit".equals(data.get(position).get("status").toString())) {
				tv_ac.setTextColor(Color.parseColor("#ff6601"));
			} else if ("backed".equals(data.get(position).get("status")
					.toString())) {
				tv_ac.setTextColor(Color.parseColor("#ff0000"));
			}
		} else if ("unaudit"
				.equals(data.get(position).get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setText("撤回");
			tv_ac.setTextColor(Color.parseColor("#179bed"));
		} else if ("published".equals(data.get(position).get("status")
				.toString())) {
			tv_caozuo02.setText("关闭");
			tv_caozuo03.setText("延期");
			tv_ac.setTextColor(Color.parseColor("#00843c"));
		} else if ("expired"
				.equals(data.get(position).get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setText("延期");
			tv_ac.setTextColor(Color.parseColor("#4caf50"));
		} else if ("closed".equals(data.get(position).get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setVisibility(View.GONE);
			tv_ac.setTextColor(Color.parseColor("#999999"));

		}
		tv_caozuo02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("published".equals(data.get(position).get("status")
						.toString())) {
					// 关闭
					if (mMaterialDialog != null) {
						mMaterialDialog
								.setTitle("提示")
								.setMessage("采购单关闭后不能再启用，且接收不到卖家报价，是否继续？")
								// mMaterialDialog.setBackgroundResource(R.drawable.background);
								.setPositiveButton("确定",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												// 关闭
												purchaseClose(data
														.get(position)
														.get("id").toString());
												mMaterialDialog.dismiss();
												ManagerPurchaseListAdapter.this
														.notify(data);
											}

										})
								.setNegativeButton("取消",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												mMaterialDialog.dismiss();
											}
										}).setCanceledOnTouchOutside(true)
								// You can change the message anytime.
								// mMaterialDialog.setTitle("提示");
								.setOnDismissListener(
										new DialogInterface.OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {
											}
										}).show();
					} else {
					}

				} else if ("unsubmit".equals(data.get(position).get("status")
						.toString())
						|| "backed".equals(data.get(position).get("status")
								.toString())) {
					// 编辑
					Intent toSavePurchaseActivity = new Intent(context,
							SavePurchaseActivity.class);
					toSavePurchaseActivity.putExtra("id", data.get(position)
							.get("id").toString());
					toSavePurchaseActivity.putExtra("projectName",
							data.get(position).get("projectName").toString());
					toSavePurchaseActivity.putExtra("cityCode",
							data.get(position).get("cityCode").toString());
					toSavePurchaseActivity.putExtra("cityName",
							data.get(position).get("cityName").toString());
					toSavePurchaseActivity.putExtra("receiptDate",
							data.get(position).get("receiptDate").toString());
					toSavePurchaseActivity.putExtra("validity",
							data.get(position).get("validity").toString());
					toSavePurchaseActivity.putExtra(
							"needInvoice",
							Boolean.parseBoolean(data.get(position)
									.get("needInvoice").toString()));
					((Activity) context).startActivityForResult(
							toSavePurchaseActivity, 1);
					((Activity) context).overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_right);
				}

			}

		});
		tv_caozuo03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ("unsubmit".equals(data.get(position).get("status")
						.toString())
						|| "backed".equals(data.get(position).get("status")
								.toString())) {

					// 删除
					if (mMaterialDialog != null) {
						mMaterialDialog
								.setTitle("提示")
								.setMessage("确定要删除该采购单吗？")
								// mMaterialDialog.setBackgroundResource(R.drawable.background);
								.setPositiveButton("确定",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												// 删除
												purchaseDeleteItem(data
														.get(position)
														.get("id").toString());
												mMaterialDialog.dismiss();
												data.remove(position);
												ManagerPurchaseListAdapter.this
														.notify(data);

											}

										})
								.setNegativeButton("取消",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												mMaterialDialog.dismiss();
											}
										}).setCanceledOnTouchOutside(true)
								// You can change the message anytime.
								// mMaterialDialog.setTitle("提示");
								.setOnDismissListener(
										new DialogInterface.OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {
											}
										}).show();
					} else {
					}

				} else if ("expired".equals(data.get(position).get("status")
						.toString())) {
					// 延期
					purchaselastDays(
							data.get(position).get("id").toString(),
							data.get(position).get("closeDate").toString(),
							Integer.parseInt(data.get(position).get("lastDays")
									.toString()));
				} else if ("published".equals(data.get(position).get("status")
						.toString())) {
					// 延期
					purchaselastDays(
							data.get(position).get("id").toString(),
							data.get(position).get("closeDate").toString(),
							Integer.parseInt(data.get(position).get("lastDays")
									.toString()));
				} else if ("unaudit".equals(data.get(position).get("status")
						.toString())) {
					// 撤回
					if (mMaterialDialog != null) {
						mMaterialDialog
								.setTitle("提示")
								.setMessage("确定要撤回该采购单吗？")
								// mMaterialDialog.setBackgroundResource(R.drawable.background);
								.setPositiveButton("确定",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												// 撤回
												purchaseRevoke(data
														.get(position)
														.get("id").toString());
												mMaterialDialog.dismiss();
												data.remove(position);
												ManagerPurchaseListAdapter.this
														.notify(data);

											}

										})
								.setNegativeButton("取消",
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												mMaterialDialog.dismiss();
											}
										}).setCanceledOnTouchOutside(true)
								// You can change the message anytime.
								// mMaterialDialog.setTitle("提示");
								.setOnDismissListener(
										new DialogInterface.OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {
											}
										}).show();
					} else {
					}

				}
			}

		});
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toManagerPurchaseListDetailActivity = new Intent(
						context, ManagerPurchaseListDetailActivity.class);
				Bundle bundleObject = new Bundle();
				final SerializableHashMaplist myMap = new SerializableHashMaplist();
				myMap.setHashMap(data.get(position));
				bundleObject.putSerializable("map", myMap);
				toManagerPurchaseListDetailActivity.putExtras(bundleObject);
				toManagerPurchaseListDetailActivity.putExtra("id",
						data.get(position).get("id").toString());
				toManagerPurchaseListDetailActivity.putExtra("projectName",
						data.get(position).get("projectName").toString());
				context.startActivity(toManagerPurchaseListDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		return inflate;
	}

	private void purchaselastDays(String id, String closeDate, int lastDays) {
		// TODO Auto-generated method stub
		EditP4 popwin = new EditP4(context, "下架日期：" + closeDate + "可延期天数："
				+ (179 - lastDays), (179 - lastDays), id,
				(ManagerPurchaseListActivity) context);
		popwin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popwin.showAtLocation(mainView, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void purchaseRevoke(String id) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/revoke", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void purchaseDeleteItem(String id) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/delete", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void purchaseClose(String id) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/close", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}
}
