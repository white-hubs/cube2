package com.guhecloud.cube.biz.daily.task.record.result;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetRefResult;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 15:09
 * @Description:
 */
@Data
public class DailyRecordSubmitResult {
    private Long recordId;

    /**
     * 提交对象关联的其他待处理任务
     */
    private List<DailyTaskTargetRefResult> taskTargetRefResults;
}
