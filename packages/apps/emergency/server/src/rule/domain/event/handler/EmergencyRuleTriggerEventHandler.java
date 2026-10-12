package com.guhecloud.cube.biz.emergency.server.rule.domain.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.apps.linkage.metadata.enumeration.BusinessTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.DateUnitEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.RuleTypeEnum;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.biz.emergency.server.dto.RuleTriggerEventConfig;
import com.guhecloud.cube.biz.emergency.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotEventTriggerEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotRuleTriggerEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.EventTriggerDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto.RuleTriggerDTO;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleTimeService;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.holder.RuleTimeTypeTriggerValidatorHolder;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.EmergencyRuleQueryService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EventConfigTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleBaseVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionDeviceCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionExpCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleDetailVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleTimeVO;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.param.RuleCustomTriggerParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleTriggerEventHandler {

    private final RuleClient ruleClient;

    private final EmergencyRuleQueryService emergencyRuleQueryService;

    private final EmergencyRuleTimeService emergencyRuleTimeService;

    private final SatAlarmLevelClient satAlarmLevelClient;

    private final RuleTimeTypeTriggerValidatorHolder ruleTimeTypeTriggerValidatorHolder;

    @EventListener
    public void iotRuleTriggerEmergencyRule(IotRuleTriggerEmergencyRuleEvent event) {
        RuleTriggerDTO ruleTriggerLog = event.getRuleTriggerLog();
        EmergencyRuleDetailVO ruleDetailVO = this.emergencyRuleQueryService.getDetail(event.getRuleId());
        if (Objects.isNull(ruleDetailVO) || Boolean.FALSE.equals(ruleDetailVO.getEnabled())) {
            return;
        }
        //规则触发时间
        LocalDateTime triggerTime = DateUtils.parse(ruleTriggerLog.getDelayEventTime());
        log.info("规则报警消费,预案规则id为:{},iot规则id为{},触发时间{}", event.getRuleId(), ruleTriggerLog.getRuleId(), ruleTriggerLog.getNotifyStatusTs());
        boolean triggered = true;
        Optional<EmergencyRuleTimePO> optionalEmergencyRuleTimePO = this.emergencyRuleTimeService.getEmergencyRuleTimeByRuleGroupId(Long.valueOf(ruleTriggerLog.getRuleId()));
        if (optionalEmergencyRuleTimePO.isPresent()) {
            EmergencyRuleTimePO emergencyRuleTimePO = optionalEmergencyRuleTimePO.get();
            //判断物联返回的设备列表是否开启
            triggered &= validateDeviceEnabled(ruleDetailVO.getEmergencyRuleConditionDevices(), event.getEventDeviceDetailList());
            triggered &= ruleTimeTypeTriggerValidatorHolder
                    .getValidator(emergencyRuleTimePO.getTimeType().name())
                    .triggerValidator(triggerTime, ObjectUtils.copying(emergencyRuleTimePO, EmergencyRuleTimeInfo.class));
        }
        if (triggered) {
            this.ruleClient.ruleTrigger(this.buildRuleTrigger(ruleDetailVO, triggerTime, ruleTriggerLog.getInstanceId(), event
                    .getEventDeviceDetailList().getFirst(), ruleTriggerLog));
        }
        log.info("规则报警处理完成,预案规则id为:{},iot规则id为{},触发时间{}", event.getRuleId(), ruleTriggerLog.getRuleId(), ruleTriggerLog.getNotifyStatusTs());
    }

    @EventListener
    public void iotEventTriggerEmergencyRule(final IotEventTriggerEmergencyRuleEvent message) {
        long identifyId = IdWorker.getId();
        EventTriggerDTO eventTriggerLog = message.getEventTriggerLog();
        //获取规则列表
        List<EmergencyRuleDetailVO> enabledRuleInfoByIds = this.emergencyRuleQueryService
                .getRuleByEventCode(message.getEventDeviceDetailList().getFirst().getProdId(),
                                    eventTriggerLog.getEventCode());
        if (CollectionUtils.isEmpty(enabledRuleInfoByIds)) {
            return;
        }
        //事件触发事件
        LocalDateTime triggerTime = DateUtils.parse(eventTriggerLog.getTimestamp());
        enabledRuleInfoByIds.parallelStream().forEach(ruleDetailVO -> {
            //如果规则设备为空或者规则设备包含触发事件设备则符合规则
            if (CollectionUtils.isEmpty(ruleDetailVO.getEmergencyRuleConditionDevices()) ||
                ruleDetailVO.getEmergencyRuleConditionDevices().stream()
                        .anyMatch(device -> Long.valueOf(eventTriggerLog.getDeviceId()).equals(device.getDeviceId()))) {
                log.info("消费唯一Id为:{},事件报警消费,规则为{},事件编号为:{}事件时间:{},开始处理时间:{}", identifyId, ruleDetailVO.getId(), message
                        .getEventTriggerLog().getEventCode(), message.getEventTriggerLog().getNotifyStatusTs(), System.currentTimeMillis());
                RuleCustomTriggerParam ruleCustomTriggerParam = this.buildRuleTrigger(ruleDetailVO, triggerTime, eventTriggerLog.getInstanceId(), message
                        .getEventDeviceDetailList().getFirst(), eventTriggerLog);
                ruleDetailVO.getEmergencyRuleTimes().forEach(emergencyRuleTimeVO -> {
                    try {
                        boolean triggered = ruleTimeTypeTriggerValidatorHolder
                                .getValidator(emergencyRuleTimeVO.getTimeType().name())
                                .triggerValidator(triggerTime, ObjectUtils.copying(emergencyRuleTimeVO, EmergencyRuleTimeInfo.class));
                        triggered &= validateDeviceEnabled(ruleDetailVO.getEmergencyRuleConditionDevices(), message.getEventDeviceDetailList());
                        if (!triggered) {
                            return;
                        }
                        if (!validateEventParamCondition(eventTriggerLog.getParams(), eventTriggerLog.getEventCode(), emergencyRuleTimeVO, ruleDetailVO)) {
                            log.info("事件报警消费,规则为{},事件编号为:{}事件参数内容:{},事件未达到触发条件", ruleDetailVO.getId(), message
                                    .getEventTriggerLog().getEventCode(), message.getEventTriggerLog().getParams());
                            return;
                        }
                        this.ruleClient.ruleTrigger(ruleCustomTriggerParam);
                    } catch (Exception e) {
                        log.error("消费唯一Id为:{},事件报警消费处理失败,规则为{},失败异常{}", identifyId, ruleDetailVO.getId(), e.getMessage());
                    }
                });
                log.info("消费唯一Id为:{},事件报警消费处理完成,规则为{},事件编号为:{},事件时间:{},结束处理时间:{}", identifyId, ruleDetailVO.getId(), message
                        .getEventTriggerLog().getEventCode(), message.getEventTriggerLog().getNotifyStatusTs(), System.currentTimeMillis());
            }
        });
    }

    private boolean validateDeviceEnabled(List<EmergencyRuleConditionDeviceCollectionVO> emergencyRuleConditionDevices,
                                          @NonNull List<DeviceInfoResponse> eventDeviceDetailList
                                         ) {
        //规则设备为空也触发
        if (ListUtils.isEmpty(emergencyRuleConditionDevices)) {
            return true;
        }
        //enable有可能为null，设置null为true，防止map出错
        Map<Long, Boolean> ruleConditionDeviceIdToEnabledMap = MapUtils.listToMap(emergencyRuleConditionDevices, EmergencyRuleConditionDeviceCollectionVO::getDeviceId, this::getEnabled);
        for (DeviceInfoResponse deviceInfoResponse : eventDeviceDetailList) {
            Long deviceId = deviceInfoResponse.getId();
            //报警规则条件设备不包含响应的设备信息，则返回false，不触发
            if (!ruleConditionDeviceIdToEnabledMap.containsKey(deviceId)) {
                return false;
            }
            Boolean enabled = ruleConditionDeviceIdToEnabledMap.get(deviceId);
            //设备为禁用状态，则返回false，不触发
            if (enabled != null && !enabled) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证事件参数条件
     * <p>
     * 逻辑关系说明：
     * 1. 多个 emergencyRuleConditionCollections 之间的关系是 OR（或）
     * 2. 单个 emergencyRuleConditionCollection 下面的多个 EmergencyRuleConditionExps 是 AND（且）
     * 3. 支持通过 logicalOp 字段配置逻辑关系，便于后续扩展
     *
     * @param params                事件参数
     * @param eventCode             事件编码
     * @param emergencyRuleTimeVO   预案规则时间配置
     * @param emergencyRuleDetailVO 预案规则详情
     *
     * @return true-条件满足，false-条件不满足
     */
    private boolean validateEventParamCondition(Map<String, Object> params,
                                                String eventCode,
                                                EmergencyRuleTimeVO emergencyRuleTimeVO,
                                                EmergencyRuleDetailVO emergencyRuleDetailVO
                                               ) {
        // 参数为空，默认通过
        if (MapUtils.isEmpty(params)) {
            return true;
        }

        // 筛选出当前事件的参数设置条件集合
        var emergencyRuleConditionCollections = emergencyRuleDetailVO.getEmergencyRuleConditionCollections().stream()
                .filter(condition -> eventCode.equals(condition.getDeviceEventCode()) &&
                                     EventConfigTypeEnum.PARAMETER_SETTING == condition.getEventConfigType()
                                     && condition.getGroupId().equals(emergencyRuleTimeVO.getGroupId()))
                .toList();

        // 无条件配置，默认通过
        if (CollectionUtils.isEmpty(emergencyRuleConditionCollections)) {
            return true;
        }

        // 无表达式或表达式为空，默认通过
        if (CollectionUtils.isEmpty(emergencyRuleDetailVO.getEmergencyRuleConditionExps()) ||
            emergencyRuleDetailVO.getEmergencyRuleConditionExps().stream().noneMatch(exp -> StringUtils.isNotBlank(exp.getPropCode()))) {
            return true;
        }

        // 构建 条件主键 到表达式列表的映射
        Map<Long, List<EmergencyRuleConditionExpCollectionVO>> conditionIdToExpsMap = emergencyRuleDetailVO.getEmergencyRuleConditionExps()
                .stream()
                .filter(exp -> StringUtils.isNotBlank(exp.getPropCode()))
                .collect(Collectors.groupingBy(EmergencyRuleConditionExpCollectionVO::getEmergencyRuleConditionId));

        // 多个条件之间是 OR 关系：只要有一个满足即可
        for (var conditionCollection : emergencyRuleConditionCollections) {
            Long conditionId = conditionCollection.getId();
            List<EmergencyRuleConditionExpCollectionVO> exps = conditionIdToExpsMap.get(conditionId);

            // 当前组无表达式，跳过
            if (CollectionUtils.isEmpty(exps)) {
                continue;
            }
            // 验证当前组的所有表达式（根据 logicalOp 决定是 AND 还是 OR）
            boolean groupResult = validateConditionGroup(params, exps, conditionCollection.getLogicalOp());
            // 如果当前组满足，则整体满足（OR 关系）
            if (groupResult) {
                return true;
            }
        }
        // 所有组都不满足
        return false;
    }

    /**
     * 验证条件组（支持 AND/OR 逻辑）
     *
     * @param params    事件参数
     * @param exps      表达式列表
     * @param logicalOp 逻辑运算符（AND/OR）
     *
     * @return true-组条件满足，false-组条件不满足
     */
    private boolean validateConditionGroup(Map<String, Object> params,
                                           List<EmergencyRuleConditionExpCollectionVO> exps,
                                           LogicalOpEnum logicalOp
                                          ) {
        // 默认为 AND 逻辑
        if (logicalOp == null) {
            logicalOp = LogicalOpEnum.AND;
        }

        // 根据逻辑运算符进行不同的验证
        if (logicalOp == LogicalOpEnum.OR) {
            // OR 逻辑：只要有一个表达式满足即可
            return exps.stream().anyMatch(exp -> evaluateExpression(params, exp));
        } else {
            // AND 逻辑：所有表达式都必须满足
            return exps.stream().allMatch(exp -> evaluateExpression(params, exp));
        }
    }

    /**
     * 计算单个表达式是否满足
     *
     * @param params 事件参数
     * @param exp    表达式
     *
     * @return true-满足，false-不满足
     */
    private boolean evaluateExpression(Map<String, Object> params, EmergencyRuleConditionExpCollectionVO exp) {
        String propCode = exp.getPropCode();
        var op = exp.getOp();
        String operand = exp.getOprand();

        // 获取参数值
        Object paramValue = params.get(propCode);
        if (paramValue == null) {
            log.debug("参数值为空，propCode={}", propCode);
            return false;
        }

        // 根据比较符进行条件判断
        try {
            return switch (op) {
                case EQ -> paramValue.toString().equals(operand);
                case NE -> !paramValue.toString().equals(operand);
                case GT -> Double.parseDouble(paramValue.toString()) > Double.parseDouble(operand);
                case GE -> Double.parseDouble(paramValue.toString()) >= Double.parseDouble(operand);
                case LT -> Double.parseDouble(paramValue.toString()) < Double.parseDouble(operand);
                case LE -> Double.parseDouble(paramValue.toString()) <= Double.parseDouble(operand);
                case IN -> evaluateInCondition(paramValue.toString(), operand);
                case NOT_IN -> !evaluateInCondition(paramValue.toString(), operand);
                case BETWEEN -> evaluateBetweenCondition(paramValue.toString(), operand);
                default -> {
                    log.warn("不支持的比较符: {}", op);
                    yield false;
                }
            };
        } catch (NumberFormatException e) {
            log.error("参数值转换为数字失败: propCode={}, paramValue={}, operand={}", propCode, paramValue, operand, e);
            return false;
        }
    }

    /**
     * 计算 IN 条件
     *
     * @param paramValue 参数值
     * @param operand    操作数（逗号分隔的值列表）
     *
     * @return true-在列表中，false-不在列表中
     */
    private boolean evaluateInCondition(String paramValue, String operand) {
        String[] inValues = operand.split(",");
        for (String inValue : inValues) {
            if (paramValue.equals(inValue.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算 BETWEEN 条件
     *
     * @param paramValue 参数值
     * @param operand    操作数（逗号分隔的范围值）
     *
     * @return true-在范围内，false-不在范围内
     */
    private boolean evaluateBetweenCondition(String paramValue, String operand) {
        String[] betweenValues = operand.split(",");
        if (betweenValues.length != 2) {
            log.warn("BETWEEN 条件格式错误，期望 2 个值，实际: {}", betweenValues.length);
            return false;
        }
        try {
            double min = Double.parseDouble(betweenValues[0].trim());
            double max = Double.parseDouble(betweenValues[1].trim());
            double value = Double.parseDouble(paramValue);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            log.error("BETWEEN 条件计算失败: paramValue={}, operand={}", paramValue, operand, e);
            return false;
        }
    }

    @NonNull
    private Boolean getEnabled(@NonNull EmergencyRuleConditionDeviceCollectionVO emergencyRuleConditionDeviceCollectionVO) {
        Boolean enabled = emergencyRuleConditionDeviceCollectionVO.getEnabled();
        return enabled == null || enabled;
    }

    private RuleCustomTriggerParam buildRuleTrigger(EmergencyRuleDetailVO ruleDetailVO, LocalDateTime triggerTime,
                                                    String iotEventInstanceId,
                                                    DeviceInfoResponse eventDeviceDetail, Object triggerInfo
                                                   ) {
        EmergencyRuleBaseVO ruleBaseVO = ObjectUtils.copying(ruleDetailVO, EmergencyRuleBaseVO.class);
        return RuleCustomTriggerParam.builder()
                .id(ruleDetailVO.getId())
                .code(String.valueOf(ruleDetailVO.getId()))
                .name(ruleDetailVO.getName())
                .ruleConfig(JsonUtils.serialize(ruleBaseVO))
                .applicationCode(ruleDetailVO.getApplicationCode())
                .applicationName(ruleDetailVO.getApplicationName())
                .businessType(BusinessTypeEnum.EMERGENCY_CENTER_ALARM)
                .enabled(ruleDetailVO.getEnabled())
                .reiterated(true)
                .type(RuleTypeEnum.ALARM_RULE)
                .repeatTime(ruleDetailVO.getRepeatTime())
                .repeatTimeUnit(DateUnitEnum.valueOf(ruleDetailVO.getRepeatTimeUnit()))
                .triggerLevelId(ruleDetailVO.getAlarmLevelId())
                .triggerTypeId(ruleDetailVO.getEmergencyTypeId())
                .triggerTargetId(eventDeviceDetail.getId().toString())
                .triggerTime(triggerTime)
                .iotEventInstanceId(iotEventInstanceId)
                .snapshot(triggerInfo)
                .eventConfig(this.buildRuleTriggerEventConfig(ruleDetailVO, eventDeviceDetail, triggerTime))
                .build();
    }

    private RuleTriggerEventConfig buildRuleTriggerEventConfig(EmergencyRuleDetailVO ruleDetailVO, DeviceInfoResponse eventDeviceDetail,
                                                               LocalDateTime triggerTime
                                                              ) {
        SessionContextHolder.requestTenant(ruleDetailVO.getTenant());
        SatAlarmLevelDTO alarmLevel = this.satAlarmLevelClient.getAlarmLevel(ruleDetailVO.getAlarmLevelId());
        return RuleTriggerEventConfig.builder()
                .triggerTargetKey(eventDeviceDetail.getId())
                .triggerTargetCode(eventDeviceDetail.getCode())
                .triggerTargetName(eventDeviceDetail.getName())
                .triggerTargetTypeId(String.valueOf(eventDeviceDetail.getProdCategoryCode()))
                .triggerTargetTypeName(eventDeviceDetail.getProdCategoryName())
                .triggerTargetLongitude(eventDeviceDetail.getLntAndLat())
                .triggerTargetAddress(eventDeviceDetail.getLocationDes())
                .triggerTime(DateUtils.format(triggerTime))
                .triggerTypeName(ruleDetailVO.getEmergencyTypeName())
                .triggerLevelName(alarmLevel.getName())
                .area(eventDeviceDetail.getAreaId())
                .areaName(eventDeviceDetail.getAreaName())
                .event(String.valueOf(ruleDetailVO.getId()))
                .eventName(ruleDetailVO.getName())
                .eventTypeId(ruleDetailVO.getEmergencyEventTypeId())
                .eventTypeCode(ruleDetailVO.getEmergencyEventTypeCode())
                .eventTypeName(ruleDetailVO.getEmergencyEventTypeName())
                .timeoutProcessTimes(ruleDetailVO.getEstimatedHandlerTime())
                .timeoutProcessTimesUnit(ruleDetailVO.getEstimatedHandlerUnit())
                .build();
    }
}