package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

import java.util.List;

/**
 * 理货机返回数据
 */
@Data
public class TallyVO {

    /**
     * 商户主键
     */
    private String merchantId;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 产品主键
     */
    private String productId;

    /**
     * 理货批次集合
     */
    private List<TallyReportVO> batches;

}
