package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimDeviceStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsActualVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 可视化设备相关统计
 *
 * @author lanchun
 * @date 2023/11/17 9:27
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimDeviceStatisticsController {

    private final AlarmBimDeviceStatisticsService alarmBimDeviceStatisticsService;

    /**
     * 根据设备标签分组统计报警正确率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-device/statistics/device-tag/group/search")
    public List<AlarmBimStatisticsActualVO> getDeviceTagActualRatioStatistics(
            @Valid @RequestBody AlarmBimStatisticsQueryParam param
                                                                             ) {
        return this.alarmBimDeviceStatisticsService.getDeviceTagActualRatioStatistics(param);
    }
}
