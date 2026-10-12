package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 货位卡字段变更记录。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_location_card_field_change_log")
public class WarehouseLocationCardFieldChangeLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 业务类型。 */
    private String bizType;
    /** 业务记录 id。 */
    private Long bizId;
    /** 变更摘要，多个字段名用、拼接。 */
    private String changeSummary;
    /** 变更明细 JSON。 */
    private String changeContent;
}
