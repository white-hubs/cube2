package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/24 14:20
 * @Description: 任务对象关联
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_target_ref")
public class DailyTaskTargetRefPO {
    private Long id;
    private Long taskId;
    private Long targetRefId;
}

