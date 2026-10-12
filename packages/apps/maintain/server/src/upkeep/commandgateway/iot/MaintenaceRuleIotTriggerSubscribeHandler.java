package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.iot;

import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceTasksCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceRuleCondition;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 规则触发属性回调
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenaceRuleIotTriggerSubscribeHandler {

    private final SubscribeContainer subscribeContainer;
    private final MaintenanceRuleConditionMapper ruleConditionMapper;
    private final MaintenancePlanMapper planMapper;
    private final MaintenanceTasksCommandService tasksCommandService;

    @EventListener
    public void subscribeRuleTrigger(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerRuleTriggerEventMsgHandler((message) -> {
            MaintenanceRuleCondition ruleConditionList = ruleConditionMapper.selectById(Long.valueOf(message.getRuleNum()));
            if (Objects.nonNull(ruleConditionList)) {
                MaintenancePlan plan = planMapper.selectById(ruleConditionList.getEmergencyRuleId());
                if (ObjectUtils.isNotEmpty(plan) && MaintenacePlanStatusEnum.ENABLED.name().equals(plan.getStatus())) {
                    tasksCommandService.saveTask(DateUtils.format(LocalDateTime.now()), plan);
                }
            }
        });
    }
}
