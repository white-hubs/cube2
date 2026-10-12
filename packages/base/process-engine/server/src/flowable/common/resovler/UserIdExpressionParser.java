package com.guhecloud.cube.base.flow.flowable.common.resovler;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.UserRef;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/9/1 14:12
 * @description
 */
@Service("assigneeParser")
public class UserIdExpressionParser {

    /**
     * 指定用户
     */
    String TYPE_USERS = "USERS";
    /**
     * 指定岗位
     */
    String TYPE_POST = "POST";
    /**
     * 指定部门
     */
    String TYPE_DEPT = "DEPT";
    /**
     * 部门负责人
     */
    String TYPE_DEPT_MANAGER = "DEPT_MANAGER";
    /**
     * 部门分管领导
     */
    String TYPE_DEPT_LEADER = "DEPT_LEADER";

    /**
     * 发起人
     */
    String TYPE_START_USER = "START_USER";
    /**
     * 发起人部门负责人
     */
    String TYPE_START_USER_DEPT_MANAGER = "START_USER_DEPT_MANAGER";
    /**
     * 发起人部门分管领导
     */
    String TYPE_START_USER_DEPT_LEADER = "START_USER_DEPT_LEADER";
    /**
     * 上一步的处理人
     */
    String TYPE_PREV_ASSIGNEE = "PREV_ASSIGNEE";
    /**
     * 上一步处理人的部门负责人
     */
    String TYPE_PREV_ASSIGNEE_DEPT_MANAGER = "PREV_ASSIGNEE_DEPT_MANAGER";
    /**
     * 上一步处理人的部门分管领导
     */
    String TYPE_PREV_ASSIGNEE_DEPT_LEADER = "PREV_ASSIGNEE_DEPT_LEADER";

    /**
     * 上一个处理人部门
     */
    String TYPE_PREV_HANDLER_DEPT = "PREV_HANDLER_DEPT";

    String TYPE_TENANT_USER = "TENANT_USER";

    /**
     * 角色支持
     */
    String TYPE_ROLES = "ROLES";

    private static final String SEPARTOR = ":";

    @Autowired
    private ProcessHelper helper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private PositionClient positionClient;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RoleClient roleClient;

    private static final List<String> TYPES = new ArrayList<>();

    public UserIdExpressionParser() {

        TYPES.add(TYPE_USERS);
        TYPES.add(TYPE_POST);
        TYPES.add(TYPE_DEPT);
        TYPES.add(TYPE_DEPT_MANAGER);
        TYPES.add(TYPE_DEPT_LEADER);
        TYPES.add(TYPE_START_USER);
        TYPES.add(TYPE_START_USER_DEPT_LEADER);
        TYPES.add(TYPE_PREV_ASSIGNEE);
        TYPES.add(TYPE_PREV_ASSIGNEE_DEPT_MANAGER);
        TYPES.add(TYPE_PREV_ASSIGNEE_DEPT_LEADER);
        TYPES.add(TYPE_TENANT_USER);
        TYPES.add(TYPE_PREV_HANDLER_DEPT);
        TYPES.add(TYPE_ROLES);
    }

    public String resolveAssigneeUserWithNotify(DelegateExecution execution, String type) {
        if (type.contains("channel")) {
            type = type.substring(0, type.indexOf(";"));
        }

        return resolveAssigneeUser(execution, type);
    }

    public String resolveAssigneeUser(DelegateExecution execution, String type) {
        List<String> userIds = resolveUsers(execution, type);
        return userIds.size() == 1 ? userIds.get(0) : null;
    }

    public Set<String> resolveCandidateUsersWithNotify(DelegateExecution execution, String type) {
        if (type.contains("channel")) {
            type = type.substring(0, type.indexOf(";"));
        }

        return resolveCandidateUsers(execution, type);
    }

    public Set<String> resolveCandidateUsers(DelegateExecution execution, String type) {
        List<String> userIds = resolveUsers(execution, type);
        return userIds.size() > 1 ? new HashSet<>(userIds) : new HashSet<>();
    }

    public Set<String> resolveMultipleUsers(DelegateExecution execution, String... types) {
        Set<String> userIds = new HashSet<>();
        if (types != null) {
            for (String type : types) {
                userIds.addAll(resolveUsers(execution, type));
            }
        }
        return userIds;
    }

    public String getType(String expression) {
        return TYPES.stream().filter(expression::contains).findFirst().get();
    }

    public String getExpression(String expression) {
        return expression.substring(expression.indexOf("'") + 1, expression.lastIndexOf("'"));
    }

    public List<UserNotificationChannelMappingDTO> resolveUsersAndNotify(DelegateExecution execution, String typeExpression) {
        String[] types = typeExpression.split("\\|");
        List<UserNotificationChannelMappingDTO> userNotificationChannelMappings = new ArrayList<>();
        for (String type : types) {
            String userType = type;
            String noticeType = type;
            if (type.contains("channel")) {
                userType = type.substring(0, type.indexOf(";"));
            }
            userType = userType.replace(";", "");

            List<String> userId = resolveUsers(execution, userType);
            // 解析通知表达式
            if (type.contains("channel")) {
                noticeType = type.substring(type.indexOf(";") + 1);
            }
            Set<ChannelEnum> channelTypeEnums = resolveNotificationChannel(noticeType);
            if (CollectionUtils.isNotEmpty(userId)) {
                userNotificationChannelMappings.add(
                        new UserNotificationChannelMappingDTO(
                                new HashSet<>(userId), channelTypeEnums)
                                                   );
            }

        }

        return userNotificationChannelMappings;
    }

    public Set<ChannelEnum> resolveNotificationChannel(String notificationChannelExpression) {
        notificationChannelExpression = notificationChannelExpression.replaceAll(";", "");
        if (notificationChannelExpression.startsWith("channel:")) {
            String[] split = notificationChannelExpression.split(":")[1].split(",");
            return Arrays.stream(split).distinct()
                    .map(ChannelEnum::valueOf)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public Set<String> resolveMultipleUsersWithNotify(DelegateExecution execution, String typeExpression) {
        String[] types = typeExpression.split("\\|");
        Set<String> userIds = new HashSet<>();
        for (String type : types) {
            if (type.contains(";")) {
                type = type.substring(0, type.indexOf(";"));
            }
            List<String> userId = resolveUsers(execution, type);
            userIds.addAll(userId);
        }
        return userIds;

    }

    /**
     * 传入表达式，解析类型和名称
     */
    public List<UserRef> resolve(DelegateExecution execution, String typeExpression) {
        String[] types = typeExpression.split("\\|");
        List<ResolvedTypeData> resolvedTypeDataList = new ArrayList<>();
        for (String type : types) {
            String userType = type;
            if (type.contains("channel")) {
                userType = type.substring(0, type.indexOf(";"));
            }
            resolvedTypeDataList.addAll(resolveType(execution, userType));
        }

        // user
        Set<Long> userIds = resolvedTypeDataList.stream()
                .filter(resolvedTypeData -> resolvedTypeData.getType().equals("user"))
                .map(ResolvedTypeData::getId).collect(Collectors.toSet());
        List<UserDTO> userDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            userDTOS = userClient.listUserByIds(userIds);
        }

        // post
        List<Long> postIds = resolvedTypeDataList.stream()
                .filter(resolvedTypeData -> resolvedTypeData.getType().equals("post"))
                .map(ResolvedTypeData::getId).collect(Collectors.toList());
        List<PositionDTO> positions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(postIds)) {
            positions = positionClient.getPositions(postIds);
        }

        // dept
        List<Long> deptIds = resolvedTypeDataList.stream()
                .filter(resolvedTypeData -> resolvedTypeData.getType().equals("dept"))
                .map(ResolvedTypeData::getId).collect(Collectors.toList());
        List<OrganizationDTO> organizations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deptIds)) {
            organizations = organizationClient.getOrganizationsByIds(deptIds);
        }

        List<UserRef> result = new ArrayList<>();
        List<UserRef> users = ObjectUtils.copying(userDTOS, UserRef.class);
        List<UserRef> posts = ObjectUtils.copying(positions, UserRef.class);
        List<UserRef> depts = ObjectUtils.copying(organizations, UserRef.class);
        result.addAll(users);
        result.addAll(posts);
        result.addAll(depts);
        return result;
    }

    public List<ResolvedTypeData> resolveType(DelegateExecution execution, String type) {
        List<ResolvedTypeData> resolvedTypeDataList = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            // 用户
            if (type.startsWith(TYPE_USERS + SEPARTOR)) {
                List<String> userIds = Arrays.asList(type.split(SEPARTOR)[1].split(","));
                userIds.forEach(userId -> {
                    resolvedTypeDataList.add(
                            new ResolvedTypeData("user", Long.parseLong(userId))
                                            );
                });
            }
            // 岗位
            else if (type.startsWith(TYPE_POST + SEPARTOR)) {
                resolvedTypeDataList.add(new ResolvedTypeData("post", Long.parseLong(type.split(SEPARTOR)[1])));
            }
            // 部门
            else if (type.startsWith(TYPE_DEPT + SEPARTOR)) {
                resolvedTypeDataList.add(new ResolvedTypeData("depte", Long.parseLong(type.split(SEPARTOR)[1])));
            }
            // 部门负责人
            else if (type.startsWith(TYPE_DEPT_MANAGER + SEPARTOR)) {
                List<String> deptManagersByDept = this.getDeptManagersByDept(type.split(SEPARTOR)[1]);
                deptManagersByDept.forEach(deptManager -> {
                    resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(deptManager)));
                });

            }
            // 部门分管领导
            else if (type.startsWith(TYPE_DEPT_LEADER + SEPARTOR)) {
                List<String> deptLeadersByDept = this.getDeptLeadersByDept(type.split(SEPARTOR)[1]);
                deptLeadersByDept.forEach(deptLeader -> {
                    resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(deptLeader)));
                });
            }
            // 发起人
            else if (type.equals(TYPE_START_USER)) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(execution.getProcessInstanceId()).singleResult();
                resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(processInstance.getStartUserId())));
            }
            // 发起人部门负责人
            else if (type.equals(TYPE_START_USER_DEPT_MANAGER)) {
                String startUserId = this.getStartUserId(execution);
                resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(startUserId)));
            }
            // 发起人部门分管领导
            else if (type.equals(TYPE_START_USER_DEPT_LEADER)) {
                String startUserId = this.getStartUserId(execution);
                resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(startUserId)));
            }
            // 上一步的处理人
            else if (type.equals(TYPE_PREV_ASSIGNEE)) {
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                prevAssigneeUserIds.forEach(prevAssigneeUserId -> {
                    resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(prevAssigneeUserId)));
                });
            }
            // 上一步处理人的部门负责人
            else if (type.equals(TYPE_PREV_ASSIGNEE_DEPT_MANAGER)) {
                List<String> deptManagerIds = new ArrayList<>();
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    deptManagerIds.addAll(this.getDeptManagersByUser(prevAssigneeUserId, execution.getTenantId()));
                }
                deptManagerIds.forEach(deptManagerId -> {
                    resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(deptManagerId)));
                });
            }
            // 上一步处理人的部门分管领导
            else if (type.equals(TYPE_PREV_ASSIGNEE_DEPT_LEADER)) {
                List<String> deptLeaderIds = new ArrayList<>();
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    deptLeaderIds.addAll(this.getDeptLeadersByUser(prevAssigneeUserId, execution.getTenantId()));
                }

                deptLeaderIds.forEach(deptManagerId -> {
                    resolvedTypeDataList.add(new ResolvedTypeData("user", Long.parseLong(deptManagerId)));
                });
            } else if (type.equals(TYPE_PREV_HANDLER_DEPT)) {
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                Set<Long> deptIds = new HashSet<>();
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    UserDTO userDTO = userClient.getUser(Long.parseLong(prevAssigneeUserId));
                    if (Objects.nonNull(userDTO.getOrganization())) {
                        deptIds.add(userDTO.getOrganization().getId());
                    }
                }

                deptIds.forEach(deptId -> resolvedTypeDataList.add(new ResolvedTypeData("user", deptId)));
            }
        }

        return resolvedTypeDataList;
    }

    /**
     * 解析带通知方式的，解析不带通知方式的
     *
     * @param execution
     * @param type
     *
     * @return
     */
    private List<String> resolveUsers(DelegateExecution execution, String type) {
        List<String> userIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            // 用户
            if (type.startsWith(TYPE_USERS + SEPARTOR)) {
                userIds.addAll(Arrays.asList(type.split(SEPARTOR)[1].split(",")));
            }
            // 岗位
            else if (type.startsWith(TYPE_POST + SEPARTOR)) {
                userIds.addAll(this.getUsersByPost(type.split(SEPARTOR)[1]));
            }
            // 部门
            else if (type.startsWith(TYPE_DEPT + SEPARTOR)) {
                userIds.addAll(this.getUsersByDept(type.split(SEPARTOR)[1]));
            }
            // 部门负责人
            else if (type.startsWith(TYPE_DEPT_MANAGER + SEPARTOR)) {
                userIds.addAll(this.getDeptManagersByDept(type.split(SEPARTOR)[1]));
            }
            // 部门分管领导
            else if (type.startsWith(TYPE_DEPT_LEADER + SEPARTOR)) {
                userIds.addAll(this.getDeptLeadersByDept(type.split(SEPARTOR)[1]));
            }
            // 发起人
            else if (type.equals(TYPE_START_USER)) {
                String startUserId = this.getStartUserId(execution);
                userIds.add(startUserId);
            }
            // 发起人部门负责人
            else if (type.equals(TYPE_START_USER_DEPT_MANAGER)) {
                String startUserId = this.getStartUserId(execution);
                userIds.addAll(this.getDeptManagersByUser(startUserId, execution.getTenantId()));
            }
            // 发起人部门分管领导
            else if (type.equals(TYPE_START_USER_DEPT_LEADER)) {
                String startUserId = this.getStartUserId(execution);
                userIds.addAll(this.getDeptLeadersByUser(startUserId, execution.getTenantId()));
            }
            // 上一步的处理人
            else if (type.equals(TYPE_PREV_ASSIGNEE)) {
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                userIds.addAll(prevAssigneeUserIds);
            }
            // 上一步处理人的部门负责人
            else if (type.equals(TYPE_PREV_ASSIGNEE_DEPT_MANAGER)) {
                List<String> deptManagerIds = new ArrayList<>();
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    deptManagerIds.addAll(this.getDeptManagersByUser(prevAssigneeUserId, execution.getTenantId()));
                }
                userIds.addAll(deptManagerIds);
            }
            // 上一步处理人的部门分管领导
            else if (type.equals(TYPE_PREV_ASSIGNEE_DEPT_LEADER)) {
                List<String> deptLeaderIds = new ArrayList<>();
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    deptLeaderIds.addAll(this.getDeptLeadersByUser(prevAssigneeUserId, execution.getTenantId()));
                }
                userIds.addAll(deptLeaderIds);
            } else if (type.equals(TYPE_TENANT_USER)) {
                userIds.add(this.getUsersByTenant(SessionContextHolder.getRequester().getTenant()));
                // 上一步处理人的部门
            } else if (type.equals(TYPE_PREV_HANDLER_DEPT)) {
                List<String> prevAssigneeUserIds = this.getPrevAssigneeUserIds(execution);
                Set<Long> deptIds = new HashSet<>();
                for (String prevAssigneeUserId : prevAssigneeUserIds) {
                    UserDTO userDTO = userClient.getUser(Long.parseLong(prevAssigneeUserId));
                    if (Objects.nonNull(userDTO.getOrganization())) {
                        deptIds.add(userDTO.getOrganization().getId());
                    }
                }

                for (Long deptId : deptIds) {
                    userIds.addAll(this.getUsersByDept(deptId.toString()));
                }

            } else if (type.startsWith(TYPE_ROLES + SEPARTOR)) {
                List<String> roleIds = Arrays.asList(type.split(SEPARTOR)[1].split(","));
                List<UserDTO> userDTOS = userClient.listUsersByRoles(roleIds.stream().map(v -> Long.parseLong(v))
                                                                             .collect(Collectors.toSet()));
                userIds.addAll(userDTOS.stream().map(u -> u.getId().toString()).collect(Collectors.toList()));
            }
        }
        return userIds;
    }

    private String getStartUserId(DelegateExecution execution) {

        ExecutionEntity executionEntity = (ExecutionEntity) execution;
        ExecutionEntity processInstance = executionEntity.getProcessInstance();
        if (processInstance != null) {
            return processInstance.getStartUserId();
        }
        return null;
    }

    private List<String> getPrevAssigneeUserIds(DelegateExecution execution) {
        List<String> assigneeIds = new ArrayList<>();
        List<PrevUserTaskDTO> list = helper.determinePrevUserTasks(execution.getCurrentFlowElement()
                                                                           .getId(), execution.getProcessDefinitionId());
        if (CollectionUtils.isNotEmpty(list)) {
            List<HistoricTaskInstance> htis = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(execution.getProcessInstanceId()).taskDefinitionKey(list.get(0).getTaskKey())
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            if (htis.size() > 0) {
                HistoricTaskInstance targetHti = htis.get(0);
                assigneeIds.add(targetHti.getAssignee());
                for (HistoricTaskInstance hti : htis) {
                    if (!hti.getId().equals(targetHti.getId()) &&
                        Math.abs(hti.getStartTime().getTime() - targetHti.getStartTime().getTime()) < 1000 &&
                        !assigneeIds.contains(hti.getAssignee())) {
                        assigneeIds.add(hti.getAssignee());
                    }
                }
            }
        }
        return assigneeIds;
    }

    private List<String> getUsersByPost(String postId) {
        List<UserDTO> userDTOS = userClient.positionUsers(Long.parseLong(postId));
        return userDTOS.stream().map(userDTO -> String.valueOf(userDTO.getId())).collect(Collectors.toList());
    }

    public String getUsersByTenant(Integer tenant) {
        UserDTO user = this.userClient.getTenantAdminByTenant(tenant);
        return String.valueOf(user.getId());
    }

    private List<String> getUsersByDept(String deptId) {
        List<UserDTO> userDTOS = userClient.organizationUsers(Long.parseLong(deptId));
        return userDTOS.stream().map(userDTO -> String.valueOf(userDTO.getId())).collect(Collectors.toList());
    }

    private List<String> getDeptManagersByDept(String deptId) {
        return null;
    }

    private List<String> getDeptLeadersByDept(String deptId) {
        return null;
    }

    private List<String> getDeptManagersByUser(String userId, String projectId) {
        return null;
    }

    private List<String> getDeptLeadersByUser(String userId, String projectId) {
        return null;
    }

}
