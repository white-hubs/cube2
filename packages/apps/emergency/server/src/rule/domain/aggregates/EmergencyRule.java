package com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleConditionDeviceCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleConditionGroupCommand;
import com.guhecloud.cube.biz.emergency.server.rule.domain.entity.EmergencyInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyEventTypeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyPushConfig;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyTypeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EstimatedHandlerTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.RepeatTimeInfo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 预案规则
 *
 * @author lanchun
 * @date 2022/9/3 11:24
 */
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRule {

    private Long id;

    /**
     * 预案信息
     */
    private EmergencyInfo emergencyInfo;

    /**
     * 预案事件分类
     */
    private EmergencyEventTypeInfo emergencyEventTypeInfo;

    /**
     * 预案类型主键
     */
    private EmergencyTypeInfo emergencyTypeInfo;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 禁用时长(默认小时,暂不扩展其它方式)
     */
    private Integer disableDuration;

    /**
     * 预计处理信息
     */
    private EstimatedHandlerTimeInfo estimatedHandlerTimeInfo;

    /**
     * 去重信息
     */
    private RepeatTimeInfo repeatTimeInfo;

    /**
     * 逻辑运算符(or and)
     */
    private LogicalOpEnum logicalOp;

    /**
     * 是否指定条件设备
     */
    private Boolean appointDevice;

    /**
     * 条件组信息
     */
    private List<EmergencyRuleConditionGroupInfo> emergencyRuleConditionGroupInfos;

    /**
     * 规则条件设备信息
     */
    private List<EmergencyRuleConditionDeviceInfo> emergencyRuleConditionDeviceInfos;

    /**
     * 预案报警推送配置
     */
    private EmergencyPushConfig emergencyPushConfig;

    public EmergencyRule(EmergencyInfo emergencyInfo,
                         EmergencyTypeInfo emergencyTypeInfo,
                         Boolean enabled,
                         LogicalOpEnum logicalOp,
                         Boolean appointDevice,
                         List<EmergencyRuleConditionGroupInfo> emergencyRuleConditionGroupInfos,
                         List<EmergencyRuleConditionDeviceInfo> emergencyRuleConditionDeviceInfos
                        ) {
        this.id = IdWorker.getId();
        this.emergencyInfo = emergencyInfo;
        this.emergencyTypeInfo = emergencyTypeInfo;
        this.enabled = enabled;
        this.logicalOp = logicalOp;
        this.appointDevice = appointDevice;
        this.emergencyRuleConditionGroupInfos = emergencyRuleConditionGroupInfos;
        this.emergencyRuleConditionDeviceInfos = emergencyRuleConditionDeviceInfos;
        this.emergencyPushConfig = new EmergencyPushConfig(Boolean.TRUE, null);
    }

    /**
     * 更改基础信息
     *
     * @param name
     * @param alarmLevelId
     * @param remark
     */
    public void changeBasicInfo(String name, Long alarmLevelId, LogicalOpEnum logicalOp, String remark) {
        this.emergencyInfo = new EmergencyInfo(name, alarmLevelId, remark);
        this.logicalOp = logicalOp;
    }

    /**
     * 更改去重时间信息
     *
     * @param repeatTime
     * @param repeatTimeUnit
     */
    public void changeRepeatTimeInfo(Integer repeatTime, String repeatTimeUnit) {
        this.repeatTimeInfo = new RepeatTimeInfo(repeatTime, repeatTimeUnit);
    }

    /**
     * 更改超时处理信息
     *
     * @param estimatedHandlerTime
     * @param estimatedHandlerUnits
     */
    public void changeEstimatedHandlerTimeInfo(Integer estimatedHandlerTime, String estimatedHandlerUnits) {
        this.estimatedHandlerTimeInfo = new EstimatedHandlerTimeInfo(estimatedHandlerTime, estimatedHandlerUnits);
    }

    /**
     * 修改是否启用
     *
     * @param enabled
     */
    public void changeEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 修改是否指定设备
     *
     * @param appointDevice
     */
    public void changeAppointDevice(Boolean appointDevice) {
        this.appointDevice = appointDevice;
    }

    /**
     * 修改预案类型信息
     */
    public void changeEmergencyTypeInfo(String applicationCode) {
        EmergencyTypeMapper emergencyTypeMapper = ApplicationContextHolder.getBean(EmergencyTypeMapper.class);
        EmergencyType emergencyType = emergencyTypeMapper.selectOne(Wrappers.<EmergencyType>lambdaQuery()
                                                                            .eq(EmergencyType::getApplicationCode, applicationCode));
        this.emergencyTypeInfo = new EmergencyTypeInfo(emergencyType.getId(), emergencyType.getApplicationCode(), emergencyType.getApplicationName());
    }

    /**
     * 修改事件分类
     *
     * @param emergencyEventTypeCode
     */
    public void changeEmergencyEventType(String emergencyEventTypeCode) {
        this.emergencyEventTypeInfo = new EmergencyEventTypeInfo(emergencyEventTypeCode);
    }

    public void settingRuleConditionDevices(List<ChangeEmergencyRuleConditionDeviceCommand> emergencyRuleConditionDeviceCommands) {
        if (!CollectionUtils.isEmpty(emergencyRuleConditionDeviceCommands)) {
            this.emergencyRuleConditionDeviceInfos = new ArrayList<>(emergencyRuleConditionDeviceCommands.size());
            emergencyRuleConditionDeviceCommands.forEach(emergencyRuleConditionDeviceCommand -> {
                String[] prodCategoryCodes = emergencyRuleConditionDeviceCommand.getDeviceProdCategoryCodeConcat().split("-");
                EmergencyRuleConditionDeviceInfo emergencyRuleConditionDeviceInfo = new EmergencyRuleConditionDeviceInfo(
                        prodCategoryCodes.length > 0 ? prodCategoryCodes[prodCategoryCodes.length - 1] : null,
                        emergencyRuleConditionDeviceCommand.getDeviceProdCategoryCodeConcat(),
                        emergencyRuleConditionDeviceCommand.getDeviceProdCategoryName(),
                        emergencyRuleConditionDeviceCommand.getDeviceProdId(),
                        emergencyRuleConditionDeviceCommand.getDeviceProdName(),
                        emergencyRuleConditionDeviceCommand.getDeviceId(),
                        emergencyRuleConditionDeviceCommand.getDeviceName(),
                        emergencyRuleConditionDeviceCommand.getDeviceAreaId(),
                        emergencyRuleConditionDeviceCommand.getDeviceAreaName(),
                        emergencyRuleConditionDeviceCommand.getEnabled());
                this.emergencyRuleConditionDeviceInfos.add(emergencyRuleConditionDeviceInfo);
            });
        } else {
            this.emergencyRuleConditionDeviceInfos = new ArrayList<>();
        }
    }

    public void settingRuleConditionGroup(List<ChangeEmergencyRuleConditionGroupCommand> emergencyRuleConditionGroupCommands) {
        this.emergencyRuleConditionGroupInfos = new ArrayList<>(emergencyRuleConditionGroupInfos.size());
        emergencyRuleConditionGroupCommands.forEach(emergencyRuleConditionGroupCommand -> {
            List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos = new ArrayList<>(emergencyRuleConditionGroupCommand
                                                                                                   .getEmergencyRuleConditions().size());
            long groupId = IdWorker.getId();
            emergencyRuleConditionGroupCommand.getEmergencyRuleConditions().forEach(emergencyRuleConditionInfo -> {
                String[] prodCategoryCodes = emergencyRuleConditionInfo.getDeviceProdCategoryCodeConcat().split("-");
                emergencyRuleConditionInfos.add(new EmergencyRuleConditionInfo(
                        groupId,
                        prodCategoryCodes.length > 0 ? prodCategoryCodes[prodCategoryCodes.length - 1] : null,
                        emergencyRuleConditionInfo.getDeviceProdCategoryCodeConcat(),
                        emergencyRuleConditionInfo.getDeviceProdCategoryName(),
                        emergencyRuleConditionInfo.getDeviceProdId(),
                        emergencyRuleConditionInfo.getDeviceProdName(),
                        emergencyRuleConditionInfo.getConditionType(),
                        emergencyRuleConditionInfo.getDeviceEventId(),
                        emergencyRuleConditionInfo.getDeviceEventCode(),
                        emergencyRuleConditionInfo.getDeviceEventName(),
                        emergencyRuleConditionInfo.getDeviceEventType(),
                        emergencyRuleConditionInfo.getLogicalOp(),
                        emergencyRuleConditionInfo.getEventConfigType(),
                        ObjectUtils.copying(emergencyRuleConditionInfo.getEmergencyRuleConditionExps(), EmergencyRuleConditionExpInfo.class)));
            });
            EmergencyRuleTimeInfo emergencyRuleTimeInfo = ObjectUtils.copying(emergencyRuleConditionGroupCommand.getEmergencyRuleTime(), EmergencyRuleTimeInfo.class);
            emergencyRuleTimeInfo.setId(IdWorker.getId());
            this.emergencyRuleConditionGroupInfos.add(new EmergencyRuleConditionGroupInfo(
                    groupId,
                    emergencyRuleConditionInfos,
                    emergencyRuleTimeInfo));
        });
    }

    /**
     * 设置去重时间
     */
    public void settingRepeatTimeInfo(Integer repeatTime, String repeatTimeUnit) {
        this.repeatTimeInfo = new RepeatTimeInfo(repeatTime, repeatTimeUnit);
    }

    /**
     * 设置超时时间配置
     *
     * @param estimatedHandlerTime
     * @param estimatedHandlerUnits
     */
    public void settingEstimatedHandlerTimeInfo(Integer estimatedHandlerTime, String estimatedHandlerUnits) {
        this.estimatedHandlerTimeInfo = new EstimatedHandlerTimeInfo(estimatedHandlerTime, estimatedHandlerUnits);
    }

    /**
     * 设置事件分类编号
     *
     * @param emergencyEventTypeCode
     */
    public void settingEmergencyEventType(String emergencyEventTypeCode) {
        this.emergencyEventTypeInfo = new EmergencyEventTypeInfo(emergencyEventTypeCode);
    }

    /**
     * 设置推送配置
     *
     * @param notifyCommandDispatch 是否推送到指挥调度
     * @param turnWarningSound      是否报警提示音
     */
    public void settingEmergencyPushConfig(Boolean notifyCommandDispatch, Boolean turnWarningSound) {
        if (Objects.nonNull(notifyCommandDispatch)) {
            this.emergencyPushConfig = new EmergencyPushConfig(notifyCommandDispatch, turnWarningSound);
        } else {
            this.emergencyPushConfig = new EmergencyPushConfig(Boolean.TRUE, turnWarningSound);
        }
    }
}
