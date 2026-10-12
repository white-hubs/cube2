package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.service.impl;

import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.service.DcmHiddenDangerService;
import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerCategoryHelper;
import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerTypeHelper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerCategoryCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerTypeCommandService;
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
public class DcmHiddenDangerServiceImpl implements DcmHiddenDangerService {

    private HiddenDangerCategoryCommandService hiddenDangerCategoryService;
    private HiddenDangerTypeCommandService hiddenDangerTypeService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeHiddenDangerCategoryConfig(Long tenant) {
        return fetchTenantedRuntimeConfig(tenant, hiddenDangerCategoryService, new DcmHiddenDangerCategoryHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyHiddenDangerCategoryConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, hiddenDangerCategoryService, new DcmHiddenDangerCategoryHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeHiddenDangerTypeConfig(Long tenant) {
        return fetchTenantedRuntimeConfig(tenant, hiddenDangerTypeService, new DcmHiddenDangerTypeHelper(hiddenDangerCategoryService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyHiddenDangerTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, hiddenDangerTypeService, new DcmHiddenDangerTypeHelper(hiddenDangerCategoryService.list())));
        return diffs2Apply;
    }
}
