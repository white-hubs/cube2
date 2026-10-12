package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物业合同返回对象
 */
@Data
public class EstateContractResult {
    private Long id;
    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 经营类型
     */
    private String businessType;

    /**
     * 装修期
     */
    private LocalDateTime zxq;

    /**
     * 装修期是否免租
     */
    private Integer isZxqCharge;

    /**
     * 签订日期
     */
    private LocalDateTime signDate;

    /**
     * 起租日期
     */
    private LocalDateTime startDate;

    /**
     * 止租日期
     */
    private LocalDateTime endDate;

    /**
     * 合同预警天数
     */
    private Integer warnDay;

    /**
     * 是否预定
     */
    private Integer isReserve;

    /**
     * 是否续签
     */
    private Integer isContinue;

    /**
     * 预警状态
     */
    private Integer warnStatus;

    /**
     * 合同类型
     */
    private Integer contractType;

    /**
     * 合同逾期天数
     * 负数为逾期
     */
    private Integer overdueDay;
}
