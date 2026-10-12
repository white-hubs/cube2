package com.guhecloud.cube.base.device.center.metadata.std.dto;

import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Version;
import lombok.Data;

@Data
public class DeviceStateCountDTO {
    private Integer totalNumber;

    private int normalNumber;

    private Integer offlineNumber;

    @VersionInfo(deprecatedSince = Version.V_2_1_0)
    private int troubleNumber;

    @VersionInfo(deprecatedSince = Version.V_2_1_0)
    private int alarmNumber;
}