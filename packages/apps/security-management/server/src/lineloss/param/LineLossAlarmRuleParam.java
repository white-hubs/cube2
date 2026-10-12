package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.linkage.client.param.LinkageActionModifyParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LossAlarmConditionDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LineLossAlarmRuleParam {
    private Long id;
    /**
     * 触发的规则id
     */
    private Long triggeredRuleId;
    @NotNull(message = "名称不能为空")
    private String name;
    /**
     * 报警级别
     */
    @NotNull(message = "报警级别不能为空")
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
     * 去重时间
     */
    private Integer repeatTime = 0;
    /**
     * 重复时间单位
     */
    private String repeatTimeUnit = "SECOND";
    private Boolean reiterated = false;

    /**
     * 事件类型ID
     */
    private Long emergencyEventTypeId;
    /**
     * 事件类型名称
     */
    private String emergencyEventTypeName;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;

    private Boolean enabled;
    private String emergencyEventTypeCode;
    private String applicationCode;
    @Valid
    @NotNull(message = "触发条件不能为空")
    private LossAlarmConditionDTO lossAlarmCondition;

    /**
     * 规则联动动作配置
     */
    @Valid
    private List<LinkageActionModifyParam> emergencyActions;

    public LineLossAlarmRule getRulePo() {
        var principal = SecurityContextHolder.getPrincipal();
        var loginBy = principal == null ? "system" : principal.getAccount();
        var lossAlarmRule = new LineLossAlarmRule();
        lossAlarmRule.setId(id == null ? IdWorker.getId() : id);
        lossAlarmRule.setName(name);
        lossAlarmRule.setAlarmLevelId(alarmLevelId);
        lossAlarmRule.setAlarmLevelName(alarmLevelName);
        lossAlarmRule.setEstimatedHandlerTime(estimatedHandlerTime);
        lossAlarmRule.setEstimatedHandlerTimeName(estimatedHandlerTimeName);
        lossAlarmRule.setEstimatedHandlerUnit(estimatedHandlerUnit);
        lossAlarmRule.setEmergencyEventTypeId(emergencyEventTypeId);
        lossAlarmRule.setEmergencyEventTypeCode(emergencyEventTypeCode);
        lossAlarmRule.setEmergencyEventTypeName(emergencyEventTypeName);
        lossAlarmRule.setCondition(lossAlarmCondition.getCondition());
        lossAlarmRule.setRemark(remark);
        lossAlarmRule.setCreateBy(createBy == null ? loginBy : createBy);
        lossAlarmRule.setCreateTime(createTime == null ? LocalDateTime.now() : createTime);
        lossAlarmRule.setUpdateTime(LocalDateTime.now());
        lossAlarmRule.setEnabled(!Boolean.FALSE.equals(enabled));
        lossAlarmRule.setDeleted(false);
        lossAlarmRule.setTriggeredRuleId(triggeredRuleId);
        return lossAlarmRule;
    }
}
