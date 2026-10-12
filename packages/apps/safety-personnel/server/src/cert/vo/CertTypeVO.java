package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import lombok.Data;

/**
 * 证书类型响应。
 */
@Data
public class CertTypeVO {

    /**
     * 证书类型ID。
     */
    private Long id;

    /**
     * 证书分类ID。
     */
    private Long categoryId;

    /**
     * 证书分类名称。
     */
    private String categoryName;

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

    public static CertTypeVO of(CertType type) {
        CertTypeVO vo = new CertTypeVO();
        vo.setId(type.getId());
        vo.setCategoryId(type.getCategoryId());
        vo.setTypeName(type.getTypeName());
        vo.setDefinition(type.getDefinition());
        vo.setValidPeriod(type.getValidPeriod());
        vo.setValidPeriodUnit(type.getValidPeriodUnit());
        vo.setWarnDays(type.getWarnDays());
        vo.setNeedReview(type.getNeedReview());
        vo.setReviewPeriod(type.getReviewPeriod());
        vo.setReviewPeriodUnit(type.getReviewPeriodUnit());
        return vo;
    }
}
