package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.WaterRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.result.WaterRechargePageResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 水表充值记录
 */
public interface WaterRechargeService {
    Pagination<WaterRechargePageResult> getWaterRechargePage(WaterRechargePageParam param);
}
