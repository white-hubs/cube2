package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseCableMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCablePoint;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableAvailableDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointMappingCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCablePointService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableAvailableDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCablePointMappingVO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseCableServiceImpl extends ServiceImpl<WarehouseCableMapper, WarehouseCable>
        implements WarehouseCableService {

    private static final String STATUS_MAPPED = "mapped";
    private static final String STATUS_PARTIAL = "partial";
    private static final String STATUS_UNMAPPED = "unmapped";

    private final WarehouseCablePointService warehouseCablePointService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createCable(WarehouseCableCreateParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(!(StringUtils.isNotBlank(param.getCableNo())), MsgBinding.CABLE_NO_REQUIRED);
        Assert.isParameterError(param.getPointCount() == null, MsgBinding.CABLE_POINT_COUNT_REQUIRED);
        Assert.isParameterError(!(param.getPointCount() > 0), MsgBinding.CABLE_POINT_COUNT_REQUIRED);
        assertWarehouseBoundAndExists(warehouseId);

        String cableNo = param.getCableNo().trim();
        boolean exists = lambdaQuery()
                .eq(WarehouseCable::getWarehouseId, warehouseId)
                .eq(WarehouseCable::getCableNo, cableNo)
                .exists();
        Assert.isParameterError(exists, MsgBinding.CABLE_ALREADY_EXISTS);

        var cable = new WarehouseCable();
        cable.setId(IdWorker.getId());
        cable.setWarehouseId(warehouseId);
        cable.setCableNo(cableNo);
        cable.setPointCount(param.getPointCount());
        save(cable);

        List<WarehouseCablePoint> points = new ArrayList<>(param.getPointCount());
        for (int i = 1; i <= param.getPointCount(); i++) {
            var point = new WarehouseCablePoint();
            point.setId(IdWorker.getId());
            point.setWarehouseId(warehouseId);
            point.setCableId(cable.getId());
            point.setPointNo(i);
            points.add(point);
        }
        warehouseCablePointService.saveBatch(points);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCable(WarehouseCableDeleteParam param) {
        Assert.isParameterError(param == null, MsgBinding.CABLE_ID_REQUIRED);
        WarehouseCable cable = requireCable(param.getCableId());
        warehouseCablePointService.remove(Wrappers.<WarehouseCablePoint>lambdaQuery()
                .eq(WarehouseCablePoint::getCableId, cable.getId()));
        removeById(cable.getId());
        return true;
    }

    @Override
    public List<WarehouseCableListItemVO> listByWarehouseId(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        List<WarehouseCable> cables = lambdaQuery()
                .eq(WarehouseCable::getWarehouseId, warehouseId)
                .orderByAsc(WarehouseCable::getCableNo)
                .list();
        if (ListUtils.isEmpty(cables)) {
            return List.of();
        }
        Set<Long> cableIds = SetUtils.listToSet(cables, WarehouseCable::getId);
        Map<Long, Integer> mappedCountByCableId = new HashMap<>();
        if (SetUtils.isNotEmpty(cableIds)) {
            List<WarehouseCablePoint> mappedPoints = warehouseCablePointService.list(
                    Wrappers.<WarehouseCablePoint>lambdaQuery()
                            .select(WarehouseCablePoint::getCableId, WarehouseCablePoint::getDeviceId)
                            .in(WarehouseCablePoint::getCableId, cableIds)
                            .isNotNull(WarehouseCablePoint::getDeviceId));
            for (WarehouseCablePoint point : mappedPoints) {
                if (point != null && point.getCableId() != null) {
                    mappedCountByCableId.merge(point.getCableId(), 1, Integer::sum);
                }
            }
        }
        List<WarehouseCableListItemVO> out = new ArrayList<>(cables.size());
        for (WarehouseCable cable : cables) {
            var vo = new WarehouseCableListItemVO();
            vo.setCableId(cable.getId());
            vo.setWarehouseId(cable.getWarehouseId());
            vo.setCableNo(cable.getCableNo());
            vo.setPointCount(cable.getPointCount());
            int mappedCount = mappedCountByCableId.getOrDefault(cable.getId(), 0);
            vo.setMappedCount(mappedCount);
            vo.setStatus(resolveCableStatus(cable.getPointCount(), mappedCount));
            out.add(vo);
        }
        return out;
    }

    @Override
    public List<WarehouseCablePointMappingVO> listPoints(Long cableId) {
        WarehouseCable cable = requireCable(cableId);
        List<WarehouseCablePoint> points = warehouseCablePointService.list(
                Wrappers.<WarehouseCablePoint>lambdaQuery()
                        .eq(WarehouseCablePoint::getCableId, cable.getId())
                        .orderByAsc(WarehouseCablePoint::getPointNo));
        if (ListUtils.isEmpty(points)) {
            return List.of();
        }
        Set<Long> deviceIds = points.stream()
                .map(WarehouseCablePoint::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> deviceDetailById = loadDeviceDetailById(deviceIds);
        List<WarehouseCablePointMappingVO> out = new ArrayList<>(points.size());
        for (WarehouseCablePoint point : points) {
            var vo = new WarehouseCablePointMappingVO();
            vo.setPointId(point.getId());
            vo.setCableId(point.getCableId());
            vo.setWarehouseId(point.getWarehouseId());
            vo.setPointNo(point.getPointNo());
            vo.setDeviceId(point.getDeviceId());
            DeviceBaseDetailsDTO detail = point.getDeviceId() == null ? null : deviceDetailById.get(point.getDeviceId());
            if (detail != null) {
                vo.setDeviceCode(detail.getDeviceCode());
                vo.setDeviceName(detail.getDeviceName());
            }
            vo.setStatus(point.getDeviceId() == null ? STATUS_UNMAPPED : STATUS_MAPPED);
            out.add(vo);
        }
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPointMappings(WarehouseCablePointMappingCreateParam param) {
        Assert.isParameterError(param == null, MsgBinding.CABLE_ID_REQUIRED);
        WarehouseCable cable = requireCable(param.getCableId());
        Assert.isParameterError(!(ListUtils.isNotEmpty(param.getMappings())), MsgBinding.CABLE_POINT_MAPPING_REQUIRED);

        Map<Integer, Long> deviceIdByPointNo = new LinkedHashMap<>();
        Set<Long> incomingDeviceIds = new HashSet<>();
        for (WarehouseCablePointMappingCreateParam.MappingItem item : param.getMappings()) {
            Assert.isParameterError(item == null, MsgBinding.CABLE_POINT_MAPPING_REQUIRED);
            Assert.isParameterError(item.getPointNo() == null, MsgBinding.CABLE_POINT_NO_REQUIRED);
            Assert.isParameterError(item.getDeviceId() == null, MsgBinding.DEVICE_IDS_REQUIRED);
            Assert.isParameterError(!(item.getPointNo() > 0 && item.getPointNo() <= cable.getPointCount()), MsgBinding.CABLE_POINT_NOT_FOUND);
            Assert.isParameterError(deviceIdByPointNo.containsKey(item.getPointNo()), MsgBinding.CABLE_POINT_DUPLICATED);
            deviceIdByPointNo.put(item.getPointNo(), item.getDeviceId());
            Assert.isParameterError(!(incomingDeviceIds.add(item.getDeviceId())), MsgBinding.CABLE_DEVICE_DUPLICATED);
        }

        List<WarehouseCablePoint> points = warehouseCablePointService.list(
                Wrappers.<WarehouseCablePoint>lambdaQuery()
                        .eq(WarehouseCablePoint::getCableId, cable.getId())
                        .in(WarehouseCablePoint::getPointNo, deviceIdByPointNo.keySet()));
        Assert.isParameterError(!(points.size() == deviceIdByPointNo.size()), MsgBinding.CABLE_POINT_NOT_FOUND);
        for (WarehouseCablePoint point : points) {
            Assert.isParameterError(!(point.getDeviceId() == null), MsgBinding.CABLE_POINT_ALREADY_MAPPED);
        }
        assertDevicesBoundToWarehouse(cable.getWarehouseId(), incomingDeviceIds);
        assertDevicesNotOccupied(incomingDeviceIds, Set.of());

        for (WarehouseCablePoint point : points) {
            point.setDeviceId(deviceIdByPointNo.get(point.getPointNo()));
        }
        warehouseCablePointService.updateBatchById(points);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindPoints(WarehouseCablePointUnbindParam param) {
        Assert.isParameterError(param == null, MsgBinding.CABLE_POINT_REQUIRED);
        List<WarehouseCablePoint> points = resolvePointsForUnbind(param);
        Assert.isParameterError(!(ListUtils.isNotEmpty(points)), MsgBinding.CABLE_POINT_NOT_FOUND);
        Set<Long> pointIds = SetUtils.listToSet(points, WarehouseCablePoint::getId);
        warehouseCablePointService.lambdaUpdate()
                .set(WarehouseCablePoint::getDeviceId, null)
                .in(WarehouseCablePoint::getId, pointIds)
                .update();
        return true;
    }

    @Override
    public Pagination<WarehouseCableAvailableDeviceVO> availableDevicePage(WarehouseCableAvailableDevicePageParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getDeviceId)
                        .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId)
                        .orderByAsc(WarehouseDeviceBinding::getDeviceId));
        if (ListUtils.isEmpty(bindings)) {
            return Pagination.empty();
        }
        Set<Long> deviceIds = SetUtils.listToSet(bindings, WarehouseDeviceBinding::getDeviceId);
        Map<Long, DeviceBaseDetailsDTO> deviceDetailById = loadDeviceDetailById(deviceIds);
        Map<Long, DeviceProductInfoDTO> productByDeviceId = loadProductByDeviceId(deviceIds);
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        Map<Long, WarehouseCablePoint> mappedPointByDeviceId = loadMappedPointByDeviceId(warehouseId, deviceIds);
        Map<Long, WarehouseCable> cableById = loadCableById(mappedPointByDeviceId.values());

        List<WarehouseCableAvailableDeviceVO> all = new ArrayList<>(bindings.size());
        String deviceNameSearch = param.getDeviceName() == null ? null : param.getDeviceName().trim();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getDeviceId() == null) {
                continue;
            }
            Long deviceId = binding.getDeviceId();
            DeviceBaseDetailsDTO detail = deviceDetailById.get(deviceId);
            if (!productMatches(productByDeviceId.get(deviceId), cfg.getTemperatureProductCodes())) {
                continue;
            }
            if (StringUtils.isNotBlank(deviceNameSearch)
                && (detail == null || StringUtils.isBlank(detail.getDeviceName())
                    || !detail.getDeviceName().contains(deviceNameSearch))) {
                continue;
            }
            WarehouseCablePoint mappedPoint = mappedPointByDeviceId.get(deviceId);
            boolean mapped = mappedPoint != null;
            if (Boolean.TRUE.equals(param.getOnlyUnmapped()) && mapped) {
                continue;
            }
            var vo = new WarehouseCableAvailableDeviceVO();
            vo.setDeviceId(deviceId);
            if (detail != null) {
                vo.setDeviceCode(detail.getDeviceCode());
                vo.setDeviceName(detail.getDeviceName());
                vo.setDeviceType(detail.getProductCategoryName());
            }
            vo.setMapped(mapped);
            vo.setStatus(mapped ? STATUS_MAPPED : STATUS_UNMAPPED);
            if (mapped) {
                vo.setMappedCableId(mappedPoint.getCableId());
                vo.setMappedPointNo(mappedPoint.getPointNo());
                WarehouseCable cable = cableById.get(mappedPoint.getCableId());
                if (cable != null) {
                    vo.setMappedCableNo(cable.getCableNo());
                }
            }
            all.add(vo);
        }
        int pageNumber = param.getPageNumber() == null ? 1 : param.getPageNumber();
        int pageSize = param.getPageSize() == null ? 10 : param.getPageSize();
        long total = all.size();
        long pages = pageSize <= 0 ? 0L : (total + pageSize - 1) / pageSize;
        return new Pagination<>(total, pages, Paging.list(all, pageNumber, pageSize));
    }

    private void assertWarehouseBoundAndExists(Long warehouseId) {
        boolean bound = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, warehouseId)
                .exists();
        Assert.isParameterError(!(bound), MsgBinding.WAREHOUSE_NOT_BOUND);
        Assert.isParameterError(!(warehousePageApiClient.fetchPageItemById(warehouseId).isPresent()), MsgBinding.WAREHOUSE_INFO_NOT_FOUND);
    }

    private WarehouseCable requireCable(Long cableId) {
        Assert.isParameterError(cableId == null, MsgBinding.CABLE_ID_REQUIRED);
        WarehouseCable cable = getById(cableId);
        Assert.isParameterError(cable == null, MsgBinding.CABLE_NOT_FOUND);
        return cable;
    }

    private List<WarehouseCablePoint> resolvePointsForUnbind(WarehouseCablePointUnbindParam param) {
        if (ListUtils.isNotEmpty(param.getPointIds())) {
            Set<Long> pointIds = new HashSet<>();
            for (Long pointId : param.getPointIds()) {
                Assert.isParameterError(pointId == null, MsgBinding.CABLE_POINT_REQUIRED);
                Assert.isParameterError(!(pointIds.add(pointId)), MsgBinding.CABLE_POINT_DUPLICATED);
            }
            List<WarehouseCablePoint> points = warehouseCablePointService.listByIds(pointIds);
            Assert.isParameterError(!(points.size() == pointIds.size()), MsgBinding.CABLE_POINT_NOT_FOUND);
            return points;
        }
        Assert.isParameterError(param.getCableId() == null, MsgBinding.CABLE_ID_REQUIRED);
        Assert.isParameterError(!(ListUtils.isNotEmpty(param.getPointNos())), MsgBinding.CABLE_POINT_NO_REQUIRED);
        Set<Integer> pointNos = new HashSet<>();
        for (Integer pointNo : param.getPointNos()) {
            Assert.isParameterError(pointNo == null, MsgBinding.CABLE_POINT_NO_REQUIRED);
            Assert.isParameterError(!(pointNo > 0), MsgBinding.CABLE_POINT_NOT_FOUND);
            Assert.isParameterError(!(pointNos.add(pointNo)), MsgBinding.CABLE_POINT_DUPLICATED);
        }
        List<WarehouseCablePoint> points = warehouseCablePointService.list(
                Wrappers.<WarehouseCablePoint>lambdaQuery()
                        .eq(WarehouseCablePoint::getCableId, param.getCableId())
                        .in(WarehouseCablePoint::getPointNo, pointNos));
        Assert.isParameterError(!(points.size() == pointNos.size()), MsgBinding.CABLE_POINT_NOT_FOUND);
        return points;
    }

    private void assertDevicesNotOccupied(Set<Long> deviceIds, Set<Long> allowedPointIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return;
        }
        var query = Wrappers.<WarehouseCablePoint>lambdaQuery()
                .select(WarehouseCablePoint::getId, WarehouseCablePoint::getDeviceId)
                .in(WarehouseCablePoint::getDeviceId, deviceIds);
        if (SetUtils.isNotEmpty(allowedPointIds)) {
            query.notIn(WarehouseCablePoint::getId, allowedPointIds);
        }
        List<WarehouseCablePoint> occupied = warehouseCablePointService.list(query);
        Assert.isParameterError(!(ListUtils.isEmpty(occupied)), MsgBinding.CABLE_DEVICE_ALREADY_MAPPED);
    }

    private void assertDevicesBoundToWarehouse(Long warehouseId, Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return;
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getDeviceId)
                        .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId)
                        .in(WarehouseDeviceBinding::getDeviceId, deviceIds));
        Set<Long> boundDeviceIds = ListUtils.isEmpty(bindings) ? Set.of()
                : SetUtils.listToSet(bindings, WarehouseDeviceBinding::getDeviceId);
        Assert.isParameterError(!(boundDeviceIds.containsAll(deviceIds)), MsgBinding.CABLE_DEVICE_NOT_BOUND_TO_WAREHOUSE);
    }

    private Map<Long, WarehouseCablePoint> loadMappedPointByDeviceId(Long warehouseId, Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        List<WarehouseCablePoint> rows = warehouseCablePointService.list(
                Wrappers.<WarehouseCablePoint>lambdaQuery()
                        .select(WarehouseCablePoint::getCableId, WarehouseCablePoint::getPointNo,
                                WarehouseCablePoint::getDeviceId)
                        .eq(WarehouseCablePoint::getWarehouseId, warehouseId)
                        .in(WarehouseCablePoint::getDeviceId, deviceIds)
                        .isNotNull(WarehouseCablePoint::getDeviceId));
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        return MapUtils.listToMap(rows, WarehouseCablePoint::getDeviceId, item -> item);
    }

    private Map<Long, WarehouseCable> loadCableById(Iterable<WarehouseCablePoint> points) {
        Set<Long> cableIds = new HashSet<>();
        for (WarehouseCablePoint point : points) {
            if (point != null && point.getCableId() != null) {
                cableIds.add(point.getCableId());
            }
        }
        if (SetUtils.isEmpty(cableIds)) {
            return Map.of();
        }
        List<WarehouseCable> cables = listByIds(cableIds);
        if (ListUtils.isEmpty(cables)) {
            return Map.of();
        }
        return MapUtils.listToMap(cables, WarehouseCable::getId, item -> item);
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceBaseDetailsDTO> details = deviceCenterStdClient.postDetailsListByDeviceId(param);
        if (ListUtils.isEmpty(details)) {
            return Map.of();
        }
        return MapUtils.listToMap(details, DeviceBaseDetailsDTO::getId, item -> item);
    }

    private Map<Long, DeviceProductInfoDTO> loadProductByDeviceId(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceProductInfoWithDeviceDTO> rows = deviceCenterStdClient.postProductInfoByDeviceId(param);
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        return rows.stream()
                .filter(row -> row != null && row.getDeviceId() != null)
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId,
                                          DeviceProductInfoWithDeviceDTO::getProductInfo, (a, b) -> b));
    }

    private static boolean productMatches(DeviceProductInfoDTO product, List<String> codes) {
        if (product == null || ListUtils.isEmpty(codes)) {
            return false;
        }
        for (String code : codes) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            var want = code.trim();
            if (product.getId() != null && want.equals(String.valueOf(product.getId()))) {
                return true;
            }
        }
        return false;
    }

    private static String resolveCableStatus(Integer pointCount, int mappedCount) {
        int total = pointCount == null ? 0 : pointCount;
        if (mappedCount <= 0) {
            return STATUS_UNMAPPED;
        }
        if (mappedCount >= total) {
            return STATUS_MAPPED;
        }
        return STATUS_PARTIAL;
    }

}
