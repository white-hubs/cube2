package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-15  10:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class ArrangeWorkParam {
    /**
     * 人员id集合
     */
    private Set<Long> userIds;
    /**
     * 工作时间
     */
    private LocalDateTime startDate;
}
