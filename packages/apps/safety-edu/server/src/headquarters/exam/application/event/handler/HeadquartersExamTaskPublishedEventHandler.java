package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.common.HeadquartersConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.event.HeadquartersExamTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 总部考试任务发布事件监听
 *
 * @author lanchun
 * @date 2024/4/9 19:08
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskPublishedEventHandler {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    private final ThemeService themeService;

    private final ExamTaskRepository examTaskRepository;

    private final ExamTaskPaperService examTaskPaperService;

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    @Order(1)
    public void createExamTaskPaper(final HeadquartersExamTaskPublishedEvent examTaskPublishedEvent) {
        HeadquartersExamTask examTask = examTaskPublishedEvent.getExamTask();
        //更改任务状态
        HeadquartersExamTaskPO examTaskPO = headquartersExamTaskService.getById(examTask.getId());
        if (!examTask.getExamTaskDateInfo().getStartTime().isAfter(LocalDateTime.now())) {
            examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.IN_PROGRESS);
        }
        //主题信息
        ThemePO themePO = this.themeService.getById(examTask.getThemeInfo().getId());
        examTaskPO.setThemeName(themePO.getName());
        this.headquartersExamTaskService.updateById(examTaskPO);
    }

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    @Order(2)
    public void createExamTask(final HeadquartersExamTaskPublishedEvent event) {
        HeadquartersExamTask headquartersExamTask = event.getExamTask();
        event.getExamTask().getExamTaskMarkets().forEach(headquartersExamTaskMarket -> {
            ExamTask examTask = new ExamTask(IdWorker.getId(),
                                             headquartersExamTask.getExamTaskInfo(),
                                             headquartersExamTask.getThemeInfo(),
                                             headquartersExamTask.getExamTaskChargeUserInfo(),
                                             headquartersExamTask.getExamTaskType(),
                                             new ExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED, ExamTaskStatusEnum.UNPUBLISHED.name()));
            examTask.settingExamTaskTimedInfo(headquartersExamTask.getExamTaskTimedInfo().getTimed(), headquartersExamTask
                    .getExamTaskTimedInfo().getExamDuration(), headquartersExamTask.getExamTaskTimedInfo().getExamDurationUnit());
            examTask.settingExamTaskDateInfo(headquartersExamTask.getExamTaskDateInfo().getStartTime(), headquartersExamTask
                    .getExamTaskDateInfo().getEndTime());
            examTask.settingExamTaskSource(HeadquartersConstant.TASK_SOURCE_CODE, HeadquartersConstant.TASK_SOURCE_NAME, event.getExamTask()
                    .getId().toString());
            examTask.settingExamTaskUserNum(headquartersExamTask.getExamUserNumber());
            examTask.settingExamTaskRetakeInfo(headquartersExamTask.getExamTaskRetakeInfo().getRetake(), headquartersExamTask
                    .getExamTaskRetakeInfo().getRetakeNumber());
            examTask.setMarketTenant(headquartersExamTaskMarket.getMarketTenantCode());
            List<ExamTaskPaperPO> examPaperPOS = headquartersExamTask.getExamPapers().stream()
                    .map(examTaskPaper ->
                                 //任务试卷信息存储
                                 ExamTaskPaperPO.builder()
                                         .examTaskId(examTask.getId())
                                         .examPaperId(examTaskPaper.getId())
                                         .examPaperName(examTaskPaper.getName())
                                         .examPaperContent(examTaskPaper.getExamPaperContent())
                                         .examPaperScore(examTaskPaper.getExamPaperScore())
                                         .examPaperPassingGradeScore(examTaskPaper.getGradeScore())
                                         .examPaperQuestionNumber(examTaskPaper.getRequestNumber())
                                         .examPaperRemark(examTaskPaper.getExamPaperRemark())
                                         .build()
                        ).collect(Collectors.toList());
            this.examTaskRepository.save(examTask);
            this.examTaskPaperService.saveBatch(examPaperPOS);
        });
    }
}
