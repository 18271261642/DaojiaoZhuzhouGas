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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.SimpleTreeAdapter;
import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.bean.AirBotInfoBean;
import com.sucheng.gas.bean.Bean;
import com.sucheng.gas.bean.BotInfoBean;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.GetFragmentDataInterface;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.tree.TreeListViewAdapter;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/25.
 */

/**
 * 显示气瓶轨迹
 */
public class TrajectoryFragment extends Fragment implements RequestView<JSONObject>{

    View traView;
//    @BindView(R.id.commentTitleTv)
//    TextView commentTitleTv;
    Unbinder unbinder;


    private List<Bean> mDatas;
    @BindView(R.id.traject_listView)
    ListView mTree;

    private TreeListViewAdapter mAdapter;

    List<BotInfoBean> botList;
    private RequestPresent requestPresent;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("com.sucheng.gas.ui.botmsg.action"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        traView = inflater.inflate(R.layout.fragment_bot_trajectory, container, false);
        unbinder = ButterKnife.bind(this, traView);

        initViews();
        initDatas();

        return traView;
    }

    private void initDatas() {
        requestQueue = NoHttp.newRequestQueue(1);
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
        mDatas = new ArrayList<>();
    }

    private void initViews() {
        //commentTitleTv.setText("气瓶轨迹");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(broadcastReceiver);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action != null){
                Logger.e("-----传递的数据=="+intent.getStringExtra("botCode"));
                String botCode = intent.getStringExtra("botCode");
                if(!Utils.isEmpty(botCode) && requestPresent != null){
                    if (mAdapter!=null) {
                        mDatas.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    Map<String,Object> maps = new HashMap<>();
                    maps.put("bottleCode",botCode);
                    maps.put("userId",""+ Utils.getUserInfo(getActivity()).getData().getUser_id());
                    requestPresent.getPresentRequestJSONObject(requestQueue,1, UrlCode.BOTMSG_BOTTRAJECORY.getUrl(),maps,botCode);
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
        Logger.e("------轨迹==="+response.get().toString());
        if (response.responseCode() == 200) {
            AirBotInfoBean airBotInfoBean = new Gson().fromJson(response.get().toString(), AirBotInfoBean.class);
            if (airBotInfoBean.getCode() == 200 && airBotInfoBean.getData() != null && !airBotInfoBean.equals("null")) {
                AirBotInfoBean.DataBean dataBean = airBotInfoBean.getData();
                if(dataBean != null){
                    VoiceUtils.showVoice(getActivity(),R.raw.beep);
                    botList = dataBean.getMobileAirBottleTrackingRecords();
                    showTreeView(botList);
                }
            }else{
                VoiceUtils.showToastVoice(getActivity(),R.raw.warning,"错误信息:"+airBotInfoBean.getMsg());
            }
        }
    }

    private void showTreeView(List<BotInfoBean> botList) {
        for (int j = botList.size() - 1; j >= 0; j--) {
            if(botList.get(j).getCreate_time() > 0){
                mDatas.add(new Bean(j + 1, 0, Utils.longToDate(botList.get(j).getCreate_time())+ " "+ botList.get(j).getState_description()));
            }
            if(botList.get(j).getCreate_time() > 0){
                mDatas.add(new Bean(botList.size()+j + 2, j + 1, "具体时间:"+Utils.longToDate(botList.get(j).getCreate_time())));
            }

            if (botList.get(j).getOperator_name() != null) {
                mDatas.add(new Bean(botList.size() + j + 2, j + 1, "操作员:"
                        + botList.get(j).getOperator_name()));
            }
            if (botList.get(j).getSecond_category_name() != null) {
                mDatas.add(new Bean(botList.size() + j + 2, j + 1, "对应门店:"
                        + botList.get(j).getSecond_category_name()));
            }

            if (botList.get(j).getDelivery_man_name() != null) {
                mDatas.add(new Bean(botList.size() + j + 2, j + 1, "对应配送工:"
                        + botList.get(j).getDelivery_man_name()));
            }

            if (botList.get(j).getWarehouse_name() != null) {
                mDatas.add(new Bean(botList.size() + j + 2, j + 1, "对应仓库:"
                        + botList.get(j).getWarehouse_name()));
            }

            if (botList.get(j).getClient_name() != null) {
                mDatas.add(new Bean(botList.size() + j + 2 + 10* botList.size(), j + 1, "对应客户:"+ botList.get(j).getClient_name()));
            }

            if (mDatas == null || mDatas.size() == 0) {
                VoiceUtils.showToast(getActivity(), "未查询到轨迹信息，请重新获取");
                return;
            }
            try {
                mAdapter = new SimpleTreeAdapter<Bean>(mTree,
                        getActivity(), mDatas, 0);
                mTree.setAdapter(mAdapter);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("-----轨迹===="+response.getException());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestPresent.detach();
    }
}
