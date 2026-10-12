package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskParentStatusSyncService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionMappingEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 各种业务处理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HandlerServiceImpl implements HandlerService {

    private final UserClient userClient;

    private final InspectQuery inspectQuery;

    private final InspectRecordMapper inspectRecordMapper;
    private final InspectTaskMapper inspectTaskMapper;
    private final InspectTaskParentStatusSyncService inspectTaskParentStatusSyncService;
    private final ApplicationClient applicationClient;

    /**
     * 将部门岗位处理成名称
     *
     * @param deptIds
     * @param postIds
     *
     * @return
     */
    @Override
    public String handlerDeptIdsAndPostIdsToNames(String deptIds, String postIds) {

        String userNameStr = "";
        //处理候选人
        if (StringUtils.isNotEmpty(deptIds) && StringUtils.isNotEmpty(postIds)) {
            List<Long> deptIdArr = Arrays.stream(deptIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> postIdArr = Arrays.stream(postIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> userIds = userClient.listUserByOrgAndPost(deptIdArr, postIdArr);
            List<UserDTO> userDTOS = userClient.listUserByIds(userIds);
            userNameStr = userDTOS.stream().map(UserDTO::getName).collect(Collectors.joining(","));
        }
        return userNameStr;
    }

    /**
     * 将用户ids处理成名称
     *
     * @param userIds
     *
     * @return
     */
    @Override
    public String handlerUserIdsToNames(String userIds) {

        String userNameStr = "";
        //处理候选人
        if (StringUtils.isNotEmpty(userIds)) {
            List<Long> userIdList = Arrays.stream(userIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
            List<UserDTO> userDTOS = userClient.listUserByIds(userIdList);
            userNameStr = userDTOS.stream().map(UserDTO::getName).collect(Collectors.joining(","));
        }
        return userNameStr;
    }

    /**
     * 将时间天、时、分处理成分钟数
     *
     * @param taskTimeLimit
     *
     * @return
     */
    @Override
    public Integer getLimitTotalMinute(String taskTimeLimit) {
        if (StringUtils.isNotEmpty(taskTimeLimit)) {
            JSONObject jsonObject = JSONObject.parseObject(taskTimeLimit);
            int day = StringUtils.isNotEmpty(jsonObject.get("day").toString()) ? Integer.parseInt(jsonObject.get("day").toString()) : 0;
            int hour = StringUtils.isNotEmpty(jsonObject.get("hour").toString()) ? Integer.parseInt(jsonObject.get("hour").toString()) : 0;
            int minute =
                    StringUtils.isNotEmpty(jsonObject.get("minute").toString()) ? Integer.parseInt(jsonObject.get("minute").toString()) : 0;
            return (day * 24 * 60) + (hour * 60) + minute;
        } else {
            return null;
        }
    }

    /**
     * 将部门ids和岗位ids处理成人员ids
     *
     * @param deptIds 部门ids
     * @param postIds 岗位ids
     * @param roleIds 角色ids
     *
     * @return 逗号隔开的人员ids
     */
    @Override
    public String HandlerDeptIdsAndPostIdsToUserIds(String deptIds, String postIds, String roleIds) {
        String userIdStr = "";
        UserQuery queryParam = new UserQuery();
        queryParam.setEnabled(true);
        if (StringUtils.isNotBlank(deptIds)) {
            queryParam.setOrganizationIds(Arrays.stream(deptIds.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
        }
        if (StringUtils.isNotBlank(postIds)) {
            queryParam.setPositionIds(Arrays.stream(postIds.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
        }
        if (StringUtils.isNotBlank(roleIds)) {
            queryParam.setRoleIds(Arrays.stream(roleIds.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
        }
        List<UserDTO> userIdList = userClient.postList(queryParam);
        if (!CollectionUtils.isEmpty(userIdList)) {
            userIdStr = userIdList.stream().map(u -> String.valueOf(u.getId())).distinct().collect(Collectors.joining(","));
        }
        return userIdStr;
    }

    /**
     * 通过用户ids查询其部门ids
     *
     * @param userIds 用户ids
     *
     * @return
     */
    @Override
    public String handlerUserIdsToDeptIds(String userIds) {

        List<Long> userIdList = Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<UserDTO> userDTOList = userClient.listUserByIds(userIdList);
        Wrappers.<PlanRelationPO>lambdaUpdate().set(PlanRelationPO::getDeleted, 1L).eq(PlanRelationPO::getDeleted, 0);
        return Optional.ofNullable(userDTOList).orElse(Collections.emptyList()).stream()
                .map(temp -> String.valueOf(temp.getOrganization().getId())).collect(Collectors.joining(","));

    }

    /**
     * 将计划关联关系处理成对象ids
     * 集合关联关系集合
     *
     * @return
     */
    @Override
    public String handlerTargetDTOListToTargetIds(List<PlanRelationPO> targetDTOList) {
        return targetDTOList.stream()
                .filter(distinctByTargetId(PlanRelationPO::getTargetId))
                .map(r -> String.valueOf(r.getTargetId()))
                .collect(Collectors.joining(","));
    }

    @Override
    public String handlerInspectTargetDTOListToTargetIds(List<InspectTargetDTO> targetDTOList) {
        if (CollectionUtils.isEmpty(targetDTOList)) {
            return null;
        }
        return targetDTOList.stream()
                .filter(distinctByTargetId(InspectTargetDTO::getTargetId))
                .map(r -> String.valueOf(r.getTargetId()))
                .collect(Collectors.joining(","));
    }

    /**
     * 将任务关联关系处理成对象ids
     * taskTargetDTOList 任务关联关系集合
     *
     * @return
     */
    @Override
    public String handlerTaskTargetDTOListToTargetIds(List<InspectTargetDTO> taskTargetDTOList) {

        return taskTargetDTOList.stream().filter(distinctByTargetId(InspectTargetDTO::getTargetId))
                .map(r -> String.valueOf(r.getTargetId()))
                .collect(Collectors.joining(","));
    }

    public static <T> Predicate<T> distinctByTargetId(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        //"这个函数将应用到每一个item";
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 设置完成
     *
     * @param po
     *
     * @return
     */
    @Override
    public TaskRateResult handleTaskRateResult(InspectTaskPO po) {
        TaskRateResult rateResult = new TaskRateResult();
        rateResult.setTaskName(po.getName());
        rateResult.setTargetAllNum(po.getTargetNum());
        rateResult.setStatus(po.getStatus());
        rateResult.setReceiveNames(inspectQuery.extendUserNames(po.getTenant(), po.getReceiveIds()));
        rateResult.setRate(this.getTaskRecordRate(po.getId(), po.getTargetNum()));
        return rateResult;
    }

    /**
     * 获取任务的记录完成率
     */
    @Override
    public Double getTaskRecordRate(Long taskId, Integer targetNum) {
        var count = this.getTaskRecordNum(taskId, null);

        double d = (count * 1D / targetNum) * 100;

        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取任务 巡检对象数
     *
     * @param taskId     任务id 必填
     * @param resultEnum 结果(正常异常) 可选
     *
     * @return
     */
    @Override
    public Long getTaskRecordNum(Long taskId, InspectRecordResultEnum resultEnum) {
        var pos = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                         .eq(InspectRecordPO::getTaskId, taskId)
                                                         .eq(resultEnum != null, InspectRecordPO::getResult, resultEnum)
                                                         .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.OVER));
        return pos == null ? 0L : pos.size();
    }

    /**
     * 获取任务的巡检记录集合(不分页)
     *
     * @param planId 计划id
     * @param taskId 任务id
     *
     * @return
     */
    @Override
    public List<InspectRecordVO> getTaskRecordList(Long planId, Long taskId) {
        var recordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                  .eq(InspectRecordPO::getPlanId, planId)
                                                                  .eq(InspectRecordPO::getTaskId, taskId));
        return Optional.ofNullable(recordPOList).orElse(Collections.emptyList())
                .parallelStream().map(po -> {
                    InspectRecordVO vo = ObjectUtils.copying(po, InspectRecordVO.class);
                    return vo;
                }).collect(Collectors.toList());
    }

    /**
     * 获取用户名称
     *
     * @param username
     *
     * @return
     */
    @Override
    public String getUserNameByName(String username) {
        String name = "";
        if (StringUtils.isNotEmpty(username)) {
            UserDTO user = userClient.getUserByUserName(username);
            if (ObjectUtils.isNotEmpty(user)) {
                name = user.getName();
            }
        }
        return name;
    }

    /**
     * 获取用户名称
     *
     * @param userId
     *
     * @return
     */
    @Override
    public String getUserNameById(Long userId) {
        String name = "";
        if (ObjectUtils.isNotEmpty(userId)) {
            UserDTO user = userClient.getUser(userId);
            if (ObjectUtils.isNotEmpty(user)) {
                name = user.getName();
            }
        }
        return name;
    }

    /**
     * 获取用户名称
     *
     * @param userId
     *
     * @return
     */
    @Override
    public String getUserNameById(String userId) {
        if (ObjectUtils.isEmpty(userId)) {
            return "";
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if (pattern.matcher(userId).matches()) {
            return this.getUserNameById(Long.parseLong(userId));
        }
        return "";
    }

    /**
     * 将字符串ids处理成List<Long>>
     *
     * @param ids id字符串
     *
     * @return
     */
    @Override
    public Set<Long> stringIdsToList(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new HashSet<>();
        }
        return Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toSet());
    }

    /**
     * 将字符串ids处理成List<Long>>
     *
     * @param ids id字符串
     *
     * @return
     */
    @Override
    public List<Long> stringIdsToList(String ids, String CharSequence) {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return Arrays.stream(ids.split(CharSequence)).map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * 将json格式的xx天xx小时xx分钟转换成字符串、
     * 示例：'{"day":"1","hour":"0","minute":"0"}' --> "1天0小时0分钟"
     *
     * @param jsonString
     *
     * @return
     */
    @Override
    public String jsonDayToStringDay(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return "0天0小时0分钟";
        }
        var jsonObject = JsonUtils.deserialize(jsonString, new TypeReference<Map<String, Object>>() {});
        if (MapUtils.isEmpty(jsonObject)) {
            return "0天0小时0分钟";
        }
        return jsonObject.get("day").toString() + "天" + jsonObject.get("hour").toString() + "小时" + jsonObject.get("minute").toString() +
               "分钟";
    }

    /**
     * 巡检任务检查处理
     * (记录变更后，需要检查任务状态)
     *
     * @param recordIdList 巡检记录id集合
     * @param principal    当前登陆信息
     */
    @Override
    public void inspectTaskStatusCheck(List<Long> recordIdList, Principal principal) {
        Set<Long> taskIdSet = new HashSet<>();
        List<InspectRecordPO> tempList = inspectRecordMapper.selectBatchIds(recordIdList);
        if (CollectionUtils.isEmpty(tempList)) {
            return;
        }
        tempList.forEach(recordPO -> taskIdSet.add(recordPO.getTaskId()));
        if (CollectionUtils.isEmpty(taskIdSet)) {
            return;
        }
        List<InspectRecordPO> inspectRecordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                                           .in(InspectRecordPO::getTaskId, taskIdSet));
        this.inspectTaskStatusCheck(new ArrayList<>(taskIdSet), inspectRecordPOList, principal);
    }

    /**
     * 巡检任务检查处理
     * (记录变更后，需要检查任务状态)
     *
     * @param taskIdList          任务id集合
     * @param inspectRecordPOList 任务的所有巡检记录
     * @param principal           登陆信息
     */
    @Override
    public void inspectTaskStatusCheck(List<Long> taskIdList, List<InspectRecordPO> inspectRecordPOList, Principal principal) {
        if (CollectionUtils.isEmpty(inspectRecordPOList) || CollectionUtils.isEmpty(taskIdList)) {
            return;
        }
        Map<Long, List<InspectRecordPO>> recordMap = inspectRecordPOList.stream()
                .collect(Collectors.groupingBy(InspectRecordPO::getTaskId));
        Set<Long> allTaskIdList = new HashSet<>();
        recordMap.forEach((taskId, list) -> {
            // 全部巡过
            boolean inspectBool = list.stream().noneMatch(po -> InspectRecordStatusEnum.EXECUTING.equals(po.getStatus()));
            if (inspectBool) {
                allTaskIdList.add(taskId);
            }
            // 其他情况(没有巡完)就不更新任务状态 保持进行中
        });

        /* 处理任务待办 */
        this.handlerTaskTodo(new ArrayList<>(allTaskIdList), recordMap, principal);
    }

    @Override
    public void handlerRepairTodo(List<InspectRecordPO> recordList, Principal principal, LocalDateTime now
                                 ) {
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        String businessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        //获取业务应用映射关系
        InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(businessType);
        ApplicationDTO application = applicationClient.getApplicationByCode(businessTypeEnum.getApplicationCode());
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        recordList.forEach((po) -> {
            var taskName = po.getTargetName() + "待修复";
            //创建消息体
            TodoTaskInitMessage message = new TodoTaskInitMessage();
            message.setApplicationCode(application.getCode());
            message.setApplicationName(application.getName());
            message.setBusinessKey(String.valueOf(po.getId()));
            message.setBusinessType("INSPECTION_REPAIR");
            message.setBusinessTypeName("巡检修复");
            message.setBusinessStatus(InspectTaskStatusEnum.WAIT_REPAIR.name());
            message.setBusinessStatusName(InspectTaskStatusEnum.WAIT_REPAIR.getDescription());
            message.setTaskName(taskName);
            message.setTaskTime(now);
            message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
            message.setUserIds(Collections.singletonList(principal.getId()));
            RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(po.getTaskId()));
            //发送工作中心消息
            WorkTaskInitMessage workTaskInitMessage = new WorkTaskInitMessage();
            workTaskInitMessage.setApplicationCode(application.getCode());
            workTaskInitMessage.setApplicationName(application.getName());
            workTaskInitMessage.setBusinessKey(String.valueOf(po.getId()));
            workTaskInitMessage.setBusinessType("INSPECTION_REPAIR");
            workTaskInitMessage.setBusinessTypeName("巡检修复");
            workTaskInitMessage.setBusinessStatus(InspectTaskStatusEnum.WAIT_REPAIR.name());
            workTaskInitMessage.setBusinessStatusName(InspectTaskStatusEnum.WAIT_REPAIR.getDescription());
            workTaskInitMessage.setTaskName(taskName);
            workTaskInitMessage.setTaskTime(now);
            workTaskInitMessage.setOperationType(WorkTaskFlowOperationEnum.INSERT);
            workTaskInitMessage.setUserIds(Collections.singletonList(principal.getId()));
            RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskInitMessage, String.valueOf(po.getTaskId()));
        });
    }

    @Override
    public void sendChangeTodo(Long taskId, InspectTaskStatusEnum nextStatus, String oldStatus, Map<Long, List<InspectRecordPO>> recordMap,
                               Long userId, List<Long> nextUserIds
                              ) {
        Set<Long> userIds = null;
        if (userId != null) {
            userIds = Collections.singleton(userId);
        } else if (nextStatus == InspectTaskStatusEnum.OVER || nextStatus == InspectTaskStatusEnum.REPAIRED) {
            // 任务完成时 查找完成人
            List<InspectRecordPO> recordPOS = recordMap.get(taskId);
            if (!CollectionUtils.isEmpty(recordPOS)) {
                userIds = recordPOS.stream().filter(Objects::nonNull).map(InspectRecordPO::getUserId).collect(Collectors.toSet());
            }
        }
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(taskId));
        message.setBusinessStatus(oldStatus);
        message.setUserIds(userIds);
        message.setHandlerTime(LocalDateTime.now());
        message.setTaskTime(LocalDateTime.now());
        boolean generatedNextStatusTask = !(InspectTaskStatusEnum.OVER.equals(nextStatus)
                                            || InspectTaskStatusEnum.STOP.equals(nextStatus)
                                            || InspectTaskStatusEnum.REPAIRED.equals(nextStatus));
        message.setGeneratedNextStatusTask(generatedNextStatusTask);
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(nextStatus.name());
        message.setNextBusinessStatusName(nextStatus.getDescription());
        message.setNextUserIds(nextUserIds);
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(taskId));

        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = new WorkTaskFlowCirculationMessage();
        workTaskFlowCirculationMessage.setBusinessKey(String.valueOf(taskId));
        workTaskFlowCirculationMessage.setBusinessStatus(oldStatus);
        workTaskFlowCirculationMessage.setUserIds(userIds);
        workTaskFlowCirculationMessage.setHandlerTime(LocalDateTime.now());
        workTaskFlowCirculationMessage.setTaskTime(LocalDateTime.now());
        workTaskFlowCirculationMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        workTaskFlowCirculationMessage.setGeneratedNextStatusTask(generatedNextStatusTask);
        workTaskFlowCirculationMessage.setNextBusinessStatus(nextStatus.name());
        workTaskFlowCirculationMessage.setNextBusinessStatusName(nextStatus.getDescription());
        workTaskFlowCirculationMessage.setNextUserIds(nextUserIds);
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage, String.valueOf(taskId));
    }

    private void handlerTaskTodo(List<Long> allTaskIdList, Map<Long, List<InspectRecordPO>> recordMap,
                                 Principal principal
                                ) {
        if (allTaskIdList == null || allTaskIdList.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Map<Long, InspectTaskPO> oldTaskMap = new HashMap<>();
        List<InspectTaskPO> oldTaskPOS = inspectTaskMapper.selectByIds(allTaskIdList);
        Optional.ofNullable(oldTaskPOS).orElse(Collections.emptyList()).forEach(po -> oldTaskMap.put(po.getId(), po));
        this.handTaskResult(allTaskIdList, recordMap, principal, now);
        allTaskIdList.forEach(taskId -> {
            var oldTask = oldTaskMap.getOrDefault(taskId, new InspectTaskPO());
            List<Long> nextUserIds = this.toLongList(oldTask.getReceiveIds());
            this.sendChangeTodo(taskId, InspectTaskStatusEnum.OVER, oldTask.getStatus().name(), recordMap, null, nextUserIds);
        });
    }

    private void handTaskResult(List<Long> taskIdList, Map<Long, List<InspectRecordPO>> recordMap, Principal principal, LocalDateTime now) {
        if (CollectionUtils.isEmpty(taskIdList) || recordMap == null || recordMap.isEmpty()) {
            return;
        }
        var abnormalIdList = new ArrayList<Long>();
        var normalIdList = new ArrayList<Long>();
        taskIdList.forEach(taskId -> {
            var recordPOS = recordMap.get(taskId);
            boolean hasAbnormal = recordPOS.stream().anyMatch(record -> record.getResult() == InspectRecordResultEnum.ABNORMAL);
            if (hasAbnormal) {
                abnormalIdList.add(taskId);
            } else {
                normalIdList.add(taskId);
            }
        });
        if (CollectionUtils.isNotEmpty(abnormalIdList)) {
            inspectTaskMapper.updateStatusByIds(abnormalIdList, InspectTaskStatusEnum.OVER.name(), InspectRecordResultEnum.ABNORMAL.name(), now, now, principal.getAccount());
        }
        if (CollectionUtils.isNotEmpty(normalIdList)) {
            inspectTaskMapper.updateStatusByIds(normalIdList, InspectTaskStatusEnum.OVER.name(), InspectRecordResultEnum.NORMAL.name(), now, now, principal.getAccount());
        }
        abnormalIdList.forEach(inspectTaskParentStatusSyncService::syncParentTaskStatus);
        normalIdList.forEach(inspectTaskParentStatusSyncService::syncParentTaskStatus);
    }

    private List<Long> toLongList(String csv) {
        if (csv == null || csv.isEmpty()) {
            return List.of();
        }
        String[] parts = csv.split(",");
        List<Long> result = new ArrayList<>(parts.length);
        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                result.add(Long.parseLong(part));
            }
        }
        return result;
    }
}
