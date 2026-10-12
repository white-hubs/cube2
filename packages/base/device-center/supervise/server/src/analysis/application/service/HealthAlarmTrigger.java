package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.linkage.client.RuleClient;
import com.guhecloud.cube.biz.linkage.client.param.RuleTriggerParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthProblemMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthValueMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthProblemPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthValuePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthAlarmRuleVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/5 14:45
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthAlarmTrigger {
    private final RuleClient ruleClient;
    private final HealthAlarmConfigService healthAlarmConfigService;
    private final DeviceHealthProblemMapper deviceHealthProblemMapper;
    private final DeviceHealthValueMapper deviceHealthValueMapper;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Getter
    public static class EventConfig {
        /**
         * 级别主键
         */
        private Long triggerLevelId;

        /**
         * 级别名称
         */
        private String triggerLevelName;

        private Long triggerTypeId;

        /**
         * 类型名称
         */
        private String triggerTypeName;

        private Long triggerTargetKey;

        private String triggerTargetCode;
        private String triggerTargetName;

        private String triggerTargetTypeId;
        private String triggerTargetTypeName;

        private Long area;

        private String areaName;

        @Setter
        private String triggerThreshold;

        private String event;

        private String eventName;

        @Setter
        private String snapshot;

        /**
         * 触发时间
         */
        private String triggerTime;

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }

    public void triggered(Long deviceId, EventConfig eventConfig, Integer healthValue) {
        HealthAlarmRuleVO alarmRuleVO = healthAlarmConfigService.getByDeviceInstanceId(deviceId);

        DeviceDetailDTO detail = deviceCenterStdClient.getDeviceById(deviceId);

        eventConfig.triggerTargetCode = detail.getBaseArchivesInfo().getCode();
        eventConfig.triggerTargetKey = detail.getBaseArchivesInfo().getId();
        eventConfig.triggerTargetTypeId = detail.getProductInfo().getProductCategory().getCode();
        eventConfig.triggerTargetTypeName = detail.getProductInfo().getProductCategory().getName();
        eventConfig.triggerTargetName = detail.getBaseArchivesInfo().getName();
        eventConfig.area = detail.getBaseArchivesInfo().getArea().getKey();
        eventConfig.areaName = detail.getBaseArchivesInfo().getArea().getVal();

        eventConfig.eventName = "设备健康度[" + healthValue + "]低于阈值[" + alarmRuleVO.getThresholdValue() + "]";
        eventConfig.triggerTime = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
        eventConfig.triggerLevelId = alarmRuleVO.getLevelId();
        eventConfig.triggerLevelName = alarmRuleVO.getLevelName();
        eventConfig.triggerTypeId = alarmRuleVO.getAlarmTypeId();
        eventConfig.triggerTypeName = alarmRuleVO.getAlarmTypeName();
        eventConfig.setTriggerThreshold(healthValue.toString());

        // 获取最新一次的检测问题项
        List<DeviceHealthValuePO> healthValuePOS =
                deviceHealthValueMapper.selectLast(Sets.newHashSet(deviceId));
        if (CollectionUtils.isNotEmpty(healthValuePOS)) {
            DeviceHealthValuePO deviceHealthValuePO = healthValuePOS.getFirst();
            List<DeviceHealthProblemPO> deviceHealthProblemPOS = deviceHealthProblemMapper.selectList(
                    Wrappers.<DeviceHealthProblemPO>lambdaQuery()
                            .eq(DeviceHealthProblemPO::getDeviceHealthValueId, deviceHealthValuePO.getId())
                                                                                                     );

            List<String> problems = deviceHealthProblemPOS.stream().map(DeviceHealthProblemPO::getDesc).collect(Collectors.toList());
            eventConfig.setSnapshot(JSONObject.toJSONString(problems));
        }

        // 触发报警
        RuleTriggerParam ruleTriggerParam = new RuleTriggerParam();
        ruleTriggerParam.setTriggerTime(LocalDateTime.now());
        ruleTriggerParam.setEventConfig(eventConfig);
        ruleClient.ruleTrigger(alarmRuleVO.getTriggeredRuleId(), ruleTriggerParam);
    }

}
