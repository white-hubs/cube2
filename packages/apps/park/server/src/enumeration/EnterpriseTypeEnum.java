package com.guhecloud.cube.srv.gen.park.management.server.enumeration;

import lombok.Getter;

/**
 * 企业类型
 */
@Getter
public enum EnterpriseTypeEnum {

    /**
     * 无限公司
     */
    UNLIMITED_COMPANY("无限公司"),

    /**
     * 有限责任公司
     */
    LIMITED_COMPANY("有限责任公司"),

    /**
     * 两合公司
     */
    LIMITED_PARTNERSHIP("两合公司"),

    /**
     * 股份有限公司
     */
    COMPANIES_LIMITED_BY_SHARES("股份有限公司"),

    /**
     * 股份两合公司
     */
    KGaA("股份两合公司"),

    ;

    /**
     * 描述信息
     */
    private final String description;

    EnterpriseTypeEnum(String description) {
        this.description = description;
    }
}
