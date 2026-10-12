package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 电表充值记录返回对象
 */
@Data
public class PowerRechargeResult {
    private Long id;
    /**
     * 电表编号
     */
    private String meterId;
    /**
     * 充值时间(对应第三方接口的创建时间createTime)
     */
    private LocalDateTime rechargeTime;
    /**
     * 售电金额
     */
    private BigDecimal saleMoney;
    /**
     * 购电类型
     * 现金缴费= 1/刷卡机缴费= 2/微信扫码支付= 3/  支付宝扫码支付= 4/公对公转账= 5/
     * 冲正或退电= 6/微信支付= 7/支付宝支付= 8/    容量费= 9/充值批量导入= 10/网银转账= 11/
     * 公摊电费= 12/基础电费= 13/在线卡充值= 20/   电费退补= 14
     */
    private Integer buyType;
    /**
     * 售电类型
     * 正常售电= 1/冲正= 2/退电= 3 /   初始化= 4/容量费= 5
     */
    private Integer saleType;
    /**
     * 上次累计金额
     */
    private BigDecimal lastMoney;
}
