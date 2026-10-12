package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_second_part")
public class WholesalerSecondPart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 对应的合同ID
     */
    private String contractId;

    /**
     * 乙方名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 企业机构代码
     */
    private String companyCode;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 法人身份证
     */
    private String identityCard;

    /**
     * 签约人
     */
    private String signatory;

    /**
     * 签约人号码
     */
    private String signatoryMobile;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    private Integer status;
}
