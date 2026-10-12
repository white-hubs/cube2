package com.guhecloud.cube.base.file.server.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-12  16:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileExpirationPolicyVO {
    private Long id;
    /**
     * 场景编码
     */
    private String sceneCode;
    private String sceneName;
    /**
     * 文件过期策略枚举类型
     */
    private String expirationPolicy;
    /**
     * 配置具体信息
     */
    private String configInfo;
}
