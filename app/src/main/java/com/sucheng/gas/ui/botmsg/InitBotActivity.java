package com.sucheng.gas.ui.botmsg;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sucheng.gas.R;
import com.sucheng.gas.adapter.AddBottleTypeAdapter;
import com.sucheng.gas.adapter.BotBelongListAdapter;
import com.sucheng.gas.adapter.DetectionAdapter;
import com.sucheng.gas.adapter.ProductAdapter;
import com.sucheng.gas.bean.AddBottleTypeBean;
import com.sucheng.gas.bean.DetectionUnit;
import com.sucheng.gas.bean.ProductUni;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.interfacepack.OnPromptDialogListener;
import com.sucheng.gas.scan.CommentScanActivity;
import com.sucheng.gas.utils.ErrorExectionUtils;
import com.sucheng.gas.utils.FileUtils;
import com.sucheng.gas.utils.MultipartRequest;
import com.sucheng.gas.utils.Utils;
import com.sucheng.gas.utils.VoiceUtils;
import com.sucheng.gas.utils.http.view.RequestPresent;
import com.sucheng.gas.utils.http.view.RequestView;
import com.sucheng.gas.view.TimeDialogView;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.CaptureActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/24.
 */

/**
 * 气瓶初始化
 */
public class InitBotActivity extends CommentScanActivity implements RequestView<JSONObject> {

    private static final int SCAN_BY_CAMERA_CODE = 1111;    //相机扫描
    private static final int TAKE_PHOTO_CODE1 = 1001;       //拍照二维码
    private static final int TAKE_PHOTO_CODE2 = 1002;       //拍照瓶身

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    //显示气瓶编码
    @BindView(R.id.initbottleCodeTv)
    TextView initbottleCodeTv;
    //输入气瓶钢印码
    @BindView(R.id.initbottleHardCode)
    EditText initbottleHardCode;
    //气瓶类型下拉
    @BindView(R.id.initbottleTypeSpinner)
    Spinner initbottleTypeSpinner;
    //设计使用年限
    @BindView(R.id.initbottlecycleTv)
    EditText initbottlecycleTv;
    //生产单位下拉
    @BindView(R.id.initbottleProComSpinner)
    Spinner initbottleProComSpinner;
    //检测单位下拉
    @BindView(R.id.initbottleDetection)
    Spinner initbottleDetection;
    //拍照1
    @BindView(R.id.initfile1Tv)
    TextView initfile1Tv;
    //拍照2
    @BindView(R.id.initfile2Tv)
    TextView initfile2Tv;
    //出厂日期
    @BindView(R.id.initbottleOutEdit)
    EditText initbottleOutEdit;
    //检修日期
    @BindView(R.id.initbottleServiceEdit)
    EditText initbottleServiceEdit;
    //下次检修日期
    @BindView(R.id.initbottleNextServiceEdit)
    EditText initbottleNextServiceEdit;
    //出厂编号
    @BindView(R.id.initbottleFactory)
    EditText initbottleFactory;

    //门店列表下拉
    @BindView(R.id.initbottleStorySpinner)
    Spinner initbottleStorySpinner;


    //门店所属单位下拉
    @BindView(R.id.initbottleBelongSpinner)
    Spinner initbottleBelongSpinner;
    //扫描图片
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;
    private int botBelongId = 0;  //气瓶所属单位ID
    private List<Map> botBelongList;
    private BotBelongListAdapter belongAdapter;

    private RequestQueue requestQueue;
    private RequestPresent requestPresent;

    private Calendar calendar;

    //检测单位集合
    private List<DetectionUnit> detcList;
    private DetectionAdapter detcAapter;
    private int detection_unit_id = 0; //检修单位ID

    //生产厂家
    private List<ProductUni> productList;
    private ProductAdapter productAdapter;
    private int production_unit_id = -1;    //生产厂家Id

    //气瓶类型
    private AddBottleTypeAdapter typeadapter;  //类型下拉列表适配器
    private List<AddBottleTypeBean> bottypeList;  //集合
    private int air_bottle_type_id = 0;    //气瓶类型id

    private Uri photoUri;
    String filePath, filePath1, filePath2;
    private List<File> photoFileList = new ArrayList<>();
    private String bottleCode = null;//气瓶编码
    com.android.volley.RequestQueue volleyRequestQueue;

    //日期选择弹窗
    private TimeDialogView timeDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);

        initViews();

        initData();

    }

    //扫描返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        verticalScanBackData(botCode);

    }

    private void initData() {
        requestPresent = new RequestPresent();
        requestPresent.attach(this);
        requestQueue = NoHttp.newRequestQueue(8);
        volleyRequestQueue = Volley.newRequestQueue(InitBotActivity.this);
        if (requestPresent != null) {
            //获取检测单位列表
            requestPresent.getPresentRequestJSONObject(requestQueue, 1, UrlCode.BOTMSG_PRODUCE.getCheck(), new HashMap<String, Object>(), null);
            //获取气瓶类型
            requestPresent.getPresentRequestJSONObject(requestQueue, 3, UrlCode.BOTMSG_BOTTRAJECORY.getCheck(), new HashMap<String, Object>(), null);
            //气瓶生产厂家
            requestPresent.getPresentRequestJSONObject(requestQueue, 4, UrlCode.BOTMSG_PRODUCE.getUrl(), new HashMap<String, Object>(), null);
            //归属单位
            requestPresent.getPresentRequestJSONObject(requestQueue, 5, UrlCode.BOTMSG_GETSTORYLIST.getCheck(), new HashMap<String, Object>(), null);

        }

        calendar = Calendar.getInstance();
    }

    private void initViews() {
        commentTitleScanImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText("气瓶初始化");
        //钢印码补前3位
        initbottleHardCode.setText(Constants.initHeavyBotCode());
        clearFocus();

    }

    //清除焦点
    private void clearFocus() {
        initbottleFactory.setFocusable(false);
        initbottleFactory.setFocusableInTouchMode(false);
        initbottleFactory.clearFocus();
        initbottleHardCode.setFocusable(false);
        initbottleHardCode.setFocusableInTouchMode(false);
        initbottleHardCode.clearFocus();
        initbottlecycleTv.setFocusable(false);
        initbottlecycleTv.setFocusableInTouchMode(false);
        initbottlecycleTv.clearFocus();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestPresent != null) {
            requestPresent.detach();
        }
    }

    @Override
    public void showLoadDialog(int what) {
        if (what == 2) {
            showWatiDialog();
        }
    }

    @Override
    public void closeLoadDialog(int what) {
        if (what == 2) {
            closeWatiDialog();
        }
    }

    @Override
    public void requestSuccessData(int what, Response<JSONObject> response, String bot) {
        Logger.e("-----d-----=" + response.get() + "----" + response.responseCode());
        if (response.responseCode() == 200) {
            JSONObject jsonObject = null;
            try {
                jsonObject = response.get();
                if (jsonObject.getInt("code") == 200) {
                    if (what == 1) {    //获取检测单位列表
                        analysisCheckPro(jsonObject.getString("data"));
                    } else if (what == 2) { //验证气瓶是否合法
                        VoiceUtils.showVoice(InitBotActivity.this, R.raw.beep);
                        initbottleCodeTv.setText(bot);
                        bottleCode = bot;
                        //出厂编号获取焦点
                        initbottleHardCode.setFocusable(true);
                        initbottleHardCode.setFocusableInTouchMode(true);
                        initbottleHardCode.requestFocus();
                        initbottlecycleTv.setFocusable(true);
                        initbottlecycleTv.setFocusableInTouchMode(true);
                        initbottlecycleTv.requestFocus();
                        initbottleFactory.setFocusable(true);
                        initbottleFactory.setFocusableInTouchMode(true);
                        initbottleFactory.requestFocus();

                    } else if (what == 3) { //获取气瓶类型
                        analysisBotTypeData(jsonObject.getString("data"));
                    } else if (what == 4) {  //气瓶生产厂家
                        analysisProductData(jsonObject.getString("data"));
                    } else if (what == 5) {  //获取气瓶所属单位列表
                        analysisBotBelongData(jsonObject.getString("data"));
                    }
                } else {
                    VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "111" + jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, response.get().getString("msg") + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestFailedData(int what, Response<JSONObject> response) {
        Logger.e("------初始化===" + response.getException() + "---=" + response.responseCode());
        VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "错误信息:" + response.responseCode() + response.getException());
    }

    //所属单位列表
    private void analysisBotBelongData(String data) {
        String aa = JSON.toJSONString(data);
        Logger.e("------data=" + data + "--=aa=" + aa);
        botBelongList = JSON.parseArray(data, Map.class);
        Logger.e("--------归属单位=" + botBelongList.toString());
        belongAdapter = new BotBelongListAdapter(botBelongList, InitBotActivity.this);
        initbottleBelongSpinner.setAdapter(belongAdapter);
        initbottleBelongSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                botBelongId = (int) botBelongList.get(position).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                botBelongId = (int) botBelongList.get(0).get("id");
            }
        });
    }

    //生产厂家
    private void analysisProductData(String data) {
        productList = new Gson().fromJson(data, new TypeToken<List<ProductUni>>() {
        }.getType());
        productAdapter = new ProductAdapter(productList, InitBotActivity.this);
        initbottleProComSpinner.setAdapter(productAdapter);
        initbottleProComSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                production_unit_id = productList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                production_unit_id = productList.get(0).getId();
            }
        });

    }

    //气瓶类型
    private void analysisBotTypeData(String data) {
        bottypeList = new Gson().fromJson(data, new TypeToken<List<AddBottleTypeBean>>() {
        }.getType());
        typeadapter = new AddBottleTypeAdapter(bottypeList, InitBotActivity.this);
        initbottleTypeSpinner.setAdapter(typeadapter);
        initbottleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                air_bottle_type_id = bottypeList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                air_bottle_type_id = bottypeList.get(0).getId();
            }
        });

    }

    //解析检测单位
    private void analysisCheckPro(String data) {
        detcList = new Gson().fromJson(data, new TypeToken<List<DetectionUnit>>() {
        }.getType());
        detcAapter = new DetectionAdapter(detcList, InitBotActivity.this);
        initbottleDetection.setAdapter(detcAapter);
        initbottleDetection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                detection_unit_id = detcList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                detection_unit_id = detcList.get(0).getId();
            }
        });
    }

    @OnClick({R.id.initbottleOutEdit, R.id.initbottleServiceEdit, R.id.initbottleNextServiceEdit,
            R.id.initfile1Tv, R.id.initfile2Tv, R.id.inituploadBtn, R.id.initcancleBtn,R.id.commentTitleScanImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commentTitleScanImg:  //摄像头扫描
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("flagCode",SCAN_BY_CAMERA_CODE);
                startActivityForResult(intent,SCAN_BY_CAMERA_CODE);
                break;
            case R.id.initbottleOutEdit:    //出厂日期
                showDatePickCheck(0);
                break;
            case R.id.initbottleServiceEdit:    //检修日期
                showDatePickCheck(1);
                break;
            case R.id.initbottleNextServiceEdit:    //下次检修日期
                showDatePickCheck(2);
                break;
            case R.id.initfile1Tv:  //拍照1
                takePhoto("file1");
                break;
            case R.id.initfile2Tv:  //拍照2
                takePhoto("file2");
                break;
            case R.id.inituploadBtn:    //确定
                String bottleHardCode = initbottleHardCode.getText().toString().trim(); //气瓶钢印码
                String bottleFactory = initbottleFactory.getText().toString().trim();   //出厂编号
                String bottleUsrTime = initbottlecycleTv.getText().toString().trim();   //设计使用年限
                String bottleOutDate = initbottleOutEdit.getText().toString().trim();   //出厂日期
                String bottleServiceDate = initbottleServiceEdit.getText().toString().trim();   //检测日期
                String bottleNextServiceDate = initbottleNextServiceEdit.getText().toString().trim();   //下次检测日期
                if (!Utils.isEmpty(bottleHardCode) && !Utils.isEmpty(bottleUsrTime) && Utils.isNumeric(bottleUsrTime) && !Utils.isEmpty(bottleOutDate)
                        && !Utils.isEmpty(bottleServiceDate) && !Utils.isEmpty(bottleNextServiceDate)) {
                    Logger.e("------参数==" + bottleHardCode + "-=" + bottleFactory + "-=" + bottleUsrTime + "-=" + bottleOutDate + "-=" + bottleServiceDate + "-=" + bottleNextServiceDate);
                    Logger.e("----时间转换=" + Utils.StringToDate(bottleOutDate).getTime() + "---=" + Utils.StringToDate(bottleServiceDate).getTime() + "-=" + Utils.StringToDate(bottleNextServiceDate).getTime());

                    if (photoFileList.size() > 0) {
                        uploadInitBotData(bottleHardCode, bottleFactory, bottleUsrTime, bottleOutDate, bottleServiceDate, bottleNextServiceDate);    //提交数据
                    } else {
                        VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "请按步骤操作!");
                    }
                } else {
                    VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "请按步骤操作!");
                }

                break;
            case R.id.initcancleBtn:    //重置
                setPromptDialogListener("提醒", "是否重置?", new OnPromptDialogListener() {
                    @Override
                    public void leftClick(int code) {

                    }

                    @Override
                    public void rightClick(int code) {
                        clearInputData();
                    }
                });
                break;
        }
    }

    //提交数据
    private void uploadInitBotData(String bottleHardCode, String bottleFactory, String bottleUsrTime, String bottleOutDate, String bottleServiceDate, String bottleNextServiceDate) {

        showWatiDialog();
        Map<String, String> uploadmap = new HashMap<>();
        uploadmap.put("air_bottle_code", bottleCode);    //气瓶编码
        if (!Utils.isEmpty(bottleFactory)) {
            uploadmap.put("factory_number", bottleFactory + "");    //出厂编号
        } else {
            uploadmap.put("factory_number", "");    //出厂编号
        }
        uploadmap.put("air_bottle_seal_code", bottleHardCode);   //钢印码
        uploadmap.put("production_unit_id", production_unit_id + "");  //生产厂家id
        uploadmap.put("detection_unit_id", detection_unit_id + "");    //检修单位Id
        uploadmap.put("use_cycle", bottleUsrTime + "");   //设计使用年限
        uploadmap.put("produce_time_tmp", Utils.StringToDate(bottleOutDate).getTime() + ""); //出厂日期
        uploadmap.put("check_time_tmp", Utils.StringToDate(bottleServiceDate).getTime() + ""); //检修日期
        uploadmap.put("next_check_time_tmp", Utils.StringToDate(bottleNextServiceDate).getTime() + "");    //下次检修日期
        uploadmap.put("air_bottle_type_id", air_bottle_type_id + "");  //气瓶类型Id
        uploadmap.put("air_bottle_belong_id", botBelongId + "");   //归属门店id
        uploadmap.put("userId", getUserInfo().getData().getUser_id() + "");  //用户id
        String url = Constants.getAbsoluteUrl() + UrlCode.BOTMSG_INITBOT.getUrl();
        Logger.e("---------参数===" + uploadmap.toString() + "---url=" + url);
        MultipartRequest multipartRequest = new MultipartRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                closeWatiDialog();
                Logger.e("-------图片上传===respnse==" + response);
                if (response != null) {
                    if (Utils.getJSONCode(response) == 200) {
                        VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.beep, "上传成功!");
                        clearInputData();   //清空数据
                    } else {
                        VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "错误返回:" + Utils.getJSONCode(response) + Utils.getJSONMSG(response));
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeWatiDialog();
                Logger.e("------------图片上传===" + error.getMessage());
                if (!Utils.isEmpty(error.getMessage())) {
                    VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "错误信息:" + ErrorExectionUtils.getMessage(error.getMessage(), InitBotActivity.this));
                } else {
                    VoiceUtils.showToastVoice(InitBotActivity.this, R.raw.warning, "出现异常，请联系后台管理员!");
                }
            }
        }, "f_file[]", photoFileList, uploadmap);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyRequestQueue.add(multipartRequest);

    }


    //拍照
    private void takePhoto(String file) {
        //判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            photoUri = pathUri(System.currentTimeMillis() + "");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (file.equals("file1")) {
                startActivityForResult(intent, TAKE_PHOTO_CODE1);
            } else {
                startActivityForResult(intent, TAKE_PHOTO_CODE2);
            }

        }
    }

    // 图片保存
    private Uri pathUri(String fileName) {
        String strPhotoName = fileName + ".jpg";
        String savePath = Environment.getExternalStorageDirectory().getPath()
                + "/MyPhoto/";
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePath = savePath + strPhotoName;
        return Uri.fromFile(new File(dir, strPhotoName));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE1) {    //二维码部分返回
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = FileUtils.compressBySize(filePath, 480, 800);
                try {
                    photoFileList.add(FileUtils.saveFiles(bitmap, filePath));
                    filePath1 = filePath;
                    initfile1Tv.setText("已拍照");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == TAKE_PHOTO_CODE2) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = FileUtils.compressBySize(filePath, 480, 800);
                try {
                    photoFileList.add(FileUtils.saveFiles(bitmap, filePath));
                    filePath2 = filePath;
                    initfile2Tv.setText("已拍照");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        //摄像头扫描返回
        if(requestCode == SCAN_BY_CAMERA_CODE){
            if(resultCode == 0 && data != null){
                String scanData = data.getStringExtra("scanResult");
                if(!Utils.isEmpty(scanData)){
                    verticalScanBackData(scanData);
                }
            }
        }
    }

    //验证扫描返回
    private void verticalScanBackData(String scanData) {
        Logger.e("-------是否是乱码="+Utils.isMessyCode(scanData));
        if (requestPresent != null ) {
            if(!Utils.isMessyCode(scanData)){
                String url = UrlCode.BOTMSG_INITBOT.getCheck();
                Map<String, Object> maps = new HashMap<>();
                maps.put("bottleCode", scanData);
                requestPresent.getPresentRequestJSONObject(requestQueue, 2, url, maps, scanData);
            }else{
                VoiceUtils.showToastVoice(InitBotActivity.this,R.raw.luanma,"乱码了!");
            }

        }
    }

    //清空数据
    private void clearInputData() {
        clearFocus();
        initbottleCodeTv.setText("扫描获取气瓶编码");
        //钢印码补前3位
        initbottleHardCode.setText(Constants.initHeavyBotCode());
        initbottleFactory.setText("");  //出厂编号
        initbottlecycleTv.setText("");  //设计使用年限
        bottleCode = null;
        filePath1 = null;
        filePath2 = null;
        initfile1Tv.setText("点击拍照");
        initfile2Tv.setText("点击拍照");
        photoFileList.clear();
    }

    //选择日期
    private void showDatePickCheck(final int code) {
        timeDialogView = new TimeDialogView(InitBotActivity.this);
        timeDialogView.show();
        timeDialogView.setTitle("选择日期");
        timeDialogView.setOnTimeDialogListener(new TimeDialogView.OnTimeDialogClickListener() {
            @Override
            public void getYesDialogTime(String timedata) {
                switch (code) {
                    case 0:
                        initbottleOutEdit.setText(timedata);
                        break;
                    case 1:
                        initbottleServiceEdit.setText(timedata);
                        break;
                    case 2:
                        initbottleNextServiceEdit.setText(timedata);
                        break;
                    default:
                        break;
                }
                timeDialogView.dismiss();
            }

            @Override
            public void getCancleDialogTime() {
                timeDialogView.dismiss();
            }
        });

    }

}
