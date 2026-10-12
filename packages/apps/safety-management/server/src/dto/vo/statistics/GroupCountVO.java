package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 单个字段 group 统计计数
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GroupCountVO {
    private String groupKey;

    private Integer count;
}
