package com.sucheng.gas.utils.http.view;


import com.sucheng.gas.R;
import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.utils.VoiceUtils;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RequestPresent {

    private RequestModel requestModel;
    private RequestView requestView;

    public RequestPresent() {
        requestModel = new RequestModel();
    }

    //JSONObject
    public void getPresentRequestJSONObject(RequestQueue requestQueue, int what, String url, Map<String,Object> params, final String botCode){
        if(requestView != null){
            requestModel.getModelRequestJSONObject(requestQueue,what, url, params, new OnResponseListener<JSONObject>() {

                @Override
                public void onStart(int what) {
                    if(requestView != null){
                        requestView.showLoadDialog(what);
                    }
                }

                @Override
                public void onSucceed(int what, Response<JSONObject> response) {
                    Logger.e("----------present=success="+response.get().toString());
                    if(requestView != null && response != null){
                        requestView.closeLoadDialog(what);
                        requestView.requestSuccessData(what,response,botCode);
                    }
                }

                @Override
                public void onFailed(int what, Response<JSONObject> response) {
                    Logger.e("----------present=failed="+response.responseCode()+response.getException().toString());
                    if(requestView != null && response != null && response.responseCode() == 200){
                        requestView.closeLoadDialog(what);
                        requestView.requestFailedData(what,response);
                    }else{
                        VoiceUtils.showToastVoice(MyApplication.getMyApplication().getApplicationContext(), R.raw.warning,"错误信息:"+response.responseCode()+response.getException().toString());
                    }
                }

                @Override
                public void onFinish(int what) {
                    if(requestView != null){
                        requestView.closeLoadDialog(what);
                    }
                }
            });
        }
    }

    //请求String类型
    public void getPresentRequestString(RequestQueue requestQueue,int what, String url, Map<String,Object> params,final String botcode){
        if(requestView != null){
            requestModel.getModelRequestString(requestQueue,what, url, params, new OnResponseListener<String>() {
                @Override
                public void onStart(int what) {
                    if(requestView != null){
                        requestView.showLoadDialog(what);
                    }
                }

                @Override
                public void onSucceed(int what, Response<String> response) {
                    if(requestView != null){
                        requestView.closeLoadDialog(what);
                        requestView.requestSuccessData(what,response,botcode);
                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    if(requestView != null){
                        requestView.closeLoadDialog(what);
                        requestView.requestFailedData(what,response);
                    }
                }

                @Override
                public void onFinish(int what) {
                    if(requestView != null){
                        requestView.closeLoadDialog(what);
                    }
                }
            });
        }
    }
    //绑定
    public void attach(RequestView requestView) {
        this.requestView = requestView;
    }

    //解除绑定
    public void detach() {
        if (requestView != null) {
            requestView = null;
        }
    }

    //取消网络请求
    public void interruptHttp(int flag) {
        requestModel.cancleHttpPost(flag);
    }
}
