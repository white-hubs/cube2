package com.guhecloud.cube.srv.gen.identity.dcm.service.impl;

import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.srv.gen.identity.dcm.service.QrCodeDcmService;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeAppDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityAppDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityStyleDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeEntityTypeDcmHelper;
import com.guhecloud.cube.srv.gen.identity.dcm.support.QrCodeStyleDcmHelper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityApplicationService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityStyleService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityTypeService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeStyleCommandService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchRuntimeConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QrCodeDcmServiceImpl implements QrCodeDcmService {

    private QrcodeStyleCommandService qrcodeStyleCommandService;

    private QrcodeApplicationService qrcodeApplicationService;

    private QrcodeEntityTypeService qrcodeEntityTypeService;

    private QrcodeEntityStyleService qrcodeEntityStyleService;

    private QrcodeEntityApplicationService qrcodeEntityApplicationService;

    private ApplicationClient applicationClient;

    @Override
    public List<RequestedConfigItem> fetchRuntimeQrCodeStyleConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(qrcodeStyleCommandService, new QrCodeStyleDcmHelper())));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyQrCodeStyleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, qrcodeStyleCommandService, new QrCodeStyleDcmHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeQrCodeAppConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(qrcodeApplicationService, new QrCodeAppDcmHelper())));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyQrCodeAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, qrcodeApplicationService, new QrCodeAppDcmHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeQrCodeEntityTypeConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(qrcodeEntityTypeService, new QrCodeEntityTypeDcmHelper())));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyQrCodeEntityTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, qrcodeEntityTypeService, new QrCodeEntityTypeDcmHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeQrCodeEntityStyleConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(
                fetchRuntimeConfig(qrcodeEntityStyleService,
                                   new QrCodeEntityStyleDcmHelper(qrcodeStyleCommandService.list(), qrcodeEntityTypeService.list()))
                                                  ));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyQrCodeEntityStyleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(
                diffs2Apply,
                qrcodeEntityStyleService,
                new QrCodeEntityStyleDcmHelper(qrcodeStyleCommandService.list(), qrcodeEntityTypeService.list())));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeQrCodeEntityAppConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(
                fetchRuntimeConfig(qrcodeEntityApplicationService,
                                   new QrCodeEntityAppDcmHelper(this.applicationClient.getApplications(), qrcodeEntityTypeService.list()))
                                                  ));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyQrCodeEntityAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(
                diffs2Apply,
                qrcodeEntityApplicationService,
                new QrCodeEntityAppDcmHelper(this.applicationClient.getApplications(), qrcodeEntityTypeService.list())));
        return diffs2Apply;
    }
}
