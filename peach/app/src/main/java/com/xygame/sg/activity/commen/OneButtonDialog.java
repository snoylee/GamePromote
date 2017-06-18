package com.xygame.sg.activity.commen;

import com.xygame.sg.R;
import com.xygame.sg.utils.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OneButtonDialog extends Dialog implements View.OnClickListener {
    private TextView tipText;
    private TextView comfirmView;
    private ButtonOneListener comfirmListener;
    private String textTip="";
    private String confirmViewText="";

    public OneButtonDialog(Context context, int attrsr) {
        super(context, attrsr);
    }

    public OneButtonDialog(Context context, String textTip, int attrs, ButtonOneListener comfirmListener) {
        super(context, attrs);
        this.comfirmListener = comfirmListener;
        this.textTip = textTip;
    }
    public OneButtonDialog(Context context, String textTip,String confirmViewText, int attrs, ButtonOneListener comfirmListener) {
        super(context, attrs);
        this.comfirmListener = comfirmListener;
        this.textTip = textTip;
        this.confirmViewText = confirmViewText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_one_button_dialog_layout);
        initViews();
        initListener();
        initDatas();
    }

    private void initViews() {
        tipText = (TextView) findViewById(R.id.tipText);
        comfirmView = (TextView)findViewById(R.id.comfirmView);
    }


    private void initListener() {
        comfirmView.setOnClickListener(this);
    }


    private void initDatas() {
        tipText.setText(textTip);
        if (!StringUtils.isEmpty(confirmViewText)){
            comfirmView.setText(confirmViewText);
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comfirmView) {
            comfirmListener.confrimListener(this);
            dismiss();
        }
    }
}
