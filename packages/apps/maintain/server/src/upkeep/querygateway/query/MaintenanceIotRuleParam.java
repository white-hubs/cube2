package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import lombok.Data;

import java.util.List;

/**
 * @author lrj
 * @Description 维保计划
 * @date 2022-4-29 15:12:36
 */
@Data
public class MaintenanceIotRuleParam {
    /**
     * 条件
     */
    private String op;
    private List<MaintenanceIotRuleParam1> rules;

}