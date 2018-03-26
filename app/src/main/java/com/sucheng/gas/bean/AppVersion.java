package com.sucheng.gas.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class AppVersion {

    private String updateMessage;
    private String url;
    private int versionCode;

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
