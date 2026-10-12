package com.guhecloud.cube.customization.xialing.server.liangcang.assembler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseDeviceBindingMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseDeviceResDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBindingDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangLocationCardInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 货位卡 VO 组装
 * 聚合智慧仓分页、设备中心、IoT 物模型与绑定表，填充 {@link LiangcangLocationCardInfoVO}。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LiangcangLocationCardInfoAssembler {

    private static final int LOCATION_CARD_NUMBER_SCALE = 2;

    private final DeviceCenterStdClient deviceCenterStdClient;
    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseDeviceBindingMapper warehouseDeviceBindingMapper;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;

    public LiangcangLocationCardInfoVO build(RadarBindingDetailParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        var cfg = locationCardConfigProvider.resolve();
        long warehouseId = param.getWarehouseId();
        Optional<LiangcangWarehousePageItemVO> pageRow = fetchWarehousePageItemSafely(warehouseId);
        WarehouseDeviceResDTO res = pageRow.map(LiangcangLocationCardInfoAssembler::toWarehouseDeviceRes)
                                           .orElse(null);

        var out = new LiangcangLocationCardInfoVO();
        out.setWarehouse(fillWarehouse(warehouseId, res));

        var bindingRows = warehouseDeviceBindingMapper.selectList(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                                                          .select(WarehouseDeviceBinding::getDeviceId)
                                                                          .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId));
        List<Long> boundDeviceIds = ListUtils.convertItem(bindingRows, WarehouseDeviceBinding::getDeviceId);

        var radarRow = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, warehouseId)
                .select(RadarWarehouseBinding::getRadarDeviceId)
                .one();
        Set<Long> detailDeviceIds = new HashSet<>(boundDeviceIds);
        Map<Long, DeviceBaseDetailsDTO> detailsByDeviceId = loadDeviceDetailsById(detailDeviceIds);
        DeviceBaseDetailsDTO radarDeviceDetail = loadRadarDeviceDetailByThirdPartyId(
                radarRow == null ? null : radarRow.getRadarDeviceId());

        var temperatureDevices = new ArrayList<Long>();
        var humidityDevices = new ArrayList<Long>();
        var gasDevices = new ArrayList<Long>();
        for (Long deviceId : boundDeviceIds) {
            var detail = detailsByDeviceId.get(deviceId);
            if (deviceMatches(detail, cfg.getTemperatureProductCodes())) {
                temperatureDevices.add(deviceId);
            }
            if (deviceMatches(detail, cfg.getHumidityProductCodes())) {
                humidityDevices.add(deviceId);
            }
            if (deviceMatches(detail, cfg.getGasProductCodes())) {
                gasDevices.add(deviceId);
            }
        }
        out.setTemperatureDeviceStats(countOnlineOffline(temperatureDevices, detailsByDeviceId));
        out.setHumidityDeviceStats(countOnlineOffline(humidityDevices, detailsByDeviceId));
        out.setGasDeviceStats(countOnlineOffline(gasDevices, detailsByDeviceId));
        out.setRadarDeviceStats(countRadarOnlineOffline(radarRow == null ? null : radarRow.getRadarDeviceId(), radarDeviceDetail));
        return out;
    }

    private static LiangcangLocationCardInfoVO.WarehouseInfo fillWarehouse(long warehouseId, WarehouseDeviceResDTO res) {
        var w = new LiangcangLocationCardInfoVO.WarehouseInfo();
        w.setWarehouseId(warehouseId);
        if (res == null) {
            return w;
        }
        w.setName(res.getName());
        w.setCategoryName(res.getCategoryName());
        w.setTotalVolume(toLocationCardScale(res.getTotalVolume() == null ? null : BigDecimal.valueOf(res.getTotalVolume())));
        w.setQualityGrade(res.getQualityGrade());
        w.setGranaryHeight(toLocationCardScale(res.getWarehouseHeight() == null ? null : BigDecimal.valueOf(res.getWarehouseHeight())));
        w.setUsageRate(toLocationCardScale(res.getUsageRate() == null ? null : BigDecimal.valueOf(res.getUsageRate())));
        return w;
    }

    private Optional<LiangcangWarehousePageItemVO> fetchWarehousePageItemSafely(long warehouseId) {
        try {
            Optional<LiangcangWarehousePageItemVO> pageRow = warehousePageApiClient.fetchPageItemById(warehouseId);
            if (pageRow.isEmpty()) {
                log.warn("货位卡粮仓信息查询为空 warehouseId={}", warehouseId);
            }
            return pageRow;
        } catch (Exception e) {
            log.warn("货位卡粮仓信息查询失败 warehouseId={}", warehouseId, e);
            return Optional.empty();
        }
    }

    private static WarehouseDeviceResDTO toWarehouseDeviceRes(LiangcangWarehousePageItemVO item) {
        var d = new WarehouseDeviceResDTO();
        d.setId(item.getId() != null ? item.getId().intValue() : null);
        d.setParentId(item.getParentId());
        d.setName(item.getName());
        d.setCategoryName(item.getCategoryName());
        d.setTotalVolume(item.getTotalVolume());
        d.setQualityGrade(item.getQualityGrade());
        d.setWarehouseHeight(item.getWarehouseHeight());
        d.setEnabledScan(item.getEnabledScan());
        d.setScanCorn(item.getScanCorn());
        d.setMaterialVolume(item.getMaterialVolume());
        d.setMaterialWeight(item.getMaterialWeight());
        d.setUsageRate(item.getUsageRate());
        d.setCameraSetting(item.getCameraSetting());
        d.setSetting(item.getSetting());
        return d;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailsById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        var rows = deviceCenterStdClient.postDetailsListByDeviceId(param);
        return ListUtils.isEmpty(rows) ? Map.of() : MapUtils.listToMap(rows, DeviceBaseDetailsDTO::getId, r -> r);
    }

    private DeviceBaseDetailsDTO loadRadarDeviceDetailByThirdPartyId(Long radarDeviceId) {
        if (radarDeviceId == null) {
            return null;
        }
        return deviceCenterStdClient.getDetailsByThirdPartyId(String.valueOf(radarDeviceId));
    }

    private static LiangcangLocationCardInfoVO.OnlineOfflineStats countOnlineOffline(
            List<Long> deviceIds, Map<Long, DeviceBaseDetailsDTO> byId) {
        var s = new LiangcangLocationCardInfoVO.OnlineOfflineStats();
        if (ListUtils.isEmpty(deviceIds)) {
            return s;
        }
        for (Long id : deviceIds) {
            if (id == null) {
                s.setOfflineCount(s.getOfflineCount() + 1);
            } else if (isDeviceOnline(byId.get(id))) {
                s.setOnlineCount(s.getOnlineCount() + 1);
            } else {
                s.setOfflineCount(s.getOfflineCount() + 1);
            }
        }
        return s;
    }

    private static LiangcangLocationCardInfoVO.OnlineOfflineStats countRadarOnlineOffline(
            Long radarDeviceId, DeviceBaseDetailsDTO detail) {
        var s = new LiangcangLocationCardInfoVO.OnlineOfflineStats();
        if (radarDeviceId == null) {
            return s;
        }
        if (isDeviceOnline(detail)) {
            s.setOnlineCount(1);
        } else {
            s.setOfflineCount(1);
        }
        return s;
    }

    private static boolean isDeviceOnline(DeviceBaseDetailsDTO d) {
        if (d == null) {
            return false;
        }
        if (d.getDeviceStateCode() != null) {
            return d.getDeviceStateCode() == 1;
        }
        return Boolean.TRUE.equals(d.getDeviceState());
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> codes) {
        if (detail == null || ListUtils.isEmpty(codes)) {
            return false;
        }
        for (String c : codes) {
            if (StringUtils.isBlank(c)) {
                continue;
            }
            var t = c.trim();
            if (detail.getDeviceIotProdId() != null && t.equals(String.valueOf(detail.getDeviceIotProdId()))) {
                return true;
            }
        }
        return false;
    }

    private static BigDecimal toLocationCardScale(BigDecimal value) {
        return value == null ? null : value.setScale(LOCATION_CARD_NUMBER_SCALE, RoundingMode.HALF_UP);
    }
}
