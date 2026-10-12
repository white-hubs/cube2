package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskJob {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    /**
     * 任务开始
     */
    @JobAnnotation(name = "safetyEduHeadUpdateStayExamTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "深农总部-安全教育-定时更新待考试任务状态"
    )
    public void updateHeadquartersExamTaskStart(String param) {
        SessionContextHolder.clearSessionContext();
        try {
            var examTaskPOS = this.headquartersExamTaskService.list(Wrappers.<HeadquartersExamTaskPO>lambdaQuery()
                                                                            .le(HeadquartersExamTaskPO::getExamStartTime, LocalDateTime.now())
                                                                            .eq(HeadquartersExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.STAY_EXAM));
            if (CollectionUtils.isEmpty(examTaskPOS)) {
                return;
            }
            examTaskPOS.forEach(examTaskPO -> {
                SessionContextHolder.requestTenant(examTaskPO.getTenant());
                examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.IN_PROGRESS);
                this.headquartersExamTaskService.updateById(examTaskPO);
            });
        } catch (Exception e) {
            log.error("总部任务开始job执行失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    /**
     * 任务结束
     */
    @JobAnnotation(name = "safetyEduHeadUpdateUnProgressExamTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "深农总部-安全教育-定时更新进行中考试任务状态"
    )
    public void updateHeadquartersExamTaskEnd(String param) {
        SessionContextHolder.clearSessionContext();
        try {
            var examTaskPOS = this.headquartersExamTaskService.list(Wrappers.<HeadquartersExamTaskPO>lambdaQuery()
                                                                            .le(HeadquartersExamTaskPO::getExamEndTime, LocalDateTime.now())
                                                                            .eq(HeadquartersExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.IN_PROGRESS));
            if (CollectionUtils.isEmpty(examTaskPOS)) {
                return;
            }
            examTaskPOS.forEach(examTaskPO -> {
                SessionContextHolder.requestTenant(examTaskPO.getTenant());
                examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.ENDED);
                this.headquartersExamTaskService.updateById(examTaskPO);
            });
        } catch (Exception e) {
            log.error("总部任务结束job执行失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
