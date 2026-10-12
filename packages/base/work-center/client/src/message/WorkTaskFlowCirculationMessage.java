package com.guhecloud.cube.biz.work.center.client.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lanchun
 * @date 2022/12/29 9:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class WorkTaskFlowCirculationMessage extends WorkTaskFlowMessage {

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;

    /**
     * 业务状态
     */
    private String businessStatus;

    /**
     * 是否生成下一节点状态的待办任务
     */
    private Boolean generatedNextStatusTask;

    /**
     * 任务时间
     */
    private LocalDateTime taskTime;

    /**
     * 下一节点状态
     */
    private String nextBusinessStatus;

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
