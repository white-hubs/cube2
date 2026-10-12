package com.guhecloud.cube.biz.mdata.server.stat.vo;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2022/4/19
 */
@Data
public class AreaSupplierStatVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 认证类型
     */
    private PurchaserTypeEnum type;

}
