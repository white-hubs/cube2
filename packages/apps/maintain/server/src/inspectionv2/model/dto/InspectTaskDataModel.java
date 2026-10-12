package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 安全管理数据采集信息
 * @date 2023/9/8 17:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InspectTaskDataModel {
    /**
     * 任务id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务创建时间
     */
    private LocalDateTime taskCreateTime;
    /**
     * 巡检类型
     */
    private String typeName;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 级别Code
     */
    private String levelCode;
    /**
     * 级别名称
     */
    private String levelName;
    /**
     * 状态code
     */
    private String statusCode;
    /**
     * 状态name
     */
    private String statusName;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;
    /**
     * 巡检对象数
     */
    private Integer targetNum;
    /**
     * 执行进度
     */
    private Double progress;

    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private String businessType;
}
