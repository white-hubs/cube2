package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 任务执行时间段
 * @date 2023/5/31 10:27
 */
@Data
public class TaskTimeQuantumVO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
