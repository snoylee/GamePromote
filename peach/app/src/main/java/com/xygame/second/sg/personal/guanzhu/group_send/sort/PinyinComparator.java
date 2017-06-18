package com.xygame.second.sg.personal.guanzhu.group_send.sort;

import com.xygame.second.sg.personal.guanzhu.group_send.MemberBean;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<MemberBean> {

	public int compare(MemberBean o1, MemberBean o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
