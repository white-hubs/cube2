package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.StaffQueryParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2023/02/21
 * @description 人员概览-变动统计
 */
@Data
public class StaffExchangeStatisticsVO {

    private int total;

    private String inductionDate;
    private String regularWorkerDate;
    private String quitDate;

    /**
     * 入职人数
     */
    private long inductionNum;

    /**
     * 转正人数
     */
    private long regularWorkerNum;

    /**
     * 离职人数
     */
    private long quitNum;

    /**
     * 变动趋势
     */
    private List<Trend> exchangeTrend;

    public List<Trend> initTrend(StaffQueryParam param) {
        var end = LocalDateTime.now();
        List<Trend> list = new ArrayList<>();
        int interval;
        switch (param.getTimeType()) {
            case "THIS_WEEK":
                param.setPeriod(new Period(DateUtils.beginOfWeek(end), end));
                param.setDateExp("%Y-%m-%d");
                interval = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                addByDay(list, param.getPeriod().getBegin(), interval, "{2}");
                break;
            case "THIS_MONTH":
                param.setPeriod(new Period(DateUtils.beginOfMonth(end), end));
                param.setDateExp("%Y-%m-%d");
                interval = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                addByDay(list, param.getPeriod().getBegin(), interval, "{0}");
                break;
            case "THIS_YEAR":
                param.setPeriod(new Period(DateUtils.beginOfYear(end), end));
                param.setDateExp("%Y-%m");
                interval = (int) DateUtils.betweenMonth(param.getPeriod().getBegin(), param.getPeriod().getEnd(), true);
                addByMonth(list, param.getPeriod().getBegin(), interval + 1, "{0}月");
                break;
        }
        return list;
    }

    private void addByDay(List<Trend> list, LocalDateTime beginTime, int interval, String dateFormat) {
        for (int i = 0; i <= interval; i++) {
            var date = beginTime.plusDays(i);
            var format = this.getFormat(date, dateFormat, "Day");
            list.add(new Trend(format, DateUtils.parse(DateUtils.format(date, "yyyy-MM-dd"), "yyyy-MM-dd"), DateUtils.format(date, "yyyy-MM-dd")));
        }
    }

    private void addByMonth(List<Trend> list, LocalDateTime beginTime, int interval, String dateFormat) {
        for (int i = 0; i < interval; i++) {
            var date = beginTime.plusMonths(i);
            String format = this.getFormat(date, dateFormat, "Month");
            list.add(new Trend(format, DateUtils.parse(DateUtils.format(date, "yyyy-MM"), "yyyy-MM"), DateUtils.format(date, "yyyy-MM")));
        }
    }

    private String getFormat(LocalDateTime date, String dateFormat, String type) {
        return switch (type) {
            case "Hour" -> MessageFormat.format(dateFormat, date.getHour(), date.getDayOfMonth());
            case "Day" -> MessageFormat.format(dateFormat,
                                               date.getDayOfMonth(),
                                               date.getMonthValue(),
                                               DateUtils.dayOfWeekFormat(date));
            case "Month" -> MessageFormat.format(dateFormat, date.getMonthValue(), date.getYear());
            default -> "";
        };
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Trend {

        /**
         * 名称
         */
        private String timeFlag;
        /**
         * 入职人数
         */
        private int inductionNum;
        /**
         * 转正人数
         */
        private int regularWorkerNum;
        /**
         * 离职人数
         */
        private int quitNum;
        private LocalDateTime sortTime;
        private String compTime;

        public Trend(String timeFlag, LocalDateTime sortTime, String compTime) {
            this.timeFlag = timeFlag;
            this.sortTime = sortTime;
            this.compTime = compTime;
        }
    }

}