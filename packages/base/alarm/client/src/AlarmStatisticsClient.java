package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.dto.AlarmTargetTypeStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmTargetTypeStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelTopStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordReportVO1;
import com.guhecloud.cube.base.alarm.client.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerRateTopVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "alarm")
public interface AlarmStatisticsClient {

    /**
     * 根据业务主键获取实际报警数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/business/total")
    Long getBusinessKeyActualTotal(@SpringQueryMap AlarmBusinessKeyStatisticsQueryParam param);

    /**
     * 报警对象类型数量统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-statistics/target-type/statistics")
    List<AlarmTargetTypeStatisticsDTO> getAlarmTargetTypeStatistics(@Valid @RequestBody AlarmTargetTypeStatisticsQueryParam param
                                                                   );

    /**
     * 报警总数环比率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/ring-ratio-analysis")
    AlarmBasicAnalysisStatisticsDTO getAlarmBasicAnalysisRingStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 获取指定应用报警数
     * 可传多个应用编码
     *
     * @param param
     *
     * @return
     */
    @GetMapping("alarm/record/application")
    AlarmRecordReportVO1 getAlarmRecordByApplication(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 按报警级别统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level")
    List<AlarmLevelStatisticsVO> getAlarmLevelStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警状态分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/status-group")
    List<AlarmStatusStatisticsVO> getAlarmStatusStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警级别处理率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level/handler-rate/statistics")
    List<AlarmLevelStatisticsVO> getAlarmLevelHandlerRateStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警平均处理效率
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/avg-handler-efficiency")
    AlarmAvgHandlerEfficiencyVO getAlarmAvgHandlerEfficiency(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警级别平均处理效率统计
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level/avg-handler-efficiency")
    List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiency(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level-handler-efficiency-analysis")
    List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiencyStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理效率排名前10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-top")
    List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopStatistics(@SpringQueryMap AlarmStatisticsQueryParam param
                                                                                           );

    /**
     * 用户报警处理效率排名倒数前10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-last-top")
    List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopStatistics(
            @SpringQueryMap AlarmStatisticsQueryParam param
                                                                                               );

    /**
     * 用户报警处理率排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user/handler-rate/top")
    List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateTopStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理率倒数排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user/handler-rate/last-top")
    List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateLastTopStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 区域报警数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/area")
    List<AlarmCurrencyStatisticsVO> getAlarmAreaStatistics(@Valid AlarmStatisticsQueryParam param);

    /**
     * 报警总数和处理数
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/alarm-number-analysis")
    AlarmBasicAnalysisStatisticsDTO getAlarmBasicAnalysisNumberStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

    /**
     * 报警数最高的等级、处理率最高的等级、处理效率最高的等级
     */
    @GetMapping("/alarm-statistics/level/top")
    AlarmLevelTopStatisticsVO getAlarmLevelTopStatistics(@SpringQueryMap AlarmStatisticsQueryParam param);

}
