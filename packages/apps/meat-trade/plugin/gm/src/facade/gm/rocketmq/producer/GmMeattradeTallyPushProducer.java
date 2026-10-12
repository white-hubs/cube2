package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer;

import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.message.GmTallyDetailMessage;
import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.message.GmTallyReportMessage;
import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.wapper.GmMeattradeProducerWarp;
import com.guhecloud.cube.gm.meattrade.plugin.tally.application.TallyPushService;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.enumeration.TallyPushStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.po.TallyPushPO;
import com.guhecloud.cube.gm.meattrade.plugin.tally.infrastructure.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 光明理货数据推送
 *
 * @author lanchun
 * @date 2023/4/13 16:51
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnBean(name = "guangMingRocketMQProperties")
public class GmMeattradeTallyPushProducer {

    private final TallyPushService tallyPushService;

    private final GmMeattradeProducerWarp gmMeattradeProducerWarp;

    @Value("${guangming.meattrade.arrange.weighCompliant:false}")
    private boolean weighCompliant;

    public void sendMessage(TallyPushPO tallyPushPO) {
        PorkArrangeMessage porkArrangeFinishedMessage = JsonUtils.deserialize(tallyPushPO.getPushContent(), PorkArrangeMessage.class);
        GmTallyReportMessage gmTallyReportMessage = getGmTallyReportMessage(porkArrangeFinishedMessage);
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(tallyPushPO.getId().toString(), Expiration.from(5000, TimeUnit.MILLISECONDS));
        try {
            TallyPushPO tallyPushInfo = this.tallyPushService.getById(tallyPushPO.getId());
            if (Objects.nonNull(tallyPushInfo) && TallyPushStatusEnum.PUSHED != tallyPushInfo.getPushStatus()) {
                SendResult sendResult = this.gmMeattradeProducerWarp.send(new Message(this.gmMeattradeProducerWarp.getTopic(), "*", JsonUtils
                        .serialize(gmTallyReportMessage).getBytes()), 300000);
                if (Objects.nonNull(sendResult)) {
                    log.info("光明理货数据MQ发送状态为:{},MsgId为:{},Topic为:{},brokerName为:{}",
                             sendResult.getSendStatus().name(),
                             sendResult.getMsgId(),
                             Objects.nonNull(sendResult.getMessageQueue()) ? sendResult.getMessageQueue().getTopic() : "",
                             Objects.nonNull(sendResult.getMessageQueue()) ? sendResult.getMessageQueue().getBrokerName() : "");
                }
                this.tallyPushService.updateStatus(tallyPushPO.getId(), TallyPushStatusEnum.PUSHED);
            }
        } catch (Exception e) {
            log.error("光明理货数据推送失败", e);
            this.tallyPushService.updateStatus(tallyPushPO.getId(), TallyPushStatusEnum.PUSH_FAILED);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }

    private GmTallyReportMessage getGmTallyReportMessage(PorkArrangeMessage porkArrangeFinishedMessage) {
        //批次信息
        GmTallyReportMessage gmTallyReportMessage = GmTallyReportMessage.builder()
                .appKey(this.gmMeattradeProducerWarp.getAppKey())
                .batchNo(porkArrangeFinishedMessage.getPorkReportCode())
                .tallyMachineNo(porkArrangeFinishedMessage.getPlace())
                .num(porkArrangeFinishedMessage.getSlices())
                .weight(porkArrangeFinishedMessage.getWeight())
                .tallyEndTime(DateUtils.format(porkArrangeFinishedMessage.getPorkArrangeTime()))
                .build();
        List<GmTallyDetailMessage> gmTallyDetailMessages = new ArrayList<>();
        if (weighCompliant) {
            //理货详细信息
            porkArrangeFinishedMessage.getPorkArrangeDetailMessages()
                    .stream()
                    .filter(porkArrangeDetailMessage -> Objects.nonNull(porkArrangeDetailMessage.getWeightCompliant()) &&
                                                        weighCompliant == porkArrangeDetailMessage.getWeightCompliant().booleanValue())
                    .forEach(porkArrangeDetailMessage ->
                                     gmTallyDetailMessages.add(GmTallyDetailMessage.builder()
                                                                       .hookNumber(porkArrangeDetailMessage.getLabel())
                                                                       .footNumber(porkArrangeDetailMessage.getFootMarkCode())
                                                                       .weight(porkArrangeDetailMessage.getWeight())
                                                                       .build())
                            );
        } else {
            //理货详细信息
            porkArrangeFinishedMessage.getPorkArrangeDetailMessages().stream()
                    .forEach(porkArrangeDetailMessage ->
                                     gmTallyDetailMessages.add(GmTallyDetailMessage.builder()
                                                                       .hookNumber(porkArrangeDetailMessage.getLabel())
                                                                       .footNumber(porkArrangeDetailMessage.getFootMarkCode())
                                                                       .weight(porkArrangeDetailMessage.getWeight())
                                                                       .build())
                            );
        }
        gmTallyReportMessage.setDetails(gmTallyDetailMessages);
        return gmTallyReportMessage;
    }
}
