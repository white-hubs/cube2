package com.guhecloud.cube.base.file.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  19:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class SceneInfo extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String sceneCode;
    private String sceneName;
    private Long configId;
    private Long parentId;
    private String remark;
}
