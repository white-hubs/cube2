package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.PublishedExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务发布校验器
 *
 * @author lanchun
 * @date 2023/4/19 19:29
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishedExamTaskValidator {

    private final ExamTaskService examTaskService;

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    private final ExamPaperQueryService examPaperQueryService;

    public void verify(final PublishedExamTaskCommand publishedExamTaskValidator) {
        ExamTaskPO examTaskPO = this.examTaskService.getById(publishedExamTaskValidator.getId());
        Assert.isOutOfRange(examTaskPO.getExamEndTime().isBefore(LocalDateTime.now()), "发布失败,考试结束已超过当前时间");
        if (ExamTaskTypeEnum.STAFF_EXAM == examTaskPO.getExamTaskType()) {
            long count = this.examTaskUserService.count(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                .eq(ExamTaskUserPO::getExamTaskId, examTaskPO.getId()));
            Assert.isNotAcceptable(count < 1, "发布失败,未指定考试用户");
        }
        Assert.isConflict(ExamTaskStatusEnum.STAY_EXAM == examTaskPO.getExamTaskStatus(), "任务已发布");
        Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == examTaskPO.getExamTaskStatus(), "考试已结束");
        List<ExamPaperVO> examTaskPaperVOS = this.examTaskPaperQueryService.getExamTaskPaperVOS(publishedExamTaskValidator.getId());
        Set<Long> examPaperIds = examTaskPaperVOS.stream().map(ExamPaperVO::getId).collect(Collectors.toSet());
        List<ExamPaperVO> examPapersIfExist = this.examPaperQueryService.getExamPapersIfExist(examPaperIds);
        Set<Float> scoreSize = examPapersIfExist.stream()
                .map(ExamPaperVO::getScore)
                .collect(Collectors.toSet());
        Assert.isNotAcceptable(scoreSize.size() > 1, "试卷总分不一致");
    }
}
