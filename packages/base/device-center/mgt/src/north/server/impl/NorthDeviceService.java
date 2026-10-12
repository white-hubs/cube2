package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.cube.iot.mgt.north.param.DeviceDetailParam;
import com.guhecloud.cube.iot.mgt.north.param.DeviceQueryParam;
import com.guhecloud.cube.iot.mgt.north.server.NorthSysDictCacheService;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceDetail;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceThin;
import com.guhecloud.cube.iot.mgt.north.vo.PropPair;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NorthDeviceService {
    @Resource
    private NorthSysDictCacheService northSysDictCacheService;
    @Resource
    private NorthDeviceMapper northDeviceMapper;
    @Resource
    private NorthProductService northProductService;
    @Resource
    private NorthDevicePropService northDevicePropService;

    public Pagination<DeviceThin> page(DeviceQueryParam deviceQueryParam) {
        Page<DeviceThin> page = northDeviceMapper.pageDevice(PagingUtils.convert(deviceQueryParam), deviceQueryParam
                , StringUtils.isEmpty(deviceQueryParam.getProductId()) ? null : Long.parseLong(deviceQueryParam.getProductId()));

        //设备品牌和型号Path拼接
        if (deviceQueryParam.isWithNumberAndBrandPath() && Objects.nonNull(page.getRecords()) && !page.getRecords().isEmpty()) {
            for (DeviceThin deviceThin : page.getRecords()) {
                String prodBrandPath = northSysDictCacheService.queryItemValWithPath(InnerDicEnum.prod_brand.id,
                                                                                     deviceThin.getProdBrandId(),
                                                                                     DeviceThin.PATH_SPLIT);
                deviceThin.setProdBrandPath(prodBrandPath);
            }
        }

        return Pagination.<DeviceThin>builder()
                .pages(page.getPages())
                .total(page.getTotal())
                .records(page.getRecords())
                .build();
    }

    public DeviceDetail detail(DeviceDetailParam deviceDetailParam) {
        DeviceDetail detail = northDeviceMapper.queryDetail(deviceDetailParam);

        //SQL有在线状态(0 or 1) 始终有detail对象返回
        if (Objects.isNull(detail) || StringUtils.isEmpty(detail.getDeviceId())) {
            throw new ResourcePreconditionFailedException("设备未找到");
        }

        if (deviceDetailParam.isWithPropertySpec()) {
            detail.setProductPropVoList(northProductService.queryProductProp(detail.getProductId()));
        }
        if (deviceDetailParam.isWithEventSpec()) {
            detail.setProductEventVoList(northProductService.queryProductEvent(detail.getProductId()));
        }
        if (deviceDetailParam.isWithSnapShot()) {
            detail.setSnapshots(northDevicePropService.deviceRealTimeStats(Long.valueOf(detail.getDeviceId()),
                                                                           null,
                                                                           0L));
        }

        //设备的扩展配置
        if (deviceDetailParam.isWithExtConfig()) {
            detail.setExtConfig(northDeviceMapper.queryDeviceExtConfig(deviceDetailParam.getDeviceId()));
        }

        detail.setOnlineState(detail.getOnlineState() == 1 ? 1 : 0);

        return detail;
    }

    public Map<String, String> devicePropName(String deviceId) {
        List<PropPair> listPair = northDeviceMapper.getDevicePropName(Long.parseLong(deviceId));

        if (Objects.isNull(listPair)) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new HashMap<>();
        for (PropPair propPair : listPair) {
            map.put(propPair.getPropCode(), propPair.getPropName());
        }
        return map;
    }

    public List<String> listDeviceIdByRawDeviceId(String rawDeviceId) {
        return northDeviceMapper.listDeviceIdByRawDeviceId(rawDeviceId);
    }
}
