package com.guhecloud.cube.base.device.center.visualization.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.visualization.param.DeviceUsefulLifeExpireParam;
import com.guhecloud.cube.base.device.center.visualization.param.VisualizationQueryParam;
import com.guhecloud.cube.base.device.center.visualization.service.DeviceCenterVisualizationService;
import com.guhecloud.cube.base.device.center.visualization.util.VisualizationUtil;
import com.guhecloud.cube.base.device.center.visualization.vo.AreaEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceAreaGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceCountVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceStatusGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceTypeGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireChainVO;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireVO;
import com.guhecloud.cube.base.device.center.visualization.vo.OverallEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.StatusEquipmentStatisticsVo;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterVisualizationServiceImpl implements DeviceCenterVisualizationService {

    private final DeviceCenterMapper deviceCenterMapper;

    private final AreaClient areaClient;

    @Override
    public OverallEquipmentStatisticsVo getOverallEquipmentStatistics(VisualizationQueryParam param) {
        OverallEquipmentStatisticsVo overallEquipmentStatisticsVo = new OverallEquipmentStatisticsVo();
        if (CollectionUtils.isNotEmpty(param.getDeviceTypes())) {
            Map<String, Object> paramMap = VisualizationUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param.getPeriod()
                    .getEnd());
            var st = (LocalDateTime) paramMap.get("st");
            var et = (LocalDateTime) paramMap.get("et");
            var ost = (LocalDateTime) paramMap.get("ost");
            var oet = (LocalDateTime) paramMap.get("oet");
            DeviceCountVo deviceCount = getDeviceCount(param.getDeviceTypes(), st, et, ost, oet);
            List<DeviceTypeGroupVo> deviceTypeGroup = deviceCenterMapper.getDeviceTypeGroup(param.getDeviceTypes(), st, et);
            if (CollectionUtils.isNotEmpty(deviceTypeGroup)) {
                deviceTypeGroup.forEach(s -> {
                    if (deviceCount.getDeviceTotal() != 0) {
                        s.setProportion(new BigDecimal(s.getNum())
                                                .divide(new BigDecimal(deviceCount.getDeviceTotal()), 2, RoundingMode.HALF_UP)
                                                .multiply(new BigDecimal(100)));
                    }
                });
                deviceTypeGroup = deviceTypeGroup.stream().sorted(Comparator.comparing(DeviceTypeGroupVo::getNum).reversed())
                        .collect(Collectors.toList());
                List<DeviceTypeGroupVo> others = new ArrayList<>();
                if (deviceTypeGroup.size() > 4) {
                    List<DeviceTypeGroupVo> deviceTypeGroupVos = deviceTypeGroup.subList(0, 3);
                    List<DeviceTypeGroupVo> otherDeviceTypeGroupVos = deviceTypeGroup.subList(4, deviceTypeGroup.size() - 1);
                    others.addAll(deviceTypeGroupVos);
                    Integer otherTotal = 0;
                    for (DeviceTypeGroupVo otherDeviceTypeGroupVo : otherDeviceTypeGroupVos) {
                        otherTotal += otherDeviceTypeGroupVo.getNum();
                    }
                    DeviceTypeGroupVo deviceTypeGroupVo = new DeviceTypeGroupVo();
                    deviceTypeGroupVo.setDeviceType("其他");
                    deviceTypeGroupVo.setNum(otherTotal);
                    deviceTypeGroupVo.setProportion(new BigDecimal(otherTotal)
                                                            .divide(new BigDecimal(deviceCount.getDeviceTotal()), 2, RoundingMode.HALF_UP)
                                                            .multiply(new BigDecimal(100)));
                    others.add(deviceTypeGroupVo);
                } else {
                    others.addAll(deviceTypeGroup);
                }
                overallEquipmentStatisticsVo.setOtherDeviceTypeGroup(others);
            }
            overallEquipmentStatisticsVo.setDeviceTotal(deviceCount.getDeviceTotal());
            overallEquipmentStatisticsVo.setRate(deviceCount.getRate());
            overallEquipmentStatisticsVo.setDeviceTypeGroup(deviceTypeGroup);
        }
        return overallEquipmentStatisticsVo;
    }

    @Override
    public StatusEquipmentStatisticsVo getStatusEquipmentStatistics(VisualizationQueryParam param) {
        StatusEquipmentStatisticsVo statusEquipmentStatisticsVo = new StatusEquipmentStatisticsVo();
        if (CollectionUtils.isNotEmpty(param.getDeviceTypes())) {
            LocalDateTime st = null;
            LocalDateTime et = null;
            LocalDateTime ost = null;
            LocalDateTime oet = null;
            if (StringUtils.isNotEmpty(param.getType()) && Objects.nonNull(param.getPeriod())) {
                Map<String, Object> paramMap = VisualizationUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                        .getPeriod().getEnd());
                st = (LocalDateTime) paramMap.get("st");
                et = (LocalDateTime) paramMap.get("et");
                ost = (LocalDateTime) paramMap.get("ost");
                oet = (LocalDateTime) paramMap.get("oet");
            }

            DeviceCountVo deviceCount = getDeviceCount(param.getDeviceTypes(), st, et, ost, oet);
            List<DeviceStatusGroupVo> deviceStatusGroup = deviceCenterMapper.getDeviceStatusGroup(param.getDeviceTypes(), st, et);
            if (CollectionUtils.isNotEmpty(deviceStatusGroup)) {
                deviceStatusGroup.forEach(s -> {
                    if (deviceCount.getDeviceTotal() != 0) {
                        s.setProportion(new BigDecimal(s.getNum())
                                                .divide(new BigDecimal(deviceCount.getDeviceTotal()), 2, RoundingMode.HALF_UP)
                                                .multiply(new BigDecimal(100)));
                    }
                });
                deviceStatusGroup = deviceStatusGroup.stream().sorted(Comparator.comparing(DeviceStatusGroupVo::getNum).reversed())
                        .collect(Collectors.toList());
            }
            statusEquipmentStatisticsVo.setDeviceTotal(deviceCount.getDeviceTotal());
            statusEquipmentStatisticsVo.setRate(deviceCount.getRate());
            statusEquipmentStatisticsVo.setDeviceStatusGroupVos(deviceStatusGroup);
        }
        return statusEquipmentStatisticsVo;
    }

    @Override
    public AreaEquipmentStatisticsVo getAreaEquipmentStatistics(VisualizationQueryParam param) {
        AreaEquipmentStatisticsVo areaEquipmentStatisticsVo = new AreaEquipmentStatisticsVo();
        if (CollectionUtils.isNotEmpty(param.getDeviceTypes())) {
            LocalDateTime st = null;
            LocalDateTime et = null;
            LocalDateTime ost = null;
            LocalDateTime oet = null;
            if (StringUtils.isNotEmpty(param.getType()) && Objects.nonNull(param.getPeriod())) {
                Map<String, Object> paramMap = VisualizationUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                        .getPeriod().getEnd());
                st = (LocalDateTime) paramMap.get("st");
                et = (LocalDateTime) paramMap.get("et");
                ost = (LocalDateTime) paramMap.get("ost");
                oet = (LocalDateTime) paramMap.get("oet");
            }
            DeviceCountVo deviceCount = getDeviceCount(param.getDeviceTypes(), st, et, ost, oet);
            List<DeviceAreaGroupVo> deviceAreaGroupVos = deviceCenterMapper.getDeviceAreaGroup(param.getDeviceTypes(), st, et);
            deviceAreaGroupVos.forEach(s -> {
                String areaNamesById = areaClient.getAreaNamesById(s.getAreaLastId());
                s.setAreaName(areaNamesById);
            });
            List<DeviceStatusGroupVo> deviceStatusGroupVos = deviceCenterMapper.getDeviceStatusGroup(param.getDeviceTypes(), st, et);
            DeviceStatusGroupVo offline = deviceStatusGroupVos.stream().filter(s -> s.getDeviceStatus().equals("离线")).findFirst()
                    .orElse(null);
            int offlineNum = 0;
            if (Objects.nonNull(offline)) {
                offlineNum += offline.getNum();
            }
            areaEquipmentStatisticsVo.setOfflineNum(offlineNum);
            areaEquipmentStatisticsVo.setOfflineRate(new BigDecimal(offline.getNum())
                                                             .divide(new BigDecimal(deviceCount.getDeviceTotal()), 2, RoundingMode.HALF_UP)
                                                             .multiply(new BigDecimal(100)));
            areaEquipmentStatisticsVo.setDeviceAreaGroupVos(deviceAreaGroupVos);
        }
        return areaEquipmentStatisticsVo;
    }

    public DeviceCountVo getDeviceCount(List<Long> ids, LocalDateTime st, LocalDateTime et, LocalDateTime ost, LocalDateTime oet) {
        DeviceCountVo deviceCountVo = new DeviceCountVo();
        //当前值
        int deviceTotal = deviceCenterMapper.getDeviceTotal(ids, st, et);
        //环比
        int deviceTotalOld = deviceCenterMapper.getDeviceTotal(ids, ost, oet);
        BigDecimal rate = VisualizationUtil.getRate(String.valueOf(deviceTotalOld), String.valueOf(deviceTotal));
        deviceCountVo.setDeviceTotalOld(deviceTotalOld);
        deviceCountVo.setDeviceTotal(deviceTotal);
        deviceCountVo.setRate(rate);
        return deviceCountVo;
    }

    @Override
    public Pagination<DeviceUsefulLifeExpireVO> getUsefulLifeExpiredDevicePage(DeviceUsefulLifeExpireParam param) {
        Page<DeviceUsefulLifeExpireVO> page = deviceCenterMapper.selectUsefulLifeExpiredDevicePage(PagingUtils.convert(param), param);
        page.getRecords().forEach(deviceUsefulLifeExpireVO -> {
            deviceUsefulLifeExpireVO.setExpireStatus("NOT_EXPIRED");
            if (Objects.nonNull(deviceUsefulLifeExpireVO.getUsefulLife())) {
                var dateTime = DateUtils.offsetMonth(DateUtils.beginOfDay(deviceUsefulLifeExpireVO.getPurchaseDate()), deviceUsefulLifeExpireVO.getUsefulLife());
                if (dateTime.isBefore(LocalDateTime.now())) {
                    deviceUsefulLifeExpireVO.setExpireStatus("EXPIRE");
                }
                double usefulLifeExpireYear = BigDecimal.valueOf(deviceUsefulLifeExpireVO.getUsefulLife())
                        .divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN)
                        .doubleValue();
                deviceUsefulLifeExpireVO.setUsefulLifeYear(usefulLifeExpireYear + "年");
            }
        });
        return PagingUtils.convert(page);
    }

    @Override
    public DeviceUsefulLifeExpireChainVO getDeviceUsefulLifeExpireChainVO(DeviceUsefulLifeExpireParam param) {
        //本期设备到期总数
        Integer currentDeviceUsefulLifeExpiredTotal = deviceCenterMapper.selectUsefulLifeExpiredDeviceCount(param);
        //上期设备到期总数
        Period previousPeriod = VisualizationUtil.getPreviousPeriod(param.getTimeType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Integer previousDeviceUsefulLifeExpiredTotal = deviceCenterMapper.selectUsefulLifeExpiredDeviceCount(param);
        float chainRatio = 0f;
        if (0 < previousDeviceUsefulLifeExpiredTotal) {
            chainRatio = new BigDecimal(currentDeviceUsefulLifeExpiredTotal - previousDeviceUsefulLifeExpiredTotal)
                    .divide(new BigDecimal(previousDeviceUsefulLifeExpiredTotal), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return DeviceUsefulLifeExpireChainVO.builder()
                .totalNumber(currentDeviceUsefulLifeExpiredTotal)
                .chainRatio(chainRatio)
                .build();
    }

}
