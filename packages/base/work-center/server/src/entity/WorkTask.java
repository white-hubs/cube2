package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 工作任务
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkTask extends BasicEnhancedTenantEntity<Long> {

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
     * 下一节点状态
     */
    private String nextBusinessStatus;

    /**
     * 下一节点状态名称
     */
    private String nextBusinessStatusName;

    /**
     * 待办状态
     */
    private WorkTaskStatusEnum status;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;

    /**
     * 任务删除状态
     */
    private Boolean taskDelState = Boolean.FALSE;
}
