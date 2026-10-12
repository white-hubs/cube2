package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.RechargeStatus;
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
 * 商户充值记录查询参数
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepositQueryParam extends Paging {
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 创建时间
     */
    private Period period;

    /**
     * 充值状态
     */
    private RechargeStatus status;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
