package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Data
public class SupplierAuthenticationModifyParam {

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 认证类型
     */
    private PurchaserTypeEnum type;

    /**
     * 认证类型名称
     */
    private String typeName;

    /**
     * 认证类型编码
     */
    private String typeCode;

    /**
     * 代表人姓名
     */
    private String delegateName;

    /**
     * 代表人身份证号码
     */
    private String delegateIdentityCard;

    /**
     * 营业执照文件主键
     */
    private Long licenseId;

    /**
     * 身份证正面文件主键
     */
    private Long frontIdentityCardId;

    /**
     * 身份证反面文件主键
     */
    private Long backIdentityCardId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 市场租户编号
     */
    @NotNull(message = "市场租户编号不能为空")
    private Integer tenant;

    /**
     * 来源
     */
    private AuthenticationSourceEnum source;

    /**
     * 审核状态
     */
    private SupplierAuthenticatedEnum status;

}
