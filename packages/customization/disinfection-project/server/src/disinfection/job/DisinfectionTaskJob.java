package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job;

import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotifySceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionNotifyHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job.dto.NotifyTaskDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionTaskJob {
    private final static String DISINFECTION_TASK_REDIS_LOCK = "disinfection:task:";
    private final DisinfectionTaskService taskService;
    private final DisinfectionNotifyHelper disinfectionNotifyHelper;
    private final DisinfectionTaskService disinfectionTaskService;

    //固定速率执行（每隔5秒执行一次）,检测即将失效的任务并发送通知，检测失效未完成的任务并发送通知，检测任务状态
    @JobAnnotation(name = "disinfectionTaskExecute", cron = "0 0/1 * * * ?", desc = "病媒生物-定时执行消杀任务")
    public void taskWithFixedRate(String param) {
        //查询当前存在的任务所关联的租户
        var allTenants = disinfectionTaskService.listAllTenantCodes();
        allTenants.forEach(tenant -> {
            log.debug("租户:{},消杀任务定时任务开始执行=============================>", tenant);
            var token = RedisUtils.tryLock(DISINFECTION_TASK_REDIS_LOCK + tenant, Expiration.seconds(10));
            try {
                if (token == null) {
                    //获取锁失败，尝试对下个租户数据操作
                    return;
                }
                SessionContextHolder.requestTenant(tenant);
                //查询非过期、非终结、非结束的任务
                var waitHandleTasks = taskService.lambdaQuery()
                        .notIn(DisinfectionTask::getStatus, Arrays.asList(DisinfectionTaskStatusEnum.EXPIRED, DisinfectionTaskStatusEnum.FINISHED, DisinfectionTaskStatusEnum.TERMINATED))
                        .list();
                //处理过期的任务状态
                var expireTasks = handleExpireTask(waitHandleTasks);
                //处理即将失效的任务
                handleWillExpireTask(waitHandleTasks);
                //处理失效未完成的任务
                handleExpireUnfinishedTask(expireTasks);
                SessionContextHolder.clearSessionContext();
            } catch (Exception e) {
                log.error("租户:{},消杀任务定时任务执行异常=============================>", tenant, e);
            } finally {
                if (StringUtils.isNotBlank(token)) {
                    RedisUtils.unlock(DISINFECTION_TASK_REDIS_LOCK + tenant, token);
                }
            }
            log.debug("租户:{},杀任务定时任务执行结束=============================>", tenant);

        });

    }

    private void handleWillExpireTask(List<DisinfectionTask> waitHandleTasks) {
        try {
            if (ListUtils.isEmpty(waitHandleTasks)) {
                return;
            }
            //获取通知配置
            List<MessageSendConfig> configs = disinfectionNotifyHelper.getConfigs(DisinfectionNotifySceneCodeEnum.TASK_LAPSE);
            if (ListUtils.isEmpty(configs)) {
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            Set<NotifyTaskDTO> willExpireTasks = new HashSet<>();
            configs.forEach(config -> {
                Integer minutes = config.getValue();
                waitHandleTasks
                        .forEach(task -> {
                            LocalDateTime deadline = task.getDeadline();
                            //没有截至日期的默认为永远不过期
                            if (Objects.isNull(deadline)) {
                                return;
                            }
                            if (now.plusMinutes(minutes).isAfter(deadline)) {
                                willExpireTasks.add(NotifyTaskDTO.builder().disinfectionTask(task).messageSendConfig(config).build());
                            }
                        });
            });

            //发送通知
            disinfectionNotifyHelper.sendDisinfectionTaskWillExpireMessage(willExpireTasks);
        } catch (Exception e) {
            log.error("处理即将失效任务异常", e);
        }

    }

    private void handleExpireUnfinishedTask(List<DisinfectionTask> expireTasks) {
        if (ListUtils.isEmpty(expireTasks)) {
            return;
        }

        disinfectionNotifyHelper.sendDisinfectionTaskExpireMessage(expireTasks);
    }

    private List<DisinfectionTask> handleExpireTask(List<DisinfectionTask> waitHandleTasks) {
        if (ListUtils.isEmpty(waitHandleTasks)) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();
        List<DisinfectionTask> expireTasks = waitHandleTasks.stream()
                .filter(task -> {
                    LocalDateTime deadline = task.getDeadline();
                    if (Objects.isNull(deadline)) {
                        return false;
                    }
                    if (deadline.isBefore(now)) {
                        //表示已经过期
                        task.setStatus(DisinfectionTaskStatusEnum.EXPIRED);
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
        taskService.updateBatchById(expireTasks);
        return expireTasks;
    }
}
