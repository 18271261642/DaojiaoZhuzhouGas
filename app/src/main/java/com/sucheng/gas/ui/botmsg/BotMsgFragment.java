package com.sucheng.gas.ui.botmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sucheng.gas.R;
import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.bean.AirBotInfoBean;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/25.
 */

/**
 * 显示气瓶信息
 */
public class BotMsgFragment extends Fragment implements RequestView<JSONObject> {

    View msgView;
//    @BindView(R.id.commentTitleTv)
//    TextView commentTitleTv;

    Unbinder unbinder;
    RequestPresent requestPresent;
    RequestQueue request;
    //气瓶编码
    @BindView(R.id.tv_bottleinfo_airbottlecode)
    TextView tvBottleinfoAirbottlecode;
    //气瓶钢印码
    @BindView(R.id.tv_bottleinfo_bottlesealcode)
    TextView tvBottleinfoBottlesealcode;
    //物料类型
    @BindView(R.id.tv_bottleinfo_bottlespecifications)
    TextView tvBottleinfoBottlespecifications;
    //出厂编号
    @BindView(R.id.tv_bottleinfo_bottleweight)
    TextView tvBottleinfoBottleweight;
    //生产单位
    @BindView(R.id.tv_bottleinfo_productionunitname)
    TextView tvBottleinfoProductionunitname;
    //生产日期
    @BindView(R.id.tv_bottleinfo_createtime)
    TextView tvBottleinfoCreatetime;
    //检修日期
    @BindView(R.id.tv_bottleinfo_checktime)
    TextView tvBottleinfoChecktime;
    //下次检修日期
    @BindView(R.id.tv_bottleinfo_nextchecktime)
    TextView tvBottleinfoNextchecktime;
    //归属单位
    @BindView(R.id.tv_bottleinfo_belong_nameTv)
    TextView tvBottleinfoBelongNameTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.sucheng.gas.ui.botmsg.action"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        msgView = inflater.inflate(R.layout.fragment_botmsg, container, false);
        unbinder = ButterKnife.bind(this, msgView);

        initViews();
        initData();
        return msgView;
    }

    private void initData() {
        request = NoHttp.newRequestQueue(2);
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
    }

    private void initViews() {
        //commentTitleTv.setText("气瓶信息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(broadcastReceiver);
        requestPresent.detach();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                String botCode = intent.getStringExtra("botCode");
                if (!Utils.isEmpty(botCode)) {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("bottleCode", botCode);
                    maps.put("userId", Utils.getUserInfo(getActivity()).getData().getUser_id() + "");
                    requestPresent.getPresentRequestJSONObject(request, 1, UrlCode.BOTMSG_BOTTRAJECORY.getUrl(), maps, botCode);
                }
            }
        }
    };

    @Override
    public void showLoadDialog(int what) {

    }

    @Override
    public void closeLoadDialog(int what) {

    }

    @Override
    public void requestSuccessData(int what, Response<JSONObject> response, String bot) {
        Logger.e("-----气瓶信息===" + response.get().toString() + "--=" + bot + "==" + response.responseCode());
        if (response.responseCode() == 200) {
            AirBotInfoBean airBotInfoBean = new Gson().fromJson(response.get().toString(), AirBotInfoBean.class);
            if (airBotInfoBean.getCode() == 200 && airBotInfoBean.getData() != null && !airBotInfoBean.equals("null")) {
                AirBotInfoBean.DataBean dataBean = airBotInfoBean.getData();
                if (dataBean != null) {
                    tvBottleinfoAirbottlecode.setText("" + dataBean.getAir_bottle_code());
                    tvBottleinfoBottlesealcode.setText("" + dataBean.getAir_bottle_seal_code());
                    tvBottleinfoBottlespecifications.setText(""+dataBean.getAir_bottle_type_name());
                    tvBottleinfoBelongNameTv.setText(""+dataBean.getAir_bottle_belong_name());
                    tvBottleinfoBottleweight.setText("" + dataBean.getFactory_number());
                    tvBottleinfoProductionunitname.setText("" + dataBean.getProduction_unit_name());
                    tvBottleinfoCreatetime.setText("" + Utils.longToDate(dataBean.getProduce_time()));
                    tvBottleinfoChecktime.setText("" + Utils.longToDate(dataBean.getCheck_time()));
                    tvBottleinfoNextchecktime.setText("" + Utils.longToDate(dataBean.getNext_check_time()));

                }
            } else {
                VoiceUtils.showToastVoice(getActivity(), R.raw.warning, "错误信息:" + airBotInfoBean.getMsg());
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        VoiceUtils.showToastVoice(getActivity(), R.raw.warning, "错误信息:" + response.responseCode() + response.getException().toString());
    }
}
