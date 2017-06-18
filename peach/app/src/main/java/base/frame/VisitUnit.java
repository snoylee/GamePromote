package base.frame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.define.gridview.RefreshGridView;
import com.xygame.sg.define.listview.RefreshListView;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.IRangeSeekBar;
import com.xygame.sg.utils.MyIRangeSeekBar;
import com.xygame.sg.utils.MyRangeSeekBar;
import com.xygame.sg.utils.RangeSeekBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import base.RRes;
import base.action.Action;
import base.action.CenterRepo;
import base.action.asyc.BackRunnable;
import base.action.asyc.CallbackRunnable;
import base.action.asyc.Compt;
import base.action.widget.LabelContainer;
import base.adapter.MapAdapter;
import base.adapter.MapContent;

/**
 * Created by minhua on 2015/10/25.
 */
public class VisitUnit {

    public Object display;

    public Object getDisplay() {
        return display;
    }

    public VisitUnit setDisplay(Object display) {
        this.display = display;
        if (display instanceof ParentFragment) {
            ((ParentFragment) display).setUnit(this);
        }
        return this;
    }
    public static void pour(VisitUnit src,Class clazz) {
        get(clazz).setDataUnit(src.getDataUnit(),true);
    }
    public static void absorb(Class clazz,VisitUnit src) {
        src.setDataUnit(get(clazz).getDataUnit(),true);
    }
    public VisitUnit(Object display) {
        super();
        this.display = display;
        setDataUnit(CenterRepo.getInsatnce(), true);
    }

    public VisitUnit() {
        this(null);
        setDataUnit(new DataUnit(), true);
    }

    public VisitUnit(boolean newDataUnit) {
        if(newDataUnit){
            setDataUnit(new DataUnit(), false);
        }
    }

    private Map<View, String> valueTransfers = new HashMap<View, String>();
    private Map<String, Set<String>> fillsignarea = new TreeMap<String, Set<String>>();// one
    // time
    // result
    // confirm
    // area
    private Map<String, Object> displayKV = new TreeMap<String, Object>();
    // need fill data by preparing above two struc

    private Map<View, Set<String>> displayRelation = new HashMap<View, Set<String>>();

    public void putDisplayRelation(List<String> displaysigns, View view) {
        String[] displaysignAys = new String[displaysigns.size()];
        displaysigns.toArray(displaysignAys);
        putDisplayRelation(displaysignAys, view);
    }

    public void putDisplayRelation(String sign, View view) {
        Set<String> set = displayRelation.get(view);
        if (set == null) {
            set = new TreeSet<String>();

            displayRelation.put(view, set);
        }
        set.add(sign);
        displayKV.put(sign, null);
    }

    public void putDisplayRelation(String[] displaysigns, View view) {
        String[] displaysignAys = displaysigns;
        Set<String> set = displayRelation.get(view);
        if (set == null) {
            set = new TreeSet<String>();

            displayRelation.put(view, set);
        }

        for (String displaysign : displaysignAys) {
            String innervk;

            if (displaysign.contains("#")) {
                int s = displaysign.indexOf("#");
                int l = displaysign.indexOf("(", s);
                int r = displaysign.indexOf(")", l);
                innervk = displaysign.substring(l + 1, r);
                valueTransfers.put(view, displaysign.substring(s + 1, l));
            } else {
                innervk = displaysign;
            }
            set.add(innervk);
            displayKV.put(innervk, null);

        }
        setView2Display(view, displaysignAys);
    }

    public Map<String, Set<String>> getFillsignarea() {
        return fillsignarea;
    }

    public void putDisplayKeyValue(Map<String, Object> addedDisplayKeyValue) {
        Set<String> keys = new TreeSet<String>(addedDisplayKeyValue.keySet());
        for (String vk : keys) {

            if (displayKV.containsKey(vk)) {
                Object value;

                value = addedDisplayKeyValue.get(vk);

                putDisplayKeyValue(vk, value);
            }
        }

    }

    public void putDisplayKeyValue(String vk, Object value) {
        displayKV.put(vk, value);
        if (submitToCenter) {
            if (target != null) {
                target.getDataUnit().getRepo().put(vk, value);

            }
        }
    }

    public boolean submitToCenter;

    public boolean isSubmitToCenter() {
        return submitToCenter;
    }

    public VisitUnit setTarget(boolean submitToCenter) {
        this.submitToCenter = submitToCenter;
        return this;
    }

    VisitUnit target;

    public VisitUnit setTarget(VisitUnit unit) {
        this.submitToCenter = true;
        target = unit;
        return this;
    }

    public void putFillsignarea(String time, Set<String> filling) {
        fillsignarea.put(time, filling);

    }

    public void setView2Display(final View view, final String[] displaysign) {
        if (view instanceof TextView) {
            ((TextView) view).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    for (String ds : displaysign) {
                        displayKV.put(ds, s.toString());
                        getDataUnit().getRepo().put(ds, s.toString());

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (view instanceof IRangeSeekBar) {
            ((IRangeSeekBar) view).setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    if (((IRangeSeekBar) view).getAbsoluteMinValue().equals(minValue)) {
                        minValue = "";
                    }
                    if (((IRangeSeekBar) view).getAbsoluteMaxValue().equals(maxValue)) {
                        maxValue = "";
                    }
                    getDataUnit().getRepo().put(((IRangeSeekBar) view).getBegin(), minValue);
                    getDataUnit().getRepo().put(((IRangeSeekBar) view).getEnd(), maxValue);
                }
            });
        }else if (view instanceof MyIRangeSeekBar) {
            ((MyIRangeSeekBar) view).setOnRangeSeekBarChangeListener(new MyRangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(MyRangeSeekBar bar, Object minValue, Object maxValue) {
                    if (((MyIRangeSeekBar) view).getAbsoluteMinValue().equals(minValue)) {
                        minValue = "";
                    }
                    if (((MyIRangeSeekBar) view).getAbsoluteMaxValue().equals(maxValue)) {
                        maxValue = "";
                    }
                    getDataUnit().getRepo().put(((MyIRangeSeekBar) view).getBegin(), minValue);
                    getDataUnit().getRepo().put(((MyIRangeSeekBar) view).getEnd(), maxValue);
                }
            });
        }
    }

    public void putValues(String time) {
        Set<String> fillinp = fillsignarea.get(time);
        Set<View> views = new HashSet(displayRelation.keySet());
        Set<String> vk;
        for (View view : views) {
            vk = displayRelation.get(view);
            for (String v : vk) {
                Object value;
                if (fillinp.contains(v)) {
                    if (valueTransfers.containsKey(view)) {
                        String m = "#" + valueTransfers.get(view) + "(" + displayKV.get(v) + ")";
                        value = new Action(m, (Activity) view.getContext(), getDisplay(), this).setOnview(view).run();
                        if (value == null) {
                            value = "";
                        }
                    } else {
                        value = displayKV.get(v);
                    }
                    setViewValues(view, value);
                }
            }
        }
        fillsignarea.remove(time);
    }



    public Map<String, BitmapFactory.Options> options = new TreeMap<String, BitmapFactory.Options>();

    public void setViewValues(final View v, final Object value) {
        if ((value instanceof String) && value.toString().contains(";")) {
            String[] values = value.toString().split(";");
            for (String str : values) {
                setViewValue(v, str);
            }
        } else {
            setViewValue(v, value);
        }
    }

    public void setViewValue(final View v, final Object value) {
        if (value instanceof String) {
            if (((String) value).contains("-")) {

                String key = value.toString().split("-")[0];
                String val = value.toString().split("-")[1];
                if (key.equals("visibility")) {
                    int ival = 0;
                    if (val.equals("visible")) {
                    } else if (val.equals("invisible")) {
                        ival = 4;
                    } else if (val.equals("gone")) {
                        ival = 8;
                    }
                    v.setVisibility(ival);
                } else if (key.equals("textcolor")) {
                    if (val.toString().contains("R.color.")) {
                        if (v instanceof TextView) {
                            ((TextView) v).setTextColor(
                                    v.getResources().getColor(RRes.get(val).getAndroidValue()));
                        }
                    }
                } else {
                    setInnerViewValue(v, value);
                }
            } else {
                setInnerViewValue(v, value);
            }
        } else {
            setInnerViewValue(v, value);
        }
    }

    public void setInnerViewValue(final View v, final Object value) {

        if (v instanceof LabelContainer) {
            List<String> labels = new ArrayList<String>();
            String texts = value.toString();
            List lists = (List) value;
            ((LabelContainer) v).removeAllViews();
            if (lists.size() != 0) {
                for (Object o : lists) {
                    Map m = (Map) o;
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.sg_model_list_item_label, null);
                    String text = m.get("typeName").toString();
//                    if (!((LabelContainer) v).getSets().contains(text)) {
//                        ((LabelContainer) v).getSets().add(text);
                    ((TextView) view.findViewById(R.id.text)).setText(text);
                    if(((LabelContainer) v).getChildCount()<3){
                    ((LabelContainer) v).addView(view);
                    ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).leftMargin = 10;
                    }

//                    }
                }
            }
        }
        if (v instanceof ImageView) {

            if (value.toString().trim().startsWith("http")) {
                ImageLoader mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
                mImageLoader.loadImage(value.toString(), (ImageView) v, false);

            } else {
                if (value.toString().startsWith("R.drawable.")) {
                    final int res = RRes.get(value.toString()).getAndroidValue();

                    new Compt().putTask(new BackRunnable() {
                        @Override
                        public void run() throws Exception {
                            object = BitmapFactory.decodeResource(v.getResources(), res);

                        }
                    }, new CallbackRunnable() {
                        @Override
                        public boolean run(Message msg, boolean error, Activity aty) throws Exception {

                            ((ImageView) v).setImageBitmap((Bitmap) getBackRunnable().getObject());
                            v.invalidate();
                            v.postInvalidate();
                            if (getDisplay() instanceof ParentFragment) {
                                ((ParentFragment) getDisplay()).updateData();
                            }
                            return false;
                        }
                    }).run();
                }
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setText(value.toString());
        } else if (v instanceof AdapterView) {
            if (((AdapterView) v).getAdapter() instanceof HeaderViewListAdapter) {
                HeaderViewListAdapter adapter = ((HeaderViewListAdapter) ((AdapterView) v).getAdapter());
                if (adapter.getWrappedAdapter() instanceof MapAdapter) {
                    if (adapter.getWrappedAdapter().getCount() == 0) {
                        ((MapAdapter) adapter.getWrappedAdapter()).setItemDataSrc(new MapContent(value));
                        ((MapAdapter) adapter.getWrappedAdapter()).notifyDataSetChanged();
//                        ((RefreshListView) v).onRefreshFinish();
                    } else {
                        ((List) ((MapAdapter) adapter.getWrappedAdapter()).getItemDataSrc().getContent()).addAll((Collection) value);
                        ((MapAdapter) adapter.getWrappedAdapter()).notifyDataSetChanged();
//                        ((RefreshListView) v).onRefreshFinish();
                    }
                    ((RefreshListView) v).onRefreshFinish();
                }
            } else if (((AdapterView) v).getAdapter() instanceof MapAdapter) {
                if (((MapAdapter) ((AdapterView) v).getAdapter()).replaceflag) {
                    ((MapAdapter) ((AdapterView) v).getAdapter()).setItemDataSrc(new MapContent(value));
                    ((MapAdapter) ((AdapterView) v).getAdapter()).notifyDataSetChanged();
                    if (v instanceof RefreshGridView) {
//                        ((RefreshGridView) v).onRefreshFinish();
                    }
                } else {
                    if (((AdapterView) v).getAdapter().getCount() == 0) {
                        ((MapAdapter) ((AdapterView) v).getAdapter()).setItemDataSrc(new MapContent(value));
                        ((MapAdapter) ((AdapterView) v).getAdapter()).notifyDataSetChanged();
                        if (v instanceof RefreshGridView) {
//                            ((RefreshGridView) v).onRefreshFinish();
                        }
                    } else {
                        ((List) ((MapAdapter) ((AdapterView) v).getAdapter()).getItemDataSrc().getContent()).addAll((Collection) value);
                        ((MapAdapter) ((AdapterView) v).getAdapter()).notifyDataSetChanged();
                        if (v instanceof RefreshGridView) {
//                            ((RefreshGridView) v).onRefreshFinish();
                        }
                    }

                }
            }
        } else if (v instanceof IRangeSeekBar) {

        }

    }

    DataUnit dataUnit = new DataUnit();

    public VisitUnit setDataUnit(DataUnit unitvalue) {
        return setDataUnit(unitvalue, false);
    }

    public VisitUnit setDataUnit(DataUnit unitvalue, boolean deep) {
        if (!deep) {

            dataUnit = unitvalue;
        } else {
            Set<String> set = new TreeSet<String>(unitvalue.getRepo().keySet());
            for (String s : set) {
                Object val = unitvalue.getRepo().get(s);
                if (val instanceof List) {
                    dataUnit.getRepo().put(s, new ArrayList((Collection) val));
                } else {
                    dataUnit.getRepo().put(s, val);
                }
            }
        }
        return this;
    }

    public DataUnit getDataUnit() {
        return dataUnit;
    }

    public static Map<String,VisitUnit> visitUnitMap = new TreeMap<String, VisitUnit>();


    public static Map<String, VisitUnit> putVisitUnitMap(Object clazz,VisitUnit visitUnit) {
        visitUnitMap.put(clazz instanceof Class?((Class)clazz).getName():clazz.toString(), visitUnit);
        return visitUnitMap;
    }

    public static VisitUnit get(Object clazz) {
        return visitUnitMap.get(clazz instanceof Class?((Class)clazz).getName():clazz.toString());
    }
    public boolean pushDataWithKey(String key) {
        Map d = new HashMap();
        if (getDataUnit().getRepo().containsKey(key)) {
            Object val = getDataUnit().getRepo().get(key);
            if (val == null || val.toString().equals("") || val.toString().equals("null")) {
                return false;
            } else
                d.put(key, getDataUnit().getRepo().get(key));
        } else {
            return false;
        }
        long time = System.currentTimeMillis();
        putFillsignarea(time + "", d.keySet());
        putDisplayKeyValue(d);
        putValues(time + "");
        return true;
    }

    public void pushData() {
        Map d = getDataUnit().getRepo();
        pushData(d);
    }

    public void pushData(Map d) {
        long time = System.currentTimeMillis();
        putFillsignarea(time + "", d.keySet());
        putDisplayKeyValue(d);
        putValues(time + "");
    }
}
