package com.guhecloud.cube.base.file.server.service;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-07  14:36
 * @Description:
 * @Version: 1.0
 */
public interface FileInitialisationTenantService {
    /**
     * 初始化租户文件服务配置
     */
    void initialisationTenant(Integer tenant);

    /**
     * 所有租户新增场景配置
     */
    void addConfigAllTenant(String sceneName, String sceneCode, String bucket);

    void updateConfigAllTenant(String sceneName, String sceneCode, String bucket);
}
