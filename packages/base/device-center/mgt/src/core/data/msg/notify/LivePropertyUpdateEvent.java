package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-26 15:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LivePropertyUpdateEvent extends NotifyInfo {

    private String deviceId;

    private List<KeyValueWithTs> updates;

    @Override
    public NotifyMsgType msgType() {
        return NotifyMsgType.DEVICE_PROP;
    }
}