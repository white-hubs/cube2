package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报警统计接口公共接口
 *
 * @author lanchun
 * @date 2025/1/3 15:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicAlarmStatisticsController {

    private final AlarmStatisticsController alarmStatisticsController;

    /**
     * 报警统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/public/alarm-statistics")
    public AlarmStatisticsVO publicGetAlarmStatistics(AlarmStatisticsQueryParam param) {
        return alarmStatisticsController.getAlarmStatistics(param);
    }
}
