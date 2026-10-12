package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain;

import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayGrantValidityDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttQuotaLevelDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayQuotaValidityEnum;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class AttHolidayDomain {

    private AttHolidayDomain() {}

    /**
     * 根据有效期规则获截至日期
     *
     * @param grantValidityDTO 有效期规则对象
     * @param nowTime          当前时间
     *
     * @return 截至日期
     */
    public static LocalDateTime calcDeadline(AttHolidayGrantValidityDTO grantValidityDTO, LocalDateTime nowTime) {
        // 默认长期有效即2199-12-31 23:59:59
        LocalDateTime deadline = AttConstants.PERMANENT_DATE;
        if (grantValidityDTO == null) {
            return deadline;
        }
        if (grantValidityDTO.getNatureYear() != null && grantValidityDTO.getNatureYear() > AttConstants.zeroInt) {
            // 发放日起多少个自然年有效
            deadline = DateUtils.endOfYear(DateUtils.offset(nowTime, ChronoUnit.YEARS, grantValidityDTO.getNatureYear() - 1));
        } else if (grantValidityDTO.getValidityNumber() != null && grantValidityDTO.getValidityNumber() > AttConstants.zeroInt) {
            deadline = DateUtils.offset(nowTime, unitToDateFiled(grantValidityDTO.getValidityUnit()), grantValidityDTO.getValidityNumber());
        }
        return deadline;
    }

    /**
     * 获取工龄的假期
     *
     * @param seniorityDate 时间
     * @param levelList     额度级别集合
     * @param nowTime       当前时间
     * @param dayOfHour     一天折算多少小时
     *
     * @return 时间戳
     */
    public static Long getUserSocietyFloat(LocalDateTime seniorityDate, List<AttQuotaLevelDTO> levelList, LocalDateTime nowTime,
                                           Integer dayOfHour
                                          ) {
        if (seniorityDate == null || levelList == null) {
            return 0L;
        }
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }
        if (dayOfHour == null || dayOfHour <= AttConstants.zeroInt) {
            dayOfHour = AttConstants.dayOfHour;
        }
        // 计算工龄
        long year = DateUtils.between(seniorityDate, nowTime, ChronoUnit.YEARS, true);
        levelList.sort(Comparator.comparing(AttQuotaLevelDTO::getAgeYear));
        for (int i = levelList.size() - 1; i >= 0; i--) {
            AttQuotaLevelDTO dto = levelList.get(i);
            if ((int) year == dto.getAgeYear()) {
                return AttHolidayDomain.unitAndNumToLong(dto.getHolidayNumber(), AttHolidayQuotaValidityEnum.getValueByName(dto.getUnit()), dayOfHour);
            }
        }
        return 0L;
    }

    /**
     * 时间戳+中文单位——>字符串显示
     *
     * @param value     时间戳
     * @param unit      中文单位
     * @param dayOfHour 一天折算多少小时
     * @param symbol    是否添加符号(正数前面加+
     *
     * @return 显示的字符串
     */
    public static String longToUnit(Long value, String unit, Integer dayOfHour, Boolean symbol) {
        String result = longToUnit(value, unit, dayOfHour, AttConstants.oneInt);
        if (Boolean.TRUE.equals(symbol) && (Double.parseDouble(result) > AttConstants.zeroDouble)) {
            // 如果需要加符号且为正数就加上符号(更灵活可以自己传符号，不传就重载默认符号的方法.不过此项目暂时没必要如此过度设计)
            result = "+" + result;
        }
        return result;
    }

    public static String longToUnit(Long value, String unit, Integer dayOfHour, String symbol) {
        return symbol + longToUnit(value, unit, dayOfHour, AttConstants.oneInt);
    }

    /**
     * 时间戳转字符串时长
     *
     * @param value     时长
     * @param unit      单位
     * @param dayOfHour 一天则算多少小时
     * @param keep      保留多少位小数
     *
     * @return 时长
     */
    public static String longToUnit(Long value, String unit, Integer dayOfHour, Integer keep) {
        if (StringUtils.isBlank(unit) || value == null) {
            return "0.0";
        }
        if (dayOfHour == null || dayOfHour <= AttConstants.zeroInt) {
            dayOfHour = 8;
        }
        String result;
        String keepStr = "%." + keep + "f";
        switch (unit) {
            case AttConstants.HOUR:
                result = String.format(keepStr, (double) value / (1000 * 60 * 60));
                break;
            case AttConstants.DAYS:
                result = String.format(keepStr, (double) value / (1000 * 60 * 60 * dayOfHour));
                break;
            case AttConstants.YEAR:
                result = String.format(keepStr, (double) value / (1000 * 60 * 60 * 365));
                break;
            case AttConstants.MONTH:
                result = String.format(keepStr, (double) value / (1000 * 60 * 60 * 30));
                break;

            case AttConstants.AM:
            case AttConstants.PM:
                result = String.format(keepStr, (double) value / (1000 * 60 * 60 * ((double) dayOfHour / 2)));
                break;

            case AttConstants.MINUTE:
                result = String.valueOf(value / (1000L * 60));
                break;
            case AttConstants.SECOND:
                result = String.valueOf(value / 1000L);
                break;
            default:
                result = value.toString();
                break;
        }

        return result;
    }

    /**
     * 枚举时间单位转data单位
     *
     * @param unit 中午单位
     *
     * @return 日期单位
     */
    public static ChronoUnit unitToDateFiled(String unit) {
        var field = ChronoUnit.HOURS;
        if (StringUtils.isBlank(unit)) {
            return field;
        }
        if (AttHolidayQuotaValidityEnum.YEAR.name().equals(unit)) {
            field = ChronoUnit.YEARS;
        } else if (AttHolidayQuotaValidityEnum.MONTH.name().equals(unit)) {
            field = ChronoUnit.MONTHS;
        } else if (AttHolidayQuotaValidityEnum.DAY.name().equals(unit)) {
            field = ChronoUnit.DAYS;
        }
        return field;
    }

    /**
     * 中文时间单位计算Long时间戳
     *
     * @param num  数量
     * @param unit 单位(AttConstants中的单位)
     *
     * @return 时间戳
     */
    public static Long unitAndNumToLong(float num, String unit, Integer dayOfHour) {
        if (dayOfHour == null || dayOfHour <= AttConstants.zeroInt) {
            dayOfHour = AttConstants.dayOfHour;
        }
        double result = num;
        if (StringUtils.isBlank(unit)) {
            return (long) result;
        }
        switch (unit) {
            case AttConstants.HOUR:
                result = num * (1000L * 60 * 60);
                break;
            case AttConstants.DAYS:
                result = num * (1000L * 60 * 60 * dayOfHour);
                break;
            case AttConstants.YEAR:
                result = num * (1000L * 60 * 60 * 24 * 365);
                break;
            case AttConstants.MONTH:
                result = num * (1000L * 60 * 60 * 24 * 30);
                break;
            case AttConstants.AM:
            case AttConstants.PM:
                result = num * (1000L * 60 * 60 * ((float) dayOfHour / 2));
                break;
            case AttConstants.MINUTE:
                result = num * (1000L * 60);
                break;
            case AttConstants.SECOND:
                result = num * 1000L;
                break;
            default:
                break;
        }
        return (long) result;
    }
}
