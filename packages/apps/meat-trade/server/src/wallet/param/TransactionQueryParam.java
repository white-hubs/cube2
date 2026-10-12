package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.BusinessType;
import com.guhecloud.cube.biz.wallet.client.enumeration.MoneyFlow;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

/**
 * 交易记录查询参数
 *
 * @author Woody
 * @date 2021/8/25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransactionQueryParam extends Paging {
    /**
     * 资金流向
     */
    private MoneyFlow flow;

    /**
     * 业务类型
     */
    private BusinessType business;

    /**
     * 创建时间
     */
    private Period period;

    /**
     * 关键字
     */
    @Size(max = 20, message = "关键字长度不能大于20")
    private String keyword;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
