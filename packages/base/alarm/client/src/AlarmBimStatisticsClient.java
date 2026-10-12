package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.dto.AlarmBimDeviceStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBimStatisticsStatusDTO;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBimStatisticsTrendDTO;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordAreaCountDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmBimHandlerResultStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmBimStatisticsHandlerVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "alarm")
public interface AlarmBimStatisticsClient {

    /**
     * 获取报警对象数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/target-count")
    Integer getAlarmTargetCount(@SpringQueryMap AlarmBimStatisticsQueryParam param);

    /**
     * 报警对象趋势统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/target-trend")
    List<AlarmBimStatisticsTrendDTO> getAlarmTargetTrend(@Valid @RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 报警对象报警数量环比统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/target-chain")
    AlarmBimStatisticsStatusDTO getAlarmTargetStatistics(@Valid @RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 区域报警统计
     *
     * @param param 区域id集合
     *
     * @return 区域报警统计
     */
    @PostMapping("/alarm-bim-statistics/area-count")
    List<AlarmRecordAreaCountDTO> getAlarmAreaCount(@RequestBody AlarmRecordAreaCountParam param);

    /**
     * @Description 设备超温报警
     * @Author lrj
     */
    @PostMapping("/device-group-statistics")
    List<AlarmBimDeviceStatisticsDTO> getDeviceGroupStatistics(@RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * @Description 设备超温报警时间分组
     * @Author lrj
     */
    @PostMapping("/time-group-statistics")
    List<AlarmBimDeviceStatisticsDTO> getTimeGroupStatistics(@RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 报警结果统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-result/statistics")
    AlarmBimHandlerResultStatisticsVO getAlarmResultStatistics(@Valid @RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 报警事件分类处理情况列表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/event-type/handler/search")
    List<AlarmBimStatisticsHandlerVO> getEventTypeHandlerListStatistics(@Valid @RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 报警总处理超时率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/handler/timeout/rate")
    AlarmHandlerTimeoutRateStatisticsVO getAlarmHandlerTimeoutRate(@Valid @RequestBody AlarmBimStatisticsQueryParam param);

    /**
     * 按用户统计报警处理超时率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/users/handler/timeout/rate")
    List<AlarmUserHandlerTimeoutRateStatisticsVO> getAlarmUserHandlerTimeoutRate(@Valid @RequestBody AlarmBimStatisticsQueryParam param);
}
