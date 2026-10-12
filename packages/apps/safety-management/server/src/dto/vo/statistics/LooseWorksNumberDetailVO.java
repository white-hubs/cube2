package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author lijie
 * @describe 小散工程详情列表
 * @date 2024/3/14 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LooseWorksNumberDetailVO {
    /**
     * 工程状态
     */
    private String status;
    /**
     * 数量
     */
    private Integer number;
}
