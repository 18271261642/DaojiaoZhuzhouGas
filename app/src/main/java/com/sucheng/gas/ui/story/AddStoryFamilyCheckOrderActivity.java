package com.sucheng.gas.ui.story;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sucheng.gas.R;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.bean.ClientBean;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.Module;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.scan.ReadClientCardActivity;
import com.sucheng.gas.ui.botmsg.InitBotActivity;
import com.sucheng.gas.utils.FileUtils;
import com.sucheng.gas.utils.MultipartRequest;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.view.ConfirmDialog;
import com.sucheng.gas.view.PromptDialog;
import com.yanzhenjie.nohttp.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/17.
 */

public class AddStoryFamilyCheckOrderActivity extends CommentScanActivity {


    private static int TAKE_PHOTO_CODE1 = 1001;
    @BindView(R.id.commentTitleBackImg)
    ImageView commentTitleBackImg;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    @BindView(R.id.verticalClientTv)
    TextView verticalClientTv;
    @BindView(R.id.takePtoTv)
    TextView takePtoTv;

    private PromptDialog promptDialog;
    private ConfirmDialog confirmDialog;

    int flagCode;
    private String clientId = "";

    private Uri photoUri;
    private String filePath;
    private String filePath1 = "";
    private List<File> photoFileList ;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_check_order);
        ButterKnife.bind(this);

        initViews();

        initData();


    }

    //扫描返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalClient(botCode);
    }

    private void verticalClient(String botCode) {
        showWatiDialog();
        String clientUrl = Constants.getAbsoluteUrl()+UrlCode.FINDCLIENT_MSG_BYCARD.getUrl()+"?cardCode="+botCode;
        StringRequest stringRequest = new StringRequest(clientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.e("----验证客户="+response);
                closeWatiDialog();
                if(!Utils.isEmpty(response)){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getInt("code") == 200){
                            if(confirmDialog != null){
                                confirmDialog.dismiss();
                            }
                            VoiceUtils.showVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.beep);
                            String data = jsonObject.getString("data");
                            if(!Utils.isEmpty(data) && !data.equals("[]")){
                                ClientBean clientBean = new Gson().fromJson(data,ClientBean.class);
                                verticalClientTv.setText("客户验证正确->>"+clientBean.getClient_name());
                                clientId = clientBean.getClientId()+"";
                            }
                        }else{
                            VoiceUtils.showToastVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.warning,""+jsonObject.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e("----验证客户error="+error.getMessage());
                closeWatiDialog();
                VoiceUtils.showToastVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.warning,"错误信息:"+error.getMessage().toString());

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void initData() {
        flagCode = getIntent().getIntExtra("flagCode",0);
        photoFileList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        if(flagCode == Module.story_in_family_order){   //选订单进入
            verticalClientTv.setText("客户正确->>"+getIntent().getStringExtra("clientName"));
            verticalClientTv.setClickable(false);
        }else{
            confiDialogs("请扫描客户编码",false);
        }
    }

    private void initViews() {
        commentTitleTv.setText("门店入户安检订单");
    }

    //读取客户信息
    private void readClientCard(){
        Intent intent = new Intent(this, ReadClientCardActivity.class);
        intent.putExtra("flagCode", flagCode);
        startActivityForResult(intent, flagCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = FileUtils.compressBySize(filePath, 480, 800);
                try {
                    photoFileList.add(FileUtils.saveFiles(bitmap, filePath));
                    filePath1 = filePath;
                    takePtoTv.setText("已拍照");
                    alertSub();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @OnClick({R.id.verticalClientTv, R.id.takePtoTv,R.id.addSubBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verticalClientTv: //验证用户
                confiDialogs("请扫描客户编码",false);
                break;
            case R.id.takePtoTv:    //拍照
                takePhotoFile();
                break;
            case R.id.addSubBtn:    //提交
                submitData();
                break;
        }
    }

    private void takePhotoFile() {
        //判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            photoUri = pathUri(System.currentTimeMillis() + "");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PHOTO_CODE1);
        }else{
            VoiceUtils.showToastVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.warning,"当前SD卡不可用!");
        }
    }

    // 图片保存
    private Uri pathUri(String fileName) {
        String strPhotoName = fileName + ".jpg";
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/MyPhoto/";
        String fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/";
        Logger.e("----图片保存路径="+savePath+"--="+fpath);
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePath = savePath + strPhotoName;
        Logger.e("-----filePath="+filePath);
        return Uri.fromFile(new File(dir, strPhotoName));
    }

    private void submitData(){

        if(!Utils.isEmpty(filePath1)){

            String url =null;
            Map<String,String> uploadmap = new HashMap<>();
            if(flagCode ==  Module.story_in_family_order){  //门店入户安检订单编辑
                url =  Constants.getAbsoluteUrl()+ UrlCode.STORY_FAMILY_CHECK_PHOTO.getUrl();
                uploadmap.put("orderId",getIntent().getStringExtra("orderId"));
            }else{  //添加订单 拍照
                url =  Constants.getAbsoluteUrl()+ UrlCode.GET_STORY_FAMILY_CHECK_ORDER.getCheck();
                if(!Utils.isEmpty(clientId)){
                    uploadmap.put("clientId",clientId);
                }else{
                    VoiceUtils.showToastVoice(this,R.raw.warning,"请按步骤操作!");
                    return;
                }

            }
            showWatiDialog();
            uploadmap.put("userId",getUserInfo().getData().getUser_id()+"");

            MultipartRequest multipartRequest = new MultipartRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.e("-----response="+response);
                    closeWatiDialog();
                    if(!Utils.isEmpty(response)){
                        if(Utils.getJSONCode(response) == 200){
                            VoiceUtils.showVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.beep);
                            confiDialogs("提交成功!",true);
                        }else{
                            VoiceUtils.showToastVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.warning,""+Utils.getJSONMSG(response));
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    closeWatiDialog();
                    Logger.e("-----error="+error.getMessage());
                    VoiceUtils.showToastVoice(AddStoryFamilyCheckOrderActivity.this,R.raw.warning,"错误信息:"+error.getMessage().toString());
                }
            },"f_file[]", photoFileList, uploadmap);
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(multipartRequest);

        }else{
            VoiceUtils.showToastVoice(this,R.raw.warning,"请按步骤操作!");
        }
    }

    private void alertSub(){
        promptDialog = new PromptDialog(this);
        promptDialog.show();
        promptDialog.setTitle("提醒");
        promptDialog.setContent("是否提交?");
        promptDialog.setleftText("否");
        promptDialog.setrightText("是");
        promptDialog.setListener(new OnPromptDialogListener() {
            @Override
            public void leftClick(int code) {
                promptDialog.dismiss();

            }

            @Override
            public void rightClick(int code) {
                promptDialog.dismiss();
                submitData();
            }
        });
    }

    private void confiDialogs(String contxt,boolean isShow){
        confirmDialog = new ConfirmDialog(this);
        confirmDialog.show();
        confirmDialog.setTitle("提醒");
        confirmDialog.setContent(contxt);
        confirmDialog.setBtnShowOrGone(isShow);
        confirmDialog.setBtnText("确定");
        confirmDialog.setListener(new ConfirmDialog.ClickListener() {
            @Override
            public void doDismiss() {
                confirmDialog.dismiss();
                finish();
            }
        });

    }
}
