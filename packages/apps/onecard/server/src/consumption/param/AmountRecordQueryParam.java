package com.guhecloud.cube.onecard.server.consumption.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AmountRecordQueryParam extends Paging {
    private String enterParam;
    private String transactionType;//交易类型 1, 消费 2 充值 3 补贴 4 补贴清零 5金额消费纠错 6 退款 7 份消费 8份补贴 9 份清零
    private String period;
}
