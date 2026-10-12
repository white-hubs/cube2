package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmBasicAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerUserEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordDepartmentExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordStatisticsExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmResultStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusTotalByUserPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmLevelService;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusUserTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTargetTypeStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.ColdWaterAlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceEventRecordVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmLevelTopStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordReportVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordReportVO1;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmTargetTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmStatisticsServiceImpl implements AlarmStatisticsService {

    private final AlarmRecordMapper alarmRecordMapper;

    private final SatAlarmLevelService satAlarmLevelService;

    private final UserClient userClient;

    private final OrganizationClient organizationClient;

    /**
     * 报警数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public Long getAlarmTotal(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmTotal(param);
    }

    /**
     * 处理的报警数
     *
     * @param param
     *
     * @return
     */
    @Override
    public Long getAlarmHandlerTotal(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmHandlerTotal(param);
    }

    /**
     * 按报警状态统计数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmStatusStatisticsVO> getAlarmStatusStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmStatusStatisticsVO> alarmStatusStatisticsVOS = this.alarmRecordMapper.selectAlarmStatusStatistics(param);
        for (AlarmStatusEnum alarmStatusEnum : AlarmStatusEnum.values()) {
            AlarmStatusStatisticsVO alarmStatusStatisticsVO = alarmStatusStatisticsVOS.stream()
                    .filter(statusStatisticsVO -> alarmStatusEnum
                            .name()
                            .equals(statusStatisticsVO.getAlarmStatus()))
                    .findFirst().orElse(null);
            //补充查询结果中不存在的报警处理状态
            if (Objects.isNull(alarmStatusStatisticsVO)) {
                alarmStatusStatisticsVO = new AlarmStatusStatisticsVO();
                alarmStatusStatisticsVO.setAlarmStatus(alarmStatusEnum.name());
                alarmStatusStatisticsVO.setAlarmStatusName(alarmStatusEnum.getDescription());
                alarmStatusStatisticsVO.setAlarmNum(0L);
                alarmStatusStatisticsVOS.add(alarmStatusStatisticsVO);
            } else {
                alarmStatusStatisticsVO.setAlarmStatusName(alarmStatusEnum.getDescription());
            }
        }
        //查询超时未处理数量
        Long timeoutUnHandlerCount = this.alarmRecordMapper.selectAlarmTimeoutUnHandlerCount(param);
        alarmStatusStatisticsVOS.add(new AlarmStatusStatisticsVO("TIME_OUT_UN_HANDLER", "超时未处理", timeoutUnHandlerCount));
        return alarmStatusStatisticsVOS;
    }

    /**
     * 按报警结果统计数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmResultStatisticsVO> getAlarmResultStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmResultStatisticsVO> alarmResultStatisticsVOS = this.alarmRecordMapper.selectAlarmResultStatistics(param);
        for (AlarmHandlerResultEnum alarmHandlerResultEnum : AlarmHandlerResultEnum.values()) {
            AlarmResultStatisticsVO alarmResultStatisticsVO = alarmResultStatisticsVOS.stream().filter(resultStatisticsVO ->
                                                                                                               alarmHandlerResultEnum ==
                                                                                                               resultStatisticsVO
                                                                                                                       .getAlarmHandlerResult())
                    .findFirst().orElse(null);
            //补充查询结果中不存在的报警处理结果
            if (Objects.isNull(alarmResultStatisticsVO)) {
                alarmResultStatisticsVO = new AlarmResultStatisticsVO();
                alarmResultStatisticsVO.setAlarmHandlerResult(alarmHandlerResultEnum);
                alarmResultStatisticsVO.setAlarmHandlerResultName(alarmHandlerResultEnum.getDescription());
                alarmResultStatisticsVO.setAlarmNum(0L);
                alarmResultStatisticsVOS.add(alarmResultStatisticsVO);
            } else {
                alarmResultStatisticsVO.setAlarmHandlerResultName(alarmHandlerResultEnum.getDescription());
            }
        }
        return alarmResultStatisticsVOS;
    }

    /**
     * 按报警类型统计数量并排名
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmTypeStatisticsVO> getAlarmTypeRankStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmTypeStatisticsVO> alarmTypeStatisticsVOS = this.alarmRecordMapper.selectAlarmTypeStatistics(param);
        //按报警数量倒叙返回
        return alarmTypeStatisticsVOS.stream().sorted(Comparator.comparing(AlarmTypeStatisticsVO::getAlarmNum).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 报警趋势统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmTrendStatisticsVO> getAlarmTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmTrendStatistics(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        if (startDate != null && endDate != null) {
            if (!"TODAY".equals(param.getType())) {
                endDate = DateUtils.endOfDay(endDate);
            }
            List<AlarmTrendStatisticsVO> finalDatas = new ArrayList<>();
            for (LocalDateTime start = startDate;
                 start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
                String date = DateUtils.format(start, param.getDateString());
                String viewDate = DateUtils.format(start, param.getViewDateString());
                AlarmTrendStatisticsVO trendStatVO = alarmTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                        .findAny().orElse(null);
                finalDatas.add(trendStatVO == null ? new AlarmTrendStatisticsVO(0L, viewDate)
                                                   : new AlarmTrendStatisticsVO(trendStatVO.getAlarmNum(), viewDate));
            }
            return finalDatas;
        } else {
            return alarmTrendStatisticsVOS;
        }
    }

    /**
     * 报警总数与已完成趋势统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmTrendStatisticsVO> getAlarmConfirmedTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        //总数趋势
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmTrendStatistics(param);
        //已处理趋势
        List<AlarmTrendStatisticsVO> alarmConfirmedTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmConfirmedTrendStatistics(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        endDate = DateUtils.endOfDay(endDate);
        List<AlarmTrendStatisticsVO> finalDatas = new ArrayList<>();
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, param.getDateString());
            String viewDate = DateUtils.format(start, param.getViewDateString());
            AlarmTrendStatisticsVO trendStatVO = alarmTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                    .findAny().orElse(null);
            AlarmTrendStatisticsVO alarmConfirmedTrendStatVO = alarmConfirmedTrendStatisticsVOS.stream().filter(t -> t.getPeriod()
                            .equals(date))
                    .findAny().orElse(null);
            AlarmTrendStatisticsVO alarmTrendStatisticsVO = new AlarmTrendStatisticsVO(viewDate);
            alarmTrendStatisticsVO.setAlarmNum(Objects.nonNull(trendStatVO) ? trendStatVO.getAlarmNum() : 0);
            alarmTrendStatisticsVO.setHandlerNum(
                    Objects.nonNull(alarmConfirmedTrendStatVO) ? alarmConfirmedTrendStatVO.getAlarmNum() : 0);
            finalDatas.add(alarmTrendStatisticsVO);
        }
        return finalDatas;

    }

    /**
     * 按报警对象类型统计数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmTypeStatisticsVO> getAlarmTargetTypeStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmTargetTypeStatistics(param);
    }

    /**
     * 按报警级别统计数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmLevelStatisticsVO> getAlarmLevelStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmLevelStatistics(param);
    }

    @Override
    public List<AlarmCurrencyStatisticsVO> getAlarmEventGroupStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmEventAnalysisStatistics(param);
    }

    @Override
    public List<AlarmCurrencyStatisticsVO> getAlarmEventGroupStatisticsTopTen(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmEventAnalysisStatisticsTop(param, 10);
    }

    @Override
    public List<AlarmCurrencyStatisticsVO> getAlarmAreaGroupStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmAreaAnalysisStatistics(param);
    }

    @Override
    public List<AlarmCurrencyStatisticsVO> getAlarmDeviceGroupTopTenStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectAlarmDeviceGroupTopStatistics(param, 10);
    }

    /**
     * 报警处理分析
     *
     * @param param
     *
     * @return
     */
    @Override
    public AlarmHandlerAnalysisStatisticsVO getAlarmHandlerAnalysisStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmHandlerCurrencyStatisticsVO> alarmHandlerCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmHandlerAnalysisStatistics(param);
        //报警总数
        Integer alarmTotal = this.alarmRecordMapper.selectAlarmTotal(param).intValue();
        //已处理的报警总数
        Integer alarmHandlerTotalCount = alarmHandlerCurrencyStatisticsVOS.stream()
                .mapToInt(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum)
                .sum();
        Integer alarmUnresolvedHandlerTotalCount = alarmTotal - alarmHandlerTotalCount;
        if (CollectionUtils.isEmpty(alarmHandlerCurrencyStatisticsVOS)) {
            return AlarmHandlerAnalysisStatisticsVO.builder()
                    .alarmHandlerTotalNum(alarmHandlerTotalCount)
                    .alarmUnresolvedHandlerTotalNum(alarmUnresolvedHandlerTotalCount)
                    .build();
        }
        //按处理数量倒序排序
        alarmHandlerCurrencyStatisticsVOS = alarmHandlerCurrencyStatisticsVOS.stream().sorted(Comparator
                                                                                                      .comparing(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum)
                                                                                                      .reversed())
                .collect(Collectors.toList());
        //top10处理人
        List<AlarmHandlerCurrencyStatisticsVO> topTenUserHandlerStatisticsVOS = alarmHandlerCurrencyStatisticsVOS.stream()
                .filter(alarmHandlerCurrencyStatisticsVO ->
                                alarmHandlerCurrencyStatisticsVO.getAlarmHandlerTotalNum() >
                                0)
                .limit(10)
                .sorted(Comparator.comparing(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum))
                .collect(Collectors.toList());
        //倒数top10处理人
        List<AlarmHandlerCurrencyStatisticsVO> lastTopTenUserHandlerStatisticsVOS = alarmHandlerCurrencyStatisticsVOS.stream()
                .filter(alarmHandlerCurrencyStatisticsVO ->
                                alarmHandlerCurrencyStatisticsVO.getAlarmHandlerTotalNum() >
                                0)
                .sorted(Comparator.comparing(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum))
                .limit(10)
                .sorted(Comparator.comparing(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum))
                .collect(Collectors.toList());
        //top前3处理人名称
        String topHandlerUserNames = topTenUserHandlerStatisticsVOS.stream()
                .map(alarmHandlerCurrencyStatisticsVO -> alarmHandlerCurrencyStatisticsVO.getName())
                .limit(3).collect(Collectors.joining("、"));
        return AlarmHandlerAnalysisStatisticsVO.builder()
                .alarmHandlerTotalNum(alarmHandlerTotalCount)
                .alarmUnresolvedHandlerTotalNum(alarmUnresolvedHandlerTotalCount)
                .alarmHandlerTopThreeUserNames(topHandlerUserNames)
                .topTenUserHandlerStatisticsVOS(topTenUserHandlerStatisticsVOS)
                .lastTopTenUserHandlerStatisticsVOS(lastTopTenUserHandlerStatisticsVOS)
                .build();
    }

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    @Override
    public List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiencyStatistics(AlarmStatisticsQueryParam param
                                                                                                 ) {
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> alarmLevelHandlerUserEfficiencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmLevelHandlerUserEfficiencyStatistics(param);
        //报警级别
        List<SatAlarmLevel> satAlarmLevels = this.satAlarmLevelService.list(Wrappers.<SatAlarmLevel>lambdaQuery()
                                                                                    .eq(Objects.nonNull(param.getAlarmLevelId()), SatAlarmLevel::getId, param
                                                                                            .getAlarmLevelId()));
        List<AlarmLevelHandlerEfficiencyStatisticsVO> statisticsVOS = Collections.synchronizedList(new ArrayList<>(satAlarmLevels.size()));
        satAlarmLevels.parallelStream().forEach(satAlarmLevel -> {
            //耗时最长的为效率最低
            Optional<AlarmLevelHandlerUserEfficiencyStatisticsVO> lowEfficiency = alarmLevelHandlerUserEfficiencyStatisticsVOS
                    .stream()
                    .filter(alarmLevelHandlerUserEfficiencyStatisticsVO -> satAlarmLevel.getId()
                            .equals(alarmLevelHandlerUserEfficiencyStatisticsVO.getAlarmLevelId()))
                    .max(Comparator.comparing(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessMinutes));
            //耗时最短的为效率最高
            Optional<AlarmLevelHandlerUserEfficiencyStatisticsVO> highEfficiency = alarmLevelHandlerUserEfficiencyStatisticsVOS
                    .stream()
                    .filter(alarmLevelHandlerUserEfficiencyStatisticsVO -> satAlarmLevel.getId()
                            .equals(alarmLevelHandlerUserEfficiencyStatisticsVO.getAlarmLevelId()))
                    .min(Comparator.comparing(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessMinutes));
            statisticsVOS.add(AlarmLevelHandlerEfficiencyStatisticsVO.builder()
                                      .id(satAlarmLevel.getId())
                                      .code(satAlarmLevel.getCode())
                                      .name(satAlarmLevel.getName())
                                      .sort(satAlarmLevel.getSort())
                                      .highEfficiencyUserName(
                                              highEfficiency.map(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAlarmHandlerName)
                                                      .orElse(null))
                                      .highEfficiencyUserAvgDuration(
                                              highEfficiency.map(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessSeconds)
                                                      .orElse(0L))
                                      .lowEfficiencyUserName(
                                              lowEfficiency.map(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAlarmHandlerName)
                                                      .orElse(null))
                                      .lowEfficiencyUserAvgDuration(
                                              lowEfficiency.map(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessSeconds)
                                                      .orElse(0L)
                                                                   )
                                      .build());
        });
        return statisticsVOS;
    }

    @Override
    public List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopTenStatistics(
            AlarmStatisticsQueryParam param
                                                                                                          ) {
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> alarmLevelHandlerUserEfficiencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmLevelHandlerUserEfficiencyStatistics(param);
        if (CollectionUtils.isEmpty(alarmLevelHandlerUserEfficiencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //top前十处理用户
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> tenAlarmLevelHandlerUserEfficiencyStatisticsVOS = alarmLevelHandlerUserEfficiencyStatisticsVOS
                .stream()
                .sorted(Comparator.comparing(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessMinutes))
                .limit(10)
                .collect(Collectors.toList());
        return tenAlarmLevelHandlerUserEfficiencyStatisticsVOS;
    }

    @Override
    public List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopTenStatistics(
            AlarmStatisticsQueryParam param
                                                                                                              ) {
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> alarmLevelHandlerUserEfficiencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmLevelHandlerUserEfficiencyStatistics(param);
        if (CollectionUtils.isEmpty(alarmLevelHandlerUserEfficiencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //top倒数前十处理用户
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> lastTenAlarmLevelHandlerUserEfficiencyStatisticsVOS = alarmLevelHandlerUserEfficiencyStatisticsVOS
                .stream()
                .sorted(Comparator.comparing(AlarmLevelHandlerUserEfficiencyStatisticsVO::getAvgProcessMinutes).reversed())
                .limit(10)
                .collect(Collectors.toList());
        return lastTenAlarmLevelHandlerUserEfficiencyStatisticsVOS;
    }

    @Override
    public AlarmBasicAnalysisStatisticsVO getAlarmBasicAnalysisRingStatistics(AlarmStatisticsQueryParam param) {
        //本期报警总数
        Long currentAlarmCount = this.alarmRecordMapper.selectAlarmTotal(param);
        //上期时间
        Period period = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        //上期报警总数
        param.setPeriod(period);
        Long previousAlarmCount = this.alarmRecordMapper.selectAlarmTotal(param);
        //同期时间
        Period samePeriod = DateUnitUtils.getSamePeriod(param.getPeriod());
        //同期报警总数
        param.setPeriod(samePeriod);
        Long samePreviousAlarmCount = this.alarmRecordMapper.selectAlarmTotal(param);
        float monthOnMonthRatio = MathUtils.calculateMonthOnMonthRatio(currentAlarmCount, samePreviousAlarmCount);
        double ringRatio = 0;
        if (0 < previousAlarmCount) {
            ringRatio = new BigDecimal(currentAlarmCount - previousAlarmCount)
                    .divide(new BigDecimal(previousAlarmCount), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).doubleValue();
        }
        DecimalFormat b = new DecimalFormat("0.00");
        String format = b.format(ringRatio);
        return AlarmBasicAnalysisStatisticsVO.builder()
                .alarmNum(currentAlarmCount)
                .ringRatio(format)
                .sameRatio(b.format(monthOnMonthRatio))
                .build();
    }

    @Override
    public AlarmTrendAnalysisStatisticsVO getAlarmTrendAnalysisStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmTrendAnalysisStatistics(param);
        LocalDateTime startDate = DateUtils.beginOfDay(DateUtils.now());
        LocalDateTime endDate = DateUtils.endOfDay(DateUtils.now());
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsRsVOS = new ArrayList<>();
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, ChronoUnit.MINUTES, 30)) {
            String date = DateUtils.format(start, "HH:mm");
            AlarmTrendStatisticsVO alarmTrendStatisticsVO = alarmTrendStatisticsVOS.stream()
                    .filter(trendStatisticsVO -> trendStatisticsVO
                            .getPeriod().equals(date)).findAny()
                    .orElse(null);
            alarmTrendStatisticsRsVOS.add(
                    alarmTrendStatisticsVO == null ? new AlarmTrendStatisticsVO(0L, date) : alarmTrendStatisticsVO);
        }
        AlarmTrendAnalysisStatisticsVO alarmTrendAnalysisStatisticsVO = AlarmTrendAnalysisStatisticsVO.builder()
                .alarmTrendStatisticsVOS(alarmTrendStatisticsRsVOS)
                .build();
        if (!CollectionUtils.isEmpty(alarmTrendStatisticsVOS)) {
            Optional<AlarmTrendStatisticsVO> opAlarmTrendStatisticsVO = alarmTrendStatisticsRsVOS.stream()
                    .max(Comparator.comparing(AlarmTrendStatisticsVO::getAlarmNum));
            opAlarmTrendStatisticsVO.ifPresent(alarmTrendStatisticsVO -> {
                //如果是23:30,那么范围取23:00 - 00:00
                if (alarmTrendStatisticsRsVOS.indexOf(alarmTrendStatisticsVO) == alarmTrendStatisticsRsVOS.size() - 1) {
                    alarmTrendAnalysisStatisticsVO.setTopTimeSlot(
                            alarmTrendStatisticsVO.getPeriod() + "-" + alarmTrendStatisticsRsVOS.get(0).getPeriod());
                } else {
                    alarmTrendAnalysisStatisticsVO.setTopTimeSlot(alarmTrendStatisticsVO.getPeriod() + "-" +
                                                                  alarmTrendStatisticsRsVOS
                                                                          .get(alarmTrendStatisticsRsVOS.indexOf(alarmTrendStatisticsVO) +
                                                                               1).getPeriod());
                }
            });
        }
        return alarmTrendAnalysisStatisticsVO;
    }

    @Override
    public List<AlarmTargetTypeStatisticsVO> getAlarmTargetTypeStatistics(AlarmTargetTypeStatisticsQueryParam param) {
        if (CollectionUtils.isEmpty(param.getAlarmTargetTypeId())) {
            return Collections.emptyList();
        }
        return this.alarmRecordMapper.selectInnerAlarmTargetTypeStatistics(param);
    }

    @Override
    public List<AlarmApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param) {
        return alarmRecordMapper.getApplicationGroupTotalStatistics(param);
    }

    @Override
    public List<AlarmDeviceStatusStatisticsTrendVO> getColdWaterAlarmTrendAnalysisStatistics(AlarmStatisticsQueryParam param) {
        String dateFormat = "%Y-%m-%d";
        LocalDateTime from = param.getPeriod().getBegin();
        LocalDateTime to = param.getPeriod().getEnd();
        int period;
        ChronoUnit dateField;
        String format = "";
        if (param.getType().equals("YEAR")) {
            dateFormat = "%Y";
            int fromYear = from.getYear();
            int toYear = to.getYear();
            period = toYear - fromYear;
            dateField = ChronoUnit.YEARS;
            format = "yyyy";
        } else if (param.getType().equals("MONTH")) {
            dateFormat = "%Y-%m";
            int fromMonth = from.getMonthValue();
            int toMonth = to.getMonthValue();
            period = toMonth - fromMonth;
            dateField = ChronoUnit.MONTHS;
            format = "yyyy-MM";
        } else {
            int fromDay = from.getDayOfMonth();
            int toDay = to.getDayOfMonth();
            period = toDay - fromDay;
            dateField = ChronoUnit.DAYS;
            format = "yyyy-MM-dd";
        }
        List<AlarmDeviceStatusStatisticsTrendVO> coldWaterAlarmTrendAnalysisStatistics = alarmRecordMapper
                .getColdWaterAlarmTrendAnalysisStatistics(param, dateFormat);
        Map<String, AlarmDeviceStatusStatisticsTrendVO> collect = coldWaterAlarmTrendAnalysisStatistics.stream()
                .collect(Collectors.toMap(AlarmDeviceStatusStatisticsTrendVO::getTimeFlag, s -> s));
        List<AlarmDeviceStatusStatisticsTrendVO> objects = new ArrayList<>();
        for (int i = 0; i <= period; i++) {
            LocalDateTime offset = DateUtils.offset(param.getPeriod().getBegin(), dateField, i);
            String s = DateUtils.format(offset, format);
            AlarmDeviceStatusStatisticsTrendVO alarmDeviceStatusStatisticsTrendVO = new AlarmDeviceStatusStatisticsTrendVO();
            alarmDeviceStatusStatisticsTrendVO.setTimeFlag(s);
            AlarmDeviceStatusStatisticsTrendVO alarmDeviceStatusStatisticsTrendVO1 = collect.get(s);
            alarmDeviceStatusStatisticsTrendVO.setNum(
                    Objects.isNull(alarmDeviceStatusStatisticsTrendVO1) ? 0 : alarmDeviceStatusStatisticsTrendVO1.getNum());
            objects.add(alarmDeviceStatusStatisticsTrendVO);
        }
        return objects;
    }

    /**
     * 按报警级别进行趋势统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmLevelTrendStatisticsVO> getAlarmLevelTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        List<AlarmLevelTrendStatisticsVO> levelTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmLevelTrendStatistics(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        List<AlarmLevelTrendStatisticsVO> trendStatisticsVOS = new ArrayList<>();
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, param.getDateString());
            String viewDate = DateUtils.format(start, param.getViewDateString());
            AlarmLevelTrendStatisticsVO alarmLevelTrendStatisticsVO = levelTrendStatisticsVOS.stream()
                    .filter(levelTrendStatisticsVO -> levelTrendStatisticsVO
                            .getPeriod().equals(date))
                    .findAny().orElse(null);
            trendStatisticsVOS.add(
                    alarmLevelTrendStatisticsVO == null ? new AlarmLevelTrendStatisticsVO(Collections.emptyList(), viewDate)
                                                        : new AlarmLevelTrendStatisticsVO(alarmLevelTrendStatisticsVO
                                                                                                  .getAlarmLevelStatisticsVOS(), viewDate));
        }
        return trendStatisticsVOS;
    }

    /**
     * 业务主键获取实际报警数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public Long getBusinessKeyActualTotal(AlarmBusinessKeyStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectBusinessKeyActualTotal(param);
    }

    /**
     * 报警事件按状态统计分页列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public Page<AlarmEventStatusStatisticsPageVO> getAlarmStatusTotalPage(AlarmStatusTotalQueryParam param) {
        return this.alarmRecordMapper.selectAlarmStatusTotalPage(PagingUtils.convert(param), param);
    }

    @Override
    public Page<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalPage(AlarmDeviceStatusTotalQueryParam param) {
        return this.alarmRecordMapper.selectDeviceAlarmStatusTotalPage(PagingUtils.convert(param), param);
    }

    /**
     * 报警事件按状态统计列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmEventStatusStatisticsPageVO> getAlarmStatusTotalList(AlarmStatusTotalQueryParam param) {
        return this.alarmRecordMapper.selectAlarmStatusTotalList(param);
    }

    @Override
    public List<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalList(AlarmDeviceStatusTotalQueryParam param) {
        return this.alarmRecordMapper.selectDeviceAlarmStatusTotalList(param);
    }

    /**
     * 按用户分组统计报警处理状态分页列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public Page<AlarmStatusTotalByUserPageVO> getAlarmStatusTotalByUserPage(AlarmStatusUserTotalQueryParam param) {
        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmRecordMapper
                .selectAlarmStatusTotalByUserPage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return alarmStatusTotalByUserPageVOPage;
        }
        extendUserAlarmStatusVO(alarmStatusTotalByUserPageVOPage.getRecords(), param);
        return alarmStatusTotalByUserPageVOPage;
    }

    /**
     * 按用户分组统计报警处理状态列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmStatusTotalByUserPageVO> getAlarmStatusTotalByUserList(AlarmStatusUserTotalQueryParam param) {
        List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserList = this.alarmRecordMapper.selectAlarmStatusTotalByUserList(param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserList)) {
            return Collections.emptyList();
        }
        extendUserAlarmStatusVO(alarmStatusTotalByUserList, param);
        return alarmStatusTotalByUserList;
    }

    private void extendUserAlarmStatusVO(List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserList,
                                         AlarmStatusUserTotalQueryParam param
                                        ) {
        Set<Long> departmentIds = alarmStatusTotalByUserList.stream().map(AlarmStatusTotalByUserPageVO::getDepartmentId)
                .collect(Collectors.toSet());
        param.setDepartmentIds(departmentIds);
        Map<String, AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = this.alarmRecordMapper
                .selectAlarmDepartmentCount(param)
                .stream().collect(Collectors.toMap(AlarmBimStatisticsTopChainVO::getId, v -> v));
        Set<Long> userIds = alarmStatusTotalByUserList.stream()
                .map(alarmStatusTotalByUserPageVO -> alarmStatusTotalByUserPageVO.getUserId())
                .collect(Collectors.toSet());
        Map<Long, UserDTO> userDTOMap = this.userClient.listUserByIds(userIds).stream()
                .collect(Collectors.toMap(UserDTO::getId, v -> v));
        alarmStatusTotalByUserList.stream().forEach(alarmStatusTotalByUserPageVO -> {
            if (Objects.nonNull(alarmStatusTotalByUserPageVO.getHandlerNum()) &&
                0 < alarmStatusTotalByUserPageVO.getHandlerNum()) {
                //计算耗时
                //平均耗时秒数
                long averageSeconds = new BigDecimal(alarmStatusTotalByUserPageVO.getProcessingTime())
                        .divide(new BigDecimal(alarmStatusTotalByUserPageVO.getHandlerNum()), BigDecimal.ROUND_UP).longValue();
                //分钟数转为秒数
                alarmStatusTotalByUserPageVO.setAverageProcessing(MathUtils.dateConvertRemark(averageSeconds));
            } else {
                alarmStatusTotalByUserPageVO.setAverageProcessing(MathUtils.dateConvertRemark(0));
                alarmStatusTotalByUserPageVO.setHandlerRate("0.00%");
            }
            //查询用户信息
            UserDTO userDTO = userDTOMap.get(alarmStatusTotalByUserPageVO.getUserId());
            if (Objects.nonNull(userDTO) && Objects.nonNull(userDTO.getOrganization())) {
                alarmStatusTotalByUserPageVO.setDepartmentId(userDTO.getOrganization().getId());
                alarmStatusTotalByUserPageVO.setDepartmentName(userDTO.getOrganization().getName());
                AlarmBimStatisticsTopChainVO alarmBimStatisticsTopChainVO = alarmBimStatisticsTopChainVOS.get(alarmStatusTotalByUserPageVO
                                                                                                                      .getDepartmentId()
                                                                                                                      .toString());
                if (Objects.nonNull(alarmBimStatisticsTopChainVO)) {
                    alarmStatusTotalByUserPageVO.setDepartmentTotalNum(alarmBimStatisticsTopChainVO.getAlarmNum().longValue());
                }
            }
        });
    }

    @Override
    public Pagination<AlarmDeviceEventRecordVO> getColdWaterAlarmEventRecord(ColdWaterAlarmRecordQueryParam param) {
        Page<AlarmDeviceEventRecordVO> coldWaterAlarmEventRecord = alarmRecordMapper
                .getColdWaterAlarmEventRecord(PagingUtils.convert(param), param);
        return PagingUtils.convert(coldWaterAlarmEventRecord);
    }

    @Override
    public AlarmRecordReportVO1 getAlarmRecordByApplication(AlarmStatisticsQueryParam param) {
        List<AlarmRecordReportVO> alarmRecordReportVOS = alarmRecordMapper.selectAlarmRecordByApplication(param);
        //用户分组：
        List<AlarmRecordReportVO> userAlarmRecordReportVOS = alarmRecordMapper.selectAlarmRecordByApplicationForUser(param);
        if (!CollectionUtils.isEmpty(userAlarmRecordReportVOS)) {
            List<String> departmentIds = userAlarmRecordReportVOS.stream()
                    .filter(t -> StringUtils.isNotBlank(t.getDepartmentId()))
                    .map(AlarmRecordReportVO::getDepartmentId)
                    .collect(Collectors.toList());
            param.setDepartmentIds(departmentIds);
            Map<String, Long> departmentMap = alarmRecordMapper.selectAlarmRecordByApplicationGroupDepartment(param)
                    .stream()
                    .collect(Collectors.toMap(AlarmRecordReportVO::getDepartmentId, AlarmRecordReportVO::getAlarmNum, (k1, k2) -> k1));
            for (AlarmRecordReportVO userAlarmRecordReportVO : userAlarmRecordReportVOS) {
                userAlarmRecordReportVO.setDepartmentNum(departmentMap.getOrDefault(userAlarmRecordReportVO.getDepartmentId(), 0L));
            }
        }
        //部门分组：
        List<AlarmRecordReportVO> departmentAlarmRecordReportVOS = alarmRecordMapper.selectAlarmRecordByApplicationForDepartment(param);
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod("THIS_MONTH", param.getPeriod());
        param.setPeriod(previousPeriod);
        List<AlarmRecordReportVO> previousAlarmRecord = alarmRecordMapper.selectAlarmRecordRingAndSameByApplication(param);
        if (CollectionUtils.isEmpty(previousAlarmRecord)) {
            previousAlarmRecord = new ArrayList<>();
        }
        //同比
        Period samePeriod = DateUnitUtils.getSamePeriod(param.getPeriod());
        param.setPeriod(samePeriod);
        List<AlarmRecordReportVO> sameAlarmRecord = alarmRecordMapper.selectAlarmRecordRingAndSameByApplication(param);
        if (CollectionUtils.isEmpty(sameAlarmRecord)) {
            sameAlarmRecord = new ArrayList<>();
        }
        return AlarmRecordReportVO1.builder()
                .alarmRecordReportVOS(alarmRecordReportVOS)
                .userAlarmRecordReportVOS(userAlarmRecordReportVOS)
                .departmentAlarmRecordReportVOS(departmentAlarmRecordReportVOS)
                .ringRecordReportVOS(previousAlarmRecord)
                .sameRecordReportVOS(sameAlarmRecord)
                .build();
    }

    @Override
    public Page<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReport(AlarmStatusUserTotalQueryParam param) {

        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmRecordMapper
                .selectAlarmRecordForSafetyReport(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return alarmStatusTotalByUserPageVOPage;
        }
        buildAverageTimeSpentInSeconds(alarmStatusTotalByUserPageVOPage.getRecords(), param);
        return alarmStatusTotalByUserPageVOPage;
    }

    @Override
    public void getAlarmRecordForSafetyReportExport(AlarmStatusUserTotalQueryParam param) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            String excelName = URLEncoder.encode("安全报表报警统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());

            List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserList = this.alarmRecordMapper.selectAlarmRecordForSafetyReport(param);
            if (!CollectionUtils.isEmpty(alarmStatusTotalByUserList)) {
                buildAverageTimeSpentInSeconds(alarmStatusTotalByUserList, param);
            }
            List<AlarmRecordStatisticsExportVO> alarmStatusTotalByUserExportVOS = ObjectUtils
                    .copying(alarmStatusTotalByUserList, AlarmRecordStatisticsExportVO.class);
            EasyExcel.write(response.getOutputStream(), AlarmRecordStatisticsExportVO.class).sheet("报警统计")
                    .doWrite(alarmStatusTotalByUserExportVOS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<AlarmLevelStatisticsVO> getAlarmLevelHandlerRateStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = this.alarmRecordMapper.selectAlarmLevelHandlerRateStatistics(param);
        alarmLevelStatisticsVOS.forEach(alarmLevelStatisticsVO -> {
            alarmLevelStatisticsVO.setHandlerRate(MathUtils.calculateProportion(alarmLevelStatisticsVO.getHandlerNum(), alarmLevelStatisticsVO.getAlarmNum()));
        });
        return alarmLevelStatisticsVOS;
    }

    @Override
    public AlarmAvgHandlerEfficiencyVO getAlarmAvgHandlerEfficiency(AlarmStatisticsQueryParam param) {
        //已处理报警数量
        Long handlerNum = this.alarmRecordMapper.selectAlarmHandlerTotal(param);
        //已处理报警的总时长
        Long durationTotal = this.alarmRecordMapper.selectAlarmHandlerDurationTotal(param);
        float avgHandlerEfficiency = MathUtils.calculateProportion(durationTotal, handlerNum);
        //最大时长
        Long maxDuration = this.alarmRecordMapper.selectAlarmHandlerMaxDuration(param);
        Long minDuration = this.alarmRecordMapper.selectAlarmHandlerMinDuration(param);
        return new AlarmAvgHandlerEfficiencyVO(avgHandlerEfficiency, maxDuration, minDuration);
    }

    @Override
    public List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiency(AlarmStatisticsQueryParam param) {
        List<AlarmLevelHandlerEfficiencyStatisticsVO> statisticsVOS = this.alarmRecordMapper.selectAlarmLevelHandlerEfficiency(param);
        statisticsVOS.forEach(statisticsVO -> {
            statisticsVO.setAvgHandlerDuration(MathUtils.calculateProportion(statisticsVO.getDurationTotal(), statisticsVO.getHandlerNum()));
        });
        return statisticsVOS;
    }

    @Override
    public List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateTopStatistics(AlarmStatisticsQueryParam param) {
        Long alarmTotal = this.alarmRecordMapper.selectAlarmTotal(param);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopVOS = this.alarmRecordMapper.selectUserAlarmHandlerStatistics(param);
        if (CollectionUtils.isEmpty(alarmUserHandlerRateTopVOS)) {
            return Collections.emptyList();
        }
        for (AlarmUserHandlerRateTopVO alarmUserHandlerRateTopVO : alarmUserHandlerRateTopVOS) {
            float calculateProportion = MathUtils.calculateProportion(alarmUserHandlerRateTopVO.getUserHandlerAlarmNumber(), alarmTotal);
            alarmUserHandlerRateTopVO.setUserHandlerAlarmNumber(alarmUserHandlerRateTopVO.getUserHandlerAlarmNumber());
            alarmUserHandlerRateTopVO.setUserHandlerRate((double) calculateProportion);
        }
        //top处理用户
        return alarmUserHandlerRateTopVOS
                .stream()
                .sorted(Comparator.comparing(AlarmUserHandlerRateTopVO::getUserHandlerRate).reversed())
                .limit(param.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateLastTopStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopStatistics = getAlarmUserHandlerRateTopStatistics(param);
        //top处理用户
        return alarmUserHandlerRateTopStatistics
                .stream()
                .sorted(Comparator.comparing(AlarmUserHandlerRateTopVO::getUserHandlerRate))
                .limit(param.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmUserHandlerEfficiencyStatisticsVO> userHandlerEfficiencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmUserHandlerUserEfficiencyStatistics(param);
        if (CollectionUtils.isEmpty(userHandlerEfficiencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //top处理用户
        return userHandlerEfficiencyStatisticsVOS
                .stream()
                .sorted(Comparator.comparing(AlarmUserHandlerEfficiencyStatisticsVO::getAvgProcessSeconds))
                .limit(param.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmUserHandlerEfficiencyStatisticsVO> userHandlerEfficiencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmUserHandlerUserEfficiencyStatistics(param);
        if (CollectionUtils.isEmpty(userHandlerEfficiencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //倒数top处理用户
        return userHandlerEfficiencyStatisticsVOS
                .stream()
                .sorted(Comparator.comparing(AlarmUserHandlerEfficiencyStatisticsVO::getAvgProcessSeconds).reversed())
                .limit(param.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public AlarmLevelTopStatisticsVO getAlarmLevelTopStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = this.alarmRecordMapper.selectAlarmLevelHandlerRateStatistics(param);
        alarmLevelStatisticsVOS.forEach(alarmLevelStatisticsVO -> {
            alarmLevelStatisticsVO.setHandlerRate(MathUtils.calculateProportion(alarmLevelStatisticsVO.getHandlerNum(), alarmLevelStatisticsVO.getAlarmNum()));
        });
        Optional<AlarmLevelStatisticsVO> alarmLevelStatisticsVO = alarmLevelStatisticsVOS.stream()
                .sorted(Comparator.comparing(AlarmLevelStatisticsVO::getAlarmNum).reversed())
                .limit(1).findFirst();
        Optional<AlarmLevelStatisticsVO> levelHandlerStatisticsVO = alarmLevelStatisticsVOS.stream()
                .sorted(Comparator.comparing(AlarmLevelStatisticsVO::getHandlerRate).reversed())
                .limit(1).findFirst();
        //处理效率
        List<AlarmHandlerTimeoutRateStatisticsVO> levelHandlerTimeoutRate = this.alarmRecordMapper.selectAlarmLevelHandlerTimeoutRate(param);
        for (AlarmHandlerTimeoutRateStatisticsVO alarmHandlerTimeoutRate : levelHandlerTimeoutRate) {
            float timeoutRate = MathUtils.calculateProportion((long) (alarmHandlerTimeoutRate.getTotalHandlerNum() -
                                                                      alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum()),
                                                              alarmHandlerTimeoutRate.getTotalHandlerNum().longValue());
            alarmHandlerTimeoutRate.setHandlerEfficiency(timeoutRate);
        }
        Optional<AlarmHandlerTimeoutRateStatisticsVO> timeoutRateStatisticsVO = levelHandlerTimeoutRate.stream()
                .sorted(Comparator.comparing(AlarmHandlerTimeoutRateStatisticsVO::getHandlerEfficiency).reversed())
                .limit(1).findFirst();
        return AlarmLevelTopStatisticsVO.builder()
                .maxAlarmLevel(alarmLevelStatisticsVO.map(AlarmLevelStatisticsVO::getCode).orElse(null))
                .maxAlarmCount(alarmLevelStatisticsVO.map(AlarmLevelStatisticsVO::getAlarmNum).orElse(null))
                .maxHandlerLevel(levelHandlerStatisticsVO.map(AlarmLevelStatisticsVO::getCode).orElse(null))
                .maxHandlerRate(levelHandlerStatisticsVO.map(AlarmLevelStatisticsVO::getHandlerRate).orElse(null))
                .maxEfficiencyLevel(timeoutRateStatisticsVO.map(AlarmHandlerTimeoutRateStatisticsVO::getAlarmLevelCode).orElse(null))
                .maxEfficiencyRate(timeoutRateStatisticsVO.map(AlarmHandlerTimeoutRateStatisticsVO::getHandlerEfficiency).orElse(null))
                .build();
    }

    @Override
    public Page<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReportByDepartment(AlarmStatusUserTotalQueryParam param) {
        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmRecordMapper
                .selectAlarmRecordForSafetyReportByDepartment(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return alarmStatusTotalByUserPageVOPage;
        }
        List<Long> departmentIds = alarmStatusTotalByUserPageVOPage.getRecords().stream()
                .map(AlarmStatusTotalByUserPageVO::getDepartmentId)
                .collect(Collectors.toList());
        Map<Long, String> deaprtmentMap = organizationClient.getOrganizationsByIds(departmentIds)
                .stream()
                .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
        alarmStatusTotalByUserPageVOPage.getRecords().forEach(t -> {
            t.setDepartmentName(deaprtmentMap.get(t.getDepartmentId()));
        });
        buildAverageTimeSpentInSeconds(alarmStatusTotalByUserPageVOPage.getRecords(), param);
        return alarmStatusTotalByUserPageVOPage;

    }

    @Override
    public void getAlarmRecordForSafetyReportByDepartmentExport(AlarmStatusUserTotalQueryParam param) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            String excelName = URLEncoder.encode("安全报表部门统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());

            List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserList = this.alarmRecordMapper
                    .selectAlarmRecordForSafetyReportByDepartment(param);
            if (!CollectionUtils.isEmpty(alarmStatusTotalByUserList)) {
                buildAverageTimeSpentInSeconds(alarmStatusTotalByUserList, param);
                Map<String, AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = this.alarmRecordMapper
                        .selectAlarmDepartmentCount(param)
                        .stream().collect(Collectors.toMap(AlarmBimStatisticsTopChainVO::getId, v -> v));
                List<Long> departmentIds = alarmStatusTotalByUserList.stream().map(AlarmStatusTotalByUserPageVO::getDepartmentId)
                        .collect(Collectors.toList());
                Map<Long, String> deaprtmentMap = organizationClient.getOrganizationsByIds(departmentIds)
                        .stream()
                        .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
                for (AlarmStatusTotalByUserPageVO alarmStatusTotalByUserPageVO : alarmStatusTotalByUserList) {
                    AlarmBimStatisticsTopChainVO alarmBimStatisticsTopChainVO = alarmBimStatisticsTopChainVOS
                            .get(alarmStatusTotalByUserPageVO
                                         .getDepartmentId()
                                         .toString());
                    if (Objects.nonNull(alarmBimStatisticsTopChainVO)) {
                        alarmStatusTotalByUserPageVO.setTotalNum(alarmBimStatisticsTopChainVO.getAlarmNum().longValue());
                    }
                    alarmStatusTotalByUserPageVO.setDepartmentName(deaprtmentMap.get(alarmStatusTotalByUserPageVO.getDepartmentId()));
                }
            }
            List<AlarmRecordDepartmentExportVO> alarmStatusTotalByUserExportVOS = ObjectUtils
                    .copying(alarmStatusTotalByUserList, AlarmRecordDepartmentExportVO.class);
            EasyExcel.write(response.getOutputStream(), AlarmRecordDepartmentExportVO.class).sheet("部门报警统计")
                    .doWrite(alarmStatusTotalByUserExportVOS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 计算平均耗时
     *
     * @param alarmStatusTotalByUserPageVOPage
     * @param param
     */
    private void buildAverageTimeSpentInSeconds(List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage,
                                                AlarmStatusUserTotalQueryParam param
                                               ) {
        Long aLong = alarmRecordMapper.selectAlarmRecordCountForSafetyReport(param);
        for (AlarmStatusTotalByUserPageVO alarmStatusTotalByUserPageVO : alarmStatusTotalByUserPageVOPage) {
            if (Objects.nonNull(alarmStatusTotalByUserPageVO.getHandlerNum()) &&
                0 < alarmStatusTotalByUserPageVO.getHandlerNum()) {
                //计算耗时
                //平均耗时秒数
                long averageSeconds = new BigDecimal(alarmStatusTotalByUserPageVO.getProcessingTime())
                        .divide(new BigDecimal(alarmStatusTotalByUserPageVO.getHandlerNum()), BigDecimal.ROUND_UP).longValue();
                //分钟数转为秒数
                alarmStatusTotalByUserPageVO.setAverageProcessing(MathUtils.dateConvertRemark(averageSeconds));
            } else {
                alarmStatusTotalByUserPageVO.setAverageProcessing(MathUtils.dateConvertRemark(0));
                alarmStatusTotalByUserPageVO.setHandlerRate("0.00%");
            }
            alarmStatusTotalByUserPageVO.setDepartmentTotalNum(aLong);
            alarmStatusTotalByUserPageVO.setAlarmTotalNum(aLong);
        }
    }
}
