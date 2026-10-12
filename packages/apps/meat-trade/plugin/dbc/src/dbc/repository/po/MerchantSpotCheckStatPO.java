package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商户食安抽检统计信息
 *
 * @author lanchun
 * @date 2025/1/23 15:55
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("merchant_spot_check_stat")
public class MerchantSpotCheckStatPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 抽检总次数
     */
    private Integer totalQty;

    /**
     * 通过总次数
     */
    private Integer passQty;

    /**
     * 检测合格率
     */
    private String passRate;

}
