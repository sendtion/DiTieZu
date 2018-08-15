package com.sendtion.ditiezu.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * CreateTime: 2018/8/11 14:16
 * Author: ShengDecheng
 */

public class SubwayListEntry implements Serializable {

    private String subway_icon;
    private String subway_name;
    private String subway_url;
    private String today_post;
    private String subway_desc;
    private List<Moderator> moderators;
    private String post_count;
    private String last_post;
    private String last_time;
    private String last_user;

    public String getSubway_icon() {
        return subway_icon;
    }

    public void setSubway_icon(String subway_icon) {
        this.subway_icon = subway_icon;
    }

    public String getSubway_name() {
        return subway_name;
    }

    public void setSubway_name(String subway_name) {
        this.subway_name = subway_name;
    }

    public String getSubway_url() {
        return subway_url;
    }

    public void setSubway_url(String subway_url) {
        this.subway_url = subway_url;
    }

    public String getToday_post() {
        return today_post;
    }

    public void setToday_post(String today_post) {
        this.today_post = today_post;
    }

    public String getSubway_desc() {
        return subway_desc;
    }

    public void setSubway_desc(String subway_desc) {
        this.subway_desc = subway_desc;
    }

    public List<Moderator> getModerators() {
        return moderators;
    }

    public void setModerators(List<Moderator> moderators) {
        this.moderators = moderators;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getLast_post() {
        return last_post;
    }

    public void setLast_post(String last_post) {
        this.last_post = last_post;
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

    public static class Moderator {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
