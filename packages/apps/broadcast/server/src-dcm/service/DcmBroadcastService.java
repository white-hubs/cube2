package com.guhecloud.cube.biz.subsystem.broadcast.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface DcmBroadcastService {

    List<RequestedConfigItem> fetchRuntimeBroadcastTemplateConfig(Long tenantId);

    List<RequestedConfigItem> applyBroadcastTemplateConfig(List<RequestedConfigItem> requestedConfigItems, Long aLong);
}
