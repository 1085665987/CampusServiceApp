package t.f.recyclerimage.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import t.f.recyclerimage.R;

/**
 * Created by Friday on 2018/7/11.
 */

public class Trade_Fragment extends Fragment {
    private  View view;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private WebView webView;
    private TextView top_txt;
    private String TAG="Trade_Fragment";
    private ProgressBar progressBar;

    private String url="http://1.eggplant1.applinzi.com/perfect/shopping/index.html";

    private boolean isFirst=true;
    // 标志位，标志已经初始化完成。
    protected boolean isVisible;
    private boolean isPrepared;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.train_ticket, null);
            initView();
            isPrepared = true;
            lazyLoad();
        }
        return view;
    }
    private void initView() {
        top_txt=(TextView)view.findViewById(R.id.top_txt);

        webView=(WebView)view.findViewById(R.id.webview);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //这里处理返回键事件
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //加上这一行网页为响应式
        //允许使用js
        //    LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //    LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //    LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //    LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        // 支持屏幕缩放
        webSettings.setSupportZoom(true);
        //不显示webview缩放按钮 //
        webSettings.setDisplayZoomControls(false);

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
    }


    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);//不跳转浏览器
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    };
    //WebChromeClient帮助webview处理js的对话框网站图标，网站title，加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient(){

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
            builder.setMessage(message).setPositiveButton("确定",null);
            builder.setCancelable(false);
            builder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //得到网页标题title
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            top_txt.setText(title);
        }

        //加载进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            Log.e(TAG,"onProgressChanged"+newProgress);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView=null;
    }

    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        //加载数据
        if(isFirst) {
            webView.loadUrl(url);
            isFirst=false;
        }
    }
    protected void onVisible(){         lazyLoad();     }

    @JavascriptInterface //仍然必不可少
    public void getClient(String str){
        Log.i("ansen","html调用客户端:"+str);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
        }
    }
}
