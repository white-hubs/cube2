package com.guhecloud.cube.biz.todo.server.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 待办完成参数
 *
 * @author lanchun
 * @date 2022/7/25 11:14
 */
@Data
public class TodoTaskFlowCirculationParam {

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 业务状态
     */
    private String businessStatus;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 本节点完成的用户主键
     */
    private Long userId;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;

    /**
     * 是否生成下一节点状态的待办任务
     */
    private Boolean generatedNextStatusTask;

    /**
     * 下一节点状态
     */
    private String nextBusinessStatus;

    /**
     * 任务时间
     */
    private LocalDateTime taskTime;

    /**
     * 下一节点业务状态名称
     */
    private String nextBusinessStatusName;

    /**
     * 下一节点业务状态处理用户集合
     */
    private List<Long> nextUserIds;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;
}
