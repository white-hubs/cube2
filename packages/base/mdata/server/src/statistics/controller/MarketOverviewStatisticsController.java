package com.guhecloud.cube.biz.mdata.server.statistics.controller;

import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.statistics.vo.MarketOverviewStatisticsVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 市场总览
 *
 * @author lanchun
 * @date 2023/11/14 9:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketOverviewStatisticsController {

    private final TenantClient tenantClient;

    private final StallService stallService;

    /**
     * 获取当前市场总览统计信息
     *
     * @return
     */
    @GetMapping("/market-overview-statistics")
    public MarketOverviewStatisticsVO getMarketOverviewStatistics() {
        TenantDTO tenantDTO = this.tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
        long stallCount = this.stallService.count();
        long supplierCount = this.stallService.getStallMasterCount();
        return MarketOverviewStatisticsVO.builder()
                .id(tenantDTO.getId())
                .code(tenantDTO.getCode())
                .name(tenantDTO.getName())
                .areaCovered(tenantDTO.getAreaCovered())
                .floorArea(tenantDTO.getFloorArea())
                .remark(tenantDTO.getRemark())
                .files(tenantDTO.getFiles())
                .stallNum(stallCount)
                .supplierNum(supplierCount)
                .build();
    }
}
