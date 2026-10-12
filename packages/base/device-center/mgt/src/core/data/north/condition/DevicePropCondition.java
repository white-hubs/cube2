package com.guhecloud.cube.iot.mgt.core.data.north.condition;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DevicePropCondition implements SubscribeCondition {

    /**
     * deviceId.propCode<br/>
     * deviceId.eventCode
     */
    private List<String> deviceIdAndPropCode;

    //TODO: 增加变化推送的条件
    private boolean changeNotify;

    /**
     * 返回当前条件的设备ID和属性编码
     *
     * @return 设备ID和属性编码
     */
    public Map<String, List<String>> toDeviceIdAndPropCode() {

        if (deviceIdAndPropCode == null || deviceIdAndPropCode.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> result = new HashMap<>();
        for (String idAndProp : deviceIdAndPropCode) {

            String[] split = idAndProp.split(SPLIT);
            if (split.length != 2) {
                throw new IllegalArgumentException("deviceIdAndPropCode must be deviceId.propCode");
            }

            String deviceId = split[0];
            String propCode = split[1];
            result.computeIfAbsent(deviceId, k -> new ArrayList<>()).add(propCode);
        }

        return result;
    }
}
