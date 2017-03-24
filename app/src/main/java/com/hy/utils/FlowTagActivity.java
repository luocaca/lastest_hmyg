package com.hy.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagClickListener;
import com.hhl.library.OnTagSelectListener;
import com.hldj.hmyg.R;

public class FlowTagActivity extends Activity {

    private FlowTagLayout mColorFlowTagLayout;
    private FlowTagLayout mSizeFlowTagLayout;
    private FlowTagLayout mMobileFlowTagLayout;
    private TagAdapter<String> mSizeTagAdapter;
    private TagAdapter<String> mColorTagAdapter;
    private TagAdapter<String> mMobileTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_flowtag);

        mColorFlowTagLayout = (FlowTagLayout) findViewById(R.id.color_flow_layout);
        mSizeFlowTagLayout = (FlowTagLayout) findViewById(R.id.size_flow_layout);
        mMobileFlowTagLayout = (FlowTagLayout) findViewById(R.id.mobile_flow_layout);

        //颜色
        mColorTagAdapter = new TagAdapter<String>(this);
        mColorFlowTagLayout.setAdapter(mColorTagAdapter);
        mColorFlowTagLayout.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                ToastUtil.showShortToast(FlowTagActivity.this, "颜色:" + parent.getAdapter().getItem(position));
                
            }
        });

        //尺寸
        mSizeTagAdapter = new TagAdapter<String>(this);
        mSizeFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mSizeFlowTagLayout.setAdapter(mSizeTagAdapter);
        mSizeFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    ToastUtil.showShortToast(FlowTagActivity.this,  "移动研发:" + sb.toString());
                }else{
                    ToastUtil.showShortToast(FlowTagActivity.this, "没有选择标签");
                }
            }
        });
        mSizeFlowTagLayout.setOnTagClickListener(new OnTagClickListener() {
			
			@Override
			public void onItemClick(FlowTagLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				
			}
		});

        //移动研发标签
        mMobileTagAdapter = new TagAdapter<String>(this);
        mMobileFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mMobileFlowTagLayout.setAdapter(mMobileTagAdapter);
        mMobileFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    ToastUtil.showShortToast(FlowTagActivity.this, "移动研发:" + sb.toString());
                }else{
                    ToastUtil.showShortToast(FlowTagActivity.this, "没有选择标签");
                }
            }
        });
        mMobileFlowTagLayout.setOnTagClickListener(new OnTagClickListener() {
			
			@Override
			public void onItemClick(FlowTagLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				
			}
		});
     
        initColorData();

        initSizeData();

        initMobileData();
    }

    private void initMobileData() {
        List<String> dataSource = new ArrayList<String>();
        dataSource.add("android");
        dataSource.add("安卓");
        dataSource.add("SDK源码");
        dataSource.add("IOS");
        dataSource.add("iPhone");
        dataSource.add("游戏");
        dataSource.add("fragment");
        dataSource.add("viewcontroller");
        dataSource.add("cocoachina");
        dataSource.add("移动研发工程师");
        dataSource.add("移动互联网");
        dataSource.add("高薪+期权");
        mMobileTagAdapter.onlyAddAll(dataSource);
    }

    private void initColorData() {
        List<String> dataSource = new ArrayList<String>();
        dataSource.add("红色");
        dataSource.add("黑色");
        dataSource.add("花边色");
        dataSource.add("深蓝色");
        dataSource.add("白色");
        dataSource.add("玫瑰红色");
        dataSource.add("紫黑紫兰色");
        dataSource.add("葡萄红色");
        dataSource.add("屎黄色");
        dataSource.add("绿色");
        dataSource.add("彩虹色");
        dataSource.add("牡丹色");
        mColorTagAdapter.onlyAddAll(dataSource);
    }

    /**
     * 初始化数据
     */
    private void initSizeData() {
        List<String> dataSource = new ArrayList<String>();
        dataSource.add("红色");
        dataSource.add("黑色");
        dataSource.add("花边色");
        dataSource.add("深蓝色");
        dataSource.add("白色");
        dataSource.add("玫瑰红色");
        dataSource.add("紫黑紫兰色");
        dataSource.add("葡萄红色");
        dataSource.add("屎黄色");
        dataSource.add("绿色");
        dataSource.add("彩虹色");
        dataSource.add("牡丹色");
        mSizeTagAdapter.onlyAddAll(dataSource);
    }

}
