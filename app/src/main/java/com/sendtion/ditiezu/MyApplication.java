package com.sendtion.ditiezu;

import android.app.Application;

import com.amnix.adblockwebview.ui.AdBlocksWebViewActivity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.sendtion.ditiezu.entry.UpdateEntry;
import com.sendtion.ditiezu.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Description:
 * CreateTime: 2018/8/14 10:20
 * Author: ShengDecheng
 */

public class MyApplication extends Application {

    private static final String UPDATE_URL = "http://app.sendtion.cn/app/ditiezu/version.json";

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        initYouMeng();

        initOkGo();

        AdBlocksWebViewActivity.init(this); //Add this Line in Application OnCreate or Activity.

        //https://github.com/yjfnypeu/UpdatePlugin/wiki
        UpdateConfig.getConfig()
                .setUrl(UPDATE_URL + "?v=2018")// 配置检查更新的API接口
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        UpdateEntry updateEntry = new Gson().fromJson(response, UpdateEntry.class);
                        // 在此对response数据进行解析，并创建出对应的update实体类数据，提供给框架内部进行使用
                        Update update = new Update();
                        update.setVersionCode(updateEntry.getVersion_code());
                        update.setVersionName(updateEntry.getVersion_name());
                        update.setUpdateUrl(updateEntry.getApk_url());
                        update.setUpdateContent(updateEntry.getUpdate_log());
                        update.setForced(updateEntry.is_forced());
                        update.setIgnore(false);
                        return update;
                    }
                });
    }

    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //非必要情况，不建议使用，第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //使用内存保持cookie，app退出后，cookie消失
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                //.addCommonHeaders(headers)                      //全局公共头
                //.addCommonParams(params)                       //全局公共参数
                ;
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
