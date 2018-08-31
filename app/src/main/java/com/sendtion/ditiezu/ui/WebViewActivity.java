package com.sendtion.ditiezu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.sendtion.ditiezu.R;
import com.sendtion.ditiezu.util.ADFilterTool;

import butterknife.BindView;

/**
 * 加载网页
 * https://github.com/JakePrim/PrimWeb
 */
public class WebViewActivity extends BaseActivity {

    private static final String TAG = "@@@@@@";

    @BindView(R.id.layout_web)
    RelativeLayout layoutWeb;

    //private PrimWeb primWeb;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_web_view);

    }

    @Override
    protected void initView() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null){
            String post_url = intent.getStringExtra("post_url");
            Log.e(TAG, "post_url: " + post_url );

//            primWeb = PrimWeb.with(this)
//                    .setWebParent(layoutWeb, new ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//                    .useDefaultUI()
//                    .useDefaultTopIndicator()
//                    .setWebViewType(PrimWeb.WebViewType.X5)
//                    .setWebChromeClient(agentChromeClient)
//                    .setWebViewClient(webViewClient)
//                    .alwaysOpenOtherPage(false)
//                    .buildWeb()
//                    .lastGo()
//                    .launch(post_url);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                if (!primWeb.handlerBack()) {
//                    this.finish();
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        primWeb.webLifeCycle().onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        primWeb.webLifeCycle().onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        primWeb.webLifeCycle().onDestory();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (primWeb.handlerKeyEvent(keyCode, event)) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    AgentWebViewClient agentWebViewClient = new AgentWebViewClient() {
//        @Override
//        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
//            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public boolean shouldOverrideUrlLoading(IAgentWebView view, WebResourceRequest request) {
//            Log.e(TAG, "shouldOverrideUrlLoading: WebResourceRequest -->　" + request.getUrl());
//            return super.shouldOverrideUrlLoading(view, request);
//        }
//
//        @Override
//        public void onPageFinished(IAgentWebView view, String url) {
//            super.onPageFinished(view, url);
//            String js = getClearAdDivJs(WebViewActivity.this);
//            Log.e("adJs", js);
//            view.loadAgentUrl(js);
//        }
//    };

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading: android --> " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading: android WebResourceRequest --> " + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url){
            url= url.toLowerCase();
            if(!ADFilterTool.hasAd(view.getContext(),url)){
                return super.shouldInterceptRequest(view,url);
            }else{
                return new WebResourceResponse(null,null,null);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 注入js去除网页广告
            String js = getClearAdDivJs(WebViewActivity.this);
            Log.e("adJs", js);
            view.loadUrl(js); //有点效果
            //view.loadUrl("JavaScript:function setTop(){document.querySelector('ins.adsbygoogle').style.display=\"none\";}setTop();");
        }
    };

    /**
     * 去除网页广告
     */
    public static String getClearAdDivJs(Context context){
        String js = "javascript:";
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for(int i=0;i<adDivs.length;i++){
            js += "var adDiv"+i+"= document.getElementsByClassName('"+adDivs[i]+"');if(adDiv"+i+" != null)adDiv"+i+".parentNode.removeChild(adDiv"+i+");";
        }
        return js;
    }

//    com.tencent.smtt.sdk.WebViewClient x5WebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
//        @Override
//        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
//            Log.e(TAG, "shouldOverrideUrlLoading: x5 --> " + s);
//            return super.shouldOverrideUrlLoading(webView, s);
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
//            Log.e(TAG, "shouldOverrideUrlLoading: x5 webResourceRequest --> " + webResourceRequest.getUrl());
//            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
//        }
//
//        @Override
//        public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, String url) {
//            //做广告拦截，ADFIlterTool 为广告拦截工具类
//            if (!ADFilterTool.hasAd(webView.getContext(),url)){
//                return super.shouldInterceptRequest(webView, url);
//            }else {
//                return new com.tencent.smtt.export.external.interfaces.WebResourceResponse(null, null, null);
//            }
//        }
//
//    };

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    };

//    AgentChromeClient agentChromeClient = new AgentChromeClient() {
//        @Override
//        public void onReceivedTitle(View webView, String s) {
//            super.onReceivedTitle(webView, s);
//            if (actionBar != null) {
//                actionBar.setTitle(s);
//            }
//        }
//    };
}
