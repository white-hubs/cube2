package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.consumer;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.CoupleService;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway.CoupleQueryService;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.entity.Couple;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration.CoupleStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.valueobj.CoupleStatus;
import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.consumer.message.TradeCompletedTallyMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmCoupleTradeCompletedListenerOrderly implements MessageListenerOrderly {

    private final CoupleService coupleService;

    private final CoupleQueryService coupleQueryService;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt message : msgs) {
            try {
                String messageStr = new String(message.getBody());
                TradeCompletedTallyMessage tradeCompletedTallyMessage = JsonUtils.deserialize(messageStr, TradeCompletedTallyMessage.class);
                var reportTime = tradeCompletedTallyMessage.getReportTime();
                log.info("接收到挂钩交易消息体为:{}", JsonUtils.serialize(tradeCompletedTallyMessage));
                var now = LocalDateTime.now();
                //消息处理,如果上报时间小于当前时间过长直接抛弃,暂定24小时
                if (DateUtils.betweenMs(reportTime, now) / 1000 > 1000 * 60 * 60 * 24) {
                    log.info("挂钩交易状态消费到的时间超过上报时间过长,不更改状态,消息数据:{}", messageStr);
                    return ConsumeOrderlyStatus.SUCCESS;
                }
                Set<String> coupleCodes = tradeCompletedTallyMessage.getDetails().stream()
                        .map(TradeCompletedTallyMessage.Hook::getHookNumber)
                        .collect(Collectors.toSet());
                //最后一次更新时间小于上报时间的挂钩集合
                List<Couple> lastUpdateCouples = this.coupleQueryService.getCouples(coupleCodes);
                List<Couple> couples = new ArrayList<>();
                tradeCompletedTallyMessage.getDetails().stream().forEach(hook -> {
                    Optional<Couple> optionalCouple = lastUpdateCouples.stream()
                            .filter(couple -> hook.getHookNumber().equals(couple.getCode())).findFirst();
                    if (!optionalCouple.isPresent()) {
                        Couple couple = new Couple(IdWorker.getId(),
                                                   hook.getHookNumber(),
                                                   new CoupleStatus(CoupleStatusEnum.TRADE_COMPLETED),
                                                   reportTime);
                        couples.add(couple);
                    } else {
                        Couple couple = optionalCouple.get();
                        if (couple.getReportTime().isBefore(reportTime)) {
                            couple.changeCoupleStatus(new CoupleStatus(CoupleStatusEnum.TRADE_COMPLETED));
                            couple.changeReportTime(reportTime);
                            couples.add(couple);
                        } else {
                            log.error("挂钩交易状态上次修改状态时间大于消息上报时间,该消息不做处理,消息内容为:{}", messageStr);
                        }
                    }
                });
                //更改挂钩状态为已交易
                this.coupleService.saveCouples(couples);
                return ConsumeOrderlyStatus.SUCCESS;
            } catch (Exception ex) {
                // 异常处理逻辑
                log.error("挂钩交易状态消息消费失败,失败原因:{}", ex);
                if (ex.getCause() instanceof JsonParseException ||
                    ex.getCause() instanceof JsonMappingException) {
                    return ConsumeOrderlyStatus.SUCCESS;
                }
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
