package com.guhecloud.cube.base.alarm.dcm.service.impl;

import com.guhecloud.cube.base.alarm.dcm.service.AlarmDcmService;
import com.guhecloud.cube.base.alarm.dcm.support.AlarmReasonDcmHelper;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmReasonConfigService;
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
public class AlarmDcmServiceImpl implements AlarmDcmService {

    private final AlarmReasonConfigService alarmReasonConfigService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeAlarmReasonConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(alarmReasonConfigService, new AlarmReasonDcmHelper())));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyAlarmReasonConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, alarmReasonConfigService, new AlarmReasonDcmHelper()));
        return diffs2Apply;
    }
}
