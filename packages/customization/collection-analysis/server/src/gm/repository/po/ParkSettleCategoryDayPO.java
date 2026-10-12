package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  18:39
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("park_settle_category_day")
public class ParkSettleCategoryDayPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String goodsCount;
    private String dayToDayFlag;
    private String percent;
    private String deliveryDate;
}
