package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.SiteBroadcastSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.GridAreaConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.InfoTypesEnum;
import com.guhecloud.cube.biz.ouaa.metadata.param.ConditionParam;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.srv.gen.attendance.management.client.AttSchedulingClient;
import com.guhecloud.cube.srv.gen.attendance.management.client.param.ArrangeWorkParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainUserClientService;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.MessageSendUtil;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.NfcDetailsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolRouteMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.ElectronicPatrolTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.NfcDetails;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.ExceptionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.AbnormalTaskRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.ElectronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolParticipateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.AbnormalTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolLocationDispatchingParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordExportParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskOperateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolRouteService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.CommonUtil;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.ElectronTaskTodoUtil;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.ElectronTaskWorkCenterUtil;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.PatrolDataPermissionUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.HomePagePatrolTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalListInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.AbnormalTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolRecordPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskPeriodVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskTimeQuantumVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 巡更任务
 * @date 2023/5/29 11:27
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskServiceImpl extends ServiceImpl<PatrolTaskMapper, PatrolTask> implements PatrolTaskService {
    private final PatrolRouteService patrolRouteService;

    private final PatrolTaskLocationService patrolTaskLocationService;
    private final MaintainUserClientService maintainUserClientService;
    private final OrganizationClientService organizationClientService;
    private final PatrolTaskStatusChangeServiceImpl patrolTaskStatusChangeService;
    private final PatrolLocationMapper locationMapper;
    private final PatrolTaskLocationMapper patrolTaskLocationMapper;
    private final NfcDetailsMapper nfcDetailsMapper;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final UserClient userClient;
    private final AttSchedulingClient attSchedulingClient;
    private final ElectronTaskTodoUtil electronTaskTodoUtil;
    private final ElectronTaskWorkCenterUtil electronTaskWorkCenterUtil;
    private final PatrolRouteMapper patrolRouteMapper;
    private final PatrolDataPermissionUtils patrolDataPermissionUtils;
    private final TodoTaskClient todoTaskClient;
    private final MsgRecordClient msgRecordClient;
    private final AreaClient areaClient;
    private final GridAreaConfigClient gridAreaConfigClient;
    private static final String USER_ID = "USER_ID";

    /**
     * @Author wql
     * @Description 新增巡更任务
     * @Date 11:31 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTask(PatrolTaskParam param) {
        Assert.isParameterError(param.getConditionParticipate() == null ||
                                CollectionUtils.isEmpty(param.getConditionParticipate().getParticipateList()), "巡更人员不允许为空!");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getConditionParticipate().getAbnormalUserList()), "兜底人员不允许为空!");
        PatrolRouteVO routeVO = patrolRouteService.queryById(param.getRouteId());
        Assert.isNotFound(ObjectUtils.isEmpty(routeVO), "没有查询到巡更路线!");
        List<PatrolPlanTaskParam> taskExecutePeriodList = param.getTaskList();
        if (taskExecutePeriodList == null || taskExecutePeriodList.isEmpty()) {
            throw new ServiceException("请设置任务执行时段！");
        }
        PatrolPlanTaskParam executePeriod = taskExecutePeriodList.get(0);
        if (StringUtils.isBlank(executePeriod.getTimeRange()) || executePeriod.getExecutePeriod() == null ||
            executePeriod.getExecutePeriod().isEmpty()) {
            throw new ServiceException("任务时段不能为空！");
        }
        param.setExecuteData(executePeriod.getTimeRange());
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        TaskTimeQuantumVO taskTimeQuantumVO = CommonUtil.cuntTaskTimeQuantum(param.getExecuteData(), param.getStartDate());

        /* 特殊处理网格区域(转换成人员id) */
        var participateParams = new ArrayList<ConditionParam>();
        var gridAreaId = new HashSet<Long>();
        param.getConditionParticipate().getParticipateList().forEach(item -> {
            if (ConditionTypeEnum.GRID_AREA.name().equals(item.getParticipateType())) {
                gridAreaId.add(item.getTargetId());
            } else {
                ConditionParam conditionParam = new ConditionParam();
                conditionParam.setConditionTypeEnum(ConditionTypeEnum.valueOf(item.getParticipateType()));
                conditionParam.setTargetId(item.getTargetId());
                participateParams.add(conditionParam);
            }
        });
        if (CollectionUtils.isNotEmpty(gridAreaId)) {
            var gridUserIdMap = gridAreaConfigClient.getGridAreaConfigWithGridLeaderList(new ArrayList<>(gridAreaId));
            gridUserIdMap.values().forEach(manageIds -> manageIds.forEach(userId -> {
                var conditionParam = new ConditionParam();
                conditionParam.setConditionTypeEnum(ConditionTypeEnum.USER);
                conditionParam.setTargetId(userId);
                participateParams.add(conditionParam);
            }));
        }

        var ruleParamList = new ArrayList<UserRuleConditionParam>();
        UserRuleConditionParam ruleParam = new UserRuleConditionParam();
        ruleParam.setRuleId(IdWorker.getId());
        // 这里只过滤人员(部门等条件)，是否在岗需要去考勤获取任务执行时间的人员在岗情况
        ruleParam.setOnDuty(false);
        ruleParam.setParticipateParams(participateParams);
        ruleParam.setAbnormalUserIdList(param.getConditionParticipate().getAbnormalUserList().stream()
                                                .map(PatrolParticipateParam::getTargetId).collect(Collectors.toList()));
        ruleParamList.add(ruleParam);
        var userMap = userClient.findUserByRule(ruleParamList);
        List<Long> userIds = new ArrayList<>(userMap.getOrDefault(ruleParam.getRuleId(), new HashSet<>()));
        if (param.getOnDuty()) {
            ArrangeWorkParam arrangeWorkParam = new ArrangeWorkParam();
            arrangeWorkParam.setUserIds(userMap.getOrDefault(ruleParam.getRuleId(), new HashSet<>()));
            arrangeWorkParam.setStartDate(taskTimeQuantumVO.getStartDate());
            userIds = attSchedulingClient.getOnDutyPersonnel(arrangeWorkParam);
        }
        Assert.isNotFound(CollectionUtils.isEmpty(userIds), "没有查询到符合条件的巡更人员!");
        int locationNum =
                ObjectUtils.isNotEmpty(param.getLocationIdList()) ? param.getLocationIdList().size() : param.getLocationIds().size();
        var dayDate = taskTimeQuantumVO.getStartDate();
        List<PatrolTaskPeriodVO> periodTimeList = new ArrayList<>();
        for (String period : executePeriod.getExecutePeriod()) {
            PatrolTaskPeriodVO vo = new PatrolTaskPeriodVO();
            vo.setTitle(period);
            String[] periodStr = period.split("-");
            String startStr = periodStr[0];
            String endStr = periodStr[1];
            var startTime = DateUtils.parse(DateUtils.formatDate(dayDate) + " " + startStr + ":00");
            if (startTime.isBefore(dayDate)) {
                startTime = DateUtils.offsetDay(startTime, 1);
            }
            var endTime = DateUtils.parse(DateUtils.formatDate(dayDate) + " " + endStr + ":00");
            if (endTime.isBefore(dayDate)) {
                endTime = DateUtils.offsetDay(endTime, 1);
            }
            vo.setStartTime(startTime);
            vo.setEndTime(endTime);
            periodTimeList.add(vo);
        }
        PatrolTask build = PatrolTask.builder()
                .taskName(param.getTaskName())
                .executeData(executePeriod.getTimeRange())
                .startDate(taskTimeQuantumVO.getStartDate())
                .endDate(taskTimeQuantumVO.getEndDate())
                .planId(param.getPlanId())
                .onDuty(param.getOnDuty())
                .inOrder(param.getInOrder())
                .belonger(StringUtils.join(",", userIds))
                .routeId(param.getRouteId())
                .status(PatrolTaskStateEnum.TO_BE_RECEIVED.name())
                .patrolWay(String.join(",", param.getPatrolWayList()))
                .locationNum(locationNum * executePeriod.getExecutePeriod().size())
                .intervalNumber(executePeriod.getIntervalNumber())
                .intervalUnit(executePeriod.getIntervalUnit())
                .executePeriod(JSON.toJSONString(executePeriod.getExecutePeriod()))
                .visibleUserId(user.getId())
                .visibleOrgId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId())
                .executePeriodTime(JSON.toJSONString(periodTimeList))
                .build();
        if (ObjectUtils.isNotEmpty(param.getCreateBy())) {
            build.setCreateBy(param.getCreateBy());
        }
        this.save(build);
        List<Long> locationIds = ObjectUtils.isNotEmpty(param.getLocationIdList()) ? param.getLocationIdList()
                                                                                   : param.getLocationIds().stream()
                                         .map(PatrolLocationVO::getId).collect(Collectors.toList());
        Assert.isParameterError(ObjectUtils.isEmpty(locationIds), "巡更点不允许为空!");
        //生成任务对应的巡更记录
        patrolTaskLocationService.saveTaskLocation(build.getId(), build.getBelonger(), executePeriod.getExecutePeriod(), build
                .getPlanId(), locationIds, taskTimeQuantumVO.getStartDate());
        // 发送代办
        electronTaskTodoUtil.todoInsert(build, PatrolTaskStateEnum.TO_BE_RECEIVED);
        // 发送任务
        electronTaskWorkCenterUtil.workInsert(build, PatrolTaskStateEnum.TO_BE_RECEIVED);
    }

    /**
     * @Author wql
     * @Description 提交巡更结果
     * @Date 14:01 2023/5/29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitResult(PatrolTaskResultParam param) {
        //提交巡更结果
        Long taskId = patrolTaskLocationService.submitResult(param);
        //判断任务是否结束
        checkTaskIsFinish(taskId);
    }

    /**
     * @Author wql
     * @Description 判断任务是否结束
     * @Date 11:14 2023/5/31
     **/
    private void checkTaskIsFinish(Long taskId) {
        String status = "";
        //统计该任务还未巡更记录数
        Long num = patrolTaskLocationService.getNotInspectedNum(taskId);
        PatrolTask byId = this.getById(taskId);
        if (num == 0) {
            byId.setCompleteDate(LocalDateTime.now());
            //查询结果异常的巡更记录
            Long abnormalNum = patrolTaskLocationService.countAbnormalResultRecord(taskId, false);
            byId.setResult(abnormalNum == 0 ? PatrolResultEnum.NORMAL.name() : PatrolResultEnum.ABNORMAL.name());
            status = PatrolTaskStateEnum.FINISHED.name();
        } else {
            if (taskIsOutTime(taskId)) {
                //将剩余未巡更的记录改为超时异常
                patrolTaskLocationService.overtimeRecord(byId.getUserId(), taskId);
                byId.setResult(PatrolResultEnum.ABNORMAL.name());
                status = PatrolTaskStateEnum.FINISHED.name();
            }
        }
        this.updateById(byId);
        byId.setStatus(status);
        if (PatrolTaskStateEnum.FINISHED.name().equals(status)) {
            patrolTaskStatusChangeService.changeToFinish(taskId);
        }
        if (PatrolTaskStateEnum.FINISHED.name().equals(byId.getStatus())) {
            Set<Long> userIds = new HashSet<>();
            userIds.add(byId.getUserId());
            if (StringUtils.isNotBlank(byId.getAssistIds())) {
                Arrays.asList(byId.getAssistIds().split(",")).forEach(s -> {
                    userIds.add(Long.parseLong(s));
                });
            }
            electronTaskTodoUtil.todoComplete(byId, userIds, PatrolTaskStateEnum.IN_EXECUTION.name());
            electronTaskWorkCenterUtil.workComplete(byId, userIds, PatrolTaskStateEnum.IN_EXECUTION.name());
        }
        if (PatrolResultEnum.ABNORMAL.name().equals(byId.getResult())
            && PatrolTaskStateEnum.FINISHED.name().equals(byId.getStatus())) {
            sendAlarmMsg(byId);
            sendAbnormalRemind(byId);
        }
    }

    /**
     * @Author wql
     * @Description 发送异常消息
     * @Date 9:45 2023/6/1
     **/
    private void sendAbnormalRemind(PatrolTask byId) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", byId.getTaskName());
            MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                    .tenant(byId.getTenant())
                    .applicationCode(MaintainApplicationEnum.ELECTRONIC_PATROL.name())
                    .msgSceneCode("ELECTRON_TASK_ABNORMAL")
                    .businessId(String.valueOf(byId.getId()))
                    .tmpParams(paramMap)
                    .messageNotifyTargets(MessageSendUtil.getNotifyUserByIds(getTaskUsers(byId)))
                    .build();
            MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
            MessageSendResponse messageSendResponse = sendClient.sceneSend(message);
            log.info("巡更任务异常消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("巡更任务异常发送短信异常:", e);
        }
    }

    /**
     * @Author wql
     * @Description 获取任务通知人员
     * @Date 16:30 2023/6/30
     **/
    private Set<Long> getTaskUsers(PatrolTask task) {
        Long userId = task.getUserId();
        if (ObjectUtils.isNotEmpty(userId)) {
            return Sets.newHashSet(userId);
        }
        Set<Long> set = new HashSet<>();
        String belonged = task.getBelonger();
        if (StringUtils.isNotBlank(belonged)) {
            String[] split = belonged.split(",");
            for (String s : split) {
                set.add(Long.parseLong(s));
            }
        }
        return set;
    }

    /**
     * @Author wql
     * @Description 发送预警消息
     * @Date 10:21 2023/5/31
     **/
    private void sendAlarmMsg(PatrolTask byId) {
        try {
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            SiteBroadcastSendRequest message = SiteBroadcastSendRequest.builder()
                    .msgType("PATROL_TASK_ABNORMAL_MESSAGE")
                    .businessPrincipal(byId)
                    .content("您有一条巡更任务异常信息，请及时前往查看")
                    .tenant(tenant)
                    .build();
            SiteMessageSendClient sendClient = ApplicationContextHolder.getBean(SiteMessageSendClient.class);
            MessageSendResponse messageSendResponse = sendClient.broadcastSend(message);
            log.info("指挥调度巡更任务异常消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("指挥调度巡更任务异常发送短信异常:", e);
        }
    }

    /**
     * @Author wql
     * @Description 判断任务是否超时
     * @Date 16:50 2023/5/29
     **/
    private Boolean taskIsOutTime(Long taskId) {
        PatrolTask byId = this.getById(taskId);
        return DateUtils.compare(byId.getEndDate(), LocalDateTime.now()) < 0;
    }

    /**
     * @Author wql
     * @Description 分页查巡更任务
     * @Date 14:58 2023/5/29
     **/
    @Override
    public Pagination<PatrolTaskPageVO> queryTaskPage(PatrolTaskPageParam param) {
        Period period = param.getPeriod();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        LambdaQueryChainWrapper<PatrolTask> wrapper = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getTaskState()), PatrolTask::getStatus, param.getTaskState())
                .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                        , ObjectUtils.ifNull(period, Period::getEnd))
                .like(StringUtils.isNotBlank(param.getKeyword()), PatrolTask::getTaskName, param.getKeyword())
                .orderByDesc(PatrolTask::getCreateTime);
        patrolDataPermissionUtils.setDataPermission(wrapper, StringUtils.isNotBlank(param.getTaskState()) ? param.getTaskState() : "total");
        // 巡更点id条件
        if (param.getLocationId() != null) {
            List<Long> taskIdList = new ArrayList<>();
            List<PatrolTaskLocation> taskLocationList = patrolTaskLocationMapper.selectList(Wrappers.<PatrolTaskLocation>lambdaQuery()
                                                                                                    .eq(PatrolTaskLocation::getLocationId, param
                                                                                                            .getLocationId())
                                                                                           );
            if (taskLocationList != null && !taskLocationList.isEmpty()) {
                taskIdList = taskLocationList.stream().map(PatrolTaskLocation::getTaskId).filter(Objects::nonNull).distinct()
                        .collect(Collectors.toList());
            }
            if (taskIdList.isEmpty()) {
                taskIdList.add(0L);
            }
            wrapper.in(PatrolTask::getId, taskIdList);
        }

        Page<PatrolTask> taskPage = wrapper.page(PagingUtils.convert(param));
        List<PatrolTaskPageVO> list = new ArrayList<>();
        Map<String, UserDTO> userIdsMap = this.getTaskUser(taskPage.getRecords(), USER_ID);
        Map<String, UserDTO> userNamesMap = this.getTaskUser(taskPage.getRecords(), null);
        // 巡更路线名称map
        Map<Long, String> routeNameMap = new HashMap<>();
        var taskIds = new ArrayList<Long>();
        if (!CollectionUtils.isEmpty(taskPage.getRecords())) {
            Set<Long> routeIdSet = new HashSet<>();
            taskPage.getRecords().forEach(patrolTask -> {
                taskIds.add(patrolTask.getId());
                routeIdSet.add(patrolTask.getRouteId());
            });
            List<PatrolRoute> routeList = patrolRouteMapper.selectList(Wrappers.<PatrolRoute>lambdaQuery()
                                                                               .in(PatrolRoute::getUuid, routeIdSet)
                                                                               .groupBy(PatrolRoute::getUuid)
                                                                      );
            if (!CollectionUtils.isEmpty(routeList)) {
                routeList.forEach(patrolRoute -> routeNameMap.put(patrolRoute.getUuid(), patrolRoute.getPatrolRouteName()));
            }
        }
        // 任务可分发点的数量map
        List<Map<String, Object>> countList = patrolTaskLocationMapper.userTaskLocationCount(userId, true, taskIds);
        Map<Long, Long> countMap = countList.stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("key")).longValue(),
                        map -> ((Number) map.get("value")).longValue()
                                         ));
        taskPage.getRecords().forEach(task -> {
            // 当前登录人是否是任务负责(第一个人为任务的负责人)
            boolean loginBelonger = StringUtils.isNotBlank(task.getBelonger()) && task.getBelonger().startsWith(String.valueOf(userId));
            boolean distribution = countMap.getOrDefault(task.getId(), 0L) > 0;
            String routeName = task.getRouteId() == null ? "" : routeNameMap.get(task.getRouteId());
            PatrolTaskPageVO patrolTaskPageVO = PatrolTaskPageVO.builder()
                    .id(task.getId())
                    .taskName(task.getTaskName())
                    .publisherTime(task.getCreateTime())
                    .executeData(task.getExecuteData())
                    .patrolRouteName(routeName == null ? "" : routeName)
                    .patrolLocationNum(task.getLocationNum())
                    .userId(task.getUserId())
                    .status(task.getStatus())
                    .startDate(task.getStartDate())
                    .result(task.getResult())
                    .endDate(task.getEndDate())
                    .loginBelonger(loginBelonger)
                    .assistIds(task.getAssistIds())
                    .startDate(task.getStartDate())
                    .endDate(task.getEndDate())
                    .inOrder(task.getInOrder())
                    .distribution(loginBelonger && distribution)
                    .build();
            this.taskExtend(patrolTaskPageVO, task, userIdsMap, userNamesMap);
            list.add(patrolTaskPageVO);
        });
        return PagingUtils.convert(taskPage, list);
    }

    /**
     * @Author wql
     * @Description 查询前先更新下任务状态
     * @Date 11:07 2023/5/31
     **/
    public void updateTaskStatus(LocalDateTime date) {
        log.info("检查任务状态");
        // 更新记录的状态
        patrolTaskLocationMapper.update(null, Wrappers.<PatrolTaskLocation>lambdaUpdate()
                                                .set(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL.name())
                                                .set(PatrolTaskLocation::getExceptionType, ExceptionTypeEnum.LEAK_DETECTION.name())
                                                .le(PatrolTaskLocation::getEndDate, date)
                                                .isNull(PatrolTaskLocation::getPatrolDate)
                                                .isNull(PatrolTaskLocation::getResult)
                                       );
        List<PatrolTask> list = this.lambdaQuery()
                .le(PatrolTask::getEndDate, date)
                .ne(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                .list();
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(task -> {
            SessionContextHolder.requestTenant(task.getTenant());
            checkTaskIsFinish(task.getId());
        });
    }

    @Override
    public List<ElectronicPatrolTaskDataModel> getSafetyManagementDataModel(Period period) {
        List<PatrolTask> tasks = this.baseMapper.getPatrolTaskCollectData(SessionContextHolder.getRequester().getTenant(), period);
        List<ElectronicPatrolTaskDataModel> models = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(tasks)) {
            List<Long> routeIds = tasks.stream().map(PatrolTask::getRouteId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            Map<Long, String> routeIdMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(routeIds)) {
                routeIdMap = Optional.ofNullable(patrolRouteService.lambdaQuery()
                                                         .select(PatrolRoute::getUuid, PatrolRoute::getPatrolRouteName)
                                                         .in(PatrolRoute::getUuid, routeIds)
                                                         .list())
                        .orElse(Collections.emptyList())
                        .stream().collect(Collectors.toMap(PatrolRoute::getUuid, PatrolRoute::getPatrolRouteName, (k1, k2) -> k1));
            }
            Map<Long, String> finalRouteIdMap = routeIdMap;
            tasks.stream().forEach(task -> {
                models.add(ElectronicPatrolTaskDataModel.builder()
                                   .id(task.getId())
                                   .taskName(task.getTaskName())
                                   .taskCreateTime(task.getCreateTime())
                                   .executeData(task.getExecuteData())
                                   .statusCode(task.getStatus())
                                   .statusName(PatrolTaskStateEnum.valueOf(task.getStatus()).getRemarks())
                                   .resultCode(task.getResult())
                                   .resultName(
                                           StringUtils.isBlank(task.getResult()) ? ""
                                                                                 : PatrolResultEnum.valueOf(task.getResult()).getRemarks())
                                   .routeName(finalRouteIdMap.get(task.getRouteId()))
                                   .locationNum(task.getLocationNum())
                                   .userName(task.getUserName())
                                   .updateTime(task.getUpdateTime())
                                   .createTime(task.getCreateTime())
                                   .tenant(task.getTenant())
                                   .createBy(task.getCreateBy())
                                   .creatorId(task.getCreatorId())
                                   .creatorType(task.getCreatorType())
                                   .updateBy(task.getUpdateBy())
                                   .updaterId(task.getUpdaterId())
                                   .updaterType(task.getUpdaterType())
                                   .deleted(task.getDeleted())
                                   .build());
            });
        }
        return models;
    }

    @Override
    public Set<Integer> getAllTenant() {
        List<PatrolTask> list = this.lambdaQuery().select(PatrolTask::getTenant).list();
        Set<Integer> set = new HashSet<>();
        if (ObjectUtils.isNotEmpty(list)) {
            set = list.stream().map(PatrolTask::getTenant).collect(Collectors.toSet());
        }
        return set;
    }

    @Override
    public PatrolTaskDataVO getLocationRecordByLocationId(Long locationId) {
        return patrolTaskLocationService.getNewestTask(locationId);
    }

    @Override
    public Pagination<AbnormalTaskVO> anomalyTaskPage(AbnormalTaskPageParam param) {
        Page<PatrolTask> page = this.lambdaQuery().eq(PatrolTask::getResult, PatrolResultEnum.ABNORMAL.name())
                .ge(PatrolTask::getStartDate, DateUtils.offsetDay(LocalDateTime.now(), -6))
                .eq(ObjectUtils.isNotNull(param.getAffirm()), PatrolTask::getAffirmState, param.getAffirm())
                .orderByDesc(PatrolTask::getUpdateTime).page(PagingUtils.convert(param));
        List<AbnormalTaskVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(vo -> {
                AbnormalTaskVO copying = ObjectUtils.copying(vo, AbnormalTaskVO.class);
                copying.setResult(PatrolResultEnum.valueOf(copying.getResult()).getRemarks());
                copying.setLocations(getTaskLocation(vo));
                vos.add(copying);
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public void taskAffirm(Long id) {
        this.lambdaUpdate().set(PatrolTask::getAffirmState, true).eq(PatrolTask::getId, id).update();
    }

    @Override
    public List<PatrolTaskDataVO> getAllLocation(LocationTaskParam param) {
        return patrolTaskLocationMapper.getTaskLocationByBluetooth(param.getBluetoothCodes(), SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<LocationRouteStatisticsVO> locationRouteStatistics(StatisticsQueryParam param) {
        List<LocationRouteStatisticsVO> statisticsVOS = this.baseMapper.locationRouteStatistics(param.getPeriod());
        if (ObjectUtils.isNotEmpty(statisticsVOS)) {
            Set<Long> collect = statisticsVOS.stream().map(LocationRouteStatisticsVO::getRouteId).collect(Collectors.toSet());
            List<PatrolRouteVO> locations = patrolRouteService.queryByUuids(collect);
            Map<Long, String> map = new HashMap<>();
            if (ObjectUtils.isNotEmpty(locations)) {
                map = locations.stream().collect(Collectors.toMap(PatrolRouteVO::getId, PatrolRouteVO::getPatrolRouteName));
            }
            for (LocationRouteStatisticsVO vo : statisticsVOS) {
                vo.setRouteName(map.get(vo.getRouteId()));
            }
        }
        return statisticsVOS;
    }

    @Override
    public TaskAbnormalNumStatisticsVO taskAbnormalNumStatistics(BaseDateStatisticsParam param) {
        List<PatrolTask> list = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), PatrolTask::getStartDate,
                                                           ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils
                                                                   .ifNull(param.getPeriod(), Period::getEnd))
                .select(PatrolTask::getId, PatrolTask::getAffirmState, PatrolTask::getResult).list();
        if (ObjectUtils.isNotEmpty(list)) {
            List<PatrolTask> abnormalTask = list.stream().filter(task -> PatrolResultEnum.ABNORMAL.name().equals(task.getResult()))
                    .collect(Collectors.toList());
            int affirm = (int) abnormalTask.stream().filter(PatrolTask::getAffirmState).count();
            return TaskAbnormalNumStatisticsVO.builder()
                    .abnormalNum(abnormalTask.size())
                    .finishedNum(affirm)
                    .undoNum(abnormalTask.size() - affirm)
                    .totalNum(list.size())
                    .abnormalRatio(MathUtils.calculateProportionPercentage(abnormalTask.size(), list.size()).floatValue())
                    .build();
        }
        return TaskAbnormalNumStatisticsVO.builder().abnormalNum(0).finishedNum(0).undoNum(0).build();
    }

    @Override
    public Pagination<TaskAbnormalListInfoVO> taskAbnormalListStatistics(AbnormalTaskRecordParam param) {
        Page<PatrolTask> page = this.lambdaQuery().between(ObjectUtils.isNotEmpty(param.getPeriod()), PatrolTask::getStartDate,
                                                           ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils
                                                                   .ifNull(param.getPeriod(), Period::getEnd))
                .eq(PatrolTask::getResult, PatrolResultEnum.ABNORMAL.name())
                .select(PatrolTask::getId, PatrolTask::getTaskName, PatrolTask::getExecuteData, PatrolTask::getStatus, PatrolTask::getUserName)
                .page(PagingUtils.convert(param));
        List<TaskAbnormalListInfoVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                vos.add(TaskAbnormalListInfoVO.builder()
                                .state(StringUtils.isBlank(record.getStatus()) ? ""
                                                                               : PatrolTaskStateEnum.getDescription(record.getStatus()))
                                .taskName(record.getTaskName())
                                .timePeriod(record.getExecuteData())
                                .username(record.getUserName())
                                .taskId(record.getId())
                                .build());
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public HomePagePatrolTaskVO getElectronTaskHomePageStatistics(BaseDateStatisticsParam pram) {
        Assert.parameterIsNull(pram.getPeriod(), "请选则时间范围");
        PatrolTaskPageParam patrolTaskPageParam = new PatrolTaskPageParam();
        patrolTaskPageParam.setPeriod(pram.getPeriod());
        patrolTaskPageParam.setAdmin(pram.getAdmin());
        PatrolOverviewVO overview = this.overview(patrolTaskPageParam);
        //异常数
        LambdaQueryChainWrapper<PatrolTaskLocation> abnormalWrapper = patrolDataPermissionUtils.setDataPermissionPatrolTaskLocation(patrolTaskLocationService.lambdaQuery());
        List<PatrolTaskLocation> abnormalList = abnormalWrapper
                .eq(PatrolTaskLocation::getResult, PatrolResultEnum.ABNORMAL)
                .between(ObjectUtils.isNotNull(pram.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                        .ifNull(pram.getPeriod(), Period::getBegin), ObjectUtils.ifNull(pram.getPeriod(), Period::getEnd))
                .select(PatrolTaskLocation::getTaskId)
                .groupBy(PatrolTaskLocation::getTaskId).list();
        Integer abnormalNum = ObjectUtils.isEmpty(abnormalList) ? 0 : abnormalList.size();
        List<Long> abnormalIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(abnormalList)) {
            abnormalIdList = abnormalList.stream()
                    .map(PatrolTaskLocation::getTaskId).distinct().collect(Collectors.toList());
        }
        //正常数
        LambdaQueryChainWrapper<PatrolTaskLocation> normalWrapper = patrolDataPermissionUtils.setDataPermissionPatrolTaskLocation(patrolTaskLocationService.lambdaQuery());

        List<PatrolTaskLocation> normalList = normalWrapper
                .eq(PatrolTaskLocation::getResult, PatrolResultEnum.NORMAL)
                .notIn(CollectionUtils.isNotEmpty(abnormalList), PatrolTaskLocation::getTaskId, abnormalIdList)
                .between(ObjectUtils.isNotNull(pram.getPeriod()), PatrolTaskLocation::getStartDate, ObjectUtils
                        .ifNull(pram.getPeriod(), Period::getBegin), ObjectUtils.ifNull(pram.getPeriod(), Period::getEnd))
                .select(PatrolTaskLocation::getTaskId)
                .groupBy(PatrolTaskLocation::getTaskId).list();
        Integer normalNum = ObjectUtils.isEmpty(normalList) ? 0 : normalList.size();

        return HomePagePatrolTaskVO.builder()
                .total(overview.getTotal())
                .normal(normalNum)
                .abnormal(abnormalNum)
                .completeRate(MathUtils.calculateProportionPercentage(overview.getFinished(), overview.getTotal()).floatValue())
                .abnormalRate(MathUtils.calculateProportionPercentage(abnormalNum, overview.getTotal()).floatValue())
                .waitReceive(overview.getToBeReceived())
                .executing(overview.getInExecution())
                .finished(overview.getFinished())
                .build();
    }

    /**
     * 获取任务下巡更点的区域
     *
     * @param taskId 任务id
     *
     * @return 区域列表
     */
    @Override
    public List<AreaDTO> getTaskLocationAreaList(Long taskId) {
        var task = this.getById(taskId);
        Assert.isNotFound(task, "该任务不存在!");
        // 获取任务下的巡更点
        var locationList = patrolTaskLocationService.lambdaQuery().select(PatrolTaskLocation::getLocationId)
                .eq(PatrolTaskLocation::getTaskId, taskId)
                .groupBy(PatrolTaskLocation::getLocationId)
                .list();
        if (CollectionUtils.isEmpty(locationList)) {
            return Collections.emptyList();
        }
        // 获取巡更点信息(这里才有区域id)
        var locationIds = locationList.stream().map(PatrolTaskLocation::getLocationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(locationIds)) {
            return Collections.emptyList();
        }
        var patrolLocationList = locationMapper.selectList(Wrappers.<PatrolLocation>lambdaQuery().select(PatrolLocation::getAreaId)
                                                                   .in(PatrolLocation::getId, locationIds)
                                                                   .groupBy(PatrolLocation::getAreaId));
        if (CollectionUtils.isEmpty(patrolLocationList)) {
            return Collections.emptyList();
        }
        // 获取区域信息
        var areaIdList = patrolLocationList.stream().map(p -> {
                    if (StringUtils.isNotBlank(p.getAreaId())) {
                        var areaIds = p.getAreaId().split(",");
                        return Long.valueOf(areaIds[areaIds.length - 1]);
                    }
                    return null;
                })
                .filter(Objects::nonNull) // 过滤 null
                .collect(Collectors.toSet());
        return areaClient.getByIds(areaIdList);
    }

    /**
     * @Author wql
     * @Description 设置巡更任务巡更点信息
     * @Date 15:59 2023/10/26
     **/
    private List<TaskLocationVO> getTaskLocation(PatrolTask task) {
        List<TaskLocationVO> vos = patrolTaskLocationMapper.getTaskLocation(task.getId());
        if (ObjectUtils.isNotEmpty(vos)) {
            vos.forEach(vo -> {
                vo.setResultValue(PatrolResultEnum.valueOf(vo.getResult()).getRemarks());
                vo.setUsername(organizationClientService.getUserName(vo.getUserId()));
            });
        }
        if (!task.getInOrder()) {
            List<TaskLocationVO> haveDone = vos.stream().filter(vo -> ObjectUtils.isNotNull(vo.getPatrolDate()))
                    .sorted(Comparator.comparing(TaskLocationVO::getPatrolOrder))
                    .collect(Collectors.toList());
            List<TaskLocationVO> undone = vos.stream().filter(vo -> ObjectUtils.isNull(vo.getPatrolDate()))
                    .sorted(Comparator.comparing(TaskLocationVO::getOrderNum))
                    .collect(Collectors.toList());
            haveDone.addAll(undone);
            return haveDone;
        }
        return vos;
    }

    /**
     * @Author wql
     * @Description 巡更记录分页查询
     * @Date 18:21 2023/5/29
     **/
    @Override
    public Pagination<PatrolRecordPageVO> queryRecordPage(PatrolRecordPageParam param) {
        Period period = param.getPeriod();
        LambdaQueryChainWrapper<PatrolTask> queryChainWrapper = this.lambdaQuery()
                .eq(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                .eq(ObjectUtils.isNotNull(param.getDepId()), PatrolTask::getUserDepId, param.getDepId())
                .in(param.getLocationId() != null, PatrolTask::getId, getTaskIdByLocationId(param.getLocationId()))
                .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                        , ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> {
                    q.like(PatrolTask::getTaskName, param.getKeyword()).or()
                            .like(PatrolTask::getUserName, param.getKeyword());
                })
                .orderByDesc(PatrolTask::getStartDate);

        Page<PatrolTask> taskPage = patrolDataPermissionUtils.setDataPermission(queryChainWrapper, "total")
                .page(PagingUtils.convert(param));

        List<PatrolRecordPageVO> list = this.extend(taskPage.getRecords());
        return PagingUtils.convert(taskPage, list);
    }

    @Override
    public void queryRecordExport(PatrolRecordExportParam param, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        Period period = param.getPeriod();
        LambdaQueryChainWrapper<PatrolTask> queryChainWrapper = this.lambdaQuery()
                .eq(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                .eq(ObjectUtils.isNotNull(param.getDepId()), PatrolTask::getUserDepId, param.getDepId())
                .in(param.getLocationId() != null, PatrolTask::getId, getTaskIdByLocationId(param.getLocationId()))
                .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                        , ObjectUtils.ifNull(period, Period::getEnd))
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> {
                    q.like(PatrolTask::getTaskName, param.getKeyword()).or()
                            .like(PatrolTask::getUserName, param.getKeyword());
                })
                .orderByDesc(PatrolTask::getStartDate);
        List<PatrolTask> tasks = patrolDataPermissionUtils.setDataPermission(queryChainWrapper, "total").list();
        List<PatrolRecordPageVO> list = this.extend(tasks);
        Assert.parameterIsNull(list, "没有巡更记录,无法导出");
        try {

            EasyExcelFactory.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .head(PatrolRecordPageVO.class)
                    .sheet("巡更记录")
                    .doWrite(list);
        } catch (IOException e) {
            log.error("电子巡更记录导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    private List<Long> getTaskIdByLocationId(Long locationId) {
        return patrolTaskLocationService.getTaskIdByLocationId(locationId);
    }

    @Override
    public void taskStartMessage(Long conditionId, LocalDateTime time, Integer value) {
        log.info("电子巡更任务开始发送短信,配置id：{};时间：{}", conditionId, time);
        List<PatrolTask> taskList = baseMapper.selectList(new LambdaQueryWrapper<PatrolTask>()
                                                                  .eq(PatrolTask::getStartDate, time)
                                                                  .eq(PatrolTask::getStatus, PatrolTaskStateEnum.TO_BE_RECEIVED.name()));
        taskList.forEach(ex -> {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", ex.getTaskName());
            paramMap.put("taskTime", value.toString());
            MessageSendResponse messageSendResponse = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                                           .notifyId(conditionId)
                                                                                           .tempParams(paramMap)
                                                                                           .notifyUsers(getTaskBelong(ex))
                                                                                           .businessId(String.valueOf(ex.getId()))
                                                                                           .build());
        });
    }

    /**
     * @Author wql
     * @Description 更新任务为待接收
     * @Date 9:22 2023/10/27
     **/
    public void changeToWaitAccept(LocalDateTime time) {
        List<PatrolTask> taskList = baseMapper.selectList(new LambdaQueryWrapper<PatrolTask>()
                                                                  .eq(PatrolTask::getStartDate, time));
        taskList.forEach(task -> {
            try {
                //配置有消息提醒时，发送消息时推送待办
                if (task.getOnDuty()) {
                    ArrangeWorkParam arrangeWorkParam = new ArrangeWorkParam();
                    Set<Long> set = new HashSet<>();
                    Arrays.stream(task.getBelonger().split(",")).mapToLong(Long::parseLong).forEach(set::add);
                    arrangeWorkParam.setUserIds(set);
                    arrangeWorkParam.setStartDate(task.getStartDate());
                    List<Long> userIds = attSchedulingClient.getOnDutyPersonnel(arrangeWorkParam);
                    String belonger = ObjectUtils.isEmpty(userIds) ? task.getBelonger()
                                                                   : userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                    task.setBelonger(belonger);
                    //按在岗人员更新任务归属人
                    this.updateById(task);
                }
                patrolTaskStatusChangeService.changeToWaitAccept(task.getId());
            } catch (Exception e) {
                log.error("电子巡更：更新任务为待接收失败", e);
            }
        });
    }

    @Override
    public void taskEndMessage(Long conditionId, LocalDateTime time, Integer value) {
        log.info("电子巡更任务到期发送短信,配置id：{};时间：{}", conditionId, time);
        List<PatrolTaskLocation> taskLocationList = patrolTaskLocationMapper.selectList(Wrappers.<PatrolTaskLocation>lambdaQuery()
                                                                                                .select(PatrolTaskLocation::getTaskId)
                                                                                                .eq(PatrolTaskLocation::getEndDate, time)
                                                                                                .isNull(PatrolTaskLocation::getResult)
                                                                                                .isNull(PatrolTaskLocation::getPatrolDate)
                                                                                                .groupBy(PatrolTaskLocation::getTaskId)
                                                                                       );
        if (taskLocationList == null || taskLocationList.isEmpty()) {
            return;
        }
        List<Long> taskIdList = taskLocationList.stream().map(PatrolTaskLocation::getTaskId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (taskIdList.isEmpty()) {
            return;
        }
        List<PatrolTask> taskList = baseMapper.selectList(new LambdaQueryWrapper<PatrolTask>()
                                                                  .in(PatrolTask::getId, taskIdList)
                                                                  .ne(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                                                         );
        if (taskList == null || taskList.isEmpty()) {
            return;
        }
        taskList.forEach(ex -> {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", ex.getTaskName());
            paramMap.put("taskTime", value.toString());
            assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                 .notifyId(conditionId)
                                                 .tempParams(paramMap)
                                                 .notifyUsers(getTaskBelong(ex))
                                                 .businessId(String.valueOf(ex.getId()))
                                                 .build());
        });
    }

    /**
     * @Author wql
     * @Description 根据芯片编码获取巡更记录信息
     * @Date 18:36 2023/6/2
     **/
    @Override
    public PatrolTaskDataVO getLocationRecordByNFC(String nfcCode) {
        NfcDetails nfcDetails = nfcDetailsMapper.selectOne(Wrappers.<NfcDetails>lambdaQuery()
                                                                   .eq(NfcDetails::getNfcCode, nfcCode)
                                                                   .ne(NfcDetails::getStatus, DeleteStatusEnum.REMOVE.name()));
        Assert.isNotFound(ObjectUtils.isEmpty(nfcDetails), "该NFC芯片不存在巡更设备");
        PatrolLocation location = locationMapper.selectOne(Wrappers.<PatrolLocation>lambdaQuery()
                                                                   .eq(PatrolLocation::getDeviceId, nfcDetails.getId())
                                                                   .ne(PatrolLocation::getStatus, DeleteStatusEnum.REMOVE.name()));
        Assert.isNotFound(location, "该NFC芯片不存在对应巡更点！");
        return patrolTaskLocationService.getNewestTask(location.getId());
    }

    /**
     * @Author wql
     * @Description 检查巡更任务是否过期
     * @Date 10:01 2023/6/6
     **/
    @Override
    public boolean checkTaskIsExpire() {
        return this.lambdaUpdate().lt(PatrolTask::getEndDate, LocalDateTime.now())
                .and(q -> {
                    q.eq(PatrolTask::getStatus, PatrolTaskStateEnum.TO_BE_RECEIVED.name())
                            .or().eq(PatrolTask::getStatus, PatrolTaskStateEnum.IN_EXECUTION.name());
                })
                .set(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name()).update();
    }

    @Override
    public boolean checkTaskIsExpire(Long taskId) {
        PatrolTask byId = this.getById(taskId);
        Assert.isNotFound(byId, "任务不存在！");
        return !PatrolTaskStateEnum.FINISHED.name().equals(byId.getStatus());
    }

    @Override
    public Boolean taskOverdue(Long taskId) {
        PatrolTask task = baseMapper.selectById(taskId);
        Boolean inDuration = DateUtils.isIn(LocalDateTime.now(), task.getStartDate(), task.getEndDate());
        Assert.isNotAcceptable(!inDuration, "巡检任务不在规定时间范围");
        return inDuration;
    }

    /**
     * @Author wql
     * @Description 获取任务归属人
     * @Date 18:39 2023/6/2
     **/
    private Set<Long> getTaskBelong(PatrolTask task) {
        if (task.getUserId() != null) {
            return Sets.newHashSet(task.getUserId());

        }
        Set<Long> set = new HashSet<>();
        Arrays.stream(task.getBelonger().split(",")).forEach(s -> {
            set.add(Long.parseLong(s));
        });
        return set;
    }

    /**
     * @Author wql
     * @Description 通过ID查询单条数据
     * @Date 15:36 2023/5/29
     **/
    @Override
    public PatrolTaskVO detail(Long id, Boolean executePeriodAll) {
        PatrolTask task = this.getById(id);
        Assert.isNotFound(task, "该任务不存在!");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = maintainUserClientService.getUser(task.getCreateBy(), true);
        String publisherName = "";
        String orgName = "";
        String assistNames = "";
        String userName = task.getUserName();
        if (ObjectUtils.isNotEmpty(user)) {
            publisherName = user.getName();
            orgName = ObjectUtils.isNotEmpty(user.getOrganization()) ? user.getOrganization().getName() : "";
        }
        if (ObjectUtils.isEmpty(userName) && ObjectUtils.isNotEmpty(task.getBelonger())) {
            Set<Long> belongerIds = new HashSet<>();
            String[] ids = task.getBelonger().split(",");
            for (String ex : ids) {
                belongerIds.add(Long.parseLong(ex));
            }
            UserMapQuery userMapQuery = new UserMapQuery();
            userMapQuery.setUserIds(belongerIds);
            Map<String, UserDTO> userIdsMap = userClient.getTenantUserMap(userMapQuery);
            for (String ex : ids) {
                UserDTO belongerUser = userIdsMap.get(ex);
                if (ObjectUtils.isNotEmpty(belongerUser)) {
                    userName = ObjectUtils.isNotEmpty(userName) && userName.length() > 1 ? userName + "," + belongerUser.getName()
                                                                                         : belongerUser.getName();
                }
            }
        }
        if (ObjectUtils.isNotEmpty(task.getAssistIds())) {
            Set<Long> assistIds = new HashSet<>();
            String[] ids = task.getAssistIds().split(",");
            for (String ex : ids) {
                assistIds.add(Long.parseLong(ex));
            }
            UserMapQuery userMapQuery = new UserMapQuery();
            userMapQuery.setUserIds(assistIds);
            Map<String, UserDTO> userIdsMap = userClient.getTenantUserMap(userMapQuery);
            for (String ex : ids) {
                UserDTO assistUser = userIdsMap.get(ex);
                if (ObjectUtils.isNotEmpty(assistUser)) {
                    assistNames = assistNames.length() > 1 ? assistNames + "," + assistUser.getName() : assistUser.getName();
                }
            }
        }
        Integer locationNum = task.getLocationNum();
        // 未巡更点数
        Long undoneNum = patrolTaskLocationService.getNotInspectedNum(id);
        // 巡更结果异常数
        Long abnormalNum = patrolTaskLocationService.countAbnormalResultRecord(id, true);
        PatrolRoute routeVO = patrolRouteMapper.selectOne(new LambdaQueryWrapper<PatrolRoute>()
                                                                  .eq(PatrolRoute::getUuid, task.getRouteId())
                                                                  .last("limit 1"));
        // 当前登录人是否是被分配的人
        boolean loginBelonger = StringUtils.isNotBlank(task.getBelonger()) && task.getBelonger().startsWith(String.valueOf(userId));
        // 任务可分发点的数量map
        List<Map<String, Object>> countList = patrolTaskLocationMapper.userTaskLocationCount(userId, true, Collections.singletonList(id));
        Map<Long, Long> countMap = countList.stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("key")).longValue(),
                        map -> ((Number) map.get("value")).longValue()
                                         ));
        boolean distribution = countMap.getOrDefault(task.getId(), 0L) > 0;
        /* 如果登陆人是任务负责人，就是全部的执行时间段。如果不是则需要获取当前登录人所属巡更点的时间段 */
        var ls = StringUtils.isNotBlank(task.getExecutePeriodTime()) ? JSON
                .parseArray(task.getExecutePeriodTime(), PatrolTaskPeriodVO.class) : new ArrayList<PatrolTaskPeriodVO>();
        List<String> executePeriod = new ArrayList<>(
                StringUtils.isNotBlank(task.getExecutePeriod()) ? JSON.parseArray(task.getExecutePeriod(), String.class)
                                                                : new ArrayList<>());
        List<PatrolTaskPeriodVO> executePeriodList = new ArrayList<>(ls);

        return PatrolTaskVO.builder()
                .id(task.getId())
                .taskName(task.getTaskName())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .executeData(task.getExecuteData())
                .patrolLocationNum(locationNum)
                .completedNum(locationNum - undoneNum)
                .undoneNum(undoneNum)
                .publisherName(publisherName)
                .publisherDepName(orgName)
                .status(task.getStatus())
                .completeDate(task.getCompleteDate())
                .routeName(routeVO.getPatrolRouteName())
                .inOrder(task.getInOrder())
                .userId(task.getUserId())
                .userName(userName)
                .loginBelonger(loginBelonger)
                .distribution(distribution)
                .affirmState(task.getAffirmState())
                .patrolWayList(
                        StringUtils.isBlank(task.getPatrolWay()) ? new ArrayList<>() : Arrays.asList((task.getPatrolWay().split(","))))
                .userDep(organizationClientService.getOrganizationName(task.getUserDepId()))
                .executePeriod(executePeriod)
                .untowardLocationNum(abnormalNum)
                .normalLocationNum(locationNum - undoneNum - abnormalNum)
                .executePeriodList(executePeriodList)
                .assistIds(task.getAssistIds())
                .assistNames(assistNames)
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .build();
    }

    /**
     * @Author wql
     * @Description 接收任务
     * @Date 16:11 2023/5/29
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void acceptTask(Long id) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        PatrolTask task = this.lambdaQuery().eq(PatrolTask::getId, id).like(PatrolTask::getBelonger, userId).one();
        Assert.isPermissionDenied(ObjectUtils.isEmpty(task), "该任务不属于自己!");
        Assert.isNotAcceptable(!PatrolTaskStateEnum.TO_BE_RECEIVED.name().equals(task.getStatus()), "该任务已接收!");
        Assert.isNotAcceptable(DateUtils.compare(task.getEndDate(), LocalDateTime.now()) <= 0, "该任务已结束!");
        UserDTO user = organizationClientService.getUserInfo(userId);
        Assert.parameterIsNull(user, "未找到当前人员信息");
        task.setUserId(userId);
        task.setUserName(user.getName());
        task.setUserDepId(user.getOrganization().getId());
        task.setBelonger(String.valueOf(userId));
        this.updateById(task);
        // 更新巡更点负责人
        patrolTaskLocationService.lambdaUpdate().set(PatrolTaskLocation::getSuperintendentUserId, userId)
                .set(PatrolTaskLocation::getUpdateTime, LocalDateTime.now()).set(PatrolTaskLocation::getUpdateBy, userId)
                .eq(PatrolTaskLocation::getTaskId, id).update();
        //接收任务将任务状态更新为执行中
        patrolTaskStatusChangeService.changeToInExecution(id);
        // 发送代办
        List<Long> userIds = new ArrayList<>();
        userIds.add(task.getUserId());
        electronTaskTodoUtil.todoRepeat(task, userIds);
    }

    @Override
    public void addSynergyUser(@NonNull Long taskId, @NonNull String userId) {
        Assert.isNotAcceptable(!checkTaskIsExpire(taskId), "该任务已结束!");
        PatrolTask taskPO = this.getById(taskId);
        Assert.isNotAcceptable(userId.contains(taskPO.getUserId().toString()), "不能添加巡更人为协同人");
        taskPO.setAssistIds(userId);
        this.baseMapper.updateById(taskPO);
        Set<Long> userIds = new HashSet<>();
        Arrays.asList(taskPO.getAssistIds().split(",")).forEach(s -> {
            userIds.add(Long.parseLong(s));
        });
        userIds.add(taskPO.getUserId());
        // 代办完成
        electronTaskTodoUtil.todoRepeat(taskPO, new ArrayList<>(userIds));
    }

    /**
     * @Author wql
     * @Description 移交任务
     * @Date 16:23 2023/5/29
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void turnOverTask(Long id, Long userId) {
        Assert.parameterIsNull(userId, "接收人不能为空!");
        Long oldUser = SecurityContextHolder.getPrincipal().getId();
        PatrolTask task = this.lambdaQuery().eq(PatrolTask::getId, id).like(PatrolTask::getUserId, oldUser).one();
        Assert.isNotAcceptable(ObjectUtils.isEmpty(task), "该任务不属于自己!");
        Assert.isNotAcceptable(
                ObjectUtils.isNotEmpty(task.getAssistIds()) && task.getAssistIds().contains(userId.toString()), "不能移交给协同人");
        Assert.isNotFound(DateUtils.compare(task.getEndDate(), LocalDateTime.now()) <= 0, "该任务已结束!");
        // 设置任务负责人
        var user = userClient.getUser(userId);
        Assert.isNotFound(user, "接收人不存在！");
        task.setUserId(userId);
        task.setUserName(user.getName());
        if (user.getOrganization() != null) {
            task.setUserDepId(user.getOrganization().getId());
        }
        task.setBelonger(task.getBelonger().replace(String.valueOf(oldUser), String.valueOf(userId)));
        this.updateById(task);
        // 更新巡更点负责人
        patrolTaskLocationService.lambdaUpdate().set(PatrolTaskLocation::getSuperintendentUserId, userId)
                .set(PatrolTaskLocation::getUpdateTime, LocalDateTime.now()).set(PatrolTaskLocation::getUpdateBy, userId)
                .eq(PatrolTaskLocation::getTaskId, id).eq(PatrolTaskLocation::getSuperintendentUserId, oldUser).update();
        // 发送代办
        List<Long> userIds = new ArrayList<>();
        userIds.add(task.getUserId());
        if (StringUtils.isNotBlank(task.getAssistIds())) {
            Arrays.asList(task.getAssistIds().split(",")).forEach(s -> {
                userIds.add(Long.parseLong(s));
            });
        }
        electronTaskTodoUtil.todoRepeat(task, userIds);
    }

    /**
     * @Author wql
     * @Description 巡更任务概览
     * @Date 16:33 2023/5/29
     **/
    @Override
    public PatrolOverviewVO overview(PatrolTaskPageParam param) {
        Period period = param.getPeriod();

        List<Long> taskIdList = null;
        if (param.getLocationId() != null) {
            taskIdList = new ArrayList<>();
            List<PatrolTaskLocation> taskLocationList = patrolTaskLocationMapper.selectList(Wrappers.<PatrolTaskLocation>lambdaQuery()
                                                                                                    .eq(PatrolTaskLocation::getLocationId, param
                                                                                                            .getLocationId())
                                                                                           );
            if (taskLocationList != null && !taskLocationList.isEmpty()) {
                taskIdList = taskLocationList.stream().map(PatrolTaskLocation::getTaskId).filter(Objects::nonNull).distinct()
                        .collect(Collectors.toList());
            }
            if (taskIdList.isEmpty()) {
                taskIdList.add(0L);
            }
        }
        Long total = patrolDataPermissionUtils.setDataPermission(this.lambdaQuery()
                                                                         .like(StringUtils.isNotBlank(param.getKeyword()), PatrolTask::getTaskName, param.getKeyword())
                                                                         .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                                                                                 , ObjectUtils.ifNull(period, Period::getEnd))
                                                                         .in(taskIdList != null, PatrolTask::getId, taskIdList), "total")
                .count();

        Long received = patrolDataPermissionUtils.setDataPermission(this.lambdaQuery()
                                                                            .like(StringUtils.isNotBlank(param.getKeyword()), PatrolTask::getTaskName, param.getKeyword())
                                                                            .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                                                                                    , ObjectUtils.ifNull(period, Period::getEnd))
                                                                            .eq(PatrolTask::getStatus, PatrolTaskStateEnum.TO_BE_RECEIVED.name())
                                                                            .in(taskIdList !=
                                                                                null, PatrolTask::getId, taskIdList), PatrolTaskStateEnum.TO_BE_RECEIVED.name())
                .count();
        Long execution = patrolDataPermissionUtils.setDataPermission(this.lambdaQuery()
                                                                             .like(StringUtils.isNotBlank(param.getKeyword()), PatrolTask::getTaskName, param.getKeyword())
                                                                             .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                                                                                     , ObjectUtils.ifNull(period, Period::getEnd))
                                                                             .eq(PatrolTask::getStatus, PatrolTaskStateEnum.IN_EXECUTION.name())
                                                                             .in(taskIdList !=
                                                                                 null, PatrolTask::getId, taskIdList), PatrolTaskStateEnum.IN_EXECUTION.name())
                .count();
        Long finish = patrolDataPermissionUtils.setDataPermission(this.lambdaQuery()
                                                                          .like(StringUtils.isNotBlank(param.getKeyword()), PatrolTask::getTaskName, param.getKeyword())
                                                                          .between(ObjectUtils.isNotNull(period), PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                                                                                  , ObjectUtils.ifNull(period, Period::getEnd))
                                                                          .eq(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                                                                          .in(taskIdList !=
                                                                              null, PatrolTask::getId, taskIdList), PatrolTaskStateEnum.FINISHED.name())
                .count();
        return PatrolOverviewVO.builder()
                .total(total)
                .toBeReceived(received)
                .inExecution(execution)
                .finished(finish)
                .build();
    }

    /**
     * 删除巡更任务(批量)
     *
     * @param param taskIdList 任务id集合
     *
     * @return 是否成功
     */
    @Override
    public Boolean removeElectronTaskBatch(PatrolTaskOperateParam param) {
        if (param == null || CollectionUtils.isEmpty(param.getTaskIdList())) {
            return true;
        }
        Set<String> taskIdSet = param.getTaskIdList().stream().map(String::valueOf).collect(Collectors.toSet());
        // 清除任务
        DatabaseContextHolder.softDelete(PatrolTask.class, new HashSet<>(param.getTaskIdList()));
        // 清除任务巡更点关联关系
        List<PatrolTaskLocation> removeTaskLocationList = patrolTaskLocationService.lambdaQuery()
                .in(PatrolTaskLocation::getTaskId, param.getTaskIdList())
                .list();
        DatabaseContextHolder.softDelete(PatrolTaskLocation.class, removeTaskLocationList);
        // 清除待办和消息
        try {
            todoTaskClient.removeBatchByBusinessIds(taskIdSet);
            msgRecordClient.removeBatchByBusinessIds(taskIdSet);
        } catch (Exception e) {
            log.error("removeElectronTaskBatch error：{}", param.getTaskIdList(), e);
        }
        return true;
    }

    /**
     * 巡更异常率
     *
     * @return 率
     */
    @Override
    public TotalNumberRateDTO getElectronErrRate(ElectronQueryParam param) {
        Period period = param.getPeriod();
        Set<Long> takIds = new HashSet<>();
        if (StringUtils.isNotBlank(param.getAreaName()) || StringUtils.isNotBlank(param.getPointName())) {
            Long areaId = null;
            if (StringUtils.isNotBlank(param.getAreaName())) {
                List<AreaDTO> byName = areaClient.getAreaByName(param.getAreaName());
                if (CollectionUtils.isEmpty(byName)) {
                    return this.getRateDto(0, 0);
                }
                areaId = byName.stream().filter(t -> 1 == t.getLevel()).findFirst().orElse(new AreaDTO()).getId();
            }

            List<PatrolLocation> patrolLocations = locationMapper.selectList(Wrappers.<PatrolLocation>lambdaQuery()
                                                                                     .like(StringUtils.isNotBlank(param.getPointName()),
                                                                                           PatrolLocation::getPatrolLocationName, param.getPointName())
                                                                                     .like(Objects.nonNull(areaId), PatrolLocation::getAreaId, areaId)
                                                                            );
            if (CollectionUtils.isEmpty(patrolLocations)) {
                return this.getRateDto(0, 0);
            }

            List<Long> locationIdList = patrolLocations.stream().map(PatrolLocation::getId).distinct().collect(Collectors.toList());
            patrolTaskLocationService.lambdaQuery().in(PatrolTaskLocation::getLocationId, locationIdList).list()
                    .forEach(t -> takIds.add(t.getTaskId()));

        }
        // 异常数量
        List<PatrolTask> taskList = this.baseMapper.getPatrolTaskErrorDataCount(period, takIds);
        int errInt = taskList == null ? 0 : taskList.size();
        // 总数量
        Long totalInt = this.getTaskTotal(period, takIds);
        // 异常率
        return this.getRateDto(errInt, totalInt);

    }

    /**
     * 巡更完成率
     *
     * @return 率
     */
    @Override
    public TotalNumberRateDTO getElectronCompleteRate(ElectronQueryParam param) {
        Period period = param.getPeriod();
        Set<Long> takIds = new HashSet<>();
        if (StringUtils.isNotBlank(param.getAreaName()) || StringUtils.isNotBlank(param.getPointName())) {
            Long areaId = null;
            if (StringUtils.isNotBlank(param.getAreaName())) {
                List<AreaDTO> byName = areaClient.getAreaByName(param.getAreaName());
                if (CollectionUtils.isEmpty(byName)) {
                    return this.getRateDto(0, 0);
                }
                areaId = byName.stream().filter(t -> 1 == t.getLevel()).findFirst().orElse(new AreaDTO()).getId();
            }

            List<PatrolLocation> patrolLocations = locationMapper.selectList(Wrappers.<PatrolLocation>lambdaQuery()
                                                                                     .like(StringUtils.isNotBlank(param.getPointName()),
                                                                                           PatrolLocation::getPatrolLocationName, param.getPointName())
                                                                                     .like(Objects.nonNull(areaId), PatrolLocation::getAreaId, areaId)
                                                                            );
            if (CollectionUtils.isEmpty(patrolLocations)) {
                return this.getRateDto(0, 0);
            }

            List<Long> locationIdList = patrolLocations.stream().map(PatrolLocation::getId).distinct().collect(Collectors.toList());
            patrolTaskLocationService.lambdaQuery().in(PatrolTaskLocation::getLocationId, locationIdList).list()
                    .forEach(t -> takIds.add(t.getTaskId()));
        }
        // 完成数
        Long completeInt = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(takIds), PatrolTask::getId, takIds)
                .ge(period != null && period.getBegin() != null, PatrolTask::getCompleteDate, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, PatrolTask::getCompleteDate, ObjectUtils.ifNull(period, Period::getEnd))
                .isNotNull(PatrolTask::getCompleteDate)
                .count();
        if (completeInt == null) {
            completeInt = 0L;
        }
        // 总数
        Long totalInt = this.getTaskTotal(period, takIds);
        return this.getRateDto(completeInt, totalInt);
    }

    /**
     * 获取任务总数
     *
     * @param period 时间
     * @param taskId
     *
     * @return 总数
     */
    @Override
    public Long getTaskTotal(Period period, Set<Long> taskId) {
        Long total = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(taskId), PatrolTask::getId, taskId)
                .ge(period != null && period.getBegin() != null, PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getEnd))
                .count();
        return total == null ? 0 : total;
    }

    @Override
    public List<LocationStatisticsVO> getLocationsStatistics(LocationQueryParam param) {
        StatisticParam statisticParam = new StatisticParam();
        statisticParam.setPeriod(param.getPeriod());
        statisticParam.setResult(param.getResult());
        return patrolTaskLocationMapper.locationExport(statisticParam);
    }

    @Override
    public HandlerRateMetricDTO getElectronCompleteRateMetric(Period period) {
        return HandlerRateMetricDTO.builder()
                .topHandlerRateDept(getTopHandlerRate(period, "DESC"))
                .bottomHandlerRateDept(getTopHandlerRate(period, null))
                .topHandlerRateUser(this.baseMapper.getTop5FinishRateUser(period, "DESC"))
                .bottomHandlerRateUser(this.baseMapper.getTop5FinishRateUser(period, null))
                .build();
    }

    @Override
    public HandlerEfficiencyMetricDTO getElectronCompleteEfficiencyMetric(Period period) {
        return HandlerEfficiencyMetricDTO.builder()
                .topHandlerEfficiencyDept(getTopHandlerEfficiency(period, "DESC"))
                .bottomHandlerEfficiencyDept(getTopHandlerEfficiency(period, null))
                .topHandlerEfficiencyUser(this.baseMapper.getTop5FinishEfficiencyUser(period, null))
                .bottomHandlerEfficiencyUser(this.baseMapper.getTop5FinishEfficiencyUser(period, null))
                .build();
    }

    @Override
    public TaskHandlerTimeVO getElectronCompleteTotalTime(Period period) {
        return this.baseMapper.selectElectronCompleteTotalTime(period);
    }

    /**
     * 任务分发（任务的巡更点分配）
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean taskLocationDistribution(PatrolLocationDispatchingParam param) {
        /* 校验 */
        Assert.isParameterError(
                param.getTaskId() == null || param.getUserId() == null || CollectionUtils.isEmpty(param.getLocationIds()), "入参不能为空！");
        var task = this.getById(param.getTaskId());
        Assert.isConflict(task == null || !SecurityContextHolder.getPrincipal().getId().equals(task.getUserId()), "不是该任务的负责人！");
        Assert.isConflict(Boolean.TRUE.equals(task.getInOrder()), "按顺序执行的巡更不能分发！");
        String belonger = this.getDistributionBelonger(task.getBelonger(), param.getUserId());
        this.lambdaUpdate().set(PatrolTask::getBelonger, belonger)
                .set(PatrolTask::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                .set(PatrolTask::getUpdateTime, LocalDateTime.now())
                .eq(PatrolTask::getId, param.getTaskId()).update();
        /* 修改任务的巡更点负责人 */
        patrolTaskLocationService.lambdaUpdate().set(PatrolTaskLocation::getSuperintendentUserId, param.getUserId())
                .eq(PatrolTaskLocation::getTaskId, param.getTaskId()).isNull(PatrolTaskLocation::getResult)
                .in(PatrolTaskLocation::getLocationId, param.getLocationIds())
                .update();
        // 发送待办
        electronTaskTodoUtil.todoInsert(task, Collections.singletonList(param.getUserId()), PatrolTaskStateEnum.IN_EXECUTION);
        return true;
    }

    private List<HandlerEfficiencyDTO> getTopHandlerEfficiency(Period period, String sort) {
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = this.baseMapper.getTop5FinishEfficiencyDept(period, sort);
        if (CollectionUtils.isNotEmpty(handlerEfficiencyDTOS)) {
            Set<Long> depIds = handlerEfficiencyDTOS.stream().map(t -> Long.valueOf(t.getName()))
                    .collect(Collectors.toSet());
            Map<Long, String> organizationNames = organizationClientService.getOrganizationNames(depIds);
            handlerEfficiencyDTOS.forEach(t -> {
                t.setName(organizationNames.get(Long.valueOf(t.getName())));
            });
        }
        return handlerEfficiencyDTOS;
    }

    private List<HandlerRateDTO> getTopHandlerRate(Period period, String sort) {
        List<HandlerRateDTO> handlerRateDeptList = this.baseMapper.getTop5FinishRateDept(period, sort);
        if (CollectionUtils.isNotEmpty(handlerRateDeptList)) {
            Set<Long> depIds = handlerRateDeptList.stream().map(t -> Long.valueOf(t.getName()))
                    .collect(Collectors.toSet());
            Map<Long, String> organizationNames = organizationClientService.getOrganizationNames(depIds);
            handlerRateDeptList.forEach(t -> {
                t.setName(organizationNames.get(Long.valueOf(t.getName())));
            });
        }
        return handlerRateDeptList;
    }

    /**
     * 巡更记录扩展
     */
    private List<PatrolRecordPageVO> extend(List<PatrolTask> tasks) {
        List<PatrolRecordPageVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(tasks)) {
            return new ArrayList<>();
        }
        List<Long> taskIdList = new ArrayList<>();
        Set<Long> deptIdSet = new HashSet<>();
        Set<Long> routeIdSet = new HashSet<>();
        tasks.forEach(patrolTask -> {
            if (Objects.nonNull(patrolTask.getId())) {
                taskIdList.add(patrolTask.getId());
            }
            if (Objects.nonNull(patrolTask.getUserDepId())) {
                deptIdSet.add(patrolTask.getUserDepId());
            }
            if (Objects.nonNull(patrolTask.getRouteId())) {
                routeIdSet.add(patrolTask.getRouteId());
            }
        });
        // 部门map
        Map<Long, String> deptMap = organizationClientService.getOrganizationNames(deptIdSet);
        // 未巡检的map
        Map<Long, List<PatrolTaskLocation>> notInspectedMap = new HashMap<>();
        List<PatrolTaskLocation> notInspectedList = patrolTaskLocationMapper.selectList(Wrappers.<PatrolTaskLocation>lambdaQuery()
                                                                                                .in(PatrolTaskLocation::getTaskId, taskIdList)
                                                                                                .and(q -> q
                                                                                                        .isNull(PatrolTaskLocation::getResult)
                                                                                                        .or()
                                                                                                        .isNull(PatrolTaskLocation::getPatrolDate))
                                                                                       );
        if (!CollectionUtils.isEmpty(notInspectedList)) {
            notInspectedMap = notInspectedList.stream().collect(Collectors.groupingBy(PatrolTaskLocation::getTaskId));
        }
        // 巡更路线名称map
        Map<Long, String> routeNameMap = new HashMap<>();
        List<PatrolRoute> routeList = patrolRouteMapper.selectList(Wrappers.<PatrolRoute>lambdaQuery()
                                                                           .in(PatrolRoute::getUuid, routeIdSet)
                                                                           .groupBy(PatrolRoute::getUuid)
                                                                  );
        if (!CollectionUtils.isEmpty(routeList)) {
            routeList.forEach(patrolRoute -> routeNameMap.put(patrolRoute.getUuid(), patrolRoute.getPatrolRouteName()));
        }

        Map<Long, List<PatrolTaskLocation>> finalNotInspectedMap = notInspectedMap;
        tasks.forEach(task -> {
            Integer locationNum = task.getLocationNum();
            List<PatrolTaskLocation> locations = finalNotInspectedMap.get(task.getId());
            Integer undoneNum = locations == null ? 0 : locations.size();
            String deptName = task.getUserDepId() == null ? "" : deptMap.get(task.getUserDepId());
            String routeName = task.getRouteId() == null ? "" : routeNameMap.get(task.getRouteId());
            list.add(PatrolRecordPageVO.builder()
                             .id(task.getId())
                             .taskName(task.getTaskName())
                             .userName(task.getUserName())
                             .userDepName(deptName == null ? "" : deptName)
                             .completeDate(task.getCompleteDate())
                             .routeName(routeName == null ? "" : routeName)
                             .patrolLocationNum(locationNum)
                             .undoneNum(undoneNum)
                             .completedNum(locationNum - undoneNum)
                             .build());
        });
        return list;
    }

    /**
     * 任务扩展人员信息
     */
    private Map<String, UserDTO> getTaskUser(List<PatrolTask> taskList, String type) {
        UserMapQuery userMapQuery = new UserMapQuery();
        Set<Long> userIds = new HashSet<>();
        if (USER_ID.equals(type)) {
            taskList.forEach(task -> {
                if (ObjectUtils.isNotEmpty(task.getUserId())) {
                    userIds.add(task.getUserId());
                } else {
                    String[] ids = task.getBelonger().split(",");
                    for (String id : ids) {
                        userIds.add(Long.parseLong(id));
                    }
                }
                if (ObjectUtils.isNotEmpty(task.getAssistIds())) {
                    String[] ids = task.getAssistIds().split(",");
                    for (String id : ids) {
                        userIds.add(Long.parseLong(id));
                    }
                }
            });
            userMapQuery.setUserIds(userIds);
        } else {
            Set<String> userInfo = new HashSet<>();
            userInfo.add(InfoTypesEnum.ORGANIZATION.name());
            userMapQuery.setInfoTypes(userInfo);
            userMapQuery.setUserIds(null);
            userMapQuery.setUserNames(taskList.stream().map(PatrolTask::getCreateBy).collect(Collectors.toSet()));
        }
        Map<String, UserDTO> userIdsMap = userClient.getTenantUserMap(userMapQuery);
        return userIdsMap;
    }

    /**
     * 任务扩展人员信息
     */
    private void taskExtend(PatrolTaskPageVO patrolTaskPageVO, PatrolTask task, Map<String, UserDTO> userIdsMap,
                            Map<String, UserDTO> userNamesMap
                           ) {
        UserDTO publisherUser = userNamesMap.get(task.getCreateBy());
        String publisherName = "";
        String orgName = "";
        String userName = "";
        if (ObjectUtils.isNotEmpty(publisherUser)) {
            publisherName = publisherUser.getName();
            orgName = ObjectUtils.isNotEmpty(publisherUser.getOrganization()) ? publisherUser.getOrganization().getName() : "";
        }
        if (ObjectUtils.isNotEmpty(task.getUserId())) {
            UserDTO user = userIdsMap.get(task.getUserId().toString());
            userName = Optional.ofNullable(user).orElse(new UserDTO()).getName();
        } else {
            String[] ids = task.getBelonger().split(",");
            for (String id : ids) {
                UserDTO user = userIdsMap.get(id);
                if (ObjectUtils.isNotEmpty(user)) {
                    userName = userName.length() > 1 ? userName + "," + user.getName() : user.getName();
                }
            }
        }
        if (ObjectUtils.isNotEmpty(task.getAssistIds())) {
            String[] ids = task.getAssistIds().split(",");
            for (String id : ids) {
                UserDTO assistUser = userIdsMap.get(id);
                if (ObjectUtils.isNotEmpty(assistUser)) {
                    userName = userName + "," + assistUser.getName();
                }
            }
        }
        patrolTaskPageVO.setPublisherName(publisherName);
        patrolTaskPageVO.setPublisherDepName(orgName);
        patrolTaskPageVO.setUserName(userName);
    }

    private boolean isAllFieldsNull(Object object) {
        if (object == null) {
            return true;
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, object);
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算比率
     *
     * @param value 除数
     * @param total 被除数
     *
     * @return 比率
     */
    private TotalNumberRateDTO getRateDto(long value, long total) {
        // 异常率
        BigDecimal rate = BigDecimal.ZERO;
        if (total > 0) {
            rate = BigDecimal.valueOf(value).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        // 返回
        return TotalNumberRateDTO.builder().total(total).number(value).rate(rate).build();
    }

    // 处理分发的任务相关人id信息
    private String getDistributionBelonger(String oldBelonger, Long userId) {
        if (StringUtils.isBlank(oldBelonger)) {
            return String.valueOf(userId);
        }
        var belongerSet = Arrays.stream(oldBelonger.split(",")).collect(Collectors.toSet());
        belongerSet.add(String.valueOf(userId));
        return String.join(",", belongerSet);
    }
}
