package com.sdc.ditiezu.entry;

import java.io.Serializable;

/**
 * Description: 帖子列表实体类
 * CreateTime: 2018/8/11 14:16
 * Author: ShengDecheng
 */

public class PostListEntry implements Serializable {

    private String post_icon;
    private String post_line;
    private String post_title;
    private String is_has_img;
    private String is_new_post;
    private String post_url;
    private String post_creator;
    private String reply_count;
    private String last_time;
    private String last_user;

    public String getPost_icon() {
        return post_icon;
    }

    public void setPost_icon(String post_icon) {
        this.post_icon = post_icon;
    }

    public String getPost_line() {
        return post_line;
    }

    public void setPost_line(String post_line) {
        this.post_line = post_line;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getIs_has_img() {
        return is_has_img;
    }

    public void setIs_has_img(String is_has_img) {
        this.is_has_img = is_has_img;
    }

    public String getIs_new_post() {
        return is_new_post;
    }

    public void setIs_new_post(String is_new_post) {
        this.is_new_post = is_new_post;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getPost_creator() {
        return post_creator;
    }

    public void setPost_creator(String post_creator) {
        this.post_creator = post_creator;
    }

    public String getReply_count() {
        return reply_count;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getLast_user() {
        return last_user;
    }

    public void setLast_user(String last_user) {
        this.last_user = last_user;
    }
}
