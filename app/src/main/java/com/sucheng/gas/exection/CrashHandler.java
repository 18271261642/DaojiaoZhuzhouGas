package com.sucheng.gas.exection;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.sucheng.gas.base.MyApplication;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.utils.AppUtils;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/31.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /** Debug Log Tag */
    public static final String TAG = "CrashHandler";
    /** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */
    public static final boolean DEBUG = true;
    /** CrashHandler实例 */
    private static CrashHandler INSTANCE;
    /** 程序的Context对象 */
    private Context mContext;
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
            // 全局推出
            MyApplication.getMyApplication().removeAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        final StringBuilder messageError = new StringBuilder();
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            messageError.append(sw.toString());
        } catch (Exception e2) {
            System.out.println("bad getErrorInfoFromException");
        }
        final String message = ex.getMessage();// hrowable 的详细消息字符串
        final String message_type = ex.getLocalizedMessage();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = null;
        try {
            cn = am.getRunningTasks(1).get(0).topActivity;
        } catch (SecurityException e) {
            cn = null;
        }
        final String msg_class = cn == null ? "" : cn.getClassName() + "";
        // 收集设备信息
        messageError.append(collectCrashDeviceInfo(mContext));
        Log.e("","----------------" + messageError.toString());
        // 使用Toast来显示异常信息
        new Thread() {

            @Override
            public void run() {
                // 方案1 Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                // 执行
                postReport(msg_class, message_type, message, messageError.toString());
                showToast();
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 自定义弹出toast
     *
     * @param
     */
    public void showToast() {
        Toast toast = new Toast(mContext);
        TextView textView = new TextView(mContext);
        textView.setText("抱歉,程序出错,即将关闭!错误报告将发送给后台管理员!");
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.LEFT);
        textView.setPadding(15, 15, 15, 15);
        toast.setView(textView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public String collectCrashDeviceInfo(Context ctx) {
        StringBuffer tagString = new StringBuffer("");
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                tagString.append("app版本号:");
                tagString.append(pi.versionName == null ? "not set" : pi.versionName );
                tagString.append("app版本:" + pi.versionCode );
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                tagString.append(field.getName() + ":" + field.get(null) );
                if (DEBUG) {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }
        return tagString.toString();
    }



    /**
     * 使用HTTP Post 发送错误报告到服务器 这里不再赘述
     *
     * @param msg_class
     *            调用类
     * @param source_type
     *            e.toString 调用此对象 getLocalizedMessage() 方法的结果
     * @para
     *            getLocalizedMessage 返回 null，则只返回类名称。
     * @param message
     *            此 throwable 的详细消息字符串。
     * @param messageError
     *            throwable 相关的堆栈跟踪
     */
    private void postReport(String msg_class, String source_type, String message, String messageError) {
        // 在上传的时候还可以将该app的version，该手机的机型等信息一并发送的服务器,
        // Android的兼容性众所周知，所以可能错误不是每个手机都会报错，还是有针对性的去debug比较好
        HashMap<String, String> mapJson = new HashMap<>();
        mapJson.put("errorClass", msg_class);// 类名
        mapJson.put("method", message);// 方法名
        mapJson.put("createtime",new SimpleDateFormat("yyy-y-MM-dd HH:mm:ss ").format(new Date( System.currentTimeMillis())));// 产生时间
        mapJson.put("loglevel", "E");// 日志级别
        mapJson.put("logmsg", messageError);// 日志信息
        mapJson.put("source_type", source_type);// 错误类型
        mapJson.put("version", AppUtils.getVersionCode(mContext)+"");//版本号
        sendLog(mapToJson(mapJson));
    }

    //异常日志上传后台请求
    private void sendLog(final String jsonMap){
        Log.e(TAG,"-----错误信息收集----"+jsonMap.toString());
        String url = Constants.getAbsoluteUrl()+Constants.EXECTIONLOG_MOBILE+"exceptionLog";
        RequestQueue requestQueue = NoHttp.newRequestQueue(1);
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        stringRequest.add("log",jsonMap);
        stringRequest.add("deviceCode",AppUtils.getDeviceId(mContext)+"");
        requestQueue.add(1, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Logger.e("--------response="+response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Logger.e("---fff-----response="+response.getException());
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    /**
     *
     * map转换json.
     * <br>详细说明
     * @param map 集合
     * @return
     * @return String json字符串
     * @throws
     * @author slj
     */
    public static String mapToJson(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            value = map.get(key);
            jsonBuffer.append(key + ":" +"\""+ value+"\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }
}
