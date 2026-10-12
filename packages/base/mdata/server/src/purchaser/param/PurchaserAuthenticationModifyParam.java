package com.guhecloud.cube.biz.mdata.server.purchaser.param;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/24
 */
@Data
public class PurchaserAuthenticationModifyParam {

    /**
     * 采购商主键
     */
    private Long purchaserId;

    /**
     * 认证状态
     */
    private PurchaserAuthenticationEnum status;

    /**
     * 认证类型
     */
    @NotNull(message = "认证类型不能为空")
    private PurchaserTypeEnum type;

    /**
     * 认证类型名称
     */
    @Size(max = 100, message = "认证类型名称不能大于100")
    private String typeName;

    /**
     * 认证类型编码
     */
    @Size(max = 100, message = "认证类型编码不能大于100")
    private String typeCode;

    /**
     * 代表人姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @Size(max = 20, message = "姓名不能大于20")
    private String delegateName;

    /**
     * 代表人身份证号码
     */
    @NotEmpty(message = "身份证不能为空")
    @Size(max = 20, message = "身份证不能大于20")
    private String delegateIdentityCard;

    /**
     * 营业执照文件主键
     */
    private Long licenseId;

    /**
     * 身份证正面文件主键
     */
    @NotNull(message = "身份证正面不能为空")
    private Long frontIdentityCardId;

    /**
     * 身份证反面文件主键
     */
    @NotNull(message = "身份证反面不能为空")
    private Long backIdentityCardId;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

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
     * 数据来源
     */
    private AuthenticationSourceEnum source;

}
