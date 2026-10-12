package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class WaterDeviceMonitorParam extends Paging {
    private String keyword;
    private String areaIds;
    private String deviceType;
}
