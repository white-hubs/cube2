package com.guhecloud.cube.sn.meattrade.plugin.dbc.task;

import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TallyPushCleanTask {

    private final PorkArrangePushDataService porkArrangePushDataService;

    public TallyPushCleanTask(PorkArrangePushDataService porkArrangePushDataService
                             ) {this.porkArrangePushDataService = porkArrangePushDataService;}

    @JobAnnotation(name = "dbcMeatTradeTallyPublicClean", cron = "0 0 0 * * ?", desc = "滑轨-大白菜插件-推送的理货数据清理")
    public void cleanPushTask(String param) {
        SessionContextHolder.clearSessionContext();
        var now = DateUtils.beginOfDay(LocalDateTime.now());
        var dateTime = DateUtils.offsetDay(now, -30);
        porkArrangePushDataService.deletedExpirePush(dateTime);
    }
}
