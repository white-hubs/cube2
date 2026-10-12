package com.guhecloud.cube.srv.gen.maintain.server.electron.job;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.MessageModuleTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskMessageJob {
    private final PatrolTaskService patrolTaskService;
    private final PatrolPlanService patrolPlanService;
    private final AssemblyNoticeClient assemblyNoticeClient;

    @JobAnnotation(name = "ElectronTaskMessageJob", cron = "0 */10 * * * ?", desc = "巡更-电子巡更任务通知")
    public void electronAddTask(String param) {
        log.info("电子巡更开始检查任务开始、结束情况");
        SessionContextHolder.clearSessionContext();
        var tenantList = patrolTaskService.getAllTenant();
        var now = LocalDateTime.now();
        for (var tenant : tenantList) {
            SessionContextHolder.requestTenant(tenant);
            patrolTaskService.changeToWaitAccept(LocalDateTime.now());
            try {
                // 发送通知信息
                var module = Stream.of(MessageModuleTypeEnum.ELECTRON_TASK_EXPIRE, MessageModuleTypeEnum.ELECTRON_TASK_RECEIVE)
                        .map(Enum::name)
                        .collect(Collectors.toList());
                var maintainElectron = assemblyNoticeClient.getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                                                     .businessType(MaintainApplicationEnum.ELECTRONIC_PATROL.name())
                                                                                     .sceneCodes(module).build());
                log.info("配置文件数量：{}", maintainElectron.size());
                maintainElectron.forEach(ex -> {
                    var time = DateUtils.offset(LocalDateTime.now(), ChronoUnit.MINUTES, ex.getValue());
                    var dateTimeStr = DateUtils.format(time);
                    var dateStr = dateTimeStr.substring(0, dateTimeStr.length() - 2).concat("00");
                    var dateTime = DateUtils.parse(dateStr);
                    if (MessageModuleTypeEnum.ELECTRON_TASK_EXPIRE.name().equals(ex.getSceneCode())) {
                        patrolTaskService.taskEndMessage(ex.getNotifyId(), dateTime, ex.getValue());
                    } else if (MessageModuleTypeEnum.ELECTRON_TASK_RECEIVE.name().equals(ex.getSceneCode())) {
                        patrolTaskService.taskStartMessage(ex.getNotifyId(), dateTime, ex.getValue());
                    }
                });
            } catch (Exception e) {
                log.error("计划发送消息异常：", e);
            }
        }
        SessionContextHolder.clearSessionContext();
        try {
            // 更新巡更任务过期状态
            log.info("更新巡更任务过期状态");
            patrolTaskService.updateTaskStatus(now);
        } catch (Exception e) {
            log.error("更新巡更任务过期状态:", e);
        }
        try {
            var date = now;
            if (StringUtils.isNotBlank(param)) {
                date = DateUtils.parse(param);
            }
            log.info("计划生成任务开始");
            Boolean bool = patrolPlanService.jobGenerateTask(date);
            log.info("计划生成任务结果:{}", bool);
        } catch (Exception e) {
            log.error("任务更新状态失败:", e);
        }
        log.info("电子巡更检查任务开始、结束情况结束");
    }
}
