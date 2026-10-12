package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleNatureEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.VehicleStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.VehicleTotalResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.TransportWeekMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.VehicleDayStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.VehicleWeekStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.TransportWeekPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.VehicleDayStatisticsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.VehicleWeekStatisticsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.service.VehicleStatisticsService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleStatisticsImpl implements VehicleStatisticsService {
    private final VehicleDayStatisticsMapper vehicleDayStatisticsMapper;
    private final VehicleWeekStatisticsMapper vehicleWeekStatisticsMapper;
    private final TransportWeekMapper transportWeekMapper;

    /**
     * 获取车辆统计
     *
     * @param param 入参条件
     *
     * @return 统计对象
     */
    @Override
    public VehicleTotalResult getPeriodTotal(VehicleStatisticsQueryParam param) {
        VehicleTotalResult result = new VehicleTotalResult();
        Period period = param.getPeriod();
        List<VehicleWeekStatisticsPO> poList = vehicleWeekStatisticsMapper.selectList(Wrappers.<VehicleWeekStatisticsPO>lambdaQuery()
                                                                                              .eq(StringUtils.isNotBlank(param.getDay()), VehicleWeekStatisticsPO::getDays, param.getDay())
                                                                                              .ge(period != null && period.getBegin() !=
                                                                                                                    null, VehicleWeekStatisticsPO::getTimeDay, ObjectUtils.ifNull(period, Period::getBegin))
                                                                                              .le(period != null && period.getEnd() !=
                                                                                                                    null, VehicleWeekStatisticsPO::getTimeDay, ObjectUtils.ifNull(period, Period::getEnd))
                                                                                     );
        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }

        Map<String, List<VehicleWeekStatisticsPO>> map = poList.stream()
                .collect(Collectors.groupingBy(VehicleWeekStatisticsPO::getVehicleNature));
        if (map.get(VehicleNatureEnum.BUY_VEHICLE.name()) != null) {
            result.setBuyNumber(map.get(VehicleNatureEnum.BUY_VEHICLE.name()).get(0).getCarSum());
        }
        if (map.get(VehicleNatureEnum.COM_VEHICLE.name()) != null) {
            result.setComeNumber(map.get(VehicleNatureEnum.COM_VEHICLE.name()).get(0).getCarSum());
        }
        if (map.get(VehicleNatureEnum.OTHER_VEHICLE.name()) != null) {
            result.setOtherNumber(map.get(VehicleNatureEnum.OTHER_VEHICLE.name()).get(0).getCarSum());
        }
        if (map.get(VehicleNatureEnum.ALL_VEHICLE.name()) != null) {
            result.setTotalNumber(map.get(VehicleNatureEnum.ALL_VEHICLE.name()).get(0).getCarSum());
        }
        return result;
    }

    /**
     * 查询趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResult> getTrend(VehicleStatisticsQueryParam param) {
        List<TrendResult> result = new ArrayList<>();
        Period period = param.getPeriod();
        List<VehicleWeekStatisticsPO> poList = vehicleWeekStatisticsMapper.selectList(Wrappers.<VehicleWeekStatisticsPO>lambdaQuery()
                                                                                              .ge(period != null && period.getBegin() !=
                                                                                                                    null, VehicleWeekStatisticsPO::getTimeDay, ObjectUtils.ifNull(period, Period::getBegin))
                                                                                              .le(period != null && period.getEnd() !=
                                                                                                                    null, VehicleWeekStatisticsPO::getTimeDay, ObjectUtils.ifNull(period, Period::getEnd))
                                                                                              .eq(StringUtils.isNotBlank(param.getVehicleNature()), VehicleWeekStatisticsPO::getVehicleNature, param.getVehicleNature())
                                                                                     );
        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }
        poList.forEach(po -> result.add(TrendResult.builder().trendX(po.getDays()).trendY(String.valueOf(po.getCarSum())).build()));
        return result;
    }

    @Override
    public List<TrendResult> getDayTrend(VehicleStatisticsQueryParam param) {
        List<TrendResult> result = new ArrayList<>();
        List<VehicleDayStatisticsPO> poList = vehicleDayStatisticsMapper.selectList(Wrappers.<VehicleDayStatisticsPO>lambdaQuery()
                                                                                            .eq(StringUtils.isNotBlank(param.getVehicleNature()), VehicleDayStatisticsPO::getVehicleNature, param.getVehicleNature())
                                                                                            .eq(VehicleDayStatisticsPO::getTimeDayFormat, param.getDay())
                                                                                   );
        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }
        Map<String, List<VehicleDayStatisticsPO>> map = poList.stream().collect(Collectors.groupingBy(VehicleDayStatisticsPO::getDateType));
        List<VehicleDayStatisticsPO> todayList = map.get("today");
        if (!CollectionUtils.isEmpty(todayList)) {
            todayList.forEach(po -> {
                TrendResult temp = new TrendResult();
                temp.setTrendX(po.getDateHour());
                temp.setTrendY(String.valueOf(po.getCarCount()));
                result.add(temp);
            });
        }
        List<VehicleDayStatisticsPO> nextDayList = map.get("nextDay");
        if (!CollectionUtils.isEmpty(nextDayList)) {
            nextDayList.forEach(po -> {
                TrendResult temp = new TrendResult();
                temp.setTrendX(po.getDateHour());
                temp.setTrendY(String.valueOf(po.getCarCount()));
                result.add(temp);
            });
        }

        return result;
    }

    /**
     * 近一周车辆装货趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResult> getLoadTrend(VehicleStatisticsQueryParam param) {
        List<TrendResult> result = new ArrayList<>();
        Period period = param.getPeriod();
        List<TransportWeekPO> poList = transportWeekMapper.selectList(Wrappers.<TransportWeekPO>lambdaUpdate()
                                                                              .ge(period != null && period.getBegin() !=
                                                                                                    null, TransportWeekPO::getEnterDate, ObjectUtils.ifNull(period, Period::getBegin))
                                                                              .le(period != null && period.getEnd() !=
                                                                                                    null, TransportWeekPO::getEnterDate, ObjectUtils.ifNull(period, Period::getEnd))
                                                                              .orderByAsc(TransportWeekPO::getEnterDate)
                                                                     );
        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }
        poList.forEach(po -> result.add(TrendResult.builder().trendX(DateUtils.formatDate(po.getEnterDate()))
                                                .trendY(String.valueOf((po.getQuickCarSum() + po.getNormalCarSum()))).build()));
        return result;
    }

}
