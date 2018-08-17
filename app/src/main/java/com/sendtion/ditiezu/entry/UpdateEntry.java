package com.sendtion.ditiezu.entry;

import java.io.Serializable;

/**
 * Description:
 * CreateTime: 2018/8/16 20:00
 * Author: ShengDecheng
 */

public class UpdateEntry implements Serializable {
    private int version_code;
    private String version_name;
    private long apk_size;
    private String apk_url;
    private String update_log;
    private boolean is_forced;

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public long getApk_size() {
        return apk_size;
    }

    public void setApk_size(long apk_size) {
        this.apk_size = apk_size;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public boolean is_forced() {
        return is_forced;
    }

    public void setIs_forced(boolean is_forced) {
        this.is_forced = is_forced;
    }
}
