package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnergyAlarmStatVO {
    private String name;
    private int notHandleNum;
    private int falseNum;
    private int trueNum;
    private int day7Num;
    private int totalNum;
    private int levelNum;
    private String handleRate;

    public String getHandleRate() {
        if (totalNum > 0) {
            return BigDecimal.valueOf(totalNum - notHandleNum).multiply(BigDecimal.valueOf(100))
                           .divide(BigDecimal.valueOf(totalNum), 2, BigDecimal.ROUND_HALF_UP).toString() + "%";
        }
        return null;
    }
}
