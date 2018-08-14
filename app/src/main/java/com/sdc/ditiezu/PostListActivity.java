package com.sdc.ditiezu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.amnix.adblockwebview.ui.AdBlocksWebViewActivity;
import com.sdc.ditiezu.adapter.MyPostListAdapter;
import com.sdc.ditiezu.entry.PostListEntry;
import com.sdc.ditiezu.util.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostListActivity extends BaseActivity {

    private String subwayUrl;
    private String subwayName;

    @BindView(R.id.rv_list_post)
    RecyclerView mListPost;
    @BindView(R.id.swipe_fresh_post)
    SwipeRefreshLayout swipeRefreshPost;

    private MyPostListAdapter mAdapter;
    private List<PostListEntry> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);

        AdBlocksWebViewActivity.init(this); //Add this Line in Application OnCreate or Activity.

        initView();

        swipeRefreshPost.setRefreshing(true);
        getPostListData();
    }

    private void initView(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Intent intent = getIntent();
        subwayUrl = intent.getStringExtra("subway_url");
        subwayName = intent.getStringExtra("subway_name");
        setTitle(subwayName);

        mDatas = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListPost.setLayoutManager(layoutManager);
        //mListArticle.addItemDecoration(new MyPaddingDecoration(this)); //设置分割线

        mAdapter = new MyPostListAdapter();
        mAdapter.setDatas(mDatas);
        mListPost.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyPostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PostListEntry postListEntry = mDatas.get(position);

                // AdBlock去广告
                AdBlocksWebViewActivity.startWebView(PostListActivity.this, postListEntry.getPost_url(),
                        getResources().getColor(R.color.colorPrimary));
            }
        });

        mAdapter.setOnItemLongClickListener(new MyPostListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                PostListEntry postListEntry = mDatas.get(position);

                Intent intent = new Intent(PostListActivity.this, WebViewActivity.class);
                intent.putExtra("post_url", postListEntry.getPost_url());
                startActivity(intent);
            }
        });

        swipeRefreshPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostListData();
            }
        });
    }

    private void getPostListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = JsoupUtil.getPostList(subwayUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshPost.setRefreshing(false);
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyDataSetChanged();
                    }
                });
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
}
