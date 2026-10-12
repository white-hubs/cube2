package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 10:54
 * @Description: 任务操作参数
 */
@Data
public class DailyTaskOperationParam {

    /**
     * 任务id
     */
    private List<Long> taskIdList;

    /**
     * 操作类型
     */
    private String dailyType;

    /**
     * 操作人
     */
    private Long operatorId;

    /**
     * 移交人
     */
    private Long transferUserId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 分发
     */
    private Boolean distribute;

    /**
     * 任务对象
     */
    private List<Long> taskTargetIdList;

    /**
     * 任务百分比
     *
     * @return
     */
    private BigDecimal taskPercent;

    public Long getOperatorId() {
        if (Objects.isNull(operatorId)) {
            operatorId = SecurityContextHolder.getPrincipal().getId();
        }
        return operatorId;
    }
}
