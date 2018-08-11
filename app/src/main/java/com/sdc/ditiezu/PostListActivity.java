package com.sdc.ditiezu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sdc.ditiezu.adapter.MyPostListAdapter;
import com.sdc.ditiezu.entry.PostListEntry;
import com.sdc.ditiezu.util.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostListActivity extends AppCompatActivity {

    private String subwayUrl;
    private String subwayName;

    @BindView(R.id.rv_list_post)
    RecyclerView mListPost;

    private MyPostListAdapter mAdapter;
    private List<PostListEntry> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);

        initView();

        Intent intent = getIntent();
        subwayUrl = intent.getStringExtra("subway_url");
        subwayName = intent.getStringExtra("subway_name");
        setTitle(subwayName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = JsoupUtil.getPostList(subwayUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void initView(){
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

//                Intent intent = new Intent(PostListActivity.this, PostListActivity.class);
//                intent.putExtra("post_url", postListEntry.getPost_url());
//                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new MyPostListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                PostListEntry postListEntry = mDatas.get(position);
            }
        });
    }
}
