package com.sucheng.gas.constants;

/**
 * Created by Administrator on 2018/1/24.
 */

public enum UrlCode {

    // 登录模块 登录，登出
    LOGINMOBILE_GETUSERLOGIN(Module.user_login, Constants.LOGIN_MOBILE+"login","用户登录",Constants.LOGIN_MOBILE+"logOut"),

    /**用户模块，通过卡号获取员工信息 **/
    FINDEMPLEYEE_MSG_BYCARDID(Module.user_findemplayee_bycard,Constants.USER_MOBILE+"findUserByCardCode","通过卡号获取员工信息",""),
    //获取匿名用户信息
    FINDANONYCLIENT_MSG(Module.user_getanonymity_msg,Constants.USER_MOBILE+"getAnonymousClientInfo","获取匿名客户信息",""),

    /**客户模块 通过卡号获取客户信息**/
    FINDCLIENT_MSG_BYCARD(Module.user_findclient_bycard,Constants.CLIENT_MOBILE+"checkClientOnlyByCardCode","通过卡号获取客户信息",""),


    /**气瓶信息 生产厂家，检测单位**/
    BOTMSG_PRODUCE(Module.bot_msg,Constants.BOTMSG_MOBILE+"getAllProductionUnitList","生产厂家",Constants.BOTMSG_MOBILE+"getAllDetectionUnitList"),
    /** 气瓶轨迹查询 | 气瓶类型**/
    BOTMSG_BOTTRAJECORY(Module.bot_path,Constants.BOTMSG_MOBILE+"getAirBottleInfo","气瓶轨迹",Constants.BOTMSG_MOBILE+"getAllAirBottleTypeList"),
    /**获取门店列表 | 归属单位**/
    BOTMSG_GETSTORYLIST(Module.get_all_story_list,Constants.BOTMSG_MOBILE+"getAllAirBottleBelongList","获取门店列表",Constants.BOTMSG_MOBILE+"getAllAirBottleBelongList"),

    /**手动版本更新  自动版本更新**/
    APP_UPDATE(Module.app_update,Constants.APP_VERSIONMSG+"getAppManualUpdateVersionInfo?","版本更新",Constants.APP_VERSIONMSG+"getAppVersionInfo?versionCode="),

    /**气瓶初始化 检测气瓶是否存在**/
    BOTMSG_INITBOT(Module.init_bot,Constants.BOTMSG_MOBILE+"addAirBottleInfo","检测气瓶是否存在",Constants.BOTMSG_MOBILE+"checkAirBottleCodeExist"),

    /**初始化二维码**/
    BOTMSG_INITBOTCODE(Module.botinfo_initbotcode,Constants.BOTMSG_MOBILE+"findNonQRCodeAirBottleInfo","初始化二维码",Constants.BOTMSG_MOBILE+"saveNewAirBottleQRCode"),


    /** 仓库强制空瓶入库 | 检测**/
    WEARHOUSE_FORCE_EMPBOTIN(Module.wearhouse_focusemptybot_inhouse,Constants.WEARHOUSE_MOBILE+"forceEmptyBottleStorage","仓库强制空瓶入库",Constants.WEARHOUSE_MOBILE+"forceEmptyBottleStorageCheck"),

    /**仓库重瓶出库给送气工 | 检测 **/
    WEARHOUSE_BOT_TO__DELIVER(Module.wearhouse_heavybot_to_deve,Constants.WEARHOUSE_MOBILE+"heavyBottleOutToDeliveryMan","仓库重瓶出库给送气工",Constants.WEARHOUSE_MOBILE+"heavyBottleOutToDeliveryManCheck"),

    /**仓库从送气工退回重瓶 | 检测 **/
    WEARHOUSE_REBACKHEAVYBOTFROM_DELIVER(Module.wearhouse_reback_heavybot_fromdeve,Constants.WEARHOUSE_MOBILE+"heavyBottleBackFromDeliveryMan","仓库从送气工退回重瓶",Constants.WEARHOUSE_MOBILE+"heavyBottleBackFromDeliveryManCheck"),

    /**送气工空瓶回仓库 | 检测**/
    WEARHOUSE_EPMTYBOT_TO_WEARHOUSE(Module.deliver_emptybot_to_wearhouse,Constants.WEARHOUSE_MOBILE+"emptyBottleStorageFromDeliveryMan","从配送工空瓶入库-仓库",Constants.WEARHOUSE_MOBILE+"emptyBottleStorageFromDeliveryManCheck"),

    /**仓库气瓶充装 | 检测**/
    WEARHOUSE_BOT_TO_FILLING(Module.wearhouse_filling_emptybot ,Constants.WEARHOUSE_MOBILE+"heavyBottleStorageByFilling","仓库重瓶充装",Constants.WEARHOUSE_MOBILE+"heavyBottleStorageByFillingCheck"),

    /**仓库匿名强制充装**/
    WEARHOUSE_NIMING_CHOGNZHUANG(Module.wearhouse_niming_focus_filling,Constants.WEARHOUSE_MOBILE+"forceFillingPickUpInWarehouseForClient","仓库匿名强制充装",Constants.WEARHOUSE_MOBILE+"forceFillingPickUpInWarehouseForClientCheck"),

    /**仓库匿名自提下单**/
    WEARHOUSE_NIMING_ZITI(Module.wearhouse_niming_ziti_orders,Constants.WEARHOUSE_MOBILE+"addMobileWarehouseOrder","仓库匿名自提下单",Constants.WEARHOUSE_MOBILE+"addMobileWarehouseOrderCheck"),

    /**仓库自提订单**/
    WEARHOUSE_ZITI_ORDERLIST(Module.wearhouse_ziti_order_list,Constants.WEARHOUSE_MOBILE+"getMyNoDeliveryList","仓库自提订单",""),
    //仓库重瓶出库给用户
    WEARHOUSE_HEAVYBOTTO_CLIENT(Module.wearhouse_heavybot_to_client,Constants.WEARHOUSE_MOBILE+"heavyBottleOutToClient","仓库重瓶出库给用户",Constants.WEARHOUSE_MOBILE+"heavyBottleOutToClientCheck"),

    /**仓库自提订单回单**/
    WEARHOUSE_ZITI_BAC_ORDER_LIST(Module.wearhouse_ziti_return_order,Constants.WEARHOUSE_MOBILE+"getMyDeliveryReceiptList","仓库自提订单回单",""),
    //仓库从用户回收空瓶
    WEARHOUSE_ZITI_RECYCLEEMPBOT(Module.wearhouse_recycle_emptybot_from_client,Constants.WEARHOUSE_MOBILE+"emptyBottleStorageFromClient","仓库从用户回收空瓶",Constants.WEARHOUSE_MOBILE+"emptyBottleStorageFromClientCheck"),


    //仓库送检出库
    WEARHOUSE_CHECK_BOT_OUTHOUSE(Module.wearhouse_check_bot_outhouse,Constants.WEARHOUSE_CHECK_BOT_MOBILE+"bottleOutToInspection","仓库送检出库",Constants.WEARHOUSE_CHECK_BOT_MOBILE+"bottleOutToInspectionCheck"),
    //仓库送检入库
    WEARHOUSE_CHECK_BOT_INHOUSE(Module.wearhouse_check_bot_inhouse,Constants.WEARHOUSE_CHECK_BOT_MOBILE+"bottleBackFromInspection","仓库送检入库",Constants.WEARHOUSE_CHECK_BOT_MOBILE+"bottleBackFromInspectionCheck"),


    /**
     * 送气工模块
     */
    /**送气工-我的未派送单 | 我的未回单**/
    DELIVER_MYNOSEND_ORDER(Module.deliver_my_nosend_orders,Constants.DELIVER_MOBILE+"getMyNoDeliveryList","我的未派送单",""),
    //配送重瓶给客户 | 检测
    DELIVER_SENDHEAVYBOT_TOCLIENT(Module.deliver_heavybot_to_client,Constants.DELIVER_MOBILE+"heavyBottleOutToClient","配送重瓶给客户",Constants.DELIVER_MOBILE+"heavyBottleOutToClientCheck"),

    /**我的未回单**/
    DELIVER_MYNOREBACK_ORDER(Module.deliver_my_noback_orders,Constants.DELIVER_MOBILE+"getMyDeliveryReceiptList","我的未回单",""),
    //从客户回收空瓶
    DELIVER_REBACK_EMPTYBOT_FROMGCLIENT(Module.deliver_recycle_empty_bot_fromclient,Constants.DELIVER_MOBILE+"emptyBottleStorageFromClient","送配工从客户回收空瓶",Constants.DELIVER_MOBILE+"emptyBottleStorageFromClientCheck"),

    /**配送送工从用户退回重瓶**/
    DELIVER_REBACK_HEAVYBOT_FROM_CLIENT(Module.deliver_reback_heavy_bot_fromclient,Constants.DELIVER_MOBILE+"heavyBottleBackFromClient","送气工从用户退回重瓶",Constants.DELIVER_MOBILE+"heavyBottleBackFromClientCheck"),

    /**
     * 门店模块
     */
    /**门店重瓶入库 | 检测**/
    STORE_HEAVYBOT_INOUT(Module.story_heavybot_in_story,Constants.STORY_MOBILE+"heavyBottleStorageFromDeliveryMan","门店重瓶入库",Constants.STORY_MOBILE+"heavyBottleStorageFromDeliveryManCheck"),

    /**门店空瓶出库给送气工 | 检测**/
    STORY_EMPTYBOT_TO_DELIVER(Module.story_emptybot_to_deliver,Constants.STORY_MOBILE+"emptyBottleOutToDeliveryMan","门店空瓶出库给送气工",Constants.STORY_MOBILE+"emptyBottleOutToDeliveryManCheck"),

    /**门店重瓶出库给送气工| 检测**/
    STORY_HEAVYBOT_OUTTODEVE(Module.story_heavybot_to_delver,Constants.STORY_MOBILE+"heavyBottleOutToDeliveryMan","门店重瓶出库给送气工",Constants.STORY_MOBILE+"heavyBottleOutToDeliveryManCheck"),

    /**门店从送气工回收空瓶 | 检测**/
    STORY_REBACKEMPTYBOT_FROMDEVE(Module.story_recycle_empbot_fromdeliver,Constants.STORY_MOBILE+"emptyBottleStorageFromDeliveryMan","门店从送气工回收空瓶",Constants.STORY_MOBILE+"emptyBottleStorageFromDeliveryManCheck"),

    /**门店自提订单下单 | 检测**/
    STORY_ZITI_PLACEORDER(Module.story_ziti_placeorders,Constants.STORY_MOBILE+"addMobileStoreOrder","门店自提订单下单",Constants.STORY_MOBILE+"addStoreOrderCheck"),

    /**门店自提订单**/
    STORY_ZITI_ORDER(Module.story_ziti_ordrs_list,Constants.STORY_MOBILE+"getMyNoDeliveryList","门店自提订单",""),
    //门店重瓶出库给用户 | 检测
    STORY_ZITI_HBOT_TOCLIENT(Module.story_heavybot_to_client,Constants.STORY_MOBILE+"heavyBottleOutToClient","门店重瓶出库给用户",Constants.STORY_MOBILE+"heavyBottleOutToClientCheck"),

    /**门店自提订单回单**/
    STORY_ZITI_BACKORDER(Module.story_ziti_order_return_order,Constants.STORY_MOBILE+"getMyDeliveryReceiptList","门店自提订单回单",""),
    //门店自提订单回单收空瓶
    STORY_ZITI_BACKORDER_EMPTYBOT(Module.story_ziti_recycler_emptybot,Constants.STORY_MOBILE+"emptyBottleStorageFromClient","回空瓶",Constants.STORY_MOBILE+"emptyBottleStorageFromClientCheck"),

    /**门店匿名自提**/
    STORYANONY_ZITI(Module.story_niming_ziti,Constants.CLIENT_MOBILE+"getAnonymousClientInfo","门店匿名自提",""),

    /**门店从用户退回重瓶 | 检测**/
    STORY_REBACK_HEAVYBOT_FROM_CLIENT(Module.story_reback_heavybot_from_client,Constants.STORY_MOBILE+"heavyBottleBackFromClient","门店从用户退回重瓶",Constants.STORY_MOBILE+"heavyBottleBackFromClientCheck"),
    /**门店从送气工退回重瓶 | 检测**/
    STORY_REBACK_HEAVYBOT_FROM_DELIVER(Module.story_reback_heavybot_from_deliver,Constants.STORY_MOBILE+"heavyBottleBackFromDeliveryMan","门店从送气工退回重瓶",Constants.STORY_MOBILE+"heavyBottleBackFromDeliveryManCheck"),
    /**门店未派送单**/
    STORY_NO_SEND_ORDER_LIST(Module.story_no_send_order,Constants.STORY_MOBILE+"getNoDispatchList","门店未派送单",""),

    /**获取门店入户安检订单**/
    GET_STORY_FAMILY_CHECK_ORDER(Module.story_in_family_order,Constants.STORY_CHECK_ORDER+"getSotreFamilyCheckOrder","门店入户安检",Constants.STORY_CHECK_ORDER+"addMobileFamilyCheckOrder"),
    /**入户安检拍照**/
    STORY_FAMILY_CHECK_PHOTO(Module.story_in_family_check_photo,Constants.STORY_CHECK_ORDER+"updateMobileFamilyCheckOrder","入户安检订单拍照",""),

    ;

    private int code;
    private String url;
    private String des;
    private String check;

    UrlCode(int code, String url, String des, String check) {
        this.code = code;
        this.url = url;
        this.des = des;
        this.check = check;
    }

    public static String getFlagUrl(int code) {
        for (UrlCode fc : UrlCode.values()) {
            if (fc.getCode() == code) {
                return fc.getUrl();
            }
        }

        return null;
    }

    public static String getFlagDes(int code){
        for (UrlCode fc : UrlCode.values()) {
            if (fc.getCode() == code) {
                return fc.getDes();
            }
        }
        return null;
    }

    public static String getFlagCheck(int code) {
        for (UrlCode fc : UrlCode.values()) {
            if (fc.getCode() == code) {
                return fc.getCheck();
            }
        }

        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
