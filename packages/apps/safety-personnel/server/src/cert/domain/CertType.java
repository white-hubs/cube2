package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证书类型。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_cert_type")
public class CertType extends BasicEnhancedTenantEntity<Long> {

    /**
     * 证书类型ID。
     */
    private Long id;

    /**
     * 证书分类ID。
     */
    private Long categoryId;

    /**
     * 证书类型名称。
     */
    private String typeName;

    /**
     * 定义说明。
     */
    private String definition;

    /**
     * 有效期数值。
     */
    private Integer validPeriod;

    /**
     * 有效期单位，DAY/MONTH/YEAR。
     */
    private String validPeriodUnit;

    /**
     * 到期提前提醒天数。
     */
    private Integer warnDays;

    /**
     * 是否需要复审。
     */
    private Boolean needReview;

    /**
     * 复审周期数值。
     */
    private Integer reviewPeriod;

    /**
     * 复审周期单位，DAY/MONTH/YEAR。
     */
    private String reviewPeriodUnit;
}
