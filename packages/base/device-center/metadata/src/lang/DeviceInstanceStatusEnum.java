package com.guhecloud.cube.base.device.center.metadata.lang;

import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import lombok.Getter;

@Getter
public enum DeviceInstanceStatusEnum {
    /**
     * 报警
     */
    @VersionInfo(plannedSince = Version.V_UNKNOWN)
    ALARM("报警"),
    /**
     * 故障
     */
    @VersionInfo(plannedSince = Version.V_UNKNOWN)
    TROUBLE("故障"),
    /**
     * 离线
     */
    @VersionInfo(availableSince = Version.V_2_0_1)
    OFFLINE("离线"),

    @VersionInfo(availableSince = Version.V_2_0_1)
    ONLINE("在线"),

    @VersionInfo(plannedSince = Version.V_UNKNOWN)
    NORMAL("正常");

    final String name;

    DeviceInstanceStatusEnum(String name) {
        this.name = name;
    }

}

