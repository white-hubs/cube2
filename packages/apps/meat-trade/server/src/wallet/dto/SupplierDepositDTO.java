package com.guhecloud.cube.biz.meattrade.server.wallet.dto;

import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Deposit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 商户充值信息
 *
 * @author Woody
 * @date 2021/9/9
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SupplierDepositDTO extends Deposit {
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
