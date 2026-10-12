package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot;

import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ChangeRuleStateParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.ModifyDeviceNotifyRuleEnabledEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.ModifyDeviceNotifyRuleEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.factory.RuleDefinitionFactory;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * IoT平台规则适配器
 * 用于监听设备通知规则变更事件并向IoT平台下发规则
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IotPlatformRuleAdapter {

    private final IotLinkageClient iotLinkageClient;

    @EventListener
    public void handleModifyDeviceNotifyRuleEvent(final ModifyDeviceNotifyRuleEvent event) {
        DeviceNotifyRule deviceNotifyRule = event.getDeviceNotifyRule();

        try {
            log.info("开始向IoT平台下发设备通知规则，设备ID：{}", deviceNotifyRule.getDeviceId());
            // 1. 将设备通知规则转换为IoT平台所需的规则格式
            var ruleCreateParam = RuleDefinitionFactory.buildRuleCreateParam(event.getDeviceNotifyRule());
            if (Objects.isNull(ruleCreateParam)) {
                return;
            }
            //iot获取规则
            if (Objects.nonNull(event.getOldIotRuleId())) {
                //删除
                this.iotLinkageClient.ruleDelete(event.getOldIotRuleId().toString());
            }
            lockAddIotRule(ruleCreateParam);
            log.info("设备通知规则下发IoT平台完成，设备ID：{}", deviceNotifyRule.getDeviceId());
        } catch (Exception e) {
            log.error("向IoT平台下发设备通知规则失败，设备ID：{}", deviceNotifyRule.getDeviceId(), e);
            // 可以考虑实现重试机制或发送告警通知
        }
    }

    @EventListener
    public void handlerModifyDeviceNotifyRuleEnabledEvent(final ModifyDeviceNotifyRuleEnabledEvent event) {
        ChangeRuleStateParam changeRuleStateParam = new ChangeRuleStateParam();
        changeRuleStateParam.setRuleNums(Collections.singletonList(event.getDeviceNotifyRule().getIotRuleId().toString()));
        changeRuleStateParam.setEnabled(event.getDeviceNotifyRule().getEnabled());
        this.iotLinkageClient.changeRuleStateBatch(changeRuleStateParam);
    }

    public void lockAddIotRule(RuleCreateParam ruleCreateParam) {
        //启用分布式锁,防止处理时重复操作
        RedissonRedLock iotRuleLock = RedisUtils.getDistributedLock()
                .tryLock(ruleCreateParam.getRuleNum(), Expiration.from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(iotRuleLock)) {
                RuleDetailDTO iotRule = this.iotLinkageClient.ruleDetail(ruleCreateParam.getRuleNum());
                if (Objects.isNull(iotRule)) {
                    //规则下发到IOT
                    this.iotLinkageClient.ruleAdd(ruleCreateParam);
                }
            }
        } catch (Exception e) {
            log.error("新增iot规则失败,失败原因", e);
        } finally {
            if (Objects.nonNull(iotRuleLock)) {
                iotRuleLock.unlock();
            }
        }
    }
}
