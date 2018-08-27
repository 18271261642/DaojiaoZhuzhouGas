package com.sucheng.gas.ui;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sucheng.gas.R;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.base.CommentDataBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.ui.botmsg.InitBotActivity;
import com.sucheng.gas.ui.botmsg.InitBotCodeActivity;
import com.sucheng.gas.ui.botmsg.TrajectoryActivity;
import com.sucheng.gas.ui.deliver.DeliverOrderActivity;
import com.sucheng.gas.ui.login.LoginActivity;
import com.sucheng.gas.ui.login.ManualVersionUpdateActivity;
import com.sucheng.gas.ui.story.AddStoryFamilyCheckOrderActivity;
import com.sucheng.gas.ui.story.StoryFamilyCheckOrderActivity;
import com.sucheng.gas.ui.story.StoryOrderActivity;
import com.sucheng.gas.ui.wearhouse.WearhouseCheckBotActivity;
import com.sucheng.gas.ui.wearhouse.WearhouseOutActivity;
import com.sucheng.gas.utils.FileUtils;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sucheng.gas.constants.Constants.T50_BUILD;
import static com.sucheng.gas.utils.ErrorExectionUtils.showErrorMsg;

/**
 * Created by Administrator on 2018/1/24.
 */

public class HomeActivity extends BaseActivity implements RequestView<JSONObject> {

    @BindView(R.id.homeItem1View)
    CardView homeItem1View;
    @BindView(R.id.homeItem2View)
    CardView homeItem2View;
    @BindView(R.id.homeItem3View)
    CardView homeItem3View;
    //用户名称
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    //用户角色
    @BindView(R.id.userCharacterTv)
    TextView userCharacterTv;
    @BindView(R.id.homeItem4View)
    CardView homeItem4View;
    @BindView(R.id.homeItem5View)
    CardView homeItem5View;
    @BindView(R.id.homeItem6View)
    CardView homeItem6View;
    @BindView(R.id.homeItem7View)
    CardView homeItem7View;
    @BindView(R.id.homeItem8View)
    CardView homeItem8View;
    @BindView(R.id.homeItem9View)
    CardView homeItem9View;
    @BindView(R.id.homeItem10View)
    CardView homeItem10View;
    @BindView(R.id.homeItem11View)
    CardView homeItem11View;
    @BindView(R.id.homeItem12View)
    CardView homeItem12View;
    @BindView(R.id.homeItem13View)
    CardView homeItem13View;
    @BindView(R.id.homeItem14View)
    CardView homeItem14View;
    @BindView(R.id.homeItem15View)
    CardView homeItem15View;
    @BindView(R.id.homeItem16View)
    CardView homeItem16View;
    @BindView(R.id.homeItem17View)
    CardView homeItem17View;
    @BindView(R.id.homeItem18View)
    CardView homeItem18View;
    @BindView(R.id.homeItem19View)
    CardView homeItem19View;
    @BindView(R.id.homeItem20View)
    CardView homeItem20View;
    @BindView(R.id.homeItem21View)
    CardView homeItem21View;
    @BindView(R.id.homeItem22View)
    CardView homeItem22View;
    @BindView(R.id.homeItem23View)
    CardView homeItem23View;
    @BindView(R.id.homeItem24View)
    CardView homeItem24View;
    @BindView(R.id.homeItem25View)
    CardView homeItem25View;
    @BindView(R.id.homeItem26View)
    CardView homeItem26View;
    @BindView(R.id.homeItem27View)
    CardView homeItem27View;
    @BindView(R.id.homeItem28View)
    CardView homeItem28View;
    @BindView(R.id.homeItem29View)
    CardView homeItem29View;
    @BindView(R.id.homeItem30View)
    CardView homeItem30View;
    @BindView(R.id.homeItem31View)
    CardView homeItem31View;

    private RequestPresent requestPressent;
    private List<Integer> listId;// 权限id

    private RequestQueue requestQueue;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.e("-------删除DCIM--" + Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/");
            String foldPath = Environment.getExternalStorageDirectory().getPath()
                    + "/DCIM/Camera/";
            if(foldPath != null){
                File[] imgFiles = new File(foldPath).listFiles();
                if(imgFiles != null){
                    if (imgFiles.length > 0) {
                        for (File f : imgFiles) {
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            ContentResolver mContentResolver = getContentResolver();
                            String where = MediaStore.Images.Media.DATA + "='" + f.getAbsolutePath() + "'";
                            //删除图片
                            mContentResolver.delete(uri, where, null);
                        }
                    }
                    FileUtils.deleteAllFiles(new File(foldPath));

                    //更新媒体库
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent mediaScanIntent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(new File("file://" + Environment.getExternalStorageDirectory()));
                        mediaScanIntent.setData(contentUri);
                        sendBroadcast(mediaScanIntent);
                    } else {
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://"
                                        + Environment.getExternalStorageDirectory())));
                    }
                }


            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initData();
        addItemViews();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isContanis(Constants.ht380kBuild, Build.PRODUCT) || Build.PRODUCT.equals(T50_BUILD)) {
            handler.sendEmptyMessage(0x01);
        }
    }

    private void initData() {
        requestQueue = NoHttp.newRequestQueue();
        requestPressent = new RequestPresent();
        requestPressent.attach(this);
        listId = new ArrayList<>();
        listId.addAll(getPermissList());
        userNameTv.setText(getResources().getString(R.string.user_name) + getUserInfo().getData().getUsername());
        userCharacterTv.setText(getResources().getString(R.string.user_character) + getUserInfo().getData().getFull_name());
    }

    private void addItemViews() {
        // 如果有给账号分配权限模块，添加模块视图
        if (listId != null && listId.size() > 0) {
            CardView rlId = null;
            for (int moduleId : listId) {
                System.out.println(moduleId);
                rlId = getMaps().get(moduleId);
                if (rlId == null) {
                    continue;
                }
                getMaps().get(moduleId).setVisibility(View.VISIBLE);// 根据权限加载模块
            }

        } else {
            VoiceUtils.showToastVoice(HomeActivity.this, R.raw.warning, "当前无权限!");
        }
    }

    public Map<Integer, CardView> getMaps() {
        Map<Integer, CardView> itemMap = new HashMap<>();
        itemMap.put(10001, homeItem1View);
        itemMap.put(10002, homeItem2View);
        itemMap.put(10003, homeItem3View);
        itemMap.put(10004, homeItem4View);
        itemMap.put(10005, homeItem5View);
        itemMap.put(10006, homeItem6View);
        itemMap.put(10007, homeItem7View);
        itemMap.put(10008, homeItem8View);
        itemMap.put(10009, homeItem9View);
        itemMap.put(10010, homeItem10View);
        itemMap.put(10011, homeItem11View);
        itemMap.put(10012, homeItem12View);
        itemMap.put(10013, homeItem13View);
        itemMap.put(10014, homeItem14View);
        itemMap.put(10015, homeItem15View);
        itemMap.put(10016, homeItem16View);
        itemMap.put(10017, homeItem17View);
        itemMap.put(10018, homeItem18View);
        itemMap.put(10019, homeItem19View);
        itemMap.put(10020, homeItem20View);
        itemMap.put(10021, homeItem21View);
        itemMap.put(10022, homeItem22View);
        itemMap.put(10023, homeItem23View);
        itemMap.put(10024, homeItem24View);
        itemMap.put(10025, homeItem25View);


        itemMap.put(10042, homeItem26View);
        itemMap.put(10043, homeItem27View);
        itemMap.put(10044, homeItem28View);

        itemMap.put(10048, homeItem29View);
        itemMap.put(10049, homeItem30View);

        itemMap.put(10051, homeItem31View);
        return itemMap;
    }


    @OnClick({R.id.userLoginOutTv, R.id.homeItem1View, R.id.homeItem2View,
            R.id.homeItem3View, R.id.homeItem4View, R.id.homeItem5View, R.id.homeItem6View,
            R.id.homeItem7View, R.id.homeItem8View, R.id.homeItem9View, R.id.homeItem10View,
            R.id.homeItem11View, R.id.homeItem12View, R.id.homeItem13View, R.id.homeItem14View,
            R.id.homeItem15View, R.id.homeItem16View, R.id.homeItem17View, R.id.homeItem18View,
            R.id.homeItem19View, R.id.homeItem20View, R.id.homeItem21View, R.id.homeItem22View,
            R.id.homeItem23View, R.id.homeItem24View, R.id.homeItem25View, R.id.homeItem26View,
            R.id.homeItem27View,R.id.homeItem28View,R.id.homeItem29View,R.id.homeItem30View,
            R.id.homeItem31View})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userLoginOutTv:   //注销
                setPromptDialogListener("提示", "是否退出登录?", new OnPromptDialogListener() {
                    @Override
                    public void leftClick(int code) {

                    }

                    @Override
                    public void rightClick(int code) {
                        logoutClick();
                    }
                });
                break;
            case R.id.homeItem1View:    //初始化气瓶
                startActivity(InitBotActivity.class);
                break;
            case R.id.homeItem2View:    //气瓶轨迹
                startIntActivity(TrajectoryActivity.class, "flagCode", UrlCode.BOTMSG_PRODUCE.getCode());
                break;
            case R.id.homeItem3View:    //手动检测更新
                startActivity(ManualVersionUpdateActivity.class);
                break;
            case R.id.homeItem4View:    //强制空瓶入库-仓库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.WEARHOUSE_FORCE_EMPBOTIN.getCode());
                break;
            case R.id.homeItem5View:    //重瓶出库给配送工-仓库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.WEARHOUSE_BOT_TO__DELIVER.getCode());
                break;
            case R.id.homeItem6View:    //从配送工退回重瓶-仓库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.WEARHOUSE_REBACKHEAVYBOTFROM_DELIVER.getCode());
                break;
            case R.id.homeItem7View:    //从配送工空瓶入库-仓库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.WEARHOUSE_EPMTYBOT_TO_WEARHOUSE.getCode());
                break;
            case R.id.homeItem8View:    //充装重瓶入库-仓库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.WEARHOUSE_BOT_TO_FILLING.getCode());
                break;
            case R.id.homeItem9View:    //我的未派送单
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.DELIVER_MYNOSEND_ORDER.getCode());
                break;
            case R.id.homeItem10View:   //送气工我的未回单
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.DELIVER_MYNOREBACK_ORDER.getCode());
                break;
            case R.id.homeItem11View:   //门店重瓶入库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.STORE_HEAVYBOT_INOUT.getCode());
                break;
            case R.id.homeItem12View:   //门店空瓶出库
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.STORY_EMPTYBOT_TO_DELIVER.getCode());
                break;
            case R.id.homeItem13View:   //门店重瓶出库给送气工
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.STORY_HEAVYBOT_OUTTODEVE.getCode());
                break;
            case R.id.homeItem14View:   //门店从送气工回收空瓶
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.STORY_REBACKEMPTYBOT_FROMDEVE.getCode());
                break;
            case R.id.homeItem15View:   //门店自提订单下单
                startIntActivity(StoryOrderActivity.class, "flagCode", UrlCode.STORY_ZITI_PLACEORDER.getCode());
                break;
            case R.id.homeItem16View:   //门店自提订单
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.STORY_ZITI_ORDER.getCode());
                break;
            case R.id.homeItem17View:   //门店自提订单回单
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.STORY_ZITI_BACKORDER.getCode());
                break;
            case R.id.homeItem18View:       //门店匿名自提
                startIntActivity(StoryOrderActivity.class, "flagCode", UrlCode.STORYANONY_ZITI.getCode());
                break;
            case R.id.homeItem19View:   //仓库匿名强制充装
                startIntActivity(StoryOrderActivity.class, "flagCode", UrlCode.WEARHOUSE_NIMING_CHOGNZHUANG.getCode());
                break;
            case R.id.homeItem20View:   //仓库匿名自提下单
                startIntActivity(StoryOrderActivity.class, "flagCode", UrlCode.WEARHOUSE_NIMING_ZITI.getCode());
                break;
            case R.id.homeItem21View:   //仓库自提订单列表
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.WEARHOUSE_ZITI_ORDERLIST.getCode());
                break;
            case R.id.homeItem22View:   //仓库自提订单回单列表
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.WEARHOUSE_ZITI_BAC_ORDER_LIST.getCode());
                break;
            case R.id.homeItem23View:   //送气工从用户退回重瓶
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.DELIVER_REBACK_HEAVYBOT_FROM_CLIENT.getCode());
                break;
            case R.id.homeItem24View:   //门店从用户退回重瓶
                startIntActivity(DeliverOrderActivity.class, "flagCode", UrlCode.STORY_REBACK_HEAVYBOT_FROM_CLIENT.getCode());
                break;
            case R.id.homeItem25View:   //门店从送气工退回重瓶
                startIntActivity(WearhouseOutActivity.class, "flagCode", UrlCode.STORY_REBACK_HEAVYBOT_FROM_DELIVER.getCode());
                break;
            case R.id.homeItem26View:   //仓库送检出库
                startIntActivity(WearhouseCheckBotActivity.class, "flagCode", Module.wearhouse_check_bot_outhouse);
                break;
            case R.id.homeItem27View:   //仓库送检入库
                startIntActivity(WearhouseCheckBotActivity.class, "flagCode", Module.wearhouse_check_bot_inhouse);
                break;
            case R.id.homeItem28View:   //门店未派送单
                startIntActivity(DeliverOrderActivity.class, "flagCode", Module.story_no_send_order);
                break;
            case R.id.homeItem29View:   //门店入户安检拍照
                startIntActivity(AddStoryFamilyCheckOrderActivity.class,"flagCode",Module.story_in_family_check_photo);
                break;
            case R.id.homeItem30View:   //门店入户安检订单列表
                startIntActivity(StoryFamilyCheckOrderActivity.class,"falgCode",Module.story_in_family_order);
                break;
            case R.id.homeItem31View:   //初始化二维码
                startActivity(InitBotCodeActivity.class);
                break;


        }
    }

    @Override
    public void showLoadDialog(int what) {
        if (what == 1) {
            showWatiDialog();
        }
    }

    @Override
    public void closeLoadDialog(int what) {
        if (what == 1) {
            closeWatiDialog();
        }
    }

    @Override
    public void requestSuccessData(int what, Response<JSONObject> response, String bot) {
        Logger.e("-----home----=" + response.toString() + "---=" + response.responseCode());
        if (response != null && response.responseCode() == 200) {
            CommentDataBean commData = new Gson().fromJson(response.get().toString(), CommentDataBean.class);
            if (what == 1) {  //退出登录
                if (commData != null && commData.getCode() == 200) {
                    startActivity(LoginActivity.class);
                    HomeActivity.this.finish();
                }
            }
        }

    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        VoiceUtils.showToastVoice(HomeActivity.this, R.raw.warning, showErrorMsg(response.getException(), HomeActivity.this));
    }

    //返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            setPromptDialogListener("提示", "是否退出登录?", new OnPromptDialogListener() {
                @Override
                public void leftClick(int code) {

                }

                @Override
                public void rightClick(int code) {
                    logoutClick();
                }

            });
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void logoutClick() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getAbsoluteUrl() + UrlCode.LOGINMOBILE_GETUSERLOGIN.getCheck(), RequestMethod.POST);
        jsonObjectRequest.add("userId", getUserInfo().getData().getUser_id() + "");
        requestQueue.add(1, jsonObjectRequest, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                showLoadDialog(what);
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                closeLoadDialog(what);
                Logger.e("-----home----=" + response.toString() + "---=" + response.responseCode());
                if (response != null && response.responseCode() == 200) {
                    CommentDataBean commData = new Gson().fromJson(response.get().toString(), CommentDataBean.class);
                    if (what == 1) {  //退出登录
                        if (commData != null && commData.getCode() == 200) {
                            startActivity(LoginActivity.class);
                            HomeActivity.this.finish();
                        }
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                closeLoadDialog(what);
                VoiceUtils.showToastVoice(HomeActivity.this, R.raw.warning, showErrorMsg(response.getException(), HomeActivity.this));
            }

            @Override
            public void onFinish(int what) {
                closeLoadDialog(what);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPressent.detach();
    }

}
