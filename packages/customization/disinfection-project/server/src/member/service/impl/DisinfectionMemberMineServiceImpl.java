package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberGroupRoleEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupTotalEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MyMemberParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberMineService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MyMemberResult;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisinfectionMemberMineServiceImpl implements DisinfectionMemberMineService {
    private static final String DEFAULT_GROUP_NAME_PREFIX = "我的家园";

    private final DisinfectionMemberGroupService disinfectionMemberGroupService;
    private final DisinfectionMemberGroupUserService disinfectionMemberGroupUserService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserClient userClient;
    private final FileClient fileClient;

    @Override
    public List<MemberGroupResult> getUserGroups(String keyword) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        // 获取用户加入的组
        List<Long> groupIds = disinfectionMemberGroupUserService.getGroupIdsByUserId(userId);
        if (ListUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        //根据名称获取组id
        if (StringUtils.isNotBlank(keyword)) {
            List<Long> groupIdsByName = disinfectionMemberGroupService.getGroupIdsByName(keyword);
            HashSet<Long> groupIdSet = new HashSet<>(groupIds);
            groupIdSet.retainAll(new HashSet<>(groupIdsByName));
            groupIds = new ArrayList<>(groupIdSet);
        }
        //查询组信息
        List<MemberGroup> memberGroups = disinfectionMemberGroupService.listByIds(groupIds);
        return ObjectUtils.copying(memberGroups, MemberGroupResult.class);

    }

    @Override
    public List<MyMemberResult> getMyMembers(MyMemberParam param) {
        String keyword = param.getKeyword();
        boolean filterUser = StringUtils.isNotBlank(keyword);
        Long userId = SecurityContextHolder.getPrincipal().getId();
        var selectedGroup = userSelectGroup(userId);
        if (selectedGroup == null || selectedGroup.getId() == null) {
            return Collections.emptyList();
        }
        // 查询当前选择组下的成员
        var groupUsers = disinfectionMemberGroupUserService.lambdaQuery()
                .select(MemberGroupUser::getUserId, MemberGroupUser::getRole)
                .eq(MemberGroupUser::getGroupId, selectedGroup.getId())
                .list();
        if (ListUtils.isEmpty(groupUsers)) {
            return Collections.emptyList();
        }
        Set<Long> userIdSet = groupUsers.
                stream()
                .map(MemberGroupUser::getUserId)
                .collect(Collectors.toSet());
        Map<Long, String> userIdToRole = groupUsers.stream()
                .collect(Collectors.toMap(MemberGroupUser::getUserId,
                                          groupUser -> StringUtils.isNotBlank(groupUser.getRole()) ? groupUser.getRole() : MemberGroupRoleEnum.MEMBER.name(),
                                          (oldValue, newValue) -> oldValue));
        if (filterUser) {
            //查询满足条件的用户
            List<UserDTO> users = userClient.listAllUsers(UserQueryParam.builder().keyword(keyword).build());
            List<Long> containUserIds = ListUtils.convertItem(users, UserDTO::getId);
            userIdSet = userIdSet.stream().filter(containUserIds::contains).collect(Collectors.toSet());
        }
        if (ListUtils.isEmpty(userIdSet)) {
            return Collections.emptyList();
        }
        //查询成员详细信息
        List<UserDTO> users = userClient.listUserByIds(userIdSet);
        List<MyMemberResult> myMemberResults = ObjectUtils.copying(users, MyMemberResult.class);
        //设置用户的头像
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(new ArrayList<>(userIdSet));
        Map<Long, List<FileVO>> avatarMap = fileClient.getByBusinessKeys(businessKeysParam);
        myMemberResults.forEach(myMemberResult -> {
            myMemberResult.setRole(userIdToRole.getOrDefault(myMemberResult.getId(), MemberGroupRoleEnum.MEMBER.name()));
            List<FileVO> fileVOS = avatarMap.get(myMemberResult.getId());
            if (ListUtils.nonEmpty(fileVOS)) {
                myMemberResult.setAvatar(fileVOS.get(0));
            }
        });
        return myMemberResults;
    }

    /**
     * 获取用户选择的组(没有就用自己默认的组)
     * （没有默认的组会创建一个组，然后设置选择的组）
     *
     * @param userId 用户id（空就会取当前登陆人）
     *
     * @return 选择的组id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberGroupResult userSelectGroup(Long userId) {
        if (userId == null) {
            userId = SecurityContextHolder.getPrincipal().getId();
        }
        // 获取选择的组
        var memberGroupUser = disinfectionMemberGroupUserService.getOne(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                                .select(MemberGroupUser::getGroupId)
                                                                                .eq(MemberGroupUser::getUserId, userId)
                                                                                .eq(MemberGroupUser::getSelectGroup, true).last("limit 1"));
        Long groupId;
        if (memberGroupUser == null) {
            // 为空就校验并获取自己默认组
            var manageGroupId = this.userDefaultGroup(userId, SecurityContextHolder.getPrincipal().getAccount());
            // 将默认的组设置为选择的组
            this.setUserSelectGroup(userId, manageGroupId);
            groupId = manageGroupId;
        } else {
            groupId = memberGroupUser.getGroupId();
        }

        var memberGroupPo = disinfectionMemberGroupService.getById(groupId);
        var memberGroupResult = new MemberGroupResult();
        memberGroupResult.setId(memberGroupPo.getId());
        memberGroupResult.setGroupName(memberGroupPo.getGroupName());
        memberGroupResult.setMemberUserId(memberGroupPo.getMemberUserId());
        memberGroupResult.setUserCount(memberGroupPo.getUserCount());
        memberGroupResult.setCreateTime(memberGroupPo.getCreateTime());
        var bool = SecurityContextHolder.getPrincipal() != null && SecurityContextHolder.getPrincipal().getId() != null &&
                   SecurityContextHolder
                           .getPrincipal().getId().equals(memberGroupPo.getMemberUserId());
        memberGroupResult.setMemberUserBool(bool);
        return memberGroupResult;
    }

    /**
     * 设置用户的选择的组
     *
     * @param userId        用户id
     * @param selectGroupId 选择的组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setUserSelectGroup(Long userId, Long selectGroupId) {
        // 先取消用户所有的
        disinfectionMemberGroupUserService.update(Wrappers.<MemberGroupUser>lambdaUpdate().set(MemberGroupUser::getSelectGroup, false)
                                                          .eq(MemberGroupUser::getUserId, userId)
                                                          .eq(MemberGroupUser::getSelectGroup, true));
        // 再设置
        disinfectionMemberGroupUserService.update(Wrappers.<MemberGroupUser>lambdaUpdate().set(MemberGroupUser::getSelectGroup, true)
                                                          .eq(MemberGroupUser::getUserId, userId)
                                                          .eq(MemberGroupUser::getGroupId, selectGroupId));
        return true;
    }

    /**
     * 人员默认组(无就添加)
     *
     * @param userId  人员id
     * @param account 人员账号
     */
    @Override
    public Long userDefaultGroup(Long userId, String account) {
        var userDto = userClient.getUser(userId);
        var defaultGroupName = buildDefaultGroupName(StringUtils.isNotBlank(userDto.getMobile()) ? userDto.getMobile() : account);
        var userGroup = disinfectionMemberGroupService.getOne(Wrappers.<MemberGroup>lambdaQuery().eq(MemberGroup::getMemberUserId, userId)
                                                                      .last("limit 1")
                                                                      .orderByDesc(MemberGroup::getCreateTime));
        if (userGroup == null) {
            var param = new MemberGroupCommandParam();
            param.setId(IdWorker.getId());
            param.setGroupName(defaultGroupName);
            param.setMemberUserId(userId);
            param.setUserCount(1);
            disinfectionMemberGroupService.addMemberGroup(param);
            return param.getId();
        } else {
            return userGroup.getId();
        }
    }

    private String buildDefaultGroupName(String mobile) {
        if (StringUtils.isBlank(mobile) || mobile.length() <= 4) {
            return DEFAULT_GROUP_NAME_PREFIX + (StringUtils.isBlank(mobile) ? "" : mobile);
        }
        return DEFAULT_GROUP_NAME_PREFIX + mobile.substring(mobile.length() - 4);
    }

    /**
     * 退出组
     *
     * @param userId  用户id（空就获取当前登陆人)
     * @param groupId 要推出的组
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean exitGroup(Long userId, Long groupId) {
        if (userId == null) {
            userId = SecurityContextHolder.getPrincipal().getId();
        }
        // 获取这个用户组信息
        var memberGroupUser = disinfectionMemberGroupUserService.getOne(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                                .select(MemberGroupUser::getId)
                                                                                .eq(MemberGroupUser::getUserId, userId)
                                                                                .eq(MemberGroupUser::getGroupId, groupId).last("limit 1"));
        if (memberGroupUser != null) {
            // 如果有就退出
            DatabaseContextHolder.softDelete(disinfectionMemberGroupUserService, memberGroupUser.getId());
            /* 获取组人数 */
            var groupUserCount = disinfectionMemberGroupUserService.count(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                                  .eq(MemberGroupUser::getGroupId, groupId));
            if (groupUserCount <= 0) {
                //* 组没有人时 就删除组
                DatabaseContextHolder.softDelete(disinfectionMemberGroupService, groupId);
            } else {
                // 更新总数量
                DisinfectionMemberGroupTotalEvent event = new DisinfectionMemberGroupTotalEvent(this, groupId, true, false);
                applicationEventPublisher.publishEvent(event);
            }
        }

        return true;
    }
}
