package com.xygame.sg.activity.commen;

import com.xygame.sg.R;
import com.xygame.sg.utils.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TwoButtonDialog extends Dialog implements View.OnClickListener {
    private TextView tipText, cancelView;
    private TextView comfirmView;
    private ButtonTwoListener listener;
    private String textTip;
    private String comfirmViewText="";
    private String cancelViewText="";

    public TwoButtonDialog(Context context, int attrsr) {
        super(context, attrsr);
    }

    public TwoButtonDialog(Context context, String textTip, int attrs, ButtonTwoListener listener) {
        super(context, attrs);
        this.listener = listener;
        this.textTip = textTip;
    }

    public TwoButtonDialog(Context context, String textTip, String comfirmViewText,String cancelViewText,int attrs, ButtonTwoListener listener) {
        super(context, attrs);
        this.listener = listener;
        this.textTip = textTip;
        this.comfirmViewText = comfirmViewText;
        this.cancelViewText = cancelViewText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_two_button_dialog_layout);
        initViews();
        initListener();
        initDatas();
    }


    private void initViews() {
        tipText = (TextView) findViewById(R.id.tipText);
        cancelView = (TextView) findViewById(R.id.cancelView);
        comfirmView =(TextView)  findViewById(R.id.comfirmView);
    }


    private void initListener() {
        comfirmView.setOnClickListener(this);
        cancelView.setOnClickListener(this);
    }


    private void initDatas() {
        tipText.setText(textTip);
        if (!StringUtils.isEmpty(comfirmViewText)){
            comfirmView.setText(comfirmViewText);
        }

        if (!StringUtils.isEmpty(cancelViewText)){
            cancelView.setText(cancelViewText);
        }
        setCanceledOnTouchOutside(false);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comfirmView) {
            listener.confrimListener();
            dismiss();
        } else if (v.getId() == R.id.cancelView) {
            listener.cancelListener();
            dismiss();
        }
    }
}
