package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyChargeReportMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyChargeRulesMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailySummaryMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.StairsEnergyContentsDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeReport;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailySummary;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.StairsEnergyTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AreaProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeReportService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.RouteProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRulesVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ProjectVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 本任务负责 将Influxdb中的日用量进行统计，然后存放到mysql中
 * T+1方式统计日用量
 * <p>
 * 注意 这里面有两个定时任务
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyProjectDailyAlyJob {
    private final EnergyDailyUsageMapper energyDailyUsageMapper;
    private final EnergyDailyUsageProjectMapper energyDailyUsageProjectMapper;
    private final EnergyDailyUsageProjectService energyDailyUsageProjectService;
    private final EnergyProjectService energyProjectService;
    private final AreaProjectService areaProjectService;
    private final RouteProjectService routeProjectService;
    private final EnergyCollectConfigMapper energyCollectConfigMapper;
    private final MeterDeviceMapper meterDeviceMapper;
    private final EnergyDailySummaryMapper energyDailySummaryMapper;
    private final EnergyChargeRulesMapper energyChargeRulesMapper;
    private final EnergyProjectItemMapper energyProjectItemMapper;
    private final EnergyDailyUsageService energyDailyUsageService;
    private final EnergyChargeReportService energyChargeReportService;
    private final EnergyChargeReportMapper energyChargeReportMapper;
    private final EnergyJobParamHandler energyJobParamHandler;

    /**
     * 每日统计用量
     *
     * @param param 入参 租户之间用“;”分割，租户内参数用“,”分割
     *              例如：58197260,2025-09-22 00:12:12;51436584,2025-09-23 00:00:00
     *              租户内的参数可以只传租户不要后面的日期，例如：58197260;51436584
     */
    @JobAnnotation(name = "EnergyProjectDailyUsage", cron = "0 0/10 * * * ?", desc = "能耗-每日用量统计")
    public void energyProjectDailyUsage(String param) {
        // 获取字典参数
        var variable = StringUtils.isBlank(param) ? energyJobParamHandler.getJobParam("EnergyProjectDailyUsage") : param;
        if (StringUtils.isBlank(variable)) {
            log.warn("EnergyProjectDailyUsage没有可执行租户");
            return;
        }
        var variables = variable.split(";");
        for (var var : variables) {
            // var 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18 13:50:00)
            energyProjectDailyUsageByTenant(var);
        }
    }

    /**
     * 每个租户的每日统计用量
     *
     * @param param 参数 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18 13:50:00)
     */
    public void energyProjectDailyUsageByTenant(String param) {
        // 如果有传入就使用传入的时间，如果没有传入时间就统计当前时间往前推半小时时间(如果直接统计当前时间，在0点时前一天的最后一次数据还没入influxDB库)
        var start = System.currentTimeMillis();
        log.info("每日统计用量开始：{}", start);
        //注意设置tenant参数
        var params = param.split(",");
        var tenant = Integer.valueOf(params[0]);
        try {
            SessionContextHolder.requestTenant(tenant);
            //获取用于统计的设备
            var deviceForStat = meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery()
                                                                     .isNull(MeterDevice::getParentId)
                                                                     .or().eq(MeterDevice::getParentId, "")
                                                            ).stream().map(d -> Long.valueOf(d.getDeviceId()))
                    .collect(Collectors.toList());
            //areaProjectService.getDeviceForStat();
            log.info("日用量统计表ID：{}", JsonUtils.serialize(deviceForStat));
            Map<String, EnergyCollectConfigVO> configFuelMap = new HashMap<>();
            var collectConfigFuelList = energyCollectConfigMapper.getEnergyCollectConfigFuelList(Wrappers.emptyWrapper());
            if (collectConfigFuelList != null) {
                for (EnergyCollectConfigVO collectConfigVO : collectConfigFuelList) {
                    if (StringUtils.isNotEmpty(collectConfigVO.getCategoryCode())) {
                        configFuelMap.put(collectConfigVO.getCategoryCode(), collectConfigVO);
                    }
                }
            }
            // 如果有传入就使用传入的时间，如果没有传入时间就当前时间往前推半小时(如果直接统计当前时间，在0点时前一天的最后一次数据还没入influxDB库)
            var current =
                    params.length > 1 ? DateUtils.parse(params[1]) : DateUtils.offset(LocalDateTime.now(), ChronoUnit.MINUTES, -30);
            //统计时段
            // UTC时间要-8小时按秒算就是28800,但一天的统计时间是左开右闭，所以是-28799秒
            var dayStartTime = DateUtils.offset(DateUtils.beginOfDay(current), ChronoUnit.SECONDS, -28799);
            var dayEndTime = DateUtils.offset(dayStartTime, ChronoUnit.DAYS, 1);
            var startTime = DateUtils.formatDate(dayStartTime, DateUtils.DEFAULT_UTC_FORMATTER);
            var endTime = DateUtils.formatDate(dayEndTime, DateUtils.DEFAULT_UTC_FORMATTER);
            //日常用量
            var dvalue = this.getQuery(startTime, endTime, "value", "instrumentType", tenant, deviceForStat);
            //峰值用量
            var fvalue = this.getQuery(startTime, endTime, "fvalue", "instrumentType", tenant, deviceForStat);
            //平时用量
            var pvalue = this.getQuery(startTime, endTime, "pvalue", "instrumentType", tenant, deviceForStat);
            //谷时用量
            var gvalue = this.getQuery(startTime, endTime, "gvalue", "instrumentType", tenant, deviceForStat);
            //尖时用量
            var jvalue = this.getQuery(startTime, endTime, "jvalue", "instrumentType", tenant, deviceForStat);

            var dquery = InfluxdbUtil.execQueryForList(dvalue, CommonStatVO.class);
            var fquery = InfluxdbUtil.execQueryForList(fvalue, CommonStatVO.class);
            var pquery = InfluxdbUtil.execQueryForList(pvalue, CommonStatVO.class);
            var gquery = InfluxdbUtil.execQueryForList(gvalue, CommonStatVO.class);
            var jquery = InfluxdbUtil.execQueryForList(jvalue, CommonStatVO.class);
            //清洗入库
            this.saveDailyUsageToMysql(dquery, fquery, pquery, gquery, jquery, current, tenant, configFuelMap);
        } catch (Exception e) {
            log.error("能耗分类统计失败,失败原因:", e);
        }
        log.info("每日统计用量结束，耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);

    }

    private void saveDailyUsageToMysql(List<CommonStatVO> dquery,
                                       List<CommonStatVO> fquery,
                                       List<CommonStatVO> pquery,
                                       List<CommonStatVO> gquery,
                                       List<CommonStatVO> jquery,
                                       LocalDateTime current, Integer tenant,
                                       Map<String, EnergyCollectConfigVO> configFuelMap
                                      ) {
        List<EnergyDailyUsage> dataList = new ArrayList<>();
        EnergyDailySummary energyDailySummary = null;
        LocalDateTime dayTime = DateUtils.beginOfDay(current);
        String dayStr = DateUtils.formatDate(current);
        if (!CollectionUtils.isEmpty(dquery)) {
            energyDailySummary = new EnergyDailySummary();
            energyDailySummary.setStatDate(dayTime);
            energyDailySummary.setDateStr(dayStr);
            BigDecimal summaryDdValue = BigDecimal.ZERO;
            BigDecimal summaryDcValue = BigDecimal.ZERO;
            BigDecimal summaryZhValue = BigDecimal.ZERO;
            BigDecimal summaryFValue = BigDecimal.ZERO;
            BigDecimal summaryPValue = BigDecimal.ZERO;
            BigDecimal summaryGValue = BigDecimal.ZERO;
            BigDecimal summaryJValue = BigDecimal.ZERO;
            for (CommonStatVO stat : dquery) {
                // 碳排因子
                BigDecimal carbonFactor = BigDecimal.ZERO;
                // 计算系数
                BigDecimal calculateProportion = BigDecimal.ZERO;
                EnergyCollectConfigVO configVO = configFuelMap.get(stat.getInstrumentType());
                if (configVO != null) {
                    if (configVO.getCarbonFactor() != null) {
                        carbonFactor = configVO.getCarbonFactor();
                    }
                    if (configVO.getCalculateProportion() != null) {
                        calculateProportion = configVO.getCalculateProportion();
                    }
                }
                //构建实体
                EnergyDailyUsage dailyUsage = EnergyDailyUsage.builder()
                        .statDate(current)
                        .instrumentType(stat.getInstrumentType())
                        .dValue(stat.getValue())
                        .build();
                for (CommonStatVO fstat : fquery) {
                    if (dailyUsage.getInstrumentType().equals(fstat.getInstrumentType())) {
                        dailyUsage.setFValue(fstat.getFvalue());
                        break;
                    }
                }
                for (CommonStatVO pstat : pquery) {
                    if (dailyUsage.getInstrumentType().equals(pstat.getInstrumentType())) {
                        dailyUsage.setPValue(pstat.getPvalue());
                        break;
                    }
                }
                for (CommonStatVO gstat : gquery) {
                    if (dailyUsage.getInstrumentType().equals(gstat.getInstrumentType())) {
                        dailyUsage.setGValue(gstat.getGvalue());
                        break;
                    }
                }
                for (CommonStatVO jstat : jquery) {
                    if (dailyUsage.getInstrumentType().equals(jstat.getInstrumentType())) {
                        dailyUsage.setJValue(jstat.getJvalue());
                        break;
                    }
                }
                //仪表名称
                dailyUsage.setInstrumentTypeName(InstrumentTypeEnum.getFireinspEnum(stat.getInstrumentType()));
                dailyUsage.setTenant(tenant);
                EntityAuditor.populateInnerAudit(dailyUsage);
                dailyUsage.setDeleted(false);
                if (dailyUsage.getDValue() != null) {
                    dailyUsage.setDcValue(dailyUsage.getDValue().multiply(carbonFactor));
                    dailyUsage.setZhValue(dailyUsage.getDValue().multiply(calculateProportion));
                }
                dataList.add(dailyUsage);
                /* 处理每日汇总数据 */
                if (dailyUsage.getDValue() != null) {
                    summaryDdValue = summaryDdValue.add(dailyUsage.getDValue());
                }
                if (dailyUsage.getZhValue() != null) {
                    summaryZhValue = summaryZhValue.add(dailyUsage.getZhValue());
                }
                if (dailyUsage.getFValue() != null) {
                    summaryFValue = summaryFValue.add(dailyUsage.getFValue());
                }
                if (dailyUsage.getPValue() != null) {
                    summaryPValue = summaryPValue.add(dailyUsage.getPValue());
                }
                if (dailyUsage.getGValue() != null) {
                    summaryGValue = summaryGValue.add(dailyUsage.getGValue());
                }
                if (dailyUsage.getJValue() != null) {
                    summaryJValue = summaryJValue.add(dailyUsage.getJValue());
                }
                if (dailyUsage.getDcValue() != null) {
                    // 碳排  (碳排总量 = 电碳排 + 燃气碳排 +燃油碳排+集中供热碳排+集中供冷碳排-可再生能源碳排+其他碳排)
                    if (
                            InstrumentTypeEnum.ELECTRICITY_METER.getCode().equals(stat.getInstrumentType())
                            || InstrumentTypeEnum.GAS_METER.getCode().equals(stat.getInstrumentType())
                            || InstrumentTypeEnum.FUEL_METER.getCode().equals(stat.getInstrumentType())
                            || InstrumentTypeEnum.HEAT_METER.getCode().equals(stat.getInstrumentType())
                            || InstrumentTypeEnum.COOL_METER.getCode().equals(stat.getInstrumentType())
                            || InstrumentTypeEnum.OTHER.getCode().equals(stat.getInstrumentType())
                    ) {
                        summaryDcValue = summaryDcValue.add(dailyUsage.getDcValue());
                    } else if (InstrumentTypeEnum.RENEWABLE_ENERGY.getCode().equals(stat.getInstrumentType())) {
                        summaryDcValue = summaryDcValue.subtract(dailyUsage.getDcValue());
                    }
                }
            }
            energyDailySummary.setDdValue(summaryDdValue);
            energyDailySummary.setDcValue(summaryDcValue);
            energyDailySummary.setZhValue(summaryZhValue);
            energyDailySummary.setFValue(summaryFValue);
            energyDailySummary.setPValue(summaryPValue);
            energyDailySummary.setGValue(summaryGValue);
            energyDailySummary.setJValue(summaryJValue);
            energyDailySummary.setDeleted(false);
            energyDailySummary.setTenant(tenant);
            EntityAuditor.populateAudit(energyDailySummary, "admin", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, current);
        }

        //保存
        if (CollectionUtils.isNotEmpty(dataList)) {
            energyDailyUsageMapper.delete(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                  .eq(EnergyDailyUsage::getStatDate, dayTime)
                                         );
            energyDailyUsageService.saveBatch(dataList);
        }

        if (energyDailySummary != null) {
            LocalDate statDate = LocalDate.parse(dayStr, DateTimeFormatter.ISO_LOCAL_DATE);
            EnergyDailySummary dailySummary = energyDailySummaryMapper.selectOne(Wrappers.<EnergyDailySummary>lambdaQuery()
                                                                                         .eq(EnergyDailySummary::getStatDate, statDate)
                                                                                         .orderByDesc(EnergyDailySummary::getCreateTime)
                                                                                         .last(" limit 1 ")
                                                                                );
            if (dailySummary == null) {
                energyDailySummaryMapper.insert(energyDailySummary);
            } else {
                energyDailySummary.setId(dailySummary.getId());
                EntityAuditor.populateCreateAudit(energyDailySummary, dailySummary.getCreateBy(), dailySummary.getCreatorId(),
                                                  dailySummary.getCreatorType(), dailySummary.getCreateTime());
                energyDailySummaryMapper.updateById(energyDailySummary);
            }
        }
    }

    /**
     * 构建统计语句
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param sumField   汇总的字段
     * @param groupField 分组的字段
     * @param tenant     租户
     * @param deviceIds  设备ids
     *
     * @return sql
     */
    private String getQuery(String startTime, String endTime, String sumField, String groupField, Integer tenant, List<Long> deviceIds) {
        return "from(bucket: \"energy\")\n" +
               "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
               "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
               "  |> filter(fn: (r) => r[\"tenant\"]==\"" + tenant + "\" " +
               InfluxdbUtil.getInfluxdbDeviceIdQuery(deviceIds) + ")\n" +
               "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
               "  |> group(columns: [\"" + groupField + "\"])\n" +
               "  |> sum(column: \"" + sumField + "\")";
    }

    /**
     * 分项类型的业务日统计，例如：分项、区域、支路
     *
     * @param param 入参 租户之间用“;”分割，租户内参数用“,”分割
     *              例如：58197260,2025-09-22 00:12:12;51436584,2025-09-23 00:00:00
     *              租户内的参数可以只传租户不要后面的日期，例如：58197260;51436584
     */
    @JobAnnotation(name = "EnergyProjectDailyUsageForProject", cron = "0 0/10 * * * ?", desc = "能耗-分项数据每日统计")
    public void energyProjectDailyUsageForProject(String param) {
        // 获取字典参数
        var variable = energyJobParamHandler.getJobParam("EnergyProjectDailyUsageForProject");
        if (StringUtils.isBlank(variable)) {
            log.warn("EnergyProjectDailyUsageForProject没有可执行租户");
            return;
        }
        var variables = variable.split(";");
        for (var var : variables) {
            // var 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18 13:50:00)
            energyProjectDailyUsageForProjectByTenant(var);
        }
    }

    /**
     * 每个租户的分项类型的业务日统计，例如：分项、区域、支路
     *
     * @param param 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18 13:50:00)
     *
     */
    public void energyProjectDailyUsageForProjectByTenant(String param) {
        // param 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18 13:50:00)
        // 如果有传入就使用传入的时间，如果没有传入时间就统计当前时间往前推半小时时间(如果直接统计当前时间，在0点时前一天的最后一次数据还没入influxDB库)
        var start = System.currentTimeMillis();
        log.info("每日分项统计用量开始：" + start);
        //注意在 设置tenant参数
        var params = param.split(",");
        var tenant = Integer.valueOf(params[0]);
        SessionContextHolder.requestTenant(tenant);
        try {
            // 如果有传入就使用传入的时间，如果没有传入时间就当前时间往前推半小时(如果直接统计当前时间，在0点时前一天的最后一次数据还没入influxDB库)
            var current =
                    params.length > 1 ? DateUtils.parse(params[1]) : DateUtils.offset(LocalDateTime.now(), ChronoUnit.MINUTES, 30);
            //统计时段
            var startTime = DateUtils.formatDate(DateUtils.offset(DateUtils.beginOfDay(current), ChronoUnit.HOURS, -8), DateUtils.DEFAULT_UTC_FORMATTER);
            var endTime = DateUtils.formatDate(DateUtils.offset(DateUtils.endOfDay(current), ChronoUnit.HOURS, -8), DateUtils.DEFAULT_UTC_FORMATTER);
            //日常用量
            var projectQuery = this.getQueryForProject("projectHour", startTime, endTime, "value", tenant, "\"instrumentType\",\"projectId\"");
            var areaQuery = this.getQueryForProject("areaHour", startTime, endTime, "value", tenant, "\"instrumentType\",\"areaId\"");
            //分项日统计
            var projectQueryList = InfluxdbUtil.execQueryForList(projectQuery, CommonStatVO.class);
            //区域日统计
            var areaQueryList = InfluxdbUtil.execQueryForList(areaQuery, CommonStatVO.class);
            Map<String, EnergyCollectConfigVO> configFuelMap = new HashMap<>();
            var collectConfigFuelList = energyCollectConfigMapper.getEnergyCollectConfigFuelList(Wrappers.emptyWrapper());
            if (collectConfigFuelList != null) {
                for (var collectConfigVO : collectConfigFuelList) {
                    if (StringUtils.isNotEmpty(collectConfigVO.getCategoryCode())) {
                        configFuelMap.put(collectConfigVO.getCategoryCode(), collectConfigVO);
                    }
                }
            }
            //入库
            this.saveDailyUsageProjectToMysql(projectQueryList, BindTypeEnum.ITEM.name(), current, tenant, configFuelMap);
            this.saveDailyUsageProjectToMysql(areaQueryList, BindTypeEnum.AREA.name(), current, tenant, configFuelMap);
        } catch (Exception e) {
            log.error("能耗分项统计,失败原因:", e);
        }
        log.info("每日分项统计用量结束，耗时：" + (System.currentTimeMillis() - start) / 1000 + " 秒");
    }

    private String getQueryForProject(String table, String startTime, String endTime, String sumField, Integer tenant, String groupFields) {
        return "from(bucket: \"energy\")\n" +
               "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
               "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + table + "\")\n" +
               "  |> filter(fn: (r) => r[\"tenant\"]==\"" + tenant + "\")\n" +
               "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
               "  |> group(columns: [" + groupFields + "])\n" +
               "  |> sum(column: \"" + sumField + "\")";
    }

    private void saveDailyUsageProjectToMysql(List<CommonStatVO> dataList, String projectCategory,
                                              LocalDateTime current, Integer tenant, Map<String, EnergyCollectConfigVO> configFuelMap
                                             ) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            LocalDateTime startCurrent = DateUtils.beginOfDay(current);
            //projectCategory 取值为 ITEM 分项 AREA 区域 ROUTE 支路
            Map<String, ProjectVO> projectMap = new HashMap<>();
            if (projectCategory.equals(BindTypeEnum.ITEM.name())) {
                //取出分项的ID
                List<Long> projectIdList = dataList.stream().map(d -> Long.valueOf(d.getProjectId())).collect(Collectors.toList());
                List<EnergyProject> energyProjects = energyProjectService.listByIds(projectIdList);
                projectMap = energyProjects.stream().collect(Collectors.toMap(d -> d.getId().toString(),
                                                                              d -> ProjectVO.builder().id(d.getId())
                                                                                      .projectCode(d.getCode())
                                                                                      .projectName(d.getProjectName()).build()));
            } else if (projectCategory.equals(BindTypeEnum.AREA.name())) {
                //取出分项的ID
                List<Long> projectIdList = dataList.stream().map(d -> Long.valueOf(d.getAreaId())).collect(Collectors.toList());
                List<AreaProject> areaProjects = areaProjectService.listByIds(projectIdList);
                projectMap = areaProjects.stream().collect(Collectors.toMap(d -> d.getId().toString(),
                                                                            d -> ProjectVO.builder().id(d.getId())
                                                                                    .projectName(d.getAreaName()).build()));
            } else if (projectCategory.equals(BindTypeEnum.ROUTE.name())) {
                //取出分项的ID
                List<Long> projectIdList = dataList.stream().map(d -> Long.valueOf(d.getRouteId())).collect(Collectors.toList());
                List<RouteProject> routeProjects = routeProjectService.listByIds(projectIdList);
                projectMap = routeProjects.stream().collect(Collectors.toMap(d -> d.getId().toString(),
                                                                             d -> ProjectVO.builder().id(d.getId())
                                                                                     .projectName(d.getRouteName()).build()));
            }
            Map<String, ProjectVO> finalProjectMap = projectMap;
            //转换数据
            List<EnergyDailyUsageProject> energyDailyUsageProjectList = dataList.stream().map(d -> {
                // 碳排因子
                BigDecimal carbonFactor = BigDecimal.ZERO;
                // 计算系数
                BigDecimal calculateProportion = BigDecimal.ZERO;
                EnergyCollectConfigVO configVO = configFuelMap.get(d.getInstrumentType());
                if (configVO != null) {
                    if (configVO.getCarbonFactor() != null) {
                        carbonFactor = configVO.getCarbonFactor();
                    }
                    if (configVO.getCalculateProportion() != null) {
                        calculateProportion = configVO.getCalculateProportion();
                    }
                }

                //处理projectId
                String projectId = StringUtils.isNotEmpty(d.getProjectId()) ? d.getProjectId()
                                                                            : (StringUtils.isNotEmpty(d.getAreaId()) ? d.getAreaId()
                                                                                                                     : d.getRouteId());

                ProjectVO projectVO = finalProjectMap.get(projectId);

                BigDecimal dcValue = BigDecimal.ZERO;
                BigDecimal zhValue = BigDecimal.ZERO;
                if (d.getValue() != null) {
                    dcValue = d.getValue().multiply(carbonFactor);
                    zhValue = d.getValue().multiply(calculateProportion);
                }
                return EnergyDailyUsageProject.builder()
                        .statDate(current)
                        .instrumentType(d.getInstrumentType())
                        .instrumentTypeName(InstrumentTypeEnum.getFireinspEnum(d.getInstrumentType()))
                        .projectCategory(projectCategory)
                        .projectId(StringUtils.isBlank(projectId) ? null : Long.valueOf(projectId))
                        .projectCode(projectVO != null ? projectVO.getProjectCode() : "")
                        .projectName(projectVO != null ? projectVO.getProjectName() : "")
                        .dValue(d.getValue())
                        .dcValue(dcValue)
                        .zhValue(zhValue)
                        .tenant(tenant)
                        .createBy("admin")
                        .creatorType(ActorTypeEnum.SYSTEM)
                        .createTime(current)
                        .updateBy("admin")
                        .updaterType(ActorTypeEnum.SYSTEM)
                        .updateTime(current)
                        .deleted(false)
                        .build();
            }).collect(Collectors.toList());
            energyDailyUsageProjectMapper.delete(Wrappers.<EnergyDailyUsageProject>lambdaQuery()
                                                         .eq(EnergyDailyUsageProject::getProjectCategory, projectCategory)
                                                         .eq(EnergyDailyUsageProject::getStatDate, startCurrent)
                                                );

            //保存
            energyDailyUsageProjectService.saveBatch(energyDailyUsageProjectList);

        }
    }

    /**
     * 能耗-能耗费用统计
     *
     * @param param @param param 入参 租户之间用“;”分割，租户内参数用“,”分割
     *              例如：58197260,2025-09-22 00:12:12;51436584,2025-09-23 00:00:00
     *              租户内的参数可以只传租户不要后面的日期，例如：58197260;51436584
     */
    @JobAnnotation(name = "energyAreaCostStat", cron = "0 0 4 * * ?", desc = "能耗-能耗费用统计")
    public void energyAreaCostStat(String param) {
        var variable = StringUtils.isBlank(param) ? energyJobParamHandler.getJobParam("energyAreaCostStat") : param;
        if (StringUtils.isBlank(variable)) {
            log.warn("energyAreaCostStat没有可执行租户");
            return;
        }
        var variables = variable.split(";");
        for (var var : variables) {
            // var 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18)
            energyAreaCostStatByTenant(var);
        }
    }

    /**
     * 能耗-能耗费用统计
     *
     * @param param 入参 可以只传租户id(如：58197260)  也可以是租户+逗号+日期(如：58197260,2025-05-18)
     */
    public void energyAreaCostStatByTenant(String param) {
        var params = param.split(",");
        var tenant = Integer.valueOf(params[0]);
        SessionContextHolder.requestTenant(tenant);

        LocalDateTime yesterdayStartTime;
        if (params.length > 1) {
            try {
                yesterdayStartTime = DateUtils.parseDate(params[1]);
            } catch (Exception e) {
                throw new InvalidArgumentException("参数日期格式为yyyy-MM-dd");
            }
        } else {
            yesterdayStartTime = DateUtils.offsetDay(DateUtils.beginOfDay(LocalDateTime.now()), -1);
        }
        var nowTime = LocalDateTime.now().withNano(BigDecimal.ONE.intValue());

        // 统计该天数据
        this.handlerEnergyCharge(yesterdayStartTime, nowTime, tenant);
        // 移除该天的旧数据
        energyChargeReportService.remove(Wrappers.<EnergyChargeReport>lambdaQuery()
                                                 .eq(EnergyChargeReport::getStatDate, yesterdayStartTime)
                                                 .ne(EnergyChargeReport::getCreateTime, nowTime)
                                        );

    }

    /**
     * @param dayTime 统计日的开始时间
     * @param nowTime 当前时间(统一创建时间)
     * @param tenant  租户
     */
    private void handlerEnergyCharge(LocalDateTime dayTime, LocalDateTime nowTime, Integer tenant) {
        String dayTimeStr = DateUtils.formatDate(dayTime);
        /* 区域id与名称对应的map*/
        Map<Long, String> areaIdNameMap = new HashMap<>();
        /* 用能类型下的区域的map */
        Map<String, List<AreaProject>> typeAreaMap = new HashMap<>();
        List<AreaProject> areaProjectList = areaProjectService.list(Wrappers.emptyWrapper());
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(areaProjectList)) {
            for (AreaProject areaProject : areaProjectList) {
                areaIdNameMap.put(areaProject.getId(), areaProject.getAreaName());
            }
            typeAreaMap = areaProjectList.stream().collect(Collectors.groupingBy(AreaProject::getInstrumentType));
        }
        /* 获取区域对应的设备id */
        Map<Long, List<Long>> areaLinkDeviceIdMap = areaProjectService.getAreaLinkDevice(null);
        /* 获取用能类型 */
        List<EnergyCollectConfig> collectConfigList = energyCollectConfigMapper.selectList(Wrappers.<EnergyCollectConfig>lambdaQuery()
                                                                                                   .select(EnergyCollectConfig::getCategoryCode, EnergyCollectConfig::getCategoryName)
                                                                                                   .groupBy(EnergyCollectConfig::getCategoryCode)
                                                                                          );
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(collectConfigList)) {
            log.warn("能耗每日费用统计，用能类型为空");
            return;
        }
        /* 获取规则列表 */
        EnergyChargeRulesQueryParam param = new EnergyChargeRulesQueryParam();
        param.setEffectiveTime(DateUtils.endOfDay(dayTime));
        Map<String, EnergyChargeRulesVO> rulesVOMap = new HashMap<>();
        List<EnergyChargeRulesVO> rulesList = energyChargeRulesMapper.getInstrumentEffectiveRule(param);
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(rulesList)) {
            log.warn("能耗每日费用统计，规则列表为空");
            return;
        }
        for (EnergyChargeRulesVO energyChargeRulesVO : rulesList) {
            rulesVOMap.put(energyChargeRulesVO.getInstrumentType(), energyChargeRulesVO);
        }
        /* 获取月度和年度用量 */
        Map<String, EnergyTypeStatVO> monthUserMap = new HashMap<>();
        Period monthPeriod = new Period(DateUtils.beginOfMonth(dayTime), DateUtils.endOfMonth(dayTime));
        List<EnergyTypeStatVO> monthUseList = energyDailyUsageService.getDailyUsageStatType(monthPeriod, collectConfigList.stream()
                .map(EnergyCollectConfig::getCategoryCode).collect(Collectors.toList()));
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(monthUseList)) {
            for (EnergyTypeStatVO typeStatVO : monthUseList) {
                monthUserMap.put(typeStatVO.getInstrumentType(), typeStatVO);
            }
        }
        Map<String, EnergyTypeStatVO> yearUseMap = new HashMap<>();
        Period yearPeriod = new Period(DateUtils.beginOfMonth(dayTime), DateUtils.endOfMonth(dayTime));
        List<EnergyTypeStatVO> yearUseList = energyDailyUsageService.getDailyUsageStatType(yearPeriod, collectConfigList.stream()
                .map(EnergyCollectConfig::getCategoryCode).collect(Collectors.toList()));
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(yearUseList)) {
            for (EnergyTypeStatVO typeStatVO : yearUseList) {
                yearUseMap.put(typeStatVO.getInstrumentType(), typeStatVO);
            }
        }
        /* 获取需要统计的设备id */
        List<Long> deviceIdList = new ArrayList<>();
        List<EnergyProjectItem> energyProjectItemList = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                                   .select(EnergyProjectItem::getDeviceId)
                                                                                          );
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(energyProjectItemList)) {
            deviceIdList = energyProjectItemList.stream().map(EnergyProjectItem::getDeviceId).distinct().collect(Collectors.toList());
        }

        /* 获取设备读数 */
        String startTime = DateUtils.formatDate(DateUtils.offset(dayTime, ChronoUnit.HOURS, -8), DateUtils.DEFAULT_UTC_FORMATTER);
        String endTime = DateUtils.formatDate(DateUtils.offset(DateUtils.endOfDay(dayTime), ChronoUnit.HOURS, -8), DateUtils.DEFAULT_UTC_FORMATTER);
        //日用量(总用量，即 fpgj用量 + 非峰平谷尖的用量)
        String tvalue = this.getQuery(startTime, endTime, "value", "deviceId", tenant, deviceIdList);
        //峰值用量
        String fvalue = this.getQuery(startTime, endTime, "fvalue", "deviceId", tenant, deviceIdList);
        //平时用量
        String pvalue = this.getQuery(startTime, endTime, "pvalue", "deviceId", tenant, deviceIdList);
        //谷时用量
        String gvalue = this.getQuery(startTime, endTime, "gvalue", "deviceId", tenant, deviceIdList);
        //尖时用量
        String jvalue = this.getQuery(startTime, endTime, "jvalue", "deviceId", tenant, deviceIdList);
        List<CommonStatVO> tquery = InfluxdbUtil.execQueryForList(tvalue, CommonStatVO.class);
        List<CommonStatVO> fquery = InfluxdbUtil.execQueryForList(fvalue, CommonStatVO.class);
        List<CommonStatVO> pquery = InfluxdbUtil.execQueryForList(pvalue, CommonStatVO.class);
        List<CommonStatVO> gquery = InfluxdbUtil.execQueryForList(gvalue, CommonStatVO.class);
        List<CommonStatVO> jquery = InfluxdbUtil.execQueryForList(jvalue, CommonStatVO.class);
        Map<Long, BigDecimal> tvMap = this.getUseMap(tquery);
        Map<Long, BigDecimal> fvMap = this.getUseMap(fquery);
        Map<Long, BigDecimal> pvMap = this.getUseMap(pquery);
        Map<Long, BigDecimal> gvMap = this.getUseMap(gquery);
        Map<Long, BigDecimal> jvMap = this.getUseMap(jquery);

        List<EnergyChargeReport> insertList = new ArrayList<>();
        for (EnergyCollectConfig energyCollectConfig : collectConfigList) {
            String categoryCode = energyCollectConfig.getCategoryCode();
            List<AreaProject> areaProjects = typeAreaMap.get(energyCollectConfig.getCategoryCode());
            if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(areaProjects)) {
                log.warn("{}类型下没有区域", energyCollectConfig.getCategoryCode());
                continue;
            }
            for (AreaProject areaProject : areaProjects) {
                Long areaId = areaProject.getId();
                // 最终入库的报表对象
                EnergyChargeReport report = this.getDefaultReport(areaId, areaIdNameMap.get(areaId), categoryCode, energyCollectConfig.getCategoryName(), dayTime, dayTimeStr, nowTime);
                List<Long> dis = areaLinkDeviceIdMap.get(areaId);
                if (dis == null) {
                    dis = new ArrayList<>();
                }
                // 区域分类的用能对象
                CommonStatVO areaTypeUse = this.getAreaTypeUse(dis, tvMap, fvMap, pvMap, gvMap, jvMap);
                // 分类的用能规则
                EnergyChargeRulesVO rulesVO = rulesVOMap.get(categoryCode);
                // 用能规则为空或者用能规则的阶梯内容为空就不统计费用
                if (rulesVO == null || com.guhecloud.platform.base.util.CollectionUtils.isEmpty(rulesVO.getStairsEnergyContentsList())) {
                    continue;
                }
                // 规则的阶梯用能
                List<StairsEnergyContentsDTO> contentsDTOList = rulesVO.getStairsEnergyContentsList();
                contentsDTOList = contentsDTOList.stream().sorted(Comparator.comparing(StairsEnergyContentsDTO::getStairs))
                        .collect(Collectors.toList());
                // 根据规则计算费用
                if (StairsEnergyTypeEnum.NOTHING.name().equals(rulesVO.getStairsEnergyType())) {
                    StairsEnergyContentsDTO dto = contentsDTOList.get(BigDecimal.ZERO.intValue());
                    if (Boolean.TRUE.equals(rulesVO.getComplexFeeRate())) {
                        this.reportSetDfpvPrice(report, areaTypeUse, dto);
                    } else {
                        this.reportSetDefaultPrice(report, areaTypeUse, dto);
                    }
                } else if (StairsEnergyTypeEnum.MONTH.name().equals(rulesVO.getStairsEnergyType())) {
                    EnergyTypeStatVO monthUseVo = monthUserMap.get(categoryCode);
                    this.setReportYearMonth(report, rulesVO, monthUseVo, areaTypeUse, contentsDTOList);
                } else if (StairsEnergyTypeEnum.YEARS.name().equals(rulesVO.getStairsEnergyType())) {
                    EnergyTypeStatVO yearUseVo = yearUseMap.get(categoryCode);
                    this.setReportYearMonth(report, rulesVO, yearUseVo, areaTypeUse, contentsDTOList);
                }

                insertList.add(report);
            }
        }

        if (!insertList.isEmpty()) {
            for (EnergyChargeReport report : insertList) {
                energyChargeReportMapper.insert(report);
            }
            //energyChargeReportService.saveBatch(insertList,1000);
        }
    }

    private CommonStatVO getAreaTypeUse(List<Long> dis, Map<Long, BigDecimal> tvMap, Map<Long, BigDecimal> fvMap,
                                        Map<Long, BigDecimal> pvMap, Map<Long, BigDecimal> gvMap, Map<Long, BigDecimal> jvMap
                                       ) {
        CommonStatVO areaTypeUse = new CommonStatVO();
        BigDecimal tvUse = BigDecimal.ZERO;
        BigDecimal fvUse = BigDecimal.ZERO;
        BigDecimal pvUse = BigDecimal.ZERO;
        BigDecimal gvUse = BigDecimal.ZERO;
        BigDecimal jvUse = BigDecimal.ZERO;
        for (Long di : dis) {
            tvUse = this.addDecimal(tvMap, di, tvUse);
            fvUse = this.addDecimal(fvMap, di, fvUse);
            pvUse = this.addDecimal(pvMap, di, pvUse);
            gvUse = this.addDecimal(gvMap, di, gvUse);
            jvUse = this.addDecimal(jvMap, di, jvUse);
        }
        areaTypeUse.setValue(tvUse);
        areaTypeUse.setFvalue(fvUse);
        areaTypeUse.setPvalue(pvUse);
        areaTypeUse.setGvalue(gvUse);
        areaTypeUse.setJvalue(jvUse);
        return areaTypeUse;
    }

    private EnergyChargeReport getDefaultReport(Long areaId, String areaName, String categoryCode, String categoryName,
                                                LocalDateTime startDayTime,
                                                String dayTimeStr, LocalDateTime nowTime
                                               ) {
        EnergyChargeReport report = new EnergyChargeReport();
        report.setAreaId(areaId);
        report.setAreaName(areaName);
        report.setInstrumentType(categoryCode);
        report.setInstrumentTypeName(categoryName);
        report.setStatDate(startDayTime);
        report.setStatDateStr(dayTimeStr);
        report.setCreateTime(nowTime);
        report.setTotalCost(BigDecimal.ZERO);
        report.setDvCost(BigDecimal.ZERO);
        report.setFvCost(BigDecimal.ZERO);
        report.setPvCost(BigDecimal.ZERO);
        report.setGvCost(BigDecimal.ZERO);
        report.setJvCost(BigDecimal.ZERO);
        return report;
    }

    private void setReportYearMonth(EnergyChargeReport report, EnergyChargeRulesVO rulesVO, EnergyTypeStatVO energyUseVo,
                                    CommonStatVO areaTypeUse, List<StairsEnergyContentsDTO> contentsDTOList
                                   ) {
        if (Boolean.TRUE.equals(rulesVO.getComplexFeeRate())) {
            for (StairsEnergyContentsDTO dto : contentsDTOList) {
                if (energyUseVo == null || (energyUseVo.getDdValue().compareTo(dto.getStartEnergy()) > BigDecimal.ZERO.intValue() &&
                                            energyUseVo.getDdValue().compareTo(dto.getEndEnergy()) <= BigDecimal.ZERO.intValue())) {
                    this.reportSetDfpvPrice(report, areaTypeUse, dto);
                    break;
                }
            }
        } else {
            for (StairsEnergyContentsDTO dto : contentsDTOList) {
                if (energyUseVo == null || (energyUseVo.getDdValue().compareTo(dto.getStartEnergy()) > BigDecimal.ZERO.intValue() &&
                                            energyUseVo.getDdValue().compareTo(dto.getEndEnergy()) <= BigDecimal.ZERO.intValue())) {
                    this.reportSetDefaultPrice(report, areaTypeUse, dto);
                    break;
                }
            }

        }
    }

    private EnergyChargeReport reportSetDefaultPrice(EnergyChargeReport report, CommonStatVO areaTypeUse, StairsEnergyContentsDTO dto) {
        return reportSetItemPrice(report, areaTypeUse, dto.getPrice(), dto.getPrice(), dto.getPrice(), dto.getPrice(), dto.getPrice());
    }

    private EnergyChargeReport reportSetDfpvPrice(EnergyChargeReport report, CommonStatVO areaTypeUse, StairsEnergyContentsDTO dto) {
        return reportSetItemPrice(report, areaTypeUse, dto.getPrice(), dto.getFvPrice(), dto.getPvPrice(), dto.getGvPrice(), dto.getJvPrice());
    }

    private EnergyChargeReport reportSetItemPrice(EnergyChargeReport report, CommonStatVO areaTypeUse, BigDecimal dvPrice,
                                                  BigDecimal fvPrice, BigDecimal pvPrice, BigDecimal gvPrice, BigDecimal jvPrice
                                                 ) {
        // 数据采集时value的和其实是总读数，所以value应该填入total中
        BigDecimal dvUse = areaTypeUse.getValue()
                .subtract(areaTypeUse.getFvalue().add(areaTypeUse.getPvalue()).add(areaTypeUse.getGvalue()).add(areaTypeUse.getJvalue()));
        dvUse = dvUse.compareTo(BigDecimal.ZERO) > 0 ? dvUse : BigDecimal.ZERO;
        report.setDvCost(dvUse.multiply(dvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setTotalCost(areaTypeUse.getValue().multiply(dvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setFvCost(areaTypeUse.getFvalue().multiply(fvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setPvCost(areaTypeUse.getPvalue().multiply(pvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setGvCost(areaTypeUse.getGvalue().multiply(gvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setJvCost(areaTypeUse.getJvalue().multiply(jvPrice).setScale(2, RoundingMode.HALF_UP));
        report.setTotalCost(report.getDvCost().add(report.getFvCost()).add(report.getPvCost()).add(report.getGvCost())
                                    .add(report.getJvCost()));
        return report;
    }

    private BigDecimal addDecimal(Map<Long, BigDecimal> map, Long deviceId, BigDecimal totalValue) {
        BigDecimal dvDecimal = map.get(deviceId);
        if (dvDecimal != null) {
            totalValue = totalValue.add(dvDecimal);
        }
        return totalValue;
    }

    private Map<Long, BigDecimal> getUseMap(List<CommonStatVO> list) {
        Map<Long, BigDecimal> map = new HashMap<>();
        if (list != null) {
            for (CommonStatVO commonStatVO : list) {
                long deviceId = BigDecimal.ZERO.longValue();
                if (StringUtils.isNoneBlank(commonStatVO.getDeviceId())) {
                    deviceId = Long.parseLong(commonStatVO.getDeviceId());
                }
                BigDecimal value = getValueBigDecimal(commonStatVO);
                map.put(deviceId, value);
            }
        }
        return map;
    }

    private static BigDecimal getValueBigDecimal(CommonStatVO commonStatVO) {
        BigDecimal value = BigDecimal.ZERO;
        if (commonStatVO.getValue() != null) {
            value = commonStatVO.getValue();
        } else if (commonStatVO.getFvalue() != null) {
            value = commonStatVO.getFvalue();
        } else if (commonStatVO.getPvalue() != null) {
            value = commonStatVO.getPvalue();
        } else if (commonStatVO.getGvalue() != null) {
            value = commonStatVO.getGvalue();
        } else if (commonStatVO.getJvalue() != null) {
            value = commonStatVO.getJvalue();
        }
        return value;
    }

}
