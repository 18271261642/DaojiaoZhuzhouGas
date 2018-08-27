package com.sucheng.gas.ui.deliver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by Administrator on 2018/5/5.
 */

/**
 * 送气工从用户退回重瓶-输入订单编码后进入此页面
 * 门店从用户退回重瓶
 */
public class DeliverRebackHBotFromClientActivity extends CommentScanActivity implements RequestView<JSONObject> {


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

    private int flagCode;
    private RequestPresent requestPresent;
    private RequestQueue requestQueue;
    //订单编码
    private String orderCode = "";

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

        orderCode = getIntent().getStringExtra("orderCode");

    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        flagCode = getIntent().getIntExtra("flagCode", 0);
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("-------摄像头返回--="+requestCode+"--="+resultCode);
        //摄像头扫描返回
        if(requestCode == flagCode){
            if(resultCode == 0 && data != null){
                String scanData = data.getStringExtra("scanResult");
                if(!Utils.isEmpty(scanData)){
                    //摄像头扫描返回
                    verticalScanBackData(scanData);
                }
            }
        }
    }

    //扫描返回的数据
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalScanBackData(botCode);
    }

    //验证气瓶
    private void verticalScanBackData(String botCode) {
        if(requestPresent != null){
            String url = UrlCode.getFlagCheck(flagCode);
            Map<String,Object> maps = new HashMap<>();
            maps.put("userId",getUserInfo().getData().getUser_id()+"");
            maps.put("orderCode",orderCode);
            maps.put("bottleCode",botCode);
            requestPresent.getPresentRequestJSONObject(requestQueue,1,url,maps,botCode);

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
            mAdapter = new TestAdapter(DeliverRebackHBotFromClientActivity.this, listType, mapTypeCount);
            wearhouseistView.setAdapter(mAdapter);
        }
    }

    @OnClick({R.id.commentTitleScanImg, R.id.wearhouseSubBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commentTitleScanImg:  //摄像头扫描按钮
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("flagCode",flagCode+1);
                startActivityForResult(intent,flagCode);
                break;
            case R.id.wearhouseSubBtn:  //提交按钮
                if(totlaList.size()>0){
                    String url = UrlCode.getFlagUrl(flagCode);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("userId", getUserInfo().getData().getUser_id() + "");
                    maps.put("orderCode", orderCode);
                    maps.put("bottleIds", Utils.ListToStr(totlaList));
                    requestPresent.getPresentRequestJSONObject(requestQueue,2,url,maps,null);
                }
                break;
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
        Logger.e("------返回="+what+"--="+response.get().toString());
        if(response.responseCode() == 200){
            try {
                if(response.get().getInt("code") == 200){
                    switch (what){
                        case 0x01:  //验证气瓶
                            JSONObject jsonObject = response.get().getJSONObject("data");
                            adapterScanCode(jsonObject.getInt("airBottleId"), jsonObject.getInt("airBottleTypeId"), bot);
                            break;
                        case 0x02:  //提交成功
                            VoiceUtils.showVoice(DeliverRebackHBotFromClientActivity.this, R.raw.beep);
                            totlaList.clear();
                            showBuilder("提交成功!", true);
                            break;
                    }
                } else {
                    VoiceUtils.showToastVoice(DeliverRebackHBotFromClientActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                VoiceUtils.showToastVoice(DeliverRebackHBotFromClientActivity.this, R.raw.warning, "错误信息:" + response.get().getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("---eroor==" + response.getException() + "--coce=" + response.responseCode());
        VoiceUtils.showToastVoice(DeliverRebackHBotFromClientActivity.this, R.raw.warning, "错误信息:" + response.getException());
    }

    //适配
    private void adapterScanCode(int airBottleId, int airBottleTypeId, String botCode) {
        Logger.e("----适配===" + airBottleId + "--=" + airBottleTypeId + "--=" + botCode);
        if (Utils.isRepeatStrCodes(totlaList, airBottleId + "")) {
            VoiceUtils.showToastVoice(DeliverRebackHBotFromClientActivity.this, R.raw.warning, "重复气瓶!");
        } else {
            VoiceUtils.showVoice(DeliverRebackHBotFromClientActivity.this, R.raw.beep);
            mapCodeTypeId.put(botCode, 1);
            totlaList.add(airBottleId + "");
            mapCount.put(airBottleTypeId, mapCount.get(airBottleTypeId) + 1);
            mapTypeCount.put(mapType.get(airBottleTypeId), mapCount.get(airBottleTypeId));
            wearhousetotalcodesTv.setText(totlaList.size() + "瓶");
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showBuilder(String content, boolean show) {
        if (cond == null) {
            cond = new ConfirmDialog(DeliverRebackHBotFromClientActivity.this);
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
