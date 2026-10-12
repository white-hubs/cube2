package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/25 14:47
 * @Description:
 */
@Data
public class DisinfectionTaskStatusVO {
    /**
     * 总数
     */
    private Integer total = 0;

    /**
     * 待接收数
     */
    private Integer waitReceive = 0;
    /**
     * 待执行数
     */
    private Integer waitExecute = 0;
    /**
     * 执行中数
     */
    private Integer executing = 0;
    /**
     * 已结束数
     */
    private Integer finished = 0;
    /**
     * 已终止数
     */
    private Integer terminated = 0;
    /**
     * 已过期数
     */
    private Integer expired = 0;

}
