package com.sendtion.ditiezu.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.AnimRes;

import java.util.Stack;

public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }


    public void finishActivity(@AnimRes final int enterAnim,
                               @AnimRes final int exitAnim) {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Activity activity, @AnimRes final int enterAnim,
                               @AnimRes final int exitAnim) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity.overridePendingTransition(enterAnim, exitAnim);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public synchronized void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
