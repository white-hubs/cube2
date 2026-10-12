package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全统计卡片
 * @date 2023/9/7 19:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SafetyStatisticsCardVO {
    /**
     * 进行中的小散工程数
     */
    private Integer projectNum;
    /**
     * 未处理隐患数
     */
    private Integer hiddenDangerNum;
    /**
     * 未完成的安全任务数
     */
    private Integer safetyTaskNum;

}
