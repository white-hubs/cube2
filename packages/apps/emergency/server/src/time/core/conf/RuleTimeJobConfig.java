package com.guhecloud.cube.biz.emergency.server.time.core.conf;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.emergency.server.time.core.helper.EmergencyRuleTimeJobHelper;
import com.guhecloud.cube.biz.emergency.server.time.core.init.EmergencyRuleTriggerJobInit;
import com.guhecloud.cube.biz.emergency.server.time.core.timer.RuleTimer;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EventBusListen(order = 1)
public class RuleTimeJobConfig implements DisposableBean {

    @Autowired
    private EmergencyRuleTriggerJobInit emergencyRuleTriggerJobInit;

    @Override
    public void destroy() throws Exception {
        //关闭预案job扫描线程
        EmergencyRuleTimeJobHelper.getRuleTimeJobHelper().stop();
        //关闭预案关闭job扫描线程
        RuleTimer.getInstance().shutdown();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void startEmergencyRuleJob(SysReadEvent event) throws Exception {
        RedissonRedLock redissonRedLock = null;
        try {
            redissonRedLock = RedisUtils.getDistributedLock().tryLock("emergencyTimerLock", Expiration.from(5000, TimeUnit.MILLISECONDS));
            if (Objects.nonNull(redissonRedLock)) {
                //启动时间轮
                RuleTimer.getInstance().start();
                //启动预案job扫描线程
                EmergencyRuleTimeJobHelper.getRuleTimeJobHelper().start();
                //扫描未执行的任务(防止重启后部分任务不执行)
                emergencyRuleTriggerJobInit.initRefreshNextTriggerCloseRuleTime();
            }
        } catch (Exception e) {
            log.error("预案时间轮启动失败,失败原因:{}", e.getMessage());
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }
}
