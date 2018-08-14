package com.sdc.ditiezu;

import android.app.Application;

import com.sdc.ditiezu.util.Utils;

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
    }
}
