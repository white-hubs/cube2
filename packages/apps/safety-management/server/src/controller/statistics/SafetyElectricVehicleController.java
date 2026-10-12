package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionItemStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.ElectricVehicleViolationStatVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.MarketNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyInspectRecordItemService;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyElectricVehicleController {

    private final PropertyStatisticsService propertyStatisticsService;

    private final SafetyInspectRecordItemService safetyInspectRecordItemService;

    /**
     * 电动车总数
     *
     * @return
     */
    @GetMapping("/safety/statistics/property/electric-vehicle/count")
    public Long getElectricVehicleDeviceCount() {
        return propertyStatisticsService.getElectrlcVehicleDeviceCount();
    }

    /**
     * 电动车设备列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/property/electric-vehicle/device/page")
    public Pagination<MarketNumStatisticsVO> getElectricVehicleDeviceList(BaseQuery query) {
        return propertyStatisticsService.getElectricVehicleDeviceList(query);
    }

    /**
     * 电动车违规情况统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/electric-vehicle/violation/stat")
    public ElectricVehicleViolationStatVO getElectricVehicleViolationStat(BaseQuery query) {
        //上期日期
        Period period = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        Integer chargeCount = getElectricVehicleCount(InspectionItemTagEnum.CHARGE.name(), query);
        Integer parkingCount = getElectricVehicleCount(InspectionItemTagEnum.PARKING.name(), query);
        query.setPeriod(period);
        //上期
        Integer chargeLastCount = getElectricVehicleCount(InspectionItemTagEnum.CHARGE.name(), query);
        Integer parkingLastCount = getElectricVehicleCount(InspectionItemTagEnum.PARKING.name(), query);
        return ElectricVehicleViolationStatVO.builder()
                .chargeNum(chargeCount)
                .chargeChainRatio(MathUtils.calculateRingRatioPercentage(chargeCount, chargeLastCount))
                .parkingNum(parkingCount)
                .parkingChainRatio(MathUtils.calculateRingRatioPercentage(parkingCount, parkingLastCount))
                .build();
    }

    private Integer getElectricVehicleCount(String tagCode, BaseQuery query) {
        InspectionItemStatisticsQuery inspectQuery = new InspectionItemStatisticsQuery();
        inspectQuery.setPeriod(query.getPeriod());
        inspectQuery.setTagList(Collections.singletonList(tagCode));
        inspectQuery.setMarketTenant(query.getMarketTenant());
        return safetyInspectRecordItemService.getInspectRecordCount(inspectQuery);
    }
}
