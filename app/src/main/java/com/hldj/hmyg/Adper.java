package com.hldj.hmyg;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hldj.hmyg.bean.OrderGood;

public class Adper extends BaseAdapter {
    List<OrderGood> list;
    Context context;
    
    public Adper(List<OrderGood> list, Context context) {
        // TODO Auto-generated constructor stub
    this.list=list;
    this.context=context;
    
    
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.list_item_buy_order, null);
         viewHolder=new ViewHolder();
         viewHolder.textView=(TextView) convertView.findViewById(R.id.text);
        viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkbox); 
        convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText("价格："+list.get(position).getName());
        //显示checkBox
        viewHolder.checkBox.setChecked(list.get(position).getBo());
        
        
        viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				viewHolder.checkBox.toggle();
				if (viewHolder.checkBox.isChecked() == true) {
					BuyOrderActivity.num++;
					BuyOrderActivity.pric += Double.parseDouble(list.get(position).getName());
				} else {
					BuyOrderActivity.num--;
					BuyOrderActivity.pric -= Double.parseDouble(list.get(position).getName());
				}
				BuyOrderActivity.showprice();
			}
		});
        
        return convertView;
        
    }
public class ViewHolder{
    TextView textView;
    CheckBox checkBox;
}
}
