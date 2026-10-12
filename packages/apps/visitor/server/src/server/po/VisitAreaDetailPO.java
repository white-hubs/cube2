package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 区域详情
 */
@Data
@TableName("visit_area_detail")
@EqualsAndHashCode(callSuper = true)
public class VisitAreaDetailPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = -5628125311375119341L;
    private Long id;
    private Long areaId;
    private String areaName;
    private LocalDateTime createTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    /**
     * 日期段字符串
     */
    private String datePeriodStr;
    private String startDateStr;
    private String endDateStr;
    /**
     * 时间段
     */
    private String timeStr;
    private String startTimeStr;
    private String endTimeStr;
    /**
     * 跨天标志
     */
    private Boolean nextDayBool;
    /**
     * 总数量
     */
    private Integer totalNumber;
    /**
     * 1正常(0逻辑删除，以前的预约数据删除了还需要能查询到这个设置)
     */
    private Boolean normal;
}
