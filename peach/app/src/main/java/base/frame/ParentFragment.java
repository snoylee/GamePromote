package base.frame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.Map;

import base.Actions;
import base.RRes;
import base.ViewBinder;
import base.action.CenterRepo;

/**
 * Created by minhua on 2015/10/25.
 */
public class ParentFragment extends android.support.v4.app.Fragment implements Handler.Callback{
    public VisitUnit unit;
    private boolean injected;

    public void setUnit(VisitUnit unit) {
        this.unit = unit;
    }

    public VisitUnit getVisit() {
        return unit;
    }

    public ParentFragment() {
    }

    public String strlayout = "";

    public void setStrlayout(String strlayout) {
        this.strlayout = strlayout;
    }

    public String getStrlayout() {
        return strlayout;
    }

//    public ParentFragment(String strlayout) {
//
//        this.strlayout = strlayout;
//        setDataUnit();
//    }

    public void setDataUnit() {
    }

    public VisitUnit getVisitUnit() {
        return unit;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (unit == null) {
            if (VisitUnit.get(this.getClass()) == null || VisitUnit.get(this) == null) {
                unit = new VisitUnit(true);
                VisitUnit.putVisitUnitMap(this.getClass() == ParentFragment.class ? this : this.getClass(), unit);
            } else {
                unit = VisitUnit.get(this.getClass() == ParentFragment.class ? this : this.getClass());
            }
            create(unit);
        }

        int layout = RRes.get(strlayout).getAndroidValue();
        return new ViewBinder(container.getContext(), getVisitUnit().setDisplay(this)).inflate(layout, null);
    }

    public void create(VisitUnit unit) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        updateData();

    }

    public void init() {
        if (init != null) {
            Actions.parseActions(init, getActivity(), this, unit).run();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        if (unit == null) {
            return;
        }

        Map d = unit.getDataUnit().getRepo();
        getVisit().pushData(d);
    }

    String init;

    public void setInit(String init) {
        this.init = init;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    public void setSubmitCenter(boolean submitCenter) {
        this.getVisitUnit().setTarget(submitCenter);
    }

    public void setInjected(boolean injected) {
        this.injected = injected;
    }


    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
//	protected abstract void lazyLoad();
    protected void lazyLoad(){

    }



    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ConstTaskTag.CONST_TAG_PUBLIC: {
                ResponseBean data = (ResponseBean) msg.obj;
                if (null == data) {
                    responseFaith();
                    Toast.makeText(getActivity(), "网络请求失败！", Toast.LENGTH_SHORT).show();
                } else {
                    getResponseBean(data);
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    protected void getResponseBean(ResponseBean data) {
    }

    protected void responseFaith() {
    }
}
