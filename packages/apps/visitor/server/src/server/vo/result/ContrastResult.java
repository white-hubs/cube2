package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

@Data
public class ContrastResult {
    /**
     * 当前值
     */
    private String currentValue;
    /**
     * 对比值
     */
    private String compareValue;
    /**
     * 比率
     */
    private String ratioValue;
}
