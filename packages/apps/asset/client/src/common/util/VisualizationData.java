package com.guhecloud.cube.property.management.client.common.util;

import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.platform.base.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.WEEK;

/**
 * @Author: lijie
 * @Date: 2023/4/8 0008 23:17
 * @Description:
 */
public class VisualizationData {
    private static final BigDecimal o = new BigDecimal(0);

    /**
     * 绘制饼状图
     *
     * @param tableName 饼状图分类名称
     * @param value     饼状图数据
     *
     * @return 饼状图数据
     */
    public static List<PieChartValueVO> getPieChartData(List<String> tableName, List<PieChartValueVO> value) {
        return VisualizationData.getPieChartData("", tableName, value);
    }

    public static List<PieChartValueVO> getPieChartData(String dimension, List<String> tableName, List<PieChartValueVO> value) {
        Map<String, PieChartValueVO> map = new HashMap<>();
        BigDecimal total = new BigDecimal(0);
        for (PieChartValueVO ex : value) {
            if (ObjectUtils.isNotEmpty(ex.getValue())) {
                total = total.add(ex.getValue());
            } else {
                ex.setValue(new BigDecimal(0));
            }
            map.put(ex.getName(), ex);
        }
        List<PieChartValueVO> list = new ArrayList<>();
        for (int i = 0; i < tableName.size(); i++) {
            String ex = tableName.get(i);
            PieChartValueVO pie = map.get(ex);
            if (ObjectUtils.isEmpty(pie)) {
                pie = new PieChartValueVO(ex, o, o, o);
            } else {
                pie.setTotalRatio(pie.getValue().compareTo(BigDecimal.ZERO) > 0 ? pie.getValue().divide(total, 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)) : BigDecimal.ZERO);
            }
            if (ObjectUtils.isNotEmpty(dimension) && dimension.equals(WEEK)) {
                switch (i) {
                    case 0:
                        pie.setName("周一");
                        break;
                    case 1:
                        pie.setName("周二");
                        break;
                    case 2:
                        pie.setName("周三");
                        break;
                    case 3:
                        pie.setName("周四");
                        break;
                    case 4:
                        pie.setName("周五");
                        break;
                    case 5:
                        pie.setName("周六");
                        break;
                    case 6:
                        pie.setName("周天");
                        break;

                }
            }
            list.add(pie);
        }
        return list;
    }

    /**
     * 绘制饼状图
     *
     * @param tableName    饼状图分类名称
     * @param newTableName 饼状图显示分类名称
     * @param value        饼状图数据
     *
     * @return 饼状图数据
     */
    public static List<PieChartValueVO> getPieChartData(List<String> newTableName, List<String> tableName, List<PieChartValueVO> value) {
        List<PieChartValueVO> list = getPieChartData(tableName, value);
        for (int i = 0; i < list.size(); i++) {
            if (newTableName.size() < i) {
                break;
            }
            list.get(i).setName(newTableName.get(i));
        }
        return list;
    }
}