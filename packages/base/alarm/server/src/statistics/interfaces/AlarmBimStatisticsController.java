package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimDeviceStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimSituationVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsDeviceTypeVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsLevelVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsRecordStatusVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsSituationVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsStatusVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordAreaCountVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 三维可视化统计接口
 *
 * @author lanchun
 * @date 2023/3/16 13:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimStatisticsController {

    private final AlarmBimStatisticsService alarmBimStatisticsService;

    /**
     * 三维可视化报警分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/records")
    public Pagination<AlarmRecordPageVO> getAlarmRecordPage(@Valid AlarmBimStatisticsQueryParam param) {
        Page<AlarmRecordPO> alarmRecordPage = this.alarmBimStatisticsService.getAlarmRecordPage(param);
        if (CollectionUtils.isEmpty(alarmRecordPage.getRecords())) {
            return Pagination.empty();
        }
        List<AlarmRecordPageVO> alarmRecordPageVOS = ObjectUtils.copying(alarmRecordPage.getRecords(), AlarmRecordPageVO.class);
        alarmRecordPageVOS.stream().forEach(alarmRecordPageVO -> {
            //待接收状态不显示处理人信息
            if (AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmRecordPageVO.getAlarmStatus()) {
                alarmRecordPageVO.setAlarmHandlerName(null);
            }
            alarmRecordPageVO.setAlarmStatusName(alarmRecordPageVO.getAlarmStatus().getDescription());
        });
        return PagingUtils.convert(alarmRecordPage, alarmRecordPageVOS);
    }

    /**
     * 报警统计
     * 报警处理占比,报警真实性统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics")
    public AlarmBimStatisticsVO getAlarmBimStatistics(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatistics(param);
    }

    /**
     * 报警级别统计
     * 报警级别统计报警数量(含环比)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/level")
    public List<AlarmBimStatisticsLevelVO> getAlarmBimStatisticsLevel(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsLevel(param);
    }

    /**
     * @Description 设备类型报警统计
     * @Author lrj
     */
    @GetMapping("/alarm-bim-statistics/device-type")
    public List<AlarmBimStatisticsDeviceTypeVO> getAlarmBimStatisticsDeviceType(@Valid AlarmBimStatisticsQueryParam param) {
        return alarmBimStatisticsService.getAlarmBimStatisticsDeviceType(param);
    }

    /**
     * 报警趋势
     * 报警趋势折线图统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/trend")
    public List<AlarmBimStatisticsTrendVO> getAlarmBimStatisticsTrend(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsTrend(param);
    }

    /**
     * 报警事件top5统计
     * 报警事件top5统计(含与上期环比)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/event/top-five")
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsEventTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsEventTopFive(param);
    }

    /**
     * 报警处理排名top5统计
     * 按人员处理排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/user-handler/top-five")
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsUserHandlerTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsUserHandlerTopFive(param);
    }

    /**
     * 报警区域top5统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/area/top-five")
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsAreaTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsAreaTopFive(param);
    }

    /**
     * 报警设备top5统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/device/top-five")
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsDeviceTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsDeviceTopFive(param);
    }

    /**
     * 报警记录状态统计(报警数,未处理数,已处理,超时报警数)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/record-status")
    public AlarmBimStatisticsRecordStatusVO getAlarmBimStatisticsRecordStatus(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsRecordStatus(param);
    }

    /**
     * 报警状态统计
     * 含报警总数,环比，报警状态分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/status")
    public AlarmBimStatisticsStatusVO getAlarmBimStatisticsStatus(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmBimStatisticsStatus(param);
    }

    /**
     * 获取报警对象数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/target-count")
    public Integer getAlarmTargetCount(@SpringQueryMap AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmTargetCount(param);
    }

    /**
     * 报警态势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/situation")
    public AlarmBimStatisticsSituationVO getAlarmSituation(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmBimStatisticsVO alarmBimStatistics = this.alarmBimStatisticsService.getAlarmBimStatistics(param);
        if (Objects.nonNull(alarmBimStatistics.getAlarmHandlerNum())) {
            alarmBimStatistics.setAlarmImpracticalNum(
                    alarmBimStatistics.getAlarmHandlerNum() - alarmBimStatistics.getAlarmActualNum());
        }
        //事件top3信息
        List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = this.alarmBimStatisticsService
                .getAlarmBimStatisticsEventTopFive(param);
        if (!CollectionUtils.isEmpty(alarmBimStatisticsTopChainVOS) && alarmBimStatisticsTopChainVOS.size() > 3) {
            List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainTopThreeVOS = this.alarmBimStatisticsService
                    .getAlarmBimStatisticsEventTopFive(param)
                    .stream().limit(3).collect(Collectors.toList());
            //其它事件
            Integer otherEventAlarmCount = alarmBimStatistics.getAlarmNum().intValue() -
                                           alarmBimStatisticsTopChainTopThreeVOS.stream()
                                                   .mapToInt(AlarmBimStatisticsTopChainVO::getAlarmNum)
                                                   .sum();
            //其它事件占比
            double otherEventAlarmRatio = 100 - alarmBimStatisticsTopChainTopThreeVOS.stream()
                    .mapToDouble(AlarmBimStatisticsTopChainVO::getRatio)
                    .sum();
            alarmBimStatisticsTopChainTopThreeVOS
                    .add(new AlarmBimStatisticsTopChainVO("otherEvent", "其他", otherEventAlarmCount, 0, otherEventAlarmRatio));
            return AlarmBimStatisticsSituationVO.builder()
                    .alarmBimStatistics(alarmBimStatistics)
                    .alarmBimStatisticsTopChains(alarmBimStatisticsTopChainTopThreeVOS)
                    .build();
        }
        return AlarmBimStatisticsSituationVO.builder()
                .alarmBimStatistics(alarmBimStatistics)
                .alarmBimStatisticsTopChains(alarmBimStatisticsTopChainVOS)
                .build();
    }

    /**
     * 报警类型态势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim-statistics/type-situation")
    public AlarmBimStatisticsSituationVO getAlarmTypeSituation(@Valid AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmTypeSituation(param);
    }

    /**
     * 报警对象趋势统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/target-trend")
    public List<AlarmBimStatisticsTrendVO> getAlarmTargetTrend(@Valid @RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmTargetTrend(param);
    }

    /**
     * 报警对象报警数量环比统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/target-chain")
    public AlarmBimStatisticsStatusVO getAlarmTargetStatistics(@Valid @RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmTargetStatistics(param);
    }

    /**
     * 区域报警统计
     *
     * @param param 区域id集合
     *
     * @return 区域报警统计
     */
    @PostMapping("/alarm-bim-statistics/area-count")
    public List<AlarmRecordAreaCountVO> getAlarmAreaCount(@RequestBody AlarmRecordAreaCountParam param) {
        return this.alarmBimStatisticsService.getAlarmAreaCount(param);
    }

    /**
     * @Description 设备超温报警
     * @Author lrj
     */
    @PostMapping("/device-group-statistics")
    public List<AlarmBimDeviceStatisticsVO> getDeviceGroupStatistics(@RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getDeviceGroupStatistics(param);
    }

    /**
     * @Description 设备超温报警时间分组
     * @Author lrj
     */
    @PostMapping("/time-group-statistics")
    public List<AlarmBimDeviceStatisticsVO> getTimeGroupStatistics(@RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getTimeGroupStatistics(param);
    }

    /**
     * @Description 应用分组统计
     * @Author lrj
     */
    @GetMapping("/application-group-statistics")
    public List<AlarmBimApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getApplicationGroupStatistics(param);
    }

    /**
     * 报警情况统计
     * 报警总数,处理率,响应率,正确率,超时率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/situation-ratio")
    public AlarmBimSituationVO getAlarmSituationRatio(@Valid @RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmSituationRatio(param);
    }

    /**
     * 报警总处理超时率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/handler/timeout/rate")
    public AlarmHandlerTimeoutRateStatisticsVO getAlarmHandlerTimeoutRate(@Valid @RequestBody AlarmBimStatisticsQueryParam param) {
        return this.alarmBimStatisticsService.getAlarmHandlerTimeoutRate(param);
    }

    /**
     * 按用户统计报警处理超时率
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-bim-statistics/users/handler/timeout/rate")
    public List<AlarmUserHandlerTimeoutRateStatisticsVO> getAlarmUserHandlerTimeoutRate(
            @Valid @RequestBody AlarmBimStatisticsQueryParam param
                                                                                       ) {
        return this.alarmBimStatisticsService.getAlarmUserHandlerTimeoutRate(param);
    }
}
