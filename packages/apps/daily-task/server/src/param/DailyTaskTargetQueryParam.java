package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;

import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/18 10:21
 * @Description:
 */
@Data
public class DailyTaskTargetQueryParam extends Paging {

    /**
     * 日常任务类型
     */
    private String dailyType;
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 子任务和当前任务id集合
     */
    private Set<Long> taskIds;

    public void setTargetIds(Set<Long> targetIds) {
        if (CollectionUtils.isNotEmpty(this.targetIds)) {
            this.taskIds.addAll(this.targetIds);
        } else {
            this.targetIds = targetIds;
        }
    }

    /**
     * 对象集合
     */
    private Set<Long> targetIds;

    /**
     * 标签id集合
     */
    private Long tagId;

    /**
     * 结果
     */
    private String result;

    /**
     * 执行时间
     */
    private Period executeTime;

    /**
     * 对象类型
     */
    private String targetType;

    /**
     * 对象状态
     */
    private String status;
    /**
     * 对象名称
     */
    private String keyword;

    private Boolean allowAssign;
}
