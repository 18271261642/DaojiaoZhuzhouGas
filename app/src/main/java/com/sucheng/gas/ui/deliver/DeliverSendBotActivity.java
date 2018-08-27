package com.sucheng.gas.ui.deliver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.adapter.TestAdapter;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.scan.ReadClientCardActivity;
import com.sucheng.gas.utils.BmapLocalUtils;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.ConfirmDialog;
import com.sucheng.gas.view.PromptDialog;
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
 * 送气工-我的未派送单
 * 送气工-我的未回单
 * 门店自提订单回单
 */
public class DeliverSendBotActivity extends CommentScanActivity implements RequestView<JSONObject> {

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.wearhouseistView)
    ListView wearhouseistView;
    @BindView(R.id.wearhousetotalcodesTv)
    TextView wearhousetotalcodesTv;
    @BindView(R.id.vercardShowTv)
    TextView vercardShowTv;
    //无瓶回单按钮
    @BindView(R.id.wearhouseNoBotSubBtn)
    Button wearhouseNoBotSubBtn;
    //扫描按钮
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    private RequestPresent requestPresent;
    private RequestQueue requestQueue;

    private PromptDialog prod;

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

    //定位
    private BmapLocalUtils bmapLocalUtils;
    private String latitude = "";   //纬度string
    private String longitude = "";   //经度string

    private Map<String,Integer> vertMap;
    int orderTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearhouse_out);
        ButterKnife.bind(this);

        initViews();

        initData();

        initLocals();




    }

    private void initLocals() {
        bmapLocalUtils = new BmapLocalUtils();
        bmapLocalUtils.initLocal(DeliverSendBotActivity.this);
        bmapLocalUtils.setBmapLocalListener(new BmapLocalUtils.BmapLocalListener() {
            @Override
            public void getLocalLatLong(double lat, double lon) {
                Logger.e("---lat=" + lat + "-=" + lon);
                latitude = lat + "";
                longitude = lon + "";
            }
        });
        bmapLocalUtils.startLocal();

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

        vertMap = new HashMap<>();

        if (Constants.isReadClientCard()) {
            readClientCard();   //读取用户卡
        } else {
            clientId = getIntent().getStringExtra("clientId") + "";
        }

    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        flagCode = getIntent().getIntExtra("flagCode", 0);
//        clientId = getIntent().getStringExtra("clientId") + "";
        switch (flagCode) {
            case Module.deliver_my_nosend_orders:   //送气工-我的未派送单-派送重瓶给客户
                commentTitleTv.setText("配送重瓶给客户");
                wearhouseNoBotSubBtn.setVisibility(View.GONE);
                break;
            case Module.deliver_my_noback_orders:   //送气工--我的未回单-从客户回收空瓶
                commentTitleTv.setText("从用户回收空瓶");
                wearhouseNoBotSubBtn.setVisibility(View.VISIBLE);
                break;
            case Module.story_ziti_order_return_order:  //门店自提订单回单-从用户回收空瓶
                commentTitleTv.setText("收空瓶");
                wearhouseNoBotSubBtn.setVisibility(View.VISIBLE);
                break;
            case Module.story_ziti_ordrs_list:      //门店自提订单送重瓶-送重瓶给用户
                commentTitleTv.setText("自提订单送重瓶");
                wearhouseNoBotSubBtn.setVisibility(View.VISIBLE);
                break;
            case Module.wearhouse_ziti_return_order:    //仓库自提订单回单-从用户回收空瓶
                commentTitleTv.setText("收空瓶");
                wearhouseNoBotSubBtn.setVisibility(View.VISIBLE);
                break;
            case Module.wearhouse_ziti_order_list:      //仓库自提订单--送重瓶
                commentTitleTv.setText("送重瓶");
                wearhouseNoBotSubBtn.setVisibility(View.GONE);
                break;
            case Module.deliver_reback_heavy_bot_fromclient: //送气工从用户退回重瓶
                commentTitleTv.setText("送气工从用户退回重瓶");
                wearhouseNoBotSubBtn.setVisibility(View.GONE);
                break;
            case Module.story_reback_heavybot_from_client:  //门店从用户退回重瓶
                commentTitleTv.setText("门店从用户退回重瓶");
                wearhouseNoBotSubBtn.setVisibility(View.GONE);
                break;
            default:
                commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
                wearhouseNoBotSubBtn.setVisibility(View.GONE);
                break;
        }

    }

    private void readClientCard() {
        Intent intent = new Intent(this, ReadClientCardActivity.class);
        intent.putExtra("flagCode", flagCode);
        startActivityForResult(intent, flagCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == flagCode) {  //读卡返回
            if (resultCode == flagCode) {
                Logger.e("-----======" + data.getStringExtra("client_name") + "--=" + data.getStringExtra("clientId"));
                vercardShowTv.setText("客户正确-->" + data.getStringExtra("client_name"));
                clientId = data.getStringExtra("clientId");
            }
        }

        //摄像头扫描返回
        if (requestCode == (flagCode + 1)) {
            if (resultCode == 0 && data != null) {
                String scanData = data.getStringExtra("scanResult");
                if (!Utils.isEmpty(scanData)) {
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
            Logger.e("-----1----="+addBottleTypeBean.toString());
            vertMap.put(addBottleTypeBean.getAir_bottle_specifications(),addBottleTypeBean.getId());
            mapCount.put(addBottleTypeBean.getId(), 0);
            mapType.put(addBottleTypeBean.getId(), addBottleTypeBean.getAir_bottle_specifications());
            mapTypeCount.put(addBottleTypeBean.getAir_bottle_specifications(), mapCount.get(addBottleTypeBean.getId()));
            listType.add(addBottleTypeBean.getAir_bottle_specifications());
            mAdapter = new TestAdapter(DeliverSendBotActivity.this, listType, mapTypeCount);
            wearhouseistView.setAdapter(mAdapter);
        }

        String orderType = getIntent().getStringExtra("air_bottle_specifications");
        Logger.e("-----orderType="+orderType);
        //对应类型id
        orderTypeId = vertMap.get(orderType);
        Logger.e("------ordetypeid="+orderTypeId);
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
            switch (flagCode) {
                case Module.deliver_my_nosend_orders:   //送气工-我的未派送单-派送重瓶给客户
                    url = UrlCode.DELIVER_SENDHEAVYBOT_TOCLIENT.getCheck();
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    break;
                case Module.deliver_my_noback_orders:   //送气工--我的未回单-从客户回收空瓶
                    url = UrlCode.DELIVER_REBACK_EMPTYBOT_FROMGCLIENT.getCheck();
                    maps.put("clientId", clientId); //客户Id 从订单获取
                    maps.put("orderId", getIntent().getStringExtra("orderId") + "");
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    break;
                case Module.story_ziti_order_return_order:  //门店自提订单回单-从用户回收空瓶
                    if (clientId != null) {
                        url = UrlCode.STORY_ZITI_BACKORDER_EMPTYBOT.getCheck();
                        maps.put("clientId", clientId);
                        maps.put("orderId", getIntent().getStringExtra("orderId"));
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    } else {
                        VoiceUtils.showToastVoice(this, R.raw.warning, "请按步骤操作!");
                    }
                    break;
                case Module.story_ziti_ordrs_list:      //门店自提订单送重瓶-送重瓶给用户
                    if (clientId != null) {
                        url = UrlCode.STORY_ZITI_HBOT_TOCLIENT.getCheck();
                        maps.put("clientId", clientId);
                        maps.put("orderId", getIntent().getStringExtra("orderId"));
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    }
                    break;
                case Module.wearhouse_ziti_return_order:    //仓库自提订单回单-从用户回收空瓶
                    if (clientId != null) {
                        url = UrlCode.WEARHOUSE_ZITI_RECYCLEEMPBOT.getCheck();
                        maps.put("clientId", clientId);
                        maps.put("orderId", getIntent().getStringExtra("orderId"));
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    }
                    break;
                case Module.wearhouse_ziti_order_list:      //仓库自提订单--送重瓶
                    if (clientId != null) {
                        url = UrlCode.WEARHOUSE_HEAVYBOTTO_CLIENT.getCheck();
                        maps.put("clientId", clientId);
                        maps.put("orderId", getIntent().getStringExtra("orderId"));
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    }
                    break;
                case Module.deliver_reback_heavy_bot_fromclient:    //送气工从用户退回重瓶
                    url = UrlCode.DELIVER_REBACK_HEAVYBOT_FROM_CLIENT.getCheck();
                    maps.put("orderId", getIntent().getStringExtra("orderId"));
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    break;
                case Module.story_reback_heavybot_from_client:      //门店从用户退回重瓶
                    url = UrlCode.STORY_REBACK_HEAVYBOT_FROM_CLIENT.getCheck();
                    maps.put("orderId", getIntent().getStringExtra("orderId"));
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, botCode);
                    break;
                default:

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
                            VoiceUtils.showVoice(DeliverSendBotActivity.this, R.raw.beep);
                            totlaList.clear();
                            showBuilder("提交成功!", true);
                            break;
                    }

                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("---eroor==" + response.getException() + "--coce=" + response.responseCode());
        VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "错误信息:" + response.getException());
    }

    //适配
    private void adapterScanCode(int airBottleId, int airBottleTypeId, String botCode) {
        Logger.e("----适配===" + airBottleId + "--=" + airBottleTypeId + "--=" + botCode);
        if(orderTypeId != -1 && orderTypeId == airBottleTypeId){
            if (Utils.isRepeatStrCodes(totlaList, airBottleId + "")) {
                VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "重复气瓶!");
            } else {
                if (cond != null) {
                    cond.dismiss();
                }
                VoiceUtils.showVoice(DeliverSendBotActivity.this, R.raw.beep);
                mapCodeTypeId.put(botCode, 1);
                totlaList.add(airBottleId + "");
                mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
                mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
                wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
                mAdapter.notifyDataSetChanged();
            }
        }else{
            VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "与订单气瓶类型不匹配!");
        }
    }

    private void showBuilder(String content, boolean show) {
        if (cond == null) {
            cond = new ConfirmDialog(DeliverSendBotActivity.this);
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

    @OnClick({R.id.wearhouseSubBtn, R.id.wearhouseNoBotSubBtn, R.id.commentTitleScanImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wearhouseSubBtn:  //提交
                subOrderData();
                break;
            case R.id.commentTitleScanImg:  //摄像头扫描
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("flagCode", (flagCode + 1));
                startActivityForResult(intent, (flagCode + 1));
                break;
            case R.id.wearhouseNoBotSubBtn: //无瓶回单
                prod = new PromptDialog(this);
                prod.show();
                prod.setTitle("提醒");
                prod.setContent("是否无瓶回单?");
                prod.setleftText("否");
                prod.setrightText("是");
                prod.setListener(new OnPromptDialogListener() {
                    @Override
                    public void leftClick(int code) {
                        prod.dismiss();

                    }

                    @Override
                    public void rightClick(int code) {
                        prod.dismiss();
                        noBotSubData();
                    }
                });
                break;
        }
    }

    //无瓶回单
    private void noBotSubData() {
        if (requestPresent != null) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("userId", getUserInfo().getData().getUser_id() + "");
            maps.put("orderId", getIntent().getStringExtra("orderId"));
            maps.put("bottleIds", "");
            String url = null;
            if (flagCode == Module.deliver_my_noback_orders) {  //送气工从客户回收空瓶
                url = UrlCode.DELIVER_REBACK_EMPTYBOT_FROMGCLIENT.getUrl();
                maps.put("clientId", clientId);
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
            } else if (flagCode == Module.story_ziti_order_return_order) {   //门店自提订单回单
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.STORY_ZITI_BACKORDER_EMPTYBOT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "请按步骤操作!");
                }
            } else if (flagCode == Module.wearhouse_ziti_return_order) {     //仓库自提订单回单
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.WEARHOUSE_ZITI_RECYCLEEMPBOT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "请按步骤操作!");
                }
            } else if (flagCode == Module.wearhouse_ziti_order_list) {   //仓库自提订单送重瓶-提交
                if (clientId != null) {
                    url = UrlCode.WEARHOUSE_HEAVYBOTTO_CLIENT.getCheck();
                    maps.put("clientId", clientId);
                    maps.put("orderId", getIntent().getStringExtra("orderId"));
                    requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, maps, null);
                }
            }
        }

    }

    //提交操作
    private void subOrderData() {
        if (totlaList.size() > 0 && requestPresent != null) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("userId", getUserInfo().getData().getUser_id() + "");
            maps.put("orderId", getIntent().getStringExtra("orderId"));
            maps.put("bottleIds", Utils.ListToStr(totlaList));
            String url = null;
            if (flagCode == Module.deliver_my_noback_orders) {  //送气工从客户回收空瓶
                url = UrlCode.DELIVER_REBACK_EMPTYBOT_FROMGCLIENT.getUrl();
                maps.put("clientId", clientId);
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
            } else if (flagCode == Module.deliver_my_nosend_orders) { //送气工送重瓶给客户
                url = UrlCode.DELIVER_SENDHEAVYBOT_TOCLIENT.getUrl();
                maps.put("clientId", clientId);
                maps.put("latitude", latitude);
                maps.put("longitude", longitude);
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
            } else if (flagCode == Module.story_ziti_order_return_order) {  //门店自定订单回空瓶提交
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.STORY_ZITI_BACKORDER_EMPTYBOT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "客户ID为空!");
                }

            } else if (flagCode == Module.story_ziti_ordrs_list) {   //门店自提订单--重瓶出库给用户
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.STORY_ZITI_HBOT_TOCLIENT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "客户ID为空!");
                }
            } else if (flagCode == Module.wearhouse_ziti_order_list) {   //仓库自提订单-提交操作
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.WEARHOUSE_HEAVYBOTTO_CLIENT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "客户ID为空!");
                }
            } else if (flagCode == Module.wearhouse_ziti_return_order) {  //仓库自提订单回单--提交操作
                if (!Utils.isEmpty(clientId)) {
                    url = UrlCode.WEARHOUSE_ZITI_RECYCLEEMPBOT.getUrl();
                    maps.put("clientId", clientId);
                    requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
                } else {
                    VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "客户ID为空!");
                }
            } else if (flagCode == Module.deliver_reback_heavy_bot_fromclient) {       //送气工从用户退回重瓶
                url = UrlCode.DELIVER_REBACK_HEAVYBOT_FROM_CLIENT.getUrl();
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
            }else if(flagCode == Module.story_reback_heavybot_from_client){     //门店从用户退回重瓶
                url = UrlCode.STORY_REBACK_HEAVYBOT_FROM_CLIENT.getUrl();
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, null);
            }

            else {
                VoiceUtils.showToastVoice(this, R.raw.warning, "待完成");
            }

        } else {
            VoiceUtils.showToastVoice(DeliverSendBotActivity.this, R.raw.warning, "请按步骤操作!");
        }
    }

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (cond != null) {
                    cond.dismiss();
                }
            }
            return true;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (bmapLocalUtils != null) {
            bmapLocalUtils.stopLocal();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bmapLocalUtils != null) {
            bmapLocalUtils.destoryLocal();
        }
    }
}
