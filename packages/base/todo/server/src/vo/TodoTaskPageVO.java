package com.guhecloud.cube.biz.todo.server.vo;

import com.guhecloud.cube.biz.todo.server.enumeration.TodoTaskStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办任务
 *
 * @author lanchun
 * @date 2022/7/22 10:28
 */
@Data
public class TodoTaskPageVO {

    private Long id;

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
     * 业务主键
     */
    private String businessKey;

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
     * 待办状态
     */
    private TodoTaskStatusEnum status;

    /**
     * 下一节点状态
     */
    private String nextBusinessStatus;

    /**
     * 下一节点状态名称
     */
    private String nextBusinessStatusName;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 任务删除状态
     */
    private Boolean taskDelState;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;
}
