package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.subscribe;

import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommonTriggerPart;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.executor.RuleTriggerExecutor;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 设备订阅服务<br/>
 * 向IOT平台订阅设备状态等信息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIOTSubscribeService {

    private final SubscribeContainer subscribeContainer;

    private final MedicalRuleService medicalRuleService;

    @EventListener
    public void onSystemReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("洁净手术室系统准备就绪，订阅报警规则");
        subscribeContainer.registerRuleTriggerEventMsgHandler(msg -> {
            SessionContextHolder.clearSessionContext();
            RuleVO ruleVO = this.medicalRuleService.queryByIotRuleId(Long.valueOf(msg.getRuleId()));
            if (Objects.isNull(ruleVO)) {
                log.warn("洁净手术室报警规则触发失败,规则不存在,规则id:{}", msg.getRuleId());
                return;
            }
            CommonTriggerPart commonTriggerPart = msg.getMatchSnapshot().get(0);
            Optional<RuleNormVO> optionalRuleNormVO = ruleVO.getRuleNormList().stream()
                    .filter(ruleNormVO -> ruleNormVO.getNormCode().equals(commonTriggerPart.getPropCode()))
                    .findAny();
            if (!optionalRuleNormVO.isPresent()) {
                log.warn("洁净手术室报警规则触发失败,规则属性不存在,规则id:{},属性code:{}", msg.getRuleId(), commonTriggerPart.getPropCode());
                return;
            }
            BigDecimal triggerValue = new BigDecimal(commonTriggerPart.getPropValue());
            RuleNormVO ruleNormVO = optionalRuleNormVO.get();
            //判定低限或高限是否满足当前条件
            boolean lowerCondition = commonTriggerPart.getCondition().contains("LT") &&
                                     triggerValue.compareTo(ruleNormVO.getLowerLimit()) > 0;
            boolean upperCondition = commonTriggerPart.getCondition().contains("GT") &&
                                     triggerValue.compareTo(ruleNormVO.getUpperLimit()) < 0;
            if (lowerCondition || upperCondition || ruleNormVO.getDisplay()==0) {
                return;
            }
            //触发报警
            RuleTriggerExecutor.triggerRule(ruleVO, ruleNormVO, msg, commonTriggerPart);
        });
    }
}
