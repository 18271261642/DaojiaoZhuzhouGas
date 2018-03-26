package com.sucheng.gas.utils.http.view;


import android.util.Log;

import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.interfacepack.RequestInterface;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RequestModel {

    /**
     *
     * @param what  int类型的标识
     * @param url   请求地址
     * @param params    参数
     * @param onResponseListener    回调
     */
    public void getModelRequestJSONObject(RequestQueue requestQueue,int what, String url, Map<String,Object> params, OnResponseListener<JSONObject> onResponseListener){
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(Constants.getAbsoluteUrl()+url, RequestMethod.POST);
        //遍历map，将参数添加至requst中
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String,Object> map : params.entrySet()){
                jsonObjectRequest.add(map.getKey(),map.getValue()+"");
            }
            Logger.e("-----最终提交有参数="+Constants.getAbsoluteUrl()+url+"--="+params.toString());
        }
        requestQueue.add(what,jsonObjectRequest,onResponseListener);
    }

    /**
     * 请求StringRequest
     * @param what
     * @param url
     * @param params
     * @param onResponseListener
     */
    public void getModelRequestString(RequestQueue requestQueue,int what, String url, Map<String,Object> params, OnResponseListener<String> onResponseListener){
        Request<String> stringRequest = NoHttp.createStringRequest(Constants.getAbsoluteUrl()+url,RequestMethod.POST);
        //遍历map，将参数添加至requst中
        Log.e("rrrr","-----params==="+params.toString());
        if(!params.isEmpty()){
            for(Map.Entry<String,Object> map : params.entrySet()){
                stringRequest.add(map.getKey(),map.getValue()+"");
                Log.e("rrr","-----mm="+map.getKey()+"-="+map.getValue());
            }
        }
        requestQueue.add(what,stringRequest,onResponseListener);
    }

    /**
     * 取消网络请求
     * @param what
     */
    public void cancleHttpPost(int what){
        MyApplication.getRequestQueue().cancelBySign(what);
    }

}
