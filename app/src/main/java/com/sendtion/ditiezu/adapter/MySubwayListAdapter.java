package com.sendtion.ditiezu.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sendtion.ditiezu.R;
import com.sendtion.ditiezu.entry.SubwayListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * CreateTime: 2018/6/11 10:30
 * Author: ShengDecheng
 */

public class MySubwayListAdapter extends RecyclerView.Adapter<MySubwayListAdapter.ViewHolder> {
    private List<SubwayListEntry> datas;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public MySubwayListAdapter(){
        datas = new ArrayList<>();
    }

    public void setDatas(List<SubwayListEntry> datas) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subway, parent, false);
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
        private ImageView iv_subway_icon;
        private TextView tv_subway_name;
        private TextView tv_subway_today;
        private TextView tv_subway_count;
        private TextView tv_subway_desc;
        private TextView tv_subway_moderator;
        private TextView tv_subway_last_time;
        private TextView tv_subway_last_user;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            iv_subway_icon = (ImageView) itemView.findViewById(R.id.iv_subway_icon);
            tv_subway_name = (TextView) itemView.findViewById(R.id.tv_subway_name);
            tv_subway_today = (TextView) itemView.findViewById(R.id.tv_subway_today);
            tv_subway_count = (TextView) itemView.findViewById(R.id.tv_subway_count);
            tv_subway_desc = (TextView) itemView.findViewById(R.id.tv_subway_desc);
            tv_subway_moderator = (TextView) itemView.findViewById(R.id.tv_subway_moderator);
            tv_subway_last_time = (TextView) itemView.findViewById(R.id.tv_subway_last_time);
            tv_subway_last_user = (TextView) itemView.findViewById(R.id.tv_subway_last_user);
        }

        public void setData(final int position) throws Exception {
            SubwayListEntry subwayListEntry = datas.get(position);

            if (subwayListEntry != null) {
                String subwayName = subwayListEntry.getSubway_name();
                if (!TextUtils.isEmpty(subwayName)) {
                    //地铁logo
                    Glide.with(view.getContext()).load(subwayListEntry.getSubway_icon()).into(iv_subway_icon);
                    //地铁名称
                    tv_subway_name.setText(subwayName);
                    //今日帖子
                    tv_subway_today.setText(subwayListEntry.getToday_post());
                    //帖子总量
                    tv_subway_count.setText(subwayListEntry.getPost_count());
                    //地铁简介
                    tv_subway_desc.setText(subwayListEntry.getSubway_desc());

                    //版主
                    tv_subway_moderator.setText("— —");
                    List<SubwayListEntry.Moderator> moderatorList = subwayListEntry.getModerators(); //版主
                    if (moderatorList != null && moderatorList.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < moderatorList.size(); i++) {
                            SubwayListEntry.Moderator moderator = moderatorList.get(i);
                            sb.append(moderator.getName());
                            if (i < moderatorList.size() - 1) {
                                sb.append(",");
                            }
                        }
                        tv_subway_moderator.setText(sb.toString());
                    }
                    //最后发表时间
                    tv_subway_last_time.setText(subwayListEntry.getLast_time());
                    //最后发表人
                    tv_subway_last_user.setText(subwayListEntry.getLast_user());
                }
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
