package com.guhecloud.cube.base.file.server.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-12  16:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileExpirationPolicyDTO {
    private Long id;
    /**
     * 场景编码
     */
    @NotNull(message = "场景编码不能为空")
    private String sceneCode;
    /**
     * 文件过期策略
     */
    @NotNull(message = "文件过期策略不能为空")
    private String expirationPolicy;
    /**
     * 配置具体信息
     */
    @NotNull(message = "文件过期配置不能为空")
    private String configInfo;
}
