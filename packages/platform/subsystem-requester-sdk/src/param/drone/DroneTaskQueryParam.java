package com.guhecloud.subsystem.requester.sdk.param.drone;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DroneTaskQueryParam extends Paging {
    private Integer type;
    private String nestSn;
    private Long trackId;
    private String taskName;
}
