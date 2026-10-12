package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.wallet.client.enumeration.MoneyFlow;
import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;

/**
 * 我的交易记录查询参数
 *
 * @author Woody
 * @date 2021/9/14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MineTransactionQueryParam extends Paging {
    /**
     * 资金流向
     */
    @NotNull(message = "资金流向不能为空")
    private MoneyFlow flow;
}
