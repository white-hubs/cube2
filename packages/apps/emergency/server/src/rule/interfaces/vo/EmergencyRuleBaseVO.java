package com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import lombok.Data;

import java.util.Objects;

@Data
public class EmergencyRuleBaseVO {

    private Long id;

    /**
     * 预案名称
     */
    private String name;

    /**
     * 预案级别主键
     */
    private Long alarmLevelId;

    /**
     * 预案级别名称
     */
    private String alarmLevelName;

    /**
     * 事件分类主键
     */
    private Long emergencyEventTypeId;

    /**
     * 事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 事件分类名称
     */
    private String emergencyEventTypeName;

    /**
     * 预案类型主键
     */
    private Long emergencyTypeId;

    /**
     * 预案类型名称
     */
    private String emergencyTypeName;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 禁用时长(默认小时,暂不扩展其它方式)
     */
    private Integer disableDuration;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private String repeatTimeUnit;

    /**
     * 去重时间名称(时间+翻译中文后的单位)
     */
    private String repeatTimeName;

    /**
     * 预计处理时间
     */
    private Integer estimatedHandlerTime;

    /**
     * 预计处理时间单位
     */
    private String estimatedHandlerUnit;

    /**
     * 预计处理时间名称(时间+翻译中文后的单位)
     */
    private String estimatedHandlerTimeName;

    /**
     * 逻辑运算符(or and)
     */
    private LogicalOpEnum logicalOp;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否指定条件设备
     */
    private Boolean appointDevice;

    /**
     * 是否推送到调度中心
     */
    private Boolean notifyCommandDispatch;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;

    /**
     * 租户信息
     */
    private Integer tenant;

    public Boolean getNotifyCommandDispatch() {
        if (Objects.nonNull(notifyCommandDispatch)) {
            return notifyCommandDispatch;
        }
        return Boolean.TRUE;
    }
}
