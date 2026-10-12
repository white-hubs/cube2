package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmType;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeQueryParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmLevelService;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeService;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
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
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

;

/**
 * 三维可视化报警统计
 *
 * @author lanchun
 * @date 2023/3/16 15:14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimStatisticsServiceImpl implements AlarmBimStatisticsService {

    private final AlarmRecordMapper alarmRecordMapper;

    private final SatAlarmLevelService satAlarmLevelService;

    private final AreaClient areaClient;

    private final SatAlarmTypeService satAlarmTypeService;

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    /**
     * 报警记录分页列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public Page<AlarmRecordPO> getAlarmRecordPage(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return new Page<>();
        }
        return this.alarmRecordMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize())
                , Wrappers.<AlarmRecordPO>lambdaQuery()
                                                         .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                                                                 .like(AlarmRecordPO::getAlarmEventName, param.getKeyword())
                                                                 .or()
                                                                 .like(AlarmRecordPO::getAlarmTargetName, param.getKeyword()))
                                                         .in(!CollectionUtils.isEmpty(queryParam
                                                                                              .getDeviceIds()), AlarmRecordPO::getAlarmTargetKey, queryParam
                                                                     .getDeviceIds())
                                                         .eq(StringUtils
                                                                     .isNotEmpty(param.getAlarmLevelCode()), AlarmRecordPO::getAlarmLevelCode, param
                                                                     .getAlarmLevelCode())
                                                         .eq(StringUtils.isNotEmpty(queryParam
                                                                                            .getAlarmTargetCode()), AlarmRecordPO::getAlarmTargetKey, queryParam
                                                                     .getAlarmTargetCode())
                                                         .eq(StringUtils
                                                                     .isNotEmpty(param.getAlarmStatus()), AlarmRecordPO::getAlarmStatus, param
                                                                     .getAlarmStatus())
                                                         .ge(Objects.nonNull(queryParam.getPeriod()) &&
                                                             Objects.nonNull(queryParam.getPeriod()
                                                                                     .getBegin()), AlarmRecordPO::getAlarmTime, ObjectUtils
                                                                     .ifNull(queryParam.getPeriod(), Period::getBegin))
                                                         .le(Objects.nonNull(queryParam.getPeriod()) &&
                                                             Objects.nonNull(queryParam.getPeriod()
                                                                                     .getEnd()), AlarmRecordPO::getAlarmTime, ObjectUtils
                                                                     .ifNull(queryParam.getPeriod(), Period::getEnd))
                                                         .orderByDesc(AlarmRecordPO::getAlarmTime)
                                                );
    }

    /**
     * 报警统计
     * 报警处理占比,报警真实性统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public AlarmBimStatisticsVO getAlarmBimStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return new AlarmBimStatisticsVO(0L, 0L, 0L, 0L);
        }
        return this.alarmRecordMapper.selectAlarmBimStatistics(queryParam);
    }

    /**
     * 报警级别统计
     * 报警级别统计报警数量(含环比)
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsLevelVO> getAlarmBimStatisticsLevel(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<AlarmBimStatisticsLevelVO> alarmBimStatisticsLevelVOS = new ArrayList<>(4);
        List<SatAlarmLevel> satAlarmLevels = this.satAlarmLevelService.list();
        if (Objects.nonNull(param.getSceneType()) && CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            satAlarmLevels.stream().forEach(satAlarmLevel ->
                                                    alarmBimStatisticsLevelVOS
                                                            .add(new AlarmBimStatisticsLevelVO(satAlarmLevel.getCode(), satAlarmLevel
                                                                    .getName(), 0L, 0))
                                           );
            return alarmBimStatisticsLevelVOS;
        }
        //按报警级别分组查询本期
        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = this.alarmRecordMapper.selectAlarmLevelStatistics(queryParam);
        //获取上期时间
        Period period = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        queryParam.setPeriod(period);
        //按报警级别分组查询上期
        List<AlarmLevelStatisticsVO> previousAlarmLevelStatisticsVOS = this.alarmRecordMapper.selectAlarmLevelStatistics(queryParam);
        alarmLevelStatisticsVOS.stream().forEach(alarmLevelStatisticsVO -> {
            //获取该级别上期数据
            Optional<AlarmLevelStatisticsVO> previousAlarmLevelOptional = previousAlarmLevelStatisticsVOS.stream()
                    .filter(previousAlarmLevelStatistics -> alarmLevelStatisticsVO
                            .getCode()
                            .equals(previousAlarmLevelStatistics.getCode()))
                    .findFirst();
            if (previousAlarmLevelOptional.isPresent() && 0 < previousAlarmLevelOptional.get().getAlarmNum()) {
                //计算环比增长率((本期-上期)/上期)
                double ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmLevelStatisticsVO.getAlarmNum(), previousAlarmLevelOptional
                        .get().getAlarmNum());
                alarmBimStatisticsLevelVOS.add(new AlarmBimStatisticsLevelVO(alarmLevelStatisticsVO.getCode(), alarmLevelStatisticsVO
                        .getName(), alarmLevelStatisticsVO.getAlarmNum(), ringRatio));
            } else {
                alarmBimStatisticsLevelVOS.add(new AlarmBimStatisticsLevelVO(alarmLevelStatisticsVO.getCode(), alarmLevelStatisticsVO
                        .getName(), alarmLevelStatisticsVO.getAlarmNum(), 0));
            }
        });
        return alarmBimStatisticsLevelVOS;
    }

    @Override
    public List<AlarmBimStatisticsDeviceTypeVO> getAlarmBimStatisticsDeviceType(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<AlarmBimStatisticsDeviceTypeVO> alarmBimStatisticsDeviceType = alarmRecordMapper.getAlarmBimStatisticsDeviceType(queryParam);
        Period period = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        //查找环比
        queryParam.setPeriod(period);
        List<AlarmBimStatisticsDeviceTypeVO> alarmBimStatisticsPreviousDeviceType = alarmRecordMapper
                .getAlarmBimStatisticsPreviousDeviceType(queryParam);
        alarmBimStatisticsDeviceType.forEach(s -> {
            Optional<AlarmBimStatisticsDeviceTypeVO> first = alarmBimStatisticsPreviousDeviceType.stream()
                    .filter(a -> a.getTypeName()
                            .equals(s.getTypeName()))
                    .findFirst();
            if (first.isPresent() && 0 < first.get().getAlarmNum()) {
                double num = (s.getAlarmNum() - first.get().getAlarmNum()) / first.get().getAlarmNum();
                s.setChainRatio(num);
            }
        });
        return alarmBimStatisticsDeviceType;
    }

    /**
     * 报警趋势
     * 报警趋势折线图统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTrendVO> getAlarmBimStatisticsTrend(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmTrendStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsTrendQueryParam(param);
        var dateField = DateUnitUtils.handleDateParam(queryParam);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        List<AlarmBimStatisticsTrendVO> finalDatas = new ArrayList<>();
        if (Objects.nonNull(param.getSceneType()) && CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            //只返回一个带有横坐标信息的集合数据
            for (LocalDateTime start = startDate;
                 start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
                String viewDate = DateUtils.format(start, queryParam.getViewDateString());
                finalDatas.add(new AlarmBimStatisticsTrendVO(0L, viewDate));
            }
            return finalDatas;
        }
        //查询数据
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmTrendStatistics(queryParam);
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, queryParam.getDateString());
            String viewDate = DateUtils.format(start, queryParam.getViewDateString());
            AlarmTrendStatisticsVO trendStatVO = alarmTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                    .findAny().orElse(null);
            finalDatas.add(trendStatVO == null ? new AlarmBimStatisticsTrendVO(0L, viewDate)
                                               : new AlarmBimStatisticsTrendVO(trendStatVO.getAlarmNum(), viewDate));
        }
        return finalDatas;
    }

    /**
     * 报警事件top5统计
     * 报警事件top5统计(含与上期环比)
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsEventTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        //本期报警总数
        Long alarmCount = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        //报警事件top5
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmEventAnalysisStatisticsTop(queryParam, 5);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        List<AlarmBimStatisticsTopChainVO> eventTopChainVOS = new ArrayList<>(alarmCurrencyStatisticsVOS.size());
        List<String> eventIds = alarmCurrencyStatisticsVOS.stream().map(AlarmCurrencyStatisticsVO::getId)
                .collect(Collectors.toList());
        queryParam.setPeriod(DateUnitUtils.getPreviousPeriod(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setEventIds(eventIds);
        //获取事件上期报警数
        List<AlarmCurrencyStatisticsVO> previousAlarmCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmEventAnalysisStatisticsTop(queryParam, 5);
        alarmCurrencyStatisticsVOS.stream().forEach(alarmCurrencyStatisticsVO -> {
            //事件上期报警数量
            Optional<AlarmCurrencyStatisticsVO> previousDeviceAlarmStatisticsOptional = previousAlarmCurrencyStatisticsVOS
                    .stream().filter(previousAlarmCurrencyStatistics -> alarmCurrencyStatisticsVO.getId()
                            .equals(previousAlarmCurrencyStatistics.getId()))
                    .findFirst();
            //环比
            double ringRatio = 0;
            //占比
            double ratio = 0;
            if (previousDeviceAlarmStatisticsOptional.isPresent() &&
                0 < previousDeviceAlarmStatisticsOptional.get().getTotalNum()) {
                //计算环比增长率((本期-上期)/上期)
                ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmCurrencyStatisticsVO.getTotalNum()
                                                                         .longValue(), previousDeviceAlarmStatisticsOptional
                                                                         .get().getTotalNum().longValue());
            }
            //计算占比
            ratio = MathUtils.calculateProportion(alarmCurrencyStatisticsVO.getTotalNum().longValue(), alarmCount);
            eventTopChainVOS.add(new AlarmBimStatisticsTopChainVO(alarmCurrencyStatisticsVO.getId(), alarmCurrencyStatisticsVO
                    .getName(), alarmCurrencyStatisticsVO.getTotalNum(), ringRatio, ratio));
        });
        return eventTopChainVOS;
    }

    @Override
    public AlarmBimStatisticsSituationVO getAlarmTypeSituation(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<SatAlarmType> satAlarmTypes = this.satAlarmTypeService.listAlarmTypes(new SatAlarmTypeQueryParam());
        List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = new ArrayList<>();
        if (Objects.nonNull(param.getSceneType()) && CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            satAlarmTypes.stream().forEach(satAlarmType -> {
                alarmBimStatisticsTopChainVOS.add(new AlarmBimStatisticsTopChainVO(
                        satAlarmType.getId().toString(),
                        satAlarmType.getName(),
                        0,
                        0,
                        0
                ));
            });
            return AlarmBimStatisticsSituationVO.builder()
                    .alarmBimStatistics(new AlarmBimStatisticsVO(0L, 0L, 0L, 0L))
                    .alarmBimStatisticsTopChains(alarmBimStatisticsTopChainVOS)
                    .build();
        }
        //本期报警总数
        queryParam.setAlarmTypeIds(satAlarmTypes.stream().map(SatAlarmType::getId).collect(Collectors.toList()));
        AlarmBimStatisticsVO alarmBimStatistics = this.alarmRecordMapper.selectAlarmBimStatistics(queryParam);
        if (Objects.nonNull(alarmBimStatistics.getAlarmHandlerNum())) {
            alarmBimStatistics.setAlarmImpracticalNum(
                    alarmBimStatistics.getAlarmHandlerNum() - alarmBimStatistics.getAlarmActualNum());
        }
        List<AlarmTypeStatisticsVO> alarmTypeStatisticsVOS = this.alarmRecordMapper.selectAlarmTypeStatistics(queryParam);
        alarmTypeStatisticsVOS.stream().forEach(alarmTypeStatisticsVO -> {
            //占比
            double ratio = MathUtils.calculateProportion(alarmTypeStatisticsVO.getAlarmNum(), alarmBimStatistics.getAlarmNum());
            alarmBimStatisticsTopChainVOS
                    .add(new AlarmBimStatisticsTopChainVO(alarmTypeStatisticsVO.getId(), alarmTypeStatisticsVO.getName(),
                                                          Objects.nonNull(alarmTypeStatisticsVO.getAlarmNum())
                                                          ? alarmTypeStatisticsVO.getAlarmNum().intValue()
                                                          : 0, 0, ratio));
        });
        return AlarmBimStatisticsSituationVO.builder()
                .alarmBimStatistics(alarmBimStatistics)
                .alarmBimStatisticsTopChains(alarmBimStatisticsTopChainVOS)
                .build();
    }

    /**
     * 报警处理排名top5统计
     * 按人员处理排名
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsUserHandlerTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        //报警处理人员
        List<AlarmHandlerCurrencyStatisticsVO> alarmHandlerCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmHandlerAnalysisStatistics(queryParam);
        if (CollectionUtils.isEmpty(alarmHandlerCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //已处理的报警总数
        Integer alarmHandlerTotalCount = alarmHandlerCurrencyStatisticsVOS.stream()
                .mapToInt(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum)
                .sum();
        //前五数据
        alarmHandlerCurrencyStatisticsVOS = alarmHandlerCurrencyStatisticsVOS.stream()
                .sorted(Comparator
                                .comparing(AlarmHandlerCurrencyStatisticsVO::getAlarmHandlerTotalNum)
                                .reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<AlarmBimStatisticsTopChainVO> userHandlerTopChainVOS = new ArrayList<>(alarmHandlerCurrencyStatisticsVOS.size());
        List<Long> userIds = alarmHandlerCurrencyStatisticsVOS.stream()
                .map(alarmHandlerCurrencyStatisticsVO -> Long.valueOf(alarmHandlerCurrencyStatisticsVO.getId()))
                .collect(Collectors.toList());
        queryParam.setUserIds(userIds);
        queryParam.setPeriod(DateUnitUtils.getPreviousPeriod(param.getType(), queryParam.getPeriod()));
        //用户处理数上期数量
        List<AlarmHandlerCurrencyStatisticsVO> previousAlarmCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmHandlerAnalysisStatistics(queryParam);
        alarmHandlerCurrencyStatisticsVOS.stream().forEach(alarmCurrencyStatisticsVO -> {
            //环比
            double ringRatio = 0;
            //用户上期处理数量
            Optional<AlarmHandlerCurrencyStatisticsVO> previousUserHandlerAlarmStatisticsOptional = previousAlarmCurrencyStatisticsVOS
                    .stream().filter(previousAlarmCurrencyStatistics -> alarmCurrencyStatisticsVO.getId()
                            .equals(previousAlarmCurrencyStatistics.getId()))
                    .findFirst();
            if (previousUserHandlerAlarmStatisticsOptional.isPresent() &&
                0 < previousUserHandlerAlarmStatisticsOptional.get().getAlarmHandlerTotalNum()) {
                //计算环比增长率((本期-上期)/上期)
                ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmCurrencyStatisticsVO.getAlarmHandlerTotalNum()
                                                                         .longValue(), previousUserHandlerAlarmStatisticsOptional
                                                                         .get().getAlarmHandlerTotalNum().longValue());
            }
            //计算占比
            double ratio = MathUtils.calculateProportion(alarmCurrencyStatisticsVO.getAlarmHandlerTotalNum()
                                                                 .longValue(), alarmHandlerTotalCount.longValue());
            userHandlerTopChainVOS.add(new AlarmBimStatisticsTopChainVO(alarmCurrencyStatisticsVO.getId(), alarmCurrencyStatisticsVO
                    .getName(), alarmCurrencyStatisticsVO.getAlarmHandlerTotalNum(), ringRatio, ratio));
        });
        return userHandlerTopChainVOS;
    }

    /**
     * 报警区域top5统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsAreaTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        //本期报警总数
        Long alarmCount = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmRecordMapper.selectAlarmAreaAnalysisStatistics(queryParam);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //区域序列号集合
        List<String> areaSequences = alarmCurrencyStatisticsVOS.stream().map(AlarmCurrencyStatisticsVO::getId)
                .collect(Collectors.toList());
        //上期时间
        queryParam.setPeriod(DateUnitUtils.getPreviousPeriod(param.getType(), queryParam.getPeriod()));
        queryParam.setAreaSequences(areaSequences);
        //获取区域上期数据
        List<AlarmCurrencyStatisticsVO> previousAlarmCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmAreaAnalysisStatistics(queryParam);
        if (Objects.isNull(param.getAreaLevel())) {
            param.setAreaLevel(1);
        }
        List<AreaDTO> areaDTOS = areaClient.getAreaByLevelOne(param.getAreaLevel());
        List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = Collections.synchronizedList(new ArrayList<>(areaDTOS.size()));
        areaDTOS.parallelStream().forEach(areaDTO -> {
            //区域本期数量(包含子级区域)
            Integer twoAreaAlarmTotalCount = alarmCurrencyStatisticsVOS.stream()
                    .filter(alarmCurrencyStatisticsVO -> (
                            alarmCurrencyStatisticsVO.getId() +
                            "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(AlarmCurrencyStatisticsVO::getTotalNum)
                    .sum();
            //区域上期报警数量(包含子级区域)
            Integer previousTwoAreaAlarmTotalCount = previousAlarmCurrencyStatisticsVOS.stream()
                    .filter(alarmCurrencyStatisticsVO -> (
                            alarmCurrencyStatisticsVO.getId() +
                            "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(AlarmCurrencyStatisticsVO::getTotalNum)
                    .sum();
            //环比率
            double ringRatio = 0;
            if (Objects.nonNull(previousTwoAreaAlarmTotalCount)) {
                //计算环比增长率((本期-上期)/上期)
                ringRatio = MathUtils
                        .calculateMonthOnMonthRatio(twoAreaAlarmTotalCount.longValue(), previousTwoAreaAlarmTotalCount.longValue());
            }
            //占比率
            double ratio = MathUtils.calculateProportion(twoAreaAlarmTotalCount.longValue(), alarmCount.longValue());
            alarmBimStatisticsTopChainVOS.add(new AlarmBimStatisticsTopChainVO(areaDTO.getId()
                                                                                       .toString(), areaDTO
                                                                                       .getName(), twoAreaAlarmTotalCount, ringRatio, ratio));
        });
        return alarmBimStatisticsTopChainVOS.stream()
                .sorted(Comparator.comparing(AlarmBimStatisticsTopChainVO::getAlarmNum).reversed())
                .limit(5).collect(Collectors.toList());
    }

    /**
     * 报警设备top5统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTopChainVO> getAlarmBimStatisticsDeviceTopFive(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        //本期报警总数
        Long alarmCount = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        //设备前五
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmRecordMapper
                .selectAlarmDeviceGroupTopStatistics(queryParam, 5);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        //设备前五返回数据集合
        List<AlarmBimStatisticsTopChainVO> deviceTopChainVOS = new ArrayList<>(alarmCurrencyStatisticsVOS.size());
        //获取设备主键集合
        List<Long> deviceIds = alarmCurrencyStatisticsVOS.stream()
                .map(alarmCurrencyStatisticsVO -> Long.valueOf(alarmCurrencyStatisticsVO.getId()))
                .collect(Collectors.toList());
        queryParam.setPeriod(DateUnitUtils.getPreviousPeriod(queryParam.getType(), queryParam.getPeriod()));
        queryParam.setDeviceIds(deviceIds);
        //获取设备上期报警数
        List<AlarmCurrencyStatisticsVO> previousAlarmCurrencyStatisticsVO = this.alarmRecordMapper.selectDeviceAlarmTotal(queryParam);
        alarmCurrencyStatisticsVOS.stream().forEach(alarmCurrencyStatisticsVO -> {
            //设备上期报警数量
            Optional<AlarmCurrencyStatisticsVO> previousDeviceAlarmStatisticsOptional = previousAlarmCurrencyStatisticsVO.stream()
                    .filter(previousAlarmCurrencyStatistics -> alarmCurrencyStatisticsVO
                            .getId()
                            .equals(previousAlarmCurrencyStatistics.getId()))
                    .findFirst();
            //占比率
            double ratio = 0;
            Long lastTotalNum = 0L;
            if (previousDeviceAlarmStatisticsOptional.isPresent() &&
                0 < previousDeviceAlarmStatisticsOptional.get().getTotalNum()) {
                lastTotalNum = previousDeviceAlarmStatisticsOptional.get().getTotalNum().longValue();
            }
            //环比率
            double ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmCurrencyStatisticsVO.getTotalNum()
                                                                            .longValue(), lastTotalNum);
            //计算占比
            ratio = MathUtils.calculateProportion(alarmCurrencyStatisticsVO.getTotalNum().longValue(), alarmCount);
            deviceTopChainVOS.add(new AlarmBimStatisticsTopChainVO(alarmCurrencyStatisticsVO.getId(), alarmCurrencyStatisticsVO
                    .getName(), alarmCurrencyStatisticsVO.getTotalNum(), ringRatio, ratio));
        });
        return deviceTopChainVOS;
    }

    /**
     * 报警记录状态统计(报警数,未处理数,超时报警数)
     *
     * @param param
     *
     * @return
     */
    @Override
    public AlarmBimStatisticsRecordStatusVO getAlarmBimStatisticsRecordStatus(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return new AlarmBimStatisticsRecordStatusVO(0L, 0, 0L, 0, 0L, 0L, 0);
        }
        AlarmBimStatisticsRecordStatusVO alarmBimStatisticsRecordStatusVO = this.alarmRecordMapper
                .selectAlarmBimStatisticsRecordStatus(queryParam);
        if (StringUtils.isNotEmpty(param.getType())) {
            param.setPeriod(DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod()));
            AlarmBimStatisticsRecordStatusVO lastAlarmBimStatisticsRecordStatusVO = this.alarmRecordMapper
                    .selectAlarmBimStatisticsRecordStatus(queryParam);
            alarmBimStatisticsRecordStatusVO
                    .setChainRatio(MathUtils.calculateMonthOnMonthRatio(alarmBimStatisticsRecordStatusVO.getAlarmNum(),
                                                                        lastAlarmBimStatisticsRecordStatusVO.getAlarmNum()));
            alarmBimStatisticsRecordStatusVO
                    .setAlarmHandlerChainRatio(MathUtils.calculateMonthOnMonthRatio(alarmBimStatisticsRecordStatusVO.getAlarmHandlerNum(),
                                                                                    lastAlarmBimStatisticsRecordStatusVO
                                                                                            .getAlarmHandlerNum()));
            alarmBimStatisticsRecordStatusVO.setAlarmUnresolvedHandlerChainRatio(MathUtils
                                                                                         .calculateMonthOnMonthRatio(alarmBimStatisticsRecordStatusVO
                                                                                                                             .getAlarmUnresolvedHandlerNum(),
                                                                                                                     lastAlarmBimStatisticsRecordStatusVO
                                                                                                                             .getAlarmUnresolvedHandlerNum()));
        }
        return alarmBimStatisticsRecordStatusVO;
    }

    /**
     * 报警状态统计
     * 含报警总数,环比，报警状态分组统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public AlarmBimStatisticsStatusVO getAlarmBimStatisticsStatus(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        AlarmStatusEnum[] alarmStatusEnums = AlarmStatusEnum.values();
        List<AlarmStatusStatisticsVO> finalAlarmStatusStatisticsVOS = new ArrayList<>(alarmStatusEnums.length);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            //返回一个状态集合但报警数量为0的集合
            for (AlarmStatusEnum alarmStatus : alarmStatusEnums) {
                finalAlarmStatusStatisticsVOS.add(new AlarmStatusStatisticsVO(alarmStatus.name(), alarmStatus.getDescription(), 0L));
            }
            return new AlarmBimStatisticsStatusVO(0L, 0L, finalAlarmStatusStatisticsVOS);
        }
        //报警总数
        Long alarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        //报警状态分组统计
        List<AlarmStatusStatisticsVO> alarmStatusStatisticsVOS = this.alarmRecordMapper.selectAlarmStatusStatistics(queryParam);
        double ringRatio = 0;
        if (Objects.nonNull(queryParam.getPeriod()) && StringUtils.isNotEmpty(queryParam.getType())) {
            //上期报警总数
            //获取上期时间
            Period period = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
            queryParam.setPeriod(period);
            Long previousAlarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
            ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmNum, previousAlarmNum);
        }
        //上期报警状态统计
        List<AlarmStatusStatisticsVO> lastAlarmStatusStatisticsVOS = this.alarmRecordMapper.selectAlarmStatusStatistics(queryParam);
        for (AlarmStatusEnum alarmStatus : alarmStatusEnums) {
            Optional<AlarmStatusStatisticsVO> alarmStatusStatisticsOptional = alarmStatusStatisticsVOS.stream()
                    .filter(alarmStatusStatisticsVO -> alarmStatus
                            .name()
                            .equals(alarmStatusStatisticsVO.getAlarmStatus()))
                    .findFirst();
            Optional<AlarmStatusStatisticsVO> lastAlarmStatusStatisticsOptional = lastAlarmStatusStatisticsVOS.stream()
                    .filter(alarmStatusStatisticsVO -> alarmStatus
                            .name()
                            .equals(alarmStatusStatisticsVO.getAlarmStatus()))
                    .findFirst();
            AlarmStatusStatisticsVO alarmStatusStatisticsVO;
            if (alarmStatusStatisticsOptional.isPresent()) {
                alarmStatusStatisticsVO = alarmStatusStatisticsOptional.get();
                alarmStatusStatisticsVO.setAlarmStatusName(alarmStatus.getDescription());
            } else {
                alarmStatusStatisticsVO = new AlarmStatusStatisticsVO(alarmStatus.name(), alarmStatus.getDescription(), 0L);
            }
            long lastTotalNum = 0;
            if (lastAlarmStatusStatisticsOptional.isPresent() && 0 < lastAlarmStatusStatisticsOptional.get().getAlarmNum()) {
                lastTotalNum = lastAlarmStatusStatisticsOptional.get().getAlarmNum().longValue();
            }
            alarmStatusStatisticsVO
                    .setChainRatio(MathUtils.calculateMonthOnMonthRatio(alarmStatusStatisticsVO.getAlarmNum(), lastTotalNum));
            finalAlarmStatusStatisticsVOS.add(alarmStatusStatisticsVO);
        }
        return new AlarmBimStatisticsStatusVO(alarmNum, ringRatio, finalAlarmStatusStatisticsVOS);
    }

    /**
     * 获取报警对象数量
     *
     * @param param
     *
     * @return
     */
    @Override
    public Integer getAlarmTargetCount(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return 0;
        }
        return this.alarmRecordMapper.selectAlarmTargetCount(queryParam);
    }

    /**
     * 报警对象趋势统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AlarmBimStatisticsTrendVO> getAlarmTargetTrend(AlarmBimStatisticsQueryParam param) {
        AlarmTrendStatisticsQueryParam queryParam = ObjectUtils.copying(param, AlarmTrendStatisticsQueryParam.class);
        var dateField = DateUnitUtils.handleDateParam(queryParam);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        List<AlarmBimStatisticsTrendVO> finalDatas = new ArrayList<>();
        //查询数据
        List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = this.alarmRecordMapper.selectAlarmTrendStatistics(queryParam);
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, queryParam.getDateString());
            String viewDate = DateUtils.format(start, queryParam.getViewDateString());
            AlarmTrendStatisticsVO trendStatVO = alarmTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                    .findAny().orElse(null);
            finalDatas.add(trendStatVO == null ? new AlarmBimStatisticsTrendVO(0L, viewDate)
                                               : new AlarmBimStatisticsTrendVO(trendStatVO.getAlarmNum(), viewDate));
        }
        return finalDatas;
    }

    /**
     * 报警对象报警数量统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public AlarmBimStatisticsStatusVO getAlarmTargetStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        Long alarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        double ringRatio = 0;
        if (Objects.nonNull(queryParam.getPeriod()) && StringUtils.isNotEmpty(queryParam.getType())) {
            //上期报警总数
            //获取上期时间
            Period period = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
            queryParam.setPeriod(period);
            Long previousAlarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
            //计算环比增长率((本期-上期)/上期)
            ringRatio = MathUtils.calculateMonthOnMonthRatio(alarmNum, previousAlarmNum);
        }
        return new AlarmBimStatisticsStatusVO(alarmNum, ringRatio, Collections.emptyList());
    }

    @Override
    public List<AlarmRecordAreaCountVO> getAlarmAreaCount(AlarmRecordAreaCountParam param) {
        List<AlarmRecordAreaCountVO> alarmRecordAreaCountVOS = new ArrayList<>();
        for (String areaId : param.getAreaIds()) {
            param.setAreaId(areaId);
            Integer alarmAreaCount = alarmRecordMapper.getAlarmAreaCount(param);
            AlarmRecordAreaCountVO alarmRecordAreaCountVO = new AlarmRecordAreaCountVO();
            alarmRecordAreaCountVO.setNumber(alarmAreaCount);
            alarmRecordAreaCountVO.setAreaId(areaId);
            alarmRecordAreaCountVOS.add(alarmRecordAreaCountVO);
        }
        return alarmRecordAreaCountVOS;
    }

    @Override
    public List<AlarmBimDeviceStatisticsVO> getDeviceGroupStatistics(AlarmBimStatisticsQueryParam param) {

        return alarmRecordMapper.getDeviceGroupStatistics(param);
    }

    @Override
    public List<AlarmBimDeviceStatisticsVO> getTimeGroupStatistics(AlarmBimStatisticsQueryParam param) {

        return alarmRecordMapper.getTimeGroupStatistics(param);
    }

    @Override
    public List<AlarmBimApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param) {
        List<AlarmBimApplicationStatisticsVO> alarmBimApplicationStatisticsVOS = new ArrayList<>();
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if (CollectionUtils.isNotEmpty(queryParam.getDeviceIds())) {
            param.setDeviceIds(queryParam.getDeviceIds());
            alarmBimApplicationStatisticsVOS = alarmRecordMapper.getApplicationGroupStatistics(param);
        }
        return alarmBimApplicationStatisticsVOS;
    }

    private AlarmStatisticsQueryParam convertAlarmStatisticsQueryParam(AlarmBimStatisticsQueryParam param) {
        List<Long> ids = new ArrayList<>();
        if (Objects.nonNull(param.getSceneType())) {
            AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
            ids = queryParam.getDeviceIds();
        }
        AlarmStatisticsQueryParam alarmStatisticsQueryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        alarmStatisticsQueryParam.setDeviceIds(ids);
        return alarmStatisticsQueryParam;
    }

    private AlarmTrendStatisticsQueryParam convertAlarmStatisticsTrendQueryParam(AlarmBimStatisticsQueryParam param) {
        List<Long> ids = new ArrayList<>();
        if (Objects.nonNull(param.getSceneType())) {
            AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
            ids = queryParam.getDeviceIds();
        }
        AlarmTrendStatisticsQueryParam alarmStatisticsQueryParam = ObjectUtils.copying(param, AlarmTrendStatisticsQueryParam.class);
        alarmStatisticsQueryParam.setDeviceIds(ids);
        return alarmStatisticsQueryParam;
    }

    @Override
    public AlarmBimSituationVO getAlarmSituationRatio(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            //返回一个状态集合但报警数量为0的集合
            return AlarmBimSituationVO.builder()
                    .totalNum(0)
                    .ratio(new BigDecimal(0.0))
                    .handlerNum(0)
                    .handlerRatio(new BigDecimal(0.0))
                    .confirmedNum(0)
                    .confirmedRatio(new BigDecimal(0.0))
                    .actualNum(0)
                    .actualRatio(new BigDecimal(0.0))
                    .timeoutNum(0)
                    .timeoutRatio(new BigDecimal(0.0))
                    .build();
        }
        AlarmBimSituationVO alarmSituationVO = this.alarmRecordMapper.selectAlarmSituation(queryParam);
        AlarmStatisticsQueryParam alarmCountQueryParam = ObjectUtils.copying(queryParam, AlarmStatisticsQueryParam.class);
        alarmCountQueryParam.setDeviceIds(null);
        Long alarmCount = this.alarmRecordMapper.selectAlarmTotal(alarmCountQueryParam);
        alarmSituationVO.setRatio(0 == alarmCount ? BigDecimal.valueOf(0.0) :
                                  BigDecimal.valueOf(alarmSituationVO.getTotalNum())
                                          .divide(BigDecimal.valueOf(alarmCount), 2, RoundingMode.HALF_UP)
                                          .multiply(new BigDecimal(100)));
        alarmSituationVO.setHandlerRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                         BigDecimal.valueOf(alarmSituationVO.getHandlerNum())
                                                 .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                 .multiply(new BigDecimal(100)));
        alarmSituationVO.setConfirmedRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                           BigDecimal.valueOf(alarmSituationVO.getConfirmedNum())
                                                   .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                   .multiply(new BigDecimal(100)));
        alarmSituationVO.setActualRatio(0 == alarmSituationVO.getHandlerNum() ? BigDecimal.valueOf(0.0) :
                                        BigDecimal.valueOf(alarmSituationVO.getActualNum())
                                                .divide(BigDecimal.valueOf(alarmSituationVO.getHandlerNum()), 2, RoundingMode.HALF_UP)
                                                .multiply(new BigDecimal(100)));
        alarmSituationVO.setTimeoutRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                         BigDecimal.valueOf(alarmSituationVO.getTimeoutNum())
                                                 .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                 .multiply(new BigDecimal(100)));
        return alarmSituationVO;
    }

    @Override
    public AlarmHandlerTimeoutRateStatisticsVO getAlarmHandlerTimeoutRate(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam statisticsQueryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        AlarmHandlerTimeoutRateStatisticsVO alarmHandlerTimeoutRateStatisticsVO = this.alarmRecordMapper.selectAlarmHandlerTimeoutRate(statisticsQueryParam);
        if (0 == alarmHandlerTimeoutRateStatisticsVO.getTotalHandlerNum()) {
            return alarmHandlerTimeoutRateStatisticsVO;
        }
        float timeoutRate = MathUtils.calculateProportion(alarmHandlerTimeoutRateStatisticsVO.getTotalHandlerTimeoutNum().longValue(),
                                                          alarmHandlerTimeoutRateStatisticsVO.getTotalHandlerNum().longValue());
        alarmHandlerTimeoutRateStatisticsVO.setTotalHandlerTimeoutRate(BigDecimal.valueOf(timeoutRate));
        return alarmHandlerTimeoutRateStatisticsVO;
    }

    @Override
    public List<AlarmUserHandlerTimeoutRateStatisticsVO> getAlarmUserHandlerTimeoutRate(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam statisticsQueryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        List<AlarmUserHandlerTimeoutRateStatisticsVO> alarmUserHandlerTimeoutRateStatisticsVOS = this.alarmRecordMapper.selectAlarmUserHandlerTimeoutRate(statisticsQueryParam);
        alarmUserHandlerTimeoutRateStatisticsVOS.forEach(alarmUserHandlerTimeoutRateStatisticsVO -> {
            float timeoutRate = MathUtils.calculateProportion(alarmUserHandlerTimeoutRateStatisticsVO.getTotalHandlerTimeoutNum()
                                                                      .longValue(),
                                                              alarmUserHandlerTimeoutRateStatisticsVO.getTotalHandlerNum().longValue());
            alarmUserHandlerTimeoutRateStatisticsVO.setTotalHandlerTimeoutRate(BigDecimal.valueOf(timeoutRate));
        });
        return alarmUserHandlerTimeoutRateStatisticsVOS;
    }
}
