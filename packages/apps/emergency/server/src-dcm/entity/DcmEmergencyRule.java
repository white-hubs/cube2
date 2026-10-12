package com.guhecloud.cube.biz.emergency.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmEmergencyRule implements Identifiable {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 预案事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    /**
     * 去重时间
     */
    private Integer repeatTime;

    /**
     * 去重时间单位
     */
    private String repeatTimeUnit;

    /**
     * 预计处理时间
     */
    private Integer estimatedHandlerTime;

    /**
     * 预计处理时间单位
     */
    private String estimatedHandlerUnit;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 规则组
     */
    private List<DcmEmergencyRuleCondGroup> emergencyRuleCondGroups = Collections.emptyList();

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s", applicationCode, name);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s:%s:%s:%s:%s:%s", alarmLevelCode, emergencyEventTypeCode, enabled, repeatTime, repeatTimeUnit,
                             estimatedHandlerTime, estimatedHandlerUnit, remark == null ? "" : remark,
                             CollectionUtils.isEmpty(emergencyRuleCondGroups) ? "" : emergencyRuleCondGroups.hashCode());
    }
}
