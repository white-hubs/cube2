package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDeviceBindingLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.BoundDevicePageQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.CountsTrendParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionBimProductsService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceBindingLogService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberLogService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimBoundDevicePageResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimPointVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimStatisticsOverviewVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.DistrictDeviceCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualCommandUsageCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductTypeCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductUserCountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisinfectionBimProductsServiceImpl implements DisinfectionBimProductsService {
    private static final String UNKNOWN_DISTRICT = "未知区县";
    private static final String COMMAND_KEYWORD_DRIVE_MOSQUITO = "驱蚊";
    private static final String COMMAND_KEYWORD_MIST_SPRAY = "雾灌";
    private static final String MOSQUITO_DOSAGE_PROP_CODE = "mosquito_dosage_ml";
    private final DisinfectionMemberGroupDeviceBindingLogService memberGroupDeviceBindingLogService;
    private final DisinfectionMemberGroupDeviceService memberGroupDeviceService;
    private final DisinfectionMemberGroupUserService memberGroupUserService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final IotProductClient iotProductClient;
    private final DisinfectionMemberGroupService disinfectionMemberGroupService;
    private final DisinfectionMemberLogService disinfectionMemberLogService;

    @Override
    public Long productsCounts() {
        //查询绑定的设备id
        Set<Long> deviceIds = memberGroupDeviceService.getAllBindDeviceIdList();
        //查询绑定的设备所属的产品
        List<DeviceProductInfoWithDeviceDTO> productInfos = getProductInfoWithDeviceDTOS(deviceIds);
        //统计不同产品的数量
        return productInfos.stream().map(DeviceProductInfoWithDeviceDTO::getProductInfo).map(DeviceProductInfoDTO::getId).distinct()
                .count();

    }

    @Override
    public List<ProductTypeCountVO> productsTypeCounts() {
        //查询绑定的设备id
        Set<Long> deviceIds = memberGroupDeviceService.getAllBindDeviceIdList();
        List<DeviceProductInfoWithDeviceDTO> productInfos = getProductInfoWithDeviceDTOS(deviceIds);
        //统计产品分类下不同产品的数量
        return statisticCategoryProductsCount(productInfos);
    }

    @Override
    public List<ProductUserCountVO> productsUsersCounts() {
        //查询绑定的设备id
        Set<Long> deviceIds = memberGroupDeviceService.getAllBindDeviceIdList();
        //查询绑定的设备所属的产品
        List<DeviceProductInfoWithDeviceDTO> productInfos = getProductInfoWithDeviceDTOS(deviceIds);
        Map<Long, DeviceProductInfoDTO> deviceToProductsMap = productInfos.stream()
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, DeviceProductInfoWithDeviceDTO::getProductInfo));
        //查询所有组与设备的绑定关系
        List<MemberGroupDevice> memberGroupDevices = memberGroupDeviceService.list();
        //根据组id进行分组
        Map<Long, List<MemberGroupDevice>> groupsMap = MapUtils.listGroupToMap(memberGroupDevices, MemberGroupDevice::getGroupId);
        //统计不同产品类型绑定的用户数量
        Map<String, Long> productUsersCountMap = statisticProductCategoryUsersMap(groupsMap, deviceToProductsMap);
        return productUsersCountMap
                .entrySet()
                .stream()
                .map(entry -> new ProductUserCountVO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BimPointVO> countsTrend(CountsTrendParam countsTrendParam) {
        String productTypeCode = countsTrendParam.getProductTypeCode();
        List<MemberGroupDeviceBindingLog> memberGroupDeviceBindingLogs = memberGroupDeviceBindingLogService.queryByPeriod(countsTrendParam.period);
        if (ListUtils.isEmpty(memberGroupDeviceBindingLogs)) {
            return Collections.emptyList();
        }
        //根据不同的日期进行分组
        Map<Integer, List<MemberGroupDeviceBindingLog>> dateGroupMap = MapUtils.listGroupToMap(memberGroupDeviceBindingLogs, memberGroupDeviceBindingLog -> DateUtils.monthOfYear(memberGroupDeviceBindingLog.getCreateTime()));
        //获取这些设备产品信息
        List<DeviceProductInfoWithDeviceDTO> deviceProductInfoWithDevices = getProductInfoWithDeviceDTOS(new HashSet<>(ListUtils.convertItem(memberGroupDeviceBindingLogs, MemberGroupDeviceBindingLog::getDeviceId)));
        Map<Long, DeviceProductInfoDTO> deviceProductInfoMap = deviceProductInfoWithDevices.stream()
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, DeviceProductInfoWithDeviceDTO::getProductInfo));
        List<BimPointVO> monthsList = getMonthsList(countsTrendParam.period);
        //统计不同日期分组产品新增数量
        return statisticProductCountsTrend(dateGroupMap, productTypeCode, deviceProductInfoMap, monthsList);
    }

    private static List<BimPointVO> getMonthsList(Period period) {
        List<BimPointVO> months = new ArrayList<>();
        LocalDateTime start = period.getBegin();
        LocalDateTime end = period.getEnd();
        LocalDateTime current = start;
        while (!current.isAfter(end)) {
            months.add(new BimPointVO(String.valueOf(DateUtils.monthOfYear(current)), 0L));
            current = current.plusMonths(1);
        }
        return months;
    }

    @Override
    public List<BimPointVO> productsMonthUserCounts(Period period) {
        List<MemberGroup> memberGroups = disinfectionMemberGroupService.queryByPeriod(period);
        //根据不同的日期进行分组
        Map<Integer, List<MemberGroup>> dateGroupMap = MapUtils.listGroupToMap(memberGroups, memberGroup -> DateUtils.monthOfYear(memberGroup.getCreateTime()));
        List<BimPointVO> monthsList = getMonthsList(period);
        if (ListUtils.isEmpty(monthsList)) {
            return Collections.emptyList();
        }
        Map<String, BimPointVO> pointVOMap = MapUtils.listToMap(monthsList, BimPointVO::getDate, bimPointVO -> bimPointVO);
        dateGroupMap.forEach((key, value) -> {
            BimPointVO bimPointVO = pointVOMap.get(String.valueOf(key));
            if (Objects.nonNull(bimPointVO)) {
                bimPointVO.setCount((long) value.size());
            }
        });
        return monthsList;
    }

    @Override
    public List<ProductTypeCountVO> productsNewCounts(Period period) {
        List<MemberGroupDeviceBindingLog> memberGroupDeviceBindingLogs = memberGroupDeviceBindingLogService.queryByPeriod(period);
        //获取这些设备产品信息
        List<DeviceProductInfoWithDeviceDTO> deviceProductInfoWithDevices = getProductInfoWithDeviceDTOS(new HashSet<>(ListUtils.convertItem(memberGroupDeviceBindingLogs, MemberGroupDeviceBindingLog::getDeviceId)));
        //统计不同产品类型新增数量
        return statisticCategoryProductsCount(deviceProductInfoWithDevices);
    }

    @Override
    public List<BimPointVO> halfYearNewDeviceMonthCounts(String productTypeCode) {
        var now = LocalDateTime.now();
        var period = Period.of(DateUtils.offsetMonth(DateUtils.beginOfMonth(now), -5), DateUtils.endOfDay(now));

        var monthsList = getMonthsList(period);
        if (ListUtils.isEmpty(monthsList)) {
            return Collections.emptyList();
        }

        // 近半年新增设备：直接根据分组-设备关联表的创建时间统计
        var memberGroupDevices = memberGroupDeviceService.lambdaQuery()
                .select(MemberGroupDevice::getDeviceId, MemberGroupDevice::getCreateTime)
                .ge(MemberGroupDevice::getCreateTime, period.getBegin())
                .lt(MemberGroupDevice::getCreateTime, period.getEnd())
                .list();
        if (ListUtils.isEmpty(memberGroupDevices)) {
            return monthsList;
        }

        // 按月份归集到“设备去重集合”
        var monthToDistinctDeviceIdsMap = new HashMap<Integer, Set<Long>>();
        memberGroupDevices.forEach(item -> {
            var monthOfYear = DateUtils.monthOfYear(item.getCreateTime());
            if (!monthToDistinctDeviceIdsMap.containsKey(monthOfYear)) {
                monthToDistinctDeviceIdsMap.put(monthOfYear, new HashSet<>());
            }
            if (Objects.nonNull(item.getDeviceId())) {
                monthToDistinctDeviceIdsMap.get(monthOfYear).add(item.getDeviceId());
            }
        });

        // 获取本周期内设备的产品分类，用于按 productTypeCode 过滤
        var deviceIdSet = monthToDistinctDeviceIdsMap.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        var productInfos = getProductInfoWithDeviceDTOS(deviceIdSet);
        var deviceToProductCategoryMap = productInfos.stream()
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId,
                                          productInfo -> productInfo.getProductInfo().getProductCategory().getCode(),
                                          (o, n) -> o));

        var bimPointMap = MapUtils.listToMap(monthsList, BimPointVO::getDate, bimPointVO -> bimPointVO);
        monthToDistinctDeviceIdsMap.forEach((monthOfYear, deviceIds) -> {
            long count;
            if (StringUtils.isBlank(productTypeCode)) {
                count = deviceIds.size();
            } else {
                count = deviceIds.stream().filter(deviceId -> {
                    var categoryCode = deviceToProductCategoryMap.get(deviceId);
                    return Objects.nonNull(categoryCode) && productTypeCode.equals(categoryCode);
                }).count();
            }
            var bimPointVO = bimPointMap.get(String.valueOf(monthOfYear));
            if (Objects.nonNull(bimPointVO)) {
                bimPointVO.setCount(count);
            }
        });

        return monthsList;
    }

    @Override
    public Pagination<BimBoundDevicePageResult> boundDevicesPage(BoundDevicePageQueryParam param) {
        var deviceIds = memberGroupDeviceService.getAllBindDeviceIdList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Pagination.empty();
        }

        var deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setMatchedDeviceIds(new ArrayList<>(deviceIds));
        deviceFilterPageParam.setKeyword(param.getKeyword());
        deviceFilterPageParam.setCategoryCode(param.getProductTypeCode());
        deviceFilterPageParam.setDeviceState(param.getDeviceState());
        deviceFilterPageParam.setPageSize(param.getPageSize());
        deviceFilterPageParam.setPageNumber(param.getPageNumber());

        var deviceVOPagination = deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
        if (deviceVOPagination == null || CollectionUtils.isEmpty(deviceVOPagination.getRecords())) {
            return Pagination.empty();
        }

        var pageDeviceIds = deviceVOPagination.getRecords().stream().map(item -> item.getId()).collect(Collectors.toList());
        var boundCoords = memberGroupDeviceService.lambdaQuery()
                .select(MemberGroupDevice::getDeviceId, MemberGroupDevice::getLatitude, MemberGroupDevice::getLongitude,
                        MemberGroupDevice::getLocationDetail, MemberGroupDevice::getCreateTime)
                .in(MemberGroupDevice::getDeviceId, pageDeviceIds)
                .orderByDesc(MemberGroupDevice::getCreateTime)
                .list();
        var deviceCoordMap = new HashMap<Long, MemberGroupDevice>();
        boundCoords.forEach(item -> deviceCoordMap.putIfAbsent(item.getDeviceId(), item));
        var liquidMap = queryRemainingLiquidMap(pageDeviceIds);
        var productIds = deviceVOPagination.getRecords().stream()
                .map(item -> item.getProductInfo() != null ? item.getProductInfo().getId() : null)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .distinct()
                .collect(Collectors.toList());
        var liquidUnitMap = queryRemainingLiquidUnitMap(productIds);

        var resultList = deviceVOPagination.getRecords().stream()
                .map(item -> {
                    var coord = deviceCoordMap.get(item.getId());
                    var productName = item.getProductInfo() != null ? item.getProductInfo().getName() : null;
                    var remainingLiquid = liquidMap.getOrDefault(item.getId(), "0");
                    var remainingLiquidUnit = item.getProductInfo() != null
                            ? liquidUnitMap.get(String.valueOf(item.getProductInfo().getId())) : null;
                    return new BimBoundDevicePageResult(item.getId(), item.getName(), item.getStatus(),
                                                        coord != null ? coord.getLatitude() : null,
                                                        coord != null ? coord.getLongitude() : null,
                                                        productName,
                                                        coord != null ? coord.getLocationDetail() : null,
                                                        remainingLiquid,
                                                        remainingLiquidUnit);
                })
                .collect(Collectors.toList());

        return new Pagination<>(deviceVOPagination.getTotal(), deviceVOPagination.getPages(), resultList);
    }

    private Map<Long, String> queryRemainingLiquidMap(List<Long> deviceIds) {
        var result = new HashMap<Long, String>();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return result;
        }
        try {
            var query = new MultiDevicePropParam();
            var devIdAndPropCodes = deviceIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> id + "." + MOSQUITO_DOSAGE_PROP_CODE)
                    .collect(Collectors.joining(","));
            if (StringUtils.isBlank(devIdAndPropCodes)) {
                return result;
            }
            query.setDevIdAndPropCodes(devIdAndPropCodes);
            var valueMap = iotDevicePropClient.multiProp(query);
            if (valueMap == null || valueMap.isEmpty()) {
                return result;
            }
            for (Long deviceId : deviceIds) {
                if (deviceId == null) {
                    continue;
                }
                var values = valueMap.get(String.valueOf(deviceId));
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                var first = values.get(0);
                if (first != null && first.getValue() != null) {
                    result.put(deviceId, String.valueOf(first.getValue()));
                }
            }
        } catch (Exception e) {
            log.warn("批量查询设备剩余药水量失败: deviceIds={}", deviceIds, e);
        }
        return result;
    }

    private Map<String, String> queryRemainingLiquidUnitMap(List<String> productIds) {
        var result = new HashMap<String, String>();
        if (CollectionUtils.isEmpty(productIds)) {
            return result;
        }
        try {
            List<ProductDTO> products = iotProductClient.prodListDetail(productIds);
            if (CollectionUtils.isEmpty(products)) {
                return result;
            }
            products.forEach(product -> {
                var props = product.getProductPropVoList();
                if (CollectionUtils.isEmpty(props)) {
                    return;
                }
                props.stream()
                        .filter(prop -> MOSQUITO_DOSAGE_PROP_CODE.equals(prop.getPropCode()))
                        .findFirst()
                        .ifPresent(prop -> result.put(product.getProdId(), prop.getUnitName()));
            });
        } catch (Exception e) {
            log.warn("批量查询药水量单位失败: productIds={}", productIds, e);
        }
        return result;
    }

    @Override
    public BimStatisticsOverviewVO statisticsOverview() {
        var deviceIds = memberGroupDeviceService.getAllBindDeviceIdList();
        var totalDeviceCount = 0L;
        var onlineDeviceCount = 0L;
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            var deviceFilterPageParam = new DeviceFilterPageParam();
            deviceFilterPageParam.setMatchedDeviceIds(new ArrayList<>(deviceIds));
            totalDeviceCount = (long) deviceCenterStdClient.deviceInfoIdTypes(deviceFilterPageParam).size();

            var stateMap = deviceBaseStatisticsClient.deviceStatistics(deviceIds);
            if (stateMap != null && !stateMap.isEmpty()) {
                onlineDeviceCount = stateMap.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(state -> Integer.valueOf(1).equals(state.getDeviceState()))
                        .count();
            }
        }
        var todayStart = LocalDate.now().atStartOfDay();
        var todayEnd = todayStart.plusDays(1);
        var todayNewDeviceCount = memberGroupDeviceService.lambdaQuery()
                .select(MemberGroupDevice::getDeviceId)
                .ge(MemberGroupDevice::getCreateTime, todayStart)
                .lt(MemberGroupDevice::getCreateTime, todayEnd)
                .list()
                .stream()
                .map(MemberGroupDevice::getDeviceId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        var todayNewUserCount = memberGroupUserService.lambdaQuery()
                .select(MemberGroupUser::getUserId)
                .ge(MemberGroupUser::getCreateTime, todayStart)
                .lt(MemberGroupUser::getCreateTime, todayEnd)
                .list()
                .stream()
                .map(MemberGroupUser::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return new BimStatisticsOverviewVO(totalDeviceCount, onlineDeviceCount, todayNewDeviceCount, todayNewUserCount);
    }

    @Override
    public List<DistrictDeviceCountVO> districtBoundDeviceCounts() {
        var devices = memberGroupDeviceService.lambdaQuery()
                .select(MemberGroupDevice::getDistrictLocation)
                .list();
        if (CollectionUtils.isEmpty(devices)) {
            return Collections.emptyList();
        }

        var districtCountMap = new HashMap<String, Long>();
        for (MemberGroupDevice device : devices) {
            var district = StringUtils.isNotBlank(device.getDistrictLocation()) ? device.getDistrictLocation() : UNKNOWN_DISTRICT;
            districtCountMap.put(district, districtCountMap.getOrDefault(district, 0L) + 1L);
        }
        return districtCountMap.entrySet().stream()
                .map(entry -> new DistrictDeviceCountVO(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
    }

    @Override
    public ManualCommandUsageCountVO manualCommandUsageCounts() {
        var driveMosquitoCount = disinfectionMemberLogService.lambdaQuery()
                .eq(MemberLog::getType, MemberLogTypeEnum.USER_OPERATION_LOG)
                .like(MemberLog::getDetail, COMMAND_KEYWORD_DRIVE_MOSQUITO)
                .count();
        var mistSprayCount = disinfectionMemberLogService.lambdaQuery()
                .eq(MemberLog::getType, MemberLogTypeEnum.USER_OPERATION_LOG)
                .like(MemberLog::getDetail, COMMAND_KEYWORD_MIST_SPRAY)
                .count();
        return new ManualCommandUsageCountVO(driveMosquitoCount, mistSprayCount);
    }

    private static List<BimPointVO> statisticProductCountsTrend(Map<Integer, List<MemberGroupDeviceBindingLog>> dateGroupMap,
                                                                String productTypeCode,
                                                                Map<Long, DeviceProductInfoDTO> deviceProductInfoMap,
                                                                List<BimPointVO> monthsList
                                                               ) {
        if (ListUtils.isEmpty(monthsList)) {
            return Collections.emptyList();
        }
        Map<String, BimPointVO> bimPointMap = MapUtils.listToMap(monthsList, BimPointVO::getDate, bimPointVO -> bimPointVO);
        dateGroupMap.forEach((key, memberGroupDeviceBindingLogList) -> {
            long count = memberGroupDeviceBindingLogList.stream().filter(memberGroupDeviceBindingLog -> {
                if (StringUtils.isBlank(productTypeCode)) {
                    return true;
                }
                Long deviceId = memberGroupDeviceBindingLog.getDeviceId();
                DeviceProductInfoDTO deviceProductInfoDTO = deviceProductInfoMap.get(deviceId);
                if (Objects.isNull(deviceProductInfoDTO)) {
                    return false;
                }
                DeviceProductInfoDTO.ProductCategory productCategory = deviceProductInfoDTO.getProductCategory();
                if (Objects.isNull(productCategory)) {
                    return false;
                }
                return productTypeCode.equals(productCategory.getCode());
            }).count();
            BimPointVO bimPointVO = bimPointMap.get(String.valueOf(key));
            if (Objects.nonNull(bimPointVO)) {
                bimPointVO.setCount(count);
            }
        });
        return monthsList;
    }

    private List<DeviceProductInfoWithDeviceDTO> getProductInfoWithDeviceDTOS(Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        //查询绑定的设备所属的产品
        return deviceCenterStdClient.getProductInfoByDeviceId(deviceIds);
    }

    private static Map<String, Long> statisticProductCategoryUsersMap(Map<Long, List<MemberGroupDevice>> groupsMap,
                                                                      Map<Long, DeviceProductInfoDTO> deviceToProductsMap
                                                                     ) {
        Map<String, Long> productUsersCountMap = new HashMap<>();
        groupsMap.forEach((groupId, memberGroupDeviceList) -> {
            memberGroupDeviceList.forEach(memberGroupDevice -> {
                DeviceProductInfoDTO deviceProductInfo = deviceToProductsMap.get(memberGroupDevice.getDeviceId());
                String name = deviceProductInfo.getProductCategory().getName();
                Long count = productUsersCountMap.getOrDefault(name, 0L);
                if (count == 0) {
                    productUsersCountMap.put(name, 1L);
                } else {
                    productUsersCountMap.put(name, count + 1);
                }
            });
        });
        return productUsersCountMap;
    }

    private static List<ProductTypeCountVO> statisticCategoryProductsCount(List<DeviceProductInfoWithDeviceDTO> productInfos) {
        Map<String, List<DeviceProductInfoWithDeviceDTO>> categoryGroupMap = MapUtils
                .listGroupToMap(productInfos, deviceProductInfoWithDeviceDTO -> deviceProductInfoWithDeviceDTO
                        .getProductInfo().getProductCategory().getCode());
        return categoryGroupMap
                .values()
                .stream()
                .map(products -> {
                    long count = products.size();
                    DeviceProductInfoWithDeviceDTO deviceProductInfoWithDeviceDTO = products.get(0);
                    return new ProductTypeCountVO(deviceProductInfoWithDeviceDTO.getProductInfo().getProductCategory().getName(), count);
                })
                .sorted((o1, o2) -> (int) (o2.getCount() - o1.getCount()))
                .collect(Collectors.toList());
    }
}
