package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供货商品跟进信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_track")
public class Track extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 理货审核主键
     */
    private Long porkArrangeAuditId;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
