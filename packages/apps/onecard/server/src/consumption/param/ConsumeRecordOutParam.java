package com.guhecloud.cube.onecard.server.consumption.param;

import com.guhecloud.cube.onecard.server.consumption.enumeration.RiXinDaInterfaceEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 17:44
 * @Description:
 */
@Data
public class ConsumeRecordOutParam extends Paging {

    /**
     * 交易类型
     */
    private RiXinDaInterfaceEnum interfaceEnum;

    /**
     * 交易时间
     */
    private Period period;

    /**
     * 账户id
     */
    private Long accountId;
}
