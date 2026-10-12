package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

/**
 * @author fl
 * @date 2022/6/1
 **/
public enum NotifyStatus {
    initial("待推送"),
    success("推送成功"),
    fail("推送失败");

    public final String desc;

    NotifyStatus(String desc) {
        this.desc = desc;
    }

}
