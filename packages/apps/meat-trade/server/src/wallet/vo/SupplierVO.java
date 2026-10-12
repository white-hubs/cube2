package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import lombok.Data;

/**
 * 商户信息
 *
 * @author Woody
 * @date 2021/9/13
 */
@Data
public class SupplierVO {
    /**
     * 商户主键
     */
    private Long id;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 联系人电话
     */
    private String phone;

    /**
     * 联系人姓名
     */
    private String liaison;
}
