package com.sucheng.gas.scan;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import com.sucheng.gas.R;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.utils.ReadCardUtils;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018/1/26.
 */

public class ReadEmplayeeCard extends CommentScanActivity implements RequestView<JSONObject>{

    // NFC
    private NfcAdapter mAdapter;
    private String[][] techList;
    private IntentFilter[] intentFilters;
    private PendingIntent pendingIntent;

    private RequestPresent requestPressent;
    private RequestQueue requestQueue;

    private int flagCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reademplyee_card);

        initNFC();
        flagCode = getIntent().getIntExtra("flagCode",0);
        requestQueue = NoHttp.newRequestQueue(1);
        requestPressent = new RequestPresent();
        requestPressent.attach(this);

    }

    private void initNFC() {
        // NFC
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            VoiceUtils.showToastVoice(this, R.raw.warning, "当前设备不支持NFC功能!");
            return;
        } else if (mAdapter != null && !mAdapter.isEnabled()) {
            VoiceUtils.showToast(this,"NFC开关未打开!");
            return;
        } else {
            if(mAdapter != null && mAdapter.isEnabled()){
                techList = new String[][]{
                        new String[]{android.nfc.tech.NfcV.class.getName()},
                        new String[]{android.nfc.tech.NfcA.class.getName()}};
                intentFilters = new IntentFilter[]{new IntentFilter(
                        NfcAdapter.ACTION_TECH_DISCOVERED),};

                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            }

        }
    }

    /**
     * 读取用户卡，读取成功返回卡号
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String cardCode = ReadCardUtils.readCardBySector8(ReadEmplayeeCard.this,
                intent);
        if (!Utils.isEmpty(cardCode.trim()) && !cardCode.trim().equals("readCustomerError")) {
            Logger.e("----读卡=="+cardCode.trim());
            findEmpDataByCardId(cardCode.trim());  //通过卡号获取员工信息
        } else {
            VoiceUtils.showToastVoice(this, R.raw.warning, "读卡失败!");
        }
    }

    private void findEmpDataByCardId(String cardCode) {
        String url = UrlCode.FINDEMPLEYEE_MSG_BYCARDID.getUrl();
        if(requestPressent != null){
            Map<String,Object> maps = new HashMap<>();
            maps.put("cardCode",cardCode.trim());
            Logger.e("---url="+url+"--="+maps.toString());
            requestPressent.getPresentRequestJSONObject(requestQueue,1,url,maps,null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null && mAdapter.isEnabled()) {
            mAdapter.enableForegroundDispatch(this, pendingIntent,
                    intentFilters, techList);
        }else{
            VoiceUtils.showToastVoice(this,R.raw.warning,"当前NFC未打开");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        Logger.e("----断开验证用户信息==="+response.get().toString());
        if(response.responseCode() == 200){
            try {
                if(response.get().getInt("code") == 200){
                    Intent intent = new Intent();
                    intent.putExtra("deliveryManId",Utils.getJSONDataFromData(response.get().getString("data"),"deliveryManId"));
                    intent.putExtra("deliveryManFullName",Utils.getJSONDataFromData(response.get().getString("data"),"deliveryManFullName"));
                    setResult(flagCode,intent);
                    this.finish();
                }else{
                    VoiceUtils.showToastVoice(this,R.raw.warning,"错误信息:"+response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("-----error=="+response.getException());
    }
}
