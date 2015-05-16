package com.bluesky.common.user_management;

/**
 * Created by liangc on 16/05/15.
 */
public class GroupInfo {
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
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

    public GroupInfo(long gid, String name, String intro){
        this.gid = gid;
        this.name = name;
        this.intro = intro;
    }
    public GroupInfo(){};

    public String toString(){
        return name + ":" + gid + ":" + intro;
    }

    private long gid;
    private String name;
    private String intro;

}
