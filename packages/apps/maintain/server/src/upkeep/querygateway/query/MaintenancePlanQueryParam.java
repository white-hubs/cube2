package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 维保计划
 * @date 2022-4-29 15:12:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenancePlanQueryParam extends Paging {

    /**
     * 维保计划名称
     */
    private String maintenancePlanName;
    /**
     * 责任id
     */
    private String responsibilityPersonId;
    /**
     * 责任名称
     */
    private String responsibilityPersonName;
    /**
     * 责任类型
     */
    private String responsibilityType;
    /**
     * 维保周期
     */
    private Integer maintenanceCycle;
    /**
     * 状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划失效时间
     */
    private LocalDateTime planEndTime;
    /**
     * 任务数
     */
    private Integer taskNum;
    /**
     * 描述
     */
    private String remark;

    private String keyword;

    private String userId;

    private String organization;

    private String position;

}