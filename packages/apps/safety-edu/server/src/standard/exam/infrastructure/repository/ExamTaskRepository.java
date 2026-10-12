package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskRepository {

    private final ExamTaskMapper examTaskMapper;

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskPaperService examTaskPaperService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    @Transactional(rollbackFor = Exception.class)
    public ExamTask save(ExamTask examTask) {
        boolean isExecuteSave = true;
        ExamTaskPO existExamTaskPO = this.examTaskMapper.selectById(examTask.getId());
        if (Objects.nonNull(existExamTaskPO)) {
            SessionContextHolder.requestTenant(existExamTaskPO.getTenant());
            isExecuteSave = false;
        }
        ExamTaskPO examTaskPO = buildExamTaskPO(examTask);
        //删除任务关联用户
        List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                     .eq(ExamTaskUserPO::getExamTaskId, examTask.getId()));
        //删除任务关联试卷
        List<ExamTaskPaperPO> taskPaperPOS = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                    .eq(ExamTaskPaperPO::getExamTaskId, examTask.getId()));
        boolean randomMode = ExamTaskPaperModeEnum.RANDOM == examTask.getExamPaperMode();
        if (!CollectionUtils.isEmpty(examTaskUserPOS)) {
            DatabaseContextHolder.softDelete(ExamTaskUserPO.class, examTaskUserPOS);
        }
        if (!CollectionUtils.isEmpty(taskPaperPOS) && !randomMode) {
            DatabaseContextHolder.softDelete(ExamTaskPaperPO.class, taskPaperPOS);
        }
        if (randomMode) {
            List<ExamTaskPaperPO> templateRows = taskPaperPOS.stream()
                    .filter(taskPaper -> Objects.isNull(taskPaper.getUserIdentify()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(templateRows)) {
                DatabaseContextHolder.softDelete(ExamTaskPaperPO.class, templateRows);
            }
            cleanupRandomUserPapersForRemovedUsers(examTask, examTaskUserPOS, taskPaperPOS);
        }
        if (!CollectionUtils.isEmpty(examTask.getExamTaskUserInfos())) {
            List<ExamTaskUserPO> saveExamTaskUserPOS = buildExamTaskUserPOS(examTask);
            this.examTaskUserService.saveBatch(saveExamTaskUserPOS);
        }
        if (!CollectionUtils.isEmpty(examTask.getExamPapers())) {
            List<ExamTaskPaperPO> examTaskPaperPOS = buildExamTaskPaperPOS(examTask);
            this.examTaskPaperService.saveBatch(examTaskPaperPOS);
        }
        if (isExecuteSave) {
            EntityAuditor.populateCreateAudit(examTaskPO);
            this.examTaskMapper.insert(examTaskPO);
        } else {
            SessionContextHolder.requestTenant(existExamTaskPO.getTenant());
            examTaskPO.setTenant(existExamTaskPO.getTenant());
            examTaskPO.setMarketTenant(existExamTaskPO.getMarketTenant());
            EntityAuditor.populateCreateAudit(examTaskPO, existExamTaskPO.getCreateBy(), existExamTaskPO.getCreatorId(),
                                              existExamTaskPO.getCreatorType(), existExamTaskPO.getCreateTime());
            this.examTaskMapper.updateById(examTaskPO);
        }
        return examTask;
    }

    private ExamTaskPO buildExamTaskPO(ExamTask examTask) {
        Principal principal = SecurityContextHolder.getPrincipal(true);
        String userName = Objects.nonNull(principal) ? principal.getAccount() : "system";
        return ExamTaskPO.builder()
                .id(examTask.getId())
                .name(examTask.getExamTaskInfo().getName())
                .themeId(examTask.getThemeInfo().getId())
                .themeName(examTask.getThemeInfo().getName())
                .sourceCode(examTask.getExamTaskSource().getSourceCode())
                .sourceName(examTask.getExamTaskSource().getSourceName())
                .sourceKey(examTask.getExamTaskSource().getSourceKey())
                .examStartTime(examTask.getExamTaskDateInfo().getStartTime())
                .examEndTime(examTask.getExamTaskDateInfo().getEndTime())
                .chargeUserId(examTask.getExamTaskChargeUserInfo().getId())
                .chargeUserName(examTask.getExamTaskChargeUserInfo().getName())
                .timed(examTask.getExamTaskTimedInfo().getTimed())
                .examDuration(examTask.getExamTaskTimedInfo().getExamDuration())
                .examDurationUnit(examTask.getExamTaskTimedInfo().getExamDurationUnit())
                .retake(examTask.getExamTaskRetakeInfo().getRetake())
                .retakeNumber(examTask.getExamTaskRetakeInfo().getRetakeNumber())
                .examTaskType(examTask.getExamTaskType().getExamTaskType())
                .examPaperMode(examTask.getExamPaperMode() != null ? examTask.getExamPaperMode() : ExamTaskPaperModeEnum.UNIFIED)
                .examUserNumber(examTask.getExamUserNumber())
                .examTaskStatus(examTask.getExamTaskStatus().getExamTaskStatus())
                .publishedTime(Objects.nonNull(examTask.getExamTaskPublishedInfo()) ? examTask.getExamTaskPublishedInfo().getPublishedTime()
                                                                                    : null)
                .remark(examTask.getExamTaskInfo().getRemark())
                .marketTenant(Objects.nonNull(examTask.getMarketTenant()) ? examTask.getMarketTenant()
                                                                          : SessionContextHolder.getRequester().getTenant())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .deleted(0L)
                .updateBy(userName)
                .updateTime(LocalDateTime.now())
                .build();
    }

    private List<ExamTaskUserPO> buildExamTaskUserPOS(ExamTask examTask) {
        //保存任务关联用户
        return examTask.getExamTaskUserInfos().stream()
                .map(examTaskUserInfo ->
                             ExamTaskUserPO.builder()
                                     .id(IdWorker.getId())
                                     .examTaskId(examTask.getId())
                                     .userIdentify(examTaskUserInfo.getIdentify())
                                     .userId(examTaskUserInfo.getId())
                                     .userName(examTaskUserInfo.getName())
                                     .userPhone(examTaskUserInfo.getPhone())
                                     .userDepartmentId(examTaskUserInfo.getDepartmentId())
                                     .userDepartmentName(examTaskUserInfo.getDepartmentName())
                                     .userPostId(examTaskUserInfo.getPostId())
                                     .userPostName(examTaskUserInfo.getPostName())
                                     .examStatus(UserExamStatusEnum.STAY_EXAM)
                                     .examNumber(0)
                                     .build()
                    ).collect(Collectors.toList());
    }

    private void cleanupRandomUserPapersForRemovedUsers(ExamTask examTask, List<ExamTaskUserPO> oldUsers,
                                                        List<ExamTaskPaperPO> taskPaperPOS
                                                       ) {
        if (CollectionUtils.isEmpty(oldUsers) || CollectionUtils.isEmpty(taskPaperPOS)) {
            return;
        }
        Set<String> latestUserIdentifies = CollectionUtils.isEmpty(examTask.getExamTaskUserInfos())
                                           ? Set.of()
                                           : examTask.getExamTaskUserInfos().stream()
                                                   .map(ExamTaskUserInfo::getIdentify)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toSet());
        Set<String> removedUserIdentifies = oldUsers.stream()
                .map(ExamTaskUserPO::getUserIdentify)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
        removedUserIdentifies.removeAll(latestUserIdentifies);
        if (CollectionUtils.isEmpty(removedUserIdentifies)) {
            return;
        }
        List<ExamTaskPaperPO> removedUserPapers = taskPaperPOS.stream()
                .filter(taskPaper -> removedUserIdentifies.contains(taskPaper.getUserIdentify()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(removedUserPapers)) {
            return;
        }
        DatabaseContextHolder.softDelete(ExamTaskPaperPO.class, removedUserPapers);
    }

    private List<ExamTaskPaperPO> buildExamTaskPaperPOS(ExamTask examTask) {
        return examTask.getExamPapers().stream()
                .map(examTaskPaper ->
                             ExamTaskPaperPO.builder()
                                     .examTaskId(examTask.getId())
                                     .examPaperId(examTaskPaper.getId())
                                     .ruleId(examTaskPaper.getRuleId())
                                     .userIdentify(examTaskPaper.getUserIdentify())
                                     .examPaperName(examTaskPaper.getName())
                                     .examPaperContent(examTaskPaper.getExamPaperContent())
                                     .examPaperScore(examTaskPaper.getExamPaperScore())
                                     .examPaperPassingGradeScore(examTaskPaper.getGradeScore())
                                     .examPaperQuestionNumber(examTaskPaper.getRequestNumber())
                                     .examPaperRemark(examTaskPaper.getExamPaperRemark())
                                     .build()
                    ).collect(Collectors.toList());
    }

    public Optional<ExamTask> get(Long id) {
        ExamTaskPO examTaskPO = this.examTaskMapper.selectById(id);
        if (Objects.isNull(examTaskPO)) {
            return Optional.empty();
        }
        ExamTask examTask = new ExamTask(examTaskPO.getId(),
                                         new ExamTaskInfo(examTaskPO.getName(), examTaskPO.getRemark()),
                                         new ThemeInfo(examTaskPO.getThemeId(), examTaskPO.getThemeName()),
                                         new ExamTaskChargeUserInfo(examTaskPO.getChargeUserId(), examTaskPO.getChargeUserName()),
                                         new ExamTaskType(examTaskPO.getExamTaskType(), examTaskPO.getExamTaskType().getDescription()),
                                         new ExamTaskStatus(examTaskPO.getExamTaskStatus(), examTaskPO.getExamTaskStatus()
                                                 .getDescription()));
        examTask.settingExamPaperMode(
                examTaskPO.getExamPaperMode() != null ? examTaskPO.getExamPaperMode() : ExamTaskPaperModeEnum.UNIFIED);
        //试卷信息
        examTask.settingExamPaper(getExamTaskPapers(examTaskPO));
        //考试时间信息
        examTask.settingExamTaskDateInfo(examTaskPO.getExamStartTime(), examTaskPO.getExamEndTime());
        //复考信息
        examTask.settingExamTaskRetakeInfo(examTaskPO.getRetake(), examTaskPO.getRetakeNumber());
        //限时信息
        examTask.settingExamTaskTimedInfo(examTaskPO.getTimed(), examTaskPO.getExamDuration(), examTaskPO.getExamDurationUnit());
        //发布信息
        examTask.settingExamTaskPublishedInfo(examTaskPO.getPublishedTime());
        //任务来源
        examTask.settingExamTaskSource(examTaskPO.getSourceCode(), examTaskPO.getSourceName(), examTaskPO.getSourceKey());
        //考试人数
        examTask.settingExamTaskUserNum(examTaskPO.getExamUserNumber());
        //考试人员
        List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                     .eq(ExamTaskUserPO::getExamTaskId, examTask.getId()));
        List<ExamTaskUserInfo> examTaskUserInfos = new ArrayList<>(examTaskUserPOS.size());
        examTaskUserPOS.forEach(examTaskUserPO ->
                                        examTaskUserInfos.add(new ExamTaskUserInfo(
                                                examTaskUserPO.getUserId(),
                                                examTaskUserPO.getUserIdentify(),
                                                examTaskUserPO.getUserName(),
                                                examTaskUserPO.getUserPhone(),
                                                examTaskUserPO.getUserDepartmentId(),
                                                examTaskUserPO.getUserDepartmentName(),
                                                examTaskUserPO.getUserPostId(),
                                                examTaskUserPO.getUserPostName()
                                        ))
                               );
        examTask.settingExamTaskUserInfos(examTaskUserInfos);
        return Optional.of(examTask);
    }

    private List<ExamTaskPaper> getExamTaskPapers(ExamTaskPO examTaskPO) {
        if (ExamTaskPaperModeEnum.RANDOM == examTaskPO.getExamPaperMode()) {
            List<ExamTaskPaperPO> templateRows = this.examTaskPaperService.list(Wrappers.<ExamTaskPaperPO>lambdaQuery()
                                                                                        .eq(ExamTaskPaperPO::getExamTaskId, examTaskPO.getId())
                                                                                        .isNull(ExamTaskPaperPO::getUserIdentify)
                                                                                        .isNotNull(ExamTaskPaperPO::getRuleId));
            if (CollectionUtils.isEmpty(templateRows)) {
                return List.of();
            }
            return templateRows.stream().map(row -> {
                ExamTaskPaper taskPaper = new ExamTaskPaper();
                taskPaper.setRuleId(row.getRuleId());
                taskPaper.setUserIdentify(row.getUserIdentify());
                taskPaper.setName(row.getExamPaperName());
                taskPaper.setExamPaperScore(row.getExamPaperScore());
                taskPaper.setGradeScore(row.getExamPaperPassingGradeScore());
                taskPaper.setRequestNumber(row.getExamPaperQuestionNumber());
                return taskPaper;
            }).collect(Collectors.toList());
        }
        List<ExamPaperVO> examTaskPaperVOS = examTaskPaperQueryService.getExamTaskPaperVOS(examTaskPO.getId());
        return ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examTaskPaperVOS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTaskById(Set<Long> ids) {
        List<ExamTaskPO> examTaskPOS = this.examTaskMapper.selectBatchIds(ids);
        if (!CollectionUtils.isEmpty(examTaskPOS)) {
            examTaskPOS.forEach(examTaskPO -> {
                examTaskPO.setDeleted(System.currentTimeMillis());
                this.examTaskMapper.updateById(examTaskPO);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTaskBySourceId(DeleteExamTaskCommand deleteExamTaskCommand) {
        List<ExamTaskPO> examTaskPOS = this.examTaskMapper.selectList(Wrappers.<ExamTaskPO>lambdaQuery()
                                                                              .in(ExamTaskPO::getSourceKey, deleteExamTaskCommand.getExamTaskIds()));
        if (!CollectionUtils.isEmpty(examTaskPOS)) {
            examTaskPOS.forEach(examTaskPO -> {
                examTaskPO.setDeleted(System.currentTimeMillis());
                this.examTaskMapper.updateById(examTaskPO);
            });
        }
    }
}
