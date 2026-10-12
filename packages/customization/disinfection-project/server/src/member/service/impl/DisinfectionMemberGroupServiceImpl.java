package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberGroupRoleEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupServiceImpl extends ServiceImpl<DisinfectionMemberGroupMapper, MemberGroup> implements DisinfectionMemberGroupService {
    private final UserClient userClient;
    private final DisinfectionMemberGroupUserService disinfectionMemberGroupUserService;

    /**
     * 组织分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MemberGroupResult> getMemberGroupPage(MemberGroupQueryParam param) {
        LambdaQueryWrapper<MemberGroup> wrapper = Wrappers.<MemberGroup>lambdaQuery()
                .between(Objects.nonNull(param.getCreateTime()), MemberGroup::getCreateTime, Optional.ofNullable(param.getCreateTime())
                        .orElse(Period.builder().build()).getBegin(), Optional.ofNullable(param.getCreateTime())
                                 .orElse(Period.builder().build()).getEnd())
                .eq(param.getMemberUserId() != null, MemberGroup::getMemberUserId, param.getMemberUserId())
                .like(StringUtils.isNotBlank(param.getName()), MemberGroup::getGroupName, param.getName());
        /* 成员名模糊条件 */
        if (StringUtils.isNotBlank(param.getKeyword())) {
            // 模糊获取人员id
            List<UserDTO> userDTOList = userClient.listUsers(param.getKeyword());
            if (CollectionUtils.isEmpty(userDTOList)) {
                return Pagination.empty();
            }
            List<Long> userIdList = userDTOList.stream().map(UserDTO::getId).collect(Collectors.toList());
            if (Boolean.TRUE.equals(param.getNotMe())) {
                userIdList.remove(SecurityContextHolder.getPrincipal().getId());
            }
            // 根据获取的用户id查询对应组
            List<MemberGroupUser> memberGroupUserList = disinfectionMemberGroupUserService.list(Wrappers.<MemberGroupUser>lambdaQuery()
                                                                                                        .select(MemberGroupUser::getGroupId)
                                                                                                        .and(param.getManagerUserId() !=
                                                                                                             null, i -> i
                                                                                                                .eq(MemberGroupUser::getUserId, param.getManagerUserId())
                                                                                                                .eq(MemberGroupUser::getRole, MemberGroupRoleEnum.MANAGER))
                                                                                                        .in(MemberGroupUser::getUserId, userIdList));
            if (CollectionUtils.isEmpty(memberGroupUserList)) {
                return Pagination.empty();
            }
            List<Long> groupIdList = memberGroupUserList.stream().map(MemberGroupUser::getGroupId).collect(Collectors.toList());
            // 添加组id条件
            wrapper.in(MemberGroup::getId, groupIdList);
        }
        wrapper.orderByDesc(MemberGroup::getCreateTime);
        // 查询组数据
        Page<MemberGroup> page = this.page(PagingUtils.convert(param), wrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        /* 处理管理人员名称 */
        List<MemberGroupResult> dtoList = handlerMemberUserName(page.getRecords());

        return PagingUtils.convert(page, dtoList);
    }

    /**
     * 获取分组信息
     *
     * @param id 主键
     *
     * @return 组信息
     */
    @Override
    public MemberGroupResult getMemberGroupById(Long id) {
        MemberGroupResult dto = new MemberGroupResult();
        MemberGroup memberGroup = this.getById(id);
        if (memberGroup == null) {
            return dto;
        }
        return this.handlerMemberUserName(memberGroup);
    }

    /**
     * 添加组
     *
     * @param param 组信息
     *
     * @return id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addMemberGroup(MemberGroupCommandParam param) {
        MemberGroup memberGroup = param.createMemberGroup();
        this.save(memberGroup);
        MemberGroupUser memberGroupUser = new MemberGroupUser();
        memberGroupUser.setGroupId(memberGroup.getId());
        memberGroupUser.setUserId(memberGroup.getMemberUserId());
        memberGroupUser.setRole(MemberGroupRoleEnum.MANAGER.name());
        memberGroupUser.setCreateBy(String.valueOf(memberGroup.getMemberUserId()));
        memberGroupUser.setUpdateBy(String.valueOf(memberGroup.getMemberUserId()));
        disinfectionMemberGroupUserService.save(memberGroupUser);
        return memberGroup.getId();
    }

    /**
     * 删除组
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMemberGroup(Long id) {
        DatabaseContextHolder.softDelete(this, id);
        disinfectionMemberGroupUserService.remove(Wrappers.<MemberGroupUser>lambdaQuery().eq(MemberGroupUser::getGroupId, id));
        return true;
    }

    /**
     * 修改组信息(只修改组)
     *
     * @param param 组
     *
     * @return 是否成功
     */
    @Override
    public Boolean editMemberGroup(MemberGroupCommandParam param) {
        MemberGroup old = this.getById(param.getId());
        Assert.isNotFound(old, "信息不存在");
        if (StringUtils.isNotBlank(param.getGroupName())) {
            old.setGroupName(param.getGroupName());
        }
        if (param.getMemberUserId() != null) {
            old.setMemberUserId(param.getMemberUserId());
        }
        if (param.getUserCount() != null) {
            old.setUserCount(param.getUserCount());
        }
        if (StringUtils.isNotBlank(param.getDescription())) {
            old.setDescription(param.getDescription());
        }
        old.setUpdateTime(LocalDateTime.now());
        old.setUpdateBy(String.valueOf(SecurityContextHolder.getPrincipal().getId()));
        old.setUpdaterId(String.valueOf(SecurityContextHolder.getPrincipal().getId()));
        this.updateById(old);
        return true;
    }

    @Override
    public List<Long> getManageGroupIdsByUserId(Long userId) {
        return Optional
                .ofNullable(userId)
                .map(id -> {
                    List<MemberGroup> list = lambdaQuery().select(MemberGroup::getId).eq(MemberGroup::getMemberUserId, userId).list();
                    return list.stream().map(MemberGroup::getId).collect(Collectors.toList());
                })
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Long> getGroupIdsByName(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Collections.emptyList();

        }
        List<MemberGroup> memberGroups = lambdaQuery().select(MemberGroup::getId).like(MemberGroup::getGroupName, keyword).list();
        return ListUtils.convertItem(memberGroups, MemberGroup::getId);
    }

    @Override
    public List<MemberGroup> queryByPeriod(Period period) {
        return lambdaQuery().between(MemberGroup::getCreateTime, period.getBegin(), period.getEnd()).list();
    }

    private MemberGroupResult handlerMemberUserName(MemberGroup po) {
        List<MemberGroupResult> list = this.handlerMemberUserName(Collections.singletonList(po));
        if (CollectionUtils.isEmpty(list)) {
            return new MemberGroupResult();
        }
        return list.get(0);
    }

    private List<MemberGroupResult> handlerMemberUserName(List<MemberGroup> poList) {
        List<MemberGroupResult> dtoList = new ArrayList<>();
        List<Long> memberUserIdList = new ArrayList<>();
        poList.forEach(item -> {
            MemberGroupResult dto = new MemberGroupResult();
            dto.setId(item.getId());
            dto.setGroupName(item.getGroupName());
            dto.setUserCount(item.getUserCount());
            dto.setCreateTime(item.getCreateTime());
            dto.setMemberUserId(item.getMemberUserId());
            memberUserIdList.add(item.getMemberUserId());
            dtoList.add(dto);
        });
        UserMapQuery userMapQuery = new UserMapQuery();
        userMapQuery.setUserIds(new HashSet<>(memberUserIdList));
        Map<String, UserDTO> userDTOMap = userClient.getTenantUserMap(userMapQuery);
        dtoList.forEach(dto -> {
            UserDTO userDTO = userDTOMap.get(String.valueOf(dto.getMemberUserId()));
            if (userDTO != null) {
                dto.setMemberUserName(userDTO.getName());
            }
        });
        return dtoList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UserKey {
        private Long id;
        private String role;
    }
}
