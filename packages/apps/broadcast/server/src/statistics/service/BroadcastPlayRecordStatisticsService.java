package com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.service;

import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayPlanMapper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastPlayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.StatisticsParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.BroadcastTemplatePlayStatisticsVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.RecordNumVo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.StatisticsPlayRecordVo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.TrendVo;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRecordStatisticsService {

    private final BroadcastPlayPlanMapper broadcastPlayPlanMapper;

    private final DeviceBimSceneStdClient deviceBimSceneClient;

    public List<StatisticsPlayRecordVo> getStatisticsPlayRecordVoTop(StatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        param.setIds(bimDeviceIds);
        List<StatisticsPlayRecordVo> statisticsPlayRecordVoTop = broadcastPlayPlanMapper.getStatisticsPlayRecordVoTop(param);
        List<String> collect = statisticsPlayRecordVoTop.stream().map(StatisticsPlayRecordVo::getName).collect(Collectors.toList());
        Period previousPeriod = getPreviousPeriod(param.getType(), param.getPeriod());
        param.setNames(collect);
        param.setPeriod(previousPeriod);
        List<StatisticsPlayRecordVo> previousStatisticsPlayRecordVoTop = broadcastPlayPlanMapper.getStatisticsPlayRecordVoTop(param);
        statisticsPlayRecordVoTop.forEach(s -> {
            Optional<StatisticsPlayRecordVo> first = previousStatisticsPlayRecordVoTop.stream()
                    .filter(statisticsPlayRecordVo -> statisticsPlayRecordVo.getName().equals(s.getName())).findFirst();
            if (first.isPresent() && 0 < first.get().getNum()) {
                BigDecimal bigDecimal = new BigDecimal((s.getNum() - first.get().getNum()) / first.get().getNum());
                s.setRatio(bigDecimal);
            }
        });
        return statisticsPlayRecordVoTop;
    }

    public RecordNumVo getRecordNum(StatisticsParam param) {
        if (Objects.nonNull(param.getSceneType())) {
            Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
            param.setIds(bimDeviceIds);
        }
        RecordNumVo recordNumVo = new RecordNumVo();
        Integer statisticsPlayRecordVoNum = broadcastPlayPlanMapper.getStatisticsPlayRecordVoNum(param);
        recordNumVo.setNum(statisticsPlayRecordVoNum);
        Period previousPeriod = getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Integer statisticsPlayRecordVoNum1 = broadcastPlayPlanMapper.getStatisticsPlayRecordVoNum(param);
        if (statisticsPlayRecordVoNum1 > 0) {
            BigDecimal bigDecimal = new BigDecimal((statisticsPlayRecordVoNum - statisticsPlayRecordVoNum1) / statisticsPlayRecordVoNum1);
            recordNumVo.setRatio(bigDecimal);
        }
        return recordNumVo;
    }

    public List<TrendVo> getRecordTrendNum(StatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        param.setIds(bimDeviceIds);
        String dateFormat = "";
        List<TrendVo> trendVos = new ArrayList<>();
        if (param.getType().equals("TODAY")) {
            dateFormat = "'%H'";
            param.setDateFormat(dateFormat);
            trendVos = broadcastPlayPlanMapper.getPlayRecordTrend(param);
        } else if (param.getType().equals("THIS_MONTH")) {
            dateFormat = "'%m.%d'";
            param.setDateFormat(dateFormat);
            trendVos = broadcastPlayPlanMapper.getPlayRecordTrend(param);
        } else if (param.getType().equals("THIS_YEAR")) {
            dateFormat = "'%Y-%m'";
            param.setDateFormat(dateFormat);
            trendVos = broadcastPlayPlanMapper.getPlayRecordTrend(param);
        } else if (param.getType().equals("THIS_WEEK")) {
            dateFormat = "'%Y-%m-%d'";
            param.setDateFormat(dateFormat);
            trendVos = broadcastPlayPlanMapper.getPlayRecordTrend(param);
            if (CollectionUtils.isNotEmpty(trendVos)) {
                for (TrendVo trendVo : trendVos) {
                    String s = dateToWeek(trendVo.getTimeFlag());
                    trendVo.setTimeFlag(s);
                }
            }
        }
        return trendVos;
    }

    public List<BroadcastPlayRecordPageVO> getTop10(StatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        param.setIds(bimDeviceIds);
        return broadcastPlayPlanMapper.getPlayRecordTop10(param);
    }

    public List<BroadcastTemplatePlayStatisticsVO> getTemplatePlayRecordStatistics(StatisticsParam param) {
        List<BroadcastTemplatePlayStatisticsVO> broadcastTemplatePlayStatisticsVOS = broadcastPlayPlanMapper.selectTemplatePlayRecordStatistics(param);
        Integer statisticsPlayRecordVoNum = broadcastPlayPlanMapper.getStatisticsPlayRecordVoNum(param);
        broadcastTemplatePlayStatisticsVOS.forEach(broadcastTemplatePlayStatisticsVO -> {
            if (0 < statisticsPlayRecordVoNum) {
                broadcastTemplatePlayStatisticsVO.setRatio(new BigDecimal(broadcastTemplatePlayStatisticsVO.getTotalNum())
                                                                   .divide(new BigDecimal(statisticsPlayRecordVoNum), 2, RoundingMode.HALF_UP)
                                                                   .multiply(new BigDecimal(100)).floatValue());
            }
        });
        return broadcastTemplatePlayStatisticsVOS;
    }

    public String dateToWeek(String datetime) {
        String[] weekDays = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
        LocalDateTime date = DateUtils.parse(datetime, "yyyy-MM-dd");
        int w = date.getDayOfWeek().ordinal(); // 指示一个星期中的某天。
        return weekDays[w];
    }

    public static Period getPreviousPeriod(String type, Period currentPeriod) {
        var endDate = LocalDateTime.now();
        //上期
        Period previousPeriod;
        if ("TODAY".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfDay(LocalDateTime.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.offsetDay(endDate, -1));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfWeek(LocalDateTime.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.offsetDay(endDate, -7));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfMonth(LocalDateTime.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.offsetMonth(endDate, -1));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(LocalDateTime.now()), -2);
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.offsetMonth(endDate, -2));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetDay(LocalDateTime.now(), -180);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.offsetDay(startDate, -1));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfYear(LocalDateTime.now());
            //上期
            previousPeriod = new Period(DateUtils.offset(startDate, ChronoUnit.YEARS, -1), DateUtils.offset(endDate, ChronoUnit.YEARS, -1));
        } else {
            //自定义时间差
            long currentTimeDifference = DateUtils.betweenMs(currentPeriod.getEnd(), currentPeriod.getBegin());
            //上期
            previousPeriod = new Period(DateUtils.offsetMillisecond(currentPeriod.getBegin(), -(int) currentTimeDifference), DateUtils.offsetMillisecond(currentPeriod.getEnd(), -(int) currentTimeDifference));
        }
        return previousPeriod;
    }
}
