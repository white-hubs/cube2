package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionGroupMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DisinfectionGroupServiceImpl extends ServiceImpl<DisinfectionGroupMapper, DisinfectionGroup>
        implements DisinfectionGroupService {
    @Override
    public String getGroupName(Long groupId) {
        Map<Long, String> nameMap = getNameMap(Collections.singletonList(groupId));
        return nameMap.getOrDefault(groupId, "");
    }

    @Override
    public void removeByProjectId(long projectId) {
        boolean remove = remove(new LambdaQueryWrapper<DisinfectionGroup>().eq(DisinfectionGroup::getProjectId, projectId));
        log.info("项目id:{},删除分组结果{}", projectId, remove);

    }

    @Override
    public Map<Long, String> getNameMap(List<Long> ids) {
        if (ListUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DisinfectionGroup> groupList = lambdaQuery().in(DisinfectionGroup::getId, ids).list();
        return MapUtils.listToMap(groupList, DisinfectionGroup::getId, DisinfectionGroup::getGroupName);
    }

    @Override
    public List<DisinfectionGroup> listByProjectId(long projectId) {
        return lambdaQuery().eq(DisinfectionGroup::getProjectId, projectId).list();
    }

    @Override
    public List<DisinfectionGroup> listGroupByIds(List<Long> groupIds) {
        if (ListUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(DisinfectionGroup::getId, groupIds).list();
    }

}