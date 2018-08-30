package com.sucheng.gas.ui.botmsg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.bean.AirBotInfoBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.ConfirmDialog;
import com.sucheng.gas.view.ListDialogView;
import com.sucheng.gas.view.PromptDialog;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/31.
 */

/**
 * 初始化二维码
 */
public class InitBotCodeActivity extends CommentScanActivity implements RequestView<JSONObject>{


    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    @BindView(R.id.initbotcode_airbottlecode)
    TextView initbotcodeAirbottlecode;
    @BindView(R.id.initbotcode_bottlesealcode)
    EditText initbotcodeBottlesealcode;
    @BindView(R.id.initbotcode_serachbysealcode)
    Button initbotcodeSerachbysealcode;
    @BindView(R.id.initbotcode_bottlespecifications)
    TextView initbotcodeBottlespecifications;
    @BindView(R.id.initbotcode_factoryNumTv)
    EditText initbotcodeFactoryNumTv;
    @BindView(R.id.factorySearchBtn)
    Button factorySearchBtn;
    @BindView(R.id.initbotcode_productionunitname)
    TextView initbotcodeProductionunitname;
    @BindView(R.id.initbotcode_detectionunitname)
    TextView initbotcodeDetectionunitname;
    @BindView(R.id.initbotcode_createtime)
    TextView initbotcodeCreatetime;
    @BindView(R.id.initbotcode_checktime)
    TextView initbotcodeChecktime;
    @BindView(R.id.initbotcode_nextchecktime)
    TextView initbotcodeNextchecktime;
    @BindView(R.id.initNewScanBtn)
    Button initNewScanBtn;

    private ConfirmDialog confirmDialog;
    private ListDialogView listDialogView;
    private PromptDialog promptDialog;

    private RequestPresent requestPresent;
    private RequestQueue requestQueue;

    private int airBotId =-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_botcode);
        ButterKnife.bind(this);


        initViews();
        requestQueue = NoHttp.newRequestQueue(1);
        requestPresent = new RequestPresent();
        requestPresent.attach(this);

        showBuilder("请先获取气瓶信息",true);
    }

    private void initViews() {
        commentTitleTv.setText("初始化二维码");
        commentTitleScanImg.setVisibility(View.VISIBLE);
    }

    //手持机扫描返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalNewBotCode(botCode);    //验证新的二维码
    }

    //验证气瓶是否存在
    private void verticalNewBotCode(String botCode) {
        if(requestPresent != null){
            Map<String,Object> mps = new HashMap<>();
            mps.put("bottleCode",botCode);
            requestPresent.getPresentRequestJSONObject(requestQueue,2,UrlCode.BOTMSG_INITBOT.getCheck(),mps,botCode);
        }
    }

    @OnClick({R.id.commentTitleScanImg, R.id.initbotcode_serachbysealcode, R.id.factorySearchBtn, R.id.initNewScanBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commentTitleScanImg:  //扫描图片按钮
               if(airBotId != -1){
                   Intent intent = new Intent(this, CaptureActivity.class);
                   intent.putExtra("flagCode",1001);
                   startActivityForResult(intent,1001);
               }else{
                   VoiceUtils.showToastVoice(this,R.raw.warning,"请按步骤操作!");
               }

                break;
            case R.id.initbotcode_serachbysealcode: //气瓶编码搜索按钮
                String botCodeSearch = initbotcodeBottlesealcode.getText().toString().trim();
                if(!Utils.isEmpty(botCodeSearch)){
                    searchBotInfoMsg(botCodeSearch,"");
                }
                break;
            case R.id.factorySearchBtn: //出厂编号搜索按钮
                String factorySearch = initbotcodeFactoryNumTv.getText().toString().trim();
                if(!Utils.isEmpty(factorySearch)){
                    searchBotInfoMsg("",factorySearch);
                }
                break;
            case R.id.initNewScanBtn:   //提交按钮
                if(airBotId != -1 && !Utils.isEmpty(initbotcodeAirbottlecode.getText().toString().trim()) && !Utils.isEmpty(initbotcodeBottlesealcode.getText().toString().trim())){
                    showSubAlertDialog();
                }else{
                    if(!Utils.isEmpty(initbotcodeBottlesealcode.getText().toString().trim())){
                        showBuilder("请扫描新的二维码",false);
                    }else{
                        VoiceUtils.showToastVoice(this,R.raw.warning,"请按步骤操作!");
                    }

                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            if(data != null){
                String resultData = data.getStringExtra("scanResult");
                if(!Utils.isEmpty(resultData)){
                    verticalNewBotCode(resultData);
                }

            }
        }
    }

    //搜索
    private void searchBotInfoMsg(String botCodeSearch,String factoryStr) {
        if(requestPresent != null){
            Map<String,Object> map = new HashMap<>();
            map.put("air_bottle_seal_code1",botCodeSearch);
            map.put("factory_number1",factoryStr);
            requestPresent.getPresentRequestJSONObject(requestQueue,1, UrlCode.BOTMSG_INITBOTCODE.getUrl(),map,"11");
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
        closeWatiDialog();
        Logger.e("-----res="+what+bot);
        try {
            if(response.get().getInt("code") == 200){
                VoiceUtils.showVoice(this,R.raw.beep);
                if(what == 1){  //回填气瓶信息
                    String data = response.get().getString("data");
                    if(!Utils.isEmpty(data) && !data.equals("[]")){
                        List<AirBotInfoBean.DataBean> airBotInfoBeanList = new Gson().fromJson(data,new TypeToken<List<AirBotInfoBean.DataBean>>(){}.getType());
                        showAirListDialog(airBotInfoBeanList);
                    }
                }else if(what == 2){    //验证气瓶是否存在
                    if(confirmDialog != null){
                        confirmDialog.dismiss();
                    }
                    initbotcodeAirbottlecode.setText(bot);
                    showSubAlertDialog();
                }else if(what == 3){    //提交返回
                    showToast(this,"提交成功!");
                    clearMsgData();
                }
            }else{
                VoiceUtils.showToastVoice(InitBotCodeActivity.this,R.raw.warning,"非200返回:"+response.get().getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void clearMsgData() {
        airBotId = -1;
        initbotcodeAirbottlecode.setText("");
        initbotcodeBottlesealcode.setText("");
        initbotcodeBottlespecifications.setText("");
        initbotcodeFactoryNumTv.setText("");
        initbotcodeProductionunitname.setText("");
        initbotcodeDetectionunitname.setText("");
        initbotcodeCreatetime.setText("");
        initbotcodeChecktime.setText("");
        initbotcodeNextchecktime.setText("");
    }


    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        closeWatiDialog();
        Logger.e("-----err="+response.getException().toString());

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(requestPresent != null){
            requestPresent.detach();
        }
    }

    private void showSubAlertDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.show();
        promptDialog.setTitle("提醒");
        promptDialog.setContent("是否初始化二维码?");
        promptDialog.setleftText("否");
        promptDialog.setrightText("是");
        promptDialog.setListener(new OnPromptDialogListener() {
            @Override
            public void leftClick(int code) {
                promptDialog.dismiss();

            }

            @Override
            public void rightClick(int code) {
                promptDialog.dismiss();
                subMitBotInfo();

            }
        });
    }

    private void subMitBotInfo() {
        String botCot = initbotcodeAirbottlecode.getText().toString().trim();   //编码
       if(requestPresent != null){
           Map<String,Object> map = new HashMap<>();
           map.put("userId",getUserInfo().getData().getUser_id()+"");
           map.put("air_bottle_code",botCot);
           map.put("air_bottle_id",airBotId+"");
           requestPresent.getPresentRequestJSONObject(requestQueue,3,UrlCode.BOTMSG_INITBOTCODE.getCheck(),map,"23");
       }


    }

    //展示钢印码列表
    private void showAirListDialog(final List<AirBotInfoBean.DataBean> airBotInfoBeanList) {
        listDialogView = new ListDialogView(this);
        listDialogView.show();
        listDialogView.setListData(airBotInfoBeanList);
        listDialogView.setDialogListener(new ListDialogView.ListDialogViewItemClickListener() {
            @Override
            public void position(int position) {
                listDialogView.dismiss();
                showAirBotInfoData(airBotInfoBeanList,position);
            }
        });
    }

    //回填信息
    private void showAirBotInfoData(List<AirBotInfoBean.DataBean> airBotInfoBeanList, int position) {
        airBotId = airBotInfoBeanList.get(position).getAir_bottle_id();
        initbotcodeAirbottlecode.setText(airBotInfoBeanList.get(position).getAir_bottle_code()+"");
        initbotcodeBottlesealcode.setText(airBotInfoBeanList.get(position).getAir_bottle_seal_code()+"");
        initbotcodeBottlespecifications.setText(airBotInfoBeanList.get(position).getAir_bottle_type_name()+"");
        initbotcodeFactoryNumTv.setText(airBotInfoBeanList.get(position).getFactory_number()+"");
        initbotcodeProductionunitname.setText(airBotInfoBeanList.get(position).getProduction_unit_name()+"");
        initbotcodeDetectionunitname.setText(airBotInfoBeanList.get(position).getDetection_unit_name()+"");
        initbotcodeCreatetime.setText(Utils.longToDate(airBotInfoBeanList.get(position).getProduce_time())+"");
        initbotcodeChecktime.setText(Utils.longToDate(airBotInfoBeanList.get(position).getCheck_time())+"");
        initbotcodeNextchecktime.setText(Utils.longToDate(airBotInfoBeanList.get(position).getNext_check_time())+"");
    }

    private void showBuilder(String msg,boolean isShow){
        confirmDialog = new ConfirmDialog(this);
        confirmDialog.show();
        confirmDialog.setBtnShowOrGone(isShow);
        confirmDialog.setTitle("提醒");
        confirmDialog.setContent(msg);
        confirmDialog.setListener(new ConfirmDialog.ClickListener() {
            @Override
            public void doDismiss() {
                confirmDialog.dismiss();
            }
        });
    }
}
