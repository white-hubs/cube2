package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 食品安全检查
 * @date 2023-12-04 10:08:36
 */
@Data
public class GoodsSafetyCheckVO {

    //
    private Long id;
    //档位号
    private String stallCode;
    //品类名
    private String goodsCategoryName;
    //产地
    private String goodsFrom;
    //检测日期
    private LocalDateTime checkDate;
    //检测项
    private String checkItem;
    //检测结果
    private String checkResult;
    //检测人
    private String checkerName;
    //样品编号
    private String sampleCode;
}