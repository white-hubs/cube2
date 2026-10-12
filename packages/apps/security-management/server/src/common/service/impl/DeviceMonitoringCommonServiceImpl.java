package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.client.vo.AreaListVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.cube.iot.north.bridge.client.util.DevicePropUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.DeviceCollectionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.DevicePropWithTsDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.SecurityDeviceInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.DeviceCollection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.HistoryDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.DeviceInfoPageQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceMonitoringCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.AreaDeviceNumVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceCollectionVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityStatusDevicesNumVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityStatusListVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.devicecenter.SecurityDeviceTagFacadeService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceMonitoringCommonServiceImpl extends ServiceImpl<SecurityCommonMapper, SecurityDevice> implements DeviceMonitoringCommonService {
    private final SecurityCommonMapper securityCommonMapper;

    private final AreaClient areaClient;

    private final DeviceCollectionMapper deviceCollectionMapper;

    private final FileClient fileClient;

    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;

    private final IotDeviceClient iotDeviceClient;

    private final DeviceInfoAssembler deviceInfoAssembler;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final SecurityDeviceTagFacadeService securityDeviceTagFacadeService;

    @Override
    public List<AreaListVO> getAreaTree(String applicationCode) {
        List<AreaDeviceNumVO> deviceNumVOList = securityCommonMapper.getDeviceNum(applicationCode);
        if (CollectionUtils.isEmpty(deviceNumVOList)) {
            return new ArrayList<>();
        }
        Set<Long> areaIdList = new HashSet<>();
        Map<Long, Integer> areaDeviceNumMap = new HashMap<>();
        for (AreaDeviceNumVO areaDeviceNumVO : deviceNumVOList) {
            Set<Long> collect = Arrays.stream(areaDeviceNumVO.getAreaId().split("-")).map(Long::parseLong).collect(Collectors.toSet());
            for (Long areaId : collect) {
                areaDeviceNumMap.merge(areaId, areaDeviceNumVO.getDeviceNum(), Integer::sum);
            }
            areaIdList.addAll(collect);
        }
        List<AreaListVO> areaListVOS = areaClient.securityAreaList(new AreaQueryParam());
        for (AreaListVO areaDTO : areaListVOS) {
            areaDTO.setDeviceNum(areaDeviceNumMap.get(areaDTO.getId()) == null ? 0 : areaDeviceNumMap.get(areaDTO.getId()));
        }
        List<AreaListVO> collect = areaListVOS.stream().filter(s -> !s.getDeviceNum().equals(0)).collect(Collectors.toList());
        // 获取下级sequence
        return TreeUtils.assembleTrees(collect);
    }

    @Override
    public SecurityStatusDevicesNumVo getStatusList(String applicationCode) {
        SecurityStatusDevicesNumVo statusDevicesNumVo = new SecurityStatusDevicesNumVo();
        List<SecurityStatusListVo> list = new ArrayList<>();
        List<SecurityDevice> securityDevices = securityCommonMapper.selectList(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                       .eq(SecurityDevice::getApplicationCode, applicationCode));
        List<Long> ids = securityDevices.stream().map(SecurityDevice::getDeviceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            ids.add(1l);
        }
        List<String> tags = new ArrayList<>();
        tags.add(SecurityManagementCommonUtil.applicationConstants + applicationCode);
        DeviceStateCountDTO deviceStateCountDTO = deviceBaseStatisticsClient.countingNumberOfGroupByStatusFromSecurity(tags);
        //正常
        SecurityStatusListVo statusListVo = new SecurityStatusListVo();
        statusListVo.setName("ONLINE");
        statusListVo.setNum(deviceStateCountDTO == null ? 0 : deviceStateCountDTO.getNormalNumber());
        list.add(statusListVo);

        //离线
        SecurityStatusListVo statusListVo1 = new SecurityStatusListVo();
        statusListVo1.setName("OFFLINE");
        statusListVo1.setNum(deviceStateCountDTO == null ? 0 : deviceStateCountDTO.getOfflineNumber());
        list.add(statusListVo1);

        statusDevicesNumVo.setTotal(securityDevices.size());
        statusDevicesNumVo.setStatusListVos(list);
        return statusDevicesNumVo;
    }

    @Override
    public List<DeviceProdCategoryVo> getApplicationGenreVo(String applicationCode) {

        return securityCommonMapper.getApplicationGenreVo(applicationCode);
    }

    @Override
    public List<ModelVo> getApplicationModels(String applicationCode) {
        return securityCommonMapper.getApplicationModels(applicationCode);
    }

    @Override
    public List<DeviceProdCategoryVo> getStatisticsGenreVo(List<Long> ids) {

        return securityCommonMapper.getStatisticsGenreVo(ids);
    }

    @Override
    public List<DeviceCollectionVo> getDeviceCollection(String applicationCode) {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<DeviceCollectionVo> deviceCollection = securityCommonMapper.getDeviceCollection(applicationCode, principal.getId());
        return deviceCollection;
    }

    @Override
    public Long getDevicesNum(String applicationCode) {
        return securityCommonMapper.selectCount(Wrappers.<SecurityDevice>lambdaQuery()
                                                        .eq(SecurityDevice::getApplicationCode, applicationCode));
    }

    @Override
    public Long getCollectionDevicesNum(String applicationCode) {
        return deviceCollectionMapper.selectCount(Wrappers.<DeviceCollection>lambdaQuery()
                                                          .eq(DeviceCollection::getApplicationCode, applicationCode)
                                                          .eq(DeviceCollection::getUserId, SecurityContextHolder.getPrincipal().getId()));
    }

    @Override
    public Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringPage(String applicationCode, DeviceMonitoringParam param) {
        DeviceInfoPageQuery deviceInfoPageQuery = ObjectUtils.copying(param, DeviceInfoPageQuery.class);
        deviceInfoPageQuery.setApplicationCode(applicationCode);
        if (StringUtils.isNotEmpty(param.getDeviceId())) {
            deviceInfoPageQuery.setDeviceId(param.getDeviceId());
        }
        if (CollectionUtils.isNotEmpty(param.getAreaId()) && param.getAreaId().size() > 0) {
            String join = String.join("-", param.getAreaId());
            deviceInfoPageQuery.setAreaId(join);
        }
        if (StringUtils.isNotEmpty(param.getAppAreaId())) {
            deviceInfoPageQuery.setAreaId(param.getAppAreaId());
        }
        if (StringUtils.isNotEmpty(param.getDeviceProdCategoryCodeConcat())) {
            deviceInfoPageQuery.setDeviceProdCategoryCodeConcat(param.getDeviceProdCategoryCodeConcat());
        }
        if (Objects.nonNull(param.getDeviceStatus())) {
            deviceInfoPageQuery.setDeviceStatus(param.getDeviceStatus());
        }
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            deviceInfoPageQuery.setKeyword(param.getKeyword());
        }
        if (!CollectionUtils.isEmpty(param.getTagCodes())) {
            List<Long> tagFilterDeviceId = filterTagDevices(applicationCode, param);
            deviceInfoPageQuery.setIds(tagFilterDeviceId);
        }

        Pagination<SecurityDeviceInfoDTO> page = deviceInfoAssembler.page(deviceInfoPageQuery);
        List<SecurityDeviceInfoDTO> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page;
        }

        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<Long> deviceIds = records.stream().map(SecurityDeviceInfoDTO::getDeviceId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return page;
        }

        List<DeviceCollection> deviceCollections = deviceCollectionMapper.selectList(Wrappers.<DeviceCollection>lambdaQuery()
                                                                                             .in(DeviceCollection::getDeviceId, deviceIds)
                                                                                             .eq(DeviceCollection::getUserId, userId)
                                                                                             .eq(DeviceCollection::getApplicationCode, applicationCode));
        Set<Long> collectedDeviceIds = CollectionUtils.isEmpty(deviceCollections)
                                       ? Collections.emptySet()
                                       : deviceCollections.stream().map(DeviceCollection::getDeviceId).collect(Collectors.toSet());

        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(deviceIds);
        Map<Long, List<FileVO>> fileMap = Optional.ofNullable(fileClient.getByBusinessKeys(businessKeysParam))
                .orElse(Collections.emptyMap());

        List<Long> distinctDeviceIds = records.stream()
                .map(SecurityDeviceInfoDTO::getDeviceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Executor executor = ExecutorContextHolder.getExecutor();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Map<Long, List<DevicePropWithTsDTO>> devicePropMap = new ConcurrentHashMap<>(distinctDeviceIds.size());
        CompletableFuture<?>[] futures = distinctDeviceIds.stream()
                .map(deviceId -> CompletableFuture
                        .supplyAsync(() -> {
                            SessionContextHolder.requestTenant(tenant);
                            return getDevicePropsWithTs(deviceId);
                        }, executor)
                        .thenAccept(deviceProps -> devicePropMap.put(deviceId, deviceProps)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

        records.forEach(s -> {
            if (collectedDeviceIds.contains(s.getDeviceId())) {
                s.setCollection(true);
            }
            List<FileVO> byBusinessKey = fileMap.get(s.getDeviceId());
            if (CollectionUtils.isNotEmpty(byBusinessKey)) {
                s.setImages(byBusinessKey);
            }
            s.setDevicePropWithTsDTOS(devicePropMap.getOrDefault(s.getDeviceId(), Collections.emptyList()));
        });
        return page;
    }

    private List<DevicePropWithTsDTO> getDevicePropsWithTs(Long deviceId) {
        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(String.valueOf(deviceId));
        deviceDetailParam.setWithSnapShot(true);
        deviceDetailParam.setWithPropertySpec(true);
        try {
            DeviceDetailDTO detail = iotDeviceClient.detail(deviceDetailParam);
            if (Objects.isNull(detail) || CollectionUtils.isEmpty(detail.getProductPropVoList())) {
                return Collections.emptyList();
            }
            List<KeyValueWithTsDTO> snapshots = Optional.ofNullable(detail.getSnapshots()).orElse(Collections.emptyList());
            return detail.getProductPropVoList().stream().map(productPropDTO -> {
                Optional<KeyValueWithTsDTO> propertyValueOptional = snapshots.stream()
                        .filter(keyValueWithTsDTO -> productPropDTO.getPropCode().equals(keyValueWithTsDTO.getKey()))
                        .findFirst();
                DevicePropWithTsDTO deviceAttributeRunningResponse = new DevicePropWithTsDTO();
                deviceAttributeRunningResponse.setKey(productPropDTO.getPropCode());
                deviceAttributeRunningResponse.setUnitName(productPropDTO.getUnitName());
                propertyValueOptional.ifPresent(propertyValue -> {
                    deviceAttributeRunningResponse.setValue(propertyValue.getValue());
                    deviceAttributeRunningResponse.setTs(propertyValue.getTs().toString());
                    deviceAttributeRunningResponse.setTranslatedValue(DevicePropUtils.iotPropVal2Chinese(productPropDTO.getDataType(),
                                                                                                         propertyValue.getValue(),
                                                                                                         productPropDTO.getDataConstraint()));
                });
                return deviceAttributeRunningResponse;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取设备属性失败, deviceId:{}, 失败原因:{}", deviceId, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Pagination<SecurityDeviceInfoDTO> getCollectionDeviceMonitoringPage(String applicationCode, DeviceMonitoringParam param) {
        DeviceInfoPageQuery deviceMonitoringPageQuery = new DeviceInfoPageQuery();
        deviceMonitoringPageQuery.setApplicationCode(applicationCode);
        if (StringUtils.isNotEmpty(param.getDeviceId())) {
            deviceMonitoringPageQuery.setDeviceId(param.getDeviceId());
        }
        if (!CollectionUtils.isEmpty(param.getAreaId())) {
            String join = String.join("-", param.getAreaId());
            deviceMonitoringPageQuery.setAreaId(join);
        }
        if (StringUtils.isNotEmpty(param.getAppAreaId())) {
            deviceMonitoringPageQuery.setAreaId(param.getAppAreaId());
        }
        if (StringUtils.isNotEmpty(param.getDeviceProdCategoryCodeConcat())) {
            deviceMonitoringPageQuery.setDeviceProdCategoryCodeConcat(param.getDeviceProdCategoryCodeConcat());
        }
        if (Objects.nonNull(param.getDeviceStatus())) {
            deviceMonitoringPageQuery.setDeviceStatus(param.getDeviceStatus());
        }
        List<DeviceCollection> deviceCollections = deviceCollectionMapper.selectList(Wrappers.<DeviceCollection>lambdaQuery()
                                                                                             .eq(DeviceCollection::getApplicationCode, applicationCode)
                                                                                             .eq(DeviceCollection::getUserId, SecurityContextHolder
                                                                                                     .getPrincipal().getId()));
        List<Long> ids = deviceCollections.stream().map(DeviceCollection::getDeviceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            ids.add(Long.valueOf("1111111111111"));
        }
        deviceMonitoringPageQuery.setIds(ids);
        if (!CollectionUtils.isEmpty(param.getTagCodes())) {
            List<Long> tagFilterDeviceId = filterTagDevices(applicationCode, param);
            if (CollectionUtils.isEmpty(tagFilterDeviceId)) {
                return Pagination.empty();
            }
            //用标签设备中过滤掉收藏的设备
            deviceMonitoringPageQuery.setIds(ids.stream().filter(collectDeviceId -> tagFilterDeviceId.contains(collectDeviceId))
                                                     .collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(deviceMonitoringPageQuery.getIds())) {
                return Pagination.empty();
            }
        }
        Pagination<SecurityDeviceInfoDTO> monitoringDeviceCollectionForSecurityPage = deviceInfoAssembler.page(deviceMonitoringPageQuery);
        if (CollectionUtils.isNotEmpty(monitoringDeviceCollectionForSecurityPage.getRecords())) {
            List<Long> deviceId = monitoringDeviceCollectionForSecurityPage.getRecords().stream().map(SecurityDeviceInfoDTO::getDeviceId)
                    .collect(Collectors.toList());
            List<DeviceCollection> deviceCollection = deviceCollectionMapper.selectList(Wrappers.<DeviceCollection>lambdaQuery()
                                                                                                .in(DeviceCollection::getDeviceId, deviceId)
                                                                                                .eq(DeviceCollection::getUserId, SecurityContextHolder
                                                                                                        .getPrincipal().getId())
                                                                                                .eq(DeviceCollection::getApplicationCode, applicationCode));
            Map<Long, DeviceCollection> deviceCollect = new HashMap<>();
            if (CollectionUtils.isNotEmpty(deviceCollection)) {
                deviceCollect = deviceCollection.stream().collect(Collectors.toMap(DeviceCollection::getDeviceId, s -> s));
            }
            for (SecurityDeviceInfoDTO record : monitoringDeviceCollectionForSecurityPage.getRecords()) {
                DeviceCollection collection = deviceCollect.get(record.getDeviceId());
                if (Objects.nonNull(collection)) {
                    record.setCollection(true);
                }
            }
        }

        return monitoringDeviceCollectionForSecurityPage;
    }

    private List<Long> filterTagDevices(String applicationCode, DeviceMonitoringParam param) {
        return securityDeviceTagFacadeService.filterTagDevices(applicationCode, param.getTagCodes()).stream().collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDeviceInfo(Long deviceId) {
        HashMap<String, Object> remap = new HashMap<>();
        com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO detail = deviceCenterStdClient.getDeviceById(deviceId);
        IotProductClient iotProductClient = ApplicationContextHolder.getBean(IotProductClient.class);

        // 根据deviceId 获取 prodId
        IotDeviceClient iotDeviceClient = ApplicationContextHolder.getBean(IotDeviceClient.class);
        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(deviceId.toString());
        DeviceDetailDTO deviceInfo = iotDeviceClient.detail(deviceDetailParam);

        ProductPageParam param = new ProductPageParam();
        param.setProductId(deviceInfo.getProductId());
        Pagination<ProductDTO> page = iotProductClient.page(param);
        List<ProductPropDTO> productPropVoList = Collections.EMPTY_LIST;
        if (com.guhecloud.platform.base.util.CollectionUtils.isNotEmpty(page.getRecords())) {
            ProductDTO productDTO = page.getRecords().get(0);
            productPropVoList = productDTO.getProductPropVoList();

        }

        remap.put("detail", detail);
        remap.put("iotAttributes", productPropVoList);

        return remap;
    }

    @Override
    public DeviceTSDTO getDeviceHistoryData(HistoryDataParam param) {
        IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
        HistorySampleParam historySampleParam = new HistorySampleParam();

        Assert.isNotAcceptable(CollectionUtils.isEmpty(param.getAttributeCode()), "该设备无属性");
        var date = DateUtils.now();

        LocalDateTime beginTime = null;
        var endTime = date;

        if (param.getTimeFlag().equals("THIS_MONTH")) {
            beginTime = DateUtils.beginOfMonth(date);

        } else if (param.getTimeFlag().equals("THIS_WEEK")) {
            beginTime = DateUtils.beginOfWeek(date);
        } else if (param.getTimeFlag().equals("TODAY")) {
            beginTime = DateUtils.beginOfDay(date);
        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            beginTime = DateUtils.parse(split[0], "yyyy-MM-dd HH:mm:ss");
            endTime = DateUtils.parse(split[1], "yyyy-MM-dd HH:mm:ss");
        }
        historySampleParam.setDeviceId(param.getDeviceId().toString());
        historySampleParam.setStep(5);
        historySampleParam.setStartTs(DateUtils.toTimestamp(beginTime));
        historySampleParam.setEndTs(DateUtils.toTimestamp(endTime));
        historySampleParam.setProperties(new ArrayList<>(param.getAttributeCode()));
        DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);

        return deviceTSDTO;
    }

    @Override
    public Pagination<SecurityDeviceInfoDTO> getInsideDeviceMonitoringPage(InsideSecurityDeviceParam param) {
        if (CollectionUtils.isNotEmpty(param.getAreaIds())) {
            String join = String.join("-", param.getAreaIds());
            param.setAreaId(join);
        }
        DeviceInfoPageQuery deviceInfoPageQuery = ObjectUtils.copying(param, DeviceInfoPageQuery.class);
        deviceInfoPageQuery.setIds(param.getDeviceIds());
        Pagination<SecurityDeviceInfoDTO> page = deviceInfoAssembler.page(deviceInfoPageQuery);
        return page;
    }

    @Override
    public Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringList(InsideSecurityDeviceParam param) {
        if (CollectionUtils.isNotEmpty(param.getAreaIds())) {
            String join = String.join("-", param.getAreaIds());
            param.setAreaId(join);
        }
        DeviceInfoPageQuery deviceInfoPageQuery = ObjectUtils.copying(param, DeviceInfoPageQuery.class);
        deviceInfoPageQuery.setIds(param.getDeviceIds());
        Pagination<SecurityDeviceInfoDTO> page = deviceInfoAssembler.page(deviceInfoPageQuery);
        return page;
    }

}
