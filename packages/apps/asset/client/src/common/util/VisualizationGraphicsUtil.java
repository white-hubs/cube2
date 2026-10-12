package com.guhecloud.cube.property.management.client.common.util;

import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartDataVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartListVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartVO;
import com.guhecloud.platform.base.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.MONTH;
import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.TODAY;
import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.WEEK;
import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.YEAR;

/**
 * 绘制柱状图、饼状图
 */
public class VisualizationGraphicsUtil {
    private static final BigDecimal o = new BigDecimal(0);

    /**
     * 根据时间维度绘制一个柱状图
     *
     * @param dimension 时间维度
     * @param tableName 柱状图名称
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(String dimension, String tableName) {
        List<String> list = new ArrayList<>();
        list.add(tableName);
        return getGraphics(dimension, list);
    }

    /**
     * 绘制 N个x点的一个柱状图
     *
     * @param number    x轴的数量
     * @param tableName 柱状图名称
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(Integer number, String tableName) {
        List<String> list = new ArrayList<>();
        list.add(tableName);
        return getGraphics(number, list);
    }

    /**
     * 根据时间维度绘制一个柱状图
     *
     * @param data 柱状图数据
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(List<StatisticalChartDataVO> data) {
        List<String> xdata = new ArrayList<>();
        List<StatisticalChartListVO> series = new ArrayList<>();
        StatisticalChartListVO chartList = new StatisticalChartListVO();
        chartList.setName("部门资产分布情况");
        List<BigDecimal> valus = new ArrayList<>();
        for (StatisticalChartDataVO ex : data) {
            if (ObjectUtils.isNotEmpty(ex.getName()) && ObjectUtils.isNotEmpty(ex.getValus())) {
                xdata.add(ex.getName());
                valus.add(new BigDecimal(ex.getValus()));
            }
        }
        chartList.setValue(valus);
        series.add(chartList);
        return new StatisticalChartVO(xdata, series);
    }

    /**
     * 根据时间维度绘制 N 个柱状图
     *
     * @param dimension 时间维度
     * @param tableName 柱状图名称集合
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(String dimension, List<String> tableName) {
        // x轴集合
        List<String> table = new ArrayList<>();
        // x轴长度
        int end = 0;
        // 根据时间维度算出x轴长度
        switch (dimension) {
            case TODAY:
                end = 24;
                break;
            case WEEK:
                end = 7;
                // 本周的显示为 周一 这种需要特殊处理，
                var week = DateLocalUtil.getWeek().getStartTime();
                for (int i = 0; i < 7; i++) {
                    int day = DateLocalUtil.getToday(week, i).getStartTime().getDayOfMonth();
                    table.add(Integer.toString(day));
                }
                break;
            case MONTH:
                end = DateLocalUtil.getMonth().getEndTime().getDayOfMonth();
                break;
            case YEAR:
                end = 12;
                break;
            default:
                throw new RuntimeException("未知的时间维度");
        }
        return getGraphics(table, end, tableName);
    }

    /**
     * 绘制 N个x点的 多个柱状图
     *
     * @param end       x轴的长度
     * @param tableName 柱状图名称集合
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(Integer end, List<String> tableName) {
        return getGraphics(null, end, tableName);
    }

    /**
     * 绘制 N个x点的 多个柱状图
     *
     * @param table     x轴集合，为空会根据x轴长度生成
     * @param end       x轴的长度
     * @param tableName 柱状图名称集合
     *
     * @return 柱状图默认数据
     */
    public static StatisticalChartVO getGraphics(List<String> table, Integer end, List<String> tableName) {
        // 线集合
        List<StatisticalChartListVO> value = new ArrayList<>(7);
        // x轴集合为空则根据x轴的长度初始化
        if (ObjectUtils.isEmpty(table)) {
            // 初始化 x 轴的名称
            table = new ArrayList<>(12);
            for (int i = 1; i <= end; i++) {
                table.add(String.valueOf(i));
            }
        }
        // 根据柱状图个数初始化线的数据
        for (String s : tableName) {
            StatisticalChartListVO chart = new StatisticalChartListVO();
            chart.setName(s);
            chart.setValue(getValue(end));
            chart.setTotal(new BigDecimal(0));
            value.add(chart);
        }
        // 初始化柱状图数据
        return new StatisticalChartVO(table, value);
    }

    /**
     * 绘制 x 轴的数据
     */
    public static List<BigDecimal> getValue(Integer end) {
        List<BigDecimal> valueData = new ArrayList<>();
        for (int i = 1; i <= end; i++) {
            valueData.add(o);
        }
        return valueData;
    }

    /**
     * 绘制饼状图
     *
     * @param tableName 饼状图分类名称
     */
    public static List<PieChartValueVO> getPieChart(List<String> tableName) {
        List<PieChartValueVO> list = new ArrayList<>();
        for (String ex : tableName) {
            PieChartValueVO value = new PieChartValueVO(ex, o, o, o);
            list.add(value);
        }
        return list;
    }
}
