package com.sendtion.ditiezu.ui;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sendtion.ditiezu.util.AppManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Description:
 * CreateTime: 2018/8/14 11:28
 * Author: ShengDecheng
 */

public abstract class BaseActivity extends AppCompatActivity {

    public void onCreate(@Nullable int resourceId) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(resourceId);

        initActivity();
        initView();
        initData();
        loadData();
    }

    protected abstract void initView();

    protected void initData() {
    }

    protected void loadData() {
    }

    private void initActivity() {
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        //初始化EventBus
        if (isBindEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //初始化ButterKnife
        ButterKnife.bind(this);
    }

    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        //解除对EventBus的绑定
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
        System.gc();
    }
}
