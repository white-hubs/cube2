package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

/**
 * 充值商户查询参数
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepositMerchantQueryParam extends Paging {
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
