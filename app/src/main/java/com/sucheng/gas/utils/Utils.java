package com.sucheng.gas.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.BuildConfig;
import com.sucheng.gas.base.CommentDataBean;
import com.sucheng.gas.base.EnvType;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.bean.UserBean;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Utils {


    //字符串非空
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * long转时间
     */
    public static String longToDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    /**
     * String  类型转Date
     * @param str
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date StringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 判断数组中是否包含某个元素
     * @param arr
     * @param value
     * @return
     */
    public static boolean isContanis(String[] arr,String value){

        return Arrays.asList(arr).contains(value);

    }

    /**
     * 解析json数组中的code
     */
    public static int getJSONCode(String jsonData){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return jsonObject.getInt("code");
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
    }

    /**
     * 解析json数组中的data
     */
    public static String getJSONData(String jsonData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            return  jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取data中的value
    public static String getJSONDataFromData(String data,String keys){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            return jsonObject.getString(keys);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取json中的MSG
     */
    public static String getJSONMSG(String jsonData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析气瓶轨迹
     */
    public static List<AddBottleTypeBean> paraseServerBottleType(String data) {

        Type type = new TypeToken<CommentDataBean<List<AddBottleTypeBean>>>() {
        }.getType();
        CommentDataBean<List<AddBottleTypeBean>> basc = JSONToObj(data, type);
        return basc.getData();
    }

    /**
     * json转实体类
     */
    public static <T> T JSONToObj(String jsonStr, Type type) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    /**
     * 判断字符串为数字型
     */

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //判断是否重复
    public static boolean isRepeatStrCodes(List<String> listCodes, String code) {

        for (String s : listCodes) {
            if (s.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    //list拆分
    public static <T> String ListToStr(List<T> list) {

        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0 && !list.isEmpty()) {

            for (T s : list) {
                sb.append(s).append(",");
            }
        }
        return sb.toString().substring(0, sb.toString().length()-1);
    }

    /**
     * 获取用户信息，此方法必须成功登录成功后使用
     * @param context
     * @return
     */
    public static UserBean getUserInfo(Context context){
        UserBean userBean = null;
        String userStringData = (String) SharedPreferenceUtils.get(context,"userInfo","");
        if(userStringData != null){
            userBean = new Gson().fromJson(userStringData,UserBean.class);
        }
        return userBean;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        String data = "\\s*|t*|r*|n*";
        Pattern p = Pattern.compile(data);
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是中文
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static void initAppMeta(Context context) {
        String meta = "com.amap.api.v2.apikey";

        ApplicationInfo applicationInfo = null;

            try {
                applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        int envType = BuildConfig.ENV_TYPE;
        switch (envType) {
            case EnvType.DAOJIAO_GAS:
                applicationInfo.metaData.putString(meta,"c90e2f92657fb20a7f825699d900e59a");//道滘SJH
                break;
            case EnvType.HENGYUAN_GAS:
                applicationInfo.metaData.putString(meta,"9a67cee7924b7a537cfbce6d704f8fce");//恒源
                break;
            case EnvType.HUAYANG_GAS:   //华洋燃气
                applicationInfo.metaData.putString(meta,"c51da8d25149aa12939b6b2c1b7fac0a");
                break;
            case EnvType.CPCT_GAS:  //常平常泰
                applicationInfo.metaData.putString(meta,"2879997a17957047aef88d0d539d285e");
                break;
            case EnvType.ZTZY_GAS:      //中堂中液
                applicationInfo.metaData.putString(meta,"cdd6f31d2c9be21faaf1ea073d5079f5");
                break;
            case EnvType.HOUJIE_HL_GAS: //厚街厚龙
                applicationInfo.metaData.putString(meta,"fc48087bc58f27907771d45884a55854");
                break;
        }

    }

}
