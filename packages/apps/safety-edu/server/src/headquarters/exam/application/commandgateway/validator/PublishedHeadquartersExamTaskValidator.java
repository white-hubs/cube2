package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.PublishedHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
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
 * @date 2024/4/9 18:10
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishedHeadquartersExamTaskValidator {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    private final HeadquartersExamTaskPaperService headquartersExamTaskPaperService;

    private final ExamPaperQueryService examPaperQueryService;

    public void verify(final PublishedHeadquartersExamTaskCommand publishedExamTaskValidator) {
        HeadquartersExamTaskPO headquartersExamTaskPO = this.headquartersExamTaskService.getById(publishedExamTaskValidator.getId());
        Assert.isNotAcceptable(headquartersExamTaskPO.getExamEndTime().isBefore(LocalDateTime.now()), "发布失败,考试结束已超过当前时间");
        Assert.isConflict(ExamTaskStatusEnum.STAY_EXAM == headquartersExamTaskPO.getExamTaskStatus(), "任务已发布");
        Assert.isConflict(ExamTaskStatusEnum.IN_PROGRESS == headquartersExamTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == headquartersExamTaskPO.getExamTaskStatus(), "考试已结束");
        List<HeadquartersExamTaskPaperPO> examTaskPaperVOS = this.headquartersExamTaskPaperService.getPublishedExamPaperVOS(publishedExamTaskValidator.getId());
        Set<Long> examPaperIds = examTaskPaperVOS.stream().map(HeadquartersExamTaskPaperPO::getExamPaperId).collect(Collectors.toSet());
        List<ExamPaperVO> examPapersIfExist = this.examPaperQueryService.getExamPapersIfExist(examPaperIds);
        Set<Float> scoreSize = examPapersIfExist.stream()
                .map(ExamPaperVO::getScore)
                .collect(Collectors.toSet());
        Assert.isConflict(scoreSize.size() > 1, "试卷总分不一致");
    }
}
