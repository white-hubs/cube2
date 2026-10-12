package com.guhecloud.cube.base.device.center.core.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizGroupMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizGroupRefMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizGroup;
import com.guhecloud.cube.base.device.center.core.model.BizGroupRef;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CenterGroupQueryPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateGroupCommand;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagBizGroupCommand;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterDeviceGroupService {

    private final CenterBizGroupMapper centerBizGroupMapper;

    private final CenterBizGroupRefMapper centerBizGroupRefMapper;

    private final CenterDeviceTagService centerDeviceTagService;

    private final CenterBizTagRefMapper centerBizTagRefMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long createGroup(CreateGroupCommand groupCommand) {
        BizGroup group = ObjectUtils.copying(groupCommand, BizGroup.class);
        BizGroup groupByName = getGroupByName(groupCommand.getName());
        Assert.isConflict(Objects.nonNull(groupByName), "分组名称已存在");
        this.centerBizGroupMapper.insert(group);
        return group.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createGroupByTag(CreateTagBizGroupCommand command) {
        CreateTagCmd createTagCmd = CreateTagCmd.createTopTag(null, command.getCode(), command.getName());
        BizTag bizTag = centerDeviceTagService.createTagIfNeed(createTagCmd);
        Long groupId = createGroup(command.getDeviceGroup());
        TagBindDeviceCmd bindDeviceCmd = TagBindDeviceCmd.of(bizTag.getTagCode(), Collections.singleton(groupId));
        centerDeviceTagService.bindTag(bindDeviceCmd);
        return groupId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(@NonNull Long id, @NonNull CreateGroupCommand command) {
        BizGroup group = getGroup(id);
        Assert.isNotFound(group, "分组不存在或已被删除");
        BizGroup groupByName = getGroupByName(command.getName());
        Assert.isConflict(Objects.nonNull(groupByName) &&
                          !groupByName.getId().equals(id), "分组名称已存在");
        group.setName(command.getName());
        group.setRemark(command.getRemark());
        this.centerBizGroupMapper.updateById(group);
        return id;
    }

    public Page<BizGroup> getPage(@NonNull CenterGroupQueryPageParam param) {
        return this.centerBizGroupMapper
                .selectPage(PagingUtils.convert(param), Wrappers.<BizGroup>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), BizGroup::getName, param.getKeyword())
                        .orderByDesc(BizGroup::getUpdateTime));
    }

    public BizGroup getGroup(@NonNull Long id) {
        return this.centerBizGroupMapper.selectById(id);
    }

    public BizGroup getGroupByName(@NonNull String groupName) {
        List<BizGroup> groups = this.centerBizGroupMapper.selectList(Wrappers.<BizGroup>lambdaQuery()
                                                                             .eq(BizGroup::getName, groupName));
        if (CollectionUtils.isEmpty(groups)) {
            return null;
        }
        return groups.getFirst();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeById(@NonNull Long id) {
        final String username = SecurityContextHolder.getPrincipal().getAccount();
        final String userId = SecurityContextHolder.getPrincipal().getId().toString();
        final String userType = SecurityContextHolder.getPrincipal().getType().name();
        centerBizGroupMapper.logicDeleteByIds(userId,
                                              username,
                                              userType,
                                              Collections.singleton(id),
                                              LocalDateTime.now(),
                                              System.currentTimeMillis());
        centerBizTagRefMapper.logicDeletedByDeviceIds(userId,
                                                      username,
                                                      userType,
                                                      Collections.singletonList(id),
                                                      LocalDateTime.now(),
                                                      System.currentTimeMillis());
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(@NonNull Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        final String username = SecurityContextHolder.getPrincipal().getAccount();
        final String userId = SecurityContextHolder.getPrincipal().getId().toString();
        final String userType = SecurityContextHolder.getPrincipal().getType().name();
        centerBizGroupMapper.logicDeleteByIds(userId,
                                              username,
                                              userType,
                                              ids,
                                              LocalDateTime.now(),
                                              System.currentTimeMillis());
        centerBizTagRefMapper.logicDeletedByDeviceIds(userId,
                                                      username,
                                                      userType,
                                                      new ArrayList<>(ids),
                                                      LocalDateTime.now(),
                                                      System.currentTimeMillis());
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeGroupRef(@NonNull Long id, @NonNull Set<Long> deviceIds) {
        this.centerBizGroupRefMapper.logicBatchDeleteByDeviceIds(id,
                                                                 deviceIds,
                                                                 LocalDateTime.now(),
                                                                 SecurityContextHolder.getPrincipal().getId().toString(),
                                                                 SecurityContextHolder.getPrincipal().getAccount(),
                                                                 SecurityContextHolder.getPrincipal().getType().name());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGroupDevice(@NonNull Long id, Set<Long> deviceIds) {
        var now = LocalDateTime.now();
        this.centerBizGroupRefMapper.logicBatchDelete(id,
                                                      now,
                                                      SecurityContextHolder.getPrincipal().getId().toString(),
                                                      SecurityContextHolder.getPrincipal().getAccount(),
                                                      SecurityContextHolder.getPrincipal().getType().name());
        List<BizGroupRef> bizGroupRefs = deviceIds.stream().map(device -> {
            BizGroupRef groupRef = new BizGroupRef();
            groupRef.setId(IdWorker.getId());
            groupRef.setGroupId(id);
            groupRef.setDeviceId(device);
            EntityAuditor.populateAudit(groupRef);
            return groupRef;
        }).collect(Collectors.toList());
        this.centerBizGroupRefMapper.insertBatch(bizGroupRefs);
    }

}
