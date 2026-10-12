package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskTimeQuantumVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Deprecated
public class CommonUtil {

    /**
     * 根据传入的比率计算环比，并返回百分比字符串形式，如：10%
     *
     * @param currentRatioString 当前比率，如：10%
     * @param lastRatioString    上期比率，如：10%
     *
     * @return 计算结果
     */
    public static String calculateRingRatioPercentageAsString(String currentRatioString, String lastRatioString) {
        if (StringUtils.isBlank(currentRatioString) || StringUtils.isBlank(lastRatioString)) {
            return "0.00%";
        }
        var last = new BigDecimal(lastRatioString.replace("%", ""));
        if (last.compareTo(new BigDecimal("0")) == 0) {
            return "0.00%";
        }
        var current = new BigDecimal(currentRatioString.replace("%", ""));
        var calcResult = MathUtils.calculateRingRatioPercentage(current, last);
        return MathUtils.simplePercentageFormat(calcResult);
    }

    /**
     * 计算比率，并返回百分比字符串形式，如：10%
     *
     * @param totalNum 总数
     * @param molecule 基数
     *
     * @return 计算结果
     *
     * @deprecated 注意，入参与 MathUtils 相反，该方法已废弃，请使用 calculateProportionPercentageAsString
     */
    @Deprecated
    public static String calculateProportionPercentageAsStringLegacy(Long totalNum, Long molecule) {
        return calculateProportionPercentageAsString(molecule, totalNum);
    }

    public static String calculateProportionPercentageAsString(Long molecule, Long totalNum) {
        var result = MathUtils.calculateProportionPercentage(molecule, totalNum);
        return MathUtils.simplePercentageFormat(result);
    }

    public static String calculateProportionPercentageAsString(Integer molecule, Integer totalNum) {
        var result = MathUtils.calculateProportionPercentage(molecule, totalNum);
        return MathUtils.simplePercentageFormat(result);
    }

    /**
     * @Author wql
     * @Description 计算任务执行时间
     * @Date 10:29 2023/5/31
     **/
    public static TaskTimeQuantumVO cuntTaskTimeQuantum(String executeData, LocalDateTime startDay) {
        TaskTimeQuantumVO vo = new TaskTimeQuantumVO();
        if (StringUtils.isNotBlank(executeData)) {
            String today = ObjectUtils.isNull(startDay) ? DateUtils.formatDate(LocalDate.now()) : DateUtils.formatDate(startDay);
            String format = "yyyy-MM-dd HH:mm";
            String[] split = executeData.split("-");
            if (StringUtils.isNotBlank(split[0])) {
                vo.setStartDate(DateUtils.parse(today + " " + split[0], format));
            }
            if (StringUtils.isNotBlank(split[1])) {
                vo.setEndDate(DateUtils.parse(today + " " + split[1], format));
            }
            if (vo.getStartDate() != null && vo.getEndDate() != null && vo.getEndDate().isBefore(vo.getStartDate())) {
                vo.setEndDate(DateUtils.offsetDay(vo.getEndDate(), 1));
            }
        }
        return vo;
    }

}
