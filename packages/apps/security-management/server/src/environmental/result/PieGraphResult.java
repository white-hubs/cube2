package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result;

import lombok.Data;

/**
 * 空气质量饼图返回对象
 */
@Data
public class PieGraphResult {
    /**
     * 中文描述
     */
    private String describe;
    /**
     * 数量
     */
    private Integer number;
}
