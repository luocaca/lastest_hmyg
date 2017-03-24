package com.zzy.common.widget.wheelview;
/**
 * 字符串选择
 */
public class StringWheelAdapter implements WheelAdapter {

	private String[] datas;

	public StringWheelAdapter(String[] datas) {
		super();
		this.datas = datas;
	}

	@Override
	public int getItemsCount() {
		return datas.length;
	}

	@Override
	public int getItemId(int index) {
		return index;
	}

	@Override
	public String getItem(int index) {
		return datas[index];
	}

	@Override
	public int getMaximumLength() {
		int maxLen = 1;
		for (int i = 0; i < datas.length; i++) {
			maxLen = Math.max(maxLen, datas[i].length());
		}
		maxLen *= 2;
		return maxLen;
	}
}
