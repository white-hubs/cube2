package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import lombok.Data;

/**
 * 所有的通知都应当继承此类,包含一些租户信息
 */
@Data
public abstract class NotifyInfo implements NotifyMsg {
    protected String tenantId;
}
