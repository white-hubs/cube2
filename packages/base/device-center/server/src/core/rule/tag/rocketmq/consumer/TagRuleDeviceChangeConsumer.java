package com.guhecloud.cube.base.device.center.core.rule.tag.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.rule.RuleCompare;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.TagRuleGroupCache;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.TagRuleRedisCache;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = "TAG_RULE_DEVICE_CHANGE", topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG)
@Slf4j
@Component
public class TagRuleDeviceChangeConsumer implements RocketMQListener<DeviceChange> {

    private final TagRuleGroupCache tagRuleGroupCache;

    private final CenterBizDeviceMapper centerBizDeviceMapper;

    private final TagRuleRedisCache tagRuleRedisCache;

    public TagRuleDeviceChangeConsumer(TagRuleGroupCache tagRuleGroupCache,
                                       CenterBizDeviceMapper centerBizDeviceMapper,
                                       TagRuleRedisCache tagRuleRedisCache
                                      ) {
        this.tagRuleGroupCache = tagRuleGroupCache;
        this.centerBizDeviceMapper = centerBizDeviceMapper;
        this.tagRuleRedisCache = tagRuleRedisCache;
    }

    @Override
    public void onMessage(DeviceChange message) {
        //防止有时候存在上下文信息
        SessionContextHolder.clearSessionContext();
        SecurityContextHolder.clearSecurityContext();
        if (message == null || CollectionUtils.isEmpty(message.getDeviceChangePairs())) {
            log.warn("skip device tag rule change message because device change pairs is empty");
            return;
        }
        for (var deviceChangePair : message.getDeviceChangePairs()) {
            if (deviceChangePair == null || deviceChangePair.getKey() == null || deviceChangePair.getKey().getDeviceId() == null) {
                log.warn("skip invalid device tag rule change pair");
                continue;
            }
            Long deviceId = deviceChangePair.getKey().getDeviceId();
            if (DeviceChangeEnum.DELETE == deviceChangePair.getVal()) {
                log.info("skip deleted device tag rule change, deviceId:{}", deviceId);
                continue;
            }

            var bizDevice = centerBizDeviceMapper.selectById(deviceId);
            if (bizDevice == null) {
                log.warn("skip device tag rule change because device is not found, deviceId:{}, changeType:{}",
                         deviceId, deviceChangePair.getVal());
                continue;
            }

            refreshMatchedTagRules(bizDevice);
        }
    }

    private void refreshMatchedTagRules(BizDevice bizDevice) {
        //快速从内存中匹配标签规则条件
        var ruleGroupMap = tagRuleGroupCache.getAll(bizDevice.getTenant());
        if (MapUtils.isEmpty(ruleGroupMap)) {
            log.info("device_tag_rule cache is empty");
            return;
        }
        SessionContextHolder.requestTenant(bizDevice.getTenant());
        for (var tagCode : ruleGroupMap.keySet()) {
            var areDeviceMatch = RuleCompare.areDeviceMatch(bizDevice, ruleGroupMap.get(tagCode));
            if (areDeviceMatch) {
                var tagRuleOptional = tagRuleRedisCache.getCache(tagCode);
                tagRuleOptional.ifPresent(tagRule -> {
                    tagRule.setUpdated(true);
                    tagRuleRedisCache.saveCache(tagRule);
                });
            }
        }
    }
}
