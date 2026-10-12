package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理货数据信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "meattrade_pork_arrange")
public class PorkArrange extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备号
     */
    private String porkReportCode;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货设备台号
     */
    private String terminalNumber;

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 市场方租户编号
     */
    private Integer tenant;

}
