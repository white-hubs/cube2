package com.guhecloud.cube.iot.mgt.core.data.north;

import com.google.common.collect.ImmutableSet;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;

import java.util.Arrays;
import java.util.List;

/**
 * 这里为了兼容老代码，保留HTTP、RABBITMQ等推送方式，
 * 但实际上MGT只考虑WebSocket推送方式
 * <p>
 */
public enum NotifyOut {
    /**
     * MGT 只考虑WebSocket推送方式
     */
    @Deprecated
    HTTP(Arrays.asList(
            NotifyMsgType.DEVICE_COMMAND,
            NotifyMsgType.DEVICE_EVENT,
            NotifyMsgType.DEVICE_STATUS,
            NotifyMsgType.RULE_TRIGGER_EVENT)
    ),
    WEBSOCKET(Arrays.asList(
            NotifyMsgType.DEVICE_COMMAND,
            NotifyMsgType.DEVICE_EVENT,
            NotifyMsgType.DEVICE_STATUS,
            NotifyMsgType.DEVICE_PROP,
            NotifyMsgType.RULE_TRIGGER_EVENT)
    ),
    /**
     * MGT 只考虑WebSocket推送方式
     */
    @Deprecated
    RABBITMQ(Arrays.asList(
            NotifyMsgType.DEVICE_COMMAND,
            NotifyMsgType.DEVICE_EVENT,
            NotifyMsgType.DEVICE_STATUS,
            NotifyMsgType.RULE_TRIGGER_EVENT)
    );

    public final ImmutableSet<NotifyMsgType> allowNotifyType;

    NotifyOut(List<NotifyMsgType> allowNotifyType) {
        this.allowNotifyType = ImmutableSet.copyOf(allowNotifyType);
    }

    public boolean allowNotifyType(NotifyMsgType notifyMsgType) {
        return this.allowNotifyType.contains(notifyMsgType);
    }
}
