package com.guhecloud.cube.srv.gen.identity.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.srv.gen.identity.dcm.service.QrCodeDcmService;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeAppDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityAppDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityStyleDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityTypeDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeStyleDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_APP;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_APP;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_STYLE;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_ENTITY_TYPE;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.CONFIG_TYPE_STYLE;
import static com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeDcmSupport.PROVIDER_QR_CODE;

@Slf4j
@RestController
@RequestMapping("/qr-code/dcm/config")
public class QrCodeDcmController extends AbstractBaseDcmController {

    private final QrCodeDcmService qrCodeDcmService;

    public QrCodeDcmController(QrCodeDcmService qrCodeDcmService) {
        this.qrCodeDcmService = qrCodeDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-identity.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_QR_CODE, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_QR_CODE, CONFIG_TYPE_STYLE,
                              (t) -> qrCodeDcmService.fetchRuntimeQrCodeStyleConfig(t),
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, QrCodeStyleDcmHelper.createDcmRciConverter()),
                              (x, t) -> qrCodeDcmService.applyQrCodeStyleConfig(x, t));
        registerTenantDcmCaps(PROVIDER_QR_CODE, CONFIG_TYPE_APP,
                              (t) -> qrCodeDcmService.fetchRuntimeQrCodeAppConfig(t),
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, QrCodeAppDcmHelper.createDcmRciConverter()),
                              (x, t) -> qrCodeDcmService.applyQrCodeAppConfig(x, t));
        registerTenantDcmCaps(PROVIDER_QR_CODE, CONFIG_TYPE_ENTITY_TYPE,
                              (t) -> qrCodeDcmService.fetchRuntimeQrCodeEntityTypeConfig(t),
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, QrCodeEntityTypeDcmHelper.createDcmRciConverter()),
                              (x, t) -> qrCodeDcmService.applyQrCodeEntityTypeConfig(x, t));
        registerTenantDcmCaps(PROVIDER_QR_CODE, CONFIG_TYPE_ENTITY_STYLE,
                              (t) -> qrCodeDcmService.fetchRuntimeQrCodeEntityStyleConfig(t),
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, QrCodeEntityStyleDcmHelper.createDcmRciConverter()),
                              (x, t) -> qrCodeDcmService.applyQrCodeEntityStyleConfig(x, t));
        registerTenantDcmCaps(PROVIDER_QR_CODE, CONFIG_TYPE_ENTITY_APP,
                              (t) -> qrCodeDcmService.fetchRuntimeQrCodeEntityAppConfig(t),
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, QrCodeEntityAppDcmHelper.createDcmRciConverter()),
                              (x, t) -> qrCodeDcmService.applyQrCodeEntityAppConfig(x, t));
    }

}
