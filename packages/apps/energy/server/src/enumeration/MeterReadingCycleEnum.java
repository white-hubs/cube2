package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadingCycleEnumVO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 抄表周期
 * @author: lw
 * @date: 2022/7/11
 */
@Getter
public enum MeterReadingCycleEnum {
    EVERY_SPECIFIED_MIN("every_specified_min", "每隔分钟数（0-59）"),
    EVERY_SPECIFIED_DAY("every_specified_day", "每隔指定天数"),
    SPECIFIED_DATE_MONTHLY("specified_date_monthly", "每月指定日期"),
    LAST_DAY_MONTHLY("last_day_monthly", "每月最后一天"),
    WEEKLY_DESIGNATED_DATE("weekly_designated_date", "每周指定日期"),
    ;

    private final String code;
    private final String name;

    MeterReadingCycleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getFireinspEnum(String code) {
        for (MeterReadingCycleEnum fireinspEnum : MeterReadingCycleEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) {
                return fireinspEnum.getName();
            }
        }
        return "";
    }

    public static List<String> getEnumTypeName() {
        ArrayList<String> enumTypeNameArr = new ArrayList<>();
        for (MeterReadingCycleEnum fireinspEnum : MeterReadingCycleEnum.values()) {
            String enumName = fireinspEnum.getName();
            enumTypeNameArr.add(enumName);
        }
        return enumTypeNameArr;
    }

    public static List<MeterReadingCycleEnumVO> getEnumTypeVo() {
        ArrayList<MeterReadingCycleEnumVO> enumTypeArr = new ArrayList<>();
        for (MeterReadingCycleEnum fireinspEnum : MeterReadingCycleEnum.values()) {
            enumTypeArr.add(new MeterReadingCycleEnumVO(fireinspEnum.getCode(), fireinspEnum.getName()));
        }
        return enumTypeArr;
    }
}
