package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamTaskUserCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamRandomExamPaperBuildService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskDateInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskPublishedInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskRetakeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskSource;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskTimedInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.Getter;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 考试任务
 *
 * @author lanchun
 * @date 2023/4/19 15:44
 */
@Getter
public class ExamTask {

    private Long id;

    /**
     * 任务基础信息
     */
    private ExamTaskInfo examTaskInfo;

    /**
     * 主题信息
     */
    private ThemeInfo themeInfo;

    /**
     * 考试时间信息
     */
    private ExamTaskDateInfo examTaskDateInfo;

    /**
     * 负责人信息
     */
    private ExamTaskChargeUserInfo examTaskChargeUserInfo;

    /**
     * 试卷信息
     */
    private List<ExamTaskPaper> examPapers;

    /**
     * 限时信息
     */
    private ExamTaskTimedInfo examTaskTimedInfo;

    /**
     * 复考信息
     */
    private ExamTaskRetakeInfo examTaskRetakeInfo;

    /**
     * 试卷类型（统一题目 / 随机出题）
     */
    private ExamTaskPaperModeEnum examPaperMode;

    /**
     * 考试类型
     */
    private ExamTaskType examTaskType;

    /**
     * 考试状态
     */
    private ExamTaskStatus examTaskStatus;

    /**
     * 发布信息
     */
    private ExamTaskPublishedInfo examTaskPublishedInfo;

    /**
     * 任务来源信息
     */
    private ExamTaskSource examTaskSource;

    /**
     * 考试人员总数
     */
    private Integer examUserNumber;

    /**
     * 考试用户信息
     */
    private List<ExamTaskUserInfo> examTaskUserInfos;

    /**
     * 市场编码
     */
    private Integer marketTenant;

    public ExamTask(Long id,
                    ExamTaskInfo examTaskInfo,
                    ThemeInfo themeInfo,
                    ExamTaskChargeUserInfo examTaskChargeUserInfo,
                    ExamTaskType examTaskType,
                    ExamTaskStatus examTaskStatus
                   ) {
        this.id = id;
        this.examTaskInfo = examTaskInfo;
        this.themeInfo = themeInfo;
        this.examTaskChargeUserInfo = examTaskChargeUserInfo;
        this.examTaskType = examTaskType;
        this.examTaskStatus = examTaskStatus;
    }

    public void setMarketTenant(Integer marketTenant) {
        this.marketTenant = marketTenant;
    }

    /**
     * 任务限时设置
     */
    public void settingExamTaskTimedInfo(Boolean timed, Integer examDuration, String examDurationUnit) {
        this.examTaskTimedInfo = new ExamTaskTimedInfo(timed, examDuration, examDurationUnit);
    }

    /**
     * 任务复考设置
     */
    public void settingExamTaskRetakeInfo(Boolean retake, Integer retakeNumber) {
        this.examTaskRetakeInfo = new ExamTaskRetakeInfo(retake, retakeNumber);
    }

    /**
     * 考试人数设置
     *
     * @param examUserNumber
     */
    public void settingExamTaskUserNum(Integer examUserNumber) {
        this.examUserNumber = examUserNumber;
    }

    /**
     * 设置时间信息
     *
     * @param startTime
     * @param endTime
     */
    public void settingExamTaskDateInfo(LocalDateTime startTime, LocalDateTime endTime) {
        this.examTaskDateInfo = new ExamTaskDateInfo(startTime, endTime);
    }

    /**
     * 任务考试人员设置
     *
     * @param examTaskUserInfos
     */
    public void settingExamTaskUserInfos(List<ExamTaskUserInfo> examTaskUserInfos) {
        this.examTaskUserInfos = examTaskUserInfos;
    }

    /**
     * 发布时间设置
     */
    public void settingExamTaskPublishedInfo(LocalDateTime publishedTime) {
        this.examTaskPublishedInfo = new ExamTaskPublishedInfo(publishedTime);
    }

    /**
     * 任务来源设置
     *
     * @param sourceCode
     * @param sourceName
     * @param sourceKey
     */
    public void settingExamTaskSource(String sourceCode, String sourceName, String sourceKey) {
        this.examTaskSource = new ExamTaskSource(sourceCode, sourceName, sourceKey);
    }

    /**
     * 重置发布时间
     */
    public void resetExamTaskPublishedInfo() {
        this.examTaskPublishedInfo = null;
    }

    /**
     * 修改任务基本信息
     *
     * @param name
     * @param remark
     */
    public void changeExamTaskInfo(String name, String remark) {
        this.examTaskInfo = new ExamTaskInfo(name, remark);
    }

    /**
     * 修改主题信息
     */
    public void changeThemeInfo(Long id, String name) {
        this.themeInfo = new ThemeInfo(id, name);
    }

    /**
     * 试卷信息设置
     *
     * @param examPapers
     */
    public void settingExamPaper(List<ExamTaskPaper> examPapers) {
        this.examPapers = examPapers;
    }

    /**
     * 试卷组卷方式
     */
    public void settingExamPaperMode(ExamTaskPaperModeEnum examPaperMode) {
        this.examPaperMode = examPaperMode;
    }

    /**
     * 修改试卷信息
     */
    public void changeExamPaper(ExamTaskPaperModeEnum examPaperMode, Set<Long> examPaperIds) {
        Set<Long> currentRuleIds = CollectionUtils.isEmpty(this.examPapers)
                ? Collections.emptySet()
                : this.examPapers.stream()
                .map(ExamTaskPaper::getRuleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> targetRuleIds = CollectionUtils.isEmpty(examPaperIds)
                ? Collections.emptySet()
                : examPaperIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        boolean modeChanged = this.examPaperMode != examPaperMode;
        boolean randomRuleChanged = ExamTaskPaperModeEnum.RANDOM == examPaperMode && !Objects.equals(currentRuleIds, targetRuleIds);
        this.examPaperMode = examPaperMode;
        ExamRandomExamPaperBuildService randomBuildService = ApplicationContextHolder.getBean(ExamRandomExamPaperBuildService.class);
        if (modeChanged || randomRuleChanged) {
            randomBuildService.removeRandomPapersByExamTaskId(this.id);
        }
        if (ExamTaskPaperModeEnum.UNIFIED == examPaperMode) {
            ExamPaperQueryService examPaperQueryService = ApplicationContextHolder.getBean(ExamPaperQueryService.class);
            List<ExamPaperVO> examPapersIfExist = examPaperQueryService.getExamPapersIfExist(examPaperIds);
            if (!CollectionUtils.isEmpty(examPapersIfExist)) {
                this.examPapers = ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examPapersIfExist);
            } else {
                this.examPapers = Collections.emptyList();
            }
            return;
        }
        if (CollectionUtils.isEmpty(examPaperIds)) {
            this.examPapers = Collections.emptyList();
            return;
        }
        ExamQuestionRuleQueryService examQuestionRuleQueryService = ApplicationContextHolder.getBean(ExamQuestionRuleQueryService.class);
        List<ExamQuestionRuleVO> rules = examQuestionRuleQueryService.listExamQuestionRulesByIds(examPaperIds);
        var ruleMap = rules.stream().collect(Collectors.toMap(ExamQuestionRuleVO::getId, r -> r));
        this.examPapers = examPaperIds.stream()
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
                    }
                    return taskPaper;
                })
                .collect(Collectors.toList());
    }

    /**
     * 修改试卷类型
     */
    public void changeExamTaskType(ExamTaskTypeEnum examTaskType) {
        this.examTaskType = new ExamTaskType(examTaskType, examTaskType.getDescription());
    }

    /**
     * 修改负责人
     *
     * @param userId
     */
    public void changeExamTaskChargeUserInfo(Long userId) {
        this.examTaskChargeUserInfo = new ExamTaskChargeUserInfo(userId, null);
    }

    /**
     * 修改考试用户
     *
     * @param changeExamTaskUserCommands
     */
    public void changeExamTaskUserInfos(List<ChangeExamTaskUserCommand> changeExamTaskUserCommands) {
        if (!CollectionUtils.isEmpty(changeExamTaskUserCommands)) {
            List<ExamTaskUserInfo> localExamTaskUserInfos = new ArrayList<>(changeExamTaskUserCommands.size());
            changeExamTaskUserCommands.forEach(changeExamTaskUserCommand ->
                                                       localExamTaskUserInfos.add(new ExamTaskUserInfo(
                                                               changeExamTaskUserCommand.getId(),
                                                               changeExamTaskUserCommand.getId().toString(),
                                                               changeExamTaskUserCommand.getName(),
                                                               changeExamTaskUserCommand.getPhone(),
                                                               changeExamTaskUserCommand.getDepartmentId(),
                                                               changeExamTaskUserCommand.getDepartmentName(),
                                                               changeExamTaskUserCommand.getPostId(),
                                                               changeExamTaskUserCommand.getPostName()))
                                              );
            this.examTaskUserInfos = localExamTaskUserInfos;
        } else {
            this.examTaskUserInfos = Collections.emptyList();
        }
    }

    /**
     * 修改考试状态
     *
     * @param examTaskStatus
     */
    public void changeExamTaskStatus(ExamTaskStatusEnum examTaskStatus) {
        this.examTaskStatus = new ExamTaskStatus(examTaskStatus, examTaskStatus.getDescription());
    }

    public Integer getExamUserNumber() {
        if (!CollectionUtils.isEmpty(examTaskUserInfos)) {
            return examTaskUserInfos.size();
        }
        return examUserNumber;
    }
}
