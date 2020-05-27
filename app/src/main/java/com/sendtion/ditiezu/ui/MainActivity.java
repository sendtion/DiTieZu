package com.sendtion.ditiezu.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sendtion.ditiezu.R;
import com.sendtion.ditiezu.adapter.MySubwayListAdapter;
import com.sendtion.ditiezu.entry.SubwayListEntry;
import com.sendtion.ditiezu.entry.UpdateEntry;
import com.sendtion.ditiezu.util.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 地铁族
 * http://www.ditiezu.com/forum.php?gid=2   都市地铁 列表
 */
public class MainActivity extends BaseActivity {

    private static final String UPDATE_URL = "http://app.sendtion.cn/app/ditiezu/version.json";
    private String subwayUrl = "http://www.ditiezu.com/forum.php?gid=2";

    @BindView(R.id.rv_list_subway)
    RecyclerView mListSubway;
    @BindView(R.id.swipe_fresh_subway)
    SwipeRefreshLayout swipeRefreshSubway;

    private DownloadManager downloadManager;
    private UpdateConfiguration configuration;
    private MySubwayListAdapter mAdapter;
    private List<SubwayListEntry> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_main);
    }

    @Override
    protected void initView(){
        mDatas = new ArrayList<>();

        setTitle("地铁族");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListSubway.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.color_d)));
        mListSubway.addItemDecoration(itemDecoration); //设置分割线

        //检查更新配置
        downloadManager = DownloadManager.getInstance(this);
        configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                //.setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(true)
                //设置强制更新
                .setForcedUpgrade(false);
                //设置对话框按钮的点击监听
                //.setButtonClickListener(this)
                //设置下载过程的监听
                //.setOnDownloadListener(this);

        mAdapter = new MySubwayListAdapter();
        mAdapter.setDatas(mDatas);
        mListSubway.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MySubwayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SubwayListEntry subwayListEntry = mDatas.get(position);

                if (subwayListEntry != null) {
                    String todayPost = subwayListEntry.getToday_post();
                    if (todayPost == null) {
                        todayPost = "(0)";
                    }
                    Intent intent = new Intent(MainActivity.this, PostListActivity.class);
                    intent.putExtra("subway_url", subwayListEntry.getSubway_url());
                    intent.putExtra("subway_name", subwayListEntry.getSubway_name()+todayPost);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setOnItemLongClickListener(position -> {
            SubwayListEntry subwayListEntry = mDatas.get(position);

            if (subwayListEntry != null) {
                showToast(subwayListEntry.getSubway_name());
            }
        });

        swipeRefreshSubway.setOnRefreshListener(() -> getSubwayListData());
    }

    @Override
    protected void initData() {
        // 首次刷新数据
        swipeRefreshSubway.setRefreshing(true);
        getSubwayListData();

        // 检查更新
        checkUpdate();
    }

    public void checkUpdate(){
        OkGo.<String>get(UPDATE_URL + "?time="+System.currentTimeMillis())             // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)   // 缓存模式，详细请看缓存介绍
                .cacheTime(3000)//缓存时间
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("response", response.body());
                        UpdateEntry updateEntry = new Gson().fromJson(response.body(), UpdateEntry.class);
                        downloadManager.setApkName("DiTieZu.apk")
                                .setApkUrl(updateEntry.getApk_url())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setShowNewerToast(false)
                                .setConfiguration(configuration)
                                .setApkVersionCode(updateEntry.getVersion_code())
                                .setApkVersionName(updateEntry.getVersion_name())
                                .setApkSize(updateEntry.getApk_size())
                                .setApkDescription(updateEntry.getUpdate_log())
                                .download();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void getSubwayListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = JsoupUtil.getSubwayList(subwayUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshSubway.setRefreshing(false);
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

}
