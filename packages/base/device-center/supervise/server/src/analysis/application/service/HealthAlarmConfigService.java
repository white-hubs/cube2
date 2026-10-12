package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.BusinessTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.RuleTypeEnum;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.SatAlarmTypeClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.dto.RuleDTO;
import com.guhecloud.cube.biz.linkage.client.param.RuleBasicModifyParam;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.param.ConfigAlarmRuleParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.HealthAlarmObjectMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.HealthAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.HealthAlarmObjectPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.HealthAlarmRulePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthAlarmRuleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/8/5 14:14
 * @description
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthAlarmConfigService {

    private final HealthAlarmRuleMapper healthAlarmRuleMapper;
    private final HealthAlarmObjectMapper healthAlarmObjectMapper;
    private final RuleClient ruleClient;
    private final SatAlarmTypeClient satAlarmTypeClient;
    private final SatAlarmLevelClient satAlarmLevelClient;
    private final DeviceCenterStdClient deviceCenterStdClient;

    public void enableAlarmRule(Long deviceInstanceId, boolean flag) {
        HealthAlarmObjectPO healthAlarmObjectPO = healthAlarmObjectMapper.selectOne(
                Wrappers.<HealthAlarmObjectPO>lambdaQuery()
                        .eq(HealthAlarmObjectPO::getDeviceInstanceId, deviceInstanceId)
                                                                                   );
        if (Objects.nonNull(healthAlarmObjectPO)) {
            HealthAlarmRulePO healthAlarmRulePO = healthAlarmRuleMapper.selectById(healthAlarmObjectPO.getAlarmRuleId());
            healthAlarmRulePO.setEnable(flag);
            healthAlarmRuleMapper.updateById(healthAlarmRulePO);
        } else {
            throw new ServiceException("启用失败，请先配置");
        }
    }

    public HealthAlarmRuleVO getByDeviceInstanceId(Long deviceInstanceId) {
        HealthAlarmObjectPO healthAlarmObjectPO = healthAlarmObjectMapper.selectOne(
                Wrappers.<HealthAlarmObjectPO>lambdaQuery()
                        .eq(HealthAlarmObjectPO::getDeviceInstanceId, deviceInstanceId)
                                                                                   );
        DeviceDetailDTO device = deviceCenterStdClient.getDeviceById(deviceInstanceId);
        if (Objects.isNull(healthAlarmObjectPO)) {
            HealthAlarmRuleVO healthAlarmRuleVO = new HealthAlarmRuleVO();
            healthAlarmRuleVO.setName(device.getBaseArchivesInfo().getName());
            return healthAlarmRuleVO;
        }

        HealthAlarmRulePO healthAlarmRulePO = healthAlarmRuleMapper.selectById(healthAlarmObjectPO.getAlarmRuleId());

        SessionContextHolder.requestTenant(healthAlarmRulePO.getTenant());
        RuleDTO rule = ruleClient.getRule(healthAlarmRulePO.getTriggeredRuleId());
        List<LinkageActionDTO> linkageActionVOS = rule.getLinkageActionVOS();
        HealthAlarmRuleVO ruleVO = ObjectUtils.copying(healthAlarmRulePO, HealthAlarmRuleVO.class);

        ruleVO.setName(device.getBaseArchivesInfo().getName());

        SatAlarmTypeDTO alarmType = satAlarmTypeClient.getByApplicationCode(rule.getRuleBasicVO().getApplicationCode());
        if (Objects.nonNull(alarmType)) {
            ruleVO.setAlarmTypeName(alarmType.getName());
        }

        try {
            SatAlarmLevelDTO alarmLevel = satAlarmLevelClient.getAlarmLevel(healthAlarmRulePO.getLevelId());
            ruleVO.setLevelName(alarmLevel.getName());
        } catch (Exception e) {
            ruleVO.setLevelName(healthAlarmRulePO.getLevelId().toString());
        }

        ruleVO.setRuleBasicDTOS(linkageActionVOS);
        return ruleVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void configAlarmRule(Long deviceInstanceId, ConfigAlarmRuleParam configAlarmRuleParam) {
        HealthAlarmRulePO healthAlarmRulePO = healthAlarmRuleMapper.selectById(configAlarmRuleParam.getId());
        if (Objects.isNull(healthAlarmRulePO)) {
            Long ruleId = ruleClient.save(buildRuleModifyParam(configAlarmRuleParam));
            healthAlarmRulePO = ObjectUtils.copying(configAlarmRuleParam, HealthAlarmRulePO.class);
            healthAlarmRulePO.setTriggeredRuleId(ruleId);
            healthAlarmRuleMapper.insert(healthAlarmRulePO);
        } else {
            ObjectUtils.copying(configAlarmRuleParam, healthAlarmRulePO);
            ruleClient.update(healthAlarmRulePO.getTriggeredRuleId(), buildRuleModifyParam(configAlarmRuleParam));
            healthAlarmRuleMapper.updateById(healthAlarmRulePO);
        }

        healthAlarmObjectMapper.delete(
                Wrappers.<HealthAlarmObjectPO>lambdaQuery()
                        .eq(HealthAlarmObjectPO::getDeviceInstanceId, deviceInstanceId)
                                      );

        HealthAlarmObjectPO healthAlarmObjectPO = new HealthAlarmObjectPO();
        healthAlarmObjectPO.setAlarmRuleId(healthAlarmRulePO.getId());
        healthAlarmObjectPO.setDeviceInstanceId(deviceInstanceId);
        healthAlarmObjectMapper.insert(healthAlarmObjectPO);
    }

    private RuleModifyParam buildRuleModifyParam(ConfigAlarmRuleParam configAlarmRuleParam) {
        /**
         * 采用内置规则
         */
        RuleModifyParam ruleModifyParam = new RuleModifyParam();
        RuleBasicModifyParam ruleBasicModifyParam = new RuleBasicModifyParam();

        if (Objects.isNull(configAlarmRuleParam.getId())) {
            configAlarmRuleParam.setId(IdWorker.getId());
        }
        //        ruleBasicModifyParam.setCode("health_alarm");
        ruleBasicModifyParam.setApplicationCode("EQUIPMENT_SUPERVISION");
        ruleBasicModifyParam.setApplicationName("设备监管");
        ruleBasicModifyParam.setEnabled(true);
        ruleBasicModifyParam.setName("健康报警规则");
        ruleBasicModifyParam.setTriggerLevelId(configAlarmRuleParam.getLevelId());
        ruleBasicModifyParam.setTriggerLevelName(configAlarmRuleParam.getLevelName());
        // todo 报警类型
        ruleBasicModifyParam.setTriggerTypeId(configAlarmRuleParam.getAlarmTypeId());
        ruleBasicModifyParam.setReiterated(false);
        ruleBasicModifyParam.setTriggerTypeName("健康报警");
        ruleBasicModifyParam.setType(RuleTypeEnum.ALARM_RULE);
        ruleBasicModifyParam.setBusinessType(BusinessTypeEnum.DEVICE_HEALTH_ALARM);

        ruleModifyParam.setLinkageEmergencyActionModifies(configAlarmRuleParam.getLinkageEmergencyActionModifies());
        ruleModifyParam.setBasicModify(ruleBasicModifyParam);
        JSONObject ruleConfig = new JSONObject();
        ruleConfig.put("thresholdValue", configAlarmRuleParam.getThresholdValue());
        ruleConfig.put("id", configAlarmRuleParam.getId());
        ruleModifyParam.setRuleConfig(ruleConfig.toJSONString());
        return ruleModifyParam;
    }
}
