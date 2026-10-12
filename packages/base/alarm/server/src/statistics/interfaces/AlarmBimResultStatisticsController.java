package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimResultStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimHandlerResultStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 可视化报警处理结果统计接口
 *
 * @author lanchun
 * @date 2023/11/14 17:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimResultStatisticsController {

    private final AlarmBimResultStatisticsService alarmBimResultStatisticsService;

    /**
     * 可视化报警处理结果统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-result/statistics")
    public AlarmBimHandlerResultStatisticsVO getAlarmResultStatistics(@Valid @RequestBody AlarmBimStatisticsQueryParam param) {
        return alarmBimResultStatisticsService.getAlarmHandlerResultStatistics(param);
    }
}
