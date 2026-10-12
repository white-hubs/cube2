package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批发商信息
 *
 * @author lzy
 * @date 2021/12/3
 */
@Data
public class SupplierPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商认证主键
     */
    private Long supplierAuthenticationId;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 联系人姓名
     */
    private String liaison;

    /**
     * 联系人手机号
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
     * 地址
     */
    private String address;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
