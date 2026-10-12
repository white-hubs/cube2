package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.EnergyDevicePropParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterDeviceService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeviceAttribute {
    private static final Logger logger = LoggerFactory.getLogger(DeviceAttribute.class);

    public static List<KeyValueWithTsDTO> deviceProp(String deviceId, String propCode) {
        AdapterDeviceService adapterService = ApplicationContextHolder.getBean(AdapterDeviceService.class);
        EnergyDevicePropParam energyDevicePropParam = new EnergyDevicePropParam();
        List<String> propCodes = new ArrayList<>();
        propCodes.add(propCode);
        energyDevicePropParam.setPropCodes(propCodes);
        energyDevicePropParam.setDeviceId(Long.valueOf(deviceId));
        try {
            return adapterService.deviceProp(energyDevicePropParam);
        } catch (ServiceException e) {
            logger.error("查询IOT属性异常,异常信息为：{},异常明细：", e.getDetails(), e);
            return Collections.emptyList();
        }
    }
}
