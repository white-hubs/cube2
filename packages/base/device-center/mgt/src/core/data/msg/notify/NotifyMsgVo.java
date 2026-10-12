package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotifyMsgVo {
    private NotifyMsgType notifyMsgType;
    private Object msg;
}
