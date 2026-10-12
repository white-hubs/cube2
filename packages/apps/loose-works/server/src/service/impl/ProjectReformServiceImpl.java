package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectReformMapper;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectScheduleMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectReformDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectReformQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformOrderVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformVO;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.FlowApproveResultEnum;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectReformPO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectReformService;
import com.guhecloud.cube.srv.gen.loose.works.util.BizDateCodeUtil;
import com.guhecloud.cube.srv.gen.loose.works.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/12 11:52
 * @Description: 工程整改业务服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectReformServiceImpl extends ServiceImpl<ProjectReformMapper, ProjectReformPO> implements ProjectReformService {

    private final UserAndOrgService userAndOrgService;
    private final ProcessInstanceClient processInstanceClient;
    private final ProjectScheduleMapper projectScheduleMapper;
    private final FlowClient flowClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pagination<ProjectReformVO> projectReformList(ProjectReformQueryParam processInstancePageQuery) {
        if (Objects.nonNull(processInstancePageQuery.getProjectScheduleId())) {
            List<String> processInsIdList = Optional.ofNullable(this.lambdaQuery()
                                                                        .select(ProjectReformPO::getProcessInsId)
                                                                        .eq(ProjectReformPO::getProjectScheduleId, processInstancePageQuery.getProjectScheduleId())
                                                                        .list()).orElse(new ArrayList<>())
                    .stream().filter(t -> StringUtils.isNotBlank(t.getProcessInsId()))
                    .map(ProjectReformPO::getProcessInsId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(processInsIdList)) {
                return new Pagination<>();
            }
            processInstancePageQuery.setProcessInstanceIds(String.join(",", processInsIdList));

        }
        Pagination<ProcessInstanceDTO> page = processInstanceClient.allPage(processInstancePageQuery);
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        List<String> processIdList = page.getRecords().stream().map(ProcessInstanceDTO::getProcessInstanceId).distinct()
                .collect(Collectors.toList());
        List<ProjectReformPO> reformPOList = this.lambdaQuery().in(ProjectReformPO::getProcessInsId, processIdList).list();

        Map<String, ProjectReformPO> projectReformPOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(reformPOList)) {
            projectReformPOMap = reformPOList.stream()
                    .collect(Collectors.toMap(ProjectReformPO::getProcessInsId, Function.identity(), (k1, k2) -> k2));
        }
        List<ProjectReformVO> vos = new ArrayList<>();
        for (ProcessInstanceDTO record : page.getRecords()) {

            ProjectReformVO vo = ObjectUtils.copying(record, ProjectReformVO.class);

            if (projectReformPOMap.containsKey(record.getProcessInstanceId())) {
                ProjectReformPO reformPO = projectReformPOMap.get(record.getProcessInstanceId());
                vo.setReformNum(reformPO.getReformNum());
                vo.setProjectScheduleId(reformPO.getProjectScheduleId());
            } else {
                //存在流引擎有而小散这边没有的数据需要向小散工程中补数据
                ProcessStartedMessage message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                log.info("工程整改申请单同步:{},data:{}", message.getProcessInstanceId(), message.getData());
                ProjectReformPO reformPO = this.addProjectReform(message, record.getInstanceName());
                vo.setReformNum(reformPO.getReformNum());
                vo.setProjectScheduleId(reformPO.getProjectScheduleId());
            }
            vos.add(vo);
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized ProjectReformPO addProjectReform(ProcessStartedMessage message, String instanceName) {
        List<ProjectReformPO> list = this.lambdaQuery().eq(ProjectReformPO::getProcessInsId, message.getProcessInstanceId()).list();
        ProjectReformPO build = new ProjectReformPO();
        if (ObjectUtils.isEmpty(list)) {
            Long startUserId = message.getStartUserId();
            ProjectReformDTO parseObject = JsonUtils.deserialize(message.getData(), ProjectReformDTO.class);
            UserDTO.Organization organization = userAndOrgService.getUserById(message.getStartUserId()).getOrganization();
            ProjectSchedulePO projectSchedulePO = new ProjectSchedulePO();
            if (Objects.nonNull(parseObject) && CollectionUtils.isNotEmpty(parseObject.getProjectScheduleId())) {
                projectSchedulePO = projectScheduleMapper.selectById(parseObject.getProjectScheduleId().get(0));
            }
            build = ProjectReformPO.builder()
                    .projectScheduleId(Objects.nonNull(projectSchedulePO.getId()) ? projectSchedulePO.getId().toString() : null)
                    .projectScheduleName(projectSchedulePO.getProjectName())
                    .processInsName(instanceName)
                    .processInsId(message.getProcessInstanceId())
                    .approveResult(FlowResult.ACTIVE.name())
                    .startUser(startUserId)
                    .startDep(organization == null ? null : organization.getId())
                    .reformNum(getApplyNum())
                    .build();
            this.save(build);
        }
        return build;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void reformCompleted(ProcessCompletedMessage message) {
        List<ProjectReformPO> list = this.lambdaQuery().eq(ProjectReformPO::getProcessInsId, message.getProcessInstanceId()).list();
        if (ObjectUtils.isNotEmpty(list)) {
            ProjectReformPO reformPO = list.get(0);
            ProjectReformDTO parseObject = JsonUtils.deserialize(message.getData(), ProjectReformDTO.class);
            reformPO.setProjectScheduleId(Objects.nonNull(parseObject) && CollectionUtils.isNotEmpty(parseObject.getProjectScheduleId())
                                          ? parseObject.getProjectScheduleId().get(0) : null);
            reformPO.setPassTime(DateUtils.now());
            reformPO.setApproveResult(message.getResult().name());
            if (FlowApproveResultEnum.PASS.name().equals(message.getResult().name())) {
                this.updateById(reformPO);
            }
        }
    }

    @Override
    public ProjectReformAnalysisVO getProjectReformAnalysis(BaseQuery query) {
        Assert.isParameterError(Objects.isNull(query.getPeriod()), "时间段不能为空");
        ProjectReformAnalysisVO projectRecordVO = new ProjectReformAnalysisVO();
        Page<ProjectReformPO> projectReformPage = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(query.getPeriod()), ProjectReformPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .page(PagingUtils.convert(query));
        if (Objects.isNull(projectReformPage) || CollectionUtils.isEmpty(projectReformPage.getRecords())) {
            return projectRecordVO;
        }
        List<ProjectReformOrderVO> projectReformOrderVOS = projectReformPage.getRecords().stream().map(t -> {
            return ProjectReformOrderVO.builder()
                    .processInsName(t.getProcessInsName())
                    .projectScheduleId(t.getProjectScheduleId())
                    .projectName(t.getProjectScheduleName())
                    .status(t.getApproveResult())
                    .build();
        }).collect(Collectors.toList());
        projectRecordVO.setReformRankList(projectReformOrderVOS);
        Long currentReformCount = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(query.getPeriod()), ProjectReformPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .count();
        projectRecordVO.setReformNum(currentReformCount);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(previousPeriod);
        Long lastReformCount = this.lambdaQuery()
                .between(ObjectUtils.isNotNull(query.getPeriod()), ProjectReformPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .count();
        projectRecordVO.setReformChainRatio(MathUtils.calculateRingRatio(currentReformCount, lastReformCount).floatValue());
        return projectRecordVO;
    }

    @Override
    public Boolean deleteProjectReformBatch(List<String> processInstanceIds) {
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(processInstanceIds)) {
            return false;
        }
        List<ProjectReformPO> projectReformPOS = this.lambdaQuery().in(ProjectReformPO::getProcessInsId, processInstanceIds).list();
        DatabaseContextHolder.softDelete(ProjectReformPO.class, projectReformPOS);
        flowClient.deleteFormFlow(new HashSet<>(processInstanceIds));
        return true;
    }

    /**
     * @return
     */
    private String getApplyNum() {
        long count = this.lambdaQuery().ge(ProjectReformPO::getCreateTime, DateUtils.beginOfDay(DateUtils.now())).count();
        return BizDateCodeUtil.generateNum("ZG", count);
    }
}
