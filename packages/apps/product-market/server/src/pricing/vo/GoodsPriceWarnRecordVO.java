package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 采价预警记录
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRecordVO {

    //
    private Long id;
    //预警名称
    private String ruleName;
    //规则ID
    private Long ruleId;
    //采价记录ID
    private Long pricingRecordId;
    //采价员ID
    private Long userId;
    //采价员姓名
    private String userName;
    //所在部门ID
    private Long deptId;
    //所在部门名称
    private String deptName;
    //采价时间
    private LocalDateTime pricingTime;
    //商品品类
    private Long goodsCategoryId;
    //商品品类名称
    private String goodsCategoryName;
    //最高价
    private BigDecimal maxPrice;
    //中间价
    private BigDecimal midPrice;
    //最低价
    private BigDecimal minPrice;
    //价格是否异常
    private Boolean priceExp;
    //价格异常描述
    private String priceExpDesc;
    //备注
    private String remark;
    //处理人ID
    private Long handlerId;
    //是否处理
    private Boolean status;

}