package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceRuleConditionPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagRuleVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.assembler.LiangcangLocationCardInfoAssembler;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehouseInfoApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.Business;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.DeviceCenter;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.DeviceCenterRuleMerge;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.WarehousePagePull;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseDeviceBindingMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseDeviceResDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.event.WarehouseUnboundEvent;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ApplicationRelatedDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBindingDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBoundPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarDeviceBindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarDeviceEditParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseBoundDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStatDimension;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseGoodsHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehousePersonPermissionService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ApplicationRelatedDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.BoundWarehouseSimpleVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangLocationCardInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehouseListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBindingDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBoundPageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseBoundDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseDeviceInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStatisticsAcquisitionDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStatisticsRowVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStatisticsVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WarehouseDeviceBindingServiceImpl extends ServiceImpl<WarehouseDeviceBindingMapper, WarehouseDeviceBinding>
        implements WarehouseDeviceBindingService {

    private static final int ROOT_PARENT_ID = 0;

    private static final DateTimeFormatter ACQUISITION_TIME_LABEL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final DeviceCenterStdClient deviceCenterStdClient;
    private final WarehouseInfoApiClient warehouseInfoApiClient;
    private final WarehousePageApiClient warehousePageApiClient;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseGoodsHistoryService warehouseGoodsHistoryService;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final LiangcangLocationCardInfoAssembler liangcangLocationCardInfoAssembler;
    private final ObjectProvider<WarehouseLocationCardConfigService> warehouseLocationCardConfigService;
    private final ObjectProvider<WarehouseTemperatureTrendStatisticsService> warehouseTemperatureTrendStatisticsService;
    private final WarehousePersonPermissionService warehousePersonPermissionService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SysDictionaryClient sysDictionaryClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindDevices(RadarDeviceBindParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getRadarDeviceId() == null, MsgBinding.RADAR_DEVICE_ID_REQUIRED);
        RadarWarehouseBinding existingWarehouseBinding = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, param.getWarehouseId())
                .one();
        Assert.isParameterError(existingWarehouseBinding != null
                                && !Objects.equals(existingWarehouseBinding.getRadarDeviceId(), param.getRadarDeviceId()),
                                MsgBinding.WAREHOUSE_ALREADY_BOUND);
        if (existingWarehouseBinding == null) {
            boolean radarBound = radarWarehouseBindingService.lambdaQuery()
                    .eq(RadarWarehouseBinding::getRadarDeviceId, param.getRadarDeviceId())
                    .exists();
            Assert.isParameterError(radarBound, MsgBinding.RADAR_ALREADY_BOUND);
        }

        List<Long> distinctDeviceIds = resolveBindDeviceIds(param);
        Assert.isParameterError(!(ListUtils.nonEmpty(distinctDeviceIds)), MsgBinding.DEVICE_IDS_REQUIRED);

        // 设备已绑定则不能再次绑定（表 uk_device_id 保证全局唯一）
        var boundDeviceRows = this.list(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                                .select(WarehouseDeviceBinding::getDeviceId)
                                                .in(WarehouseDeviceBinding::getDeviceId, distinctDeviceIds));
        var boundDeviceIds = ListUtils.convertItem(boundDeviceRows, WarehouseDeviceBinding::getDeviceId);
        Assert.isParameterError(!(ListUtils.isEmpty(boundDeviceRows)), MsgBinding.DEVICES_ALREADY_BOUND_PREFIX + boundDeviceIds);

        List<WarehouseDeviceBinding> insertList = ListUtils.convertItem(distinctDeviceIds, deviceId -> {
            var po = new WarehouseDeviceBinding();
            po.setId(IdWorker.getId());
            po.setWarehouseId(param.getWarehouseId());
            po.setDeviceId(deviceId);
            return po;
        });
        this.saveBatch(insertList);

        if (existingWarehouseBinding == null) {
            var warehouseBinding = new RadarWarehouseBinding();
            warehouseBinding.setId(IdWorker.getId());
            warehouseBinding.setWarehouseId(param.getWarehouseId());
            warehouseBinding.setRadarDeviceId(param.getRadarDeviceId());
            radarWarehouseBindingService.save(warehouseBinding);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editDevices(RadarDeviceEditParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getOriginalWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getOriginalRadarDeviceId() == null, MsgBinding.RADAR_DEVICE_ID_REQUIRED);
        Assert.isParameterError(param.getNewWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getNewRadarDeviceId() == null, MsgBinding.RADAR_DEVICE_ID_REQUIRED);
        Long sourceWarehouseId = param.getOriginalWarehouseId();
        Long sourceRadarDeviceId = param.getOriginalRadarDeviceId();
        Long targetWarehouseId = param.getNewWarehouseId();
        Long targetRadarDeviceId = param.getNewRadarDeviceId();

        var warehouseRadar = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, sourceWarehouseId)
                .eq(RadarWarehouseBinding::getRadarDeviceId, sourceRadarDeviceId)
                .one();
        Assert.isParameterError(warehouseRadar == null, MsgBinding.WAREHOUSE_NOT_BOUND);

        boolean warehouseBoundByOtherRadar = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, targetWarehouseId)
                .ne(RadarWarehouseBinding::getId, warehouseRadar.getId())
                .exists();
        Assert.isParameterError(warehouseBoundByOtherRadar, MsgBinding.WAREHOUSE_ALREADY_BOUND);
        boolean radarBoundByOtherWarehouse = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getRadarDeviceId, targetRadarDeviceId)
                .ne(RadarWarehouseBinding::getId, warehouseRadar.getId())
                .exists();
        Assert.isParameterError(radarBoundByOtherWarehouse, MsgBinding.RADAR_ALREADY_BOUND);

        List<Long> distinctDeviceIds = resolveEditDeviceIds(param, sourceWarehouseId, targetRadarDeviceId);
        Assert.isParameterError(!(ListUtils.nonEmpty(distinctDeviceIds)), MsgBinding.DEVICE_IDS_REQUIRED);

        var boundByOtherWarehouseRows = this.list(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                                          .select(WarehouseDeviceBinding::getDeviceId)
                                                          .in(WarehouseDeviceBinding::getDeviceId, distinctDeviceIds)
                                                          .ne(WarehouseDeviceBinding::getWarehouseId, sourceWarehouseId));
        var boundByOtherWarehouseIds = ListUtils.convertItem(boundByOtherWarehouseRows, WarehouseDeviceBinding::getDeviceId);
        Assert.isParameterError(!(ListUtils.isEmpty(boundByOtherWarehouseRows)),
                                MsgBinding.DEVICES_ALREADY_BOUND_PREFIX + boundByOtherWarehouseIds);

        var currentRows = this.list(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                            .select(WarehouseDeviceBinding::getDeviceId)
                                            .eq(WarehouseDeviceBinding::getWarehouseId, sourceWarehouseId));
        Set<Long> currentDeviceIds = SetUtils.listToSet(currentRows, WarehouseDeviceBinding::getDeviceId);
        Set<Long> targetDeviceIds = new HashSet<>(distinctDeviceIds);

        Set<Long> removeDeviceIds = new HashSet<>(currentDeviceIds);
        removeDeviceIds.removeAll(targetDeviceIds);
        if (SetUtils.isNotEmpty(removeDeviceIds)) {
            this.remove(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                .eq(WarehouseDeviceBinding::getWarehouseId, sourceWarehouseId)
                                .in(WarehouseDeviceBinding::getDeviceId, removeDeviceIds));
        }

        Set<Long> retainDeviceIds = new HashSet<>(currentDeviceIds);
        retainDeviceIds.retainAll(targetDeviceIds);
        if (!Objects.equals(sourceWarehouseId, targetWarehouseId) && SetUtils.isNotEmpty(retainDeviceIds)) {
            this.update(Wrappers.<WarehouseDeviceBinding>lambdaUpdate()
                                .set(WarehouseDeviceBinding::getWarehouseId, targetWarehouseId)
                                .eq(WarehouseDeviceBinding::getWarehouseId, sourceWarehouseId)
                                .in(WarehouseDeviceBinding::getDeviceId, retainDeviceIds));
        }

        Set<Long> addDeviceIds = new HashSet<>(targetDeviceIds);
        addDeviceIds.removeAll(currentDeviceIds);
        if (SetUtils.isNotEmpty(addDeviceIds)) {
            List<WarehouseDeviceBinding> insertList = ListUtils.convertItem(new ArrayList<>(addDeviceIds), deviceId -> {
                var po = new WarehouseDeviceBinding();
                po.setId(IdWorker.getId());
                po.setWarehouseId(targetWarehouseId);
                po.setDeviceId(deviceId);
                return po;
            });
            this.saveBatch(insertList);
        }

        if (!Objects.equals(sourceWarehouseId, targetWarehouseId)) {
            warehouseRadar.setWarehouseId(targetWarehouseId);
        }
        if (!Objects.equals(sourceRadarDeviceId, targetRadarDeviceId)) {
            warehouseRadar.setRadarDeviceId(targetRadarDeviceId);
        }
        if (!Objects.equals(sourceWarehouseId, targetWarehouseId)
            || !Objects.equals(sourceRadarDeviceId, targetRadarDeviceId)) {
            radarWarehouseBindingService.updateById(warehouseRadar);
        }
        return true;
    }

    private List<Long> resolveBindDeviceIds(RadarDeviceBindParam param) {
        if (Boolean.TRUE.equals(param.getSelectAllDevices())) {
            return queryAllApplicationRelatedDeviceIds(filterNullIds(param.getProductIds()),
                                                       filterNullIds(param.getAreaIds()),
                                                       param.getDeviceName(),
                                                       null,
                                                       param.getRadarDeviceId());
        }
        Assert.isParameterError(!(ListUtils.nonEmpty(param.getDeviceIds())), MsgBinding.DEVICE_IDS_REQUIRED);
        return ListUtils.distinct(param.getDeviceIds());
    }

    private List<Long> resolveEditDeviceIds(RadarDeviceEditParam param, Long sourceWarehouseId, Long targetRadarDeviceId) {
        if (Boolean.TRUE.equals(param.getSelectAllDevices())) {
            return queryAllApplicationRelatedDeviceIds(filterNullIds(param.getProductIds()),
                                                       filterNullIds(param.getAreaIds()),
                                                       param.getDeviceName(),
                                                       sourceWarehouseId,
                                                       targetRadarDeviceId);
        }
        Assert.isParameterError(!(ListUtils.nonEmpty(param.getDeviceIds())), MsgBinding.DEVICE_IDS_REQUIRED);
        return ListUtils.distinct(param.getDeviceIds());
    }

    private List<Long> queryAllApplicationRelatedDeviceIds(Set<Long> productIds, Set<Long> areaIds, String deviceName,
                                                           Long excludeWarehouseId, Long radarDeviceId
                                                          ) {
        DeviceTagRuleVO tagRule = deviceCenterStdClient.getTagRule(DeviceCenter.TAG_CODE_APP_RESERVE_MONITORING);
        if (tagRule == null || StringUtils.isBlank(tagRule.getRule())) {
            return List.of();
        }
        Set<Long> excludedDeviceIds = collectExcludedDeviceIdsFromBindings(excludeWarehouseId);
        if (radarDeviceId != null) {
            excludedDeviceIds.add(radarDeviceId);
        }
        Object ruleGroup = buildApplicationDeviceRuleGroup(tagRule.getRule(), excludedDeviceIds,
                                                           productIds, areaIds, deviceName);
        if (ruleGroup == null) {
            return List.of();
        }
        List<Long> deviceIds = new ArrayList<>();
        for (int pageNumber = 1; ; pageNumber++) {
            var pageReq = new DeviceRuleConditionPageParam();
            pageReq.setPageNumber(pageNumber);
            pageReq.setPageSize(DeviceCenter.APPLICATION_DEVICE_SELECT_ALL_PAGE_SIZE);
            pageReq.setRuleGroup(ruleGroup);
            Pagination<DevicePageInfoVO> page = deviceCenterStdClient.deviceRuleConditionInfoPage(pageReq);
            List<DevicePageInfoVO> records = page.getRecords();
            if (ListUtils.isEmpty(records)) {
                break;
            }
            for (DevicePageInfoVO record : records) {
                if (record != null && record.getId() != null) {
                    deviceIds.add(record.getId());
                }
            }
            if (page.getPages() == null || page.getPages() <= pageNumber) {
                break;
            }
        }
        return ListUtils.distinct(deviceIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindDevices(WarehouseUnbindParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        List<Long> deviceIds = resolveUnbindDeviceIds(param);
        if (ListUtils.nonEmpty(deviceIds)) {
            this.remove(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId)
                                .in(WarehouseDeviceBinding::getDeviceId, deviceIds));
        }
        cleanupWarehouseBindingIfNoDevices(warehouseId);
        return true;
    }

    private void cleanupWarehouseBindingIfNoDevices(Long warehouseId) {
        boolean hasBoundDevices = this.lambdaQuery()
                .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId)
                .exists();
        if (hasBoundDevices) {
            return;
        }
        radarWarehouseBindingService.remove(Wrappers.<RadarWarehouseBinding>lambdaQuery()
                                                    .eq(RadarWarehouseBinding::getWarehouseId, warehouseId));
        applicationEventPublisher.publishEvent(new WarehouseUnboundEvent(warehouseId));
    }

    private List<Long> resolveUnbindDeviceIds(WarehouseUnbindParam param) {
        if (Boolean.TRUE.equals(param.getSelectAllDevices())) {
            return queryBoundDeviceIds(param.getWarehouseId(),
                                       filterNullIds(param.getProductIds()),
                                       filterNullIds(param.getAreaIds()),
                                       param.getDeviceName());
        }
        Assert.isParameterError(!(ListUtils.nonEmpty(param.getDeviceIds())), MsgBinding.DEVICE_IDS_REQUIRED);
        return ListUtils.distinct(param.getDeviceIds());
    }

    private List<Long> queryBoundDeviceIds(Long warehouseId, Set<Long> productIds, Set<Long> areaIds, String deviceName) {
        List<WarehouseDeviceBinding> bindings = this.list(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                                                  .select(WarehouseDeviceBinding::getDeviceId)
                                                                  .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId));
        if (ListUtils.isEmpty(bindings)) {
            return List.of();
        }
        Set<Long> deviceIds = SetUtils.listToSet(bindings, WarehouseDeviceBinding::getDeviceId);
        Map<Long, DeviceBaseDetailsDTO> detailMap = loadDeviceDetailsById(deviceIds);
        List<Long> out = new ArrayList<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getDeviceId() == null) {
                continue;
            }
            DeviceBaseDetailsDTO detail = detailMap.get(binding.getDeviceId());
            if (boundDeviceMatches(detail, productIds, areaIds, deviceName)) {
                out.add(binding.getDeviceId());
            }
        }
        return ListUtils.distinct(out);
    }

    /**
     * 三方 warehousePage 与本地绑定交集后分页。
     */
    @Override
    public Pagination<RadarBoundPageVO> boundWarehouseInfoPage(RadarBoundPageParam param) {
        Boolean bindingStatus = param == null || param.getBindingStatus() == null ? Boolean.TRUE : param.getBindingStatus();
        boolean queryUnbound = Boolean.FALSE.equals(bindingStatus);
        long pn = param == null || param.getPageNumber() == null || param.getPageNumber() < 1 ? 1 : param.getPageNumber();
        long ps = param == null || param.getPageSize() == null || param.getPageSize() < 1 ? 10 : param.getPageSize();
        String radarName = param == null ? null : param.getRadarName();
        var warehouseQuery = new LiangcangWarehousePageQueryParam();
        var input = new LiangcangWarehousePageQueryParam.WarehousePageInput();
        input.setSearch(radarName);
        input.setPageNum(1);
        input.setPageSize(WarehousePagePull.DEFAULT_PAGE_SIZE);
        warehouseQuery.setInput(input);

        List<LiangcangWarehousePageItemVO> matchedWarehouses = fetchAllWarehouseRows(warehouseQuery);
        if (queryUnbound && ListUtils.isEmpty(matchedWarehouses)) {
            return new Pagination<>(0L, 0L, List.of());
        }
        Map<Long, LiangcangWarehousePageItemVO> warehouseInfoMap = new LinkedHashMap<>();
        for (LiangcangWarehousePageItemVO item : matchedWarehouses) {
            if (item != null && item.getId() != null) {
                warehouseInfoMap.putIfAbsent(item.getId(), item);
            }
        }
        if (queryUnbound && MapUtils.isEmpty(warehouseInfoMap)) {
            return new Pagination<>(0L, 0L, List.of());
        }
        var matchedWarehouseIds = new ArrayList<>(warehouseInfoMap.keySet());
        if (queryUnbound) {
            Set<Long> boundWarehouseIds = SetUtils.listToSet(
                    radarWarehouseBindingService.list(Wrappers.<RadarWarehouseBinding>lambdaQuery()
                                                              .select(RadarWarehouseBinding::getWarehouseId)
                                                              .in(RadarWarehouseBinding::getWarehouseId, matchedWarehouseIds)),
                    RadarWarehouseBinding::getWarehouseId);
            return unboundWarehouseInfoPage(matchedWarehouses, boundWarehouseIds, pn, ps);
        }

        var pageQuery = radarWarehouseBindingService.lambdaQuery();
        if (StringUtils.isNotBlank(radarName)) {
            if (ListUtils.isEmpty(matchedWarehouseIds)) {
                return new Pagination<>(0L, 0L, List.of());
            }
            pageQuery.in(RadarWarehouseBinding::getWarehouseId, matchedWarehouseIds);
        }
        var page = pageQuery.page(new Page<>(pn, ps));
        if (page.getTotal() == 0) {
            return new Pagination<>(0L, 0L, List.of());
        }
        Set<Long> pageWarehouseIds = new HashSet<>();
        for (RadarWarehouseBinding bindingRow : page.getRecords()) {
            if (bindingRow != null && bindingRow.getWarehouseId() != null) {
                pageWarehouseIds.add(bindingRow.getWarehouseId());
            }
        }
        Long loginPersonId = resolveLoginPersonId();
        Set<Long> copyAllowedWarehouseIds = Set.of();
        if (loginPersonId != null && SetUtils.isNotEmpty(pageWarehouseIds)) {
            copyAllowedWarehouseIds = warehousePersonPermissionService.warehouseIdsBoundForPerson(loginPersonId, pageWarehouseIds);
        }
        if (StringUtils.isBlank(radarName)) {
            warehouseInfoMap = warehousePageApiClient.fetchPageItemsByIds(pageWarehouseIds);
        }
        Map<Long, RelatedDeviceSummary> relatedSummaryMap = relatedDeviceSummaryByWarehouseId(pageWarehouseIds);
        List<RadarBoundPageVO> records = new ArrayList<>();
        for (RadarWarehouseBinding row : page.getRecords()) {
            if (row == null || row.getWarehouseId() == null || row.getRadarDeviceId() == null) {
                continue;
            }
            long wid = row.getWarehouseId();
            long rid = row.getRadarDeviceId();
            var vo = new RadarBoundPageVO();
            vo.setWarehouseId(wid);
            vo.setRadarDeviceId(rid);
            vo.setBindingStatus(Business.BINDING_STATUS_CREATED);
            vo.setCopyAllowed(loginPersonId != null && copyAllowedWarehouseIds.contains(wid));
            var info = warehouseInfoMap.get(wid);
            if (info != null) {
                fillWarehouseInfo(vo, info);
            }
            fillRelatedDeviceSummary(vo, relatedSummaryMap.get(wid));
            records.add(vo);
        }
        return new Pagination<>(page.getTotal(), page.getPages(), records);
    }

    private Pagination<RadarBoundPageVO> unboundWarehouseInfoPage(List<LiangcangWarehousePageItemVO> matchedWarehouses,
                                                                  Set<Long> boundWarehouseIds, long pageNumber, long pageSize
                                                                 ) {
        List<RadarBoundPageVO> allRecords = new ArrayList<>();
        for (LiangcangWarehousePageItemVO item : matchedWarehouses) {
            if (item == null || item.getId() == null || boundWarehouseIds.contains(item.getId())) {
                continue;
            }
            for (WarehouseDeviceInfoVO radar : listRadarDevices(item.getDeviceList())) {
                if (radar == null || radar.getId() == null) {
                    continue;
                }
                var vo = new RadarBoundPageVO();
                vo.setWarehouseId(item.getId());
                vo.setRadarDeviceId(radar.getId().longValue());
                vo.setBindingStatus(Business.BINDING_STATUS_NOT_CREATED);
                vo.setRelatedDeviceCount(0);
                vo.setCopyAllowed(false);
                fillWarehouseInfo(vo, item);
                allRecords.add(vo);
            }
        }
        if (ListUtils.isEmpty(allRecords)) {
            return new Pagination<>(0L, 0L, List.of());
        }
        long total = allRecords.size();
        long pages = (total + pageSize - 1) / pageSize;
        int fromIndex = (int) Math.min((pageNumber - 1) * pageSize, total);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        return new Pagination<>(total, pages, allRecords.subList(fromIndex, toIndex));
    }

    private void fillWarehouseInfo(RadarBoundPageVO vo, LiangcangWarehousePageItemVO info) {
        vo.setGranaryName(info.getName());
        vo.setCategory(info.getCategoryName());
        vo.setLevel(info.getQualityGrade());
        vo.setMaxHeight(info.getWarehouseHeight());
        vo.setGranaryVolume(info.getTotalVolume());
    }

    private void fillRelatedDeviceSummary(RadarBoundPageVO vo, RelatedDeviceSummary summary) {
        if (summary == null) {
            vo.setRelatedDeviceCount(0);
            return;
        }
        vo.setRelatedDeviceCount(summary.deviceCount());
        vo.setBoundProductTypes(summary.productTypes());
    }

    private Map<Long, RelatedDeviceSummary> relatedDeviceSummaryByWarehouseId(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        List<WarehouseDeviceBinding> bindings = this.list(Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                                                                  .select(WarehouseDeviceBinding::getWarehouseId,
                                                                          WarehouseDeviceBinding::getDeviceId)
                                                                  .in(WarehouseDeviceBinding::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(bindings)) {
            return Map.of();
        }
        Set<Long> deviceIds = SetUtils.listToSet(bindings, WarehouseDeviceBinding::getDeviceId);
        Map<Long, DevicePageInfoVO> deviceInfoMap = loadDevicePageInfoById(deviceIds);
        Map<Long, Integer> countMap = new HashMap<>();
        Map<Long, Set<String>> productNameMap = new HashMap<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getWarehouseId() == null || binding.getDeviceId() == null) {
                continue;
            }
            countMap.merge(binding.getWarehouseId(), 1, Integer::sum);
            DevicePageInfoVO deviceInfo = deviceInfoMap.get(binding.getDeviceId());
            String productName = resolveProductName(deviceInfo);
            if (StringUtils.isNotBlank(productName)) {
                productNameMap.computeIfAbsent(binding.getWarehouseId(), key -> new LinkedHashSet<>())
                        .add(productName);
            }
        }
        Map<Long, RelatedDeviceSummary> out = new HashMap<>();
        for (Long warehouseId : countMap.keySet()) {
            Set<String> productTypes = productNameMap.get(warehouseId);
            out.put(warehouseId, new RelatedDeviceSummary(countMap.get(warehouseId),
                                                          SetUtils.isEmpty(productTypes) ? null : String.join("、", productTypes)));
        }
        return out;
    }

    private record RelatedDeviceSummary(Integer deviceCount, String productTypes) {
    }

    /**
     * 当前登录主体为用户时返回用户 id，否则无人员上下文（复制权限一律 false）。
     */
    private static Long resolveLoginPersonId() {
        Principal p = SecurityContextHolder.getPrincipal(true);
        if (p == null || p.getType() != Principal.Type.USER || p.getId() == null) {
            return null;
        }
        return p.getId();
    }

    @Override
    public List<BoundWarehouseSimpleVO> boundWarehouseSimpleList() {
        List<RadarWarehouseBinding> rows = radarWarehouseBindingService.lambdaQuery()
                .orderByDesc(RadarWarehouseBinding::getWarehouseId)
                .list();
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        List<Long> orderedIds = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        for (RadarWarehouseBinding row : rows) {
            if (row != null && row.getWarehouseId() != null && seen.add(row.getWarehouseId())) {
                orderedIds.add(row.getWarehouseId());
            }
        }
        if (ListUtils.isEmpty(orderedIds)) {
            return List.of();
        }
        Set<Long> needed = new HashSet<>(orderedIds);
        Map<Long, LiangcangWarehousePageItemVO> remoteById = warehousePageApiClient.fetchPageItemsByIds(needed);
        List<BoundWarehouseSimpleVO> out = new ArrayList<>();
        for (Long wid : orderedIds) {
            var vo = new BoundWarehouseSimpleVO();
            vo.setWarehouseId(wid);
            LiangcangWarehousePageItemVO item = remoteById.get(wid);
            if (item != null) {
                vo.setName(item.getName());
                vo.setCategoryName(item.getCategoryName());
            }
            out.add(vo);
        }
        return out;
    }

    @Override
    public RadarBindingDetailVO bindingDetail(RadarBindingDetailParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        long warehouseId = param.getWarehouseId();
        var remoteOpt = warehouseInfoApiClient.fetchWarehouseInfo(warehouseId);
        Assert.isParameterError(!(remoteOpt.isPresent()), MsgBinding.WAREHOUSE_INFO_NOT_FOUND);
        WarehouseDeviceResDTO res = remoteOpt.get();
        WarehouseMeasureHistory measureInfo = warehouseMeasureHistoryService.latestByWarehouseId(warehouseId)
                .orElse(null);
        var result = new RadarBindingDetailVO();
        result.setWarehouseId(warehouseId);
        result.setGranaryName(res.getName());
        result.setCategory(res.getCategoryName());
        result.setLevel(res.getQualityGrade());
        if (measureInfo != null) {
            result.setMaxHeight(toDouble(measureInfo.getTotalHeight()));
            result.setGranaryVolume(toDouble(measureInfo.getTotalVolume()));
        }

        var warehouseRadar = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, warehouseId)
                .one();
        result.setRadarDeviceId(warehouseRadar != null ? warehouseRadar.getRadarDeviceId() : null);
        return result;
    }

    @Override
    public Pagination<WarehouseBoundDeviceVO> boundDevicePage(WarehouseBoundDevicePageParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        long pageNumber = param.getPageNumber() == null || param.getPageNumber() < 1 ? 1 : param.getPageNumber();
        long pageSize = param.getPageSize() == null || param.getPageSize() < 1 ? 10 : param.getPageSize();
        List<WarehouseDeviceBinding> bindings = this.lambdaQuery()
                .eq(WarehouseDeviceBinding::getWarehouseId, param.getWarehouseId())
                .orderByDesc(WarehouseDeviceBinding::getId)
                .list();
        if (ListUtils.isEmpty(bindings)) {
            return Pagination.empty();
        }
        Set<Long> deviceIds = SetUtils.listToSet(bindings, WarehouseDeviceBinding::getDeviceId);
        Map<Long, DevicePageInfoVO> devicePageInfoMap = loadDevicePageInfoById(deviceIds);
        Set<Long> productIds = filterNullIds(param.getProductIds());
        Set<Long> areaIds = filterNullIds(param.getAreaIds());
        String deviceName = param.getDeviceName();
        List<WarehouseBoundDeviceVO> matchedRecords = new ArrayList<>();
        for (WarehouseDeviceBinding item : bindings) {
            DevicePageInfoVO deviceInfo = item.getDeviceId() == null ? null : devicePageInfoMap.get(item.getDeviceId());
            if (!boundDevicePageInfoMatches(deviceInfo, productIds, areaIds, deviceName)) {
                continue;
            }
            var vo = new WarehouseBoundDeviceVO();
            vo.setDeviceId(item.getDeviceId());
            if (deviceInfo != null) {
                vo.setDeviceType(resolveProductCategoryName(deviceInfo));
                vo.setProductName(resolveProductName(deviceInfo));
                vo.setDeviceName(deviceInfo.getName());
                vo.setAreaId(deviceInfo.getAreaId());
                vo.setAreaName(deviceInfo.getAreaName());
            }
            matchedRecords.add(vo);
        }
        if (ListUtils.isEmpty(matchedRecords)) {
            return Pagination.empty();
        }
        long total = matchedRecords.size();
        long pages = (total + pageSize - 1) / pageSize;
        int fromIndex = (int) Math.min((pageNumber - 1) * pageSize, total);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        return new Pagination<>(total, pages, matchedRecords.subList(fromIndex, toIndex));
    }

    private static boolean boundDeviceMatches(DeviceBaseDetailsDTO detail, Set<Long> productIds, Set<Long> areaIds, String deviceName) {
        if (detail == null) {
            return SetUtils.isEmpty(productIds) && SetUtils.isEmpty(areaIds) && StringUtils.isBlank(deviceName);
        }
        if (SetUtils.isNotEmpty(productIds) && !productIds.contains(resolveDeviceProductId(detail))) {
            return false;
        }
        if (SetUtils.isNotEmpty(areaIds) && !areaIds.contains(detail.getAreaLastId())) {
            return false;
        }
        return StringUtils.isBlank(deviceName)
               || (StringUtils.isNotBlank(detail.getDeviceName()) && detail.getDeviceName().contains(deviceName.trim()));
    }

    private static Long resolveDeviceProductId(DeviceBaseDetailsDTO detail) {
        return detail.getDeviceIotProdId();
    }

    private static boolean boundDevicePageInfoMatches(DevicePageInfoVO deviceInfo, Set<Long> productIds, Set<Long> areaIds,
                                                      String deviceName) {
        if (deviceInfo == null) {
            return SetUtils.isEmpty(productIds) && SetUtils.isEmpty(areaIds) && StringUtils.isBlank(deviceName);
        }
        if (SetUtils.isNotEmpty(productIds)
            && (deviceInfo.getProductInfo() == null || !productIds.contains(deviceInfo.getProductInfo().getId()))) {
            return false;
        }
        if (SetUtils.isNotEmpty(areaIds)
            && (deviceInfo.getArea() == null || !areaIds.contains(deviceInfo.getArea().getId()))) {
            return false;
        }
        return StringUtils.isBlank(deviceName)
               || (StringUtils.isNotBlank(deviceInfo.getName()) && deviceInfo.getName().contains(deviceName.trim()));
    }

    private Map<Long, DevicePageInfoVO> loadDevicePageInfoById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        Map<Long, DevicePageInfoVO> out = new HashMap<>();
        List<Long> orderedDeviceIds = new ArrayList<>(deviceIds);
        for (int fromIndex = 0; fromIndex < orderedDeviceIds.size(); fromIndex += 1000) {
            List<Long> batchDeviceIds = orderedDeviceIds.subList(fromIndex, Math.min(fromIndex + 1000, orderedDeviceIds.size()));
            var param = new DeviceFilterPageParam();
            param.setPageNumber(1);
            param.setPageSize(batchDeviceIds.size());
            param.setMatchedDeviceIds(batchDeviceIds);
            Pagination<DevicePageInfoVO> page = deviceCenterStdClient.deviceInfoPage(param);
            if (page == null || ListUtils.isEmpty(page.getRecords())) {
                continue;
            }
            fillAreaFields(page.getRecords());
            for (DevicePageInfoVO item : page.getRecords()) {
                if (item != null && item.getId() != null) {
                    out.put(item.getId(), item);
                }
            }
        }
        return out;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailsById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var detailParam = new DevicePostByIdSetParam();
        detailParam.setDeviceIdSet(deviceIds);
        var detailList = deviceCenterStdClient.postDetailsListByDeviceId(detailParam);
        return ListUtils.isEmpty(detailList) ? Map.of() : MapUtils.listToMap(detailList, DeviceBaseDetailsDTO::getId, item -> item);
    }

    @Override
    public LiangcangLocationCardInfoVO locationCardInfo(RadarBindingDetailParam param) {
        Requester previousRequester = SessionContextHolder.getRequester(true);
        Integer locationCardTenant = loadLocationCardTenantFromSystemDictionary(previousRequester);
        try {
            SessionContextHolder.setRequester(withTenant(previousRequester, locationCardTenant));
            LiangcangLocationCardInfoVO info = liangcangLocationCardInfoAssembler.build(param);
            var reportParam = new WarehouseTemperatureReportParam();
            reportParam.setWarehouseId(param == null ? null : param.getWarehouseId());
            List<List<WarehouseTemperatureReportPointVO>> reportList =
                    warehouseTemperatureTrendStatisticsService.getObject().temperatureReport(reportParam);
            var detailParam = new WarehouseLocationCardConfigDetailParam();
            detailParam.setWarehouseId(param == null ? null : param.getWarehouseId());
            info.setConfigDetail(warehouseLocationCardConfigService.getObject().detail(detailParam, reportList));
            if (info.getConfigDetail() != null) {
                info.setTemperatureAlertNotice(info.getConfigDetail().getTemperatureAlertNotice());
            }
            info.setReportList(reportList);
            return info;
        } finally {
            restoreRequester(previousRequester);
        }
    }

    private Integer loadLocationCardTenantFromSystemDictionary(Requester previousRequester) {
        try {
            SessionContextHolder.clearSessionContext();
            var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                             LiangcangSystemDictionary.CODE_CARD_LOCATION);
            Assert.isParameterError(dict == null || StringUtils.isBlank(dict.getVariable()), "货位卡租户系统字典未配置");
            try {
                return Integer.valueOf(dict.getVariable().trim());
            } catch (NumberFormatException e) {
                Assert.isParameterError(true, "货位卡租户系统字典 variable 必须为数字");
                return null;
            }
        } finally {
            restoreRequester(previousRequester);
        }
    }

    private static Requester withTenant(Requester source, Integer tenant) {
        if (source == null) {
            return Requester.builder().tenant(tenant).build();
        }
        return Requester.builder()
                .uri(source.getUri())
                .host(source.getHost())
                .domain(source.getDomain())
                .authorization(source.getAuthorization())
                .tenant(tenant)
                .terminal(source.getTerminal())
                .version(source.getVersion())
                .signature(source.getSignature())
                .permission(source.getPermission())
                .build();
    }

    private static void restoreRequester(Requester requester) {
        if (requester == null) {
            SessionContextHolder.clearSessionContext();
        } else {
            SessionContextHolder.setRequester(requester);
        }
    }

    @Override
    public List<LiangcangWarehouseListItemVO> warehouseList(LiangcangWarehousePageQueryParam param) {
        List<LiangcangWarehousePageItemVO> rows = fetchAllWarehouseRows(param);
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        Set<Long> warehouseIds = new HashSet<>();
        for (LiangcangWarehousePageItemVO item : rows) {
            if (item != null && item.getId() != null) {
                warehouseIds.add(item.getId());
            }
        }
        Map<Long, WarehouseMeasureHistory> measureInfoByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(warehouseIds);
        Map<String, LiangcangWarehouseListItemVO> byWarehouseAndRadar = new LinkedHashMap<>();
        for (LiangcangWarehousePageItemVO item : rows) {
            if (item == null || item.getId() == null) {
                continue;
            }
            for (WarehouseDeviceInfoVO dev : listRadarDevices(item.getDeviceList())) {
                if (dev == null || dev.getId() == null) {
                    continue;
                }
                long rid = dev.getId().longValue();
                String key = item.getId() + Business.KEY_SEPARATOR_WAREHOUSE_RADAR + rid;
                if (byWarehouseAndRadar.containsKey(key)) {
                    continue;
                }
                var vo = new LiangcangWarehouseListItemVO();
                vo.setWarehouseId(item.getId());
                vo.setWarehouseName(item.getName());
                vo.setCategoryName(item.getCategoryName());
                vo.setQualityGrade(item.getQualityGrade());
                WarehouseMeasureHistory measureInfo = measureInfoByWarehouseId.get(item.getId());
                if (measureInfo != null) {
                    vo.setWarehouseHeight(toDouble(measureInfo.getTotalHeight()));
                    vo.setTotalVolume(toDouble(measureInfo.getTotalVolume()));
                }
                vo.setRadarDeviceId(rid);
                vo.setSn(dev.getDeviceNum());
                vo.setType(dev.getDeviceType());
                byWarehouseAndRadar.put(key, vo);
            }
        }
        if (MapUtils.isEmpty(byWarehouseAndRadar)) {
            return List.of();
        }
        if (SetUtils.isEmpty(warehouseIds)) {
            return new ArrayList<>(byWarehouseAndRadar.values());
        }
        var boundRows = radarWarehouseBindingService.list(Wrappers.<RadarWarehouseBinding>lambdaQuery()
                                                                  .select(RadarWarehouseBinding::getWarehouseId)
                                                                  .in(RadarWarehouseBinding::getWarehouseId, warehouseIds));
        Set<Long> boundWarehouseIds = SetUtils.listToSet(boundRows, RadarWarehouseBinding::getWarehouseId);
        for (LiangcangWarehouseListItemVO vo : byWarehouseAndRadar.values()) {
            Long warehouseId = vo.getWarehouseId();
            boolean created = warehouseId != null && boundWarehouseIds.contains(warehouseId);
            vo.setStatus(created ? Business.BINDING_STATUS_CREATED : Business.BINDING_STATUS_NOT_CREATED);
        }
        return new ArrayList<>(byWarehouseAndRadar.values());
    }

    /**
     * deviceType=雷达。
     */
    private static List<WarehouseDeviceInfoVO> listRadarDevices(List<WarehouseDeviceInfoVO> deviceList) {
        if (ListUtils.isEmpty(deviceList)) {
            return List.of();
        }
        List<WarehouseDeviceInfoVO> out = new ArrayList<>();
        for (WarehouseDeviceInfoVO d : deviceList) {
            if (d != null && isRadarDeviceType(d.getDeviceType())) {
                out.add(d);
            }
        }
        return out;
    }

    private static boolean isRadarDeviceType(String deviceType) {
        if (deviceType == null) {
            return false;
        }
        return Business.DEVICE_TYPE_RADAR.equals(deviceType.trim());
    }

    /**
     * 先拉父级仓库，再按父仓 id 拉取全部子级 warehouse 行。
     */
    private List<LiangcangWarehousePageItemVO> fetchAllWarehouseRows(LiangcangWarehousePageQueryParam param) {
        var input = param != null && param.getInput() != null
                    ? param.getInput()
                    : new LiangcangWarehousePageQueryParam.WarehousePageInput();
        int pageSize = input.getPageSize() != null && input.getPageSize() > 0
                       ? input.getPageSize()
                       : WarehousePagePull.DEFAULT_PAGE_SIZE;
        List<LiangcangWarehousePageItemVO> parentRows = fetchPagedWarehouseRows(ROOT_PARENT_ID, null, pageSize);
        if (ListUtils.isEmpty(parentRows)) {
            return List.of();
        }

        var search = input.getSearch();
        var matchedParentRows = StringUtils.isBlank(search)
                                ? parentRows
                                : fetchPagedWarehouseRows(ROOT_PARENT_ID, search, pageSize);
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = new LinkedHashMap<>();
        for (LiangcangWarehousePageItemVO parent : matchedParentRows) {
            if (parent != null && parent.getId() != null) {
                warehouseById.putIfAbsent(parent.getId(), parent);
            }
        }
        for (LiangcangWarehousePageItemVO parent : parentRows) {
            Integer parentId = toParentId(parent);
            if (parentId == null) {
                continue;
            }
            List<LiangcangWarehousePageItemVO> childRows = fetchPagedWarehouseRows(parentId, search, pageSize);
            for (LiangcangWarehousePageItemVO child : childRows) {
                if (child != null && child.getId() != null) {
                    warehouseById.putIfAbsent(child.getId(), child);
                }
            }
        }
        return new ArrayList<>(warehouseById.values());
    }

    /**
     * 按指定 parentId 翻页拉取 warehouse 行。
     */
    private List<LiangcangWarehousePageItemVO> fetchPagedWarehouseRows(Integer parentId, String search, int pageSize) {
        var query = new LiangcangWarehousePageQueryParam();
        var input = new LiangcangWarehousePageQueryParam.WarehousePageInput();
        input.setParentId(parentId);
        input.setSearch(search);
        input.setPageSize(pageSize);
        query.setInput(input);

        List<LiangcangWarehousePageItemVO> rows = new ArrayList<>();
        for (int pageNum = 1; pageNum <= WarehousePagePull.MAX_PAGES; pageNum++) {
            input.setPageNum(pageNum);
            var remote = warehousePageApiClient.fetchWarehousePage(query);
            List<LiangcangWarehousePageItemVO> batch = remote.getRecords();
            if (ListUtils.isEmpty(batch)) {
                break;
            }
            rows.addAll(batch);
            if (batch.size() < pageSize) {
                break;
            }
            long total = remote.getTotal();
            if (total > 0 && rows.size() >= total) {
                break;
            }
        }
        return rows;
    }

    private static Integer toParentId(LiangcangWarehousePageItemVO parent) {
        if (parent == null || parent.getId() == null || parent.getId() > Integer.MAX_VALUE || parent.getId() < Integer.MIN_VALUE) {
            return null;
        }
        return parent.getId().intValue();
    }

    private static Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    @Override
    public WarehouseStatisticsVO warehouseStatistics(WarehouseStatisticsParam param) {
        validateWarehouseStatisticsParam(param);
        LocalDateTime begin = param.getPeriod().getBegin();
        LocalDateTime endTime = param.getPeriod().getEnd();
        LocalDate start = begin.toLocalDate();
        LocalDate end = endTime.toLocalDate();
        List<WarehouseStatWarehouse> warehouses = resolveStatisticsWarehouses(param);
        List<String> timeBuckets = listTimeDimensionLabels(param.getDimension(), start, end);
        List<WarehouseStatisticsRowVO> items =
                buildAggregatedStatRows(param.getDimension(), start, end, begin, endTime, warehouses);
        WarehouseStatisticsVO result = new WarehouseStatisticsVO();
        result.setItems(items);
        if (param.getDimension() == WarehouseStatDimension.DAY) {
            List<WarehouseStatisticsAcquisitionDetailVO> acquisitionDetails =
                    buildDayPerAcquisitionDetailRows(begin, endTime, warehouses);
            result.setAcquisitionItems(acquisitionDetails);
            if (param.getWarehouseId() != null) {
                result.setAcquisitionTurnoverItems(mapAcquisitionTurnoverItems(acquisitionDetails));
            } else {
                result.setAcquisitionTurnoverItems(List.of());
            }
        } else {
            result.setAcquisitionItems(List.of());
            result.setAcquisitionTurnoverItems(List.of());
        }
        BigDecimal currentPeriodAverageTurnover = calcAverageTurnoverRate(items);
        result.setAverageTurnoverRate(currentPeriodAverageTurnover);
        result.setTimeDimensionTurnovers(calcTimeDimensionTurnovers(items));
        if (ListUtils.isEmpty(warehouses)) {
            result.setLastYearSamePeriodAverageTurnoverRate(null);
            result.setTotalYearOnYearChangeRate(null);
            result.setPreviousPeriodAverageTurnoverRate(null);
            result.setTotalChainRatioChangeRate(null);
            result.setTimeDimensionTurnoverYearOnYearTrend(List.of());
            result.setTimeDimensionTurnoverChainRatioTrend(List.of());
        } else {
            LocalDate yoyStart = start.minusYears(1);
            LocalDate yoyEnd = end.minusYears(1);
            LocalDateTime yoyBegin = begin.minusYears(1);
            LocalDateTime yoyEndTime = endTime.minusYears(1);
            List<WarehouseStatisticsRowVO> yoyItems =
                    buildAggregatedStatRows(param.getDimension(), yoyStart, yoyEnd, yoyBegin, yoyEndTime, warehouses);
            BigDecimal lastYearPeriodAvg = calcAverageTurnoverRate(yoyItems);
            result.setLastYearSamePeriodAverageTurnoverRate(lastYearPeriodAvg);
            result.setTotalYearOnYearChangeRate(
                    turnoverChangeRatePercent(currentPeriodAverageTurnover, lastYearPeriodAvg));
            LocalDate[] chainPrev = resolveChainPreviousLocalDateRange(start, end);
            BigDecimal previousPeriodAvg = null;
            if (chainPrev != null) {
                List<WarehouseStatisticsRowVO> chainPeriodItems = buildAggregatedStatRows(
                        param.getDimension(), chainPrev[0], chainPrev[1],
                        chainPrev[0].atStartOfDay(), chainPrev[1].atTime(LocalTime.MAX), warehouses);
                previousPeriodAvg = calcAverageTurnoverRate(chainPeriodItems);
            }
            result.setPreviousPeriodAverageTurnoverRate(previousPeriodAvg);
            result.setTotalChainRatioChangeRate(turnoverChangeRatePercent(currentPeriodAverageTurnover, previousPeriodAvg));
            if (ListUtils.isEmpty(timeBuckets)) {
                result.setTimeDimensionTurnoverYearOnYearTrend(List.of());
                result.setTimeDimensionTurnoverChainRatioTrend(List.of());
            } else {
                Map<String, BigDecimal> currentByBucket = averageTurnoverByTimeBuckets(timeBuckets, items);
                List<String> yoyBuckets = listTimeDimensionLabels(param.getDimension(), yoyStart, yoyEnd);
                Map<String, BigDecimal> yoyByBucket = averageTurnoverByTimeBuckets(yoyBuckets, yoyItems);
                result.setTimeDimensionTurnoverYearOnYearTrend(
                        buildYearOnYearTurnoverTrend(param.getDimension(), timeBuckets, currentByBucket, yoyByBucket));
                result.setTimeDimensionTurnoverChainRatioTrend(
                        buildChainRatioTurnoverTrend(param.getDimension(), timeBuckets, currentByBucket, warehouses));
            }
        }
        return result;
    }

    /**
     * 等长、紧邻上一自然日区间（环比基期）。
     */
    private static LocalDate[] resolveChainPreviousLocalDateRange(LocalDate start, LocalDate end) {
        long inclusiveDays = ChronoUnit.DAYS.between(start, end) + 1;
        if (inclusiveDays <= 0) {
            return null;
        }
        LocalDate prevEnd = start.minusDays(1);
        LocalDate prevStart = prevEnd.minusDays(inclusiveDays - 1);
        return new LocalDate[]{ prevStart, prevEnd };
    }

    private List<WarehouseStatisticsRowVO> buildAggregatedStatRows(WarehouseStatDimension dimension, LocalDate start,
                                                                   LocalDate end, LocalDateTime begin, LocalDateTime endTime,
                                                                   List<WarehouseStatWarehouse> warehouses
                                                                  ) {
        List<String> timeBuckets = listTimeDimensionLabels(dimension, start, end);
        Map<String, Map<Long, VolumeMetrics>> volumeMetricsByBucketWarehouse =
                computeVolumeMetricsByBucket(dimension, begin, endTime, warehouses);
        List<WarehouseStatisticsRowVO> out = new ArrayList<>();
        for (String timeLabel : timeBuckets) {
            Map<Long, VolumeMetrics> byWarehouse = volumeMetricsByBucketWarehouse.getOrDefault(timeLabel, Map.of());
            for (WarehouseStatWarehouse w : warehouses) {
                var row = new WarehouseStatisticsRowVO();
                row.setTimeDimension(timeLabel);
                row.setWarehouseId(w.warehouseId());
                row.setWarehouseName(w.name());
                row.setType(w.type());
                row.setLevel(w.level());
                row.setWarehouseHeight(w.warehouseHeight());
                VolumeMetrics metrics = byWarehouse.get(w.warehouseId());
                if (metrics != null) {
                    row.setAverageInventory(metrics.averageActualVolume());
                    row.setAverageInventoryWeight(metrics.averageWeight());
                    row.setAverageEmptyWarehouseVolume(metrics.averageEmptyVolume());
                    if (metrics.averageEmptyVolume() != null
                        && metrics.averageActualVolume() != null
                        && BigDecimal.ZERO.compareTo(metrics.averageActualVolume()) != 0) {
                        row.setTurnoverRate(metrics.averageEmptyVolume()
                                                    .divide(metrics.averageActualVolume(), 4, RoundingMode.HALF_UP));
                    }
                }
                out.add(row);
            }
        }
        return out;
    }

    private static Map<String, BigDecimal> averageTurnoverByTimeBuckets(
            List<String> orderedBuckets, List<WarehouseStatisticsRowVO> items) {
        Map<String, BigDecimal> byBucket = new LinkedHashMap<>();
        for (String b : orderedBuckets) {
            BigDecimal sum = BigDecimal.ZERO;
            long count = 0;
            for (WarehouseStatisticsRowVO r : items) {
                if (r == null || r.getTimeDimension() == null || r.getTurnoverRate() == null) {
                    continue;
                }
                if (!b.equals(r.getTimeDimension())) {
                    continue;
                }
                sum = sum.add(r.getTurnoverRate());
                count++;
            }
            byBucket.put(b, count == 0 ? null : sum.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP));
        }
        return byBucket;
    }

    private static BigDecimal turnoverChangeRatePercent(BigDecimal current, BigDecimal base) {
        if (current == null || base == null || BigDecimal.ZERO.compareTo(base) == 0) {
            return null;
        }
        return current.subtract(base).divide(base, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private static List<WarehouseStatisticsVO.TurnoverTrendItemVO> buildYearOnYearTurnoverTrend(
            WarehouseStatDimension dimension, List<String> timeBuckets, Map<String, BigDecimal> currentByBucket,
            Map<String, BigDecimal> yoyByBucket
                                                                                               ) {
        List<WarehouseStatisticsVO.TurnoverTrendItemVO> out = new ArrayList<>();
        for (String td : timeBuckets) {
            String yoyL = toYearOnYearLabel(td, dimension);
            var vo = new WarehouseStatisticsVO.TurnoverTrendItemVO();
            vo.setTimeDimension(td);
            vo.setChangeRate(turnoverChangeRatePercent(
                    currentByBucket.get(td), yoyByBucket.get(yoyL)));
            out.add(vo);
        }
        return out;
    }

    private List<WarehouseStatisticsVO.TurnoverTrendItemVO> buildChainRatioTurnoverTrend(
            WarehouseStatDimension dimension, List<String> timeBuckets, Map<String, BigDecimal> currentByBucket,
            List<WarehouseStatWarehouse> warehouses
                                                                                        ) {
        if (ListUtils.isEmpty(warehouses)) {
            return List.of();
        }
        List<WarehouseStatisticsVO.TurnoverTrendItemVO> out = new ArrayList<>();
        for (int i = 0; i < timeBuckets.size(); i++) {
            String td = timeBuckets.get(i);
            BigDecimal cur = currentByBucket.get(td);
            BigDecimal base;
            if (i > 0) {
                String prevT = timeBuckets.get(i - 1);
                base = currentByBucket.get(prevT);
            } else {
                String prevL = toChainPreviousLabel(td, dimension);
                base = fetchAverageTurnoverForSingleBucket(dimension, prevL, warehouses);
            }
            var vo = new WarehouseStatisticsVO.TurnoverTrendItemVO();
            vo.setTimeDimension(td);
            vo.setChangeRate(turnoverChangeRatePercent(cur, base));
            out.add(vo);
        }
        return out;
    }

    private BigDecimal fetchAverageTurnoverForSingleBucket(WarehouseStatDimension dimension, String bucketLabel,
                                                           List<WarehouseStatWarehouse> warehouses
                                                          ) {
        if (StringUtils.isBlank(bucketLabel) || ListUtils.isEmpty(warehouses)) {
            return null;
        }
        LocalDate[] se;
        try {
            se = toBucketLocalDateRange(dimension, bucketLabel);
        } catch (RuntimeException e) {
            return null;
        }
        if (se == null) {
            return null;
        }
        LocalDateTime b = se[0].atStartOfDay();
        LocalDateTime t = se[1].atTime(LocalTime.MAX);
        List<WarehouseStatisticsRowVO> rows = buildAggregatedStatRows(dimension, se[0], se[1], b, t, warehouses);
        List<String> one = listTimeDimensionLabels(dimension, se[0], se[1]);
        if (ListUtils.isEmpty(one)) {
            return null;
        }
        return averageTurnoverByTimeBuckets(one, rows).get(one.get(0));
    }

    private static String toYearOnYearLabel(String label, WarehouseStatDimension d) {
        Assert.isParameterError(!(isSupportedStatDimension(d)), MsgBinding.UNSUPPORTED_STAT_DIMENSION);
        switch (d) {
            case DAY: {
                return LocalDate.parse(label).minusYears(1).toString();
            }
            case MONTH: {
                return YearMonth.parse(label).minusYears(1).toString();
            }
            case YEAR: {
                return String.valueOf(Integer.parseInt(label) - 1);
            }
            case QUARTER: {
                int p = label.indexOf(Business.TIME_QUARTER_LABEL_PREFIX);
                int y = Integer.parseInt(label.substring(0, p));
                int q = Integer.parseInt(label.substring(p + Business.TIME_QUARTER_LABEL_PREFIX.length()));
                return (y - 1) + Business.TIME_QUARTER_LABEL_PREFIX + q;
            }
            default:
                return null;
        }
    }

    private static String toChainPreviousLabel(String label, WarehouseStatDimension d) {
        Assert.isParameterError(!(isSupportedStatDimension(d)), MsgBinding.UNSUPPORTED_STAT_DIMENSION);
        switch (d) {
            case DAY: {
                return LocalDate.parse(label).minusDays(1).toString();
            }
            case MONTH: {
                return YearMonth.parse(label).minusMonths(1).toString();
            }
            case YEAR: {
                return String.valueOf(Integer.parseInt(label) - 1);
            }
            case QUARTER: {
                int p = label.indexOf(Business.TIME_QUARTER_LABEL_PREFIX);
                int y = Integer.parseInt(label.substring(0, p));
                int q = Integer.parseInt(label.substring(p + Business.TIME_QUARTER_LABEL_PREFIX.length()));
                if (q == 1) {
                    return (y - 1) + Business.TIME_QUARTER_LABEL_PREFIX + 4;
                }
                return y + Business.TIME_QUARTER_LABEL_PREFIX + (q - 1);
            }
            default:
                return null;
        }
    }

    private static LocalDate[] toBucketLocalDateRange(WarehouseStatDimension d, String label) {
        Assert.isParameterError(!(isSupportedStatDimension(d)), MsgBinding.UNSUPPORTED_STAT_DIMENSION);
        switch (d) {
            case DAY: {
                LocalDate date = LocalDate.parse(label);
                return new LocalDate[]{ date, date };
            }
            case MONTH: {
                YearMonth ym = YearMonth.parse(label);
                return new LocalDate[]{ ym.atDay(1), ym.atEndOfMonth() };
            }
            case YEAR: {
                int y = Integer.parseInt(label);
                return new LocalDate[]{ LocalDate.of(y, 1, 1), LocalDate.of(y, 12, 31) };
            }
            case QUARTER: {
                int p = label.indexOf(Business.TIME_QUARTER_LABEL_PREFIX);
                int y = Integer.parseInt(label.substring(0, p));
                int q = Integer.parseInt(label.substring(p + Business.TIME_QUARTER_LABEL_PREFIX.length()));
                LocalDate s = LocalDate.of(y, (q - 1) * 3 + 1, 1);
                LocalDate e = s.plusMonths(3).minusDays(1);
                return new LocalDate[]{ s, e };
            }
            default:
                return null;
        }
    }

    /**
     * 日维度：每次采集一行明细。
     */
    private List<WarehouseStatisticsAcquisitionDetailVO> buildDayPerAcquisitionDetailRows(
            LocalDateTime begin, LocalDateTime end, List<WarehouseStatWarehouse> warehouses) {
        if (ListUtils.isEmpty(warehouses)) {
            return List.of();
        }
        Map<Long, WarehouseStatWarehouse> warehouseById =
                MapUtils.listToMap(warehouses, WarehouseStatWarehouse::warehouseId, w -> w);
        Set<Long> warehouseIds = warehouseById.keySet();
        List<WarehouseGoodsHistory> histories = warehouseGoodsHistoryService.lambdaQuery()
                .in(WarehouseGoodsHistory::getWarehouseId, warehouseIds)
                .between(WarehouseGoodsHistory::getAcquisitionDate, begin, end)
                .orderByAsc(WarehouseGoodsHistory::getAcquisitionDate)
                .orderByAsc(WarehouseGoodsHistory::getWarehouseId)
                .orderByAsc(WarehouseGoodsHistory::getId)
                .list();
        if (ListUtils.isEmpty(histories)) {
            return List.of();
        }
        List<WarehouseStatisticsAcquisitionDetailVO> out = new ArrayList<>();
        for (WarehouseGoodsHistory h : histories) {
            if (h == null || h.getVolumeValue() == null || h.getWarehouseId() == null || h.getAcquisitionDate() == null) {
                continue;
            }
            WarehouseStatWarehouse w = warehouseById.get(h.getWarehouseId());
            if (w == null) {
                continue;
            }
            var row = new WarehouseStatisticsAcquisitionDetailVO();
            row.setAcquisitionTime(h.getAcquisitionDate().format(ACQUISITION_TIME_LABEL));
            row.setWarehouseId(w.warehouseId());
            row.setWarehouseName(w.name());
            row.setType(w.type());
            row.setLevel(w.level());
            row.setWarehouseHeight(w.warehouseHeight());
            row.setGoodsVolume(h.getVolumeValue());
            row.setWeight(h.getWeightValue());
            if (w.totalVolume() != null) {
                BigDecimal emptyVolume = w.totalVolume().subtract(h.getVolumeValue());
                row.setEmptyWarehouseVolume(emptyVolume);
                if (BigDecimal.ZERO.compareTo(h.getVolumeValue()) != 0) {
                    row.setTurnoverRate(
                            emptyVolume.divide(h.getVolumeValue(), 4, java.math.RoundingMode.HALF_UP));
                }
            }
            out.add(row);
        }
        return out;
    }

    /**
     * 由采集明细拆当日周转项。
     */
    private static List<WarehouseStatisticsVO.AcquisitionTurnoverItemVO> mapAcquisitionTurnoverItems(
            List<WarehouseStatisticsAcquisitionDetailVO> details
                                                                                                    ) {
        if (ListUtils.isEmpty(details)) {
            return List.of();
        }
        List<WarehouseStatisticsVO.AcquisitionTurnoverItemVO> out = new ArrayList<>(details.size());
        for (WarehouseStatisticsAcquisitionDetailVO d : details) {
            if (d == null) {
                continue;
            }
            var vo = new WarehouseStatisticsVO.AcquisitionTurnoverItemVO();
            String acquisitionTime = d.getAcquisitionTime();
            if (acquisitionTime != null && acquisitionTime.length() >= 10) {
                vo.setTimeDimension(acquisitionTime.substring(0, 10));
            }
            vo.setTurnoverRate(d.getTurnoverRate());
            vo.setAcquisitionTime(acquisitionTime);
            out.add(vo);
        }
        return out;
    }

    @Override
    public Pagination<ApplicationRelatedDeviceVO> applicationRelatedDevicePage(ApplicationRelatedDevicePageParam param) {
        DeviceTagRuleVO tagRule = deviceCenterStdClient.getTagRule(DeviceCenter.TAG_CODE_APP_RESERVE_MONITORING);
        if (tagRule == null || StringUtils.isBlank(tagRule.getRule())) {
            return Pagination.empty();
        }
        Set<Long> excludedDeviceIds = collectExcludedDeviceIdsFromBindings(null);
        Set<Long> productIds = filterNullIds(param == null ? null : param.getProductIds());
        Set<Long> areaIds = filterNullIds(param == null ? null : param.getAreaIds());
        String deviceName = param == null ? null : param.getDeviceName();
        Object ruleGroup = buildApplicationDeviceRuleGroup(tagRule.getRule(), excludedDeviceIds, productIds, areaIds, deviceName);
        if (ruleGroup == null) {
            return Pagination.empty();
        }
        var pageReq = new DeviceRuleConditionPageParam();
        pageReq.setPageNumber(resolvePageNumber(param));
        pageReq.setPageSize(resolvePageSize(param));
        pageReq.setRuleGroup(ruleGroup);
        Pagination<DevicePageInfoVO> page = deviceCenterStdClient.deviceRuleConditionInfoPage(pageReq);
        fillAreaFields(page.getRecords());
        return convertApplicationRelatedDevicePage(page);
    }

    private Pagination<ApplicationRelatedDeviceVO> convertApplicationRelatedDevicePage(Pagination<DevicePageInfoVO> page) {
        if (page == null || ListUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<ApplicationRelatedDeviceVO> records = ListUtils.convertItem(page.getRecords(), item -> {
            var vo = new ApplicationRelatedDeviceVO();
            vo.setDeviceId(item.getId());
            vo.setDeviceName(item.getName());
            vo.setDeviceType(resolveProductCategoryName(item));
            vo.setProductName(resolveProductName(item));
            vo.setAreaId(item.getAreaId());
            vo.setAreaName(item.getAreaName());
            return vo;
        });
        return new Pagination<>(page.getTotal(), page.getPages(), records);
    }

    private static String resolveProductCategoryName(DevicePageInfoVO item) {
        if (item == null || item.getProductInfo() == null || item.getProductInfo().getProductCategory() == null) {
            return null;
        }
        return item.getProductInfo().getProductCategory().getName();
    }

    private static String resolveProductName(DevicePageInfoVO item) {
        return item == null || item.getProductInfo() == null ? null : item.getProductInfo().getName();
    }

    private Object buildApplicationDeviceRuleGroup(String ruleJson, Set<Long> excludedDeviceIds,
                                                   Set<Long> productIds, Set<Long> areaIds, String deviceName
                                                  ) {
        try {
            return mergeApplicationDeviceFiltersIntoRuleJson(ruleJson, excludedDeviceIds, productIds, areaIds, deviceName);
        } catch (ServiceException e) {
            throw e;
        } catch (RuntimeException e) {
            ServiceException tagRuleEx = new ServiceException(MsgBinding.TAG_RULE_JSON_PARSE_FAILED);
            tagRuleEx.initCause(e);
            throw tagRuleEx;
        }
    }

    private static Integer resolvePageNumber(ApplicationRelatedDevicePageParam param) {
        return param == null || param.getPageNumber() == null || param.getPageNumber() < 1
               ? DeviceCenter.APPLICATION_DEVICE_PAGE_NUMBER
               : param.getPageNumber();
    }

    private static Integer resolvePageSize(ApplicationRelatedDevicePageParam param) {
        return param == null || param.getPageSize() == null || param.getPageSize() < 1
               ? DeviceCenter.APPLICATION_DEVICE_PAGE_SIZE
               : param.getPageSize();
    }

    /**
     * 已绑业务设备 + 雷达 id，供规则 NOT IN。
     */
    private Set<Long> collectExcludedDeviceIdsFromBindings(Long excludeWarehouseId) {
        var warehouseDeviceQuery = Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                .select(WarehouseDeviceBinding::getDeviceId);
        if (excludeWarehouseId != null) {
            warehouseDeviceQuery.ne(WarehouseDeviceBinding::getWarehouseId, excludeWarehouseId);
        }
        Set<Long> out = new HashSet<>(SetUtils.listToSet(
                this.list(warehouseDeviceQuery),
                WarehouseDeviceBinding::getDeviceId));
        out.addAll(SetUtils.listToSet(
                radarWarehouseBindingService.list(Wrappers.<RadarWarehouseBinding>lambdaQuery()
                                                          .select(RadarWarehouseBinding::getRadarDeviceId)),
                RadarWarehouseBinding::getRadarDeviceId));
        return out;
    }

    /**
     * 规则 JSON 追加已绑设备排除、产品和区域筛选条件。
     */
    private Object mergeApplicationDeviceFiltersIntoRuleJson(String ruleJson, Set<Long> excludedDeviceIds,
                                                             Set<Long> productIds, Set<Long> areaIds, String deviceName
                                                            ) {
        if (SetUtils.isEmpty(excludedDeviceIds) && SetUtils.isEmpty(productIds) && SetUtils.isEmpty(areaIds)
            && StringUtils.isBlank(deviceName)) {
            return JsonUtils.deserialize(ruleJson, Object.class);
        }
        try {
            ObjectMapper om = JsonUtils.getObjectMapper();
            JsonNode rootNode = om.readTree(ruleJson);
            Assert.isParameterError(!(rootNode.isObject()), MsgBinding.RULE_JSON_MUST_BE_OBJECT);
            ObjectNode root = (ObjectNode) rootNode;
            ArrayNode rules;
            if (root.has(DeviceCenterRuleMerge.JSON_KEY_RULES) && root.get(DeviceCenterRuleMerge.JSON_KEY_RULES).isArray()) {
                rules = (ArrayNode) root.get(DeviceCenterRuleMerge.JSON_KEY_RULES);
            } else {
                rules = om.createArrayNode();
                root.set(DeviceCenterRuleMerge.JSON_KEY_RULES, rules);
            }
            if (rules.size() == 0) {
                ObjectNode rule = om.createObjectNode();
                ArrayNode fields = om.createArrayNode();
                appendApplicationDeviceFilterFields(om, fields, excludedDeviceIds, productIds, areaIds, deviceName);
                rule.set(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS, fields);
                rules.add(rule);
            } else {
                for (JsonNode r : rules) {
                    if (!r.isObject()) {
                        continue;
                    }
                    ObjectNode ruleObj = (ObjectNode) r;
                    ArrayNode fields = ruleObj.has(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS)
                                       && ruleObj.get(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS).isArray()
                                       ? (ArrayNode) ruleObj.get(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS)
                                       : om.createArrayNode();
                    if (!ruleObj.has(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS)) {
                        ruleObj.set(DeviceCenterRuleMerge.JSON_KEY_RULE_FIELDS, fields);
                    }
                    appendApplicationDeviceFilterFields(om, fields, excludedDeviceIds, productIds, areaIds, deviceName);
                }
            }
            return om.treeToValue(root, Object.class);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            ServiceException _mergeEx = new ServiceException(MsgBinding.MERGE_EXCLUDE_RULE_FAILED);
            _mergeEx.initCause(e);
            throw _mergeEx;
        }
    }

    private static void appendApplicationDeviceFilterFields(ObjectMapper om, ArrayNode fields, Set<Long> excludedDeviceIds,
                                                            Set<Long> productIds, Set<Long> areaIds, String deviceName
                                                           ) {
        if (SetUtils.isNotEmpty(excludedDeviceIds)) {
            fields.add(buildRuleField(om, DeviceCenterRuleMerge.FIELD_NAME_DEVICE,
                                      DeviceCenterRuleMerge.OPERATOR_NOT_IN, excludedDeviceIds));
        }
        if (SetUtils.isNotEmpty(productIds)) {
            fields.add(buildRuleField(om, DeviceCenterRuleMerge.FIELD_NAME_PRODUCT,
                                      DeviceCenterRuleMerge.OPERATOR_IN, productIds));
        }
        if (SetUtils.isNotEmpty(areaIds)) {
            fields.add(buildRuleField(om, DeviceCenterRuleMerge.FIELD_NAME_AREA,
                                      DeviceCenterRuleMerge.OPERATOR_IN, areaIds));
        }
        if (StringUtils.isNotBlank(deviceName)) {
            fields.add(buildRuleField(om, DeviceCenterRuleMerge.FIELD_NAME_DEVICE_NAME,
                                      DeviceCenterRuleMerge.OPERATOR_LIKE, deviceName.trim()));
        }
    }

    private static ObjectNode buildNotInDeviceIdRuleField(ObjectMapper om, Set<Long> excludedDeviceIds) {
        return buildRuleField(om, DeviceCenterRuleMerge.FIELD_NAME_DEVICE,
                              DeviceCenterRuleMerge.OPERATOR_NOT_IN, excludedDeviceIds);
    }

    private static ObjectNode buildRuleField(ObjectMapper om, String fieldName, String operator, Set<Long> values) {
        ObjectNode field = om.createObjectNode();
        field.put(DeviceCenterRuleMerge.JSON_KEY_FIELD, fieldName);
        field.put(DeviceCenterRuleMerge.JSON_KEY_OPERATOR, operator);
        ArrayNode arr = om.createArrayNode();
        for (Long value : values) {
            if (value != null) {
                arr.add(value);
            }
        }
        field.set(DeviceCenterRuleMerge.JSON_KEY_VALUE, arr);
        return field;
    }

    private static ObjectNode buildRuleField(ObjectMapper om, String fieldName, String operator, String value) {
        ObjectNode field = om.createObjectNode();
        field.put(DeviceCenterRuleMerge.JSON_KEY_FIELD, fieldName);
        field.put(DeviceCenterRuleMerge.JSON_KEY_OPERATOR, operator);
        field.put(DeviceCenterRuleMerge.JSON_KEY_VALUE, value);
        return field;
    }

    private static Set<Long> filterNullIds(Set<Long> ids) {
        Set<Long> out = new HashSet<>();
        if (SetUtils.isNotEmpty(ids)) {
            for (Long item : ids) {
                if (item != null) {
                    out.add(item);
                }
            }
        }
        return out;
    }

    private static void fillAreaFields(List<DevicePageInfoVO> records) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        for (DevicePageInfoVO record : records) {
            if (record == null || record.getArea() == null) {
                continue;
            }
            record.setAreaId(record.getArea().getPathId());
            record.setAreaName(StringUtils.isNotBlank(record.getArea().getPathName())
                               ? record.getArea().getPathName()
                               : record.getArea().getName());
        }
    }

    private static BigDecimal calcAverageTurnoverRate(List<WarehouseStatisticsRowVO> rows) {
        BigDecimal sum = BigDecimal.ZERO;
        long count = 0;
        for (WarehouseStatisticsRowVO row : rows) {
            if (row != null && row.getTurnoverRate() != null) {
                sum = sum.add(row.getTurnoverRate());
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum.divide(BigDecimal.valueOf(count), 4, java.math.RoundingMode.HALF_UP);
    }

    private static List<WarehouseStatisticsVO.TimeDimensionTurnoverVO> calcTimeDimensionTurnovers(List<WarehouseStatisticsRowVO> rows) {
        Map<String, BucketAggregate> bucketAgg = new LinkedHashMap<>();
        for (WarehouseStatisticsRowVO row : rows) {
            if (row == null || row.getTimeDimension() == null || row.getTurnoverRate() == null) {
                continue;
            }
            BucketAggregate agg = bucketAgg.computeIfAbsent(row.getTimeDimension(), k -> new BucketAggregate());
            agg.turnoverRateSum = agg.turnoverRateSum.add(row.getTurnoverRate());
            agg.turnoverRateCount++;
        }
        List<WarehouseStatisticsVO.TimeDimensionTurnoverVO> out = new ArrayList<>();
        for (Map.Entry<String, BucketAggregate> e : bucketAgg.entrySet()) {
            BucketAggregate agg = e.getValue();
            if (agg.turnoverRateCount == 0) {
                continue;
            }
            WarehouseStatisticsVO.TimeDimensionTurnoverVO vo = new WarehouseStatisticsVO.TimeDimensionTurnoverVO();
            vo.setTimeDimension(e.getKey());
            vo.setAverageTurnoverRate(
                    agg.turnoverRateSum.divide(BigDecimal.valueOf(agg.turnoverRateCount), 4, java.math.RoundingMode.HALF_UP));
            out.add(vo);
        }
        return out;
    }

    private static void validateWarehouseStatisticsParam(WarehouseStatisticsParam param) {
        Assert.isParameterError(param == null, MsgBinding.STAT_DIMENSION_REQUIRED);
        Assert.isParameterError(param.getDimension() == null, MsgBinding.STAT_DIMENSION_REQUIRED);
        Period period = param.getPeriod();
        Assert.isParameterError(period == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getBegin() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getEnd() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        LocalDate start = period.getBegin().toLocalDate();
        LocalDate end = period.getEnd().toLocalDate();
        Assert.isParameterError(start.isAfter(end), MsgBinding.PERIOD_BEGIN_AFTER_END);
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        Assert.isParameterError(param.getDimension() == WarehouseStatDimension.DAY && days > 800, MsgBinding.DAILY_RANGE_TOO_LONG);
        Assert.isParameterError(param.getDimension() == WarehouseStatDimension.MONTH
                                && YearMonth.from(start).until(YearMonth.from(end), ChronoUnit.MONTHS) + 1 > 120,
                                MsgBinding.MONTHLY_RANGE_TOO_LONG);
    }

    /**
     * 统计用仓列表：已绑仓 + 可选 id/种类过滤。
     */
    private List<WarehouseStatWarehouse> resolveStatisticsWarehouses(WarehouseStatisticsParam param) {
        String categoryFilter = param.getWarehouseCategory() == null ? null : param.getWarehouseCategory().trim();
        boolean filterCategory = categoryFilter != null && !categoryFilter.isEmpty();
        List<RadarWarehouseBinding> rows = radarWarehouseBindingService.lambdaQuery()
                .orderByDesc(RadarWarehouseBinding::getWarehouseId)
                .list();
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        Long targetWarehouseId = param.getWarehouseId();
        Set<Long> orderedIds = new java.util.LinkedHashSet<>();
        for (RadarWarehouseBinding row : rows) {
            if (row == null || row.getWarehouseId() == null) {
                continue;
            }
            if (targetWarehouseId != null && !Objects.equals(row.getWarehouseId(), targetWarehouseId)) {
                continue;
            }
            orderedIds.add(row.getWarehouseId());
        }
        if (SetUtils.isEmpty(orderedIds)) {
            return List.of();
        }
        Map<Long, LiangcangWarehousePageItemVO> byId = warehousePageApiClient.fetchPageItemsByIds(orderedIds);
        Map<Long, WarehouseMeasureHistory> measureInfoByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(orderedIds);
        List<WarehouseStatWarehouse> out = new ArrayList<>();
        for (Long warehouseId : orderedIds) {
            LiangcangWarehousePageItemVO item = byId.get(warehouseId);
            String type = item == null ? null : item.getCategoryName();
            if (filterCategory && (type == null || !categoryFilter.equals(type))) {
                continue;
            }
            WarehouseMeasureHistory measureInfo = measureInfoByWarehouseId.get(warehouseId);
            BigDecimal totalVolume = measureInfo == null ? null : measureInfo.getTotalVolume();
            String name = item == null ? null : item.getName();
            String level = item == null ? null : item.getQualityGrade();
            Double warehouseHeight = measureInfo == null ? null : toDouble(measureInfo.getTotalHeight());
            out.add(new WarehouseStatWarehouse(warehouseId, name, type, level, warehouseHeight, totalVolume));
        }
        return out;
    }

    private Map<String, Map<Long, VolumeMetrics>> computeVolumeMetricsByBucket(WarehouseStatDimension dimension,
                                                                               LocalDateTime begin,
                                                                               LocalDateTime end,
                                                                               List<WarehouseStatWarehouse> warehouses
                                                                              ) {
        if (ListUtils.isEmpty(warehouses)) {
            return Map.of();
        }
        Map<Long, WarehouseStatWarehouse> warehouseById = MapUtils.listToMap(warehouses, WarehouseStatWarehouse::warehouseId, item -> item);
        Set<Long> warehouseIds = SetUtils.listToSet(warehouses, WarehouseStatWarehouse::warehouseId);
        List<WarehouseGoodsHistory> histories = warehouseGoodsHistoryService.lambdaQuery()
                .select(WarehouseGoodsHistory::getWarehouseId, WarehouseGoodsHistory::getAcquisitionDate,
                        WarehouseGoodsHistory::getVolumeValue, WarehouseGoodsHistory::getWeightValue)
                .in(WarehouseGoodsHistory::getWarehouseId, warehouseIds)
                .between(WarehouseGoodsHistory::getAcquisitionDate, begin, end)
                .list();
        if (ListUtils.isEmpty(histories)) {
            return Map.of();
        }
        Map<String, Map<Long, BucketAggregate>> agg = new HashMap<>();
        for (WarehouseGoodsHistory h : histories) {
            if (h == null || h.getWarehouseId() == null || h.getAcquisitionDate() == null) {
                continue;
            }
            String bucket = toTimeBucketLabel(dimension, h.getAcquisitionDate().toLocalDate());
            Map<Long, BucketAggregate> bucketMap = agg.computeIfAbsent(bucket, k -> new HashMap<>());
            BucketAggregate bucketAggregate = bucketMap.computeIfAbsent(h.getWarehouseId(), k -> new BucketAggregate());
            if (h.getVolumeValue() != null) {
                bucketAggregate.actualVolumeSum = bucketAggregate.actualVolumeSum.add(h.getVolumeValue());
                bucketAggregate.actualCount++;
            }
            if (h.getWeightValue() != null) {
                bucketAggregate.weightSum = bucketAggregate.weightSum.add(h.getWeightValue());
                bucketAggregate.weightCount++;
            }
            WarehouseStatWarehouse warehouse = warehouseById.get(h.getWarehouseId());
            if (warehouse != null && warehouse.totalVolume() != null && h.getVolumeValue() != null) {
                BigDecimal emptyVolume = warehouse.totalVolume().subtract(h.getVolumeValue());
                bucketAggregate.emptyVolumeSum = bucketAggregate.emptyVolumeSum.add(emptyVolume);
                bucketAggregate.emptyCount++;
            }
        }
        Map<String, Map<Long, VolumeMetrics>> out = new HashMap<>();
        for (Map.Entry<String, Map<Long, BucketAggregate>> entry : agg.entrySet()) {
            Map<Long, VolumeMetrics> warehouseAvg = new HashMap<>();
            for (Map.Entry<Long, BucketAggregate> warehouseEntry : entry.getValue().entrySet()) {
                BucketAggregate a = warehouseEntry.getValue();
                if (a.actualCount > 0) {
                    BigDecimal averageActualVolume = a.actualVolumeSum.divide(BigDecimal.valueOf(a.actualCount), 4, java.math.RoundingMode.HALF_UP);
                    BigDecimal averageWeight = a.weightCount > 0
                                               ? a.weightSum.divide(BigDecimal.valueOf(a.weightCount), 4, java.math.RoundingMode.HALF_UP)
                                               : null;
                    BigDecimal averageEmptyVolume = a.emptyCount > 0
                                                    ? a.emptyVolumeSum.divide(BigDecimal.valueOf(a.emptyCount), 4, java.math.RoundingMode.HALF_UP)
                                                    : null;
                    warehouseAvg.put(warehouseEntry.getKey(), new VolumeMetrics(averageActualVolume, averageWeight, averageEmptyVolume));
                }
            }
            out.put(entry.getKey(), warehouseAvg);
        }
        return out;
    }

    private static String toTimeBucketLabel(WarehouseStatDimension dimension, LocalDate date) {
        Assert.isParameterError(!(isSupportedStatDimension(dimension)), MsgBinding.UNSUPPORTED_STAT_DIMENSION);
        switch (dimension) {
            case DAY:
                return date.toString();
            case MONTH:
                return String.format(Business.TIME_FORMAT_YEAR_MONTH, date.getYear(), date.getMonthValue());
            case YEAR:
                return String.valueOf(date.getYear());
            case QUARTER:
                int quarter = (date.getMonthValue() - 1) / 3 + 1;
                return date.getYear() + Business.TIME_QUARTER_LABEL_PREFIX + quarter;
            default:
                return null;
        }
    }

    private static class BucketAggregate {
        private BigDecimal actualVolumeSum = BigDecimal.ZERO;
        private long actualCount = 0;
        private BigDecimal weightSum = BigDecimal.ZERO;
        private long weightCount = 0;
        private BigDecimal emptyVolumeSum = BigDecimal.ZERO;
        private long emptyCount = 0;
        private BigDecimal turnoverRateSum = BigDecimal.ZERO;
        private long turnoverRateCount = 0;
    }

    private record VolumeMetrics(BigDecimal averageActualVolume, BigDecimal averageWeight, BigDecimal averageEmptyVolume) {
    }

    private record WarehouseStatWarehouse(Long warehouseId, String name, String type, String level,
                                          Double warehouseHeight, BigDecimal totalVolume) {
    }

    private static List<String> listTimeDimensionLabels(WarehouseStatDimension dimension, LocalDate start, LocalDate end) {
        Assert.isParameterError(!(isSupportedStatDimension(dimension)), MsgBinding.UNSUPPORTED_STAT_DIMENSION);
        switch (dimension) {
            case DAY:
                return listDayLabels(start, end);
            case MONTH:
                return listMonthLabels(start, end);
            case YEAR:
                return listYearLabels(start, end);
            case QUARTER:
                return listQuarterLabels(start, end);
            default:
                return List.of();
        }
    }

    private static boolean isSupportedStatDimension(WarehouseStatDimension dimension) {
        return dimension == WarehouseStatDimension.DAY
               || dimension == WarehouseStatDimension.MONTH
               || dimension == WarehouseStatDimension.YEAR
               || dimension == WarehouseStatDimension.QUARTER;
    }

    private static List<String> listDayLabels(LocalDate start, LocalDate end) {
        List<String> out = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            out.add(d.toString());
        }
        return out;
    }

    private static List<String> listMonthLabels(LocalDate start, LocalDate end) {
        List<String> out = new ArrayList<>();
        YearMonth ymStart = YearMonth.from(start);
        YearMonth ymEnd = YearMonth.from(end);
        for (YearMonth ym = ymStart; !ym.isAfter(ymEnd); ym = ym.plusMonths(1)) {
            out.add(String.format(Business.TIME_FORMAT_YEAR_MONTH, ym.getYear(), ym.getMonthValue()));
        }
        return out;
    }

    private static List<String> listYearLabels(LocalDate start, LocalDate end) {
        List<String> out = new ArrayList<>();
        for (int y = start.getYear(); y <= end.getYear(); y++) {
            out.add(String.valueOf(y));
        }
        return out;
    }

    private static List<String> listQuarterLabels(LocalDate start, LocalDate end) {
        int sy = start.getYear();
        int sq = (start.getMonthValue() - 1) / 3 + 1;
        int ey = end.getYear();
        int eq = (end.getMonthValue() - 1) / 3 + 1;
        List<String> out = new ArrayList<>();
        int y = sy;
        int q = sq;
        while (y < ey || (y == ey && q <= eq)) {
            out.add(y + Business.TIME_QUARTER_LABEL_PREFIX + q);
            q++;
            if (q > 4) {
                q = 1;
                y++;
            }
        }
        return out;
    }
}
