package com.sucheng.gas.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sucheng.gas.bean.UserBean;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.utils.SharedPreferenceUtils;
import com.sucheng.gas.utils.http.HttpListener;
import com.sucheng.gas.utils.http.HttpResponseListener;
import com.sucheng.gas.view.PromptDialog;
import com.sucheng.gas.view.WaitDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class BaseActivity extends AppCompatActivity {

    private  static  Snackbar snackbar;
    private static Toast mToast;

    private MyApplication myApplication;
    private BaseActivity baseActivity;
    private WaitDialog mWatiDialog;
    private RequestQueue requestQueue;

    private PromptDialog promptDialog;
    /**
     * 用来标记取消。标签
     */
    private Object object = new Object();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myApplication == null){
            myApplication = (MyApplication) getApplication();
        }
        baseActivity = this;
        requestQueue = NoHttp.newRequestQueue(10);
        addActivity();
    }

    //将所有Activity都添加至集合中
    public void addActivity() {
        myApplication.addActivity(baseActivity);
    }

    //销毁所有Activity
    public void removeAllActivity(){
        myApplication.removeAllActivity();
    }

    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, HttpListener<T> callback,
                            boolean canCancel, boolean isLoading) {
        request.setCancelSign(object);
        requestQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
    }
    //取消所有请求
    protected void cancelAll() {
        requestQueue.cancelAll();
    }

    //根据标签取消请求
    protected void cancelBySign(Object object) {
        requestQueue.cancelBySign(object);
    }


    //通用的跳转
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    //带参数跳转
    public void startActivity(Class<?> cls ,String[] keys,String[] values){
        Intent intent = new Intent(this,cls);
        int size = keys.length;
        for(int i = 0;i<size;i++){
            intent.putExtra(keys[i],values[i]);
        }
        startActivity(intent);
    }

    public void startIntActivity(Class<?> cls,String keys,int value){
        Intent intent = new Intent(this,cls);
        intent.putExtra(keys,value);
        startActivity(intent);
    }

    //保存用户信息
    public void setPutUserInfo(UserBean userInfo){
        String userString = new Gson().toJson(userInfo);
        SharedPreferenceUtils.put(this,"userInfo",userString);
    }

    //获取用户信息
    public UserBean getUserInfo(){
        UserBean userBean = null;
        String userStringData = (String) SharedPreferenceUtils.get(BaseActivity.this,"userInfo","");
        userBean = new Gson().fromJson(userStringData,UserBean.class);
        return userBean;
    }

    //权限集合
    public List<Integer> getPermissList(){
        List<Integer> integerList = new ArrayList<>();
        if(getUserInfo() != null){
            List<UserBean.DataBean.MobileModulesBean> mobileModulesBeanList = getUserInfo().getData().getMobileModules();
            for(int i = 0;i<mobileModulesBeanList.size();i++){
                integerList.add(mobileModulesBeanList.get(i).getModule_id());
            }
        }
        return integerList;
    }


    //Snackbar显示
    public static void showSnackbarMsg(View view,String msg){
        if(snackbar == null){
            snackbar = Snackbar.make(view,msg,Snackbar.LENGTH_SHORT);
        }else{
             Snackbar.make(view,msg,Snackbar.LENGTH_SHORT);
        }
        snackbar.show();
    }

    //Toast显示， 显示短时间
    public static void showToast(Context mContext, String msg){
        if(mToast == null){
            mToast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }

    //显示设置的时间
    public static void showToast(Context mContext,String msg,int duration){
        if(mToast == null){
            mToast = Toast.makeText(mContext,msg,duration);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
        MyApplication.getRequestQueue().cancelBySign(object);
        // 因为回调函数持有了activity，所以退出activity时请停止队列。
        MyApplication.getRequestQueue().stop();
    }

    public void showWatiDialog(){
        if(mWatiDialog == null){
            mWatiDialog = new WaitDialog(BaseActivity.this);
            mWatiDialog.show();
        }else{
            mWatiDialog.show();
        }
    }

    public void closeWatiDialog(){
        if(mWatiDialog != null && !BaseActivity.this.isFinishing()){
            mWatiDialog.dismiss();
        }
    }

    //提示框
    public void setPromptDialogListener(String title, String msg, final OnPromptDialogListener alertListener){
        if(promptDialog == null){
            promptDialog = new PromptDialog(BaseActivity.this);
        }
        promptDialog.show();
        promptDialog.setTitle(title);
        promptDialog.setContent(msg);
        promptDialog.setleftText("否");
        promptDialog.setrightText("是");
        promptDialog.setListener(new OnPromptDialogListener() {
            @Override
            public void leftClick(int code) {
                promptDialog.dismiss();
                alertListener.leftClick(code);
            }

            @Override
            public void rightClick(int code) {
                promptDialog.dismiss();
                alertListener.rightClick(code);
            }
        });
    }




    /**
     * 输入框获取焦点时自动弹出软键盘，
     * 点击屏幕的其它任何位置，软件盘消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText || v instanceof TextInputEditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
