package com.sucheng.gas.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
/**
 * App工具类
 * 
 * @author
 * @data
 */
public class AppUtils {
	 private AppUtils()  
	    {  
	        /**cannot be instantiated **/ 
	        throw new UnsupportedOperationException("cannot be instantiated");  
	  
	    }  

	    /** 
	     * 获取程序应用名称
	     */ 
	    public static String getAppName(Context context)  
	    {  
	        try 
	        {  
	            PackageManager packageManager = context.getPackageManager();  
	            PackageInfo packageInfo = packageManager.getPackageInfo(  
	                    context.getPackageName(), 0);  
	            int labelRes = packageInfo.applicationInfo.labelRes;  
	            return context.getResources().getString(labelRes);  
	        } catch (NameNotFoundException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	  
	    /** 
	     * 获取应用版本名称
	     *  
	     * @param context 
	     * @return
	     */ 
	    public static String getVersionName(Context context)  
	    {  
	        try 
	        {  
	            PackageManager packageManager = context.getPackageManager();  
	            PackageInfo packageInfo = packageManager.getPackageInfo(  
	                    context.getPackageName(), 0);  
	            return packageInfo.versionName;  
	  
	        } catch (NameNotFoundException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    } 
	    
	    
	    /**
	     * 获取应用版本号
	     * @param context
	     * @return
	     */
	    public static int getVersionCode(Context context){	    	
	    	try {
	    		PackageManager packM = context.getPackageManager();
				PackageInfo packInfo = packM.getPackageInfo(context.getPackageName(), 0);
				
				return packInfo.versionCode;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			return 0;
	    	
	    } 
	    
	    /**
	     * 获取设备ID
	     * @param context
	     * @return deviceId
		 * permission READ_PHONE_STATE
	     */
	    public static String getDeviceId(Context context){
	    	try {
				TelephonyManager tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    		System.out.println("tm.getDeviceId():"+tm.getDeviceId());
	    		return tm.getDeviceId();
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	return null;
	    }
}
