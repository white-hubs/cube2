package com.guhecloud.cube.base.file.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.server.minio.entity.FileStoreSignUp;

/**
 * @author pengjian
 * @date 2022/7/19 11:19
 * @description 路由策略
 */
public interface FileStoreRouteStrategy extends IService<FileStoreSignUp> {

    /**
     * 获取File Store
     *
     * @return
     */
    @Deprecated
    FileStore getFileStore();

    FileStore getFileStore(Long configId);
}
