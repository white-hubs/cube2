package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import lombok.Data;

import java.util.List;

/**
 * @author lrj
 * @Description 维保计划
 * @date 2022-4-29 15:12:36
 */
@Data
public class MaintenanceIotRulesParam {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 条件
     */
    private String op;
    /**
     * 延迟时间
     */
    private Integer delayTime;
    /**
     * 属性code
     */
    private String propCode;
    /**
     * 设备id
     */
    private List<String> oprand;

}