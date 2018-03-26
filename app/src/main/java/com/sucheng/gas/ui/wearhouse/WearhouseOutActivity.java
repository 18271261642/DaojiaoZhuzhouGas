package com.sucheng.gas.ui.wearhouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.TestAdapter;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.scan.ReadEmplayeeCard;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.ConfirmDialog;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/25.
 */

public class WearhouseOutActivity extends CommentScanActivity implements RequestView<JSONObject> {


    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.wearhouseistView)
    ListView wearhouseistView;
    @BindView(R.id.wearhousetotalcodesTv)
    TextView wearhousetotalcodesTv;
    @BindView(R.id.vercardShowTv)
    TextView vercardShowTv;
    //扫描
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    private RequestPresent requestPresent;
    private RequestQueue requestQueue;

    //打开就显示的列表 适配
    private Map<Integer, Integer> mapCount;// 数量
    private Map<Integer, String> mapType;// 类型
    private Map<String, Integer> mapTypeCount;// 类型的数量
    private Map<String, Integer> mapCodeTypeId;// 气瓶码对应的气瓶id
    private List<String> listType;
    private TestAdapter mAdapter;
    //需要提交的数据，
    private List<String> totlaList;
    private ConfirmDialog cond;
    private String deliveryManId = null;    //送气工的id
    int flagCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearhouse_out);
        ButterKnife.bind(this);

        initViews();
        initData();

    }

    private void initData() {
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
        requestQueue = NoHttp.newRequestQueue(3);

        mapCount = new HashMap<>();
        mapType = new HashMap<>();
        mapTypeCount = new HashMap<>();
        mapCodeTypeId = new HashMap<>();
        listType = new ArrayList<>();
        totlaList = new ArrayList<>();

    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        flagCode = getIntent().getIntExtra("flagCode", 0);
        //显示标题
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
        //仓库强制空瓶入库||仓库气瓶充装
        if (flagCode == Module.wearhouse_focusemptybot_inhouse || flagCode == Module.wearhouse_filling_emptybot ) {
            showBuilder("请扫描气瓶编码", false);
        } else {
            commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
            readEmplayeedCard();    //读取员工卡
        }
    }

    //读取员工卡
    private void readEmplayeedCard() {
        Intent intent = new Intent(WearhouseOutActivity.this, ReadEmplayeeCard.class);
        intent.putExtra("flagCode", flagCode);
        startActivityForResult(intent, flagCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("-----" + requestCode + "---=" + resultCode);
        if (requestCode == flagCode) {
            if (resultCode == flagCode) {
                if (cond != null) {
                    cond.dismiss();
                }
                Logger.e("-----读卡返回==" + data.getStringExtra("deliveryManFullName") + "--=" + data.getStringExtra("deliveryManId"));
                vercardShowTv.setText("验证通过-->" + data.getStringExtra("deliveryManFullName"));
                deliveryManId = data.getStringExtra("deliveryManId");

                switch (flagCode) {
                    case Module.wearhouse_heavybot_to_deve: //仓库重瓶出库给送气工
                        showBuilder("请扫描出库重瓶编码", false);
                        break;
                    case Module.wearhouse_reback_heavybot_fromdeve: //从配送工退回重瓶-仓库

                        break;
                    case Module.deliver_emptybot_to_wearhouse: //从配送工空瓶入库-仓库

                        break;
                }
            }
        }

        //摄像头扫描返回
        if(requestCode == (flagCode + 1)){
            if(resultCode == 0 && data != null){
                String scanData = data.getStringExtra("scanResult");
                Logger.e("-------scan="+scanData);
                if(!Utils.isEmpty(scanData)){
                    verticalScanBackData(scanData);
                }
            }
        }
    }

    //得到气瓶类型列表
    @Override
    public void onBottleType(List<AddBottleTypeBean> listBottleType) {
        super.onBottleType(listBottleType);
        for (AddBottleTypeBean addBottleTypeBean : listBottleType) {
            mapCount.put(addBottleTypeBean.getId(), 0);
            mapType.put(addBottleTypeBean.getId(), addBottleTypeBean.getAir_bottle_specifications());
            mapTypeCount.put(addBottleTypeBean.getAir_bottle_specifications(), mapCount.get(addBottleTypeBean.getId()));
            listType.add(addBottleTypeBean.getAir_bottle_specifications());
            mAdapter = new TestAdapter(WearhouseOutActivity.this, listType, mapTypeCount);
            wearhouseistView.setAdapter(mAdapter);
        }

    }

    //扫描返回数据
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalScanBackData(botCode);
    }

    //验证扫描返回的数据
    private void verticalScanBackData(String botCode) {
        if (requestPresent != null) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("bottleCode", botCode);
            switch (flagCode){
                case Module.wearhouse_heavybot_to_deve:     //仓库重瓶出库给送气工--检测气瓶
                    //检测气瓶
                    maps.put("userId", getUserInfo().getData().getUser_id() + "");
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.WEARHOUSE_BOT_TO__DELIVER.getCheck(), maps, botCode);
                    break;
                case Module.wearhouse_focusemptybot_inhouse:    //仓库强制空瓶入库
                    //检测气瓶
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.WEARHOUSE_FORCE_EMPBOTIN.getCheck(), maps, botCode);
                    break;
                case Module.wearhouse_reback_heavybot_fromdeve:     //仓库从送气工退回重瓶--检测重瓶
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("deliveryManId", deliveryManId + "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.WEARHOUSE_REBACKHEAVYBOTFROM_DELIVER.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
                case Module.wearhouse_filling_emptybot:     //仓库重瓶充装入库
                    maps.put("userId", getUserInfo().getData().getUser_id()+ "");
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.WEARHOUSE_BOT_TO_FILLING.getCheck(), maps, botCode);
                    break;
                case Module.deliver_emptybot_to_wearhouse:      //送气工空瓶入库--检测气瓶
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("deliveryManId", deliveryManId + "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.WEARHOUSE_EPMTYBOT_TO_WEARHOUSE.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
                case Module.story_heavybot_in_story:        //门店重瓶入库--检测气瓶
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("deliveryManId", deliveryManId + "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORE_HEAVYBOT_INOUT.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
                case Module.story_emptybot_to_deliver:  //门店空瓶出库给送气工
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("userId",getUserInfo().getData().getUser_id() + "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORY_EMPTYBOT_TO_DELIVER.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
                case Module.story_heavybot_to_delver:   //门店重瓶出库给送气工
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("userId",getUserInfo().getData().getUser_id() + "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORY_HEAVYBOT_OUTTODEVE.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
                case Module.story_recycle_empbot_fromdeliver:   //门店从送气工回收空瓶
                    if (!Utils.isEmpty(deliveryManId)) {
                        maps.put("deliveryManId",deliveryManId+ "");
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORY_REBACKEMPTYBOT_FROMDEVE.getCheck(), maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请先验证送气工!");
                    }
                    break;
            }

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
        Logger.e("-----返回===" + response.get().toString() + "--=" + response.responseCode());
        if (response.responseCode() == 200) {
            try {
                if (response.get().getInt("code") == 200) {
                    switch (what) {
                        case 1: //验证气瓶
                            JSONObject jsonObject = response.get().getJSONObject("data");
                            adapterScanCode(jsonObject.getInt("airBottleId"), jsonObject.getInt("airBottleTypeId"), bot);
                            break;
                        case 2: //提交成功
                            VoiceUtils.showVoice(WearhouseOutActivity.this, R.raw.beep);
                            totlaList.clear();
                            showBuilder("提交成功!", true);
                            break;
                    }

                } else {
                    VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void adapterScanCode(int airBottleId, int airBottleTypeId, String botCode) {
        Logger.e("----适配===" + airBottleId + "--=" + airBottleTypeId + "--=" + botCode);
        if (Utils.isRepeatStrCodes(totlaList, airBottleId + "")) {
            VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "重复气瓶!");
        } else {
            if (cond != null) {
                cond.dismiss();
            }
            VoiceUtils.showVoice(WearhouseOutActivity.this, R.raw.beep);
            mapCodeTypeId.put(botCode, 1);
            totlaList.add(airBottleId + "");
            mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
            mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
            wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("---eroor==" + response.getException() + "--coce=" + response.responseCode());
        VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "错误信息:" + response.getException());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPresent.detach();
    }

    @OnClick({R.id.wearhouseSubBtn,R.id.commentTitleScanImg})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.wearhouseSubBtn:  //提交按钮
                if (totlaList.size() > 0 && requestPresent != null) {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("userId", getUserInfo().getData().getUser_id() + "");
                    maps.put("bottleIds", Utils.ListToStr(totlaList));
                    String url = UrlCode.getFlagUrl(flagCode);
                    //仓库强制空瓶入库；仓库气瓶充装
                    if (flagCode == UrlCode.WEARHOUSE_FORCE_EMPBOTIN.getCode() || flagCode == UrlCode.WEARHOUSE_BOT_TO_FILLING.getCode()) {

                    } else {
                        maps.put("deliveryManId", deliveryManId);
                    }
                    Logger.e("---参数==" + url + "--=" + maps.toString());
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(WearhouseOutActivity.this, R.raw.warning, "请按步骤操作!");
                }
                break;
            case R.id.commentTitleScanImg:  //扫描按钮
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("flagCode",(flagCode+1));
                startActivityForResult(intent,(flagCode+1));
                break;
        }

    }

    private void showBuilder(String content, boolean show) {
        if (cond == null) {
            cond = new ConfirmDialog(WearhouseOutActivity.this);
            cond.show();
            cond.setTitle("提醒");
            cond.setContent(content);
            cond.setBtnShowOrGone(show);
            cond.setOnKeyListener(onKeyListener);
        } else {
            cond.show();
            cond.setTitle("提醒");
            cond.setContent(content);
            cond.setBtnShowOrGone(show);
            cond.setCanceledOnTouchOutside(false);
            cond.setOnKeyListener(onKeyListener);
        }
        cond.setListener(new ConfirmDialog.ClickListener() {
            @Override
            public void doDismiss() {
                cond.dismiss();
                finish();
            }
        });
    }

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                if(cond != null){
                    cond.dismiss();
                }
            }
            return true;
        }
    };
}
