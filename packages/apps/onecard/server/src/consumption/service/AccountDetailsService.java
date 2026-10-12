package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.AccountDetails;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;

public interface AccountDetailsService extends IService<AccountDetails> {

    /**
     * 账户明细数据采集
     */
    void syncAccountDetails(OneCardJobParam oneCardJobParam);

}
