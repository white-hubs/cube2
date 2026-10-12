package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevicePropParam {
    @NotNull
    Long deviceId;
    String propertyCodes;
    Long notBeforeTs;
}
