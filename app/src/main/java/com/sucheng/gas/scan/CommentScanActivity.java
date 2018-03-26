package com.sucheng.gas.scan;

import com.mexxen.barcode.BarcodeEvent;
import com.mexxen.barcode.BarcodeListener;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.utils.Utils;
import com.yanzhenjie.nohttp.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.jb.Preference;
import android.jb.barcode.BarcodeManager;
import android.jb.barcode.BarcodeManager.Callback;
import android.jb.utils.Tools;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * 
 * @Describe
 * @author sunjianhua QiAnEnergyMob 2017-5-26
 * 
 */
public class CommentScanActivity extends MyScanAct{

    private static final String TAG = "CommentScanActivity";

	private final static String HT380K_ScanAction = "com.jb.action.F4key";

	private long nowTime = 0;
	private long lastTime = 0;
	// ht380k
	android.jb.barcode.BarcodeManager mHt380kCodeManager;
	// mx5050
	private com.mexxen.barcode.BarcodeManager mMx5050CodeManager;

	private final int SCANKEY_LEFT = 301;
	private final int SCANKEY_RIGHT = 300;
	private final int SCANKEY_CENTER = 302;
	private final int SCANTIMEOUT = 3000;
	// private boolean mbKeyDown = true;

	private Handler mDoDecodeHandler;

	class DoDecodeThread extends Thread {
		public void run() {
			Looper.prepare();

			mDoDecodeHandler = new Handler();

			Looper.loop();
		}
	}
	//T50的扫描
	private ScanInterface scanDecode;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.e("-----扫描-----"+Build.PRODUCT);
		if (Build.PRODUCT.equals(Constants.MX505_BUILD)) {
			judgePhoneModels();
		}

		//T50初始化
		if(Build.PRODUCT.equals("T50")){
			scanDecode = new ScanDecode(this);
			scanDecode.initService("true");//初始化扫描服务
			scanDecode.getBarCode(new ScanInterface.OnScanListener() {
				@Override
				public void getBarcode(String s) {
					if(!Utils.isEmpty(s)){
						getScanResultData(s);
					}
				}
			});
		}
	}

    @Override
	protected void onResume() {
		super.onResume();
		// 判断是否是非防爆机，非防爆机需要在onResume中判断
		if (Utils.isContanis(Constants.ht380kBuild, Build.PRODUCT)) {
			judgePhoneModels();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Utils.isContanis(Constants.ht380kBuild, Build.PRODUCT)) {
			unregisterReceiver(scanDataReceiver);
		} else if (Build.PRODUCT.equals(Constants.MX505_BUILD)) {
			mMx5050CodeManager.dismiss();
		}
		if(scanDecode != null){
			scanDecode.onDestroy();
		}
	}

	//扫描的回调
	public void getScanResultData(String botCode) {
		Logger.e("----扫描==="+botCode);
	}

	/**
	 * 扫描回调
	 */
	Callback dataReceived = new Callback() {

		@Override
		public void Barcode_Read(byte[] buffer, String codeId, int errorCode) {
			String localPro = System.getProperty("file.encoding");
			Logger.e("--------local=="+localPro);
			String codeType = Tools.returnType(buffer);
			Logger.e("-----codeType="+codeType);
			if(codeType.equals("default")){
				Logger.e("------default="+new String(buffer));
			}else{
				try {
					Logger.e("-----else=="+new String(buffer,"GB2312"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			try {
				String scData = new String(buffer,"UTF-8");
				Logger.e("------scData="+scData);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (buffer != null) {
				String mxCode = new String(buffer);
				getScanResultData(mxCode.trim());
			}
		}
	};

	/**
	 * HT380K按键接收广播
	 */
	private BroadcastReceiver scanDataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("F4key")) {
				if (intent.getStringExtra("F4key").equals("down")) {
					Log.e("trig", "key down");
					// isContines = true;
					if (null != mHt380kCodeManager) {
						nowTime = System.currentTimeMillis();

						if (nowTime - lastTime > 200) {
							mHt380kCodeManager.Barcode_Stop();
							lastTime = nowTime;
							if (null != mHt380kCodeManager) {
								mHt380kCodeManager.Barcode_Start();
							}
						}
					}
				} else if (intent.getStringExtra("F4key").equals("up")) {
					Log.e("trig", "key up");
				}
			}
		}
	};

	/**
	 * 判断手持机
	 */
	private void judgePhoneModels() {

		if (Build.PRODUCT.equals(Constants.MX505_BUILD)) {// MX5050

			mMx5050CodeManager = new com.mexxen.barcode.BarcodeManager(this);
			mMx5050CodeManager.addListener(new BarcodeListener() {
				@Override
				public void barcodeEvent(BarcodeEvent arg0) {
					// 调用 getBarcode()方法读取二维码信息
					String mxCode = mMx5050CodeManager.getBarcode();
					if (!TextUtils.isEmpty(mxCode)) {
						getScanResultData(mxCode.trim());
					} else {

					}
				}
			});
		} else if (Utils.isContanis(Constants.ht380kBuild, Build.PRODUCT)) {// HT380K
			IntentFilter scanDataFilter = new IntentFilter();
			scanDataFilter.addAction(HT380K_ScanAction);
			registerReceiver(scanDataReceiver, scanDataFilter);
//			if (mHt380kCodeManager == null) {
//				mHt380kCodeManager = android.jb.barcode.BarcodeManager
//						.getInstance();
//			}
//
//			mHt380kCodeManager.Barcode_Open(CommentScanActivity.this,
//					dataReceived);
//
//			if(Build.PRODUCT.equals("HT380D")){
//				Preference.setScanOutMode(this, 3);
//				mHt380kCodeManager.setScannerModel(4);
//			}
//			Logger.e("---22===="+Preference.getScannerModel(CommentScanActivity.this)+"----="+Preference.getScanOutMode(CommentScanActivity.this));

			if (mHt380kCodeManager == null) {
				mHt380kCodeManager = BarcodeManager.getInstance();
			}
//			else {
//				// if (scanManager.isSerialPort_isOpen()) {
//				mHt380kCodeManager.Barcode_Close();
//			}
			Log.e(TAG, "onStart()");
			mHt380kCodeManager.Barcode_Open(CommentScanActivity.this, dataReceived);
			System.out.println("Service onStart()");
			Preference.setScanOutMode(CommentScanActivity.this,3);
			mHt380kCodeManager.setScannerModel(4);
			Log.e(TAG,"-=-----22----"+mHt380kCodeManager.getScannerModel());
			if (mHt380kCodeManager == null) {
				mHt380kCodeManager = BarcodeManager.getInstance();
			} else {
				// if (scanManager.isSerialPort_isOpen()) {
				mHt380kCodeManager.Barcode_Close();
			}
			Log.e(TAG, "onStart()");
			mHt380kCodeManager.Barcode_Open(CommentScanActivity.this, dataReceived);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		System.out.println(keyCode);
		switch (keyCode) {
		// case KeyEvent.KEYCODE_MUTE://
		// // scanning
		// if (event.getRepeatCount() == 0) {
		// BarcodeAPI.getInstance().scan();
		// }

		case KeyEvent.KEYCODE_ENTER:
		case SCANKEY_LEFT:
		case SCANKEY_CENTER:
		case SCANKEY_RIGHT:

		}

		return super.onKeyDown(keyCode, event);
	}

}
