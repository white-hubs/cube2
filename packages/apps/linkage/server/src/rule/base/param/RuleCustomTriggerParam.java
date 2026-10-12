package com.guhecloud.cube.biz.linkage.server.rule.base.param;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.DateUnitEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.RuleTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则触发参数
 *
 * @author lanchun
 * @date 2022/11/1 19:22
 */
@Data
public class RuleCustomTriggerParam {

    private Long id;

    /**
     * 规则编号
     */
    private String code;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则配置
     */
    private String ruleConfig;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 规则类型
     */
    private RuleTypeEnum type = RuleTypeEnum.ALARM_RULE;

    /**
     * 业务类型
     */
    private BusinessTypeEnum businessType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否去重
     */
    private Boolean reiterated;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private DateUnitEnum repeatTimeUnit;

    /**
     * 触发级别主键
     */
    private Long triggerLevelId;

    /**
     * 触发级别名称
     */
    private String triggerLevelName;

    /**
     * 触发类型主键
     */
    private Long triggerTypeId;

    /**
     * 触发类型名称
     */
    private String triggerTypeName;

    /**
     * 触发对象唯一标识
     */
    private String triggerTargetId;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /**
     * 物联网事件实例主键
     */
    private String iotEventInstanceId;

    /**
     * 事件产生快照的信息
     */
    private Object snapshot;

    /**
     * 触发事件json
     */
    private Object eventConfig;
}
