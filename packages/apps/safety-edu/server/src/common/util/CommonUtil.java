package com.guhecloud.cube.srv.gen.safety.education.server.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wql
 * @describe 时间查询参数工具
 * @date 2023/9/11 14:22
 */
public class CommonUtil {

    private CommonUtil() {

    }

    /**
     * @Author wql
     * @Description 计算占比
     * @Date 19:01 2023/9/13
     **/
    public static Double countProportion(BigDecimal total, BigDecimal member) {
        if (total.equals(BigDecimal.ZERO)) {
            return 0D;
        }
        return member.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
    }
}
