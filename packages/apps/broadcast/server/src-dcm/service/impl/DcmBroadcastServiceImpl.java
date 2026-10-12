package com.guhecloud.cube.biz.subsystem.broadcast.dcm.service.impl;

import com.guhecloud.cube.biz.subsystem.broadcast.dcm.service.DcmBroadcastService;
import com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastTemplateHelper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastTemplateService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchTenantedRuntimeConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DcmBroadcastServiceImpl implements DcmBroadcastService {

    private final BroadcastTemplateService broadcastTemplateService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeBroadcastTemplateConfig(Long tenantId) {
        return fetchTenantedRuntimeConfig(tenantId, broadcastTemplateService, new DcmBroadcastTemplateHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyBroadcastTemplateConfig(List<RequestedConfigItem> diffs2Apply, Long tenantId) {
        executeWithTenant(tenantId, () -> applyDcmConfig(diffs2Apply, broadcastTemplateService, new DcmBroadcastTemplateHelper()));
        return diffs2Apply;
    }
}
