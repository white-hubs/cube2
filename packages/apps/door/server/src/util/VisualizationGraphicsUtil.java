package com.guhecloud.cube.biz.door.server.util;

import com.guhecloud.cube.biz.door.server.vo.DateTimeSlotVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartListVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.guhecloud.cube.biz.door.server.constant.TimeDimensionConstant.MONTH;
import static com.guhecloud.cube.biz.door.server.constant.TimeDimensionConstant.TODAY;
import static com.guhecloud.cube.biz.door.server.constant.TimeDimensionConstant.WEEK;
import static com.guhecloud.cube.biz.door.server.constant.TimeDimensionConstant.YEAR;

/**
 * 初始化可视化图形
 */
public class VisualizationGraphicsUtil {
    /**
     * 初始化可视化图形
     *
     * @param dimension 时间维度
     * @param tableName 柱状图名称
     */
    public static StatisticalChartVO getGraphics(String dimension, String tableName) {
        List<String> list = new ArrayList<>();
        list.add(tableName);
        return getGraphics(dimension, list);
    }

    public static StatisticalChartVO getGraphics(Integer number, String tableName) {
        List<String> list = new ArrayList<>();
        list.add(tableName);
        return getGraphics(number, list);
    }

    public static StatisticalChartVO getGraphics(String dimension, List<String> tableName) {
        List<String> table = new ArrayList<>();
        int end;
        switch (dimension) {
            case TODAY:
                end = 24;
                break;
            case WEEK:
                end = 7;
                LocalDateTime week = DateLocalUtil.getWeek().getStartTime();
                for (int i = 0; i < 7; i++) {
                    int day = DateLocalUtil.getToday(week, i).getStartTime().getDayOfMonth();
                    table.add(Integer.toString(day));
                }
                break;
            case MONTH:
                var dateTime = DateLocalUtil.getMonth().getEndTime();
                end = dateTime.getDayOfMonth();
                break;
            case YEAR:
                end = 12;
                break;
            default:
                throw new RuntimeException("未知的时间维度");
        }
        return getGraphics(table, end, tableName);
    }

    public static StatisticalChartVO getGraphics(Integer end, List<String> tableName) {
        return getGraphics(null, end, tableName);
    }

    public static StatisticalChartVO getGraphics(List<String> table, Integer end, List<String> tableName) {
        BigDecimal o = new BigDecimal(0);
        List<StatisticalChartListVO> value = new ArrayList<>(7);
        if (ObjectUtils.isEmpty(table)) {
            // 初始化 x 轴的名称
            table = new ArrayList<>(12);
            for (int i = 1; i <= end; i++) {
                table.add(String.valueOf(i));
            }
        }
        // 根据柱状图个数生成对应的数据
        for (String s : tableName) {
            StatisticalChartListVO chart = new StatisticalChartListVO();
            chart.setName(s);
            chart.setValue(getValue(end));
            chart.setTotal(new BigDecimal(0));
            value.add(chart);
        }
        // 初始化柱状图数据
        return new StatisticalChartVO(o, table, value);
    }

    /**
     * 初始化 x 轴的数据
     */
    public static List<BigDecimal> getValue(Integer end) {
        BigDecimal o = new BigDecimal(0);
        List<BigDecimal> valueData = new ArrayList<>();
        for (int i = 1; i <= end; i++) {
            valueData.add(o);
        }
        return valueData;
    }

    /**
     * 将查询的数据填充至初始化集合
     */
    public static StatisticalChartVO processingData(String dimension, StatisticalChartVO chart, List<List<StatisticalChartDataVO>> data) {
        // 遍历x轴，填充数据
        for (int i = 0; i < chart.getXdata().size(); i++) {
            // 遍历线
            for (int k = 0; k < data.size(); k++) {
                // 遍历线计算总数、环比等
                for (StatisticalChartDataVO ex : data.get(k)) {
                    if (StringUtils.isNotEmpty(ex.getName())) {
                        // x轴和数据name相同，将数据插入x轴对应的下标
                        if (chart.getXdata().get(i).equals(ex.getName())) {
                            chart.getSeries().get(k).getValue().set(i, new BigDecimal(ex.getValus()));
                            chart.getSeries().get(k).setTotal(chart.getSeries().get(k).getTotal().add(new BigDecimal(ex.getValus())));
                        }
                    }
                }
            }
        }
        // 如果时间维度是本周、本月需要重新定义
        List<String> table = new ArrayList<>();
        switch (dimension) {
            case WEEK:
                table.add("周一");
                table.add("周二");
                table.add("周三");
                table.add("周四");
                table.add("周五");
                table.add("周六");
                table.add("周天");
                chart.setXdata(table);
                break;
            case MONTH:
                DateTimeSlotVO date = DateLocalUtil.getMonth();
                var dateTime = date.getEndTime();
                Integer month = dateTime.getMonthValue();
                for (int i = 1; i <= dateTime.getDayOfMonth(); i++) {
                    table.add(month + "-" + i);
                }
                chart.setXdata(table);
                break;
        }
        return chart;
    }
}
