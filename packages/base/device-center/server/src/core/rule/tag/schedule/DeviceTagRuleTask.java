package com.guhecloud.cube.base.device.center.core.rule.tag.schedule;

import com.guhecloud.cube.base.device.center.core.rule.RuleParse;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.cube.base.device.center.core.rule.tag.DeviceTagRuleChangeDiff;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.TagRuleRedisCache;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.entity.TagRule;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceService;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceTagRuleChange;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DeviceTagRuleTask {

    private final TagRuleRedisCache tagRuleRedisCache;

    private final DeviceTagRuleChangeDiff deviceTagRuleChangeDiff;

    private final CenterDeviceService centerDeviceService;

    private static final ThreadFactory THREAD_FACTORY = runnable -> {
        return new Thread(runnable, "device-tag-rule-task");
    };

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, THREAD_FACTORY);

    public DeviceTagRuleTask(TagRuleRedisCache tagRuleRedisCache, DeviceTagRuleChangeDiff deviceTagRuleChangeDiff,
                             CenterDeviceService centerDeviceService
                            ) {
        this.tagRuleRedisCache = tagRuleRedisCache;
        this.deviceTagRuleChangeDiff = deviceTagRuleChangeDiff;
        this.centerDeviceService = centerDeviceService;
        scheduler.scheduleWithFixedDelay(this::deviceTagRuleChangeTask, 60, 20, TimeUnit.SECONDS);
    }

    public void deviceTagRuleChangeTask() {
        long startTime = System.currentTimeMillis();
        try {
            doDeviceTagRuleChangeTask();
        } catch (Exception e) {
            log.error("设备标签规则变更定时任务执行失败:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
            long cost = System.currentTimeMillis() - startTime;
            if (cost > TimeUnit.MINUTES.toMillis(1)) {
                log.warn("设备标签规则变更定时任务执行耗时较长,cost:{}ms", cost);
            }
        }
    }

    private void doDeviceTagRuleChangeTask() {
        Map<String, TagRule> allCache = tagRuleRedisCache.getAllCache();
        if (MapUtils.isEmpty(allCache)) {
            log.info("scheduler skip,device_tag_rule is empty");
            return;
        }
        for (Map.Entry<String, TagRule> entry : allCache.entrySet()) {
            TagRule value = entry.getValue();
            if (Objects.isNull(value)) {
                log.warn("scheduler skip,device_tag_rule cache value is null,key:{}", entry.getKey());
                continue;
            }
            if (!Boolean.TRUE.equals(value.getUpdated())) {
                log.info("scheduler skip,device_tag_rule not change,code:{}", entry.getKey());
                continue;
            }
            //存在变更
            tagRuleChange(value);
        }
    }

    public void tagRuleChange(TagRule tagRule) {
        if (Objects.isNull(tagRule)) {
            log.warn("scheduler skip,device_tag_rule is null");
            return;
        }
        if (Objects.isNull(tagRule.getTenant()) || Objects.isNull(tagRule.getCode())) {
            log.warn("scheduler skip,device_tag_rule tenant or code is null,tenant:{},code:{}",
                     tagRule.getTenant(), tagRule.getCode());
            return;
        }
        RedissonRedLock redLock = null;
        try {
            String key = tagRule.getTenant() + "_" + tagRule.getCode();
            redLock = RedisUtils.getDistributedLock().tryLock(key, Expiration.from(2, TimeUnit.MINUTES));
            if (Objects.nonNull(redLock)) {
                Integer tenant = tagRule.getTenant();
                SessionContextHolder.requestTenant(tenant);
                Optional<TagRule> tagRuleOptional = tagRuleRedisCache.getCache(tagRule.getCode());
                if (tagRuleOptional.isPresent() && Boolean.TRUE.equals(tagRuleOptional.get().getUpdated())) {
                    //存在变更
                    RuleGroup ruleGroup = RuleParse.parseRuleConfig(tagRule.getRule());
                    Set<Long> removeTagDeviceIds = deviceTagRuleChangeDiff.removeDeviceIds(tagRule.getCode(), ruleGroup);
                    Set<Long> newTagDeviceIds = deviceTagRuleChangeDiff.newDeviceIds(tagRule.getCode(), ruleGroup);
                    centerDeviceService.batchRemoveAndAddTagRef(tagRule.getCode(), removeTagDeviceIds, newTagDeviceIds);
                    tagRule.setUpdated(false);
                    tagRuleRedisCache.saveCache(tagRule);
                    //发送标签绑定设备变更事件
                    RocketMQContextHolder.send(DeviceTagRuleChange.TOPIC, new DeviceTagRuleChange(tagRule.getCode()));
                }
            }
        } catch (Exception e) {
            log.error("设备标签规则变更同步修改失败:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
            unlock(redLock, tagRule);
        }
    }

    private void unlock(RedissonRedLock redLock, TagRule tagRule) {
        if (Objects.isNull(redLock)) {
            return;
        }
        try {
            redLock.unlock();
        } catch (Exception e) {
            log.warn("设备标签规则变更同步释放锁失败,tenant:{},code:{}",
                     tagRule.getTenant(), tagRule.getCode(), e);
        }
    }
}
