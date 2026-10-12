package com.guhecloud.cube.srv.gen.product.market.server.pricing.message.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnUserMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnUser;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PriceWarnEvent;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PricingTaskEvent;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PricingTaskReceivedEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PricingTaskEventHandler {

    private final GoodsPricingRecordMapper goodsPricingRecordMapper;
    private final GoodsPriceWarnUserMapper goodsPriceWarnUserMapper;
    private final MessageSendClient messageSendClient;

    /**
     * 消息发送
     *
     * @param taskPublishedEvent
     */
    @EventListener
    public void sendMessage(final PricingTaskEvent taskPublishedEvent) {
        try {
            SessionContextHolder.requestTenant(taskPublishedEvent.getPricingTask().getTenant());
            if (CollectionUtils.isEmpty(taskPublishedEvent.getUserIds())) {
                log.warn("商品采价任务【{}】推送没有人员。场景code{}", taskPublishedEvent.getPricingTask()
                        .getName(), taskPublishedEvent.getSceneCode());
                return;
            }
            MessageSceneSendRequest request = MessageSceneSendRequest.builder()
                    .msgSceneCode(taskPublishedEvent.getSceneCode())
                    .businessId(taskPublishedEvent.getPricingTask().getId().toString())
                    .tmpParams(taskPublishedEvent.getMsgParams())
                    .applicationCode("GOODSPURCHASEPRICE")
                    .applicationName("商品采价")
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                            .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                            .ids(taskPublishedEvent.getUserIds())
                                                                            .build()))
                    .build();
            this.messageSendClient.sceneSend(request);
        } catch (Exception e) {
            //消息发送失败
            log.error("商品采价任务推送失败,任务主键:{},场景code:{}", taskPublishedEvent.getPricingTask()
                    .getId(), taskPublishedEvent.getSceneCode(), e);
        }
    }

    @EventListener
    public void sendMessage(final PriceWarnEvent warnPublishedEvent) {
        try {
            //查询采价任务名称
            GoodsPricingRecord pricingRecord = goodsPricingRecordMapper.selectById(warnPublishedEvent.getWarnRecord().getPricingRecordId());
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("warnTime", DateUtils.format(warnPublishedEvent.getWarnRecord().getCreateTime(), "yyyy-MM-dd"));//任务名称
            msgParams.put("warnName", warnPublishedEvent.getWarnRecord().getRuleName());//预警名称
            msgParams.put("pricingTaskName", pricingRecord != null ? pricingRecord.getTaskName() : "近期采价");
            msgParams.put("goodsCategoryName", warnPublishedEvent.getWarnRecord().getGoodsCategoryName());
            msgParams.put("warnReason", warnPublishedEvent.getWarnRecord().getPriceExpDesc());
            //查询要推送的人员
            List<GoodsPriceWarnUser> goodsPriceWarnUsers = goodsPriceWarnUserMapper.selectList(Wrappers.<GoodsPriceWarnUser>lambdaQuery()
                                                                                                       .eq(GoodsPriceWarnUser::getGroupId, warnPublishedEvent
                                                                                                               .getWarnRecord()
                                                                                                               .getGroupId()));
            if (!CollectionUtils.isEmpty(goodsPriceWarnUsers)) {
                this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                         .msgSceneCode("PRICE_WARN_PUBLISHED_USER_NOTIFY")
                                                         .businessId(String.valueOf(warnPublishedEvent.getPriceTaskId()))
                                                         .tmpParams(msgParams)
                                                         .tenant(SessionContextHolder.getRequester().getTenant())
                                                         .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                 .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                 .ids(goodsPriceWarnUsers
                                                                                                                              .stream()
                                                                                                                              .map(GoodsPriceWarnUser::getUserId)
                                                                                                                              .collect(Collectors.toSet()))
                                                                                                                 .build()))
                                                         .build());
            }
        } catch (Exception e) {
            //消息发送失败
            log.error("商品价格预警消息,预警主键:{}", warnPublishedEvent.getWarnRecord().getId(), e);
        }
    }

    @EventListener
    public void sendMessage(final PricingTaskReceivedEvent taskReceivedEvent) {
        try {
            Set<Long> userIds = Arrays.stream(taskReceivedEvent.getPricingTaskDetail().getUserIds().split(","))
                    .map(Long::parseLong).collect(Collectors.toSet());
            this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                     .msgSceneCode(taskReceivedEvent.getSceneCode())
                                                     .businessId(taskReceivedEvent.getPricingTask().getId().toString())
                                                     .tmpParams(taskReceivedEvent.getMsgParams())
                                                     .tenant(SessionContextHolder.getRequester().getTenant())
                                                     .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                             .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                             .ids(userIds)
                                                                                                             .build()))
                                                     .build());
        } catch (Exception e) {
            //消息发送失败
            log.error("商品采价任务推送失败,任务主键:{}", taskReceivedEvent.getPricingTask().getId(), e);
        }
    }
}
