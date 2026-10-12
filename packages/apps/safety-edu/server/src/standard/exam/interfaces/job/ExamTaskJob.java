package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.common.HeadquartersConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskEndEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskJob {

    private final ExamTaskService examTaskService;

    private final ExamTaskUserService examTaskUserService;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 任务开始
     */
    @JobAnnotation(name = "safetyEduUpdateStayExamTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "安全教育-定时更新待考试任务状态"
    )
    public void updateExamTaskStart(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var examTaskPOS = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                .eq(ExamTaskPO::getDeleted, 0)
                                                                .le(ExamTaskPO::getExamStartTime, LocalDateTime.now())
                                                                .eq(ExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.STAY_EXAM));
            if (CollectionUtils.isEmpty(examTaskPOS)) {
                return;
            }
            examTaskPOS.forEach(examTaskPO -> {
                SessionContextHolder.requestTenant(examTaskPO.getTenant());
                examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.IN_PROGRESS);
                this.examTaskService.updateById(examTaskPO);
            });
        } catch (Exception e) {
            log.error("考试任务开始job执行失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    /**
     * 任务结束
     */
    @JobAnnotation(name = "safetyEduUpdateUnProgressExamTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "安全教育-定时更新进行中考试任务状态"
    )
    public void updateExamTaskEnd(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var examTaskPOS = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                .eq(ExamTaskPO::getDeleted, 0)
                                                                .le(ExamTaskPO::getExamEndTime, LocalDateTime.now())
                                                                .eq(ExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.IN_PROGRESS));
            updateExamTaskInfo(examTaskPOS);
        } catch (Exception e) {
            log.error("考试任务结束job执行失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }

    }

    public boolean updateExamTaskInfo(List<ExamTaskPO> examTaskPOS) {
        if (CollectionUtils.isEmpty(examTaskPOS)) {
            return true;
        }
        examTaskPOS.forEach(examTaskPO -> {
            SessionContextHolder.requestTenant(examTaskPO.getTenant());
            examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.ENDED);
            updateExamTaskUserInfo(examTaskPO);
            this.examTaskService.updateById(examTaskPO);
        });
        return false;
    }

    public void updateExamTaskUserInfo(ExamTaskPO examTaskPO) {
        List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                     .eq(ExamTaskUserPO::getExamTaskId, examTaskPO.getId())
                                                                                     .eq(ExamTaskUserPO::getExamStatus, UserExamStatusEnum.STAY_EXAM));
        if (!CollectionUtils.isEmpty(examTaskUserPOS)) {
            this.applicationEventPublisher.publishEvent(new ExamTaskEndEvent(this, examTaskPO.getId(), examTaskUserPOS));
        }
        LambdaUpdateWrapper<ExamTaskUserPO> updateWrapper =
                Wrappers.<ExamTaskUserPO>lambdaUpdate()
                        .eq(ExamTaskUserPO::getExamTaskId, examTaskPO.getId())
                        .eq(ExamTaskUserPO::getExamStatus, UserExamStatusEnum.STAY_EXAM)
                        .set(ExamTaskUserPO::getExamStatus, UserExamStatusEnum.MISS_EXAM);
        this.examTaskUserService.update(updateWrapper);
    }

    /**
     * 外部任务结束
     */
    @JobAnnotation(name = "safetyEduUpdateHeadExamTaskStatus", cron = "0 0/1 * * * ?",
                   desc = "安全教育-定时更改总部考试任务状态"
    )
    public void updateOtherSourceExamTaskEnd(String param) {
        try {
            SessionContextHolder.clearSessionContext();
            var examTaskPOS = this.examTaskService.list(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                .eq(ExamTaskPO::getDeleted, 0)
                                                                .le(ExamTaskPO::getExamEndTime, LocalDateTime.now())
                                                                .eq(ExamTaskPO::getSourceCode, HeadquartersConstant.TASK_SOURCE_CODE)
                                                                .ne(ExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.ENDED));
            updateExamTaskInfo(examTaskPOS);
        } catch (Exception e) {
            log.error("考试任务结束job执行失败,失败原因:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
