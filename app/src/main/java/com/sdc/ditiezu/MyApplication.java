package com.sdc.ditiezu;

import android.app.Application;

import com.sdc.ditiezu.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Description:
 * CreateTime: 2018/8/14 10:20
 * Author: ShengDecheng
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        initYouMeng();

    }

    private void initYouMeng() {
        //UMConfigure.init(Context context, String appkey, String channel, int deviceType, String pushSecret);
        //注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
        /**
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(getApplicationContext(), UMConfigure.DEVICE_TYPE_PHONE, null);
        //EScenarioType.E_UM_NORMAL 普通统计场景，EScenarioType.E_UM_GAME 游戏场景
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(false);
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);
    }
}
