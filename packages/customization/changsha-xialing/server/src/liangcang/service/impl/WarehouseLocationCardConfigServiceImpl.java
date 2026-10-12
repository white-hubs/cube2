package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseLocationCardFieldChangeLog;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseLocationCardPositionProfile;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseLocationCardQualityInspection;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardBoundWarehousePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardChangeLogQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardPositionProfileSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardFieldChangeLogService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardPositionProfileService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardQualityInspectionService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendAlertService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardBoundWarehousePageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardConfigDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardEnvironmentMonitorVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardFieldChangeLogVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardPositionProfileVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardQualityInspectionVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertNoticeVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 货位卡配置。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseLocationCardConfigServiceImpl implements WarehouseLocationCardConfigService {

    private static final String BIZ_TYPE_POSITION_PROFILE = "POSITION_PROFILE";
    private static final String BIZ_TYPE_QUALITY_INSPECTION = "QUALITY_INSPECTION";
    private static final int NUMBER_SCALE = 5;
    private static final BigDecimal PPM_TO_PERCENT_VOL_DIVISOR = BigDecimal.valueOf(10000);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final WarehouseLocationCardPositionProfileService positionProfileService;
    private final WarehouseLocationCardQualityInspectionService qualityInspectionService;
    private final WarehouseLocationCardFieldChangeLogService fieldChangeLogService;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;
    private final DataDictionaryClient dataDictionaryClient;
    private final ObjectProvider<WarehouseTemperatureTrendStatisticsService> warehouseTemperatureTrendStatisticsService;
    private final ObjectProvider<WarehouseTemperatureTrendAlertService> warehouseTemperatureTrendAlertService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProfile(WarehouseLocationCardPositionProfileSaveParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);

        WarehouseLocationCardPositionProfile old = positionProfileService.lambdaQuery()
                .eq(WarehouseLocationCardPositionProfile::getWarehouseId, param.getWarehouseId())
                .one();
        WarehouseLocationCardPositionProfile entity = old == null ? new WarehouseLocationCardPositionProfile() : copyProfile(old);
        if (old == null) {
            entity.setId(IdWorker.getId());
            entity.setWarehouseId(param.getWarehouseId());
        }
        fillProfile(entity, param);

        if (old == null) {
            positionProfileService.save(entity);
        } else {
            positionProfileService.updateById(entity);
        }
        saveChangeLog(compareFields(param.getWarehouseId(),
                                    BIZ_TYPE_POSITION_PROFILE,
                                    entity.getId(),
                                    old,
                                    entity,
                                    profileFields()));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createQualityInspection(WarehouseLocationCardQualityInspectionSaveParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getInspectionTime() == null, MsgBinding.LOCATION_CARD_INSPECTION_TIME_REQUIRED);

        WarehouseLocationCardQualityInspection entity = new WarehouseLocationCardQualityInspection();
        entity.setId(IdWorker.getId());
        entity.setWarehouseId(param.getWarehouseId());
        fillQualityInspection(entity, param);
        qualityInspectionService.save(entity);
        saveChangeLog(compareFields(entity.getWarehouseId(),
                                    BIZ_TYPE_QUALITY_INSPECTION,
                                    entity.getId(),
                                    null,
                                    entity,
                                    qualityInspectionFields()));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateQualityInspection(WarehouseLocationCardQualityInspectionSaveParam param) {
        Assert.isParameterError(param == null, MsgBinding.ID_REQUIRED);
        Assert.isParameterError(param.getId() == null, MsgBinding.ID_REQUIRED);
        Assert.isParameterError(param.getInspectionTime() == null, MsgBinding.LOCATION_CARD_INSPECTION_TIME_REQUIRED);

        WarehouseLocationCardQualityInspection old = qualityInspectionService.getById(param.getId());
        Assert.isParameterError(old == null, MsgBinding.LOCATION_CARD_QUALITY_INSPECTION_NOT_FOUND);
        WarehouseLocationCardQualityInspection entity = copyQualityInspection(old);
        fillQualityInspection(entity, param);
        qualityInspectionService.updateById(entity);
        saveChangeLog(compareFields(entity.getWarehouseId(),
                                    BIZ_TYPE_QUALITY_INSPECTION,
                                    entity.getId(),
                                    old,
                                    entity,
                                    qualityInspectionFields()));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteQualityInspection(WarehouseLocationCardQualityInspectionDeleteParam param) {
        Assert.isParameterError(param == null, MsgBinding.ID_REQUIRED);
        Assert.isParameterError(param.getId() == null, MsgBinding.ID_REQUIRED);
        WarehouseLocationCardQualityInspection old = qualityInspectionService.getById(param.getId());
        Assert.isParameterError(old == null, MsgBinding.LOCATION_CARD_QUALITY_INSPECTION_NOT_FOUND);
        qualityInspectionService.removeById(param.getId());

        WarehouseLocationCardFieldChangeLog log = newChangeLog(old.getWarehouseId(),
                                                               BIZ_TYPE_QUALITY_INSPECTION,
                                                               old.getId(),
                                                               List.of(new FieldChangeDetail("deleted",
                                                                                             "删除记录",
                                                                                             "存在",
                                                                                             "已删除")));
        fieldChangeLogService.save(log);
        return true;
    }

    @Override
    public WarehouseLocationCardConfigDetailVO detail(WarehouseLocationCardConfigDetailParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        if (!matchesTemperatureReportLayoutWarehouse(param.getWarehouseId(), loadTemperatureReportLayoutConfig())) {
            return detail(param, null);
        }
        var reportParam = new WarehouseTemperatureReportParam();
        reportParam.setWarehouseId(param.getWarehouseId());
        List<List<WarehouseTemperatureReportPointVO>> reportList =
                warehouseTemperatureTrendStatisticsService.getObject().temperatureReport(reportParam);
        return detail(param, reportList);
    }

    @Override
    public WarehouseLocationCardConfigDetailVO detail(WarehouseLocationCardConfigDetailParam param,
                                                      List<List<WarehouseTemperatureReportPointVO>> reportList) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);

        WarehouseLocationCardPositionProfile profile = positionProfileService.lambdaQuery()
                .eq(WarehouseLocationCardPositionProfile::getWarehouseId, param.getWarehouseId())
                .one();
        List<WarehouseLocationCardQualityInspection> inspections = qualityInspectionService.list(
                Wrappers.<WarehouseLocationCardQualityInspection>lambdaQuery()
                        .eq(WarehouseLocationCardQualityInspection::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(WarehouseLocationCardQualityInspection::getInspectionTime));

        var out = new WarehouseLocationCardConfigDetailVO();
        var profileVO = profile == null ? new WarehouseLocationCardPositionProfileVO() : toProfileVO(profile);
        profileVO.setWarehouseId(param.getWarehouseId());
        applyWarehouseName(param.getWarehouseId(), profileVO);
        applyMeasureInfo(param.getWarehouseId(), profileVO);
        out.setProfile(profileVO);
        out.setQualityInspections(ListUtils.isEmpty(inspections) ? List.of() : inspections.stream()
                                                                               .map(this::toQualityInspectionVO)
                                                                               .toList());
        out.setEnvironmentMonitor(buildEnvironmentMonitor(param.getWarehouseId(), reportList));
        out.setTemperatureAlertNotice(resolveTemperatureAlertNotice(param.getWarehouseId()));
        return out;
    }

    private void applyWarehouseName(Long warehouseId, WarehouseLocationCardPositionProfileVO profile) {
        if (warehouseId == null || profile == null) {
            return;
        }
        Map<Long, LiangcangWarehousePageItemVO> remoteById = warehousePageApiClient.fetchPageItemsByIds(Set.of(warehouseId));
        LiangcangWarehousePageItemVO remote = remoteById.get(warehouseId);
        if (remote != null) {
            profile.setPositionNo(remote.getName());
        }
    }

    private void applyMeasureInfo(Long warehouseId, WarehouseLocationCardPositionProfileVO profile) {
        if (warehouseId == null || profile == null) {
            return;
        }
        WarehouseMeasureHistory measureInfo = warehouseMeasureHistoryService.latestByWarehouseId(warehouseId)
                .orElse(null);
        if (measureInfo != null) {
            profile.setMaterialVolume(toLocationCardScale(measureInfo.getMaterialVolume()));
            profile.setRemainingVolume(toLocationCardScale(measureInfo.getRemainingVolume()));
            profile.setWeight(toLocationCardScale(measureInfo.getWeight()));
        }
    }

    @Override
    public Pagination<WarehouseLocationCardBoundWarehousePageVO> boundWarehousePage(
            WarehouseLocationCardBoundWarehousePageParam param
                                                                                   ) {
        long pageNumber = param == null || param.getPageNumber() == null || param.getPageNumber() < 1
                          ? 1 : param.getPageNumber();
        long pageSize = param == null || param.getPageSize() == null || param.getPageSize() < 1
                        ? 10 : param.getPageSize();
        String warehouseName = param == null ? null : param.getWarehouseName();
        if (StringUtils.isBlank(warehouseName)) {
            Page<RadarWarehouseBinding> page = radarWarehouseBindingService.lambdaQuery()
                    .select(RadarWarehouseBinding::getWarehouseId)
                    .orderByDesc(RadarWarehouseBinding::getWarehouseId)
                    .page(new Page<>(pageNumber, pageSize));
            if (page.getTotal() == 0) {
                return new Pagination<>(0L, 0L, List.of());
            }
            return new Pagination<>(page.getTotal(),
                                    page.getPages(),
                                    buildBoundWarehouseRecords(page.getRecords()));
        }

        List<RadarWarehouseBinding> bindings = radarWarehouseBindingService.lambdaQuery()
                .select(RadarWarehouseBinding::getWarehouseId)
                .orderByDesc(RadarWarehouseBinding::getWarehouseId)
                .list();
        List<WarehouseLocationCardBoundWarehousePageVO> all = buildBoundWarehouseRecords(bindings).stream()
                .filter(item -> item.getWarehouseName() != null && item.getWarehouseName().contains(warehouseName.trim()))
                .toList();
        long total = all.size();
        long pages = total == 0 ? 0 : (total + pageSize - 1) / pageSize;
        int fromIndex = (int) Math.min((pageNumber - 1) * pageSize, total);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        return new Pagination<>(total, pages, all.subList(fromIndex, toIndex));
    }

    @Override
    public Pagination<WarehouseLocationCardFieldChangeLogVO> changeLogList(WarehouseLocationCardChangeLogQueryParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        long pageNumber = param.getPageNumber() == null || param.getPageNumber() < 1 ? 1 : param.getPageNumber();
        long pageSize = param.getPageSize() == null || param.getPageSize() < 1 ? 10 : param.getPageSize();
        var query = Wrappers.<WarehouseLocationCardFieldChangeLog>lambdaQuery()
                .eq(WarehouseLocationCardFieldChangeLog::getWarehouseId, param.getWarehouseId())
                .orderByDesc(WarehouseLocationCardFieldChangeLog::getCreateTime);
        if (StringUtils.isNotBlank(param.getBizType())) {
            query.eq(WarehouseLocationCardFieldChangeLog::getBizType, param.getBizType().trim());
        }
        Page<WarehouseLocationCardFieldChangeLog> page = fieldChangeLogService.page(
                new Page<>(pageNumber, pageSize),
                query);
        if (ListUtils.isEmpty(page.getRecords())) {
            return new Pagination<>(page.getTotal(), page.getPages(), List.of());
        }
        List<WarehouseLocationCardFieldChangeLogVO> records = page.getRecords()
                .stream()
                .map(this::toChangeLogVO)
                .toList();
        return new Pagination<>(page.getTotal(), page.getPages(), records);
    }

    private List<WarehouseLocationCardBoundWarehousePageVO> buildBoundWarehouseRecords(List<RadarWarehouseBinding> bindings) {
        if (ListUtils.isEmpty(bindings)) {
            return List.of();
        }
        List<Long> warehouseIds = bindings.stream()
                .map(RadarWarehouseBinding::getWarehouseId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (ListUtils.isEmpty(warehouseIds)) {
            return List.of();
        }
        Map<Long, LiangcangWarehousePageItemVO> remoteById =
                warehousePageApiClient.fetchPageItemsByIds(Set.copyOf(warehouseIds));
        Map<Long, String> keeperByWarehouseId = loadKeeperByWarehouseId(warehouseIds);
        var out = new ArrayList<WarehouseLocationCardBoundWarehousePageVO>();
        for (Long warehouseId : warehouseIds) {
            LiangcangWarehousePageItemVO remote = remoteById.get(warehouseId);
            var vo = new WarehouseLocationCardBoundWarehousePageVO();
            vo.setWarehouseId(warehouseId);
            if (remote != null) {
                vo.setWarehouseName(remote.getName());
                vo.setCategoryName(remote.getCategoryName());
                vo.setQualityGrade(remote.getQualityGrade());
                vo.setMaxHeight(remote.getWarehouseHeight());
                vo.setWarehouseVolume(remote.getTotalVolume());
            }
            vo.setKeeper(keeperByWarehouseId.get(warehouseId));
            out.add(vo);
        }
        return out;
    }

    private Map<Long, String> loadKeeperByWarehouseId(List<Long> warehouseIds) {
        if (ListUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        List<WarehouseLocationCardPositionProfile> profiles = positionProfileService.list(
                Wrappers.<WarehouseLocationCardPositionProfile>lambdaQuery()
                        .select(WarehouseLocationCardPositionProfile::getWarehouseId,
                                WarehouseLocationCardPositionProfile::getKeeper)
                        .in(WarehouseLocationCardPositionProfile::getWarehouseId, warehouseIds));
        return ListUtils.isEmpty(profiles) ? Map.of() : profiles.stream()
                                                        .filter(row -> row != null && row.getWarehouseId() != null)
                                                        .collect(Collectors.toMap(WarehouseLocationCardPositionProfile::getWarehouseId,
                                                                                  WarehouseLocationCardPositionProfile::getKeeper,
                                                                                  (a, b) -> a));
    }

    private WarehouseLocationCardEnvironmentMonitorVO buildEnvironmentMonitor(Long warehouseId,
                                                                              List<List<WarehouseTemperatureReportPointVO>> reportList) {
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        var out = new WarehouseLocationCardEnvironmentMonitorVO();
        applyWarehouseTemperature(warehouseId, cfg, reportList, out);
        applyHumidity(warehouseId, cfg, out);
        applyGas(warehouseId, cfg, out);
        return out;
    }

    private WarehouseTemperatureAlertNoticeVO resolveTemperatureAlertNotice(Long warehouseId) {
        if (warehouseId == null || warehouseTemperatureTrendAlertService == null) {
            return null;
        }
        WarehouseTemperatureTrendAlertService service = warehouseTemperatureTrendAlertService.getIfAvailable();
        return service == null ? null : service.activeNotice(warehouseId);
    }

    private void applyWarehouseTemperature(Long warehouseId, LiangcangLocationCardConfigDTO cfg,
                                           List<List<WarehouseTemperatureReportPointVO>> reportList,
                                           WarehouseLocationCardEnvironmentMonitorVO out
                                          ) {
        TemperatureReportLayoutConfig layoutConfig = loadTemperatureReportLayoutConfig();
        if (matchesTemperatureReportLayoutWarehouse(warehouseId, layoutConfig)) {
            applySpecialWarehouseTemperature(resolveReportList(warehouseId, reportList), out);
            return;
        }
        DeviceBaseDetailsDTO device = deviceCenterStdClient.getDetailsByThirdPartyId(String.valueOf(warehouseId));
        if (device == null || device.getId() == null) {
            return;
        }
        Map<String, TemperatureRole> roleByPropCode = new LinkedHashMap<>();
        addTemperatureRole(roleByPropCode, cfg.getWarehouseMaxTemperaturePropCode(), TemperatureRole.MAX);
        addTemperatureRole(roleByPropCode, cfg.getWarehouseMinTemperaturePropCode(), TemperatureRole.MIN);
        addTemperatureRole(roleByPropCode, cfg.getWarehouseAvgTemperaturePropCode(), TemperatureRole.AVG);
        if (MapUtils.isEmpty(roleByPropCode)) {
            return;
        }
        var param = new DevicePropParam();
        param.setDeviceId(device.getId());
        param.setPropertyCodes(String.join(",", roleByPropCode.keySet()));
        List<KeyValueWithTsDTO> values = iotDevicePropClient.deviceProp(param);
        if (ListUtils.isEmpty(values)) {
            return;
        }
        for (KeyValueWithTsDTO value : values) {
            if (value == null || value.getKey() == null) {
                continue;
            }
            TemperatureRole role = roleByPropCode.get(value.getKey().trim());
            BigDecimal bd = toLocationCardScale(toBigDecimal(value.getValue()));
            if (bd == null || role == null) {
                continue;
            }
            switch (role) {
                case MAX -> out.setMaxTemperature(bd);
                case MIN -> out.setMinTemperature(bd);
                case AVG -> out.setAvgTemperature(bd);
            }
        }
    }

    private void applySpecialWarehouseTemperature(List<List<WarehouseTemperatureReportPointVO>> reportList,
                                                  WarehouseLocationCardEnvironmentMonitorVO out) {
        List<BigDecimal> values = new ArrayList<>();
        if (ListUtils.isEmpty(reportList)) {
            return;
        }
        for (List<WarehouseTemperatureReportPointVO> row : reportList) {
            if (row == null) {
                continue;
            }
            for (WarehouseTemperatureReportPointVO point : row) {
                if (point != null && point.getTemperature() != null) {
                    values.add(point.getTemperature());
                }
            }
        }
        applyTemperatureAggregate(values, out);
    }

    private List<List<WarehouseTemperatureReportPointVO>> resolveReportList(
            Long warehouseId,
            List<List<WarehouseTemperatureReportPointVO>> reportList
                                                                           ) {
        if (ListUtils.nonEmpty(reportList) || warehouseId == null) {
            return reportList;
        }
        var param = new WarehouseTemperatureReportParam();
        param.setWarehouseId(warehouseId);
        return warehouseTemperatureTrendStatisticsService.getObject().temperatureReport(param);
    }

    private void applyHumidity(Long warehouseId, LiangcangLocationCardConfigDTO cfg,
                               WarehouseLocationCardEnvironmentMonitorVO out
                              ) {
        List<Long> deviceIds = resolveBoundDeviceIdsByProductCodes(warehouseId, cfg.getHumidityProductCodes());
        BigDecimal humidity = averageCurrentProp(deviceIds, cfg.getHumidityRealtimePropCode());
        out.setHumidity(toLocationCardScale(humidity));
    }

    private void applyGas(Long warehouseId, LiangcangLocationCardConfigDTO cfg,
                          WarehouseLocationCardEnvironmentMonitorVO out
                         ) {
        Map<Long, DeviceBaseDetailsDTO> detailByDeviceId = resolveBoundDeviceDetailsByProductCodes(warehouseId, cfg.getGasProductCodes());
        if (MapUtils.isEmpty(detailByDeviceId)) {
            return;
        }
        String gasPropCode = resolveGasRealtimePropCode(cfg);
        if (StringUtils.isBlank(gasPropCode)) {
            return;
        }
        Map<Long, BigDecimal> gasValueByDeviceId = currentPropByDevice(new ArrayList<>(detailByDeviceId.keySet()), gasPropCode);
        if (MapUtils.isEmpty(gasValueByDeviceId)) {
            return;
        }
        var co2Values = new ArrayList<BigDecimal>();
        var o2Values = new ArrayList<BigDecimal>();
        var ph3Values = new ArrayList<BigDecimal>();
        for (Map.Entry<Long, DeviceBaseDetailsDTO> entry : detailByDeviceId.entrySet()) {
            BigDecimal value = gasValueByDeviceId.get(entry.getKey());
            GasRole role = resolveGasRole(entry.getValue());
            if (value == null || role == null) {
                continue;
            }
            switch (role) {
                case CO2 -> co2Values.add(value);
                case O2 -> o2Values.add(value);
                case PH3 -> ph3Values.add(value);
            }
        }
        out.setCo2Ppm(toLocationCardScale(ppmToPercentVol(average(co2Values))));
        out.setO2Percent(toLocationCardScale(average(o2Values)));
        out.setPh3Ppm(toLocationCardScale(ppmToPercentVol(average(ph3Values))));
    }

    private List<Long> resolveBoundDeviceIdsByProductCodes(Long warehouseId, List<String> productCodes) {
        return new ArrayList<>(resolveBoundDeviceDetailsByProductCodes(warehouseId, productCodes).keySet());
    }

    private Map<Long, DeviceBaseDetailsDTO> resolveBoundDeviceDetailsByProductCodes(Long warehouseId, List<String> productCodes) {
        if (warehouseId == null || ListUtils.isEmpty(productCodes)) {
            return Map.of();
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getDeviceId)
                        .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId));
        if (ListUtils.isEmpty(bindings)) {
            return Map.of();
        }
        Set<Long> deviceIds = bindings.stream()
                .map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> detailById = loadDeviceDetailsById(deviceIds);
        Map<Long, DeviceBaseDetailsDTO> out = new LinkedHashMap<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getDeviceId() == null) {
                continue;
            }
            DeviceBaseDetailsDTO detail = detailById.get(binding.getDeviceId());
            if (deviceMatches(detail, productCodes)) {
                out.putIfAbsent(binding.getDeviceId(), detail);
            }
        }
        return out;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailsById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceBaseDetailsDTO> rows = deviceCenterStdClient.postDetailsListByDeviceId(param);
        return ListUtils.isEmpty(rows) ? Map.of() : rows.stream()
                                                    .filter(row -> row != null && row.getId() != null)
                                                    .collect(Collectors.toMap(DeviceBaseDetailsDTO::getId, row -> row, (a, b) -> b));
    }

    private BigDecimal averageCurrentProp(List<Long> deviceIds, String propCode) {
        Map<Long, BigDecimal> valueByDeviceId = currentPropByDevice(deviceIds, propCode);
        return MapUtils.isEmpty(valueByDeviceId) ? null : average(new ArrayList<>(valueByDeviceId.values()));
    }

    private Map<Long, BigDecimal> currentPropByDevice(List<Long> deviceIds, String propCode) {
        if (ListUtils.isEmpty(deviceIds) || StringUtils.isBlank(propCode)) {
            return Map.of();
        }
        String spec = deviceIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(deviceId -> deviceId + "." + propCode.trim())
                .collect(Collectors.joining(","));
        if (StringUtils.isBlank(spec)) {
            return Map.of();
        }
        var param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(spec);
        Map<String, List<KeyValueWithTsDTO>> byDevice = iotDevicePropClient.multiProp(param);
        if (MapUtils.isEmpty(byDevice)) {
            return Map.of();
        }
        Map<Long, BigDecimal> out = new LinkedHashMap<>();
        String code = propCode.trim();
        for (Long deviceId : deviceIds) {
            BigDecimal value = findPropValue(byDevice.get(String.valueOf(deviceId)), code);
            if (value != null) {
                out.put(deviceId, value);
            }
        }
        return out;
    }

    private void fillProfile(WarehouseLocationCardPositionProfile entity, WarehouseLocationCardPositionProfileSaveParam param) {
        entity.setWarehouseId(param.getWarehouseId());
        entity.setPositionCode(param.getPositionCode());
        entity.setFlatWarehouseNo(param.getFlatWarehouseNo());
        entity.setLength(param.getLength());
        entity.setWidth(param.getWidth());
        entity.setHeight(param.getHeight());
        entity.setTemperatureThreshold(param.getTemperatureThreshold());
        entity.setTemperatureRangeMin(param.getTemperatureRangeMin());
        entity.setTemperatureRangeMax(param.getTemperatureRangeMax());
        entity.setDigitalGrainVolume(param.getDigitalGrainVolume());
        entity.setDeductedVolume(param.getDeductedVolume());
        entity.setStorageBagCount(param.getStorageBagCount());
        entity.setAverageBagWeight(param.getAverageBagWeight());
        entity.setOriginPlace(param.getOriginPlace());
        entity.setHarvestYear(param.getHarvestYear());
        entity.setKeeper(param.getKeeper());
        entity.setFilingDate(param.getFilingDate());
        entity.setAuthorizedUnit(param.getAuthorizedUnit());
        entity.setStorageSite(param.getStorageSite());
        entity.setInboundTime(param.getInboundTime());
        entity.setStorageNature(param.getStorageNature());
        entity.setStorageMode(param.getStorageMode());
        entity.setConstantTemperatureRate(param.getConstantTemperatureRate());
        entity.setBulkDensityOrRoughness(param.getBulkDensityOrRoughness());
        entity.setConvertedMoisture(param.getConvertedMoisture());
        entity.setWholeOrImperfectGrain(param.getWholeOrImperfectGrain());
    }

    private void fillQualityInspection(WarehouseLocationCardQualityInspection entity,
                                       WarehouseLocationCardQualityInspectionSaveParam param
                                      ) {
        entity.setInspectionTime(param.getInspectionTime());
        entity.setMoisture(param.getMoisture());
        entity.setImpurity(param.getImpurity());
        entity.setRoughness(param.getRoughness());
        entity.setColorSmell(param.getColorSmell());
        entity.setMixedRate(param.getMixedRate());
        entity.setHeadRiceRate(param.getHeadRiceRate());
        entity.setOutOfPaddyBrownRiceRate(param.getOutOfPaddyBrownRiceRate());
        entity.setYellowRiceRate(param.getYellowRiceRate());
        entity.setTasteScore(param.getTasteScore());
        entity.setFattyAcid(param.getFattyAcid());
        entity.setCadmium(param.getCadmium());
        entity.setAflatoxinB1(param.getAflatoxinB1());
        entity.setLead(param.getLead());
        entity.setMercury(param.getMercury());
        entity.setInorganicArsenic(param.getInorganicArsenic());
        entity.setInspector(param.getInspector());
    }

    private void saveChangeLog(WarehouseLocationCardFieldChangeLog log) {
        if (log != null) {
            fieldChangeLogService.save(log);
        }
    }

    private <T> WarehouseLocationCardFieldChangeLog compareFields(Long warehouseId, String bizType, Long bizId,
                                                                  T oldEntity, T newEntity,
                                                                  List<FieldMeta<T>> fields
                                                                 ) {
        var changes = new ArrayList<FieldChangeDetail>();
        for (FieldMeta<T> field : fields) {
            String oldValue = normalizeChangeValue(oldEntity == null ? null : formatValue(field.getter().apply(oldEntity)));
            String newValue = normalizeChangeValue(newEntity == null ? null : formatValue(field.getter().apply(newEntity)));
            if (oldEntity == null && newValue == null) {
                continue;
            }
            if (Objects.equals(oldValue, newValue)) {
                continue;
            }
            changes.add(new FieldChangeDetail(field.fieldName(), field.fieldLabel(), oldValue, newValue));
        }
        return newChangeLog(warehouseId, bizType, bizId, changes);
    }

    private WarehouseLocationCardFieldChangeLog newChangeLog(Long warehouseId, String bizType, Long bizId,
                                                             List<FieldChangeDetail> changes
                                                            ) {
        if (ListUtils.isEmpty(changes)) {
            return null;
        }
        var log = new WarehouseLocationCardFieldChangeLog();
        log.setId(IdWorker.getId());
        log.setWarehouseId(warehouseId);
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setChangeSummary(changes.stream()
                                     .map(FieldChangeDetail::fieldLabel)
                                     .filter(StringUtils::isNotBlank)
                                     .collect(Collectors.joining("、")));
        log.setChangeContent(JsonUtils.serialize(changes));
        return log;
    }

    private WarehouseLocationCardPositionProfileVO toProfileVO(WarehouseLocationCardPositionProfile entity) {
        if (entity == null) {
            return null;
        }
        var vo = new WarehouseLocationCardPositionProfileVO();
        vo.setWarehouseId(entity.getWarehouseId());
        vo.setPositionCode(entity.getPositionCode());
        vo.setFlatWarehouseNo(entity.getFlatWarehouseNo());
        vo.setLength(entity.getLength());
        vo.setWidth(entity.getWidth());
        vo.setHeight(entity.getHeight());
        vo.setTemperatureThreshold(entity.getTemperatureThreshold());
        vo.setTemperatureRangeMin(entity.getTemperatureRangeMin());
        vo.setTemperatureRangeMax(entity.getTemperatureRangeMax());
        vo.setDigitalGrainVolume(entity.getDigitalGrainVolume());
        vo.setDeductedVolume(entity.getDeductedVolume());
        vo.setStorageBagCount(entity.getStorageBagCount());
        vo.setAverageBagWeight(entity.getAverageBagWeight());
        vo.setOriginPlace(entity.getOriginPlace());
        vo.setHarvestYear(entity.getHarvestYear());
        vo.setKeeper(entity.getKeeper());
        vo.setFilingDate(entity.getFilingDate());
        vo.setAuthorizedUnit(entity.getAuthorizedUnit());
        vo.setStorageSite(entity.getStorageSite());
        vo.setInboundTime(entity.getInboundTime());
        vo.setStorageNature(entity.getStorageNature());
        vo.setStorageMode(entity.getStorageMode());
        vo.setConstantTemperatureRate(entity.getConstantTemperatureRate());
        vo.setBulkDensityOrRoughness(entity.getBulkDensityOrRoughness());
        vo.setConvertedMoisture(entity.getConvertedMoisture());
        vo.setWholeOrImperfectGrain(entity.getWholeOrImperfectGrain());
        return vo;
    }

    private WarehouseLocationCardQualityInspectionVO toQualityInspectionVO(WarehouseLocationCardQualityInspection entity) {
        var vo = new WarehouseLocationCardQualityInspectionVO();
        vo.setId(entity.getId());
        vo.setInspectionTime(entity.getInspectionTime());
        vo.setMoisture(entity.getMoisture());
        vo.setImpurity(entity.getImpurity());
        vo.setRoughness(entity.getRoughness());
        vo.setColorSmell(entity.getColorSmell());
        vo.setMixedRate(entity.getMixedRate());
        vo.setHeadRiceRate(entity.getHeadRiceRate());
        vo.setOutOfPaddyBrownRiceRate(entity.getOutOfPaddyBrownRiceRate());
        vo.setYellowRiceRate(entity.getYellowRiceRate());
        vo.setTasteScore(entity.getTasteScore());
        vo.setFattyAcid(entity.getFattyAcid());
        vo.setCadmium(entity.getCadmium());
        vo.setAflatoxinB1(entity.getAflatoxinB1());
        vo.setLead(entity.getLead());
        vo.setMercury(entity.getMercury());
        vo.setInorganicArsenic(entity.getInorganicArsenic());
        vo.setInspector(entity.getInspector());
        return vo;
    }

    private WarehouseLocationCardFieldChangeLogVO toChangeLogVO(WarehouseLocationCardFieldChangeLog entity) {
        var vo = new WarehouseLocationCardFieldChangeLogVO();
        vo.setId(entity.getId());
        vo.setWarehouseId(entity.getWarehouseId());
        vo.setBizType(entity.getBizType());
        vo.setBizId(entity.getBizId());
        vo.setChangeSummary(entity.getChangeSummary());
        vo.setChangeContent(entity.getChangeContent());
        vo.setCreateBy(entity.getCreateBy());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    private static WarehouseLocationCardPositionProfile copyProfile(WarehouseLocationCardPositionProfile source) {
        var target = new WarehouseLocationCardPositionProfile();
        target.setId(source.getId());
        copyBasicFields(source, target);
        target.setWarehouseId(source.getWarehouseId());
        target.setPositionCode(source.getPositionCode());
        target.setFlatWarehouseNo(source.getFlatWarehouseNo());
        target.setLength(source.getLength());
        target.setWidth(source.getWidth());
        target.setHeight(source.getHeight());
        target.setTemperatureThreshold(source.getTemperatureThreshold());
        target.setTemperatureRangeMin(source.getTemperatureRangeMin());
        target.setTemperatureRangeMax(source.getTemperatureRangeMax());
        target.setDigitalGrainVolume(source.getDigitalGrainVolume());
        target.setDeductedVolume(source.getDeductedVolume());
        target.setStorageBagCount(source.getStorageBagCount());
        target.setAverageBagWeight(source.getAverageBagWeight());
        target.setOriginPlace(source.getOriginPlace());
        target.setHarvestYear(source.getHarvestYear());
        target.setKeeper(source.getKeeper());
        target.setFilingDate(source.getFilingDate());
        target.setAuthorizedUnit(source.getAuthorizedUnit());
        target.setStorageSite(source.getStorageSite());
        target.setInboundTime(source.getInboundTime());
        target.setStorageNature(source.getStorageNature());
        target.setStorageMode(source.getStorageMode());
        target.setConstantTemperatureRate(source.getConstantTemperatureRate());
        target.setBulkDensityOrRoughness(source.getBulkDensityOrRoughness());
        target.setConvertedMoisture(source.getConvertedMoisture());
        target.setWholeOrImperfectGrain(source.getWholeOrImperfectGrain());
        return target;
    }

    private static WarehouseLocationCardQualityInspection copyQualityInspection(WarehouseLocationCardQualityInspection source) {
        var target = new WarehouseLocationCardQualityInspection();
        target.setId(source.getId());
        copyBasicFields(source, target);
        target.setWarehouseId(source.getWarehouseId());
        target.setInspectionTime(source.getInspectionTime());
        target.setMoisture(source.getMoisture());
        target.setImpurity(source.getImpurity());
        target.setRoughness(source.getRoughness());
        target.setColorSmell(source.getColorSmell());
        target.setMixedRate(source.getMixedRate());
        target.setHeadRiceRate(source.getHeadRiceRate());
        target.setOutOfPaddyBrownRiceRate(source.getOutOfPaddyBrownRiceRate());
        target.setYellowRiceRate(source.getYellowRiceRate());
        target.setTasteScore(source.getTasteScore());
        target.setFattyAcid(source.getFattyAcid());
        target.setCadmium(source.getCadmium());
        target.setAflatoxinB1(source.getAflatoxinB1());
        target.setLead(source.getLead());
        target.setMercury(source.getMercury());
        target.setInorganicArsenic(source.getInorganicArsenic());
        target.setInspector(source.getInspector());
        return target;
    }

    private static void copyBasicFields(BasicEnhancedTenantEntity<Long> source, BasicEnhancedTenantEntity<Long> target) {
        target.setDeleted(source.getDeleted());
        target.setCreateBy(source.getCreateBy());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateBy(source.getUpdateBy());
        target.setUpdateTime(source.getUpdateTime());
        target.setCreatorId(source.getCreatorId());
        target.setCreatorType(source.getCreatorType());
        target.setUpdaterId(source.getUpdaterId());
        target.setUpdaterType(source.getUpdaterType());
        target.setTenant(source.getTenant());
    }

    private static List<FieldMeta<WarehouseLocationCardPositionProfile>> profileFields() {
        return List.of(
                new FieldMeta<>("positionCode", "货位编号", WarehouseLocationCardPositionProfile::getPositionCode),
                new FieldMeta<>("flatWarehouseNo", "平房仓号", WarehouseLocationCardPositionProfile::getFlatWarehouseNo),
                new FieldMeta<>("length", "长度", WarehouseLocationCardPositionProfile::getLength),
                new FieldMeta<>("width", "宽度", WarehouseLocationCardPositionProfile::getWidth),
                new FieldMeta<>("height", "高度", WarehouseLocationCardPositionProfile::getHeight),
                new FieldMeta<>("temperatureThreshold", "温度阈值", WarehouseLocationCardPositionProfile::getTemperatureThreshold),
                new FieldMeta<>("temperatureRangeMin", "温度范围最小值", WarehouseLocationCardPositionProfile::getTemperatureRangeMin),
                new FieldMeta<>("temperatureRangeMax", "温度范围最大值", WarehouseLocationCardPositionProfile::getTemperatureRangeMax),
                new FieldMeta<>("digitalGrainVolume", "数字粮体积", WarehouseLocationCardPositionProfile::getDigitalGrainVolume),
                new FieldMeta<>("deductedVolume", "扣除体积", WarehouseLocationCardPositionProfile::getDeductedVolume),
                new FieldMeta<>("storageBagCount", "包存量包数", WarehouseLocationCardPositionProfile::getStorageBagCount),
                new FieldMeta<>("averageBagWeight", "平均包量", WarehouseLocationCardPositionProfile::getAverageBagWeight),
                new FieldMeta<>("originPlace", "产地", WarehouseLocationCardPositionProfile::getOriginPlace),
                new FieldMeta<>("harvestYear", "收获年度", WarehouseLocationCardPositionProfile::getHarvestYear),
                new FieldMeta<>("keeper", "保管员", WarehouseLocationCardPositionProfile::getKeeper),
                new FieldMeta<>("filingDate", "填卡日期", WarehouseLocationCardPositionProfile::getFilingDate),
                new FieldMeta<>("authorizedUnit", "授权单位", WarehouseLocationCardPositionProfile::getAuthorizedUnit),
                new FieldMeta<>("storageSite", "储存库点", WarehouseLocationCardPositionProfile::getStorageSite),
                new FieldMeta<>("inboundTime", "入仓时间", WarehouseLocationCardPositionProfile::getInboundTime),
                new FieldMeta<>("storageNature", "储存性质", WarehouseLocationCardPositionProfile::getStorageNature),
                new FieldMeta<>("storageMode", "保管方式", WarehouseLocationCardPositionProfile::getStorageMode),
                new FieldMeta<>("constantTemperatureRate", "恒温率指标", WarehouseLocationCardPositionProfile::getConstantTemperatureRate),
                new FieldMeta<>("bulkDensityOrRoughness", "容重/粗糙率", WarehouseLocationCardPositionProfile::getBulkDensityOrRoughness),
                new FieldMeta<>("convertedMoisture", "折合水分", WarehouseLocationCardPositionProfile::getConvertedMoisture),
                new FieldMeta<>("wholeOrImperfectGrain", "整精米粒/不完善粒", WarehouseLocationCardPositionProfile::getWholeOrImperfectGrain)
                      );
    }

    private static List<FieldMeta<WarehouseLocationCardQualityInspection>> qualityInspectionFields() {
        return List.of(
                new FieldMeta<>("inspectionTime", "检测时间", WarehouseLocationCardQualityInspection::getInspectionTime),
                new FieldMeta<>("moisture", "水分", WarehouseLocationCardQualityInspection::getMoisture),
                new FieldMeta<>("impurity", "杂质", WarehouseLocationCardQualityInspection::getImpurity),
                new FieldMeta<>("roughness", "出糙率", WarehouseLocationCardQualityInspection::getRoughness),
                new FieldMeta<>("colorSmell", "色泽气味", WarehouseLocationCardQualityInspection::getColorSmell),
                new FieldMeta<>("mixedRate", "互混率", WarehouseLocationCardQualityInspection::getMixedRate),
                new FieldMeta<>("headRiceRate", "整精米率", WarehouseLocationCardQualityInspection::getHeadRiceRate),
                new FieldMeta<>("outOfPaddyBrownRiceRate", "谷外糙米", WarehouseLocationCardQualityInspection::getOutOfPaddyBrownRiceRate),
                new FieldMeta<>("yellowRiceRate", "黄粒米", WarehouseLocationCardQualityInspection::getYellowRiceRate),
                new FieldMeta<>("tasteScore", "品尝分值", WarehouseLocationCardQualityInspection::getTasteScore),
                new FieldMeta<>("fattyAcid", "脂肪酸值", WarehouseLocationCardQualityInspection::getFattyAcid),
                new FieldMeta<>("cadmium", "镉", WarehouseLocationCardQualityInspection::getCadmium),
                new FieldMeta<>("aflatoxinB1", "黄曲霉毒素B1", WarehouseLocationCardQualityInspection::getAflatoxinB1),
                new FieldMeta<>("lead", "铅", WarehouseLocationCardQualityInspection::getLead),
                new FieldMeta<>("mercury", "汞", WarehouseLocationCardQualityInspection::getMercury),
                new FieldMeta<>("inorganicArsenic", "无机砷", WarehouseLocationCardQualityInspection::getInorganicArsenic),
                new FieldMeta<>("inspector", "检测人", WarehouseLocationCardQualityInspection::getInspector)
                      );
    }

    private TemperatureReportLayoutConfig loadTemperatureReportLayoutConfig() {
        Requester previousRequester = SessionContextHolder.getRequester(true);
        try {
            var query = new DataDictionaryaramByItemAndCodeParam();
            query.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
            query.setCode(TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT);
            var dict = dataDictionaryClient.getDataDictionaryByItemAndCode(query);
            if (dict == null || StringUtils.isBlank(dict.getVariable())) {
                return null;
            }
            TemperatureReportLayoutConfig config = JsonUtils.deserialize(dict.getVariable(), TemperatureReportLayoutConfig.class);
            if (config == null || config.getWarehouseId() == null || ListUtils.isEmpty(config.getLayout())) {
                log.warn("货位卡特殊仓温度配置不完整 itemCode={} code={}",
                         TenantDictionary.ITEM_THIRD_CONFIG, TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT);
                return null;
            }
            return config;
        } catch (RuntimeException e) {
            log.warn("货位卡特殊仓温度配置解析失败 itemCode={} code={}",
                     TenantDictionary.ITEM_THIRD_CONFIG, TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT, e);
            return null;
        } finally {
            restoreRequester(previousRequester);
        }
    }

    private static boolean matchesTemperatureReportLayoutWarehouse(Long warehouseId, TemperatureReportLayoutConfig layoutConfig) {
        return warehouseId != null && layoutConfig != null && warehouseId.equals(layoutConfig.getWarehouseId());
    }

    private static void applyTemperatureAggregate(List<BigDecimal> values, WarehouseLocationCardEnvironmentMonitorVO out) {
        if (ListUtils.isEmpty(values)) {
            return;
        }
        BigDecimal max = null;
        BigDecimal min = null;
        for (BigDecimal value : values) {
            if (value == null) {
                continue;
            }
            if (max == null || value.compareTo(max) > 0) {
                max = value;
            }
            if (min == null || value.compareTo(min) < 0) {
                min = value;
            }
        }
        out.setMaxTemperature(toLocationCardScale(max));
        out.setMinTemperature(toLocationCardScale(min));
        out.setAvgTemperature(toLocationCardScale(average(values)));
    }

    private static void restoreRequester(Requester requester) {
        if (requester == null) {
            SessionContextHolder.clearSessionContext();
        } else {
            SessionContextHolder.setRequester(requester);
        }
    }

    private static void addTemperatureRole(Map<String, TemperatureRole> out, String propCode, TemperatureRole role) {
        if (StringUtils.isNotBlank(propCode)) {
            out.put(propCode.trim(), role);
        }
    }

    private static BigDecimal findPropValue(List<KeyValueWithTsDTO> values, String propCode) {
        if (ListUtils.isEmpty(values) || StringUtils.isBlank(propCode)) {
            return null;
        }
        String code = propCode.trim();
        for (KeyValueWithTsDTO value : values) {
            if (value != null && value.getKey() != null && code.equals(value.getKey().trim())) {
                return toBigDecimal(value.getValue());
            }
        }
        return null;
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> productCodes) {
        if (detail == null || ListUtils.isEmpty(productCodes)) {
            return false;
        }
        for (String code : productCodes) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            if (detail.getDeviceIotProdId() != null && code.trim().equals(String.valueOf(detail.getDeviceIotProdId()))) {
                return true;
            }
        }
        return false;
    }

    private static String resolveGasRealtimePropCode(LiangcangLocationCardConfigDTO cfg) {
        if (cfg == null) {
            return null;
        }
        return StringUtils.isBlank(cfg.getGasRealtimePropCode()) ? null : cfg.getGasRealtimePropCode().trim();
    }

    private static GasRole resolveGasRole(DeviceBaseDetailsDTO detail) {
        if (detail == null || StringUtils.isBlank(detail.getDeviceName())) {
            return null;
        }
        String name = detail.getDeviceName().trim();
        if (name.contains("二氧化碳") || name.contains("CO2") || name.contains("co2")) {
            return GasRole.CO2;
        }
        if (name.contains("磷化氢") || name.contains("PH3") || name.contains("ph3") || name.contains("PH₃")) {
            return GasRole.PH3;
        }
        if (name.contains("氧气") || name.contains("O2") || name.contains("o2")) {
            return GasRole.O2;
        }
        return null;
    }

    private static BigDecimal average(List<BigDecimal> values) {
        if (ListUtils.isEmpty(values)) {
            return null;
        }
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
                count++;
            }
        }
        return count == 0 ? null : sum.divide(BigDecimal.valueOf(count), NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal ppmToPercentVol(BigDecimal ppm) {
        return ppm == null ? null : ppm.divide(PPM_TO_PERCENT_VOL_DIVISOR, NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static String normalizeChangeValue(String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    private static BigDecimal toLocationCardScale(BigDecimal value) {
        return value == null ? null : value.setScale(NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal toBigDecimal(Object value) {
        switch (value) {
            case null -> {
                return null;
            }
            case BigDecimal bigDecimal -> {
                return bigDecimal;
            }
            case Number number -> {
                return BigDecimal.valueOf(number.doubleValue());
            }
            default -> {
            }
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String formatValue(Object value) {
        switch (value) {
            case null -> {
                return null;
            }
            case BigDecimal bigDecimal -> {
                return bigDecimal.stripTrailingZeros().toPlainString();
            }
            case LocalDateTime localDateTime -> {
                return TIME_FORMATTER.format(localDateTime);
            }
            default -> {
                return String.valueOf(value);
            }
        }
    }

    private enum TemperatureRole {
        MAX,
        MIN,
        AVG
    }

    private enum GasRole {
        CO2,
        O2,
        PH3
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TemperatureReportLayoutConfig {
        private Long warehouseId;
        private List<List<List<String>>> layout;
    }

    private record FieldMeta<T>(String fieldName, String fieldLabel, Function<T, Object> getter) {
    }

    private record FieldChangeDetail(String fieldName, String fieldLabel, String oldValue, String newValue) {
    }
}
