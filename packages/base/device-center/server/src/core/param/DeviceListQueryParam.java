package com.guhecloud.cube.base.device.center.core.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListQueryParam {
    private String keyword;
    private String rightLikeAreaId;
    private String lastAreaId;
    private String departmentId;
    private Integer state;
    private Set<Long> bizTypes;
    private Set<Long> filterDeviceIds;
}
