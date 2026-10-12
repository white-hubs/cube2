package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionMaterialMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionMaterial;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionMaterialService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class DisinfectionMaterialServiceImpl extends ServiceImpl<DisinfectionMaterialMapper, DisinfectionMaterial>
        implements DisinfectionMaterialService {
    @Override
    public Map<Long, Long> getFileCount(List<Long> fileTypeIds) {
        if (ListUtils.isEmpty(fileTypeIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionMaterial> materialList = lambdaQuery().in(DisinfectionMaterial::getFileTypeId, fileTypeIds).list();
        return MapUtils.listToCountMap(materialList, DisinfectionMaterial::getFileTypeId);
    }

    @Override
    public boolean updateFileTypeName(long fileTypeId, String dataName) {
        //修改使用该类型的文件类型名称
        return lambdaUpdate().set(DisinfectionMaterial::getFileTypeName, dataName).eq(DisinfectionMaterial::getFileTypeId, fileTypeId)
                .update();
    }

    @Override
    public void removeByProjectId(long projectId) {
        boolean remove = remove(new LambdaQueryWrapper<DisinfectionMaterial>().eq(DisinfectionMaterial::getProjectId, projectId));
        log.info("项目id:{},删除项目下的资料结果{}", projectId, remove);
    }

    @Override
    public List<DisinfectionMaterial> listByProjectId(long projectId) {
        return lambdaQuery().eq(DisinfectionMaterial::getProjectId, projectId).list();
    }

    @Override
    public Map<Long, Long> getProjectFileCount(List<Long> fileTypeIds, Long projectId) {
        if (Objects.isNull(projectId) || ListUtils.isEmpty(fileTypeIds)) {
            return Collections.emptyMap();
        }
        List<DisinfectionMaterial> materialList = lambdaQuery()
                .eq(DisinfectionMaterial::getProjectId, projectId)
                .in(DisinfectionMaterial::getFileTypeId, fileTypeIds).list();
        return MapUtils.listToCountMap(materialList, DisinfectionMaterial::getFileTypeId);
    }
}