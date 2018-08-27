package com.sucheng.gas.utils;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import com.sucheng.gas.BuildConfig;
import com.sucheng.gas.R;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ReadCardUtils {

    //private static final String sector7pwd = "2956802978ff"; // 第7扇区密码
    private static final String sector7pwd = "1229179627ff";    //第7扇区密码
    //private static final String sector8pwd = "0587317295ff";// 第8扇区密码 0587317295ff
    private static final String sector8pwd = "0587317295ff";

    /**
     * 读卡返回值解析
     *
     * @param b
     * @param len
     * @return result
     */
    public static byte[] subByte(byte[] b, int len) {

        byte[] result = new byte[len];

        for (int i = 0; i < result.length; i++) {
            result[i] = b[i];
        }
        return result;
    }

    /**
     * 卡密码解析
     *
     * @param inputStr
     */
    public static byte[] change(String inputStr) {
        byte[] result = new byte[7];
        for (int i = 0; i < inputStr.length() / 2; ++i)
            result[i] = (byte) (Integer.parseInt(
                    inputStr.substring(i * 2, i * 2 + 2), 16) & 0xff);
        return result;
    }

    /**
     * 读卡 第七扇区第二十八块或第八扇区第三十二块的信息
     *
     * @param context
     * @param intent
     * @return 客户卡编码, 员工卡编码
     */
    public static String readCardBySector7(Context context, Intent intent) {

        return readCard(context, intent, 7, 16);
    }

    public static String readCardBySector8(Context context, Intent intent) {

        return readCard(context, intent, 8, 12);
    }

    public static String readCard(Context context, Intent intent, int b,
                                  int resultLen) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            if (mfc != null) {
                try {
                    mfc.connect();
                    boolean auth = false;
                    byte[] result;
                    if (b == 7) {

                        auth = mfc.authenticateSectorWithKeyA(7,change(sector7pwd)); // 验证密码
                        //auth = mfc.authenticateSectorWithKeyA(7,sector7pwd.getBytes()); // 验证密码
                        if (auth) {
                            result = mfc.readBlock(Integer.valueOf(28));
                            return new String(subByte(result, resultLen));// 读取M1卡的第28块即7扇区第0块
                        }else{
                            VoiceUtils.showToast(context,"密码认证失败");
                        }
                    } else if (b == 8) {

                        auth = mfc.authenticateSectorWithKeyA(8,
                                change(sector8pwd)); // 验证密码
                        if (auth) {

                            result = mfc.readBlock(Integer.valueOf(32));
                            return new String(subByte(result, resultLen));// 读取M1卡的第32块即7扇区第0块
                        }
                    } else{
                        VoiceUtils.showToastVoice(context, R.raw.warning ,"密码认证失败");//infoShow(context, "密码认证失败");
                    }

                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "readCustomerError";
    }

}
