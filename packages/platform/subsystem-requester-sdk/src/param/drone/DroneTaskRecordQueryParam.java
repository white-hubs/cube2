package com.guhecloud.subsystem.requester.sdk.param.drone;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DroneTaskRecordQueryParam extends Paging {
    private Long taskId;
    /**
     * 任务调度状态
     */
    private Integer scheduleStatus;
    /**
     * 任务执行时间段
     */
    private Period executionTime;
}
