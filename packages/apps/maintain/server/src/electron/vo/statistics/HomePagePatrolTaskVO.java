package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/31 14:43
 * @Description:
 */

@Data
@Builder
public class HomePagePatrolTaskVO {

    /**
     * 巡更任务数据
     */
    private Long total;

    /**
     * 正常数
     */
    private Integer normal;

    /**
     * 异常数
     */
    private Integer abnormal;

    /**
     * 完成率
     */
    private Float completeRate;

    /**
     * 异常率
     */
    private Float abnormalRate;

    /**
     * 待接收
     */
    private Long waitReceive;

    /**
     * 执行中
     */
    private Long executing;

    /**
     * 已结束
     */
    private Long finished;
}
