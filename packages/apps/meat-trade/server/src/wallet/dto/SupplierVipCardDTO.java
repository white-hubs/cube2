package com.guhecloud.cube.biz.meattrade.server.wallet.dto;

import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户会员卡信息
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierVipCardDTO extends VipCard {
    /**
     * 商户名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 联系人姓名
     */
    private String liaison;
}
