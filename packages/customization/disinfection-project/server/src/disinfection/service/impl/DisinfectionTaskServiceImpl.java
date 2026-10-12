package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionTaskMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.TaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionTaskServiceImpl extends ServiceImpl<DisinfectionTaskMapper, DisinfectionTask>
        implements DisinfectionTaskService {
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionGroupUserService disinfectionGroupUserService;

    @Override
    public boolean updateTaskTypeName(long taskTypeId, String dataName) {
        return lambdaUpdate().set(DisinfectionTask::getTaskTypeName, dataName).eq(DisinfectionTask::getTaskTypeId, taskTypeId).update();
    }

    @Override
    public boolean hasTask(long projectId) {
        return lambdaQuery().eq(DisinfectionTask::getProjectId, projectId).count() > 0;
    }

    @Override
    public List<DisinfectionTask> listByProjectId(long projectId) {
        return lambdaQuery().eq(DisinfectionTask::getProjectId, projectId).list();
    }

    @Override
    public boolean existNotCompletedTasksByAreaId(List<Long> areaIds) {
        if (ListUtils.isEmpty(areaIds)) {
            return false;
        }
        List<DisinfectionTaskStatusEnum> notCompletedList = Arrays.asList(DisinfectionTaskStatusEnum.WAIT_EXECUTE, DisinfectionTaskStatusEnum.EXECUTING, DisinfectionTaskStatusEnum.WAIT_RECEIVE);
        return lambdaQuery().in(DisinfectionTask::getTaskAreaId, areaIds).in(DisinfectionTask::getStatus, notCompletedList).count() > 0;
    }

    @Override
    public boolean existNotCompletedTasksByGroupId(long groupId) {

        List<DisinfectionTaskStatusEnum> notCompletedList = Arrays.asList(DisinfectionTaskStatusEnum.WAIT_EXECUTE, DisinfectionTaskStatusEnum.EXECUTING, DisinfectionTaskStatusEnum.WAIT_RECEIVE);
        return lambdaQuery().eq(DisinfectionTask::getNotificationGroupId, groupId).in(DisinfectionTask::getStatus, notCompletedList)
                       .count() > 0;

    }

    @Override
    public List<ProjectStatisticsBaseVO> countTasksByStatus(TaskQueryParam taskQueryParam) {
        List<DisinfectionTask> disinfectionTasks = this.lambdaQuery()
                .select(DisinfectionTask::getStatus, DisinfectionTask::getProjectId)
                .eq(Objects.nonNull(taskQueryParam.getStatus()), DisinfectionTask::getStatus, taskQueryParam.getStatus())
                .eq(Objects.nonNull(taskQueryParam.getTaskTypeId()), DisinfectionTask::getTaskTypeId, taskQueryParam.getTaskTypeId())
                .like(StringUtils.isNotBlank(taskQueryParam.getKeyword()), DisinfectionTask::getTaskName, taskQueryParam.getKeyword())
                .and(Objects.nonNull(taskQueryParam.getExecutorId()), this::setQueryableScope)
                .list();
        Map<DisinfectionTaskStatusEnum, Long> statusEnumLongMap = disinfectionTasks.stream()
                .collect(Collectors.groupingBy(DisinfectionTask::getStatus, Collectors.counting()));
        List<ProjectStatisticsBaseVO> result = new ArrayList<>();
        statusEnumLongMap.forEach((k, v) -> {
            result.add(ProjectStatisticsBaseVO.builder()
                               .keyName(k.name())
                               .number(Objects.nonNull(v) ? v.intValue() : 0)
                               .build());
        });
        return result;
    }

    @Override
    public boolean updateTaskStatus(long taskId, DisinfectionTaskStatusEnum status) {

        return update(new LambdaUpdateWrapper<DisinfectionTask>().eq(DisinfectionTask::getId, taskId)
                              .set(DisinfectionTask::getStatus, status));
    }

    @Override
    public List<Integer> listAllTenantCodes() {
        return this.getBaseMapper().listAllTenantCodes();
    }

    private void setQueryableScope(LambdaQueryWrapper<DisinfectionTask> q) {
        Long curUserId = SecurityContextHolder.getPrincipal().getId();
        q.eq(DisinfectionTask::getExecutorId, curUserId);
        q.or().eq(DisinfectionTask::getProjectLeaderId, curUserId);
        q.or().eq(DisinfectionTask::getCreateBy, disinfectionUserHelper.getUserAccountById(curUserId));
        //查询当前用户可被通知的范围，包括角色、部门、岗位、小组、小组+角色
        List<UserDTO.Role> roleList = disinfectionUserHelper.getUsersRoles(Collections.singletonList(curUserId));
        List<UserDTO.Organization> deptList = disinfectionUserHelper.getUsersDept(Collections.singletonList(curUserId));
        List<UserDTO.Appointment> appointmentList = disinfectionUserHelper.getUsersPosts(Collections.singletonList(curUserId));
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
        if (ListUtils.nonEmpty(appointmentList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.POST)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationPostId, ListUtils.convertItem(appointmentList, UserDTO.Appointment::getId));
        }
        if (ListUtils.nonEmpty(groupUserList) && ListUtils.nonEmpty(roleList)) {
            q.or().eq(DisinfectionTask::getNotificationType, DisinfectionNotificationTypeEnum.GROUP_ROLE)
                    .in(DisinfectionTask::getStatus, DisinfectionTaskStatusEnum.getAppStatusList())
                    .isNull(DisinfectionTask::getExecutorId)
                    .in(DisinfectionTask::getNotificationGroupId, ListUtils.convertItem(groupUserList, DisinfectionGroupUser::getGroupId))
                    .in(DisinfectionTask::getNotificationRoleId, ListUtils.convertItem(roleList, UserDTO.Role::getId));
        }
    }
}