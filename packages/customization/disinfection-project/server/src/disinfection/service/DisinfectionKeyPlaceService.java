package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionKeyPlace;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DisinfectionKeyPlaceService extends IService<DisinfectionKeyPlace> {
    /**
     * @param placeDataTypeIds 场所的数据类型ID
     *
     * @return 键为数据类型ID，值为该数据类型下，场所数量
     */
    Map<Long, Long> getKeyPlaceCount(List<Long> placeDataTypeIds);

    /**
     * @param dataName 数据类型名称
     */
    void updatePlaceTypeName(long placeTypeId, String dataName);

    /**
     * @param projectId 项目ID
     */
    void removeByProjectId(long projectId);

    /**
     * @param projectId 项目ID
     *
     * @return 项目下场所列表
     */
    List<DisinfectionKeyPlace> listByProjectId(long projectId);

    List<DisinfectionKeyPlace> listKeyPlacesByIds(Collection<Long> placeIds);

    Map<Long, Long> getProjectKeyPlaceCount(List<Long> dataTypeIds, Long projectId);
}