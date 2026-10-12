package com.guhecloud.cube.biz.emergency.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface EmergencyDcmService {

    List<RequestedConfigItem> fetchRuntimeEmergencyTypeConfig();

    List<RequestedConfigItem> applyEmergencyTypeConfig(List<RequestedConfigItem> diffs2Apply);

    List<RequestedConfigItem> fetchRuntimeEmergencyEventTypeConfig(Long tenant);

    List<RequestedConfigItem> applyEmergencyEventTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);

    List<RequestedConfigItem> fetchRuntimeEmergencyRuleConfig(Long tenant);

    List<RequestedConfigItem> applyEmergencyRuleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);
}
