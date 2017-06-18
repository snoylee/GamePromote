package base.action;

import android.app.Activity;

import java.util.List;

import base.frame.DataUnit;
import base.frame.ParentFragment;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/1.
 */
public class Epr {
	public Object run() {
		return null;
	}


	public static Object parse(String text, Activity activity, Object fragment, VisitUnit visit) {
		boolean methodprior = text.startsWith("#") && text.contains("(") && text.contains(")");
		boolean varprior = text.startsWith("$") && text.contains("{") && text.contains("}");

		String epr;
		int pre;
		int after;
		String spre = "";
		String safter = "";
		if (methodprior || text.contains("#") && text.contains("(") && text.contains(")")) {
			epr = text.substring(pre = text.indexOf("#"), after = text.lastIndexOf(")") + 1);
			if (pre != 0) {
				spre = text.substring(0, pre);
			}
			if (after != text.length()) {
				safter = text.substring(after);
			}
			return new Param(spre, new Action(text, activity, fragment, visit), safter);
		} else if (varprior || text.contains("$") && text.contains("{") && text.contains("}")) {
			epr = text.substring(pre = text.indexOf("$"), after = text.lastIndexOf("}") + 1);
			if (pre != 0) {
				spre = text.substring(0, pre);
			}
			if (after != text.length()) {
				safter = text.substring(after);
			}
			return new Param(spre, new Var(epr, visit.getDataUnit()), safter);
		}
		return text;
	}

	public static void addAll(List<Object> set, String[] str, Activity aty, ParentFragment parentFragment,
			VisitUnit visit) {
		for (int i = 0; i < str.length; i++) {
			set.add(Epr.parse(str[i], aty, parentFragment, visit));
		}
	}
}
