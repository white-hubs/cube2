package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

import java.util.List;

@Data
public class EnergyTrendParam {
    private String instrumentType;
    private String bindType;
    private String timeFlag;
    private String period;
    private String itemId;
    private List<String> areaFlag;
    private List<TreeIds> treeIds;
}
