package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionMaterial;

import java.util.List;
import java.util.Map;

public interface DisinfectionMaterialService extends IService<DisinfectionMaterial> {
    /**
     * 获取文件数量
     *
     * @param fileTypeIds 数据类型id
     *
     * @return 文件数量
     */
    Map<Long, Long> getFileCount(List<Long> fileTypeIds);

    /**
     * 更新文件类型名称
     *
     * @param fileTypeId 文件类型id
     * @param dataName   类型名称名称
     *
     * @return 是否更新成功
     */
    boolean updateFileTypeName(long fileTypeId, String dataName);

    /**
     * 删除文件
     *
     * @param projectId 项目id
     */
    void removeByProjectId(long projectId);

    /**
     * 根据项目id查询文件
     *
     * @param projectId 项目id
     *
     * @return 文件列表
     */
    List<DisinfectionMaterial> listByProjectId(long projectId);

    Map<Long, Long> getProjectFileCount(List<Long> fileTypeIds, Long projectId);
}