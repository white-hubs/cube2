package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.service;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.param.EstateContractQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.result.EstateContractResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 物业合同服务
 */
public interface EstateContractService {
    /**
     * 物业合同分页
     *
     * @param param 条件
     * @return 分页
     */
    Pagination<EstateContractResult> getPage(EstateContractQueryParam param);

    /**
     * 获取租赁趋势
     *
     * @param param 条件
     * @return 趋势
     */
    List<TrendResult> getTrend(EstateContractQueryParam param);
}
