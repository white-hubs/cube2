package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.iot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceTasksCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 规则触发事件回调
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenaceRuleIotEventSubscribeHandler {

    private final SubscribeContainer subscribeContainer;
    private final MaintenanceRuleConditionMapper ruleConditionMapper;

    private final MaintenanceTasksCommandService tasksCommandService;
    private final MaintenancePlanMapper planMapper;

    @EventListener
    public void subscribeDeviceEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceEventMsgHandler((message) -> {
            DeviceEventTriggerMsg ruleTriggerDTO = ObjectUtils.copying(message, DeviceEventTriggerMsg.class);
            List<MaintenanceRuleCondition> ruleConditionList = ruleConditionMapper.selectList(new LambdaQueryWrapper<MaintenanceRuleCondition>()
                                                                                                      .eq(MaintenanceRuleCondition::getDeviceEventCode, ruleTriggerDTO.getEventCode()));
            if (CollectionUtils.isNotEmpty(ruleConditionList)) {
                ruleConditionList.forEach(ex -> {
                    MaintenancePlan plan = planMapper.selectById(ex.getEmergencyRuleId());
                    if (ObjectUtils.isNotEmpty(plan) && MaintenacePlanStatusEnum.ENABLED.name().equals(plan.getStatus())) {
                        tasksCommandService.saveTask(DateUtils.format(LocalDateTime.now()), plan);
                    }
                });
            }
            log.info("{}", message.getDeviceId(), message);
        });
    }
}
