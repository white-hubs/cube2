package com.guhecloud.cube.biz.work.center.server.job;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.work.center.server.service.HandoverRecordService;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkCenterNoticeJobHandler {
    public static final String SHIFT_OVER_MESSAGE_OVERDUE = "SHIFT_OVER_MESSAGE_OVERDUE";
    private final HandoverRecordService handoverRecordService;
    private final AssemblyNoticeClient assemblyNoticeClient;

    /**
     * 任务通知检测 每分钟执行一次
     * 即将超时、超时、任务进度提醒
     *
     * @param param 暂未使用的入参
     *
     * @return
     */
    @JobAnnotation(name = "workCenterNoticeJobHandler", cron = "0 */1 * * * ?", desc = "工作中心消息通知检查")
    public void workCenterNoticeJobHandler(String param) {
        // 通知检测
        try {
            //获取租户
            var tenantList = handoverRecordService.getTaskTenantList();
            if (CollectionUtils.isEmpty(tenantList)) {
                log.info("未查询到租户信息");
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            for (Integer tenant : tenantList) {
                SessionContextHolder.requestTenant(tenant);
                // 获取任务失效通知配置
                var messageSendConfigs = assemblyNoticeClient
                        .getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                  .businessType("WORK_CENTER")
                                                  .sceneCodes(Collections.singletonList("SHIFT_OVER_MESSAGE"))
                                                  .build());
                if (CollectionUtils.isEmpty(messageSendConfigs)) {
                    continue;
                }
                MessageSendConfig messageSendConfig = messageSendConfigs.getFirst();
                if (Objects.isNull(messageSendConfig.getValue())) {
                    continue;
                }
                var startDate = DateUtils.offsetMinute(now, -1);
                List<HandoverRecordVO> overdueTask = handoverRecordService.getOverdueTask(new Period(startDate, now),
                                                                                          messageSendConfig.getValue());
                for (HandoverRecordVO handoverRecordVO : overdueTask) {
                    sendMessage(handoverRecordVO, messageSendConfig, Collections.singleton(handoverRecordVO.getTakeOverUserId()), null);
                }
                if (StringUtils.isBlank(messageSendConfig.getConfig())) {
                    continue;
                }
                List<HandoverRecordVO> willOverdueTask = handoverRecordService.getOverdueTask(new Period(startDate, now),
                                                                                              Integer.parseInt(messageSendConfig.getConfig()));
                for (HandoverRecordVO handoverRecordVO : willOverdueTask) {
                    Set<Long> userIds = messageSendConfig.getNoticeObjList().stream().map(t -> Long.parseLong(t.getObjId()))
                            .collect(Collectors.toSet());
                    sendMessage(handoverRecordVO, messageSendConfig, userIds, SHIFT_OVER_MESSAGE_OVERDUE);
                }
            }

        } catch (Exception e) {
            log.error("日常任务消息通知检查失败,失败原因:", e);
        } finally {
            SessionContextHolder.requestTenant(null);
        }
    }

    private void sendMessage(HandoverRecordVO taskPO, MessageSendConfig messageSendConfig, Set<Long> notifyUsers,
                             String shiftOverMessageOverdue
                            ) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", taskPO.getShiftOverUserName());
        paramMap.put("taskName", taskPO.getRecordName());
        MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                            .businessId(String.valueOf(taskPO.getId()))
                                                                            .notifyId(messageSendConfig.getNotifyId())
                                                                            .notifyUsers(notifyUsers)
                                                                            .msgSceneCode(StringUtils.isNotBlank(shiftOverMessageOverdue) ?
                                                                                          shiftOverMessageOverdue
                                                                                                                                          : messageSendConfig.getSceneCode())
                                                                            .tempParams(paramMap)
                                                                            .build());
        log.info("工作交接超时消息发送结果:{}", JsonUtils.serialize(response));
    }
}
