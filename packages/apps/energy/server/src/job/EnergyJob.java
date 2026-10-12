package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class EnergyJob {
    
    /**
     * @Description 格式化时间 方便influxdb 覆盖插入数据
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    public static LocalDateTime switchTime(DateEnum dateEnum, LocalDateTime offset) {
        LocalDateTime dateTime;
        switch (dateEnum) {
            case Hour:
                dateTime = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-MM-dd HH:00:00"));
                break;
            case Day:
                dateTime = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-MM-dd 00:00:00"));
                break;
            case Month:
                dateTime = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-MM-01 00:00:00"));
                break;
            case Year:
                dateTime = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-01-01 00:00:00"));
                break;
            default:
                dateTime = offset;

        }
        return dateTime;
    }

    /**
     * @Description 时间格式化
     * @Author lrj
     */
    public static String timeFormat(String timeFlag) {
        String timeFormat;
        switch (timeFlag) {
            case "TODAY":
                timeFormat = "yyyy-MM-dd";
                break;
            case "THIS_MONTH":
                timeFormat = "yyyy-MM";
                break;
            case "THIS_YEAR":
                timeFormat = "yyyy";
                break;
            default:
                timeFormat = "";

        }
        return timeFormat;
    }

    public static void timeMovingUnit(LocalDateTime dateTime, String timeFlag, int i) {
        switch (timeFlag) {
            case "TODAY":
                DateUtils.offsetDay(dateTime, i);
                break;
            case "THIS_MONTH":
                DateUtils.offsetMonth(dateTime, i);
                break;
            case "THIS_YEAR":
                DateUtils.offsetMonth(dateTime, i * 12);
                break;
            default:
                DateUtils.offsetDay(dateTime, i);

        }
    }

    public static boolean isNumeric(String str) {
        BigDecimal bigDecimal = new BigDecimal(str);
        Integer i = bigDecimal.intValue();
        Integer a = i.MAX_VALUE;
        if (i.equals(a) || i.equals(0)) {
            return true;
        } else {
            return false;
        }

    }

    public static String getProperties(String instrumentType) {
        String properties = "";
        if (instrumentType.equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
            properties = "dds";
        } else if (instrumentType.equals(InstrumentTypeEnum.WATER_METER.getCode())) {
            properties = "sds";
        } else if (instrumentType.equals(InstrumentTypeEnum.GAS_METER.getCode())) {
            properties = "qds";
        }
        return properties;
    }

}
