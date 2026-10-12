package com.guhecloud.cube.srv.gen.loose.works.util;

import com.guhecloud.platform.base.util.DateUtils;

/**
 * @author wql
 * @describe 业务日期编码生成
 * @date 2023/9/15 16:31
 */
@Deprecated
public class BizDateCodeUtil {

    @Deprecated
    public static String generateNum(String prefix, Long num) {
        String suffix = String.valueOf(num + 1);
        if (suffix.length() == 2) {
            suffix = "0" + suffix;
        } else if (suffix.length() == 1) {
            suffix = "00" + suffix;
        }
        String date = DateUtils.formatDate(DateUtils.now(), "yyyyMMdd");
        return prefix + date + suffix;
    }
}
