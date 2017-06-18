package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.utils.StringUtils;

public class TwoButtonNumsDialog extends Dialog implements View.OnClickListener {
    private TextView tipText, cancelView,dialogTitle;
    private TextView comfirmView;
    private ButtonTwoTextListener listener;
    private String textTip;
    private String comfirmViewText="";
    private String cancelViewText="";
    private String dialogTitleStr="";
    private EditText inputText;

    public TwoButtonNumsDialog(Context context, int attrsr) {
        super(context, attrsr);
    }

    public TwoButtonNumsDialog(Context context, String dialogTitleStr, String textTip, int attrs, ButtonTwoTextListener listener) {
        super(context, attrs);
        this.listener = listener;
        this.textTip = textTip;
        this.dialogTitleStr=dialogTitleStr;
    }

    public TwoButtonNumsDialog(Context context, String dialogTitleStr, String textTip, String comfirmViewText, String cancelViewText, int attrs, ButtonTwoTextListener listener) {
        super(context, attrs);
        this.listener = listener;
        this.textTip = textTip;
        this.dialogTitleStr=dialogTitleStr;
        this.comfirmViewText = comfirmViewText;
        this.cancelViewText = cancelViewText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_button_nums_dialog_layout);
        initViews();
        initListener();
        initDatas();
    }


    private void initViews() {
        dialogTitle=(TextView)findViewById(R.id.dialogTitle);
        tipText = (TextView) findViewById(R.id.tipText);
        cancelView = (TextView) findViewById(R.id.cancelView);
        comfirmView =(TextView)  findViewById(R.id.comfirmView);
        inputText=(EditText)findViewById(R.id.inputText);
    }


    private void initListener() {
        comfirmView.setOnClickListener(this);
        cancelView.setOnClickListener(this);
    }


    private void initDatas() {
        dialogTitle.setText(dialogTitleStr);
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
            listener.confrimListener(inputText.getText().toString().trim());
        } else if (v.getId() == R.id.cancelView) {
            listener.cancelListener();
            dismiss();
        }
    }
}
