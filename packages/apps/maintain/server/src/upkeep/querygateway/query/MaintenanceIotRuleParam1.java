package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import lombok.Data;

import java.util.List;

@Data
public class MaintenanceIotRuleParam1 {
    /**
     * 条件
     */
    private String op;
    private List<MaintenanceIotRulesParam> rules;
}
