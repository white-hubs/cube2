package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 实体绑定应用
 * @date 2023/10/23 10:36
 */
@Data
@TableName("qrcode_entity_application")
@EqualsAndHashCode(callSuper = true)
public class QrcodeEntityApplicationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 应用id
     */
    private Long applicationId;
    /**
     * 是否选中
     */
    private Boolean selected;
}
