package com.nat.android.javashoplib.netutils;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 监听下载进度的自定义ResponseBody
 */

public class ProgressResponseBody extends ResponseBody
{
    /**
     * 用来计算下载进度的ResponseBody
     */
    private final ResponseBody responseBody;
    /**
     * 监听接口
     */
    private final NetServiceFactory.ProgressLisener progressListener;

    /**
     *
     */
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody,NetServiceFactory.ProgressLisener lisener){
        this.responseBody = responseBody;
          this.progressListener = lisener;
    }

    @Override
    public MediaType contentType()
    {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source()
    {
        if(bufferedSource == null)
        {
            //构建文件缓冲源
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            //已经下载的总大小
            long totalBytesRead = 0L;
            //读取下载byte
            @Override
            public long read(Buffer sink, long byteCount) throws IOException
            {
                //获取单次读取的长度
                long bytesRead = super.read(sink, byteCount);
                //计算总的下载长度
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //设置监听
                if (progressListener!=null){
                    progressListener.prigress(totalBytesRead,responseBody.contentLength(),bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }
}
