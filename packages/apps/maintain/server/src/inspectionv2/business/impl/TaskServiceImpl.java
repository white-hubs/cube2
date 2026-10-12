package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainUserClientService;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.DatePeriodUtils;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.MessageSendUtil;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl.OrganizationClientService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskTargetRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskTargetItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskTargetItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectItemTemplateMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.PlanRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskTargetRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectNoticeModuleTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRepairStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionMappingEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectDepStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskReceiveUsersParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskObjCountResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectAbnormalOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskDepartmentMemberCompletionStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskTypeDistributionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.PerformInspectionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskDepStatistics;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.utils.DateUtil;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@SuppressWarnings("OptionalOfRedundantMethod")
public class TaskServiceImpl extends ServiceImpl<TaskMapper, InspectTaskPO> implements TaskService {

    @Resource
    private UserClient userClient;
    @Resource
    private HandlerService handlerService;
    @Resource
    private TaskTargetRelationService taskTargetRelationService;
    @Resource
    private InspectQuery inspectQuery;
    @Resource
    private InspectRecordQueryService recordQueryService;
    @Resource
    private InspectRecordCommandService recordCommandService;
    @Resource
    @Lazy
    private PlanService planService;

    @Resource
    private InspectTargetInfoQuery inspectTargetInfoQuery;

    @Resource
    private InspectTaskStatusChangeService inspectTaskStatusChangeService;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private PlanRelationMapper planRelationMapper;

    @Resource
    private MaintainUserClientService maintainUserClientService;

    @Resource
    private OrganizationClientService organizationClientService;

    @Resource
    private InspectTypeQueryService inspectTypeQueryService;

    @Resource
    private InspectStandardMapper inspectStandardMapper;
    @Resource
    private InspectStandardItemMapper inspectStandardItemMapper;
    @Resource
    private InspectItemTemplateMapper inspectItemTemplateMapper;
    @Resource
    private InspectResultMapper inspectResultMapper;
    @Resource
    private InspectTaskTargetItemCommandService inspectTaskTargetItemCommandService;
    @Resource
    private InspectTaskTargetItemQueryService inspectTaskTargetItemQueryService;
    @Resource
    private AreaClient areaClient;
    @Resource
    private OrganizationClient organizationClient;
    @Resource
    private AppointmentClient appointmentClient;
    @Resource
    private RoleClient roleClient;
    @Resource
    private InspectRecordMapper inspectRecordMapper;
    @Resource
    private InspectRecordItemMapper inspectRecordItemMapper;
    @Resource
    private TaskTargetRelationMapper taskTargetRelationMapper;

    /**
     * 巡检任务详情
     *
     * @param taskId    任务id
     * @param notTarget 不查询巡检对象(为true时才不查询)
     *
     * @return 任务详情
     */
    @Override
    public TaskDetailResult getDetail(Long taskId, Boolean notTarget) {
        InspectTaskPO taskPO = this.getById(taskId);
        Assert.isNotFound(taskPO, "任务不存在！");

        TaskDetailResult taskResult = ObjectUtils.copying(taskPO, TaskDetailResult.class);
        taskResult.setReceiveNames(inspectQuery.extendUserNames(taskResult.getTenant(), taskResult.getReceiveIds()));
        taskResult.setReceiveDeptNames(inspectQuery.extendDeptNames(taskResult.getTenant(), taskResult.getReceiveDeptIds()));
        taskResult.setReceivePostNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceivePostIds()));
        taskResult.setReceiveRoleNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceiveRoleIds()));
        taskResult.setAssistNames(inspectQuery.extendUserNames(taskResult.getTenant(), taskResult.getAssistIds()));
        // 有关联被稽查任务id就是稽查任务
        taskResult.setIsCheck(taskPO.getCheckTaskId() != 0);
        if (!Boolean.TRUE.equals(notTarget)) {
            // 计划对象集合
            taskResult.setTypeTargets(taskTargetRelationService.getTargetInfoByTaskId(taskPO.getId()));
        }
        // 返回
        return taskResult;
    }

    /**
     * 任务详情-对象标准列表
     *
     * @param taskId
     * @param targetId
     *
     * @return
     */
    @Override
    public List<StandardResult> getDeailStandardList(Long taskId, Long targetId) {
        Assert.isParameterError(taskId == null, "任务不能为空(id)");
        Assert.isParameterError(targetId == null, "对象不能为空(id)");
        InspectTaskPO taskPO = this.getById(taskId);
        Assert.isNotFound(taskPO, "任务不存在！");
        // 任务列表只显示主任务所以跟计划的对象(标准)是一样的
        List<StandardResult> deailStandardList = planService.getDeailStandardList(taskPO.getPlanId(), targetId);
        if (ObjectUtils.isNotEmpty(deailStandardList)) {
            List<InspectTaskPO> list = this.lambdaQuery().eq(InspectTaskPO::getParentId, taskId).like(InspectTaskPO::getTargetIds, targetId)
                    .list();
            String usernames = maintainUserClientService.getUserNames(
                    ObjectUtils.isNotEmpty(list) ? list.get(0).getReceiveIds() : taskPO.getReceiveIds());
            for (StandardResult result : deailStandardList) {
                result.setReceiveUserNames(usernames);
            }
        }
        return deailStandardList;
    }

    /**
     * 巡检任务-详情-执行进度
     *
     * @param param taskId keyword targetCategory
     *
     * @return
     */
    @Override
    public DetailRateResult getDetailRateResult(TaskPageParam param) {
        Long taskId = param.getId();
        DetailRateResult result = new DetailRateResult();

        // 获取任务信息判断是否有子任务
        InspectTaskPO parentTask = this.getById(taskId);
        Assert.isNotFound(parentTask, "任务不存在，检查是否被删除！");
        param.setPlanId(parentTask.getPlanId());
        /*
         * 处理任务进度和巡检记录的集合列表
         */
        List<TaskRateResult> taskRateResults = new ArrayList<>();
        List<InspectRecordVO> recordTaskResultList = new ArrayList<>();
        if (!parentTask.getIsSplit()) {
            taskRateResults.add(this.handleTaskRateResult(parentTask));
            param.setTaskId(parentTask.getId());
            Page<InspectRecordVO> page = inspectRecordMapper.querTaskRecordList(PagingUtils.convert(param), param);
            result.setTotal(page.getTotal());
            recordTaskResultList.addAll(page.getRecords());
        } else {
            // 获取子任务列表
            List<Long> taskIdList = new ArrayList<>();
            this.lambdaQuery().eq(InspectTaskPO::getParentId, taskId).list()
                    .forEach(t -> {
                        taskIdList.add(t.getId());
                        // 获取每个子任务的执行进度
                        taskRateResults.add(this.handleTaskRateResult(t));
                    });
            if (!taskIdList.isEmpty()) {
                param.setTaskIdList(taskIdList);
                Page<InspectRecordVO> page = inspectRecordMapper.querTaskRecordList(PagingUtils.convert(param), param);
                result.setTotal(page.getTotal());
                recordTaskResultList.addAll(page.getRecords());
            }
        }
        // 设置返回对象的任务进度列表
        result.setTaskRateResults(taskRateResults);
        result.setRecordTaskResultList(recordTaskResultList);
        return result;
    }

    /**
     * 巡检任务详情对象列表导出
     *
     * @param param taskId keyword targetCategory
     *
     * @return 待导出的巡检项列表
     */
    @Override
    public List<InspectRecordVO> getDetailRateExportList(TaskPageParam param) {
        InspectTaskPO parentTask = this.getById(param.getId());
        Assert.isNotFound(parentTask, "任务不存在，检查是否被删除！");
        param.setPlanId(parentTask.getPlanId());
        if (!parentTask.getIsSplit()) {
            param.setTaskId(parentTask.getId());
            param.setTaskIdList(null);
        } else {
            List<Long> taskIdList = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, parentTask.getId())
                    .list()
                    .stream()
                    .map(InspectTaskPO::getId)
                    .toList();
            if (taskIdList.isEmpty()) {
                return Collections.emptyList();
            }
            param.setTaskId(null);
            param.setTaskIdList(taskIdList);
        }
        return inspectRecordMapper.queryTaskRecordItemExportList(param);
    }

    /**
     * 巡检任务分页列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public Pagination<TaskDetailResult> getPage(TaskPageParam param) {
        Period period = null;
        if (param.getPeriod() != null) {
            period = DateUtil.simpleDateTypeHandle(param.getDateType(), param.getPeriod());
        }

        LambdaQueryChainWrapper<InspectTaskPO> wrapper = this.lambdaQuery().between(period !=
                                                                                    null, InspectTaskPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                // 父任务id为0即为主任务
                .eq(true, InspectTaskPO::getParentId, 0)
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                        .like(InspectTaskPO::getName, param.getKeyword())
                    )
                .eq(StringUtils.isNotBlank(param.getStatus()), InspectTaskPO::getStatus, param.getStatus())
                .eq(StringUtils.isNotBlank(param.getTypeId()), InspectTaskPO::getTypeId, param.getTypeId())
                .like(StringUtils.isNotBlank(param.getDepId()), InspectTaskPO::getReceiveDeptIds, param.getDepId())
                .le(InspectTaskPO::getValidBeginTime, LocalDateTime.now());
        if ("TIMEOUT".equals(param.getTaskTimeout())) {
            wrapper
                    .in(InspectTaskPO::getStatus, Arrays.asList(InspectTaskStatusEnum.ACCEPT, InspectTaskStatusEnum.EXECUTING, InspectTaskStatusEnum.WAIT_REPAIR))
                    .lt(InspectTaskPO::getValidEndTime, LocalDateTime.now());
        } else if ("NO_TIMEOUT".equals(param.getTaskTimeout())) {
            wrapper.and(i ->
                                i       /*为已完成*/
                                        .in(InspectTaskPO::getStatus, Arrays.asList(InspectTaskStatusEnum.OVER, InspectTaskStatusEnum.REPAIRED, InspectTaskStatusEnum.STOP))
                                        /*未完成且当前时间小于等于任务期限时间*/
                                        .or(j -> j
                                                .in(InspectTaskPO::getStatus, Arrays.asList(InspectTaskStatusEnum.ACCEPT, InspectTaskStatusEnum.EXECUTING, InspectTaskStatusEnum.WAIT_REPAIR))
                                                .ge(InspectTaskPO::getValidEndTime, LocalDateTime.now()))
                       );
        }
        wrapper.orderByDesc(InspectTaskPO::getCreateTime);
        /*
         * 分页查询任务列表
         */
        Page<InspectTaskPO> page = wrapper.page(PagingUtils.convert(param));
        /*
         * 扩展列表字段
         */
        List<TaskDetailResult> collect = this.extTaskPageList(page.getRecords());

        return PagingUtils.convert(page, collect);
    }

    @Override
    public List<TaskDetailResult> getExportPage(TaskPageParam param) {
        Pagination<TaskDetailResult> page = this.getPage(param);
        List<TaskDetailResult> records = ObjectUtils.ifNull(page, Pagination::getRecords);
        return CollectionUtils.isEmpty(records) ? new ArrayList<>() : records;
    }

    /**
     * 任务拆解
     *
     * @param taskSplitParam
     */
    @Override
    @Transactional
    public void splitTask(TaskSplitParam taskSplitParam) {
        Assert.isNotFound(taskSplitParam);
        Assert.isNotFound(taskSplitParam.getTaskId());
        // 获取任务信息
        InspectTaskPO parentTaskPO = this.getById(taskSplitParam.getTaskId());
        Assert.isNotFound(parentTaskPO, "计划不存在！");
        Assert.isNotAcceptable(!InspectTaskStatusEnum.SPLIT.equals(parentTaskPO.getStatus()), "非待拆解任务！");

        // 保存子任务
        taskSplitParam.getTaskReceiveUsersDTOList().forEach(r -> {
            // 组装子任务数据
            InspectTaskPO taskPO = ObjectUtils.copying(parentTaskPO, InspectTaskPO.class);
            taskPO.setId(IdWorker.getId());
            taskPO.setIsSplit(false);
            taskPO.setParentId(parentTaskPO.getId());
            taskPO.setStatus(InspectTaskStatusEnum.ACCEPT);
            taskPO.setReceiveIds(r.getReceiveIds());
            taskPO.setTargetNum(r.getTaskTargetDTOList().size());
            taskPO.setTargetIds(handlerService.handlerTaskTargetDTOListToTargetIds(r.getTaskTargetDTOList()));
            taskPO.setReceiveDeptIds(handlerService.handlerUserIdsToDeptIds(r.getReceiveIds()));
            // 保存子任务
            this.saveTask(taskPO);
            // 保存任务和对象关联关系
            List<TaskTargetRelationPO> taskTargetRelationPOList = this.getTaskRelation(taskPO, r);
            // 批量保存任务管理关系
            taskTargetRelationService.saveBatch(taskTargetRelationPOList);
            // 生成巡检记录
            this.createInspectionRecord(taskPO.getId());
        });
        // 修改主任务状态
        updateTaskState(parentTaskPO.getId(), InspectTaskStatusEnum.ACCEPT);
    }

    /**
     * @Author wql
     * @Description 更新主任务状态
     * @Date 13:42 2023/8/15
     **/
    private void updateTaskState(Long taskId, InspectTaskStatusEnum state) {
        InspectTaskPO taskPO = taskMapper.selectById(taskId);
        taskPO.setStatus(state);
        taskMapper.updateById(taskPO);
    }

    /**
     * 根据计划创建任务
     * 请使用他下面的createTaskByPlan这个方法
     *
     * @param planPO
     *
     * @return
     */
    @Override
    public String createTaskByPlan(PlanPO planPO) {
        List<PlanRelationPO> planRelationPOList = planRelationMapper.selectList(Wrappers.<PlanRelationPO>lambdaQuery()
                                                                                        .eq(PlanRelationPO::getPlanId, planPO.getId()));
        return createTaskByPlan(planPO, planRelationPOList);
    }

    /**
     * 根据计划创建任务
     *
     * @param planPO
     *
     * @return
     */
    @Override
    public String createTaskByPlan(PlanPO planPO, List<PlanRelationPO> targetDTOList) {

        InspectTaskPO taskPO = ObjectUtils.copying(planPO, InspectTaskPO.class);
        // 当接收类型 2＝部门时，需要把部门转换为 部门下的人
        if ("2".equals(planPO.getReceiveType())) {
            taskPO.setReceiveIds(handlerService.HandlerDeptIdsAndPostIdsToUserIds(planPO.getReceiveDeptIds(), planPO.getReceivePostIds(), planPO.getReceiveRoleIds()));
            Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "巡检任务选择的岗位下无人员");
        } else {
            // 为接受人时需要把部门也赋上值 （岗位暂时未赋值，岗位并未使用）
            taskPO.setReceiveDeptIds(handlerService.handlerUserIdsToDeptIds(planPO.getReceiveIds()));
        }

        // 处理对象ids
        taskPO.setTargetIds(handlerService.handlerTargetDTOListToTargetIds(targetDTOList));

        taskPO.setCode(BizCodeUtils.generateNoRandom("IPK"));
        taskPO.setPlanId(planPO.getId());
        taskPO.setPlanName(planPO.getName());
        String format = ObjectUtils.isEmpty(planPO.getExecTimes()) ? "" : String.format("%04d", planPO.getExecTimes());
        taskPO.setName(taskPO.getName() + "_V" + format);
        //因为是从计划中复制的 ，这里手动设置为空
        taskPO.setId(null);
        taskPO.setBeginTime(null);
        taskPO.setEndTime(null);
        taskPO.setIsSplit(planPO.isSplit());
        // 任务结束时间为计划开始时间加上任务的有效天(分钟)数
        taskPO.setCreateTime(LocalDateTime.now());
        taskPO.setUpdateTime(LocalDateTime.now());
        taskPO.setValidBeginTime(LocalDateTime.now());
        taskPO.setValidEndTime(DateUtils.offset(taskPO.getCreateTime(), ChronoUnit.MINUTES, planPO.getLimitTotalMinute()));
        taskPO.setPublisherId(planPO.getCreatorId().toString());
        taskPO.setPublisherName(userClient.getUser(Long.valueOf(planPO.getCreatorId())).getName());
        taskPO.setStatus(planPO.isSplit() ? InspectTaskStatusEnum.SPLIT : InspectTaskStatusEnum.ACCEPT);
        taskPO.setCheckTaskId(0L);

        // 保存普通任务(非普通为稽查任务)
        if (planPO.isSplit()) {
            // 主任务不需要保存巡检记录和发送通知消息
            this.save(taskPO);
            // 保存任务关联关系和巡检内容
            this.saveTaskRelation(taskPO, targetDTOList);
        } else {
            log.info("创建巡检任务开始");
            this.saveTask(taskPO).getId();
            log.info("创建巡检任务结束");
            // 保存任务关联关系和巡检内容
            this.saveTaskRelation(taskPO, targetDTOList);
            // 生成巡检记录
            this.createInspectionRecord(taskPO.getId());
        }

        //如果需要稽查，还有创建稽查任务
        if (planPO.isCheck()) {
            // 创建稽查任务
            taskPO.setCheckTaskId(taskPO.getId());
            // 检查一下这一步会不把上面的值清空
            taskPO.setId(null);
            taskPO.setCode(BizCodeUtils.generateNoRandom("IPK"));
            taskPO.setIsSplit(false);
            taskPO.setStatus(InspectTaskStatusEnum.ACCEPT);
            taskPO.setReceiveDeptIds(planPO.getCheckDeptIds());
            taskPO.setReceivePostIds(planPO.getCheckPostIds());
            taskPO.setReceiveIds(planPO.getCheckUserIds());
            // 当稽查人类型 2＝部门时，需要把部门转换为 部门下的人
            if ("2".equals(planPO.getCheckUserType())) {
                taskPO.setReceiveIds(handlerService.HandlerDeptIdsAndPostIdsToUserIds(planPO.getCheckDeptIds(), planPO.getCheckPostIds(), planPO.getCheckRoleIds()));
                Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "稽查任务选择的岗位下无人员");
            } else {
                // 为接受人时需要把部门也赋上值 （岗位暂时未赋值，岗位并未使用）
                taskPO.setReceiveDeptIds(handlerService.handlerUserIdsToDeptIds(planPO.getCheckUserIds()));
            }
            taskPO.setName(taskPO.getName() + "稽查");
            // 保存任务
            this.saveTask(taskPO);
            // 保存任务关联关系和巡检内容
            this.saveTaskRelation(taskPO, targetDTOList);
            // 生成巡检记录
            this.createInspectionRecord(taskPO.getId());
        }
        return taskPO.getId().toString();
    }

    private void saveTaskRelation(InspectTaskPO taskPO, List<PlanRelationPO> targetDTOList) {
        List<TaskTargetRelationPO> taskTargetRelationPOList = new ArrayList<>();
        List<InspectTaskTargetItemPO> resultList = new ArrayList<>();
        Set<Long> standeIdSet = new HashSet<>();
        Set<Long> itemIdSet = new HashSet<>();
        targetDTOList.forEach(relation -> {
            standeIdSet.add(relation.getStandardId());
            itemIdSet.add(relation.getItemId());
        });
        Map<Long, InspectStandardPO> standardPOMap = this.getInspectStandardMap(standeIdSet);
        Map<Long, InspectionItemVO> itemVOMap = this.getInspectItemVOMap(itemIdSet);
        this.handlerTaskRelation(taskPO, targetDTOList, standardPOMap, itemVOMap, taskTargetRelationPOList, resultList);
        // 批量保存 任务管理关系
        taskTargetRelationService.saveBatch(taskTargetRelationPOList);
        // 批量保存任务的巡检内容
        inspectTaskTargetItemCommandService.saveBatch(resultList);
    }

    /**
     * @Author wql
     * @Description 生成巡检任务的巡检记录
     * @Date 19:13 2023/4/21
     **/
    public void createInspectionRecord(Long taskId) {
        InspectTaskPO taskPO = this.getById(taskId);
        List<TaskTargetRelationPO> infos = taskTargetRelationService.getObjInfo(taskId);
        if (ObjectUtils.isNotEmpty(infos)) {
            List<InspectRecordPO> list = new ArrayList<>();
            for (TaskTargetRelationPO info : infos) {
                InspectRecordPO recordPO = InspectRecordPO.builder().planId(taskPO.getPlanId())
                        .planName(taskPO.getPlanName())
                        .taskId(taskId)
                        .taskName(taskPO.getName())
                        .status(InspectRecordStatusEnum.EXECUTING)
                        .targetCategory(info.getTargetCategory().name())
                        .targetId(info.getTargetId())
                        .targetName(info.getTargetName())
                        .build();
                InspectTargetInfoDTO targetInfo = inspectTargetInfoQuery.getTargetInfoById(info.getTargetId(), info.getTargetCategory()
                        .name());
                if (ObjectUtils.isNotEmpty(targetInfo)) {
                    recordPO.setAreaName(targetInfo.getAreaName());
                    recordPO.setAreaId(targetInfo.getAreaId());
                }
                list.add(recordPO);
            }
            recordCommandService.saveBatch(list);
        }
    }

    /**
     * @Author wql
     * @Description app端巡检任务详情
     * @Date 9:24 2023/4/21
     **/
    @Override
    public TaskDetailResult appTaskDetail(Long taskId) {
        InspectTaskPO taskPO = this.getById(taskId);
        Assert.isNotFound(taskPO, "任务不存在！");
        TaskDetailResult taskResult = ObjectUtils.copying(taskPO, TaskDetailResult.class);
        taskResult.setReceiveNames(inspectQuery.extendUserNames(taskResult.getTenant(), taskResult.getReceiveIds()));
        taskResult.setReceiveDeptNames(inspectQuery.extendDeptNames(taskResult.getTenant(), taskResult.getReceiveDeptIds()));
        taskResult.setReceivePostNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceivePostIds()));
        taskResult.setReceiveRoleNames(inspectQuery.extendPostNames(taskResult.getTenant(), taskResult.getReceiveRoleIds()));
        taskResult.setAssistNames(inspectQuery.extendUserNames(taskResult.getTenant(), taskResult.getAssistIds()));
        return taskResult;
    }

    @Override
    public Pagination<InspectionObjVO> getInspectionObjInfo(TaskPageParam param) {
        Assert.isNotFound(param, "查询参数不能为空！");
        Long taskId = ObjectUtils.ifNull(param.getTaskId(), param.getId());
        Assert.isParameterError(taskId == null, "任务id不能为空！");
        param.setTaskId(taskId);
        Page<InspectionObjVO> page = taskTargetRelationMapper.selectInspectionObjPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    /**
     * @Author wql
     * @Description 获取任务对象的巡检内容
     * @Date 11:30 2023/4/21
     **/
    @Override
    public PerformInspectionVO performInspection(Long taskId, Long objId) {
        Assert.isParameterError(taskId == null, "任务id不能为空！");
        Assert.isParameterError(objId == null, "对象id不能为空！");
        InspectTaskPO taskPO = this.getById(taskId);
        Assert.isNotFound(taskPO, "任务不存在！");
        TaskTargetRelationPO relationPO = taskTargetRelationService.getOne(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                                                                                   .eq(TaskTargetRelationPO::getTaskId, taskPO.getId())
                                                                                   .eq(TaskTargetRelationPO::getTargetId, objId)
                                                                                   .last(" limit 1 ")
                                                                          );
        Assert.isNotFound(relationPO, "该任务没有关联巡检对象！");
        InspectRecordVO record = recordQueryService.getVOByTarget(relationPO.getTargetCategory().name(), objId, taskId);

        PerformInspectionVO inspectionVO = PerformInspectionVO.builder()
                .taskName(taskPO.getName())
                .taskId(taskPO.getId())
                .targetId(objId)
                .recordId(record.getId())
                .targetName(relationPO.getTargetName())
                .targetCategory(relationPO.getTargetCategory().name())
                .areaId(record.getAreaId())
                .areaName(record.getAreaName())
                .build();
        InspectTargetInfoDTO info = inspectTargetInfoQuery.getTargetInfoById(objId, relationPO.getTargetCategory().name());
        if (ObjectUtils.isNotEmpty(info)) {
            inspectionVO.setAddress(info.getAddress());
            inspectionVO.setTargetType(info.getTargetType());
            inspectionVO.setTargetTypeName(info.getTargetTypeName());
            inspectionVO.setAreaId(info.getAreaId());
            inspectionVO.setAreaName(info.getAreaName());
        }
        //查询对象的巡检标准
        inspectionVO.setStandardVOS(inspectTaskTargetItemQueryService.getObjStandardList(taskId, objId));
        // 查询关联对象
        List<TaskTargetRelationPO> targetNameList = taskTargetRelationService.list(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                                                                                           .select(TaskTargetRelationPO::getTargetId, TaskTargetRelationPO::getTargetName)
                                                                                           .eq(TaskTargetRelationPO::getTaskId, relationPO.getTaskId())
                                                                                           .eq(TaskTargetRelationPO::getTargetParentId, inspectionVO.getTargetId())
                                                                                           .groupBy(TaskTargetRelationPO::getTargetId)
                                                                                  );
        if (CollectionUtils.isNotEmpty(targetNameList)) {
            inspectionVO.setChildTargetNameList(targetNameList.stream()
                                                        .map(p -> KeyValueDTO.builder().longKey(p.getTargetId()).value(p.getTargetName())
                                                                .build()).collect(Collectors.toList()));
        }
        return inspectionVO;
    }

    /**
     * 保存任务
     */
    private InspectTaskPO saveTask(InspectTaskPO taskPO) {
        // 保存任务数据
        this.save(taskPO);
        // 检查任务消息配置
        this.checkTaskGenerateSendMsg(taskPO);
        inspectTaskStatusChangeService.changeForTaskAccept(taskPO.getId());
        return taskPO;
    }

    /**
     * 处理任务关联关系/和巡检内容
     */
    private void handlerTaskRelation(InspectTaskPO taskPO, List<PlanRelationPO> planRelationPOList,
                                     Map<Long, InspectStandardPO> standardPOMap, Map<Long, InspectionItemVO> itemVOMap,
                                     List<TaskTargetRelationPO> taskTargetRelationPOList, List<InspectTaskTargetItemPO> resultList
                                    ) {
        var targetIdMap = new HashMap<Long, Boolean>();

        Map<String, List<PlanRelationPO>> categoryMap = planRelationPOList.stream()
                .collect(Collectors.groupingBy(p -> String.valueOf(p.getTargetCategory())));
        categoryMap.forEach((category, list) -> {
            List<InspectTargetInfoDTO> targetInfoDTOList = inspectTargetInfoQuery.getInspectObject(category, list.stream()
                    .map(PlanRelationPO::getTargetId)
                    .collect(Collectors.toList()), null);
            Map<Long, InspectTargetInfoDTO> targetMap = targetInfoDTOList.stream()
                    .collect(Collectors.toMap(InspectTargetInfoDTO::getTargetId, targetInfoDTO -> targetInfoDTO, (o, n) -> o));
            list.forEach(relation -> {
                // 组装任务对象关联关系数据
                if (!targetIdMap.containsKey(relation.getTargetId())) {
                    InspectTargetInfoDTO target = targetMap.get(relation.getTargetId());
                    if (target == null) {
                        target = new InspectTargetInfoDTO();
                    }
                    taskTargetRelationPOList.add(new TaskTargetRelationPO(IdWorker.getId(), taskPO.getPlanId(), taskPO.getId(), InspectTargetCategoryEnum.getTargetCategoryEnum(target.getTargetCategory()), target.getTargetType(), target.getTargetId(), target.getTargetCode(), target.getTargetName(), target.getLatAndLon(), target.getLocation(), target.getTargetParentId(), target.getAddress(), target.getAreaId(), target.getAreaName()));
                    targetIdMap.put(relation.getTargetId(), true);
                }
                /* 组装巡检内容 */
                this.handlerTaskTargetItem(taskPO, relation, standardPOMap, itemVOMap, resultList);
            });
        });
    }

    /**
     * 组装任务对象的巡检内容
     */
    private void handlerTaskTargetItem(InspectTaskPO taskPO, PlanRelationPO relation, Map<Long, InspectStandardPO> standardPOMap,
                                       Map<Long, InspectionItemVO> itemVOMap, List<InspectTaskTargetItemPO> resultList
                                      ) {
        /* 组装巡检内容 */
        InspectTaskTargetItemPO po = InspectTaskTargetItemPO.builder()
                .taskId(taskPO.getId())
                .targetId(relation.getTargetId())
                .standardId(relation.getStandardId())
                .itemId(relation.getItemId())
                .build();
        InspectStandardPO standardPO = standardPOMap.get(relation.getStandardId());
        if (standardPO != null) {
            po.setStandardCode(standardPO.getCode());
            po.setStandardName(standardPO.getName());
        }
        InspectionItemVO itemVO = itemVOMap.get(relation.getItemId());
        if (itemVO != null) {
            po.setItemName(itemVO.getName());
            po.setItemAlias(itemVO.getAlias());
            po.setImageResult(itemVO.getImageResult());
            po.setRemarkResult(itemVO.getRemarkResult());
            po.setResultItemValue(itemVO.getResultItemValue());
            po.setItemTag(itemVO.getItemTag());
            po.setResultId(itemVO.getResultId());
            Map<Long, List<InspectResultPO>> resultMap = this.getRsultMap();
            List<InspectResultPO> resultPOList = resultMap.get(po.getResultId());
            if (CollectionUtils.isNotEmpty(resultPOList)) {
                InspectResultPO result = resultPOList.get(0);
                InspectResultVO vo = ObjectUtils.copying(result, InspectResultVO.class);
                List<InspectResultOptionCommandModel> items = ObjectUtils.copying(resultPOList, InspectResultOptionCommandModel.class);
                vo.setOptions(items);
                po.setResultText(JSON.toJSONString(vo));
            }
        }
        resultList.add(po);
    }

    private Map<Long, InspectStandardPO> getInspectStandardMap(Set<Long> idSet) {
        Map<Long, InspectStandardPO> standardPOMap = new HashMap<>();
        List<InspectStandardPO> standardPOList = inspectStandardMapper.selectList(Wrappers.<InspectStandardPO>lambdaQuery()
                                                                                          .in(CollectionUtils.isNotEmpty(idSet), InspectStandardPO::getId, idSet));
        if (CollectionUtils.isNotEmpty(standardPOList)) {
            standardPOList.forEach(po -> standardPOMap.put(po.getId(), po));
        }
        return standardPOMap;
    }

    private Map<Long, InspectionItemVO> getInspectItemVOMap(Set<Long> idSet) {
        Map<Long, InspectionItemVO> itemVOMap = new HashMap<>();
        // 模板中的巡检项
        List<InspectItemTemplatePO> templateItemPOList = inspectItemTemplateMapper.selectList(Wrappers.<InspectItemTemplatePO>lambdaQuery()
                                                                                                      .in(CollectionUtils.isNotEmpty(idSet), InspectItemTemplatePO::getId, idSet));
        if (CollectionUtils.isNotEmpty(templateItemPOList)) {
            templateItemPOList.forEach(item -> itemVOMap.put(item.getId(), InspectionItemVO.builder().id(item.getId()).name(item.getName())
                    .alias(item.getAlias()).resultItemValue(item.getResultItemValue()).imageResult(item.getImageResult())
                    .remarkResult(item.getRemarkResult()).itemTag(item.getItemTag()).resultId(item.getResultId()).build()));
        }
        // 巡检标准中的巡检项
        List<InspectStandardItemPO> standardItemPOList = inspectStandardItemMapper.selectList(Wrappers.<InspectStandardItemPO>lambdaQuery()
                                                                                                      .in(CollectionUtils.isNotEmpty(idSet), InspectStandardItemPO::getId, idSet));
        if (CollectionUtils.isNotEmpty(standardItemPOList)) {
            standardItemPOList.forEach(item -> itemVOMap.put(item.getId(), InspectionItemVO.builder().id(item.getId()).name(item.getName())
                    .alias(item.getAlias()).resultItemValue(item.getResultItemValue()).imageResult(item.getImageResult())
                    .remarkResult(item.getRemarkResult()).itemTag(item.getItemTag()).resultId(item.getResultId()).build()));
        }

        return itemVOMap;
    }

    private Map<Long, List<InspectResultPO>> getRsultMap() {
        Map<Long, List<InspectResultPO>> resultMap = new HashMap<>();
        List<InspectResultPO> resultPOList = inspectResultMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isNotEmpty(resultPOList)) {
            return resultPOList.stream().collect(Collectors.groupingBy(InspectResultPO::getId));
        }
        return resultMap;
    }

    /**
     * 保存任务关联关系
     * 任务拆解时使用
     */
    private List<TaskTargetRelationPO> getTaskRelation(InspectTaskPO taskPO, TaskReceiveUsersParam dto) {
        //组装任务对象关联关系
        List<TaskTargetRelationPO> taskTargetRelationPOList = new ArrayList<>();
        dto.getTaskTargetDTOList().forEach(t -> {
            TaskTargetRelationPO po = new TaskTargetRelationPO();
            po.setTaskId(taskPO.getId());
            po.setTenant(taskPO.getTenant());
            po.setPlanId(taskPO.getPlanId());
            po.setTargetId(t.getTargetId());
            po.setTargetCategory(t.getTargetCategory());
            po.setTargetCode(t.getTargetCode());
            po.setTargetName(t.getTargetName());
            po.setTargetType(t.getTargetType());
            taskTargetRelationPOList.add(po);
        });
        return taskTargetRelationPOList;
    }

    private TaskRateResult handleTaskRateResult(InspectTaskPO po) {
        TaskRateResult rateResult = new TaskRateResult();
        rateResult.setTaskName(po.getName());
        rateResult.setTargetAllNum(po.getTargetNum());
        rateResult.setStatus(po.getStatus());
        rateResult.setReceiveNames(inspectQuery.extendUserNames(po.getTenant(), po.getReceiveIds()));
        rateResult.setRate(this.getTaskRecordRate(po.getId(), po.getTargetNum(), po.getIsSplit()));
        return rateResult;
    }

    /**
     * 获取任务的记录完成率
     *
     * @Param isSplit 是否是拆解任务 默认不是
     */
    @Override
    public Double getTaskRecordRate(Long taskId, Integer targetNum, Boolean isSplit) {
        if (targetNum == null || targetNum <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (isSplit == null) {
            isSplit = false;
        }

        Long count = 0L;
        if (isSplit) {
            // 获取子任务
            List<InspectTaskPO> sonList = this.lambdaQuery().eq(InspectTaskPO::getParentId, taskId).list();
            count = Optional.ofNullable(sonList).orElse(Collections.emptyList())
                    .stream().mapToLong(o -> this.getTaskRecordNum(o.getId(), null)).sum();
        } else {
            count = this.getTaskRecordNum(taskId, null);
        }

        double d = (count * 1D / targetNum) * 100;
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取任务 已完成巡检数
     *
     * @param taskId     任务id 必填
     * @param resultEnum 结果(正常异常) 可选
     *
     * @return 已完成巡检数
     */
    private Long getTaskRecordNum(Long taskId, InspectRecordResultEnum resultEnum) {

        return recordQueryService.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, taskId)
                .eq(resultEnum != null, InspectRecordPO::getResult, resultEnum)
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.OVER)
                .count();
    }

    /**
     * 获取任务的所有租户code
     *
     * @return 租户code
     */
    @Override
    public List<Long> getTaskTenantList() {
        return taskMapper.getTaskTenantList();
    }

    /**
     * 任务下发发送消息
     *
     * @param taskPO 任务信息
     */
    @Override
    public void checkTaskGenerateSendMsg(InspectTaskPO taskPO) {
        InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get());
        Map<String, String> paramMap = new HashMap<>();
        Set<Long> userIds = handlerService.stringIdsToList(taskPO.getReceiveIds());
        paramMap.put("taskName", taskPO.getName());
        paramMap.put("content", DateUtils.format(taskPO.getValidEndTime()));
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(taskPO.getTenant())
                .applicationCode(businessTypeEnum.getApplicationCode())
                .msgSceneCode(businessTypeEnum.getMsgSceneCode(InspectNoticeModuleTypeEnum.TASK_GENERATE.name()))
                .businessId(String.valueOf(taskPO.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(MessageSendUtil.getNotifyUserByIds(userIds))
                .build();
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        try {
            MessageSendResponse messageSendResponse = sendClient.sceneSend(message);
            log.info("巡检任务下发消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("巡检任务下发消息发送失败", e);
        }
    }

    /**
     * @Author wql
     * @Description 巡检任务数据采集接口
     * @Date 17:49 2023/9/8
     **/
    @Override
    public List<InspectTaskDataModel> getSafetyManagementDataModel(Period period) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
        List<InspectTaskDataModel> result = new ArrayList<>();
        try {
            // 用xml查询采集数据
            List<InspectTaskPO> list = this.baseMapper.getTaskCollectData(SessionContextHolder.getRequester().getTenant(), period);
            /* 已转换为上面的mapper查询(为了查询出已经逻辑删除的数据)
            List<InspectTaskPO> list = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, 0)
                    .between(period != null, InspectTaskPO::getUpdateTime,
                            ObjectUtils.ifNull(period, Period::getBegin),
                            ObjectUtils.ifNull(period, Period::getEnd)).list();
                            */
            if (ObjectUtils.isNotEmpty(list)) {
                List<Long> userIds = list.stream().filter(t -> StringUtils.isNotBlank(t.getReceiveIds()))
                        .map(t -> {
                            return Stream.of(t.getReceiveIds().split(","))
                                    .map(Long::valueOf)
                                    .collect(Collectors.toList());
                        }).flatMap(Collection::stream).distinct().collect(Collectors.toList());
                Map<String, String> userMap = Optional.ofNullable(userClient.listUserByIds(userIds))
                        .orElse(new ArrayList<>())
                        .stream().collect(Collectors.toMap(t -> t.getId().toString(), UserDTO::getName));

                list.forEach(taskPO -> {
                    result.add(InspectTaskDataModel.builder()
                                       .id(taskPO.getId())
                                       .taskName(taskPO.getName())
                                       .taskCreateTime(taskPO.getCreateTime())
                                       .typeName(taskPO.getTypeName())
                                       .taskType(taskPO.getCheckTaskId() == 0L ? "普通" : "稽查")
                                       .levelCode(taskPO.getLevel().name())
                                       .levelName(taskPO.getLevel().getDescription())
                                       .statusCode(taskPO.getStatus().name())
                                       .statusName(taskPO.getStatus().getDescription())
                                       .executor(getUserNames(taskPO.getReceiveIds(), userMap))
                                       .validEndTime(taskPO.getValidEndTime())
                                       .targetNum(taskPO.getTargetNum())
                                       .progress(getTaskRecordRate(taskPO.getId(), taskPO.getTargetNum(), taskPO.getIsSplit()))
                                       .updateTime(taskPO.getUpdateTime())
                                       .createTime(taskPO.getCreateTime())
                                       .tenant(taskPO.getTenant())
                                       .createBy(taskPO.getCreateBy())
                                       .updateBy(taskPO.getUpdateBy())
                                       .deleted(taskPO.getDeleted())
                                       .businessType(taskPO.getBusinessType())
                                       .build());
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
        return result;
    }

    @Override
    public TaskObjCountResult getTaskObjCount(Long taskId) {
        // 获取任务信息判断是否有子任务
        InspectTaskPO parentTask = this.getById(taskId);
        Assert.isNotFound(parentTask, "任务不存在，检查是否被删除！");
        TaskPageParam param = new TaskPageParam();
        param.setPlanId(parentTask.getPlanId());
        // 待巡检数
        var waitNum = new AtomicLong();
        // 已巡检数
        var inspectNum = new AtomicLong();
        // 结果异常数
        var errNum = new AtomicLong();
        // 异常对象修复数
        var repNum = new AtomicLong();
        List<InspectRecordVO> recordTaskResultList = new ArrayList<>();

        if (!parentTask.getIsSplit()) {
            param.setTaskId(parentTask.getId());
            recordTaskResultList.addAll(inspectRecordMapper.querTaskRecordList(param));
        } else {
            // 获取子任务列表
            List<Long> taskIdList = new ArrayList<>();
            this.lambdaQuery().eq(true, InspectTaskPO::getParentId, taskId).list()
                    .forEach(t -> taskIdList.add(t.getId()));
            if (!taskIdList.isEmpty()) {
                param.setTaskIdList(taskIdList);
                recordTaskResultList.addAll(inspectRecordMapper.querTaskRecordList(param));
            }
        }

        recordTaskResultList.forEach(v -> {
            //计算
            if (InspectRecordStatusEnum.EXECUTING.name().equals(v.getStatus())) {
                waitNum.getAndIncrement();
            } else {
                if (InspectRecordStatusEnum.WAIT_REPAIR.name().equals(v.getStatus()) ||
                    InspectRecordStatusEnum.CANCEL_REPAIR.name().equals(v.getStatus()) ||
                    InspectRecordStatusEnum.OVER.name().equals(v.getStatus())) {
                    inspectNum.getAndIncrement();
                }
                if (InspectRecordResultEnum.ABNORMAL.name().equals(v.getResult())) {
                    errNum.getAndIncrement();
                    if (InspectRepairStatusEnum.REPAIRED.name().equals(v.getRepairStatus())) {
                        repNum.getAndIncrement();
                    }
                }
            }
        });

        return TaskObjCountResult.builder().targetAllNum(parentTask.getTargetNum()).completedTargetNum(inspectNum.get())
                .waitTargetNum(waitNum.get()).errTargetNum(errNum.get()).repairCount(repNum.get()).build();
    }

    @Override
    public InspectionTaskStatisticsVO getInspectionTaskNumStatistics(Period period) {
        List<InspectTaskPO> list = this.lambdaQuery().between(InspectTaskPO::getValidBeginTime, period.getBegin(), period.getEnd())
                .eq(InspectTaskPO::getParentId, 0)
                .select(InspectTaskPO::getStatus, InspectTaskPO::getId).list();
        return InspectionTaskStatisticsVO.builder()
                .inspectionTaskNum(list.size())
                .waitAcceptNum(list.stream().filter(po -> InspectTaskStatusEnum.ACCEPT.equals(po.getStatus())).count())
                .underwayTaskNum(list.stream().filter(po -> InspectTaskStatusEnum.EXECUTING.equals(po.getStatus())).count())
                .build();
    }

    @Override
    public List<TaskDepStatistics> inspectionDepTaskNumStatistics(InspectDepStatisticsParam param) {
        List<InspectTaskPO> newList = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), InspectTaskPO::getValidBeginTime,
                                                                 ObjectUtils.ifNull(param.getPeriod(), Period::getBegin),
                                                                 ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .eq(InspectTaskPO::getParentId, 0)
                .eq(StringUtils.isNotBlank(param.getStatus()), InspectTaskPO::getStatus, param.getStatus())
                .select(InspectTaskPO::getReceiveDeptIds, InspectTaskPO::getId, InspectTaskPO::getStatus).list();
        Period period = DatePeriodUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        List<InspectTaskPO> oldList = this.lambdaQuery().between(ObjectUtils.isNotNull(period), InspectTaskPO::getValidBeginTime,
                                                                 ObjectUtils.ifNull(period, Period::getBegin),
                                                                 ObjectUtils.ifNull(period, Period::getEnd))
                .eq(InspectTaskPO::getParentId, 0)
                .eq(StringUtils.isNotBlank(param.getStatus()), InspectTaskPO::getStatus, param.getStatus())
                .select(InspectTaskPO::getReceiveDeptIds, InspectTaskPO::getId, InspectTaskPO::getStatus).list();

        return depStatistics(newList, oldList);
    }

    public List<TaskDepStatistics> depStatistics(List<InspectTaskPO> newList, List<InspectTaskPO> oldList) {
        Map<Long, Long> newDepMap = inspectionDepStatistics(newList);
        Map<Long, Long> oldDepMap = inspectionDepStatistics(oldList);
        List<TaskDepStatistics> taskDepStatistics = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(newDepMap)) {
            Map<Long, String> depNameMap = organizationClientService.getOrganizationNames(newDepMap.keySet());
            newDepMap.keySet().forEach(key -> {
                taskDepStatistics.add(TaskDepStatistics.builder()
                                              .depId(key)
                                              .depName(depNameMap.get(key))
                                              .taskNum(newDepMap.get(key))
                                              .chainRatio(MathUtils.calculateRingRatio(newDepMap.get(key),
                                                                                       oldDepMap.get(key) == null ? 0
                                                                                                                  : oldDepMap.get(key))
                                                                  .floatValue())
                                              .build());
            });
        }
        return taskDepStatistics.stream().sorted(Comparator.comparing(TaskDepStatistics::getTaskNum).reversed())
                .collect(Collectors.toList());
    }

    private Map<Long, Long> inspectionDepStatistics(List<InspectTaskPO> list) {
        if (ObjectUtils.isNotEmpty(list)) {
            Map<String, Long> collect = list.stream()
                    .collect(Collectors.groupingBy(InspectTaskPO::getReceiveDeptIds, Collectors.counting()));
            Map<Long, Long> depStatistics = new HashMap<>();
            collect.keySet().forEach(key -> {
                String[] split = key.split(",");
                for (String depId : split) {
                    if (StringUtils.isNotBlank(depId)) {
                        Long parsed = Long.parseLong(depId);
                        depStatistics.put(parsed, (depStatistics.containsKey(parsed) ? depStatistics.get(parsed) : 0) + collect.get(key));
                    }
                }
            });
            return depStatistics;
        }
        return new HashMap<>();
    }

    @Override
    public Float inspectionTaskFinishedRatio(Period period, InspectTaskStatusEnum statusEnum) {
        List<InspectTaskPO> list = this.lambdaQuery().between(InspectTaskPO::getCreateTime, period.getBegin(), period.getEnd())
                .eq(InspectTaskPO::getParentId, 0)
                .select(InspectTaskPO::getStatus, InspectTaskPO::getId).list();
        if (ObjectUtils.isEmpty(list)) {
            return 100f;
        }
        long count = list.stream().filter(po -> statusEnum.equals(po.getStatus())).count();
        return MathUtils.calculateProportionPercentage(count, list.size()).floatValue();
    }

    /**
     * 获取任务执行状态统计
     *
     * @param param period
     *
     * @return
     */
    @Override
    public InspectTaskStatusStatVO taskStatusStat(InspectionCommonParam param) {
        InspectTaskStatusStatVO vo = new InspectTaskStatusStatVO();

        Long totalCount = getTaskTotal(copyTaskStatusStatParam(param, null, null));
        Long completedCount = getTaskTotal(copyTaskStatusStatParam(param,
                                                                  Arrays.asList(InspectTaskStatusEnum.OVER.name(),
                                                                                InspectTaskStatusEnum.STOP.name()),
                                                                  null));
        Long uncompletedCount = getTaskTotal(copyTaskStatusStatParam(param,
                                                                    Arrays.asList(InspectTaskStatusEnum.ACCEPT.name(),
                                                                                  InspectTaskStatusEnum.EXECUTING.name()),
                                                                    null));
        Long timeoutCount = getTaskTotal(copyTaskStatusStatParam(param, null, true));

        vo.setTotalCount(totalCount);
        vo.setCompletedCount(completedCount);
        vo.setUncompletedCount(uncompletedCount);
        vo.setTimeoutCount(timeoutCount);
        vo.setCompletedRate(MathUtils.calculateProportionPercentage(completedCount, totalCount));
        return vo;
    }

    private InspectionCommonParam copyTaskStatusStatParam(InspectionCommonParam param, List<String> statusList, Boolean overTimeBool) {
        InspectionCommonParam queryParam = ObjectUtils.copying(param, InspectionCommonParam.class);
        queryParam.setStatus(null);
        queryParam.setStatusList(statusList);
        queryParam.setOverTimeBool(overTimeBool);
        return queryParam;
    }

    @Override
    public InspectAbnormalOverviewVO abnormalOverview(InspectionCommonParam param) {
        Period period = resolveInspectionStatisticsPeriod(param);
        InspectionCommonParam currentParam = ObjectUtils.copying(param, InspectionCommonParam.class);
        currentParam.setPeriod(period);

        InspectionCommonParam previousParam = ObjectUtils.copying(currentParam, InspectionCommonParam.class);
        previousParam.setPeriod(getPreviousSameLengthPeriod(period));

        Long abnormalTaskTotal = countAbnormalTask(currentParam);
        Long previousAbnormalTaskTotal = countAbnormalTask(previousParam);
        Long abnormalPointTotal = countAbnormalPoint(currentParam);
        Long previousAbnormalPointTotal = countAbnormalPoint(previousParam);

        return InspectAbnormalOverviewVO.builder()
                .abnormalTask(buildAbnormalOverviewMetric(abnormalTaskTotal, previousAbnormalTaskTotal))
                .abnormalPoint(buildAbnormalOverviewMetric(abnormalPointTotal, previousAbnormalPointTotal))
                .build();
    }

    @Override
    public List<InspectTaskTypeDistributionVO> taskTypeDistribution(InspectionCommonParam param) {
        Period period = resolveInspectionStatisticsPeriod(param);
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            List<Map<String, Object>> rows = this.listMaps(Wrappers.<InspectTaskPO>query()
                    .select("type_id AS typeId", "type_name AS typeName", "COUNT(1) AS taskTotal")
                    .eq("parent_id", 0L)
                    .ge("create_time", period.getBegin())
                    .le("create_time", period.getEnd())
                    .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), "plan_id", param.getPlanIdList())
                    .groupBy("type_id", "type_name")
                    .orderByDesc("taskTotal"));
            if (CollectionUtils.isEmpty(rows)) {
                return Collections.emptyList();
            }
            return rows.stream().map(this::convertTaskTypeDistribution).collect(Collectors.toList());
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    @Override
    public List<InspectTaskDepartmentCompletionStatVO> taskDepartmentCompletionStat(InspectionCommonParam param) {
        Period period = resolveInspectionStatisticsPeriod(param);
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            List<InspectTaskPO> tasks = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, 0L)
                    .ge(InspectTaskPO::getCreateTime, period.getBegin())
                    .le(InspectTaskPO::getCreateTime, period.getEnd())
                    .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), InspectTaskPO::getPlanId, param.getPlanIdList())
                    .select(InspectTaskPO::getReceiveDeptIds, InspectTaskPO::getStatus)
                    .list();
            if (CollectionUtils.isEmpty(tasks)) {
                return Collections.emptyList();
            }

            Map<String, Long> taskTotalMap = tasks.stream()
                    .filter(task -> StringUtils.isNotBlank(task.getReceiveDeptIds()))
                    .collect(Collectors.groupingBy(InspectTaskPO::getReceiveDeptIds, Collectors.counting()));
            Map<String, Long> completedTaskTotalMap = tasks.stream()
                    .filter(task -> InspectTaskStatusEnum.OVER.equals(task.getStatus()))
                    .filter(task -> StringUtils.isNotBlank(task.getReceiveDeptIds()))
                    .collect(Collectors.groupingBy(InspectTaskPO::getReceiveDeptIds, Collectors.counting()));
            if (completedTaskTotalMap.isEmpty()) {
                return Collections.emptyList();
            }

            Set<Long> deptIds = completedTaskTotalMap.keySet().stream().map(Long::valueOf).collect(Collectors.toSet());
            Map<Long, String> deptNameMap = organizationClientService.getOrganizationNames(deptIds);

            return completedTaskTotalMap.entrySet().stream()
                    .map(entry -> buildTaskDepartmentCompletionStat(entry, taskTotalMap, deptNameMap))
                    .sorted(Comparator.comparing(InspectTaskDepartmentCompletionStatVO::getCompletedTaskTotal).reversed()
                                    .thenComparing(InspectTaskDepartmentCompletionStatVO::getTaskTotal, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    private InspectTaskDepartmentCompletionStatVO buildTaskDepartmentCompletionStat(Map.Entry<String, Long> completedEntry,
                                                                                   Map<String, Long> taskTotalMap,
                                                                                   Map<Long, String> deptNameMap
                                                                                  ) {
        Long deptId = Long.valueOf(completedEntry.getKey());
        return InspectTaskDepartmentCompletionStatVO.builder()
                .deptId(deptId)
                .deptName(deptNameMap.getOrDefault(deptId, ""))
                .taskTotal(taskTotalMap.getOrDefault(completedEntry.getKey(), 0L))
                .completedTaskTotal(completedEntry.getValue())
                .build();
    }

    @Override
    public List<InspectTaskDepartmentMemberCompletionStatVO> taskDepartmentMemberCompletionStat(Long deptId, InspectionCommonParam param) {
        Assert.isParameterError(deptId == null, "部门ID不能为空!");
        Period period = resolveInspectionStatisticsPeriod(param);
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            List<InspectTaskPO> tasks = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, 0L)
                    .eq(InspectTaskPO::getReceiveDeptIds, String.valueOf(deptId))
                    .ge(InspectTaskPO::getCreateTime, period.getBegin())
                    .le(InspectTaskPO::getCreateTime, period.getEnd())
                    .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), InspectTaskPO::getPlanId, param.getPlanIdList())
                    .select(InspectTaskPO::getReceiveIds, InspectTaskPO::getStatus)
                    .list();
            if (CollectionUtils.isEmpty(tasks)) {
                return Collections.emptyList();
            }

            Map<Long, Long> taskTotalMap = countTaskByReceiveUser(tasks);
            if (taskTotalMap.isEmpty()) {
                return Collections.emptyList();
            }
            Map<Long, Long> completedTaskTotalMap = countTaskByReceiveUser(tasks.stream()
                    .filter(task -> InspectTaskStatusEnum.OVER.equals(task.getStatus()))
                    .collect(Collectors.toList()));
            Map<Long, String> userNameMap = Optional.ofNullable(userClient.listUserByIds(taskTotalMap.keySet()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (first, second) -> first));

            return taskTotalMap.entrySet()
                    .stream()
                    .map(entry -> buildTaskDepartmentMemberCompletionStat(entry, completedTaskTotalMap, userNameMap))
                    .sorted(Comparator.comparing(InspectTaskDepartmentMemberCompletionStatVO::getCompletedTaskTotal).reversed()
                                    .thenComparing(InspectTaskDepartmentMemberCompletionStatVO::getTaskTotal, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    private Map<Long, Long> countTaskByReceiveUser(List<InspectTaskPO> tasks) {
        Map<Long, Long> userTaskCountMap = new HashMap<>();
        Optional.ofNullable(tasks).orElse(Collections.emptyList())
                .forEach(task -> parseReceiveUserIds(task.getReceiveIds())
                        .forEach(userId -> userTaskCountMap.merge(userId, 1L, Long::sum)));
        return userTaskCountMap;
    }

    private List<Long> parseReceiveUserIds(String receiveIds) {
        if (StringUtils.isBlank(receiveIds)) {
            return Collections.emptyList();
        }
        return StringUtils.splitAndIgnoreBlank(receiveIds)
                .stream()
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private InspectTaskDepartmentMemberCompletionStatVO buildTaskDepartmentMemberCompletionStat(Map.Entry<Long, Long> taskTotalEntry,
                                                                                               Map<Long, Long> completedTaskTotalMap,
                                                                                               Map<Long, String> userNameMap
                                                                                              ) {
        Long taskTotal = taskTotalEntry.getValue();
        Long completedTaskTotal = completedTaskTotalMap.getOrDefault(taskTotalEntry.getKey(), 0L);
        BigDecimal completionRate = MathUtils.calculateProportionPercentage(completedTaskTotal, taskTotal);
        return InspectTaskDepartmentMemberCompletionStatVO.builder()
                .memberId(taskTotalEntry.getKey())
                .memberName(userNameMap.getOrDefault(taskTotalEntry.getKey(), ""))
                .taskTotal(taskTotal)
                .completedTaskTotal(completedTaskTotal)
                .completionRate(completionRate)
                .completionText(completedTaskTotal + " / " + taskTotal + " (" + MathUtils.simplePercentageFormat(completionRate) + ")")
                .build();
    }

    private InspectTaskTypeDistributionVO convertTaskTypeDistribution(Map<String, Object> row) {
        return InspectTaskTypeDistributionVO.builder()
                .typeId(getMapLongValue(row, "typeId", "type_id"))
                .typeName(getMapStringValue(row, "typeName", "type_name"))
                .taskTotal(ObjectUtils.ifNull(getMapLongValue(row, "taskTotal", "task_total", "COUNT(1)"), 0L))
                .build();
    }

    private Long getMapLongValue(Map<String, Object> row, String... keys) {
        Object value = getMapValue(row, keys);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    private String getMapStringValue(Map<String, Object> row, String... keys) {
        Object value = getMapValue(row, keys);
        return value == null ? "" : value.toString();
    }

    private Object getMapValue(Map<String, Object> row, String... keys) {
        if (row == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            if (row.containsKey(key)) {
                return row.get(key);
            }
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            for (String key : keys) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private Period resolveInspectionStatisticsPeriod(InspectionCommonParam param) {
        Period period = param.getPeriod();
        Assert.isParameterError(period == null || period.getBegin() == null || period.getEnd() == null, "统计时间范围不允许为空!");
        Assert.isParameterError(period.getEnd().isBefore(period.getBegin()), "统计时间范围不合法!");
        return period;
    }

    private Period getPreviousSameLengthPeriod(Period period) {
        Duration duration = Duration.between(period.getBegin(), period.getEnd()).plusNanos(1);
        return new Period(period.getBegin().minus(duration), period.getEnd().minus(duration));
    }

    private InspectAbnormalOverviewVO.OverviewMetric buildAbnormalOverviewMetric(Long currentTotal, Long previousTotal) {
        Long safeCurrentTotal = ObjectUtils.ifNull(currentTotal, 0L);
        Long safePreviousTotal = ObjectUtils.ifNull(previousTotal, 0L);
        return InspectAbnormalOverviewVO.OverviewMetric.builder()
                .total(safeCurrentTotal)
                .chainRatio(MathUtils.calculateRingRatioPercentage(safeCurrentTotal, safePreviousTotal))
                .build();
    }

    private Long countAbnormalTask(InspectionCommonParam param) {
        Period period = param.getPeriod();
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            Long total = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, 0L)
                    .eq(InspectTaskPO::getResult, InspectRecordResultEnum.ABNORMAL.name())
                    .ge(period.getBegin() != null, InspectTaskPO::getCreateTime, period.getBegin())
                    .le(period.getEnd() != null, InspectTaskPO::getCreateTime, period.getEnd())
                    .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), InspectTaskPO::getPlanId, param.getPlanIdList())
                    .count();
            return total == null ? 0L : total;
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    private Long countAbnormalPoint(InspectionCommonParam param) {
        Period period = param.getPeriod();
        List<Long> recordIdList = getRecordIdListByItemName(param.getItemName());
        if (StringUtils.isNotBlank(param.getItemName()) && CollectionUtils.isEmpty(recordIdList)) {
            return 0L;
        }
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            List<Object> countList = inspectRecordMapper.selectObjs(Wrappers.<InspectRecordPO>query()
                    .select("count(distinct target_id)")
                    .isNotNull("target_id")
                    .eq("result", InspectRecordResultEnum.ABNORMAL.name())
                    .like(StringUtils.isNotBlank(param.getAreaName()), "area_name", param.getAreaName())
                    .eq(StringUtils.isNotBlank(param.getObjectType()), "target_category", param.getObjectType())
                    .ge(period.getBegin() != null, "time", period.getBegin())
                    .le(period.getEnd() != null, "time", period.getEnd())
                    .in(CollectionUtils.isNotEmpty(param.getPlanIdList()), "plan_id", param.getPlanIdList())
                    .in(CollectionUtils.isNotEmpty(recordIdList), "id", recordIdList));
            return getFirstLongValue(countList);
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    private List<Long> getRecordIdListByItemName(String itemName) {
        if (StringUtils.isBlank(itemName)) {
            return Collections.emptyList();
        }
        List<InspectRecordItemPO> inspectRecordItemPOList = inspectRecordItemMapper.selectList(Wrappers.<InspectRecordItemPO>lambdaQuery()
                                                                                                      .like(InspectRecordItemPO::getItemName, itemName)
                                                                                                      .select(InspectRecordItemPO::getRecordId));
        if (CollectionUtils.isEmpty(inspectRecordItemPOList)) {
            return Collections.emptyList();
        }
        return inspectRecordItemPOList.stream().map(InspectRecordItemPO::getRecordId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    private Long getFirstLongValue(List<Object> values) {
        if (CollectionUtils.isEmpty(values) || values.get(0) == null) {
            return 0L;
        }
        Object value = values.get(0);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    @Override
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Float inspectionTaskFinishedRatioPermission(Period period, InspectTaskStatusEnum statusEnum) {
        return this.inspectionTaskFinishedRatio(period, statusEnum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectTask(InspectTaskParam taskParam) {
        InspectTaskPO taskPO = ObjectUtils.copying(taskParam, InspectTaskPO.class);
        // 当接收类型 2＝部门时，需要把部门转换为 部门下的人
        if ("2".equals(taskParam.getReceiveType())) {
            taskPO.setReceiveIds(handlerService.HandlerDeptIdsAndPostIdsToUserIds(taskParam.getReceiveDeptIds(), taskParam.getReceivePostIds(), taskParam.getReceiveRoleIds()));
            Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "巡检任务选择的岗位下无人员");
        } else {
            // 为接受人时需要把部门也赋上值 （岗位暂时未赋值，岗位并未使用）
            taskPO.setReceiveDeptIds(handlerService.handlerUserIdsToDeptIds(taskParam.getReceiveIds()));
        }
        List<PlanRelationPO> distinctTargetList = Optional.ofNullable(taskParam.getTaskTargetDTOList()).orElse(Collections.emptyList())
                .parallelStream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PlanRelationPO::getTargetId))), ArrayList::new)
                                         );
        taskPO.setId(IdWorker.getId());
        taskPO.setPlanId(taskPO.getId());
        taskPO.setTargetNum(distinctTargetList.size());
        taskPO.setLimitTotalMinute(handlerService.getLimitTotalMinute(taskParam.getLimitTimeJson()));
        if (Objects.nonNull(taskParam.getTypeId())) {
            InspectTypePO inspectTypePO = inspectTypeQueryService.getById(taskParam.getTypeId());
            taskPO.setTypeName(Objects.nonNull(inspectTypePO) ? inspectTypePO.getName() : null);
        }
        taskPO.setCode(BizCodeUtils.generateNoRandom("IPK"));
        String format = String.format("%04d", 1);
        taskPO.setName(taskPO.getName() + "_V" + format);
        taskPO.setId(IdWorker.getId());
        // 任务结束时间为计划开始时间加上任务的有效天(分钟)数
        taskPO.setCreateTime(LocalDateTime.now());
        taskPO.setUpdateTime(LocalDateTime.now());
        //任务有效时间
        taskPO.setValidBeginTime(taskPO.getCreateTime());
        taskPO.setValidEndTime(DateUtils.offset(taskPO.getCreateTime(), ChronoUnit.MINUTES, taskPO.getLimitTotalMinute()));
        taskPO.setPublisherId(SecurityContextHolder.getPrincipal().getId().toString());
        taskPO.setPublisherName(SecurityContextHolder.getPrincipal().getName());
        taskPO.setStatus(taskPO.getIsSplit() ? InspectTaskStatusEnum.SPLIT : InspectTaskStatusEnum.ACCEPT);
        taskPO.setCheckTaskId(0L);
        //处理对象ids
        taskPO.setTargetIds(handlerService.handlerTargetDTOListToTargetIds(taskParam.getTaskTargetDTOList()));
        taskPO.setBusinessType(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get());
        // 保存普通任务(非普通为稽查任务)
        if (taskPO.getIsSplit()) {
            // 主任务不需要保存巡检记录和发送通知消息
            this.save(taskPO);
        } else {
            log.info("创建巡检任务开始");
            this.saveTask(taskPO).getId();
            log.info("创建巡检任务结束");
        }
        List<TaskTargetRelationPO> taskTargetRelationPOS = new ArrayList<>();
        List<InspectTaskTargetItemPO> inspectTaskTargetItemPOS = new ArrayList<>();
        List<InspectRecordPO> inspectRecordPOList = new ArrayList<>();
        Map<String, InspectTargetInfoDTO> inspectTargetInfoDTOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(taskParam.getTaskTargetDTOList())) {
            Map<String, List<PlanRelationPO>> inspectTargetCategoryMap = taskParam
                    .getTaskTargetDTOList().stream().collect(Collectors.groupingBy(t -> t.getTargetCategory().name()));
            Set<InspectTargetInfoDTO> resultList = new HashSet<>(taskParam.getTaskTargetDTOList().size());
            inspectTargetCategoryMap.forEach((k, v) -> {
                List<Long> targetIdList = v.stream().map(PlanRelationPO::getTargetId).collect(Collectors.toList());
                resultList.addAll(inspectTargetInfoQuery.getTargetInfo(targetIdList, k));
            });
            inspectTargetInfoDTOMap = resultList.stream()
                    .collect(Collectors.toMap(t -> t.getTargetId() + "-" + t.getTargetCategory(),
                                              Function.identity(), (k1, k2) -> k1));
            buildTaskTargetRelationAndInspectRecord(taskParam, taskTargetRelationPOS, inspectTaskTargetItemPOS, taskPO, inspectTargetInfoDTOMap, inspectRecordPOList);
            taskParam.getTaskTargetDTOList().forEach(t ->
                                                             t.setPlanId(taskPO.getPlanId()));
        }
        if (CollectionUtils.isNotEmpty(inspectTaskTargetItemPOS)) {
            // 保存任务巡检内容
            inspectTaskTargetItemCommandService.saveBatch(inspectTaskTargetItemPOS);
            // 清空
            inspectTaskTargetItemPOS = new ArrayList<>();
        }
        //如果需要稽查，还有创建稽查任务
        if (taskParam.getIsCheck()) {
            // 创建稽查任务
            taskPO.setCheckTaskId(taskPO.getId());
            // 检查一下这一步会不把上面的值清空
            taskPO.setId(IdWorker.getId());
            taskPO.setCode(BizCodeUtils.generateNoRandom("IPK"));
            taskPO.setIsSplit(false);
            taskPO.setStatus(InspectTaskStatusEnum.ACCEPT);
            taskPO.setReceiveDeptIds(taskParam.getCheckDeptIds());
            taskPO.setReceivePostIds(taskParam.getCheckPostIds());
            taskPO.setReceiveIds(taskParam.getCheckUserIds());
            // 当稽查人类型 2＝部门时，需要把部门转换为 部门下的人
            if ("2".equals(taskParam.getCheckUserType())) {
                taskPO.setReceiveIds(handlerService.HandlerDeptIdsAndPostIdsToUserIds(taskParam.getCheckDeptIds(), taskParam.getCheckPostIds(), taskParam.getCheckRoleIds()));
                Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "稽查任务选择的岗位下无人员");
            } else {
                // 为接受人时需要把部门也赋上值 （岗位暂时未赋值，岗位并未使用）
                taskPO.setReceiveDeptIds(handlerService.handlerUserIdsToDeptIds(taskParam.getCheckUserIds()));
            }
            taskPO.setName(taskPO.getName() + "稽查");
            // 保存任务
            this.saveTask(taskPO);
            if (CollectionUtils.isNotEmpty(taskParam.getTaskTargetDTOList())) {
                buildTaskTargetRelationAndInspectRecord(taskParam, taskTargetRelationPOS, inspectTaskTargetItemPOS, taskPO, inspectTargetInfoDTOMap, inspectRecordPOList);
            }
            if (CollectionUtils.isNotEmpty(inspectTaskTargetItemPOS)) {
                // 保存任务巡检内容
                inspectTaskTargetItemCommandService.saveBatch(inspectTaskTargetItemPOS);
            }
        }
        // 批量保存 任务管理关系
        taskTargetRelationService.saveBatch(taskTargetRelationPOS);
        // 批量保存 计划和对象关联关系
        planService.saveBatchPlanRelation(taskParam.getTaskTargetDTOList());
        // 生成巡检记录
        recordCommandService.saveBatch(inspectRecordPOList);
    }

    private void buildTaskTargetRelationAndInspectRecord(InspectTaskParam taskParam, List<TaskTargetRelationPO> taskTargetRelationPOS,
                                                         List<InspectTaskTargetItemPO> inspectTaskTargetItemPOS, InspectTaskPO taskPO,
                                                         Map<String, InspectTargetInfoDTO> inspectTargetInfoDTOMap,
                                                         List<InspectRecordPO> inspectRecordPOList
                                                        ) {
        Map<Long, InspectStandardPO> standardPOMap = this.getInspectStandardMap(null);
        Map<Long, InspectionItemVO> itemVOMap = this.getInspectItemVOMap(null);
        Set<Long> targetIds = new HashSet<>();
        taskTargetRelationPOS.addAll(taskParam.getTaskTargetDTOList().stream().map(t -> {
            // 组装任务对象巡检内容
            this.handlerTaskTargetItem(taskPO, t, standardPOMap, itemVOMap, inspectTaskTargetItemPOS);
            TaskTargetRelationPO info = null;
            if (!targetIds.contains(t.getTargetId())) {
                info = ObjectUtils.copying(t, TaskTargetRelationPO.class);
                info.setTaskId(taskPO.getId());
                info.setPlanId(taskPO.getPlanId());
                InspectTargetInfoDTO inspectTargetInfoDTO = inspectTargetInfoDTOMap
                        .getOrDefault(info.getTargetId() + "-" + info.getTargetCategory().name(), new InspectTargetInfoDTO());
                InspectRecordPO recordPO = buildInspectRecordPO(taskPO, info, inspectTargetInfoDTO);
                inspectRecordPOList.add(recordPO);
                targetIds.add(info.getTargetId());
            }
            return info;
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private static InspectRecordPO buildInspectRecordPO(InspectTaskPO taskPO, TaskTargetRelationPO info,
                                                        InspectTargetInfoDTO inspectTargetInfoDTO
                                                       ) {
        return InspectRecordPO.builder()
                .planId(taskPO.getPlanId())
                .planName(taskPO.getPlanName())
                .taskId(taskPO.getId())
                .taskName(taskPO.getName())
                .status(InspectRecordStatusEnum.EXECUTING)
                .targetCategory(info.getTargetCategory().name())
                .targetId(info.getTargetId())
                .targetName(info.getTargetName())
                .areaId(inspectTargetInfoDTO.getAreaId())
                .areaName(inspectTargetInfoDTO.getAreaName())
                .build();
    }

    private String getUserNames(String receiveIds, Map<String, String> userMap) {
        StringBuilder sb = new StringBuilder();
        if (ObjectUtils.isNotEmpty(receiveIds)) {
            String[] split = receiveIds.split(",");
            for (String id : split) {
                String name = userMap.get(id);
                if (StringUtils.isNotBlank(name)) {
                    sb.append(name).append(",");
                }
            }
        }
        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
    }

    /**
     * @Author wql
     * @Description 判断巡检任务是否过期
     * @Date 9:59 2023/5/26
     **/
    public boolean checkTaskIsExpire(Long taskId) {
        InspectTaskPO byId = this.getById(taskId);
        Assert.isNotFound(byId, "任务不存在！");
        return !InspectTaskStatusEnum.EXPIRE.name().equals(byId.getStatus().name());
    }

    /**
     * 获取任务总数
     *
     * @param param period 时间
     *
     * @return 总数
     */
    @Override
    public Long getTaskTotal(InspectionCommonParam param) {
        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(param.getBusinessType());
        try {
            Set<Long> taskIds = new HashSet<>();
            if (StringUtils.isNotBlank(param.getAreaName())) {
                List<InspectRecordPO> inspectRecordPOS = recordQueryService.lambdaQuery()
                        .like(InspectRecordPO::getAreaName, param.getAreaName())
                        .list();
                if (CollectionUtils.isEmpty(inspectRecordPOS)) {
                    return 0L;
                }
                taskIds = inspectRecordPOS.stream().map(InspectRecordPO::getTaskId).collect(Collectors.toSet());
            }

            Long total = this.baseMapper.getTaskTotal(new ArrayList<>(taskIds), param);
            return total == null ? 0L : total;
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    @Override
    public HandlerRateMetricDTO getInspectTaskCompleteRateMetric(Period period) {
        List<InspectTaskPO> totalList = this.lambdaQuery().between(ObjectUtils.isNotNull(period), InspectTaskPO::getValidBeginTime,
                                                                   ObjectUtils.ifNull(period, Period::getBegin),
                                                                   ObjectUtils.ifNull(period, Period::getEnd))
                .eq(InspectTaskPO::getParentId, 0)
                .select(InspectTaskPO::getReceiveDeptIds, InspectTaskPO::getReceiveIds,
                        InspectTaskPO::getId, InspectTaskPO::getStatus).list();
        List<InspectTaskPO> finishList = this.lambdaQuery().between(ObjectUtils.isNotNull(period), InspectTaskPO::getValidBeginTime,
                                                                    ObjectUtils.ifNull(period, Period::getBegin),
                                                                    ObjectUtils.ifNull(period, Period::getEnd))
                .eq(InspectTaskPO::getParentId, 0)
                .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.OVER)
                .select(InspectTaskPO::getReceiveDeptIds, InspectTaskPO::getId, InspectTaskPO::getReceiveIds,
                        InspectTaskPO::getStatus).list();
        Map<Long, Long> newDepMap = inspectionDepStatistics(totalList);
        Map<Long, Long> finishMap = inspectionDepStatistics(finishList);
        List<HandlerRateDTO> handlerRateDept = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(newDepMap)) {
            Map<Long, String> depNameMap = organizationClientService.getOrganizationNames(newDepMap.keySet());
            newDepMap.keySet().forEach(key -> {
                handlerRateDept.add(HandlerRateDTO.builder()
                                            .name(depNameMap.get(key))
                                            .number(newDepMap.get(key))
                                            .rate(MathUtils.calculateProportionPercentage(
                                                    finishMap.get(key) == null ? 0 : finishMap.get(key),
                                                    newDepMap.get(key)
                                                                                         ).doubleValue())
                                            .build());
            });
        }
        //人员
        Map<Long, Long> userTottalMap = inspectionUserStatistics(totalList);
        Map<Long, Long> userFinishMap = inspectionUserStatistics(finishList);

        List<HandlerRateDTO> handlerRateUser = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(userTottalMap)) {
            Map<Long, String> userNameMap = userClient.listUserByIds(userTottalMap.keySet())
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            userTottalMap.keySet().forEach(key -> {
                handlerRateUser.add(HandlerRateDTO.builder()
                                            .name(userNameMap.get(key))
                                            .number(userTottalMap.get(key))
                                            .rate(MathUtils.calculateProportionPercentage(
                                                    userFinishMap.get(key) == null ? 0 : userFinishMap.get(key),
                                                    userTottalMap.get(key)
                                                                                         ).doubleValue())
                                            .build());
            });
        }
        return HandlerRateMetricDTO.builder()
                .topHandlerRateDept(handlerRateDept.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                                            .limit(5)
                                            .collect(Collectors.toList()))
                .bottomHandlerRateDept(handlerRateDept.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate))
                                               .limit(5)
                                               .collect(Collectors.toList()))
                .topHandlerRateUser(handlerRateUser.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                                            .limit(5)
                                            .collect(Collectors.toList()))
                .bottomHandlerRateUser(handlerRateUser.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate))
                                               .limit(5)
                                               .collect(Collectors.toList()))
                .build();
    }

    @Override
    public HandlerEfficiencyMetricDTO getInspectTaskCompleteEfficiencyMetric(Period period) {
        List<HandlerEfficiencyDTO> topHandlerUserList = this.baseMapper.selectInspectTaskCompleteEfficiencyMetric(period, null);
        List<HandlerEfficiencyDTO> bottomHandlerUserList = this.baseMapper.selectInspectTaskCompleteEfficiencyMetric(period, "DESC");
        Set<Long> userIds = topHandlerUserList.stream().map(t -> Long.parseLong(t.getName()))
                .collect(Collectors.toSet());
        userIds.addAll(bottomHandlerUserList.stream().map(t -> Long.parseLong(t.getName())).collect(Collectors.toSet()));
        Map<String, String> userMap = userClient.listUserByIds(userIds)
                .stream().collect(Collectors.toMap(t -> t.getId().toString(), UserDTO::getName));
        topHandlerUserList.forEach(t -> {
            t.setName(userMap.get(t.getName()));
        });
        bottomHandlerUserList.forEach(t -> {
            t.setName(userMap.get(t.getName()));
        });
        return HandlerEfficiencyMetricDTO.builder()
                .topHandlerEfficiencyUser(topHandlerUserList)
                .bottomHandlerEfficiencyUser(bottomHandlerUserList)
                .build();
    }

    @Override
    public TaskHandlerTimeVO getInspectTaskTimeCompleteTimeNumMetric(Period period) {
        return this.baseMapper.selectInspectTaskCompleteTimeNumMetric(period);
    }

    private Map<Long, Long> inspectionUserStatistics(List<InspectTaskPO> totalList) {

        return totalList.stream().filter(t -> !t.getReceiveIds().contains(","))
                .collect(Collectors.groupingBy(t -> Long.valueOf(t.getReceiveIds()), Collectors.counting()));
    }

    private List<TaskDetailResult> extTaskPageList(List<InspectTaskPO> list) {
        var taskIdSet = new HashSet<Long>();
        var userIdSet = new HashSet<Long>();
        var userDeptSet = new HashSet<Long>();
        var userPostSet = new HashSet<Long>();
        var userRoleSet = new HashSet<Long>();
        list.forEach(vo -> {
            taskIdSet.add(vo.getId());
            if (StringUtils.isNotBlank(vo.getReceiveIds())) {
                userIdSet.addAll(Arrays.stream(vo.getReceiveIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
            if (StringUtils.isNotBlank(vo.getReceiveDeptIds())) {
                userDeptSet.addAll(Arrays.stream(vo.getReceiveDeptIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
            if (StringUtils.isNotBlank(vo.getReceivePostIds())) {
                userPostSet.addAll(Arrays.stream(vo.getReceivePostIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
            if (StringUtils.isNotBlank(vo.getReceiveRoleIds())) {
                userRoleSet.addAll(Arrays.stream(vo.getReceiveRoleIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }
        });
        // 计算任务统计
        var countMap = this.handleTaskRate(taskIdSet);

        Map<Long, UserDTO> userDTOMap = new HashMap<>();
        Map<Long, OrganizationDTO> organizationDTOMap = new HashMap<>();
        Map<Long, AppointmentDetailDTO> positionDTOMap = new HashMap<>();
        Map<Long, RoleDTO> roleDTOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            var userDTOList = userClient.listUserByIds(userIdSet);
            Optional.ofNullable(userDTOList).orElse(Collections.emptyList()).forEach(userDTO -> userDTOMap.put(userDTO.getId(), userDTO));
        }
        if (CollectionUtils.isNotEmpty(userDeptSet)) {
            var deptDTOList = organizationClient.getOrganizationsByIds(new ArrayList<>(userDeptSet));
            Optional.ofNullable(deptDTOList).orElse(Collections.emptyList())
                    .forEach(organizationDTO -> organizationDTOMap.put(organizationDTO.getId(), organizationDTO));
        }
        if (CollectionUtils.isNotEmpty(userPostSet)) {
            var positionDTOList = appointmentClient.getAppointmentByIds(userPostSet.stream()
                                                                                .map(String::valueOf)
                                                                                .collect(Collectors.joining(",")));
            Optional.ofNullable(positionDTOList).orElse(Collections.emptyList()).forEach(p -> positionDTOMap.put(p.getId(), p));
        }
        if (CollectionUtils.isNotEmpty(userRoleSet)) {
            var roleDTOList = roleClient.listRolesByIds(new ArrayList<>(userRoleSet));
            Optional.ofNullable(roleDTOList).orElse(Collections.emptyList()).forEach(r -> roleDTOMap.put(r.getId(), r));
        }
        return list.stream().map(po -> {
            var vo = ObjectUtils.copying(po, TaskDetailResult.class);
            if (StringUtils.isNotBlank(po.getReceiveIds())) {
                var stringBuilder = new StringBuilder();
                for (String s : po.getReceiveIds().split(",")) {
                    UserDTO userDTO = userDTOMap.get(Long.valueOf(s));
                    if (userDTO != null) {
                        stringBuilder.append(",").append(userDTO.getName());
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    vo.setReceiveNames(stringBuilder.substring(1, stringBuilder.length()));
                }
            }
            if (StringUtils.isNotBlank(po.getReceiveDeptIds())) {
                var stringBuffer = new StringBuffer();
                for (String s : po.getReceiveDeptIds().split(",")) {
                    var organizationDTO = organizationDTOMap.get(Long.valueOf(s));
                    if (organizationDTO != null) {
                        stringBuffer.append(",").append(organizationDTO.getName());
                    }
                }
                if (StringUtils.isNotBlank(stringBuffer)) {
                    vo.setReceivePostNames(stringBuffer.substring(1, stringBuffer.length()));
                }
            }
            if (StringUtils.isNotBlank(po.getReceivePostIds())) {
                var stringBuffer = new StringBuffer();
                for (String s : po.getReceivePostIds().split(",")) {
                    var positionDTO = positionDTOMap.get(Long.valueOf(s));
                    if (positionDTO != null) {
                        stringBuffer.append(",").append(positionDTO.getName());
                    }
                }
                if (StringUtils.isNotBlank(stringBuffer)) {
                    vo.setReceivePostNames(stringBuffer.substring(1, stringBuffer.length()));
                }
            }
            if (StringUtils.isNotBlank(po.getReceiveRoleIds())) {
                var stringBuffer = new StringBuffer();
                for (String s : po.getReceiveRoleIds().split(",")) {
                    var roleDTO = roleDTOMap.get(Long.valueOf(s));
                    if (roleDTO != null) {
                        stringBuffer.append(",").append(roleDTO.getName());
                    }
                }
                if (StringUtils.isNotBlank(stringBuffer)) {
                    vo.setReceiveRoleNames(stringBuffer.substring(1, stringBuffer.length()));
                }
            }
            var dto = countMap.get(po.getId());
            if (dto != null) {
                vo.setErrCount(dto.getErrCount());
                vo.setAbnormalItemCount(dto.getAbnormalItemCount());
                vo.setRepairCount(dto.getRepairCount());
                vo.setRecordCompletionRate(dto.getRecordCompletionRate());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private Map<Long, taskRecordCountDTO> handleTaskRate(Set<Long> taskIdSet) {
        if (CollectionUtils.isEmpty(taskIdSet)) {
            return Collections.emptyMap();
        }
        Map<Long, taskRecordCountDTO> result = new HashMap<>();
        List<InspectRecordPO> recordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                                    .in(InspectRecordPO::getTaskId, taskIdSet));
        Map<Long, Long> recordTaskMap = recordPOList.stream()
                .collect(Collectors.toMap(InspectRecordPO::getId, InspectRecordPO::getTaskId, (oldValue, newValue) -> oldValue));
        Map<Long, Long> abnormalItemCountMap = getAbnormalItemCountMap(recordTaskMap);

        recordPOList.stream().collect(Collectors.groupingBy(InspectRecordPO::getTaskId)).forEach((taskId, list) -> {
            int overCount = 0;
            int errCount = 0;
            int repairCount = 0;
            for (InspectRecordPO r : list) {
                boolean inspectBool = !InspectRecordStatusEnum.EXECUTING.equals(r.getStatus());
                if (inspectBool) {
                    overCount++;
                }
                if (InspectRecordResultEnum.ABNORMAL.equals(r.getResult())) {
                    errCount++;
                }
                if (InspectRepairStatusEnum.REPAIRED.name().equals(r.getRepairStatus())) {
                    repairCount++;
                }
            }
            double recordCompletionRate = (double) overCount / list.size();
            taskRecordCountDTO dto = new taskRecordCountDTO();
            dto.setOverCount(overCount);
            dto.setErrCount(errCount);
            dto.setAbnormalItemCount(Math.toIntExact(abnormalItemCountMap.getOrDefault(taskId, 0L)));
            dto.setRepairCount(repairCount);
            dto.setRecordCompletionRate(new BigDecimal(recordCompletionRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
            result.put(taskId, dto);
        });
        return result;
    }

    private Map<Long, Long> getAbnormalItemCountMap(Map<Long, Long> recordTaskMap) {
        if (recordTaskMap.isEmpty()) {
            return Collections.emptyMap();
        }
        List<InspectRecordItemPO> abnormalItems = inspectRecordItemMapper.listAbnormalRecordIdsByRecordIds(new ArrayList<>(recordTaskMap.keySet()));
        if (CollectionUtils.isEmpty(abnormalItems)) {
            return Collections.emptyMap();
        }
        return abnormalItems.stream()
                .map(InspectRecordItemPO::getRecordId)
                .filter(recordTaskMap::containsKey)
                .collect(Collectors.groupingBy(recordTaskMap::get, Collectors.counting()));
    }

    @Data
    static class taskRecordCountDTO {
        private Long taskId;
        private Integer overCount;
        private Integer errCount;
        private Integer abnormalItemCount;
        private Integer repairCount;
        private Double recordCompletionRate;
    }
}
