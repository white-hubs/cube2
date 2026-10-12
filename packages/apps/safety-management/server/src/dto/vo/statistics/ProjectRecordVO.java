package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 小散工程记录
 * @date 2023/9/7 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectRecordVO {
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 租户code
     */
    private Integer marketTenant;

    /**
     * 小散工程数量
     */
    private Integer num;
}
