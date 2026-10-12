package com.guhecloud.subsystem.requester.sdk.param.drone;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DroneRouteQueryParam extends Paging {
    private String trackName;
    private String nestSn;
    private List<Integer> types;
}
