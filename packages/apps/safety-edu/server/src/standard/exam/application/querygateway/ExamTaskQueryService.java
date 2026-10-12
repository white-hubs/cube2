package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ExamQuestionRuleService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ExamQuestionRulePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.open.ExamTaskOpenQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskStandardVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskUserVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskQueryService {

    private final ExamTaskMapper examTaskMapper;

    private final ExamTaskService examTaskService;

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    private final ThemeService themeService;

    private final UserClient userClient;

    private final ExamQuestionRuleService examQuestionRuleService;

    /**
     * 任务分页列表
     *
     * @param examTaskQuery
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "safetyEducation:examTask")
    public Pagination<ExamTaskVO> getExamTaskPage(ExamTaskQuery examTaskQuery) {
        Page<ExamTaskVO> page = this.examTaskMapper.selectExamTaskPage(PagingUtils.convert(examTaskQuery), SessionContextHolder
                .getRequester().getTenant(), examTaskQuery);
        extendList(page.getRecords());
        return PagingUtils.convert(page);
    }

    /**
     * 任务分页列表
     *
     * @param examTaskQuery
     *
     * @return
     */
    public Pagination<ExamTaskVO> getPublicExamTaskPage(ExamTaskQuery examTaskQuery) {
        Page<ExamTaskVO> page = this.examTaskMapper.selectExamTaskPage(PagingUtils.convert(examTaskQuery), examTaskQuery.getTenant(), examTaskQuery);
        extendList(page.getRecords());
        return PagingUtils.convert(page);
    }

    /**
     * 获取用户任务分页列表
     *
     * @param examTaskQuery
     *
     * @return
     */
    public Pagination<UserExamTaskVO> getUserExamTaskPage(ExamTaskQuery examTaskQuery) {
        examTaskQuery.setUserId(SecurityContextHolder.getPrincipal().getId());
        Page<UserExamTaskVO> page = this.examTaskMapper.selectUserExamTaskPage(PagingUtils.convert(examTaskQuery), SessionContextHolder
                .getRequester().getTenant(), examTaskQuery);
        page.getRecords().forEach(this::extendUserExamTaskVO);
        return PagingUtils.convert(page);
    }

    public UserExamTaskVO getUserExamTask(ExamTaskQuery examTaskQuery) {

        Page<UserExamTaskVO> page = this.examTaskMapper.selectUserExamTaskPage(PagingUtils.convert(examTaskQuery), SessionContextHolder
                .getRequester().getTenant(), examTaskQuery);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return null;
        }
        page.getRecords().forEach(this::extendUserExamTaskVO);
        return page.getRecords().get(0);
    }

    /**
     * 获取正在使用的任务数量
     *
     * @param paperId 试卷主键
     *
     * @return 任务总数
     */
    public Integer getUsedExamTaskByPaperId(Long paperId) {
        return this.getUsedExamTaskByPaperIds(Collections.singleton(paperId));
    }

    /**
     * 获取正在使用的任务数量
     *
     * @param paperIds 试卷主键集合
     *
     * @return 任务总数
     */
    public Integer getUsedExamTaskByPaperIds(Set<Long> paperIds) {
        return this.examTaskMapper.selectUsedExamTaskCount(paperIds);
    }

    public void extendUserExamTaskVO(UserExamTaskVO userExamTaskVO) {
        List<ExamPaperVO> examTaskPaperVOS = this.examTaskPaperQueryService.getExamTaskPaperVOS(userExamTaskVO.getId());
        userExamTaskVO.setExamTaskStatusName(userExamTaskVO.getExamTaskStatus().getDescription());
        userExamTaskVO.setExamTaskTypeName(userExamTaskVO.getExamTaskType().getDescription());
        userExamTaskVO.setExamFrequencyLimit(false);
        if (!CollectionUtils.isEmpty(examTaskPaperVOS)) {
            ExamPaperVO examPaperVO = examTaskPaperVOS.get(0);
            userExamTaskVO.setExamPaperScore(examPaperVO.getScore());
            userExamTaskVO.setExamPaperPassingGradeScore(examPaperVO.getPassingGradeScore());
            userExamTaskVO.setExamPaperQuestionNumber(examPaperVO.getExamPaperQuestionNumber());
        }
        if (Objects.nonNull(userExamTaskVO.getExamNumber()) && userExamTaskVO.getExamNumber() > 0) {
            if (Objects.equals(Boolean.FALSE, userExamTaskVO.getRetake())) {
                userExamTaskVO.setExamFrequencyLimit(true);
            }
            if (Objects.equals(Boolean.TRUE, userExamTaskVO.getRetake()) &&
                userExamTaskVO.getRetakeNumber() <= userExamTaskVO.getExamNumber()) {
                userExamTaskVO.setExamFrequencyLimit(true);
            }
        }
    }

    /**
     * 获取用户任务分页列表
     *
     * @param query
     *
     * @return
     */
    public Pagination<UserExamTaskVO> getPublicUserExamTaskPage(ExamTaskOpenQuery query) {
        Page<UserExamTaskVO> page = this.examTaskMapper.selectPublicUserExamTaskPage(PagingUtils.convert(query), query);
        page.getRecords().forEach(this::extendUserExamTaskVO);
        return PagingUtils.convert(page);
    }

    /**
     * 任务列表
     *
     * @param examTaskQuery
     *
     * @return
     */
    public List<ExamTaskStandardVO> getExamTaskStandardList(ExamTaskQuery examTaskQuery) {
        var examTaskStandardVOS = this.examTaskMapper.selectExamTaskStandardList(SessionContextHolder.getRequester()
                                                                                         .getTenant(), examTaskQuery);
        if (CollectionUtils.isEmpty(examTaskStandardVOS)) {
            return Collections.emptyList();
        }
        var ids = examTaskStandardVOS.stream().map(ExamTaskStandardVO::getId).collect(Collectors.toSet());
        var examTaskPaperMap = examTaskPaperQueryService.getPublishedExamPapers(ids)
                .stream()
                .collect(Collectors.groupingBy(ExamTaskPaperPO::getExamTaskId));
        examTaskStandardVOS.forEach(examTaskStandardVO -> {
            examTaskStandardVO.setExamTaskStatusName(examTaskStandardVO.getExamTaskStatus().getDescription());
            examTaskStandardVO.setExamTaskTypeName(examTaskStandardVO.getExamTaskType().getDescription());
            if (Objects.nonNull(examTaskStandardVO.getExamPaperMode())) {
                examTaskStandardVO.setExamPaperModeName(examTaskStandardVO.getExamPaperMode().getDescription());
            }
            Optional.ofNullable(examTaskPaperMap.get(examTaskStandardVO.getId())).ifPresent((examTaskPaperPOS) -> {
                var examPaperVOS = examTaskPaperPOS.stream().map(examTaskPaperPO -> ExamPaperVO.builder()
                        .id(examTaskPaperPO.getExamPaperId())
                        .name(examTaskPaperPO.getExamPaperName())
                        .build()).collect(Collectors.toList());
                examTaskStandardVO.setExamPapers(examPaperVOS);
            });
        });
        return examTaskStandardVOS;
    }

    /**
     * 任务详情
     *
     * @param id
     *
     * @return
     */
    public ExamTaskVO getExamTask(Long id) {
        ExamTaskVO examTaskVO = this.getExamTaskInfo(id);
        List<ExamTaskUserPO> examTaskUserPOS = getExamTaskUserPOS(examTaskVO);
        Optional<ExamTaskUserPO> optionalExamTaskUserPO = examTaskUserPOS.stream()
                .filter(examTaskUserPO -> SecurityContextHolder.getPrincipal().getId().equals(examTaskUserPO.getUserId()))
                .findFirst();
        extendFrequencyLimit(examTaskVO, optionalExamTaskUserPO);
        examTaskVO.setCanExam(optionalExamTaskUserPO.isPresent());
        return examTaskVO;
    }

    public List<ExamTaskUserPO> getExamTaskUserPOS(ExamTaskVO examTaskVO) {
        //考试人员信息
        List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                     .eq(ExamTaskUserPO::getExamTaskId, examTaskVO.getId()));
        List<ExamTaskUserVO> examTaskUserVOS = ObjectUtils.copying(examTaskUserPOS, ExamTaskUserVO.class);
        examTaskVO.setExamUsers(examTaskUserVOS.stream()
                                        .sorted(Comparator.comparing(examTaskUserVO -> examTaskUserVO.getExamStatus().getSort()))
                                        .collect(Collectors.toList()));
        return examTaskUserPOS;
    }

    /**
     * 任务详情
     *
     * @param id
     *
     * @return
     */
    public ExamTaskVO getPublicExamTask(Long id, String userIdentify) {
        ExamTaskVO examTaskVO = this.getExamTaskInfo(id);
        //考试人员信息
        List<ExamTaskUserPO> examTaskUserPOS = getExamTaskUserPOS(examTaskVO);
        if (StringUtils.isNotBlank(userIdentify)) {
            Optional<ExamTaskUserPO> optionalExamTaskUserPO = examTaskUserPOS.stream()
                    .filter(examTaskUserPO -> userIdentify.equals(examTaskUserPO.getUserIdentify()))
                    .findFirst();
            extendFrequencyLimit(examTaskVO, optionalExamTaskUserPO);
        }
        return examTaskVO;
    }

    public ExamTaskVO getExamTaskInfo(Long id) {
        ExamTaskPO examTaskPO = this.examTaskService.getById(id);
        if (Objects.isNull(examTaskPO)) {
            ExamTaskUserPO examTaskUser = this.examTaskUserService.getById(id);
            Assert.isNotFound(examTaskUser, "任务不存在");
            examTaskPO = this.examTaskService.getById(examTaskUser.getExamTaskId());
        }
        Assert.isNotFound(examTaskPO, "任务不存在");
        ExamTaskVO examTaskVO = ObjectUtils.copying(examTaskPO, ExamTaskVO.class);
        this.extendDetail(examTaskVO);
        return examTaskVO;
    }

    public void extendFrequencyLimit(ExamTaskVO examTaskVO, Optional<ExamTaskUserPO> optionalExamTaskUserPO) {
        optionalExamTaskUserPO.ifPresent(examTaskUserPO -> {
            if (examTaskUserPO.getExamNumber() > 0) {
                if (Objects.equals(Boolean.FALSE, examTaskVO.getRetake())) {
                    examTaskVO.setExamFrequencyLimit(true);
                }
                if (Objects.equals(Boolean.TRUE, examTaskVO.getRetake()) &&
                    examTaskVO.getRetakeNumber() <= examTaskUserPO.getExamNumber()) {
                    examTaskVO.setExamFrequencyLimit(true);
                }
            }
        });
    }

    private void extendList(List<ExamTaskVO> examTaskVOS) {
        for (ExamTaskVO examTaskVO : examTaskVOS) {
            this.extend(examTaskVO);
            List<ExamTaskPaperPO> examTaskPapersBaseInfo = examTaskPaperQueryService.getExamTaskPapersBaseInfo(Collections.singleton(examTaskVO.getId()));
            if (ExamTaskStatusEnum.UNPUBLISHED == examTaskVO.getExamTaskStatus()) {
                Set<Long> paperIds = examTaskPapersBaseInfo.stream().map(ExamTaskPaperPO::getExamPaperId).collect(Collectors.toSet());
                examTaskVO.setExamPapers(this.examTaskPaperQueryService.getExamPaperBaseInfoByIds(paperIds));
            } else {
                List<ExamPaperVO> examPaperVOS = examTaskPapersBaseInfo.stream().map(examTaskPaperPO -> ExamPaperVO.builder()
                        .id(examTaskPaperPO.getExamPaperId())
                        .name(examTaskPaperPO.getExamPaperName())
                        .build()).collect(Collectors.toList());
                examTaskVO.setExamPapers(examPaperVOS);
            }
        }
    }

    private void extend(ExamTaskVO examTaskVO) {
        examTaskVO.setExamTaskTypeName(examTaskVO.getExamTaskType().getDescription());
        examTaskVO.setExamTaskStatusName(examTaskVO.getExamTaskStatus().getDescription());
        if (Objects.nonNull(examTaskVO.getExamPaperMode())) {
            examTaskVO.setExamPaperModeName(examTaskVO.getExamPaperMode().getDescription());
        }
        if (ExamTaskStatusEnum.UNPUBLISHED == examTaskVO.getExamTaskStatus()) {
            //主题信息
            ThemePO themePO = this.themeService.getById(examTaskVO.getThemeId());
            if (Objects.nonNull(themePO)) {
                examTaskVO.setThemeName(themePO.getName());
            }
            //负责人信息
            UserDTO user = this.userClient.getUser(examTaskVO.getChargeUserId());
            if (Objects.nonNull(user)) {
                examTaskVO.setChargeUserName(user.getName());
            }
        }
    }

    private void extendDetail(ExamTaskVO examTaskVO) {
        this.extend(examTaskVO);
        //试卷信息
        List<ExamPaperVO> examTaskPaperVOS = this.examTaskPaperQueryService.getExamTaskPaperVOS(examTaskVO.getId());
        if (!CollectionUtils.isEmpty(examTaskPaperVOS)) {
            Set<Long> ruleIds = examTaskPaperVOS.stream().filter(t -> Objects.nonNull(t.getRuleId())).map(ExamPaperVO::getRuleId)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(ruleIds)) {
                Map<Long, String> ruleMap = examQuestionRuleService.listByIds(ruleIds)
                        .stream().collect(Collectors.toMap(ExamQuestionRulePO::getId, ExamQuestionRulePO::getRuleName));

                examTaskPaperVOS.forEach(t -> t.setRuleName(ruleMap.get(t.getRuleId())));
            }
            examTaskVO.setExamPapers(examTaskPaperVOS);
        }
    }
}
