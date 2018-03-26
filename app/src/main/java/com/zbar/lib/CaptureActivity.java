package com.zbar.lib;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.sucheng.gas.R;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;
import com.zbar.lib.zxing.BitmapDecoder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 扫码界面
 *
 * @author TLJ
 *         created at 2016/9/19 14:50
 *         QQ:172864659
 */
public class CaptureActivity extends Activity implements Callback, View.OnClickListener {

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    //返回图片按钮
    @BindView(R.id.commentTitleBackImg)
    ImageView commentTitleBackImg;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;
    private RelativeLayout mContainer = null;
    private RelativeLayout mCropLayout = null;
    private boolean isNeedCapture = false;

    private ImageView sanguangdent_Img;
    private TextView shoudian_Tv;

    public boolean isNeedCapture() {
        return isNeedCapture;
    }

    public void setNeedCapture(boolean isNeedCapture) {
        this.isNeedCapture = isNeedCapture;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }


    private static final String TAG = "CaptureActivity";
    private List<String> scanList = new ArrayList<>();

    private static final int REQUEST_CODE = 1000;

    //标识码
    private int flagCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        ButterKnife.bind(this);

        initViews();

        // 初始化 CameraManager
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
        mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
        shoudian_Tv = (TextView) findViewById(R.id.shoudian_Tv);
        sanguangdent_Img = (ImageView) findViewById(R.id.sanguangdent_Img);
        sanguangdent_Img.setOnClickListener(this);

        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
        TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mQrLineView.setAnimation(mAnimation);

    }

    private void initViews() {
        commentTitleTv.setText("条码扫描");
        commentTitleBackImg.setVisibility(View.VISIBLE);
        commentTitleBackImg.setOnClickListener(this);

    }

    Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(CaptureActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(CaptureActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            try {
                final Uri selectedImage = data.getData();

                Log.e(TAG, "----------11-----" + data.getData());
                Log.e(TAG, "----------222-----" + selectedImage.toString());


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Bitmap bit = null;
                        try {
                            Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                            BitmapDecoder decoder = new BitmapDecoder(
                                    CaptureActivity.this);
                            Result result = decoder.getRawResult(bit);
                            Message messsage = new Message();
                            if (result != null) {
                                String resultData = ResultParser.parseResult(result).toString();

                                Log.e(TAG, "----------解析的数据-----" + resultData);
                                messsage.what = 1;
                                messsage.obj = resultData;
                                handlers.sendMessage(messsage);

                            } else {
                                Log.e(TAG, "----------result为null--解析失败---");
                                messsage.what = 2;
                                messsage.obj = "解析失败";
                                handlers.sendMessage(messsage);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//				String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//				Cursor cursor = getContentResolver().query(selectedImage,
//						filePathColumn, null, null, null);
//				cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				final String photoPath = cursor.getString(columnIndex);
//				String photoPath = getAbsoluteImagePath(selectedImage);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "----------错误日志-----" + e.getMessage());
            }

        }
    }

    private String getAbsoluteImagePath(Uri uri) {
        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, proj, // Which
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }
        return imagePath;
    }

    boolean flag = true;

    protected void light() {
        if (flag == true) {
            flag = false;
            // 开闪光灯
            CameraManager.get().openLight();
            sanguangdent_Img.setImageResource(R.mipmap.shoudian1);
            shoudian_Tv.setText(getResources().getString(R.string.close_light));
        } else {
            flag = true;
            // 关闪光灯
            CameraManager.get().offLight();
            sanguangdent_Img.setImageResource(R.mipmap.shoudian2);
            shoudian_Tv.setText(getResources().getString(R.string.open_light));
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(String result) {
        Log.e(TAG, "-------扫描结果=" + result);
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        Intent intent = new Intent();
        intent.putExtra("scanResult", result.trim());
        setResult(flagCode, intent);
        this.finish();


    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.y;
            int height = point.x;

            int x = mCropLayout.getLeft() * width / mContainer.getWidth();
            int y = mCropLayout.getTop() * height / mContainer.getHeight();

            int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
            // 设置是否需要截图
            setNeedCapture(true);


        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commentTitleBackImg:    //返回
                finish();
                break;
            case R.id.sanguangdent_Img:    //打开闪光灯
                light();
                break;
        }
    }
}