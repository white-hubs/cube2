package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物业合同数据模型实体
 *
 * @author lanchun
 * @date 2024/8/28 16:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@TableName(value = "V_ZHYQ_YQWY_CONTRACT")
public class EstateContractPO {

    /**
     * 顺序号
     */
    @TableField(value = "ID")
    private Long id;

    /**
     * 客户ID
     */
    @TableField(value = "CUSTID")
    private String custId;

    /**
     * 联系人
     */
    @TableField(value = "CONTACTS")
    private String contacts;

    /**
     * 客户手机号码
     */
    @TableField(value = "CUSTPHONE")
    private String custPhone;

    /**
     * 合同编号
     */
    @TableField(value = "CONTRACTNO")
    private String contractNo;

    /**
     * 合同名称
     */
    @TableField(value = "CONTRACTNAME")
    private String contractName;

    /**
     * 经营类型
     */
    @TableField(value = "BUSINESSTYPE")
    private String businessType;

    /**
     * 装修期
     */
    @TableField(value = "ZXQ")
    private LocalDateTime zxq;

    /**
     * 装修期是否免租
     */
    @TableField(value = "IS_ZXQ_CHARGE")
    private Integer isZxqCharge;

    /**
     * 签订日期
     */
    @TableField(value = "SIGNDATE")
    private LocalDateTime signDate;

    /**
     * 起租日期
     */
    @TableField(value = "STARTDATE")
    private LocalDateTime startDate;

    /**
     * 止租日期
     */
    @TableField(value = "ENDDATE")
    private LocalDateTime endDate;

    /**
     * 合同预警天数
     */
    @TableField(value = "WARNDAY")
    private Integer warnDay;

    /**
     * 押金
     */
    @TableField(value = "DEPOSIT")
    private BigDecimal deposit;

    /**
     * 来源
     */
    @TableField(value = "SOURCE")
    private Integer source;

    /**
     * 来源ID
     */
    @TableField(value = "SOURCE_ID")
    private Integer sourceId;

    /**
     * 送审状态
     */
    @TableField(value = "STATUS")
    private Integer status;

    /**
     * 创建日期
     */
    @TableField(value = "CREATETIME")
    private LocalDateTime createTime;

    /**
     * 公司编码
     */
    @TableField(value = "UNITCODE")
    private String unitCode;

    /**
     * 资料
     */
    @TableField(value = "ENCLOSURE")
    private String enclosure;

    /**
     * 送审标志
     */
    @TableField(value = "SPFLAG")
    private String spflag;

    /**
     * 签订人
     */
    @TableField(value = "USERID")
    private Long userId;

    /**
     * 是否预定
     */
    @TableField(value = "IS_RESERVE")
    private Integer isReserve;

    /**
     * 是否续签
     */
    @TableField(value = "IS_CONTINUE")
    private Integer isContinue;

    /**
     * 预警状态
     */
    @TableField(value = "WARN_STATUS")
    private Integer warnStatus;

    /**
     * 意向ID
     */
    @TableField(value = "INTENTION_ID")
    private Long intentionId;

    /**
     * 止租日期
     */
    @TableField(value = "TERMINATE_DATE")
    private LocalDateTime terminateDate;

    /**
     * 合同类型
     */
    @TableField(value = "CONTRACT_TYPE")
    private Integer contractType;

    /**
     * 合同逾期天数
     * 负数为逾期
     */
    @TableField(value = "OVERDUEDAY")
    private Integer overdueDay;

}
