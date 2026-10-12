package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 19:36
 * @Description:
 */
@Data
public class RiXinDaConsumeRecordOutVO {

    /**
     * 系统编号
     */
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 物理卡号
     */
    private String cardNumber;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 补贴金额
     */
    private BigDecimal subsidy;

    /**
     * 消费金额
     */
    private BigDecimal amount;

    /**
     * 消费时间
     */
    private LocalDateTime consumptionDate;

    /**
     * 消费类型
     */
    private Integer type;

    /**
     * 消费设备
     */
    private String orgId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 账户类型
     */
    private Integer mode;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 设备名称
     */
    private String orgName;

    /**
     * 次数
     */
    private Long times;

    /**
     * 账户类型
     */
    private Long accountType;

    /**
     * 退款时间
     */
    private LocalDateTime refundDate;

    /**
     * 退款金额
     */
    private BigDecimal cash;

    /**
     * 充减值时间
     */
    private LocalDateTime rechargeDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
