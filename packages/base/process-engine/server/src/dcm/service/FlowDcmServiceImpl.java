package com.guhecloud.cube.base.flow.dcm.service;

import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowCommandService;
import com.guhecloud.cube.base.flow.dcm.support.FlowDcmHelper;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchTenantedRuntimeConfig;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-25  19:42
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FlowDcmServiceImpl implements FlowDcmService {
    private final FormFlowCommandService flowCommandService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeAppGroupConfig(Long tenant) {
        return fetchTenantedRuntimeConfig(tenant, flowCommandService, new FlowDcmHelper(flowCommandService, flowCommandService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyAppGroupConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> {
            FlowDcmHelper dcmHelper = new FlowDcmHelper(flowCommandService, flowCommandService.list());
            DcmSupport.applyDcmConfig(diffs2Apply,
                                      flowCommandService::list,
                                      dcmHelper::batchAdd,
                                      dcmHelper::batchUpdate,
                                      dcmHelper::batchRemove,
                                      dcmHelper);
        });
        return diffs2Apply;
    }
}
