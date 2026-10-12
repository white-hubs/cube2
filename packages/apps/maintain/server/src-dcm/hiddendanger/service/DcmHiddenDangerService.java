package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface DcmHiddenDangerService {

    List<RequestedConfigItem> fetchRuntimeHiddenDangerCategoryConfig(Long tenant);

    List<RequestedConfigItem> applyHiddenDangerCategoryConfig(List<RequestedConfigItem> requestedConfigItems, Long tenant);

    List<RequestedConfigItem> fetchRuntimeHiddenDangerTypeConfig(Long tenant);

    List<RequestedConfigItem> applyHiddenDangerTypeConfig(List<RequestedConfigItem> requestedConfigItems, Long tenant);
}
