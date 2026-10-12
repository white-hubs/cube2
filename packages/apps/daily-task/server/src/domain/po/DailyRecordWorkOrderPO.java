package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 记录工单关联关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("daily_record_work_order")
public class DailyRecordWorkOrderPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long taskId;
    private Long recordId;
    private Long targetId;
    private String targetCategory;
    /**
     * 工单类型(DailyWorkOrderTypeEnum)
     */
    private String workOrderType;
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    /**
     * 工单名称
     */
    private String processInstanceName;
    /**
     * 工单状态(FlowResult)
     */
    private String processInstanceStatus;
}
