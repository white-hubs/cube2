package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.biz.mdata.server.purchaser.enumeration.AuthenticationSourceEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Data
public class SupplierAuthenticationPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 商户名称
     */
    private String supplierName;

    /**
     * 认证主体名称
     */
    private String typeName;

    /**
     * 代表名称
     */
    private String delegateName;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 认证状态
     */
    private SupplierAuthenticatedEnum status;

    /**
     * 认证类型
     */
    private PurchaserTypeEnum type;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 来源
     */
    private AuthenticationSourceEnum source;
}
