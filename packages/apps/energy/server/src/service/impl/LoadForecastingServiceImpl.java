package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AreaProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.RouteProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.QueryTimeUnitEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.LoadForecastingParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.QueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.LoadForecastingService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.LinearRegression;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.LoadForecastingVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.PeriodVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_MONTH_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_UTC_FORMAT;

/**
 * @author wql
 * @describe 负荷预测
 * @date 2023/4/10 18:06
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoadForecastingServiceImpl implements LoadForecastingService {
    private final InfluxDBClient influxdbClient;

    private final RedisTemplate redisTemplate;

    private final AreaProjectMapper areaProjectMapper;

    private final RouteProjectMapper routeProjectMapper;

    /**
     * 回归预测训练数据取值范围
     */
    @Value("${energy.queryLimit}")
    private Integer predictiveNum;

    @Override
    public List<LoadForecastingVO> getLoadForecasting(LoadForecastingParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        QueryParam queryParam = getQueryParam(param.getTimeFlag(), param.getPeriod());
        List<String> objects = new ArrayList<>();
        //分支id筛选
        if (param.getFilterFlag().equals("area")) {
            List<Long> areaCount = areaProjectMapper.getAreaCount(param.getInstrumentType());
            if (areaCount.size() > 0) {
                List<String> ids = getIds(areaCount, param.getTreeIds(), objects);
                if (ListUtils.nonEmpty(ids)) {
                    param.setIds(ids);
                } else {
                    return new ArrayList<LoadForecastingVO>();
                }
            } else {
                return new ArrayList<LoadForecastingVO>();
            }
        } else if (param.getFilterFlag().equals("route")) {
            List<Long> routeCount = routeProjectMapper.getRouteCount(param.getInstrumentType());
            if (routeCount.size() > 0) {
                List<String> ids = getIds(routeCount, param.getTreeIds(), objects);
                if (ListUtils.nonEmpty(ids)) {
                    param.setIds(ids);
                } else {
                    return new ArrayList<LoadForecastingVO>();
                }
            } else {
                return new ArrayList<LoadForecastingVO>();
            }
        }
        QueryApi queryApi = influxdbClient.getQueryApi();
        String queryList = createQueryStatement(queryParam, tenant, param);
        List<DataVo> query = queryApi.query(queryList, DataVo.class);
        Map<String, Object> stringObjectMap = resultList(param.getTimeFlag(), query, param.getPeriod());
        Map<String, List<DataVo>> o = (Map<String, List<DataVo>>) stringObjectMap.get("collect");
        List<List<DataVo>> o1 = (List<List<DataVo>>) stringObjectMap.get("resultList");
        return getLoadForecastingVos(o, o1, queryParam, param.getPeriod());
    }

    private Map<String, Object> resultList(String timeFlag, List<DataVo> query, String period) {
        Map<String, Object> remap = new HashMap<>();
        List<List<DataVo>> objects = new ArrayList<>();
        for (DataVo dataVo : query) {
            String format = "";
            LocalDateTime date = DateUtils.parse(dataVo.getTime().toEpochMilli());
            int a = 0;
            if (QueryTimeUnitEnum.TODAY.getCode().equals(timeFlag)) {
                a = date.getHour();
            } else if (QueryTimeUnitEnum.THIS_MONTH.getCode().equals(timeFlag)) {
                a = date.getDayOfMonth();

            } else if (QueryTimeUnitEnum.THIS_YEAR.getCode().equals(timeFlag)) {
                a = date.getMonthValue() + 1;
            } else if (StringUtils.isNotEmpty(period)) {
                a = date.getDayOfMonth();
            }
            format = String.valueOf(a);
            dataVo.setDateFlag(format);
        }

        Map<String, List<DataVo>> collect = query.stream().collect(Collectors.groupingBy(DataVo::getDateFlag));

        for (String s : collect.keySet()) {
            objects.add(collect.get(s));
        }
        remap.put("resultList", objects);
        remap.put("collect", collect);
        return remap;
    }

    private QueryParam getQueryParam(String timeFlag, String period) {
        QueryParam param = new QueryParam();
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime dateTime = date;
        List<Map<String, String>> list = new ArrayList<>();
        if (QueryTimeUnitEnum.TODAY.getCode().equals(timeFlag)) {
            param.setFlagTime(0);
            param.setFlag(date.getHour());
            dateTime = DateUtils.offsetMinute(DateUtils.beginOfDay(date), -8 * 60);
            //取前N天数据作为回归预测训练数据
            for (int i = predictiveNum; i > 0; i--) {
                Map<String, String> map = new HashMap<>();
                map.put("startTime", DateUtils.formatDate(DateUtils.offsetDay(dateTime, -i), DEFAULT_DATE_FORMAT) + "T00:00:00Z");
                map.put("endTime", DateUtils.formatDate(DateUtils.offsetDay(date, -i), DEFAULT_DATE_FORMAT) + "T23:59:59Z");
                list.add(map);
            }
        } else if (QueryTimeUnitEnum.THIS_MONTH.getCode().equals(timeFlag)) {
            param.setFlagTime(1);
            param.setFlag(date.getDayOfMonth());
            dateTime = DateUtils.beginOfMonth(date);
            //取前N月数据作为回归预测训练数据
            for (int i = predictiveNum; i > 0; i--) {
                Map<String, String> map = new HashMap<>();
                map.put("startTime",
                        DateUtils.formatDate(DateUtils.offsetMonth(dateTime, -i), DEFAULT_DATE_FORMAT) + "T00:00:00Z");
                map.put("endTime", DateUtils.formatDate(DateUtils.offsetMonth(date, -i), DEFAULT_DATE_FORMAT) + "T23:59:59Z");
                list.add(map);
            }
        } else if (QueryTimeUnitEnum.THIS_YEAR.getCode().equals(timeFlag)) {
            param.setFlagTime(1);
            param.setFlag(date.getMonthValue() + 1);
            dateTime = DateUtils.beginOfYear(date);
            //取前N年数据作为回归预测训练数据
            for (int i = predictiveNum; i > 0; i--) {
                Map<String, String> map = new HashMap<>();
                map.put("startTime",
                        DateUtils.formatDate(DateUtils.offsetMonth(dateTime, -i * 12), DEFAULT_DATE_FORMAT) + "T00:00:00Z");
                map.put("endTime",
                        DateUtils.formatDate(DateUtils.offsetMonth(date, -i * 12), DEFAULT_DATE_FORMAT) + "T23:59:59Z");
                list.add(map);
            }
        } else if (StringUtils.isNotEmpty(period)) {
            PeriodVo period1 = getPeriod(period);
            param.setFlagTime(1);
            param.setFlag(period1.getDay());
            dateTime = DateUtils.parse(period.split(",")[0]);
            date = DateUtils.parse(period.split(",")[1]);
            //取前N月数据作为回归预测训练数据
            for (int i = predictiveNum; i > 0; i--) {
                Map<String, String> map = new HashMap<>();
                map.put("startTime",
                        DateUtils.formatDate(DateUtils.offset(dateTime, ChronoUnit.MONTHS, -i), DEFAULT_DATE_FORMAT) +
                        "T00:00:00Z");
                map.put("endTime",
                        DateUtils.formatDate(DateUtils.offset(date, ChronoUnit.MONTHS, -i), DEFAULT_DATE_FORMAT) + "T23:59:59Z");
                list.add(map);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("startTime", DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z");
        map.put("endTime", DateUtils.formatDate(date, DEFAULT_DATE_FORMAT) + "T23:59:59Z");
        list.add(map);
        param.setHistoryTimeParameter(list);
        param.setStartTime(DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z");
        param.setEndTime(DateUtils.formatDate(date, DEFAULT_DATE_FORMAT) + "T23:59:59Z");
        param.setTimeFlag(timeFlag);
        return param;
    }

    /**
     * @Author wql
     * @Description 设置负荷预测返回结果列表
     * @Date 10:22 2023/4/11
     **/
    private List<LoadForecastingVO> getLoadForecastingVos(Map<String, List<DataVo>> maps, List<List<DataVo>> queryList,
                                                          QueryParam queryParam, String period
                                                         ) {

        Map<String, List<BigDecimal>> map = new HashMap<>();
        for (List<DataVo> queryResult : queryList) {
            //计算每个查询结果的序列
            if (Objects.nonNull(queryResult)) {
                LocalDateTime date = DateUtils.parse(queryResult.getFirst().getTime().toEpochMilli());
                List<BigDecimal> collect = queryResult.stream().map(DataVo::getValue).collect(Collectors.toList());
                if (QueryTimeUnitEnum.TODAY.getCode().equals(queryParam.getTimeFlag())) {
                    map.put(String.valueOf(date.getHour()), collect);
                } else if (QueryTimeUnitEnum.THIS_MONTH.getCode().equals(queryParam.getTimeFlag())) {
                    map.put(String.valueOf(date.getDayOfMonth()), collect);
                } else if (QueryTimeUnitEnum.THIS_YEAR.getCode().equals(queryParam.getTimeFlag())) {
                    map.put(String.valueOf(date.getMonthValue() + 1), collect);
                } else if (StringUtils.isNotEmpty(period)) {
                    map.put(String.valueOf(date.getDayOfMonth()), collect);
                }

            }
        }
        //计算每个序列的预测值
        Map<String, String> predictedValue = countLinearRegression(map);
        List<LoadForecastingVO> loadForecastingVos = new ArrayList<>();
        LocalDateTime dateTime = DateUtils.parse(queryParam.getStartTime(), DEFAULT_UTC_FORMAT);
        for (int i = queryParam.getFlagTime(); i <= queryParam.getFlag(); i++) {
            String timeQuantum = "";
            if (QueryTimeUnitEnum.TODAY.getCode().equals(queryParam.getTimeFlag())) {
                LocalDateTime time = DateUtils.offset(dateTime, ChronoUnit.HOURS, i);
                timeQuantum = DateUtils.formatDate(time, COMPACT_DATETIME_FORMAT);
            } else if (QueryTimeUnitEnum.THIS_MONTH.getCode().equals(queryParam.getTimeFlag())) {
                LocalDateTime time = DateUtils.offset(dateTime, ChronoUnit.DAYS, i - 1);
                timeQuantum = DateUtils.formatDate(time, DEFAULT_DATE_FORMAT);
            } else if (QueryTimeUnitEnum.THIS_YEAR.getCode().equals(queryParam.getTimeFlag())) {
                LocalDateTime time = DateUtils.offset(dateTime, ChronoUnit.MONTHS, i - 1);
                timeQuantum = DateUtils.formatDate(time, DEFAULT_MONTH_FORMAT);
            } else if (StringUtils.isNotEmpty(period)) {
                LocalDateTime time = DateUtils.offset(dateTime, ChronoUnit.DAYS, i - 1);
                timeQuantum = DateUtils.formatDate(time, DEFAULT_DATE_FORMAT);
            }
            if (maps.size() > 0) {
                List<DataVo> dataVos = null;
                if (StringUtils.isNotEmpty(period)) {
                    String[] split = period.split(",");
                    String s1 = split[0];
                    LocalDateTime parse1 = DateUtils.parse(s1);
                    if (!(parse1.isAfter(LocalDateTime.now()))) {
                        String s = split[1];
                        LocalDateTime parse = DateUtils.parse(s);
                        int month = DateUtils.offset(parse, ChronoUnit.DAYS, -(i - 1)).getDayOfMonth();
                        dataVos = maps.get(String.valueOf(month));
                    }

                } else {
                    dataVos = maps.get(String.valueOf(i));
                }
                loadForecastingVos.add(getLoadForecastingVo(i, dataVos == null ? new DataVo() : dataVos.get(
                        dataVos.size() - 1), timeQuantum, predictedValue));
            }
        }
        return countTotal(loadForecastingVos);
    }

    /**
     * @Author wql
     * @Description 计算每个序列的回归预测算法系数
     * @Date 10:27 2023/4/12
     **/
    private Map<String, String> countLinearRegression(Map<String, List<BigDecimal>> originalData) {
        //回归预测算法训练并计算预测值
        Map<String, String> predictedValue = new HashMap<>();
        //住房率
        Object o = redisTemplate.opsForValue().get("occupancyRate");
        Double occupancyRate = Double.valueOf("1");
        if (Objects.nonNull(o)) {
            occupancyRate = Double.valueOf(o.toString());
        }
        if (Objects.nonNull(originalData)) {
            Double finalOccupancyRate = occupancyRate;
            originalData.forEach((k, v) -> {
                //至少有两个训练数据才能训练回归预测算法
                if (Objects.nonNull(v) && v.size() > 1) {
                    Map<Double, Double> data = new HashMap<>();
                    for (int i = 0; i < v.size(); i++) {
                        data.put((double) i, v.get(i).doubleValue());
                    }
                    LinearRegression regression = new LinearRegression(data);
                    Double regressionY = regression.getY((double) (v.size()), finalOccupancyRate);
                    predictedValue.put(k, String.format("%.2f", regressionY > 0D ? regressionY : 0D));
                }
            });
        }
        return predictedValue;
    }

    /**
     * @Author wql
     * @Description 计算预测和历史总能耗
     * @Date 11:52 2023/4/11
     **/
    private List<LoadForecastingVO> countTotal(List<LoadForecastingVO> loadForecastingVos) {
        BigDecimal predictedTotal = new BigDecimal("0.00");
        BigDecimal historicalTotal = new BigDecimal("0.00");
        if (Objects.nonNull(loadForecastingVos)) {
            for (LoadForecastingVO vo : loadForecastingVos) {
                predictedTotal = predictedTotal.add(new BigDecimal(vo.getPredictedValue()));
                historicalTotal = historicalTotal.add(new BigDecimal(vo.getHistoricalValue()));
            }
            BigDecimal finalHistoricalTotal = historicalTotal;
            BigDecimal finalPredictedTotal = predictedTotal;
            loadForecastingVos.forEach(vo -> {
                vo.setHistoricalTotal(finalHistoricalTotal.toString());
                vo.setPredictedTotal(finalPredictedTotal.toString());
            });
        }
        return loadForecastingVos;
    }

    private LoadForecastingVO getLoadForecastingVo(int i, DataVo dataVo, String timeQuantum, Map<String, String> predictedMap) {
        LoadForecastingVO vo = new LoadForecastingVO();
        vo.setTimeFlag(i);
        if (Objects.nonNull(dataVo)) {
            BigDecimal zero = new BigDecimal("0.00");
            vo.setTimeQuantum(timeQuantum);
            BigDecimal historicalValue = dataVo.getValue() == null ? BigDecimal.ZERO : dataVo.getValue();
            String predictedStr = predictedMap.get(String.valueOf(i));
            BigDecimal predictedValue = StringUtils.isBlank(predictedStr) ? zero : new BigDecimal(predictedStr);
            BigDecimal deviationValue = predictedValue.subtract(historicalValue);
            vo.setPredictedValue(predictedValue.setScale(2, RoundingMode.HALF_UP).toString());
            vo.setHistoricalValue(historicalValue.setScale(2, RoundingMode.HALF_UP).toString());
            vo.setDeviationValue(deviationValue.setScale(2, RoundingMode.HALF_UP).toString());
            if (zero.compareTo(historicalValue) == 0) {
                vo.setDeviationPercent("0.00");
            } else {
                vo.setDeviationPercent(
                        deviationValue.divide(historicalValue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")) + "%");
            }
        } else {
            vo.setTimeQuantum(timeQuantum);
            vo.setPredictedValue("0.00");
            vo.setHistoricalValue("0.00");
            vo.setDeviationValue("0.00");
            vo.setDeviationPercent("0.00%");
        }
        return vo;
    }

    /**
     * @Author wql
     * @Description 创建influxDB查询语句
     * @Date 19:55 2023/4/10
     **/
    private String createQueryStatement(QueryParam queryParam, Integer tenant, LoadForecastingParam param) {
        String measurement = param.getFilterFlag();
        if (QueryTimeUnitEnum.TODAY.getCode().equals(param.getTimeFlag())) {
            measurement += DateEnum.Hour;
        } else if (QueryTimeUnitEnum.THIS_MONTH.getCode().equals(param.getTimeFlag())) {
            measurement += DateEnum.Day;
        } else if (QueryTimeUnitEnum.THIS_YEAR.getCode().equals(param.getTimeFlag())) {
            measurement += DateEnum.Month;
        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            measurement += DateEnum.Day;
        }

        String filterIds = " and (";
        for (int i = 1; i <= param.getIds().size(); i++) {
            if (i == 1) {
                filterIds = filterIds + "r." + param.getFilterFlag() + "Id==\"" + param.getIds().get(i - 1) + "\"";
            } else {
                filterIds = filterIds + " or r." + param.getFilterFlag() + "Id==\"" + param.getIds().get(i - 1) + "\"";
            }
            if (i == param.getIds().size()) {
                filterIds = filterIds + ")";
            }
        }

        List<Map<String, String>> historyTimeParameter = queryParam.getHistoryTimeParameter();
        Map<String, String> stringStringMap = historyTimeParameter.get(0);
        Map<String, String> stringStringMap1 = historyTimeParameter.get(historyTimeParameter.size() - 1);
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + stringStringMap.get("startTime") + ", stop: " + stringStringMap1.get("endTime") + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" " + filterIds + ")\n" +
                      "  |> group(columns: [\"_time\"])\n" +
                      "  |> sum(column: \"value\")";

        return flux;
    }

    private List<String> getIds(List<Long> ids, List<TreeIds> treeIds, List<String> areaOrRouteIds) {
        for (TreeIds treeId : treeIds) {
            if (ids.contains(treeId.getId())) {
                areaOrRouteIds.add(treeId.getId().toString());
            } else if (ListUtils.nonEmpty(treeId.getChildren())) {
                getIds(ids, treeId.getChildren(), areaOrRouteIds);
            }
        }
        return areaOrRouteIds;
    }

    private PeriodVo getPeriod(String period) {
        String[] split = period.split(",");
        LocalDateTime parse = DateUtils.offsetMinute(DateUtils.parse(split[0]), -8 * 60);
        LocalDateTime parse1 = DateUtils.parse(split[1]);
        String time = DateUtils.formatDate(parse, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(parse, "HH:mm:ss") + "Z," +
                      DateUtils.formatDate(parse1, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        int l = (int) DateUtils.between(parse, parse1, ChronoUnit.DAYS);
        PeriodVo periodVo = new PeriodVo();
        periodVo.setDay(l);
        periodVo.setTime(time);
        return periodVo;
    }
}
