package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.impl;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.biz.linkage.client.LinkageClient;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleTriggerParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.EventDto;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AlarmRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AlarmRuleNameEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AlarmRulesService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class InteriorAdapterAlarmServiceImpl implements AdapterAlarmService {

    @Autowired
    @Lazy
    private AlarmRulesService alarmRulesService;
    @Autowired
    private SatAlarmLevelClient satAlarmLevelClient;
    @Autowired
    private RuleClient ruleClient;
    @Autowired
    private LinkageClient linkageClient;

    @Override
    public void pushAlarm(MeterDevice device, BigDecimal lastDeviceValue, BigDecimal readeValue) {

        List<AlarmRules> alarmRules = alarmRulesService.getRules(Long.valueOf(device.getDeviceId()), Integer.valueOf(SessionContextHolder
                                                                                                                             .getRequester()
                                                                                                                             .getTenant()));
        if (alarmRules.size() > 0) {
            alarmPush(device, lastDeviceValue, alarmRules, readeValue);
        }
    }

    @Override
    public List<SatAlarmLevelDTO> listAlarmLevels() {

        return satAlarmLevelClient.listAlarmLevels();
    }

    @Override
    public Long save(RuleModifyParam ruleModifyParam) {
        Long save = ruleClient.save(ruleModifyParam);
        return save;
    }

    @Override
    public void update(Long id, RuleModifyParam param) {
        ruleClient.update(id, param);
    }

    @Override
    public void updateEnabled(Long id, Boolean enabled) {
        ruleClient.updateEnabled(id, enabled);
    }

    @Override
    public void remove(Long id) {
        ruleClient.remove(id);
    }

    @Override
    public List<LinkageActionDTO> getLinkageActions(Long ruleId) {

        return linkageClient.getLinkageActions(ruleId);
    }

    //报警推送
    private void alarmPush(MeterDevice device, BigDecimal lastDeviceValue, List<AlarmRules> rlarmRules, BigDecimal readeValue) {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal nowReadeValue = device.getLastReadeValue();//上期用量
        BigDecimal subtract = readeValue.subtract(lastDeviceValue);//本期用量 = 上期表值-本期表值
        BigDecimal subtract1 = subtract.subtract(nowReadeValue);//用量差值
        for (AlarmRules rlarmRule : rlarmRules) {
            String alarmValue = rlarmRule.getAlarmValue();
            if (rlarmRule.getAlarmRule().equals(AlarmRuleNameEnum.FLOATING_TOPLIMIT.getCode())) {
                if (nowReadeValue.compareTo(new BigDecimal("0")) == 1) {
                    BigDecimal divide = subtract1.abs().divide(nowReadeValue, 2, RoundingMode.HALF_UP);//用量百分比
                    BigDecimal multiply = divide.multiply(new BigDecimal("100"));
                    int i = multiply.compareTo(new BigDecimal(alarmValue));
                    if (i == 1) {
                        pushEvent(device, rlarmRule, divide);
                    }
                }

            } else if (rlarmRule.getAlarmRule().equals(AlarmRuleNameEnum.RATED_CONSUMPTION.getCode())) {
                int i = subtract.compareTo(new BigDecimal(alarmValue));
                if (i == 1) {
                    pushEvent(device, rlarmRule, subtract);
                }
            } else if (rlarmRule.getAlarmRule().equals(AlarmRuleNameEnum.ZERO_DOSAGE_DURATION.getCode())) {
                LocalDateTime nowReadeTime = device.getLastReadeTime();
                long between = DateUtils.between(nowReadeTime, now, ChronoUnit.HOURS);
                int i = new BigDecimal(between).compareTo(new BigDecimal(rlarmRule.getAlarmValue()));
                int i1 = subtract.compareTo(new BigDecimal("0.00"));
                if (i == 1 && i1 == 0) {
                    pushEvent(device, rlarmRule, new BigDecimal(between));
                }
            }
        }

    }

    private void pushEvent(MeterDevice device, AlarmRules rlarmRules, BigDecimal value) {
        SatAlarmLevelDTO alarmLevel = satAlarmLevelClient.getAlarmLevel(Long.valueOf(rlarmRules.getAlarmLevel()));
        SessionContextHolder.requestTenant(device.getTenant());
        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date);
        EventDto build = EventDto.builder()
                .eventName(rlarmRules.getAlarmName())
                .event(rlarmRules.getMessageId().toString())
                .triggerTargetCode(device.getCode())
                .triggerTargetKey(Long.valueOf(device.getDeviceId()))
                .triggerLevelId(Long.valueOf(rlarmRules.getId()))
                .triggerLevelName(alarmLevel.getName())
                .area(device.getDeviceAreaId())
                .areaName(device.getDeviceArea())
                .triggerTargetName(device.getDeviceName())
                .triggerThreshold(value.toString())
                .triggerTypeId(Long.valueOf(device.getDeviceTypeId()))
                .triggerTypeName(device.getDeviceType())
                .triggerTargetTypeName(InstrumentTypeEnum.getFireinspEnum(device.getInstrumentType()))
                .triggerTargetTypeId(device.getInstrumentType())
                .triggerTime(format1)
                .build();
        RuleTriggerParam ruleTriggerParam = new RuleTriggerParam();
        ruleTriggerParam.setTriggerTime(date);
        ruleTriggerParam.setEventConfig(build);
        ruleClient.ruleTrigger(rlarmRules.getMessageId(), ruleTriggerParam);
    }

    @Override
    public List<EnergyAlarmVO> getRecentAlarms() {
        return null;
    }

    @Override
    public EnergyAlarmStatVO energyAlarmStatForHandle() {
        return null;
    }

    @Override
    public List<EnergyAlarmStatVO> energyAlarmStatForLevel() {
        return null;
    }
}
