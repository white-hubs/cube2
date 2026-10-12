package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionArea;

import java.util.List;
import java.util.Map;

public interface DisinfectionAreaService extends IService<DisinfectionArea> {
    /**
     * 删除指定项目下的所有区域
     *
     * @param projectId 项目ID
     */
    void removeByProjectId(long projectId);

    /**
     * 根据项目ID获取区域列表
     *
     * @param projectId 项目ID
     *
     * @return 区域列表
     */
    List<DisinfectionArea> listByProjectId(long projectId);

    /**
     * 获取项目对应的区域个数
     *
     * @param projectIds 项目ID列表
     *
     * @return 项目对应区域个数map
     */
    Map<Long, Long> countMapByProjectIds(List<Long> projectIds);

    /**
     * 根据区域ID获取区域名称
     */
    String getAreaName(Long taskAreaId);

    Map<Long, String> getAreaNameMap(List<Long> taskAreaIds);

    List<DisinfectionArea> listAreas(Long projectId, String keyword);

    List<DisinfectionArea> listByAreaIds(List<Long> areaIds);
}