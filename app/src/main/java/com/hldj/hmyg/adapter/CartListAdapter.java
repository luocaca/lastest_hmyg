package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class CartListAdapter extends BaseAdapter {
	private static final String TAG = "CartListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private CartProductListAdapter adapternei;
	private SwipeMenuListView lv_waitpay_nei;

	private ArrayList<HashMap<String, Object>> cartList;

	public CartListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		this.context = context;
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

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.list_item_cart, null);
		CheckBox remmber = (CheckBox) inflate.findViewById(R.id.remmber);
		lv_waitpay_nei = (SwipeMenuListView) inflate
				.findViewById(R.id.lv_waitpay_nei);
		remmber.setText(data.get(position).get("cityName").toString());
		cartList = (ArrayList<HashMap<String, Object>>) data.get(position).get(
				"cartList");
		adapternei = new CartProductListAdapter(context, cartList);
		lv_waitpay_nei.setAdapter(adapternei);
		remmber.setChecked((Boolean) data.get(position).get("isCheck"));
		remmber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		remmber.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1 == true) {
					for (int i = 0; i < cartList.size(); i++) {
						// ((CheckBox) (lv_waitpay_nei.getChildAt(i))
						// .findViewById(R.id.remmber)).setChecked(true);
						HashMap<String, Object> products_hash = new HashMap<String, Object>();
						products_hash.put("isCheck", true);
						products_hash.put("show_type", "seedling_list");
						products_hash.put("id", cartList.get(i).get("id")
								.toString());
						products_hash.put("name", cartList.get(i).get("id")
								.toString());
						products_hash.put("imageUrl", cartList.get(i).get("id")
								.toString());
						products_hash.put("cityName", cartList.get(i).get("id")
								.toString());
						products_hash.put("price", (Double) cartList.get(i)
								.get("price"));
						products_hash.put("count", (Integer) cartList.get(i)
								.get("count"));
						products_hash.put("unitTypeName",
								cartList.get(i).get("unitTypeName").toString());
						products_hash.put("diameter", (Double) cartList.get(i)
								.get("diameter"));
						products_hash.put("height", (Double) cartList.get(i)
								.get("height"));
						products_hash.put("crown", (Double) cartList.get(i)
								.get("crown"));
						products_hash.put("cityName",
								cartList.get(i).get("cityName").toString());
						products_hash.put("fullName",
								cartList.get(i).get("fullName").toString());
						products_hash.put("ciCity_name",
								cartList.get(i).get("ciCity_name").toString());
						products_hash.put("realName",
								cartList.get(i).get("realName").toString());
						products_hash.put("companyName",
								cartList.get(i).get("companyName").toString());
						products_hash.put("publicName",
								cartList.get(i).get("publicName").toString());
						products_hash.put("status",
								cartList.get(i).get("status").toString());
						products_hash.put("statusName",
								cartList.get(i).get("statusName").toString());
						products_hash.put("closeDate",
								cartList.get(i).get("closeDate").toString());
						cartList.set(i, products_hash);
						adapternei.notifyDataSetChanged();
					}
				} else {

				}

			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(context);
				// set item background
				// openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
				// 0xC9,
				// 0xCE)));
				openItem.setBackground(R.color.main_color);
				// set item width
				openItem.setWidth(dp2px(70));
				// set item title
				openItem.setTitle("验苗");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(70));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		lv_waitpay_nei.setMenuCreator(creator);

		// step 2. listener item click event
		lv_waitpay_nei
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public void onMenuItemClick(int pos, SwipeMenu menu,
							int index) {
						switch (index) {
						case 0:
							break;
						case 1:
							// delete
							// delete(item);
							cartList.remove(pos);
							adapternei.notify(cartList);
							break;
						}
					}
				});

		// set SwipeListener
		lv_waitpay_nei.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
		lv_waitpay_nei.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent toFlowerDetailActivity = new Intent(context,
						FlowerDetailActivity.class);
				toFlowerDetailActivity.putExtra("id",
						cartList.get(arg2).get("id").toString());
				toFlowerDetailActivity.putExtra("show_type", cartList.get(arg2)
						.get("show_type").toString());
				context.startActivity(toFlowerDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}

		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
