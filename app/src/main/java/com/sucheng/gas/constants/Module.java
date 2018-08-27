package com.sucheng.gas.constants;


/**
 * Created by Administrator on 2018/3/21.
 */

public class Module {

    public static final int user_login = 101;   //登录
    public static final int user_findemplayee_bycard = 102; //通过卡号获取员工信息
    public static final int user_findclient_bycard = 103 ;  //通过卡号获取用户信息
    public static final int user_getanonymity_msg = 1020;   //获取匿名用户信息



    public static final int init_bot = 10001;       //气瓶初始化
    public static final int bot_msg = 10002;        //气瓶信息
    public static final int bot_path = 1002;        //气瓶轨迹
    public static final int app_update = 10003;     //APP版本更新
    public static final int wearhouse_focusemptybot_inhouse = 10004;    //仓库强制空瓶入库
    public static final int wearhouse_heavybot_to_deve = 10005;         //仓库重瓶出库--出库给送气工
    public static final int wearhouse_reback_heavybot_fromdeve = 10006; //仓库从送气工退回重瓶
    public static final int deliver_emptybot_to_wearhouse = 10007;      //送气工空瓶回仓库
    public static final int wearhouse_filling_emptybot = 10008;         //仓库气瓶充装
    public static final int wearhouse_niming_focus_filling = 10019;     //仓库匿名强制充装
    public static final int wearhouse_niming_ziti_orders = 10020;       //仓库匿名自提下单
    public static final int wearhouse_ziti_order_list = 10021;          //仓库自提订单
    public static final int wearhouse_heavybot_to_client = 1021;        //仓库重瓶出库给用户
    public static final int wearhouse_ziti_return_order = 10022;        //仓库自提订单回单
    public static final int wearhouse_recycle_emptybot_from_client = 1022;  //仓库从用户回收空瓶


    /**
     * 送气工模块
     */
    public static final int deliver_my_nosend_orders = 10009;        //送气工-我的未派送单
    public static final int deliver_heavybot_to_client = 1009;      //送气工配送重瓶给用户
    public static final int deliver_my_noback_orders = 10010;       //送气工我的未回单
    public static final int deliver_recycle_empty_bot_fromclient = 1010;    //送气工从用户回收空瓶
    public static final int deliver_reback_heavy_bot_fromclient = 10023;    //送气工从用户退回重瓶


    /**
     * 门店模块
     */
    public static final int get_all_story_list = 1011;              //获取门店列表
    public static final int story_heavybot_in_story = 10011;        //门店重瓶入库
    public static final int story_emptybot_to_deliver = 10012;      //门店空瓶出库给送气工
    public static final int story_heavybot_to_delver = 10013;       //门店重瓶出库给送气工
    public static final int story_recycle_empbot_fromdeliver = 10014;   //门店从送气工回收空瓶
    public static final int story_ziti_placeorders = 10015;         //门店自提订单下单
    public static final int story_ziti_ordrs_list = 10016;          //门店自提订单列表
    public static final int story_heavybot_to_client = 1016;        //门店重瓶出库给用户
    public static final int story_ziti_order_return_order = 10017;  //门店自提订单回单
    public static final int story_ziti_recycler_emptybot = 1017;    //门店自提订单从用户回收空瓶
    public static final int story_niming_ziti = 10018;              //门店匿名自提
    public static final int story_reback_heavybot_from_client = 10024; //门店从用户退回重瓶
    public static final int story_reback_heavybot_from_deliver = 10025; //门店从送气工退回重瓶


    public static final int wearhouse_check_bot_outhouse = 10042;   //仓库送检出库
    public static final int wearhouse_check_bot_inhouse = 10043;    //仓库送检入库
    public static final int story_no_send_order = 10044;            //门店未派送单


    public static final int story_in_family_check_photo = 10048;    //门店入库安检拍照
    public static final int story_in_family_order = 10049;          //门店入户安检订单


    public static final int botinfo_initbotcode = 10051;            //初始化二维码





}
