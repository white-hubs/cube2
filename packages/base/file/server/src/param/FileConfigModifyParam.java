package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2022/2/24
 */
@Data
public class FileConfigModifyParam {
    private Long id;

    /**
     * OSS域名
     */
    @NotEmpty(message = "OSS域名不能为空")
    private String endpoint;

    /**
     * 文件存储名称
     */
    @NotEmpty(message = "bucket不能为空")
    private String bucket;

    /**
     * 访问密钥主键
     */
    @NotEmpty(message = "accessKeyId不能为空")
    private String accessKeyId;

    /**
     * 访问密钥
     */
    @NotEmpty(message = "accessKeySecret不能为空")
    private String accessKeySecret;
    private String accessPrefix;
    /**
     * 市场租户编号
     */
    @NotNull(message = "市场租户编号")
    private Integer tenant;
    @NotNull(message = "存储对象服务不能为空")
    private String tag;
    @NotNull(message = "实现类型不能为空")
    private String storageObjectType;

}
