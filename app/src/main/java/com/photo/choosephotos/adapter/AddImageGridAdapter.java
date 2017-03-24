package com.photo.choosephotos.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.framework.DragGridBaseAdapter;
import com.hldj.hmyg.R;
import com.photo.choosephotos.photo.Item;

public class AddImageGridAdapter extends BaseAdapter{
	// 定义Context
	private Context context;
	// 图片地址
	private List<Bitmap> imageList = new ArrayList<Bitmap>();
	private List<Item> photoList = new ArrayList<Item>();
	private int mHidePosition = -1;

	public AddImageGridAdapter(Context context) {
		this.context = context;
	}

	public AddImageGridAdapter(Context context, List<Bitmap> imageList,
			List<Item> photoList) {
		this.context = context;
		this.imageList = imageList;
		this.photoList = photoList;
	}

	// 获取图片的个数
	public int getCount() {
		return imageList.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return position;
	}

	// 获取图片ID
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.image_add_grid_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.img_view);
		ImageView iv_img2 = (ImageView) view.findViewById(R.id.iv_img2);
		if(imageList.size()>1){
			if(position ==0){
				iv_img2.setImageResource(R.drawable.fengmian);
			}
		}
		imageView.setImageBitmap(imageList.get(position));
		return view;
	}

	public List<Bitmap> getImageList() {
		return imageList;
	}

	public List<Item> getPhotoList() {
		return photoList;
	}

	public void notify(ArrayList<Bitmap> imageList) {
		this.imageList = imageList;
		notifyDataSetChanged();
	}


}
