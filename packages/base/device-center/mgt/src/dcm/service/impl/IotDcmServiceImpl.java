package com.guhecloud.cube.iot.mgt.dcm.service.impl;

import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictService;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.dcm.service.IotDcmService;
import com.guhecloud.cube.iot.mgt.dcm.support.DictEntryDcmHelper;
import com.guhecloud.cube.iot.mgt.dcm.support.ProdModelDcmHelper;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IotDcmServiceImpl implements IotDcmService {
    private final SysDictItemService sysDictItemService;
    private final SysDictService sysDictService;
    private final MdProductService mdProductService;
    private final MdProductPropService productPropService;
    private final MdProductEventService productEventService;
    private final PlatformEvnCom platformEvnCom;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<RequestedConfigItem> applyDictEntryConfig(Long tenantId, List<RequestedConfigItem> diffs2Apply) {
        DcmSupport.executeWithTenant(tenantId, () ->
                                             DcmSupport.treeApplyDcmConfig(diffs2Apply, sysDictItemService,
                                                                           new DictEntryDcmHelper(sysDictService.list(), sysDictItemService.list(), platformEvnCom))
                                    );
        return diffs2Apply;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<RequestedConfigItem> applyProductModelConfig(Long tenantId, List<RequestedConfigItem> requestedConfigItems) {
        DcmSupport.executeWithTenant(tenantId, () -> {
            ProdModelDcmHelper dcmHelper = new ProdModelDcmHelper(mdProductService, productPropService,
                                                                  productEventService, platformEvnCom,
                                                                  sysDictService.list(), sysDictItemService.list());

            DcmSupport.applyDcmConfig(requestedConfigItems,
                                      mdProductService::list,
                                      dcmHelper::batchAdd,
                                      dcmHelper::batchUpdate,
                                      dcmHelper::batchRemove,
                                      dcmHelper
                                     );
        });
        return requestedConfigItems;
    }
}
