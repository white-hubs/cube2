package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LineLossAlarmRuleVO {

    private Long id;
    /**
     * 触发的规则id
     */
    private Long triggeredRuleId;
    private String name;
    /**
     * 报警级别
     */
    private Long alarmLevelId;
    private String alarmLevelName;
    /**
     * 处理时限值
     */
    private Integer estimatedHandlerTime;
    /**
     * 处理时限名称
     */
    private String estimatedHandlerTimeName;
    /**
     * 处理时限单位
     */
    private String estimatedHandlerUnit;
    /**
     * 事件类型id
     */
    private Long emergencyEventTypeId;
    /**
     * 报警事件类型编码
     */
    private String emergencyEventTypeCode;
    /**
     * 事件类型名称
     */
    private String emergencyEventTypeName;
    private String description;
    private Boolean enabled;
    /**
     * 触发条件
     */
    private LossAlarmConditionVO lossAlarmCondition = new LossAlarmConditionVO();
    private String createBy;
    private LocalDateTime createTime;

    /**
     * 规则动作
     */
    private List<LinkageActionDTO> emergencyActions;

    public void covertLineLossBasicVO(LineLossAlarmRule rule) {
        if (rule != null) {
            id = rule.getId();
            triggeredRuleId = rule.getTriggeredRuleId();
            name = rule.getName();
            alarmLevelId = rule.getAlarmLevelId();
            estimatedHandlerTime = rule.getEstimatedHandlerTime();
            estimatedHandlerTimeName = rule.getEstimatedHandlerUnit();
            estimatedHandlerUnit = rule.getEstimatedHandlerUnit();
            emergencyEventTypeId = rule.getEmergencyEventTypeId();
            emergencyEventTypeName = rule.getEmergencyEventTypeName();
            emergencyEventTypeCode = rule.getEmergencyEventTypeCode();
            lossAlarmCondition.setCondition(rule.getCondition());
            createTime = rule.getCreateTime();
            createBy = rule.getCreateBy();
            enabled = rule.getEnabled();
        }
    }
}
