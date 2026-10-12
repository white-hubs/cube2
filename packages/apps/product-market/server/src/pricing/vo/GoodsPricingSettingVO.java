package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 采价通知规则
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingSettingVO {

    //
    private Long id;
    //编码
    private String code;
    //延迟通知时间
    private int noticeMinute;

}