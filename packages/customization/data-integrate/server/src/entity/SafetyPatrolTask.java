package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 巡更数据采集
 * @date 2023-09-12 15:08:36
 */
@Data
public class SafetyPatrolTask {
    //
    private Long id;
    //巡更任务
    private String taskName;
    //任务创建时间
    private LocalDateTime taskCreateTime;
    //执行时间段
    private String executeData;
    //任务状态Code
    private String statusCode;
    //任务状态Name
    private String statusName;
    //任务结果_code
    private String resultCode;
    //任务结果_name
    private String resultName;
    //巡更路线
    private String routeName;
    //巡更点数
    private Integer locationNum;
    //执行人姓名
    private String userName;
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

}