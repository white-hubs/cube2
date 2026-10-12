package com.guhecloud.cube.srv.gen.park.management.server.enumeration;

import lombok.Getter;

/**
 * 企业规模
 */
@Getter
public enum EnterpriseScaleEnum {

    /**
     * 10人以下
     */
    TEN_BELOW("10人以下"),

    /**
     * 10人~99人
     */
    TEN("10人~99人"),

    /**
     * 100人~199人
     */
    ONE_HUNDRED("100人~199人"),

    /**
     * 200人~500人
     */
    TWO_HUNDRED("200人~500人"),

    /**
     * 500人以上
     */
    FILE_HUNDRED("500人以上"),

    ;

    /**
     * 描述信息
     */
    private final String description;

    EnterpriseScaleEnum(String description) {
        this.description = description;
    }
}
