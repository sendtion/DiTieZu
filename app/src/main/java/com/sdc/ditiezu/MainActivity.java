package com.sdc.ditiezu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sdc.ditiezu.adapter.MySubwayListAdapter;
import com.sdc.ditiezu.entry.SubwayListEntry;
import com.sdc.ditiezu.util.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地铁族
 * http://www.ditiezu.com/forum.php?gid=2   都市地铁 列表
 */
public class MainActivity extends AppCompatActivity {

    private String subwayUrl = "http://www.ditiezu.com/forum.php?gid=2";

    @BindView(R.id.rv_list_subway)
    RecyclerView mListSubway;

    private MySubwayListAdapter mAdapter;
    private List<SubwayListEntry> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = JsoupUtil.getSubwayList(subwayUrl);
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
        mListSubway.setLayoutManager(layoutManager);
        //mListArticle.addItemDecoration(new MyPaddingDecoration(this)); //设置分割线

        mAdapter = new MySubwayListAdapter();
        mAdapter.setDatas(mDatas);
        mListSubway.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MySubwayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SubwayListEntry subwayListEntry = mDatas.get(position);

                Intent intent = new Intent(MainActivity.this, PostListActivity.class);
                intent.putExtra("post_url", subwayListEntry.getSubway_url());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new MySubwayListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                SubwayListEntry subwayListEntry = mDatas.get(position);
            }
        });
    }

}
