package com.sucheng.gas.ui.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.DeliverOrderAdapter;
import com.sucheng.gas.bean.DeliverOrderBean;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.ItemClickListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
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

/**
 * Created by Administrator on 2018/1/27.
 */

public class DeliverOrderActivity extends CommentScanActivity implements RequestView<JSONObject>,ItemClickListener{


    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.deliverOrderRecyclerView)
    RecyclerView deliverOrderRecyclerView;

    private List<DeliverOrderBean> list;
    private DeliverOrderAdapter adapter;
    private RequestPresent requestPressent;
    private RequestQueue requestQueue;

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
        if(requestPressent != null){
            Map<String,Object> maps = new HashMap<>();
            maps.put("userId",""+ getUserInfo().getData().getUser_id());
            requestPressent.getPresentRequestJSONObject(requestQueue,1, UrlCode.getFlagUrl(flagCode),maps,null);
        }
    }

    private void initDatas() {
        requestQueue = NoHttp.newRequestQueue(1);
        requestPressent = new RequestPresent();
        requestPressent.attach(this);
    }

    private void initViews() {
        flagCode = getIntent().getIntExtra("flagCode",0);
        commentTitleTv.setText(UrlCode.getFlagDes(flagCode));
        LinearLayoutManager ling = new LinearLayoutManager(this);
        ling.setOrientation(LinearLayoutManager.VERTICAL);
        deliverOrderRecyclerView.setLayoutManager(ling);
        list = new ArrayList<>();
        adapter = new DeliverOrderAdapter(DeliverOrderActivity.this,list);
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
        if(response.responseCode() == 200){
            try {
                if(response.get().getInt("code") == 200){
                    String data = response.get().getString("data");
                    if(data != null && !data.equals("[]")){
                        List<DeliverOrderBean> tempLsit = new Gson().fromJson(data,new TypeToken<List<DeliverOrderBean>>(){}.getType());
                        list.clear();
                        list.addAll(tempLsit);
                        adapter.notifyDataSetChanged();
                    }else{
                        list.clear();
                        adapter.notifyDataSetChanged();
                        VoiceUtils.showToast(this,"当前无订单!");
                    }

                }else{
                    VoiceUtils.showToastVoice(this,R.raw.warning,"返回信息:"+response.get().getString("msg"));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPressent.detach();
    }

    @Override
    public void itemPosition(int position) {
        Intent intent = new Intent(this,DeliverSendBotActivity.class);
        intent.putExtra("flagCode",flagCode);
        intent.putExtra("clientId",list.get(position).getClient_id()+"");   //客户ID
        intent.putExtra("orderId",list.get(position).getId()+"");
        startActivity(intent);
    }
}
