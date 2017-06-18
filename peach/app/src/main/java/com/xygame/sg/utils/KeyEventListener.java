package com.xygame.sg.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by tony on 2016/1/24.
 */
public class KeyEventListener implements OnKeyListener {

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){

            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if(imm.isActive()){

                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );

            }

            return true;

        }
        return false;
    }
}
