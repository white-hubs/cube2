package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyCategoryEnum;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.UseAirSafetyManageService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 用气安全
 * @date 2023/9/8 14:35
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UseAirSafetyManageServiceImpl implements UseAirSafetyManageService {
    private final SafetyAlarmStatisticsService safetyAlarmStatisticsService;
    private final PropertyStatisticsServiceImpl propertyStatisticsService;

    @Override
    public UseAirSafetyStatisticsCardVO getStatisticalCard(String applicationCode) {
        AlarmStatusVO alarmStatus = safetyAlarmStatisticsService.getAlarmStatus(AlarmStatisticsQuery.builder()
                                                                                        .applicationCode(applicationCode)
                                                                                        .period(new Period(DateUtils.beginOfYear(LocalDateTime.now()), LocalDateTime.now()))
                                                                                        .build());
        Integer filingNum = propertyStatisticsService.getDeviceFilingNum(CountPropertyFilingNumQuery.builder()
                                                                                 .deviceType(PropertyCategoryEnum.FLAMMABLE_EQUIPMENT.name())
                                                                                 .build());
        return UseAirSafetyStatisticsCardVO.builder()
                .totalNum(alarmStatus.getTotalNum())
                .noResponseNum(alarmStatus.getUnConfirmedNum())
                .overtimeUntreatedNum(alarmStatus.getTimeoutNotHandlerNum())
                .timeoutNum(alarmStatus.getTimeoutNum())
                .accuracyRatio(alarmStatus.getActualRatio().doubleValue())
                .deviceFilingNum(filingNum)
                .build();
    }

    @Override
    public UseAirSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery baseQuery) {
        Integer filingNum = propertyStatisticsService.getDeviceFilingNum(CountPropertyFilingNumQuery.builder()
                                                                                 .marketTenant(baseQuery.getMarketTenant())
                                                                                 .deviceType(PropertyCategoryEnum.FLAMMABLE_EQUIPMENT.name())
                                                                                 .build());
        return UseAirSafetyStatisticsMarketCardVO.builder()
                .deviceFilingNum(filingNum)
                .build();
    }
}
