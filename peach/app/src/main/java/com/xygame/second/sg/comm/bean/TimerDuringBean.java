package com.xygame.second.sg.comm.bean;

import com.xygame.second.sg.jinpai.UsedTimeBean;
import com.xygame.second.sg.jinpai.bean.ScheduleTimeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/8/10.
 */
public class TimerDuringBean implements Serializable {
    private List<FreeTimeBean> timers;

    private FreeTimeBean timersOfDate;

    private List<ServiceTimeDateBean> dateDatas;

    private List<ScheduleTimeBean> scheduleTimeBeans;

    private List<UsedTimeBean> usedTimeBeans;

    public List<UsedTimeBean> getUsedTimeBeans() {
        return usedTimeBeans;
    }

    public void setUsedTimeBeans(List<UsedTimeBean> usedTimeBeans) {
        this.usedTimeBeans = usedTimeBeans;
    }

    public List<ServiceTimeDateBean> getDateDatas() {
        return dateDatas;
    }

    public void setDateDatas(List<ServiceTimeDateBean> dateDatas) {
        this.dateDatas = dateDatas;
    }

    public List<FreeTimeBean> getTimers() {
        return timers;
    }

    public void setTimers(List<FreeTimeBean> timers) {
        this.timers = timers;
    }

    public List<ScheduleTimeBean> getScheduleTimeBeans() {
        return scheduleTimeBeans;
    }

    public void setScheduleTimeBeans(List<ScheduleTimeBean> scheduleTimeBeans) {
        this.scheduleTimeBeans = scheduleTimeBeans;
    }

    public FreeTimeBean getTimersOfDate() {
        return timersOfDate;
    }

    public void setTimersOfDate(FreeTimeBean timersOfDate) {
        this.timersOfDate = timersOfDate;
    }
}
