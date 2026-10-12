package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.PowerRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.result.PowerRechargeResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 充值记录
 */
public interface PowerRechargeService {
    Pagination<PowerRechargeResult> getPowerRechargePage(PowerRechargePageParam param);
}
