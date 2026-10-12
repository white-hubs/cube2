package com.guhecloud.subsystem.requester.sdk.param.drone;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DroneResultQueryParam extends Paging {
    private Integer fileType;
    private Integer channelType;
    private Long scheduledId;
    private Integer pType;
}
