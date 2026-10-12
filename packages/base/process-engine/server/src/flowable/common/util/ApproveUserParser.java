package com.guhecloud.cube.base.flow.flowable.common.util;

import com.guhecloud.cube.base.flow.flowable.branch.SubmitterService;
import com.guhecloud.cube.base.flow.flowable.common.constant.ProcessConstant;
import com.guhecloud.cube.base.flow.flowable.common.resovler.PreviousAssigneeResolver;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.PostInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.RoleInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.UserInfo;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 节点审批人员处理
 * @date 2023/12/12 10:07
 */
@Slf4j
@Component
public class ApproveUserParser {
    @Resource
    private UserClient userClient;
    @Resource
    private UserTenantClient userTenantClient;
    @Resource
    private SubmitterService submitterService;
    @Resource
    private PreviousAssigneeResolver previousAssigneeResolver;

    /**
     * 查询上一节点审批人
     *
     * @param execution 执行器
     *
     * @return 上一节点最近一次完成任务的审批人
     */
    public List<String> getPrevAssigneeUser(DelegateExecution execution, Properties props, Long startUserId) {
        return previousAssigneeResolver.resolve(execution.getCurrentActivityId(), execution);
    }

    /**
     * 查询发起人自己
     *
     * @param execution 执行器
     *
     * @return 审批人员
     */
    public List<String> getSelfUser(DelegateExecution execution, Properties props, Long startUserId) {
        List<String> assigneeList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(startUserId)) {
            assigneeList.add(startUserId.toString());
        } else {
            assigneeList.add(SecurityContextHolder.getPrincipal().getId().toString());
        }
        return assigneeList;
    }

    /**
     * 获取表单内审批人
     *
     * @param props     节点属性
     * @param execution 执行器
     *
     * @return 审批人员
     */
    public List<String> getFormUser(DelegateExecution execution, Properties props, Long startUserId)  {
        String formUser = props.getFormUser();
        List<String> assigneeList = new ArrayList<>();
        if (ProcessConstant.PRE_SHOW.equals(execution.getProcessInstanceId())) {
            return assigneeList;
        }
        if (ObjectUtils.isNotEmpty(formUser)) {
            // todo 获取表单人员信息时报错(lazy loading outside command context) 临时使用调用两次来解决懒加载问题
            List<Map<String, Object>> list = new ArrayList<>();
            try {
                Object variable = execution.getVariable(formUser);
                if (variable instanceof List) {
                    list = (List<Map<String, Object>>) variable;
                } else {
                    //根据电话号码找到用户
                    UserDTO userDTO = userClient.getUserByMobile(variable.toString());
                    assigneeList.add(Objects.nonNull(userDTO) ? userDTO.getId().toString() : variable.toString());
                }
            } catch (Exception e) {
                list = execution.getVariable(formUser, List.class);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                for (Map<String, Object> assigneeUser : list) {
                    assigneeList.add(assigneeUser.get("id").toString());
                }
            }
        }
        return assigneeList;
    }

    /**
     * 获取岗位审批人
     *
     * @param execution 执行器
     * @param props     节点属性
     *
     * @return 审批人员
     */
    public List<String> getAppointmentUser(DelegateExecution execution, Properties props, Long startUserId)  {
        Long rootId = SecurityContextHolder.getPrincipal().getId();
        List<String> assigneeList = new ArrayList<>();
        if (!ProcessConstant.PRE_SHOW.equals(execution.getProcessInstanceId())) {
            rootId = startUserId;
        }
        UserDTO user = userClient.getUser(rootId);
        List<PostInfo> postInfos = getAssignedInfos(props.getAssignedUser(), PostInfo.class);
        if (ObjectUtils.isNotEmpty(postInfos)) {
            // 获取发起人信息
            Set<Long> userIds = userTenantClient.getDeptListByPost(user.getOrganization().getId(), postInfos.stream()
                    .map(postInfo -> Long.parseLong(postInfo.getId())).collect(Collectors.toSet()));
            assigneeList.addAll(userIds.stream().map(Object::toString).collect(Collectors.toList()));
        }
        return assigneeList;
    }

    /**
     * 获取岗位审批人（不受发起人限制）
     *
     * @param execution 执行器
     * @param props     节点属性
     *
     * @return 审批人员
     */
    public List<String> getAppointmentSingletUser(DelegateExecution execution, Properties props, Long startUserId)  {
        List<String> assigneeList = new ArrayList<>();
        List<PostInfo> postInfos = getAssignedInfos(props.getAssignedUser(), PostInfo.class);
        if (ObjectUtils.isNotEmpty(postInfos)) {
            List<UserDTO> userListByAppointmentIds = userClient.getUserListByAppointmentIds(postInfos.stream().map(PostInfo::getId)
                                                                                                    .collect(Collectors.joining(",")));
            if (CollectionUtils.isNotEmpty(userListByAppointmentIds)) {
                assigneeList.addAll(userListByAppointmentIds.stream().map(UserDTO::getId).map(Object::toString)
                                            .collect(Collectors.toList()));
            }
        }
        return assigneeList;
    }

    /**
     * 获取职位审批人
     *
     * @param execution 执行器
     * @param props     节点属性
     *
     * @return 审批人员
     */
    public List<String> getPostUser(DelegateExecution execution, Properties props, Long startUserId)  {
        // 审批人是岗位；查询哪些用户的管理部门涉及到发起人所属部门
        List<String> assigneeList = new ArrayList<>();
        Long rootId = SecurityContextHolder.getPrincipal().getId();
        if (!execution.getProcessInstanceId().equals(ProcessConstant.PRE_SHOW)) {
            rootId = startUserId;
        }
        UserDTO user = userClient.getUser(rootId);
        List<PostInfo> postInfos = getAssignedInfos(props.getAssignedUser(), PostInfo.class);
        if (CollectionUtils.isNotEmpty(postInfos)) {
            // 获取发起人信息
            Set<Long> userIds = userTenantClient.getManagedDeptList(user.getOrganization().getId(), postInfos.stream()
                    .map(postInfo -> Long.parseLong(postInfo.getId())).collect(Collectors.toSet()));
            assigneeList.addAll(userIds.stream().map(Object::toString).collect(Collectors.toList()));
        }
        return assigneeList;
    }

    /**
     * 获取职位审批人（不限定发起人所在部门）
     *
     * @param execution 执行器
     * @param props     节点属性
     *
     * @return 审批人员
     */
    public List<String> getPostSingleUser(DelegateExecution execution, Properties props, Long startUserId)  {
        // 审批人是指定岗位
        List<String> assigneeList = new ArrayList<>();

        List<PostInfo> postInfos = getAssignedInfos(props.getAssignedUser(), PostInfo.class);
        if (CollectionUtils.isNotEmpty(postInfos)) {
            assigneeList = userClient.listUserByOrgAndPost(new ArrayList<>(), postInfos.stream().map(postInfo ->
                                                                                                             Long.parseLong(postInfo.getId()))
                            .distinct().collect(Collectors.toList()))
                    .stream().map(String::valueOf).collect(Collectors.toList());

        }
        return assigneeList;
    }

    private <T> List<T> getAssignedInfos(List<Object> assignedUser, Class<T> clazz) {
        if (CollectionUtils.isEmpty(assignedUser)) {
            return new ArrayList<>();
        }
        return assignedUser.stream()
                .map(postInfo -> JsonUtils.deserialize(JsonUtils.serialize(postInfo), clazz)).collect(Collectors.toList());
    }

    /**
     * 获取角色人员
     *
     * @param props 节点属性
     *
     * @return 审批人员
     */
    public List<String> getRoleUser(DelegateExecution execution, Properties props, Long startUserId)  {
        List<String> assigneeList = new ArrayList<>();
        List<RoleInfo> roleInfos = getAssignedInfos(props.getAssignedUser(), RoleInfo.class);
        List<UserDTO> userDTOS = userClient.listUsersByRoles(roleInfos.stream().map(RoleInfo::getId).map(Long::valueOf)
                                                                     .collect(Collectors.toSet()));
        if (CollectionUtils.isNotEmpty(userDTOS)) {
            assigneeList.addAll(
                    userDTOS.stream().map(userDTO -> userDTO.getId().toString()).collect(Collectors.toSet())
                               );
        }
        return assigneeList;
    }

    /**
     * 查询指定审批人
     *
     * @param props 节点属性信息
     *
     * @return 审批人员
     */
    public List<String> getAssignUser(DelegateExecution execution, Properties props, Long startUserId)  {
        List<String> assigneeList = new ArrayList<>();
        List<UserInfo> assignedUser = getAssignedInfos(props.getAssignedUser(), UserInfo.class);
        if (ObjectUtils.isNotEmpty(assignedUser)) {
            assignedUser.forEach(user -> assigneeList.add(user.getId()));
        }
        return assigneeList;
    }

    /**
     * 查询部门+岗位审批人员
     *
     * @param props 节点属性
     *
     * @return 审批人员
     */
    public List<String> getDepPostUser(DelegateExecution execution, Properties props, Long startUserId)  {
        Map<String, Object> deptPost = props.getDeptPost();
        Map<String, Object> depMap = (Map<String, Object>) deptPost.get("dept");
        String depId = (String) depMap.get("id");
        log.info("部门id:{}", depId);
        List<Map<String, Object>> postList = (List<Map<String, Object>>) deptPost.get("post");
        Set<Long> postIds = postList.stream().map(map -> Long.parseLong((String) map.get("id"))).collect(Collectors.toSet());
        log.info("岗位id:{}", JsonUtils.serialize(postIds));
        if (StringUtils.isNotBlank(depId) && ObjectUtils.isNotEmpty(postIds)) {
            Set<Long> longs = userTenantClient.getDeptListByPost(Long.parseLong(depId), postIds);
            if (ObjectUtils.isNotEmpty(longs)) {
                return longs.stream().map(Object::toString).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 查询部门+职位审批人员
     *
     * @param props 节点属性
     *
     * @return 审批人员
     */
    public List<String> getDepPositionUser(DelegateExecution execution, Properties props, Long startUserId)  {
        Map<String, Object> deptPost = props.getDeptPosition();
        Map<String, Object> depMap = (Map<String, Object>) deptPost.get("dept");
        String depId = (String) depMap.get("id");
        log.info("部门id:{}", depId);
        List<Map<String, Object>> positionList = (List<Map<String, Object>>) deptPost.get("position");
        Set<Long> positionIds = positionList.stream().map(map -> Long.parseLong((String) map.get("id"))).collect(Collectors.toSet());
        log.info("职位id:{}", JsonUtils.serialize(positionIds));
        if (StringUtils.isNotBlank(depId) && ObjectUtils.isNotEmpty(positionIds)) {
            Set<Long> longs = userTenantClient.getManagedDeptListByAppointment(Long.parseLong(depId), positionIds);
            if (ObjectUtils.isNotEmpty(longs)) {
                return longs.stream().map(Object::toString).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
