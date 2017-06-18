package com.xygame.second.sg.personal.guanzhu.group.sort;

import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBean;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<GZ_GroupBean> {

	public int compare(GZ_GroupBean o1, GZ_GroupBean o2) {
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
