package com.guhecloud.cube.base.flow.context.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowGroupService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowGroupCommand;
import com.guhecloud.cube.base.flow.context.dao.FormFlowMapper;
import com.guhecloud.cube.base.flow.context.dao.FormFlowGroupMapper;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupTreeVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowGroupPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 10:15
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowGroupServiceImpl extends ServiceImpl<FormFlowGroupMapper, FormFlowGroupPO> implements FormFlowGroupService {

    private final FormFlowMapper formFlowMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(FormFlowGroupCommand command) {
        if (Objects.nonNull(command.getId())) {
            FormFlowGroupPO formFlowGroupPO = this.getById(command.getId());
            if (Objects.isNull(formFlowGroupPO)) {
                throw new ServiceException("表单分组不存在");
            }
            Long parentId = Objects.isNull(command.getParentId()) ? formFlowGroupPO.getParentId() : command.getParentId();
            String application = resolveApplication(command.getApplication(), parentId, formFlowGroupPO.getApplication());
            validateParent(command.getId(), parentId, application);
            if (!Objects.equals(formFlowGroupPO.getParentId(), parentId)) {
                validateParentCanAddChild(parentId);
            }
            if (hasActiveFormFlow(command.getId()) && hasChildren(command.getId())) {
                throw new ServiceException("表单分组下存在表单流程，无法调整为非末级分组");
            }
            formFlowGroupPO.setName(command.getName());
            formFlowGroupPO.setParentId(parentId);
            formFlowGroupPO.setApplication(application);
            return this.updateById(formFlowGroupPO);
        } else {
            String application = resolveApplication(command.getApplication(), command.getParentId(), null);
            validateParent(null, command.getParentId(), application);
            validateParentCanAddChild(command.getParentId());
            FormFlowGroupPO formFlowGroupPO = new FormFlowGroupPO();
            formFlowGroupPO.setName(command.getName());
            formFlowGroupPO.setParentId(command.getParentId());
            formFlowGroupPO.setApplication(application);
            return this.save(formFlowGroupPO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletedById(Long id) {
        if (hasChildren(id)) {
            throw new ServiceException("表单分组下存在子分组，无法删除");
        }
        if (hasActiveFormFlow(id)) {
            throw new ServiceException("表单分组下存在表单流程，无法删除");
        }
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<FormFlowGroupTreeVO> getTree(String keyword, String application) {
        List<FormFlowGroupPO> groups = listOrderedGroups();
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        groups = filterGroupsByApplication(groups, application);
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        if (StringUtils.isNotEmpty(keyword)) {
            Set<Long> includeIds = new HashSet<>();
            for (FormFlowGroupPO group : groups) {
                if (StringUtils.isNotEmpty(group.getName()) && group.getName().contains(keyword)) {
                    includeIds.add(group.getId());
                    if (Objects.nonNull(group.getParentId())) {
                        includeIds.add(group.getParentId());
                    }
                    groups.stream()
                            .filter(child -> Objects.equals(child.getParentId(), group.getId()))
                            .map(FormFlowGroupPO::getId)
                            .forEach(includeIds::add);
                }
            }
            groups = groups.stream()
                    .filter(group -> includeIds.contains(group.getId()))
                    .collect(Collectors.toList());
        }
        List<FormFlowGroupTreeVO> groupTreeVOS = ObjectUtils.copying(groups, FormFlowGroupTreeVO.class);
        return TreeUtils.assembleTrees(groupTreeVOS);
    }

    @Override
    public List<FormFlowGroupPO> listLeafGroups(String keyword, String application) {
        List<FormFlowGroupPO> groups = listOrderedGroups(application);
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        Set<Long> parentIds = groups.stream()
                .map(FormFlowGroupPO::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return groups.stream()
                .filter(group -> !parentIds.contains(group.getId()))
                .filter(group -> StringUtils.isEmpty(keyword) || StringUtils.isNotEmpty(group.getName()) && group.getName().contains(keyword))
                .collect(Collectors.toList());
    }

    private List<FormFlowGroupPO> filterGroupsByApplication(List<FormFlowGroupPO> groups, String application) {
        if (StringUtils.isBlank(application)) {
            return groups;
        }
        Set<Long> includeIds = new HashSet<>();
        Map<Long, FormFlowGroupPO> groupMap = groups.stream()
                .collect(Collectors.toMap(FormFlowGroupPO::getId, Function.identity(), (k1, k2) -> k1));
        for (FormFlowGroupPO group : groups) {
            if (Objects.equals(group.getApplication(), application)) {
                includeIds.add(group.getId());
                Long parentId = group.getParentId();
                while (Objects.nonNull(parentId) && groupMap.containsKey(parentId)) {
                    includeIds.add(parentId);
                    parentId = groupMap.get(parentId).getParentId();
                }
            }
        }
        return groups.stream()
                .filter(group -> includeIds.contains(group.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void assertLeafGroupForFormFlow(Long formGroupId) {
        if (Objects.isNull(formGroupId)) {
            return;
        }
        FormFlowGroupPO group = this.getById(formGroupId);
        if (Objects.isNull(group)) {
            throw new ServiceException("表单分组不存在");
        }
        if (hasChildren(formGroupId)) {
            throw new ServiceException("表单模板只能关联末级分组");
        }
    }

    @Override
    public List<Long> listLeafGroupIds(Long formGroupId) {
        if (Objects.isNull(formGroupId)) {
            return Collections.emptyList();
        }
        List<FormFlowGroupPO> groups = listOrderedGroups();
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        Map<Long, FormFlowGroupPO> groupMap = groups.stream()
                .collect(Collectors.toMap(FormFlowGroupPO::getId, Function.identity(), (k1, k2) -> k1));
        if (!groupMap.containsKey(formGroupId)) {
            return Collections.emptyList();
        }
        Set<Long> parentIds = groups.stream()
                .map(FormFlowGroupPO::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!parentIds.contains(formGroupId)) {
            return Collections.singletonList(formGroupId);
        }
        return groups.stream()
                .filter(group -> Objects.equals(group.getParentId(), formGroupId))
                .filter(group -> !parentIds.contains(group.getId()))
                .map(FormFlowGroupPO::getId)
                .collect(Collectors.toList());
    }

    private String resolveApplication(String application, Long parentId, String defaultApplication) {
        if (StringUtils.isNotBlank(application)) {
            return application;
        }
        if (Objects.nonNull(parentId)) {
            FormFlowGroupPO parent = this.getById(parentId);
            if (Objects.nonNull(parent)) {
                return parent.getApplication();
            }
        }
        return defaultApplication;
    }

    private void validateParent(Long id, Long parentId, String application) {
        if (Objects.isNull(parentId)) {
            return;
        }
        if (Objects.equals(id, parentId)) {
            throw new ServiceException("上级分组不能选择自身");
        }
        FormFlowGroupPO parent = this.getById(parentId);
        if (Objects.isNull(parent)) {
            throw new ServiceException("上级分组不存在");
        }
        if (Objects.nonNull(parent.getParentId()) || Objects.equals(parent.getLevel(), 2)) {
            throw new ServiceException("表单分组最多支持2级");
        }
        if (StringUtils.isNotBlank(parent.getApplication()) && StringUtils.isNotBlank(application)
                && !Objects.equals(parent.getApplication(), application)) {
            throw new ServiceException("上级分组与当前分组所属应用不一致");
        }
    }

    private void validateParentCanAddChild(Long parentId) {
        if (Objects.isNull(parentId)) {
            return;
        }
        if (hasActiveFormFlow(parentId)) {
            throw new ServiceException("表单分组下存在表单流程，无法新增子分组");
        }
    }

    private boolean hasChildren(Long id) {
        return Objects.nonNull(id) && this.lambdaQuery()
                .eq(FormFlowGroupPO::getParentId, id)
                .count() > 0;
    }

    private boolean hasActiveFormFlow(Long formGroupId) {
        if (Objects.isNull(formGroupId)) {
            return false;
        }
        Long count = formFlowMapper.selectCount(Wrappers.<FormFlowPO>lambdaQuery()
                                                        .eq(FormFlowPO::getFormGroupId, formGroupId)
                                                        .ne(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED));
        return Objects.nonNull(count) && count > 0;
    }

    private List<FormFlowGroupPO> listOrderedGroups() {
        return listOrderedGroups(null);
    }

    private List<FormFlowGroupPO> listOrderedGroups(String application) {
        return this.lambdaQuery()
                .eq(StringUtils.isNotBlank(application), FormFlowGroupPO::getApplication, application)
                .orderByAsc(FormFlowGroupPO::getLevel)
                .orderByAsc(FormFlowGroupPO::getCreateTime)
                .list();
    }
}
