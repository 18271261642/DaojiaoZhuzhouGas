package com.sucheng.gas.scan;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.google.gson.Gson;
import com.sucheng.gas.R;
import com.sucheng.gas.bean.ClientBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.utils.ReadCardUtils;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.HttpListener;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/27.
 */

public class ReadClientCardActivity extends CommentScanActivity implements RequestView<JSONObject>{

    // NFC
    private NfcAdapter mAdapter;
    private String[][] techList;
    private IntentFilter[] intentFilters;
    private PendingIntent pendingIntent;

    private RequestPresent requestPressent;
    private RequestQueue requestQueue;
    private int flagCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readclient_card);

        initNFC();
        flagCode = getIntent().getIntExtra("flagCode",0);
        requestQueue = NoHttp.newRequestQueue(1);
        requestPressent = new RequestPresent();
        requestPressent.attach(this);

//        //获取匿名客户信息
//        if(flagCode == UrlCode.STORYANONY_ZITI.getCode()){
//            String urls = UrlCode.STORYANONY_ZITI.getUrl();
//            if(requestPressent != null){
//                requestPressent.getPresentRequestJSONObject(requestQueue,1,urls,null,null);
//            }
//        }

    }

    private void initNFC() {
        // NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mAdapter != null && mAdapter.isEnabled()){
            techList = new String[][]{
                    new String[]{android.nfc.tech.NfcV.class.getName()},
                    new String[]{android.nfc.tech.NfcA.class.getName()}};
            intentFilters = new IntentFilter[]{new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED),};

            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }else{
            if(mAdapter == null){
              VoiceUtils.showToastVoice(ReadClientCardActivity.this,R.raw.warning,"当前设备不支持NFC功能!");
            }else{
                if(!mAdapter.isEnabled()){
                    VoiceUtils.showToastVoice(ReadClientCardActivity.this,R.raw.warning,"当前设备NFC为打开!");
                    startActivity(new Intent("android.settings.NFC_SETTINGS"));
                }
            }

        }

    }

    //扫描信息返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        findEmpDataByCardId(botCode);
    }

    /**
     * 读取用户卡，读取成功返回卡号
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String cardCode = ReadCardUtils.readCardBySector7(ReadClientCardActivity.this,
                intent);
        if (!Utils.isEmpty(cardCode.trim()) && !cardCode.trim().equals("readCustomerError")) {
            Logger.e("----读卡=="+cardCode.trim());
            findEmpDataByCardId(cardCode.trim());  //通过卡号获取员工信息
        } else {
            VoiceUtils.showToastVoice(this, R.raw.warning, "读卡失败!");
        }
    }

    //通过卡号或扫描客户编码查询客户信息
    private void findEmpDataByCardId(String cardcode) {
        String url = UrlCode.FINDCLIENT_MSG_BYCARD.getUrl();
        Map<String,Object> maps = new HashMap<>();
        if(requestPressent != null){
            maps.put("cardCode",cardcode);
            requestPressent.getPresentRequestJSONObject(requestQueue,1,url,maps,cardcode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null && mAdapter.isEnabled()) {
            mAdapter.enableForegroundDispatch(this, pendingIntent,
                    intentFilters, techList);
        }else{
            if(mAdapter == null){
                VoiceUtils.showToastVoice(this,R.raw.warning,"当设备不支持NFC功能!");
            }else{
                VoiceUtils.showToastVoice(this,R.raw.warning,"当前NFC未打开");
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null && mAdapter.isEnabled()) {
            mAdapter.disableForegroundDispatch(this);
        }

    }

    @Override
    public void showLoadDialog(int what) {
        showWatiDialog();
    }

    @Override
    public void closeLoadDialog(int what) {
        closeWatiDialog();
    }

    @Override
    public void requestSuccessData(int what, Response<JSONObject> response, String bot) {
        Logger.e("-----获取客户信息返回=="+response.get().toString());
        if(response.responseCode() == 200){ //请求成功
            try {
                if(response.get().getInt("code") == 200){
                    VoiceUtils.showVoice(this,R.raw.beep);
                    String data = response.get().getString("data");
                    if(data != null){
                        ClientBean clientBean = new Gson().fromJson(data,ClientBean.class);
                        Intent intent = new Intent();
                        intent.putExtra("client_name",clientBean.getClient_name());
                        intent.putExtra("clientId",clientBean.getClientId()+"");
                        setResult(flagCode,intent);
                        this.finish();
                    }
                }else{
                    VoiceUtils.showToastVoice(this,R.raw.warning,"验证失败:"+response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        VoiceUtils.showToastVoice(this,R.raw.warning,"错误信息:"+response.getException().toString());
    }
}
