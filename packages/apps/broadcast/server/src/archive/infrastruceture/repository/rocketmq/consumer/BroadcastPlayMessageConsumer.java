package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastPlayPlanService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.BroadcastPlayTextCommandService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextDeviceCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayPlanPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.BroadcastPlayPlanRepository;
import com.guhecloud.cube.biz.subsystem.broadcast.server.schedule.infrastruceture.rocketmq.message.BroadcastPlayMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.subsystem.requester.sdk.client.BroadcastClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "broadcast_play_text", topic = BroadcastPlayMessage.TOPIC, selectorType = SelectorType.TAG)
public class BroadcastPlayMessageConsumer implements RocketMQListener<BroadcastPlayMessage> {

    private final BroadcastPlayTextCommandService broadcastPlayTextCommandService;

    private final BroadcastClient broadcastClient;

    private final BroadcastPlayPlanRepository broadcastPlayPlanRepository;

    private final BroadcastPlayPlanService broadcastPlayPlanService;

    @Override
    public void onMessage(BroadcastPlayMessage message) {
        SessionContextHolder.requestTenant(message.getTenant());
        //使用分布式锁,避免被重复处理
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(message.getPlanId().toString(), Expiration.from(60000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                BroadcastPlayPlanPO broadcastPlayPlanPO = broadcastPlayPlanService.getById(message.getPlanId());
                if (Objects.isNull(broadcastPlayPlanPO)) {
                    return;
                }
                BroadcastPlayPlan broadcastPlayPlan = JsonUtils.deserialize(broadcastPlayPlanPO.getPlayParam(), BroadcastPlayPlan.class);
                if (CollectionUtils.isEmpty(broadcastPlayPlan.getBroadcastPlayText().getDeviceInfos())) {
                    return;
                }
                if (StringUtils.isNotEmpty(broadcastPlayPlan.getCurrentPlayTextTaskId())) {
                    try {
                        Integer state = broadcastClient.devicePlayTextState(broadcastPlayPlan.getCurrentPlayTextTaskId());
                        //播放中,不再重复播放
                        if (1 == state) {
                            return;
                        }
                    } catch (Exception e) {
                        log.error("获取广播任务执行状态失败,播放任务id为{},错误原因:{}", broadcastPlayPlan.getCurrentPlayTextTaskId(), e.getMessage());
                    }
                }
                //封装成command命令
                SecurityContextHolder.setPrincipal(Principal.builder().id(Long.valueOf(broadcastPlayPlan.getOperatorBy())).type(Principal.Type.USER).build());
                BroadcastPlayTextCommand broadcastPlayTextCommand = new BroadcastPlayTextCommand();
                broadcastPlayTextCommand.setId(broadcastPlayPlan.getBroadcastPlayText().getBroadcastTemplate().getId());
                broadcastPlayTextCommand.setBroadcastPlayPlanId(message.getPlanId());
                broadcastPlayTextCommand.setBroadcastPlayNum(broadcastPlayPlan.getBroadcastPlayText().getBroadcastPlayNum());
                broadcastPlayTextCommand.setBroadcastPlayVariableParams(broadcastPlayPlan.getBroadcastPlayText()
                                                                                .getBroadcastPlayVariableParams());
                broadcastPlayTextCommand.setBroadcastPlayDevices(ObjectUtils.copying(broadcastPlayPlan.getBroadcastPlayText()
                                                                                             .getDeviceInfos(), BroadcastPlayTextDeviceCommand.class));
                String taskId = this.broadcastPlayTextCommandService.playText(broadcastPlayTextCommand);
                broadcastPlayPlan.settingCurrentPlayTextTask(taskId);
                broadcastPlayPlanRepository.save(broadcastPlayPlan);
            }
        } catch (Exception e) {
            log.error("广播循环播放失败任务id为:{},异常原因:", message.getPlanId(), e);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }
}
