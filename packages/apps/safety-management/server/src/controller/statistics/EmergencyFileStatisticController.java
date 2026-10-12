package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.EmergencyFileQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.EmergencyFileClassifyVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.SafetyEmergencyFileVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.EmergencyFileStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.EmergencyFileStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 预案文件统计
 *
 * @author wql
 * @describe 预案文件统计
 * @date 2023/10/8 17:44
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileStatisticController {
    private final EmergencyFileStatisticService emergencyFileStatisticService;
    private final MarketConfigService marketConfigService;

    /**
     * 预案文件统计
     *
     * @Author wql
     * @Description 预案文件统计
     * @Date 17:51 2023/10/8
     **/
    @GetMapping("/emergencyFile/statistics")
    public EmergencyFileStatisticsVO getEmergencyFileStatistic(BaseQuery query) {
        return emergencyFileStatisticService.getEmergencyFileStatistic(query);
    }

    /**
     * 预案文件明细
     *
     * @Author wql
     * @Description 预案文件明细
     * @Date 19:17 2023/10/8
     **/
    @GetMapping("/emergencyFile/detail")
    public Pagination<SafetyEmergencyFileVO> getEmergencyFileList(EmergencyFileQuery query) {
        Pagination<SafetyEmergencyFileVO> emergencyPagination = emergencyFileStatisticService.getEmergencyFileList(query);
        if (CollectionUtils.isEmpty(emergencyPagination.getRecords())) {
            return emergencyPagination;
        }
        List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list();
        emergencyPagination.getRecords().stream().forEach(safetyEmergencyFileVO -> {
            if (Objects.nonNull(safetyEmergencyFileVO.getMarketTenant())) {
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS.stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() ==
                                                  safetyEmergencyFileVO.getMarketTenant().intValue())
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       safetyEmergencyFileVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            }
        });
        return emergencyPagination;
    }

    /**
     * 事件分类列表
     *
     * @Author wql
     * @Description 事件分类列表
     * @Date 20:00 2023/10/8
     **/
    @GetMapping("/emergencyFile/classifyList")
    public List<EmergencyFileClassifyVO> getEmergencyFileClassify() {
        return emergencyFileStatisticService.getEmergencyFileClassify();
    }

    /**
     * 预案文件数量
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-emergency/count")
    public Integer emergencyFileCount(@SpringQueryMap BaseStatisticsQuery query) {
        query.setCreatedPeriod(null);
        return emergencyFileStatisticService.count(query);
    }
}
