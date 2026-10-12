package com.guhecloud.cube.base.file.server.vo;

import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lijie
 * @date 2023/5/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileConfigVO extends BasicEntity<Long> {

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
    private String type;
}
