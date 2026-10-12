package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 安全教育培训统计结果
 * @date 2024/3/20 16:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafetyCultivateStatisticVO {
    /**
     * 教育培训数量
     */
    private Long recordNum;
}
