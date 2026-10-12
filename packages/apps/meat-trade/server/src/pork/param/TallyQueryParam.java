package com.guhecloud.cube.biz.meattrade.server.pork.param;

import lombok.Data;

/**
 * 理货批次查询
 */
@Data
public class TallyQueryParam {

    /**
     * 理货机编号
     */
    private String deviceId;

    /**
     * 商户标识类型
     */
    private String midType;

    /**
     * 商户标识值
     */
    private String midValue;

    /**
     * 商户名称
     */
    private String midName;

    /**
     * 商户编号
     */
    private String midCode;

    /**
     * 批次号
     */
    private String tallyId;
}
