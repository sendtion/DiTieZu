package com.sendtion.ditiezu.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.amnix.adblockwebview.ui.AdBlocksWebViewActivity;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sendtion.ditiezu.R;
import com.sendtion.ditiezu.adapter.MyPostListAdapter;
import com.sendtion.ditiezu.entry.PostListEntry;
import com.sendtion.ditiezu.util.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class PostListActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    private static final String SUFFIX = ".html";
    private String baseUrl;
    private String subwayUrl;

    @BindView(R.id.rv_list_post)
    XRecyclerView mListPost;
    @BindView(R.id.swipe_fresh_post)
    SwipeRefreshLayout swipeRefreshPost;

    private MyPostListAdapter mAdapter;
    private List<PostListEntry> mDatas;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_post_list);

    }

    @Override
    protected void initView(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Intent intent = getIntent();
        subwayUrl = intent.getStringExtra("subway_url");
        String subwayName = intent.getStringExtra("subway_name");
        setTitle(subwayName);

        //http://www.ditiezu.com/forum-64-1.html
        String currentPage = subwayUrl.substring(subwayUrl.lastIndexOf("-")+1, subwayUrl.indexOf(SUFFIX));
        //Log.e("@@@", "currentPage: " + currentPage );
        //Log.e("@@@", "url: " +  subwayUrl.substring(0, subwayUrl.lastIndexOf("-")+1));
        baseUrl = subwayUrl.substring(0, subwayUrl.lastIndexOf("-")+1);
        page = Integer.valueOf(currentPage);

        mDatas = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListPost.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.color_d)));
        mListPost.addItemDecoration(itemDecoration); //设置分割线
        mListPost.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mListPost.setPullRefreshEnabled(false);
        mListPost.setLoadingListener(this);

        mAdapter = new MyPostListAdapter();
        mAdapter.setDatas(mDatas);
        mListPost.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyPostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PostListEntry postListEntry = mDatas.get(position);

                // AdBlock去广告
                AdBlocksWebViewActivity.startWebView(PostListActivity.this, postListEntry.getPost_url(),
                        ContextCompat.getColor(PostListActivity.this, R.color.colorPrimary));
            }
        });

        mAdapter.setOnItemLongClickListener(new MyPostListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
//                PostListEntry postListEntry = mDatas.get(position);
//
//                Intent intent = new Intent(PostListActivity.this, WebViewActivity.class);
//                intent.putExtra("post_url", postListEntry.getPost_url());
//                startActivity(intent);
            }
        });

        swipeRefreshPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPostListData();
            }
        });
    }

    @Override
    protected void initData() {
        swipeRefreshPost.setRefreshing(true);
        refreshPostListData();
    }

    private void refreshPostListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = JsoupUtil.getPostList(subwayUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mListPost.refreshComplete();
                        swipeRefreshPost.setRefreshing(false);
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void loadPostListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postUrl = baseUrl + ++page + SUFFIX;
                //Log.e("@@@", "postUrl: " + postUrl );
                final List<PostListEntry> dataList = JsoupUtil.getPostList(postUrl);
                if (dataList != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListPost.loadMoreComplete();
                            //swipeRefreshPost.setRefreshing(false);
                            mDatas.addAll(dataList);
                            mAdapter.setDatas(mDatas);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Toasty.normal(PostListActivity.this, "没有更多数据了").show();
                    mListPost.loadMoreComplete();
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadPostListData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListPost != null){
            mListPost.destroy(); // this will totally release XR's memory
            mListPost = null;
        }
    }
}
