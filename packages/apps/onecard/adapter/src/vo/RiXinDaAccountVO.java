package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 20:09
 * @Description:
 */
@Data
public class RiXinDaAccountVO {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 卡片状态
     */
    private Integer cardStatus;

    /**
     * 现金余额（单位：分）
     */
    private BigDecimal cashBalance;

    /**
     * 补贴金额（单位：分）
     */
    private BigDecimal subsidyAmount;

    /**
     * 理发券金额（单位：分）
     */
    private BigDecimal hairCouponAmount;

    /**
     * 洗衣次数
     */
    private Integer laundryCount;

    /**
     * 创建时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime createTime;

    /**
     * 删除标志
     */
    private String deleteFlag;
    /**
     * 预留金额（单位：分）
     */
    private BigDecimal reserveAmount;

    /**
     * 预留次数
     */
    private Integer reserveCount;
    /**
     * 状态：
     * 0 - 禁用
     * 1 - 启用
     */
    private Integer status;

    /**
     * 更新时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private LocalDateTime updateTime;

}
