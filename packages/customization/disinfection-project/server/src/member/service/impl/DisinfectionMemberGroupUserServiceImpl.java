package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupUserMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberGroupRoleEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupTotalEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberInviteByMobileParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ListDiffDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupUserResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupUserServiceImpl extends ServiceImpl<DisinfectionMemberGroupUserMapper, MemberGroupUser> implements DisinfectionMemberGroupUserService {
    private final UserClient userClient;
    private final FileClient fileClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 分页获取分组人员分页
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MemberGroupUserResult> groupUserPage(MemberGroupUserQueryParam param) {
        /* 获取人员id */
        List<MemberGroupUser> groupUserList = this.list(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                .eq(MemberGroupUser::getGroupId, param.getGroupId()));
        if (CollectionUtils.isEmpty(groupUserList)) {
            return Pagination.empty();
        }
        List<Long> groupUserIdList = groupUserList.stream().map(MemberGroupUser::getUserId).distinct().collect(Collectors.toList());
        /* 查询人员信息 */
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPageNumber(param.getPageNumber());
        userQueryParam.setPageSize(param.getPageSize());
        userQueryParam.setKeyword(param.getKeyword());
        userQueryParam.setIds(groupUserIdList);
        Pagination<UserDTO> dtoPagination = userClient.listUsers(userQueryParam);
        if (CollectionUtils.isEmpty(dtoPagination.getRecords())) {
            return Pagination.empty();
        }
        List<Long> userIdList = new ArrayList<>();
        List<MemberGroupUserResult> resultList = dtoPagination.getRecords().stream().map(dto -> {
            userIdList.add(dto.getId());
            MemberGroupUserResult result = new MemberGroupUserResult();
            result.setId(dto.getId());
            result.setName(dto.getName());
            result.setMobile(dto.getMobile());
            result.setLastLoginTime(dto.getLastLoginTime());
            result.setCreateTime(dto.getCreateTime());
            return result;
        }).collect(Collectors.toList());
        BusinessKeysParam fileParam = new BusinessKeysParam();
        fileParam.setBusinessKeys(userIdList);
        Map<Long, List<FileVO>> fileMap = fileClient.getByBusinessKeys(fileParam);
        resultList.forEach(result -> {
            List<FileVO> fileVOList = fileMap.get(result.getId());
            if (CollectionUtils.isNotEmpty(fileVOList)) {
                result.setFileVO(fileVOList.get(0));
            }
        });
        return PagingUtils.convert(dtoPagination, resultList);
    }

    /**
     * 获取分组人员列表
     *
     * @param param 条件
     *
     * @return 分组人员列表
     */
    @Override
    public List<MemberGroupUserResult> groupUserList(MemberGroupUserQueryParam param) {
        List<Long> userIdList = new ArrayList<>();
        List<MemberGroupUser> groupUserList = this.list(Wrappers.<MemberGroupUser>lambdaQuery().select(MemberGroupUser::getUserId)
                                                                .eq(MemberGroupUser::getGroupId, param.getGroupId()));
        if (CollectionUtils.isEmpty(groupUserList)) {
            return Collections.emptyList();
        }
        Long loginUserId = SecurityContextHolder.getPrincipal().getId();
        groupUserList.forEach(groupUser -> {
            // 如果不是排除自己就直接添加  或者 不为自己也直接添加
            if (!Boolean.TRUE.equals(param.getNotMe()) || !Objects.equals(loginUserId, groupUser.getId())) {
                userIdList.add(groupUser.getUserId());
            }
        });
        List<UserDTO> users = userClient.listAllUsers(UserQueryParam.builder().keyword(param.getKeyword()).build());
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        List<MemberGroupUserResult> resultList = ObjectUtils.copying(users, MemberGroupUserResult.class);
        //设置用户的头像
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(userIdList);
        Map<Long, List<FileVO>> fileMap = fileClient.getByBusinessKeys(businessKeysParam);
        resultList.forEach(result -> {
            List<FileVO> fileVOList = fileMap.get(result.getId());
            if (ListUtils.nonEmpty(fileVOList)) {
                result.setFileVO(fileVOList.get(0));
            }
        });
        return resultList;
    }

    /**
     * 批量添加分组人员
     *
     * @param param 人员信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addGroupUserBatch(MemberGroupUserCommandParam param) {
        List<MemberGroupUser> insertList = new ArrayList<>();
        List<MemberGroupUser> oldList = this.list(Wrappers.<MemberGroupUser>lambdaQuery().select(MemberGroupUser::getUserId)
                                                          .eq(MemberGroupUser::getGroupId, param.getGroupId())
                                                          .in(MemberGroupUser::getUserId, param.getUserIdList()));
        if (CollectionUtils.isNotEmpty(oldList)) {
            param.getUserIdList().removeAll(oldList.stream().map(MemberGroupUser::getUserId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(param.getUserIdList())) {
            log.warn("人员重复无需操作:{}", param.getUserIdList());
            return true;
        }
        param.getUserIdList().forEach(id -> {
            MemberGroupUser po = new MemberGroupUser();
            po.setId(IdWorker.getId());
            po.setUserId(id);
            po.setGroupId(param.getGroupId());
            po.setRole(StringUtils.isBlank(param.getRole()) ? MemberGroupRoleEnum.MEMBER.name() : param.getRole());
            EntityAuditor.populateAudit(po);
            insertList.add(po);
        });
        this.saveBatch(insertList);
        // 发送更新消息
        DisinfectionMemberGroupTotalEvent groupTotalEvent = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), true, false);
        applicationEventPublisher.publishEvent(groupTotalEvent);
        return true;
    }

    /**
     * 管理员通过手机号邀请成员
     *
     * @param param 邀请信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean inviteByMobile(MemberInviteByMobileParam param) {
        var loginUserId = SecurityContextHolder.getPrincipal().getId();
        var manager = this.getOne(Wrappers.<MemberGroupUser>lambdaQuery()
                                          .select(MemberGroupUser::getId)
                                          .eq(MemberGroupUser::getGroupId, param.getGroupId())
                                          .eq(MemberGroupUser::getUserId, loginUserId)
                                          .eq(MemberGroupUser::getRole, MemberGroupRoleEnum.MANAGER.name())
                                          .last("limit 1"));
        Assert.isPermissionDenied(manager == null, "当前用户不是该分组管理员");

        var inviteUser = userClient.getUserByMobile(param.getMobile());
        Assert.isNotFound(inviteUser == null || inviteUser.getId() == null, "手机号对应用户不存在");

        var exist = this.exists(Wrappers.<MemberGroupUser>lambdaQuery()
                                       .eq(MemberGroupUser::getGroupId, param.getGroupId())
                                       .eq(MemberGroupUser::getUserId, inviteUser.getId()));
        Assert.isConflict(exist, "成员已在分组中");

        var now = LocalDateTime.now();
        var principal = SecurityContextHolder.getPrincipal();
        var userType = ActorTypeEnum.safeValueOf(principal.getType().name());
        var memberGroupUser = this.createMemberGroupUser(param.getGroupId(), inviteUser.getId(), MemberGroupRoleEnum.MEMBER.name(), now,
                                                         principal.getAccount(), principal.getId().toString(), userType);
        this.save(memberGroupUser);

        DisinfectionMemberGroupTotalEvent groupTotalEvent = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), true, false);
        applicationEventPublisher.publishEvent(groupTotalEvent);
        return true;
    }

    /**
     * 批量移除人员消息
     *
     * @param param 人员信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeGroupUserBatch(MemberGroupUserCommandParam param) {
        Assert.isParameterError(param.getGroupId() == null, "分组不能为空");
        if (CollectionUtils.isEmpty(param.getUserIdList())) {
            log.warn("移除人员为空无需操作：{}", param.getGroupId());
            return true;
        }
        this.remove(Wrappers.<MemberGroupUser>lambdaQuery().eq(MemberGroupUser::getGroupId, param.getGroupId())
                            .in(MemberGroupUser::getUserId, param.getUserIdList()));
        // 发送更新消息
        DisinfectionMemberGroupTotalEvent groupTotalEvent = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), true, false);
        applicationEventPublisher.publishEvent(groupTotalEvent);
        return true;
    }

    /**
     * 编辑组人员
     *
     * @param param 编辑信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editGroupUser(MemberGroupUserCommandParam param) {
        /* 获取原来的 */
        List<MemberGroupUser> memberGroupUserList = this.list(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                      .eq(MemberGroupUser::getGroupId, param.getGroupId()));

        // 比较
        ListDiffDTO diffDTO = this.diffLongList(memberGroupUserList, param.getUserIdList(), param.getRole());
        var now = LocalDateTime.now();
        var account = SecurityContextHolder.getPrincipal().getAccount();
        var createUserId = SecurityContextHolder.getPrincipal().getId().toString();
        var userType = ActorTypeEnum.safeValueOf(SecurityContextHolder.getPrincipal().getType().name());
        // 处理新增的
        List<Long> addList = diffDTO.getAddList();
        if (CollectionUtils.isNotEmpty(addList)) {
            List<MemberGroupUser> insertList = new ArrayList<>();
            addList.forEach(userId -> insertList.add(this.createMemberGroupUser(param.getGroupId(), userId, param.getRole(), now, account, createUserId, userType)));
            this.saveBatch(insertList);
        }
        // 处理删除的
        List<Long> removeList = diffDTO.getRemoveList();
        if (CollectionUtils.isNotEmpty(removeList)) {
            this.remove(Wrappers.<MemberGroupUser>lambdaQuery().in(MemberGroupUser::getUserId, removeList));
        }
        // 更新总数量
        DisinfectionMemberGroupTotalEvent event = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), true, false);
        applicationEventPublisher.publishEvent(event);
        return true;
    }

    @Override
    public List<Long> getGroupIdsByUserId(Long userId) {
        return Optional.ofNullable(userId)
                .map(id -> {
                    List<MemberGroupUser> list = lambdaQuery().select(MemberGroupUser::getGroupId)
                            .eq(MemberGroupUser::getUserId, id).list();
                    return ListUtils.convertItem(list, MemberGroupUser::getGroupId);
                })
                .orElse(new ArrayList<>());

    }

    @Override
    public List<Long> getUserIdsByGroupIds(List<Long> groupIds) {
        if (ListUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        List<MemberGroupUser> memberGroupUsers = lambdaQuery().select(MemberGroupUser::getUserId)
                .in(MemberGroupUser::getGroupId, groupIds).list();
        return ListUtils.convertItem(memberGroupUsers, MemberGroupUser::getUserId);
    }

    private ListDiffDTO diffLongList(List<MemberGroupUser> oldList, List<Long> newList, String role) {
        // 处理 null
        oldList = Optional.ofNullable(oldList).orElse(Collections.emptyList());
        newList = Optional.ofNullable(newList).orElse(Collections.emptyList());

        // 提取比较 key: (id, name)
        Set<DisinfectionMemberGroupServiceImpl.UserKey> oldKeys = oldList.stream()
                .map(u -> new DisinfectionMemberGroupServiceImpl.UserKey(u.getUserId(), u.getRole()))
                .collect(Collectors.toSet());

        Set<DisinfectionMemberGroupServiceImpl.UserKey> newKeys = newList.stream()
                .map(userId -> new DisinfectionMemberGroupServiceImpl.UserKey(userId, role))
                .collect(Collectors.toSet());

        // 计算差集
        Set<DisinfectionMemberGroupServiceImpl.UserKey> addedKeys = new HashSet<>(newKeys);
        addedKeys.removeAll(oldKeys);

        Set<DisinfectionMemberGroupServiceImpl.UserKey> removedKeys = new HashSet<>(oldKeys);
        removedKeys.removeAll(newKeys);

        // 提取 id 列表
        List<Long> addedIds = addedKeys.stream().map(DisinfectionMemberGroupServiceImpl.UserKey::getId).collect(Collectors.toList());
        List<Long> removedIds = removedKeys.stream().map(DisinfectionMemberGroupServiceImpl.UserKey::getId).collect(Collectors.toList());

        return new ListDiffDTO(addedIds, removedIds);
    }

    private MemberGroupUser createMemberGroupUser(Long groupId, Long userId, String role, LocalDateTime now, String account,
                                                  String createUserId,
                                                  ActorTypeEnum userType
                                                 ) {
        MemberGroupUser memberGroupUser = new MemberGroupUser();
        memberGroupUser.setId(IdWorker.getId());
        memberGroupUser.setGroupId(groupId);
        memberGroupUser.setUserId(userId);
        memberGroupUser.setRole(role);
        EntityAuditor.populateAudit(memberGroupUser, account, createUserId, userType, now);
        return memberGroupUser;
    }
}
