package com.guhecloud.cube.srv.gen.product.market.server.pricing.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskDetailMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PricingNoticeCodeEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.UrgencyEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event.PricingTaskEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采价延迟通知
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PricingDelayNoticeJob {
    private final GoodsPricingTaskMapper goodsPricingTaskMapper;
    private final GoodsPricingTaskDetailMapper goodsPricingTaskDetailMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AssemblyNoticeClient assemblyNoticeClient;

    /**
     * 采价任务超时通知
     *
     * @param param 无
     *
     * @return 结果
     */
    @JobAnnotation(name = "PricingDelayNoticeHandler", cron = "0 0/1 * * * ?", desc = "商品采价-采价任务通知")
    public void execute(String param) {
        // 超时未接收
        this.overtimeNotReceive();
        // 超时未完成
        this.overtimeNotComplete();
    }

    /**
     * 超时未接收提醒
     */
    private void overtimeNotReceive() {
        //查询需要通知的
        List<GoodsPricingTaskDetail> pricingTaskDetailList = goodsPricingTaskDetailMapper.selectList(Wrappers
                                                                                                             .<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                             .isNull(GoodsPricingTaskDetail::getUserId)
                                                                                                             .isNull(GoodsPricingTaskDetail::getNoticeTime));
        if (CollectionUtils.isNotEmpty(pricingTaskDetailList)) {
            var currentTime = LocalDateTime.now();
            pricingTaskDetailList.forEach(td -> {
                SessionContextHolder.requestTenant(td.getTenant());
                //获取配置时间
                Integer delayNoticeMinute = this.getDelayNoticeMinute();
                try {
                    if (delayNoticeMinute != null) {
                        //检查时间
                        var dateTime = DateUtils.offset(td.getCreateTime(), ChronoUnit.MINUTES, delayNoticeMinute);
                        if (DateUtils.toTimestamp(currentTime) >= DateUtils.toTimestamp(dateTime)) {
                            //查询采价任务
                            GoodsPricingTask goodsPricingTask = goodsPricingTaskMapper.selectById(td.getTaskId());
                            if (goodsPricingTask != null) {//解决一些异常数据
                                //参数信息
                                Map<String, String> msgParams = new HashMap<>();
                                msgParams.put("pricingTaskName", goodsPricingTask.getName());
                                msgParams.put("pricingTaskUrgency", UrgencyEnum.valueOf(goodsPricingTask.getUrgency()).getLabel());
                                Set<Long> userIds = Arrays.stream(td.getUserIds().split(","))
                                        .map(Long::parseLong).collect(Collectors.toSet());
                                applicationEventPublisher.publishEvent(new PricingTaskEvent(this, goodsPricingTask, userIds, PricingNoticeCodeEnum.PRICING_TASK_RECEIVED_NOTIFY.name(), msgParams));
                            }
                            //更新推送时间
                            td.setNoticeTime(LocalDateTime.now());
                            goodsPricingTaskDetailMapper.updateById(td);
                        }
                    }
                } catch (Exception e) {
                    log.error("采价任务接受消息，消息推送异常：", e);
                } finally {
                    SessionContextHolder.requestTenant(null);
                }
            });
        }
    }

    /**
     * 超时未完成提醒
     */
    private void overtimeNotComplete() {
        //查询超时未完成的任务
        List<GoodsPricingTask> pricingTaskList = goodsPricingTaskMapper.selectList(Wrappers.<GoodsPricingTask>lambdaQuery()
                                                                                           .ne(GoodsPricingTask::getStatus, TaskStatusEnum.OVER.name())
                                                                                           .isNotNull(GoodsPricingTask::getDeadline)
                                                                                           .le(GoodsPricingTask::getDeadline, LocalDateTime.now())
                                                                                           .isNull(GoodsPricingTask::getIncompleteNoticeTime));

        if (CollectionUtils.isEmpty(pricingTaskList)) {
            return;
        }
        // 查询任务详情
        List<GoodsPricingTaskDetail> pricingTaskDetailList = goodsPricingTaskDetailMapper.selectList(Wrappers
                                                                                                             .<GoodsPricingTaskDetail>lambdaQuery()
                                                                                                             .in(GoodsPricingTaskDetail::getTaskId, pricingTaskList
                                                                                                                     .stream()
                                                                                                                     .map(GoodsPricingTask::getId)
                                                                                                                     .collect(Collectors.toSet()))
                                                                                                             .isNotNull(GoodsPricingTaskDetail::getUserIds)
                                                                                                    );
        if (CollectionUtils.isEmpty(pricingTaskDetailList)) {
            return;
        }
        Map<Long, List<GoodsPricingTaskDetail>> pricingTaskDatailMap = pricingTaskDetailList.stream()
                .collect(Collectors.groupingBy(GoodsPricingTaskDetail::getTaskId));
        for (GoodsPricingTask goodsPricingTask : pricingTaskList) {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("pricingTaskName", goodsPricingTask.getName());
            msgParams.put("pricingTaskUrgency", UrgencyEnum.valueOf(goodsPricingTask.getUrgency()).getLabel());
            // 获取任务的任务详情数据
            List<GoodsPricingTaskDetail> detailList = pricingTaskDatailMap.get(goodsPricingTask.getId());
            Set<Long> userIds = new HashSet<>();
            // 获取任务详情的接收人
            detailList.forEach(d -> userIds.addAll(
                    Arrays.stream(d.getUserIds().split(",")).map(Long::parseLong).collect(Collectors.toSet())
                                                  ));
            applicationEventPublisher.publishEvent(new PricingTaskEvent(this, goodsPricingTask, userIds, PricingNoticeCodeEnum.PRICING_TASK_COMPLETE_NOTIFY.name(), msgParams));
            goodsPricingTaskMapper.update(null, Wrappers.<GoodsPricingTask>lambdaUpdate()
                                                  .set(GoodsPricingTask::getIncompleteNoticeTime, LocalDateTime.now())
                                                  .eq(GoodsPricingTask::getId, goodsPricingTask.getId())
                                         );
        }
    }

    private Integer getDelayNoticeMinute() {
        MessageSendSceneQueryPram queryPram = new MessageSendSceneQueryPram();
        queryPram.setBusinessType("GOODSPURCHASEPRICE");
        queryPram.setSceneCodes(Collections.singletonList(PricingNoticeCodeEnum.PRICING_TASK_RECEIVED_NOTIFY.name()));
        List<MessageSendConfig> messageSendConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
        if (CollectionUtils.isNotEmpty(messageSendConfigs)) {
            return messageSendConfigs.get(0).getValue();
        }
        return null;
    }
}
