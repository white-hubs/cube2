package com.guhecloud.cube.base.file.server.minio.entity;

import com.guhecloud.cube.base.file.server.enumeration.FileStoreType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/7/18 16:53
 * @description 文件存储注册
 */
@Data
public class FileStoreSignUp extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String tag;
    private Long fileStore;
    private FileStoreType type;
}
