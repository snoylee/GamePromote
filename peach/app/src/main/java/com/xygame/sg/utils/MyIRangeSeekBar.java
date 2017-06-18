package com.xygame.sg.utils;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/17.
 */
public class MyIRangeSeekBar extends MyRangeSeekBar {

    public MyIRangeSeekBar(Integer absoluteMinValue, Integer absoluteMaxValue, Context context, AttributeSet attr) throws IllegalArgumentException {
        super(absoluteMinValue, absoluteMaxValue, context, attr);
    }

    static final String resauto = "http://schemas.android.com/apk/res-auto";
    static final String resbase = "http://schemas.android.com/apk/base";

    public MyIRangeSeekBar(Context context, AttributeSet attrs) {
        this(Integer.parseInt(attrs.getAttributeValue(resauto, "min")), Integer.parseInt(attrs.getAttributeValue(resauto, "max")), context, attrs);
        String maxStr = attrs.getAttributeValue(resauto, "max");

        String text = attrs.getAttributeValue(resauto, "text");

        if(text.equals("+￥")){
            setMinText("￥");
        } else {
            setMinText(text);
        }
        setMaxText(text);
        String sequence = attrs.getAttributeValue(resauto, "sequence");
        if (!(sequence == null || sequence.equals(""))) {
            List<String> seq = Arrays.asList(sequence.split(","));
            init(1, seq.size());
            setSequence(seq);
        }
        begin = attrs.getAttributeValue(resbase, "begin");
        begin = begin.substring(2, begin.length() - 1);
        end = attrs.getAttributeValue(resbase, "end");
        end = end.substring(2, end.length() - 1);

    }

    String begin;
    String end;

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    VisitUnit visitUnit;

    public MyIRangeSeekBar setVisitUnit(VisitUnit visitUnit) {
        this.visitUnit = visitUnit;
        return this;
    }

    public void reset() {
        super.reset();
        visitUnit.getDataUnit().getRepo().put(begin, "");
        visitUnit.getDataUnit().getRepo().put(end, "");

    }
}
