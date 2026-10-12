package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 异常任务
 * @date 2023/10/26 15:12
 */
@Data
public class AbnormalTaskVO {
    /**
     * 任务id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 负责人
     */
    private String userName;
    /**
     * 巡更点数
     */
    private Integer locationNum;
    /**
     * 任务状态
     */
    private String result;
    /**
     * 巡更点信息
     */
    private List<TaskLocationVO> locations;
}
