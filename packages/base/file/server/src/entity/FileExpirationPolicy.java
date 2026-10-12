package com.guhecloud.cube.base.file.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author lijei
 * @date 2022/1/5
 */
@Data
public class FileExpirationPolicy extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 场景编码
     */
    private String sceneCode;
    /**
     * 文件过期策略枚举类型
     */
    private String expirationPolicy;
    /**
     * 配置具体信息
     */
    private String configInfo;
}
