package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 电子巡更安全管理数据采集信息
 * @date 2023/9/8 18:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ElectronicPatrolTaskDataModel {
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
     * 执行时间段
     */
    private String executeData;
    /**
     * 任务状态code
     */
    private String statusCode;
    /**
     * 任务状态name
     */
    private String statusName;
    /**
     * 任务结果code
     */
    private String resultCode;
    /**
     * 任务结果name
     */
    private String resultName;
    /**
     * 巡更路线
     */
    private String routeName;
    /**
     * 巡更点数
     */
    private Integer locationNum;
    /**
     * 执行人名称
     */
    private String userName;

    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    private LocalDateTime createTime;
    private String updateBy;
    private String updaterId;
    private ActorTypeEnum updaterType;
    private LocalDateTime updateTime;
}
