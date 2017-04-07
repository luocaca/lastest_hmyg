package com.hldj.hmyg.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.util.D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortSpinner {
    // PopupWindow对象
    private PopupWindow selectPopupWindow = null;
    // 自定义Adapter
    private SortListAdapter optionsAdapter = null;
    // 下拉框依附组件
    private View parent;
    // 下拉框依附组件宽度，也将作为下拉框的宽度
    private int pwidth;
    // 文本框
    private EditText et;
    // 下拉箭头图片组件
    private ImageView image;
    // 展示所有下拉选项的ListView
    private ListView listView = null;

    // 是否初始化完成标志
    private boolean flag = false;
    Activity activity;

    TextView mTextView;

    public SortListAdapter getSortListAdapter()
    {
        return optionsAdapter ;
    }
    private static SortSpinner sortSpinner;

    private SortSpinner(Activity activity, View parent) {
        this.activity = activity;
        this.parent = parent;
        initData();//初始化数据
        initWedget();//初始化控件

    }


    public static SortSpinner getInstance(Activity activity, View line) {
        if (sortSpinner == null) {

            sortSpinner = new SortSpinner(activity, line);
        }
        return sortSpinner;
    }

    public static SortSpinner getInstance() {

        return sortSpinner;
    }

    //	AdapterView.OnItemClickListener onItemClickListener;
    public SortSpinner addOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
//		this.onItemClickListener = onItemClickListener ;
        listView.setOnItemClickListener(onItemClickListener);

        return this;
    }



    List<Map<String, String>> list_map = new ArrayList<>();


    public String orderBy  = "";
    public SortSpinner addData(String str ) {

        this.orderBy = str ;

        return this;
    }

    public void initData()
    {
        this.list_map   =  getSortMaps();

    }

    List<Map<String,String>> list_maps = new ArrayList<>();
    /**
     * 获取排序列表
     *
     * @return
     */
    private List<Map<String, String>> getSortMaps() {
        list_maps.add(createMap("default_asc", "综合排序"));
        list_maps.add(createMap("publishDate_desc", "最新发布"));
        list_maps.add(createMap("distance_asc", "最近距离"));
        list_maps.add(createMap("price_asc", "价格从低到高"));
        list_maps.add(createMap("price_desc", "价格从高到底"));
        return list_maps;
    }

    public Map createMap (String key ,String value)
    {
        Map map = new HashMap();
        map.put(key, value);
        return map ;
    }



    /**
     * 初始化界面控件
     */
    Context context;


    private void initWedget() {


        // 获取下拉框依附的组件宽度
        int width = parent.getWidth();
        pwidth = width;

        // 初始化PopupWindow
        initPopuWindow();

    }


    /**
     * 初始化PopupWindow
     */
    private void initPopuWindow() {
        // PopupWindow浮动下拉框布局
        View loginwindow = (View) activity.getLayoutInflater().inflate(R.layout.popo_shop_type_list, null);
        listView = (ListView) loginwindow.findViewById(R.id.listview);
        // 设置自定义Adapter
        optionsAdapter = new SortListAdapter();
        listView.setAdapter(optionsAdapter);
//		listView.setOnItemClickListener(onItemClickListener);
        selectPopupWindow = new PopupWindow(loginwindow, pwidth, LayoutParams.WRAP_CONTENT, true);

        // 设置动画效果
//		selectPopupWindow.setAnimationStyle(R.style.Dialog);


//		activity.getWindow().addFlags(View.dar);

//				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//				| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        selectPopupWindow.setOutsideTouchable(true);

        selectPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

        // 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
        // 没有这一句则效果不能出来，但并不会影响背景
        // 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 显示PopupWindow窗口
     *
     * @param
     */
    public SortSpinner Show() {
        // 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
        // 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
        // （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
        selectPopupWindow.showAsDropDown(parent, 0, -3);
        optionsAdapter.notifyDataSetChanged();
        return this;
    }
    /**
     * 显示PopupWindow窗口
     *
     * @param
     */
    public SortSpinner ShowWithPos(int pos) {
        getSortListAdapter().setSeclection(pos);
        // 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
        // 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
        // （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
        selectPopupWindow.showAsDropDown(parent, 0, -3);
        optionsAdapter.notifyDataSetChanged();
        return this;
    }

    /**
     * PopupWindow消失
     */
    public void dismiss() {
        D.e("========dismiss=========");


        selectPopupWindow.dismiss();
    }


    class SortListAdapter extends BaseAdapter {

        private int clickTemp = 0;

        // 标识选择的Item
        public void setSeclection(int position) {
            clickTemp = position;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_map.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View sort_list_item = activity.getLayoutInflater().inflate(R.layout.list_item_sort, null);
            RelativeLayout rl_sort_list_item = (RelativeLayout) sort_list_item.findViewById(R.id.rl_popo_list_item);
            TextView area_tv_item = (TextView) sort_list_item.findViewById(R.id.tv_item);


            for (Map.Entry<String, String> entry : list_map.get(position).entrySet()) {
                area_tv_item.setText(entry.getValue());
            }

            ImageView is_check = (ImageView) sort_list_item.findViewById(R.id.is_check);
//			area_tv_item.setText(listData.get(position).getName());
            if (clickTemp == position) {
                rl_sort_list_item.setBackgroundColor(Color.argb(155, 192, 192,
                        192)); // #COCOCO
                is_check.setVisibility(View.VISIBLE);
            } else {
                rl_sort_list_item.setBackgroundColor(Color.argb(155, 255, 255,
                        255)); // #FFFFFF
                is_check.setVisibility(View.INVISIBLE);
            }


            return sort_list_item;
        }

    }

    class ViewHolder {
        TextView tv_item;
        ImageView is_check;
    }

}
