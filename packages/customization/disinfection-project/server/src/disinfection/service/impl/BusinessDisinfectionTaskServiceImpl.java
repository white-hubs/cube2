package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.PlanTaskTimeLimitConfigDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionArea;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionKeyPlace;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskCheckIn;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskExecuteRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionDeptHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionNotifyHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionPostHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionRoleHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCheckInParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCompletionRecordsParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.TaskPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.TaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.TaskUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionAreaService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionPlanService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskCheckInService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskExecuteRecordService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DisinfectionTaskStatusVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCompletionRecordVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.TaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionTaskServiceImpl implements BusinessDisinfectionTaskService {
    private final DisinfectionTaskService taskService;
    private final DisinfectionGroupService groupService;
    private final DisinfectionProjectService disinfectionProjectService;
    private final DisinfectionAreaService disinfectionAreaService;
    private final DisinfectionDeptHelper deptHelper;
    private final DisinfectionRoleHelper roleHelper;
    private final DisinfectionPostHelper postHelper;
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionNotifyHelper notifyHelper;
    private final DisinfectionDataTypeService disinfectionDataTypeService;
    private final DisinfectionPlanService disinfectionPlanService;
    private final DisinfectionTaskCheckInService disinfectionTaskCheckInService;
    private final DisinfectionTaskExecuteRecordService disinfectionTaskExecuteRecordService;
    private final DisinfectionKeyPlaceService disinfectionKeyPlaceService;
    private final DisinfectionFileHelper disinfectionFileHelper;
    private final DisinfectionGroupService disinfectionGroupService;
    private final DisinfectionGroupUserService disinfectionGroupUserService;
    private final UserClient userClient;
    private final FileRefClient fileRefClient;
    private final FileOperateClient fileOperateClient;

    @Override
    public List<TaskInfoVO> listAll(TaskQueryParam param) {

        LambdaQueryChainWrapper<DisinfectionTask> queryChainWrapper = buildQueryWrapper(param);
        List<DisinfectionTask> list = queryChainWrapper.list();
        List<TaskInfoVO> taskInfoVOList = ObjectUtils.copying(list, TaskInfoVO.class);
        buildTaskDetailExtendInfo(taskInfoVOList);
        return taskInfoVOList;
    }

    @Override
    @Transactional
    public boolean insertTask(TaskUpsertParam param) {
        DisinfectionTask disinfectionTask = ObjectUtils.copying(param, DisinfectionTask.class);
        //检查任务接收人
        checkTaskReceiver(disinfectionTask);
        disinfectionTask.setId(IdWorker.getId());
        //设置任务状态
        disinfectionTask.setStatus(DisinfectionTaskStatusEnum.WAIT_RECEIVE);
        //设置任务类型名称
        disinfectionTask.setTaskTypeName(disinfectionDataTypeService.getTypeName(disinfectionTask.getTaskTypeId()));
        //设置任务时限配置
        PlanTaskTimeLimitConfigDTO taskTimeLimitConfig = param.getTaskTimeLimitConfig();
        if (Objects.nonNull(taskTimeLimitConfig)) {
            disinfectionTask.setTaskTimeLimitConfig(JsonUtils.serialize(taskTimeLimitConfig));
            buildTaskDeadLine(taskTimeLimitConfig, disinfectionTask);
        }
        if (StringUtils.isNotBlank(param.getAppendix())) {
            //文件状态持久化
            List<Long> fileIds = Arrays.stream(param.getAppendix().split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            disinfectionFileHelper.induceFiles(fileIds, DinsinfectionFileInduceSceneCodeEnum.TASK);
        }
        //需要发送通知给对应人员
        notifyHelper.sendDisinfectionTaskCreateMessage(disinfectionTask);
        return taskService.save(disinfectionTask);
    }

    @Override
    @Transactional
    public boolean updateTask(long id, TaskUpsertParam param) {
        DisinfectionTask task = taskService.getById(id);
        Assert.parameterIsNull(task, "该数据不存在");
        checkTaskReceiver(task);
        ObjectUtils.copying(param, task);
        task.setTaskTypeName(disinfectionDataTypeService.getTypeName(task.getTaskTypeId()));
        if (Objects.nonNull(param.getTaskTimeLimitConfig())) {
            //设置任务时限配置
            task.setTaskTimeLimitConfig(JsonUtils.serialize(param.getTaskTimeLimitConfig()));
            //设置任务截至时间
            buildTaskDeadLine(param.getTaskTimeLimitConfig(), task);
        }

        return taskService.updateById(task);
    }

    @Override
    public boolean deleteTask(long id) {
        return taskService.removeById(id);
    }

    @Override
    public Pagination<TaskPageVO> pageTasks(TaskPageParam pageParam) {
        LambdaQueryChainWrapper<DisinfectionTask> queryChainWrapper = buildTaskPageQueryWrapper(pageParam);
        Page<DisinfectionTask> taskPage = queryChainWrapper
                .orderByDesc(DisinfectionTask::getCreateTime)
                .page(PagingUtils.convert(pageParam));
        Pagination<TaskPageVO> voPagination = PagingUtils.convert(taskPage, TaskPageVO.class);
        //设置扩展信息
        buildExtendInfo(voPagination.getRecords());
        return voPagination;
    }

    @Override
    public List<ProjectStatisticsBaseVO> countTasksByStatus() {
        return taskService.countTasksByStatus(new TaskQueryParam());
    }

    @Override
    public DisinfectionTaskStatusVO overviewTasksByStatus(TaskQueryParam taskQueryParam) {
        DisinfectionTaskStatusVO disinfectionTaskStatusVO = new DisinfectionTaskStatusVO();
        taskQueryParam.setExecutorId(SecurityContextHolder.getPrincipal().getId());

        List<ProjectStatisticsBaseVO> projectStatisticsBaseVOS = taskService.countTasksByStatus(taskQueryParam);
        if (CollectionUtils.isEmpty(projectStatisticsBaseVOS)) {
            return disinfectionTaskStatusVO;
        }
        Map<String, Integer> countMap = projectStatisticsBaseVOS.stream()
                .collect(Collectors.toMap(ProjectStatisticsBaseVO::getKeyName, ProjectStatisticsBaseVO::getNumber));
        disinfectionTaskStatusVO.setTotal(countMap.values().stream().reduce(Integer::sum).orElse(0));
        disinfectionTaskStatusVO.setWaitReceive(countMap.getOrDefault(DisinfectionTaskStatusEnum.WAIT_RECEIVE.name(), 0));
        disinfectionTaskStatusVO.setWaitExecute(countMap.getOrDefault(DisinfectionTaskStatusEnum.WAIT_EXECUTE.name(), 0));
        disinfectionTaskStatusVO.setExecuting(countMap.getOrDefault(DisinfectionTaskStatusEnum.EXECUTING.name(), 0));
        disinfectionTaskStatusVO.setTerminated(countMap.getOrDefault(DisinfectionTaskStatusEnum.TERMINATED.name(), 0));
        disinfectionTaskStatusVO.setFinished(countMap.getOrDefault(DisinfectionTaskStatusEnum.FINISHED.name(), 0));
        disinfectionTaskStatusVO.setExpired(countMap.getOrDefault(DisinfectionTaskStatusEnum.EXPIRED.name(), 0));
        return disinfectionTaskStatusVO;
    }

    @Transactional
    @Override
    public boolean stopTask(long taskId) {
        DisinfectionTask task = taskService.getById(taskId);
        Assert.isUnknownError(DisinfectionTaskStatusEnum.FINISHED.equals(task.getStatus()) ||
                              DisinfectionTaskStatusEnum.TERMINATED.equals(task.getStatus()), "任务状态无法终止");
        return taskService.updateTaskStatus(taskId, DisinfectionTaskStatusEnum.TERMINATED);
    }

    @Transactional
    @Override
    public boolean checkIn(long taskId, TaskCheckInParam param) {
        DisinfectionTask disinfectionTask = taskService.getById(taskId);
        Assert.parameterIsNull(disinfectionTask, "任务不存在，打卡失败！");
        //执行人员不能为空
        Assert.parameterIsNull(disinfectionTask.getExecutorId(), "任务执行人员为空，打卡失败！");
        //校验打卡人员是否为执行人员
        validateExecutor(disinfectionTask);
        //任务状态需要是待执行状态或者过期状态才能打卡
        Assert.isNotAcceptable(!DisinfectionTaskStatusEnum.WAIT_EXECUTE.equals(disinfectionTask.getStatus()) &&
                               !DisinfectionTaskStatusEnum.EXPIRED.equals(disinfectionTask.getStatus()), "任务状态不是可打卡状态，打卡失败！");
        var userDTO = userClient.getUser(disinfectionTask.getExecutorId());

        //变更任务为执行中
        taskService.updateTaskStatus(taskId, DisinfectionTaskStatusEnum.EXECUTING);
        //保存打卡信息
        DisinfectionTaskCheckIn checkIn = ObjectUtils.copying(param, DisinfectionTaskCheckIn.class);
        var checkInId = IdWorker.getId();
        checkIn.setId(checkInId);
        checkIn.setTaskId(taskId);
        checkIn.setTaskName(disinfectionTask.getTaskName());
        checkIn.setProjectId(disinfectionTask.getProjectId());
        checkIn.setCheckUserName(userDTO.getName());
        disinfectionTaskCheckInService.save(checkIn);
        // 文件持久化
        if (StringUtils.isNotBlank(param.getFiles())) {
            var fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdStr(param.getFiles());
            fileInduceParam.setBusinessKey(checkInId);
            fileInduceParam.setSceneCode(DinsinfectionFileInduceSceneCodeEnum.TASK.getCode());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean acceptTask(long taskId) {

        DisinfectionTask disinfectionTask = taskService.getById(taskId);
        Assert.parameterIsNull(disinfectionTask, "任务不存在，接收失败！");
        //任务执行人为空才可接收
        Assert.isNotAcceptable(Objects.nonNull(disinfectionTask.getExecutorId()), "任务已接收！");
        //任务终止状态不可以接收
        Assert.isNotAcceptable(DisinfectionTaskStatusEnum.TERMINATED.equals(disinfectionTask.getStatus()), "任务已终止，不可接收！");
        //任务需要是待接收状态或者过期状态
        Assert.isNotAcceptable(!DisinfectionTaskStatusEnum.WAIT_RECEIVE.equals(disinfectionTask.getStatus()) &&
                               !DisinfectionTaskStatusEnum.EXPIRED.equals(disinfectionTask.getStatus()), "任务状态不是可接收状态，接收失败！");
        //校验接收人是否在任务通知范围内
        validateAcceptor(disinfectionTask);
        //更新执行人
        disinfectionTask.setExecutorId(SecurityContextHolder.getPrincipal().getId());

        //更新任务状态未待执行
        disinfectionTask.setStatus(DisinfectionTaskStatusEnum.WAIT_EXECUTE);
        return taskService.updateById(disinfectionTask);

    }

    @Transactional
    @Override
    public boolean uploadCompletionRecords(long taskId, TaskCompletionRecordsParam param) {
        DisinfectionTask task = taskService.getById(taskId);
        Assert.parameterIsNull(task, "任务不存在");
        //校验上传人是否为执行人
        validateExecutor(task);
        //校验任务是否为可处理状态（执行中，过期）
        Assert.isNotAcceptable(!DisinfectionTaskStatusEnum.EXECUTING.equals(task.getStatus()) &&
                               !DisinfectionTaskStatusEnum.EXPIRED.equals(task.getStatus()), "任务状态不是执行中，不可上传！");
        //保存任务进度记录
        boolean saveCompletionRecord = saveCompletionRecord(taskId, param, task);
        //校验任务进度记录，修改任务状态
        if (isCompletion(param)) {
            task.setStatus(DisinfectionTaskStatusEnum.FINISHED);
        }
        //修改任务进度
        task.setProcess(param.getProcess());
        boolean updateTask = taskService.updateById(task);
        return saveCompletionRecord && updateTask;
    }

    @Override
    public List<TaskCompletionRecordVO> getTaskCompletionRecords(long taskId) {
        List<DisinfectionTaskExecuteRecord> records = disinfectionTaskExecuteRecordService.listByTaskId(taskId);
        //获取需要查询的人员信息
        Map<Long, String> userNameMap = disinfectionUserHelper.getUserNameMap(ListUtils.convertItem(records, DisinfectionTaskExecuteRecord::getExecutorId));
        //采集需要的文件信息
        Set<Long> fileIdSet = getExecuteRecordsFiles(records);
        Map<Long, FileVO> fileMap = disinfectionFileHelper.getFilesMap(new ArrayList<>(fileIdSet));
        Set<Long> placeIds = getExecteRecordPlaceIds(records);
        //查询场所信息
        List<DisinfectionKeyPlace> keyPlaces = disinfectionKeyPlaceService.listKeyPlacesByIds(placeIds);
        Map<Long, DisinfectionKeyPlace> keyPlaceMap = MapUtils.listToMap(keyPlaces, DisinfectionKeyPlace::getId, keyPlace -> keyPlace);
        return records.stream().map(record -> {
            TaskCompletionRecordVO recordVO = ObjectUtils.copying(record, TaskCompletionRecordVO.class);
            //设置图片文件信息
            buildExecuteRecordFiles(record, fileMap, recordVO);
            //设置执行人名称
            recordVO.setExecutorName(userNameMap.getOrDefault(record.getExecutorId(), ""));
            //设置场所信息
            buildKeyPlaceInfos(record, keyPlaceMap, fileMap, recordVO);
            return recordVO;
        }).collect(Collectors.toList());

    }

    @Override
    public TaskInfoVO detail(long taskId) {
        DisinfectionTask task = taskService.getById(taskId);
        TaskInfoVO taskInfoVO = ObjectUtils.copying(task, TaskInfoVO.class);
        //设置项目名称
        taskInfoVO.setProjectName(disinfectionProjectService.getProjectName(task.getProjectId()));
        //设置类型名称
        taskInfoVO.setTaskTypeName(disinfectionDataTypeService.getTypeName(task.getTaskTypeId()));
        //设置区域名称
        taskInfoVO.setTaskAreaName(disinfectionAreaService.getAreaName(task.getTaskAreaId()));
        //设置项目负责人名称
        taskInfoVO.setProjectLeaderName(disinfectionUserHelper.getUserName(task.getProjectLeaderId()));
        //设置执行人名称
        taskInfoVO.setExecutorName(disinfectionUserHelper.getUserName(task.getExecutorId()));
        //设置任务时限配置
        if (StringUtils.isNotBlank(task.getTaskTimeLimitConfig())) {
            taskInfoVO.setTaskTimeLimitConfigDTO(JsonUtils.deserialize(task.getTaskTimeLimitConfig(), PlanTaskTimeLimitConfigDTO.class));
        }
        //设置通知名称
        taskInfoVO.setNotificationGroupName(disinfectionGroupService.getGroupName(task.getNotificationGroupId()));
        taskInfoVO.setNotificationDeptName(deptHelper.getDeptName(task.getNotificationDeptId()));
        taskInfoVO.setNotificationPostName(postHelper.getPostName(task.getNotificationPostId()));
        taskInfoVO.setNotificationRoleName(roleHelper.getRoleName(task.getNotificationRoleId()));
        //设置文件信息
        if (StringUtils.isNotBlank(task.getAppendix())) {
            List<Long> fileIds = Arrays.stream(task.getAppendix().split(","))
                    .filter(Objects::nonNull)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            taskInfoVO.setFiles(disinfectionFileHelper.getFiles(fileIds));

        }
        //设置发布人
        taskInfoVO.setCreateName(disinfectionUserHelper.getUserNameByAccount(task.getCreateBy()));
        //设置项目消杀范围
        DisinfectionProject project = disinfectionProjectService.getById(task.getProjectId());

        taskInfoVO.setDisinfectionScope(Objects.nonNull(project) ? project.getDisinfectionScope() : "");
        return taskInfoVO;
    }

    @Transactional
    @Override
    public boolean transferTask(long taskId, long executorId) {
        DisinfectionTask task = taskService.getById(taskId);
        Assert.parameterIsNull(task, "任务不存在，转派失败！");
        //任务执行中，接收状态才能移交
        Assert.isNotAcceptable(!DisinfectionTaskStatusEnum.EXECUTING.equals(task.getStatus()) &&
                               !DisinfectionTaskStatusEnum.WAIT_EXECUTE.equals(task.getStatus()) &&
                               !DisinfectionTaskStatusEnum.EXPIRED.equals(task.getStatus()), "任务需要接收或者执行中才能移交，请勿转派！");

        //查询移交人是否为当前执行人员
        Long curUserId = SecurityContextHolder.getPrincipal().getId();
        Assert.isNotAcceptable(!curUserId.equals(task.getExecutorId()), "当前用户不是任务执行人员，请勿转派！");

        task.setExecutorId(executorId);
        return taskService.updateById(task);
    }

    private void checkTaskReceiver(DisinfectionTask disinfectionTask) {
        Assert.parameterIsNull(disinfectionTask, "任务通知人为空");
        List<Long> receiveDeptIds = new ArrayList<>();
        List<Long> receivePostIds = new ArrayList<>();
        List<Long> receiveRoleIds = new ArrayList<>();
        List<Long> receiveGroupIds = new ArrayList<>();
        DisinfectionNotificationTypeEnum notificationType = disinfectionTask.getNotificationType();
        Assert.parameterIsNull(notificationType, "通知类型为空");
        switch (notificationType) {
            case DEPT:
                Long notificationDeptId = disinfectionTask.getNotificationDeptId();
                Assert.parameterIsNull(notificationDeptId, "任务通知部门为空");
                receiveDeptIds.add(notificationDeptId);
                break;

            case POST:
                Long notificationPostId = disinfectionTask.getNotificationPostId();
                Assert.parameterIsNull(notificationPostId, "任务通知岗位为空");
                receivePostIds.add(notificationPostId);
                break;

            case GROUP:
                Long notificationGroupId = disinfectionTask.getNotificationGroupId();
                Assert.parameterIsNull(notificationGroupId, "任务通知小组为空");
                receiveGroupIds.add(notificationGroupId);
                break;

            case ROLE:
                Long notificationRoleId = disinfectionTask.getNotificationRoleId();
                Assert.parameterIsNull(notificationRoleId, "任务通知角色为空");
                receiveRoleIds.add(notificationRoleId);
                break;
            case GROUP_ROLE:
                Long notificationGroupRoleId = disinfectionTask.getNotificationRoleId();
                Assert.parameterIsNull(notificationGroupRoleId, "任务通知角色为空");
                Long notificationGroupRoleGroupId = disinfectionTask.getNotificationGroupId();
                receiveRoleIds.add(notificationGroupRoleId);
                receiveGroupIds.add(notificationGroupRoleGroupId);
                break;
            default:
                throw new ServiceException("未知的通知类型！");

        }
        Map<Long, Boolean> roleExistUserMap = roleHelper.isExistUsersByRoleIds(receiveRoleIds);
        Map<Long, Boolean> groupExistUserMap = disinfectionGroupUserService.isExistUsersByGroupIds(receiveGroupIds);
        Map<Long, Boolean> deptExistUserMap = deptHelper.isExistUsersByDeptIds(receiveDeptIds);
        Map<Long, Boolean> postExistUserMap = postHelper.isExistUsersByPostIds(receivePostIds);
        switch (notificationType) {
            case DEPT:
                Long notificationDeptId = disinfectionTask.getNotificationDeptId();
                Assert.isNotAcceptable(!deptExistUserMap.getOrDefault(notificationDeptId, false), "计划任务通知部门不存在用户！");
                break;
            case POST:
                Long notificationPostId = disinfectionTask.getNotificationPostId();
                Assert.isNotAcceptable(!postExistUserMap.getOrDefault(notificationPostId, false), "计划任务通知岗位不存在用户！");
                break;
            case GROUP:
                Long notificationGroupId = disinfectionTask.getNotificationGroupId();
                Assert.isNotAcceptable(!groupExistUserMap.getOrDefault(notificationGroupId, false), "计划任务通知小组不存在用户！");
                break;
            case ROLE:
                Long notificationRoleId = disinfectionTask.getNotificationRoleId();
                Assert.isNotAcceptable(!roleExistUserMap.getOrDefault(notificationRoleId, false), "计划任务通知角色不存在用户！");
                break;
            case GROUP_ROLE:
                Long notificationGroupRoleId = disinfectionTask.getNotificationRoleId();
                Long notificationGroupRoleGroupId = disinfectionTask.getNotificationGroupId();
                Assert.isNotAcceptable(!roleExistUserMap.getOrDefault(notificationGroupRoleId, false), "计划任务通知角色不存在用户！");
                Assert.isNotAcceptable(!groupExistUserMap.getOrDefault(notificationGroupRoleGroupId, false), "计划任务通知小组不存在用户！");
                break;
            default:
                throw new ServiceException("未知的通知类型！");

        }
    }

    private void validateExecutor(DisinfectionTask task) {
        Assert.isNotAcceptable(!SecurityContextHolder.getPrincipal().getId().equals(task.getExecutorId()), "当前用户不是任务执行者");
    }

    private LambdaQueryChainWrapper<DisinfectionTask> buildTaskPageQueryWrapper(TaskPageParam pageParam) {
        LambdaQueryChainWrapper<DisinfectionTask> lambdaQuery = taskService.lambdaQuery();
        if (Objects.nonNull(pageParam.getDeadLinePeriod())) {
            lambdaQuery.between(DisinfectionTask::getDeadline, pageParam.getDeadLinePeriod()
                    .getBegin(), pageParam.getDeadLinePeriod().getEnd());
        }
        LambdaQueryChainWrapper<DisinfectionTask> queryChainWrapper = lambdaQuery
                .eq(Objects.nonNull(pageParam.getProjectId()), DisinfectionTask::getProjectId, pageParam.getProjectId())
                .eq(Objects.nonNull(pageParam.getStatus()), DisinfectionTask::getStatus, pageParam.getStatus())
                .eq(Objects.nonNull(pageParam.getTaskTypeId()), DisinfectionTask::getTaskTypeId, pageParam.getTaskTypeId())
                .and(Objects.nonNull(pageParam.getKeyword()), q -> {
                    q.like(DisinfectionTask::getTaskName, pageParam.getKeyword());
                    //查询满足条件的人员id
                    List<Long> userIds = disinfectionUserHelper.getUserIdsByName(pageParam.getKeyword());
                    if (ListUtils.nonEmpty(userIds)) {
                        q.or().in(DisinfectionTask::getProjectLeaderId, userIds);
                    }
                });
        queryChainWrapper.and(Objects.nonNull(pageParam.getExecutorId()), this::setQueryableScope);

        //构建查询参数
        return queryChainWrapper;
    }

    private void validateAcceptor(DisinfectionTask task) {
        Assert.parameterIsNull(task, "任务不存在");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        DisinfectionNotificationTypeEnum notificationType = task.getNotificationType();
        if (DisinfectionNotificationTypeEnum.GROUP.equals(notificationType)) {
            //判断用户是否在当前小组
            Long notificationGroupId = task.getNotificationGroupId();
            Assert.parameterIsNull(notificationGroupId, "任务通知组为空，无法校验用户所属组");
            Assert.isNotAcceptable(!disinfectionGroupUserService.isInGroup(userId, notificationGroupId), "用户不属于当前任务通知组，无法接收任务！");
        } else if (DisinfectionNotificationTypeEnum.GROUP_ROLE.equals(notificationType)) {
            //判断用户是否在当前小组
            Long notificationGroupId = task.getNotificationGroupId();
            Assert.parameterIsNull(notificationGroupId, "任务通知组为空，无法校验用户所属组");
            Assert.isNotAcceptable(!disinfectionGroupUserService.isInGroup(userId, notificationGroupId), "用户不属于当前任务通知组，无法接收任务！");
            List<UserDTO.Role> roleList = disinfectionUserHelper.getUsersRoles(Collections.singletonList(userId));
            Assert.isParameterError(ListUtils.isEmpty(roleList), "用户角色为空，无法校验用户角色！");
            //判断用户当前角色是否包含配置的通知角色
            Long notificationRoleId = task.getNotificationRoleId();
            Assert.parameterIsNull(notificationRoleId, "任务通知角色为空，无法校验用户角色！");
            boolean isContain = false;
            for (UserDTO.Role role : roleList) {
                if (role.getId().equals(notificationRoleId)) {
                    isContain = true;
                    break;
                }
            }
            Assert.isNotAcceptable(!isContain, "用户角色不包含任务通知角色，无法接收任务！");
        } else if (DisinfectionNotificationTypeEnum.DEPT.equals(notificationType)) {
            Long notificationDeptId = task.getNotificationDeptId();
            Assert.parameterIsNull(notificationDeptId, "任务通知部门为空，无法校验用户部门！");
            List<UserDTO.Organization> usersDept = disinfectionUserHelper.getUsersDept(Collections.singletonList(userId));
            Assert.isParameterError(ListUtils.isEmpty(usersDept), "用户部门为空，无法校验用户部门！");
            boolean isContain = false;
            for (UserDTO.Organization dept : usersDept) {
                if (dept.getId().equals(notificationDeptId)) {
                    isContain = true;
                    break;
                }
            }
            Assert.isNotAcceptable(!isContain, "用户部门不包含任务通知部门，无法接收任务！");

        } else if (DisinfectionNotificationTypeEnum.POST.equals(notificationType)) {
            Long notificationPostId = task.getNotificationPostId();
            Assert.parameterIsNull(notificationPostId, "任务通知岗位为空，无法校验用户岗位！");
            List<UserDTO.Appointment> userAppointments = disinfectionUserHelper.getUsersPosts(Collections.singletonList(userId));
            Assert.isParameterError(ListUtils.isEmpty(userAppointments), "用户岗位为空，无法校验用户岗位！");
            boolean isContain = false;
            for (UserDTO.Appointment appointment : userAppointments) {
                if (appointment.getId().equals(notificationPostId)) {
                    isContain = true;
                    break;
                }
            }
            Assert.isNotAcceptable(!isContain, "用户岗位不包含任务通知岗位，无法接收任务！");

        } else {
            throw new ServiceException("任务通知类型错误");
        }

    }

    private boolean isCompletion(TaskCompletionRecordsParam param) {
        return param.getProcess().compareTo(BigDecimal.valueOf(100.0)) >= 0;
    }

    private boolean saveCompletionRecord(long taskId, TaskCompletionRecordsParam param, DisinfectionTask task) {
        DisinfectionTaskExecuteRecord executeRecord = ObjectUtils.copying(param, DisinfectionTaskExecuteRecord.class);
        executeRecord.setProjectId(task.getProjectId());
        executeRecord.setExecutorId(SecurityContextHolder.getPrincipal().getId());
        executeRecord.setTaskId(taskId);
        List<TaskCompletionRecordsParam.PlaceRecordParam> placeRecords = ObjectUtils.getOrElse(ListUtils.nonEmpty(param.getPlaceRecords()), param.getPlaceRecords(), Collections.emptyList());
        executeRecord.setKeyPlaceRecord(JsonUtils.serialize(placeRecords));
        return disinfectionTaskExecuteRecordService.save(executeRecord) && induceCompletionRecordsFiles(param);
    }

    private boolean induceCompletionRecordsFiles(TaskCompletionRecordsParam param) {
        List<Long> induceFileIds = new ArrayList<>();
        String appendix = param.getAppendix();
        if (StringUtils.isNotBlank(appendix)) {
            List<Long> fileIds = Arrays.stream(appendix.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            induceFileIds.addAll(fileIds);
        }
        List<TaskCompletionRecordsParam.PlaceRecordParam> placeRecords = param.getPlaceRecords();
        if (ListUtils.nonEmpty(placeRecords)) {
            List<Long> fileIds = placeRecords.stream()
                    .map(TaskCompletionRecordsParam.PlaceRecordParam::getAppendix)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(placeAppendix -> Arrays.stream(placeAppendix.split(",")))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            induceFileIds.addAll(fileIds);
        }
        return disinfectionFileHelper.induceFiles(induceFileIds, DinsinfectionFileInduceSceneCodeEnum.UPLOAD_COMPLETION_RECORD);

    }

    private void buildExtendInfo(List<TaskPageVO> records) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        //查询需要的项目信息
        List<Long> projectIds = ListUtils.convertItem(records, TaskPageVO::getProjectId);
        List<DisinfectionProject> projectList = disinfectionProjectService.listProjectsByIds(projectIds);
        Map<Long, String> projectNameMap = MapUtils.listToMap(projectList, DisinfectionProject::getId, DisinfectionProject::getProjectName);
        //查询需要的小组信息
        List<Long> executorGroupIds = ListUtils.convertItem(records, TaskPageVO::getNotificationGroupId);
        List<DisinfectionGroup> groupList = groupService.listGroupByIds(executorGroupIds);
        Map<Long, String> groupNameMap = MapUtils.listToMap(groupList, DisinfectionGroup::getId, DisinfectionGroup::getGroupName);
        //查询需要的区域信息
        List<Long> areaIds = ListUtils.convertItem(records, TaskPageVO::getTaskAreaId);
        List<DisinfectionArea> areaList = disinfectionAreaService.listByAreaIds(areaIds);
        Map<Long, String> areaNameMap = MapUtils.listToMap(areaList, DisinfectionArea::getId, DisinfectionArea::getAreaName);
        //查询需要的部门信息
        Map<Long, String> deptNameMap = deptHelper.getDeptNameMap(ListUtils.convertItem(records, TaskPageVO::getNotificationDeptId));
        //查询需要的岗位信息
        Map<Long, String> postNameMap = postHelper.getPostNameMap(ListUtils.convertItem(records, TaskPageVO::getNotificationPostId));
        //查询需要的角色信息
        Map<Long, String> roleNameMap = roleHelper.getRoleNameMap(ListUtils.convertItem(records, TaskPageVO::getNotificationRoleId));
        //查询需要的计划名称
        Map<Long, String> planNameMap = disinfectionPlanService.getPlanNameMap(ListUtils.convertItem(records, TaskPageVO::getPlanId));
        //查询需要的文件信息
        List<Long> fileIds = ListUtils.convertItem(records, TaskPageVO::getAppendix)
                .stream()
                .filter(StringUtils::isNotBlank)
                .flatMap(fileIdStr -> Arrays.stream(fileIdStr.split(",")))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(fileIds);
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        //查询需要的人员信息
        List<Long> userIds = ListUtils.convertItem(records, TaskPageVO::getProjectLeaderId);
        List<Long> executorIds = ListUtils.convertItem(records, TaskPageVO::getExecutorId).stream().filter(Objects::nonNull)
                .collect(Collectors.toList());
        userIds.addAll(executorIds);
        Map<Long, String> userNameMap = disinfectionUserHelper.getUserNameMap(userIds);
        records.forEach(record -> {
            //设置项目名称
            record.setProjectName(projectNameMap.getOrDefault(record.getProjectId(), ""));
            //设置小组名称
            if (Objects.nonNull(record.getNotificationGroupId())) {
                record.setNotificationGroupName(groupNameMap.getOrDefault(record.getNotificationGroupId(), ""));
            }
            //设置任务时限配置
            if (StringUtils.isNotBlank(record.getTaskTimeLimitConfig())) {
                record.setTaskTimeLimitConfigDTO(JsonUtils.deserialize(record.getTaskTimeLimitConfig(), PlanTaskTimeLimitConfigDTO.class));
            }
            //设置部门名称
            if (Objects.nonNull(record.getNotificationDeptId())) {
                record.setNotificationDeptName(deptNameMap.getOrDefault(record.getNotificationDeptId(), ""));
            }
            //设置岗位名称
            if (Objects.nonNull(record.getNotificationPostId())) {
                record.setNotificationPostName(postNameMap.getOrDefault(record.getNotificationPostId(), ""));
            }
            //设置角色名称
            if (Objects.nonNull(record.getNotificationRoleId())) {
                record.setNotificationRoleName(roleNameMap.getOrDefault(record.getNotificationRoleId(), ""));
            }

            //设置区域名称
            record.setTaskAreaName(areaNameMap.getOrDefault(record.getTaskAreaId(), ""));
            //设置附件信息
            setAppendixFiles(record, fileVOMap);
            //设置负责人员名称
            record.setProjectLeaderName(userNameMap.getOrDefault(record.getProjectLeaderId(), ""));
            //设置计划名称
            record.setPlanName(Objects.nonNull(record.getPlanId()) ? planNameMap.getOrDefault(record.getPlanId(), "") : "");
            //设置执行者名称
            if (Objects.nonNull(record.getExecutorId())) {
                record.setExecutorName(userNameMap.getOrDefault(record.getExecutorId(), ""));
            }
        });

    }

    private void buildTaskDetailExtendInfo(List<TaskInfoVO> records) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        //查询需要的项目信息
        List<Long> projectIds = ListUtils.convertItem(records, TaskInfoVO::getProjectId);
        List<DisinfectionProject> projectList = disinfectionProjectService.listProjectsByIds(projectIds);
        Map<Long, String> projectNameMap = MapUtils.listToMap(projectList, DisinfectionProject::getId, DisinfectionProject::getProjectName);
        //查询需要的小组信息
        List<Long> executorGroupIds = ListUtils.convertItem(records, TaskInfoVO::getNotificationGroupId);
        List<DisinfectionGroup> groupList = groupService.listGroupByIds(executorGroupIds);
        Map<Long, String> groupNameMap = MapUtils.listToMap(groupList, DisinfectionGroup::getId, DisinfectionGroup::getGroupName);
        //查询需要的区域信息
        List<Long> areaIds = ListUtils.convertItem(records, TaskInfoVO::getTaskAreaId);
        List<DisinfectionArea> areaList = disinfectionAreaService.listByAreaIds(areaIds);
        Map<Long, String> areaNameMap = MapUtils.listToMap(areaList, DisinfectionArea::getId, DisinfectionArea::getAreaName);
        //查询需要的部门信息
        Map<Long, String> deptNameMap = deptHelper.getDeptNameMap(ListUtils.convertItem(records, TaskInfoVO::getNotificationDeptId));
        //查询需要的岗位信息
        Map<Long, String> postNameMap = postHelper.getPostNameMap(ListUtils.convertItem(records, TaskInfoVO::getNotificationPostId));
        //查询需要的角色信息
        Map<Long, String> roleNameMap = roleHelper.getRoleNameMap(ListUtils.convertItem(records, TaskInfoVO::getNotificationRoleId));
        //查询需要的计划名称
        Map<Long, String> planNameMap = disinfectionPlanService.getPlanNameMap(ListUtils.convertItem(records, TaskInfoVO::getPlanId));
        //查询需要的文件信息
        List<Long> fileIds = ListUtils.convertItem(records, TaskInfoVO::getAppendix)
                .stream()
                .filter(StringUtils::isNotBlank)
                .flatMap(fileIdStr -> Arrays.stream(fileIdStr.split(",")))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(fileIds);
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        //查询需要的人员信息
        List<Long> userIds = ListUtils.convertItem(records, TaskInfoVO::getProjectLeaderId);
        List<Long> executorIds = ListUtils.convertItem(records, TaskInfoVO::getExecutorId).stream().filter(Objects::nonNull)
                .collect(Collectors.toList());
        userIds.addAll(executorIds);
        Map<Long, String> userNameMap = disinfectionUserHelper.getUserNameMap(userIds);
        records.forEach(record -> {
            //设置项目名称
            record.setProjectName(projectNameMap.getOrDefault(record.getProjectId(), ""));
            //设置小组名称
            if (Objects.nonNull(record.getNotificationGroupId())) {
                record.setNotificationGroupName(groupNameMap.getOrDefault(record.getNotificationGroupId(), ""));
            }
            //设置任务时限配置
            if (StringUtils.isNotBlank(record.getTaskTimeLimitConfig())) {
                record.setTaskTimeLimitConfigDTO(JsonUtils.deserialize(record.getTaskTimeLimitConfig(), PlanTaskTimeLimitConfigDTO.class));
            }
            //设置部门名称
            if (Objects.nonNull(record.getNotificationDeptId())) {
                record.setNotificationDeptName(deptNameMap.getOrDefault(record.getNotificationDeptId(), ""));
            }
            //设置岗位名称
            if (Objects.nonNull(record.getNotificationPostId())) {
                record.setNotificationPostName(postNameMap.getOrDefault(record.getNotificationPostId(), ""));
            }
            //设置角色名称
            if (Objects.nonNull(record.getNotificationRoleId())) {
                record.setNotificationRoleName(roleNameMap.getOrDefault(record.getNotificationRoleId(), ""));
            }

            //设置区域名称
            record.setTaskAreaName(areaNameMap.getOrDefault(record.getTaskAreaId(), ""));
            //设置附件信息
            if (StringUtils.isNotBlank(record.getAppendix())) {
                List<FileVO> fileVOS = Arrays.stream(record.getAppendix().split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(Long::valueOf)
                        .map(fileVOMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                record.setFiles(fileVOS);

            } else {
                record.setFiles(new ArrayList<>());
            }
            //设置负责人员名称
            record.setProjectLeaderName(userNameMap.getOrDefault(record.getProjectLeaderId(), ""));
            //设置计划名称
            record.setPlanName(Objects.nonNull(record.getPlanId()) ? planNameMap.getOrDefault(record.getPlanId(), "") : "");
            //设置执行者名称
            if (Objects.nonNull(record.getExecutorId())) {
                record.setExecutorName(userNameMap.getOrDefault(record.getExecutorId(), ""));
            }
        });

    }

    private LambdaQueryChainWrapper<DisinfectionTask> buildQueryWrapper(TaskQueryParam param) {
        LambdaQueryChainWrapper<DisinfectionTask> queryChainWrapper = taskService.lambdaQuery()
                .eq(Objects.nonNull(param.getProjectId()), DisinfectionTask::getProjectId, param.getProjectId());
        queryChainWrapper.like(StringUtils.isNotBlank(param.getKeyword()), DisinfectionTask::getTaskName, param.getKeyword());
        queryChainWrapper.eq(Objects.nonNull(param.getStatus()), DisinfectionTask::getStatus, param.getStatus());
        queryChainWrapper.and(Objects.nonNull(param.getExecutorId()), this::setQueryableScope);

        queryChainWrapper.eq(Objects.nonNull(param.getTaskTypeId()), DisinfectionTask::getTaskTypeId, param.getTaskTypeId());
        if (Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod().getBegin()) &&
            Objects.nonNull(param.getPeriod().getEnd())) {
            queryChainWrapper.between(DisinfectionTask::getCreateTime, param.getPeriod().getBegin(), param.getPeriod().getEnd());
        }
        return queryChainWrapper;
    }

    private void setQueryableScope(LambdaQueryWrapper<DisinfectionTask> q) {
        Long curUserId = SecurityContextHolder.getPrincipal().getId();
        q.eq(DisinfectionTask::getExecutorId, curUserId);
        q.or().eq(DisinfectionTask::getProjectLeaderId, curUserId);
        q.or().eq(DisinfectionTask::getCreateBy, disinfectionUserHelper.getUserAccountById(curUserId));
        //查询当前用户可被通知的范围，包括角色、部门、岗位、小组、小组+角色
        List<UserDTO.Role> roleList = disinfectionUserHelper.getUsersRoles(Collections.singletonList(curUserId));
        List<UserDTO.Organization> deptList = disinfectionUserHelper.getUsersDept(Collections.singletonList(curUserId));
        List<UserDTO.Appointment> appointments = disinfectionUserHelper.getUsersPosts(Collections.singletonList(curUserId));
        List<DisinfectionGroupUser> groupUserList = disinfectionGroupUserService.getGroupsByUserId(curUserId);
        if (ListUtils.nonEmpty(groupUserList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.GROUP)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationGroupId, ListUtils.convertItem(groupUserList, DisinfectionGroupUser::getGroupId));
        }
        if (ListUtils.nonEmpty(roleList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.ROLE)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationRoleId, ListUtils.convertItem(roleList, UserDTO.Role::getId));
        }
        if (ListUtils.nonEmpty(deptList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.DEPT)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationDeptId, ListUtils.convertItem(deptList, UserDTO.Organization::getId));
        }
        if (ListUtils.nonEmpty(appointments)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.POST)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationPostId, ListUtils.convertItem(appointments, UserDTO.Appointment::getId));
        }
        if (ListUtils.nonEmpty(groupUserList) && ListUtils.nonEmpty(roleList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.GROUP_ROLE)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationGroupId, ListUtils.convertItem(groupUserList, DisinfectionGroupUser::getGroupId))
                    .in(DisinfectionTask::getNotificationRoleId, ListUtils.convertItem(roleList, UserDTO.Role::getId));
        }
    }

    private static void setAppendixFiles(TaskPageVO record, Map<Long, FileVO> fileVOMap) {
        if (StringUtils.isNotBlank(record.getAppendix())) {
            List<FileVO> fileVOS = Arrays.stream(record.getAppendix().split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .map(fileVOMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            record.setFiles(fileVOS);

        } else {
            record.setFiles(new ArrayList<>());
        }
    }

    private static void buildExecuteRecordFiles(DisinfectionTaskExecuteRecord record, Map<Long, FileVO> fileMap,
                                                TaskCompletionRecordVO recordVO
                                               ) {
        String appendix = record.getAppendix();
        if (StringUtils.isNotBlank(appendix)) {
            List<FileVO> fileVOS = Arrays.stream(appendix.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .map(fileId -> fileMap.getOrDefault(fileId, null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            recordVO.setFiles(fileVOS);
        }
    }

    private static Set<Long> getExecteRecordPlaceIds(List<DisinfectionTaskExecuteRecord> records) {
        return ListUtils.convertItem(records, DisinfectionTaskExecuteRecord::getKeyPlaceRecord)
                .stream()
                .map(keyPlaceRecord -> JsonUtils.deserialize(keyPlaceRecord, new TypeReference<List<TaskCompletionRecordVO.PlaceRecordVO>>() {}))
                .filter(ListUtils::nonEmpty)
                .flatMap(Collection::stream)
                .map(TaskCompletionRecordVO.PlaceRecordVO::getPlaceId)
                .collect(Collectors.toSet());
    }

    private static Set<Long> getExecuteRecordsFiles(List<DisinfectionTaskExecuteRecord> records) {
        Set<Long> fileIdSet = new HashSet<>();
        //查询场所信息
        records.forEach(record -> {
            String appendix = record.getAppendix();
            //收集记录的文件图片id
            if (StringUtils.isNotBlank(appendix)) {
                List<Long> fileIds = Arrays.stream(appendix.split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                fileIdSet.addAll(fileIds);
            }
            //收集场所对应的文件图片id
            String keyPlaceRecord = record.getKeyPlaceRecord();
            if (StringUtils.isBlank(keyPlaceRecord)) {
                return;
            }
            List<TaskCompletionRecordVO.PlaceRecordVO> placeRecordVOS = JsonUtils.deserialize(keyPlaceRecord, new TypeReference<List<TaskCompletionRecordVO.PlaceRecordVO>>() {});
            if (ListUtils.isEmpty(placeRecordVOS)) {
                return;
            }
            Set<Long> keyPlaceFileIds = placeRecordVOS.stream()
                    .map(TaskCompletionRecordVO.PlaceRecordVO::getAppendix)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(placeAppendix -> Arrays.stream(placeAppendix.split(",")))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            fileIdSet.addAll(keyPlaceFileIds);
        });
        return fileIdSet;
    }

    private static void buildKeyPlaceInfos(DisinfectionTaskExecuteRecord record, Map<Long, DisinfectionKeyPlace> keyPlaceMap,
                                           Map<Long, FileVO> fileMap, TaskCompletionRecordVO recordVO
                                          ) {
        String keyPlaceRecord = record.getKeyPlaceRecord();
        if (StringUtils.isNotBlank(keyPlaceRecord)) {
            List<TaskCompletionRecordVO.PlaceRecordVO> placeRecordVOS = JsonUtils.deserialize(keyPlaceRecord, new TypeReference<List<TaskCompletionRecordVO.PlaceRecordVO>>() {});
            if (ListUtils.nonEmpty(placeRecordVOS)) {
                placeRecordVOS.forEach(placeRecordVO -> {
                    Long placeId = placeRecordVO.getPlaceId();
                    DisinfectionKeyPlace disinfectionKeyPlace = keyPlaceMap.get(placeId);
                    if (Objects.isNull(disinfectionKeyPlace)) {
                        return;
                    }
                    placeRecordVO.setPlaceName(disinfectionKeyPlace.getPlaceName());
                    placeRecordVO.setPlaceTypeId(disinfectionKeyPlace.getPlaceTypeId());
                    placeRecordVO.setPlaceTypeName(disinfectionKeyPlace.getPlaceTypeName());
                    placeRecordVO.setPlaceDetail(disinfectionKeyPlace.getPlaceDetail());
                    String appendix = placeRecordVO.getAppendix();
                    if (StringUtils.isBlank(appendix)) {
                        return;
                    }
                    List<FileVO> fileVOS = Arrays.stream(appendix.split(","))
                            .filter(StringUtils::isNotBlank)
                            .map(Long::parseLong)
                            .map(fileMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    placeRecordVO.setFiles(fileVOS);
                });
                //设置重点场所信息
                recordVO.setPlaceRecords(placeRecordVOS);
                return;
            }
        }
        recordVO.setPlaceRecords(new ArrayList<>());
    }

    private static void buildTaskDeadLine(PlanTaskTimeLimitConfigDTO taskTimeLimitConfig, DisinfectionTask disinfectionTask) {
        //设置任务截至时间
        LocalDateTime now = LocalDateTime.now();
        if (Objects.nonNull(taskTimeLimitConfig.getDay())) {
            now = now.plusDays(taskTimeLimitConfig.getDay());
        }
        if (Objects.nonNull(taskTimeLimitConfig.getHour())) {
            now = now.plusHours(taskTimeLimitConfig.getHour());
        }
        if (Objects.nonNull(taskTimeLimitConfig.getMinute())) {
            now = now.plusMinutes(taskTimeLimitConfig.getMinute());
        }
        disinfectionTask.setDeadline(now);
    }
}