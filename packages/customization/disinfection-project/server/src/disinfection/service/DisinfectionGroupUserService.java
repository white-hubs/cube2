package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroupUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DisinfectionGroupUserService extends IService<DisinfectionGroupUser> {
    /**
     * 获取分组用户ID的Map
     *
     * @param groupIds 分组ID列表
     *
     * @return 分组用户ID的Map
     */
    Map<Long, List<Long>> getGroupUserIdsMap(List<Long> groupIds);

    /**
     * 删除分组用户
     *
     * @param groupId 分组ID
     */
    void removeByGroupId(long groupId);

    /**
     * 获取分组用户ID列表
     *
     * @param groupId 分组ID
     *
     * @return 分组用户ID列表
     */
    List<Long> getGroupUserIds(long groupId);

    /**
     * 添加分组用户
     *
     * @param groupId 分组ID
     * @param userId  用户ID
     *
     * @return 是否添加成功
     */
    boolean addUser(long groupId, long userId);

    void removeGroupUsers(long groupId, Collection<Long> waitDeleteUserIds);

    void saveUsers(long groupId, Collection<Long> waitInsertUserIds);

    boolean isInGroup(Long userId, Long groupId);

    List<DisinfectionGroupUser> getGroupsByUserId(Long useId);

    Map<Long, Boolean> isExistUsersByGroupIds(List<Long> groupIds);
}