package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionProjectMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DisinfectionProjectServiceImpl extends ServiceImpl<DisinfectionProjectMapper, DisinfectionProject>
        implements DisinfectionProjectService {
    @Override
    public boolean updateProjectTypeName(long projectTypeId, String dataName) {
        return lambdaUpdate().eq(DisinfectionProject::getProjectTypeId, projectTypeId)
                .set(DisinfectionProject::getProjectTypeName, dataName).update();
    }

    @Override
    public List<DisinfectionProject> queryByName(String projectName) {
        return lambdaQuery().like(DisinfectionProject::getProjectName, projectName).list();
    }

    @Override
    public String getProjectName(long projectId) {
        DisinfectionProject project = getById(projectId);
        return Objects.isNull(project) ? "" : project.getProjectName();
    }

    @Override
    public Map<Long, String> getProjectNamesMap(List<Long> projectIds) {
        if (ListUtils.isEmpty(projectIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionProject> projectList = lambdaQuery().select(DisinfectionProject::getId, DisinfectionProject::getProjectName)
                .in(DisinfectionProject::getId, projectIds).list();
        return MapUtils.listToMap(projectList, DisinfectionProject::getId, DisinfectionProject::getProjectName);
    }

    @Override
    public List<String> listAllProjectNames() {
        List<DisinfectionProject> projectList = lambdaQuery().select(DisinfectionProject::getProjectName).list();
        if (ListUtils.isEmpty(projectList)) {
            return Collections.emptyList();
        }
        return ListUtils.convertItem(projectList, DisinfectionProject::getProjectName);
    }

    @Override
    public List<DisinfectionProject> listInNames(List<String> projectNames) {
        if (ListUtils.isEmpty(projectNames)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(DisinfectionProject::getProjectName, projectNames).list();
    }

    @Override
    public List<DisinfectionProject> listProjectsByIds(List<Long> projectIds) {
        if (ListUtils.isEmpty(projectIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(DisinfectionProject::getId, projectIds).list();
    }

    @Override
    public List<Integer> getAllTenants() {
        return this.getBaseMapper().getAllTenants();
    }
}