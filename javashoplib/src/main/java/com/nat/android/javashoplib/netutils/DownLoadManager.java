package com.nat.android.javashoplib.netutils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import com.nat.android.javashoplib.utils.CurrencyUtils;

/**
 * 本地写入工具类
 */

public class DownLoadManager implements BaseDownLoadManager {

    /**
     * Log TAG信息
     */
    public static final String TAG = "DownLoadManager";

    /**
     * APK contentType
     */
    public static String APK_CONTENTTYPE = "application/octet-stream";

    /**
     * APK contentType
     */
    public static String APK_CONTENTTYPE2 = "application/vnd.android.package-archive";

    /**
     * png contentType
     */
    public static String PNG_CONTENTTYPE = "image/png";

    /**
     * jpg contentType
     */
    public static String JPG_CONTENTTYPE = "image/jpg";

    /**
     * 文件后缀
     */
    public static String fileSuffix="";

    /**
     * 文件存储路径
     */
    private String path=null;

    /**
     * 使用无参构造方法 使用默认路径 默认文件名
     */
    public DownLoadManager() {

    }

    /**
     * 使用一参构造方法 自定义存储文件的路径 以及文件名
     * @param path
     */
    public DownLoadManager(String path) {
        this.path = path;
    }

    /**
     * 写入本地操作
     * @param context   上下文
     * @param body      ResponseBody
     * @return          是否存储完成
     */
    public  boolean  writeResponseBodyToDisk(Context context, ResponseBody body) {
        //记录文件类型
        Log.d(TAG, "contentType:>>>>" + body.contentType().toString());
        //如果路径为空则使用默认路径
        if (path==null||path.equals("")){
            String type = body.contentType().toString();
            //判断文件类型使用相应后缀
            if (type.equals(APK_CONTENTTYPE)||type.equals(APK_CONTENTTYPE2)) {
                fileSuffix = ".apk";
            } else if (type.equals(PNG_CONTENTTYPE)) {
                fileSuffix = ".png";
            }
            //生成最终path，默认在应用程序根目录
            path = context.getExternalFilesDir(null) + File.separator + System.currentTimeMillis() + fileSuffix;
        }
        //记录存储路径
        Log.d(TAG, "path:>>>>" + path);

        try {
            //根据路径生成file
            File file = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                //一次写入4096
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();

                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                }

                outputStream.flush();

                return true;

            } catch (IOException e) {

                return false;

            } finally {
                if (inputStream != null) {

                    inputStream.close();

                }

                if (outputStream != null) {

                    outputStream.close();

                }
            }
        } catch (IOException e) {

            return false;

        }
    }
}
