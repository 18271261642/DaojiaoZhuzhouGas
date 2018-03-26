package com.sucheng.gas.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.sucheng.gas.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class VoiceUtils {

    private static MediaPlayer mediaPlayer;
    private static Toast mToast;

    public static void showToast(Context context,String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }


    //开启提示音
    public static void showVoice(Context mContext,int voice){
        mediaPlayer = MediaPlayer.create(mContext,voice);
        if(mediaPlayer != null){
            mediaPlayer.start();
        }else{
            VoiceUtils.showToast(mContext,"提示音加载错误!");
        }

    }

    //提示音+文字
    public static void showToastVoice(Context mContext,int voice,String txt){
        showToast(mContext,txt);
        showVoice(mContext, voice);
    }

}
