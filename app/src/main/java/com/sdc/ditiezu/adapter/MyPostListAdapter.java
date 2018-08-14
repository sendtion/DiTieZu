package com.sdc.ditiezu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdc.ditiezu.R;
import com.sdc.ditiezu.entry.PostListEntry;
import com.sdc.ditiezu.util.SpanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 帖子列表
 * CreateTime: 2018/6/11 10:30
 * Author: ShengDecheng
 */

public class MyPostListAdapter extends RecyclerView.Adapter<MyPostListAdapter.ViewHolder> {
    private List<PostListEntry> datas;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public MyPostListAdapter(){
        datas = new ArrayList<>();
    }

    public void setDatas(List<PostListEntry> datas) {
        this.datas = datas;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.setData(position);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView tv_post_list_title;
        private TextView tv_post_last_time;
        private TextView tv_post_reply_read;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            tv_post_list_title = (TextView) itemView.findViewById(R.id.tv_post_list_title);
            tv_post_last_time = (TextView) itemView.findViewById(R.id.tv_post_last_time);
            tv_post_reply_read = (TextView) itemView.findViewById(R.id.tv_post_reply_read);
        }

        public void setData(final int position) throws Exception {
            PostListEntry postListEntry = datas.get(position);

            if (postListEntry != null) {
                String line = postListEntry.getPost_line(); //线路
                if (line != null) {
                    line = "[" + line + "] ";
                } else {
                    line = "";
                }
                String isNewPost = postListEntry.getIs_new_post(); //是否新帖
                if (isNewPost == null){
                    isNewPost = "";
                } else {
                    isNewPost = " " + isNewPost;
                }

                SpanUtils spanUtils = new SpanUtils();
                spanUtils = spanUtils.append(line).append(postListEntry.getPost_title());

                String isHasImg = postListEntry.getIs_has_img(); //是否有图片
                if (isHasImg != null){
                    spanUtils = spanUtils.appendImage(R.drawable.image_s);
                }
                spanUtils.append(isNewPost).setForegroundColor(view.getResources().getColor(R.color.color_f87d76));
                //tv_post_list_title.setText(line + postListEntry.getPost_title() + " " + isNewPost);
                tv_post_list_title.setText(spanUtils.create());
                tv_post_last_time.setText(postListEntry.getLast_time());
                tv_post_reply_read.setText(postListEntry.getReply_count());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }
    }
}
