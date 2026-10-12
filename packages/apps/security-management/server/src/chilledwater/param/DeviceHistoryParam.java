package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class DeviceHistoryParam extends Paging {
    private String startTime;
    private String endTime;
    private String timeType;
    private Long deviceId;
    private String attribute;
}
