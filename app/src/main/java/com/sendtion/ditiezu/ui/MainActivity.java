package com.sendtion.ditiezu.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.sendtion.ditiezu.R;
import com.sendtion.ditiezu.adapter.MySubwayListAdapter;
import com.sendtion.ditiezu.callback.JsonCallback;
import com.sendtion.ditiezu.entry.SubwayListEntry;
import com.sendtion.ditiezu.entry.UpdateEntry;
import com.sendtion.ditiezu.util.AppUtil;
import com.sendtion.ditiezu.util.JsoupUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地铁族
 * http://www.ditiezu.com/forum.php?gid=2   都市地铁 列表
 */
public class MainActivity extends BaseActivity {

    private String subwayUrl = "http://www.ditiezu.com/forum.php?gid=2";
    private String updateUrl = "http://app.sendtion.cn/app/ditiezu/version.json";

    @BindView(R.id.rv_list_subway)
    RecyclerView mListSubway;
    @BindView(R.id.swipe_fresh_subway)
    SwipeRefreshLayout swipeRefreshSubway;

    private MySubwayListAdapter mAdapter;
    private List<SubwayListEntry> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        swipeRefreshSubway.setRefreshing(true);
        getSubwayListData();

        checkUpdate();
    }

    private void initView(){
        mDatas = new ArrayList<>();

        setTitle("地铁族");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListSubway.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.color_d)));
        mListSubway.addItemDecoration(itemDecoration); //设置分割线

        mAdapter = new MySubwayListAdapter();
        mAdapter.setDatas(mDatas);
        mListSubway.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MySubwayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SubwayListEntry subwayListEntry = mDatas.get(position);

                Intent intent = new Intent(MainActivity.this, PostListActivity.class);
                intent.putExtra("subway_url", subwayListEntry.getSubway_url());
                intent.putExtra("subway_name", subwayListEntry.getSubway_name()+subwayListEntry.getToday_post());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new MySubwayListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                //SubwayListEntry subwayListEntry = mDatas.get(position);
            }
        });

        swipeRefreshSubway.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubwayListData();
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

    private void checkUpdate() {
        OkGo.<UpdateEntry>get(updateUrl + "?v=2018")
                .tag(this)
                .execute(new JsonCallback<UpdateEntry>(UpdateEntry.class){

                    @Override
                    public void onSuccess(Response<UpdateEntry> response) {
                        UpdateEntry updateEntry = response.body();
                        long version_code = updateEntry.getVersion_code();
                        if (AppUtil.getVersionCode(MainActivity.this) < version_code){
                            showUpdateDialog(updateEntry);
                        }
                    }
                });
    }

    private void showUpdateDialog(final UpdateEntry updateEntry){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage(updateEntry.getUpdate_log());
        builder.setNegativeButton("稍后更新", null);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String apk_url = updateEntry.getApk_url();
                //Log.e("@@@", "apk_url: " + apk_url );
                downloadApk(apk_url);
            }
        });
        builder.create().show();
    }

    //TODO 需要做权限处理
    private void downloadApk(String apkUrl){
        OkGo.<File>get(apkUrl)
                .tag(this)
                .execute(new FileCallback("/storage/emulated/0/Download/", "ditiezu.apk") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File apkFile = response.body();
                        AppUtil.installApkByPath(MainActivity.this, apkFile);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }
                });
    }

}
