package com.sucheng.gas.utils;

import android.content.Context;
import android.content.res.Resources;

import com.android.volley.ServerError;
import com.sucheng.gas.R;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2018/1/24.
 */

public class ErrorExectionUtils {

    public static String showErrorMsg(Exception exception, Context mContext){
        if (exception instanceof NetworkError) {// 网络不好
            return mContext.getString(R.string.net_nogood);
        } else if (exception instanceof TimeoutError) {// 请求超时
            return  mContext.getString(R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            return mContext.getString(R.string.error_timeout);
        } else if (exception instanceof URLError) {// URL是错的
            return mContext.getString(R.string.error_notfound);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            return mContext.getString(R.string.error_timeout);
        } else {
            return mContext.getString(R.string.error_message);
        }
    }

    //判断错误类型
    public static String getMessage(Object error,Context context){
        if(error instanceof ConnectTimeoutException){  //连接超时
            return context.getResources().getString(R.string.error_timeout);
        }else if(error instanceof ConnectException ){  	//客户端请求超时
            return context.getResources().getString(R.string.error_connetexection);
        }else if(error instanceof SocketTimeoutException){	//服务器响应超时,客户端已请求，服务器未响应
            return context.getResources().getString(R.string.error_sockettimeout);
        }else if(error instanceof JSONException){	//JSON解析异常
            return context.getResources().getString(R.string.error_jsonexection);
        }else if(error instanceof Resources.NotFoundException){	//404 地址为找到
            return context.getResources().getString(R.string.error_notfound);
        }else if(error.equals("Not Fount")){
            return context.getResources().getString(R.string.error_notfound);
        }else if(error instanceof ServerError){
            return context.getResources().getString(R.string.error_sockettimeout);
        }
        else{
            return context.getResources().getString(R.string.error_message);
        }

    }
}
