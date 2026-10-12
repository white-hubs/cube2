package com.guhecloud.cube.base.alarm.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.base.alarm.dcm.service.AlarmDcmService;
import com.guhecloud.cube.base.alarm.dcm.support.AlarmReasonDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.base.alarm.dcm.support.AlarmDcmSupport.CONFIG_ALARM_REASON;
import static com.guhecloud.cube.base.alarm.dcm.support.AlarmDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.base.alarm.dcm.support.AlarmDcmSupport.PROVIDER_ALARM;

@Slf4j
@RestController
@RequestMapping("/alarm/dcm/config")
public class AlarmDcmController extends AbstractBaseDcmController {

    private final AlarmDcmService alarmDcmService;

    public AlarmDcmController(AlarmDcmService alarmDcmService) {
        this.alarmDcmService = alarmDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-alarm.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_ALARM, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_ALARM, CONFIG_ALARM_REASON,
                              alarmDcmService::fetchRuntimeAlarmReasonConfig,
                              (to, from, partial) -> DcmSupport
                                      .compare1dRCIs(to, from, partial, AlarmReasonDcmHelper.createDcmRciConverter()),
                              alarmDcmService::applyAlarmReasonConfig);
    }
}
