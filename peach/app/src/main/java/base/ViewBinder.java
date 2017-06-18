package base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.utils.IRangeSeekBar;
import com.xygame.sg.utils.MyIRangeSeekBar;

import java.util.Map;
import java.util.TreeMap;

import base.action.ClickListener;
import base.action.Var;
import base.action.widget.LabelContainer;
import base.adapter.Logs;
import base.adapter.MapAdapter;
import base.adapter.MapAdapter.AdaptInfo;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/10/23.
 */
public class ViewBinder extends LayoutInflater {
    public static final String ANDROID_NAMINGSPACE = "http://schemas.android.com/apk/res/android";
    private static final String BASE_NAMING = "http://schemas.android.com/apk/base";
    static final String resauto = "http://schemas.android.com/apk/res-auto";

    private Context context;
    View view;
    private VisitUnit visitUnit;

    public ViewBinder(Context context, VisitUnit visitUnit) {
        super(context);
        this.visitUnit = visitUnit;
        if (visitUnit.getDisplay() == null) {
            visitUnit.setDisplay(context);
        }

    }


    @Override
    public LayoutInflater cloneInContext(Context context) {

        return null;
    }

    public static Map<String, String> view_names = new TreeMap<String, String>();

    {
        view_names.put("WebView", "android.webkit.");
        view_names.put("RefreshListView", "com.xygame.sg.define.listview.");
        view_names.put("RefreshGridView", "com.xygame.sg.define.gridview.");
        view_names.put("ImageView", "base.action.widget.");
        view_names.put("CircularImage", "com.xygame.sg.define.view.");
        view_names.put("IRangeSeekBar", "com.xygame.sg.utils.");
        view_names.put("RadioTextGroup", "base.action.widget.");
        view_names.put("RadioText", "base.action.widget.");
        view_names.put("HorizontalListView", "com.xygame.sg.utils.");
        view_names.put("LabelContainer", "base.action.widget.");
        view_names.put("NoScrollGridView", "com.xygame.sg.utils.");
        view_names.put("MyIRangeSeekBar", "com.xygame.sg.utils.");
    }


    @Override
    protected View onCreateView(String name, AttributeSet attrs) {
        try {
            if (name.contains(".") || name.equals("View")) {
                view = super.onCreateView(name, attrs);

            } else if (view_names.containsKey(name)) {
                view = super.createView(name, view_names.get(name), attrs);
            } else {
                Logs.e("------------ name" + name);
                view = super.createView(name, "android.widget.", attrs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (view instanceof LabelContainer) {
                int value = attrs.getAttributeResourceValue(BASE_NAMING, "text", -1);
                if (value != -1) {
                    String text = view.getContext().getResources().getString(value);
                    if (text.startsWith("${") && text.endsWith("}")) {
                        Var var = new Var(text, visitUnit.getDataUnit());

                        visitUnit.putDisplayRelation(var.getNames(), view);
                        boolean notinData = false;
                        for (String n : var.getNames()) {
                            notinData = visitUnit.pushDataWithKey(n);

                        }
                        if (!notinData) {
                            if (var.hasDefault()) {
                                visitUnit.setInnerViewValue(view, var.getDefault());
                            } else {
                                visitUnit.setInnerViewValue(view, "");
                            }
                        }
                    }
                }
            } else if (view instanceof TextView) {
                int value = attrs.getAttributeResourceValue(ANDROID_NAMINGSPACE, "text", -1);
                if (value != -1) {
                    String text = view.getContext().getResources().getString(value);
                    if (text.startsWith("${") && text.endsWith("}")) {
                        Var var = new Var(text, visitUnit.getDataUnit());

                        visitUnit.putDisplayRelation(var.getNames(), view);
                        boolean notinData = false;
                        for (String n : var.getNames()) {
                            notinData = visitUnit.pushDataWithKey(n);

                        }
                        if (!notinData) {
                            if (var.hasDefault()) {
                                visitUnit.setInnerViewValue(view, var.getDefault());
                            } else {
                                visitUnit.setInnerViewValue(view, "");
                            }
                        }
                    }
                }
            } else if (view instanceof AdapterView) {
                int value = attrs.getAttributeResourceValue(BASE_NAMING, "layout", -1);
                int replace = attrs.getAttributeResourceValue(BASE_NAMING, "replace", -1);
                String ids = attrs.getAttributeValue(BASE_NAMING, "ids");
                String names = attrs.getAttributeValue(BASE_NAMING, "names");
                int itext = attrs.getAttributeResourceValue(BASE_NAMING, "text", -1);
                String text = null;
                if (itext != -1) {
                    text = view.getContext().getResources().getString(itext).trim();

                }

                String replaceflag = "true";
                if (replace != -1) {
                    replaceflag = view.getContext().getResources().getString(replace).trim();
                }
                if (!(value == -1 || text == null || text.trim().equals(""))) {
                    if (!(ids == null || ids.trim().equals("") || names == null || names.trim().equals(""))) {
                        String[] idsry = ids.replaceAll(" ", "").split(",");
                        String[] namesry = names.replaceAll(" ", "").split(",");
                        AdaptInfo adaptInfo = new AdaptInfo();
                        adaptInfo.listviewItemLayoutId = value;
                        adaptInfo.objectFields = namesry;
                        adaptInfo.viewIds = AdaptInfo.transferStringIds(idsry);
                        MapAdapter adapter = new MapAdapter(view.getContext(), adaptInfo);
                        adapter.replaceflag = Boolean.parseBoolean(replaceflag);
                        ((AdapterView) view).setAdapter(adapter);

                        if (text.startsWith("${") && text.endsWith("}")) {
                            Var var = new Var(text, visitUnit.getDataUnit());

                            visitUnit.putDisplayRelation(var.getNames(), view);
                            boolean notinData = false;
                            for (String n : var.getNames()) {
                                notinData = visitUnit.pushDataWithKey(n);

                            }

                        } else if (text.startsWith("#") && text.endsWith("")) {

                        }
                    } else {
                        AdaptInfo adaptInfo = new AdaptInfo();
                        adaptInfo.listviewItemLayoutId = value;
                        MapAdapter adapter = new MapAdapter(view.getContext(), adaptInfo);
                        adapter.replaceflag = Boolean.parseBoolean(replaceflag);
                        ((AdapterView) view).setAdapter(adapter);

                        if (text.startsWith("${") && text.endsWith("}")) {
                            Var var = new Var(text, visitUnit.getDataUnit());

                            visitUnit.putDisplayRelation(var.getNames(), view);
                            boolean notinData = false;
                            for (String n : var.getNames()) {
                                notinData = visitUnit.pushDataWithKey(n);

                            }

                        }
                    }

                } else {
                    AdaptInfo adaptInfo = new AdaptInfo();
                    adaptInfo.listviewItemLayoutId = value;
                    MapAdapter adapter = new MapAdapter(view.getContext(), adaptInfo);
                    ((AdapterView) view).setAdapter(adapter);

                    if (text != null && text.startsWith("${") && text.endsWith("}")) {
                        Var var = new Var(text, visitUnit.getDataUnit());
                        visitUnit.putDisplayRelation(var.getNames(), view);
                        boolean notinData = false;
                        for (String n : var.getNames()) {
                            notinData = visitUnit.pushDataWithKey(n);
                        }

                    }
                }
            } else if (view instanceof IRangeSeekBar) {
                visitUnit.setView2Display(view, null);
                ((IRangeSeekBar) view).setVisitUnit(visitUnit);
                Object minv = visitUnit.getDataUnit().getRepo().get(((IRangeSeekBar) view).getBegin());
                if (minv!=null&& !minv.toString().equals("false")&& !minv.toString().equals("true")&& !minv.toString().equals("")) {
                    Integer min = Integer.parseInt(minv.toString());
                    ((IRangeSeekBar) view).setSelectedMinValue(min);
                }
                Object maxv = visitUnit.getDataUnit().getRepo().get(((IRangeSeekBar) view).getEnd());
                if (maxv!=null&& !maxv.toString().equals("false")&& !maxv.toString().equals("true")&& !maxv.toString().equals("")) {
                    Integer max = Integer.parseInt( maxv.toString());
                    ((IRangeSeekBar) view).setSelectedMaxValue(max);
                }
                view.invalidate();
            } else if (view instanceof MyIRangeSeekBar) {
                visitUnit.setView2Display(view, null);
                ((MyIRangeSeekBar) view).setVisitUnit(visitUnit);
                Object minv = visitUnit.getDataUnit().getRepo().get(((MyIRangeSeekBar) view).getBegin());
                if (minv!=null&& !minv.toString().equals("false")&& !minv.toString().equals("true")&& !minv.toString().equals("")) {
                    Integer min = Integer.parseInt(minv.toString());
                    ((MyIRangeSeekBar) view).setSelectedMinValue(min);
                }
                Object maxv = visitUnit.getDataUnit().getRepo().get(((MyIRangeSeekBar) view).getEnd());
                if (maxv!=null&& !maxv.toString().equals("false")&& !maxv.toString().equals("true")&& !maxv.toString().equals("")) {
                    Integer max = Integer.parseInt(maxv.toString());
                    ((MyIRangeSeekBar) view).setSelectedMaxValue(max);
                }
                view.invalidate();
            } else if (view instanceof ImageView) {
//                if(view instanceof base.action.widget.ImageView){
                int itext = attrs.getAttributeResourceValue(BASE_NAMING, "url", 0);
                if (itext != 0) {

                    String text = view.getContext().getResources().getString(itext).trim();
                    if (text != null && !text.equals("")) {

                        if (text.startsWith("${") && text.endsWith("}")) {
                            Var var = new Var(text, visitUnit.getDataUnit());

                            visitUnit.putDisplayRelation(var.getNames(), view);
                            boolean notinData = false;
                            for (String n : var.getNames()) {
                                notinData = visitUnit.pushDataWithKey(n);
                            }
                            if (!notinData) {
                                if (var.hasDefault()) {
                                    visitUnit.setInnerViewValue(view, var.getDefault());
                                } else {
                                    visitUnit.setInnerViewValue(view, "");
                                }
                            }
                        }
                    }
                }
            }

            int value = attrs.getAttributeResourceValue(BASE_NAMING, "onClick", -1);
            if (value != -1) {
                String text = view.getContext().getResources().getString(value).trim();
                if (text.startsWith("#") && text.contains("(") && text.contains(")")) {

                    view.setOnClickListener(new ClickListener(Actions.parseActions(text, (Activity) context, visitUnit.getDisplay(), visitUnit)));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

}
