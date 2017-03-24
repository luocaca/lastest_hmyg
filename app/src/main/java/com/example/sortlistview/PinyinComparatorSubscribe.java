package com.example.sortlistview;

import java.util.Comparator;

import com.hldj.hmyg.bean.XiaoQuYu;
import com.hldj.hmyg.saler.bean.Subscribe;

/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparatorSubscribe implements Comparator<Subscribe> {

	public int compare(Subscribe o1, Subscribe o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
