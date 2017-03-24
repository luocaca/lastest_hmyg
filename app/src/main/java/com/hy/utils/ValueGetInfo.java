package com.hy.utils;

public class ValueGetInfo {

	public static String doubleTrans1(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}

	public static String doubleTrans2(double num) {
		if (Math.round(num) - num == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}

	public static String formatNoDataString(String noData) {
		if (noData != null && "-".equals(noData)) {
			String daString = "规格：" + noData;
			return daString;
		} else {
			return noData;
		}

	}

	public static String formatFloatNumber(double value) {
		if (value != 0.00) {
			java.text.DecimalFormat df = new java.text.DecimalFormat(
					"########.00");
			return df.format(value);
		} else {
			return "0.00";
		}

	}

	public static String getValueString(String dbh, String height,
			String crown, String diameter, String offbarHeight) {

		StringBuffer sBuffer = new StringBuffer();
		if (!"0".equals(dbh) && !"0.0".equals(dbh) && !"".equals(dbh)) {
			sBuffer.append("胸径：" + dbh);
		}
		if (!"0".equals(height) && !"0.0".equals(height) && !"".equals(height)) {
			sBuffer.append("高度：" + height);
		}
		if (!"0".equals(crown) && !"0.0".equals(crown) && !"".equals(crown)) {
			sBuffer.append("冠幅：" + crown);
		}
		if (!"0".equals(diameter) && !"0.0".equals(diameter)
				&& !"".equals(diameter)) {
			sBuffer.append("地径：" + diameter);
		}
		if (!"0".equals(offbarHeight) && !"0.0".equals(offbarHeight)
				&& !"".equals(offbarHeight)) {
			sBuffer.append("脱杆高：" + offbarHeight);
		}
		return sBuffer.toString();
	}

	public static String getValueStringByTag(String tag, String dbh,
			String height, String crown, String diameter, String offbarHeight) {

		StringBuffer sBuffer = new StringBuffer();
		if (tag.contains("dbh")) {
			if (!"0".equals(dbh) && !"0.0".equals(dbh) && !"".equals(dbh)) {
				sBuffer.append("胸径：" + dbh);
			} else {
				sBuffer.append("胸径:-");
			}
		}
		if (tag.contains("height")) {
			if (!"0".equals(height) && !"0.0".equals(height)
					&& !"".equals(height)) {
				sBuffer.append("高度：" + height);
			} else {
				sBuffer.append("高度：-");
			}
		}
		if (tag.contains("crown")) {
			if (!"0".equals(crown) && !"0.0".equals(crown) && !"".equals(crown)) {
				sBuffer.append("冠幅：" + crown);
			} else {
				sBuffer.append("冠幅：-");
			}
		}
		if (tag.contains("diameter")) {
			if (!"0".equals(diameter) && !"0.0".equals(diameter)
					&& !"".equals(diameter)) {
				sBuffer.append("地径：" + diameter);
			} else {
				sBuffer.append("地径：-");
			}
		}

		if (tag.contains("offbarHeight")) {
			if (!"0".equals(offbarHeight) && !"0.0".equals(offbarHeight)
					&& !"".equals(offbarHeight)) {
				sBuffer.append("脱杆高：" + offbarHeight);
			} else {
				sBuffer.append("脱杆高：-");
			}
		}

		return sBuffer.toString();
	}

}
