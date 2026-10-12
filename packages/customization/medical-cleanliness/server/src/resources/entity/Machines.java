package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 机组
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Machines extends BasicEnhancedTenantEntity<Long> {
    Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 参数
     */
    private String arguments;
    /**
     * 附件（文件ids）
     */
    private String fileIds;
    /**
     * 关联的手术室ids
     */
    private String roomIds;
}
