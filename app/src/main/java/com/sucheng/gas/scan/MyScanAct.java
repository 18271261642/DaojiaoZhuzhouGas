package com.sucheng.gas.scan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.http.HttpListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class MyScanAct extends BaseActivity implements MyScanV4Interface{

    private List<AddBottleTypeBean> serverBottleType;

    /**
     * 通过这个方法得到List<AddBottleTypeBean>列表
     */
    @Override
    public void onBottleType(List<AddBottleTypeBean> listBottleType) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBotTypeData();   //获取气瓶类型
    }

    private void getBotTypeData() {
        String url = Constants.getAbsoluteUrl()+UrlCode.BOTMSG_BOTTRAJECORY.getCheck();
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        request(1, stringRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.responseCode() == 200){
                    if(Utils.getJSONCode(response.get()) == 200){
                        onBottleType(Utils.paraseServerBottleType(response.get()));
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }
        }, true,false);
    }
}
