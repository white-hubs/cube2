package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.TodoTaskTransferService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskTargetRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl.TaskServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskReceiveUsersParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@SuppressWarnings("OptionalOfRedundantMethod")
public class InspectTaskCommandServiceImpl extends ServiceImpl<InspectTaskMapper, InspectTaskPO> implements InspectTaskCommandService {
    @Resource
    private UserClient userClient;
    @Resource
    private InspectPlanMapper inspectPlanMapper;
    @Resource
    private InspectTaskMapper inspectTaskMapper;
    @Resource
    private InspectRecordCommandService recordCommandService;
    @Resource
    private InspectTaskStatusChangeService taskStatusChangeService;
    @Resource
    private HandlerService handlerService;
    @Resource
    private TaskTargetRelationService taskTargetRelationService;
    @Resource
    private InspectTargetInfoQuery inspectTargetInfoQuery;
    @Resource
    private PlanRelationService planRelationService;
    @Resource
    private TaskServiceImpl taskService;
    @Resource
    private TodoTaskTransferService todoTaskTransferService;
    @Resource
    private ApplicationClient applicationClient;
    @Resource
    private InspectRecordItemCommandService inspectRecordItemCommandService;
    @Resource
    private MsgRecordClient msgRecordClient;
    @Resource
    private TodoTaskClient todoTaskClient;

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
            taskPO.setParentId(parentTaskPO.getId());
            taskPO.setStatus(InspectTaskStatusEnum.ACCEPT);
            taskPO.setReceiveIds(r.getReceiveIds());
            taskPO.setTargetNum(r.getTaskTargetDTOList().size());
            taskPO.setTargetIds(handlerService.handlerTaskTargetDTOListToTargetIds(r.getTaskTargetDTOList()));
            // 保存子任务
            this.saveTask(taskPO);
            // 生成巡检记录
            // TODO 异步方式，MQ或线程池
            this.createInspectionRecord(taskPO.getId());
            // 保存任务和对象关联关系
            this.saveTaskRelation(taskPO, r);
        });
        // 修改主任务状态
        taskStatusChangeService.changeForTaskAccept(parentTaskPO.getId());
    }

    /**
     * 根据计划创建任务
     *
     * @param planPO
     *
     * @return
     */
    @Override
    public String createTaskByPlan(InspectPlanPO planPO) {
        List<PlanRelationPO> planRelationPOList = planRelationService.lambdaQuery()
                .eq(PlanRelationPO::getPlanId, planPO.getId()).list();
        Assert.isNotAcceptable(planRelationPOList == null || planRelationPOList.isEmpty(), "数据已被清理！");
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
    public String createTaskByPlan(InspectPlanPO planPO, List<PlanRelationPO> targetDTOList) {
        // 重新设置 计划的执行次数
        planPO.setExecTimes(planPO.getExecTimes() == null ? 1 : planPO.getExecTimes() + 1);

        InspectTaskPO taskPO = ObjectUtils.copying(planPO, InspectTaskPO.class);
        // 当接收类型 2＝部门时，需要把部门转换为 部门下的人
        if ("2".equals(planPO.getReceiveType())) {
            taskPO.setReceiveIds(handlerService.HandlerDeptIdsAndPostIdsToUserIds(planPO.getReceiveDeptIds(), planPO.getReceivePostIds(), planPO.getReceiveRoleIds()));
            Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "巡检任务选择的岗位下无人员");
        }

        // 处理对象ids
        taskPO.setTargetIds(handlerService.handlerTargetDTOListToTargetIds(targetDTOList));

        taskPO.setCode(BizCodeUtils.generateNoRandom("IPK"));
        taskPO.setPlanId(planPO.getId());
        taskPO.setPlanName(planPO.getName());
        taskPO.setName(taskPO.getName() + "_V" + String.format("%04d", planPO.getExecTimes()));
        //因为是从计划中复制的 ，这里手动设置为空
        taskPO.setId(null);
        taskPO.setBeginTime(null);
        taskPO.setEndTime(null);
        taskPO.setIsSplit(planPO.isSplit());
        // 任务结束时间为计划开始时间加上任务的有效天(分钟)数
        taskPO.setValidEndTime(DateUtils.parse(
                DateUtils.toTimestamp(planPO.getBeginTime()) + Math.abs(planPO.getLimitTotalMinute() * 60 * 1000)));
        taskPO.setPublisherId(planPO.getCreatorId().toString());
        taskPO.setPublisherName(userClient.getUser(Long.valueOf(planPO.getCreatorId())).getName());
        taskPO.setStatus(planPO.isSplit() ? InspectTaskStatusEnum.SPLIT : InspectTaskStatusEnum.ACCEPT);
        taskPO.setCheckTaskId(0L);

        // 保存普通任务(非普通为稽查任务)
        if (planPO.isSplit()) {
            // 主任务不需要保存巡检记录和发送通知消息
            this.save(taskPO);
            List<TaskTargetRelationPO> taskTargetRelationPOList = this.getTaskRelation(taskPO, targetDTOList);
            // 批量保存 任务管理关系
            taskTargetRelationService.saveBatch(taskTargetRelationPOList);
        } else {
            this.saveTask(taskPO).getId();
            List<TaskTargetRelationPO> taskTargetRelationPOList = this.getTaskRelation(taskPO, targetDTOList);
            // 批量保存 任务管理关系
            taskTargetRelationService.saveBatch(taskTargetRelationPOList);
            // 生成巡检记录
            this.createInspectionRecord(taskPO.getId());

        }

        /**
         * 如果需要稽查，还有创建稽查任务
         */
        if (planPO.isCheck()) {
            // 创建稽查任务
            taskPO.setCheckTaskId(taskPO.getId());
            // TODO 检查一下这一步会不把上面的值清空
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
            }
            taskPO.setName(taskPO.getName() + "稽查");
            // 保存任务
            this.saveTask(taskPO);

            List<TaskTargetRelationPO> taskTargetRelationList = this.getTaskRelation(taskPO, targetDTOList);
            // 批量保存 任务管理关系
            taskTargetRelationService.saveBatch(taskTargetRelationList);
            // 生成巡检记录
            this.createInspectionRecord(taskPO.getId());
        }
        return taskPO.getId().toString();
    }

    @Override
    public void saveByPlanPO(long planId) {
        InspectPlanPO planPO = inspectPlanMapper.selectById(planId);
        this.saveByPlanPO(planPO);
    }

    @Override
    public void saveByPlanPO(InspectPlanPO planPO) {
        // 重新设置 计划的执行次数
        planPO.setExecTimes(planPO.getExecTimes() == null ? 1 : planPO.getExecTimes() + 1);

        InspectTaskPO taskPO = ObjectUtils.copying(planPO, InspectTaskPO.class);
        // 当接收类型 2＝部门时，需要把部门转换为 部门下的人
        if ("2".equals(planPO.getReceiveType())) {
            taskPO.setReceiveIds(this.getReceiveIdsByPlan(planPO));
            Assert.isNotAcceptable(StringUtils.isEmpty(taskPO.getReceiveIds()), "责任人检测为空");
        }

        taskPO.setPlanId(planPO.getId());
        taskPO.setPlanName(planPO.getName());
        taskPO.setName(taskPO.getName() + "_V" + String.format("%04d", planPO.getExecTimes()));
        taskPO.setId(null);//因为是从计划中复制的 ，这里手动设置为空
        taskPO.setBeginTime(null);
        taskPO.setEndTime(null);
        taskPO.setPublisherId(planPO.getCreatorId().toString());
        taskPO.setPublisherName(userClient.getUser(Long.valueOf(planPO.getCreatorId())).getName());
        this.save(taskPO);
        // 新增巡检记录
        recordCommandService.saveByTask(taskPO);

        // 更新巡检计划的任务数量
        this.updateTaskNum(planPO);

        // 设置任务状态
        taskStatusChangeService.changeForTaskGenerate(taskPO.getId());

    }

    @Override
    public void stopById(Long id) {

        // 对需要停止的任务做校验
        InspectTaskPO parentPo = this.getById(id);
        Assert.isNotFound(parentPo, "未找到指定的巡检任务");
        Assert.isNotAcceptable(parentPo.getStatus() != InspectTaskStatusEnum.EXECUTING, "只能对待巡检的任务处理");

        // 如果是主任务还要更新子任务
        if (parentPo.getIsSplit()) {
            // 直接修改主任务状态
            parentPo.setStatus(InspectTaskStatusEnum.STOP);
            this.saveOrUpdate(parentPo);
            // 获取子任务
            List<InspectTaskPO> sonList = this.lambdaQuery()
                    .eq(InspectTaskPO::getParentId, parentPo.getId())
                    .list();
            // 停止子任务
            Optional.ofNullable(sonList).orElse(Collections.emptyList()).parallelStream().forEach(po -> {
                // 修改任务状态已停止
                taskStatusChangeService.changeForTaskSTOP(po.getId());
            });

        } else {
            // 修改任务状态已停止
            taskStatusChangeService.changeForTaskSTOP(id);
        }

    }

    @Override
    public void expire(InspectTaskPO parentPo) {
        recordCommandService.expireByTaskId(parentPo.getId());
        //修改 任务本身的状态
        taskStatusChangeService.changeForTaskExpire(parentPo.getId());
    }

    /**
     * @Author wql
     * @Description 接收任务
     * @Date 10:38 2023/4/20
     **/
    @Override
    public void acceptTask(Long id) {
        Assert.isNotAcceptable(!taskService.checkTaskIsExpire(id), "任务已过期！");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        InspectTaskPO taskPO = inspectTaskMapper.selectOne(lambdaQuery().getWrapper().eq(InspectTaskPO::getId, id));
        Assert.isNotAcceptable(Objects.nonNull(taskPO.getBeginTime()), "任务已被接收");
        taskPO.setBeginTime(LocalDateTime.now());
        setDutyOfficer(userId, taskPO);
        inspectTaskMapper.updateById(taskPO);
        taskStatusChangeService.changeForTaskGenerate(id);
        if (taskPO.getParentId() != 0) {
            InspectTaskPO parentTask = inspectTaskMapper.selectOne(lambdaQuery().getWrapper()
                                                                           .eq(InspectTaskPO::getId, taskPO.getParentId()));
            if (parentTask.getBeginTime() == null) {
                parentTask.setBeginTime(taskPO.getBeginTime());
            }
            parentTask.setStatus(InspectTaskStatusEnum.EXECUTING);
            inspectTaskMapper.updateById(parentTask);
        }
    }

    /**
     * @Author wql
     * @Description 设置责任人
     * @Date 11:33 2023/4/20
     **/
    private void setDutyOfficer(Long userId, InspectTaskPO taskPO) {
        taskPO.setReceiveIds(String.valueOf(userId));
        UserDTO user = userClient.getUser(userId);
        taskPO.setReceiveDeptIds(String.valueOf(user.getOrganization().getId()));
        StringBuffer postIds = new StringBuffer("");
        Optional.of(user.getPositions()).orElse(Collections.emptyList()).forEach(post -> {
            postIds.append(post.getId()).append(",");
        });
        taskPO.setReceivePostIds(postIds.length() > 0 ? postIds.substring(0, postIds.length() - 1) : "");
    }

    /**
     * @Author wql
     * @Description 移交任务
     * @Date 10:55 2023/4/20
     **/
    @Override
    public void turnOverTask(Long taskId, Long userId) {
        Assert.isNotAcceptable(!taskService.checkTaskIsExpire(taskId), "任务已过期！");
        InspectTaskPO taskPO = inspectTaskMapper.selectOne(lambdaQuery().getWrapper().eq(InspectTaskPO::getId, taskId));
        taskPO.setReceiveIds(String.valueOf(userId));
        setDutyOfficer(userId, taskPO);
        inspectTaskMapper.updateById(taskPO);
        todoTaskTransferService.transferTodoTask(taskPO.getReceiveIds(), taskPO);
    }

    /**
     * @Author wql
     * @Description 添加协同人
     * @Date 10:58 2023/4/20
     **/
    @Override
    public void addSynergyUser(Long taskId, String userId) {
        Assert.isNotAcceptable(!taskService.checkTaskIsExpire(taskId), "任务已过期！");
        InspectTaskPO taskPO = inspectTaskMapper.selectOne(lambdaQuery().getWrapper().eq(InspectTaskPO::getId, taskId));
        taskPO.setAssistIds(userId);
        inspectTaskMapper.updateById(taskPO);
        List<Long> userIds = new ArrayList<>();
        Arrays.asList(taskPO.getAssistIds().split(",")).forEach(s -> {
            userIds.add(Long.parseLong(s));
        });
        if (StringUtils.isNotBlank(taskPO.getReceiveIds())) {
            userIds.add(Long.valueOf(taskPO.getReceiveIds()));
        }
        this.sendTodoTask(taskPO, userIds.stream().distinct().collect(Collectors.toList()));
    }

    /**
     * 批量删除巡检任务
     *
     * @param taskIdList 巡检任务id
     *
     * @return 是否成功
     */
    @Override
    public Boolean removeBatch(List<Long> taskIdList) {
        if (taskIdList == null || taskIdList.isEmpty()) {
            return true;
        }
        Set<String> taskIdSet = taskIdList.stream().map(String::valueOf).collect(Collectors.toSet());
        // 移除任务
        DatabaseContextHolder.softDelete(InspectTaskPO.class, new HashSet<>(taskIdList));
        /*移除记录*/
        // 获取记录id(这里不直接按任务id移除，而是先获取id再移除是因为记录id后面还要用这里就直接查询出来按id移除了)
        List<Long> recordIdList = recordCommandService.lambdaQuery()
                .select(InspectRecordPO::getId)
                .in(InspectRecordPO::getTaskId, taskIdList)
                .eq(InspectRecordPO::getDeleted, BigDecimal.ZERO.intValue())
                .list()
                .stream().map(InspectRecordPO::getId).collect(Collectors.toList());
        DatabaseContextHolder.softDelete(InspectRecordPO.class, new HashSet<>(recordIdList));

        // 移除记录项
        List<InspectRecordItemPO> removeRecordItemList = inspectRecordItemCommandService.lambdaQuery()
                .in(InspectRecordItemPO::getRecordId, recordIdList)
                .list();
        DatabaseContextHolder.softDelete(InspectRecordItemPO.class, removeRecordItemList);

        try {
            // 移除消息
            msgRecordClient.removeBatchByBusinessIds(taskIdSet);
            // 移除待办
            todoTaskClient.removeBatchByBusinessIds(taskIdSet);
        } catch (Exception e) {
            log.error("移除任务的待办和消息出错：{}", taskIdList, e);
        }

        return true;
    }

    private void sendTodoTask(InspectTaskPO taskPO, List<Long> userIds) {
        TodoTaskFlowCirculationMessage deleteMessage = new TodoTaskFlowCirculationMessage();
        deleteMessage.setOperationType(TodoTaskFlowOperationEnum.DELETE);
        deleteMessage.setBusinessKey(String.valueOf(taskPO.getId()));
        // 删除
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, deleteMessage, String.valueOf(taskPO.getId()));

        WorkTaskFlowCirculationMessage deleteWorkMessage = new WorkTaskFlowCirculationMessage();
        deleteWorkMessage.setOperationType(WorkTaskFlowOperationEnum.DELETE);
        deleteWorkMessage.setBusinessKey(String.valueOf(taskPO.getId()));
        // 删除
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, deleteWorkMessage, String.valueOf(taskPO.getId()));

        //重新发送消息
        if (ObjectUtils.isEmpty(userIds)) {
            return;
        }
        log.info("巡检任务新增代办任务：{}", taskPO.getStatus());
        //获取应用
        ApplicationDTO application = applicationClient.getApplicationByCode(MaintainApplicationEnum.INSPECTION.name());
        //创建消息体
        TodoTaskInitMessage message = new TodoTaskInitMessage();
        message.setApplicationCode(application.getCode());
        message.setApplicationName(application.getName());
        message.setBusinessKey(String.valueOf(taskPO.getId()));
        message.setBusinessType(MaintainApplicationEnum.INSPECTION.name());
        message.setBusinessTypeName(MaintainApplicationEnum.INSPECTION.getDescription());
        message.setBusinessStatus(taskPO.getStatus().name());
        message.setBusinessStatusName(taskPO.getStatus().getDescription());
        message.setTaskName(taskPO.getName());
        message.setTaskTime(taskPO.getCreateTime());
        message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
        message.setUserIds(userIds);

        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(taskPO.getId()));

        WorkTaskInitMessage workMessage = new WorkTaskInitMessage();
        workMessage.setApplicationCode(application.getCode());
        workMessage.setApplicationName(application.getName());
        workMessage.setBusinessKey(String.valueOf(taskPO.getId()));
        workMessage.setBusinessType(MaintainApplicationEnum.INSPECTION.name());
        workMessage.setBusinessTypeName(MaintainApplicationEnum.INSPECTION.getDescription());
        workMessage.setBusinessStatus(taskPO.getStatus().name());
        workMessage.setBusinessStatusName(taskPO.getStatus().getDescription());
        workMessage.setTaskName(taskPO.getName());
        workMessage.setTaskTime(taskPO.getCreateTime());
        workMessage.setOperationType(WorkTaskFlowOperationEnum.INSERT);
        workMessage.setUserIds(userIds);
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, message, String.valueOf(taskPO.getId()));
    }

    private String getReceiveIdsByPlan(InspectPlanPO planPO) {
        String userIdStr = "";
        //处理候选人
        if (StringUtils.isNotEmpty(planPO.getReceiveDeptIds()) && StringUtils.isNotEmpty(planPO.getReceivePostIds())) {
            List<Long> deptIds = Arrays.stream(planPO.getReceiveDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> postIds = Arrays.stream(planPO.getReceivePostIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> userIds = userClient.listUserByOrgAndPost(deptIds, postIds);
            userIdStr = userIds.stream().map(Object::toString).collect(Collectors.joining(","));
        }
        return userIdStr;
    }

    private void updateTaskNum(InspectPlanPO planPO) {
        if (planPO != null) {
            inspectPlanMapper.updateById(planPO);
        }
    }

    /**
     * 保存任务
     *
     * @param taskPO
     *
     * @return
     */
    private InspectTaskPO saveTask(InspectTaskPO taskPO) {
        // 保存任务数据
        this.save(taskPO);
        return taskPO;
    }

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
     * 保存任务关联关系和巡检记录
     *
     * @param taskPO
     * @param planRelationPOList
     */
    private void saveTaskRelation(InspectTaskPO taskPO, List<PlanRelationPO> planRelationPOList) {
        // 组装任务对象关联关系数据
        List<TaskTargetRelationPO> taskTargetRelationPOList = new ArrayList<>();
        planRelationPOList.forEach(po -> taskTargetRelationPOList.add(
                new TaskTargetRelationPO(null, taskPO.getPlanId(), taskPO.getId(), po.getTargetCategory(), po.getTargetType(), po.getTargetId(), po.getTargetCode(), po.getTargetName(), po.getLatAndLon(), po.getLocation(), po.getTargetParentId(), po.getTargetAddress(), po.getTargetAreaId(), po.getTargetAreaName())));
        // 批量保存
        taskTargetRelationService.saveBatch(taskTargetRelationPOList);

    }

    /**
     * 保存任务关联关系
     * 任务拆解时使用
     *
     * @param taskPO
     * @param dto
     */
    private void saveTaskRelation(InspectTaskPO taskPO, TaskReceiveUsersParam dto) {
        /**
         * 组装任务对象关联关系
         */
        List<TaskTargetRelationPO> taskTargetRelationPOList = new ArrayList<>();
        dto.getTaskTargetDTOList().forEach(t -> {
            TaskTargetRelationPO po = new TaskTargetRelationPO();
            po.setTaskId(taskPO.getId());
            po.setTenant(taskPO.getTenant());
            po.setPlanId(taskPO.getPlanId());
            po.setTargetId(t.getTargetId());
            po.setTargetCategory(t.getTargetCategory());
            po.setTargetName(t.getTargetName());
            po.setTargetType(t.getTargetType());
            taskTargetRelationPOList.add(po);
        });

        // 批量保存任务和对象关联关系
        taskTargetRelationService.saveBatch(taskTargetRelationPOList);
    }

    /**
     * 保存任务关联关系
     *
     * @param taskPO
     * @param planRelationPOList
     */
    private List<TaskTargetRelationPO> getTaskRelation(InspectTaskPO taskPO, List<PlanRelationPO> planRelationPOList) {
        // 组装任务对象关联关系数据
        List<TaskTargetRelationPO> taskTargetRelationPOList = new ArrayList<>();
        planRelationPOList.forEach(po -> taskTargetRelationPOList.add(
                new TaskTargetRelationPO(null, taskPO.getPlanId(), taskPO.getId(), po.getTargetCategory(), po.getTargetType(), po.getTargetId(), po.getTargetCode(), po.getTargetName(), po.getLatAndLon(), po.getLocation(), po.getTargetParentId(), po.getTargetAddress(), po.getTargetAreaId(), po.getTargetAreaName())));

        return taskTargetRelationPOList;
    }
}
