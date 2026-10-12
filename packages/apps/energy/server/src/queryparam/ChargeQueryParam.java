package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

import java.util.List;

@Data
public class ChargeQueryParam {
    private String instrumentType;
    private String timeType;
    private String areaIds;
    private List<TreeIds> treeIds;
    private String period;
}
