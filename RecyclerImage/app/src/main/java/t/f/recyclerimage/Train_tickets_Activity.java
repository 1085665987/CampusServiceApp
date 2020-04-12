package t.f.recyclerimage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import utils.StatusBarLightModeUtil;
import views.CommomDialog;

/**
 * Created by Friday on 2018/8/6.
 */

public class Train_tickets_Activity extends Activity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private WebView webView;
    private TextView top_txt;
    private String TAG="Train_tickets_Activity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_ticket);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        checkPermission();
        initView();
    }

    private void initView() {
        top_txt=(TextView)findViewById(R.id.top_txt);

        webView=(WebView)findViewById(R.id.webview);
        String u="http://1.eggplant1.applinzi.com/perfect/shopping/index.html";
        String xiecheng_url=getResources().getString(R.string.buy_train_url);
        xiecheng_url="https://m.ctrip.com/webapp/train/?from=https%3A%2F%2Fm.ctrip.com%2Fhtml5%2F%3Fsourceid%3D497%26allianceid%3D4897%26sid%3D182042%26utm_source%3Dbaidu%26utm_medium%3Dcpc%26utm_campaign%3Dbaidu497%26popup%3Dclose&undefined=#/index?VNK=70432097";
        webView.loadUrl(xiecheng_url);

        webView.addJavascriptInterface(Train_tickets_Activity.this,"android");
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings=webView.getSettings();
        webSettings.setDomStorageEnabled(true);
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

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
    }


    ///WebViewClient帮助webview处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {                  //页面加载完成
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {   //页面开始加载做啥
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            view.loadUrl(url);
            return true;
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
            if(newProgress==100){
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //webView.canGoBack();是否有上一个页面
        if(webView.canGoBack()&&keyCode==KeyEvent.KEYCODE_BACK){
            webView.goBack();               //返回上一级web页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * JS调用android的方法
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str){
        Log.i("ansen","html调用客户端:"+str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        webView.destroy();
        webView=null;
    }
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            //Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkPermission: 已经授权！");
        }
    }
}
