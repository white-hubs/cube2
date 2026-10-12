package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 任务巡更点信息
 * @date 2023/10/26 15:48
 */
@Data
public class TaskLocationVO {
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 计划巡更顺序
     */
    private Integer orderNum;
    /**
     * 实际巡更顺序
     */
    private Integer patrolOrder;
    /**
     * 巡更点名称
     */
    private String patrolLocationName;
    /**
     * 经度
     */
    private String patrolLongitude;
    /**
     * 纬度
     */
    private String patrolDimension;
    /**
     * 巡更结果
     */
    private String result;
    private String resultValue;
    /**
     * 巡更人员
     */
    private Long userId;
    private String username;
    /**
     * 巡更时间
     */
    private LocalDateTime patrolDate;

}
