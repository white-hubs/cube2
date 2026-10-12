package com.guhecloud.cube.customized.youxianqu.park.management.server.project.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目管理返回对象
 */
@Data
public class ProjectManagementResult {
    private Long id;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 是否立项
     * 0.未立项,1.已立项
     */
    private Integer foundStatus;

    /**
     * 是否结项
     * 0.未结,1.已结
     */
    private Integer endStatus;
}
