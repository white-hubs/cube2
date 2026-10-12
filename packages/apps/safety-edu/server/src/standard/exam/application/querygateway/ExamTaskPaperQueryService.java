package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamRandomExamPaperBuildService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserRecordService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserRecordPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskPaperQueryService {

    private final ExamTaskService examTaskService;

    private final ExamPaperQueryService examPaperQueryService;

    private final ExamTaskPaperService examTaskPaperService;

    private final ExamTaskUserRecordService examTaskUserRecordService;

    private final ExamRandomExamPaperBuildService examRandomExamPaperBuildService;

    public ExamPaperVO getExamTaskPaperVO(Long id, Long examPaperId) {
        ExamTaskPO examTask = this.examTaskService.getById(id);
        Assert.isNotFound(examTask, "任务不存在,无法获取试卷信息");
        return getExamPaperVO(examPaperId, examTask);
    }

    public List<ExamPaperVO> getExamTaskPaperVOS(Long id) {
        ExamTaskPO examTask = this.examTaskService.getById(id);
        Assert.isNotFound(examTask, "任务不存在,无法获取试卷信息");
        return getExamPaperVOs(examTask);
    }

    public List<ExamTaskPaperPO> getPublishedExamPapers(Long id) {
        return this.getPublishedExamPapers(Collections.singleton(id));
    }

    public List<ExamTaskPaperPO> getPublishedExamPapers(Set<Long> ids) {
        return this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                      .in(ExamTaskPaperPO::getExamTaskId, ids)
                                                      .isNull(ExamTaskPaperPO::getUserIdentify));
    }

    public List<ExamTaskPaperPO> getExamTaskPapersBaseInfo(Set<Long> ids) {
        return this.examTaskPaperService.getExamTaskPaperBaseInfoVOS(ids);
    }

    public List<ExamPaperVO> getExamPaperBaseInfoByIds(Set<Long> paperIds) {
        return this.examPaperQueryService.getExamPapersBaseInfoIfExist(paperIds);
    }

    public ExamPaperVO userGetExamTaskPaper(Long id, String userIdentity) {
        ExamTaskPO examTask = this.examTaskService.getById(id);
        Assert.isNotFound(examTask, "任务不存在,无法获取试卷信息");
        if (ExamTaskPaperModeEnum.RANDOM == examTask.getExamPaperMode()) {
            return this.userGetRandomExamTaskPaper(examTask, userIdentity);
        }
        //已存在考试记录的情况直接返回上次考试的试卷
        Optional<ExamTaskUserRecordPO> optionalExamTaskUserRecordPO = examTaskUserRecordService
                .getUserTaskLastRecord(id, userIdentity);
        if (optionalExamTaskUserRecordPO.isPresent()) {
            ExamTaskUserRecordPO examTaskUserRecord = optionalExamTaskUserRecordPO.get();
            ExamTaskPaperPO examTaskPaper = this.examTaskPaperService.getOne(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                     .eq(ExamTaskPaperPO::getExamTaskId, examTaskUserRecord.getExamTaskId())
                                                                                     .eq(ExamTaskPaperPO::getExamPaperId, examTaskUserRecord.getExamPaperId())
                                                                            );
            return getExamPaperVO(examTask, examTaskPaper);
        }
        //随机获取
        return randomGetExamTaskPaper(examTask);
    }

    public ExamPaperVO randomGetExamTaskPaper(ExamTaskPO examTask) {
        List<ExamTaskPaperPO> taskPaperPOS = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                    .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                                                                                    .isNull(ExamTaskPaperPO::getUserIdentify)
                                                                           );
        if (taskPaperPOS.size() <= 1) {
            return getExamPaperVO(examTask, taskPaperPOS.get(0));
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(taskPaperPOS.size());
        ExamTaskPaperPO examTaskPaperPO = taskPaperPOS.get(randomIndex);
        return getExamPaperVO(examTask, examTaskPaperPO);
    }

    public ExamPaperVO getExamPaperVO(Long examPaperId, ExamTaskPO examTask) {
        if (ExamTaskStatusEnum.UNPUBLISHED == examTask.getExamTaskStatus()) {
            ExamPaperVO examPaperVO = this.examPaperQueryService.getExamPaper(examPaperId);
            var query = Wrappers.<ExamTaskPaperPO>lambdaQuery()
                    .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                    .eq(ExamTaskPaperPO::getExamPaperId, examPaperId);
            if (ExamTaskPaperModeEnum.UNIFIED == examTask.getExamPaperMode()) {
                query.isNull(ExamTaskPaperPO::getUserIdentify);
            }
            ExamTaskPaperPO taskPaper = this.examTaskPaperService.getOne(query);
            if (taskPaper != null) {
                examPaperVO.setRuleId(taskPaper.getRuleId());
            }
            examPaperVO.setTimed(examTask.getTimed());
            examPaperVO.setRetake(examTask.getRetake());
            return examPaperVO;
        } else {
            //获取任务上的试卷信息
            var query = Wrappers.<ExamTaskPaperPO>lambdaQuery()
                    .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                    .eq(ExamTaskPaperPO::getExamPaperId, examPaperId);
            if (ExamTaskPaperModeEnum.UNIFIED == examTask.getExamPaperMode()) {
                query.isNull(ExamTaskPaperPO::getUserIdentify);
            }
            ExamTaskPaperPO paperServiceOne = this.examTaskPaperService.getOne(query);
            return getExamPaperVO(examTask, paperServiceOne);
        }
    }

    public List<ExamPaperVO> getExamPaperVOs(ExamTaskPO examTask) {
        List<ExamTaskPaperPO> examTaskPaperPOs = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                        .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                                                                                        .isNull(ExamTaskPaperPO::getUserIdentify));
        if (ExamTaskPaperModeEnum.RANDOM == examTask.getExamPaperMode()) {
            return examTaskPaperPOs.stream()
                    .filter(taskPaperPO -> taskPaperPO.getRuleId() != null)
                    .map(taskPaperPO -> {
                        ExamPaperVO examPaperVO = new ExamPaperVO();
                        examPaperVO.setRuleId(taskPaperPO.getRuleId());
                        examPaperVO.setScore(taskPaperPO.getExamPaperScore());
                        examPaperVO.setPassingGradeScore(taskPaperPO.getExamPaperPassingGradeScore());
                        examPaperVO.setExamPaperQuestionNumber(taskPaperPO.getExamPaperQuestionNumber());
                        examPaperVO.setTimed(examTask.getTimed());
                        examPaperVO.setRetake(examTask.getRetake());
                        return examPaperVO;
                    }).collect(Collectors.toList());
        }
        if (ExamTaskStatusEnum.UNPUBLISHED == examTask.getExamTaskStatus()) {
            Set<Long> examPaperIds = examTaskPaperPOs.stream()
                    .map(ExamTaskPaperPO::getExamPaperId).collect(Collectors.toSet());
            List<ExamPaperVO> examPaperVOS = this.examPaperQueryService.getExamPapersIfExist(examPaperIds);
            Map<Long, Long> ruleIdByPaperId = new HashMap<>(examTaskPaperPOs.size());
            for (ExamTaskPaperPO taskPaperPO : examTaskPaperPOs) {
                ruleIdByPaperId.put(taskPaperPO.getExamPaperId(), taskPaperPO.getRuleId());
            }
            examPaperVOS.forEach(examPaperVO -> {
                examPaperVO.setRuleId(ruleIdByPaperId.get(examPaperVO.getId()));
                examPaperVO.setTimed(examTask.getTimed());
                examPaperVO.setRetake(examTask.getRetake());
            });
            return examPaperVOS;
        } else {
            return examTaskPaperPOs.stream().map(examTaskPaperPO ->
                                                         getExamPaperVO(examTask, examTaskPaperPO)
                                                ).collect(Collectors.toList());
        }
    }

    private ExamPaperVO getExamPaperVO(ExamTaskPO examTask, ExamTaskPaperPO examTaskPaperPO) {
        Assert.isNotFound(examTaskPaperPO, "任务试卷不存在");
        ExamPaperVO examPaperVO = JsonUtils.deserialize(examTaskPaperPO.getExamPaperContent(), ExamPaperVO.class);
        List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>();
        examPaperVO.getQuestionCategories().forEach(examPaperQuestionCategoryVO ->
                                                            examPaperQuestionVOS.addAll(examPaperQuestionCategoryVO.getExamQuestions())
                                                   );
        examPaperVO.setExamQuestions(examPaperQuestionVOS);
        examPaperVO.setTimed(examTask.getTimed());
        examPaperVO.setRetake(examTask.getRetake());
        examPaperVO.setRuleId(examTaskPaperPO.getRuleId());
        return examPaperVO;
    }

    private ExamPaperVO userGetRandomExamTaskPaper(ExamTaskPO examTask, String userIdentity) {
        Assert.isNotAcceptable(userIdentity == null || userIdentity.isBlank(), "用户标识不能为空");
        List<ExamTaskPaperPO> userPapers = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                  .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                                                                                  .eq(ExamTaskPaperPO::getUserIdentify, userIdentity)
                                                                                  .isNotNull(ExamTaskPaperPO::getExamPaperId));
        Optional<ExamTaskUserRecordPO> optionalExamTaskUserRecordPO = this.examTaskUserRecordService
                .getUserTaskLastRecord(examTask.getId(), userIdentity);
        if (CollectionUtils.isNotEmpty(userPapers) && optionalExamTaskUserRecordPO.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(userPapers.size());
            return this.getExamPaperVO(examTask, userPapers.get(randomIndex));
        }
        this.examRandomExamPaperBuildService.removeRandomPapersByExamTaskIdAndUserIdentify(examTask.getId(), userIdentity);
        List<ExamTaskPaperPO> templates = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                 .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId())
                                                                                 .isNull(ExamTaskPaperPO::getUserIdentify)
                                                                                 .isNotNull(ExamTaskPaperPO::getRuleId));
        Set<Long> ruleIds = templates.stream()
                .map(ExamTaskPaperPO::getRuleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Assert.isNotAcceptable(CollectionUtils.isEmpty(ruleIds), "任务未配置随机考题规则");
        List<ExamPaperVO> generated = this.examRandomExamPaperBuildService.generateAndPersistPapersForUser(examTask.getId(), ruleIds);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(generated), "未生成随机试卷");
        List<ExamTaskPaperPO> saveRows = generated.stream().map(examPaperVO -> ExamTaskPaperPO.builder()
                .examTaskId(examTask.getId())
                .userIdentify(userIdentity)
                .examPaperId(examPaperVO.getId())
                .ruleId(examPaperVO.getRuleId())
                .examPaperName(examPaperVO.getName())
                .examPaperContent(JsonUtils.serialize(examPaperVO))
                .examPaperScore(examPaperVO.getScore())
                .examPaperPassingGradeScore(examPaperVO.getPassingGradeScore())
                .examPaperQuestionNumber(examPaperVO.getExamPaperQuestionNumber())
                .examPaperRemark(examPaperVO.getRemark())
                .build()).collect(Collectors.toList());
        this.examTaskPaperService.saveBatch(saveRows);
        int randomIndex = ThreadLocalRandom.current().nextInt(generated.size());
        ExamPaperVO result = generated.get(randomIndex);
        result.setTimed(examTask.getTimed());
        result.setRetake(examTask.getRetake());
        return result;
    }
}
