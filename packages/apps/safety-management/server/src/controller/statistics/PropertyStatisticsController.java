package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FilingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource.RiskSourceMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FilingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FlammableDeviceFilingVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.MarketNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyByTypePageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingAnalyseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.RiskSourceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 资产统计
 * @date 2023/9/8 17:07
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyStatisticsController {
    private final PropertyStatisticsService propertyStatisticsService;
    private final MarketConfigService marketConfigService;

    /**
     * 资产设备列表
     *
     * @Author wql
     * @Description 建档设备统计
     * @Date 13:47 2023/9/8
     **/
    @GetMapping("/property/deviceList")
    public Pagination<FilingDeviceVO> getDeviceExpireList(@Valid FilingDeviceQuery expiredDevice) {
        Pagination<FilingDeviceVO> deviceExpireList = propertyStatisticsService.getDeviceExpireList(expiredDevice);
        if (CollectionUtils.isEmpty(deviceExpireList.getRecords())) {
            return deviceExpireList;
        }
        Set<Integer> marketTenants = deviceExpireList.getRecords().stream().filter(deviceVO -> Objects.nonNull(deviceVO.getMarketTenant()))
                .map(deviceVO -> deviceVO.getMarketTenant())
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(marketTenants)) {
            List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                         .in(MarketConfigPO::getMarketTenant, marketTenants));
            deviceExpireList.getRecords().stream().forEach(deviceVO -> {
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS
                        .stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() == deviceVO.getMarketTenant().intValue())
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       deviceVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            });
        }
        return deviceExpireList;
    }

    /**
     * 资产建档分析
     *
     * @Author wql
     * @Description 资产建档分析
     * @Date 17:11 2023/9/8
     **/
    @GetMapping("/property/filingAnalyse")
    public List<PropertyFilingAnalyseVO> getFilingAnalyse(BaseQuery query) {
        return propertyStatisticsService.getFilingAnalyse(query);
    }

    /**
     * 消防设备到期统计
     *
     * @Author wql
     * @Description 消防设备到期统计
     * @Date 13:47 2023/9/8
     **/
    @GetMapping("/property/deviceExpire")
    public Pagination<MarketNumStatisticsVO> getDeviceExpireStatistics(BaseQuery query) {
        return propertyStatisticsService.getDeviceExpireStatistics(query);
    }

    /**
     * 易燃设备建档统计
     *
     * @Author wql
     * @Description 易燃设备建档统计
     * @Date 18:39 2023/9/13
     **/
    @GetMapping("/property/flammableDevice")
    public FlammableDeviceFilingVO getFlammableDeviceStatistics(BaseQuery query) {
        return propertyStatisticsService.getFlammableDeviceStatistics(query);
    }

    /**
     * 易燃设备列表
     *
     * @Author wql
     * @Description 易燃设备列表
     * @Date 16:26 2023/9/20
     **/
    @GetMapping("/property/flammableDeviceList")
    public Pagination<MarketNumStatisticsVO> getFlammableDeviceList(BaseQuery query) {
        return propertyStatisticsService.getFlammableDeviceList(query);
    }

    /**
     * 资产建档统计
     *
     * @Author wql
     * @Description 资产建档统计
     * @Date 19:25 2023/9/13
     **/
    @GetMapping("/property/propertyFilingStatistics")
    public PropertyFilingStatisticsVO getPropertyFilingStatistics(BaseQuery query) {
        return propertyStatisticsService.getPropertyFilingStatistics(query);
    }

    /**
     * 资产建档统计轮播
     *
     * @Author wql
     * @Description 资产建档统计轮播
     * @Date 16:32 2023/9/20
     **/
    @GetMapping("/property/propertyFilingList")
    public Pagination<MarketNumStatisticsVO> getPropertyFilingList(BaseQuery query) {
        return propertyStatisticsService.getPropertyFilingList(query);
    }

    /**
     * 资产类别列表
     *
     * @Author wql
     * @Description 资产类别列表
     * @Date 19:11 2023/9/13
     **/
    @GetMapping("/property/propertyTypeList")
    public List<KeyValueVO> getPropertyTypeList() {
        return propertyStatisticsService.getPropertyTypeList();
    }

    /**
     * 风险源列表
     *
     * @author: lijie
     * @date: 2024/3/15 9:48
     * @param:
     * @return:
     **/
    @GetMapping("/property/propertyTypePage")
    public Pagination<PropertyByTypePageVO> getPropertyTypePage(BaseQuery query) {
        return propertyStatisticsService.getPropertyTypePage(query);
    }

    /**
     * 根据风险源类型统计状态数
     *
     * @return
     */
    @GetMapping("/safety/statistics/property/risk-source/count-status")
    public List<RiskSourceCountStatusVO> countStatus(RiskSourceMetricQuery query) {
        return propertyStatisticsService.countStatusByType(query);
    }

    /**
     * 柱状图
     *
     * @return
     */
    @GetMapping("/safety/statistics/property/vehicle-bar")
    public Pagination<StackedBarChartVO> getVehicleCountBar(RiskSourceMetricQuery param) {
        if (CollectionUtils.isEmpty(param.getMarketTenantIDs())) {
            List<MarketConfigPO> configPOList = marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                 .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(configPOList)) {
                return Pagination.empty();
            }
            List<String> tenantList = configPOList.stream().map(c -> String.valueOf(c.getMarketTenant())).collect(Collectors.toList());
            param.setMarketTenantIDs(tenantList);
        }
        return propertyStatisticsService.getVehicleCountBar(param);
    }

    /**
     * 饼图
     *
     * @return
     */
    @GetMapping("/safety/statistics/property/vehicle-pie")
    public List<AlarmEventTypeRatioVO> getVehicleCountPie(RiskSourceMetricQuery param) {
        if (CollectionUtils.isEmpty(param.getMarketTenantIDs())) {
            List<MarketConfigPO> configPOList = marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                 .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(configPOList)) {
                return Collections.emptyList();
            }
            List<String> tenantList = configPOList.stream().map(c -> String.valueOf(c.getMarketTenant())).collect(Collectors.toList());
            param.setMarketTenantIDs(tenantList);
        }
        return propertyStatisticsService.getVehicleCountPie(param);
    }

    /**
     * 风险源建档数量分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/risk/source/count")
    public List<GroupCountVO> riskSourceCountByMarket(BaseStatisticsQuery query) {
        return propertyStatisticsService.countByMarket(query);
    }

    /**
     * 风险源类型数量分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/risk/source/count/type")
    public List<GroupCountVO> riskSourceCountByType(RiskSourceMetricQuery query) {
        return propertyStatisticsService.countByType(query);
    }
}
