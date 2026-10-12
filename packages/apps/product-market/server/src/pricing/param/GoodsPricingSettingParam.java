package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author Appet
 * @Description 采价设置
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingSettingParam {
    //编号
    @NotEmpty(message = "编号不能为空")
    private String code;
    //时间
    private int noticeMinute;

}