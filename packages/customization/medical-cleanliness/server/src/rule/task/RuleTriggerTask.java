package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommonTriggerPart;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleNotifyConfig;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device.DeviceInfoDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device.DevicePropHistoryDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service.MedicalIotDevicePropService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service.MedicalIotDeviceService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerNormParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleNotifyConfigService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleTriggerService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RuleTriggerTask implements Runnable {

    private final RuleVO ruleVO;

    private final RuleNormVO ruleNormVO;

    private final RuleTriggerMsg ruleTriggerMsg;

    private final CommonTriggerPart commonTriggerPart;

    public RuleTriggerTask(RuleVO ruleVO,
                           RuleNormVO ruleNormVO,
                           RuleTriggerMsg ruleTriggerMsg,
                           CommonTriggerPart commonTriggerPart
                          ) {
        this.ruleVO = ruleVO;
        this.ruleNormVO = ruleNormVO;
        this.ruleTriggerMsg = ruleTriggerMsg;
        this.commonTriggerPart = commonTriggerPart;
    }

    @Override
    public void run() {
        try {
            SessionContextHolder.requestTenant(ruleVO.getTenant());
            MedicalRuleNotifyConfigService medicalRuleNotifyConfigService = ApplicationContextHolder.getBean(MedicalRuleNotifyConfigService.class);
            MedicalRuleNotifyConfig medicalRuleNotifyConfig = medicalRuleNotifyConfigService.getMedicalRuleNotifyConfig();
            //获取指标历史数据
            List<DevicePropHistoryDTO> devicePropHistoriesByPropCode = getDevicePropHistoryDTOS(medicalRuleNotifyConfig);

            RuleTriggerParam ruleTriggerParam = buildRuleTriggerParam(ruleVO, commonTriggerPart, ruleTriggerMsg.getInstanceId());
            if (!CollectionUtils.isEmpty(devicePropHistoriesByPropCode)) {
                ruleTriggerParam.setNorms(buildRuleTriggerNormParams(ruleNormVO, devicePropHistoriesByPropCode));
            }
            //存入数据库
            MedicalRuleTriggerService medicalRuleTriggerService = ApplicationContextHolder.getBean(MedicalRuleTriggerService.class);
            Long ruleTriggerId = medicalRuleTriggerService.save(ruleTriggerParam);
            //消息发送
            sendMessage(medicalRuleNotifyConfig, medicalRuleNotifyConfigService, ruleTriggerId, ruleTriggerParam);
        } catch (Exception e) {
            log.error("推送报警异常,{}", e.getMessage(), e);
        }

    }

    private void sendMessage(MedicalRuleNotifyConfig medicalRuleNotifyConfig, MedicalRuleNotifyConfigService medicalRuleNotifyConfigService,
                             Long ruleTriggerId, RuleTriggerParam ruleTriggerParam
                            ) {
        Map<String, String> msgParams = new HashMap<>(2);
        msgParams.put("devicePropName", ruleNormVO.getNormName());
        msgParams.put("upperMinutes", String.valueOf(medicalRuleNotifyConfig.getTimes()));
        medicalRuleNotifyConfigService.sendMessage(msgParams, String.valueOf(ruleTriggerId), Collections.singleton(ruleTriggerParam.getDepartId()));
    }

    private List<DevicePropHistoryDTO> getDevicePropHistoryDTOS(MedicalRuleNotifyConfig medicalRuleNotifyConfig) {
        MedicalIotDevicePropService iotDevicePropService = ApplicationContextHolder.getBean(MedicalIotDevicePropService.class);
        var endTs = DateUtils.parse(ruleTriggerMsg.getDelayEventTime());
        var startTs = DateUtils.add(endTs, ChronoUnit.MILLIS, -medicalRuleNotifyConfig.getTimes() * 60 * 1000);
        //获取触发指标历史数据
        return iotDevicePropService.getDevicePropHistoriesByPropCode(commonTriggerPart.getDeviceId(),
                                                                     commonTriggerPart.getPropCode(),
                                                                     DateUtils.toTimestamp(startTs),
                                                                     DateUtils.toTimestamp(endTs));
    }

    private RuleTriggerParam buildRuleTriggerParam(RuleVO ruleVO,
                                                   CommonTriggerPart commonTriggerPart,
                                                   String iotInstanceId
                                                  ) {
        MedicalIotDeviceService medicalIotDeviceService = ApplicationContextHolder.getBean(MedicalIotDeviceService.class);
        DeviceInfoDTO deviceInfoDTO = medicalIotDeviceService.queryDevices(Long.parseLong(commonTriggerPart.getDeviceId()));
        return RuleTriggerParam.builder()
                .ruleId(ruleVO.getId())
                .ruleName(ruleVO.getName())
                .iotInstanceId(iotInstanceId)
                .productId(deviceInfoDTO.getProdId())
                .deviceId(deviceInfoDTO.getId())
                .deviceName(deviceInfoDTO.getName())
                .departId(deviceInfoDTO.getDepartmentId())
                .departName(deviceInfoDTO.getDepartmentName())
                .build();
    }

    private List<RuleTriggerNormParam> buildRuleTriggerNormParams(RuleNormVO ruleNormVO,
                                                                  List<DevicePropHistoryDTO> devicePropHistoriesByPropCode
                                                                 ) {
        return devicePropHistoriesByPropCode.stream()
                .map(norm ->
                             RuleTriggerNormParam.builder()
                                     .uploadTime(norm.getUploadTime())
                                     .normValue(norm.getValue())
                                     .unit(ruleNormVO.getUnit())
                                     .normCode(ruleNormVO.getNormCode())
                                     .normName(ruleNormVO.getNormName())
                                     .upperLimit(ruleNormVO.getUpperLimit())
                                     .lowerLimit(ruleNormVO.getLowerLimit())
                                     .build()
                    ).collect(Collectors.toList());
    }
}
