package com.sdc.ditiezu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webclient.webchromeclient.AgentChromeClient;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 加载网页
 * https://github.com/JakePrim/PrimWeb
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "@@@@@@";

    @BindView(R.id.layout_web)
    RelativeLayout layoutWeb;

    private PrimWeb primWeb;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null){
            String post_url = intent.getStringExtra("post_url");
            Log.e(TAG, "post_url: " + post_url );

            primWeb = PrimWeb.with(this)
                    .setWebParent(layoutWeb, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultUI()
                    .useDefaultTopIndicator()
                    .setWebViewType(PrimWeb.WebViewType.X5)
                    .setWebChromeClient(agentChromeClient)
                    .setWebViewClient(agentWebViewClient)
                    .alwaysOpenOtherPage(false)
                    .buildWeb()
                    .lastGo()
                    .launch(post_url);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!primWeb.handlerBack()) {
                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        primWeb.webLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        primWeb.webLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        primWeb.webLifeCycle().onDestory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (primWeb.handlerKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    AgentWebViewClient agentWebViewClient = new AgentWebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading: WebResourceRequest -->　" + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

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
    };

    com.tencent.smtt.sdk.WebViewClient x5WebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
            Log.e(TAG, "shouldOverrideUrlLoading: x5 --> " + s);
            return super.shouldOverrideUrlLoading(webView, s);
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
            Log.e(TAG, "shouldOverrideUrlLoading: x5 webResourceRequest --> " + webResourceRequest.getUrl());
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    };

    AgentChromeClient agentChromeClient = new AgentChromeClient() {
        @Override
        public void onReceivedTitle(View webView, String s) {
            super.onReceivedTitle(webView, s);
            if (actionBar != null) {
                actionBar.setTitle(s);
            }
        }
    };
}
