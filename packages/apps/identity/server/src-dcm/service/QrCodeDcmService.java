package com.guhecloud.cube.srv.gen.identity.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface QrCodeDcmService {

    List<RequestedConfigItem> fetchRuntimeQrCodeStyleConfig(Long tenant);

    List<RequestedConfigItem> applyQrCodeStyleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeQrCodeAppConfig(Long tenant);

    List<RequestedConfigItem> applyQrCodeAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeQrCodeEntityTypeConfig(Long tenant);

    List<RequestedConfigItem> applyQrCodeEntityTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeQrCodeEntityStyleConfig(Long tenant);

    List<RequestedConfigItem> applyQrCodeEntityStyleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeQrCodeEntityAppConfig(Long tenant);

    List<RequestedConfigItem> applyQrCodeEntityAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);
}
