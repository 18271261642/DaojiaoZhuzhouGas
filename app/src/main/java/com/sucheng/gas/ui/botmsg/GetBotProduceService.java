package com.sucheng.gas.ui.botmsg;

/**
 * Created by Administrator on 2018/1/24.
 */

import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.interfacepack.DataInfoInterface;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONObject;
import java.util.Map;

/**
 * 获取气瓶的生产厂家
 */
public class GetBotProduceService {

    private DataInfoInterface dataInfoInterface;

    public void setDataInfoInterface(DataInfoInterface dataInfoInterface) {
        this.dataInfoInterface = dataInfoInterface;
    }

    //获取气瓶生成厂家
    public void getBotProductData(RequestQueue requestQueue, String url, Map<String,Object> maps){
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(Constants.getAbsoluteUrl()+url, RequestMethod.POST);
        if(!maps.isEmpty()){
            for(Map.Entry<String,Object> map : maps.entrySet()){
                jsonObjectRequest.add(map.getKey(),map.getValue()+"");
            }
        }
        requestQueue.add(1001, jsonObjectRequest, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(dataInfoInterface != null && response != null){
                    dataInfoInterface.successInfo(response.get());
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                if(dataInfoInterface != null && response != null){
                    dataInfoInterface.failedInfo(response.getException());
                }
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    public void cancleRequest(RequestQueue requestQueue){
        if(requestQueue != null){
            requestQueue.cancelBySign(1001);
        }
    }
}
