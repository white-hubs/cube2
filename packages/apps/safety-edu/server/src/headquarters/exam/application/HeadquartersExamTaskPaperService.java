package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper.HeadquartersExamTaskPaperMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskPaperService extends ServiceImpl<HeadquartersExamTaskPaperMapper, HeadquartersExamTaskPaperPO> {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    private final ExamPaperQueryService examPaperQueryService;

    public ExamPaperVO getExamTaskPaperVO(Long id, Long examPaperId) {
        HeadquartersExamTaskPO examTask = this.headquartersExamTaskService.getById(id);
        Assert.isNotFound(examTask, "任务不存在,无法获取试卷信息");
        return getExamPaperVO(id, examPaperId, examTask);
    }

    public ExamPaperVO getExamPaperVO(Long id, Long examPaperId, HeadquartersExamTaskPO examTask) {
        if (ExamTaskStatusEnum.UNPUBLISHED == examTask.getExamTaskStatus()) {
            ExamPaperVO examPaperVO = this.examPaperQueryService.getExamPaper(examPaperId);
            examPaperVO.setTimed(examTask.getTimed());
            examPaperVO.setRetake(examTask.getRetake());
            return examPaperVO;
        } else {
            //获取任务上的试卷信息
            HeadquartersExamTaskPaperPO paperServiceOne = this.baseMapper.selectOne(Wrappers.<HeadquartersExamTaskPaperPO>lambdaQuery()
                                                                                            .eq(HeadquartersExamTaskPaperPO::getHeadquartersExamTaskId, id)
                                                                                            .eq(HeadquartersExamTaskPaperPO::getExamPaperId, examPaperId));
            ExamPaperVO examPaperVO = JsonUtils.deserialize(paperServiceOne.getExamPaperContent(), ExamPaperVO.class);
            List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>();
            examPaperVO.getQuestionCategories().stream().forEach(examPaperQuestionCategoryVO ->
                                                                         examPaperQuestionVOS.addAll(examPaperQuestionCategoryVO.getExamQuestions())
                                                                );
            examPaperVO.setExamQuestions(examPaperQuestionVOS);
            examPaperVO.setTimed(examTask.getTimed());
            examPaperVO.setRetake(examTask.getRetake());
            return examPaperVO;
        }
    }

    public List<ExamPaperVO> getExamTaskPaperVOS(Long id) {
        HeadquartersExamTaskPO examTask = this.headquartersExamTaskService.getById(id);
        Assert.isNotFound(examTask, "任务不存在,无法获取试卷信息");
        return getExamPaperVOs(examTask);
    }

    public List<ExamPaperVO> getExamPaperVOs(HeadquartersExamTaskPO examTask) {
        List<HeadquartersExamTaskPaperPO> examTaskPaperPOs = this.baseMapper.selectList(Wrappers.<HeadquartersExamTaskPaperPO>lambdaQuery()
                                                                                                .eq(HeadquartersExamTaskPaperPO::getHeadquartersExamTaskId, examTask.getId()));
        if (ExamTaskStatusEnum.UNPUBLISHED == examTask.getExamTaskStatus()) {
            Set<Long> examPaperIds = examTaskPaperPOs.stream()
                    .map(HeadquartersExamTaskPaperPO::getExamPaperId).collect(Collectors.toSet());
            List<ExamPaperVO> examPaperVOS = this.examPaperQueryService.getExamPapersIfExist(examPaperIds);
            examPaperVOS.forEach(examPaperVO -> {
                examPaperVO.setTimed(examTask.getTimed());
                examPaperVO.setRetake(examTask.getRetake());
            });
            return examPaperVOS;
        } else {
            return examTaskPaperPOs.stream().map(examTaskPaperPO -> getExamPaperVO(examTask, examTaskPaperPO)
                                                ).collect(Collectors.toList());
        }
    }

    public List<HeadquartersExamTaskPaperPO> getPublishedExamPaperVOS(Long id) {
        return this.baseMapper.selectList(Wrappers.<HeadquartersExamTaskPaperPO>lambdaQuery()
                                                  .eq(HeadquartersExamTaskPaperPO::getHeadquartersExamTaskId, id)
                                         );
    }

    private ExamPaperVO getExamPaperVO(HeadquartersExamTaskPO examTask, HeadquartersExamTaskPaperPO examTaskPaperPO) {
        ExamPaperVO examPaperVO = JsonUtils.deserialize(examTaskPaperPO.getExamPaperContent(), ExamPaperVO.class);
        List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>();
        examPaperVO.getQuestionCategories().forEach(examPaperQuestionCategoryVO ->
                                                            examPaperQuestionVOS.addAll(examPaperQuestionCategoryVO.getExamQuestions())
                                                   );
        examPaperVO.setExamQuestions(examPaperQuestionVOS);
        examPaperVO.setTimed(examTask.getTimed());
        examPaperVO.setRetake(examTask.getRetake());
        return examPaperVO;
    }
}
