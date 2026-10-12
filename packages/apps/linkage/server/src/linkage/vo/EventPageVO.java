package com.guhecloud.cube.biz.linkage.server.linkage.vo;

import com.guhecloud.cube.biz.linkage.server.linkage.enumeration.EventTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事件分页返回对象
 *
 * @author lanchun
 * @date 2022/6/29 19:56
 */
@Data
public class EventPageVO {

    private Long id;

    /**
     * 事件编号
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 事件json
     */
    private String eventConfig;

    /**
     * 事件类型
     */
    private EventTypeEnum type;

    /**
     * 业务类型
     */
    private BusinessTypeEnum businessType;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作时间
     */
    private String operatorName;
}
