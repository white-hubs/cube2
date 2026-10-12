package com.guhecloud.cube.biz.meattrade.server.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 会员卡数据模型
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("meattrade_vip_card")
public class VipCard extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 会员卡号
     */
    @Unique(composites = "deleted", message = "会员卡号已存在")
    private String code;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 会员卡内容
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        /**
         * 商户主键
         */
        private Long supplierId;

        /**
         * 会员卡号
         */
        private String code;
    }
}
