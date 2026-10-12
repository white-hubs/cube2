package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.application.datasync.controller;

import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.config.DingTalkConfig;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.application.datasync.DingTalkScheduleSyncJob;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
public class DingTalkScheduleController {

    private final DingTalkScheduleSyncJob dingTalkScheduleSyncJob;

    public DingTalkScheduleController(DingTalkScheduleSyncJob dingTalkScheduleSyncJob
                                     ) {this.dingTalkScheduleSyncJob = dingTalkScheduleSyncJob;}

    @PostMapping("/ding-talk/schedules/sync")
    public void triggerScheduleSync(@RequestParam Period period) {
        var dingTalkConfig = DingTalkConfig.getDingTalkConfig();
        if (Objects.isNull(dingTalkConfig)) {
            return;
        }
        var dingTalkUsers = dingTalkScheduleSyncJob.getDingTalkUsers();
        if (CollectionUtils.isEmpty(dingTalkUsers)) {
            log.info("获取钉钉的用户信息为空");
            return;
        }
        dingTalkScheduleSyncJob.processSchedule(dingTalkUsers, period.getBegin(), period.getEnd());
    }
}
