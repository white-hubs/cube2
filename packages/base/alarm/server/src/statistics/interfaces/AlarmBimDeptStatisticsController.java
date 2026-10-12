package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimDeptStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警部门可视化相关统计
 *
 * @author lanchun
 * @date 2023/11/15 11:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimDeptStatisticsController {

    private final AlarmBimDeptStatisticsService alarmBimDeptStatisticsService;

    /**
     * 报警部门处理排名TOP
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/department-handler/top")
    public List<AlarmBimStatisticsTopChainVO> getDepartmentHandlerChainTop(@RequestBody AlarmBimStatisticsQueryParam param) {
        return alarmBimDeptStatisticsService.getDeptHandlerChainTopStatistics(param);
    }
}
