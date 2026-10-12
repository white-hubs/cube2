package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimEventStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsEventGroupVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsHandlerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警事件可视化相关统计
 *
 * @author lanchun
 * @date 2023/11/15 11:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimEventStatisticsController {

    private final AlarmBimEventStatisticsService alarmBimEventStatisticsService;

    /**
     * 报警事件分类处理情况列表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/event-type/handler/search")
    public List<AlarmBimStatisticsHandlerVO> getEventTypeHandlerListStatistics(@RequestBody AlarmBimStatisticsQueryParam param) {
        return alarmBimEventStatisticsService.getEventTypeHandlerListStatistics(param);
    }

    /**
     * 报警事件分类分组总数统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/event-type/group-total/search")
    public List<AlarmBimStatisticsEventGroupVO> getEventTypeGroupTotalStatistics(@RequestBody AlarmBimStatisticsQueryParam param
                                                                                ) {
        return alarmBimEventStatisticsService.getEventTypeGroupTotalStatistics(param);
    }
}
