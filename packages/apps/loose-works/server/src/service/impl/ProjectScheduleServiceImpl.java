package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectApplyMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectFeedbackMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectReformMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectScheduleMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectSignContractMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.LooseWorksDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleMetricDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.CheckAcceptParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.FeedbackParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectScheduleParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.AbnormalProjectReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.FeedbackDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO2;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectFeedbackVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNumStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformProcessTimeVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrackRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrendStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.CheckAcceptResultEnum;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.FeedbackTypeEnum;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectStateEnum;
import com.guhecloud.cube.srv.gen.loose.works.message.ProjectScheduleMessage;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectApplyPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectFeedbackPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectReformPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectScheduleExtendPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSignContractPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectTypePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectFeedbackService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleExtendService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectTypeService;
import com.guhecloud.cube.srv.gen.loose.works.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.client.InspectionClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.loose.works.enumeration.FeedbackTypeEnum.CHECK_ACCEPT;

/**
 * @author wql
 * @describe 项目进度
 * @date 2023/9/14 18:43
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectScheduleServiceImpl extends ServiceImpl<ProjectScheduleMapper, ProjectSchedulePO> implements ProjectScheduleService {
    private final static String PROJECT_CHECK_ACCEPT_NOTICE = "PROJECT_CHECK_ACCEPT_NOTICE";//工程验收通知
    private final static String PROJECT_SUPERVISION_NOTICE = "PROJECT_SUPERVISION_NOTICE";//工程监管通知
    public final static String SMALLENFINEERING = "SMALLENFINEERING";//应用名称

    private final ProjectFeedbackService projectFeedbackService;
    private final UserAndOrgService userAndOrgService;
    private final ProjectTypeService projectTypeService;
    private final ProjectApplyMapper projectApplyMapper;
    private final LooseWorksNumberCreaterCreater looseWorksNumberCreaterCreater;
    private final InspectionClient inspectionClient;

    private final String XS = "XS";
    private final ProjectFeedbackMapper projectFeedbackMapper;
    private final MessageSendClient messageSendClient;
    private final ApplicationClient applicationClient;
    private final ProjectScheduleExtendService projectScheduleExtendService;
    private final ProjectReformMapper projectReformMapper;
    private final FileOperateClient fileOperateClient;
    private final MsgRecordClient msgRecordClient;
    private final ProjectSignContractMapper projectSignContractMapper;
    private final FlowClient flowClient;

    @Override
    public ProjectScheduleVO projectScheduleGetById(Long id) {
        ProjectSchedulePO schedulePO = this.getById(id);
        ProjectScheduleVO projectScheduleVO = ObjectUtils.copying(schedulePO, ProjectScheduleVO.class);
        if (Objects.nonNull(schedulePO) && Objects.nonNull(schedulePO.getTypeId())) {
            ProjectTypePO projectTypePO = projectTypeService.getById(schedulePO.getTypeId());
            projectScheduleVO.setProjectType(projectTypePO != null ? projectTypePO.getId() : null);
            projectScheduleVO.setProjectTypeName(projectTypePO != null ? projectTypePO.getName() : null);
        }
        return projectScheduleVO;
    }

    @Override
    public List<ProjectScheduleVO> projectScheduleBatchGet(ProjectScheduleQueryParam param) {
        List<ProjectSchedulePO> projectSchedulePOList = this.lambdaQuery()
                .in(!CollectionUtils.isEmpty(param.getProjectIds()), ProjectSchedulePO::getId, param.getProjectIds())
                .in(!CollectionUtils.isEmpty(param.getProjectNames()), ProjectSchedulePO::getProjectName, param.getProjectNames())
                .in(!CollectionUtils.isEmpty(param.getProjectNumList()), ProjectSchedulePO::getProjectNum, param.getProjectNumList())
                .list();
        if (CollectionUtils.isEmpty(projectSchedulePOList)) {
            return Collections.emptyList();
        }
        List<Long> typeIds = projectSchedulePOList.stream().filter(t -> Objects.nonNull(t.getTypeId()))
                .map(ProjectSchedulePO::getTypeId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> projectTypeMap = getProjectTypeMap(typeIds);
        return projectSchedulePOList.stream().map(projectSchedulePO -> {
            ProjectScheduleVO projectScheduleVO = ObjectUtils.copying(projectSchedulePO, ProjectScheduleVO.class);
            projectScheduleVO.setProjectType(projectSchedulePO.getTypeId());
            projectScheduleVO.setProjectTypeName(projectTypeMap.get(projectScheduleVO.getProjectType()));
            return projectScheduleVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectScheduleIdTypeDTO> getProjectScheduleIds(ProjectScheduleQuery query) {
        var wrapper = new LambdaQueryWrapper<ProjectSchedulePO>();
        wrapper.select(ProjectSchedulePO::getId, ProjectSchedulePO::getProjectName, ProjectSchedulePO::getTypeId)
                .like(StringUtils.isNotBlank(query.getKeyword()), ProjectSchedulePO::getProjectName, query.getKeyword())
                .in(!CollectionUtils.isEmpty(query.getIdList()), ProjectSchedulePO::getId, query.getIdList());
        if (ObjectUtils.isNotNull(query.getDepId())) {
            wrapper.and(w -> w.like(ProjectSchedulePO::getSuperviseDep, "," + query.getDepId() + ",")
                    .or().likeRight(ProjectSchedulePO::getSuperviseDep, query.getDepId() + ",")
                    .or().likeLeft(ProjectSchedulePO::getSuperviseDep, "," + query.getDepId())
                    .or().eq(ProjectSchedulePO::getSuperviseDep, query.getDepId().toString()));
        }
        var list = this.baseMapper.selectList(wrapper);
        return list.stream().map(po -> new ProjectScheduleIdTypeDTO(po.getId(), po.getProjectName(), po.getTypeId(), null))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<ProjectScheduleVO> getProjectScheduleList(ProjectScheduleQuery query) {
        //责任人筛选
        if (ObjectUtils.isNotEmpty(query.getKeyword())) {
            List<UserDTO> userDTOS = userAndOrgService.getUserListByName(query.getKeyword());
            if (!CollectionUtils.isEmpty(userDTOS)) {
                List<Long> filterIdList = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList());
                query.setLeaderList(filterIdList);
            }
        }
        if (ProjectStateEnum.ABNORMAL.name().equals(query.getStatus())) {
            List<AbnormalProjectReportVO> abnormalProjects = projectFeedbackMapper.selectAbnormalProject(query.getPeriod());
            if (!CollectionUtils.isEmpty(abnormalProjects)) {
                query.setIdList(abnormalProjects.stream().map(AbnormalProjectReportVO::getId).collect(Collectors.toList()));
            }

        }
        Page<ProjectSchedulePO> page = this.baseMapper.getProjectScheduleList(PagingUtils.convert(query), query);
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> superviseDepIds = new HashSet<>();
        String userIds = "";
        Set<Long> projectTypeIds = new HashSet<>();
        for (ProjectSchedulePO record : page.getRecords()) {
            if (StringUtils.isNotBlank(record.getSuperviseDep())) {
                superviseDepIds.addAll(Arrays.stream(record.getSuperviseDep().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
            if (Objects.nonNull(record.getTypeId())) {
                projectTypeIds.add(record.getTypeId());
            }
            if (ObjectUtils.isNotEmpty(record.getSuperviseUsers())) {
                userIds = ObjectUtils.isNotEmpty(userIds) ? userIds + "," + record.getSuperviseUsers() : record.getSuperviseUsers();
            }
        }
        Map<Long, String> projectTypeMap = getProjectTypeMap(projectTypeIds);
        Map<Long, String> organizationMap = userAndOrgService.getOrgInfoByIdList(superviseDepIds)
                .stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
        Map<Long, String> userMap = userAndOrgService.getUserByIds(userIds)
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        List<ProjectScheduleVO> projectScheduleVOS = page.getRecords().stream().map(po -> {
            ProjectScheduleVO vo = ObjectUtils.copying(po, ProjectScheduleVO.class);
            String superviseUserName = "";
            if (ObjectUtils.isNotEmpty(po.getSuperviseUsers())) {
                String[] ids = po.getSuperviseUsers().split(",");
                for (String id : ids) {
                    superviseUserName += ObjectUtils.isNotEmpty(superviseUserName) ? "," + userMap.get(ConvertUtils.toLong(id))
                                                                                   : userMap.get(ConvertUtils.toLong(id));
                }
            }
            ProjectFeedbackPO projectFeedbackPO = projectFeedbackMapper.selectOne(new LambdaQueryWrapper<>(ProjectFeedbackPO.class)
                                                                                          .eq(ProjectFeedbackPO::getScheduleId, po.getId())
                                                                                          .eq(ProjectFeedbackPO::getOperationType, CHECK_ACCEPT.name())
                                                                                          .orderByDesc(ProjectFeedbackPO::getCreateTime)
                                                                                          .last("limit 1"));
            if (ObjectUtils.isNotEmpty(projectFeedbackPO)) {
                vo.setAcceptanceUserName(userAndOrgService.getUserByPhone(Long.valueOf(projectFeedbackPO.getCreateBy())).getName());
                vo.setAcceptanceTime(projectFeedbackPO.getCreateTime());
                vo.setAcceptanceResult(projectFeedbackPO.getAcceptanceResult());
            }
            vo.setSuperviseUserName(superviseUserName);
            if (StringUtils.isNotBlank(po.getSuperviseDep())) {
                String depName = Arrays.stream(po.getSuperviseDep().split(","))
                        .map(t -> organizationMap.get(Long.valueOf(t))).filter(StringUtils::isNotBlank).collect(Collectors.joining("/"));
                vo.setDepName(depName);
            }
            vo.setStatus(ProjectStateEnum.valueOf(po.getStatus()).getDescribe());
            vo.setStatusCode(po.getStatus());
            vo.setUserId(po.getSuperviseUsers());
            vo.setProjectType(po.getTypeId());
            vo.setProjectTypeName(projectTypeMap.get(po.getTypeId()));
            return vo;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, projectScheduleVOS);
    }

    @Override
    public ProjectScheduleDetailVO getProjectScheduleDetail(Long id) {
        ProjectSchedulePO schedulePO = this.getById(id);
        Assert.isNotFound(schedulePO, "工程进度信息不存在");
        List<ProjectFeedbackPO> checkAcceptPOs = projectFeedbackService.getCheckAcceptList(schedulePO.getId(), CHECK_ACCEPT.name());
        List<ProjectFeedbackPO> feedbackPOs = projectFeedbackService.getCheckAcceptList(schedulePO.getId(), FeedbackTypeEnum.FEEDBACK.name());
        ProjectTypePO projectTypePO = projectTypeService.getById(schedulePO.getTypeId());
        List<OrganizationDTO> orgInfoByIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(schedulePO.getSuperviseDep())) {
            Set<Long> depIds = Arrays.stream(schedulePO.getSuperviseDep().split(","))
                    .map(Long::valueOf).collect(Collectors.toSet());
            orgInfoByIdList = userAndOrgService.getOrgInfoByIdList(depIds);
        }
        ProjectScheduleDetailVO vo = ProjectScheduleDetailVO.builder()
                .id(schedulePO.getId())
                .projectNum(schedulePO.getProjectNum())
                .projectName(schedulePO.getProjectName())
                .status(ProjectStateEnum.valueOf(schedulePO.getStatus()).getDescribe())
                .statusCode(schedulePO.getStatus())
                .partyB(schedulePO.getPartyB())
                .overTime(schedulePO.getOverTime())
                .superviseDepList(orgInfoByIdList)
                .superviseUser(userAndOrgService.getUserByIds(schedulePO.getSuperviseUsers()))
                .projectFiles(userAndOrgService.getFileListByIds(schedulePO.getProjectFiles()))
                .projectType(projectTypePO != null ? projectTypePO.getId() : null)
                .projectTypeName(projectTypePO != null ? projectTypePO.getName() : null)
                .remarks(schedulePO.getRemarks())
                .constructionSite(schedulePO.getConstructionSite())
                .completionTime(schedulePO.getCompletionTime())
                .duration(schedulePO.getDuration())
                .build();
        if (ObjectUtils.isNotEmpty(feedbackPOs)) {
            vo.setSchedule(feedbackPOs.get(0).getSchedule());
        }
        if (ObjectUtils.isNotEmpty(checkAcceptPOs)) {
            ProjectFeedbackPO feedbackPO = checkAcceptPOs.get(0);
            vo.setCheckAcceptResult(feedbackPO.getAcceptanceResult());
            vo.setCheckAcceptRemark(feedbackPO.getDescription());
            vo.setCheckFiles(userAndOrgService.getFileListByIds(feedbackPO.getFiles()));
        }
        projectScheduleExtendService.extendInfo(vo);
        return vo;
    }

    @Override
    public List<TrackRecordVO> getTrackDownList(Long id) {
        List<ProjectFeedbackPO> feedbackList = projectFeedbackService.getFeedbackList(id, FeedbackTypeEnum.FEEDBACK.name());
        List<TrackRecordVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(feedbackList)) {
            feedbackList.addAll(projectFeedbackService.getFeedbackList(id, CHECK_ACCEPT.name()));
            feedbackList.forEach(po -> {
                UserDTO user = userAndOrgService.getUserByPhone(Long.parseLong(po.getCreateBy()));
                String feedbackContent = ObjectUtils.isNotEmpty(po.getSchedule()) ? po.getSchedule().toString() : "";
                vos.add(TrackRecordVO.builder()
                                .id(po.getId())
                                .recordTime(po.getCreateTime())
                                .username(user.getName())
                                .depName(user.getOrganization() == null ? null : user.getOrganization().getName())
                                .operation(FeedbackTypeEnum.valueOf(po.getOperationType()).getDescribe())
                                .operationCode(FeedbackTypeEnum.valueOf(po.getOperationType()).name())
                                .feedbackContent(feedbackContent)
                                .acceptanceResult(po.getAcceptanceResult())
                                .checkAcceptContent(StringUtils.isNotBlank(po.getAcceptanceResult()) ? CheckAcceptResultEnum
                                        .valueOf(po.getAcceptanceResult()).getDescribe() : "")
                                .build());
            });
        }
        return vos;
    }

    @Override
    public FeedbackDetailVO getTrackDownDetail(Long id) {
        ProjectFeedbackVO po = projectFeedbackService.getFeedBackById(id);
        return FeedbackDetailVO.builder()
                .schedule(po.getSchedule())
                .checkAcceptResult(po.getAcceptanceResult())
                .hasProblem(po.getHasProblem())
                .operationType(po.getOperationType())
                .describe(po.getDescription())
                .files(userAndOrgService.getFileListByIds(po.getFiles()))
                .build();
    }

    @Override
    public ProjectScheduleStatisticsVO scheduleStatistics(ProjectStatisticsQuery query) {
        List<Long> filterIdList;
        if (ObjectUtils.isNotEmpty(query.getKeyword())) {
            List<UserDTO> userDTOS = userAndOrgService.getUserListByName(query.getKeyword());
            if (!CollectionUtils.isEmpty(userDTOS)) {
                filterIdList = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList());
            } else {
                filterIdList = new ArrayList<>();
            }
        } else {
            filterIdList = new ArrayList<>();
        }
        List<ProjectSchedulePO> list = this.lambdaQuery().between(ObjectUtils.isNotNull(query.getPeriod()),
                                                                  ProjectSchedulePO::getCreateTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                                                                  ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .and(StringUtils.isNotBlank(query.getKeyword()), ex -> {
                    ex.or().like(ProjectSchedulePO::getProjectName, query.getKeyword());
                    ex.or().like(ProjectSchedulePO::getPartyB, query.getKeyword());
                    ex.or(!CollectionUtils.isEmpty(filterIdList), t -> {
                        for (Long aLong : filterIdList) {
                            t.or(q -> {
                                q.like(ProjectSchedulePO::getSuperviseUsers, aLong);
                            });
                        }
                    });
                })
                .eq(ObjectUtils.isNotNull(query.getStartUser()), ProjectSchedulePO::getStartUser, query.getStartUser())
                .select(ProjectSchedulePO::getId, ProjectSchedulePO::getStatus).list();
        Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(ProjectSchedulePO::getStatus, Collectors.counting()));
        return ProjectScheduleStatisticsVO.builder()
                .completed(collect.get(ProjectStateEnum.COMPLETED.name()))
                .waitAccept(collect.get(ProjectStateEnum.WAIT_ACCEPT.name()))
                .underway(collect.get(ProjectStateEnum.UNDERWAY.name()))
                .build();
    }

    @Override
    public List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailList(Long id, String feedbackType) {
        List<ProjectSchedulePO> list = this.lambdaQuery().eq(ProjectSchedulePO::getApplyId, id).select(ProjectSchedulePO::getId).list();
        if (ObjectUtils.isNotEmpty(list)) {
            Long scheduleId = list.get(0).getId();
            return projectFeedbackService.getFeedbackDetailList(scheduleId, feedbackType);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void projectFeedback(FeedbackParam feedbackParam) {
        ProjectFeedbackPO po = new ProjectFeedbackPO();
        po.setId(IdWorker.getId());
        ObjectUtils.copying(feedbackParam, po);
        po.setFiles(StringUtils.join(",", feedbackParam.getFileIds()));
        po.setProblemFiles(StringUtils.join(",", feedbackParam.getProblemFileIds()));
        po.setOperationType(FeedbackTypeEnum.FEEDBACK.name());
        projectFeedbackService.saveFeedback(po);
        //更新工程进度
        ProjectSchedulePO schedulePO = this.getById(feedbackParam.getScheduleId());
        int schedule = Integer.parseInt(feedbackParam.getSchedule());
        schedulePO.setSchedule(schedule);
        if (schedule == 100) {
            //完成进度到100后,将进度中的工程状态更新为待验收
            schedulePO.setStatus(ProjectStateEnum.WAIT_ACCEPT.name());
        }
        this.updateById(schedulePO);
        if (schedule == 100) {
            //通知巡更计划结束
            RocketMQContextHolder.send(ProjectScheduleMessage.TOPIC, ProjectScheduleMessage.builder()
                    .projectId(schedulePO.getId())
                    .projectName(schedulePO.getProjectName())
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void projectCheckAccept(CheckAcceptParam checkAcceptParam) {
        ProjectFeedbackPO po = new ProjectFeedbackPO();
        po.setId(IdWorker.getId());
        ObjectUtils.copying(checkAcceptParam, po);
        po.setFiles(StringUtils.join(",", checkAcceptParam.getFileIds()));
        po.setOperationType(CHECK_ACCEPT.name());
        projectFeedbackService.saveFeedback(po);
        String result = checkAcceptParam.getAcceptanceResult();
        ProjectSchedulePO schedulePO = this.getById(checkAcceptParam.getScheduleId());
        if (CheckAcceptResultEnum.QUALIFIED.name().equals(result)) {
            //验收完成后将进度中的工程状态更新为已完成
            schedulePO.setStatus(ProjectStateEnum.COMPLETED.name());
            schedulePO.setOverTime(DateUtils.now());
            this.updateById(schedulePO);
        }
        //发送验收消息
        Map<String, String> tmpParams = new HashMap<>();
        tmpParams.put("projectName", schedulePO.getProjectName());
        tmpParams.put("acceptResult", CheckAcceptResultEnum.valueOf(result).getDescribe());
        this.sendMsg(schedulePO, PROJECT_CHECK_ACCEPT_NOTICE, tmpParams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScheduleInfo(ProjectSchedulePO build) {
        this.save(build);
    }

    @Override
    public LooseWorksDTO looseWorksDataCollect(Period period, Period reformPeriod) {
        LooseWorksDTO looseWorksDTO = new LooseWorksDTO();
        List<ProjectSchedulePO> list = this.baseMapper.selectAll(period, SessionContextHolder.getRequester().getTenant());
        List<ProjectScheduleDTO> dtoList = new ArrayList<>(list.size());
        if (ObjectUtils.isNotEmpty(list)) {
            Set<Long> depIds = list.stream().filter(t -> StringUtils.isNotBlank(t.getSuperviseDep()))
                    .map(t -> Arrays.stream(t.getSuperviseDep().split(","))
                            .collect(Collectors.toSet()))
                    .flatMap(Collection::stream)
                    .map(Long::valueOf).collect(Collectors.toSet());
            Map<Long, String> organizationMap = userAndOrgService.getOrgInfoByIdList(depIds)
                    .stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
            list.forEach(po -> {
                boolean hasProblem = false;
                List<ProjectFeedbackPO> feedbackList = projectFeedbackService.getFeedbackList(po.getId(), CHECK_ACCEPT.name());
                if (ObjectUtils.isNotEmpty(feedbackList)) {
                    long count = feedbackList.stream()
                            .filter(t -> CheckAcceptResultEnum.DISQUALIFICATION.name().equals(t.getAcceptanceResult())).count();
                    hasProblem = count > 0;
                }
                ProjectScheduleDTO worksDTO = new ProjectScheduleDTO();
                ObjectUtils.copying(po, worksDTO);
                worksDTO.setHasProblem(hasProblem);
                if (StringUtils.isNotBlank(po.getSuperviseDep())) {
                    String depName = Arrays.stream(po.getSuperviseDep().split(","))
                            .map(t -> organizationMap.get(Long.valueOf(t))).filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining("/"));
                    worksDTO.setSuperviseDepName(depName);
                }
                dtoList.add(worksDTO);
            });
            looseWorksDTO.setProjectScheduleList(dtoList);
        }
        List<ProjectReformPO> reformPOList = projectReformMapper.selectAll(reformPeriod, SessionContextHolder.getRequester().getTenant());
        if (!CollectionUtils.isEmpty(reformPOList)) {
            looseWorksDTO.setProjectReformList(reformPOList);
        }
        return looseWorksDTO;
    }

    @Override
    public ProjectSchedulePO getProjectScheduleByApplyId(Long id) {
        List<ProjectSchedulePO> list = this.lambdaQuery().eq(ProjectSchedulePO::getApplyId, id).list();
        if (ObjectUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ProjectNumStatisticsVO projectNumStatistics(Period period) {
        Long total = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();
        Long abnormal = projectFeedbackService.abnormalProjectNum(period);
        return ProjectNumStatisticsVO.builder().total(total).abnormal(abnormal).build();
    }

    @Override
    public List<ProjectTrendStatisticsVO> projectTrendStatistics() {
        LocalDateTime begin = DateUtils.beginOfYear(DateUtils.now());
        int month = (int) DateUtils.betweenMonth(begin, DateUtils.now(), true);
        List<ProjectTrendStatisticsVO> vos = new ArrayList<>();
        for (int i = 0; i <= month; i++) {
            Long count = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, begin, DateUtils.endOfMonth(begin)).count();
            vos.add(ProjectTrendStatisticsVO.builder()
                            .month(i + 1)
                            .num(count)
                            .build());
            begin = DateUtils.offsetMonth(begin, 1);
        }
        return vos;
    }

    @Override
    public Float projectAbnormalRatio(Period period) {
        ProjectNumStatisticsVO statisticsVO = projectNumStatistics(period);
        return MathUtils.calculateProportion(statisticsVO.getTotal(), statisticsVO.getAbnormal()).floatValue();
    }

    @Override
    public ProjectDataForReportVO projectDataForReport(Period period) {
        Long total = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();
        //环比
        LocalDateTime endTime = DateUtils.offsetMonth(period.getEnd(), -1);
        Period previousPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -1), DateUtils.endOfDay(endTime));
        Long ringTotal = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, previousPeriod.getBegin(), previousPeriod.getEnd())
                .count();
        //同比
        Period samePeriod = new Period(DateUtils.offset(period.getBegin(), ChronoUnit.YEARS, -1),
                                       DateUtils.offset(period.getEnd(), ChronoUnit.YEARS, -1));
        Long sameTotal = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, samePeriod.getBegin(), samePeriod.getEnd())
                .count();
        List<AbnormalProjectReportVO> projectScheduleReportVOS = projectFeedbackService.selectAbnormalProject(period);
        if (!CollectionUtils.isEmpty(projectScheduleReportVOS)) {
            List<Long> deparmentIdList = projectScheduleReportVOS.stream().filter(t -> Objects.nonNull(t.getSuperviseDep()))
                    .map(AbnormalProjectReportVO::getSuperviseDep).distinct().collect(Collectors.toList());
            List<OrganizationDTO> orgInfoByIdList = userAndOrgService.getOrgInfoByIdList(deparmentIdList);
            Map<Long, String> deparmentMap = new HashMap<>(orgInfoByIdList.size());
            if (!CollectionUtils.isEmpty(orgInfoByIdList)) {
                deparmentMap = orgInfoByIdList.stream()
                        .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName, (k1, k2) -> k1));
            }
            for (AbnormalProjectReportVO projectScheduleReportVO : projectScheduleReportVOS) {
                projectScheduleReportVO.setSuperviseDepName(deparmentMap.get(projectScheduleReportVO.getSuperviseDep()));
            }
            for (int i = 0; i < projectScheduleReportVOS.size(); i++) {
                projectScheduleReportVOS.get(i).setIndex(i + 1L);
            }
        } else {
            projectScheduleReportVOS = new ArrayList<>();
        }
        return ProjectDataForReportVO.builder()
                .nowTotal(Long.valueOf(total))
                .ringTotal(Long.valueOf(ringTotal))
                .sameTotal(Long.valueOf(sameTotal))
                .abnormalProjectReportVOS(projectScheduleReportVOS)
                .build();
    }

    @Override
    public ProjectDataForAnalysisVO projectDataForAbnormalSafetyAnalysis(Period period, String type) {
        //总数
        Long total = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();
        //异常数
        Long abnormal = projectFeedbackService.abnormalProjectNum(period);
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(type, period);
        Long ringAbnormal = projectFeedbackService.abnormalProjectNum(previousPeriod);
        return ProjectDataForAnalysisVO.builder()
                .totalNum(Long.valueOf(abnormal))
                .ratio(MathUtils.calculateProportion(abnormal, total).floatValue())
                .chainRatio(MathUtils.calculateRingRatio(abnormal, ringAbnormal).floatValue())
                .build();
    }

    @Override
    public ProjectDataForAnalysisVO2 projectDataForSafetyAnalysis(Period period, String type) {

        Period previousPeriod = DateUnitUtils.getPreviousPeriod(type, period);
        //总数
        Long total = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();
        Long ringTotal = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, previousPeriod.getBegin(), previousPeriod.getEnd())
                .count();

        //进行中的个数
        Long underwayNum = this.lambdaQuery()
                .between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd())
                .eq(ProjectSchedulePO::getStatus, ProjectStateEnum.UNDERWAY.name())
                .count();
        Long underwayRingNum = this.lambdaQuery()
                .between(ProjectSchedulePO::getCreateTime, previousPeriod.getBegin(), previousPeriod.getEnd())
                .eq(ProjectSchedulePO::getStatus, ProjectStateEnum.UNDERWAY.name())
                .count();
        //异常数
        Long abnormal = projectFeedbackService.abnormalProjectNum(period);
        Long abnormalRingNum = projectFeedbackService.abnormalProjectNum(previousPeriod);

        return ProjectDataForAnalysisVO2.builder()
                .total(total)
                .totalRingRatio(MathUtils.calculateRingRatio(total, ringTotal).floatValue())
                .abnormal(abnormal)
                .abnormalRingRatio(MathUtils.calculateRingRatio(abnormal, abnormalRingNum).floatValue())
                .underway(underwayNum)
                .underwayRingRatio(MathUtils.calculateRingRatio(underwayNum, underwayRingNum).floatValue())
                .build();
    }

    @Override
    public List<ProjectTrendStatisticsVO> projectTrendForSafetyAnalysis(TrendStatisticsQueryParam param) {

        var dateField = DateUnitUtils.handleDateParam(param);
        List<ProjectTrendStatisticsVO> projectTrendStatistics = this.baseMapper.selectProjectTrendStatistics(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        if (startDate != null && endDate != null) {
            endDate = DateUtils.endOfDay(endDate);
            List<ProjectTrendStatisticsVO> finalDatas = new ArrayList<>();
            for (LocalDateTime start = startDate;
                 start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
                String date = DateUtils.format(start, param.getDateString());
                String viewDate = DateUtils.format(start, param.getViewDateString());
                ProjectTrendStatisticsVO trendStatVO = projectTrendStatistics.stream().filter(t -> t.getPeriod().equals(date)).findAny()
                        .orElse(null);
                finalDatas.add(trendStatVO == null ? new ProjectTrendStatisticsVO(null, 0L, viewDate)
                                                   : new ProjectTrendStatisticsVO(null, trendStatVO.getNum(), viewDate));
            }
            return finalDatas;
        } else {
            return projectTrendStatistics;
        }
    }

    @Override
    public ProjectStatisticsVO homepageStatistics(TrendStatisticsQueryParam param) {

        ProjectStatisticsQuery query = new ProjectStatisticsQuery();
        query.setPeriod(param.getPeriod());
        ProjectScheduleStatisticsVO vo = this.scheduleStatistics(query);
        Period previousPeriod =
                Objects.nonNull(param.getPeriod()) ? DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod()) : null;
        //总数
        long total = 0L;
        List<Long> targetIdList = new ArrayList<>();
        List<ProjectSchedulePO> list = this.lambdaQuery().between(ObjectUtils.isNotNull(query.getPeriod()),
                                                                  ProjectSchedulePO::getCreateTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                                                                  ObjectUtils.ifNull(query.getPeriod(), Period::getEnd)).list();
        if (!CollectionUtils.isEmpty(list)) {
            total = list.size();
            targetIdList = list.stream().map(ProjectSchedulePO::getId).collect(Collectors.toList());
        }
        Long ringTotal = 0L;
        if (Objects.nonNull(previousPeriod)) {
            ringTotal = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, previousPeriod.getBegin(), previousPeriod.getEnd())
                    .count();
        }
        Long abnormal = inspectionClient.getErrNumByTargets(targetIdList);
        return ProjectStatisticsVO.builder()
                .total(total)
                .ringRatio(MathUtils.calculateRingRatio(total, ringTotal).floatValue())
                .normal(total - abnormal)
                .abnormal(abnormal)
                .underway(vo.getUnderway())
                .waitAccept(vo.getWaitAccept())
                .completed(vo.getCompleted())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveProjectSchedule(ProjectScheduleParam param) {
        ProjectSchedulePO schedulePO = ConvertUtils.convert(ProjectSchedulePO.class, param);
        schedulePO.setId(IdWorker.getId());
        schedulePO.setStatus(ProjectStateEnum.UNDERWAY.name());
        schedulePO.setProjectNum(looseWorksNumberCreaterCreater.create(XS));
        schedulePO.setStartUser(SecurityContextHolder.getPrincipal().getId());
        ProjectApplyPO apply = new ProjectApplyPO();
        apply.setProjectName(param.getProjectName());
        apply.setApplyNum(schedulePO.getProjectNum());
        projectApplyMapper.insert(apply);
        schedulePO.setApplyId(apply.getId());
        boolean save = super.save(schedulePO);
        //保存或者信息
        projectScheduleExtendService.saveOrUpdateProjectScheduleExtend(schedulePO.getId(), param);
        //文件持久化
        if (StringUtils.isNotBlank(param.getProjectFiles())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setBusinessKey(schedulePO.getId());
            fileInduceParam.setFileIdStr(param.getProjectFiles());
            fileInduceParam.setSceneCode(SMALLENFINEERING);
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        //发送消息通知
        Map<String, String> tmpParams = new HashMap<>();
        tmpParams.put("projectName", param.getProjectName());
        this.sendMsg(schedulePO, PROJECT_SUPERVISION_NOTICE, tmpParams);
        return schedulePO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editProjectSchedule(ProjectScheduleParam param) {
        Assert.isParameterError(param.getId() == null, "主键不能为空");
        ProjectSchedulePO schedulePO = this.getById(param.getId());
        if (StringUtils.isNotBlank(schedulePO.getProjectFiles()) &&
            !StringUtils.equals(schedulePO.getProjectFiles(), param.getProjectFiles())) {
            String projectFiles = schedulePO.getProjectFiles();
            if (StringUtils.isNotBlank(param.getProjectFiles())) {
                List<String> parmFileIds = Arrays.stream(param.getProjectFiles().split(",")).collect(Collectors.toList());
                projectFiles = Arrays.stream(projectFiles.split(",")).filter(t -> !parmFileIds.contains(t))
                        .collect(Collectors.joining(","));
            }
            if (StringUtils.isNotBlank(projectFiles)) {
                FileParam fileParam = new FileParam();
                fileParam.setFileIdStr(projectFiles);
                fileOperateClient.physicalDeleteByIds(fileParam);
            }
        }
        schedulePO.setProjectName(param.getProjectName());
        schedulePO.setTypeId(param.getTypeId());
        schedulePO.setCompletionTime(param.getCompletionTime());
        schedulePO.setDuration(param.getDuration());
        schedulePO.setConstructionSite(param.getConstructionSite());
        schedulePO.setPartyB(param.getPartyB());
        schedulePO.setSuperviseUsers(param.getSuperviseUsers());
        schedulePO.setSuperviseUserName(param.getSuperviseUserName());
        schedulePO.setSuperviseDep(param.getSuperviseDep());
        schedulePO.setRemarks(param.getRemarks());
        schedulePO.setProjectFiles(param.getProjectFiles());
        schedulePO.setUpdateTime(DateUtils.now());
        projectScheduleExtendService.saveOrUpdateProjectScheduleExtend(schedulePO.getId(), param);
        //文件持久化
        if (StringUtils.isNotBlank(param.getProjectFiles())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setBusinessKey(schedulePO.getId());
            fileInduceParam.setFileIdStr(param.getProjectFiles());
            fileInduceParam.setSceneCode(SMALLENFINEERING);
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        return super.updateById(schedulePO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProjectSchedule(Set<Long> idList) {
        List<ProjectSchedulePO> projectSchedulePOS = this.lambdaQuery().in(ProjectSchedulePO::getId, idList).list();
        if (CollectionUtils.isEmpty(projectSchedulePOS)) {
            return false;
        }
        Set<Long> applyIds = new HashSet<>();
        Set<Long> contractIds = new HashSet<>();
        for (ProjectSchedulePO projectSchedulePO : projectSchedulePOS) {
            if (Objects.nonNull(projectSchedulePO.getApplyId())) {
                applyIds.add(projectSchedulePO.getApplyId());
            }
            if (Objects.nonNull(projectSchedulePO.getContractId())) {
                contractIds.add(projectSchedulePO.getContractId());
            }
        }
        //逻辑删除业务数据
        DatabaseContextHolder.softDelete(ProjectSchedulePO.class, idList);
        DatabaseContextHolder.softDelete(ProjectScheduleExtendPO.class, idList);
        //删除反馈信息
        List<ProjectFeedbackPO> projectFeedbackPOS = projectFeedbackService.lambdaQuery().in(ProjectFeedbackPO::getScheduleId, idList)
                .list();
        if (!CollectionUtils.isEmpty(projectFeedbackPOS)) {
            DatabaseContextHolder.softDelete(ProjectFeedbackPO.class, projectFeedbackPOS);
        }
        //删除关联审批工单
        Set<String> processInsIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(applyIds)) {
            List<ProjectApplyPO> projectApplyPOS = projectApplyMapper.selectList(Wrappers.<ProjectApplyPO>lambdaQuery()
                                                                                         .in(ProjectApplyPO::getId, applyIds));
            if (!CollectionUtils.isEmpty(projectApplyPOS)) {
                DatabaseContextHolder.softDelete(ProjectApplyPO.class, projectApplyPOS);
                projectApplyPOS.stream().filter(t -> StringUtils.isNotBlank(t.getProcessInsId()))
                        .forEach(t -> processInsIds.add(t.getProcessInsId()));

            }
        }
        if (!CollectionUtils.isEmpty(contractIds)) {
            List<ProjectSignContractPO> projectContractPOS = projectSignContractMapper.selectList(Wrappers
                                                                                                          .<ProjectSignContractPO>lambdaQuery()
                                                                                                          .in(ProjectSignContractPO::getId, contractIds));
            if (!CollectionUtils.isEmpty(projectContractPOS)) {
                DatabaseContextHolder.softDelete(ProjectSignContractPO.class, projectContractPOS);
                projectContractPOS.stream().filter(t -> StringUtils.isNotBlank(t.getProcessInsId()))
                        .forEach(t -> processInsIds.add(t.getProcessInsId()));
            }
        }
        if (!CollectionUtils.isEmpty(processInsIds)) {
            flowClient.deleteFormFlow(processInsIds);
        }
        //删除消息
        msgRecordClient.removeBatchByBusinessIds(idList.stream().map(String::valueOf).collect(Collectors.toSet()));
        return true;
    }

    @Override
    public ProjectScheduleMetricDTO projectScheduleMetric(Period period, MetricType metricType) {
        //总数
        Long totalNum = this.lambdaQuery().between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();
        //整改数
        Long reformNumber = projectReformMapper.selectCount(Wrappers.<ProjectReformPO>lambdaQuery()
                                                                    .between(ProjectReformPO::getCreateTime, period.getBegin(), period.getEnd())
                                                           );
        switch (metricType) {
            case TOTAL_COUNT:
                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .number(totalNum)
                        .rate(0f)
                        .build();
            case COMPLETED_RATE:
                //完成的个数
                Long completeNum = this.lambdaQuery()
                        .between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd())
                        .eq(ProjectSchedulePO::getStatus, ProjectStateEnum.COMPLETED.name())
                        .count();
                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .number(completeNum)
                        .rate(MathUtils.calculateProportion(totalNum, completeNum).floatValue())
                        .build();
            case ABNORMAL_RATE:
                //异常数
                Long abnormal = projectFeedbackService.abnormalProjectNum(period);
                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .number(abnormal)
                        .rate(MathUtils.calculateProportion(totalNum, abnormal).floatValue())
                        .build();
            case REFORM_RATE:

                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .number(reformNumber)
                        .rate(MathUtils.calculateProportion(totalNum, reformNumber).floatValue())
                        .build();
            case REFORM_EFFICIENCY:
                //整改完成数
                ProjectReformProcessTimeVO projectReformProcessTimeVO = projectReformMapper.selectAvgProcessTime(period);
                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .avgTime(Objects.nonNull(projectReformProcessTimeVO) ? projectReformProcessTimeVO.getAvgProcessSeconds() : null)
                        .build();
            case ACCEPTANCE_RATE:
                List<ProjectFeedbackPO> projectFeedbackPOS = projectFeedbackMapper.selectList(Wrappers.<ProjectFeedbackPO>lambdaQuery()
                                                                                                      .eq(ProjectFeedbackPO::getOperationType, CHECK_ACCEPT.name())
                                                                                                      .select(ProjectFeedbackPO::getScheduleId));
                long acceptNum = 0L;
                if (!CollectionUtils.isEmpty(projectFeedbackPOS)) {
                    List<Long> scheduleIds = projectFeedbackPOS.stream().map(ProjectFeedbackPO::getScheduleId).distinct()
                            .collect(Collectors.toList());
                    acceptNum = this.lambdaQuery()
                            .in(ProjectSchedulePO::getId, scheduleIds)
                            .between(ProjectSchedulePO::getCreateTime, period.getBegin(), period.getEnd()).count();

                }
                return ProjectScheduleMetricDTO.builder()
                        .totalNumber(totalNum.intValue())
                        .number(acceptNum)
                        .rate(MathUtils.calculateProportion(totalNum, acceptNum).floatValue())
                        .build();
            default:
                return ProjectScheduleMetricDTO.builder()
                        .number(0L)
                        .totalNumber(0)
                        .rate(0f)
                        .build();
        }
    }

    private Map<Long, String> getProjectTypeMap(Collection<Long> typeIds) {
        if (CollectionUtils.isEmpty(typeIds)) {
            return new HashMap<>();
        }
        Map<Long, String> projectTypeMap = Optional.ofNullable(projectTypeService.lambdaQuery().in(ProjectTypePO::getId, typeIds).list())
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(ProjectTypePO::getId, ProjectTypePO::getName));
        return projectTypeMap;
    }

    /**
     * 消息发送
     *
     * @param projectSchedulePO
     * @param msgSceneCode
     * @param tmpParams
     */
    private void sendMsg(ProjectSchedulePO projectSchedulePO, String msgSceneCode, Map<String, String> tmpParams) {

        ApplicationDTO application = applicationClient.getApplicationByCode(SMALLENFINEERING);
        //发送人
        List<MessageNotifyTarget> messageNotifyTargets = new ArrayList<>();
        //工程对接人
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isNotBlank(projectSchedulePO.getSuperviseUsers())) {
            userIds.addAll(Arrays.stream(projectSchedulePO.getSuperviseUsers().split(","))
                                   .map(Long::valueOf)
                                   .collect(Collectors.toSet()));

        }
        //安全负责人
        ProjectScheduleExtendPO scheduleExtend = projectScheduleExtendService.lambdaQuery()
                .eq(ProjectScheduleExtendPO::getId, projectSchedulePO.getId())
                .last("limit 1")
                .one();
        if (Objects.nonNull(scheduleExtend) && StringUtils.isNotBlank(scheduleExtend.getSafetyDockUserId())) {
            userIds.addAll(Arrays.stream(scheduleExtend.getSafetyDockUserId().split(","))
                                   .map(Long::valueOf)
                                   .collect(Collectors.toSet()));
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            MessageNotifyTarget messageNotifyTarget = new MessageNotifyTarget();
            messageNotifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
            messageNotifyTarget.setIds(userIds);
            messageNotifyTargets.add(messageNotifyTarget);
        }
        //监管部门
        if (StringUtils.isNotBlank(projectSchedulePO.getSuperviseDep())) {
            MessageNotifyTarget messageNotifyTarget = new MessageNotifyTarget();
            messageNotifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.DEPARTMENT);
            messageNotifyTarget.setIds(Arrays.stream(projectSchedulePO.getSuperviseDep().split(","))
                                               .map(Long::valueOf)
                                               .collect(Collectors.toSet()));
            messageNotifyTargets.add(messageNotifyTarget);
        }
        if (CollectionUtils.isEmpty(messageNotifyTargets)) {
            return;
        }
        MessageSpecifyTmpSendRequest message = MessageSpecifyTmpSendRequest.builder()
                .tenant(SessionContextHolder.getRequester()
                                .getTenant())
                .applicationCode(application == null ? ""
                                                     : application.getCode())
                .applicationName(application == null ? ""
                                                     : application.getName())
                .msgSceneCode(msgSceneCode)
                .tmpParams(tmpParams)
                .messageNotifyTargets(messageNotifyTargets)
                .build();
        MessageSendResponse messageSendResponse = null;
        try {
            messageSendResponse = messageSendClient.sceneSpecifyTmpSend(message);
            log.info("小散工程消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("小散工程消息发送失败:{}", e.getMessage());
        }
    }
}
