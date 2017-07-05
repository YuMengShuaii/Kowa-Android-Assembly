package com.nat.android.javashoplib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nat.android.javashoplib.BuildConfig;
import com.nat.android.javashoplib.R;
import com.nat.android.javashoplib.init.FrameInit;
import com.nat.android.javashoplib.weight.MyDialog;

/**
 * Created by LDD on 17/2/27.
 */

public class Utils {
    private static final String TAG = "YMS_Frame";
    private static Toast toast;
    /**
     * 创建Dialog
     * @param paramString1         标题
     * @param paramString2         取消
     * @param paramString3         确定
     * @param paramContext         上下文
     * @param paramDialogInterface 监听接口
     */
    public static void createDialog(String paramString1, String paramString2, String paramString3, Context paramContext, final DialogInterface paramDialogInterface)
    {
        final Dialog localDialog = new Dialog(paramContext,R.style.Dialog);
        View localView = LayoutInflater.from(paramContext).inflate(R.layout.newdialog_lay, null);
        localDialog.setContentView(localView);
        TextView localTextView1 = (TextView)localView.findViewById(R.id.yes);
        TextView localTextView2 = (TextView)localView.findViewById(R.id.on);
        localTextView1.setText(paramString3);
        localTextView2.setText(paramString2);
        ((TextView)localView.findViewById(R.id.message)).setText(paramString1);
        localTextView1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramView)
            {
                paramDialogInterface.yes();
                localDialog.dismiss();
            }
        });
        localTextView2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramView)
            {
                paramDialogInterface.no();
                localDialog.dismiss();
            }
        });
        localDialog.setCanceledOnTouchOutside(true);
        localDialog.setCancelable(true);
        if (!(localDialog.isShowing()))
            localDialog.show();
    }

    public static MyDialog createLoadingDialog(Context context,int layoutid,int imageviewid){
        View localView = LayoutInflater.from(context).inflate(layoutid, null);
        ImageView view  = (ImageView) localView.findViewById(imageviewid);
        MyDialog localMyDialog = new MyDialog(context,R.style.loading_dialog,view);
        localMyDialog.setCancelable(true);
        localMyDialog.setContentView(localView);
        return localMyDialog;
    }
    public static MyDialog createLoadingDialog(Context paramContext)
    {
        View localView = LayoutInflater.from(paramContext).inflate(R.layout.dialog_loading, null);
        ImageView view  = (ImageView) localView.findViewById(R.id.img);
        MyDialog localMyDialog = new MyDialog(paramContext,R.style.loading_dialog,view);
        localMyDialog.setCancelable(true);
        localMyDialog.setContentView(localView);
        return localMyDialog;
    }


    /**
     * 监听接口
     */
    public interface DialogInterface
    {
         void no();

         void yes();
    }

    /**
     * Toast提示  3秒
     * @param content 提示内容
     */
    public static void ToastS(String content){
        if (toast==null){
            toast = Toast.makeText(FrameInit.Mcontext,content,Toast.LENGTH_SHORT);
        }else{
            toast.cancel();
            toast = Toast.makeText(FrameInit.Mcontext,content,Toast.LENGTH_SHORT);
        }

        toast.show();
    }
    /**
     * Toast提示  5秒
     * @param content 提示内容
     */
    public static void ToastL(String content){
        if (toast==null){
            toast = Toast.makeText(FrameInit.Mcontext,content,Toast.LENGTH_LONG);
        }else{
            toast.cancel();
            toast = Toast.makeText(FrameInit.Mcontext,content,Toast.LENGTH_LONG);
        }
        toast.show();
    }


    /**
     * 安装某个应用
     *
     * @param context
     * @param apkFile
     * @return
     */
    public static boolean installApp(Context context, File apkFile) {
        try {
            context.startActivity(getInstallAppIntent(apkFile));
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
    /**
     * 获取安装应用的Intent
     *
     * @param apkFile
     * @return
     */
    public static Intent getInstallAppIntent(File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return null;
        }

        Utils.chmod("777", apkFile.getAbsolutePath());
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * �?查某个包名的App是否已经安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasAppInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 根据包名启动第三方App
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean launchAppByPackageName(Context context,
                                                 String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return false;
    }

    /**
     * 获取Assets资源
     * @param context
     * @param name
     * @return
     * @throws IOException
     */
    public static String getAssetsFie(Context context, String name)
            throws IOException {

        InputStream is = context.getAssets().open(name);
        int size = is.available();

        // Read the entire asset into a local byte buffer.
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String content = new String(buffer, "UTF-8");

        return content;

    }

    /**
     * 是否为wifi连接状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo != null) {
            if ("wifi".equals(networkinfo.getTypeName().toLowerCase(Locale.US))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetConnect(Context context) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        return networkinfo != null;
    }

    /**
     * 获取权限
     *
     * @param permission
     *            权限
     * @param path
     *            文件路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            Log.e(TAG, "chmod", e);
        }
    }

    /**
     * 是否安装了sdcard卡
     *
     * @return true表示有，false表示没有
     */
    public static boolean haveSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统内部可用空间大小
     *
     * @return available size
     */
    public static long getSystemAvailableSize() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    /**
     * 获取sd卡可用空间大小
     *
     * @return available size
     */
    public static long getSDCardAvailableSize() {
        long available = 0;
        if (haveSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();

            available = availaBlock * blocSize;
        } else {
            available = -1;
        }
        return available;
    }

    /**
     * 获取application层级的metadata
     *
     * @param context
     * @param key
     * @return
     */
    public static String getApplicationMetaData(Context context, String key) {
        try {
            Object metaObj = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA).metaData
                    .get(key);
            if (metaObj instanceof String) {
                return metaObj.toString();
            } else if (metaObj instanceof Integer) {
                return ((Integer) metaObj).intValue() + "";
            } else if (metaObj instanceof Boolean) {
                return ((Boolean) metaObj).booleanValue() + "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return 0;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param
     *            （DisplayMetrics类中属�?�density�?
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param
     *            （DisplayMetrics类中属�?�density�?
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param
     *            （DisplayMetrics类中属�?�scaledDensity�?
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     *            （DisplayMetrics类中属�?�scaledDensity�?
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     *            activity
     */
    public static void hideInputMethod(Activity activity) {
        hideInputMethod(activity, activity.getCurrentFocus());
    }

    /**
     * 隐藏键盘
     *
     * @param context
     *            context
     * @param view
     *            The currently focused view
     */
    public static void hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示输入键盘
     *
     * @param context
     *            context
     * @param view
     *            The currently focused view, which would like to receive soft
     *            keyboard input
     */
    public static void showInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * Bitmap缩放，注意源Bitmap在缩放后将会被回收
     *
     * @param origin
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getScaleBitmap(Bitmap origin, int width, int height) {
        float originWidth = origin.getWidth();
        float originHeight = origin.getHeight();

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scale = Bitmap.createBitmap(origin, 0, 0, (int) originWidth,
                (int) originHeight, matrix, true);
        origin.recycle();
        return scale;
    }


    /**
     * 计算某一时间与现在时间间隔的文字提示
     */
    public static String countTimeIntervalText(long time) {
        long dTime = System.currentTimeMillis() - time;
        // 15分钟
        if (dTime < 15 * 60 * 1000) {
            return "刚刚";
        } else if (dTime < 60 * 60 * 1000) {
            // �?小时
            return "1小时前";
        } else if (dTime < 24 * 60 * 60 * 1000) {
            return (int) (dTime / (60 * 60 * 1000)) + "小时前";
        } else {
            return DateFormat.format("MM-dd kk:mm", System.currentTimeMillis())
                    .toString();
        }
    }

    /**
     * 获取通知栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int x = 0, statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取标题栏高度
     *
     * @param context
     * @return
     */
    public static int getTitleBarHeight(Activity context) {
        int contentTop = context.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight(context);
    }

    /**
     * 获取屏幕宽度，px
     *
     * @param context
     * @return
     */
    public static float getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度，px
     *
     * @param context
     * @return
     */
    public static float getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕像素密度
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取scaledDensity
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }


    /**
     * 获取当前小时分钟
     *
     * @return
     */
    public static String getTime24Hours() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取电池电量,0~1
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unused")
    public static float getBattery(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext()
                .registerReceiver(null,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryInfoIntent.getIntExtra("status", 0);
        int health = batteryInfoIntent.getIntExtra("health", 1);
        boolean present = batteryInfoIntent.getBooleanExtra("present", false);
        int level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 0);
        int plugged = batteryInfoIntent.getIntExtra("plugged", 0);
        int voltage = batteryInfoIntent.getIntExtra("voltage", 0);
        int temperature = batteryInfoIntent.getIntExtra("temperature", 0); // 温度的单位是10�?
        String technology = batteryInfoIntent.getStringExtra("technology");
        return ((float) level) / scale;
    }

    /**
     * 获取手机名称
     *
     * @return
     */
    public static String getMobileName() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

    /**
     * 是否安装了sdcard卡
     *
     * @return true表示有，false表示没有
     */
    public static boolean hasSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取sd卡可用空�?
     *
     * @return available size
     */
    public static long getAvailableExternalSize() {
        long available = 0;
        if (hasSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();

            available = availaBlock * blocSize;
        } else {
            available = -1;
        }
        return available;
    }

    /**
     * 获取内存可用空间
     *
     * @return available size
     */
    public static long getAvailableInternalSize() {
        long available = 0;
        if (hasSDCard()) {
            File path = Environment.getRootDirectory();
            StatFs statfs = new StatFs(path.getPath());
            long blocSize = statfs.getBlockSize();
            long availaBlock = statfs.getAvailableBlocks();

            available = availaBlock * blocSize;
        } else {
            available = -1;
        }
        return available;
    }


    /**
     * 是否为2.2版本及以上
     *
     * @return
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * 是否为2.3版本及以上
     *
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * 是否为3.0版本及以上
     *
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * 是否为3.1版本及以上
     *
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 是否为4.1版本及以上
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static String getPhoneType() {

        String phoneType = android.os.Build.MODEL;

        Log.d(TAG, "phoneType is : " + phoneType);

        return phoneType;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getOsVersion() {
        String osversion;
        int osversion_int = getOsVersionInt();
        osversion = osversion_int + "";
        return osversion;

    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static int getOsVersionInt() {
        return Build.VERSION.SDK_INT;

    }


    /**
     * 获取手机号，几乎获取不到
     *
     * @param context
     * @return
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        String phoneNum = mTelephonyMgr.getLine1Number();
        return TextUtils.isEmpty(phoneNum) ? "" : phoneNum;
    }

    /**
     * 获取imei
     *
     * @param context
     * @return
     */
    public static String getPhoneImei(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        String phoneImei = mTelephonyMgr.getDeviceId();
        Log.d(TAG, "IMEI is : " + phoneImei);
        return TextUtils.isEmpty(phoneImei) ? "" : phoneImei;
    }

    /**
     * 获取imsi
     *
     * @param context
     * @return
     */
    public static String getPhoneImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        String phoneImsi = mTelephonyMgr.getSubscriberId();
        Log.d(TAG, "IMSI is : " + phoneImsi);

        return TextUtils.isEmpty(phoneImsi) ? "" : phoneImsi;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String Mac = null;
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis.close();
            }

            if (Mac == null || Mac.length() == 0) {
                return "";
            } else if (Mac.endsWith("\n")) {
                Mac = Mac.substring(0, Mac.length() - 1);
            }
        } catch (Exception io) {
            Log.w(TAG, "Exception", io);
        }

        return TextUtils.isEmpty(Mac) ? "" : Mac;
    }

    /**
     * 获取重复字段多的个数
     *
     * @param s
     * @return
     */
    public static int getRepeatTimes(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }

        int mCount = 0;
        char[] mChars = s.toCharArray();
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < mChars.length; i++) {
            char key = mChars[i];
            Integer value = map.get(key);
            int count = value == null ? 0 : value.intValue();
            map.put(key, ++count);
            if (mCount < count) {
                mCount = count;
            }
        }

        return mCount;
    }

    /**
     * 判断是否为手机号码
     *
     * @param num
     * @return
     */
    public static boolean isPhoneNum(String num) {
        // 确保每一位都是数�?
        return !TextUtils.isEmpty(num) && num.matches("1[0-9]{10}")
                && !isRepeatedStr(num) && !isContinuousNum(num);
    }

    /**
     * 判断是否400服务代码
     *
     * @param num
     * @return
     */
    public static boolean is400or800(String num) {
        return !TextUtils.isEmpty(num)
                && (num.startsWith("400") || num.startsWith("800"))
                && num.length() == 10;
    }

    /**
     * 判断是否区域号码
     *
     * @param num
     * @return
     */
    public static boolean isAdCode(String num) {
        return !TextUtils.isEmpty(num) && num.matches("[0]{1}[0-9]{2,3}")
                && !isRepeatedStr(num);
    }

    /**
     * 判断是否座机号码
     *
     * @param num
     * @return
     */
    public static boolean isPhoneHome(String num) {
        return !TextUtils.isEmpty(num) && num.matches("[0-9]{7,8}")
                && !isRepeatedStr(num);
    }

    /**
     * 判断是否为重复字符串
     *
     * @param str
     *         需要检查的字符
     */
    public static boolean isRepeatedStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        if (len <= 1) {
            return false;
        } else {
            char firstChar = str.charAt(0);// 第一个字�?
            for (int i = 1; i < len; i++) {
                char nextChar = str.charAt(i);// 第i个字�?
                if (firstChar != nextChar) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 判断字符串是否为连续数字 45678901
     */
    public static boolean isContinuousNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (!isNumbericString(str))
            return true;
        int len = str.length();
        for (int i = 0; i < len - 1; i++) {
            char curChar = str.charAt(i);
            char verifyChar = (char) (curChar + 1);
            if (curChar == '9')
                verifyChar = '0';
            char nextChar = str.charAt(i + 1);
            if (nextChar != verifyChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为连续字母 xyZaBcd
     */
    public static boolean isContinuousWord(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (!isAlphaBetaString(str))
            return true;
        int len = str.length();
        String local = str.toLowerCase();
        for (int i = 0; i < len - 1; i++) {
            char curChar = local.charAt(i);
            char verifyChar = (char) (curChar + 1);
            if (curChar == 'z')
                verifyChar = 'a';
            char nextChar = local.charAt(i + 1);
            if (nextChar != verifyChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     *            ，需要检查的字符
     */
    public static boolean isNumbericString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[0-9]+$");// 从开头到结尾必须全部为数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 判断是否为纯字母
     *
     * @param str
     * @return
     */
    public static boolean isAlphaBetaString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[a-zA-Z]+$");// 从开头到结尾必须全部为字母或者数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * 判断是否为纯字母或数
     *
     * @param str
     * @return
     */
    public static boolean isAlphaBetaNumbericString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");// 从开头到结尾必须全部为字母或者数�?
        Matcher m = p.matcher(str);

        return m.find();
    }

    private static String regEx = "[\u4e00-\u9fa5]";
    private static Pattern pat = Pattern.compile(regEx);

    /**
     * 判断是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        return pat.matcher(str).find();
    }

    public static boolean patternMatcher(String pattern, String input) {
        if (TextUtils.isEmpty(pattern) || TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern pat = Pattern.compile(pattern);
        Matcher matcher = pat.matcher(input);

        return matcher.find();
    }

    /****************************************************************************/
    // import PPutils
    private static int id = 1;

    public static int getNextId() {
        return id++;
    }

    /**
     * 将输入流转为字节数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] read2Byte(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();

        return outSteam.toByteArray();
    }

    /**
     * 判断是否符合月和年的过期时间规则
     *
     * @param date
     * @return
     */
    public static boolean isMMYY(String date) {
        try {
            if (!TextUtils.isEmpty(date) && date.length() == 4) {
                int mm = Integer.parseInt(date.substring(0, 2));
                return mm > 0 && mm < 13;
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }

        return false;
    }

    /**
     * 20120506 共八位，前四�?-年，中间两位-月，�?后两�?-�?
     *
     * @param date
     * @return
     */
    public static boolean isRealDate(String date, int yearlen) {
        // if(yearlen != 2 && yearlen != 4)
        // return false;
        int len = 4 + yearlen;
        if (date == null || date.length() != len)
            return false;

        if (!date.matches("[0-9]+"))
            return false;

        int year = Integer.parseInt(date.substring(0, yearlen));
        int month = Integer.parseInt(date.substring(yearlen, yearlen + 2));
        int day = Integer.parseInt(date.substring(yearlen + 2, yearlen + 4));

        if (year <= 0)
            return false;
        if (month <= 0 || month > 12)
            return false;
        if (day <= 0 || day > 31)
            return false;

        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return day > 30 ? false : true;
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    return day > 29 ? false : true;
                return day > 28 ? false : true;
            default:
                return true;
        }
    }

    /**
     * 判断字符串是否为连续字符 abcdef 45678
     */
    public static boolean isContinuousStr(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char curChar = str.charAt(i);
            char nextChar = (char) (curChar + 1);
            if (i + 1 < len) {
                nextChar = str.charAt(i + 1);
            }
            if (nextChar != (curChar + 1)) {
                return false;
            }
        }
        return true;
    }

    public static final String REGULAR_NUMBER = "(-?[0-9]+)(,[0-9]+)*(\\.[0-9]+)?";

    /**
     * 为字符串中的数字染颜色
     *
     * @param str
     *            ，待处理的字符串
     * @param color
     *            ，需要染的颜色
     * @return
     */
    public static SpannableString setDigitalColor(String str, int color) {
        if (str == null)
            return null;
        SpannableString span = new SpannableString(str);

        Pattern p = Pattern.compile(REGULAR_NUMBER);
        Matcher m = p.matcher(str);
        while (m.find()) {
            int start = m.start();
            int end = start + m.group().length();
            span.setSpan(new ForegroundColorSpan(color), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return span;
    }

    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    public static String getFixedNumber(String str, int length) {
        if (str == null || length <= 0 || str.length() < length) {
            return null;
        }
        Log.d(TAG, "getFixedNumber, str is : " + str);
        Pattern p = Pattern.compile("\\d{" + length + "}");
        Matcher m = p.matcher(str);
        String result = null;
        if (m.find()) {
            int start = m.start();
            int end = start + m.group().length();
            result = str.substring(start, end);
        }

        return result;
    }

    public static int getLengthWithoutSpace(CharSequence s) {
        int len = s.length();

        int rlen = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) != ' ')
                rlen++;
        }

        return rlen;
    }

    /**
     * 获取控件的宽度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(TextView view) {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(TextView view) {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        // int width =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // int height =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // view.measure(width,height);

        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static String getDisDsrc(float dis) {
        if (dis <= 0) {
            return "";
        }
        String disStr = null;
        if (dis > 1000) {
            disStr = (float) Math.round(dis / 1000 * 10) / 10 + "km";
        } else {
            disStr = dis + "m";
        }
        return disStr;
    }
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比�?2007/02/29会被接受，并转换�?2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或�?�NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 将时间戳转换为字符串
     * @param time
     * @param pattern
     * @return
     */
    public static String DataMilltoString(Long time,String pattern){
        if(time>0){
            return toString(new Date(time * 1000), pattern);
        }
        return "";
    }

    /**
     * 把日期转换成字符串型
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern){
        if(date == null){
            return "";
        }
        if(pattern == null){
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

    /**
     * 字符串为数字
     */
    public static boolean isNumber(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        else {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(number);
            if (m.matches())
                return true;
            else
                return false;
        }
    }

    /**
     * 带小数的数字
     */
    public static boolean isDecimal(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        else {
            Pattern p = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
            Matcher m = p.matcher(number);
            if (m.matches())
                return true;
            else
                return false;
        }
    }

    /**
     * 字符串为字母
     */
    public static boolean isLetter(String letter) {
        if (TextUtils.isEmpty(letter))
            return false;
        else
            return letter.matches("^[a-zA-Z]*");
    }

    /**
     * 字符串是否含有汉字汉字
     */
    public static boolean hasChinese(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        else {
            String regEx = "[\u4e00-\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if (m.find())
                return true;
            else
                return false;
        }
    }


    /**
     * 判断数字是奇数还是偶数
     */
    public static int isEvenNumbers(String even) {
        if (!TextUtils.isEmpty(even) && isNumber(even)) {
            int i = Integer.parseInt(even);
            if (i % 2 == 0) {
                //偶数
                return 2;
            } else {
                //奇数
                return 1;
            }
        } else {
            //不是奇数也不是偶数
            return 0;
        }
    }

    /**
     * 判断字符串是否字母开头
     */
    public static boolean isLetterBegin(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        else {
            char c = s.charAt(0);
            int i = (int) c;
            if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断字符串是否以指定内容开头
     */
    public static boolean startWithMytext(String mytext, String begin) {
        if (TextUtils.isEmpty(mytext) && TextUtils.isEmpty(begin))
            return false;
        else {
            if (mytext.startsWith(begin))
                return true;
            else
                return false;
        }
    }

    /**
     * 判断字符串是否以指定内容结尾
     */
    public static boolean endWithMytext(String mytext, String end) {
        if (TextUtils.isEmpty(mytext) && TextUtils.isEmpty(end))
            return false;
        else {
            if (mytext.endsWith(end))
                return true;
            else
                return false;
        }
    }

    /**
     * 判断字符串中是否含有指定内容
     */
    public static boolean hasMytext(String string, String mytext) {
        if (TextUtils.isEmpty(string) && TextUtils.isEmpty(mytext))
            return false;
        else {
            if (string.contains(mytext))
                return true;
            else
                return false;
        }
    }


    /**
     * 验证是否是手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    新增：17开头的电话号码，如170、177
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证是否是邮箱格式格式
     */
    public static boolean isEmailAdd(String email) {
        String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (TextUtils.isEmpty(email))
            return false;
        else
            return email.matches(emailRegex);
    }


    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static String IDCardValidate(String IDStr) throws ParseException, ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }
    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 生成单例对象
     * @param cls
     * @param context
     * @return
     */
    public static Object createInstance(Class<?> cls,Context context) {
        Object obj;
        try {
            Constructor c1 = cls.getDeclaredConstructor(Context.class);
            c1.setAccessible(true);
            obj = c1.newInstance(context);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public static void LogerE(String tag,String message){
        if (FrameInit.LOG){
            Log.e(tag,message);
        }
    }

    /**
     * 数组转换ArrayList
     * @param data 数组源数据
     * @param <T>  泛型
     * @return     ArrayList
     */
    public static <T> ArrayList<T> TranformList(T[] data){
        if (data==null||data.length==0){
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        for (T t : data) {
            list.add(t);
        }
        return list;
    }

    /**获取虚拟功能键高度 */
    public static int getVirtualBarHeigh(AppCompatActivity activity) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }
}
