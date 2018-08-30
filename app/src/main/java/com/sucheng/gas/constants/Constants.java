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
    private static final String DAOJIAO_GAS_BASEURL = "http://djzz.suchkj.com/";  //http://djzz.suchkj.com/
    //谢岗恒源燃气地址
    private static final String HENGYUAN_GAS_BASEURL = "http://xghy.suchkj.com/";   //http://xghy.suchkj.com/
    //沙田华洋燃气地址
    private static final String HUAYANG_GAS_BASEURL = "http://sthy.suchkj.com/";    //http://sthy.suchkj.com/
    //常平常态
    private static final String CPCT_GAS_BASEURL = "http://cpct.suchkj.com:8100/";   //http://cpct.suchkj.com/
    //中堂中液
    private static final String ZHONGTANG_ZY_GAS_BASEURL = "http://ztzy.suchkj.com/";   //http://ztzy.suchkj.com/
    //厚街厚龙
    private static final String HOUJIE_HL_GAS_BASEURL = "http://hjhl.suchkj.com/"; //厚街厚龙
    //黄江宝山
    private static final String HJ_BAOSHAN_GAS_BASEURL = "http://hjbs.suchkj.com/";    //黄江宝山
    //樟木头新世纪
    private static final String ZMTXSJ_GAS_BASEURL = "http://zmtxsj.suchkj.com/";    //樟木头新世纪
    //横沥大山燃气
    private static final String HENGLIDS_GAS_BASEURL = "http://hlds.suchkj.com/";      //横沥大山
    //黄江长隆
    private static final String HJCL_GAS_BASEURL = "http://hjcl.suchkj.com/";       //黄江长隆

    //梅州大丰气体
    private static final String MEIZHOU_DF_GAS_BASEURL = "http://mzdf.zxlcloud.com/";   //梅州大丰
    //梅州大埔县高陂燃气
    private static final String MEIZHOU_DF_GAOPO_GAS_BASEURL = "http://mzdbgb.zxlcloud.com/";


    //捷宝手持机型号
    private static final String HT380K_BUILD1 = "M9PLUS";
    private static final String HT380K_BUILD2 = "HT380K";
    private static final String HT380K_BUILD3 = "IT380";
    private static final String HT380K_BUILD4 = "HT380D";

    public static final String[] ht380kBuild = new String[]{HT380K_BUILD1,HT380K_BUILD2,HT380K_BUILD3,HT380K_BUILD4};

    //MX505防爆机型号
    public static final String MX505_BUILD = "scx35_sp7730eccuccspecBplus_UUI";

    //T50防爆机
    public static final String T50_BUILD = "T50";


    //是否读卡标识
     static boolean isReadCard = false;

    /**
     * 地址
     * 道滘株洲 http://djzz.suchkj.com/
     */
    //public static final String BASURL = "http://djzz.suchkj.com/";
    private static String BASURL = getBASEURL();

    private static final String BASURL1 = "http://192.168.3.101:8080/new-energymanage/";
    private static final String BASEURL2 = "http://test.zxlcloud.com/";


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
        }else if(envType == EnvType.HOUJIE_HL_GAS){
            return BASURL = HOUJIE_HL_GAS_BASEURL;
        }else if(envType == EnvType.HJ_BAOSHAN_GAS){
            return BASURL = HJ_BAOSHAN_GAS_BASEURL;
        }else if(envType == EnvType.ZMTXSJ_GAS){
            return BASURL = ZMTXSJ_GAS_BASEURL;
        }else if(envType == EnvType.HLDS_GAS){
            return BASURL =HENGLIDS_GAS_BASEURL;
        }else if(envType == EnvType.HJCL_GAS){
            return BASURL = HJCL_GAS_BASEURL;
        }else if(envType == EnvType.MeiZhouDF_GAS){
            return MEIZHOU_DF_GAS_BASEURL;
        }else if(envType == EnvType.MeiZhouDPGP_GAS){
            return MEIZHOU_DF_GAOPO_GAS_BASEURL;
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

    //初始化中气瓶钢印码补码
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
            case EnvType.CPCT_GAS:  //常平常泰
                heavybotCode = "SAA";
                break;
            case EnvType.ZTZY_GAS:      //中堂中液
                heavybotCode = "SGF";
                break;
            case EnvType.HOUJIE_HL_GAS: //厚街厚龙
                heavybotCode = "SFE";
                break;
            case EnvType.HJ_BAOSHAN_GAS:    //黄江宝山
                heavybotCode = "SXY";
                break;
            case EnvType.ZMTXSJ_GAS:
                heavybotCode = "";          //樟木头新世纪
                break;
            case EnvType.HLDS_GAS:      //横沥大山
                heavybotCode = "SDS";
                break;
            case EnvType.HJCL_GAS:      //黄江长隆
                heavybotCode = "SYW";
                break;
             case EnvType.MeiZhouDF_GAS: //梅州大丰
                heavybotCode = "";
                break;
             case EnvType.MeiZhouDPGP_GAS:  //梅州大埔县高陂燃气
                heavybotCode = "";
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
            case EnvType.CPCT_GAS:     //常平常泰
                appName = context.getResources().getString(R.string.cpct_gas_name);    //常平常态
                break;
             case EnvType.ZTZY_GAS:
                appName = context.getResources().getString(R.string.ztzy_gas_name);    //中堂中液
                break;
            case EnvType.HOUJIE_HL_GAS: //厚街厚龙
                appName = context.getResources().getString(R.string.houjie_hl_gas_name);
                break;
            case EnvType.HJ_BAOSHAN_GAS:    //黄江宝山
                appName = context.getResources().getString(R.string.hj_baoshan_gas_name);
                break;
            case EnvType.ZMTXSJ_GAS:    //樟木头新世纪
                appName = context.getResources().getString(R.string.zmtxsj_gas_name);
                break;
            case EnvType.HLDS_GAS:      //横沥大山
                appName = context.getResources().getString(R.string.hlds_gas_name);
                break;
            case EnvType.HJCL_GAS:  //黄江长隆
                appName = context.getResources().getString(R.string.hjcl_gas_name);
                break;
            case EnvType.MeiZhouDF_GAS: //梅州大丰
                appName = context.getResources().getString(R.string.meizhou_df_gas_name);
                break;
            case EnvType.MeiZhouDPGP_GAS:   //梅州大埔县高陂燃气
                appName = context.getResources().getString(R.string.meizhou_dp_gaopo_gas_name);
                break;
        }
        return appName;
    }

    //判断初始化是否必须拍照
    //true-必须拍照；false-不必须拍照
    public static boolean initIsTakePick(){
        boolean isPick = true;
        int envType = BuildConfig.ENV_TYPE;
        switch (envType){
            case EnvType.DAOJIAO_GAS:
                isPick = true;  //道滘
                break;
            case EnvType.HENGYUAN_GAS:
                isPick = true;  //恒源
                break;
            case EnvType.HUAYANG_GAS:
                isPick = false;  //华洋燃气 ，不必必须拍照
                break;
            case EnvType.CPCT_GAS:     //常平常泰
                isPick = true;    //常平常态
                break;
            case EnvType.ZTZY_GAS:
                isPick = true;     //中堂中液
                break;
            case EnvType.HOUJIE_HL_GAS:
                isPick = true;      //厚街厚龙
                break;
            case EnvType.HJ_BAOSHAN_GAS: //黄江宝山燃气
                isPick = true;
                break;
            case EnvType.ZMTXSJ_GAS:    //樟木头新世纪
                isPick = true;
                break;
            case EnvType.HLDS_GAS:      //横沥大山
                isPick = true;
                break;
            case EnvType.HJCL_GAS:  //黄江长隆
                isPick = true;
                break;
            case EnvType.MeiZhouDF_GAS: //梅州大丰
                isPick = true;
                break;
            case EnvType.MeiZhouDPGP_GAS:   //梅州大埔县高陂燃气
                isPick = true;
                break;
        }
        return isPick;
    }



    //内网和外网地址切换
    public static String getAbsoluteUrl(){
        String barUrl = null;
        if(MyApplication.urlFlagCode == 0){ //外网
            barUrl = BASURL;
        }else if(MyApplication.urlFlagCode == 1){   //内网
            barUrl = BASURL1;
        }else if(MyApplication.urlFlagCode == 2){   //演示服务器
            barUrl = BASEURL2;
        }
        else{
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

    /**仓库送检模块**/
    public static String WEARHOUSE_CHECK_BOT_MOBILE = "mobile/inspection/";

    /**入户安检模块**/
    public static String STORY_CHECK_ORDER = "mobile/familyCheck/";
}
