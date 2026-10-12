package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskMarketService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskMarketPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.valueobj.HeadquartersExamTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper.HeadquartersExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskChargeUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskStatus;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils.ExamTaskPaperConvertUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskRepository {

    private final HeadquartersExamTaskMapper headquartersExamTaskMapper;

    private final HeadquartersExamTaskMarketService headquartersExamTaskMarketService;

    private final HeadquartersExamTaskPaperService headquartersExamTaskPaperService;

    @Transactional(rollbackFor = Exception.class)
    public HeadquartersExamTask save(HeadquartersExamTask headquartersExamTask) {
        boolean isExecuteSave = true;
        HeadquartersExamTaskPO existExamTaskPO = this.headquartersExamTaskMapper.selectById(headquartersExamTask.getId());
        if (Objects.nonNull(existExamTaskPO)) {
            isExecuteSave = false;
        }
        //考试人数
        Integer examUserNumber = headquartersExamTask.getExamUserNumber();
        HeadquartersExamTaskPO examTaskPO = buildHeadquartersExamTaskPO(headquartersExamTask, examUserNumber);
        //删除任务关联市场
        List<HeadquartersExamTaskMarketPO> examTaskMarketPOS = this.headquartersExamTaskMarketService
                .list(Wrappers.<HeadquartersExamTaskMarketPO>lambdaQuery()
                              .eq(HeadquartersExamTaskMarketPO::getHeadquartersExamTaskId, headquartersExamTask.getId()));
        if (!CollectionUtils.isEmpty(examTaskMarketPOS)) {
            DatabaseContextHolder.softDelete(HeadquartersExamTaskMarketPO.class, examTaskMarketPOS);
        }
        List<HeadquartersExamTaskPaperPO> examTaskPaperPOS = this.headquartersExamTaskPaperService
                .list(Wrappers.<HeadquartersExamTaskPaperPO>lambdaQuery()
                              .eq(HeadquartersExamTaskPaperPO::getHeadquartersExamTaskId, headquartersExamTask.getId()));
        //保存任务关联市场
        List<HeadquartersExamTaskMarketPO> saveExamTaskMarketPOS = buildHeadquartersExamTaskMarketPOS(headquartersExamTask);
        this.headquartersExamTaskMarketService.saveBatch(saveExamTaskMarketPOS);
        //删除任务关联试卷
        if (!CollectionUtils.isEmpty(examTaskPaperPOS)) {
            DatabaseContextHolder.softDelete(HeadquartersExamTaskPaperPO.class, examTaskPaperPOS);
        }
        if (!CollectionUtils.isEmpty(headquartersExamTask.getExamPapers())) {
            List<HeadquartersExamTaskPaperPO> saveExamTaskPaperPOS = buildExamTaskPaperPOS(headquartersExamTask);
            this.headquartersExamTaskPaperService.saveBatch(saveExamTaskPaperPOS);
        }
        if (isExecuteSave) {
            EntityAuditor.populateCreateAudit(examTaskPO);
            this.headquartersExamTaskMapper.insert(examTaskPO);
        } else {
            examTaskPO.setDeleted(existExamTaskPO.getDeleted());
            EntityAuditor.populateCreateAudit(examTaskPO, existExamTaskPO.getCreateBy(), existExamTaskPO.getCreatorId(),
                                              existExamTaskPO.getCreatorType(), existExamTaskPO.getCreateTime());
            this.headquartersExamTaskMapper.updateById(examTaskPO);
        }
        return headquartersExamTask;
    }

    private List<HeadquartersExamTaskPaperPO> buildExamTaskPaperPOS(HeadquartersExamTask examTask) {
        return examTask.getExamPapers().stream()
                .map(examTaskPaper ->
                             HeadquartersExamTaskPaperPO.builder()
                                     .headquartersExamTaskId(examTask.getId())
                                     .examPaperId(examTaskPaper.getId())
                                     .examPaperName(examTaskPaper.getName())
                                     .examPaperContent(examTaskPaper.getExamPaperContent())
                                     .examPaperScore(examTaskPaper.getExamPaperScore())
                                     .examPaperPassingGradeScore(examTaskPaper.getGradeScore())
                                     .examPaperQuestionNumber(examTaskPaper.getRequestNumber())
                                     .examPaperRemark(examTaskPaper.getExamPaperRemark())
                                     .build()
                    ).collect(Collectors.toList());
    }

    private List<HeadquartersExamTaskMarketPO> buildHeadquartersExamTaskMarketPOS(HeadquartersExamTask headquartersExamTask) {
        return headquartersExamTask.getExamTaskMarkets().stream()
                .map(examTaskUserInfo ->
                             HeadquartersExamTaskMarketPO.builder()
                                     .id(IdWorker.getId())
                                     .headquartersExamTaskId(headquartersExamTask.getId())
                                     .marketCode(examTaskUserInfo.getMarketCode())
                                     .marketTenantCode(examTaskUserInfo.getMarketTenantCode())
                                     .marketName(examTaskUserInfo.getMarketName())
                                     .build())
                .collect(Collectors.toList());
    }

    private HeadquartersExamTaskPO buildHeadquartersExamTaskPO(HeadquartersExamTask headquartersExamTask, Integer examUserNumber) {
        return HeadquartersExamTaskPO.builder()
                .id(headquartersExamTask.getId())
                .name(headquartersExamTask.getExamTaskInfo().getName())
                .themeId(headquartersExamTask.getThemeInfo().getId())
                .themeName(headquartersExamTask.getThemeInfo().getName())
                .examStartTime(headquartersExamTask.getExamTaskDateInfo().getStartTime())
                .examEndTime(headquartersExamTask.getExamTaskDateInfo().getEndTime())
                .chargeUserId(headquartersExamTask.getExamTaskChargeUserInfo().getId())
                .chargeUserName(headquartersExamTask.getExamTaskChargeUserInfo().getName())
                .timed(headquartersExamTask.getExamTaskTimedInfo().getTimed())
                .examDuration(headquartersExamTask.getExamTaskTimedInfo().getExamDuration())
                .examDurationUnit(headquartersExamTask.getExamTaskTimedInfo().getExamDurationUnit())
                .retake(headquartersExamTask.getExamTaskRetakeInfo().getRetake())
                .retakeNumber(headquartersExamTask.getExamTaskRetakeInfo().getRetakeNumber())
                .examTaskType(headquartersExamTask.getExamTaskType().getExamTaskType())
                .examUserNumber(examUserNumber)
                .examTaskStatus(headquartersExamTask.getExamTaskStatus().getExamTaskStatus())
                .publishedTime(
                        Objects.nonNull(headquartersExamTask.getExamTaskPublishedInfo()) ? headquartersExamTask.getExamTaskPublishedInfo()
                                .getPublishedTime() : null)
                .remark(headquartersExamTask.getExamTaskInfo().getRemark())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .updateBy(SecurityContextHolder.getPrincipal().getAccount())
                .updateTime(LocalDateTime.now())
                .build();
    }

    public Optional<HeadquartersExamTask> get(Long id) {
        HeadquartersExamTaskPO examTaskPO = this.headquartersExamTaskMapper.selectById(id);
        if (Objects.isNull(examTaskPO)) {
            return Optional.empty();
        }
        HeadquartersExamTask examTask = new HeadquartersExamTask(examTaskPO.getId(),
                                                                 new ExamTaskInfo(examTaskPO.getName(), examTaskPO.getRemark()),
                                                                 new ThemeInfo(examTaskPO.getThemeId(), examTaskPO.getThemeName()),
                                                                 new ExamTaskChargeUserInfo(examTaskPO.getChargeUserId(), examTaskPO.getChargeUserName()),
                                                                 new ExamTaskType(examTaskPO.getExamTaskType(), examTaskPO.getExamTaskType()
                                                                         .getDescription()),
                                                                 new ExamTaskStatus(examTaskPO.getExamTaskStatus(), examTaskPO
                                                                         .getExamTaskStatus().getDescription()));
        //考试时间信息
        examTask.settingExamTaskDateInfo(examTaskPO.getExamStartTime(), examTaskPO.getExamEndTime());
        //复考信息
        examTask.settingExamTaskRetakeInfo(examTaskPO.getRetake(), examTaskPO.getRetakeNumber());
        //限时信息
        examTask.settingExamTaskTimedInfo(examTaskPO.getTimed(), examTaskPO.getExamDuration(), examTaskPO.getExamDurationUnit());
        //发布信息
        examTask.settingExamTaskPublishedInfo(examTaskPO.getPublishedTime());
        //试卷信息
        examTask.settingExamPaper(getExamTaskPapers(examTaskPO));
        //考试人数
        examTask.settingExamTaskUserNum(examTaskPO.getExamUserNumber());
        //考试市场
        examTask.settingExamTaskMarketInfos(getHeadquartersExamTaskMarkets(examTask));
        return Optional.of(examTask);
    }

    private List<HeadquartersExamTaskMarket> getHeadquartersExamTaskMarkets(HeadquartersExamTask examTask) {
        List<HeadquartersExamTaskMarketPO> examTaskMarketPOS = this.headquartersExamTaskMarketService.list(Wrappers
                                                                                                                   .<HeadquartersExamTaskMarketPO>lambdaQuery()
                                                                                                                   .eq(HeadquartersExamTaskMarketPO::getHeadquartersExamTaskId, examTask.getId()));
        List<HeadquartersExamTaskMarket> examTaskMarkets = new ArrayList<>(examTaskMarketPOS.size());
        examTaskMarketPOS.forEach(examTaskMarketPO ->
                                          examTaskMarkets.add(new HeadquartersExamTaskMarket(
                                                  examTaskMarketPO.getMarketCode(),
                                                  examTaskMarketPO.getMarketTenantCode(),
                                                  examTaskMarketPO.getMarketName()
                                          ))
                                 );
        return examTaskMarkets;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTaskById(Set<Long> ids) {
        List<HeadquartersExamTaskPO> examTaskPOS = this.headquartersExamTaskMapper.selectBatchIds(ids);
        if (!CollectionUtils.isEmpty(examTaskPOS)) {
            examTaskPOS.forEach(examTaskPO ->
                                        DatabaseContextHolder.softDelete(HeadquartersExamTaskPO.class, examTaskPOS)
                               );
        }
    }

    private List<ExamTaskPaper> getExamTaskPapers(HeadquartersExamTaskPO examTaskPO) {
        List<ExamPaperVO> examPaperVOS = this.headquartersExamTaskPaperService.getExamTaskPaperVOS(examTaskPO.getId());
        return ExamTaskPaperConvertUtils.examPaperVOConvertExamTaskPaper(examPaperVOS);
    }
}
