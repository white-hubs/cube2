package com.guhecloud.cube.iot.mgt.north.vo;

import com.guhecloud.cube.iot.mgt.core.data.lang.SpecialValConst;
import lombok.Data;

@Data
public class DeviceCommandIssues {
    private Long deviceId;
    private String eventCode;
    /**
     * 详情{@link SpecialValConst#SUPPORT_DEV_EVENT_THRESHOLD}
     */
    private String thresholdCode;//阈值code
    private String value;
    private String commandSeq;
}
