package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionAreaMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionArea;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionAreaService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class DisinfectionAreaServiceImpl extends ServiceImpl<DisinfectionAreaMapper, DisinfectionArea>
        implements DisinfectionAreaService {
    @Override
    public void removeByProjectId(long projectId) {
        boolean remove = remove(new LambdaQueryWrapper<DisinfectionArea>().eq(DisinfectionArea::getProjectId, projectId));
        log.info("项目id:{},删除区域结果{}", projectId, remove);
    }

    @Override
    public List<DisinfectionArea> listByProjectId(long projectId) {
        return lambdaQuery().eq(DisinfectionArea::getProjectId, projectId).list();
    }

    @Override
    public Map<Long, Long> countMapByProjectIds(List<Long> projectIds) {
        if (ListUtils.isEmpty(projectIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionArea> list = lambdaQuery().in(DisinfectionArea::getProjectId, projectIds).list();
        Map<Long, List<DisinfectionArea>> groupMap = MapUtils.listGroupToMap(list, DisinfectionArea::getProjectId);

        Map<Long, Long> result = new HashMap<>();
        groupMap.forEach((key, value) -> result.put(key, (long) value.size()));
        return result;
    }

    @Override
    public String getAreaName(Long taskAreaId) {
        if (Objects.isNull(taskAreaId)) {
            return "";
        }
        DisinfectionArea area = getById(taskAreaId);
        return Objects.isNull(area) ? "" : area.getAreaName();
    }

    @Override
    public Map<Long, String> getAreaNameMap(List<Long> taskAreaIds) {
        if (ListUtils.isEmpty(taskAreaIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionArea> areaList = lambdaQuery().select(DisinfectionArea::getId, DisinfectionArea::getAreaName)
                .in(DisinfectionArea::getId, taskAreaIds).list();
        return MapUtils.listToMap(areaList, DisinfectionArea::getId, DisinfectionArea::getAreaName);
    }

    @Override
    public List<DisinfectionArea> listAreas(Long projectId, String keyword) {
        return lambdaQuery().eq(Objects.nonNull(projectId), DisinfectionArea::getProjectId, projectId)
                .like(StringUtils.isNotBlank(keyword), DisinfectionArea::getAreaName, keyword).list();
    }

    @Override
    public List<DisinfectionArea> listByAreaIds(List<Long> areaIds) {
        if (ListUtils.isEmpty(areaIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(DisinfectionArea::getId, areaIds).list();
    }
}