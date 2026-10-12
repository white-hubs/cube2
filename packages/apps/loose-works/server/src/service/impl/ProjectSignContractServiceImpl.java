package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectApplyMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectSignContractMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectSignContractDetail;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.FlowApproveResultEnum;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectStateEnum;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectApplyPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSignContractPO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleService;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectSignContractService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 工程签约
 * @date 2023/9/14 18:44
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectSignContractServiceImpl extends ServiceImpl<ProjectSignContractMapper, ProjectSignContractPO> implements ProjectSignContractService {
    private final UserAndOrgService userAndOrgService;
    private final ProcessInstanceClient processInstanceClient;
    private final ProjectScheduleService projectScheduleService;
    private final ProjectApplyMapper projectApplyMapper;
    private final FlowClient flowClient;

    @Override
    public Long getUnderContractNum(ProjectStatisticsQuery query) {
        return this.lambdaQuery().isNull(ProjectSignContractPO::getPassTime)
                .between(ObjectUtils.isNotNull(query.getPeriod()), ProjectSignContractPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .eq(ObjectUtils.isNotNull(query.getStartUser()), ProjectSignContractPO::getStartUser, query.getStartUser()).count();
    }

    @Override
    public ProjectDetailVO.ApplyDetail getContractByApply(Long id) {
        List<ProjectSignContractPO> list = this.lambdaQuery().eq(ProjectSignContractPO::getApplyId, id).list();
        if (ObjectUtils.isNotEmpty(list)) {
            ProjectSignContractPO contractPO = list.get(0);
            return ProjectDetailVO.ApplyDetail.builder()
                    .username(userAndOrgService.getUserById(contractPO.getStartUser()).getName())
                    .depName(userAndOrgService.getOrgInfoById(contractPO.getStartDep()).getName())
                    .passTime(contractPO.getPassTime())
                    .orderNumber(contractPO.getProjectNum())
                    .processInsId(contractPO.getProcessInsId())
                    .build();
        }
        return null;
    }

    @Override
    public Pagination<ProjectApplyListVO> projectSignContractList(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        List<ProjectApplyListVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page)) {
            page.getRecords().forEach(record -> {
                ProjectApplyListVO vo = new ProjectApplyListVO();
                ObjectUtils.copying(record, vo);
                List<ProjectSignContractPO> list = this.lambdaQuery()
                        .eq(ProjectSignContractPO::getProcessInsId, record.getProcessInstanceId()).list();
                if (ObjectUtils.isNotEmpty(list)) {
                    ProjectSignContractPO contractPO = list.get(0);
                    vo.setApplyNum(contractPO.getProjectNum());
                    vo.setProjectName(contractPO.getProjectName());
                } else {
                    //存在流引擎有而小散这边没有的数据需要向小散工程中补数据
                    ProcessStartedMessage message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    log.info("工程签约申请单同步:{},data:{}", message.getProcessInstanceId(), message.getData());
                    ProjectSignContractPO contractPO = addSignContract(message);
                    vo.setApplyNum(contractPO.getProjectNum());
                    vo.setProjectName(contractPO.getProjectName());
                }
                vos.add(vo);
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public synchronized ProjectSignContractPO addSignContract(ProcessStartedMessage message) {
        List<ProjectSignContractPO> list = this.lambdaQuery().eq(ProjectSignContractPO::getProcessInsId, message.getProcessInstanceId())
                .list();
        ProjectSignContractPO build = new ProjectSignContractPO();
        if (ObjectUtils.isEmpty(list)) {
            Long startUserId = message.getStartUserId();
            UserDTO.Organization organization = userAndOrgService.getUserById(message.getStartUserId()).getOrganization();
            ProjectSignContractDetail contract = JsonUtils.deserialize(message.getData(), ProjectSignContractDetail.class);
            Long applyId = Long.parseLong(contract.getProjectName());
            ProjectApplyPO applyPO = projectApplyMapper.selectById(applyId);
            build = ProjectSignContractPO.builder()
                    .projectName(applyPO.getProjectName())
                    .processInsId(message.getProcessInstanceId())
                    .startUser(startUserId)
                    .startDep(organization == null ? null : organization.getId())
                    .applyId(applyId)
                    .projectNum(applyPO.getApplyNum())
                    .build();
            this.save(build);
        }
        return build;
    }

    @Override
    public void signContractCompleted(ProcessCompletedMessage message) {
        List<ProjectSignContractPO> list = this.lambdaQuery().eq(ProjectSignContractPO::getProcessInsId, message.getProcessInstanceId())
                .list();
        if (ObjectUtils.isNotEmpty(list)) {
            ProjectSignContractPO contractPO = list.get(0);
            contractPO.setPassTime(DateUtils.now());
            contractPO.setApproveResult(message.getResult().name());
            if (FlowApproveResultEnum.PASS.name().equals(message.getResult().name())) {
                //审批通过时,自动生成一条工程进度跟踪信息
                ProjectSignContractDetail contract = JsonUtils.deserialize(message.getData(), ProjectSignContractDetail.class);
                Long superviseUser = ObjectUtils.isEmpty(contract.getSuperviseUserId()) ? null : contract.getSuperviseUserId()[0];
                String superviseDepId = null;
                if (CollectionUtils.isNotEmpty(contract.getSuperviseDepId())) {
                    superviseDepId = contract.getSuperviseDepId().stream().map(String::valueOf).collect(Collectors.joining(","));
                }
                projectScheduleService.saveScheduleInfo(ProjectSchedulePO.builder()
                                                                .applyId(contractPO.getApplyId())
                                                                .contractId(contractPO.getId())
                                                                .startUser(contractPO.getStartUser())
                                                                .projectNum(contractPO.getProjectNum())
                                                                .projectName(contractPO.getProjectName())
                                                                .partyB(contract.getPartyB())
                                                                .completionTime(contract.getFinishTime())
                                                                .superviseUsers(
                                                                        Objects.nonNull(superviseUser) ? String.valueOf(superviseUser)
                                                                                                       : null)
                                                                .superviseUserName(superviseUser == null ? null : userAndOrgService
                                                                        .getUserById(superviseUser).getName())
                                                                .superviseDep(superviseDepId)
                                                                .status(ProjectStateEnum.UNDERWAY.name())
                                                                .build());
                this.updateById(contractPO);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProjectSignContractBatch(List<String> processInstanceIds) {
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(processInstanceIds)) {
            return;
        }
        List<ProjectSignContractPO> projectSignContractPOS = this.lambdaQuery()
                .in(ProjectSignContractPO::getProcessInsId, processInstanceIds).list();
        DatabaseContextHolder.softDelete(ProjectSignContractPO.class, projectSignContractPOS);
        flowClient.deleteFormFlow(new HashSet<>(processInstanceIds));
    }
}
