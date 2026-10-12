package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationPageDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolRouteMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanSuperintendent;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolConditionParticipateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolParticipateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanLocationParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanStatusParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanSuperintendentService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolPlanTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolSuperintendentVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolConditionParticipateVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan.PatrolPlanVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 消息配置
 *
 * @author : lijie
 * @date : 2023-5-29
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolPlanServiceImpl extends ServiceImpl<PatrolPlanMapper, PatrolPlan> implements PatrolPlanService {
    private final UserClient userClient;
    private final PatrolPlanLocationMapper planLocationMapper;
    private final PatrolLocationService locationService;
    private final PatrolTaskService taskService;
    private final PatrolRouteMapper patrolRouteMapper;
    private final TenantClient tenantClient;
    private final PatrolPlanTaskMapper patrolPlanTaskMapper;
    private final PatrolPlanSuperintendentService planSuperintendentService;
    private final OrganizationClient organizationClient;
    private final RoleClient roleClient;
    private final AppointmentClient appointmentClient;
    private final AreaClient areaClient;

    /**
     * 查询计划详情
     *
     * @param id 计划id
     *
     * @return 计划详情
     */
    @Override
    public PatrolPlanVO getPatrolPlan(Long id) {
        PatrolPlan patrolPlan = baseMapper.selectById(id);
        PatrolPlanVO plan = ConvertUtils.convert(PatrolPlanVO.class, patrolPlan);
        String patrolWay = patrolPlan.getPatrolWay();
        if (StringUtils.isNotBlank(patrolWay)) {
            plan.setPatrolWayList(Arrays.asList(patrolWay.split(",")));
        }
        // 反查人员信息
        if (StringUtils.isNotBlank(patrolPlan.getExceptionUserIds())) {
            List<String> userStringIdList = Arrays.asList(patrolPlan.getExceptionUserIds().split(","));
            List<UserDTO> user = this.getUserList(userStringIdList);
            var abnormalUserList = user.stream().map(u -> PatrolSuperintendentVO.builder().participateType(ConditionTypeEnum.USER.name())
                    .targetId(u.getId()).targetName(u.getName()).build()).collect(Collectors.toList());
            var participateList = this.getSuperintendentList(planSuperintendentService.lambdaQuery()
                                                                     .eq(PatrolPlanSuperintendent::getMasterId, plan.getId()).list());

            plan.setConditionParticipate(PatrolConditionParticipateVO.builder().conditionType(patrolPlan.getConditionType())
                                                 .abnormalUserList(abnormalUserList)
                                                 .participateList(participateList).build());
        }
        if (StringUtils.isNotBlank(patrolPlan.getUserId())) {
            List<String> userStringIdList = Arrays.asList(patrolPlan.getUserId().split(","));
            plan.setUserList(this.getUserList(userStringIdList));
        }

        // 查询巡更路线
        plan.setLocationList(locationService.queryLocationByPlan(plan.getId()));
        // 查询计划的任务
        List<PatrolPlanTask> planTaskList = patrolPlanTaskMapper.selectList(Wrappers.<PatrolPlanTask>lambdaQuery()
                                                                                    .eq(PatrolPlanTask::getPlanId, plan.getId())
                                                                           );
        List<PatrolPlanTaskVO> planTaskVOList = new ArrayList<>();
        if (planTaskList != null && !planTaskList.isEmpty()) {
            for (PatrolPlanTask patrolPlanTask : planTaskList) {
                PatrolPlanTaskVO planTaskVO = new PatrolPlanTaskVO();
                BeanUtils.copyProperties(patrolPlanTask, planTaskVO);
                String executePeriod = patrolPlanTask.getExecutePeriod();
                if (StringUtils.isNotBlank(executePeriod)) {
                    planTaskVO.setExecutePeriod(JSON.parseArray(executePeriod, String.class));
                }
                planTaskVOList.add(planTaskVO);
            }
        }
        plan.setTaskList(planTaskVOList);
        return plan;
    }

    /**
     * 查询计划列表
     *
     * @param param 查询条件
     *
     * @return 计划信息分页
     */
    @Override
    public Pagination<PatrolPlanPageVO> getPalnPage(PatrolPlanPageParam param) {
        Page<PatrolPlan> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<String> status = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            status.add(param.getStatus());
        } else {
            status.add(PlanStatusEnum.ENABLE.name());
            status.add(PlanStatusEnum.END.name());
            status.add(PlanStatusEnum.STOP.name());
        }
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<PatrolPlan>()
                .in(PatrolPlan::getStatus, status)
                .eq(ObjectUtils.isNotEmpty(param.getExecuteData()), PatrolPlan::getExecuteData, param.getExecuteData())
                .eq(ObjectUtils.isNotEmpty(param.getPatrolCycle()), PatrolPlan::getPatrolCycle, param.getPatrolCycle())
                .like(ObjectUtils.isNotEmpty(param.getKeyword()), PatrolPlan::getPlanName, param.getKeyword())
                .orderByDesc(PatrolPlan::getCreateTime));
        Pagination<PatrolPlanPageVO> convert = PagingUtils.convert(page, PatrolPlanPageVO.class);
        if (CollectionUtils.isEmpty(convert.getRecords())) {
            return convert;
        }
        Set<Long> planIdSet = new HashSet<>();
        Set<Long> routeSet = new HashSet<>();
        convert.getRecords().forEach(plan -> {
            planIdSet.add(plan.getId());
            routeSet.add(plan.getRouteId());
        });
        /* 巡更点名称*/
        Map<Long, String> routeNameMap = new HashMap<>();
        List<PatrolRoute> patrolRouteList = patrolRouteMapper.selectList(Wrappers.<PatrolRoute>lambdaQuery()
                                                                                 .in(PatrolRoute::getUuid, routeSet)
                                                                                 .groupBy(PatrolRoute::getUuid)
                                                                        );
        if (!CollectionUtils.isEmpty(patrolRouteList)) {
            patrolRouteList.forEach(route -> routeNameMap.put(route.getUuid(), route.getPatrolRouteName()));
        }
        /* 巡更计划 */
        Map<Long, List<PatrolPlanTask>> planTaskMap = new HashMap<>();
        List<PatrolPlanTask> planTaskList = patrolPlanTaskMapper.selectList(Wrappers.<PatrolPlanTask>lambdaQuery()
                                                                                    .in(PatrolPlanTask::getPlanId, planIdSet)
                                                                           );
        if (!CollectionUtils.isEmpty(planTaskList)) {
            planTaskMap = planTaskList.stream().collect(Collectors.groupingBy(PatrolPlanTask::getPlanId));
        }
        Map<Long, List<PatrolPlanTask>> finalPlanTaskMap = planTaskMap;
        convert.getRecords().forEach(plan -> {
            String routeName = plan.getRouteId() == null ? "" : routeNameMap.get(plan.getRouteId());
            plan.setRouteName(routeName == null ? "" : routeName);
            List<PatrolPlanTask> planTasks = finalPlanTaskMap.get(plan.getId());
            if (planTasks != null && !planTasks.isEmpty()) {
                plan.setTaskTimeRangeList(planTasks.stream().map(PatrolPlanTask::getTimeRange).collect(Collectors.toList()));
            }

        });
        return convert;
    }

    @Override
    public List<KeyValueVO> getPalnStatus() {
        List<KeyValueVO> data = baseMapper.getPalnStatus();
        if (ObjectUtils.isEmpty(data)) {
            data = new ArrayList<>();
            data.add(new KeyValueVO("ENABLE", 0, ""));
            data.add(new KeyValueVO("STOP", 0, ""));
            data.add(new KeyValueVO("END", 0, ""));
            data.add(new KeyValueVO("ALL", 0, ""));
            return data;
        }
        Integer total = 0;
        for (PlanStatusEnum ex : PlanStatusEnum.values()) {
            boolean bool = true;
            for (KeyValueVO obj : data) {
                if (ex.name().equals(obj.getName())) {
                    bool = false;
                    total += obj.getValue();
                    break;
                }
            }
            if (bool) {
                data.add(new KeyValueVO(ex.name(), 0, "0"));
            }
        }
        data.add(new KeyValueVO("ALL", total, ""));
        return data;
    }

    /**
     * 查询用户是否有考勤应用
     *
     * @return 是否有考勤应用
     */
    @Override
    public Boolean attendance() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<ApplicationPageDTO> applicationPageDTOS = tenantClient.listTenantProductApplications(tenant, null);
        for (ApplicationPageDTO ex : applicationPageDTOS) {
            if ("ATTENDANCE_MANAGEMENT".equals(ex.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 停止、启动计划
     *
     * @param param 计划信息
     *
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PatrolPlanStatusParam param) {
        PatrolPlan plan = this.baseMapper.selectById(param.getId());
        // 判断计划是否过期
        plan.setStatus(DateUtils.compare(plan.getEndData(), LocalDateTime.now()) <= 0 ?
                       PlanStatusEnum.END.name() : param.getStatus());
        Boolean bool = baseMapper.updateById(plan) > 0;
        if (PlanStatusEnum.ENABLE.name().equals(param.getStatus())) {
            this.checkPlanLocation(plan.getId());
            List<PatrolPlanTask> planTaskList = patrolPlanTaskMapper.selectList(Wrappers.<PatrolPlanTask>lambdaQuery()
                                                                                        .eq(PatrolPlanTask::getPlanId, plan.getId())
                                                                               );
            var superintendentList = planSuperintendentService.lambdaQuery().eq(PatrolPlanSuperintendent::getMasterId, plan.getId()).list();
            this.addTaskByPlan(plan, superintendentList, planTaskList, LocalDateTime.now());
        }
        return bool;
    }

    /**
     * 校验计划的线路和计划线路中的巡更点是否被删除
     * 如果线路或者线路中的巡更点被删除，则抛出异常提示，计划线路被删除或者哪些巡更点被删除
     *
     * @param planId 计划id
     */
    private void checkPlanLocation(Long planId) {
        PatrolPlan plan = this.baseMapper.selectById(planId);
        if (ObjectUtils.isEmpty(plan) || ObjectUtils.isEmpty(plan.getRouteId())) {
            return;
        }

        // 1. 校验计划的线路是否被删除(通过线路状态判断)
        List<PatrolRoute> routeList = patrolRouteMapper.selectList(Wrappers.<PatrolRoute>lambdaQuery()
                                                                           .eq(PatrolRoute::getUuid, plan.getRouteId())
                                                                           .orderByAsc(PatrolRoute::getRuleOrder));
        boolean routeDeleted = CollectionUtils.isEmpty(routeList) || routeList.stream()
                .noneMatch(item -> DeleteStatusEnum.NO_REMOVE.name().equals(item.getStatus()));
        Assert.isConflict(routeDeleted, "巡更路线已被删除");

        // 2. 校验计划线路中的巡更点是否被删除(通过巡更点状态判断)
        List<Long> locationIds = planLocationMapper.selectList(Wrappers.<PatrolPlanLocation>lambdaQuery()
                                                                       .select(PatrolPlanLocation::getLocationId)
                                                                       .eq(PatrolPlanLocation::getPlanId, planId)).stream()
                .map(PatrolPlanLocation::getLocationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(locationIds)) {
            return;
        }
        Map<Long, PatrolLocation> locationMap = locationService.lambdaQuery()
                .in(PatrolLocation::getId, locationIds)
                .list()
                .stream()
                .collect(Collectors.toMap(PatrolLocation::getId, item -> item, (a, b) -> a));
        List<String> deletedLocationNameList = new ArrayList<>();
        locationIds.forEach(locationId -> {
            PatrolLocation location = locationMap.get(locationId);
            boolean locationDeleted = ObjectUtils.isEmpty(location) || DeleteStatusEnum.REMOVE.name().equals(location.getStatus());
            if (locationDeleted) {
                String locationName = location == null ? String.valueOf(locationId) : location.getPatrolLocationName();
                if (!deletedLocationNameList.contains(locationName)) {
                    deletedLocationNameList.add(locationName);
                }
            }
        });

        Assert.isConflict(CollectionUtils.isNotEmpty(deletedLocationNameList),
                          "巡更点：" + String.join("、", deletedLocationNameList) + "已被删除");

    }

    /**
     * 删除计划
     *
     * @param id 计划id
     *
     * @return 删除结果
     */
    @Override
    public Boolean deleteById(Long id) {
        PatrolPlan plan = this.baseMapper.selectById(id);
        plan.setStatus(DeleteStatusEnum.REMOVE.name());
        return baseMapper.updateById(plan) > 0;
    }

    /**
     * 新增巡更计划v1.2
     *
     * @param param 计划信息
     *
     * @return id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPlan(PatrolPlanParam param) {
        Long planId = IdWorker.getId();
        var user = checkPlanUser(param);
        PatrolPlan patrolPlan = PatrolPlanParam.getPatrolPlan(param, user);
        patrolPlan.setId(planId);
        // 插入基本信息
        baseMapper.insert(patrolPlan);
        // 插入计划人员
        var planSuperintendentList = Optional.ofNullable(param.getConditionParticipate().getParticipateList())
                .orElse(CollectionUtils.emptyList()).stream()
                .map(p -> (PatrolPlanSuperintendent) PatrolPlanSuperintendent.builder().masterId(planId)
                        .superintendentType(p.getParticipateType())
                        .targetId(p.getTargetId()).build()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(planSuperintendentList)) {
            planSuperintendentService.saveBatch(planSuperintendentList);
        }

        int i = 1;
        // 记录巡更点
        for (PatrolPlanLocationParam ex : param.getLocationIds()) {
            PatrolPlanLocation location = new PatrolPlanLocation();
            location.setPlanId(patrolPlan.getId());
            location.setLocationId(ex.getId());
            location.setOrderNum(i);
            planLocationMapper.insert(location);
            i += 1;
        }
        // 记录计划的任务
        List<PatrolPlanTaskParam> taskList = param.getTaskList();
        List<PatrolPlanTask> planTaskList = new ArrayList<>();
        if (taskList != null && !taskList.isEmpty()) {
            for (PatrolPlanTaskParam patrolPlanTaskParam : taskList) {
                PatrolPlanTask planTask = PatrolPlanTask.builder()
                        .id(IdWorker.getId())
                        .timeRange(patrolPlanTaskParam.getTimeRange())
                        .intervalNumber(patrolPlanTaskParam.getIntervalNumber())
                        .intervalUnit(patrolPlanTaskParam.getIntervalUnit())
                        .executePeriod(JSON.toJSONString(patrolPlanTaskParam.getExecutePeriod()))
                        .planId(planId)
                        .build();
                planTaskList.add(planTask);
                patrolPlanTaskMapper.insert(planTask);
            }
        }
        // 是否生成任务
        this.addTaskByPlan(patrolPlan, planSuperintendentList, planTaskList, LocalDateTime.now());
        return planId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePlan(PatrolPlanParam param) {
        UserDTO user = this.checkPlanUser(param);
        PatrolPlan patrolPlanData = baseMapper.selectById(param.getId());
        PatrolPlan patrolPlan = PatrolPlanParam.getPatrolPlan(param, user);
        patrolPlan.setLastDate(patrolPlanData.getLastDate());
        EntityAuditor.populateCreateAudit(patrolPlan, patrolPlanData.getCreateBy(), patrolPlanData.getCreateBy(),
                                          patrolPlanData.getCreatorType(),
                                          patrolPlanData.getCreateTime());
        baseMapper.updateById(patrolPlan);
        // 插入计划人员
        List<PatrolPlanSuperintendent> planSuperintendentList = new ArrayList<>();
        planSuperintendentService.remove(Wrappers.<PatrolPlanSuperintendent>lambdaQuery()
                                                 .eq(PatrolPlanSuperintendent::getMasterId, patrolPlan.getId()));
        if (param.getConditionParticipate() != null) {
            planSuperintendentList = Optional.ofNullable(param.getConditionParticipate().getParticipateList())
                    .orElse(CollectionUtils.emptyList()).stream()
                    .map(p -> (PatrolPlanSuperintendent) PatrolPlanSuperintendent.builder().masterId(param.getId())
                            .superintendentType(p.getParticipateType())
                            .targetId(p.getTargetId()).build()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(planSuperintendentList)) {
                planSuperintendentService.saveBatch(planSuperintendentList);
            }
        }
        int i = 1;
        // 记录巡更点
        if (ObjectUtils.isNotEmpty(param.getLocationIds())) {
            planLocationMapper.delete(new LambdaQueryWrapper<>(PatrolPlanLocation.class)
                                              .eq(PatrolPlanLocation::getPlanId, patrolPlan.getId()));
            for (PatrolPlanLocationParam ex : param.getLocationIds()) {
                PatrolPlanLocation location = new PatrolPlanLocation();
                location.setPlanId(patrolPlan.getId());
                location.setLocationId(ex.getId());
                location.setOrderNum(i);
                planLocationMapper.insert(location);
                i += 1;
            }
        }
        // 记录计划的任务
        List<PatrolPlanTaskParam> taskList = param.getTaskList();
        List<PatrolPlanTask> planTaskList = new ArrayList<>();
        if (taskList != null && !taskList.isEmpty()) {
            patrolPlanTaskMapper.delete(new LambdaQueryWrapper<>(PatrolPlanTask.class)
                                                .eq(PatrolPlanTask::getPlanId, param.getId()));
            for (PatrolPlanTaskParam patrolPlanTaskParam : taskList) {
                PatrolPlanTask planTask = PatrolPlanTask.builder()
                        .id(IdWorker.getId())
                        .timeRange(patrolPlanTaskParam.getTimeRange())
                        .intervalNumber(patrolPlanTaskParam.getIntervalNumber())
                        .intervalUnit(patrolPlanTaskParam.getIntervalUnit())
                        .executePeriod(JSON.toJSONString(patrolPlanTaskParam.getExecutePeriod()))
                        .planId(param.getId())
                        .build();
                planTaskList.add(planTask);
                patrolPlanTaskMapper.insert(planTask);
            }
        }
        // 是否生成任务
        this.addTaskByPlan(patrolPlan, planSuperintendentList, planTaskList, LocalDateTime.now());
        return true;
    }

    /**
     * 计划生成任务v1.2
     *
     * @param plan               计划
     * @param superintendentList 计划人员
     * @param planTaskList       计划的任务
     * @param newTime            指定时间
     *
     * @return 是否成功
     */
    public Boolean addTaskByPlan(PatrolPlan plan, List<PatrolPlanSuperintendent> superintendentList, List<PatrolPlanTask> planTaskList,
                                 LocalDateTime newTime
                                ) {
        // 判断当天是否已经生成过任务
        if (ObjectUtils.isNotEmpty(plan.getLastDate()) && DateUtils.isSameDay(newTime, plan.getLastDate())) {
            return false;
        }
        // 判断计划是否已过期
        if (DateUtils.compare(newTime, plan.getEndData()) > 0) {
            plan.setStatus(PlanStatusEnum.END.name());
            baseMapper.updateById(plan);
            return false;
        }
        // 计划是否开启
        if (!PlanStatusEnum.ENABLE.name().equals(plan.getStatus())) {
            return false;
        }
        // 是否在计划生效时间内
        if (!DateUtils.isIn(newTime, plan.getStartData(), plan.getEndData())) {
            return false;
        }
        // 是否是计划中的日期
        boolean dayBool = false;

        if ("day".equals(plan.getPatrolDataType())) {
            dayBool = true;
        } else {
            if (StringUtils.isBlank(plan.getPatrolData())) {
                return false;
            }
            List<String> patrolDataList = Arrays.asList(plan.getPatrolData().split(","));
            boolean month = "month".equals(plan.getPatrolDataType()) && patrolDataList.contains(String.valueOf(newTime.getDayOfMonth()));
            int dayOfWeek = newTime.getDayOfWeek().getValue();
            boolean week = "week".equals(plan.getPatrolDataType()) && patrolDataList.contains(String.valueOf(dayOfWeek));
            if (month || week) {
                dayBool = true;
            }
        }
        if (!dayBool) {
            return false;
        }

        // 是否有任务
        if (planTaskList == null || planTaskList.isEmpty()) {
            return false;
        }
        // 遍历任务
        int i = 1;
        boolean planExecute = false;
        for (PatrolPlanTask planTask : planTaskList) {
            // 是否已过任务生效时间范围
            String timeRange = planTask.getTimeRange();
            if (StringUtils.isBlank(timeRange)) {
                continue;
            }
            String[] timeRanges = timeRange.split("-");
            String dateStr = DateUtils.formatDate(newTime);
            var taskRangeStart = DateUtils.parse(dateStr + " " + timeRanges[0] + ":00");
            var taskRangeEnd = DateUtils.parse(dateStr + " " + timeRanges[1] + ":00");
            // 如果结束时间小于开始时间，视为第二天
            if (taskRangeEnd.isBefore(taskRangeStart)) {
                taskRangeEnd = DateUtils.offsetDay(taskRangeEnd, 1);
            }
            // 判断任务时间范围
            if (DateUtils.compare(LocalDateTime.now(), taskRangeEnd) >= 0) {
                continue;
            }
            // 是否在执行时间段
            boolean periodBool = false;
            String executePeriod = planTask.getExecutePeriod();
            if (StringUtils.isBlank(executePeriod)) {
                continue;
            }
            List<String> executePeriodList = JSON.parseArray(executePeriod, String.class);
            for (String period : executePeriodList) {
                if (StringUtils.isBlank(period)) {
                    continue;
                }
                String[] periods = period.split("-");
                var periodEnd = DateUtils.parse(DateUtils.formatDate(newTime) + " " + periods[1] + ":00");
                if (periodEnd.isBefore(taskRangeStart)) {
                    periodEnd = DateUtils.offsetDay(periodEnd, 1);
                }
                if (DateUtils.compare(newTime, periodEnd) < 0) {
                    periodBool = true;
                    break;
                }
            }
            if (periodBool) {
                this.generateTaskByPlan(plan, superintendentList, planTask, newTime, i);
                planExecute = true;
                i++;
            }
        }
        // 如果有生成任务需要更新计划
        if (planExecute) {
            // 如果是单次需要改为结束
            if ("one".equals(plan.getPatrolCycle())) {
                plan.setStatus(PlanStatusEnum.END.name());
            }
            plan.setLastDate(newTime);
            baseMapper.updateById(plan);
        }
        return true;
    }

    /**
     * 根据计划生成任务
     */
    private PatrolTaskParam generateTaskByPlan(PatrolPlan plan, List<PatrolPlanSuperintendent> superintendentList, PatrolPlanTask planTask,
                                               LocalDateTime date, Integer i
                                              ) {
        log.info("根据计划生成任务:addTaskByPlan");
        PatrolTaskParam task = ConvertUtils.convert(PatrolTaskParam.class, plan);
        List<PatrolPlanTaskParam> taskList = new ArrayList<>();
        PatrolPlanTaskParam taskPeriod = new PatrolPlanTaskParam();
        taskPeriod.setTimeRange(planTask.getTimeRange());
        taskPeriod.setIntervalNumber(planTask.getIntervalNumber());
        taskPeriod.setIntervalUnit(planTask.getIntervalUnit());
        taskPeriod.setExecutePeriod(JSON.parseArray(planTask.getExecutePeriod(), String.class));
        taskList.add(taskPeriod);

        task.setExecuteData(planTask.getTimeRange());
        task.setTaskName(plan.getPlanName() + DateUtils.format(date, "MMdd") + String.format("%02d", i));
        task.setPlanId(plan.getId());
        if (CollectionUtils.isNotEmpty(superintendentList)) {
            var superiontendList = superintendentList.stream()
                    .map(p -> PatrolParticipateParam.builder().participateType(p.getSuperintendentType())
                            .targetId(p.getTargetId()).build())
                    .collect(Collectors.toList());

            var exceptionUserIdList = Stream.of(plan.getExceptionUserIds().split(","))
                    .map(id -> PatrolParticipateParam.builder().participateType(ConditionTypeEnum.USER.name())
                            .targetId(Long.valueOf(id)).build())
                    .collect(Collectors.toList());
            task.setConditionParticipate(PatrolConditionParticipateParam.builder().participateList(superiontendList)
                                                 .abnormalUserList(exceptionUserIdList)
                                                 .conditionType(plan.getConditionType()).build());
        }
        if (StringUtils.isNotBlank(plan.getUserId())) {
            List<UserDTO> userIds = new ArrayList<>();
            Arrays.stream(plan.getUserId().split(",")).forEach(ex -> {
                UserDTO user = new UserDTO();
                user.setId(Long.parseLong(ex));
                userIds.add(user);
            });
            task.setUserIds(userIds);
        }
        List<PatrolPlanLocation> locationList = planLocationMapper.selectList(new LambdaQueryWrapper<PatrolPlanLocation>()
                                                                                      .eq(PatrolPlanLocation::getPlanId, plan.getId()));
        List<Long> locationIds = locationList.stream().map(PatrolPlanLocation::getLocationId).collect(Collectors.toList());
        task.setLocationIdList(locationIds);
        task.setStartDate(date);
        task.setCreateBy(plan.getCreateBy());
        task.setPatrolWayList(Arrays.asList(plan.getPatrolWay().split(",")));
        task.setTaskList(taskList);
        taskService.addTask(task);
        return task;
    }

    /**
     * 定时任务生成计划
     *
     * @param nowTime 时间
     *
     * @return 结果
     */
    @Override
    public boolean jobGenerateTask(LocalDateTime nowTime) {
        int dayOfWeek = nowTime.getDayOfWeek().getValue();
        int dayOfMonth = nowTime.getDayOfMonth();
        List<PatrolPlan> planList = new ArrayList<>();
        // 按天生成任务
        List<PatrolPlan> planDayList = this.baseMapper.selectList(new LambdaQueryWrapper<PatrolPlan>()
                                                                          .eq(PatrolPlan::getStatus, PlanStatusEnum.ENABLE.name())
                                                                          .lt(PatrolPlan::getStartData, nowTime)
                                                                          .gt(PatrolPlan::getEndData, nowTime)
                                                                          .eq(PatrolPlan::getPatrolDataType, "day")
                                                                 );
        // 按周生成任务
        int finalDayOfWeek = dayOfWeek;
        List<PatrolPlan> planWeekList = this.baseMapper.selectList(new LambdaQueryWrapper<PatrolPlan>()
                                                                           .eq(PatrolPlan::getStatus, PlanStatusEnum.ENABLE.name())
                                                                           .lt(PatrolPlan::getStartData, nowTime)
                                                                           .eq(PatrolPlan::getPatrolDataType, "week")
                                                                           .and(i -> i
                                                                                   .eq(PatrolPlan::getPatrolData, String.valueOf(finalDayOfWeek))
                                                                                   .or().likeRight(PatrolPlan::getPatrolData,
                                                                                                   finalDayOfWeek + ",").or()
                                                                                   .like(PatrolPlan::getPatrolData,
                                                                                         "," + finalDayOfWeek + ",").or()
                                                                                   .likeLeft(PatrolPlan::getPatrolData,
                                                                                             "," + finalDayOfWeek))
                                                                  );
        // 按月生成任务
        List<PatrolPlan> planMonthList = this.baseMapper.selectList(new LambdaQueryWrapper<PatrolPlan>()
                                                                            .eq(PatrolPlan::getStatus, PlanStatusEnum.ENABLE.name())
                                                                            .lt(PatrolPlan::getStartData, nowTime)
                                                                            .eq(PatrolPlan::getPatrolDataType, "month")
                                                                            .and(i -> i
                                                                                    .eq(PatrolPlan::getPatrolData, String.valueOf(dayOfMonth))
                                                                                    .or()
                                                                                    .likeRight(PatrolPlan::getPatrolData, dayOfMonth + ",")
                                                                                    .or()
                                                                                    .like(PatrolPlan::getPatrolData, "," + dayOfMonth + ",")
                                                                                    .or()
                                                                                    .likeLeft(PatrolPlan::getPatrolData, "," + dayOfMonth))
                                                                   );
        planMonthList.addAll(planWeekList);
        if (ObjectUtils.isNotEmpty(planDayList)) {
            planList.addAll(planDayList);
        }
        if (ObjectUtils.isNotEmpty(planWeekList)) {
            planList.addAll(planWeekList);
        }
        if (ObjectUtils.isNotEmpty(planMonthList)) {
            planList.addAll(planMonthList);
        }
        for (PatrolPlan patrolPlan : planList) {
            List<PatrolPlanTask> planTasks = patrolPlanTaskMapper.selectList(Wrappers.<PatrolPlanTask>lambdaQuery()
                                                                                     .eq(PatrolPlanTask::getPlanId, patrolPlan.getId())
                                                                            );
            var superintendentList = planSuperintendentService.lambdaQuery().eq(PatrolPlanSuperintendent::getMasterId, patrolPlan.getId())
                    .list();
            SessionContextHolder.requestTenant(patrolPlan.getTenant());
            Principal principal = Principal.builder()
                    .id(patrolPlan.getVisibleUserId())
                    .account(patrolPlan.getCreateBy())
                    .name(patrolPlan.getCreateBy())
                    .type(Principal.Type.USER)
                    .build();
            SecurityContextHolder.setPrincipal(principal);
            this.addTaskByPlan(patrolPlan, superintendentList, planTasks, nowTime);
        }
        // 更新过期的计划
        List<String> status = new ArrayList<>();
        status.add(PlanStatusEnum.STOP.name());
        status.add(PlanStatusEnum.ENABLE.name());
        this.lambdaUpdate().set(PatrolPlan::getStatus, PlanStatusEnum.END.name())
                .in(PatrolPlan::getStatus, status)
                .lt(PatrolPlan::getEndData, LocalDateTime.now())
                .update();
        return true;
    }

    /**
     * 是否存在计划名称
     *
     * @param planName 计划名称
     * @param planId   计划id(排除id)
     *
     * @return true表示已经存在
     */
    @Override
    public Boolean existPatrolPlanName(String planName, Long planId) {
        if (StringUtils.isBlank(planName)) {
            return false;
        }
        List<PatrolPlan> list = this.list(Wrappers.<PatrolPlan>lambdaQuery()
                                                  .select(PatrolPlan::getId).eq(PatrolPlan::getPlanName, planName)
                                                  .ne(PatrolPlan::getStatus, DeleteStatusEnum.REMOVE)
                                                  .ne(planId != null, PatrolPlan::getId, planId)
                                         );
        return !CollectionUtils.isEmpty(list);
    }

    private List<UserDTO> getUserList(List<String> userStringIdList) {
        List<Long> userIds = userStringIdList.stream().map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return CollectionUtils.emptyList();
        }
        List<UserDTO> userList = userClient.listUserByIds(userIds);
        userList.forEach(ex -> {
            List<UserDTO.Organization> orgList = new ArrayList<>();
            orgList.add(ex.getOrganization());
            ex.setOrganizations(orgList);
        });
        return userList;
    }

    private List<PatrolSuperintendentVO> getSuperintendentList(List<PatrolPlanSuperintendent> planSuperintendentList) {
        if (CollectionUtils.isEmpty(planSuperintendentList)) {
            return CollectionUtils.emptyList();
        }
        List<PatrolSuperintendentVO> resultList = new ArrayList<>();
        planSuperintendentList.stream().collect(Collectors.groupingBy(PatrolPlanSuperintendent::getSuperintendentType))
                .forEach((type, list) -> {
                    // ConditionTypeEnum
                    Set<Long> targetIds = list.stream().map(PatrolPlanSuperintendent::getTargetId).collect(Collectors.toSet());
                    switch (type) {
                        case "USER":
                            var userList = userClient.listUserByIds(targetIds);
                            resultList.addAll(Optional.ofNullable(userList).orElse(CollectionUtils.emptyList()).stream()
                                                      .map(dto -> PatrolSuperintendentVO.builder().participateType(type)
                                                              .targetId(dto.getId())
                                                              .targetName(dto.getName()).build()).collect(Collectors.toList()));
                            break;
                        case "DEPT":
                            var detpList = organizationClient.getOrganizationsByIds(new ArrayList<>(targetIds));
                            resultList.addAll(Optional.ofNullable(detpList).orElse(CollectionUtils.emptyList()).stream()
                                                      .map(dto -> PatrolSuperintendentVO.builder().participateType(type)
                                                              .targetId(dto.getId())
                                                              .targetName(dto.getName()).build()).collect(Collectors.toList()));
                            break;
                        case "ROLE":
                            var roleList = roleClient.listRolesByIds(new ArrayList<>(targetIds));
                            resultList.addAll(Optional.ofNullable(roleList).orElse(CollectionUtils.emptyList()).stream()
                                                      .map(dto -> PatrolSuperintendentVO.builder().participateType(type)
                                                              .targetId(dto.getId())
                                                              .targetName(dto.getName()).build()).collect(Collectors.toList()));
                            break;
                        case "POST":
                            AppointmentQueryParam appointmentQueryParam = new AppointmentQueryParam();
                            appointmentQueryParam.setIds(targetIds);
                            var postList = appointmentClient.getList(appointmentQueryParam);
                            resultList.addAll(Optional.ofNullable(postList).orElse(CollectionUtils.emptyList()).stream()
                                                      .map(dto -> PatrolSuperintendentVO.builder().participateType(type)
                                                              .targetId(dto.getId())
                                                              .targetName(dto.getName()).build()).collect(Collectors.toList()));
                            break;
                        case "GRID_AREA":
                            AreaQueryParam queryParam = new AreaQueryParam();
                            queryParam.setIds(targetIds);
                            var araList = areaClient.listInnerAreaPost(queryParam);
                            resultList.addAll(Optional.ofNullable(araList).orElse(CollectionUtils.emptyList()).stream()
                                                      .map(dto -> PatrolSuperintendentVO.builder().participateType(type)
                                                              .targetId(dto.getId())
                                                              .targetName(dto.getName()).build()).collect(Collectors.toList()));
                            break;
                        default:
                            break;
                    }
                });
        return resultList;
    }

    private UserDTO checkPlanUser(PatrolPlanParam param) {
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        Assert.isNotFound(user, "未获取到用户信息");
        Assert.isConflict(this.existPatrolPlanName(param.getPlanName(), param.getId()), "计划名称已存在！");
        var pb = param.getConditionParticipate() == null ||
                 CollectionUtils.isEmpty(param.getConditionParticipate().getParticipateList());
        var ab = param.getConditionParticipate() == null || CollectionUtils.isEmpty(param.getConditionParticipate().getAbnormalUserList());
        var ub = CollectionUtils.isEmpty(param.getUserIds());
        Assert.isParameterError((pb || ab) && ub, "巡更人员不允许为空!");
        return user;
    }
}
