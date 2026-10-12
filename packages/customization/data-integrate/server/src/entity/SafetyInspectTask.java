package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 巡检数据采集
 * @date 2023-09-12 15:08:36
 */
@Data
public class SafetyInspectTask {
    //
    private Long id;
    //任务名称
    private String taskName;
    //任务创建时间
    private LocalDateTime taskCreateTime;
    //巡检类型
    private String typeName;
    //任务类型
    private String taskType;
    //级别code
    private String levelCode;
    //级别name
    private String levelName;
    //状态_code
    private String statusCode;
    //状态_name
    private String statusName;
    //执行人
    private String executor;
    //生效结束时间
    private LocalDateTime validEndTime;
    //巡检对象数
    private Integer targetNum;
    //执行进度
    private Double progress;
    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

    private Integer tenant;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Boolean deleted;
    /**
     * 业务类型
     */
    private String businessType;
}