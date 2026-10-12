package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionGroupUserMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisinfectionGroupUserServiceImpl extends ServiceImpl<DisinfectionGroupUserMapper, DisinfectionGroupUser>
        implements DisinfectionGroupUserService {
    @Override
    public Map<Long, List<Long>> getGroupUserIdsMap(List<Long> groupIds) {
        if (ListUtils.isEmpty(groupIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionGroupUser> list = lambdaQuery().in(DisinfectionGroupUser::getGroupId, groupIds).list();
        Map<Long, List<DisinfectionGroupUser>> groupUserMap = MapUtils.listGroupToMap(list, DisinfectionGroupUser::getGroupId);
        Map<Long, List<Long>> groupUserIdsMap = new HashMap<>();
        groupUserMap.forEach((k, v) -> {
            groupUserIdsMap.put(k, ListUtils.convertItem(v, DisinfectionGroupUser::getUserId));
        });
        return groupUserIdsMap;
    }

    @Override
    public void removeByGroupId(long groupId) {
        remove(new LambdaQueryWrapper<DisinfectionGroupUser>().eq(DisinfectionGroupUser::getGroupId, groupId));
    }

    @Override
    public List<Long> getGroupUserIds(long groupId) {
        return lambdaQuery().eq(DisinfectionGroupUser::getGroupId, groupId).list().stream().map(DisinfectionGroupUser::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addUser(long groupId, long userId) {
        DisinfectionGroupUser disinfectionGroupUser = new DisinfectionGroupUser();
        disinfectionGroupUser.setGroupId(groupId);
        disinfectionGroupUser.setUserId(userId);

        return save(disinfectionGroupUser);
    }

    @Override
    public void removeGroupUsers(long groupId, Collection<Long> waitDeleteUserIds) {
        if (ListUtils.isEmpty(waitDeleteUserIds)) {
            return;
        }
        remove(new LambdaQueryWrapper<DisinfectionGroupUser>().eq(DisinfectionGroupUser::getGroupId, groupId)
                       .in(DisinfectionGroupUser::getUserId, waitDeleteUserIds));
    }

    @Override
    public void saveUsers(long groupId, Collection<Long> waitInsertUserIds) {
        if (ListUtils.isEmpty(waitInsertUserIds)) {
            return;
        }
        Set<DisinfectionGroupUser> groupUsers = waitInsertUserIds.stream().map(userId -> {
            DisinfectionGroupUser disinfectionGroupUser = new DisinfectionGroupUser();
            disinfectionGroupUser.setGroupId(groupId);
            disinfectionGroupUser.setUserId(userId);
            return disinfectionGroupUser;
        }).collect(Collectors.toSet());
        saveBatch(groupUsers);
    }

    @Override
    public boolean isInGroup(Long userId, Long groupId) {
        if (Objects.isNull(userId) || Objects.isNull(groupId)) {
            return false;
        }
        return lambdaQuery().eq(DisinfectionGroupUser::getGroupId, groupId).eq(DisinfectionGroupUser::getUserId, userId).count() > 0;
    }

    @Override
    public List<DisinfectionGroupUser> getGroupsByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        return lambdaQuery().eq(DisinfectionGroupUser::getUserId, userId).list();
    }

    @Override
    public Map<Long, Boolean> isExistUsersByGroupIds(List<Long> groupIds) {
        if (ListUtils.isEmpty(groupIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionGroupUser> list = lambdaQuery()
                .in(DisinfectionGroupUser::getGroupId, groupIds)
                .list();
        return MapUtils.listGroupToMap(list, DisinfectionGroupUser::getGroupId)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> ListUtils.nonEmpty(entry.getValue())));

    }

}