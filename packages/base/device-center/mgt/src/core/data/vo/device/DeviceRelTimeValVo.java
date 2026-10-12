package com.guhecloud.cube.iot.mgt.core.data.vo.device;

import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import lombok.Data;

import java.util.List;

@Data
public class DeviceRelTimeValVo {
    private String deviceId;

    private String deviceName;

    private List<KeyValueWithTs> propValList;
}