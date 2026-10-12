package com.guhecloud.cube.iot.north.bridge.consumer.util;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.BizDeviceStatus;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceStatusUpdateMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;

import java.util.List;
import java.util.Objects;

public class CallbackUtil {
    private CallbackUtil() {
    }

    /**
     * 判断设备是否在线
     *
     * @param msg 设备状态更新消息
     * @return true 在线，false 不在线
     */
    public static boolean isOnlineSingle(DeviceStatusUpdateMsg msg) {
        return communicateIsNormal(msg);
    }

    /**
     * 通信是否正常
     *
     * @param msg 设备状态更新消息
     * @return true 通信正常(物联在线)，false 通信异常(物联离线)
     */
    public static boolean communicateIsNormal(DeviceStatusUpdateMsg msg) {
        if (Objects.isNull(msg))
            return false;

        List<KeyValueWithTs> updates = msg.getUpdates();
        if (Objects.isNull(updates) || updates.isEmpty())
            return false;

        String status = String.valueOf(msg.getUpdates().getFirst().getValue());
        return BizDeviceStatus.valueOf(status) == BizDeviceStatus.ON_LINE;
    }

}
