package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 预案文件统计
 * @date 2023/10/8 18:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmergencyFileStatisticsVO {
    private Long totalNum;

    private List<EmergencyFileClassify> classifyStatistics;

}
