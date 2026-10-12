package com.guhecloud.cube.srv.gen.park.management.server.service.impl;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomMapper;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.IndustryStatisticsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.ParkFloorsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.UtilizationRateVo;
import com.guhecloud.cube.srv.gen.park.management.server.service.ParkDataStatisticsService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkDataStatisticsServiceImpl implements ParkDataStatisticsService {
    private final RoomMapper roomMapper;

    private final AreaClient areaClient;

    @Override
    public Map<String, Object> getSpaceUtilizationRate() {
        Map<String, Object> remap = new HashMap<>();
        //总面积
        BigDecimal total = roomMapper.getProportion("");
        //使用面积
        BigDecimal useArea = roomMapper.getProportion(RoomStatusEnum.NORMAL.name());
        //使用率
        BigDecimal utilizationRate = BigDecimal.ZERO;
        if (total.compareTo(BigDecimal.ZERO) == 1) {
            utilizationRate = utilizationRate.add(useArea.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }
        remap.put("total", total);
        remap.put("useArea", useArea);
        remap.put("utilizationRate", utilizationRate);
        return remap;
    }

    @Override
    public List<UtilizationRateVo> getBuildingSpaceUtilizationRate() {
        List<AreaDTO> filterAreaByLevelOne = areaClient.getFilterAreaByLevelOne(2);
        List<String> collect = filterAreaByLevelOne.stream().map(s -> {
            return String.valueOf(s.getId());
        }).collect(Collectors.toList());
        List<UtilizationRateVo> buildingSpaceUtilizationRate = roomMapper.getBuildingSpaceUtilizationRate(collect);
        Map<String, AreaDTO> areas = filterAreaByLevelOne.stream().collect(Collectors.toMap(s -> String.valueOf(s.getId()), s -> s));
        for (UtilizationRateVo utilizationRateVo : buildingSpaceUtilizationRate) {
            if (utilizationRateVo.getTotal().compareTo(BigDecimal.ZERO) == 1) {
                utilizationRateVo.setUseAreaRate(utilizationRateVo.getUseArea()
                                                         .divide(utilizationRateVo.getTotal(), 2, RoundingMode.HALF_UP)
                                                         .multiply(new BigDecimal(100)));
                utilizationRateVo.setDisabledAreaRate(utilizationRateVo.getDisabledArea()
                                                              .divide(utilizationRateVo.getTotal(), 2, RoundingMode.HALF_UP)
                                                              .multiply(new BigDecimal(100)));
            }
            AreaDTO areaDTO = areas.get(utilizationRateVo.getAreaId());
            utilizationRateVo.setAreaName(areaDTO.getName());
        }
        return buildingSpaceUtilizationRate;
    }

    @Override
    public Map<String, Object> getIndustryStatisticsEnterprises() {
        Map<String, Object> remap = new HashMap<>();
        List<IndustryStatisticsVo> industryStatisticsEnterprises = roomMapper.getIndustryStatisticsEnterprises();
        int total = 0;
        if (CollectionUtils.isNotEmpty(industryStatisticsEnterprises)) {
            for (IndustryStatisticsVo industryStatisticsVo : industryStatisticsEnterprises) {
                total += industryStatisticsVo.getNum();
            }
            for (IndustryStatisticsVo industryStatisticsEnterpris : industryStatisticsEnterprises) {
                industryStatisticsEnterpris.setRate(new BigDecimal(
                        industryStatisticsEnterpris.getNum() / total).multiply(new BigDecimal(100)));
            }
        }
        remap.put("total", total);
        remap.put("industryStatisticsEnterprises", industryStatisticsEnterprises);
        return remap;
    }

    @Override
    public Map<String, Object> getOverallSituationParkLeasing() {
        Map<String, Object> remap = new HashMap<>();
        //总可租面积
        BigDecimal proportion = roomMapper.getProportion(RoomStatusEnum.NORMAL.name());
        BigDecimal leasedArea = BigDecimal.ZERO;
        BigDecimal leasedAreaRate = BigDecimal.ZERO;
        BigDecimal unleasedArea = BigDecimal.ZERO;
        BigDecimal unleasedAreaRate = BigDecimal.ZERO;
        if (proportion.compareTo(BigDecimal.ZERO) == 1) {
            BigDecimal overallSituationParkLeasing = roomMapper.getOverallSituationParkLeasing();
            //已租赁面积
            leasedArea = leasedArea.add(overallSituationParkLeasing);
            //未租面积
            unleasedArea = unleasedArea.add(proportion.subtract(leasedArea));
            //已租占比
            leasedAreaRate = leasedAreaRate.add(leasedArea.divide(proportion, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            //未租占比
            unleasedAreaRate = unleasedAreaRate.add(unleasedArea.divide(proportion, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }
        remap.put("total", proportion);
        List<UtilizationRateVo> objects = new ArrayList<>();
        UtilizationRateVo utilizationRateVo = new UtilizationRateVo();
        utilizationRateVo.setName("已租赁");
        utilizationRateVo.setUseArea(leasedArea);
        utilizationRateVo.setUseAreaRate(leasedAreaRate);
        objects.add(utilizationRateVo);
        UtilizationRateVo utilizationRateVo1 = new UtilizationRateVo();
        utilizationRateVo1.setName("未租赁");
        utilizationRateVo1.setUseArea(unleasedArea);
        utilizationRateVo1.setUseAreaRate(unleasedAreaRate);
        objects.add(utilizationRateVo1);
        remap.put("leased", objects);
        return remap;
    }

    @Override
    public Map<String, Object> getRentalTrends() {
        var remap = new HashMap<String, Object>();
        var date = LocalDateTime.now();
        var beginOfYear = DateUtils.beginOfYear(date);
        var rentalTrends = roomMapper.getRentalTrends(beginOfYear, date);
        var xtitle = new ArrayList<>();
        int month = date.getMonthValue();
        for (int i = 1; i <= month; i++) {
            if (i < 10) {
                xtitle.add("0" + (i));
            } else {
                xtitle.add(String.valueOf(i));
            }
        }
        remap.put("xtitle", xtitle);
        remap.put("rentalTrends", rentalTrends);
        return remap;
    }

    @Override
    public List<ParkFloorsVo> getRentalRatioParkFloors(String areaId) {
        List<ParkFloorsVo> rentalRatioParkFloors = roomMapper.getRentalRatioParkFloors(areaId);
        if (CollectionUtils.isNotEmpty(rentalRatioParkFloors)) {
            List<Long> collect = rentalRatioParkFloors.stream().map(ParkFloorsVo::getAreaId).collect(Collectors.toList());
            List<AreaDTO> byIds = areaClient.getByIds(collect);
            Map<Long, AreaDTO> areaDTOMap = byIds.stream().collect(Collectors.toMap(AreaDTO::getId, s -> s));
            for (ParkFloorsVo rentalRatioParkFloor : rentalRatioParkFloors) {
                AreaDTO areaDTO = areaDTOMap.get(rentalRatioParkFloor.getAreaId());
                rentalRatioParkFloor.setAreaName(areaDTO.getName());
                if (rentalRatioParkFloor.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                    rentalRatioParkFloor.setRentedAreaRate(rentalRatioParkFloor.getRentedArea()
                                                                   .divide(rentalRatioParkFloor.getTotal(), 2, RoundingMode.HALF_UP)
                                                                   .multiply(new BigDecimal(100)));
                    rentalRatioParkFloor.setUnleasedAreaRate(rentalRatioParkFloor.getTotal().subtract(rentalRatioParkFloor.getRentedArea())
                                                                     .divide(rentalRatioParkFloor.getTotal(), 2, RoundingMode.HALF_UP)
                                                                     .multiply(new BigDecimal(100)));
                }
            }
        }

        return rentalRatioParkFloors;
    }
}
