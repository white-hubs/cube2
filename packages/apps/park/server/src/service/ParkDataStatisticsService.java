package com.guhecloud.cube.srv.gen.park.management.server.service;

import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.ParkFloorsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.UtilizationRateVo;

import java.util.List;
import java.util.Map;

public interface ParkDataStatisticsService {
    Map<String, Object> getSpaceUtilizationRate();

    List<UtilizationRateVo> getBuildingSpaceUtilizationRate();

    Map<String, Object> getIndustryStatisticsEnterprises();

    Map<String, Object> getOverallSituationParkLeasing();

    Map<String, Object> getRentalTrends();

    List<ParkFloorsVo> getRentalRatioParkFloors(String areaId);
}
