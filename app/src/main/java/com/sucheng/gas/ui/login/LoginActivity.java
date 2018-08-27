package com.sucheng.gas.ui.login;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sucheng.gas.BuildConfig;
import com.sucheng.gas.R;
import com.sucheng.gas.base.EnvType;
import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.bean.UserBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.ui.HomeActivity;
import com.sucheng.gas.utils.AppUtils;
import com.sucheng.gas.utils.FileUtils;
import com.sucheng.gas.utils.SHA1Utils;
import com.sucheng.gas.utils.SharedPreferenceUtils;
import com.sucheng.gas.utils.UpdateChecker;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
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
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/24.
 */

public class LoginActivity extends CommentScanActivity implements RequestView<JSONObject> {


    @BindView(R.id.loginAccountEdit)
    TextInputEditText loginAccountEdit;
    @BindView(R.id.loginPwdEdit)
    TextInputEditText loginPwdEdit;
    @BindView(R.id.loginVersionTv)
    TextView loginVersionTv;
    @BindView(R.id.login_app_nameTv)
    TextView loginAppNameTv;

    private RequestPresent requestPresent;
    private RequestQueue requestQueue;
    private List<Integer> integerList;

    String uName = null;
    String uPwd = null;

    //检查更新
    private UpdateChecker updateChecker;

    private BottomSheetDialog bottomSheetDialog;

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initViews();
        initData();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!hasNfc(this)) {
            VoiceUtils.showToastVoice(this, R.raw.warning, "当前设备不支持NFC功能!");
        }
        Logger.e("-----packname="+getPackageName());
        Logger.e("----SHA1="+ SHA1Utils.sHA1(this));

    }

    public static boolean hasNfc(Context context) {
        boolean bRet = false;
        if (context == null)
            return bRet;
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // adapter存在，能启用
            bRet = true;
        }
        return bRet;
    }


    private void initViews() {
        loginVersionTv.setText("当前版本: v" + AppUtils.getVersionName(LoginActivity.this));
        String name = (String) SharedPreferenceUtils.get(LoginActivity.this, "username", "");
        String pwd = (String) SharedPreferenceUtils.get(LoginActivity.this, "userpwd", "");
        loginAppNameTv.setText(Constants.showAppName(LoginActivity.this));
        if (!Utils.isEmpty(name) && !Utils.isEmpty(pwd)) {
            loginAccountEdit.setText(name);
            loginPwdEdit.setText(pwd);
        }

//        //删除拍摄的图片
//        Logger.e("------图片地址="+Environment.getExternalStorageDirectory().getAbsoluteFile().getPath()
//                + "/MyPhoto");
//        FileUtils.deleteFile(Environment.getExternalStorageDirectory().getPath()
//                + "/MyPhoto");
//
//        FileUtils.deleteFile(Environment.getExternalStorageDirectory().getPath()
//                + "/MyPhoto/"+"1522310446602.jpg");

    }

    private void initData() {
        requestQueue = NoHttp.newRequestQueue(1);
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
        integerList = new ArrayList<>();

        int envType = BuildConfig.ENV_TYPE;
        Logger.e("---------eventType=" + envType);
        switch (envType) {
            case EnvType.DAOJIAO_GAS:   //道滘株洲燃气

                break;
            case EnvType.HENGYUAN_GAS:      //恒源燃气

                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateChecker = new UpdateChecker(LoginActivity.this);
        updateChecker.setCheckUrl(Constants.getAbsoluteUrl() + UrlCode.APP_UPDATE.getCheck() + AppUtils.getVersionCode(LoginActivity.this));
        updateChecker.checkForUpdates();
    }

    @OnClick({R.id.loginBtn, R.id.loginCopyTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginBtn: //登录
                uName = loginAccountEdit.getText().toString().trim();
                uPwd = loginPwdEdit.getText().toString().trim();
                String url = UrlCode.LOGINMOBILE_GETUSERLOGIN.getUrl();
                Logger.e("---url==" + url);
                if (!Utils.isEmpty(uName) && !Utils.isEmpty(uPwd)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", uName);
                    map.put("password", uPwd);
                    map.put("deviceCode", AppUtils.getDeviceId(LoginActivity.this) + "");
                    if (requestPresent != null) {
                        requestPresent.getPresentRequestJSONObject(requestQueue, 1, url, map, null);
                    }
                }
                break;
            case R.id.loginCopyTv:  //服务器选择
                View serView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.bot_item, null);
                TextView tv1 = (TextView) serView.findViewById(R.id.item1);
                TextView tv2 = (TextView) serView.findViewById(R.id.item2);
                TextView tv3 = (TextView) serView.findViewById(R.id.item3);
                TextView tv4 = (TextView) serView.findViewById(R.id.item4);
                tv1.setOnClickListener(new itemClick());
                tv2.setOnClickListener(new itemClick());
                tv3.setOnClickListener(new itemClick());
                tv4.setOnClickListener(new itemClick());
                bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
                bottomSheetDialog.setContentView(serView);
                bottomSheetDialog.show();
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
        Logger.e("----what=" + what + "---==" + response.get());
        if (what == 1) {
            UserBean userBean = new Gson().fromJson(response.get().toString(), UserBean.class);
            if (userBean != null && userBean.getCode() == 200) {
                //保存用户信息
                setPutUserInfo(userBean);
                SharedPreferenceUtils.put(LoginActivity.this, "username", uName);
                SharedPreferenceUtils.put(LoginActivity.this, "userpwd", uPwd);
                startActivity(HomeActivity.class);
                finish();
            } else {
                VoiceUtils.showToastVoice(LoginActivity.this, R.raw.warning, "错误返回:" + userBean.getCode() + userBean.getMsg());
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        VoiceUtils.showToastVoice(LoginActivity.this, R.raw.warning, "错误信息:" + response.responseCode() + response.getException().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestPresent.detach();
    }

    class itemClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item1:
                    MyApplication.code(0);
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.item2:
                    MyApplication.code(1);
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.item3:
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.item4:    //演示服务器
                    MyApplication.code(2);
                    bottomSheetDialog.dismiss();
                    break;
            }
        }
    }

    public long exitTime; // 储存点击退出时间

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    showToast(LoginActivity.this, "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                    return false;
                } else {
                    MyApplication.getMyApplication().removeAllActivity();
                    return true;
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
