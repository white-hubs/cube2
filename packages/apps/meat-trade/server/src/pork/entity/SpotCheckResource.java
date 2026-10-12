package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 食安溯源信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_spot_check_resource")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotCheckResource extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 来货报备号
     */
    private String porkReportCode;

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
