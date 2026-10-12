package com.guhecloud.cube.sn.meattrade.plugin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PorkArrangeDetail {

    /**
     * 理货编号
     */
    private String code;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备号
     */
    private String porkReportCode;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标编码
     */
    private String footMarkCode;

    /**
     * 净重
     */
    private BigDecimal weight;

    /**
     * 重量是否达标
     */
    private Boolean weightCompliant;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 是否作弊
     */
    private Boolean cheat;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 附件主键集合
     */
    private String transferFiles;

    /**
     * 转存后的图片地址
     */
    private String transferPaths;
}
