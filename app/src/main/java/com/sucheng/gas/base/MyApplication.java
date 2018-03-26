package com.sucheng.gas.base;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sucheng.gas.bean.UserBean;
import com.sucheng.gas.exection.CrashHandler;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;
    //网络请求队列
    volatile private static RequestQueue mRequest;
    //activity集合
    private List<AppCompatActivity> appList;
    private static int count = 0;

    //内网或外网的标识
    public static int urlFlagCode = 0;


    CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //初始化Http的相关配置
        initHttpData();
        appList = new ArrayList<>();
        //初始化异常日志统计
        crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

    }

    private void initHttpData() {
        Logger.setDebug(true);
        Logger.setTag("日志Tag");
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                //设置全局连接超时时间
                .connectionTimeout(30 * 1000)
                //服务器响应时间
                .readTimeout(10 * 1000)
                //重试次数
                .retry(1)
                //底层网络配置
                .networkExecutor(new OkHttpNetworkExecutor()).build());
    }

    //RequestQueue单例
    public static RequestQueue getRequestQueue(){
        if(mRequest == null){
            synchronized (RequestQueue.class){
                if(mRequest == null){
                    count++;
                    mRequest = NoHttp.newRequestQueue(8);
                }
            }
        }
        Log.e("app","---count=="+count);
        return mRequest;
    }

    public static MyApplication getMyApplication(){

        return myApplication;
    }


    //添加所有Activity
    public void addActivity(AppCompatActivity activity){
        if(!appList.contains(activity)){
            appList.add(activity);
        }
    }

    //销毁所有Activity
    public void removeAllActivity(){
        for(AppCompatActivity app : appList){
            app.finish();
        }
    }

    public static int code(int code){
        return urlFlagCode = code;
    }
}
