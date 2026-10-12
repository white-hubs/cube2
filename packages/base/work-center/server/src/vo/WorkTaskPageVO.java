package com.guhecloud.cube.biz.work.center.server.vo;

import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作任务
 */
@Data
public class WorkTaskPageVO {

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
     * 任务删除状态
     */
    private Boolean taskDelState;

    /**
     * 业务实体信息
     */
    private String businessPrincipal;
}
