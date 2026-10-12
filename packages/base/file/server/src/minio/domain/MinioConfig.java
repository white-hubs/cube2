package com.guhecloud.cube.base.file.server.minio.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  18:51
 * @Description:
 * @Version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "file")
@Data
public class MinioConfig {
    private String type;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String accessPrefix;

}
