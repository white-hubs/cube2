package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采价通知规则
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingSetting extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //编码
    private String code;
    //延迟通知时间
    private int noticeMinute;

}