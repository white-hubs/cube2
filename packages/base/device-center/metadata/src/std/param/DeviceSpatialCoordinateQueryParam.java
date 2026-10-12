package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceSpatialCoordinateQueryParam extends Paging {

    /**
     * 设备id
     */
    private Long deviceId;
}
