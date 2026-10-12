package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登记检疫票
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "meattrade_pratique_register")
public class PratiqueRegister extends BasicEnhancedEntity<Long> {
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
     * 登记员主键
     */
    private Long registrantId;
    /**
     * 检疫片数
     */
    private Integer pratiqueSlices;
    /**
     * 检疫票数
     */
    private Integer pratiqueTickets;
    /**
     * 检疫证号
     */
    private String pratiqueCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否登记
     */
    private Boolean status;
    /**
     * 登记时间
     */
    private LocalDateTime registerTime;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
