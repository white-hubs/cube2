package com.guhecloud.cube.property.management.client.risk.source.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:47
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceLedgerExtendInfoVO {
    private Long extendInfoId;
    /**
     * 台账表id
     */
    private Long ledgerId;
    /**
     * 使用人类型
     */
    private String receiveUserType;
    /**
     * 购买日期
     */
    private LocalDateTime purchasingDate;
    /**
     * 功率
     */
    private String efficiency;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 是否购买保险
     */
    private String insurance;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 电动车分类
     */
    private String electronMobileType;
    /**
     * 电动车颜色
     */
    private String electronMobileColour;
}
