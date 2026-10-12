package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsMarketCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PropertyCategoryEnum;
import com.guhecloud.cube.srv.gen.safety.management.service.FireSafetyManageService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 消防管理
 * @date 2023/9/8 14:51
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireSafetyManageServiceImpl implements FireSafetyManageService {
    private final SafetyAlarmStatisticsService safetyAlarmStatisticsService;
    private final PropertyStatisticsService propertyStatisticsService;

    @Override
    public FireSafetyStatisticsCardVO getStatisticalCard(String applicationCode) {
        AlarmStatusVO alarmStatus = safetyAlarmStatisticsService.getAlarmStatus(AlarmStatisticsQuery.builder()
                                                                                        .applicationCode(applicationCode)
                                                                                        .period(new Period(DateUtils.beginOfYear(LocalDateTime.now()), LocalDateTime.now()))
                                                                                        .build());
        Integer expireNum = propertyStatisticsService.getFireFightingEquipmentExpiredNum(BaseQuery.builder().expired(true).build());
        return FireSafetyStatisticsCardVO.builder()
                .totalNum(alarmStatus.getTotalNum() == null ? 0 : alarmStatus.getTotalNum())
                .noResponseNum(alarmStatus.getUnConfirmedNum() == null ? 0 : alarmStatus.getUnConfirmedNum())
                .overtimeUntreatedNum(alarmStatus.getTimeoutNotHandlerNum() == null ? 0 : alarmStatus.getTimeoutNotHandlerNum())
                .timeoutNum(alarmStatus.getTimeoutNum() == null ? 0 : alarmStatus.getTimeoutNum())
                .accuracyRatio(alarmStatus.getActualRatio().doubleValue())
                .deviceFilingNum(expireNum == null ? 0 : expireNum)
                .build();
    }

    @Override
    public FireSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery query) {
        //消防设备建档数
        CountPropertyFilingNumQuery filingQuery = CountPropertyFilingNumQuery.builder().marketTenant(query.getMarketTenant())
                .period(query.getPeriod())
                .deviceType(PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.name()).build();
        Integer filingNum = propertyStatisticsService.getDeviceFilingNum(filingQuery);
        query.setExpired(true);
        Integer expiredNum = propertyStatisticsService.getFireFightingEquipmentExpiredNum(query);
        return FireSafetyStatisticsMarketCardVO.builder()
                .deviceFilingNum(filingNum)
                .deviceExpireNum(expiredNum)
                .build();
    }
}
