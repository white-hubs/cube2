package com.guhecloud.cube.biz.meattrade.server.order.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2021/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "商户主键不能大于100")
    private String keyword;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 结算状态
     */
    private Boolean status;

    /**
     * 时间
     */
    private Period period;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
