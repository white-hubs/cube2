package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExamTaskFactory {

    private ExamTaskFactory() {

    }

    public static ExamTask newExamTask(final IncreaseExamTaskCommand increaseExamTaskCommand) {
        //基础信息
        ExamTaskInfo examTaskInfo = new ExamTaskInfo(increaseExamTaskCommand.getName(), increaseExamTaskCommand.getRemark());
        //主题信息
        ThemeInfo themeInfo = getThemeInfo(increaseExamTaskCommand);
        //负责人
        ExamTaskChargeUserInfo examTaskChargeUserInfo = getExamTaskChargeUserInfo(increaseExamTaskCommand);
        //考试类型
        ExamTaskType examTaskType = getExamTaskType(increaseExamTaskCommand);
        ExamTask examTask = new ExamTask(IdWorker.getId(), examTaskInfo, themeInfo, examTaskChargeUserInfo, examTaskType, getExamTaskStatus());
        examTask.settingExamPaperMode(increaseExamTaskCommand.getExamPaperMode());
        //考卷
        examTask.settingExamPaper(getExamTaskPapers(increaseExamTaskCommand));
        //考试时间信息
        examTask.settingExamTaskDateInfo(increaseExamTaskCommand.getExamStartTime(), increaseExamTaskCommand.getExamEndTime());
        //复考信息
        examTask.settingExamTaskRetakeInfo(increaseExamTaskCommand.getRetake(), increaseExamTaskCommand.getRetakeNumber());
        //限时信息
        examTask.settingExamTaskTimedInfo(increaseExamTaskCommand.getTimed(), increaseExamTaskCommand.getExamDuration(), increaseExamTaskCommand.getExamDurationUnit());
        //考试人数
        examTask.settingExamTaskUserNum(increaseExamTaskCommand.getExamUserNumber());
        //考试人员
        examTask.settingExamTaskUserInfos(examTaskUserInfos(increaseExamTaskCommand));
        //任务来源设置
        examTask.settingExamTaskSource("LOCAL_CREATE", "自建", null);
        return examTask;
    }

    private static ExamTaskType getExamTaskType(IncreaseExamTaskCommand increaseExamTaskCommand) {
        return new ExamTaskType(increaseExamTaskCommand.getExamTaskType(), increaseExamTaskCommand.getExamTaskType()
                .getDescription());
    }

    private static List<ExamTaskUserInfo> examTaskUserInfos(IncreaseExamTaskCommand increaseExamTaskCommand) {
        if (CollectionUtils.isEmpty(increaseExamTaskCommand.getExamUsers())) {
            return Collections.emptyList();
        }
        //考试人员
        return increaseExamTaskCommand.getExamUsers().stream()
                .map(increaseExamTaskUserCommand ->
                             new ExamTaskUserInfo(
                                     increaseExamTaskUserCommand.getId(),
                                     increaseExamTaskUserCommand.getId().toString(),
                                     increaseExamTaskUserCommand.getName(),
                                     increaseExamTaskUserCommand.getPhone(),
                                     increaseExamTaskUserCommand.getDepartmentId(),
                                     increaseExamTaskUserCommand.getDepartmentName(),
                                     increaseExamTaskUserCommand.getPostId(),
                                     increaseExamTaskUserCommand.getPostName()
                             )
                    ).collect(Collectors.toList());
    }

    private static ExamTaskStatus getExamTaskStatus() {
        //任务状态
        return new ExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED, ExamTaskStatusEnum.UNPUBLISHED.getDescription());
    }

    private static ExamTaskChargeUserInfo getExamTaskChargeUserInfo(IncreaseExamTaskCommand increaseExamTaskCommand) {
        //负责人信息
        return new ExamTaskChargeUserInfo(increaseExamTaskCommand.getChargeUserId(), null);
    }

    private static List<ExamTaskPaper> getExamTaskPapers(IncreaseExamTaskCommand increaseExamTaskCommand) {
        if (ExamTaskPaperModeEnum.UNIFIED == increaseExamTaskCommand.getExamPaperMode()) {
            ExamPaperQueryService examPaperQueryService = ApplicationContextHolder.getBean(ExamPaperQueryService.class);
            List<ExamPaperVO> examPapersIfExist = examPaperQueryService.getExamPapersIfExist(increaseExamTaskCommand.getExamPaperIds());
            return ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examPapersIfExist);
        }
        if (CollectionUtils.isEmpty(increaseExamTaskCommand.getExamPaperIds())) {
            return Collections.emptyList();
        }
        ExamQuestionRuleQueryService examQuestionRuleQueryService = ApplicationContextHolder.getBean(ExamQuestionRuleQueryService.class);
        List<ExamQuestionRuleVO> rules = examQuestionRuleQueryService.listExamQuestionRulesByIds(increaseExamTaskCommand.getExamPaperIds());
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        var ruleMap = rules.stream().collect(Collectors.toMap(ExamQuestionRuleVO::getId, r -> r));
        return increaseExamTaskCommand.getExamPaperIds().stream()
                .filter(Objects::nonNull)
                .sorted()
                .map(ruleId -> {
                    ExamTaskPaper taskPaper = new ExamTaskPaper();
                    taskPaper.setRuleId(ruleId);
                    ExamQuestionRuleVO rule = ruleMap.get(ruleId);
                    if (rule != null) {
                        taskPaper.setExamPaperScore(rule.getTotalScore() == null ? null : rule.getTotalScore().floatValue());
                        taskPaper.setGradeScore(rule.getPassScore() == null ? null : rule.getPassScore().floatValue());
                        taskPaper.setRequestNumber(rule.getQuestionNum());
                        taskPaper.setName(rule.getRuleName());
                    }
                    return taskPaper;
                })
                .collect(Collectors.toList());
    }

    private static ThemeInfo getThemeInfo(IncreaseExamTaskCommand increaseExamTaskCommand) {
        //插入主题名称
        ThemeService themeService = ApplicationContextHolder.getBean(ThemeService.class);
        ThemePO theme = themeService.getById(increaseExamTaskCommand.getThemeId());
        //主题信息
        ThemeInfo themeInfo = new ThemeInfo(increaseExamTaskCommand.getThemeId(), null);
        themeInfo.setName(theme != null ? theme.getName() : "");
        return themeInfo;
    }
}
