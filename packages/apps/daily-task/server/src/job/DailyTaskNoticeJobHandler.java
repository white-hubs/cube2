package com.guhecloud.cube.biz.daily.task.job;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/19 16:13
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskNoticeJobHandler {

    private final DailyTaskService dailyTaskService;
    private final DailyTaskUserService dailyTaskUserService;
    private final AssemblyNoticeClient assemblyNoticeClient;

    /**
     * 任务通知检测 每分钟执行一次
     * 即将超时、超时、任务进度提醒
     *
     * @param param 暂未使用的入参
     *
     * @return
     */
    @JobAnnotation(name = "dailyTaskNoticeJobHandler", cron = "0 */1 * * * ?", desc = "日常任务消息通知检查")
    public void dailyTaskNoticeJobHandler(String param) {
        // 通知检测
        try {
            //获取租户
            var tenantList = dailyTaskService.getTaskTenantList();
            var businessTypeList = dailyTaskService.getTaskType();
            if (CollectionUtils.isEmpty(tenantList)) {
                log.info("未查询到租户信息");
                return;
            }
            for (Integer tenant : tenantList) {
                SessionContextHolder.requestTenant(tenant);
                // 获取任务失效通知配置
                var messageSendConfigs = assemblyNoticeClient
                        .getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                  .businessType(StringUtils.join(",", businessTypeList))
                                                  .sceneCodes(DailyTaskNoticeMessageEnum.getJobNoticeSceneCode())
                                                  .build());
                if (CollectionUtils.isEmpty(messageSendConfigs)) {
                    continue;
                }
                // 根据业务类型分组
                var noticeTypeConfigMap = messageSendConfigs.stream()
                        .collect(Collectors.groupingBy(MessageSendConfig::getBusinessType));
                for (var entry : noticeTypeConfigMap.entrySet()) {
                    var groupByScene = entry.getValue().stream()
                            .collect(Collectors.groupingBy(MessageSendConfig::getSceneCode));
                    for (var messageSendConfig : groupByScene.entrySet()) {
                        // 根据业务类型获取需要发送通知的任务
                        List<DailyTaskPO> noticeTask = DailyTaskNoticeMessageEnum.valueOf(messageSendConfig.getKey())
                                .sendMessage(messageSendConfig.getValue());
                        var messageSendConfigMap = messageSendConfig.getValue().stream()
                                .collect(Collectors.toMap(MessageSendConfig::getBusinessType, Function.identity(), (k1, k2) -> k1));
                        if (CollectionUtils.isEmpty(noticeTask)) {
                            continue;
                        }
                        Map<Long, Set<Long>> notifyUserMap = new HashMap<>();
                        if (DailyTaskNoticeMessageEnum.DAILY_TASK_WILL_OVERDUE.name().equals(messageSendConfig.getKey())) {
                            notifyUserMap = Optional.ofNullable(dailyTaskUserService.lambdaQuery()
                                                                        .in(DailyTaskUserPO::getMasterId, noticeTask.stream()
                                                                                .map(DailyTaskPO::getId).collect(Collectors.toList()))
                                                                        .list()).orElse(new ArrayList<>())
                                    .stream().collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId,
                                                                            Collectors.mapping(DailyTaskUserPO::getMasterId, Collectors.toSet())));
                        }
                        for (DailyTaskPO taskPO : noticeTask) {
                            MessageSendConfig config = messageSendConfigMap.get(taskPO.getDailyType());
                            if (ObjectUtils.isEmpty(config)) {
                                continue;
                            }
                            sendMessage(taskPO, config, notifyUserMap.get(taskPO.getId()));
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("日常任务消息通知检查失败,失败原因:", e);
        } finally {
            SessionContextHolder.requestTenant(null);
        }
    }

    private void sendMessage(DailyTaskPO taskPO, MessageSendConfig messageSendConfig, Set<Long> notifyUsers) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("taskName", taskPO.getName());
        paramMap.put("process",
                     Objects.isNull(taskPO.getProgress()) ? "0" : taskPO.getProgress().multiply(new BigDecimal(100)).toString() + "%");
        MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                            .businessId(String.valueOf(taskPO.getId()))
                                                                            .notifyId(messageSendConfig.getNotifyId())
                                                                            .notifyUsers(notifyUsers)
                                                                            .msgSceneCode(messageSendConfig.getSceneCode())
                                                                            .tempParams(paramMap)
                                                                            .build());
        log.info("日常任务消息发送结果:{}", JsonUtils.serialize(response));
    }
}
