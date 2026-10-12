package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 10:33
 */
@Data
@TableName("qrcode_entity_type")
@EqualsAndHashCode(callSuper = true)
public class QrcodeEntityTypePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 实体名称
     */
    private String typeName;
    /**
     * 实体类型
     */
    private String entityType;
}
