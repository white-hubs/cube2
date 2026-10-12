package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "meattrade_pork_report")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PorkReport extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 报备号
     */
    private String code;
    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 档位主键
     */
    private Long stallId;

    /**
     * 屠宰场主键
     */
    private Long abattoirId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 报备片数
     */
    private Integer reportSlices;

    /**
     * 报备重量
     */
    @Decimal
    private BigDecimal reportWeight;

    /**
     * 报备时间
     */
    private LocalDateTime reportTime;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 到达时间
     */
    private LocalDateTime arrivedTime;

    /**
     * 车辆号码
     */
    private String vehiclePlate;

    /**
     * 检疫证号
     */
    private String pratiqueCode;

    /**
     * 产地名称
     */
    private String originPlace;

    /**
     * 报备状态
     */
    private PorkReportEnum status;

    /**
     * 审核人主键
     */
    private Long auditorId;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 租户编号
     */
    private Integer tenant;
}
