package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

import java.util.List;

@Data
public class AssociationParam {
    //商户id
    private List<Long> wholesalerId;
    //档位时间
    private List<GearPositionTimeParam> timePositionParams;
}
