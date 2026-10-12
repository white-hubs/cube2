package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 食安抽检项
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "meattrade_spot_check_detail")
public class SpotCheckDetail extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 食安检测主键
     */
    private Long spotCheckId;

    /**
     * 检测项字典主键
     */
    private Long spotCheckItemId;

    /**
     * 食安抽检枚举
     */
    private SpotCheckEnum passed;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
