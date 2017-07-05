package com.nat.android.javashoplib.utils;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JWebViewClient extends WebViewClient
{
    private WebViewListener lis;

    public JWebViewClient()
    {
    }

    public JWebViewClient(WebViewListener paramWebViewListener)
    {
        this.lis = paramWebViewListener;
    }

    public void onLoadResource(WebView paramWebView, String paramString)
    {
        super.onLoadResource(paramWebView, paramString);
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
        if (this.lis != null)
            this.lis.over();
        super.onPageFinished(paramWebView, paramString);
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
        if (this.lis != null)
            this.lis.start();
        super.onPageStarted(paramWebView, paramString, paramBitmap);
    }

    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
        if (this.lis != null)
            this.lis.over();
        paramWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        paramWebView.loadDataWithBaseURL("", "<div style='padding-top:200px;text-align:center;color:#666;'>无网络，请打开网络下拉刷新！</div>", "text/html", "UTF-8", "");
        super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
    }

    public void onReceivedHttpError(WebView paramWebView, WebResourceRequest paramWebResourceRequest, WebResourceResponse paramWebResourceResponse)
    {
        if (this.lis != null)
            this.lis.over();
        super.onReceivedHttpError(paramWebView, paramWebResourceRequest, paramWebResourceResponse);
    }

    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
        paramSslErrorHandler.proceed();
    }

    public boolean shouldOverrideKeyEvent(WebView paramWebView, KeyEvent paramKeyEvent)
    {
        return super.shouldOverrideKeyEvent(paramWebView, paramKeyEvent);
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
        paramWebView.loadUrl(paramString);
        return true;
    }

    public static abstract interface WebViewListener
    {
        public abstract void error();

        public abstract void over();

        public abstract void start();
    }
}