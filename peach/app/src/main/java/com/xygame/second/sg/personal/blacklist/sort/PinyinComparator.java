package com.xygame.second.sg.personal.blacklist.sort;

import com.xygame.second.sg.personal.blacklist.BlackMemberBean;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<BlackMemberBean> {

	public int compare(BlackMemberBean o1, BlackMemberBean o2) {
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
