package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.vo;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 条件规则分页返回参数
 *
 * @author lanchun
 * @date 2022/6/30 10:34
 */
@Data
public class RuleDeviceConditionPageVO {

    /**
     * 规则主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 触发次数
     */
    private Integer triggerNum;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人主键
     */
    private String creatorId;

    /**
     * 创建人类型
     */
    private ActorTypeEnum creatorType;

    /**
     * 创建人名称
     */
    private String createByName;
}
