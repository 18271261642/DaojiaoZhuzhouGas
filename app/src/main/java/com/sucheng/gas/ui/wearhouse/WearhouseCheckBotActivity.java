package com.sucheng.gas.ui.wearhouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.TestAdapter;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.scan.CommentScanActivity;
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
 * Created by Administrator on 2018/6/7.
 */

/**
 * 仓库送检入库，出库
 */
public class WearhouseCheckBotActivity extends CommentScanActivity implements RequestView<JSONObject>{


    @BindView(R.id.commentTitleBackImg)
    ImageView commentTitleBackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    @BindView(R.id.wearhouseistView)
    ListView wearhouseistView;
    @BindView(R.id.wearhousetotalcodesTv)
    TextView wearhousetotalcodesTv;
    @BindView(R.id.vercardShowTv)
    TextView vercardShowTv;
    @BindView(R.id.wearhouseNoBotSubBtn)
    Button wearhouseNoBotSubBtn;
    @BindView(R.id.wearhouseSubBtn)
    Button wearhouseSubBtn;

    private ConfirmDialog cond;

    //打开就显示的列表 适配
    private Map<Integer, Integer> mapCount;// 数量
    private Map<Integer, String> mapType;// 类型
    private Map<String, Integer> mapTypeCount;// 类型的数量
    private Map<String, Integer> mapCodeTypeId;// 气瓶码对应的气瓶id
    private List<String> listType;
    private TestAdapter mAdapter;
    //需要提交的数据，
    private List<String> totlaList;

    RequestQueue requestQueue;
    RequestPresent requestPresent;

    private int flagCode ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearhouse_out);
        ButterKnife.bind(this);

        initViews();
        flagCode = getIntent().getIntExtra("flagCode",0);
        initData();

        showBuilder("请扫描气瓶编码",false);
    }

    private void initData() {
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
        requestQueue = NoHttp.newRequestQueue();
        requestPresent = new RequestPresent();
        requestPresent.attach(this);

        mapCount = new HashMap<>();
        mapType = new HashMap<>();
        mapTypeCount = new HashMap<>();
        mapCodeTypeId = new HashMap<>();
        listType = new ArrayList<>();
        totlaList = new ArrayList<>();
    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        wearhouseNoBotSubBtn.setVisibility(View.GONE);

    }

    //扫描返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        if(!Utils.isEmpty(botCode)){
            verticalScanData(botCode);
        }
    }

    private void verticalScanData(String scanBot){
        if(requestPresent != null){
            Map<String,Object> maps = new HashMap<>();
            maps.put("userId", getUserInfo().getData().getUser_id()+"");
            maps.put("bottleCode",scanBot);
            requestPresent.getPresentRequestJSONObject(requestQueue,1,UrlCode.getFlagCheck(flagCode),maps,scanBot);
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
            mAdapter = new TestAdapter(WearhouseCheckBotActivity.this, listType, mapTypeCount);
            wearhouseistView.setAdapter(mAdapter);
        }

    }

    @OnClick({R.id.commentTitleScanImg, R.id.wearhouseSubBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commentTitleScanImg:  //摄像头扫描
                Intent intent = new Intent(WearhouseCheckBotActivity.this, CaptureActivity.class);
                intent.putExtra("flagCode", flagCode);
                startActivityForResult(intent,flagCode);
                break;
            case R.id.wearhouseSubBtn:  //提交
                if(totlaList.size()>0 && requestPresent != null){
                    Map<String,Object> mp = new HashMap<>();
                    mp.put("userId",getUserInfo().getData().getUser_id()+"");
                    mp.put("bottleCodes",Utils.ListToStr(totlaList));
                    requestPresent.getPresentRequestJSONObject(requestQueue,2,UrlCode.getFlagUrl(flagCode),mp,"11");
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("----requestCode="+requestCode+"--resultCode="+resultCode);
        if(requestCode == flagCode){
            if(data != null){
                String scanData = data.getStringExtra("scanResult");
                Logger.e("----scanData="+scanData);
                if(!Utils.isEmpty(scanData))
                    verticalScanData(scanData);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPresent.detach();
    }

    @Override
    public void showLoadDialog(int what) {
        showWatiDialog();
    }

    @Override
    public void closeLoadDialog(int what) {

    }

    @Override
    public void requestSuccessData(int what, Response<JSONObject> response, String bot) {
        Logger.e("------succ="+what+"--res="+response.get().toString());
        closeWatiDialog();
        try {
            if(response.get().getInt("code") == 200){
                if(what == 1){  //验证气瓶返回
                    JSONObject jsonObject = response.get().getJSONObject("data");
                    adapterScanCode(jsonObject.getInt("airBottleId"), jsonObject.getInt("airBottleTypeId"), bot);
                }else if(what == 2){    //提交返回
                    VoiceUtils.showVoice(WearhouseCheckBotActivity.this,R.raw.beep);
                    showBuilder("提交成功!",true);

                }
            }else{
                VoiceUtils.showToastVoice(WearhouseCheckBotActivity.this,R.raw.warning,"非200返回:"+response.get().getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        closeWatiDialog();
        Logger.e("----fail="+response.getException().toString());
        VoiceUtils.showToastVoice(WearhouseCheckBotActivity.this,R.raw.warning,"错误返回:"+response.getException().toString());
    }

    //添加至列表中
    private void adapterScanCode(int airBottleId, int airBottleTypeId, String botCode) {
        Logger.e("----适配===" + airBottleId + "--=" + airBottleTypeId + "--=" + botCode);
        if (Utils.isRepeatStrCodes(totlaList, airBottleId + "")) {
            VoiceUtils.showToastVoice(WearhouseCheckBotActivity.this, R.raw.warning, "重复气瓶!");
        } else {
            if (cond != null) {
                cond.dismiss();
            }
            VoiceUtils.showVoice(WearhouseCheckBotActivity.this, R.raw.beep);
            mapCodeTypeId.put(botCode, 1);
            totlaList.add(botCode + "");
            mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
            mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
            wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showBuilder(String content, boolean show) {
        if (cond == null) {
            cond = new ConfirmDialog(WearhouseCheckBotActivity.this);
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
