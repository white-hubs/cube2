package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskCirculateConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlan;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlanTaskConfig;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionEnableEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionPlanStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionDeptHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionPostHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionRoleHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.PlanPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.PlanUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanTaskConfigService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.PlanDetailVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.PlanPageVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionPlanServiceImpl implements BusinessDisinfectionPlanService {
    private final DisinfectionPlanService disinfectionPlanService;
    private final DisinfectionPlanTaskConfigService planTaskConfigService;
    private final DisinfectionProjectService disinfectionProjectService;
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionDataTypeService disinfectionDataTypeService;
    private final DisinfectionGroupService disinfectionGroupService;
    private final DisinfectionDeptHelper disinfectionDeptHelper;
    private final DisinfectionPostHelper disinfectionPostHelper;
    private final DisinfectionRoleHelper disinfectionRoleHelper;
    private final DisinfectionAreaService disinfectionAreaService;
    private final DisinfectionGroupUserService disinfectionGroupUserService;

    @Transactional
    @Override
    public boolean insertPlan(PlanUpsertParam param) {
        long id = IdWorker.getId();
        return insertDisinfectionPlan(param, id) && insertDisinfectionPlanTaskConfig(param, id);
    }

    private boolean insertDisinfectionPlanTaskConfig(PlanUpsertParam param, long id) {
        List<PlanUpsertParam.ConfigTaskUpsertParam> taskUpsertParamList = param.getPlanTaskConfigList();
        List<DisinfectionPlanTaskConfig> taskConfigs = ObjectUtils.copying(taskUpsertParamList, DisinfectionPlanTaskConfig.class);
        //校验配置的计划任务是否存在在接收人
        checkPlanTaskConfigReceiver(taskConfigs);
        //获取日常消杀的id
        Long taskTypeId = disinfectionDataTypeService.getIdByName(DisinfectionTaskTypeEnum.DAILY_DISINFECTION.getCode(), DisinfectionDataTypeEnum.TASK);
        taskConfigs.forEach(taskConfig -> {
            taskConfig.setProjectId(param.getProjectId());
            taskConfig.setPlanId(id);
            //设置任务类型为默认类型
            taskConfig.setTaskTypeId(taskTypeId);
        });
        return planTaskConfigService.saveBatch(taskConfigs);
    }

    private void checkPlanTaskConfigReceiver(List<DisinfectionPlanTaskConfig> taskConfigs) {
        Assert.isParameterError(ListUtils.isEmpty(taskConfigs), "计划任务配置为空");
        List<Long> receiveDeptIds = new ArrayList<>();
        List<Long> receivePostIds = new ArrayList<>();
        List<Long> receiveRoleIds = new ArrayList<>();
        List<Long> receiveGroupIds = new ArrayList<>();
        taskConfigs.forEach(taskConfig -> {
            DisinfectionNotificationTypeEnum notificationType = taskConfig.getNotificationType();
            Assert.parameterIsNull(notificationType, "通知类型为空");
            switch (notificationType) {
                case DEPT:
                    Long notificationDeptId = taskConfig.getNotificationDeptId();
                    Assert.parameterIsNull(notificationDeptId, "任务通知部门为空");
                    receiveDeptIds.add(notificationDeptId);
                    break;

                case POST:
                    Long notificationPostId = taskConfig.getNotificationPostId();
                    Assert.parameterIsNull(notificationPostId, "任务通知岗位为空");
                    receivePostIds.add(notificationPostId);
                    break;

                case GROUP:
                    Long notificationGroupId = taskConfig.getNotificationGroupId();
                    Assert.parameterIsNull(notificationGroupId, "任务通知小组为空");
                    receiveGroupIds.add(notificationGroupId);
                    break;

                case ROLE:
                    Long notificationRoleId = taskConfig.getNotificationRoleId();
                    Assert.parameterIsNull(notificationRoleId, "任务通知角色为空");
                    receiveRoleIds.add(notificationRoleId);
                    break;
                case GROUP_ROLE:
                    Long notificationGroupRoleId = taskConfig.getNotificationRoleId();
                    Assert.parameterIsNull(notificationGroupRoleId, "任务通知角色为空");
                    Long notificationGroupRoleGroupId = taskConfig.getNotificationGroupId();
                    receiveRoleIds.add(notificationGroupRoleId);
                    receiveGroupIds.add(notificationGroupRoleGroupId);
                    break;
                default:
                    throw new ServiceException("未知的通知类型！");

            }
        });
        Map<Long, Boolean> roleExistUserMap = disinfectionRoleHelper.isExistUsersByRoleIds(receiveRoleIds);
        Map<Long, Boolean> groupExistUserMap = disinfectionGroupUserService.isExistUsersByGroupIds(receiveGroupIds);
        Map<Long, Boolean> deptExistUserMap = disinfectionDeptHelper.isExistUsersByDeptIds(receiveDeptIds);
        Map<Long, Boolean> postExistUserMap = disinfectionPostHelper.isExistUsersByPostIds(receivePostIds);
        taskConfigs.forEach(taskConfig -> {
            DisinfectionNotificationTypeEnum notificationType = taskConfig.getNotificationType();
            switch (notificationType) {
                case DEPT:
                    Long notificationDeptId = taskConfig.getNotificationDeptId();
                    Assert.isNotAcceptable(!deptExistUserMap.getOrDefault(notificationDeptId, false), "计划任务通知部门不存在用户！");
                    break;
                case POST:
                    Long notificationPostId = taskConfig.getNotificationPostId();
                    Assert.isNotAcceptable(!postExistUserMap.getOrDefault(notificationPostId, false), "计划任务通知岗位不存在用户！");
                    break;
                case GROUP:
                    Long notificationGroupId = taskConfig.getNotificationGroupId();
                    Assert.isNotAcceptable(!groupExistUserMap.getOrDefault(notificationGroupId, false), "计划任务通知小组不存在用户！");
                    break;
                case ROLE:
                    Long notificationRoleId = taskConfig.getNotificationRoleId();
                    Assert.isNotAcceptable(!roleExistUserMap.getOrDefault(notificationRoleId, false), "计划任务通知角色不存在用户！");
                    break;
                case GROUP_ROLE:
                    Long notificationGroupRoleId = taskConfig.getNotificationRoleId();
                    Long notificationGroupRoleGroupId = taskConfig.getNotificationGroupId();
                    Assert.isNotAcceptable(!roleExistUserMap.getOrDefault(notificationGroupRoleId, false), "计划任务通知角色不存在用户！");
                    Assert.isNotAcceptable(!groupExistUserMap.getOrDefault(notificationGroupRoleGroupId, false), "计划任务通知小组不存在用户！");
                    break;
                default:
                    throw new ServiceException("未知的通知类型！");

            }
        });
    }

    private boolean insertDisinfectionPlan(PlanUpsertParam param, long id) {
        DisinfectionPlan disinfectionPlan = ObjectUtils.copying(param, DisinfectionPlan.class);
        //校验计划名称是否存在
        existPlanName(param.getPlanName());
        disinfectionPlan.setId(id);
        //根据时间判断状态
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(disinfectionPlan.getBeginTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.NOT_STARTED);
        }
        if (now.isAfter(disinfectionPlan.getBeginTime()) && now.isBefore(disinfectionPlan.getEndTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.EFFECTIVE);
        }
        if (now.isAfter(disinfectionPlan.getEndTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.ENDED);
        }
        disinfectionPlan.setTaskGenerationCycleConfig(JsonUtils.serialize(param.getTaskGenerationCycleConfigs()));
        if (Objects.nonNull(param.getTaskTimeLimitConfig())) {
            disinfectionPlan.setTaskTimeLimitConfig(JsonUtils.serialize(param.getTaskTimeLimitConfig()));
        }

        //设置任务类型
        return disinfectionPlanService.save(disinfectionPlan);
    }

    private void existPlanName(String planName) {
        Assert.isParameterError(StringUtils.isEmpty(planName), "计划名称为空");
        DisinfectionPlan plan = disinfectionPlanService.getPlanByName(planName);
        Assert.isConflict(Objects.nonNull(plan), String.format("计划名称:%s,已存在", planName));
    }

    @Transactional
    @Override
    public boolean deletePlan(long id) {
        //删除计划
        disinfectionPlanService.removeById(id);
        //删除计划下的任务配置
        planTaskConfigService.remove(new LambdaQueryWrapper<DisinfectionPlanTaskConfig>().eq(DisinfectionPlanTaskConfig::getPlanId, id));
        return true;
    }

    @Transactional
    @Override
    public boolean updatePlan(long id, PlanUpsertParam param) {
        //修改计划
        return updateDisinfectionPlan(id, param) && updateDisinfectionPlanTaskConfig(id, param);
    }

    @Override
    public Pagination<PlanPageVO> pagePlans(PlanPageParam pageParam) {
        LambdaQueryChainWrapper<DisinfectionPlan> lambdaQuery = disinfectionPlanService.lambdaQuery();
        if (Objects.nonNull(pageParam.getPeriod())) {
            lambdaQuery.between(DisinfectionPlan::getCreateTime, pageParam.getPeriod().getBegin(), pageParam.getPeriod().getEnd());
        }
        Page<DisinfectionPlan> disinfectionPlanPage = lambdaQuery
                .eq(Objects.nonNull(pageParam.getStatus()), DisinfectionPlan::getStatus, pageParam.getStatus())
                .eq(Objects.nonNull(pageParam.getEnable()), DisinfectionPlan::getEnable, pageParam.getEnable())
                .and(StringUtils.isNotBlank(pageParam.getKeyword()), q -> {
                    //查询符合名称的项目
                    List<DisinfectionProject> projects = disinfectionProjectService.queryByName(pageParam.getKeyword());

                    if (ListUtils.nonEmpty(projects)) {
                        List<Long> projectIds = ListUtils.convertItem(projects, DisinfectionProject::getId);
                        q.in(DisinfectionPlan::getProjectId, projectIds);
                    }
                    //查询符合条件的人员
                    List<UserDTO> userDTOS = disinfectionUserHelper.listUsers(pageParam.getKeyword());
                    if (ListUtils.nonEmpty(userDTOS)) {
                        List<Long> userIds = ListUtils.convertItem(userDTOS, UserDTO::getId);
                        q.or().in(DisinfectionPlan::getProjectLeaderId, userIds);
                    }
                    q.or().like(DisinfectionPlan::getPlanName, pageParam.getKeyword());
                })
                .page(PagingUtils.convert(pageParam));
        List<DisinfectionPlan> planList = disinfectionPlanPage.getRecords();
        Map<Long, String> projectNamesMap = disinfectionProjectService.getProjectNamesMap(ListUtils.convertItem(planList, DisinfectionPlan::getProjectId));
        Map<Long, DisinfectionPlan> planMap = MapUtils.listToMap(planList, DisinfectionPlan::getId, plan -> plan);
        //查询获取的人员信息
        List<String> accounts = ListUtils.convertItem(planList, BasicEntity::getCreateBy);
        Map<String, String> userNamesMapByAccounts = disinfectionUserHelper.getUserNamesMapByAccounts(accounts);
        Pagination<PlanPageVO> voPagination = PagingUtils.convert(disinfectionPlanPage, PlanPageVO.class);
        voPagination.getRecords().forEach(record -> buildPlanExtendInfo(record, planMap, projectNamesMap, userNamesMapByAccounts));
        //设置计划任务配置信息
        buildPlanTaskConfigInfo(voPagination.getRecords());
        return voPagination;
    }

    private void buildPlanExtendInfo(PlanPageVO record, Map<Long, DisinfectionPlan> planMap, Map<Long, String> projectNamesMap,
                                     Map<String, String> userNamesMapByAccounts
                                    ) {
        //设置项目负责人名称
        record.setProjectLeaderName(disinfectionUserHelper.getUserName(record.getProjectLeaderId()));
        DisinfectionPlan plan = planMap.get(record.getId());
        //设置任务生成周期信息
        if (Objects.nonNull(plan) && StringUtils.isNotBlank(plan.getTaskGenerationCycleConfig())) {
            record.setTaskGenerationCycleConfigs(JsonUtils.deserialize(plan.getTaskGenerationCycleConfig(), new TypeReference<List<PlanTaskCirculateConfigDTO>>() {}));

        }
        //设置时限配置信息
        if (Objects.nonNull(plan) && StringUtils.isNotBlank(plan.getTaskTimeLimitConfig())) {
            record.setTaskTimeLimitConfig(JsonUtils.deserialize(plan.getTaskTimeLimitConfig(), PlanTaskTimeLimitConfigDTO.class));
        }
        //设置项目名称
        record.setProjectName(projectNamesMap.getOrDefault(record.getProjectId(), ""));
        //设置计划启用bool值
        record.setEnableBool(record.getEnable().getValue());
        //设置创建人信息
        record.setCreateName(userNamesMapByAccounts.getOrDefault(record.getCreateBy(), ""));
    }

    @Override
    public PlanDetailVO detailPlan(Long id) {
        DisinfectionPlan plan = disinfectionPlanService.getById(id);
        Assert.parameterIsNull(plan, "该数据不存在");
        PlanPageVO pageVO = ObjectUtils.copying(plan, PlanPageVO.class);

        Map<Long, String> projectNamesMap = disinfectionProjectService.getProjectNamesMap(Collections.singletonList(plan.getProjectId()));
        List<DisinfectionPlan> plans = Collections.singletonList(plan);
        Map<Long, DisinfectionPlan> planMap = MapUtils.listToMap(plans, DisinfectionPlan::getId, plan1 -> plan1);
        //设置计划拓展信息
        buildPlanExtendInfo(pageVO, planMap, projectNamesMap, disinfectionUserHelper.getUserNamesMapByAccounts(Collections.singletonList(plan.getCreateBy())));
        //设置任务拓展信息
        buildPlanTaskConfigInfo(Collections.singletonList(pageVO));
        return ObjectUtils.copying(pageVO, PlanDetailVO.class);
    }

    @Override
    public boolean updatePlanEnable(long id, DisinfectionEnableEnum enable) {
        DisinfectionPlan plan = disinfectionPlanService.getById(id);
        Assert.parameterIsNull(plan, "计划不存在！");
        plan.setEnable(enable);
        return disinfectionPlanService.updateById(plan);
    }

    private void buildPlanTaskConfigInfo(List<PlanPageVO> records) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        //获取计划的任务配置
        List<DisinfectionPlanTaskConfig> planTaskConfigList = planTaskConfigService.getPlanTaskByPlanIds(ListUtils.convertItem(records, PlanPageVO::getId));
        if (ListUtils.isEmpty(planTaskConfigList)) {
            return;
        }
        Map<Long, List<DisinfectionPlanTaskConfig>> planTaskMap = MapUtils.listGroupToMap(planTaskConfigList, DisinfectionPlanTaskConfig::getPlanId);

        //获取需要的小组信息
        List<Long> groupIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getNotificationGroupId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> groupNameMap = disinfectionGroupService.getNameMap(groupIds);

        //获取需要的部门信息
        List<Long> deptIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getNotificationDeptId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> deptNameMap = disinfectionDeptHelper.getDeptNameMap(deptIds);

        //获取需要的岗位信息
        List<Long> postIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getNotificationPostId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> postNameMap = disinfectionPostHelper.getPostNameMap(postIds);

        //获取需要的角色信息
        List<Long> roleIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getNotificationRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> roleNameMap = disinfectionRoleHelper.getRoleNameMap(roleIds);
        //获取需要的项目信息
        List<Long> projectIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getProjectId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> projectNamesMap = disinfectionProjectService.getProjectNamesMap(projectIds);
        //获取需要的计划信息
        List<Long> planIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getPlanId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> planNameMap = disinfectionPlanService.getPlanNameMap(planIds);
        //获取需要的区域信息
        List<Long> taskAreaIds = planTaskConfigList
                .stream()
                .map(DisinfectionPlanTaskConfig::getTaskAreaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> taskAreaNameMap = disinfectionAreaService.getAreaNameMap(taskAreaIds);
        records.forEach(record -> {
            List<DisinfectionPlanTaskConfig> taskConfigs = planTaskMap.getOrDefault(record.getId(), Collections.emptyList());
            List<PlanPageVO.PlanTaskConfigPageVO> taskConfigPageVOList = ObjectUtils.copying(taskConfigs, PlanPageVO.PlanTaskConfigPageVO.class);
            taskConfigPageVOList.forEach(taskConfigPageVO -> {
                taskConfigPageVO.setNotificationGroupName(groupNameMap.getOrDefault(taskConfigPageVO.getNotificationGroupId(), ""));
                taskConfigPageVO.setNotificationDeptName(deptNameMap.getOrDefault(taskConfigPageVO.getNotificationDeptId(), ""));
                taskConfigPageVO.setNotificationPostName(postNameMap.getOrDefault(taskConfigPageVO.getNotificationPostId(), ""));
                taskConfigPageVO.setNotificationRoleName(roleNameMap.getOrDefault(taskConfigPageVO.getNotificationRoleId(), ""));
                taskConfigPageVO.setTaskAreaName(taskAreaNameMap.getOrDefault(taskConfigPageVO.getTaskAreaId(), ""));
                taskConfigPageVO.setPlanName(planNameMap.getOrDefault(taskConfigPageVO.getPlanId(), ""));
                taskConfigPageVO.setProjectName(projectNamesMap.getOrDefault(taskConfigPageVO.getProjectId(), ""));

            });
            record.setPlanTaskConfigList(taskConfigPageVOList);
        });
    }

    private boolean updateDisinfectionPlanTaskConfig(long planId, PlanUpsertParam param) {
        List<PlanUpsertParam.ConfigTaskUpsertParam> taskUpsertParamList = param.getPlanTaskConfigList();
        if (ListUtils.isEmpty(taskUpsertParamList)) {
            //删除计划任务
            return planTaskConfigService.removePlanTaskByPlanId(planId);
        }
        //获取旧计划任务
        List<DisinfectionPlanTaskConfig> taskConfigList = planTaskConfigService.getPlanTaskByPlanId(planId);
        //获取要插入的任务
        List<PlanUpsertParam.ConfigTaskUpsertParam> waitInsertList = taskUpsertParamList
                .stream()

                .filter(configTaskUpsertParam -> Objects.isNull(configTaskUpsertParam.getId()))
                .collect(Collectors.toList());
        //获取要修改的任务
        List<PlanUpsertParam.ConfigTaskUpsertParam> waitUpdateList = taskUpsertParamList.stream()
                .filter(configTaskUpsertParam -> Objects.nonNull(configTaskUpsertParam.getId()))
                .collect(Collectors.toList());
        Map<Long, PlanUpsertParam.ConfigTaskUpsertParam> taskUpsertParamMap = MapUtils.listToMap(waitUpdateList, PlanUpsertParam.ConfigTaskUpsertParam::getId, updateParam -> updateParam);
        //保留的任务id
        List<Long> reveresIds = ListUtils.convertItem(waitUpdateList, PlanUpsertParam.ConfigTaskUpsertParam::getId);
        //旧的任务id
        List<Long> oldIds = ListUtils.convertItem(taskConfigList, DisinfectionPlanTaskConfig::getId);
        //获取要删除的任务
        List<Long> waitDelList = oldIds.stream().filter(id -> !reveresIds.contains(id)).collect(Collectors.toList());
        //删除任务
        planTaskConfigService.removeByIds(waitDelList);
        //修改任务
        List<DisinfectionPlanTaskConfig> updateList = taskConfigList.stream().filter(taskPO -> reveresIds.contains(taskPO.getId()))
                .collect(Collectors.toList());
        updateList.forEach(taskPO -> {
            PlanUpsertParam.ConfigTaskUpsertParam taskUpsertParam = taskUpsertParamMap.get(taskPO.getId());
            ObjectUtils.copying(taskUpsertParam, taskPO);
        });
        planTaskConfigService.updateBatchById(updateList);
        //新增任务
        List<DisinfectionPlanTaskConfig> insertList = ObjectUtils.copying(waitInsertList, DisinfectionPlanTaskConfig.class);
        checkPlanTaskConfigReceiver(insertList);
        insertList.forEach(taskPO -> {
            taskPO.setPlanId(planId);
            taskPO.setProjectId(param.getProjectId());
        });
        planTaskConfigService.saveBatch(insertList);
        return true;
    }

    private boolean updateDisinfectionPlan(long id, PlanUpsertParam param) {
        DisinfectionPlan disinfectionPlan = disinfectionPlanService.getById(id);
        Assert.parameterIsNull(disinfectionPlan, "修改的数据不存在");
        //校验名称是否有修改
        if (!disinfectionPlan.getPlanName().equals(param.getPlanName())) {
            existPlanName(param.getPlanName());
        }
        ObjectUtils.copying(param, disinfectionPlan);
        //重新设置状态
        //根据时间判断状态
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(disinfectionPlan.getBeginTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.NOT_STARTED);
        }
        if (now.isAfter(disinfectionPlan.getBeginTime()) && now.isBefore(disinfectionPlan.getEndTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.EFFECTIVE);
        }
        if (now.isAfter(disinfectionPlan.getEndTime())) {
            disinfectionPlan.setStatus(DisinfectionPlanStatusEnum.ENDED);
        }
        disinfectionPlan.setTaskGenerationCycleConfig(JsonUtils.serialize(param.getTaskGenerationCycleConfigs()));
        if (Objects.nonNull(param.getTaskTimeLimitConfig())) {
            disinfectionPlan.setTaskTimeLimitConfig(JsonUtils.serialize(param.getTaskTimeLimitConfig()));
        }
        return disinfectionPlanService.updateById(disinfectionPlan);
    }

}
