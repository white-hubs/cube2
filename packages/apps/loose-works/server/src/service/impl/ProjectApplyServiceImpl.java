package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.dto.StartUserInfo;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectApplyMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectFeedbackMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectApplyDetail;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNodeInfo;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.projectNameListVO;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.FeedbackTypeEnum;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectApprovalBizEnum;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectStateEnum;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectApplyPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectApplyService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectSignContractService;
import com.guhecloud.cube.srv.gen.loose.works.util.BizDateCodeUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 工程申请
 * @date 2023/9/14 18:39
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectApplyServiceImpl extends ServiceImpl<ProjectApplyMapper, ProjectApplyPO> implements ProjectApplyService {
    private final ProjectSignContractService projectSignContractService;
    private final ProjectScheduleService projectScheduleService;
    private final UserAndOrgService userAndOrgService;
    private final ProcessInstanceClient processInstanceClient;
    private final ProjectFeedbackMapper projectFeedbackMapper;
    private final FlowClient flowClient;

    @Override
    public ProjectStatisticsVO projectStatistics(ProjectStatisticsQuery query) {
        Set<Long> applyIdList = new HashSet<>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            applyIdList = this.getFilterApplyIds(query.getKeyword());
            if (CollectionUtils.isEmpty(applyIdList)) {
                return ProjectStatisticsVO.builder()
                        .total(0L)
                        .apply(0L)
                        .signing(0L)
                        .underway(0L)
                        .waitAccept(0L)
                        .completed(0L)
                        .abnormal(0L)
                        .build();
            }
        }

        Long total = this.lambdaQuery().between(ObjectUtils.isNotNull(query.getPeriod()), ProjectApplyPO::getCreateTime,
                                                ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                                                ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .in(!CollectionUtils.isEmpty(applyIdList), ProjectApplyPO::getId, applyIdList)
                .eq(ObjectUtils.isNotNull(query.getStartUser()), ProjectApplyPO::getStartUser, query.getStartUser()).count();
        ProcessInstancePageQuery pageQuery = new ProcessInstancePageQuery();
        pageQuery.setBusinessType(ProjectApprovalBizEnum.PROJECT_APPLY.name());
        pageQuery.setSubmitTime(query.getPeriod());
        pageQuery.setStartUserId(query.getStartUser());
        pageQuery.setStatus("ACTIVE");
        Pagination<ProcessInstanceDTO> applyPage = processInstanceClient.page(pageQuery);
        pageQuery.setBusinessType(ProjectApprovalBizEnum.PROJECT_SIGN_CONTRACT.name());
        Pagination<ProcessInstanceDTO> signPage = processInstanceClient.page(pageQuery);
        ProjectScheduleStatisticsVO vo = projectScheduleService.scheduleStatistics(query);
        return ProjectStatisticsVO.builder()
                .total(total)
                .apply(ObjectUtils.isNull(applyPage) ? 0 : applyPage.getTotal())
                .signing(ObjectUtils.isNull(signPage) ? 0 : signPage.getTotal())
                .underway(vo.getUnderway() == null ? 0 : vo.getUnderway())
                .waitAccept(vo.getWaitAccept() == null ? 0 : vo.getWaitAccept())
                .completed(vo.getCompleted() == null ? 0 : vo.getCompleted())
                .abnormal(projectFeedbackMapper.abnormalProjectNum(query.getPeriod()))
                .build();
    }

    @Override
    public ProjectDetailVO projectDetail(Long id) {
        ProjectDetailVO vo = new ProjectDetailVO();
        ProjectApplyPO applyPO = this.getById(id);
        if (ObjectUtils.isNotEmpty(applyPO)) {
            vo.setProjectName(applyPO.getProjectName());
            vo.setApply(ProjectDetailVO.ApplyDetail.builder()
                                .username(userAndOrgService.getUserById(applyPO.getStartUser()).getName())
                                .depName(userAndOrgService.getOrgInfoById(applyPO.getStartDep()).getName())
                                .passTime(applyPO.getPassTime())
                                .orderNumber(applyPO.getApplyNum())
                                .processInsId(applyPO.getProcessInsId())
                                .build());
        }
        ProjectSchedulePO projectSchedulePO = projectScheduleService.getProjectScheduleByApplyId(id);
        if (ObjectUtils.isNotEmpty(projectSchedulePO)) {
            vo.setScheduleId(projectSchedulePO.getId());
        }
        ProjectDetailVO.ApplyDetail contractDetail = projectSignContractService.getContractByApply(id);
        if (ObjectUtils.isNotNull(contractDetail)) {
            vo.setSigning(contractDetail);
        }
        List<ProjectDetailVO.FeedbackDetail> feedbackList = projectScheduleService.getFeedbackDetailList(id, FeedbackTypeEnum.FEEDBACK.name());
        if (ObjectUtils.isNotEmpty(feedbackList)) {
            vo.setFeedbackDetailList(feedbackList);
        }
        List<ProjectDetailVO.FeedbackDetail> checkAcceptList = projectScheduleService.getFeedbackDetailList(id, FeedbackTypeEnum.CHECK_ACCEPT.name());
        if (ObjectUtils.isNotEmpty(checkAcceptList)) {
            vo.setCheckAccept(checkAcceptList.getFirst());
        }
        return vo;
    }

    @Override
    public Pagination<ProjectApplyListVO> projectApplyList(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        List<ProjectApplyListVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page)) {
            page.getRecords().forEach(record -> {
                ProjectApplyListVO vo = new ProjectApplyListVO();
                ObjectUtils.copying(record, vo);
                List<ProjectApplyPO> list = this.lambdaQuery().eq(ProjectApplyPO::getProcessInsId, record.getProcessInstanceId()).list();
                if (ObjectUtils.isNotEmpty(list)) {
                    ProjectApplyPO applyPO = list.getFirst();
                    vo.setApplyNum(applyPO.getApplyNum());
                    vo.setProjectName(applyPO.getProjectName());
                } else {
                    //存在流引擎有而小散这边没有的数据需要向小散工程中补数据
                    ProcessStartedMessageAttrCommand message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    ProjectApplyPO applyPO = addProjectApply(message);
                    vo.setApplyNum(applyPO.getApplyNum());
                    vo.setProjectName(applyPO.getProjectName());
                }
                StartUserInfo startUserInfo = vo.getStartUserInfo();
                if (StringUtils.isBlank(startUserInfo.getOrganizationName())) {
                    startUserInfo.setOrganizationName(userAndOrgService.getOrgInfoById(startUserInfo.getOrganizationId()).getName());
                }
                vos.add(vo);
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public synchronized ProjectApplyPO addProjectApply(ProcessStartedMessage message) {
        List<ProjectApplyPO> list = this.lambdaQuery().eq(ProjectApplyPO::getProcessInsId, message.getProcessInstanceId()).list();
        ProjectApplyPO build = new ProjectApplyPO();
        if (ObjectUtils.isEmpty(list)) {
            Long startUserId = message.getStartUserId();
            ProjectApplyDetail projectApply = JsonUtils.deserialize(message.getData(), ProjectApplyDetail.class);
            UserDTO.Organization organization = userAndOrgService.getUserById(message.getStartUserId()).getOrganization();
            build = ProjectApplyPO.builder()
                    .projectName(projectApply.getProjectName())
                    .processInsId(message.getProcessInstanceId())
                    .startUser(startUserId)
                    .startDep(organization == null ? null : organization.getId())
                    .applyNum(getApplyNum())
                    .build();
            this.save(build);
        }
        return build;
    }

    @Override
    public void approveCompleted(ProcessCompletedMessage message) {
        List<ProjectApplyPO> list = this.lambdaQuery().eq(ProjectApplyPO::getProcessInsId, message.getProcessInstanceId()).list();
        if (ObjectUtils.isNotEmpty(list)) {
            ProjectApplyPO applyPO = list.get(0);
            applyPO.setPassTime(DateUtils.now());
            applyPO.setApproveResult(message.getResult().name());
            this.updateById(applyPO);
        }
    }

    @Override
    public List<projectNameListVO> projectNameList() {
        List<ProjectApplyPO> list = this.baseMapper.getProjectNameList();
        List<projectNameListVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(key -> {
                vos.add(projectNameListVO.builder()
                                .ApplyId(key.getId())
                                .projectName(key.getProjectName())
                                .build());
            });
        }
        return vos;
    }

    @Override
    public Pagination<ProjectNodeInfo> projectNodeInfoList(BaseQuery query) {
        //责任人筛选
        Set<Long> applyIdList = new HashSet<>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            applyIdList = this.getFilterApplyIds(query.getKeyword());
            if (CollectionUtils.isEmpty(applyIdList)) {
                return new Pagination<>();
            }
        }
        Page<ProjectApplyPO> page = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(query.getPeriod()), ProjectApplyPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .in(!CollectionUtils.isEmpty(applyIdList), ProjectApplyPO::getId, applyIdList)
                .page(PagingUtils.convert(query));
        List<ProjectNodeInfo> nodeInfos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(apply -> {
                ProjectNodeInfo projectNodeInfo = ProjectNodeInfo.builder()
                        .projectNum(apply.getApplyNum())
                        .projectName(apply.getProjectName())
                        .processInsId(apply.getProcessInsId())
                        .id(apply.getId())
                        .build();
                this.getProjectStatus(projectNodeInfo);
                nodeInfos.add(projectNodeInfo);
            });
        }
        return PagingUtils.convert(page, nodeInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProjectApplyBatch(List<String> processInstanceIds) {
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return;
        }
        List<ProjectApplyPO> projectApplyPOS = this.lambdaQuery().in(ProjectApplyPO::getProcessInsId, processInstanceIds).list();
        DatabaseContextHolder.softDelete(ProjectApplyPO.class, projectApplyPOS);
        flowClient.deleteFormFlow(new HashSet<>(processInstanceIds));
    }

    private Set<Long> getFilterApplyIds(String keyWord) {
        Set<Long> applyIdList = new HashSet<>();
        List<Long> filterIdList = getFilterUserIdList(keyWord);
        List<Long> applyIds = projectScheduleService
                .lambdaQuery()
                .and(ex -> {
                    ex.or().like(ProjectSchedulePO::getProjectName, keyWord);
                    ex.or().like(ProjectSchedulePO::getPartyB, keyWord);
                    ex.or(!CollectionUtils.isEmpty(filterIdList), t -> {
                        for (Long aLong : filterIdList) {
                            t.or(q -> {
                                q.like(ProjectSchedulePO::getSuperviseUsers, aLong);
                            });
                        }
                    });
                })
                .list()
                .stream().map(ProjectSchedulePO::getApplyId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(applyIds)) {
            applyIdList.addAll(applyIds);
        }
        List<Long> idList = this.lambdaQuery().select(ProjectApplyPO::getId)
                .like(ObjectUtils.isNotEmpty(keyWord), ProjectApplyPO::getProjectName, keyWord)
                .list()
                .stream().map(ProjectApplyPO::getId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(idList)) {
            applyIdList.addAll(idList);
        }
        return applyIdList;
    }

    private List<Long> getFilterUserIdList(String keyWord) {
        List<UserDTO> userDTOS = userAndOrgService.getUserListByName(keyWord);
        List<Long> filterIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userDTOS)) {
            filterIdList = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList());
        }
        return filterIdList;
    }

    private void getProjectStatus(ProjectNodeInfo projectNodeInfo) {
        List<String> status = new ArrayList<>();
        status.add(ProjectStateEnum.APPLYING.name());
        ProjectDetailVO.ApplyDetail apply = projectSignContractService.getContractByApply(projectNodeInfo.getId());
        if (ObjectUtils.isNotNull(apply)) {
            status.add(ProjectStateEnum.SIGNING.name());
        }
        ProjectSchedulePO schedulePO = projectScheduleService.getProjectScheduleByApplyId(projectNodeInfo.getId());
        if (ObjectUtils.isNotNull(schedulePO)) {
            projectNodeInfo.setScheduleId(schedulePO.getId());
            projectNodeInfo.setCompletionTime(schedulePO.getCompletionTime());
            projectNodeInfo.setPartyB(schedulePO.getPartyB());
            status.add(ProjectStateEnum.UNDERWAY.name());
            if (ProjectStateEnum.COMPLETED.name().equals(schedulePO.getStatus())) {
                status.add(ProjectStateEnum.COMPLETED.name());
            } else if (ProjectStateEnum.WAIT_ACCEPT.name().equals(schedulePO.getStatus())) {
                status.add(ProjectStateEnum.COMPLETED.name());
                status.add(ProjectStateEnum.WAIT_ACCEPT.name());
            }
        }
        projectNodeInfo.setNodes(status);
        projectNodeInfo.setStatus(status.get(status.size() - 1));
    }

    /**
     * @Author wql
     * @Description 获取申请单编号
     * @Date 16:26 2023/9/15
     **/
    private String getApplyNum() {
        Long count = this.lambdaQuery().ge(ProjectApplyPO::getCreateTime, DateUtils.beginOfDay(DateUtils.now())).count();
        return BizDateCodeUtil.generateNum("XS", count);
    }
}
