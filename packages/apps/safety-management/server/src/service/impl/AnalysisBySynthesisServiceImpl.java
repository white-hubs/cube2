package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskIndexQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AnalysisBySynthesisMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyCategoryEnum;
import com.guhecloud.cube.srv.gen.safety.management.service.AnalysisBySynthesisService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyRiskIndexService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 综合分析
 * @date 2023/9/8 16:41
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnalysisBySynthesisServiceImpl implements AnalysisBySynthesisService {
    private final SafetyAlarmStatisticsService safetyAlarmStatisticsService;
    private final PropertyStatisticsService propertyStatisticsService;
    private final LooseWorksStatisticServiceImpl looseWorksStatisticService;
    private final SafetyRiskIndexService safetyRiskIndexService;

    @Override
    public AnalysisBySynthesisCardVO getAnalysisBySynthesisStatistics() {
        AlarmStatisticsQuery statisticsQuery = new AlarmStatisticsQuery();
        Period period = new Period(DateUtils.beginOfYear(LocalDateTime.now()), LocalDateTime.now());
        statisticsQuery.setPeriod(period);
        AlarmStatusVO status = safetyAlarmStatisticsService.getAlarmStatus(statisticsQuery);
        AnalysisBySynthesisCardVO build = AnalysisBySynthesisCardVO.builder()
                .totalNum(status.getTotalNum() == null ? 0 : status.getTotalNum())
                .noResponseNum(status.getUnConfirmedNum() == null ? 0 : status.getUnConfirmedNum())
                .inProcessNum(status.getProgressConfirmedNum() == null ? 0 : status.getProgressConfirmedNum())
                .handlerNum(status.getHandlerNum() == null ? 0 : status.getHandlerNum())
                .overtimeUntreatedNum(status.getTimeoutNotHandlerNum() == null ? 0 : status.getTimeoutNotHandlerNum())
                .notHandlerNum(status.getNotHandlerNum())
                .build();
        PropertyFilingNumQuery query = new PropertyFilingNumQuery();
        build.setDangerFilingNum(propertyStatisticsService.countPropertyFilingNum(query));
        query.setExpired(true);
        query.setPropertyCategory(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name());
        build.setMaintenanceExpireNum(propertyStatisticsService.countPropertyFilingNum(query));
        return build;
    }

    @Override
    public AnalysisBySynthesisMarketCardVO getAnalysisBySynthesisMarketStatistics(BaseQuery query) {
        query.setPeriod(null);
        CountPropertyFilingNumQuery numQuery = CountPropertyFilingNumQuery.builder()
                .marketTenant(query.getMarketTenant()).period(query.getPeriod())
                .deviceType(PropertyCategoryEnum.FLAMMABLE_EQUIPMENT.name())
                .build();
        //易燃设备建档数
        Integer fireNum = propertyStatisticsService.getDeviceFilingNum(numQuery);
        numQuery.setDeviceType(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name());
        //消防设备建档数
        Integer fireControlNum = propertyStatisticsService.getDeviceFilingNum(numQuery);
        numQuery.setDeviceType(PropertyCategoryEnum.TRANSPORT_DEVICES.name());
        //电动车建档数
        Integer transportDevicesNum = propertyStatisticsService.getDeviceFilingNum(numQuery);
        //消防设备到期数
        query.setExpired(true);
        Integer expiredNum = propertyStatisticsService.getFireFightingEquipmentExpiredNum(query);
        LooseWorksCountVO countVO = looseWorksStatisticService.countInfo(query);
        List<RiskIndexVO> riskIndexVOS = null;
        if (ObjectUtils.isNotEmpty(query.getMarketTenant())) {
            riskIndexVOS = safetyRiskIndexService.riskIndexList(RiskIndexQuery.builder().marketTenant(query.getMarketTenant()).build());
        }
        return AnalysisBySynthesisMarketCardVO.builder()
                .riskIndex(ObjectUtils.isEmpty(riskIndexVOS) ? 0 : riskIndexVOS.getFirst().getRiskIndex())
                .flammableEquipmentNum(fireNum)
                .powderProjectNum(countVO.getTotal())
                .fireFightingEquipmentFilingNum(fireControlNum)
                .fireFightingEquipmentExpireNum(expiredNum)
                .electricVehicleFilingNum(transportDevicesNum)
                .build();
    }
}
