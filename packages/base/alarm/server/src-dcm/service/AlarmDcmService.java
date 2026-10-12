package com.guhecloud.cube.base.alarm.dcm.service;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

public interface AlarmDcmService {

    List<RequestedConfigItem> fetchRuntimeAlarmReasonConfig(Long tenant);

    List<RequestedConfigItem> applyAlarmReasonConfig(List<RequestedConfigItem> diffs2Apply, Long tenant);
}
