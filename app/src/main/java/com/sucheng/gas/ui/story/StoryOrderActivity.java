package com.sucheng.gas.ui.story;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.TestAdapter;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.bean.ClientBean;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.scan.ReadClientCardActivity;
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
 * Created by Administrator on 2018/1/27.
 */

/**
 * 门店自提订单下单
 * 门店匿名自提下单
 */
public class StoryOrderActivity extends CommentScanActivity implements RequestView<JSONObject> {

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.wearhouseistView)
    ListView wearhouseistView;
    @BindView(R.id.wearhousetotalcodesTv)
    TextView wearhousetotalcodesTv;
    @BindView(R.id.vercardShowTv)
    TextView vercardShowTv;
    //摄像头扫描
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
    private String clientId = null;    //客户的id
    int flagCode;
    private int airBotTypeId = -1;  //气瓶类型Id

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

        //门店自提 需要读卡
        if(flagCode == UrlCode.STORY_ZITI_PLACEORDER.getCode()){
            readClientCard();   //读取用户卡
        }else{  //门店匿名客户自提、仓库强制自提
            //获取匿名客户信息
            String urls = UrlCode.STORYANONY_ZITI.getUrl();
            if(requestPresent != null){
                requestPresent.getPresentRequestJSONObject(requestQueue,3,urls,null,null);
            }
        }

    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        flagCode = getIntent().getIntExtra("flagCode", 0);
        //显示标题
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));

    }

    private void readClientCard() {
        Intent intent = new Intent(this, ReadClientCardActivity.class);
        intent.putExtra("flagCode", flagCode);
        startActivityForResult(intent, flagCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == flagCode) {      //读卡返回
            if (resultCode == flagCode) {
                Logger.e("-----======" + data.getStringExtra("client_name") + "--=" + data.getStringExtra("clientId"));
                vercardShowTv.setText("客户正确-->" + data.getStringExtra("client_name"));
                clientId = data.getStringExtra("clientId");
            }
        }
        //摄像头扫描返回
        if(requestCode == (flagCode + 1)){
            if(resultCode == 0 && data != null){
                String scanData = data.getStringExtra("scanResult");
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
            mAdapter = new TestAdapter(StoryOrderActivity.this, listType, mapTypeCount);
            wearhouseistView.setAdapter(mAdapter);
        }
    }

    //扫描返回的数据
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalScanBackData(botCode);
    }

    //验证扫描返回的数据
    private void verticalScanBackData(String botCode) {
        if (requestPresent != null) {
            String url = null;
            Map<String, Object> maps = new HashMap<>();
            maps.put("userId", getUserInfo().getData().getUser_id() + "");
            maps.put("bottleCode", botCode);
            if(flagCode == Module.story_niming_ziti){   //门店匿名自提，送重瓶
                url = UrlCode.STORY_ZITI_HBOT_TOCLIENT.getCheck();
            }else{
                url = UrlCode.getFlagCheck(flagCode);
            }
            requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
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
                            VoiceUtils.showVoice(StoryOrderActivity.this, R.raw.beep);
                            totlaList.clear();
                            showBuilder("下单成功!", true);
                            break;
                        case 3: //获取匿名客户信息
                            String data = response.get().getString("data");
                            if(data != null){
                                ClientBean clientBean = new Gson().fromJson(data,ClientBean.class);
                                if(clientBean != null){
                                    vercardShowTv.setText("客户正确-->" + clientBean.getClient_name());
                                    clientId = clientBean.getClientId()+"";
                                }
                            }
                            break;
                    }

                } else {
                    VoiceUtils.showToastVoice(StoryOrderActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                VoiceUtils.showToastVoice(StoryOrderActivity.this, R.raw.warning, "错误信息:" +response.responseCode()+ response.get().getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("---eroor==" + response.getException() + "--coce=" + response.responseCode());
        VoiceUtils.showToastVoice(StoryOrderActivity.this, R.raw.warning, "错误信息:" +response.responseCode()+ response.getException());
    }

    //适配
    private void adapterScanCode(int airBottleId, int airBottleTypeId, String botCode) {
        Logger.e("----适配===" + airBottleId + "--=" + airBottleTypeId + "--=" + botCode);
        if (Utils.isRepeatStrCodes(totlaList, airBottleId + "")) {
            VoiceUtils.showToastVoice(StoryOrderActivity.this, R.raw.warning, "重复气瓶!");
        } else {
            if (cond != null) {
                cond.dismiss();
            }
            if (airBotTypeId == -1) {
                airBotTypeId = airBottleTypeId;
                VoiceUtils.showVoice(StoryOrderActivity.this, R.raw.beep);
                mapCodeTypeId.put(botCode, 1);
                totlaList.add(airBottleId + "");
                mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
                mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
                wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
                mAdapter.notifyDataSetChanged();
            } else {
                if (airBotTypeId == airBottleTypeId) {
                    VoiceUtils.showVoice(StoryOrderActivity.this, R.raw.beep);
                    mapCodeTypeId.put(botCode, 1);
                    totlaList.add(airBottleId + "");
                    mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
                    mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
                    wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
                    mAdapter.notifyDataSetChanged();
                } else {
                    VoiceUtils.showToastVoice(StoryOrderActivity.this, R.raw.warning, "只能同一种气瓶类型!");
                }
            }

        }
    }

    private void showBuilder(String content, boolean show) {
        if (cond == null) {
            cond = new ConfirmDialog(StoryOrderActivity.this);
            cond.show();
            cond.setTitle("提醒");
            cond.setContent(content);
            cond.setBtnShowOrGone(show);
            cond.setCanceledOnTouchOutside(false);
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

    @OnClick({R.id.wearhouseSubBtn,R.id.commentTitleScanImg})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.wearhouseSubBtn:  //提交
                if (!Utils.isEmpty(clientId) && totlaList.size() > 0 && requestPresent != null) {
                    Map<String, Object> maps = new HashMap<>();
                    String url = null;
                    if(flagCode == Module.story_niming_ziti){   //门店匿名自提
                        url = UrlCode.STORY_ZITI_PLACEORDER.getUrl();
                    }else{
                        url = UrlCode.getFlagUrl(flagCode);
                    }
                    maps.put("userId", getUserInfo().getData().getUser_id() + "");
                    maps.put("clientId", clientId);
                    maps.put("airBottleTypeId", airBotTypeId + "");
                    maps.put("bottleIds", Utils.ListToStr(totlaList));
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(this, R.raw.warning, "请按步骤操作!");
                }
                break;
            case R.id.commentTitleScanImg:  //摄像头扫描
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("flagCode",(flagCode + 1));
                startActivityForResult(intent,flagCode+1);
                break;
        }

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
