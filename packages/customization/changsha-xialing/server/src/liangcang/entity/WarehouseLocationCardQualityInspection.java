package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 货位卡质量检测记录。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_location_card_quality_inspection")
public class WarehouseLocationCardQualityInspection extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 检测时间。 */
    private LocalDateTime inspectionTime;
    /** 水分，单位 %。 */
    private BigDecimal moisture;
    /** 杂质，单位 %。 */
    private BigDecimal impurity;
    /** 出糙率，单位 %。 */
    private BigDecimal roughness;
    /** 色泽气味。 */
    private String colorSmell;
    /** 互混率，单位 %。 */
    private BigDecimal mixedRate;
    /** 整精米率，单位 %。 */
    private BigDecimal headRiceRate;
    /** 谷外糙米，单位 %。 */
    private BigDecimal outOfPaddyBrownRiceRate;
    /** 黄粒米，单位 %。 */
    private BigDecimal yellowRiceRate;
    /** 品尝分值。 */
    private BigDecimal tasteScore;
    /** 脂肪酸值，单位 mgKOH/100g。 */
    private BigDecimal fattyAcid;
    /** 镉，单位 mg/kg。 */
    private BigDecimal cadmium;
    /** 黄曲霉毒素B1，单位 μg/kg。 */
    private BigDecimal aflatoxinB1;
    /** 铅，单位 mg/kg。 */
    private BigDecimal lead;
    /** 汞，单位 mg/kg。 */
    private BigDecimal mercury;
    /** 无机砷，单位 mg/kg。 */
    private BigDecimal inorganicArsenic;
    /** 检测人。 */
    private String inspector;
}
