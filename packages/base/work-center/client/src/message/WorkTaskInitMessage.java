package com.guhecloud.cube.biz.work.center.client.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作任务消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class WorkTaskInitMessage extends WorkTaskFlowMessage {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务时间
     */
    private LocalDateTime taskTime;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 业务状态
     */
    private String businessStatus;

    /**
     * 业务状态名称
     */
    private String businessStatusName;

    /**
     * 用户主键
     */
    private List<Long> userIds;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;
}
