package com.guhecloud.cube.iot.mgt.dcm.controller;

import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictService;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.dcm.service.IotDcmService;
import com.guhecloud.cube.iot.mgt.dcm.support.DictEntryDcmHelper;
import com.guhecloud.cube.iot.mgt.dcm.support.DictTypeDcmHelper;
import com.guhecloud.cube.iot.mgt.dcm.support.ProdModelDcmHelper;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_DICT_ENTRY;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_DICT_TYPE;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_PROD_MODEL;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.IOT_DCM_PROVIDER_TYPES;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.PROVIDER_IOT;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchTenantedRuntimeConfig;

@Slf4j
@RestController
@RequestMapping("/iot/dcm/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IotDcmController extends AbstractBaseDcmController {

    private final SysDictService sysDictService;

    private final SysDictItemService sysDictItemService;

    private final IotDcmService iotDcmService;

    private final PlatformEvnCom platformEvnCom;

    private final MdProductService mdProductServiceImpl;
    private final MdProductEventService mdProductEventService;
    private final MdProductPropService mdProductPropService;

    private DictTypeDcmHelper dictTypeDcmHelper;

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "/dcm/dcm-declaration-iot.yaml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return IOT_DCM_PROVIDER_TYPES;
    }

    private synchronized DictTypeDcmHelper getDictTypeDcmHelper() {
        if (dictTypeDcmHelper == null) {
            dictTypeDcmHelper = new DictTypeDcmHelper(platformEvnCom);
        }
        return dictTypeDcmHelper;
    }

    private ProdModelDcmHelper getProdModelDcmHelper() {
        return new ProdModelDcmHelper(mdProductServiceImpl,
                                      mdProductPropService, mdProductEventService, platformEvnCom,
                                      sysDictService.list(), sysDictItemService.list());
    }

    @Override
    protected void registerDcmCaps() {
        registerSystemDcmCaps(PROVIDER_IOT, CONFIG_TYPE_DICT_TYPE,
                              () -> DcmSupport.fetchRuntimeConfig(sysDictService.list(), getDictTypeDcmHelper()),
                              (t, f, p) -> DcmSupport.compare1dRCIs(t, f, p, getDictTypeDcmHelper()),
                              (x) -> DcmSupport.applyDcmConfig(x, sysDictService, getDictTypeDcmHelper()));

        registerTenantDcmCaps(PROVIDER_IOT, CONFIG_TYPE_DICT_ENTRY,
                              (t) ->
                                      fetchTenantedRuntimeConfig(
                                              t, sysDictItemService.list().stream()
                                                      .sorted(Comparator.comparing(SysDictItem::getDictId))  // 尽量保存输出的稳定顺序
                                                      .collect(Collectors.toList()),
                                              new DictEntryDcmHelper(
                                                      sysDictService.list(),
                                                      sysDictItemService.list())),
                              (t, f, p) ->
                                      DcmSupport.compare1dRCIs(
                                              t, f, p, DictEntryDcmHelper.createRciComparator()),
                              (x, t) -> iotDcmService.applyDictEntryConfig(t, x));

        registerTenantDcmCaps(PROVIDER_IOT, CONFIG_TYPE_PROD_MODEL,
                              (t) ->
                                      fetchTenantedRuntimeConfig(
                                              t, mdProductServiceImpl.list(),
                                              getProdModelDcmHelper()),
                              (t, f, p) -> DcmSupport.compare1dRCIs(
                                      t, f, p, ProdModelDcmHelper.createRciComparator()),
                              (x, t) -> iotDcmService.applyProductModelConfig(t, x));
    }
}
