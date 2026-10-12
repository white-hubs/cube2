package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result;

import lombok.Data;

/**
 * 环比统计返回对象
 */
@Data
public class QoqStatResult {
    /**
     * 日环比
     */
    private ContrastResult dayContrast;
    /**
     * 周环比
     */
    private ContrastResult weekContrast;
}
