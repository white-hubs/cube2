package com.guhecloud.cube.deliver.project.merchant.commons.utils;

/**
 * 生成Cron表达式工具类
 *
 * @author lanchun
 * @since 2020/9/7 11:30
 */
public class CronUtil {

    /**
     * 生成cron表达式
     *
     * @param cycleType
     * @param cycleDate
     * @param execTime
     * @return
     */
    public static String createCronExpression(String cycleType, String cycleDate, String execTime) {
        StringBuffer cronExp = new StringBuffer("");
        String[] hhmmss = execTime.split(":");
        //秒
        cronExp.append(hhmmss[2]).append(" ");
        //分
        cronExp.append(hhmmss[1]).append(" ");
        //小时
        cronExp.append(hhmmss[0]).append(" ");
        if (cycleType.equals(DateUtils.DEMENSION_DAY)) {//天
            cronExp.append("* ");//日
            cronExp.append("* ");//月
            cronExp.append("? ");//周
        } else if (cycleType.equals(DateUtils.DEMENSION_WEEK)) {//周
            cronExp.append("? ");//周与日只能指定一种，设置日为不指定
            cronExp.append("* ");//月
            String[] weeks = cycleDate.split(",");
            //设置周的执行周期
            for (int i = 0; i < weeks.length; i++) {
                int weekNum = Integer.parseInt(weeks[i]);
                if (weekNum == 7) {
                    weekNum = 1;
                } else {
                    weekNum += 1;
                }
                cronExp.append(weekNum);
                if (i == weeks.length - 1) {
                    cronExp.append(" ");
                } else {
                    cronExp.append(",");
                }
            }
        } else if (cycleType.equals(DateUtils.DEMENSION_MONTH)) {
            //设置具体某天执行
            String[] days = cycleDate.split(",");
            for (int i = 0; i < days.length; i++) {
                cronExp.append(days[i]);
                if (i == days.length - 1) {
                    cronExp.append(" ");
                } else {
                    cronExp.append(",");
                }
            }
            cronExp.append("* ");//月
            cronExp.append("? ");//周
        }
        cronExp.append("*");//年
        return cronExp.toString();
    }

}
