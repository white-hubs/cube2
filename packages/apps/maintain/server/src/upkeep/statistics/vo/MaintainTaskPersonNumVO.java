package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/30 14:51
 * @Description: 维保任务人员统计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintainTaskPersonNumVO {

    /**
     * Y轴key
     */
    private String key;

    /**
     * 执行率
     */
    private Float ratio;

}
