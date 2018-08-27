package com.sucheng.gas.ui.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.DeliverOrderAdapter;
import com.sucheng.gas.bean.DeliverOrderBean;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.ItemClickListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.InputOrderCodeDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

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

public class DeliverOrderActivity extends CommentScanActivity implements RequestView<JSONObject>, ItemClickListener {


    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.deliverOrderRecyclerView)
    RecyclerView deliverOrderRecyclerView;
    //输入订单编码的按钮
    @BindView(R.id.rebackInputOrderCodeBtn)
    Button rebackInputOrderCodeBtn;

    private List<DeliverOrderBean> list;
    private DeliverOrderAdapter adapter;
    private RequestPresent requestPressent;
    private RequestQueue requestQueue;
    private InputOrderCodeDialog inputOrderCodeDialog;

    private int flagCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order);
        ButterKnife.bind(this);

        initViews();

        initDatas();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestPressent != null) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("userId", "" + getUserInfo().getData().getUser_id());
            switch (flagCode){
                case Module.deliver_reback_heavy_bot_fromclient:  //送气工从用户退回重瓶
                    rebackInputOrderCodeBtn.setVisibility(View.VISIBLE);
                    requestPressent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.DELIVER_MYNOREBACK_ORDER.getUrl(), maps, null);
                    break;
                case Module.story_reback_heavybot_from_client:  //门店从用户退重瓶
                    rebackInputOrderCodeBtn.setVisibility(View.VISIBLE);
                    requestPressent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORY_ZITI_BACKORDER.getUrl(), maps, null);
                    break;
                case Module.story_no_send_order:    //门店未派送单
                    rebackInputOrderCodeBtn.setVisibility(View.GONE);
                    requestPressent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.STORY_NO_SEND_ORDER_LIST.getUrl(), maps, null);
                    break;
                    default:
                        rebackInputOrderCodeBtn.setVisibility(View.GONE);
                        requestPressent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.getFlagUrl(flagCode), maps, null);
                        break;

            }

        }
    }

    private void initDatas() {
        requestQueue = NoHttp.newRequestQueue(1);
        requestPressent = new RequestPresent();
        requestPressent.attach(this);
    }

    private void initViews() {
        flagCode = getIntent().getIntExtra("flagCode", 0);
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
        LinearLayoutManager ling = new LinearLayoutManager(this);
        ling.setOrientation(LinearLayoutManager.VERTICAL);
        deliverOrderRecyclerView.setLayoutManager(ling);
        list = new ArrayList<>();
        adapter = new DeliverOrderAdapter(DeliverOrderActivity.this, list);
        deliverOrderRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
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
        if (response.responseCode() == 200) {
            try {
                if (response.get().getInt("code") == 200) {
                    String data = response.get().getString("data");
                    if (data != null && !data.equals("[]")) {
                        List<DeliverOrderBean> tempLsit = new Gson().fromJson(data, new TypeToken<List<DeliverOrderBean>>() {
                        }.getType());
                        list.clear();
                        list.addAll(tempLsit);
                        adapter.notifyDataSetChanged();
                    } else {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        VoiceUtils.showToast(this, "当前无订单!");
                    }

                } else {
                    VoiceUtils.showToastVoice(this, R.raw.warning, "返回信息:" + response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        VoiceUtils.showToastVoice(this, R.raw.warning, "错误信息:" + response.getException().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPressent.detach();
    }

    @Override
    public void itemPosition(int position) {
        if(flagCode != Module.story_no_send_order){
            Intent intent = new Intent(this, DeliverSendBotActivity.class);
            intent.putExtra("flagCode", flagCode);
            intent.putExtra("air_bottle_specifications",list.get(position).getAir_bottle_specifications()); //气瓶类型
            intent.putExtra("clientId", list.get(position).getClient_id() + "");   //客户ID
            intent.putExtra("orderId", list.get(position).getId() + "");
            startActivity(intent);
        }
    }

    //输入订单编码
    @OnClick(R.id.rebackInputOrderCodeBtn)
    public void onViewClicked() {
        inputOrderCodeDialog = new InputOrderCodeDialog(DeliverOrderActivity.this);
        inputOrderCodeDialog.show();
        inputOrderCodeDialog.setInputTitle("提示");
        inputOrderCodeDialog.setHitData("请输入订单编码");
        inputOrderCodeDialog.setOrderListener(new InputOrderCodeDialog.GetInputOrderCodeListener() {
            @Override
            public void getInputData(String inputD) {
                inputOrderCodeDialog.dismiss();
                Intent intent = new Intent(DeliverOrderActivity.this, DeliverRebackHBotFromClientActivity.class);
                intent.putExtra("flagCode", flagCode);
                intent.putExtra("orderCode", inputD+"");    //订单编码
                startActivity(intent);
            }
        });

    }
}
