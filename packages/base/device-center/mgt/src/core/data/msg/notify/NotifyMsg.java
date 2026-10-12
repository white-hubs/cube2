package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import com.alibaba.fastjson2.annotation.JSONField;

public interface NotifyMsg {
    /**
     * 通知消息种类
     *
     * @return 种类
     */
    @JSONField(name = "msgType")
    NotifyMsgType msgType();
}
