package com.guhecloud.cube.srv.gen.maintain.dcm.fault.service.impl;

import com.guhecloud.cube.srv.gen.maintain.dcm.fault.service.DcmFaultService;
import com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultTypeHelper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultTypeCommandService;
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
public class DcmFaultServiceImpl implements DcmFaultService {

    private final FaultTypeCommandService faultTypeService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeFaultTypeConfig(Long tenant) {
        return fetchTenantedRuntimeConfig(tenant, faultTypeService, new DcmFaultTypeHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyFaultTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, faultTypeService, new DcmFaultTypeHelper()));
        return diffs2Apply;
    }
}
