package com.sucheng.gas.utils.http.view;

import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2017/12/27.
 */

public interface RequestView<T> {

    //显示进度条
    void showLoadDialog(int what);

    //隐藏进度条
    void closeLoadDialog(int what);

    //请求成功数据回调
    void requestSuccessData(int what, Response<T> response,String bot);

    //请求失败数据回调
    void requestFailedData(int what, Response<T> response);
}
