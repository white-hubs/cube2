package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 实体类型关联二维码样式
 * @date 2024/1/30 10:14
 */
@Data
@TableName("qrcode_entity_style")
@EqualsAndHashCode(callSuper = true)
public class QrcodeEntityStylePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 实体类型
     */
    private Long entityType;
    /**
     * 样式id
     */
    private Long styleId;

}
