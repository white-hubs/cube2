package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.service;

import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.param.RentRecordQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentRecordResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentedSoldStatResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.UtilizationRateStatResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 租控记录服务
 */
public interface RentRecordService {
    /**
     * 分页
     *
     * @param param 条件
     * @return 分页
     */
    Pagination<RentRecordResult> getPage(RentRecordQueryParam param);

    /**
     * 空间利用率统计
     *
     * @return 结果
     */
    UtilizationRateStatResult utilizationRateStat();

    /**
     * 租售记录情况统计
     *
     * @return 租售记录
     */
    RentedSoldStatResult rentedSoldStat();
}
