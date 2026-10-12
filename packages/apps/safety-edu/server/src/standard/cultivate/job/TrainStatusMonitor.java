package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.common.HeadquartersConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.CultivateTaskUserMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.UserCultivateOverEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskService;
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
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrainStatusMonitor {
    private final CultivateTaskService cultivateTaskService;
    private final CultivateTaskUserMapper cultivateTaskUserMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @JobAnnotation(name = "safetyEduUpdateCultivateTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "安全教育-定时更新培训任务状态"
    )
    public void trainStatusMonitor(String param) {
        SessionContextHolder.clearSessionContext();
        //查询 已发布、培训中
        var list = cultivateTaskService.list(Wrappers.<CultivateTask>lambdaQuery()
                                                     .in(CultivateTask::getCultivateTaskStatus, Arrays.asList(TrainStatusEnum.PUBLISHED, TrainStatusEnum.TRAINING)));
        if (CollectionUtils.isNotEmpty(list)) {
            var currentMilli = DateUtils.currentTimestamp();
            list.forEach(t -> {
                try {
                    if (currentMilli >= DateUtils.toTimestamp(t.getCultivateStartTime()) &&
                        currentMilli < DateUtils.toTimestamp(t.getCultivateEndTime())) {
                        t.setCultivateTaskStatus(TrainStatusEnum.TRAINING.name());
                    } else if (currentMilli >= DateUtils.toTimestamp(t.getCultivateEndTime())) {
                        t.setCultivateTaskStatus(TrainStatusEnum.OVER.name());
                    }
                    SessionContextHolder.requestTenant(t.getTenant());
                    cultivateTaskService.updateById(t);
                    //如果培训结束，更新所有培训人员的代办状态
                    if (t.getCultivateTaskStatus().equals(TrainStatusEnum.OVER.name())) {
                        var taskUserList = cultivateTaskUserMapper.selectList(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                      .eq(CultivateTaskUser::getCultivateTaskId, t.getId()));
                        applicationEventPublisher.publishEvent(new UserCultivateOverEvent(this, t, taskUserList));
                    }
                } catch (Exception e) {
                    log.error("安全培训状态更新异常：", e);
                }
            });
        }
        SessionContextHolder.clearSessionContext();
    }

    @JobAnnotation(name = "safetyEduUpdateHeadCultivateTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "安全教育-定时更新已结束的总部培训任务状态"
    )
    public void trainOtherSourceStatusMonitor(String param) {
        SessionContextHolder.clearSessionContext();
        //查询 已发布、培训中
        var list = cultivateTaskService.list(Wrappers.<CultivateTask>lambdaQuery()
                                                     .le(CultivateTask::getCultivateEndTime, LocalDateTime.now())
                                                     .eq(CultivateTask::getSourceCode, HeadquartersConstant.TASK_SOURCE_CODE)
                                                     .ne(CultivateTask::getCultivateTaskStatus, TrainStatusEnum.OVER));
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(t -> {
                try {
                    SessionContextHolder.requestTenant(t.getTenant());
                    t.setCultivateTaskStatus(TrainStatusEnum.OVER.name());
                    cultivateTaskService.updateById(t);
                    //培训结束，更新所有培训人员的代办状态
                    var taskUserList = cultivateTaskUserMapper.selectList(Wrappers.<CultivateTaskUser>lambdaQuery()
                                                                                  .eq(CultivateTaskUser::getCultivateTaskId, t.getId()));
                    applicationEventPublisher.publishEvent(new UserCultivateOverEvent(this, t, taskUserList));
                } catch (Exception e) {
                    log.error("安全培训状态更新异常：", e);
                }
            });
        }
        SessionContextHolder.clearSessionContext();
    }
}
