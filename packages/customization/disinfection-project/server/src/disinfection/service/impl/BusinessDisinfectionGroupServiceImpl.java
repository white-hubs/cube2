package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.GroupPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.GroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.GroupUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.GroupInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.GroupUserPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionGroupServiceImpl implements BusinessDisinfectionGroupService {
    private final static String ALL_PROJECTS = "-";
    private final DisinfectionGroupService groupService;
    private final DisinfectionGroupUserService groupUserService;
    private final DisinfectionTaskService taskService;
    private final DisinfectionProjectService disinfectionProjectService;
    private final DisinfectionUserHelper disinfectionUserHelper;

    private static DisinfectionGroup buildGroup(long projectId, GroupUpsertParam.GroupParam param) {
        DisinfectionGroup group = new DisinfectionGroup();
        long groupId = IdWorker.getId();
        group.setId(groupId);
        group.setGroupName(param.getGroupName());
        group.setProjectId(projectId);
        return group;
    }

    @Override
    public List<GroupInfoVO> listAll(GroupQueryParam param) {
        Long groupId = param.getGroupId();
        String projectIdStr = param.getProjectId();
        Long projectId = null;
        if (!ALL_PROJECTS.equals(projectIdStr) && StringUtils.isNotBlank(projectIdStr)) {
            projectId = Long.parseLong(projectIdStr);
        }
        String keyword = param.getKeyword();
        LambdaQueryChainWrapper<DisinfectionGroup> groupLambdaQueryChainWrapper = buildQueryWrapper(projectId, groupId);
        List<DisinfectionGroup> list = groupLambdaQueryChainWrapper.list();
        Map<Long, List<Long>> groupUserIdsMap = groupUserService.getGroupUserIdsMap(ListUtils.convertItem(list, DisinfectionGroup::getId));
        List<GroupInfoVO> groupInfoList = ObjectUtils.copying(list, GroupInfoVO.class);
        buildGroupUserExtendInfo(param, keyword, groupInfoList, groupUserIdsMap);

        return groupInfoList;
    }

    private void buildGroupUserExtendInfo(GroupQueryParam param, String keyword, List<GroupInfoVO> groupInfoList,
                                          Map<Long, List<Long>> groupUserIdsMap
                                         ) {
        if (ListUtils.isEmpty(groupInfoList)) {
            return;
        }
        List<Long> containsUserIds = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            List<UserDTO> userDTOS = disinfectionUserHelper.listUsers(keyword);
            containsUserIds.addAll(ListUtils.convertItem(userDTOS, UserDTO::getId));
        }
        //需要的用户id
        Set<Long> needUserIds = new HashSet<>();
        //获取每组的用户id以及需要的用户id
        groupInfoList.forEach(groupInfo -> {
            List<Long> groupUserIds = groupUserIdsMap.getOrDefault(groupInfo.getId(), Collections.emptyList());
            if (StringUtils.isNotBlank(keyword)) {
                //进行用户过滤
                groupUserIds = groupUserIds.stream().filter(containsUserIds::contains).collect(Collectors.toList());
            }
            if (Objects.nonNull(param.getDeptId())) {
                List<UserDTO> deptUsers = disinfectionUserHelper.getUserByDeptId(param.getDeptId());
                List<Long> deptUserIds = ListUtils.convertItem(deptUsers, UserDTO::getId);
                groupUserIds = groupUserIds.stream().filter(deptUserIds::contains).collect(Collectors.toList());
            }
            if (Objects.nonNull(param.getPostId())) {
                List<Long> postUserIds = disinfectionUserHelper.getUserByPostId(param.getPostId())
                        .stream().map(UserDTO::getId).collect(Collectors.toList());
                groupUserIds = groupUserIds.stream().filter(postUserIds::contains).collect(Collectors.toList());
            }
            groupInfo.setGroupUserIds(groupUserIds);
            needUserIds.addAll(groupUserIds);

        });
        Map<Long, UserDTO> userMap = disinfectionUserHelper.getUserMap(new ArrayList<>(needUserIds));
        groupInfoList.forEach(groupInfo -> {
            Collection<Long> groupUserIds = groupInfo.getGroupUserIds();
            if (ListUtils.isEmpty(groupUserIds)) {
                return;
            }
            List<UserDTO> userInfos = groupUserIds.stream().map(userMap::get).filter(Objects::nonNull).collect(Collectors.toList());
            groupInfo.setUsers(userInfos);
            //设置项目成员个数
            groupInfo.setCount((long) groupUserIds.size());
        });

    }

    @Transactional
    @Override
    public boolean insert(GroupUpsertParam params) {
        List<GroupUpsertParam.GroupParam> groups = params.getGroups();
        Assert.isParameterError(ListUtils.isEmpty(groups), "分组不能为空");
        for (GroupUpsertParam.GroupParam groupParam : groups) {
            List<Long> userIds = groupParam.getUserIds();
            Long projectId = params.getProjectId();
            Assert.isParameterError(ListUtils.isEmpty(userIds), "请选择小组成员！");
            DisinfectionGroup group = buildGroup(projectId, groupParam);
            groupService.save(group);
            saveGroupUsers(group.getId(), userIds);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean delete(long groupId) {
        //查询小组是否存在未完成的任务，如果存在未完成的任务，则无法删除
        boolean exist = taskService.existNotCompletedTasksByGroupId(groupId);
        Assert.isNotAcceptable(exist, "删除的小组存在未完成任务，无法删除！");
        //删除小组成员
        groupUserService.removeByGroupId(groupId);
        return groupService.removeById(groupId);
    }

    @Transactional
    @Override
    public boolean update(GroupUpsertParam groupUpsertParam) {
        Long projectId = groupUpsertParam.getProjectId();
        List<GroupUpsertParam.GroupParam> groups = groupUpsertParam.getGroups();
        Assert.isParameterError(ListUtils.isEmpty(groups), "请选择要更新的小组！");
        //查询项目下以存在的小组
        List<DisinfectionGroup> existGroups = groupService.listByProjectId(projectId);
        Map<Long, DisinfectionGroup> disinfectionGroupMap = MapUtils.listToMap(existGroups, DisinfectionGroup::getId, disinfectionGroup -> disinfectionGroup);
        List<Long> existGroupIds = ListUtils.convertItem(existGroups, DisinfectionGroup::getId);
        handleWaitDeleteGroups(groups, existGroupIds);
        handleWaitUpdateGroups(groups, disinfectionGroupMap);
        handleWaitInsertGroups(groups, projectId);
        return true;

    }

    private void handleWaitDeleteGroups(List<GroupUpsertParam.GroupParam> groups, List<Long> existGroupIds) {
        //获取待删除的小组id
        List<Long> nowGroupIds = groups.stream()
                .map(GroupUpsertParam.GroupParam::getGroupId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> waitDelGroupIds = existGroupIds.stream().filter(id -> !nowGroupIds.contains(id)).collect(Collectors.toList());
        waitDelGroupIds.forEach(this::delete);

    }

    private void handleWaitUpdateGroups(List<GroupUpsertParam.GroupParam> groups, Map<Long, DisinfectionGroup> disinfectionGroupMap) {
        //获取待更新的小组
        List<DisinfectionGroup> waitUpdateGroups = groups.stream()
                .filter(groupParam -> Objects.nonNull(groupParam.getGroupId()))
                .map(groupParam -> {
                    DisinfectionGroup group = disinfectionGroupMap.get(groupParam.getGroupId());
                    if (Objects.isNull(group)) {
                        return null;
                    }
                    group.setGroupName(groupParam.getGroupName());
                    return group;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //修改小组
        groupService.updateBatchById(waitUpdateGroups);
        //修改小组的成员信息
        List<GroupUpsertParam.GroupParam> groupParams = groups.stream()
                .filter(groupParam -> Objects.nonNull(groupParam.getGroupId()))
                .collect(Collectors.toList());
        groupParams.forEach(group -> updateGroupUsers(group.getGroupId(), group.getUserIds()));
    }

    private void handleWaitInsertGroups(List<GroupUpsertParam.GroupParam> groups, Long projectId) {
        //获取待新增的小组
        List<DisinfectionGroup> waitInsertGroups = groups.stream()
                .filter(groupParam -> Objects.isNull(groupParam.getGroupId()))
                .map(groupParam -> {
                    DisinfectionGroup group = new DisinfectionGroup();
                    group.setGroupName(groupParam.getGroupName());
                    group.setProjectId(projectId);
                    return group;
                })
                .collect(Collectors.toList());
        groupService.saveBatch(waitInsertGroups);
        //获取待新增的小组成员
        List<DisinfectionGroupUser> waitInsertGroupUsers = groups.stream()
                .filter(group -> Objects.isNull(group.getGroupId()))
                .flatMap(groupParam -> {
                    List<DisinfectionGroupUser> groupUsers = groupParam.getUserIds().stream().map(userId -> {
                        DisinfectionGroupUser disinfectionGroupUser = new DisinfectionGroupUser();
                        disinfectionGroupUser.setGroupId(groupParam.getGroupId());
                        disinfectionGroupUser.setUserId(userId);
                        return disinfectionGroupUser;
                    }).collect(Collectors.toList());
                    return groupUsers.stream();
                }).collect(Collectors.toList());
        Assert.isParameterError(ListUtils.isEmpty(waitInsertGroupUsers), "小组成员不能为空");
        groupUserService.saveBatch(waitInsertGroupUsers);
    }

    private void updateGroupUsers(long groupId, List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return;
        }
        //查询小组成员
        List<Long> existUserIds = groupUserService.getGroupUserIds(groupId);
        //获取待删除的小组成员
        Set<Long> waitDeleteUserIds = existUserIds.stream().filter(userId -> !userIds.contains(userId)).collect(Collectors.toSet());
        //删除小组成员
        groupUserService.removeGroupUsers(groupId, waitDeleteUserIds);
        //获取待新增的小组成员
        Set<Long> waitInsertUserIds = userIds.stream().filter(userId -> !existUserIds.contains(userId)).collect(Collectors.toSet());
        //保存新增的小组成员
        groupUserService.saveUsers(groupId, waitInsertUserIds);
        //查询小组成员个数
        List<Long> groupUserIds = groupUserService.getGroupUserIds(groupId);
        Assert.isParameterError(ListUtils.isEmpty(groupUserIds), "小组成员不能为空");
    }

    @Override
    public List<Long> groupMembers(long groupId) {
        return groupUserService.getGroupUserIds(groupId);
    }

    @Override
    public boolean addMember(long groupId, long userId) {
        return groupUserService.addUser(groupId, userId);
    }

    @Override
    public boolean deleteMember(long groupId, long userId) {
        return groupUserService.remove(new LambdaQueryWrapper<DisinfectionGroupUser>().eq(DisinfectionGroupUser::getGroupId, groupId)
                                               .eq(DisinfectionGroupUser::getUserId, userId));
    }

    @Override
    public Pagination<GroupUserPageVO> pageGroup(String projectIdStr, GroupPageParam param) {
        Long projectId = null;
        if (!ALL_PROJECTS.equals(projectIdStr)) {
            projectId = Long.parseLong(projectIdStr);
        }

        Page<DisinfectionGroup> groupPage = getGroupPageInfo(param, projectId);
        Pagination<GroupUserPageVO> voPagination = PagingUtils.convert(groupPage, GroupUserPageVO.class);
        List<GroupUserPageVO> records = voPagination.getRecords();
        //查询项目信息
        Map<Long, String> projectMap = disinfectionProjectService.getProjectNamesMap(ListUtils.convertItem(records, GroupUserPageVO::getProjectId));
        //查询小组成员信息
        Map<Long, List<Long>> groupUserIdsMap = groupUserService.getGroupUserIdsMap(ListUtils.convertItem(records, GroupUserPageVO::getId));

        records.forEach(buildGroupUserInfo(groupUserIdsMap, projectMap));

        return voPagination;
    }

    @Override
    public List<UserDTO.Role> roles(Long groupId) {
        //获取小组小的角色列表
        if (Objects.isNull(groupId)) {
            return Collections.emptyList();
        }
        List<Long> userIds = groupUserService.getGroupUserIds(groupId);

        return disinfectionUserHelper.getUsersRoles(userIds);
    }

    @Transactional
    @Override
    public boolean singleUpdate(GroupUpsertParam.GroupParam groupParam) {
        Long groupId = groupParam.getGroupId();
        Assert.parameterIsNull(groupId, "小组ID不能为空");
        DisinfectionGroup group = groupService.getById(groupId);
        Assert.parameterIsNull(group, "小组不存在");
        group.setGroupName(groupParam.getGroupName());
        boolean updateGroup = groupService.updateById(group);
        //更新小组下的成员
        List<Long> userIds = groupParam.getUserIds();
        Assert.isParameterError(ListUtils.isEmpty(userIds), "小组成员不能为空");
        updateGroupUsers(groupId, userIds);
        return updateGroup;
    }

    private Page<DisinfectionGroup> getGroupPageInfo(GroupPageParam param, Long projectId) {
        return groupService
                .lambdaQuery()
                .eq(Objects.nonNull(projectId), DisinfectionGroup::getProjectId, projectId)
                .page(PagingUtils.convert(param));
    }

    private Consumer<GroupUserPageVO> buildGroupUserInfo(Map<Long, List<Long>> groupUserIdsMap, Map<Long, String> projectMap) {
        return vo -> {
            //设置小组成员数
            List<Long> userList = groupUserIdsMap.getOrDefault(vo.getId(), Collections.emptyList());
            vo.setCount((long) userList.size());
            //设置小组成员id
            vo.setUserIds(userList.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(",")));
            //设置小组成员信息
            vo.setGroupUsers(disinfectionUserHelper.getUsers(userList));
            //设置项目信息
            vo.setProjectName(projectMap.getOrDefault(vo.getProjectId(), ""));
        };
    }

    private LambdaQueryChainWrapper<DisinfectionGroup> buildQueryWrapper(Long projectId, Long groupId) {
        LambdaQueryChainWrapper<DisinfectionGroup> queryChainWrapper = groupService.lambdaQuery()
                .eq(Objects.nonNull(projectId), DisinfectionGroup::getProjectId, projectId);
        queryChainWrapper.eq(Objects.nonNull(groupId), DisinfectionGroup::getId, groupId);
        return queryChainWrapper;
    }

    private void saveGroupUsers(long groupId, List<Long> userIds) {
        Assert.isParameterError(ListUtils.isEmpty(userIds), "小组成员不能为空");
        Set<DisinfectionGroupUser> groupUsers = userIds
                .stream()
                .map(userId -> {
                    DisinfectionGroupUser groupUser = new DisinfectionGroupUser();
                    groupUser.setGroupId(groupId);
                    groupUser.setUserId(userId);
                    return groupUser;
                })
                .collect(Collectors.toSet());
        groupUserService.saveBatch(groupUsers);
    }
}
