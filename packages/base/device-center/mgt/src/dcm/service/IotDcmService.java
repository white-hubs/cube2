package com.guhecloud.cube.iot.mgt.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface IotDcmService {

    List<RequestedConfigItem> applyDictEntryConfig(
            Long tenantId, List<RequestedConfigItem> requestedConfigItemList);

    List<RequestedConfigItem> applyProductModelConfig(
            Long tenantId, List<RequestedConfigItem> requestedConfigItems);
}
