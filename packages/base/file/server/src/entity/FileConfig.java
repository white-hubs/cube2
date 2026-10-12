package com.guhecloud.cube.base.file.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/1/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileConfig extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * OSS域名
     */
    private String endpoint;

    /**
     * 文件存储名称
     */
    private String bucket;

    /**
     * 访问密钥主键
     */
    private String accessKeyId;

    /**
     * 访问密钥
     */
    private String accessKeySecret;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 访问前缀
     */
    private String accessPrefix;
}
