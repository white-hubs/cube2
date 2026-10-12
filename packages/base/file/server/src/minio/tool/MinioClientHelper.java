package com.guhecloud.cube.base.file.server.minio.tool;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/7/18 19:06
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MinioClientHelper {
    private final FileConfigMapper fileConfigMapper;

    @Deprecated
    public MinioClient getMinioClient(Integer tenant) {
        FileConfig fileConfig = this.fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant)
                                                                        .last("limit 1"));
        MinioClient minioClient = MinioClient.builder().endpoint(fileConfig.getEndpoint())
                .credentials(fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret()).build();
        return minioClient;
    }

    public MinioClient getMinioClient(FileConfig fileConfig) {
        return MinioClient.builder().endpoint(fileConfig.getEndpoint())
                .credentials(fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret()).build();
    }

}
