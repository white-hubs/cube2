package com.guhecloud.cube.biz.mdata.server.purchaser.entity;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.PurchaserAuthenticationEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PurchaserAuthentication extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

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
    private Integer tenant;
    /**
     * 来源
     */
    private AuthenticationSourceEnum source;

}
