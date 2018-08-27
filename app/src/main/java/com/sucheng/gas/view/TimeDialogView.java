package com.sucheng.gas.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.sucheng.gas.R;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/1/24.
 */

/**
 * 日期选择弹窗
 */
public class TimeDialogView extends Dialog implements View.OnClickListener{

    private static final String TAG = "TimeDialogView";
    private DatePicker datePicker;
    private Button yesbtn,canclebtn;
    private Calendar calendar;


    private OnTimeDialogClickListener onTimeDialogListener;

    public void setOnTimeDialogListener(OnTimeDialogClickListener onTimeDialogListener) {
        this.onTimeDialogListener = onTimeDialogListener;
    }

    public TimeDialogView(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_dialog_view);

        initViews();

        initData();

    }

    private void initData() {
        calendar = Calendar.getInstance();
        //判断系统版本
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    }

    private void initViews() {
        datePicker = (DatePicker) findViewById(R.id.dateDialogTimpickerView);
        yesbtn = (Button) findViewById(R.id.timeDialogSureBtn);
        canclebtn = (Button) findViewById(R.id.timeDialogCancleBtn);
        yesbtn.setOnClickListener(this);
        canclebtn.setOnClickListener(this);

    }

    //设置+4年日期
    public String get4DateTime(){
       // int month = datePicker.getMonth()+5;
        return (datePicker.getYear()+4)+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timeDialogCancleBtn:  //取消
                if(onTimeDialogListener != null){
                    onTimeDialogListener.getCancleDialogTime();
                    cancel();
                }
                break;
            case R.id.timeDialogSureBtn:    //确定
                datePicker.clearFocus();
                if(onTimeDialogListener != null){
                    String timeData = datePicker.getYear()+"-" + (datePicker.getMonth() +1)+ "-" + datePicker.getDayOfMonth();
                    Log.e("日期选择","---timedata="+timeData);
                    onTimeDialogListener.getYesDialogTime( datePicker.getYear(),(datePicker.getMonth() +1),datePicker.getDayOfMonth());
                }
                break;
        }
    }

    public interface OnTimeDialogClickListener{
        void getYesDialogTime(int year,int month,int daty);
        void getCancleDialogTime();
    }
}
