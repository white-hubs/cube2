package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;

import java.util.List;
import java.util.Map;

public interface DisinfectionProjectService extends IService<DisinfectionProject> {
    /**
     * 更新项目类型名称
     */
    boolean updateProjectTypeName(long projectTypeId, String dataName);

    /**
     * 根据名称查询项目
     */
    List<DisinfectionProject> queryByName(String projectName);

    /**
     * 根据项目ID获取项目名称
     */
    String getProjectName(long projectId);

    Map<Long, String> getProjectNamesMap(List<Long> projectIds);

    List<String> listAllProjectNames();

    List<DisinfectionProject> listInNames(List<String> projectNames);

    List<DisinfectionProject> listProjectsByIds(List<Long> projectIds);

    List<Integer> getAllTenants();
}