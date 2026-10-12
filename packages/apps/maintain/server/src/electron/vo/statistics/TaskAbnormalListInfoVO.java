package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡更异常记录列表
 * @date 2024/1/25 17:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAbnormalListInfoVO {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 时间段
     */
    private String timePeriod;
    /**
     * 状态
     */
    private String state;
    /**
     * 执行人
     */
    private String username;

}
