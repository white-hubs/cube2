package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.controller;

import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.param.RentRecordQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentRecordResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentedSoldStatResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.UtilizationRateStatResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.service.RentRecordService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租控记录接口管理
 *
 * @author lanchun
 * @date 2024/8/28 15:24
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RentRecordController {

    private final RentRecordService rentRecordService;

    /**
     * 分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/rentRecord/page")
    public Pagination<RentRecordResult> getPage(RentRecordQueryParam param) {
        return rentRecordService.getPage(param);
    }

    /**
     * 空间利用率统计
     *
     * @return 结果
     */
    @GetMapping("/rentRecord/stat/utilizationRate")
    public UtilizationRateStatResult utilizationRateStat() {
        return rentRecordService.utilizationRateStat();
    }

    /**
     * 租售记录情况统计
     *
     * @return 租售记录
     */
    @GetMapping("/rentRecord/stat/rentedSold")
    public RentedSoldStatResult rentedSoldStat() {
        return rentRecordService.rentedSoldStat();
    }
}
