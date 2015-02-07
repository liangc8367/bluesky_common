package com.bluesky.common;

/**
 * Created by liangc on 01/02/15.
 */
public interface OLog {
    public void e(String tag, String info);
    public void w(String tag, String info);
    public void i(String tag, String info);
    public void d(String tag, String info);
}
