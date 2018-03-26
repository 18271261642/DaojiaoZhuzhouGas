package com.sucheng.gas.utils;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sucheng.gas.bean.AppVersion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class UpdateChecker{

	public static final String TAG = "UpdateChecker";

	private Context mContext;	
	//地址
	private String mCheckUrl;
	private AppVersion mAppVersion;
	//下载提示框
	private ProgressDialog mProgressDialog;

	private File apkFile;

	public void setCheckUrl(String url) {
		mCheckUrl = url;

		Log.e(TAG, "---url===" + mCheckUrl);
	}

	public UpdateChecker(Context context) {
		mContext = context;
		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage("下载中...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

					}
				});
		mProgressDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
	}

	@SuppressLint("HandlerLeak")
	public void checkForUpdates() {
		if (mCheckUrl == null) {
			// throw new Exception("checkUrl can not be null");
			Log.e(TAG, "-------===");
			return;
		}
		//第一步，先获取服务器版本号
		RequestQueue requestQueue = Volley.newRequestQueue(mContext);
		StringRequest StringRequest = new StringRequest(mCheckUrl, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println("=----response==="+response);
				if(response!=null){
				analysisResponse(response);
				}else{
					return;
				}
			}
		},new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});
		requestQueue.add(StringRequest);		
		
	}

	/**
	 * 解析
	 * @param response
	 */
	protected void analysisResponse(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(response);
			String data = jsonObject.getString("data");
			if (data != null && !data.equals("null")) {
				Gson gson = new Gson();
				mAppVersion = gson.fromJson(data, AppVersion.class);	
				System.out.println("----apV===" + mAppVersion.getVersionCode());
				int versionCode = mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 0).versionCode;
				if (mAppVersion.getVersionCode() > versionCode) {
					showUpdateDialog();
				} else {

				}
			} else {
				VoiceUtils.showToast(mContext,"当前已是最新版本");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 *
	 */
	public void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		// builder.setIcon(R.drawable.icon);
		builder.setTitle("提醒");
		builder.setMessage(""+mAppVersion.getUpdateMessage());
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				downLoadApk();
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
		     
			}
		});
		builder.show();

	}
	/**
	 * 下载apk
	 */
	public void downLoadApk() {
		String apkUrl = mAppVersion.getUrl();
		
		System.out.println("-------apkUrl==="+apkUrl);
		
		String dir = mContext.getExternalFilesDir("apk").getAbsolutePath();
		
		File folder = Environment.getExternalStoragePublicDirectory(dir);
		if (folder.exists() && folder.isDirectory()) {
			//删除
			FileUtils.deleteFile(folder);
		} else {
			folder.mkdirs();
		}
//		String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),
//				apkUrl.length());
		String filename = "daojiaoupdateload.apk";
		String destinationFilePath = dir + "/" + filename;	
		
		System.out.println("---destinationFilePath---"+destinationFilePath);
		apkFile = new File(destinationFilePath);
		mProgressDialog.show();
		Intent intent = new Intent(mContext, DownloadService.class);
		intent.putExtra("url", apkUrl);
		intent.putExtra("dest", destinationFilePath);
		intent.putExtra("receiver", new DownloadReceiver(new Handler()));
		mContext.startService(intent);

	}

	private class DownloadReceiver extends ResultReceiver {
		public DownloadReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == DownloadService.UPDATE_PROGRESS) {
				int progress = resultData.getInt("progress");
				mProgressDialog.setProgress(progress);
				mProgressDialog.setCancelable(false);
				if (progress == 100) {
					mProgressDialog.dismiss();
					String[] command = { "chmod", "777", apkFile.toString() };
					try {
						ProcessBuilder builder = new ProcessBuilder(command);
						builder.start();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(apkFile),
								"application/vnd.android.package-archive");
						mContext.startActivity(intent);
					} catch (Exception e) {

					}
				}
			}
		}
	}

}
