package com.sucheng.gas.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.sucheng.gas.R;


/**
 * Created by Administrator on 2017/12/26.
 */

public class WaitDialog extends ProgressDialog {
    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage(context.getText(R.string.wait_dialog_title));
    }
}
