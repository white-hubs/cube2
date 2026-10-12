package com.guhecloud.cube.biz.work.center.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface WorkDcmService {

    List<RequestedConfigItem> fetchRuntimeWorkTagConfig();

    List<RequestedConfigItem> applyWorkTagConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeWorkTagRefBusinessTypeConfig();

    List<RequestedConfigItem> applyWorkTagRefBusinessTypeConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeWorkBusinessUrConfig();

    List<RequestedConfigItem> applyWorkBusinessUrlConfig(List<RequestedConfigItem> diffs2Apply);
}
