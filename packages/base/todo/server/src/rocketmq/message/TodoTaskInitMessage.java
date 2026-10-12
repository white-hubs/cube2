package com.guhecloud.cube.biz.todo.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办任务消息
 *
 * @author lanchun
 * @date 2022/7/23 15:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TodoTaskInitMessage extends TodoTaskFlowMessage {

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
