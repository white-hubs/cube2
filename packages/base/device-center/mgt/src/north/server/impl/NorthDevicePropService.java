package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.MultiDevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.core.def.cache.ProductCacheSupport;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.cube.iot.mgt.north.param.MultiHistorySampleDto;
import com.guhecloud.cube.iot.mgt.north.vo.ts.DeviceTSBo;
import com.guhecloud.cube.iot.mgt.north.vo.ts.PropertyTsBo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class NorthDevicePropService {
    @Resource
    private DeviceCacheSupport deviceCacheSupport;
    @Resource
    private ProductCacheSupport productCacheSupport;
    @Resource
    private TimeSeriesInterface timeSeriesInterface;
    @Resource
    private NorthDeviceMapper northDeviceMapper;

    /**
     * 多设备-历史属性采样获取（一次SQL批量查询）
     */
    public Map<String, DeviceTSBo> multiDevicePropSampleHistoryTS(Integer tenant, Map<Long, Set<String>> deviceProp,
                                                                  MultiHistorySampleDto multiHistorySampleDto) {
        if (Objects.isNull(tenant)) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(multiHistorySampleDto), "tenant is blank");
        }

        Map<Long, DeviceDigestVo> deviceDigestMap = new java.util.HashMap<>();
        for (Long deviceId : deviceProp.keySet()) {
            Optional<DeviceDigestVo> digestOpt = deviceCacheSupport.queryDeviceDigestCache(deviceId);
            if (digestOpt.isEmpty()) {
                throw new ResourcePreconditionFailedException(String.valueOf(deviceId), "设备未找到");
            }
            deviceDigestMap.put(deviceId, digestOpt.get());
        }

        MultiDevicePropHistoryQueryVo queryVo = buildMultiDevicePropHistoryQueryVo(tenant, deviceProp, deviceDigestMap, multiHistorySampleDto);
        List<DevicePropHistoryResultVo> records = timeSeriesInterface.queryMultiDevicePropHistory(queryVo);
        if (Objects.isNull(records) || records.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<DevicePropHistoryResultVo>> deviceRecordMap = records.stream()
                .filter(it -> StringUtils.isNotBlank(it.getDeviceId()))
                .collect(Collectors.groupingBy(DevicePropHistoryResultVo::getDeviceId));

        Map<String, DeviceTSBo> result = new HashMap<>();
        for (Map.Entry<Long, Set<String>> en : deviceProp.entrySet()) {
            Long deviceId = en.getKey();
            Set<String> propCodes = en.getValue();
            DeviceDigestVo digest = deviceDigestMap.get(deviceId);
            List<DevicePropHistoryResultVo> deviceRecords = deviceRecordMap.get(String.valueOf(deviceId));
            if (Objects.isNull(deviceRecords) || deviceRecords.isEmpty()) {
                continue;
            }

            //按属性分组
            Map<String, List<DevicePropHistoryResultVo>> propMap = deviceRecords.stream()
                    .collect(Collectors.groupingBy(DevicePropHistoryResultVo::getPropCode));

            DeviceTSBo deviceTSBo = new DeviceTSBo();
            deviceTSBo.setDeviceId(String.valueOf(deviceId));
            deviceTSBo.setDeviceName(digest.getDeviceName());
            deviceTSBo.setDeviceNumber(digest.getDeviceNumber());
            deviceTSBo.setPropertyTsBoList(new LinkedList<>());

            List<ProductPropSchema> schemas = productCacheSupport.queryProductPropSchemaCache(digest.getProdId());
            for (ProductPropSchema schema : schemas) {
                if (!propCodes.contains(schema.getPropCode())) {
                    continue;
                }

                PropertyTsBo propertyTsBo = new PropertyTsBo();
                propertyTsBo.setPropertyCode(schema.getPropCode());
                propertyTsBo.setPropertyName(schema.getPropName());
                propertyTsBo.setDataType(schema.getDataType());
                propertyTsBo.setPropertyUnit(schema.getUnitName());
                propertyTsBo.setDataConstraint(JSON.parseArray(schema.getDataConfig()));
                propertyTsBo.setValueTsBoList(convert(propMap.get(schema.getPropCode())));

                deviceTSBo.getPropertyTsBoList().add(propertyTsBo);
            }

            result.put(String.valueOf(deviceId), deviceTSBo);
        }

        return result;
    }

    public List<KeyValueWithTs> deviceRealTimeStats(Long deviceId, String propertyCodes, Long notBeforeTs) {
        if (Objects.isNull(notBeforeTs)) {
            notBeforeTs = 0L;
        }

        // propertyCodes 为空默认查询所有属性
        if (StringUtils.isBlank(propertyCodes)) {
            Optional<DeviceDigestVo> digestVoOpt = deviceCacheSupport.queryDeviceDigestCache(deviceId);
            if (digestVoOpt.isEmpty()) {
                return Collections.emptyList();
            }

            DeviceDigestVo deviceDigestVo = digestVoOpt.get();
            List<ProductPropSchema> propSchemas = productCacheSupport.queryProductPropSchemaCache(deviceDigestVo.getProdId());

            if (propSchemas.isEmpty()) {
                return Collections.emptyList();
            }

            List<KeyValueWithTs> result = new ArrayList<>(propSchemas.size());
            for (ProductPropSchema propSchema : propSchemas) {
                Optional<DevicePropValueVo> devicePropValueVo = deviceCacheSupport.queryDevicePropCache(deviceId, propSchema.getPropCode());

                if (devicePropValueVo.isPresent() && devicePropValueVo.get().getTs() >= notBeforeTs) {
                    DevicePropValueVo dpv = devicePropValueVo.get();

                    KeyValueWithTs keyValueWithTs = new KeyValueWithTs();
                    keyValueWithTs.setPreValue(dpv.getPreValue());
                    keyValueWithTs.setValue(dpv.getValue());
                    keyValueWithTs.setKey(propSchema.getPropCode());
                    keyValueWithTs.setTs(dpv.getTs());
                    result.add(keyValueWithTs);
                }
            }

            return result;
        }

        //基于propertyCodes进行查询
        String[] propCodes = propertyCodes.split(",");
        List<KeyValueWithTs> keyValueWithTs = new ArrayList<>();

        for (String propCode : propCodes) {
            Optional<DevicePropValueVo> devicePropValueVo = deviceCacheSupport.queryDevicePropCache(deviceId, propCode);
            if (devicePropValueVo.isPresent() && devicePropValueVo.get().getTs() >= notBeforeTs) {
                DevicePropValueVo dpv = devicePropValueVo.get();

                KeyValueWithTs keyValue = new KeyValueWithTs();
                keyValue.setPreValue(dpv.getPreValue());
                keyValue.setValue(dpv.getValue());
                keyValue.setKey(propCode);
                keyValue.setTs(dpv.getTs());
                keyValueWithTs.add(keyValue);
            }
        }
        return keyValueWithTs;
    }

    private String queryProductIdByDeviceId(Long deviceId) {
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(deviceId);

        if (Objects.isNull(deviceDigestVo)) {
            throw new ResourcePreconditionFailedException(String.valueOf(deviceId), "设备未找到");
        }
        return String.valueOf(deviceDigestVo.getProdId());
    }

    public DeviceTSBo devicePropSampleHistoryTS(DevicePropHistoryQueryVo query) {
        //支持品类变更查询
        query.setProductId(this.queryProductIdByDeviceId(query.getDeviceId()));
        IPage<DevicePropHistoryResultVo> page = timeSeriesInterface.querySingleDevicePropPage(query, 1, query.getMaxRecords());

        if (Objects.isNull(page) || Objects.isNull(page.getRecords()) || page.getRecords().isEmpty()) {
            return null;
        }

        String deviceIdStr = String.valueOf(query.getDeviceId());
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(query.getDeviceId());

        if (Objects.isNull(deviceDigestVo)) {
            return null;
        }

        //按属性分组
        Map<String, List<DevicePropHistoryResultVo>> propMapCache = page.getRecords()
                .stream()
                .collect(Collectors.groupingBy(DevicePropHistoryResultVo::getPropCode));

        //构建返回对象
        DeviceTSBo deviceTSBo = new DeviceTSBo();
        deviceTSBo.setDeviceId(deviceIdStr);
        deviceTSBo.setDeviceName(deviceDigestVo.getDeviceName());
        deviceTSBo.setDeviceNumber(deviceDigestVo.getDeviceNumber());
        deviceTSBo.setPropertyTsBoList(new LinkedList<>());

        //查询属性元信息
        List<ProductPropSchema> listPropCacheBo = productCacheSupport.queryProductPropSchemaCache(deviceDigestVo.getProdId());

        //遍历属性
        for (ProductPropSchema propCacheBo : listPropCacheBo) {

            if (!query.getPropCodes().contains(propCacheBo.getPropCode())) {
                continue;
            }

            PropertyTsBo propertyTsBo = new PropertyTsBo();
            propertyTsBo.setPropertyCode(propCacheBo.getPropCode());
            propertyTsBo.setPropertyName(propCacheBo.getPropName());
            propertyTsBo.setDataType(propCacheBo.getDataType());
            propertyTsBo.setPropertyUnit(propCacheBo.getUnitName());
            propertyTsBo.setDataConstraint(JSON.parseArray(propCacheBo.getDataConfig()));
            propertyTsBo.setValueTsBoList(convert(propMapCache.get(propCacheBo.getPropCode())));

            deviceTSBo.getPropertyTsBoList().add(propertyTsBo);
        }

        return deviceTSBo;
    }

    private MultiDevicePropHistoryQueryVo buildMultiDevicePropHistoryQueryVo(Integer tenant, Map<Long, Set<String>> deviceProp,
                                                                             Map<Long, DeviceDigestVo> deviceDigestMap,
                                                                             MultiHistorySampleDto multiHistorySampleDto
                                                                            ) {
        MultiDevicePropHistoryQueryVo queryVo = new MultiDevicePropHistoryQueryVo();
        queryVo.setTenantId(String.valueOf(tenant));

        if (Objects.nonNull(multiHistorySampleDto)) {
            if (StringUtils.isNotBlank(multiHistorySampleDto.getOrder())) {
                queryVo.setOrder(multiHistorySampleDto.getOrder());
            }
            if (Objects.nonNull(multiHistorySampleDto.getMaxRecords()) && multiHistorySampleDto.getMaxRecords() > 0) {
                queryVo.setMaxRecords(multiHistorySampleDto.getMaxRecords());
            }
            if (Objects.nonNull(multiHistorySampleDto.getStep()) && multiHistorySampleDto.getStep() > 0) {
                queryVo.setStep(multiHistorySampleDto.getStep());
            }
            if (StringUtils.isNotBlank(multiHistorySampleDto.getAggType())) {
                queryVo.setAggType(multiHistorySampleDto.getAggType());
            }
            if (Objects.nonNull(multiHistorySampleDto.getStartTs()) && multiHistorySampleDto.getStartTs() >= 0 &&
                Objects.nonNull(multiHistorySampleDto.getEndTs()) && multiHistorySampleDto.getEndTs() > 0) {
                queryVo.setStartTime(toDateStr(new Date(multiHistorySampleDto.getStartTs())));
                queryVo.setEndTime(toDateStr(new Date(multiHistorySampleDto.getEndTs())));
            }
        }

        boolean raw = "raw".equals(queryVo.getAggType());
        List<MultiDevicePropHistoryQueryVo.QueryItem> items = new java.util.ArrayList<>();
        for (Map.Entry<Long, Set<String>> en : deviceProp.entrySet()) {
            Long deviceId = en.getKey();
            Set<String> props = en.getValue();
            if (Objects.isNull(deviceId) || Objects.isNull(props) || props.isEmpty()) {
                continue;
            }

            String productId = null;
            if (raw) {
                DeviceDigestVo digest = Objects.isNull(deviceDigestMap) ? null : deviceDigestMap.get(deviceId);
                if (Objects.isNull(digest)) {
                    throw new ResourcePreconditionFailedException(String.valueOf(deviceId), "设备未找到");
                }
                productId = String.valueOf(digest.getProdId());
            }

            for (String propCode : props) {
                if (StringUtils.isBlank(propCode)) {
                    continue;
                }
                MultiDevicePropHistoryQueryVo.QueryItem item = new MultiDevicePropHistoryQueryVo.QueryItem();
                item.setDeviceId(String.valueOf(deviceId));
                item.setPropCode(propCode);
                item.setProductId(productId);
                items.add(item);
            }
        }

        if (items.isEmpty()) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(multiHistorySampleDto), "properties is blank");
        }

        queryVo.setItems(items);
        return queryVo;
    }

    private String toDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public Pagination<KeyValueWithTs> devicePropHistoryPropPage(DevicePropHistoryQueryVo propHistoryQueryVo, Integer page,
                                                                Integer pageSize
                                                               ) {
        //支持品类变更查询
        propHistoryQueryVo.setProductId(this.queryProductIdByDeviceId(propHistoryQueryVo.getDeviceId()));
        IPage<DevicePropHistoryResultVo> pageObj = timeSeriesInterface.querySingleDevicePropPage(propHistoryQueryVo, page, pageSize);

        IPage<KeyValueWithTs> result = pageObj.convert(this::convert);
        return Pagination.<KeyValueWithTs>builder()
                .total(result.getTotal())
                .pages(result.getPages())
                .records(result.getRecords())
                .build();
    }

    /**
     * 内部表达历史属性值转换为统一的
     *
     * @param devicePropHistoryResultVos devicePropHistoryResultVos
     *
     * @return list
     */
    private List<KeyValueWithTs> convert(List<DevicePropHistoryResultVo> devicePropHistoryResultVos) {
        if (Objects.isNull(devicePropHistoryResultVos) || devicePropHistoryResultVos.isEmpty()) {
            return Collections.emptyList();
        }

        return devicePropHistoryResultVos.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private KeyValueWithTs convert(DevicePropHistoryResultVo devicePropHistoryResultVo) {
        KeyValueWithTs keyValue = new KeyValueWithTs();
        keyValue.setKey(devicePropHistoryResultVo.getPropCode());
        keyValue.setValue(devicePropHistoryResultVo.getValue());
        keyValue.setTs(devicePropHistoryResultVo.getTs());
        return keyValue;
    }

    private String queryProdCategoryIdByDeviceId(Long deviceId) {
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(deviceId);

        if (Objects.isNull(deviceDigestVo)) {
            throw new ResourcePreconditionFailedException(String.valueOf(deviceId), "设备未找到");
        }
        return String.valueOf(deviceDigestVo.getProductCategoryId());
    }

    public Pagination<DeviceSamplingHistoryResultVo> deviceHistorySamplingPage(DeviceSamplingHistoryQueryVo query, Integer pageNumber,
                                                                               Integer pageSize
                                                                              ) {
        //支持品类变更查询
        query.setProductId(this.queryProductIdByDeviceId(query.getDeviceId()));
        IPage<DeviceSamplingHistoryResultVo> iPage = timeSeriesInterface.queryDeviceSamplingPropPage(query, pageNumber, pageSize);
        return Pagination.<DeviceSamplingHistoryResultVo>builder()
                .total(iPage.getTotal())
                .pages(iPage.getPages())
                .records(iPage.getRecords())
                .build();
    }
}
