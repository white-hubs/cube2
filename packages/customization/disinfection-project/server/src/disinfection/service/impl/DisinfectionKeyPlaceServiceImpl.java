package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionKeyPlaceMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionKeyPlace;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionKeyPlaceService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class DisinfectionKeyPlaceServiceImpl extends ServiceImpl<DisinfectionKeyPlaceMapper, DisinfectionKeyPlace>
        implements DisinfectionKeyPlaceService {
    @Override
    public List<DisinfectionKeyPlace> listKeyPlacesByIds(Collection<Long> placeIds) {
        if (ListUtils.isEmpty(placeIds)) {
            return Collections.emptyList();
        }
        return listByIds(placeIds);
    }

    @Override
    public Map<Long, Long> getProjectKeyPlaceCount(List<Long> dataTypeIds, Long projectId) {
        if (ListUtils.isEmpty(dataTypeIds) || Objects.isNull(projectId)) {
            return Collections.emptyMap();
        }
        List<DisinfectionKeyPlace> keyPlaceList = lambdaQuery()
                .eq(DisinfectionKeyPlace::getProjectId, projectId)
                .in(DisinfectionKeyPlace::getPlaceTypeId, dataTypeIds).list();
        return MapUtils.listToCountMap(keyPlaceList, DisinfectionKeyPlace::getPlaceTypeId);
    }

    @Override
    public Map<Long, Long> getKeyPlaceCount(List<Long> placeDataTypeIds) {
        if (ListUtils.isEmpty(placeDataTypeIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionKeyPlace> keyPlaceList = lambdaQuery().in(DisinfectionKeyPlace::getPlaceTypeId, placeDataTypeIds).list();
        return MapUtils.listToCountMap(keyPlaceList, DisinfectionKeyPlace::getPlaceTypeId);
    }

    @Override
    public void updatePlaceTypeName(long placeTypeId, String dataName) {
        lambdaUpdate().eq(DisinfectionKeyPlace::getPlaceTypeId, placeTypeId).set(DisinfectionKeyPlace::getPlaceTypeName, dataName)
                .update();
    }

    @Override
    public void removeByProjectId(long projectId) {
        boolean remove = remove(new LambdaQueryWrapper<DisinfectionKeyPlace>().eq(DisinfectionKeyPlace::getProjectId, projectId));
        log.info("项目id:{},删除重点场所结果{}", projectId, remove);
    }

    @Override
    public List<DisinfectionKeyPlace> listByProjectId(long projectId) {
        return lambdaQuery().eq(DisinfectionKeyPlace::getProjectId, projectId).list();
    }
}