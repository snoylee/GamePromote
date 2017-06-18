package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/17.
 */
public class PriceStepBean implements Serializable {
    private int rangeMax,rangeMin,step;

    public int getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }

    public int getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(int rangeMin) {
        this.rangeMin = rangeMin;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
