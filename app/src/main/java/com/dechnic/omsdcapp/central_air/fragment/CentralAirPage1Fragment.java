package com.dechnic.omsdcapp.central_air.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.base.LazyFragment;
import com.dechnic.omsdcapp.commons.Constants;
import com.dechnic.omsdcapp.commons.NetWorkUtils;

/**
 * Created by Administrator on 2017/5/4.
 */

@SuppressWarnings("WrongConstant")
public class CentralAirPage1Fragment extends LazyFragment {

    WebView webView;
    private boolean isPrepared;

    String url = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_central_air_page1);
          url = "http://vrv.daikin-china.com.cn/#/login";
//        url = HttpURL.Url(getContext())+HttpURL.DAIILELECTRIC+"?access_token="+AppUtils.getAccessToken(getActivity());
        Log.e("==url",url);
        initViews();
        return getContentView();
    }

    private void initViews(){
        webView = (WebView) findViewById(R.id.webView);
        WebSettings setting = webView.getSettings();
        setting.setBlockNetworkImage(false);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);//add by houqida 12019-12-31 h5  添加本地存储
        webView.setScrollBarStyle(0);
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        if (NetWorkUtils.getNetState(getActivity())==Constants.NO_NETWORK) {//没有网络时
            setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//使用缓存
        }else {//有网络时
            //不使用缓存：
            setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        isPrepared = true;
        if (isVisible) {
            Log.e("请求连接","initViews");
            getlocal();
        }

    }

    private void getlocal() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showHttpLoading();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        webView.loadUrl(url);
    }
    @Override
    protected void lazyLoad() {
        if (isVisible&&isPrepared) {
            getlocal();
            Log.e("请求连接","lazyLoad");
        }
    }

    private ProgressDialog dialog;

    public void showHttpLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            // 设置进度条风格，风格为圆形，旋转的
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("请稍等...");
        }
        dialog.show();
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }
}
