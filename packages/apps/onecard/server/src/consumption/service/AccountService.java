package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.Account;
import com.guhecloud.cube.onecard.server.consumption.param.AccountInfoQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVO2;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountVo;
import com.guhecloud.platform.base.model.Pagination;

public interface AccountService extends IService<Account> {
    Pagination<AccountVo> getConsumptionInfo(AccountInfoQueryParam param);

    AccountDetailsVo getConsumptionDetails(long id);

    /**
     * 获取指定员工的账户信息
     *
     * @param id
     *
     * @return
     */
    AccountDetailsVO2 getAccountInfo(Long id);
}
