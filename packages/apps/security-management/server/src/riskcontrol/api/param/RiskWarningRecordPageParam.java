package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskWarningRecordPageParam extends Paging {
    /**
     * 预警状态筛选，取值：ACTIVE=待处置或处置中，DISPOSED=已处置。
     */
    private String status;

    /**
     * 预警类型筛选，取值：LEVEL_UPGRADE=等级升级预警，ASSESSMENT_OVERDUE=评估超期预警，
     * INDICATOR_EXCEED=指标超标预警。
     */
    private String warningType;

    /**
     * 预警等级筛选，取值：RED=红色，ORANGE=橙色，YELLOW=黄色。
     */
    private String severity;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 触发时间开始。
     */
    private LocalDateTime startTime;

    /**
     * 触发时间结束。
     */
    private LocalDateTime endTime;

    /**
     * 输出格式；导出接口固定传 xlsx。
     */
    private String format;
}
