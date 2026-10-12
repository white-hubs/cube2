package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupUserMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberDeviceAutomatic;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionDeviceAutomaticService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberUserResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberUserServiceImpl implements DisinfectionMemberUserService {
    private final UserClient userClient;
    private final DisinfectionMemberGroupUserMapper disinfectionMemberGroupUserMapper;
    private final DisinfectionMemberGroupMapper disinfectionMemberGroupMapper;
    private final DisinfectionMemberGroupService disinfectionMemberGroupService;
    private final DisinfectionMemberGroupUserService disinfectionMemberGroupUserService;
    private final DisinfectionMemberGroupDeviceService disinfectionMemberGroupDeviceService;
    private final DisinfectionDeviceAutomaticService disinfectionDeviceAutomaticService;

    /**
     * 获取用户列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MemberUserResult> getMemberUserPage(MemberUserQueryParam param) {
        var groupUserList = disinfectionMemberGroupUserMapper.selectList(Wrappers.<MemberGroupUser>lambdaQuery());
        if (CollectionUtils.isEmpty(groupUserList)) {
            return Pagination.empty();
        }
        var userSet = groupUserList.stream().map(MemberGroupUser::getUserId).collect(Collectors.toSet());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPageSize(param.getPageSize());
        userQueryParam.setPageNumber(param.getPageNumber());
        userQueryParam.setEnabled(true);
        userQueryParam.setKeyword(param.getKeyword());
        userQueryParam.setCreateTime(param.getCreateTime());
        userQueryParam.setIds(new ArrayList<>(userSet));
        Pagination<UserDTO> userPage = userClient.postByPage(userQueryParam);
        if (userPage == null || userPage.getRecords() == null || userPage.getRecords().isEmpty()) {
            return Pagination.empty();
        }
        List<MemberUserResult> memberUserResults = new ArrayList<>();
        List<UserDTO> userDTOList = userPage.getRecords();
        List<Long> userIdList = new ArrayList<>();
        userDTOList.forEach(user -> {
            userIdList.add(user.getId());
            memberUserResults.add(MemberUserResult.builder().id(user.getId()).mobile(user.getMobile()).name(user.getName())
                                          .createTime(user.getCreateTime())
                                          .lastLoginTime(user.getLastLoginTime()).build());
        });
        List<MemberGroupUser> memberGroupUserList = disinfectionMemberGroupUserMapper.selectList(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                                                         .in(MemberGroupUser::getUserId, userIdList));
        Map<Long, MemberGroup> memberGroupMap = new HashMap<>();
        List<Long> groupIdList = memberGroupUserList.stream().map(MemberGroupUser::getGroupId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            List<MemberGroup> memberGroupList = disinfectionMemberGroupMapper.selectBatchIds(groupIdList);
            Optional.ofNullable(memberGroupList).orElse(Collections.emptyList()).forEach(item -> memberGroupMap.put(item.getId(), item));
        }

        Map<Long, List<MemberGroupUser>> userGroupMap = memberGroupUserList.stream()
                .collect(Collectors.groupingBy(MemberGroupUser::getUserId));

        memberUserResults.forEach(item -> {
            List<MemberGroupUser> memberGroupUsers = userGroupMap.get(item.getId());
            if (memberGroupUsers != null && !memberGroupUsers.isEmpty()) {
                String groupNames = memberGroupUsers.stream()
                        .map(MemberGroupUser::getGroupId)           // 提取 groupId
                        .map(memberGroupMap::get)                   // 查 map，找不到返回 null
                        .filter(Objects::nonNull)                   // 过滤掉 null（即没匹配上的）
                        .map(MemberGroup::getGroupName)                  // 提取 name
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .collect(Collectors.joining(","));        // 用 ", " 拼接
                item.setGroups(groupNames);
            }
        });
        return PagingUtils.convert(userPage, memberUserResults);
    }

    /**
     * 删除用户
     *
     * @param id 用户主键
     *
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteMemberUser(Long id) {
        //查询用户管理的组
        var manGroupIds = disinfectionMemberGroupService.getManageGroupIdsByUserId(id);
        if (CollectionUtils.isEmpty(manGroupIds)) {
            disinfectionMemberGroupUserService.remove(Wrappers.<MemberGroupUser>lambdaQuery()
                                                              .eq(MemberGroupUser::getUserId, id));
            return true;
        }
        //查询管理的组是否有成员
        var exist = disinfectionMemberGroupUserService.lambdaQuery().in(MemberGroupUser::getGroupId, manGroupIds)
                .ne(MemberGroupUser::getUserId, id)
                .exists();
        //如果有成员则不许删除
        Assert.isConflict(exist, "用户管理的组，存在成员，不允许删除用户！");
        //如果管理的组没有成员，删除管理的组以及关联的设备信息,删除该用户加入的成员组里的信息，以及组的指令信息

        disinfectionMemberGroupService.removeByIds(manGroupIds);
        disinfectionMemberGroupDeviceService.remove(Wrappers.<MemberGroupDevice>lambdaQuery()
                                                            .in(MemberGroupDevice::getGroupId, manGroupIds));
        disinfectionMemberGroupUserService.remove(Wrappers.<MemberGroupUser>lambdaQuery()
                                                          .in(MemberGroupUser::getGroupId, manGroupIds));
        disinfectionMemberGroupUserService.remove(Wrappers.<MemberGroupUser>lambdaQuery()
                                                          .eq(MemberGroupUser::getUserId, id));
        disinfectionDeviceAutomaticService.remove(Wrappers.<MemberDeviceAutomatic>lambdaQuery()
                                                          .in(MemberDeviceAutomatic::getGroupId, manGroupIds));
        return true;
    }
}
