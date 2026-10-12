package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 小散工程数量统计
 * @date 2024/1/24 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectNumStatisticsVO {
    /**
     * 总数
     */
    private Long total;
    /**
     * 异常数
     */
    private Long abnormal;
}
