package com.sucheng.gas.utils.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import com.sucheng.gas.view.WaitDialog;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2017/12/26.
 */

public class HttpResponseListener<T> implements OnResponseListener<T>{

    private Context mActivity;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequests;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;


    /**
     *
     * @param mActivity Context
     * @param mRequest  请求
     * @param callback  请求回调
     * @param cancleable    进度条是否可以关闭
     * @param isShow    显示进度条了是否可以关闭 按返回键的取消显示
     */
    public HttpResponseListener(Context mActivity, Request<?> mRequest, HttpListener<T> callback, boolean cancleable, boolean isShow) {
        this.mActivity = mActivity;
        this.mRequests = mRequest;
        if(mActivity != null && isShow){
            mWaitDialog = new WaitDialog(mActivity);
            mWaitDialog.setCancelable(cancleable);
            mWaitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if(mWaitDialog != null && mWaitDialog.isShowing()){
                        mWaitDialog.dismiss();
                        mRequests.cancel();
                    }
                    return true;
                }
            });
        }
        this.callback = callback;
    }

    @Override
    public void onStart(int what) {
        //开始显示进度条
        if (mWaitDialog != null && !((Activity)mActivity).isFinishing() && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 成功的回调
     * @param what
     * @param response
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if(callback != null){
            callback.onSucceed(what,response);
        }
    }

    //失败的回调
    @Override
    public void onFailed(int what, Response<T> response) {
        if(callback != null){
            callback.onFailed(what,response);
        }
    }

    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }
}
