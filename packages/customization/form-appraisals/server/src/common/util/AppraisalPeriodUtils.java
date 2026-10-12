package com.guhecloud.cube.srv.gen.appraisals.server.common.util;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class AppraisalPeriodUtils {
    // 私有构造器，防止实例化
    private AppraisalPeriodUtils() {
    }

    /**
     * 判断今天是否应该生成指定周期的考核记录
     *
     * @param periodType 周期类型
     * @param today      今天的日期
     *
     * @return true-应该生成 false-不应该生成
     */
    public static boolean shouldGenerate(AppraisalPeriodTypeEnum periodType, LocalDateTime today) {
        return switch (periodType) {
            // 月度：每月 1 号生成上月考核
            case MONTHLY -> today.getDayOfMonth() == 1;
            // 季度：1 月、4 月、7 月、10 月的 1 号生成上季度考核
            case QUARTERLY -> today.getDayOfMonth() == 1 &&
                              (today.getMonthValue() == 1 || today.getMonthValue() == 4 || today.getMonthValue() == 7 ||
                               today.getMonthValue() == 10);
            // 半年度：1 月、7 月的 1 号生成上半年度考核
            case HALF_YEAR -> today.getDayOfMonth() == 1 && (today.getMonthValue() == 1 || today.getMonthValue() == 7);
        };
    }

    /**
     * 计算上一周期的目标期间
     *
     * @param periodType 周期类型
     * @param today      今天的日期
     *
     * @return 上一周期的年度、期数和时间范围
     */
    public static TargetPeriod previousPeriod(AppraisalPeriodTypeEnum periodType, LocalDateTime today) {
        return switch (periodType) {
            // 月度周期：计算上月
            case MONTHLY -> monthly(today);
            // 季度周期：计算上季度
            case QUARTERLY -> quarterly(today);
            // 半年度周期：计算上半年度
            case HALF_YEAR -> halfYear(today);
        };
    }

    /**
     * 根据指定的年度和期数计算目标期间
     *
     * @param periodType       周期类型
     * @param assessmentYear   考核年度
     * @param assessmentPeriod 考核期数
     *
     * @return 目标期间的年度、期数和时间范围
     */
    public static TargetPeriod targetPeriod(AppraisalPeriodTypeEnum periodType, Integer assessmentYear, Integer assessmentPeriod) {
        return switch (periodType) {
            // 月度：期数为月份（1-12）
            case MONTHLY -> monthly(assessmentYear, assessmentPeriod);
            // 季度：期数为季度（1-4）
            case QUARTERLY -> quarterly(assessmentYear, assessmentPeriod);
            // 半年度：期数为半年（1-2）
            case HALF_YEAR -> halfYear(assessmentYear, assessmentPeriod);
        };
    }

    /**
     * 计算当前进行中周期的统计期间，结束时间为指定预览截止时间。
     *
     * @param periodType 周期类型
     * @param asOfTime   预览截止时间
     *
     * @return 当前周期的年度、期数和时间范围
     */
    public static TargetPeriod currentPeriodUntil(AppraisalPeriodTypeEnum periodType, LocalDateTime asOfTime) {
        LocalDateTime endTime = asOfTime.withNano(0);
        return switch (periodType) {
            case MONTHLY -> currentMonthly(endTime);
            case QUARTERLY -> currentQuarterly(endTime);
            case HALF_YEAR -> currentHalfYear(endTime);
        };
    }

    /**
     * 计算上月的期间
     *
     * @param today 今天的日期
     *
     * @return 上月的年度、月份和时间范围
     */
    private static TargetPeriod monthly(LocalDateTime today) {
        // 减去一个月得到上月日期
        LocalDateTime base = today.minusMonths(1);
        // 构建上月期间对象
        return monthly(base.getYear(), base.getMonthValue());
    }

    /**
     * 计算上季度的期间
     *
     * @param today 今天的日期
     *
     * @return 上季度的年度、季度数和时间范围
     */
    private static TargetPeriod quarterly(LocalDateTime today) {
        // 减去三个月得到上季度日期
        LocalDateTime base = today.minusMonths(3);
        // 计算当前月份属于第几季度：(月份 -1 ) / 3 + 1
        int quarter = ((base.getMonthValue() - 1) / 3) + 1;
        // 构建上季度期间对象
        return quarterly(base.getYear(), quarter);
    }

    /**
     * 计算上半年度的期间
     *
     * @param today 今天的日期
     *
     * @return 上半年度的年度、半年度数和时间范围
     */
    private static TargetPeriod halfYear(LocalDateTime today) {
        // 减去六个月得到上半年度日期
        LocalDateTime base = today.minusMonths(6);
        // 判断属于上半年还是下半年：1-6 月为第 1 半年度，7-12 月为第 2 半年度
        int half = base.getMonthValue() <= 6 ? 1 : 2;
        // 构建上半年度期间对象
        return halfYear(base.getYear(), half);
    }

    /**
     * 构建月度期间对象
     *
     * @param assessmentYear   年度
     * @param assessmentPeriod 月份（1-12）
     *
     * @return 月度期间的年度、月份和时间范围
     */
    private static TargetPeriod monthly(Integer assessmentYear, Integer assessmentPeriod) {
        // 构建当月第一天的日期
        LocalDate firstDay = LocalDate.of(assessmentYear, assessmentPeriod, 1);
        // 获取当月开始时间（月初 00:00:00）
        LocalDateTime begin = DateUtils.beginOfMonth(firstDay.atStartOfDay());
        // 获取当月结束时间（月末 23:59:59）
        LocalDateTime end = DateUtils.endOfMonth(firstDay.atStartOfDay());
        // 返回月度期间对象
        return new TargetPeriod(assessmentYear, assessmentPeriod, begin, end);
    }

    /**
     * 构建当前月度预览期间。
     *
     * @param endTime 预览截止时间
     *
     * @return 当前月度期间
     */
    private static TargetPeriod currentMonthly(LocalDateTime endTime) {
        LocalDate firstDay = LocalDate.of(endTime.getYear(), endTime.getMonthValue(), 1);
        return new TargetPeriod(endTime.getYear(), endTime.getMonthValue(), firstDay.atStartOfDay(), endTime);
    }

    /**
     * 构建季度期间对象
     *
     * @param assessmentYear   年度
     * @param assessmentPeriod 季度数（1-4）
     *
     * @return 季度期间的年度、季度数和时间范围
     */
    private static TargetPeriod quarterly(Integer assessmentYear, Integer assessmentPeriod) {
        // 计算季度起始月份：Q1=1 月，Q2=4 月，Q3=7 月，Q4=10 月
        int startMonth = (assessmentPeriod - 1) * 3 + 1;
        // 构建季度第一天的日期
        LocalDate firstDay = LocalDate.of(assessmentYear, startMonth, 1);
        // 季度开始时间为第一天 00:00:00
        LocalDateTime begin = firstDay.atStartOfDay();
        // 季度结束时间为第三个月月末 23:59:59
        LocalDateTime end = DateUtils.endOfMonth(firstDay.plusMonths(2).atStartOfDay());
        // 返回季度期间对象
        return new TargetPeriod(assessmentYear, assessmentPeriod, begin, end);
    }

    /**
     * 构建当前季度预览期间。
     *
     * @param endTime 预览截止时间
     *
     * @return 当前季度期间
     */
    private static TargetPeriod currentQuarterly(LocalDateTime endTime) {
        int quarter = ((endTime.getMonthValue() - 1) / 3) + 1;
        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate firstDay = LocalDate.of(endTime.getYear(), startMonth, 1);
        return new TargetPeriod(endTime.getYear(), quarter, firstDay.atStartOfDay(), endTime);
    }

    /**
     * 构建半年度期间对象
     *
     * @param assessmentYear   年度
     * @param assessmentPeriod 半年度数（1-2）
     *
     * @return 半年度期间的年度、半年度数和时间范围
     */
    private static TargetPeriod halfYear(Integer assessmentYear, Integer assessmentPeriod) {
        // 计算半年度起始月份：H1=1 月，H2=7 月
        int startMonth = assessmentPeriod == 1 ? 1 : 7;
        // 构建半年度第一天的日期
        LocalDate firstDay = LocalDate.of(assessmentYear, startMonth, 1);
        // 半年度开始时间为第一天 00:00:00
        LocalDateTime begin = firstDay.atStartOfDay();
        // 半年度结束时间为第六个月月末 23:59:59
        LocalDateTime end = DateUtils.endOfMonth(firstDay.plusMonths(5).atStartOfDay());
        // 返回半年度期间对象
        return new TargetPeriod(assessmentYear, assessmentPeriod, begin, end);
    }

    /**
     * 构建当前半年度预览期间。
     *
     * @param endTime 预览截止时间
     *
     * @return 当前半年度期间
     */
    private static TargetPeriod currentHalfYear(LocalDateTime endTime) {
        int half = endTime.getMonthValue() <= 6 ? 1 : 2;
        int startMonth = half == 1 ? 1 : 7;
        LocalDate firstDay = LocalDate.of(endTime.getYear(), startMonth, 1);
        return new TargetPeriod(endTime.getYear(), half, firstDay.atStartOfDay(), endTime);
    }

    /**
     * 考核周期目标期间
     * 包含年度、期数和时间范围信息
     */
    @Data
    @AllArgsConstructor
    public static class TargetPeriod {
        /**
         * 考核年度
         */
        private Integer assessmentYear;
        /**
         * 考核期数（月度：1-12，季度：1-4，半年度：1-2）
         */
        private Integer assessmentPeriod;
        /**
         * 周期开始时间
         */
        private LocalDateTime beginTime;
        /**
         * 周期结束时间
         */
        private LocalDateTime endTime;
    }
}
