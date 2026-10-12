package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionGroup;

import java.util.List;
import java.util.Map;

public interface DisinfectionGroupService extends IService<DisinfectionGroup> {
    /**
     * 根据项目id查询分组信息
     */
    String getGroupName(Long groupId);

    /**
     * 根据项目id删除分组信息
     */
    void removeByProjectId(long projectId);

    /**
     * 根据分组id查询分组名称
     */
    Map<Long, String> getNameMap(List<Long> ids);

    /**
     * 根据项目id查询分组信息
     */
    List<DisinfectionGroup> listByProjectId(long projectId);

    List<DisinfectionGroup> listGroupByIds(List<Long> groupIds);

}