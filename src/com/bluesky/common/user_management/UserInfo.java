package com.bluesky.common.user_management;

/**
 * Created by liangc on 16/05/15.
 */
public class UserInfo {
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public UserInfo(long uid, String name, String intro){
        this.uid = uid;
        this.name = name;
        this.intro = intro;
    }
    public UserInfo(){};

    public String toString(){
        return name + ":" + uid + ":" + intro;
    }

    private long uid;
    private String name;
    private String intro;
}
