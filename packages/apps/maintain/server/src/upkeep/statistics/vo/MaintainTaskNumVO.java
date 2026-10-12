package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/30 14:51
 * @Description: 维保任务统计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintainTaskNumVO {

    /**
     * 维保人名称
     */
    private String keyName;

    /**
     * 维保人id
     */
    private Long keyId;

    /**
     * 已完成数据
     */
    private Integer finished = 0;

    /**
     * 未完成数据
     */
    private Integer unfinished = 0;
}
