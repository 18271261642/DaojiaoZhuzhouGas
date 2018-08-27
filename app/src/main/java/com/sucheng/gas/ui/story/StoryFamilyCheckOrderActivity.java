package com.sucheng.gas.ui.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.StoryFamilyCheckAdapter;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.bean.FamilyCheckOrderBean;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.ItemClickListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/17.
 */

public class StoryFamilyCheckOrderActivity extends BaseActivity implements RequestView<JSONObject> ,ItemClickListener{


    @BindView(R.id.commentTitleBackImg)
    ImageView commentTitleBackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    @BindView(R.id.storyFamilyCheckRecy)
    RecyclerView storyFamilyCheckRecy;

    private RequestQueue requestQueue;
    private RequestPresent requestPresent;

    private List<FamilyCheckOrderBean> list;
    private StoryFamilyCheckAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_family_order);
        ButterKnife.bind(this);

        initViews();

        initData();



    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderData();
    }

    private void getOrderData() {
        if (requestPresent != null) {
            Map<String, Object> mps = new HashMap<>();
            mps.put("userId", getUserInfo().getData().getUser_id() + "");
            requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.GET_STORY_FAMILY_CHECK_ORDER.getUrl(), mps, "");
        }
    }

    private void initData() {
        requestQueue = NoHttp.newRequestQueue(1);
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        storyFamilyCheckRecy.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new StoryFamilyCheckAdapter(list, this);
        storyFamilyCheckRecy.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestPresent != null)
            requestPresent.detach();
    }

    private void initViews() {
        commentTitleTv.setText("门店入户安检订单");
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
        closeWatiDialog();
        Logger.e("-----succ-=" + response.get().toString());
        try {
            if (response.get().getInt("code") == 200) {
                String data = response.get().getString("data");
                if (!Utils.isEmpty(data) && !data.equals("[]")) {
                    List<FamilyCheckOrderBean> tmpLt = new Gson().fromJson(data,new TypeToken<List<FamilyCheckOrderBean>>(){}.getType());
                    list.addAll(tmpLt);
                    adapter.notifyDataSetChanged();

                }else{
                    list.clear();
                    adapter.notifyDataSetChanged();
                    showToast(this,"无数据!");
                }
            } else {
                VoiceUtils.showToastVoice(StoryFamilyCheckOrderActivity.this, R.raw.warning, "非200返回:" + response.get().getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        closeWatiDialog();
        VoiceUtils.showToastVoice(StoryFamilyCheckOrderActivity.this,R.raw.warning,"错误信息:"+response.getException().toString());
        Logger.e("-----err-=" + response.getException().toString());

    }

    @Override
    public void itemPosition(int position) {
        Intent intent = new Intent(this,AddStoryFamilyCheckOrderActivity.class);
        intent.putExtra("clientName",list.get(position).getClient_name());
        intent.putExtra("flagCode", Module.story_in_family_order);
        //intent.putExtra("client_id",list.get(position).getClient_id()+"");
        intent.putExtra("orderId",list.get(position).getFamily_check_id()+"");
        startActivity(intent);
    }
}
