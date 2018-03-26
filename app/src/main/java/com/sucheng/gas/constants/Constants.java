package com.sucheng.gas.constants;

import android.content.Context;
import android.util.Log;

import com.sucheng.gas.BuildConfig;
import com.sucheng.gas.R;
import com.sucheng.gas.base.EnvType;
import com.sucheng.gas.base.MyApplication;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Constants {

    //道滘燃气地址
    public static final String DAOJIAO_GAS_BASEURL = "http://djzz.suchkj.com/";  //http://djzz.suchkj.com/
    //谢岗恒源燃气地址
    public static final String HENGYUAN_GAS_BASEURL = "http://xghy.suchkj.com/";   //http://xghy.suchkj.com/
    //沙田华洋燃气地址
    public static final String HUAYANG_GAS_BASEURL = "http://sthy.suchkj.com/";    //http://sthy.suchkj.com/
    //常平常态
    public static final String CPCT_GAS_BASEURL = "http://cpct.suchkj.com/";   //http://cpct.suchkj.com/
    //中堂中液
    public static final String ZHONGTANG_ZY_GAS_BASEURL = "http://ztzy.suchkj.com/";   //http://ztzy.suchkj.com/



    //捷宝手持机型号
    public static final String HT380K_BUILD1 = "M9PLUS";
    public static final String HT380K_BUILD2 = "HT380K";
    public static final String HT380K_BUILD3 = "IT380";
    public static final String HT380K_BUILD4 = "HT380D";

    public static final String[] ht380kBuild = new String[]{HT380K_BUILD1,HT380K_BUILD2,HT380K_BUILD3,HT380K_BUILD4};

    //MX505防爆机型号
    public static final String MX505_BUILD = "scx35_sp7730eccuccspecBplus_UUI";


    //是否读卡标识
     static boolean isReadCard = false;

    /**
     * 地址
     * 道滘株洲 http://djzz.suchkj.com/
     */
    //public static final String BASURL = "http://djzz.suchkj.com/";
    public static String BASURL = getBASEURL();

    public static final String BASURL1 = "http://192.168.3.101:8080/new-energymanage/";


    public static String getBASEURL(){
        int envType = BuildConfig.ENV_TYPE;
        Log.e("常量;","--------envType="+envType);
        if(envType == EnvType.DAOJIAO_GAS){
           return BASURL = DAOJIAO_GAS_BASEURL;
        }else if(envType == EnvType.HENGYUAN_GAS) {
            return BASURL = HENGYUAN_GAS_BASEURL;
        }else if(envType == EnvType.HUAYANG_GAS){
            return BASURL = HUAYANG_GAS_BASEURL;
        }else if(envType == EnvType.ZTZY_GAS){
            return BASURL = ZHONGTANG_ZY_GAS_BASEURL;
        }else if(envType == EnvType.CPCT_GAS){
            return BASURL = CPCT_GAS_BASEURL;
        }
        else{
            return null;
        }
    }

    //判断是否需要读卡
    public static boolean isReadClientCard(){
        int envType = BuildConfig.ENV_TYPE;
        switch (envType){
            case EnvType.DAOJIAO_GAS:
                isReadCard = false;
                break;
            case EnvType.HENGYUAN_GAS:
                isReadCard = false;
                break;
        }
        return  isReadCard;

    }

    //初始化重气瓶钢印码补码
    public static String initHeavyBotCode(){
        String heavybotCode = "";
        int envType = BuildConfig.ENV_TYPE;
        switch (envType){
            case EnvType.DAOJIAO_GAS:
                heavybotCode = "SJH";  //道滘SJH
                break;
            case EnvType.HENGYUAN_GAS:
                heavybotCode = "SML";  //恒源
                break;
            case EnvType.HUAYANG_GAS:   //华洋燃气
                heavybotCode = "SHR";
                break;
            case EnvType.CPCT_GAS:  //常平常态
                heavybotCode = "SAA";
                break;
            case EnvType.ZTZY_GAS:      //中堂中液
                heavybotCode = "SGF";
                break;
        }
        return heavybotCode;
    }

    //登录页显示名称
    public static String showAppName(Context context){
        String appName = "";
        int envType = BuildConfig.ENV_TYPE;
        switch (envType){
            case EnvType.DAOJIAO_GAS:
                appName = context.getResources().getString(R.string.daojiao_name);  //道滘SJH
                break;
            case EnvType.HENGYUAN_GAS:
                appName = context.getResources().getString(R.string.hengyuang_gas_name);  //恒源
                break;
            case EnvType.HUAYANG_GAS:
                appName = context.getResources().getString(R.string.huayang_gas_name); //华洋燃气
                break;
            case EnvType.CPCT_GAS:     //常平常态
                appName = context.getResources().getString(R.string.cpct_gas_name);    //常平常态
                break;
                case EnvType.ZTZY_GAS:
                 appName = context.getResources().getString(R.string.ztzy_gas_name);    //中堂中液
                  break;
        }
        return appName;
    }



    //内网和外网地址切换
    public static String getAbsoluteUrl(){
        String barUrl = null;
        if(MyApplication.urlFlagCode == 0){ //外网
            barUrl = BASURL;
        }else if(MyApplication.urlFlagCode == 1){   //内网
            barUrl = BASURL1;
        }else{
            return  null;
        }
        return barUrl;
    }

    /**登录模块**/
    public static final String LOGIN_MOBILE = "mobile/login/";

    /**用户模块**/
    public static final String USER_MOBILE = "mobile/user/";

    /**客户模块**/
    public static final String CLIENT_MOBILE = "mobile/client/";

    /**气瓶信息模块**/
    public static final String BOTMSG_MOBILE = "mobile/airBottleInfo/";

    /**版本信息模块**/
    public static final String APP_VERSIONMSG = "mobile/appVersionInfo/";


    /**仓库模块**/
    public static String WEARHOUSE_MOBILE = "mobile/warehouse/";

    /**送气工模块**/
    public static String DELIVER_MOBILE = "mobile/deliveryMan/";

    /**门店模块**/
    public static String STORY_MOBILE = "mobile/store/";

    /**日志记录模块**/
    public static String EXECTIONLOG_MOBILE = "mobile/log/";
}
