package com.guhecloud.cube.deliver.project.merchant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDate(Date date) {
        return getDate(date, "yyyy-MM-dd");
    }

}
