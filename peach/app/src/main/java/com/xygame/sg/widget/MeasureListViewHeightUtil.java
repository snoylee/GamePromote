package com.xygame.sg.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by xy on 2015/12/24.
 */
public class MeasureListViewHeightUtil {
    /**
     * 解决scrollview或listview嵌套的listview只显示第一项的问题（在为ListView设置了Adapter之后使用），注意：
     * 一是Adapter中getView方法返回的View的必须由LinearLayout组成，因为只有LinearLayout才有measure()方法，
     * 如果使用其他的布局如RelativeLayout，在调用listItem.measure(0, 0);时就会抛异常，因为除LinearLayout外的其他布局的这个方法就是直接抛异常的。
     *二是需要手动把ScrollView滚动至最顶端，因为使用这个方法的话，默认在ScrollView顶端的项是ListView，具体原因不了解，求大神解答…可以在Activity中设置：
     * sv = (ScrollView) findViewById(R.id.act_solution_1_sv);
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
