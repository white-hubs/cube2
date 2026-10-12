package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 隐患数量统计
 * @date 2024/1/24 10:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerNumStatisticsVO {
    /**
     * 隐患总数
     */
    private Integer total;
    /**
     * 已完成隐患数
     */
    private Integer finished;
    /**
     * 处理中隐患数
     */
    private Integer inHand;
}
