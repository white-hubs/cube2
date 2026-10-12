package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.TimePeriodQueryEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyRingRatioQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.EnergyStatUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.TimeStatUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageRingRatioVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageWithCarbonVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyUsageStatWithQoqVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：报警规则配置服务层
 *
 * @author lrj
 * @date 2021-12-30 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyDailyUsageServiceImpl extends ServiceImpl<EnergyDailyUsageMapper, EnergyDailyUsage> implements EnergyDailyUsageService {
    private static final String MONTH_GROUP_FORMAT = "%Y-%m";
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final EnergyDailyUsageMapper energyDailyUsageMapper;
    private final EnergyCollectConfigMapper energyCollectConfigMapper;
    private final EnergyDailyUsageProjectMapper energyDailyUsageProjectMapper;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<EnergyDailyUsageResultVO> getTypePage(EnergyDailyUsageParam param) {
        Period period = param.getPeriod();
        Page<EnergyDailyUsageResultVO> page = energyDailyUsageMapper.getPage(PagingUtils.convert(param), Wrappers
                                                                                     .<EnergyDailyUsage>lambdaQuery()
                                                                                     .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsage::getInstrumentType, param.getInstrumentType())
                                                                                     .between(period !=
                                                                                              null, EnergyDailyUsage::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                                     .orderByDesc(EnergyDailyUsage::getStatDate)
                                                                            );
        return PagingUtils.convert(page);

    }

    @Override
    public Pagination<EnergyDailyUsageVO> getPageForCategory(EnergyDailyUsageParam param) {
        Period period = param.getPeriod();
        Page<EnergyDailyUsage> page = energyDailyUsageMapper.selectPage(PagingUtils.convert(param), Wrappers.<EnergyDailyUsage>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsage::getInstrumentType, param.getInstrumentType())
                .between(period !=
                         null, EnergyDailyUsage::getStatDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd)));
        return PagingUtils.convert(page, page.getRecords().stream().map(d -> {
            EnergyDailyUsageVO vo = ObjectUtils.copying(d, EnergyDailyUsageVO.class);
            vo.setDdValue(d.getDValue());
            return vo;
        }).collect(Collectors.toList()));
    }

    @Override
    public Pagination<EnergyDailyUsageProjectVO> getPageForProject(EnergyDailyUsageParam param) {
        Period period = param.getPeriod();
        Page<EnergyDailyUsageProject> page = energyDailyUsageProjectMapper.selectPage(PagingUtils.convert(param), Wrappers
                .<EnergyDailyUsageProject>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsageProject::getInstrumentType, param.getInstrumentType())
                .eq(param.getProjectId() != null, EnergyDailyUsageProject::getProjectId, param.getProjectId())
                .eq(StringUtils.isNotBlank(param.getProjectCategory()), EnergyDailyUsageProject::getProjectCategory, param.getProjectCategory())
                .between(period !=
                         null, EnergyDailyUsageProject::getStatDate, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd())));
        if (page.getRecords() == null || page.getRecords().isEmpty()) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, page.getRecords().stream().map(up -> {
            EnergyDailyUsageProjectVO vo = new EnergyDailyUsageProjectVO();
            BeanUtils.copyProperties(up, vo);
            vo.setDdValue(up.getDValue());
            return vo;
        }).collect(Collectors.toList()));
    }

    @Override
    public void batchSaveOrUpdateEnergyDailyUsage(List<EnergyDailyUsage> dataList) {
        energyDailyUsageMapper.insertOrUpdateEnergyDailyUsage(dataList);
    }

    //计算环比时间段
    private Period calQOQPeriod(Period period) {
        Long between = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS);
        LocalDateTime start = DateUtils.offsetDay(period.getBegin(), -(between.intValue() + 1));
        LocalDateTime end = DateUtils.offsetDay(period.getEnd(), -(between.intValue() + 1));
        return Period.of(start, end);
    }

    @Override
    public EnergyDailyUsageStatVO getUsageStatForDMY(EnergyDailyUsageParam param) {
        LocalDateTime current = LocalDateTime.now();
        //查询日用量
        Period today = Period.of(DateUtils.beginOfDay(current), DateUtils.endOfDay(current));
        param.setPeriod(today);
        EnergyDailyUsageVO todayUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);
        //月用量
        Period thisMonth = Period.of(DateUtils.beginOfMonth(current), DateUtils.endOfMonth(current));
        param.setPeriod(thisMonth);
        EnergyDailyUsageVO thisMonthUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);
        //年用量
        Period thisYear = Period.of(DateUtils.beginOfYear(current), DateUtils.endOfYear(current));
        param.setPeriod(thisYear);
        EnergyDailyUsageVO thisYearUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);
        //环比日
        Period preDay = this.calQOQPeriod(today);
        param.setPeriod(preDay);
        EnergyDailyUsageVO preDayUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);
        //环比月
        Period preMonth = this.calQOQPeriod(thisMonth);
        param.setPeriod(preMonth);
        EnergyDailyUsageVO preMonthUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);
        //环比年
        Period preYear = this.calQOQPeriod(thisYear);
        param.setPeriod(preYear);
        EnergyDailyUsageVO preYearUsage = energyDailyUsageMapper.getUsageStatByInstrumentType(param);

        //构建返回对象
        return EnergyDailyUsageStatVO.builder()
                .dayValue(todayUsage != null ? todayUsage.getDdValue() : null)
                .monthValue(thisMonthUsage != null ? thisMonthUsage.getDdValue() : null)
                .yearValue(thisYearUsage != null ? thisYearUsage.getDdValue() : null)
                .qoqDayValue(preDayUsage != null ? preDayUsage.getDdValue() : null)
                .qoqMonthValue(preMonthUsage != null ? preMonthUsage.getDdValue() : null)
                .qoqYearValue(preYearUsage != null ? preYearUsage.getDdValue() : null)
                .build();
    }

    /**
     * 所有分类的总和统计
     *
     * @param param
     *
     * @return
     */
    @Override
    public EnergyDailyUsageStatVO getUsageStatWithCarbonForDMY(EnergyDailyUsageParam param) {
        LocalDateTime current = LocalDateTime.now();
        //查询日用量
        Period today = Period.of(DateUtils.beginOfDay(current), DateUtils.endOfDay(current));
        param.setPeriod(today);
        //        EnergyDailyUsageWithCarbonVO todayUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO todayUsage = this.getStat(today);
        //月用量
        Period thisMonth = Period.of(DateUtils.beginOfMonth(current), DateUtils.endOfMonth(current));
        param.setPeriod(thisMonth);
        //        EnergyDailyUsageWithCarbonVO thisMonthUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO thisMonthUsage = this.getStat(thisMonth);
        //年用量
        Period thisYear = Period.of(DateUtils.beginOfYear(current), DateUtils.endOfYear(current));
        param.setPeriod(thisYear);
        //        EnergyDailyUsageWithCarbonVO thisYearUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO thisYearUsage = this.getStat(thisYear);
        //环比日
        Period preDay = this.calQOQPeriod(today);
        param.setPeriod(preDay);
        //        EnergyDailyUsageWithCarbonVO preDayUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO preDayUsage = this.getStat(preDay);
        //环比月
        Period preMonth = this.calQOQPeriod(thisMonth);
        param.setPeriod(preMonth);
        //        EnergyDailyUsageWithCarbonVO preMonthUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO preMonthUsage = this.getStat(preMonth);
        //环比年
        Period preYear = this.calQOQPeriod(thisYear);
        param.setPeriod(preYear);
        //        EnergyDailyUsageWithCarbonVO preYearUsage = energyDailyUsageMapper.getUsageStatWithCarbonByTime(param);
        EnergyTypeStatVO preYearUsage = this.getStat(preYear);
        //取碳排值进行计算
        if ("CARBON".equals(param.getExportType())) {
            //构建返回对象
            return EnergyDailyUsageStatVO.builder()
                    .dayValue(todayUsage != null ? todayUsage.getDcValue() : null)
                    .monthValue(thisMonthUsage != null ? thisMonthUsage.getDcValue() : null)
                    .yearValue(thisYearUsage != null ? thisYearUsage.getDcValue() : null)
                    .qoqDayValue(preDayUsage != null ? preDayUsage.getDcValue() : null)
                    .qoqMonthValue(preMonthUsage != null ? preMonthUsage.getDcValue() : null)
                    .qoqYearValue(preYearUsage != null ? preYearUsage.getDcValue() : null)
                    .build();
        } else {
            //构建返回对象
            return EnergyDailyUsageStatVO.builder()
                    .dayValue(todayUsage != null ? todayUsage.getZhValue() : null)
                    .monthValue(thisMonthUsage != null ? thisMonthUsage.getZhValue() : null)
                    .yearValue(thisYearUsage != null ? thisYearUsage.getZhValue() : null)
                    .qoqDayValue(preDayUsage != null ? preDayUsage.getZhValue() : null)
                    .qoqMonthValue(preMonthUsage != null ? preMonthUsage.getZhValue() : null)
                    .qoqYearValue(preYearUsage != null ? preYearUsage.getZhValue() : null)
                    .build();
        }
    }

    @Override
    public List<TrendStatVO> getUsageToDcStatForTrend(EnergyTrendUsageParam param) {
        //初始化时间
        TimeStatUtil.initStatParam(param);
        return this.fillUsageToDcStatForTrend(param, this.baseMapper.getUsageToDcStatForTrend(param));
    }

    @Override
    public List<TrendStatVO> getUsageToDcStatByTimeAndType(EnergyTrendUsageParam param) {
        //初始化时间
        TimeStatUtil.initStatParam(param);
        return this.fillUsageToDcStatByTimeAndType(param, this.baseMapper.getUsageToDcStatByTimeAndType(param));
    }

    private List<TrendStatVO> fillUsageToDcStatForTrend(EnergyTrendUsageParam param, List<TrendStatVO> dataList) {
        List<TrendStatVO> sourceList = dataList == null ? new ArrayList<>() : dataList;
        // mapper 只返回有数据的时间桶，这里用完整时间轴补齐缺失点，避免趋势图断点。
        List<String> timeList = this.buildStatTimeList(param);
        if (CollectionUtils.isEmpty(timeList)) {
            return sourceList;
        }
        Map<String, TrendStatVO> dataMap = sourceList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTime()))
                .collect(Collectors.toMap(TrendStatVO::getTime, item -> item, (oldItem, newItem) -> oldItem));
        List<TrendStatVO> resultList = new ArrayList<>();
        for (String time : timeList) {
            TrendStatVO stat = dataMap.get(time);
            resultList.add(stat == null ? this.buildZeroTrendStat(time, null) : stat);
        }
        return resultList;
    }

    private List<TrendStatVO> fillUsageToDcStatByTimeAndType(EnergyTrendUsageParam param, List<TrendStatVO> dataList) {
        List<TrendStatVO> sourceList = dataList == null ? new ArrayList<>() : dataList;
        List<String> timeList = this.buildStatTimeList(param);
        List<String> nameList = this.buildInstrumentTypeNameList(param, sourceList);
        if (CollectionUtils.isEmpty(timeList)) {
            return sourceList;
        }
        if (CollectionUtils.isEmpty(nameList)) {
            // 没有类型维度时按新计划降级为一维补零，补出的数据 name 为空。
            return this.fillUsageToDcStatForTrend(param, sourceList);
        }
        // 有类型维度时按 time + name 二维补零，保证同一类型覆盖完整时间轴。
        Map<String, Map<String, TrendStatVO>> dataMap = sourceList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTime()) && StringUtils.isNotBlank(item.getName()))
                .collect(Collectors.groupingBy(TrendStatVO::getTime,
                                               Collectors.toMap(TrendStatVO::getName, item -> item, (oldItem, newItem) -> oldItem)));
        List<TrendStatVO> resultList = new ArrayList<>();
        for (String time : timeList) {
            Map<String, TrendStatVO> nameDataMap = dataMap.get(time);
            for (String name : nameList) {
                TrendStatVO stat = nameDataMap == null ? null : nameDataMap.get(name);
                resultList.add(stat == null ? this.buildZeroTrendStat(time, name) : stat);
            }
        }
        return resultList;
    }

    private List<String> buildStatTimeList(EnergyTrendUsageParam param) {
        List<String> timeList = new ArrayList<>();
        if (param == null || param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            return timeList;
        }
        if (MONTH_GROUP_FORMAT.equals(param.getGroupFormat())) {
            YearMonth current = YearMonth.from(param.getPeriod().getBegin());
            YearMonth end = YearMonth.from(param.getPeriod().getEnd());
            while (!current.isAfter(end)) {
                timeList.add(current.format(MONTH_FORMATTER));
                current = current.plusMonths(1);
            }
        } else {
            LocalDate current = param.getPeriod().getBegin().toLocalDate();
            LocalDate end = param.getPeriod().getEnd().toLocalDate();
            while (!current.isAfter(end)) {
                timeList.add(current.format(DAY_FORMATTER));
                current = current.plusDays(1);
            }
        }
        return timeList;
    }

    private List<String> buildInstrumentTypeNameList(EnergyTrendUsageParam param, List<TrendStatVO> dataList) {
        // 类型维度只从 mapper 结果和指定 instrumentType 推断，不额外查库扩大接口返回范围。
        List<String> nameList = dataList.stream()
                .map(TrendStatVO::getName)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (param != null && StringUtils.isNotBlank(param.getInstrumentType())) {
            String instrumentTypeName = InstrumentTypeEnum.getFireinspEnum(param.getInstrumentType());
            if (StringUtils.isNotBlank(instrumentTypeName) && !nameList.contains(instrumentTypeName)) {
                nameList.add(instrumentTypeName);
            }
        }
        return nameList;
    }

    private TrendStatVO buildZeroTrendStat(String time, String name) {
        return TrendStatVO.builder()
                .time(time)
                .name(name)
                .value(BigDecimal.ZERO)
                .dcValue(BigDecimal.ZERO)
                .zhValue(BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<EnergyUsageStatWithQoqVO> getUsageStatWithCarbonByTypeForAll(EnergyTrendUsageParam param) {
        //查询当期数据
        List<EnergyDailyUsageWithCarbonVO> usageStatWithCarbonByTypeList = this.baseMapper.getUsageStatWithCarbonByType(param);
        //查询上期数据
        Period preTime = this.calQOQPeriod(param.getPeriod());
        param.setPeriod(preTime);//设置上期时间
        List<EnergyDailyUsageWithCarbonVO> preUsageStatWithCarbonByTypeList = this.baseMapper.getUsageStatWithCarbonByType(param);
        //数据转换
        Map<String, EnergyDailyUsageWithCarbonVO> usageMap = usageStatWithCarbonByTypeList.stream()
                .collect(Collectors.toMap(EnergyDailyUsageWithCarbonVO::getInstrumentType, n -> n));
        Map<String, EnergyDailyUsageWithCarbonVO> preUsageMap = preUsageStatWithCarbonByTypeList.stream()
                .collect(Collectors.toMap(EnergyDailyUsageWithCarbonVO::getInstrumentType, n -> n));
        //构建返回的数据结构
        List<EnergyUsageStatWithQoqVO> finalDataList = new ArrayList<>();
        //碳排总量
        BigDecimal currentCarbon = BigDecimal.ZERO;//当期碳排
        BigDecimal preCarbon = BigDecimal.ZERO;//上期碳排
        //综合能耗总量
        BigDecimal currentZh = BigDecimal.ZERO;//当期综合
        BigDecimal preZh = BigDecimal.ZERO;//上期综合
        for (String key : usageMap.keySet()) {
            //获取当前数据
            EnergyDailyUsageWithCarbonVO current = usageMap.get(key);
            //获取上期数据
            EnergyDailyUsageWithCarbonVO qoq = preUsageMap.get(key);

            EnergyUsageStatWithQoqVO build = EnergyUsageStatWithQoqVO.builder()
                    .instrumentType(key)
                    .instrumentTypeName(InstrumentTypeEnum.getFireinspEnum(key))
                    .value(current != null ? current.getDValue() : null)
                    .qoqValue(qoq != null ? qoq.getDValue() : null)
                    .build();
            finalDataList.add(build);
            //累计碳排和综合能耗
            // 碳排  (碳排总量 = 电碳排 + 燃气碳排 +燃油碳排+集中供热碳排+集中供冷碳排-可再生能源碳排+其他碳排)
            if (InstrumentTypeEnum.RENEWABLE_ENERGY.getCode().equals(key)) {
                // 碳排是减去可再生能源
                currentCarbon = currentCarbon.subtract(
                        current != null && current.getDcValue() != null ? current.getDcValue() : BigDecimal.ZERO);
                preCarbon = preCarbon.subtract(qoq != null && qoq.getDcValue() != null ? qoq.getDcValue() : BigDecimal.ZERO);
            } else if (
                    InstrumentTypeEnum.ELECTRICITY_METER.getCode().equals(key)
                    || InstrumentTypeEnum.GAS_METER.getCode().equals(key)
                    || InstrumentTypeEnum.FUEL_METER.getCode().equals(key)
                    || InstrumentTypeEnum.HEAT_METER.getCode().equals(key)
                    || InstrumentTypeEnum.COOL_METER.getCode().equals(key)
                    || InstrumentTypeEnum.OTHER.getCode().equals(key)
            ) {
                currentCarbon = currentCarbon.add(current != null && current.getDcValue() != null ? current.getDcValue() : BigDecimal.ZERO);
                preCarbon = preCarbon.add(qoq != null && qoq.getDcValue() != null ? qoq.getDcValue() : BigDecimal.ZERO);
            }
            // 综合能耗
            currentZh = currentZh.add(current != null && current.getZhValue() != null ? current.getZhValue() : BigDecimal.ZERO);
            preZh = preZh.add(qoq != null && qoq.getZhValue() != null ? qoq.getZhValue() : BigDecimal.ZERO);
        }
        //构造 碳排和综合能耗 特殊节点
        //构造碳排
        finalDataList.add(EnergyUsageStatWithQoqVO.builder()
                                  .instrumentType("carbon")
                                  .instrumentTypeName("碳排")
                                  .value(currentCarbon)
                                  .qoqValue(preCarbon)
                                  .build());
        //构造综合
        finalDataList.add(EnergyUsageStatWithQoqVO.builder()
                                  .instrumentType("zh")
                                  .instrumentTypeName("综合能耗")
                                  .value(currentZh)
                                  .qoqValue(preZh)
                                  .build());
        return finalDataList;
    }

    private EnergyTypeStatVO getStat(Period period) {
        List<EnergyTypeStatVO> usageList = energyDailyUsageMapper.getUsageStatGroupType(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                                                .ge(EnergyDailyUsage::getStatDate, period.getBegin())
                                                                                                .lt(EnergyDailyUsage::getStatDate, period.getEnd())
                                                                                       );
        return EnergyStatUtil.getStat(usageList);
    }

    /**
     * 查询单个用能类型统计
     *
     * @param param period 时间段 instrumentType 用能类型
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    @Override
    public EnergyTypeStatVO getDailyUsageStatType(EnergyDailyUsageStatParam param) {
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            throw new ServiceException("时间段不能为空！");
        }
        if (StringUtils.isBlank(param.getInstrumentType())) {
            throw new ServiceException("用能类型不能为空！");
        }
        return energyDailyUsageMapper.getUsageStatAllIn(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                .eq(EnergyDailyUsage::getInstrumentType, param.getInstrumentType())
                                                                .ge(EnergyDailyUsage::getStatDate, param.getPeriod().getBegin())
                                                                .lt(EnergyDailyUsage::getStatDate, param.getPeriod().getEnd())
                                                       );
    }

    /**
     * 所有开启的用能类型统计
     *
     * @param param 时间段
     *
     * @return 每个分类的 用量 碳排 综合能耗 f p g j
     */
    @Override
    public List<EnergyTypeStatVO> getDailyUsageStatOn(EnergyDailyUsageStatParam param) {
        /* 获取开启的用能类型 */
        List<EnergyCollectConfig> configList = energyCollectConfigMapper.selectList(Wrappers.<EnergyCollectConfig>lambdaQuery()
                                                                                            .eq(EnergyCollectConfig::getCollectOn, Boolean.TRUE)
                                                                                   );
        if (configList == null || configList.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> instrumentTypeList = configList.stream().map(EnergyCollectConfig::getCategoryCode).collect(Collectors.toList());
        // 获取统计数据
        return this.getDailyUsageStatType(param.getPeriod(), instrumentTypeList);
    }

    /**
     * 所有开启的用能类型总和统计(类型加起来)
     *
     * @param param 时间段
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    @Override
    public EnergyTypeStatVO getDailyUsageStatAllOn(EnergyDailyUsageStatParam param) {
        /* 获取开启的用能类型 */
        List<EnergyCollectConfig> configList = energyCollectConfigMapper.selectList(Wrappers.<EnergyCollectConfig>lambdaQuery()
                                                                                            .eq(EnergyCollectConfig::getCollectOn, Boolean.TRUE)
                                                                                   );
        if (configList == null || configList.isEmpty()) {
            return new EnergyTypeStatVO();
        }
        List<String> instrumentTypeList = configList.stream().map(EnergyCollectConfig::getCategoryCode).collect(Collectors.toList());
        // 获取统计数据
        return this.getDailyUsageAllIn(param.getPeriod(), instrumentTypeList);
    }

    /**
     * 环比
     *
     * @param param 条件
     *
     * @return 环比
     */
    @Override
    public EnergyDailyUsageRingRatioVO getDailyUsageRingRatio(EnergyRingRatioQueryParam param) {
        EnergyDailyUsageRingRatioVO result = new EnergyDailyUsageRingRatioVO();
        Period period = param.getPeriod();
        Assert.isParameterError(period == null || period.getBegin() == null || period.getEnd() == null, "时间段不能为空！");
        Assert.isNotFound(param.getTimeType(), "环比类型不能为空！");
        // 当期数据
        EnergyTypeStatVO typeStatVO = energyDailyUsageMapper.getUsageStatAllIn(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                                       .between(EnergyDailyUsage::getStatDate, period.getBegin(), period.getEnd())
                                                                                       .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsage::getInstrumentType, param.getInstrumentType())

                                                                              );
        if (typeStatVO != null) {
            result.setCurrentDdValue(typeStatVO.getDdValue());
            result.setCurrentDcValue(typeStatVO.getDcValue());
            result.setCurrentZhValue(typeStatVO.getZhValue());
        }
        Period ringPeriod;
        if (TimePeriodQueryEnum.TODAY.name().equals(param.getTimeType())) {
            ringPeriod = new Period(DateUtils.offsetDay(period.getBegin(), -1), DateUtils.offsetDay(period.getEnd(), -1));
        } else if (TimePeriodQueryEnum.THIS_WEEK.name().equals(param.getTimeType())) {
            ringPeriod = new Period(DateUtils.offsetDay(period.getBegin(), -7), DateUtils.offsetDay(period.getEnd(), -7));
        } else if (TimePeriodQueryEnum.THIS_MONTH.name().equals(param.getTimeType())) {
            ringPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -1), DateUtils.offsetMonth(period.getEnd(), -1));
        } else if (TimePeriodQueryEnum.RECENT_THREE_MONTH.name().equals(param.getTimeType())) {
            ringPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -3), DateUtils.offsetMonth(period.getEnd(), -3));
        } else if (TimePeriodQueryEnum.RECENT_SIX_MONTH.name().equals(param.getTimeType())) {
            ringPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -6), DateUtils.offsetMonth(period.getEnd(), -6));
        } else {
            ringPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -12), DateUtils.offsetMonth(period.getEnd(), -12));
        }
        // 环比数据
        EnergyTypeStatVO ringTypeStatVO = energyDailyUsageMapper.getUsageStatAllIn(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                                           .between(EnergyDailyUsage::getStatDate, ringPeriod.getBegin(), ringPeriod.getEnd())
                                                                                           .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyDailyUsage::getInstrumentType, param.getInstrumentType())

                                                                                  );
        if (ringTypeStatVO != null) {
            result.setRingRatioDdValue(ringTypeStatVO.getDdValue());
            result.setRingRatioDcValue(ringTypeStatVO.getDcValue());
            result.setRingRatioZhValue(ringTypeStatVO.getZhValue());
        }
        return result;
    }

    /**
     * 统计（使用每日数据统计，按类型统计）
     *
     * @param period             时间段
     * @param instrumentTypeList 用能类型
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    @Override
    public List<EnergyTypeStatVO> getDailyUsageStatType(Period period, List<String> instrumentTypeList) {
        if (period == null || period.getBegin() == null || period.getEnd() == null) {
            throw new ServiceException("时间段不能为空！");
        }
        return energyDailyUsageMapper.getUsageStatGroupType(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                    .ge(EnergyDailyUsage::getStatDate, period.getBegin())
                                                                    .lt(EnergyDailyUsage::getStatDate, period.getEnd())
                                                                    .in(CollectionUtils.isNotEmpty(instrumentTypeList), EnergyDailyUsage::getInstrumentType, instrumentTypeList)
                                                           );
    }

    /**
     * 统计分类的总和(分类加起来统计)
     *
     * @param period             时间段
     * @param instrumentTypeList 用能类型集合
     *
     * @return 用能 碳排 综合能耗 f p g j
     */
    private EnergyTypeStatVO getDailyUsageAllIn(Period period, List<String> instrumentTypeList) {
        if (period == null || period.getBegin() == null || period.getEnd() == null) {
            throw new ServiceException("时间段不能为空！");
        }
        return energyDailyUsageMapper.getUsageStatAllIn(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                .ge(EnergyDailyUsage::getStatDate, period.getBegin())
                                                                .lt(EnergyDailyUsage::getStatDate, period.getEnd())
                                                                .in(CollectionUtils.isNotEmpty(instrumentTypeList), EnergyDailyUsage::getInstrumentType, instrumentTypeList)
                                                       );
    }
}
