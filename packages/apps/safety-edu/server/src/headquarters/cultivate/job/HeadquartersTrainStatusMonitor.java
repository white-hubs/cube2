package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersTrainStatusMonitor {
    private final HeadquartersCultivateTaskService headquartersCultivateTaskService;

    @JobAnnotation(name = "safetyEduHeadUpdateCultivateTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "深农总部-安全教育-定时更新培训任务状态"
    )
    public void trainStatusMonitor(String param) {
        SessionContextHolder.clearSessionContext();
        //查询 已发布、培训中
        var list = headquartersCultivateTaskService.list(Wrappers.<HeadquartersCultivateTask>lambdaQuery()
                                                                 .in(HeadquartersCultivateTask::getCultivateTaskStatus, Arrays.asList(TrainStatusEnum.PUBLISHED, TrainStatusEnum.TRAINING)));
        if (CollectionUtils.isNotEmpty(list)) {
            var currentMilli = DateUtils.currentTimestamp();
            list.stream().forEach(t -> {
                try {
                    if (currentMilli >= DateUtils.toTimestamp(t.getCultivateStartTime()) &&
                        currentMilli < DateUtils.toTimestamp(t.getCultivateEndTime())) {
                        t.setCultivateTaskStatus(TrainStatusEnum.TRAINING.name());
                    } else if (currentMilli >= DateUtils.toTimestamp(t.getCultivateEndTime())) {
                        t.setCultivateTaskStatus(TrainStatusEnum.OVER.name());
                    }
                    SessionContextHolder.requestTenant(t.getTenant());
                    headquartersCultivateTaskService.updateById(t);
                } catch (Exception e) {
                    log.error("总部安全培训状态更新异常：", e);
                }
            });
        }
        SessionContextHolder.clearSessionContext();
    }
}
