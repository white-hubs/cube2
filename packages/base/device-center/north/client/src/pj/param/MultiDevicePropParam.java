package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MultiDevicePropParam {
    /**
     * eg: deviceId1.propA,deviceId2.propB === 1329841713324.temp,1329841713324.hum
     */
    @NotNull
    String devIdAndPropCodes;
    Long notBeforeTs;
}
