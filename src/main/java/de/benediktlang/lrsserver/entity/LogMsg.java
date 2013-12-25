package de.benediktlang.lrsserver.entity;

import com.google.gson.Gson;

public class LogMsg {
    private String profile;
    private String msg;
    private long time;

    public LogMsg(String profile, String msg, long time) {
        this.profile = profile;
        this.msg = msg;
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
