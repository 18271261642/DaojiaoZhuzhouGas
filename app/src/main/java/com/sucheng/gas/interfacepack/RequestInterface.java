package com.sucheng.gas.interfacepack;

import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2018/1/25.
 */

public interface RequestInterface<T> extends OnResponseListener<T> {

    void onRequestStart(int what);

    void onRequestSuccess(int what, Response<T> response,String bot);

    void onRequestFailed(int what,Response<T> response);

    void onFinish(int what);
}
