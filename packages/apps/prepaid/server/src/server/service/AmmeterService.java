package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.server.param.AmmeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.AmmeterPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UnConnectAmmeterStatResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 电表服务接口
 */
public interface AmmeterService {
    Pagination<AmmeterPageResult> getAmmeterPage(AmmeterPageParam param);

    List<String> getAmmeterIdList();

    UnConnectAmmeterStatResult getUnConnectAmmeterStat();
}
